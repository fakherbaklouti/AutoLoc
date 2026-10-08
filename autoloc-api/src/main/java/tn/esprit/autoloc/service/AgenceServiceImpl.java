package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    public Agence create(Agence agence) {
        return agenceRepository.save(agence);
    }

    @Override
    public Agence getById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable avec l'id : " + id));
    }

    @Override
    public List<Agence> getAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existing = getById(id);
        existing.setNom(agence.getNom());
        existing.setVille(agence.getVille());
        existing.setAdresse(agence.getAdresse());
        existing.setTelephone(agence.getTelephone());
        return agenceRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Agence existing = getById(id);
        agenceRepository.delete(existing);
    }
}
