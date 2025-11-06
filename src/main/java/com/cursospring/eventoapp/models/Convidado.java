package com.cursospring.eventoapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="convidados")
public class Convidado {

    @Id
    @NotBlank(message = "O RG não pode ser vazio")
    private String rg; //por ser único o RG é notado como id
    
    @NotBlank(message = "O Nome não pode ser vazio")
    private String nomeConvidado;

    @ManyToOne(optional = false)
    private Evento evento;

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }
    
    public String getNomeConvidado() {
        return nomeConvidado;
    }
    
    public void setNomeConvidado(String nomeConvidado) {
        this.nomeConvidado = nomeConvidado;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    public Evento getEvento() {
        return evento;
    }
}