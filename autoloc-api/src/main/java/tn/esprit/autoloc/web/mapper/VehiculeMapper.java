package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeRequestDto;
import tn.esprit.autoloc.web.dto.VehiculeResponseDto;

import java.util.List;

@Component
public class VehiculeMapper {

    public Vehicule toEntity(VehiculeRequestDto dto, Agence agence, List<Equipement> equipements) {
        if (dto == null) {
            return null;
        }
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(dto.getImmatriculation());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setCategorie(dto.getCategorie());
        vehicule.setTarifJournalier(dto.getTarifJournalier());
        vehicule.setStatut(dto.getStatut());
        vehicule.setAgence(agence);
        vehicule.setEquipements(equipements != null ? equipements : List.of());
        return vehicule;
    }

    public VehiculeResponseDto toResponse(Vehicule vehicule) {
        if (vehicule == null) {
            return null;
        }
        Long idAgence = vehicule.getAgence() != null ? vehicule.getAgence().getIdAgence() : null;
        List<Long> idEquipements = vehicule.getEquipements() == null
                ? List.of()
                : vehicule.getEquipements().stream()
                .map(Equipement::getIdEquipement)
                .toList();

        return new VehiculeResponseDto(
                vehicule.getIdVehicule(),
                vehicule.getImmatriculation(),
                vehicule.getMarque(),
                vehicule.getModele(),
                vehicule.getCategorie(),
                vehicule.getTarifJournalier(),
                vehicule.getStatut(),
                idAgence,
                idEquipements
        );
    }

    public List<VehiculeResponseDto> toResponseList(List<Vehicule> vehicules) {
        if (vehicules == null) {
            return List.of();
        }
        return vehicules.stream().map(this::toResponse).toList();
    }
}
