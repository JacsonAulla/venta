package com.Proyecto.Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class controlController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/viajes/region/{codigo}")
    public String mostrarRegion(@PathVariable String codigo, Model model) {
        model.addAttribute("codigoRegion", codigo);
        return "region";
    }
}
