package com.swust.kelab.repos.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.swust.kelab.domain.enums.Sentiment;

public class SentimentTypeHandler implements TypeHandler<Sentiment> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Sentiment parameter, JdbcType jdbcType) throws SQLException {
    }

    @Override
    public Sentiment getResult(ResultSet rs, String columnName) throws SQLException {
        float sentiment = rs.getFloat(columnName);
        return Sentiment.codeOf(classifySentiment(sentiment));
    }

    private int classifySentiment(float sentiment) {
        if (sentiment < -3) {
            return 1;
        } else if (sentiment <= -1) {
            return 2;
        } else if (sentiment < 0) {
            return 3;
        } else if (sentiment == 0) {
            return 4;
        } else if (sentiment < 1) {
            return 5;
        } else if (sentiment < 3) {
            return 6;
        } else {
            return 7;
        }
    }

    @Override
    public Sentiment getResult(ResultSet rs, int columnIndex) throws SQLException {
        float sentiment = rs.getFloat(columnIndex);
        return Sentiment.codeOf(classifySentiment(sentiment));
    }

    @Override
    public Sentiment getResult(CallableStatement cs, int columnIndex) throws SQLException {
        float sentiment = cs.getFloat(columnIndex);
        return Sentiment.codeOf(classifySentiment(sentiment));
    }

}
