package br.com.reservas.usecase;

import br.com.reservas.domain.Reserva;
import br.com.reservas.gateway.ReservaGateway;
import br.com.reservas.gateway.dto.ReservaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GerenciarReservasUsecase {

    private final ReservaGateway reservaGateway;

    public Reserva atualizarReserva(Reserva reserva) {
        ReservaDTO dto = reservaGateway.atualizarReserva(reserva);

        return new Reserva(dto.reservaId(), dto.restauranteId(), dto.quantidadeLugares(), dto.nomeCliente(), dto.inicioReserva(), dto.status());
    }
}
