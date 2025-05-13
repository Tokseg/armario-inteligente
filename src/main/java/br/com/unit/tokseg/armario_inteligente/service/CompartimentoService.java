package br.com.unit.tokseg.armario_inteligente.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.unit.tokseg.armario_inteligente.model.Compartimento;
import br.com.unit.tokseg.armario_inteligente.repository.CompartimentoRepository;

@Service
public class CompartimentoService {
    @Autowired
    private CompartimentoRepository repository;

    public List<Compartimento> findAll() {
        return repository.findAll();
    }

    public Optional<Compartimento> findById(Long id) {
        return repository.findById(id);
    }

    public Compartimento save(Compartimento compartimento) {
        return repository.save(compartimento);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
} 