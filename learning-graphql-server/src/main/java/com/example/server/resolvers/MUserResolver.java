package com.example.server.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.example.server.beans.Job;
import com.example.server.beans.MUser;
import org.springframework.stereotype.Component;

/**
 * MUser的Resolver，Resolver和Entity组合起来实现Schema所定义的功能，Entity定义纯粹的属性，Resolver实现属性的getter方法
 */
@Component
public class MUserResolver implements GraphQLResolver<MUser> {
    // @Resource
    // private MongoTemplate mongoTemplate;

    public Job getJob(MUser user, String name){
        Job result=new Job();
        result.setName("干饭");
        result.setSalary(100.0);
        return result;
    }
}
