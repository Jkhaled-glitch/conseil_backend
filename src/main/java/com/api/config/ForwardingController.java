package com.api.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ForwardingController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        // Redirige vers index.html pour toutes les routes frontend
        return "forward:/index.html";
    }
}
