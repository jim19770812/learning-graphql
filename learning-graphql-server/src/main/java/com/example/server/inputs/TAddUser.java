package com.example.server.inputs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@EqualsAndHashCode
public class TAddUser {
    private String name;
    private String gender;
    private Integer age;    
}
