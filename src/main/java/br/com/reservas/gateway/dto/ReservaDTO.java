package br.com.reservas.gateway.dto;

import br.com.reservas.domain.Reserva;

import java.time.LocalDateTime;

public record ReservaDTO(Long reservaId, Long restauranteId, Integer quantidadeLugares, String nomeCliente, LocalDateTime inicioReserva, Reserva.Status status) { }
