import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Main Spring Boot Application class
@SpringBootApplication // Combines @Configuration, @EnableAutoConfiguration, @ComponentScan
@EnableMongoRepositories // Enables Spring Data MongoDB repositories
public class SpringBootMongoExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMongoExampleApplication.class, args);
    }

    // Optional: CommandLineRunner to add some initial data on startup
    @Bean
    CommandLineRunner initData(ProductRepository repository) {
        return args -> {
            repository.deleteAll(); // Clear existing data for a fresh start

            repository.save(new Product("Laptop", 1200.00, "Powerful computing device"));
            repository.save(new Product("Smartphone", 800.00, "Mobile communication and entertainment"));
            repository.save(new Product("Headphones", 150.00, "Noise-cancelling audio experience"));

            System.out.println("Initial products added to MongoDB:");
            repository.findAll().forEach(System.out::println);
        };
    }
}

// MongoDB Document (Model)
@Document(collection = "products") // Maps this class to a MongoDB collection named "products"
class Product { // Package-private class
    @Id // Marks this field as the primary identifier in MongoDB
    private String id;
    private String name;
    private double price;
    private String description;

    public Product() {}
    public Product(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Product{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", price=" + price +
               ", description='" + description + '\'' +
               '}';
    }
}

// Spring Data MongoDB Repository (Data Access Layer)
interface ProductRepository extends MongoRepository<Product, String> { // Package-private interface
    // Spring Data automatically implements CRUD operations and custom query methods
    List<Product> findByNameContaining(String name); // Example of a custom query method
}

// REST Controller (API Layer)
@RestController // Marks this class as a REST controller
@RequestMapping("/api/products") // Base path for all endpoints in this controller
class ProductController { // Package-private class

    @Autowired // Injects the ProductRepository dependency
    private ProductRepository productRepository;

    // GET all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST create new product
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product); // Saves the product to MongoDB
    }

    // PUT update product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product productDetails) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product existingProduct = product.get();
            existingProduct.setName(productDetails.getName());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setDescription(productDetails.getDescription());
            return ResponseEntity.ok(productRepository.save(existingProduct)); // Updates and saves
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE product
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id); // Deletes from MongoDB
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET products by name containing a string
    @GetMapping("/search")
    public List<Product> searchProductsByName(@RequestParam String name) {
        return productRepository.findByNameContaining(name); // Uses custom query method
    }
}
