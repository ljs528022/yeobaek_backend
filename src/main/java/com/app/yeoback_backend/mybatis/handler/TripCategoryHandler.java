package com.app.yeoback_backend.mybatis.handler;

import com.app.yeoback_backend.common.enumeration.TripCategory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

// 여행 카테고리 (DB: trip_category)
@MappedTypes(TripCategory.class)
public class TripCategoryHandler implements TypeHandler<TripCategory> {
    @Override
    public void setParameter(PreparedStatement ps, int i, TripCategory parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.OTHER);
            return;
        }
        ps.setObject(i, parameter.getValue(), Types.OTHER);
    }

    @Override
    public TripCategory getResult(ResultSet rs, int columnIndex) throws SQLException {
        return TripCategory.getTripCategory(rs.getString(columnIndex));
    }

    @Override
    public TripCategory getResult(ResultSet rs, String columnName) throws SQLException {
        return TripCategory.getTripCategory(rs.getString(columnName));
    }

    @Override
    public TripCategory getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return TripCategory.getTripCategory(cs.getString(columnIndex));
    }
}
