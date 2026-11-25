package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.autoloc.domain.Reservation;

import java.time.LocalDate;
import java.util.Optional;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
           "WHERE r.vehicule.idVehicule = :idVehicule " +
           "AND (:excludeId IS NULL OR r.idReservation <> :excludeId) " +
           "AND r.statut <> tn.esprit.autoloc.domain.StatutReservation.ANNULEE " +
           "AND r.dateDebut <= :dateFin " +
           "AND r.dateFin >= :dateDebut")
    boolean hasOverlappingReservations(
            @Param("idVehicule") Long idVehicule,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            @Param("excludeId") Long excludeId);

    Optional<Reservation> findByDateDebutAndDateFin(
            LocalDate dateDebut,
            LocalDate dateFin
    );
}
