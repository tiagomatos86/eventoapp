package com.cursospring.eventoapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EventoController {

    @RequestMapping("/cadastro-eventos")
    public String form() {
        return "evento/form-evento";
    }
    
}
