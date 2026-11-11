package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ContratRequestDto;
import tn.esprit.autoloc.web.dto.ContratResponseDto;

import java.util.List;

@Component
public class ContratMapper {

    public Contrat toEntity(ContratRequestDto request, Reservation reservation) {
        if (request == null) {
            return null;
        }
        Contrat contrat = new Contrat();
        contrat.setDateSignature(request.getDateSignature());
        contrat.setMontantTotal(request.getMontantTotal());
        contrat.setValide(request.getValide());
        contrat.setReservation(reservation);
        return contrat;
    }

    public ContratResponseDto toResponse(Contrat contrat) {
        if (contrat == null) {
            return null;
        }
        Long idReservation = contrat.getReservation() != null ? contrat.getReservation().getIdReservation() : null;
        return new ContratResponseDto(
                contrat.getIdContrat(),
                contrat.getDateSignature(),
                contrat.getMontantTotal(),
                contrat.getValide(),
                idReservation
        );
    }

    public List<ContratResponseDto> toResponseList(List<Contrat> contrats) {
        if (contrats == null) {
            return List.of();
        }
        return contrats.stream().map(this::toResponse).toList();
    }
}
