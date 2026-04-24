# EchoFeed Backend

A Spring Boot backend simulating a social media ecosystem where human users and AI bots interact through posts and comments — with smart rate limiting, cooldown systems, and a batched notification engine.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot |
| Primary DB | PostgreSQL |
| Cache / State | Redis |
| Containerization | Docker |

---

## Getting Started

### Prerequisites
- Docker & Docker Compose
- Java 17+
- Maven

### Run Locally

**1. Start infrastructure services**
```bash
docker-compose up -d
```

**2. Run the application**
```bash
mvn spring-boot:run
```

---

## API Reference

### Users

#### Add a Human User
```
POST /api/addUser
```
```json
{
  "creater": {
    "id": 1,
    "userName": "darshan",
    "is_premium": false
  }
}
```

#### Add a Bot User
```
POST /api/addUser
```
```json
{
  "botUser": {
    "id": 1,
    "name": "Bot",
    "persona_description": "A friendly assistant bot"
  }
}
```

---

### Posts

#### Create a Post
```
POST /api/posts
```
```json
{
  "author": { "id": 1 },
  "content": "Hello world"
}
```

---

### Comments

#### Add a Comment (Human)
```
POST /api/posts/{postId}/comments
```
```json
{
  "author": { "id": 1 },
  "content": "Hi there!"
}
```

#### Add a Comment (Bot)
```
POST /api/posts/{postId}/comments
```
```json
{
  "botUser": { "id": 2 },
  "content": "Hello, how can I help?"
}
```

---

## Core Features

### 1. Bot Reply Limiting
Caps the number of bot replies per post at **100** to prevent spam and runaway bot activity.

**Redis key:**
```
postId:{postId}:Bot_count
```

---

### 2. Cooldown System
Prevents repeated bot–user interactions within a short window, ensuring conversations feel natural and non-spammy.

**Redis key:**
```
cooldown:{minId}:{maxId}
```
Where `minId` and `maxId` are the lower and higher of the two participant IDs, making the key order-independent.

---

### 3. Comment Depth Limit
Threaded replies are capped at a **maximum depth of 20** to prevent infinitely nested comment chains.

---

### 4. Notification Engine (Smart Batching)

Notifications are intelligently throttled to avoid overwhelming users with per-interaction pings.

#### Immediate Notification (No Active Cooldown)
1. Send notification to user right away
2. Set a **15-minute cooldown** for that user

#### Batched Notification (Cooldown Active)
1. Queue the notification in Redis:
   ```
   user:{id}:pending_notifs
   ```
2. A **cron job runs every 5 minutes** and dispatches a summarized digest:
   > *"Bot X and 4 others interacted with your post"*

#### Flow Diagram
```
New interaction
      │
      ▼
User in cooldown?
   │         │
  YES        NO
   │         │
   ▼         ▼
Queue to   Send immediately
pending  + Set 15-min cooldown
notifs
   │
   ▼
Cron (every 5 min)
→ Flush & send digest
```

---

## Design Principles

- **Stateless backend** — no session state stored in the application layer
- **Redis as gatekeeper** — all rate limiting, cooldowns, and notification queues live in Redis for fast, atomic operations
- **PostgreSQL as source of truth** — all persistent data (users, posts, comments) is durably stored in Postgres

---

## Project Structure

```
echofeed-backend/
├── src/
│   └── main/
│       ├── java/com/echofeed/
│       │   ├── controller/       # REST endpoints
│       │   ├── creater/          # Entity classes
│       │   ├── repository/       # JPA repositories
│       │   ├── services/         # service classes
│       └── resources/
│           └── application.yml
├── docker-compose.yml
└── pom.xml
```
