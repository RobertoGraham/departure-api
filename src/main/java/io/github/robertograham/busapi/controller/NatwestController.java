package io.github.robertograham.busapi.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/natwest")
final class NatwestController {

    @CrossOrigin({"*"})
    @PostMapping
    public String natwest() {
        return "OK";
    }
}
