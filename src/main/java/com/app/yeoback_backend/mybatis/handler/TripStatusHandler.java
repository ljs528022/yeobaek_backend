package com.app.yeoback_backend.mybatis.handler;

import com.app.yeoback_backend.common.enumeration.TripStatus;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

// 여행 상태 (DB: trip_status)
@MappedTypes(TripStatus.class)
public class TripStatusHandler implements TypeHandler<TripStatus> {
    @Override
    public void setParameter(PreparedStatement ps, int i, TripStatus parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.OTHER);
            return;
        }
        ps.setObject(i, parameter.getValue(), Types.OTHER);
    }

    @Override
    public TripStatus getResult(ResultSet rs, int columnIndex) throws SQLException {
        return TripStatus.getTripStatus(rs.getString(columnIndex));
    }

    @Override
    public TripStatus getResult(ResultSet rs, String columnName) throws SQLException {
        return TripStatus.getTripStatus(rs.getString(columnName));
    }

    @Override
    public TripStatus getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return TripStatus.getTripStatus(cs.getString(columnIndex));
    }
}
