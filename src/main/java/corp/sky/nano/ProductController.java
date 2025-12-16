package corp.sky.nano;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    private ProductRepository repo;
    private String pathUpload = "images/";

    public ProductController(ProductRepository prodRepo) {
        this.repo = prodRepo;
    }

    @GetMapping("/")
    public String getProducts(Model model) {
        List<ProductEntity> products = repo.findAll();
        model.addAttribute("title", "Product List");
        model.addAttribute("path", "fragments/list_product");
        model.addAttribute("fragment", "list_product");
        model.addAttribute("products", products);
        return "product";
    }

    @GetMapping("/createProduct")
    public String getProduct(Model model) {
        model.addAttribute("title", "Create Product");
        model.addAttribute("path", "fragments/form");
        model.addAttribute("fragment", "form");
        model.addAttribute("dto", new ProductDTO());
        return "product";
    }

    @GetMapping("/files/{file:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable("file") String filename) {
        try {

            Path imageFile = Path.of(pathUpload).resolve(filename);
            if (imageFile.toUri() == null) { // fixes @NotNull check
                return ResponseEntity.badRequest().build();
            }
            Resource resource = new UrlResource(Objects.requireNonNull(imageFile.toUri(), "File URI cannot be null."));

            // Resource resource =
            if (resource.exists() || resource.isReadable())
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
        } catch (Exception e) {
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {

        ProductEntity product = repo.findById(Objects.requireNonNull(id, "ID cannot be null"))
                .orElseThrow(() -> new RuntimeException("Product not found."));

        model.addAttribute("title", "Update Product");
        model.addAttribute("path", "fragments/form");
        model.addAttribute("fragment", "form");

        ProductDTO dto = new ProductDTO();
        dto.setId(product.id);
        dto.setName(product.name);
        dto.setDescription(product.description);
        dto.setPrice(product.price);

        model.addAttribute("dto", dto);

        return "product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@Valid @ModelAttribute("dto") ProductDTO dto, BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            System.out.println("Validation errors:");
            result.getAllErrors().forEach(error -> System.out.println(error.toString().replaceAll("; ", ";\n")));

            // return to form as usual
            if (dto.getImage().isEmpty()) {
                result.rejectValue("image", "error.dto", "Image is required.");
            }

            model.addAttribute("title", "Create Product");
            model.addAttribute("path", "fragments/form");
            model.addAttribute("fragment", "form");
            return "product";
        }

        ProductEntity product;
        // check form if have product id so we have to find by id
        Long id = dto.getId();

        product = id == null ? new ProductEntity() // CREATE
                : repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found.")); // UPDATE

        product.name = dto.getName();
        product.price = dto.getPrice();
        product.description = dto.getDescription();
        MultipartFile image = dto.getImage();
        String imageURL = "";
        if (image != null && !image.isEmpty()) {
            try {
                Path imagePath = Path.of(pathUpload, image.getOriginalFilename());
                Files.createDirectories(imagePath.getParent());
                try (InputStream is = image.getInputStream()) {
                    Files.copy(is, imagePath, StandardCopyOption.REPLACE_EXISTING);
                }
                imageURL = "/files/" + image.getOriginalFilename();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image file", e);
            }
        }

        product.image_path = imageURL;
        repo.save(product);

        return "redirect:/";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        ProductEntity product = repo.findById(Objects.requireNonNull(id, "ID cannot be null"))
                .orElseThrow(() -> new RuntimeException("Product not found."));
        if (product.image_path != null) {
            try {
                Path file = Path.of(pathUpload, product.image_path.substring("/files".length()));
                Files.deleteIfExists(file);
            } catch (Exception e) {
            }
        }

        repo.delete(product);
        return "redirect:/";
    }

}
