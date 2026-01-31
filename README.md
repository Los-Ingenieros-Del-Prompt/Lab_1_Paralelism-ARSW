# 🧮 Lab 1: Parallelism - ARSW

> <b>Implementation of π digit calculation using sequential and parallel strategies with Java</b>

---

## 📑 Table of Contents

1. [🎯 Project Objective](#-project-objective)
2. [⚡ Main Features](#-main-features)
3. [⚙️ Technologies Used](#️-technologies-used)
4. [🏗️ Architecture](#️-architecture)
5. [🚀 Running the Project](#-running-the-project)
6. [📡 API Endpoints](#-api-endpoints)
7. [🧪 Testing](#-testing)
8. [📊 Code Coverage](#-code-coverage)

---

## 🎯 Project Objective

This project extends the code given by our teacher to calculate hexadecimal digits of π (Pi) after the decimal point. The main objective is to demonstrate and compare the performance between **sequential** and **parallel** execution using multiple threads in Java.

The project includes a REST API built with Spring Boot that allows:
- Calculate π digits at any position
- Compare execution times between strategies
- Configure the number of threads for parallelization
- Measure the performance of different implementations

---

## ⚡ Main Features

### 🔹 Pi Digit Calculation
- **BBP Algorithm**: Implementation of the Bailey–Borwein–Plouffe algorithm for hexadecimal digit calculation
- **Range Calculation**: Obtain specific digits from any starting position
- **Configurable Strategies**: 
  - `sequential`: Sequential execution in a single thread
  - `threads`: Parallel execution with multiple threads

### 🔹 Performance Measurement
- **Execution Timers**: Precise measurement of calculation time
- **Strategy Comparison**: API to compare performance between sequential and parallel strategies
- **Detailed Metrics**: Information about execution time and configuration used

### 🔹 REST API
- **Documented Endpoints**: Integration with Swagger/OpenAPI for interactive documentation
- **Parameter Validation**: Automatic input validation with descriptive error messages
- **JSON Responses**: Structured format with complete result information

---

## ⚙️ Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 21 | Main programming language |
| **Spring Boot** | 3.3.5 | REST API framework |
| **Maven** | - | Dependency management and build |
| **SpringDoc OpenAPI** | 2.6.0 | Automatic API documentation (Swagger UI) |
| **JUnit** | 5.x | Unit testing framework |
| **JaCoCo** | 0.8.12 | Code coverage analysis |

---

## 🏗️ Architecture

The project is organized in the following layers:

```
src/main/java/edu/eci/arsw/parallelism/
├── api/                           # Presentation layer
│   ├── PiDigitsController.java    # REST controller
│   ├── PiResponse.java            # Response DTO
│   └── GlobalExceptionHandler.java # Global error handling
│
├── core/                          # Business logic
│   ├── PiDigits.java              # BBP algorithm
│   └── PiDigitsService.java       # Calculation service
│
├── concurrency/                   # Parallelization strategies
│   ├── ParallelStrategy.java      # Strategy interface
│   └── ThreadJoinStrategy.java    # Thread-based implementation
│
├── monitoring/                    # Performance measurement
│   ├── PerformanceMonitor         # Executes a Pi calculation and measures its execution time.
│   └── PiExecutionResult.java     # Pi-specific result
│
└── ParallelismApplication.java    # Spring Boot main application
```

### Main Components

- **PiDigits**: BBP algorithm implementation for digit calculation
- **PiDigitsService**: Orchestrates execution strategies and measurement
- **ParallelStrategy**: Strategy pattern for different execution modes
- **ExecutionTimer**: Utility for precise execution time measurement

---

## 🚀 Running the Project

### Prerequisites
- Java 21 or higher
- Maven 3.6+

### Compile the project
```bash
mvn clean compile
```

### Run tests
```bash
mvn test
```

### Run with coverage
```bash
mvn verify
```

### Start the application
```bash
mvn spring-boot:run
```

The application will be available at: `http://localhost:8080`

### Access Swagger documentation
Once the application is running, navigate to:
```
http://localhost:8080/swagger-ui.html
```

---

## 📡 API Endpoints

### 1. Calculate π Digits

**GET** `/api/v1/pi/digits`

Calculates and returns hexadecimal digits of π.

**Parameters:**
- | `start` | int | Yes | Starting position (0-based)
- | `count` | int | Yes | Number of digits
- | `threads` | int | No | Number of threads (parallel) 
- | `strategy` | string | No | Strategy: `sequential` or `threads` 

**Request Example:**
```bash
curl "http://localhost:8080/api/v1/pi/digits?start=0&count=10&threads=4&strategy=threads"
```

**Response Example:**
```json
{
  "start": 0,
  "count": 10,
  "digits": "243F6A8885"
}
```

---

### 2. Measure Calculation Performance

**GET** `/api/v1/pi/digits/measure`

Calculates π digits and returns execution time metrics.

**Parameters:** *(Same as the previous endpoint)*

**Request Example:**
```bash
curl "http://localhost:8080/api/v1/pi/digits/measure?start=0&count=1000&threads=4&strategy=threads"
```

**Response Example:**
```json
{
  "start": 0,
  "count": 1000,
  "digits": "243F6A8885A308D31319...",
  "executionTimeMs": 45,
  "strategy": "threads",
  "threads": 4
}
```

---

## 🧪 Testing

The project includes unit tests to validate:
- ✅ Correctness of the BBP algorithm
- ✅ Input parameter validation
- ✅ Parallel strategy functionality
- ✅ REST API endpoints
- ✅ Error and exception handling

### Run all tests
```bash
mvn test
```

### Run tests for a specific class
```bash
mvn test -Dtest=PiDigitsTest
```

---

## 📊 Code Coverage

The project uses **JaCoCo** to generate code coverage reports.

### Generate coverage report
```bash
mvn verify
```

The HTML report is generated at:
```
target/site/jacoco/index.html
```

### Coverage Requirements
- **Minimum required**: 80% line coverage
- The build will fail if this threshold is not met

### View report
```bash
open target/site/jacoco/index.html  # macOS
xdg-open target/site/jacoco/index.html  # Linux
start target/site/jacoco/index.html  # Windows
```

---

## 📝 Notes on Parallelism

### Thread Strategy
The parallel implementation:
1. Divides the digit range into segments
2. Assigns each segment to a different thread
3. Waits for all threads to complete (join)
4. Assembles the results in order

### Performance
The speedup depends on:
- Number of available processor cores
- Calculation range size (thread overhead)
- Thread synchronization overhead

For small ranges, the overhead may make the parallel version slower than the sequential one.

