// This line defines the "package" where this file belongs.
// A package is like a folder that groups related files together.
// Here, the package is called "com.spring.ai.LLMs.rag.simple_retrieval_augmented_generation".
package com.spring.ai.LLMs.rag.simple_retrieval_augmented_generation;

// Importing Spring AI classes.
// Document → Represents a piece of text (like a page or chunk).
// PagePdfDocumentReader → Reads PDF files and converts them into Document objects.
// TokenTextSplitter → Splits large text into smaller chunks for better processing.
// VectorStore → A database-like store for embeddings (numerical representations of text).
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;

// Importing Spring framework classes.
import org.springframework.core.io.Resource;   // Represents a file resource (like a PDF file).
import org.springframework.stereotype.Service; // Marks this class as a Spring Service.

// Importing Java utilities.
import java.util.List;

// @Service tells Spring:
// "This class contains business logic and should be managed as a Service bean."
@Service
public class DocumentLoaderService {

    // A reference to the VectorStore.
    // VectorStore is where we save embeddings (numerical vectors) of documents
    // so they can be searched later for similarity (used in RAG).
    private final VectorStore vectorStore;

    // Constructor injection:
    // Spring will automatically provide a VectorStore object when creating DocumentLoaderService.
    public DocumentLoaderService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    // Method to load a PDF file into the vector store.
    // Steps:
    // 1. Read the PDF file into Document objects.
    // 2. Split the documents into smaller chunks (to handle large text).
    // 3. Save the chunks into the vector store for later retrieval.
    public void loadPdf(Resource pdfResource) {

        // Step 1: Read the PDF file.
        // PagePdfDocumentReader takes a PDF resource and converts each page into a Document object.
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource);
        List<Document> documents = pdfReader.get();

        // Step 2: Split the documents into smaller chunks.
        // TokenTextSplitter breaks large text into smaller pieces (tokens).
        // This helps embeddings work better because smaller chunks are easier to process.
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.split(documents);

        // Step 3: Save the chunks into the vector store.
        // vectorStore.accept(chunks) → Converts each chunk into embeddings and stores them.
        vectorStore.accept(chunks);
    }
}