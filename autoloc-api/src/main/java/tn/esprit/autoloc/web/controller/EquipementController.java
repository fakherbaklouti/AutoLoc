package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.web.dto.EquipementRequestDto;
import tn.esprit.autoloc.web.dto.EquipementResponseDto;
import tn.esprit.autoloc.web.mapper.EquipementMapper;

import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@RequiredArgsConstructor
public class EquipementController {

    private final IEquipementService equipementService;
    private final EquipementMapper equipementMapper;

    @PostMapping
    public ResponseEntity<EquipementResponseDto> create(@Valid @RequestBody EquipementRequestDto request) {
        Equipement created = equipementService.create(equipementMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(equipementMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipementResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(equipementMapper.toResponse(equipementService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<EquipementResponseDto>> getAll() {
        return ResponseEntity.ok(equipementMapper.toResponseList(equipementService.getAll()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipementResponseDto> update(@PathVariable Long id,
                                                        @Valid @RequestBody EquipementRequestDto request) {
        Equipement updated = equipementService.update(id, equipementMapper.toEntity(request));
        return ResponseEntity.ok(equipementMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
