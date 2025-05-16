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

import br.com.unit.tokseg.armario_inteligente.model.RegistroAuditoria;
import br.com.unit.tokseg.armario_inteligente.service.RegistroAuditoriaService;

@RestController
@RequestMapping("/api/auditoria")
public class RegistroAuditoriaController {
    @Autowired
    private RegistroAuditoriaService service;

    @GetMapping
    public List<RegistroAuditoria> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<RegistroAuditoria> getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public RegistroAuditoria create(@RequestBody RegistroAuditoria registro) {
        return service.save(registro);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
