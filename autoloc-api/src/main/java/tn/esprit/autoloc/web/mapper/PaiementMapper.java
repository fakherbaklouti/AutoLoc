package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.web.dto.PaiementResponseDto;

import java.util.List;

@Component
public class PaiementMapper {

    public PaiementResponseDto toResponse(Paiement paiement) {
        if (paiement == null) {
            return null;
        }
        Long idContrat = paiement.getContrat() != null ? paiement.getContrat().getIdContrat() : null;
        return new PaiementResponseDto(
                paiement.getIdPaiement(),
                paiement.getMontant(),
                paiement.getDatePaiement(),
                paiement.getModePaiement(),
                idContrat
        );
    }

    public List<PaiementResponseDto> toResponseList(List<Paiement> paiements) {
        if (paiements == null) {
            return List.of();
        }
        return paiements.stream().map(this::toResponse).toList();
    }
}
