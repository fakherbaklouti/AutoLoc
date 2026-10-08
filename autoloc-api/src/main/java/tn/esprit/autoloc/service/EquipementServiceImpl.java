package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    @Override
    public Equipement create(Equipement equipement) {
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement getById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement introuvable avec l'id : " + id));
    }

    @Override
    public List<Equipement> getAll() {
        return equipementRepository.findAll();
    }

    @Override
    public Equipement update(Long id, Equipement equipement) {
        Equipement existing = getById(id);
        existing.setLibelle(equipement.getLibelle());
        return equipementRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Equipement existing = getById(id);
        equipementRepository.delete(existing);
    }
}
