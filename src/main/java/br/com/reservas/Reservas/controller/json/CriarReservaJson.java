package br.com.reservas.Reservas.controller.json;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CriarReservaJson {
    @NotNull
    private Long restauranteId;
    @NotNull
    private Integer quantidadeLugares;
    @NotBlank
    private String nomeCliente;
    @NotNull
    private LocalDateTime inicioReserva;
}
