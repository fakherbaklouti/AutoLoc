package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEmployeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe create(Employe employe) {
        return employeRepository.save(employe);
    }

    @Override
    public Employe getById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable avec l'id : " + id));
    }

    @Override
    public List<Employe> getAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe employe) {
        Employe existing = getById(id);
        existing.setNom(employe.getNom());
        existing.setPrenom(employe.getPrenom());
        existing.setRole(employe.getRole());
        existing.setAgence(employe.getAgence());
        return employeRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Employe existing = getById(id);
        employeRepository.delete(existing);
    }
}
