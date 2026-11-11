package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.MaintenanceRequestDto;
import tn.esprit.autoloc.web.dto.MaintenanceResponseDto;

import java.util.List;

@Component
public class MaintenanceMapper {

    public Maintenance toEntity(MaintenanceRequestDto request, Vehicule vehicule) {
        if (request == null) {
            return null;
        }
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(request.getDateDebut());
        maintenance.setDateFin(request.getDateFin());
        maintenance.setDescription(request.getDescription());
        maintenance.setVehicule(vehicule);
        return maintenance;
    }

    public MaintenanceResponseDto toResponse(Maintenance maintenance) {
        if (maintenance == null) {
            return null;
        }
        Long idVehicule = maintenance.getVehicule() != null ? maintenance.getVehicule().getIdVehicule() : null;
        return new MaintenanceResponseDto(
                maintenance.getIdMaintenance(),
                maintenance.getDateDebut(),
                maintenance.getDateFin(),
                maintenance.getDescription(),
                idVehicule
        );
    }

    public List<MaintenanceResponseDto> toResponseList(List<Maintenance> maintenances) {
        if (maintenances == null) {
            return List.of();
        }
        return maintenances.stream().map(this::toResponse).toList();
    }
}
