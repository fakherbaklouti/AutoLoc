package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;

    @Override
    public Paiement create(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement getById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable avec l'id : " + id));
    }

    @Override
    public List<Paiement> getAll() {
        return paiementRepository.findAll();
    }

    @Override
    public Paiement update(Long id, Paiement paiement) {
        Paiement existing = getById(id);
        existing.setMontant(paiement.getMontant());
        existing.setDatePaiement(paiement.getDatePaiement());
        existing.setModePaiement(paiement.getModePaiement());
        existing.setContrat(paiement.getContrat());
        return paiementRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Paiement existing = getById(id);
        paiementRepository.delete(existing);
    }
}
