# Spring Boot MongoDB CRUD Example

This example demonstrates a basic CRUD (Create, Read, Update, Delete) application using Spring Boot and MongoDB. It showcases how to define a MongoDB document, create a Spring Data MongoDB repository for data access, and expose a REST API using Spring Web. The application initializes some sample product data on startup.

## Language

`java`

## How to Run

1. Ensure Java 17+, Maven, and a MongoDB instance (e.g., `docker run -p 27017:27017 --name mongo -d mongo`) are ready.
2. Create a Spring Boot Maven project, add `spring-boot-starter-data-mongodb` and `spring-boot-starter-web` dependencies to `pom.xml`, and place this code in `src/main/java/com/example/SpringBootMongoExampleApplication.java`.
3. Run `mvn spring-boot:run` from your project root and access the API at `http://localhost:8080/api/products`.

## Original Article

This example accompanies the Turkish article: [Spring Boot ve MongoDB Entegrasyonu: Modern Uygulama Geliştirme Rehberi](https://fatihsoysal.com/blog/?p=43680).

## License

MIT — see [LICENSE](LICENSE).
