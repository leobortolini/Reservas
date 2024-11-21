package br.com.reservas.Reservas.usecase;

import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.ReservaNaoEncontradaException;
import br.com.reservas.Reservas.gateway.ReservaGateway;
import br.com.reservas.Reservas.gateway.dto.ReservaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarReservasUsecaseTest {

    @InjectMocks
    private ListarReservasUsecase listarReservasUsecase;

    @Mock
    private ReservaGateway reservaGateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarReservas_DeveRetornarListaDeReservas_QuandoExistiremReservas() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime inicioReserva = LocalDateTime.now();
        List<ReservaDTO> reservaDTOS = List.of(
                new ReservaDTO(1L, restauranteId, 4, "Cliente A", inicioReserva, Reserva.Status.PENDENTE),
                new ReservaDTO(2L, restauranteId, 2, "Cliente B", inicioReserva.plusHours(1), Reserva.Status.PENDENTE)
        );
        when(reservaGateway.listarReservas(restauranteId, inicioReserva)).thenReturn(reservaDTOS);

        // Act
        List<Reserva> reservas = listarReservasUsecase.listarReservas(restauranteId, inicioReserva);

        // Assert
        assertEquals(2, reservas.size());
        assertEquals("Cliente A", reservas.get(0).getNomeCliente());
        assertEquals(4, reservas.get(0).getQuantidadeLugares());
        assertEquals(Reserva.Status.PENDENTE, reservas.get(0).getStatus());
        verify(reservaGateway, times(1)).listarReservas(restauranteId, inicioReserva);
    }

    @Test
    void listarReservas_DeveRetornarListaVazia_QuandoNaoExistiremReservas() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime inicioReserva = LocalDateTime.now();
        when(reservaGateway.listarReservas(restauranteId, inicioReserva)).thenReturn(List.of());

        // Act
        List<Reserva> reservas = listarReservasUsecase.listarReservas(restauranteId, inicioReserva);

        // Assert
        assertTrue(reservas.isEmpty());
        verify(reservaGateway, times(1)).listarReservas(restauranteId, inicioReserva);
    }

    @Test
    void listarReserva_DeveRetornarReserva_QuandoReservaExistir() {
        // Arrange
        Long reservaId = 1L;
        ReservaDTO reservaDTO = new ReservaDTO(reservaId, 1L, 4, "Cliente A", LocalDateTime.now(), Reserva.Status.PENDENTE);
        when(reservaGateway.listarReserva(reservaId)).thenReturn(reservaDTO);

        // Act
        Reserva reserva = listarReservasUsecase.listarReserva(reservaId);

        // Assert
        assertNotNull(reserva);
        assertEquals("Cliente A", reserva.getNomeCliente());
        assertEquals(4, reserva.getQuantidadeLugares());
        verify(reservaGateway, times(1)).listarReserva(reservaId);
    }

    @Test
    void listarReserva_DeveLancarReservaNaoEncontradaException_QuandoReservaNaoExistir() {
        // Arrange
        Long reservaId = 1L;
        when(reservaGateway.listarReserva(reservaId)).thenReturn(null);

        // Act & Assert
        assertThrows(ReservaNaoEncontradaException.class, () -> listarReservasUsecase.listarReserva(reservaId));
        verify(reservaGateway, times(1)).listarReserva(reservaId);
    }
}
