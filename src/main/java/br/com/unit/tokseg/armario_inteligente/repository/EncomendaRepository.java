package br.com.unit.tokseg.armario_inteligente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unit.tokseg.armario_inteligente.model.Encomenda;

public interface EncomendaRepository extends JpaRepository<Encomenda, String> {
} 