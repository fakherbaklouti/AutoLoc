package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientRequestDto;
import tn.esprit.autoloc.web.dto.ClientResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDto> create(@Valid @RequestBody ClientRequestDto request) {
        Client created = clientService.create(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(clientService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> getAll() {
        List<ClientResponseDto> clients = clientService.getAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDto> update(@PathVariable Long id,
                                                    @Valid @RequestBody ClientRequestDto request) {
        Client updated = clientService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Client toEntity(ClientRequestDto request) {
        Client client = new Client();
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setEmail(request.getEmail());
        client.setTelephone(request.getTelephone());
        client.setNumPermis(request.getNumPermis());
        client.setDateInscription(request.getDateInscription());
        return client;
    }

    private ClientResponseDto toResponse(Client client) {
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
}
