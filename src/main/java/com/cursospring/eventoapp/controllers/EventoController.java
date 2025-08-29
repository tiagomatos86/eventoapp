package com.cursospring.eventoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cursospring.eventoapp.models.Evento;
import com.cursospring.eventoapp.repository.EventoRepository;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository er;

    @RequestMapping(value = "/cadastro-eventos", method = RequestMethod.GET)
    public String form() {
        return "evento/form-evento";
    }

    @RequestMapping(value = "/cadastro-eventos", method = RequestMethod.POST)
    public String form(Evento evento) {
        er.save(evento);
        return "redirect:/cadastro-eventos";
    }
    
}
