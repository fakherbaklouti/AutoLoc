package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.web.dto.EquipementRequestDto;
import tn.esprit.autoloc.web.dto.EquipementResponseDto;

import java.util.List;

@Component
public class EquipementMapper {

    public Equipement toEntity(EquipementRequestDto request) {
        if (request == null) {
            return null;
        }
        Equipement equipement = new Equipement();
        equipement.setLibelle(request.getLibelle());
        return equipement;
    }

    public EquipementResponseDto toResponse(Equipement equipement) {
        if (equipement == null) {
            return null;
        }
        return new EquipementResponseDto(
                equipement.getIdEquipement(),
                equipement.getLibelle()
        );
    }

    public List<EquipementResponseDto> toResponseList(List<Equipement> equipements) {
        if (equipements == null) {
            return List.of();
        }
        return equipements.stream().map(this::toResponse).toList();
    }
}
