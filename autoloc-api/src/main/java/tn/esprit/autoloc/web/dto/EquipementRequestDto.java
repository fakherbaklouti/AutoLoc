package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipementRequestDto {

    @NotBlank
    @Size(max = 100)
    private String libelle;
}
