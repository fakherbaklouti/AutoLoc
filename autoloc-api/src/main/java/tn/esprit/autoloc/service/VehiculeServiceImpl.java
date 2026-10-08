package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Vehicule create(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule getById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'id : " + id));
    }

    @Override
    public List<Vehicule> getAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        Vehicule existing = getById(id);
        existing.setImmatriculation(vehicule.getImmatriculation());
        existing.setMarque(vehicule.getMarque());
        existing.setModele(vehicule.getModele());
        existing.setCategorie(vehicule.getCategorie());
        existing.setTarifJournalier(vehicule.getTarifJournalier());
        existing.setStatut(vehicule.getStatut());
        existing.setAgence(vehicule.getAgence());
        return vehiculeRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Vehicule existing = getById(id);
        vehiculeRepository.delete(existing);
    }
}
