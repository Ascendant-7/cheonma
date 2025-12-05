package corp.sky.nano.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import corp.sky.nano.Product;

@Controller
public class AppController {

    @GetMapping("/TP04/task1")
    public String task1() {
        return "<h1>Welcome to TP04 Task1</h1>";
    }

    @GetMapping("/TP04/task2")
    public String task2() {
        return "task2"; // source: check src/main/resources/templates/task2.html. this page is brought
                        // over as a template using thymeleaf
    }

    @GetMapping("/TP04/task3")
    public String task3() {
        return "task3"; // source: check src/main/resources/templates/task3.html. this page is brought
                        // over as a template using thymeleaf
    }

    @GetMapping("/TP04/task4")
    public String task4(Model model) {
        return "task4";
    }

    @GetMapping("/TP04/task5")
    public String task5(Model model) {
        return "task5";
    }

    @GetMapping("/")
    public String landing(Model model) {
        List<Product> products = new ArrayList<Product>();
        products.add(new Product("Naruto 1"));
        products.add(new Product("Naruto 2"));
        model.addAttribute("product_name", "naruto toy");
        model.addAttribute("products", products);
        return "product";
    }

    @GetMapping("/redirectTodo")
    public String redirect(Model model) {
        return "redirect:/todo";
    }

    @GetMapping("/todo")
    public String getTodo(Model model) {
        return "todo/todos";
    }
}