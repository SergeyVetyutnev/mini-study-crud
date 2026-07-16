package dev.vetyutnev.ministudycrud;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/v2/test")
    public String testV2() {
        return "тут критический баг!";
    }
}
