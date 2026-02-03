package com.joy.config.apiPrefix;

import org.springframework.core.convert.converter.Converter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatToDateConverter implements Converter<String, Date> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

    @Override
    public Date convert(String source) {
        try {
            return DATE_FORMAT.parse(source);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected: yyyy-MM-dd' 'HH:mm:ss");
        }
    }
}
