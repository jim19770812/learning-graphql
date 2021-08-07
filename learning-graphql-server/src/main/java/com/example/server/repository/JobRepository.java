package com.example.server.repository;

import com.example.server.beans.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("jobRepository")
public interface JobRepository extends MongoRepository<Job, String> {
}
