package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.web.dto.EmployeRequestDto;
import tn.esprit.autoloc.web.dto.EmployeResponseDto;

import java.util.List;

@Component
public class EmployeMapper {

    public Employe toEntity(EmployeRequestDto request, Agence agence) {
        if (request == null) {
            return null;
        }
        Employe employe = new Employe();
        employe.setNom(request.getNom());
        employe.setPrenom(request.getPrenom());
        employe.setRole(request.getRole());
        employe.setAgence(agence);
        return employe;
    }

    public EmployeResponseDto toResponse(Employe employe) {
        if (employe == null) {
            return null;
        }
        Long idAgence = employe.getAgence() != null ? employe.getAgence().getIdAgence() : null;
        return new EmployeResponseDto(
                employe.getIdEmploye(),
                employe.getNom(),
                employe.getPrenom(),
                employe.getRole(),
                idAgence
        );
    }

    public List<EmployeResponseDto> toResponseList(List<Employe> employes) {
        if (employes == null) {
            return List.of();
        }
        return employes.stream().map(this::toResponse).toList();
    }
}
