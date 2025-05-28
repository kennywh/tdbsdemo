# API Test Project

This Spring Boot project provides a collection of test APIs that return random data for testing purposes. It also includes endpoints to call external APIs.

## API Endpoints

### User API

- `GET /api/users/random` - Get a random user
- `GET /api/users/list/{count}` - Get a list of random users
- `POST /api/users/generate` - Generate a random user (can include preferences)
- `POST /api/users/batch` - Generate a batch of random users

### Product API

- `GET /api/products/random` - Get a random product
- `GET /api/products/catalog/{count}` - Get a list of random products
- `POST /api/products/create` - Create a random product
- `POST /api/products/bulk-create` - Create multiple random products

### Order API

- `GET /api/orders/random` - Get a random order
- `GET /api/orders/history/{count}` - Get a list of random orders
- `POST /api/orders/place` - Place a random order
- `POST /api/orders/bulk-place` - Place multiple random orders

### External API Integration

- `GET /api/external/random` - Call a random external API endpoint
- `GET /api/external/{type}/{id}` - Call a specific external API endpoint with the given ID
- `GET /api/external/query?type={type}&id={id}` - Call a specific external API with query parameters

## API Response Format

All APIs return responses in the following format:

```json
{
  "success": true,
  "message": "Operation successful message",
  "data": { /* Response data object or array */ },
  "timestamp": "2023-06-20T12:34:56.789Z",
  "requestId": "REQ-1234567890-123"
}
```

## Running the Application

1. Ensure you have Java 21 installed
2. Run the application using Maven:
   ```
   ./mvnw spring-boot:run
   ```
3. The API will be available at http://localhost:8080

## Logging

The application logs all API requests and responses. Logs are written to:
- Console
- `logs/demo-api.log` file

## Examples

### Get a random user

```
GET http://localhost:8080/api/users/random
```

### Generate a random product with specific category

```
POST http://localhost:8080/api/products/create
Content-Type: application/json

{
  "category": "Electronics",
  "available": true
}
```

### Call an external API

```
GET http://localhost:8080/api/external/posts/1
``` 