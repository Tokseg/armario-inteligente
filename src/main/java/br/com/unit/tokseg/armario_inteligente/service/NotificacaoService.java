package br.com.unit.tokseg.armario_inteligente.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.unit.tokseg.armario_inteligente.model.Notificacao;
import br.com.unit.tokseg.armario_inteligente.repository.NotificacaoRepository;

@Service
public class NotificacaoService {
    @Autowired
    private NotificacaoRepository repository;

    public List<Notificacao> findAll() {
        return repository.findAll();
    }

    public Optional<Notificacao> findById(String id) {
        return repository.findById(id);
    }

    public Notificacao save(Notificacao notificacao) {
        return repository.save(notificacao);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
} 