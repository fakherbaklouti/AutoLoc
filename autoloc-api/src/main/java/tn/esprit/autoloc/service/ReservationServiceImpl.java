package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.ReservationResponseDto;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDto;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final ReservationMapper reservationMapper;
    private final IVehiculeRepository vehiculeRepository;
    private final IContratRepository contratRepository;

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

    @Override
    public ReservationResponseDto affecterReservationAClient(
            Long idReservation, String nom, String prenom) {

        Reservation reservation = getById(idReservation);

        Client client = clientRepository.findByNomAndPrenom(nom, prenom)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client introuvable : " + nom + " " + prenom));

        reservation.setClient(client);

        Reservation saved = reservationRepository.save(reservation);

        return reservationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReservationResponseDto creerReservationEtContratAssocie(
            ReservationWithContratRequestDto requestDTO) {

        Client client = clientRepository.findById(requestDTO.getIdClient())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client", requestDTO.getIdClient()));

        Vehicule vehicule = vehiculeRepository.findById(requestDTO.getIdVehicule())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicule", requestDTO.getIdVehicule()));

        Reservation reservation = new Reservation();
        reservation.setDateDebut(requestDTO.getDateDebut());
        reservation.setDateFin(requestDTO.getDateFin());
        reservation.setStatut(requestDTO.getStatut());
        reservation.setClient(client);
        reservation.setVehicule(vehicule);

        Reservation savedReservation = create(reservation);

        Contrat contrat = new Contrat();
        contrat.setDateSignature(requestDTO.getDateSignature());
        contrat.setMontantTotal(requestDTO.getMontantTotal());
        contrat.setValide(requestDTO.getValide());
        contrat.setReservation(savedReservation);

        if (contrat.getMontantTotal() == null
                || contrat.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException(
                    "Le montant total doit être positif ou nul");
        }

        contratRepository.save(contrat);

        return reservationMapper.toResponse(savedReservation);
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
