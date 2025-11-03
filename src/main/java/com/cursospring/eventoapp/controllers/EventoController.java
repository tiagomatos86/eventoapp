package com.cursospring.eventoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cursospring.eventoapp.models.Convidado;
import com.cursospring.eventoapp.models.Evento;
import com.cursospring.eventoapp.repository.ConvidadoRepository;
import com.cursospring.eventoapp.repository.EventoRepository;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository er;

    @Autowired
    private ConvidadoRepository cr;

    @RequestMapping(value = "/cadastro-eventos", method = RequestMethod.GET)
    public String form() {
        return "evento/form-evento";
    }

    @RequestMapping(value = "/cadastro-eventos", method = RequestMethod.POST)
    public String form(Evento evento) {
        er.save(evento);
        return "redirect:/cadastro-eventos";
    }

    @RequestMapping(value = "/eventos")
    public ModelAndView listaEventos(){
        ModelAndView mv = new ModelAndView("index");
        Iterable<Evento> eventos = er.findAll();
        mv.addObject("eventos", eventos);
        return mv;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ModelAndView detalhesEvento(@PathVariable("id") long id) {
        Evento evento = er.findById(id).orElse(null);
        ModelAndView mv = new ModelAndView("evento/detalhes-evento");
        mv.addObject("evento", evento);

        Iterable<Convidado> convidados = cr.findByEvento(evento);
        mv.addObject("convidados", convidados);
        return mv;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.POST)
    public String detalhesEventoPost(@PathVariable("id") long id, Convidado convidado) {
        Evento evento = er.findById(id).orElse(null);
        convidado.setEvento(evento);
        cr.save(convidado);
        return "redirect:/{id} ";
    }
    
}
