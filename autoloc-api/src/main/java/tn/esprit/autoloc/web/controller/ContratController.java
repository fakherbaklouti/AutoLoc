package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratRequestDto;
import tn.esprit.autoloc.web.dto.ContratResponseDto;
import tn.esprit.autoloc.web.mapper.ContratMapper;

import java.util.List;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
public class ContratController {

    private final IContratService contratService;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;

    @PostMapping
    public ResponseEntity<ContratResponseDto> create(
            @Valid @RequestBody ContratRequestDto request) {

        Reservation reservation = findReservation(request.getIdReservation());
        Contrat contrat = contratMapper.toEntity(request, reservation);
        Contrat created = contratService.create(contrat);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                contratMapper.toResponse(contratService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<ContratResponseDto>> getAll() {

        return ResponseEntity.ok(contratMapper.toResponseList(contratService.findAll()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ContratRequestDto request) {

        Reservation reservation = findReservation(request.getIdReservation());
        Contrat contrat = contratMapper.toEntity(request, reservation);
        Contrat updated = contratService.update(id, contrat);

        return ResponseEntity.ok(contratMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Reservation findReservation(Long idReservation) {
        return reservationRepository.findById(idReservation)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Réservation", idReservation));
    }
}
