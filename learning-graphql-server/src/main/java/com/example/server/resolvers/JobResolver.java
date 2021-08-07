package com.example.server.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.example.server.beans.Job;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobResolver implements GraphQLResolver<Job> {
    private MongoTemplate mongoTemplate;
    public JobResolver() {
    }

    public Job job(String name){
        return null;
    }
}
