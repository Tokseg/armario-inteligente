package br.com.unit.tokseg.armario_inteligente.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.unit.tokseg.armario_inteligente.model.RegistroAuditoria;
import br.com.unit.tokseg.armario_inteligente.repository.RegistroAuditoriaRepository;

@Service
public class RegistroAuditoriaService {
    @Autowired
    private RegistroAuditoriaRepository repository;

    public List<RegistroAuditoria> findAll() {
        return repository.findAll();
    }

    public Optional<RegistroAuditoria> findById(Long id) {
        return repository.findById(id);
    }

    public RegistroAuditoria save(RegistroAuditoria registro) {
        return repository.save(registro);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
} 