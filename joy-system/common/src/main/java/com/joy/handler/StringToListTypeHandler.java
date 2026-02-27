package com.joy.handler;

import com.alibaba.fastjson2.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class StringToListTypeHandler extends BaseTypeHandler<List<String>> {

    // 支持的分隔符正则表达式（逗号、分号、冒号）
    private static final Pattern DELIMITER_PATTERN = Pattern.compile("[,;:]");
    // 数字识别正则
    private static final Pattern DIGIT_PATTERN = Pattern.compile("^\\d+$");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    List<String> parameter, JdbcType jdbcType) throws SQLException {
        // 将List转换为字符串，默认使用逗号分隔
        String value = String.join(",", parameter);
        ps.setString(i, value);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String value = rs.getString(columnName);
        return parseStringToList(value);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String value = rs.getString(columnIndex);
        return parseStringToList(value);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String value = cs.getString(columnIndex);
        return parseStringToList(value);
    }

    /**
     * 智能解析混合内容
     * @param value
     * @return
     */
    private List<String> parseStringToList(String value) {
        List<String> str = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        List<String> texts = new ArrayList<>();
        if (value != null && !value.trim().isEmpty()) {
            // 分割
            String[] parts = DELIMITER_PATTERN.split(value);
            // 分类提取
            for (String part : parts) {
                if (DIGIT_PATTERN.matcher(part).matches()) {
                    numbers.add(part);
                } else if (!part.trim().isEmpty()) {
                    texts.add(part.trim());
                }
            }
            str.add(String.join(",", numbers));
            str.add(String.join(",", texts));
            return str;
        }else {
            return Collections.emptyList();
        }
    }

}
