package br.com.reservas.Reservas.gateway.dto;

import br.com.reservas.Reservas.domain.Reserva;

import java.time.LocalDateTime;

public record ReservaDTO(Long reservaId, Long restauranteId, Integer quantidadeLugares, String nomeCliente, LocalDateTime inicioReserva, Reserva.Status status) { }
