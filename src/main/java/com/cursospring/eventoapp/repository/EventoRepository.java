package com.cursospring.eventoapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.cursospring.eventoapp.models.Evento;

public interface EventoRepository extends CrudRepository<Evento, Long> {

    
} 