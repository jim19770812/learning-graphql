package com.example.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * http://localhost:8080/graphiql 可以打开调试页面
 */
@SpringBootApplication
public class LearningGraphqlServerApplication {

    public static void main(String[] args) {
        // System.getProperties().list(System.out);
        try{
            SpringApplication.run(LearningGraphqlServerApplication.class, args);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
	// @Bean
	// SchemaParserDictionary schemaParserDictionary() {
	//     return new SchemaParserDictionary()
    //             .add(CreatedUser.class)
    //             .add(ErrorContainer.class)
	// 			.add(LoginPayload.class)
	// 			.add(Dog.class)
	// 			.add(Fish.class);
    // }
}
