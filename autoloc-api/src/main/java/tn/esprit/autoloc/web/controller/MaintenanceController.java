package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IMaintenanceService;
import tn.esprit.autoloc.web.dto.MaintenanceRequestDto;
import tn.esprit.autoloc.web.dto.MaintenanceResponseDto;
import tn.esprit.autoloc.web.mapper.MaintenanceMapper;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;
    private final IVehiculeRepository vehiculeRepository;
    private final MaintenanceMapper maintenanceMapper;

    @PostMapping
    public ResponseEntity<MaintenanceResponseDto> create(
            @Valid @RequestBody MaintenanceRequestDto request) {

        Vehicule vehicule = findVehicule(request.getIdVehicule());
        Maintenance maintenance = maintenanceMapper.toEntity(request, vehicule);

        Maintenance created = maintenanceService.create(maintenance);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(maintenanceMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                maintenanceMapper.toResponse(maintenanceService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceResponseDto>> getAll() {

        return ResponseEntity.ok(maintenanceMapper.toResponseList(maintenanceService.getAll()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceRequestDto request) {

        Vehicule vehicule = findVehicule(request.getIdVehicule());
        Maintenance maintenance = maintenanceMapper.toEntity(request, vehicule);

        Maintenance updated = maintenanceService.update(id, maintenance);

        return ResponseEntity.ok(maintenanceMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Vehicule findVehicule(Long idVehicule) {
        return vehiculeRepository.findById(idVehicule)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Véhicule", idVehicule));
    }
}
