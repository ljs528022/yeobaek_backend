--------------------------------------------------
-- 1. ENUM 타입 정의
--------------------------------------------------
create type status as enum ('ACTIVE', 'SUSPENDED', 'WITHDRAWN'); -- 회원 상태 (활성, 정지, 탈퇴)
create type member_role as enum ('admin', 'member'); -- 회원 권한 (관리자, 일반 회원)
create type oauth_provider as enum ('kakao', 'naver', 'google');
create type rel_status as enum ('pending', 'done'); -- 친구 관계 상태 (대기 중, 완료)
create type visibility as enum ('private', 'friends', 'public'); -- 공개 범위 (비공개, 친구만, 공개)
create type companion_relation as enum ('pending', 'done', 'reject'); -- 동행 관계 상태 (대기 중, 완료, 거절)
create type together_purpose as enum ('meal', 'spot'); -- 모임 목적 (식사, 장소/관광)
create type together_area as enum ('near', 'other'); -- 모임 지역 구분 (근처, 기타)
create type together_status as enum ('open', 'close', 'ended'); -- 모임 상태 (모집 중, 모집 마감, 종료)
create type together_role as enum ('host', 'member'); -- 모임 내 역할 (방장, 일반 참여자)
create type report_status as enum ('pending', 'done', 'reject'); -- 신고 처리 상태 (대기 중, 처리 완료, 반려)
create type destination_type as enum ('city', 'country'); -- 목적지 타입 (도시, 국가)
create type trip_category as enum ('domestic', 'overseas'); -- 여행 카테고리 (국내, 해외)
create type trip_status as enum ('write', 'save', 'finish'); -- 여행 상태 (작성 중, 임시 저장, 완료)



--------------------------------------------------
-- 2. 독립 참조 테이블 (부모 테이블이 없는 기본 테이블)
--------------------------------------------------

-- 회원 테이블
create table tbl_member (
                            id bigint generated always as identity primary key,
                            member_email varchar(255) unique,
                            member_phone varchar(15) unique,
                            member_password varchar(255),
                            member_name varchar(10),
                            member_alias varchar(20),
                            member_bio varchar(255),
                            member_status status default 'ACTIVE',      -- (활동: ACTIVE, 휴면: SUSPENDED, 탈퇴: WITHDRAWN)
                            member_login_verified boolean default true,
                            created_datetime timestamp default now(),
                            updated_datetime timestamp default now()
);

-- 장소
create table tbl_place (
                           id bigint generated always as identity primary key,
                           google_place_id varchar(255) unique,
                           kakao_place_id varchar(255) unique,
                           place_name varchar(255) not null,
                           place_address varchar(255) not null,
                           place_lat decimal not null,
                           place_lng decimal not null,
                           created_datetime timestamp default now(),
                           updated_datetime timestamp default now()
);

-- 이미지 파일
create table tbl_file (
                          id bigint generated always as identity primary key,
                          original_name varchar(255) not null,
                          file_name varchar(255) not null,
                          file_path varchar(255) not null,
                          file_size bigint,
                          created_datetime timestamp default now()
);

-- 여행 목적지 (자기참조 구조)
create table tbl_destination (
                                 id bigint generated always as identity primary key,
                                 destination_parent_id bigint,
                                 destination_name varchar(255) not null,
                                 destination_type destination_type not null,
                                 created_datetime timestamp default now(),
                                 constraint fk_destination_parent foreign key (destination_parent_id)
                                     references tbl_destination(id)
);

-- 여행 취향 (화면: 프로필 > 여행 취향 · 기초 데이터는 이 파일 맨 아래)
create table tbl_preference (
                                id bigint generated always as identity primary key,
                                preference_name varchar(30) not null unique
);

-- 선호 활동 (화면: 프로필 > 여행 키워드 · 기초 데이터는 이 파일 맨 아래)
create table tbl_vibe (
                          id bigint generated always as identity primary key,
                          vibe_name varchar(30) not null unique
);


--------------------------------------------------
-- 3. 회원 연관 테이블 (tbl_member 참조)
--------------------------------------------------

-- Oauth (소셜 로그인 정보)
create table tbl_oauth (
                           id bigint generated always as identity primary key,
                           provider_id varchar(255) unique not null,            -- 공급자 측 고유 사용자 식별자
                           provider oauth_provider not null,                    -- oauth 공급자 (enum: kakao/google/naver)
                           profile_url varchar(255),
                           member_id bigint not null,                           -- fk → tbl_member.id | 연결된 회원
                           created_datetime timestamp not null default now(),
                           updated_datetime timestamp default now(),
                           constraint fk_oauth_member foreign key (member_id)
                               references tbl_member(id)
);

-- 회원 권한 및 정보
create table tbl_auth (
                          id bigint not null primary key,
                          member_role member_role default 'member',     -- (ADMIN, MEMBER)
                          created_datetime timestamp default now(),
                          updated_datetime timestamp default now(),
                          constraint fk_auth_member foreign key (id)
                              references tbl_member(id)
);

-- 회원 개인 설정
create table tbl_member_setting (
                                    id bigint not null primary key,
                                    thema_color varchar(30) default 'sky',      -- 팔레트 id(sky·aqua·blossom·lavender·mint) 또는 직접 고른 #rrggbb
                                    name_visibility visibility default 'private',   -- 실명 공개 범위 (회원 프로필에서 실명을 보여 줄 대상)
                                    phone_visibility visibility default 'private',  -- 연락처 공개 범위
                                    note_auto_save boolean default true,
                                    created_datetime timestamp default now(),
                                    updated_datetime timestamp default now(),
                                    constraint fk_setting_member foreign key (id)
                                        references tbl_member(id) on delete cascade
);

-- 회원이 고른 여행 취향 (여러 개)
create table tbl_member_preference (
                                       member_id bigint not null,
                                       preference_id bigint not null,
                                       created_datetime timestamp default now(),
                                       primary key (member_id, preference_id),
                                       constraint fk_member_preference_member foreign key (member_id)
                                           references tbl_member(id) on delete cascade,
                                       constraint fk_member_preference_preference foreign key (preference_id)
                                           references tbl_preference(id)
);

-- 회원이 고른 여행 키워드 (여러 개)
create table tbl_member_vibe (
                                 member_id bigint not null,
                                 vibe_id bigint not null,
                                 created_datetime timestamp default now(),
                                 primary key (member_id, vibe_id),
                                 constraint fk_member_vibe_member foreign key (member_id)
                                     references tbl_member(id) on delete cascade,
                                 constraint fk_member_vibe_vibe foreign key (vibe_id)
                                     references tbl_vibe(id)
);

-- 친구 신청 목록
create table tbl_member_rel (
                                id bigint generated always as identity primary key,
                                rel_requester_id bigint not null,                  -- 친구 신청한 회원
                                rel_receiver_id bigint not null,                   -- 친구 신청 받은 회원
                                rel_status rel_status not null default 'pending',  -- pending: 신청 중, done: 친구
                                created_datetime timestamp default now(),
                                updated_datetime timestamp default now(),          -- 수락한 시각
                                constraint fk_rel_requester foreign key (rel_requester_id)
                                    references tbl_member(id) on delete cascade,
                                constraint fk_rel_receiver foreign key (rel_receiver_id)
                                    references tbl_member(id) on delete cascade,
                                constraint ck_rel_not_self check (rel_requester_id <> rel_receiver_id)
);
-- 두 회원 사이의 관계는 방향과 상관없이 하나만 (A→B 가 있으면 B→A 도 만들 수 없음, 동시 신청 대비)
create unique index uq_member_rel_pair
    on tbl_member_rel (least(rel_requester_id, rel_receiver_id), greatest(rel_requester_id, rel_receiver_id));


--------------------------------------------------
-- 4. 모임 및 커뮤니티 테이블
--------------------------------------------------

-- 모임 게시글 (tbl_place 참조)
create table tbl_get_together (
                                  id bigint generated always as identity primary key,
                                  place_id bigint not null,
                                  together_title varchar(255) not null,
                                  together_content text not null,
                                  together_purpose together_purpose default 'spot',     -- (장소/관광: SPOT, 식사: MEAL)
                                  together_area together_area default 'near',           -- (근처: NEAR, 기타: OTHER)
                                  together_max_member int not null,
                                  together_start_date timestamp not null,
                                  together_end_date timestamp not null,
                                  together_status together_status default 'open',
                                  created_datetime timestamp default now(),
                                  updated_datetime timestamp default now(),
                                  constraint fk_together_place foreign key (place_id)
                                      references tbl_place(id)
);

-- 모임 참여자 목록 (tbl_get_together, tbl_member 참조)
create table tbl_together_member (
                                     id bigint generated always as identity primary key,
                                     together_id bigint not null,
                                     member_id bigint not null,
                                     together_role together_role not null,
                                     together_guest_member int default 0,
                                     created_datetime timestamp default now(),
                                     constraint fk_together_id foreign key (together_id)
                                         references tbl_get_together(id),
                                     constraint fk_together_member foreign key (member_id)
                                         references tbl_member(id)
);

-- 채팅방 (tbl_get_together 참조)
create table tbl_chat (
                          id bigint generated always as identity primary key,
                          together_id bigint not null,
                          created_datetime timestamp default now(),
                          updated_datetime timestamp default now(),
                          constraint fk_chat_together foreign key (together_id)
                              references tbl_get_together(id)
);

-- 채팅방 인원 목록 (tbl_chat, tbl_member 참조)
create table tbl_chat_member (
                                 id bigint generated always as identity primary key,
                                 chat_id bigint not null,
                                 member_id bigint not null,
                                 joined_at timestamp default now(),
                                 last_read_message_id bigint null,
                                 status varchar(20) default 'JOINED',  -- (참여: JOINED, 나감: LEFT)
                                 created_datetime timestamp default now(),
                                 constraint fk_chat_member_chat foreign key (chat_id)
                                     references tbl_chat(id),
                                 constraint fk_chat_member_member foreign key (member_id)
                                     references tbl_member(id)
);

-- 메시지 (tbl_chat, tbl_member 참조)
create table tbl_chat_message (
                                  id bigint generated always as identity primary key,
                                  chat_id bigint not null,
                                  sender_id bigint not null,
                                  message text not null,
                                  message_type varchar(20) default 'TEXT',   -- 메시지 타입 (TEXT, IMAGE, NOTICE)
                                  created_datetime timestamp default now(),
                                  constraint fk_chat_message_chat foreign key (chat_id)
                                      references tbl_chat(id),
                                  constraint fk_chat_message_sender foreign key (sender_id)
                                      references tbl_member(id)
);


--------------------------------------------------
-- 5. 여행 및 일정 관련 테이블
--------------------------------------------------

-- 여행 (tbl_member, tbl_destination 참조)
create table tbl_trip (
                          id bigint generated always as identity primary key,
                          member_id bigint not null,
                          destination_id bigint not null,
                          departure_id bigint not null,
                          trip_category trip_category not null,
                          trip_title varchar(255) not null,
                          trip_start_date timestamp not null,
                          trip_end_date timestamp not null,
                          trip_status trip_status default 'write',
                          created_datetime timestamp default now(),
                          updated_datetime timestamp default now(),
                          constraint fk_trip_member foreign key (member_id)
                              references tbl_member(id),
                          constraint fk_trip_destination foreign key (destination_id)
                              references tbl_destination(id),
                          constraint fk_trip_departure foreign key (departure_id)
                              references tbl_destination(id)
);

-- 여행 날짜 (tbl_trip 참조)
create table tbl_trip_day (
                              id bigint generated always as identity primary key,
                              trip_id bigint not null,
                              trip_day timestamp not null,
                              trip_day_title varchar(255),
                              created_datetime timestamp default now(),
                              updated_datetime timestamp default now(),
                              constraint fk_day_trip foreign key (trip_id)
                                  references tbl_trip(id)
);

-- 여행 세부일정 (tbl_trip_day, tbl_member, tbl_place 참조)
create table tbl_schedule (
                              id bigint generated always as identity primary key,
                              trip_day_id bigint not null,
                              member_id bigint not null,
                              place_id bigint,
                              schedule_title varchar(255) not null,
                              schedule_start_time timestamp not null,
                              schedule_memo varchar(255),
                              schedule_ai_source boolean default false,
                              created_datetime timestamp default now(),
                              updated_datetime timestamp default now(),
                              constraint fk_schedule_trip_day foreign key (trip_day_id)
                                  references tbl_trip_day(id),
                              constraint fk_schedule_member foreign key (member_id)
                                  references tbl_member(id),
                              constraint fk_schedule_place foreign key (place_id)
                                  references tbl_place(id)
);

-- 수첩 하루 기록 (tbl_trip_day, tbl_member 참조)
create table tbl_trip_note (
                               id bigint generated always as identity primary key,
                               trip_day_id bigint not null,
                               member_id bigint not null,
                               note_title varchar(255) not null,
                               note_sentence varchar(255),
                               created_datetime timestamp default now(),
                               updated_datetime timestamp default now(),
                               constraint fk_note_trip_day foreign key (trip_day_id)
                                   references tbl_trip_day(id),
                               constraint fk_note_member foreign key (member_id)
                                   references tbl_member(id)
);

-- 동행 인원 정보 (tbl_trip, tbl_member 참조)
create table tbl_trip_companion (
                                    id bigint generated always as identity primary key,
                                    trip_id bigint not null,
                                    member_id bigint,
                                    companion_is_member boolean default false,
                                    companion_name varchar(20),
                                    companion_contact varchar(20),
                                    companion_relation companion_relation default 'reject',
                                    created_datetime timestamp default now(),
                                    constraint fk_companion_trip foreign key (trip_id)
                                        references tbl_trip(id),
                                    constraint fk_companion_member foreign key (member_id)
                                        references tbl_member(id)
);


--------------------------------------------------
-- 6. 시스템 관리 및 기타 테이블
--------------------------------------------------

-- 신고 (tbl_member 참조)
create table tbl_report (
                            id bigint generated always as identity primary key,
                            reporter_id bigint not null,
                            target_id bigint not null,
                            report_reason varchar(255) not null,
                            report_content text,
                            report_status report_status default 'pending',
                            created_datetime timestamp default now(),
                            updated_datetime timestamp default now(),
                            constraint fk_report_reporter foreign key (reporter_id)
                                references tbl_member(id),
                            constraint fk_report_target foreign key (target_id)
                                references tbl_member(id)
);


--------------------------------------------------
-- 7. 이미지 조인 테이블 (tbl_file 참조)
--------------------------------------------------

-- 유저 프로필 이미지 (tbl_file, tbl_member 참조)
create table tbl_file_member (
                                 id bigint not null primary key,
                                 member_id bigint not null,
                                 constraint fk_file_member_file foreign key (id)
                                     references tbl_file(id),
                                 constraint fk_file_member foreign key (member_id)
                                     references tbl_member(id)
);

-- 여행 이미지 (tbl_file, tbl_trip 참조)
create table tbl_file_trip (
                               id bigint not null primary key,
                               trip_id bigint not null,
                               constraint fk_file_trip_file foreign key (id)
                                   references tbl_file(id),
                               constraint fk_file_trip foreign key (trip_id)
                                   references tbl_trip(id)
);

-- 모임 사진 (tbl_file, tbl_get_together 참조)
create table tbl_file_together (
                                   id bigint not null primary key,
                                   together_id bigint not null,
                                   constraint fk_file_together_file foreign key (id)
                                       references tbl_file(id),
                                   constraint fk_file_together foreign key (together_id)
                                       references tbl_get_together(id)
);

-- 수첩 사진 (tbl_file, tbl_trip_note 참조)
create table tbl_file_note (
                               id bigint not null primary key,
                               note_id bigint not null,
                               constraint fk_file_note_file foreign key (id)
                                   references tbl_file(id),
                               constraint fk_file_note foreign key (note_id)
                                   references tbl_trip_note(id)
);

--------------------------------------------------
-- 8. 기초 데이터 (프론트 worldStore 의 TASTE_OPTIONS · KEYWORD_OPTIONS 와 같은 순서)
--------------------------------------------------

-- 여행 취향
insert into tbl_preference (preference_name) values
                                                 ('힐링'), ('풍경'), ('식도락'), ('디저트'), ('물멍'), ('로컬'), ('카페'), ('쇼핑'), ('휴양'), ('캠핑'), ('축제'), ('가성비'),
                                                 ('어드벤처'), ('팝업'), ('공연'), ('호캉스'), ('자연'), ('사진'), ('기념품'), ('숙소'), ('가족'), ('커플'), ('친구'), ('나홀로');

-- 여행 키워드
insert into tbl_vibe (vibe_name) values
                                     ('평온한'), ('계획적인'), ('평화로운'), ('활발한'), ('여유로운'), ('재밌는'), ('무계획의'), ('회복되는'), ('조용한'),
                                     ('나즈막한'), ('체계적인'), ('가벼운'), ('로컬적인'), ('활기찬'), ('체험하는');

-- 여행 목적지 기본 데이터 (id 순서가 프론트 src/utils/destination.ts의 임시 매핑과 같아야 해서, 순서를 고정해서 넣는다)
-- 나라: 1 대한민국, 2 일본, 3 베트남
insert into tbl_destination (destination_name, destination_type) values
                                                                     ('대한민국','country'),('일본','country'),('베트남','country');
-- 도시: 4 서울, 5 부산, 6 제주, 7 도쿄, 8 오사카, 9 다낭 (상위 나라는 이름으로 찾아서 parent_id에 넣는다)
insert into tbl_destination (destination_parent_id, destination_name, destination_type)
select p.id, c.city_name, 'city'::destination_type
from (values ('대한민국', '서울', 1), ('대한민국', '부산', 2), ('대한민국', '제주', 3),
             ('일본', '도쿄', 4), ('일본', '오사카', 5),
             ('베트남', '다낭', 6)) as c(country_name, city_name, ord)
         join tbl_destination p on p.destination_name = c.country_name and p.destination_type = 'country'
order by c.ord;
-- 나라(출발지용 포함): 10 대만, 11 태국, 12 프랑스, 13 미국, 14 중국, 15 기타(출발지 선택용 임시 항목)
insert into tbl_destination (destination_name, destination_type) values
                                                                     ('대만','country'),('태국','country'),('프랑스','country'),
                                                                     ('미국','country'),('중국','country'),('기타','country');
-- 도시: 16 강릉, 17 경주, 18 여수, 19 후쿠오카, 20 타이베이, 21 방콕, 22 파리
insert into tbl_destination (destination_parent_id, destination_name, destination_type)
select p.id, c.city_name, 'city'::destination_type
from (values ('대한민국', '강릉', 1), ('대한민국', '경주', 2), ('대한민국', '여수', 3),
             ('일본', '후쿠오카', 4), ('대만', '타이베이', 5), ('태국', '방콕', 6), ('프랑스', '파리', 7)) as c(country_name, city_name, ord)
         join tbl_destination p on p.destination_name = c.country_name and p.destination_type = 'country'
order by c.ord;
