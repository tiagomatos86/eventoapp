package com.cursospring.eventoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cursospring.eventoapp.models.Convidado;
import com.cursospring.eventoapp.models.Evento;
import com.cursospring.eventoapp.repository.ConvidadoRepository;
import com.cursospring.eventoapp.repository.EventoRepository;

import jakarta.validation.Valid;


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
    public String form(@Valid Evento evento,
    BindingResult result, 
    RedirectAttributes attributes) {
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/cadastro-eventos"; // Redireciona de volta para a mesma página
        }

        er.save(evento);
        attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso");
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

        mv.addObject("convidado", new Convidado());

        return mv;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.POST)
    public String detalhesEventoPost(@PathVariable("id") long id, 
    @Valid Convidado convidado, 
    BindingResult result, 
    RedirectAttributes attributes) {
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/{id}"; // Redireciona de volta para a mesma página
        }

        Evento evento = er.findById(id).orElse(null);
        convidado.setEvento(evento);
        cr.save(convidado);
        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
        return "redirect:/{id}";
    }
    
}
