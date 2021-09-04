package com.example.server.scalars;

import com.inrevo.common.utils.DT;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateScalarType extends GraphQLScalarType {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public DateScalarType() {
        super("Date", "日期标量", new Coercing<Date, String>() {
            @Override
            @SneakyThrows
            public String serialize(Object o) {
                Date date = (Date) o;
                return DT.ofDate(date).format(DATE_FORMAT);
            }
 
            @Override
            @SneakyThrows
            public Date parseValue(Object o) {
                String value = String.valueOf(o);
                if ("null".equalsIgnoreCase(value) || "".equalsIgnoreCase(value)) {
                    return null;
                }
                Date result=DT.of_yyyy_MM_dd(value).orElseThrow(()->new RuntimeException(value+" 日期格式转换失败")).date();
                return result;
            }
 
            @Override
            @SneakyThrows
            public Date parseLiteral(Object o) {
                String value = String.valueOf(o);
                if ("null".equalsIgnoreCase(value) || "".equalsIgnoreCase(value)) {
                    return null;
                }
                Date result=DT.of_yyyy_MM_dd(value).orElseThrow(()->new RuntimeException(value+"日期格式转换失败")).date();
                return result;
            }
        });
    }
 
}
