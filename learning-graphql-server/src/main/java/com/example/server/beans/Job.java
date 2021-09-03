package com.example.server.beans;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(value = "job")
public class Job {
    @Id
    private String id;
    @Field("job_name")
    @Accessors(chain = true)
    private String name;
    @Field("job_salary")
    @Accessors(chain = true)
    private Double salary;
}
