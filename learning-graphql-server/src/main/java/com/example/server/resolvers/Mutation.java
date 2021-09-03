package com.example.server.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.example.server.beans.MUser;
import com.example.server.inputs.TAddUser;
import com.example.server.inputs.TUpdateUser;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Component;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Mutation implements GraphQLMutationResolver {

    /**
     * 添加用户
     * @param user
     * @return
     */
    public MUser addUser(TAddUser user){
        MUser result=new MUser();
        result.setName("吕布");
        result.setAge(20);
        result.setGender("男");
        result.setId("1");
        result.setJoinDate(new Date());
        result.setTitles(Lists.newArrayList("武将"));
        log.debug("addUser done");
        return result;
    }

    /**
     * 更新用户
     * @param user
     */
    public MUser updateUser(TUpdateUser user){
        MUser result=new MUser();
        result.setName("吕布");
        result.setAge(20);
        result.setGender("男");
        result.setId("1");
        result.setJoinDate(new Date());
        result.setTitles(Lists.newArrayList("武将"));
        log.debug("updateUser done");
        return result;
    }
}
