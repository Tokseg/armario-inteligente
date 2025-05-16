package br.com.unit.tokseg.armario_inteligente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unit.tokseg.armario_inteligente.model.Compartimento;

public interface CompartimentoRepository extends JpaRepository<Compartimento, Long> {
} 