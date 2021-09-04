package com.example.server.scalars;

import java.util.Date;

import com.inrevo.common.utils.DT;
import org.springframework.stereotype.Component;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import lombok.SneakyThrows;

@Component
public class DateTimeScalarType extends GraphQLScalarType {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
 
    public DateTimeScalarType() {
        super("DateTime", "日期时间标量", new Coercing<Date, String>() {
            @Override
            @SneakyThrows
            public String serialize(Object o) {
                Date date = (Date) o;
                return DT.ofDate(date).format(DATE_TIME_FORMAT);
            }
 
            @Override
            @SneakyThrows
            public Date parseValue(Object o) {
                String value = String.valueOf(o);
                if ("null".equalsIgnoreCase(value) || "".equalsIgnoreCase(value)) {
                    return null;
                }
                Date result=DT.of_yyyy_MM_dd_HH_mm_ss(value).orElseThrow(()->new RuntimeException(value+" 日期格式转换失败")).date();
                return result;
            }
 
            @Override
            @SneakyThrows
            public Date parseLiteral(Object o) {
                String value = String.valueOf(o);
                if ("null".equalsIgnoreCase(value) || "".equalsIgnoreCase(value)) {
                    return null;
                }
                Date result= DT.of_yyyy_MM_dd_HH_mm_ss(value).orElseThrow(()->new RuntimeException(value+"日期格式转换失败")).date();
                return result;
            }
        });
    }
 
}
