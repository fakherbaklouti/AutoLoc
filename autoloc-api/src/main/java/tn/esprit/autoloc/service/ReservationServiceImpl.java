package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;

    @Override
    public Reservation create(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable avec l'id : " + id));
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        Reservation existing = getById(id);
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
}
