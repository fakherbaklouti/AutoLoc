package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.web.dto.ClientRequestDto;
import tn.esprit.autoloc.web.dto.ClientResponseDto;

import java.util.List;

@Component
public class ClientMapper {

    public Client toEntity(ClientRequestDto request) {
        if (request == null) {
            return null;
        }
        Client client = new Client();
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setEmail(request.getEmail());
        client.setTelephone(request.getTelephone());
        client.setNumPermis(request.getNumPermis());
        client.setDateInscription(request.getDateInscription());
        return client;
    }

    public ClientResponseDto toResponse(Client client) {
        if (client == null) {
            return null;
        }
        return new ClientResponseDto(
                client.getIdClient(),
                client.getNom(),
                client.getPrenom(),
                client.getEmail(),
                client.getTelephone(),
                client.getNumPermis(),
                client.getDateInscription()
        );
    }

    public List<ClientResponseDto> toResponseList(List<Client> clients) {
        if (clients == null) {
            return List.of();
        }
        return clients.stream().map(this::toResponse).toList();
    }
}
