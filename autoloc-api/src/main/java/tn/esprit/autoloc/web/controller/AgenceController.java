package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceRequestDto;
import tn.esprit.autoloc.web.dto.AgenceResponseDto;
import tn.esprit.autoloc.web.mapper.AgenceMapper;

import java.util.List;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceService agenceService;
    private final AgenceMapper agenceMapper;

    @PostMapping
    public ResponseEntity<AgenceResponseDto> create(
            @Valid @RequestBody AgenceRequestDto request) {

        Agence agence = agenceMapper.toEntity(request);
        Agence created = agenceService.create(agence);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(agenceMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenceResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                agenceMapper.toResponse(agenceService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<AgenceResponseDto>> getAll() {

        List<AgenceResponseDto> agences = agenceMapper.toResponseList(agenceService.getAll());

        return ResponseEntity.ok(agences);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgenceResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AgenceRequestDto request) {

        Agence agence = agenceMapper.toEntity(request);
        Agence updated = agenceService.update(id, agence);

        return ResponseEntity.ok(agenceMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        agenceService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
