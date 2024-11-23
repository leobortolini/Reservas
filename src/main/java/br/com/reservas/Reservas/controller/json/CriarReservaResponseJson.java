package br.com.reservas.Reservas.controller.json;

import java.time.LocalDateTime;

public record CriarReservaResponseJson(Long id, Long restauranteId, Integer quantidadeLugares, String nomeCliente, LocalDateTime inicioReserva,
                                       String status){ }
