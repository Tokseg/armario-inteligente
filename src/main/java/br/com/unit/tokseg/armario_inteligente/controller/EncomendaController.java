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

import br.com.unit.tokseg.armario_inteligente.model.Encomenda;
import br.com.unit.tokseg.armario_inteligente.service.EncomendaService;

@RestController
@RequestMapping("/api/encomendas")
public class EncomendaController {
    @Autowired
    private EncomendaService service;

    @GetMapping
    public List<Encomenda> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Encomenda> getById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public Encomenda create(@RequestBody Encomenda encomenda) {
        return service.save(encomenda);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteById(id);
    }
} 