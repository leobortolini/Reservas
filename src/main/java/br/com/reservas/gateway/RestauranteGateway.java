package br.com.reservas.gateway;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public interface RestauranteGateway {
    Long quantidadeDeLugares(Long restauranteId, LocalDateTime inicioReserva);
}