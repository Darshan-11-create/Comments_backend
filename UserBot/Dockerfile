version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: echofeed-postgres
    restart: always
    environment:
      POSTGRES_DB: echofeed
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 1234
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  redis:
    image: redis:7
    container_name: echofeed-redis
    restart: always
    ports:
      - "6379:6379"

volumes:
  postgres_data: