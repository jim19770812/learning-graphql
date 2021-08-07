package com.example.server.repository;

import com.example.server.beans.MUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 定义用户Repository
 */
@Repository("userRepository")
public interface MUserRepository extends MongoRepository<MUser, String> {
}
