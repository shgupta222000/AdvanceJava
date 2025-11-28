# Java Mastery — Advanced Plan & Must-Do Interview Questions

This living plan is designed for SDE-2 / Senior Backend level interviews (MAANG-style). It focuses on advanced Java topics, must-do coding questions, and hands-on exercises. For each coding problem we will provide Brute / Better / Best implementations, complexity analysis, and test cases — and you’ll code in IntelliJ while I review.

⸻

## How to use this document
1. Follow the weekly/topic plan below.  
2. For each topic you’ll get:  
   • Concept checklist  
   • 6–12 must-do interview problems (from easy → hard)  
   • Practical tasks (micro-projects, debugging, profiling)  
   • Common pitfalls & focused revision notes  
3. When you start a problem, paste your code (or tell me you want me to write the implementations). I’ll provide:  
   • Brute solution (easy-to-understand)  
   • Better solution (optimized, idiomatic)  
   • Best solution (production-ready, thread-safety, edge cases)  
   • Explanation, complexity, and IntelliJ tips

⸻

## Overall roadmap (8–10 weeks flexible)

### Week 1 — Concurrency & Multithreading (deep)
• Concepts: Java Memory Model, volatile, synchronized, wait/notify, ReentrantLock, Condition, ThreadPoolExecutor, ForkJoinPool, ThreadLocal, atomic classes, CAS, Deadlocks, Livelocks, Starvation  
• Must-do questions: bounded blocking queue; producer-consumer with multiple producers/consumers; read-write lock implementation; fixed thread-pool task scheduling; dining philosophers (practical); implement a lock-free stack using AtomicReference  
• Practical: profile synchronized vs ReentrantLock; debug a deadlock in a sample app

### Week 2 — JVM internals & Performance
• Concepts: Classloading, bytecode, JIT, HotSpot tiers, escape analysis, GC algorithms (G1, ZGC, Shenandoah), allocation, object layout, metaspace  
• Must-do: memory leak debugging (common patterns), tune GC for throughput vs latency, analyze a flamegraph, reduce allocation pressure, lazy-holder idiom  
• Practical: run jmap/jstack/jstat on a sample app

### Week 3 — Collections, Generics & Streams (deep)
• Concepts: HashMap internals, ConcurrentHashMap design, TreeMap, ArrayList vs LinkedList, Collisions, load factor, generics type erasure, PECS, Stream pipeline, terminal operations, parallel streams  
• Must-do: implement a HashMap simplified, design a concurrent LRU cache, stream-based partitioning and grouping tasks

### Week 4 — Networking, NIO & Serialization
• Concepts: Sockets, NIO (Channels, Buffers, Selectors), async IO, ByteBuffer, serialization pitfalls, custom serialization (Externalizable), protobuf/gRPC basics  
• Must-do: implement non-blocking echo server, design a simple RPC stub, serialize/deserialize large objects efficiently

### Week 5 — Design Patterns, Architecture & Best Practices
• Concepts: Factory, Builder, Strategy, Observer, Singleton nuances in Java, SOLID, clean code, dependency injection, modules (JPMS)  
• Must-do: refactor a spaghetti design to SOLID, implement plugin architecture using ServiceLoader

### Week 6 — Reactive, CompletableFuture & Async programming
• Concepts: CompletableFuture chaining, exceptionally, combining futures, backpressure, Reactor vs RxJava basics  
• Must-do: implement retry with exponential backoff using CompletableFuture, convert callback-based API to CompletableFuture

### Week 7 — Spring ecosystem (core) & Testing
• Concepts: Spring IoC, bean lifecycle, AOP, transaction management, Spring Boot internals, testing with JUnit5 and Mockito, integration tests, TestContainers basics  
• Must-do: write unit and integration tests for a REST service, mock tricky dependencies, test transactional rollback

### Week 8 — Distributed Systems basics & Microservices
• Concepts: CAP, consensus basics (raft/paxos high-level), idempotency, retries, circuit-breaker, eventual consistency, distributed tracing, caching strategies  
• Must-do: design a scalable order service, implement idempotent endpoint, design cache invalidation strategy

### Stretch topics (optional)
• Security (JWT, OAuth2), Native Image / GraalVM, JVM languages interop (Kotlin), Kafka fundamentals, Database internals and indexing

⸻

## Coding practice cadence & rules
• Solve problems on paper first; write tests in IntelliJ before implementation.  
• Timebox: 45–90 minutes per problem in interview simulations.  
• Practice explaining: for every solution write a short explanation for a non-coder and for an engineering peer.  
• After coding: run static analysis, measure memory/time for large inputs, and profile if needed.

⸻

## Evaluation checklist (what interviewers look for)
• Correctness and robustness (edge cases)  
• Time and space complexity understanding  
• Code readability, naming and small functions  
• Concurrency safety and performance where applicable  
• Trade-offs and alternatives discussed  
• Testing and error handling

⸻

## IntelliJ setup & tips (quick)
• Use Live Templates for common boilerplate (synchronized block, try-with-resources).  
• Use structural search to find thread-safety smells.  
• Run configurations: create Gradle/Maven run configs for tests, and attach debugger configs with breakpoints.  
• Use VisualVM / Flight Recorder integration for profiling.

⸻

## Example list of must-do interview coding problems (high-impact)

(We’ll iterate on these; each will have brute/better/best implementations)  
1. Thread-safe bounded blocking queue (wait/notify and ReentrantLock versions)  
2. Producer-consumer with multiple producers/consumers and graceful shutdown  
3. Implement LRU cache (thread-safe) — LinkedHashMap vs custom doubly-linked list + hashmap  
4. ConcurrentHashMap simplified implementation and segment design discussion  
5. Implement a lock-free stack using AtomicReference (Treiber stack)  
6. Design and implement a barrier (CyclicBarrier-like)  
7. Implement readers-writer lock from scratch  
8. Implement a simple thread pool (fixed-size) with task queue and worker threads  
9. Merge k sorted lists (with heap) — discuss parallel merge  
10. Serialize/deserialize large object graph efficiently (avoid cycles)  
11. Design rate limiter (token bucket and leaky bucket implementations)  
12. Implement top-K elements in streaming data  
13. Convert callback-style API to CompletableFuture and show composition patterns  
14. Implement custom classloader that can load classes from byte[] and reload  
15. Implement a concurrent LRU cache with expiration and weak references