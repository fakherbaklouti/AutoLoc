package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDto;
import tn.esprit.autoloc.web.dto.ReservationResponseDto;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDto;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationMapper reservationMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto create(
            @Valid @RequestBody ReservationRequestDto dto) {

        Client client = findClient(dto.getIdClient());
        Vehicule vehicule = findVehicule(dto.getIdVehicule());
        Reservation reservation = reservationMapper.toEntity(dto, client, vehicule);

        return reservationMapper.toResponse(reservationService.create(reservation));
    }

    @GetMapping("/{id}")
    public ReservationResponseDto getById(@PathVariable Long id) {
        return reservationMapper.toResponse(reservationService.getById(id));
    }

    @GetMapping
    public List<ReservationResponseDto> getAll() {
        return reservationMapper.toResponseList(reservationService.getAll());
    }

    @PutMapping("/{id}")
    public ReservationResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequestDto dto) {

        Client client = findClient(dto.getIdClient());
        Vehicule vehicule = findVehicule(dto.getIdVehicule());
        Reservation reservation = reservationMapper.toEntity(dto, client, vehicule);

        return reservationMapper.toResponse(reservationService.update(id, reservation));
    }

    @PutMapping("/{id}/affecter-client")
    public ReservationResponseDto affecterReservationAClient(
            @PathVariable Long id,
            @RequestParam String nom,
            @RequestParam String prenom) {

        return reservationService.affecterReservationAClient(id, nom, prenom);
    }

    @PostMapping("/avec-contrat")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto creerReservationEtContratAssocie(
            @Valid @RequestBody ReservationWithContratRequestDto requestDTO) {

        return reservationService.creerReservationEtContratAssocie(requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reservationService.delete(id);
    }

    private Client findClient(Long idClient) {
        return clientRepository.findById(idClient)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client", idClient));
    }

    private Vehicule findVehicule(Long idVehicule) {
        return vehiculeRepository.findById(idVehicule)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Véhicule", idVehicule));
    }
}
