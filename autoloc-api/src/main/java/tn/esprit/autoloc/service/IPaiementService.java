package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Paiement;

import java.util.List;

public interface IPaiementService {

    Paiement create(Paiement paiement);

    Paiement getById(Long id);

    List<Paiement> getAll();

    Paiement update(Long id, Paiement paiement);

    void delete(Long id);
}
