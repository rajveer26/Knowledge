# Knowledge Microservice

 
## JDK Version Requirement

*   **Java 21**

## How to Run the App

1.  **Prerequisites**: Ensure you have Java 21 and Maven installed.
2.  **Build**:
    ```bash
    mvn clean install
    ```
3.  **Run**:
    ```bash
    mvn spring-boot:run
    ```

The application will start on port **5004** with context path `/knowledge-service`.
Base URL: `http://localhost:5004/knowledge-service`


## How to Load Initial Sample Data to the Embedded DB

The application uses an in-memory H2 database (`jdbc:h2:mem:knowledge-db`).
Currently, the database is initialized empty on each restart.
To load sample data, you can use the provided Postman collection or curl commands below to create records via the API.

*Note: `spring.jpa.hibernate.ddl-auto` is set to `update`, so the schema is automatically created.*

## Sample Curl Calls

**1. Login (Get Token)**

```bash
curl --location 'http://localhost:5004/knowledge-service/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "admin",
    "password": "admin123"
}'
```

**2. Create Text Knowledge (Secured)**

*Replace `<ACCESS_TOKEN>` with the token received from the login response.*

```bash
curl --location 'http://localhost:5004/knowledge-service/api/v1/knowledge' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <ACCESS_TOKEN>' \
--data '{
    "type": "TEXT",
    "title": "My First Note",
    "content": "This is a simple text note."
}'
```

**3. Get All Knowledge (Secured)**

```bash
curl --location 'http://localhost:5004/knowledge-service/api/v1/knowledge?page=0&size=10' \
--header 'Authorization: Bearer <ACCESS_TOKEN>'
```

## In-Memory Usernames/Passwords

The application uses an in-memory user details service for demonstration purposes.

| Username | Password | Role |
| :--- | :--- | :--- |
| `admin` | `admin123` | `ADMIN` |
| `user` | `user123` | `USER` |

## Engineering Notes

### Key Design Choices

*   **SOLID Principles**: The service layer is split into `Read` and `Write` interfaces and implementations (CQRS-lite) to separate concerns.
*   **Polymorphism**: Used JPA Single Table Inheritance (`@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`) for the `Knowledge` entity to efficiently store different types (Text, Link, Quote, Nested) in one table while allowing polymorphic queries.
*   **DTOs & Mapping**: Used MapStruct for clean and type-safe mapping between Entities and DTOs.
*   **Security**: Implemented JWT-based stateless authentication using Spring Security.
*   **JSON Handling**: Configured Jackson with `@JsonTypeInfo` to handle polymorphic deserialization of the `KnowledgeInput` abstract class.

### What I Would Improve with 2 More Hours

*   **Integration Tests**: Add full integration tests using `MockMvc` to verify the controller-service-repository flow.
*   **Data Seeding**: Implement a `CommandLineRunner` bean to automatically populate the H2 database with sample data on startup.
*   **Exception Handling**: Enhance the global exception handler to return more structured and user-friendly error responses for validation failures and specific business exceptions.
*   **API Documentation**: Fully configure Swagger/OpenAPI to properly document the polymorphic request bodies and security requirements.
