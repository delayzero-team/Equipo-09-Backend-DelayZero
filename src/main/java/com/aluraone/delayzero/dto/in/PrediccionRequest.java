package com.aluraone.delayzero.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PrediccionRequest(
        @NotBlank String nombreAerolinea,
        @NotBlank String origenVuelo,
        @NotBlank String destinoVuelo,
        @NotNull LocalDateTime fechaPartidaVuelo,
        int distanciaKilometros
) {
}
