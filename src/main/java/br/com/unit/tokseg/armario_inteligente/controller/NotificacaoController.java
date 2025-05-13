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

import br.com.unit.tokseg.armario_inteligente.model.Notificacao;
import br.com.unit.tokseg.armario_inteligente.service.NotificacaoService;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {
    @Autowired
    private NotificacaoService service;

    @GetMapping
    public List<Notificacao> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Notificacao> getById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public Notificacao create(@RequestBody Notificacao notificacao) {
        return service.save(notificacao);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteById(id);
    }
} 