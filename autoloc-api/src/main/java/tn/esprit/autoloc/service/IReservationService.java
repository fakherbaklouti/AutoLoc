package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationResponseDto;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDto;

import java.util.List;

public interface IReservationService {

    Reservation create(Reservation reservation);

    Reservation getById(Long id);

    List<Reservation> getAll();

    Reservation update(Long id, Reservation reservation);

    void delete(Long id);

    ReservationResponseDto affecterReservationAClient(
            Long idReservation,
            String nom,
            String prenom
    );

    ReservationResponseDto creerReservationEtContratAssocie(
            ReservationWithContratRequestDto requestDTO
    );
}
