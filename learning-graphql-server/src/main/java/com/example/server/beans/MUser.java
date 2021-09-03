package com.example.server.beans;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 用户Bean，需要定义好Mongo @Document和@Field
 */
@Getter @Setter
@ToString
@EqualsAndHashCode
@Document(value = "m_user")
public class MUser {
    @Id
    @Accessors(chain = true)
    private String id;
    @Indexed
    @Field("user_name")
    @Accessors(chain = true)
    private String name;
    @Field("gender")
    @Accessors(chain = true)
    private String gender;
    @Field("age")
    @Accessors(chain = true)
    private Integer age;
    @Field("join_date")
    @Accessors(chain = true)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date joinDate;
    @Field("titles")
    @Accessors(chain = true)
    private List<String> titles;
}
