# ☕ Java Concurrency Comparison: Green Threads vs Project Loom vs Quasar

This repository provides a **comparative implementation and performance analysis** of three key concurrency models in Java:

- ✅ **Green Threads** (historical, cooperative threads)
- 🚀 **Project Loom Virtual Threads** (modern lightweight threads in Java 19+)
- 🌀 **Quasar Fibers** (user-space fibers using bytecode instrumentation)

We also include **Python utilities** for plotting and evaluating execution results.

---


## 🧠 Execution Models Compared

### 1. 🧵 Green Threads (Simulated)
- Manually switches between tasks at fixed intervals (round-robin style)
- Cooperative multitasking within a single OS thread

### 2. 🧵 Java Threads
- Uses `ExecutorService` and native OS threads
- Parallel execution using system threads

### 3. 🌱 Project Loom (Virtual Threads)
- Introduced in Java 19 (via `Thread.ofVirtual()`)
- Ultra-lightweight threads, ideal for I/O-bound concurrency

### 4. 🌌 Quasar Fibers
- Bytecode-instrumented fibers using `quasar-core` library
- Java fibers prior to official Loom support

---

## 📊 Performance Metrics

Each execution model performs:

- ✅ Matrix multiplication  
- ✅ Merge sort  
- ✅ Prime number calculation  

The total execution time for each model is written to `execution_times.txt` in the format:

