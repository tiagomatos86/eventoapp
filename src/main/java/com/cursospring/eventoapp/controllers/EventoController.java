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

    @RequestMapping("/deletarEvento/{id}")
    public String deletarEvento(@PathVariable("id") long id, RedirectAttributes attributes) {
        Evento evento = er.findById(id).orElse(null);
        if (evento != null) {
            Iterable<Convidado> convidados = cr.findByEvento(evento);
            cr.deleteAll(convidados); // exclui os convidados primeiro
            er.delete(evento);        // agora pode excluir o evento
            attributes.addFlashAttribute("mensagem", "Evento excluído com sucesso!");
        } else {
            attributes.addFlashAttribute("mensagem", "Evento não encontrado.");
        }
        return "redirect:/eventos";
    }

    @RequestMapping("/deletarConvidado/{rg:.+}")
    public String deletarConvidado(@PathVariable("rg") String rg, RedirectAttributes attributes) {
        Convidado convidado = cr.findByRg(rg);
        if(convidado != null) {
            Long eventoId = convidado.getEvento().getId(); // pega o ID do evento antes de excluir
            cr.delete(convidado);
            attributes.addFlashAttribute("mensagem", "Convidado excluído com sucesso!");
            return "redirect:/" + eventoId; // redireciona para a página do evento
        } else {
            attributes.addFlashAttribute("mensagem", "Convidado não encontrado.");
            return "redirect:/eventos";
        }
    }

    @RequestMapping(value = "/editar-evento/{id}", method = RequestMethod.GET)
    public ModelAndView editarEvento(@PathVariable("id") long id) {
        Evento evento = er.findById(id).orElse(null);
        ModelAndView mv = new ModelAndView("evento/form-evento-edit");
        mv.addObject("evento", evento);
        return mv;
    }

    @RequestMapping(value = "/atualizar-evento", method = RequestMethod.POST)
    public String atualizarEvento(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/editar-evento/" + evento.getId();
        }

        er.save(evento); // Atualiza o evento
        attributes.addFlashAttribute("mensagem", "Evento atualizado com sucesso!");
        return "redirect:/eventos";
    }

    @RequestMapping(value = "/editar-convidado/{rg}", method = RequestMethod.GET)
    public ModelAndView editarConvidado(@PathVariable("rg") String rg) {
        Convidado convidado = cr.findByRg(rg);
        ModelAndView mv = new ModelAndView("evento/convidado-edit");
        mv.addObject("convidado", convidado);
        mv.addObject("eventoId", convidado.getEvento().getId());
        return mv;
    }

    @RequestMapping(value = "/atualizar-convidado", method = RequestMethod.POST)
    public String atualizarConvidado(@Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/editar-convidado/" + convidado.getRg();
        }

        cr.save(convidado);
        attributes.addFlashAttribute("mensagem", "Convidado atualizado com sucesso!");
        return "redirect:/" + convidado.getEvento().getId();
    }
}
