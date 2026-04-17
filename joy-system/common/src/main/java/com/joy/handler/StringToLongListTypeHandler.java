package com.joy.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class StringToLongListTypeHandler extends BaseTypeHandler<List<Long>> {

    // 支持的分隔符正则表达式（逗号、分号、冒号、空格等）
    private static final Pattern DELIMITER_PATTERN = Pattern.compile("\\s*[;,:\\|]\\s*");
    // 数字识别正则
    private static final Pattern DIGIT_PATTERN = Pattern.compile("^\\d+$");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    List<Long> parameter, JdbcType jdbcType) throws SQLException {
        String value = parameter != null && !parameter.isEmpty() ?
                String.join(",", parameter.stream().map(String::valueOf).toArray(String[]::new)) :
                "";
        ps.setString(i, value);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String value = rs.getString(columnName);
        return parseStringToLongList(value);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String value = rs.getString(columnIndex);
        return parseStringToLongList(value);
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String value = cs.getString(columnIndex);
        return parseStringToLongList(value);
    }

    /**
     * 解析字符串为Long列表
     * @param value 原始字符串
     * @return 解析后的Long列表
     */
    private List<Long> parseStringToLongList(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 使用分隔符分割字符串
        String[] parts = DELIMITER_PATTERN.split(value);

        List<Long> result = new ArrayList<>();
        for (String part : parts) {
            part = part.trim();
            // 过滤空字符串和"null"字符串
            if (!part.isEmpty() && !"null".equalsIgnoreCase(part) && DIGIT_PATTERN.matcher(part).matches()) {
                try {
                    result.add(Long.parseLong(part));
                } catch (NumberFormatException e) {
                    // 忽略无法解析的数字
                    System.err.println("无法解析为Long: " + part);
                }
            }
        }

        return result;
    }
}