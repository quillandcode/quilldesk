package com.quillandcode.quilldesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class H2ConsoleController {

    @GetMapping("/h2-console")
    public RedirectView redirectToH2Console() {
        return new RedirectView("/h2-console/");
    }
}
