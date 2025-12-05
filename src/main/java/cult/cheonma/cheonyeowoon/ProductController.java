package cult.cheonma.cheonyeowoon;

import org.springframework.web.bind.annotation.GetMapping;

public class ProductController {
    private ProductRepository prodRepo;

    public ProductController(ProductRepository prodRepo) {
        this.prodRepo = prodRepo;
    }

    @GetMapping("/products")
    public String getProducts() {
        return "products";
    }
}
