package br.com.reservas.Reservas.usecase;

import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.ReservaNaoEncontradaException;
import br.com.reservas.Reservas.gateway.ReservaGateway;
import br.com.reservas.Reservas.gateway.dto.ReservaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarReservasUsecase {
    private final ReservaGateway reservaGateway;

    public List<Reserva> listarReservas(Long restauranteId, LocalDateTime inicioReserva) {
        List<ReservaDTO> reservaDTOS = reservaGateway.listarReservas(restauranteId, inicioReserva);

        return mapToDomain(reservaDTOS);
    }

    public Reserva listarReserva(Long reservaId) {
        ReservaDTO reservaDTO = reservaGateway.listarReserva(reservaId);

        if (reservaDTO != null) {
            return mapToDomain(reservaDTO);
        }

        throw new ReservaNaoEncontradaException();
    }

    private List<Reserva> mapToDomain(List<ReservaDTO> reservaDTOS) {
        return reservaDTOS.stream().map(this::mapToDomain).toList();
    }

    private Reserva mapToDomain(ReservaDTO dto) {
        return new Reserva(dto.reservaId(), dto.restauranteId(), dto.quantidadeLugares(), dto.nomeCliente(), dto.inicioReserva(), dto.status());
    }
}
