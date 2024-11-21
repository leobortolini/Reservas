package br.com.reservas.Reservas.controller.json;

import br.com.reservas.Reservas.domain.Reserva;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarReservaJson {
    @NotNull
    private Long reservaId;
    @NotNull
    private Long restauranteId;
    @NotNull
    private Integer quantidadeLugares;
    @NotBlank
    private String nomeCliente;
    @NotNull
    private LocalDateTime inicioReserva;
    @NotNull
    private Reserva.Status status;
}
