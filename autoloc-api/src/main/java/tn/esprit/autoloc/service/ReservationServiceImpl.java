package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;

    @Override
    public Reservation create(Reservation reservation) {
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle réservation ne doit pas avoir d'identifiant");
        }

        validateReservation(reservation);
        checkOverlap(reservation, null);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation", id));
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        Reservation existing = getById(id);
        validateReservation(reservation);
        checkOverlap(reservation, id);

        existing.setDateDebut(reservation.getDateDebut());
        existing.setDateFin(reservation.getDateFin());
        existing.setStatut(reservation.getStatut());
        existing.setVehicule(reservation.getVehicule());
        existing.setClient(reservation.getClient());

        return reservationRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Reservation existing = getById(id);
        reservationRepository.delete(existing);
    }

    private void validateReservation(Reservation reservation) {
        if (reservation.getDateDebut() == null
                || reservation.getDateFin() == null) {
            throw new IllegalArgumentException(
                    "Les dates de début et de fin sont obligatoires");
        }

        if (reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException(
                    "La date de fin ne peut pas être antérieure à la date de début");
        }
    }

    private void checkOverlap(Reservation reservation, Long excludeId) {
        if (reservation.getStatut() != StatutReservation.ANNULEE
                && reservation.getVehicule() != null
                && reservation.getVehicule().getIdVehicule() != null) {
            boolean overlap = reservationRepository.hasOverlappingReservations(
                    reservation.getVehicule().getIdVehicule(),
                    reservation.getDateDebut(),
                    reservation.getDateFin(),
                    excludeId
            );
            if (overlap) {
                throw new IllegalStateException("Le véhicule est déjà réservé sur cette période");
            }
        }
    }
}
