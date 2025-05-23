package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.Armario;
import br.com.unit.tokseg.armario_inteligente.model.ArmarioStatus;
import br.com.unit.tokseg.armario_inteligente.repository.ArmarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócios relacionada aos armários.
 * Implementa as operações de CRUD e regras específicas do domínio.
 * 
 * Este serviço é responsável por:
 * - Validar operações antes de persistir no banco
 * - Implementar regras de negócio específicas
 * - Coordenar operações entre diferentes repositórios
 * - Fornecer uma interface limpa para os controllers
 */
@Service
public class ArmarioService {

    private final ArmarioRepository armarioRepository;

    /**
     * Construtor que recebe o repositório de armários via injeção de dependência.
     * O Spring Boot gerencia automaticamente a criação e injeção do repositório.
     */
    public ArmarioService(ArmarioRepository armarioRepository) {
        this.armarioRepository = armarioRepository;
    }

    /**
     * Verifica se já existe um armário com o número especificado.
     * Usado para evitar duplicidade de números de armário.
     * 
     * @param numero Número do armário a ser verificado
     * @return true se o número já existe, false caso contrário
     */
    public boolean existeNumero(String numero) {
        return armarioRepository.existsByNumero(numero);
    }

    /**
     * Salva um novo armário no sistema.
     * O parâmetro emailUsuario é usado para auditoria, registrando quem fez a operação.
     * 
     * @param armario Armário a ser salvo
     * @param emailUsuario Email do usuário que está realizando a operação
     * @return Armário salvo com ID gerado
     */
    public Armario salvar(Armario armario, String emailUsuario) {
        return armarioRepository.save(armario);
    }

    /**
     * Lista todos os armários cadastrados no sistema.
     * 
     * @return Lista de todos os armários
     */
    public List<Armario> listarTodos() {
        return armarioRepository.findAll();
    }

    /**
     * Busca armários por status.
     * Útil para encontrar armários disponíveis, ocupados ou em manutenção.
     * 
     * @param status Status dos armários a serem buscados
     * @return Lista de armários com o status especificado
     */
    public List<Armario> buscarPorStatus(ArmarioStatus status) {
        return armarioRepository.findByStatus(status);
    }

    /**
     * Busca armários por localização.
     * Permite encontrar armários em uma área específica.
     * 
     * @param localizacao Localização dos armários
     * @return Lista de armários na localização especificada
     */
    public List<Armario> buscarPorLocalizacao(String localizacao) {
        return armarioRepository.findByLocalizacao(localizacao);
    }

    /**
     * Busca armários combinando status e localização.
     * Permite filtrar armários por ambos os critérios simultaneamente.
     * 
     * @param status Status dos armários
     * @param localizacao Localização dos armários
     * @return Lista de armários que atendem aos dois critérios
     */
    public List<Armario> buscarPorStatusELocalizacao(ArmarioStatus status, String localizacao) {
        return armarioRepository.findByStatusAndLocalizacao(status, localizacao);
    }

    /**
     * Atualiza o status de um armário específico.
     * Se o armário não for encontrado, retorna Optional vazio.
     * 
     * @param id ID do armário a ser atualizado
     * @param status Novo status do armário
     * @return Optional contendo o armário atualizado, ou vazio se não encontrado
     */
    public Optional<Armario> atualizarStatus(Long id, ArmarioStatus status) {
        return armarioRepository.findById(id)
                .map(armario -> {
                    armario.setStatus(status);
                    return armarioRepository.save(armario);
                });
    }

    /**
     * Busca um armário específico pelo ID.
     * 
     * @param id ID do armário a ser buscado
     * @return Optional contendo o armário encontrado, ou vazio se não existir
     */
    public Optional<Armario> buscarPorId(Long id) {
        return armarioRepository.findById(id);
    }

    /**
     * Conta quantos armários existem com um determinado status.
     * Útil para estatísticas e monitoramento do sistema.
     * 
     * @param status Status a ser contado
     * @return Quantidade de armários com o status especificado
     */
    public long contarPorStatus(ArmarioStatus status) {
        return armarioRepository.countByStatus(status);
    }
} 