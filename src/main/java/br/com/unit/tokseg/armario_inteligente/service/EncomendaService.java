package br.com.unit.tokseg.armario_inteligente.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.unit.tokseg.armario_inteligente.model.Encomenda;
import br.com.unit.tokseg.armario_inteligente.repository.EncomendaRepository;

@Service
public class EncomendaService {
    @Autowired
    private EncomendaRepository repository;

    public List<Encomenda> findAll() {
        return repository.findAll();
    }

    public Optional<Encomenda> findById(String id) {
        return repository.findById(id);
    }

    public Encomenda save(Encomenda encomenda) {
        return repository.save(encomenda);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
} 