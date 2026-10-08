package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;

    @Override
    public Maintenance create(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance getById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance introuvable avec l'id : " + id));
    }

    @Override
    public List<Maintenance> getAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance update(Long id, Maintenance maintenance) {
        Maintenance existing = getById(id);
        existing.setDateDebut(maintenance.getDateDebut());
        existing.setDateFin(maintenance.getDateFin());
        existing.setDescription(maintenance.getDescription());
        existing.setVehicule(maintenance.getVehicule());
        return maintenanceRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Maintenance existing = getById(id);
        maintenanceRepository.delete(existing);
    }
}
