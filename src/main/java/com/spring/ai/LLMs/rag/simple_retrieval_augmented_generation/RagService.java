// This line defines the "package" where this file belongs.
// A package is like a folder that groups related files together.
// Here, the package is called "com.spring.ai.LLMs.rag.simple_retrieval_augmented_generation".
package com.spring.ai.LLMs.rag.simple_retrieval_augmented_generation;

// Importing Spring AI classes.
// ChatClient → Used to interact with AI models (like Gemini, GPT).
// QuestionAnswerAdvisor → Advisor that helps the AI answer questions using a VectorStore (retrieval).
// SearchRequest → Defines how many documents to retrieve and similarity thresholds.
// VectorStore → Stores embeddings (numerical vectors) for documents.
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

// Importing Spring annotations.
import org.springframework.stereotype.Service; // Marks this class as a Spring Service.

// @Service tells Spring:
// "This class contains business logic and should be managed as a Service bean."
@Service
public class RagService {

    // A reference to the ChatClient.
    // ChatClient is used to send prompts/questions to the AI model and get responses.
    private final ChatClient chatClient;

    // Constructor injection:
    // Spring will automatically provide a ChatClient.Builder and a VectorStore when creating RagService.
    public RagService(ChatClient.Builder builder , VectorStore vectorStore) {

        // Step 1: Define a SearchRequest.
        // - topK(10) → Retrieve the top 10 most similar documents.
        // - similarityThreshold(0.5) → Only consider documents with similarity >= 0.5.
        SearchRequest searchRequest = SearchRequest.builder()
                .topK(10)
                .similarityThreshold(0.5)
                .build();

        // Step 2: Build the ChatClient with a QuestionAnswerAdvisor.
        // QuestionAnswerAdvisor connects the ChatClient with the VectorStore.
        // It ensures that when a user asks a question, the AI retrieves relevant documents
        // from the VectorStore and uses them as context for answering.
        this.chatClient = builder
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(searchRequest)
                        .build())
                .build();
    }

    // Method to ask a question and get an AI-generated answer.
    public String askQuestion(String question) {
        // Steps:
        // 1. chatClient.prompt() → Start building a prompt.
        // 2. .user(question) → Add the user’s question to the prompt.
        // 3. .call() → Send the prompt to the AI model.
        // 4. .content() → Extract the AI’s answer as plain text.
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }
}