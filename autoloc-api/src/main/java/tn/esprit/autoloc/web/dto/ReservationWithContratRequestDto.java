package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.autoloc.domain.StatutReservation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ReservationWithContratRequestDto {

    // Informations de réservation
    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    @NotNull
    private StatutReservation statut;

    @NotNull
    private Long idVehicule;

    @NotNull
    private Long idClient;

    // Informations du contrat
    @NotNull
    private LocalDate dateSignature;

    @NotNull
    @PositiveOrZero
    private BigDecimal montantTotal;

    @NotNull
    private Boolean valide;
}