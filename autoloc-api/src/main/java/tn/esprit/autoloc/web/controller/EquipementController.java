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

import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@RequiredArgsConstructor
public class EquipementController {

    private final IEquipementService equipementService;

    @PostMapping
    public ResponseEntity<EquipementResponseDto> create(@Valid @RequestBody EquipementRequestDto request) {
        Equipement created = equipementService.create(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipementResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(equipementService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<EquipementResponseDto>> getAll() {
        List<EquipementResponseDto> equipements = equipementService.getAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(equipements);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipementResponseDto> update(@PathVariable Long id,
                                                        @Valid @RequestBody EquipementRequestDto request) {
        Equipement updated = equipementService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Equipement toEntity(EquipementRequestDto request) {
        Equipement equipement = new Equipement();
        equipement.setLibelle(request.getLibelle());
        return equipement;
    }

    private EquipementResponseDto toResponse(Equipement equipement) {
        return new EquipementResponseDto(
                equipement.getIdEquipement(),
                equipement.getLibelle()
        );
    }
}
