package com.example.server.resolvers;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.server.beans.Job;
import com.example.server.beans.MUser;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Component;

@Component
public class QueryResolver implements GraphQLQueryResolver {
    @Resource
    private MUserResolver userResolver;

    public MUser user(String usrName){
        MUser result=new MUser();
        result.setName("jim");
        result.setAge(30);
        result.setId("1");
        result.setGender("male");
        result.setJoinDate(new Date());
        result.setTitles(Lists.newArrayList("武将"));
        return result;
    }

    public MUser firstUser(){
        MUser result=new MUser();
        result.setName("jim");
        result.setAge(30);
        result.setId("1");
        result.setGender("male");
        result.setJoinDate(new Date());
        result.setTitles(Lists.newArrayList("武将"));
        return result;
    }

    public List<Job> jobs(){
        Job job=new Job();
        job.setName("武将");
        job.setSalary(20000.0);
        return Lists.newArrayList(job);
    }
}
