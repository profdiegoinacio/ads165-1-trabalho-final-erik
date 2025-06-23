package com.example.backend.service;

import com.example.backend.domain.AreaDeAtuacao;
import com.example.backend.dto.AreaDeAtuacaoRequestDTO;
import com.example.backend.dto.AreaDeAtuacaoResponseDTO;
import com.example.backend.repository.AreaDeAtuacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaDeAtuacaoService {

    private final AreaDeAtuacaoRepository repository;

    // Injeção de dependência via construtor (ótima prática)
    @Autowired
    public AreaDeAtuacaoService(AreaDeAtuacaoRepository areaDeAtuacaoRepository) {
        this.repository = areaDeAtuacaoRepository;
    }

    @Transactional(readOnly = true)
    public List<AreaDeAtuacaoResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AreaDeAtuacaoResponseDTO buscarPorId(Long id) {
        AreaDeAtuacao area = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área de Atuação não encontrada com o ID: " + id));
        return converterParaResponseDTO(area);
    }

    @Transactional
    public AreaDeAtuacaoResponseDTO criar(AreaDeAtuacaoRequestDTO requestDTO) {
        AreaDeAtuacao novaArea = new AreaDeAtuacao();
        novaArea.setNome(requestDTO.nome());

        AreaDeAtuacao areaSalva = repository.save(novaArea);

        return converterParaResponseDTO(areaSalva);
    }

    @Transactional
    public AreaDeAtuacaoResponseDTO atualizar(Long id, AreaDeAtuacaoRequestDTO requestDTO) {
        AreaDeAtuacao areaExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área de Atuação não encontrada com o ID: " + id));

        areaExistente.setNome(requestDTO.nome());
        AreaDeAtuacao areaAtualizada = repository.save(areaExistente);

        return converterParaResponseDTO(areaAtualizada);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Área de Atuação não encontrada com o ID: " + id);
        }
        repository.deleteById(id);
    }

    private AreaDeAtuacaoResponseDTO converterParaResponseDTO(AreaDeAtuacao area) {
        return new AreaDeAtuacaoResponseDTO(area.getId(), area.getNome());
    }
}