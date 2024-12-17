package br.com.reservas.usecase;

import br.com.reservas.domain.Reserva;
import br.com.reservas.exception.RestauranteNaoEncontradoException;
import br.com.reservas.exception.RestauranteSemLugaresDisponiveisException;
import br.com.reservas.gateway.ReservaGateway;
import br.com.reservas.gateway.RestauranteGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarReservaUsecase {

    private final ReservaGateway reservaGateway;
    private final RestauranteGateway restauranteGateway;

    public Reserva criar(Reserva reserva) {
        Long capacidadeTotal = restauranteGateway.quantidadeDeLugares(reserva.getRestauranteId(), reserva.getInicioReserva());

        if (capacidadeTotal == null) {
            throw new RestauranteNaoEncontradoException();
        }

        Long reservas = reservaGateway.contarReservas(reserva.getRestauranteId(), reserva.getInicioReserva());

        if (reservas != null) {
            Long lugaresDisponiveis = capacidadeTotal - reservas;

            if (lugaresDisponiveis - reserva.getQuantidadeLugares() < 0) {
                throw new RestauranteSemLugaresDisponiveisException();
            }
        }

        reserva.setStatus(Reserva.Status.PENDENTE);

        return reservaGateway.criar(reserva);
    }
}
