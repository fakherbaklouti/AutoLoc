package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceService agenceService;

    @PostMapping
    public ResponseEntity<Agence> create(@RequestBody Agence agence) {
        Agence created = agenceService.create(agence);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agence> getById(@PathVariable Long id) {
        return ResponseEntity.ok(agenceService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Agence>> getAll() {
        return ResponseEntity.ok(agenceService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agence> update(
            @PathVariable Long id,
            @RequestBody Agence agence) {
        return ResponseEntity.ok(agenceService.update(id, agence));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}