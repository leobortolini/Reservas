package br.com.reservas.gateway;

import br.com.reservas.domain.Reserva;
import br.com.reservas.gateway.dto.ReservaDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaGateway {

    Long contarReservas(Long restauranteId, LocalDateTime data);
    Reserva criar(Reserva reserva);
    ReservaDTO atualizarReserva(Reserva reserva);
    List<ReservaDTO> listarReservas(Long restauranteId, LocalDateTime data);
    ReservaDTO listarReserva(Long reservaId);
}
