package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.ReservationRequestDto;
import tn.esprit.autoloc.web.dto.ReservationResponseDto;

import java.util.List;

@Component
public class ReservationMapper {

    public Reservation toEntity(ReservationRequestDto dto, Client client, Vehicule vehicule) {
        if (dto == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setStatut(dto.getStatut());
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        return reservation;
    }

    public ReservationResponseDto toResponse(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        Long idVehicule = reservation.getVehicule() != null ? reservation.getVehicule().getIdVehicule() : null;
        Long idClient = reservation.getClient() != null ? reservation.getClient().getIdClient() : null;
        return new ReservationResponseDto(
                reservation.getIdReservation(),
                reservation.getDateDebut(),
                reservation.getDateFin(),
                reservation.getStatut(),
                idVehicule,
                idClient
        );
    }

    public List<ReservationResponseDto> toResponseList(List<Reservation> reservations) {
        if (reservations == null) {
            return List.of();
        }
        return reservations.stream().map(this::toResponse).toList();
    }
}
