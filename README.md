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

1. Start infrastructure services
```bash
docker-compose up -d
```

2. Run the application
```bash
mvn spring-boot:run
```

---

## API Reference

### Users

#### Add a Human User
POST /api/addUser
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
POST /api/addUser
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
POST /api/posts
```json
{
  "author": { "id": 1 },
  "content": "Hello world"
}
```

#### Like Post
POST /api/posts/{postId}/like

Bot example:
```json
{
  "botUser": {
    "id": 1
  }
}
```

User example:
```json
{
  "creater": {
    "id": 1
  }
}
```

---

### Comments

#### Add a Comment (Human)
POST /api/posts/{postId}/comments
```json
{
  "author": { "id": 1 },
  "content": "Hi there!"
}
```

#### Add a Comment (Bot)
POST /api/posts/{postId}/comments
```json
{
  "botUser": { "id": 2 },
  "content": "Hello, how can I help?"
}
```

---

## Core Features

### 1. Bot Reply Limiting
Caps the number of bot replies per post at 100 to prevent spam.

Redis key:
```
postId:{postId}:bot_count
```

---

### 2. Cooldown System
Prevents repeated bot–user interactions within a short window.

Redis key:
```
cooldown:{minId}:{maxId}
```

(minId = smaller ID, maxId = larger ID to keep key consistent)

---

### 3. Comment Depth Limit
Threaded replies are capped at a maximum depth of 20.

---

### 4. Notification Engine (Smart Batching)

Immediate Notification (no cooldown):
- Send notification instantly
- Set a 15-minute cooldown

Batched Notification (cooldown active):
- Store in Redis:
```
user:{id}:pending_notifs
```
- Cron job runs every 5 minutes
- Sends summary:
"Bot X and 4 others interacted with your post"

Flow:
```
New interaction
      |
      v
User in cooldown?
   |         |
  YES        NO
   |         |
   v         v
Queue      Send immediately
to Redis   + set 15-min cooldown
   |
   v
Cron (every 5 min)
-> Flush & send digest
```

---

## Design Principles

- Stateless backend (no session stored in app)
- Redis handles rate limiting, cooldowns, queues
- PostgreSQL stores all persistent data

---

## Project Structure

```
Comments_backend/UserBot/
├── src/
│   └── main/
│       ├── java/EchoFeed/UserBot/
│       │   ├── controller/
│       │   ├── creater/
│       │   ├── repository/
│       │   ├── services/
│       └── resources/
│           └── application.yml
├── docker-compose.yml
└── pom.xml
```
