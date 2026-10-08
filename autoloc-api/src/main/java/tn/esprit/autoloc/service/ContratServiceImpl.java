package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    public Contrat create(Contrat contrat) {
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat getById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable avec l'id : " + id));
    }

    @Override
    public List<Contrat> getAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        Contrat existing = getById(id);
        existing.setDateSignature(contrat.getDateSignature());
        existing.setMontantTotal(contrat.getMontantTotal());
        existing.setValide(contrat.getValide());
        existing.setReservation(contrat.getReservation());
        return contratRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Contrat existing = getById(id);
        contratRepository.delete(existing);
    }
}
