package tn.esprit.autoloc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.VehiculeResponseDto;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehiculeServiceImplTest {

    @Mock
    private IVehiculeRepository vehiculeRepository;

    @Mock
    private IEquipementRepository equipementRepository;

    @Mock
    private VehiculeMapper vehiculeMapper;

    @InjectMocks
    private VehiculeServiceImpl vehiculeService;

    @Test
    @DisplayName("Affecte un équipement, sauvegarde le véhicule et retourne son DTO")
    void affecterVehiculeAEquipementAjouteEtRetourneLeDto() {
        Long idVehicule = 1L;
        Long idEquipement = 2L;
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(idVehicule);
        Equipement equipement = new Equipement();
        equipement.setIdEquipement(idEquipement);
        VehiculeResponseDto response = new VehiculeResponseDto();

        when(vehiculeRepository.findById(idVehicule)).thenReturn(Optional.of(vehicule));
        when(equipementRepository.findById(idEquipement)).thenReturn(Optional.of(equipement));
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        VehiculeResponseDto result = vehiculeService.affecterVehiculeAEquipement(
                idVehicule, idEquipement);

        assertSame(response, result);
        assertTrue(vehicule.getEquipements().contains(equipement));
        verify(vehiculeRepository).save(vehicule);
        verify(vehiculeMapper).toResponse(vehicule);
    }

    @Test
    @DisplayName("N'ajoute pas deux fois le même équipement")
    void affecterVehiculeAEquipementEviteLeDoublon() {
        Long idVehicule = 1L;
        Long idEquipement = 2L;
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(idVehicule);
        Equipement equipement = new Equipement();
        equipement.setIdEquipement(idEquipement);
        vehicule.setEquipements(new ArrayList<>(List.of(equipement)));

        when(vehiculeRepository.findById(idVehicule)).thenReturn(Optional.of(vehicule));
        when(equipementRepository.findById(idEquipement)).thenReturn(Optional.of(equipement));
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);

        vehiculeService.affecterVehiculeAEquipement(idVehicule, idEquipement);

        assertTrue(vehicule.getEquipements().size() == 1);
        verify(vehiculeRepository).save(vehicule);
    }

    @Test
    @DisplayName("Signale un équipement inexistant sans sauvegarder le véhicule")
    void affecterVehiculeAEquipementEquipementIntrouvable() {
        Long idVehicule = 1L;
        Long idEquipement = 2L;
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(idVehicule);

        when(vehiculeRepository.findById(idVehicule)).thenReturn(Optional.of(vehicule));
        when(equipementRepository.findById(idEquipement)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                vehiculeService.affecterVehiculeAEquipement(idVehicule, idEquipement));

        verify(vehiculeRepository, never()).save(vehicule);
    }

    @Test
    @DisplayName("Signale un véhicule inexistant sans rechercher l'équipement")
    void affecterVehiculeAEquipementVehiculeIntrouvable() {
        Long idVehicule = 1L;
        Long idEquipement = 2L;
        when(vehiculeRepository.findById(idVehicule)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                vehiculeService.affecterVehiculeAEquipement(idVehicule, idEquipement));

        verify(equipementRepository, never()).findById(idEquipement);
        verify(vehiculeRepository, never()).save(org.mockito.ArgumentMatchers.any(Vehicule.class));
    }
}