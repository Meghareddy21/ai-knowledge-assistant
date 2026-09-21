#  Advanced RAG: Spring AI Advisor API

## Project Overview
This project demonstrates the most efficient way to build RAG (Retrieval-Augmented Generation) applications. Instead of manually writing SQL queries or building prompts, we use **Spring AI Advisors**. An Advisor acts as a "middleware" that automatically intercepts your chat request, finds relevant data in your database, and attaches it to the prompt before sending it to the AI.



---

##  Technical Concepts

### 1. The ETL Pipeline (Extract, Transform, Load)
We process raw PDF files into "AI-ready" knowledge using three specialized tools:
* **`PagePdfDocumentReader`**: Extracts raw text from each page of a PDF.
* **`TokenTextSplitter`**: AI models have a "context window" limit. This tool breaks long documents into smaller chunks (e.g., 800 tokens) so the AI doesn't get overwhelmed.
* **`VectorStore.accept()`**: Automatically converts text chunks into vectors (embeddings) and saves them to PostgreSQL.

### 2. QuestionAnswerAdvisor
This is the "magic" component. When you call `chatClient.prompt().call()`:
1.  The **Advisor** takes your question.
2.  It queries **PGVector** for the top 10 (`topK(10)`) most similar document chunks.
3.  It reformats your prompt to look like: *"Answer this question using the following context: [Retrieved Data]... Question: [Your Question]"*.
4.  It sends this "Augmented" prompt to Google Gemini.

### 3. Managed Vector Storage
By setting `spring.ai.vectorstore.pgvector.initialize-schema=true`, Spring AI creates a professional-grade table for you:
* **Table Name**: `vector_store`
* **Column**: `embedding` (Type: `vector(768)`)
* **Index**: An **HNSW** or **IVFFlat** index is created for lightning-fast similarity searches.



---

## Component Reference

### `DocumentLoaderService.java`
Handles the "Ingestion" phase. It converts a file into a searchable list of vectors.
> **Note**: It uses `TokenTextSplitter` which is smarter than character splitting; it ensures we don't cut words or sentences in half awkwardly.

### `RagService.java`
Handles the "Retrieval & Generation" phase.
* Uses a **Fluent Builder** to create a `ChatClient`.
* Attaches the `QuestionAnswerAdvisor` globally, so every chat interaction is automatically "context-aware."

### `RagController.java`
Exposes two endpoints:
1. `/load-pdf?fileName=...`: To ingest new data.
2. `/chat`: To ask questions about the ingested data.

---

##  Configuration Highlight
```properties
# Dimensions must match the embedding model (text-embedding-004 = 768)
spring.ai.vectorstore.pgvector.dimensions=768
# Schema init creates the DB table and vector extension automatically
spring.ai.vectorstore.pgvector.initialize-schema=true
```
---
## How to Test
Upload Data:

Method: POST

URL: http://localhost:8080/api/rag/load-pdf?fileName=/path/to/your/manual.pdf

Ask a Question:

Method: POST

URL: http://localhost:8080/api/rag/chat

Body (Raw Text): What are the warranty terms mentioned in the document?

---
## Why this is better than "Manual RAG"
Portability: You can swap PostgreSQL for Redis or Weaviate by changing one dependency.

Maintainability: No custom SQL logic is hidden in your code.

Sophistication: You can add more advisors, like a MessageChatMemoryAdvisor, to give your bot "conversation history" with just one extra line of code!