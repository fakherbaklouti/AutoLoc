package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'id : " + id));
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client update(Long id, Client client) {
        Client existing = getById(id);
        existing.setNom(client.getNom());
        existing.setPrenom(client.getPrenom());
        existing.setEmail(client.getEmail());
        existing.setTelephone(client.getTelephone());
        existing.setNumPermis(client.getNumPermis());
        existing.setDateInscription(client.getDateInscription());
        return clientRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Client existing = getById(id);
        clientRepository.delete(existing);
    }
}
