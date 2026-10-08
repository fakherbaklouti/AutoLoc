package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientRequestDto {

    @NotBlank
    @Size(max = 100)
    private String nom;

    @NotBlank
    @Size(max = 100)
    private String prenom;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String telephone;

    @NotBlank
    @Size(max = 50)
    private String numPermis;

    @NotNull
    private LocalDate dateInscription;
}
