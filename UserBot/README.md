# EchoFeed Backend

## Overview
This project is a Spring Boot backend simulating a social media system where users and bots interact through posts and comments.

## Tech Stack
- Spring Boot
- PostgreSQL
- Redis
- Docker

## Features

### 1. Bot Reply Limiting
- Max 100 bot replies per post
- Stored in Redis:
  postId:{postId}:Bot_count

### 2. Cooldown System
- Prevents repeated bot-user interaction
- Redis key:
  cooldown:{minId}:{maxId}

### 3. Comment Depth Limit
- Maximum depth = 20

### 4. Notification Engine (Smart Batching)

#### Redis Throttler
- If user NOT in cooldown:
  - Send notification immediately
  - Set 15 min cooldown

- If user in cooldown:
  - Store in:
    user:{id}:pending_notifs

#### Cron Job
- Runs every 5 minutes
- Sends summarized notifications:
  "Bot X and N others interacted with your post"

---

## How to Run

### 1. Start services
docker-compose up -d

### 2. Run application
mvn spring-boot:run

---

## API Endpoints

### Create Comment
POST /api/posts/{postId}/comments

Example (Bot):
{
  "botUser": { "id": 2 },
  "content": "Hello"
}

Example (User):
{
  "author": { "id": 1 },
  "content": "Hi"
}

---

## Design Principles
- Stateless backend
- Redis as gatekeeper
- PostgreSQL as source of truth
