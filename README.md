# Advanced RAG: Spring AI Advisor API

## Project Overview

This project demonstrates a Retrieval-Augmented Generation (RAG) application built using **Spring Boot, Spring AI, Google Gemini, PostgreSQL, and PGVector**.

Instead of manually implementing retrieval logic and prompt construction, the application uses the **Spring AI Advisor API**. The `QuestionAnswerAdvisor` automatically retrieves relevant information from the vector database and adds it to the prompt before sending the request to Google Gemini.

The application allows users to load PDF documents, convert their content into vector embeddings, store them in PostgreSQL using PGVector, and ask questions based on the indexed document content.

---

## Technical Concepts

### 1. The Document Ingestion Pipeline

The application processes raw PDF files into AI-searchable knowledge using Spring AI components:

- **`PagePdfDocumentReader`**: Extracts text from each page of a PDF document.
- **`TokenTextSplitter`**: Splits extracted document content into smaller chunks suitable for embedding and retrieval.
- **`VectorStore`**: Converts document chunks into vector embeddings and stores them in PostgreSQL using PGVector.

The resulting embeddings allow the application to perform semantic similarity searches instead of relying only on keyword matching.

### 2. QuestionAnswerAdvisor

The **`QuestionAnswerAdvisor`** is responsible for the retrieval-augmented generation flow.

When a user submits a question:

1. The question is converted into an embedding.
2. PGVector performs a semantic similarity search.
3. Relevant document chunks are retrieved.
4. The retrieved context is added to the prompt.
5. Google Gemini generates an answer using the retrieved context.

This approach avoids manually implementing the complete retrieval and prompt-enrichment logic.

### 3. Vector Storage with PostgreSQL and PGVector

The application uses **PostgreSQL with PGVector** as the vector database.

The vector store is configured with:

```properties
spring.ai.vectorstore.pgvector.dimensions=768
spring.ai.vectorstore.pgvector.distance-type=COSINE_DISTANCE
spring.ai.vectorstore.pgvector.initialize-schema=true
```

The application uses the following embedding model:

```text
gemini-embedding-001
```

The embedding output is configured to use:

```text
768 dimensions
```

This dimension must match the PGVector configuration.

---

## RAG Architecture

```text
PDF Document
      ↓
PagePdfDocumentReader
      ↓
TokenTextSplitter
      ↓
Gemini Embedding Model
      ↓
PostgreSQL + PGVector
      ↓
Semantic Similarity Search
      ↓
QuestionAnswerAdvisor
      ↓
Google Gemini
      ↓
Context-Aware Answer
```

---

## Component Reference

### `DocumentLoaderService.java`

Handles the document ingestion phase.

It:

1. Reads the PDF document.
2. Extracts the document content.
3. Splits the content into smaller chunks.
4. Generates embeddings for the chunks.
5. Stores the embeddings in the PGVector store.

### `RagService.java`

Handles the retrieval and generation phase.

It uses Spring AI's `ChatClient` together with `QuestionAnswerAdvisor` to retrieve relevant document context and generate an answer using Google Gemini.

### `RagController.java`

Exposes REST endpoints for document ingestion and question answering.

The controller provides two endpoints:

- `/api/rag/load-pdf` - Loads and indexes a PDF document.
- `/api/rag/chat` - Accepts a question and generates an answer using the indexed document context.

---

## Tech Stack

- **Java 23**
- **Spring Boot 3.5.9**
- **Spring AI 1.1.2**
- **Google Gemini**
- **Gemini Embeddings**
- **PostgreSQL**
- **PGVector**
- **Docker**
- **Docker Compose**
- **Maven**
- **REST APIs**

---

## Key Features

- PDF document ingestion
- PDF text extraction
- Token-based document chunking
- Gemini-powered embeddings
- Vector storage using PostgreSQL and PGVector
- Semantic similarity search
- Retrieval-Augmented Generation (RAG)
- Context-aware question answering
- REST API integration
- Dockerized PostgreSQL and PGVector environment

---

## API Endpoints

### 1. Load PDF

**Method:**

```text
POST
```

**URL:**

```text
http://localhost:8080/api/rag/load-pdf?fileName=/path/to/your/document.pdf
```

This endpoint reads the PDF, processes its content, generates embeddings, and stores the resulting vectors in PGVector.

### 2. Ask a Question

**Method:**

```text
POST
```

**URL:**

```text
http://localhost:8080/api/rag/chat
```

**Request Body:**

```text
What are the warranty terms mentioned in the document?
```

The application retrieves relevant document context from PGVector and generates an answer using Google Gemini.

---

## Configuration

The main vector store configuration is:

```properties
spring.ai.vectorstore.pgvector.dimensions=768
spring.ai.vectorstore.pgvector.distance-type=COSINE_DISTANCE
spring.ai.vectorstore.pgvector.initialize-schema=true
```

The Gemini embedding configuration uses:

```properties
spring.ai.google.genai.embedding.text.options.model=gemini-embedding-001
spring.ai.google.genai.embedding.text.options.dimensions=768
```

API keys and local configuration should not be committed to GitHub.

---

## How to Run

### 1. Start PostgreSQL and PGVector

The project includes a Docker Compose configuration for PostgreSQL with PGVector.

Run:

```bash
docker compose up -d
```

### 2. Configure Gemini

Create the following local configuration file:

```text
src/main/resources/application.properties
```

Configure your Gemini API key and PostgreSQL connection details.

**Do not commit API keys or other secrets to GitHub.**

### 3. Start the Spring Boot Application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

## RAG Workflow

The complete RAG workflow is:

1. A user provides a PDF document.
2. `PagePdfDocumentReader` extracts the document content.
3. `TokenTextSplitter` divides the content into smaller chunks.
4. Gemini generates vector embeddings for the document chunks.
5. The embeddings are stored in PostgreSQL using PGVector.
6. The user submits a natural-language question.
7. The question is converted into an embedding.
8. PGVector performs semantic similarity search.
9. Relevant document chunks are retrieved.
10. `QuestionAnswerAdvisor` adds the retrieved context to the prompt.
11. Google Gemini generates the final answer using the retrieved context.

---

## Why Use Spring AI Advisors?

Spring AI Advisors provide a reusable abstraction for common AI application concerns.

In this project, `QuestionAnswerAdvisor` handles the retrieval augmentation process so the application does not need to manually implement the complete retrieval and prompt construction flow.

This makes the RAG implementation easier to maintain and allows additional advisors to be incorporated as the application evolves.

---

## Project Purpose

This project demonstrates how a Java Spring Boot application can integrate modern Generative AI capabilities with:

- Embedding models
- Vector databases
- Semantic search
- Retrieval-Augmented Generation
- Spring AI Advisor APIs
- REST APIs
- PostgreSQL and PGVector
- Docker

The project provides a practical example of building an AI-powered knowledge assistant using the Java and Spring ecosystem.