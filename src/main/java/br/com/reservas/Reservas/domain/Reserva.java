package br.com.reservas.Reservas.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Reserva {
    private Long reservaId;
    private Long restauranteId;
    private Integer quantidadeLugares;
    private String nomeCliente;
    private LocalDateTime inicioReserva;
    private Status status;

    public enum Status {
        PENDENTE,
        CANCELADA,
        INICIADA,
        COMPLETADA
    }
}
