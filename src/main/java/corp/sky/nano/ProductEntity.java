package corp.sky.nano;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public String description;
    public double price;
    public String image_path;
    @CreationTimestamp
    public LocalDateTime created_at;
    @UpdateTimestamp
    public LocalDateTime updated_at;

    public ProductEntity() {
    }

    public ProductEntity(String name, String description, double price, String image_path) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image_path = image_path;
    }
}
