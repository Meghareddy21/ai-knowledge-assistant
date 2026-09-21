// This line defines the "package" where this file belongs.
// A package is like a folder that groups related files together.
// Here, the package is called "com.spring.ai.LLMs.rag.simple_retrieval_augmented_generation".
package com.spring.ai.LLMs.rag.simple_retrieval_augmented_generation;

// Importing Spring Boot classes.
import org.springframework.boot.SpringApplication;          // Used to launch the Spring Boot application.
import org.springframework.boot.autoconfigure.SpringBootApplication; // Marks this as a Spring Boot application.

// @SpringBootApplication is a special annotation that combines three things:
// 1. @Configuration → Marks this class as a source of bean definitions.
// 2. @EnableAutoConfiguration → Tells Spring Boot to automatically configure beans based on dependencies.
// 3. @ComponentScan → Tells Spring to scan the package for components (@Controller, @Service, etc.).
@SpringBootApplication
public class SimpleRetrievalAugmentedGenerationApplication {

    // This is the main method.
    // It is the entry point of the Java application.
    // When you run the program, this method starts first.
    public static void main(String[] args) {
        // SpringApplication.run(...) starts the Spring Boot application.
        // It sets up the Spring context, loads beans, and starts the embedded server (like Tomcat).
        // After this line runs, your REST APIs (like /api/rag/load-pdf and /api/rag/chat) become available.
        SpringApplication.run(SimpleRetrievalAugmentedGenerationApplication.class, args);
    }
}