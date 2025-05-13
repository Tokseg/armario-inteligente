package br.com.unit.tokseg.armario_inteligente.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unit.tokseg.armario_inteligente.model.Compartimento;
import br.com.unit.tokseg.armario_inteligente.service.CompartimentoService;

@RestController
@RequestMapping("/api/compartimentos")
public class CompartimentoController {
    @Autowired
    private CompartimentoService service;

    @GetMapping
    public List<Compartimento> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Compartimento> getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Compartimento create(@RequestBody Compartimento compartimento) {
        return service.save(compartimento);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
} 