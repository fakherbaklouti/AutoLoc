package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgenceRequestDto {

    @NotBlank
    @Size(max = 100)
    private String nom;

    @NotBlank
    @Size(max = 100)
    private String ville;

    @NotBlank
    @Size(max = 255)
    private String adresse;

    @NotBlank
    @Size(max = 20)
    private String telephone;
}