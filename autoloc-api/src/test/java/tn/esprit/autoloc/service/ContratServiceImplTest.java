package tn.esprit.autoloc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.web.dto.ContratResponseDto;
import tn.esprit.autoloc.web.mapper.ContratMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContratServiceImplTest {

    @Mock
    private IContratRepository contratRepository;

    @Mock
    private IReservationRepository reservationRepository;

    @Mock
    private ContratMapper contratMapper;

    @InjectMocks
    private ContratServiceImpl contratService;

    @Test
    @DisplayName("Crée un contrat affecté à la réservation et retourne son DTO")
    void affecterContratAReservationCalculeLeMontantEtRetourneLeDto() {
        LocalDate dateSignature = LocalDate.of(2026, 6, 1);
        LocalDate dateDebut = LocalDate.of(2026, 6, 10);
        LocalDate dateFin = LocalDate.of(2026, 6, 15);
        Reservation reservation = new Reservation();
        reservation.setIdReservation(7L);
        Vehicule vehicule = new Vehicule();
        vehicule.setTarifJournalier(new BigDecimal("125.50"));
        reservation.setVehicule(vehicule);
        ContratResponseDto response = new ContratResponseDto();

        when(reservationRepository.findByDateDebutAndDateFin(dateDebut, dateFin))
                .thenReturn(Optional.of(reservation));
        when(contratRepository.save(any(Contrat.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(contratMapper.toResponse(any(Contrat.class))).thenReturn(response);

        ContratResponseDto result = contratService.affecterContratAReservation(
                dateSignature, dateDebut, dateFin);

        assertSame(response, result);
        org.mockito.ArgumentCaptor<Contrat> contratCaptor =
                org.mockito.ArgumentCaptor.forClass(Contrat.class);
        verify(contratRepository).save(contratCaptor.capture());
        verify(contratMapper).toResponse(any(Contrat.class));
        Contrat contrat = contratCaptor.getValue();
        assertEquals(dateSignature, contrat.getDateSignature());
        assertEquals(new BigDecimal("627.50"), contrat.getMontantTotal());
        assertEquals(Boolean.FALSE, contrat.getValide());
        assertSame(reservation, contrat.getReservation());
    }

    @Test
    @DisplayName("Rejette une date obligatoire manquante")
    void affecterContratAReservationRejetteLesDatesManquantes() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contratService.affecterContratAReservation(
                        null, LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 15)));

        assertEquals("Toutes les dates sont obligatoires", exception.getMessage());
        verify(reservationRepository, never()).findByDateDebutAndDateFin(any(), any());
        verify(contratRepository, never()).save(any());
    }

    @Test
    @DisplayName("Rejette une date de fin antérieure à la date de début")
    void affecterContratAReservationRejetteUnePeriodeInvalide() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contratService.affecterContratAReservation(
                        LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 15),
                        LocalDate.of(2026, 6, 10)));

        assertEquals("La date de fin ne peut pas être antérieure à la date de début",
                exception.getMessage());
        verify(reservationRepository, never()).findByDateDebutAndDateFin(any(), any());
        verify(contratRepository, never()).save(any());
    }

    @Test
    @DisplayName("Signale l'absence de réservation correspondant aux dates")
    void affecterContratAReservationRejetteUneReservationIntrouvable() {
        LocalDate dateDebut = LocalDate.of(2026, 6, 10);
        LocalDate dateFin = LocalDate.of(2026, 6, 15);
        when(reservationRepository.findByDateDebutAndDateFin(dateDebut, dateFin))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                contratService.affecterContratAReservation(
                        LocalDate.of(2026, 6, 1), dateDebut, dateFin));

        verify(contratRepository, never()).save(any());
    }
}