package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeRequestDto;
import tn.esprit.autoloc.web.dto.EmployeResponseDto;
import tn.esprit.autoloc.web.mapper.EmployeMapper;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final IEmployeService employeService;
    private final IAgenceRepository agenceRepository;
    private final EmployeMapper employeMapper;

    @PostMapping
    public ResponseEntity<EmployeResponseDto> create(
            @Valid @RequestBody EmployeRequestDto request) {

        Agence agence = findAgence(request.getIdAgence());
        Employe employe = employeMapper.toEntity(request, agence);

        Employe created = employeService.create(employe);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeMapper.toResponse(employeService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<EmployeResponseDto>> getAll() {

        return ResponseEntity.ok(employeMapper.toResponseList(employeService.getAll()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeRequestDto request) {

        Agence agence = findAgence(request.getIdAgence());
        Employe employe = employeMapper.toEntity(request, agence);

        Employe updated = employeService.update(id, employe);

        return ResponseEntity.ok(employeMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Agence findAgence(Long idAgence) {
        return agenceRepository.findById(idAgence)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agence", idAgence));
    }
}
