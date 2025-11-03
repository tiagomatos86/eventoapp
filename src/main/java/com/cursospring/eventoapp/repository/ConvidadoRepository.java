package com.cursospring.eventoapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.cursospring.eventoapp.models.Convidado;
import com.cursospring.eventoapp.models.Evento;

public interface ConvidadoRepository extends CrudRepository<Convidado, String>{
    Iterable<Convidado> findByEvento(Evento evento);
    
}