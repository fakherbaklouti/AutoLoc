package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.ReservationResponseDto;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDto;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private IReservationRepository reservationRepository;

    @Mock
    private IClientRepository clientRepository;

    @Mock
    private IVehiculeRepository vehiculeRepository;

    @Mock
    private IContratRepository contratRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Vehicule vehicule;
    private Client client;

    @BeforeEach
    void setUp() {
        vehicule = new Vehicule();
        vehicule.setIdVehicule(1L);

        client = new Client();
        client.setIdClient(1L);
    }

    @Test
    @DisplayName("Dates incohérentes : dateFin avant dateDebut -> IllegalArgumentException (400)")
    void testIncoherentDates() {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2026, 6, 10));
        reservation.setDateFin(LocalDate.of(2026, 6, 5));
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        reservation.setVehicule(vehicule);
        reservation.setClient(client);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                reservationService.create(reservation));

        assertEquals("La date de fin ne peut pas être antérieure à la date de début", ex.getMessage());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deux réservations qui se chevauchent : rejet IllegalStateException (409)")
    void testOverlappingReservationCreateThrowsConflict() {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2026, 6, 1));
        reservation.setDateFin(LocalDate.of(2026, 6, 10));
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setVehicule(vehicule);
        reservation.setClient(client);

        when(reservationRepository.hasOverlappingReservations(
                eq(1L),
                eq(LocalDate.of(2026, 6, 1)),
                eq(LocalDate.of(2026, 6, 10)),
                eq(null)
        )).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                reservationService.create(reservation));

        assertEquals("Le véhicule est déjà réservé sur cette période", ex.getMessage());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deux réservations sur des périodes distinctes : acceptées")
    void testDistinctPeriodsAccepted() {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2026, 6, 1));
        reservation.setDateFin(LocalDate.of(2026, 6, 10));
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        reservation.setVehicule(vehicule);
        reservation.setClient(client);

        when(reservationRepository.hasOverlappingReservations(
                eq(1L),
                eq(LocalDate.of(2026, 6, 1)),
                eq(LocalDate.of(2026, 6, 10)),
                eq(null)
        )).thenReturn(false);

        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation created = reservationService.create(reservation);
        assertNotNull(created);
        verify(reservationRepository).save(reservation);
    }

    @Test
    @DisplayName("Réservation annulée : ne bloque pas la création")
    void testCancelledReservationDoesNotBlock() {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2026, 6, 1));
        reservation.setDateFin(LocalDate.of(2026, 6, 10));
        reservation.setStatut(StatutReservation.ANNULEE);
        reservation.setVehicule(vehicule);
        reservation.setClient(client);

        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation created = reservationService.create(reservation);
        assertNotNull(created);
        verify(reservationRepository, never()).hasOverlappingReservations(any(), any(), any(), any());
        verify(reservationRepository).save(reservation);
    }

    @Test
    @DisplayName("Modification d'une réservation sans changement de période : acceptée (exclusion id)")
    void testUpdateWithoutPeriodChangeAccepted() {
        Long reservationId = 5L;
        Reservation existing = new Reservation();
        existing.setIdReservation(reservationId);
        existing.setDateDebut(LocalDate.of(2026, 6, 1));
        existing.setDateFin(LocalDate.of(2026, 6, 10));
        existing.setStatut(StatutReservation.CONFIRMEE);
        existing.setVehicule(vehicule);
        existing.setClient(client);

        Reservation updateData = new Reservation();
        updateData.setDateDebut(LocalDate.of(2026, 6, 1));
        updateData.setDateFin(LocalDate.of(2026, 6, 10));
        updateData.setStatut(StatutReservation.CONFIRMEE);
        updateData.setVehicule(vehicule);
        updateData.setClient(client);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existing));
        when(reservationRepository.hasOverlappingReservations(
                eq(1L),
                eq(LocalDate.of(2026, 6, 1)),
                eq(LocalDate.of(2026, 6, 10)),
                eq(reservationId)
        )).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation updated = reservationService.update(reservationId, updateData);
        assertNotNull(updated);
        verify(reservationRepository).save(existing);
    }

    @Test
    @DisplayName("Modification vers une période déjà occupée : rejet IllegalStateException (409)")
    void testUpdateToOccupiedPeriodThrowsConflict() {
        Long reservationId = 5L;
        Reservation existing = new Reservation();
        existing.setIdReservation(reservationId);
        existing.setDateDebut(LocalDate.of(2026, 6, 1));
        existing.setDateFin(LocalDate.of(2026, 6, 10));
        existing.setStatut(StatutReservation.CONFIRMEE);
        existing.setVehicule(vehicule);
        existing.setClient(client);

        Reservation updateData = new Reservation();
        updateData.setDateDebut(LocalDate.of(2026, 7, 1));
        updateData.setDateFin(LocalDate.of(2026, 7, 10));
        updateData.setStatut(StatutReservation.CONFIRMEE);
        updateData.setVehicule(vehicule);
        updateData.setClient(client);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existing));
        when(reservationRepository.hasOverlappingReservations(
                eq(1L),
                eq(LocalDate.of(2026, 7, 1)),
                eq(LocalDate.of(2026, 7, 10)),
                eq(reservationId)
        )).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                reservationService.update(reservationId, updateData));

        assertEquals("Le véhicule est déjà réservé sur cette période", ex.getMessage());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Crée la réservation et son contrat associé puis retourne le DTO")
    void creerReservationEtContratAssocieCreeLesDeuxEtRetourneLeDto() {
        LocalDate dateDebut = LocalDate.of(2026, 6, 1);
        LocalDate dateFin = LocalDate.of(2026, 6, 10);
        ReservationWithContratRequestDto request = new ReservationWithContratRequestDto();
        request.setDateDebut(dateDebut);
        request.setDateFin(dateFin);
        request.setStatut(StatutReservation.CONFIRMEE);
        request.setIdVehicule(1L);
        request.setIdClient(1L);
        request.setDateSignature(LocalDate.of(2026, 5, 20));
        request.setMontantTotal(new java.math.BigDecimal("900.00"));
        request.setValide(true);
        ReservationResponseDto response = new ReservationResponseDto();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(vehiculeRepository.findById(1L)).thenReturn(Optional.of(vehicule));
        when(reservationRepository.hasOverlappingReservations(
                1L, dateDebut, dateFin, null)).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation saved = invocation.getArgument(0);
            saved.setIdReservation(10L);
            return saved;
        });
        when(contratRepository.save(any(Contrat.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(response);

        ReservationResponseDto result = reservationService.creerReservationEtContratAssocie(request);

        assertSame(response, result);
        org.mockito.ArgumentCaptor<Reservation> reservationCaptor =
                org.mockito.ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(reservationCaptor.capture());
        Reservation savedReservation = reservationCaptor.getValue();
        assertEquals(dateDebut, savedReservation.getDateDebut());
        assertEquals(dateFin, savedReservation.getDateFin());
        assertSame(client, savedReservation.getClient());
        assertSame(vehicule, savedReservation.getVehicule());

        org.mockito.ArgumentCaptor<Contrat> contratCaptor =
                org.mockito.ArgumentCaptor.forClass(Contrat.class);
        verify(contratRepository).save(contratCaptor.capture());
        Contrat savedContrat = contratCaptor.getValue();
        assertEquals(request.getDateSignature(), savedContrat.getDateSignature());
        assertEquals(request.getMontantTotal(), savedContrat.getMontantTotal());
        assertEquals(request.getValide(), savedContrat.getValide());
        assertSame(savedReservation, savedContrat.getReservation());
        verify(reservationMapper).toResponse(savedReservation);
    }
}
