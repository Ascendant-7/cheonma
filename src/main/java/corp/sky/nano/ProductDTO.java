package corp.sky.nano;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
// import jakarta.validation.constraints.Size;

public class ProductDTO {

    // @NotNull
    private Long id;
    @NotNull(message = "Please input name of product")
    @NotEmpty(message = "Product cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Please input valid username")
    private String name;

    private String description;

    // @Size(min = 1, message = "Price must be greater than 0") is for strings or
    // arrays
    @Min(1)
    private double price;

    @NotNull(message = "Please upload file")
    private MultipartFile image;

    public ProductDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
