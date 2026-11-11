package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeRequestDto;
import tn.esprit.autoloc.web.dto.VehiculeResponseDto;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;
    private final VehiculeMapper vehiculeMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeResponseDto create(
            @Valid @RequestBody VehiculeRequestDto dto) {

        Vehicule vehicule = prepareEntity(dto);
        return vehiculeMapper.toResponse(vehiculeService.create(vehicule));
    }

    @GetMapping("/{id}")
    public VehiculeResponseDto getById(@PathVariable Long id) {
        return vehiculeMapper.toResponse(vehiculeService.getById(id));
    }

    @GetMapping
    public List<VehiculeResponseDto> getAll() {
        return vehiculeMapper.toResponseList(vehiculeService.getAll());
    }

    @PutMapping("/{id}")
    public VehiculeResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody VehiculeRequestDto dto) {

        Vehicule vehicule = prepareEntity(dto);
        return vehiculeMapper.toResponse(vehiculeService.update(id, vehicule));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehiculeService.delete(id);
    }

    private Vehicule prepareEntity(VehiculeRequestDto dto) {
        Agence agence = agenceRepository.findById(dto.getIdAgence())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agence", dto.getIdAgence()));

        List<Equipement> equipements = dto.getIdEquipements() == null
                ? List.of()
                : equipementRepository.findAllById(dto.getIdEquipements());

        if (dto.getIdEquipements() != null
                && equipements.size() != dto.getIdEquipements().stream().distinct().count()) {
            throw new ResourceNotFoundException("Un ou plusieurs équipements sont introuvables");
        }

        return vehiculeMapper.toEntity(dto, agence, equipements);
    }
}
