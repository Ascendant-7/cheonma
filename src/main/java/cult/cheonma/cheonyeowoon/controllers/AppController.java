package cult.cheonma.cheonyeowoon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/TP04/task1")
    public String task1() {
        return "<h1>Welcome to TP04 Task1</h1>";
    }

    @GetMapping("/TP04/task2")
    public String task2() {
        return "task2"; // source: check src/main/resources/templates/task2.html. this page is brought over as a template using thymeleaf
    }
}
