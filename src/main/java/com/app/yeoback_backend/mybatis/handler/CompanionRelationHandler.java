package com.app.yeoback_backend.mybatis.handler;

import com.app.yeoback_backend.common.enumeration.CompanionRelation;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

// 동행 관계 상태 (DB: companion_relation)
@MappedTypes(CompanionRelation.class)
public class CompanionRelationHandler implements TypeHandler<CompanionRelation> {
    @Override
    public void setParameter(PreparedStatement ps, int i, CompanionRelation parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.OTHER);
            return;
        }
        ps.setObject(i, parameter.getValue(), Types.OTHER);
    }

    @Override
    public CompanionRelation getResult(ResultSet rs, int columnIndex) throws SQLException {
        return CompanionRelation.getCompanionRelation(rs.getString(columnIndex));
    }

    @Override
    public CompanionRelation getResult(ResultSet rs, String columnName) throws SQLException {
        return CompanionRelation.getCompanionRelation(rs.getString(columnName));
    }

    @Override
    public CompanionRelation getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return CompanionRelation.getCompanionRelation(cs.getString(columnIndex));
    }
}
