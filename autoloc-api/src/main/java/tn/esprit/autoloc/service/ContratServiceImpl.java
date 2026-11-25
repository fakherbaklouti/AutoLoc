
package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.web.dto.ContratResponseDto;
import tn.esprit.autoloc.web.mapper.ContratMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;

    @Override
    public Contrat create(Contrat contrat) {
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau contrat ne doit pas avoir d'identifiant");
        }

        validateMontant(contrat);

        Long idReservation = contrat.getReservation().getIdReservation();

        if (contratRepository.existsByReservation_IdReservation(idReservation)) {
            throw new IllegalStateException(
                    "Cette réservation possède déjà un contrat");
        }

        return contratRepository.save(contrat);
    }

    @Override
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrat", id));
    }

    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        Contrat existing = findById(id);
        validateMontant(contrat);

        Long idReservation = contrat.getReservation().getIdReservation();

        if (contratRepository.existsByReservation_IdReservationAndIdContratNot(
                idReservation, id)) {
            throw new IllegalStateException(
                    "Cette réservation possède déjà un autre contrat");
        }

        existing.setDateSignature(contrat.getDateSignature());
        existing.setMontantTotal(contrat.getMontantTotal());
        existing.setValide(contrat.getValide());
        existing.setReservation(contrat.getReservation());

        return contratRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        Contrat existing = findById(id);
        contratRepository.delete(existing);
    }

    @Override
    public ContratResponseDto affecterContratAReservation(
            LocalDate dateSignature,
            LocalDate dateDebut,
            LocalDate dateFin) {

        if (dateSignature == null || dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Toutes les dates sont obligatoires");
        }

        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException(
                    "La date de fin ne peut pas être antérieure à la date de début");
        }

        Reservation reservation = reservationRepository
                .findByDateDebutAndDateFin(dateDebut, dateFin)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Aucune réservation trouvée pour ces dates"));

        long nombreJours = ChronoUnit.DAYS.between(dateDebut, dateFin);

        BigDecimal montantTotal = reservation.getVehicule()
                .getTarifJournalier()
                .multiply(BigDecimal.valueOf(nombreJours));

        Contrat contrat = new Contrat();
        contrat.setDateSignature(dateSignature);
        contrat.setMontantTotal(montantTotal);
        contrat.setValide(false);
        contrat.setReservation(reservation);

        Contrat saved = create(contrat);

        return contratMapper.toResponse(saved);
    }

    private void validateMontant(Contrat contrat) {
        if (contrat.getMontantTotal() != null
                && contrat.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException(
                    "Le montant total du contrat ne peut pas être négatif");
        }
    }
}
