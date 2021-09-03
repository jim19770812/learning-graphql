package com.example.server.inputs;

import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter @Setter
public class TUpdateUser {
    private String id;
    private String name;
    private String gender;
    private Integer age;
    private Date joinDate;
    private List<String> titles;
    
}
