package com.example.backend.controller;

import com.example.backend.dto.AreaDeAtuacaoRequestDTO;
import com.example.backend.dto.AreaDeAtuacaoResponseDTO;
import com.example.backend.service.AreaDeAtuacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/areas-de-atuacao")
public class AreaDeAtuacaoController {

    @Autowired
    private AreaDeAtuacaoService areaDeAtuacaoService;

    @GetMapping
    public ResponseEntity<List<AreaDeAtuacaoResponseDTO>> listarTodasAreas() {
        List<AreaDeAtuacaoResponseDTO> areas = areaDeAtuacaoService.listarTodas();
        return ResponseEntity.ok(areas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AreaDeAtuacaoResponseDTO> buscarAreaPorId(@PathVariable Long id) {
        AreaDeAtuacaoResponseDTO area = areaDeAtuacaoService.buscarPorId(id);
        return ResponseEntity.ok(area);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AreaDeAtuacaoResponseDTO> criarArea(@Valid @RequestBody AreaDeAtuacaoRequestDTO requestDTO) {
        AreaDeAtuacaoResponseDTO novaArea = areaDeAtuacaoService.criar(requestDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaArea.id())
                .toUri();

        return ResponseEntity.created(location).body(novaArea);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AreaDeAtuacaoResponseDTO> atualizarArea(@PathVariable Long id, @Valid @RequestBody AreaDeAtuacaoRequestDTO requestDTO) {
        AreaDeAtuacaoResponseDTO areaAtualizada = areaDeAtuacaoService.atualizar(id, requestDTO);
        return ResponseEntity.ok(areaAtualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarArea(@PathVariable Long id) {
        areaDeAtuacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}