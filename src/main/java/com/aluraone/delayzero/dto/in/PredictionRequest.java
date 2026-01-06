package com.aluraone.delayzero.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PredictionRequest(
        @NotBlank String nombreAerolinea,
        @NotBlank String origenVuelo,
        @NotBlank String destinoVuelo,
        /* Buscar como formatear LocalDateTime en el json de entrada */
        @NotNull LocalDateTime fechaPartidaVuelo,
        int distanciaKilometros
) {
}
