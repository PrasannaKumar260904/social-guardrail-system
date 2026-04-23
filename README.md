# 🚀 Social Media Guardrail System

### High-Performance Spring Boot Microservice with Redis-Based Concurrency Control

---

## 🧠 Project Overview

This project is a **scalable backend system** designed to manage social interactions (posts, comments, likes) while enforcing strict **anti-spam guardrails using Redis**.

It acts as a **central API gateway + control system**, ensuring:

* Controlled bot interactions
* Real-time scoring (virality)
* Prevention of abuse using atomic Redis operations
* Efficient notification batching

---

## 🏗️ Tech Stack

* **Java 17**
* **Spring Boot 3.x**
* **PostgreSQL**
* **Redis (Spring Data Redis)**
* **Spring Security + JWT**
* **Docker (PostgreSQL + Redis)**
* **Postman (API testing)**

---

## 📁 Project Structure

```
com.project.backend
├── config              # Redis configuration
├── controller          # REST APIs
├── dto                 # Request/Response DTOs
├── entity              # JPA entities
├── exception           # Custom exceptions + global handler
├── repository          # Database access layer
├── scheduler           # CRON jobs
├── security            # JWT + Spring Security
├── service             # Core business logic
```

---

## 👥 Entities

* **User** → id, username, isPremium
* **Bot** → id, name, personaDescription
* **Post** → id, authorId, authorType, content, createdAt
* **Comment** → id, postId, authorId, authorType, content, depthLevel
* **AuthorType** → USER / BOT

---

## 🔐 Authentication & Security

* JWT-based authentication
* Stateless backend (no sessions)
* Custom JWT filter validates each request

### 🔑 Flow:

1. User logs in → receives JWT
2. Client sends token:

```
Authorization: Bearer <JWT_TOKEN>
```

3. Backend validates token before processing request

### 🔒 Protected APIs:

* Create Post
* Add Comment
* Like Post

---

## 📡 REST APIs

### 📝 Posts

* `POST /api/posts` → Create post
* `GET /api/posts/my` → Get posts (pagination)
* `PUT /api/posts/{id}` → Update
* `DELETE /api/posts/{id}` → Delete

### 💬 Comments

* `POST /api/posts/{postId}/comments`

### ❤️ Likes

* `POST /api/posts/{postId}/like`

### 🔐 Auth

* `POST /api/users`
* `POST /api/users/login`

---

# 🔥 Phase 2 — Redis Virality Engine & Guardrails
## ⚡ Thread Safety

Redis acts as a **gatekeeper**:
- Validation happens before DB write  
- Uses atomic operations (`INCR`, `EXISTS`)  
- Prevents race conditions  

---

## ⚡ How Thread Safety is Achieved

Redis is used as a gatekeeper before database writes.

- Bot limit uses atomic `INCR`
- Cooldown uses `EXISTS + TTL`
- No in-memory state is used

This ensures:
- No race conditions
- Exact enforcement of limits (100 bot comments max)
- Safe concurrent handling
---
## 🧮 Virality Score

Stored in Redis:

```
post:{id}:virality_score
```

| Action        | Points |
| ------------- | ------ |
| Bot Reply     | +1     |
| Human Comment | +50    |
| Like          | +20    |

---

## 🔒 Atomic Guardrails (Concurrency Safe)

### 1️⃣ Horizontal Cap (Bot Limit)

```
post:{id}:bot_count
```

* Max = 100 bot comments per post
* Enforced using Redis atomic operations

---

### 2️⃣ Vertical Cap (Depth Limit)

* Maximum depth = **20 levels**
* Prevents infinite nested threads

---

### 3️⃣ Cooldown Cap

```
cooldown:bot:{botId}:human:{userId}
```

* TTL = 10 minutes
* Prevents repeated bot spam on same user

---

## ⚡ Thread Safety (IMPORTANT)

Redis is used as a **gatekeeper**:

* Validation happens **before DB write**
* Atomic operations (`INCR`, `EXISTS`) ensure:

  * No race conditions
  * Strict limit enforcement

---

## 🧪 Concurrency Test (Spam Test)

Simulated:

* **200 concurrent bot requests**

### ✅ Result:

| System              | Value |
| ------------------- | ----- |
| Redis bot_count     | 100   |
| PostgreSQL comments | 100   |

✔ No overflow
✔ No race condition
✔ Perfect consistency

---

# 🔔 Phase 3 — Notification Engine

## 📩 Redis Throttler

Key:

```
user:{id}:pending_notifs
```

### Logic:

| Condition         | Action              |
| ----------------- | ------------------- |
| Recently notified | Store in Redis list |
| Not notified      | Send immediately    |

---

## ⏱ Cooldown

```
user:{id}:notification_cooldown
```

* TTL = 15 minutes

---

## 🔁 Scheduler (CRON)

* Runs every **5 minutes**
* Aggregates notifications

### Example Output:

```
Bot X and 5 others interacted with your posts
```

---

# ⚙️ Configuration

Using `application.yml`:

* PostgreSQL connection
* Redis connection
* JPA settings

---

# 🐳 Docker Setup

## Start Services:

```
docker-compose up -d
```

### Services:

* PostgreSQL → 5432
* Redis → 6379

---

# 🧪 Testing

* Postman collection included
* Load testing using `bots.csv`
* Redis verified using `redis-cli`

---


# 🧠 Key Design Decisions

### 🔥 Redis as Gatekeeper

All validation logic happens in Redis **before database write**

---

### 🔥 Stateless Architecture

* No in-memory state
* Fully scalable

---

### 🔥 Clean Architecture

* Controller → API
* Service → Business logic
* Redis → Guardrails
* DB → Source of truth

---

# 🚀 Features Implemented

* JWT Authentication
* Redis-based concurrency control
* Bot vs Human interaction system
* Virality scoring engine
* Notification batching system
* CRON scheduler
* Exception handling
* Dockerized setup

---

# 🏁 Conclusion

This project demonstrates:

* Scalable backend system design
* Strong concurrency handling using Redis
* Real-time decision-making architecture
* Production-ready coding practices

---

## 📦 Deliverables

* GitHub Repository
* Docker Compose Setup
* Postman Collection
* This README

---

##  Final Note

Redis is used as a **real-time guardrail engine** to ensure strict control over system behavior under high concurrency, making the backend reliable, scalable, and production-ready.
---
For Reference :
# 🚀 How to Run the Project

## 1️⃣ Start Docker

```bash
docker-compose up -d
```

Verify:

```bash
docker ps
```

---

## 2️⃣ Run Backend

```bash
mvn clean install
mvn spring-boot:run
```

Server:

```
http://localhost:9999
```

---

# 🗄️ Database Access

```bash
psql -U postgres -d social_db -h localhost -p 5432
```

```sql
SELECT * FROM post;
SELECT * FROM comment;
SELECT COUNT(*) FROM comment WHERE post_id = 1;
```

---

# ⚡ Redis Commands

```bash
redis-cli
```

```bash
GET post:1:bot_count
GET post:1:virality_score
KEYS *
FLUSHALL
```

---

# 🧪 Testing (Postman)

## Register

```json
{
  "username": "user1",
  "password": "1234"
}
```

## Login → Get JWT

## Use Header

```
Authorization: Bearer <JWT_TOKEN>
```

---

## Create Post

`POST /api/posts`

---

## Add Comment

```json
{
  "authorId": 1,
  "authorType": "USER",
  "content": "Nice post",
  "depthLevel": 0
}
```

---

## Bot Comment

```json
{
  "authorId": 100,
  "authorType": "BOT",
  "content": "Spam bot",
  "depthLevel": 0
}
```

---

# 🔥 Guardrail Tests

* Bot limit → 100 allowed
* Cooldown → blocked
* Depth > 20 → rejected
* Invalid post → 404

---

# 🔔 Notification Testing

* Check logs → "Push Notification Sent"
* Redis → pending notifications
* Scheduler → summary message

---

# 🐳 Docker Commands

```bash
docker ps
docker-compose up -d
docker-compose down
docker logs social_postgres
docker logs social_redis
```



