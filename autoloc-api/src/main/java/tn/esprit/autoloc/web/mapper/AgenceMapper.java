package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceRequestDto;
import tn.esprit.autoloc.web.dto.AgenceResponseDto;

import java.util.List;

@Component
public class AgenceMapper {

    public Agence toEntity(AgenceRequestDto request) {
        if (request == null) {
            return null;
        }
        Agence agence = new Agence();
        agence.setNom(request.getNom());
        agence.setVille(request.getVille());
        agence.setAdresse(request.getAdresse());
        agence.setTelephone(request.getTelephone());
        return agence;
    }

    public AgenceResponseDto toResponse(Agence agence) {
        if (agence == null) {
            return null;
        }
        return new AgenceResponseDto(
                agence.getIdAgence(),
                agence.getNom(),
                agence.getVille(),
                agence.getAdresse(),
                agence.getTelephone()
        );
    }

    public List<AgenceResponseDto> toResponseList(List<Agence> agences) {
        if (agences == null) {
            return List.of();
        }
        return agences.stream().map(this::toResponse).toList();
    }
}
