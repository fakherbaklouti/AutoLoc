
package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.VehiculeResponseDto;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IEquipementRepository equipementRepository;
    private final VehiculeMapper vehiculeMapper;
    private final IMaintenanceRepository maintenanceRepository;

    @Override
    public Vehicule create(Vehicule vehicule) {
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau véhicule ne doit pas avoir d'identifiant");
        }

        validateVehicule(vehicule);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule getById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicule", id));
    }

    @Override
    public List<Vehicule> getAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        Vehicule existing = getById(id);
        validateVehicule(vehicule);

        existing.setImmatriculation(vehicule.getImmatriculation());
        existing.setMarque(vehicule.getMarque());
        existing.setModele(vehicule.getModele());
        existing.setCategorie(vehicule.getCategorie());
        existing.setTarifJournalier(vehicule.getTarifJournalier());
        existing.setStatut(vehicule.getStatut());
        existing.setAgence(vehicule.getAgence());
        existing.setEquipements(vehicule.getEquipements());

        return vehiculeRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Vehicule existing = getById(id);
        vehiculeRepository.delete(existing);
    }

    @Override
    public VehiculeResponseDto affecterVehiculeAEquipement(
            Long idVehicule, Long idEquipement) {

        Vehicule vehicule = getById(idVehicule);

        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Equipement", idEquipement));

        if (vehicule.getEquipements() == null) {
            vehicule.setEquipements(new java.util.ArrayList<>());
        }

        if (!vehicule.getEquipements().contains(equipement)) {
            vehicule.getEquipements().add(equipement);
        }

        Vehicule saved = vehiculeRepository.save(vehicule);

        return vehiculeMapper.toResponse(saved);
    }

    @Override
    public VehiculeResponseDto affecterMaintenanceAvehicule(
            String description, String immatriculation) {

        Maintenance maintenance = maintenanceRepository.findByDescription(description)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Maintenance non trouvée : " + description));

        Vehicule vehicule = vehiculeRepository.findByImmatriculation(immatriculation)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Véhicule non trouvé : " + immatriculation));

        maintenance.setVehicule(vehicule);
        maintenanceRepository.save(maintenance);

        return vehiculeMapper.toResponse(vehicule);
    }

    private void validateVehicule(Vehicule vehicule) {
        if (vehicule.getTarifJournalier() != null
                && vehicule.getTarifJournalier().signum() < 0) {
            throw new IllegalArgumentException(
                    "Le tarif journalier ne peut pas être négatif");
        }
    }
}
