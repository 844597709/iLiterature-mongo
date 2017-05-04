package com.swust.kelab.repos.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.swust.kelab.domain.enums.AttentionLevel;

public class AttentionLevelTypeHandler implements TypeHandler<AttentionLevel> {

    @Override
    public void setParameter(PreparedStatement ps, int i, AttentionLevel parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter != null) {
            ps.setInt(i, parameter.getCode());
        }
    }

    @Override
    public AttentionLevel getResult(ResultSet rs, String columnName) throws SQLException {
        return AttentionLevel.codeOf(rs.getInt(columnName));
    }

    @Override
    public AttentionLevel getResult(ResultSet rs, int columnIndex) throws SQLException {
        return AttentionLevel.codeOf(rs.getInt(columnIndex));
    }

    @Override
    public AttentionLevel getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return AttentionLevel.codeOf(cs.getInt(columnIndex));
    }

}
