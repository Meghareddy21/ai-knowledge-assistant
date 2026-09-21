// This line defines the "package" where this file belongs.
// A package is like a folder that groups related files together.
// Here, the package is called "com.spring.ai.LLMs.rag.simple_retrieval_augmented_generation".
package com.spring.ai.LLMs.rag.simple_retrieval_augmented_generation;

// Importing Spring framework classes.
// FileSystemResource → Represents a file stored on the local file system.
// RestController → Marks this class as a REST API controller.
// RequestMapping → Maps a base URL to this controller.
// PostMapping → Maps HTTP POST requests to specific methods.
// RequestParam → Extracts query parameters from the request URL.
// RequestBody → Extracts the request body (JSON or text).
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path; // Used to represent file paths in the system.

// @RestController tells Spring:
// "This class will handle REST API requests and return responses directly (usually JSON or text)."
@RestController

// @RequestMapping("/api/rag") means:
// All URLs handled by this controller will start with "/api/rag".
// Example: http://localhost:8080/api/rag/load-pdf
@RequestMapping("/api/rag")
public class RagController {

    // References to the services used by this controller.
    // DocumentLoaderService → Handles loading and splitting PDF documents into chunks, then storing them in the vector store.
    // RagService → Handles asking questions and retrieving answers using RAG (Retrieval-Augmented Generation).
    private final DocumentLoaderService documentLoaderService;
    private final RagService ragService;

    // Constructor injection:
    // Spring will automatically provide DocumentLoaderService and RagService when creating RagController.
    public RagController(DocumentLoaderService documentLoaderService , RagService ragService) {
        this.documentLoaderService = documentLoaderService;
        this.ragService = ragService;
    }

    // Endpoint to load and index a PDF file.
    // @PostMapping("/load-pdf") → This method runs when a POST request is sent to /api/rag/load-pdf.
    // @RequestParam String fileName → Extracts the file name from the request parameter.
    //
    // Example request:
    // POST http://localhost:8080/api/rag/load-pdf?fileName=/path/to/file.pdf
    //
    // Steps:
    // 1. Convert the fileName into a Path object.
    // 2. Wrap it in a FileSystemResource (so Spring can handle it as a resource).
    // 3. Call documentLoaderService.loadPdf(...) to read, split, and store the PDF in the vector store.
    // 4. Return a success message.
    @PostMapping("/load-pdf")
    public String load(@RequestParam String fileName) {
        documentLoaderService.loadPdf(new FileSystemResource(Path.of(fileName)));
        return "PDF Loaded and Indexed!";
    }

    // Endpoint to ask a question and get an AI-generated answer.
    // @PostMapping("/chat") → This method runs when a POST request is sent to /api/rag/chat.
    // @RequestBody String question → Extracts the question text from the request body.
    //
    // Example request:
    // POST http://localhost:8080/api/rag/chat
    // Body: "What does Spring AI do?"
    //
    // Steps:
    // 1. Pass the question to ragService.askQuestion(...).
    // 2. RagService retrieves relevant documents from the vector store.
    // 3. AI model uses those documents as context to generate an answer.
    // 4. Return the AI-generated answer.
    @PostMapping("/chat")
    public String chat(@RequestBody String question) {
        return ragService.askQuestion(question);
    }
}