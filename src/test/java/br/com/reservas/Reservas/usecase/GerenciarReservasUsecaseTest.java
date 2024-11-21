package br.com.reservas.Reservas.usecase;

import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.gateway.ReservaGateway;
import br.com.reservas.Reservas.gateway.dto.ReservaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GerenciarReservasUsecaseTest {

    @InjectMocks
    private GerenciarReservasUsecase gerenciarReservasUsecase;

    @Mock
    private ReservaGateway reservaGateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void atualizarReserva_DeveRetornarReservaAtualizada_QuandoAtualizacaoForBemSucedida() {
        // Arrange
        Reserva reserva = new Reserva(1L, 10L, 4, "Cliente A", LocalDateTime.now(), Reserva.Status.PENDENTE);
        ReservaDTO reservaDTOAtualizada = new ReservaDTO(
                1L, 10L, 4, "Cliente A", reserva.getInicioReserva(), Reserva.Status.COMPLETADA
        );

        when(reservaGateway.atualizarReserva(reserva)).thenReturn(reservaDTOAtualizada);

        // Act
        Reserva reservaAtualizada = gerenciarReservasUsecase.atualizarReserva(reserva);

        // Assert
        assertNotNull(reservaAtualizada);
        assertEquals(1L, reservaAtualizada.getReservaId());
        assertEquals(10L, reservaAtualizada.getRestauranteId());
        assertEquals(4, reservaAtualizada.getQuantidadeLugares());
        assertEquals("Cliente A", reservaAtualizada.getNomeCliente());
        assertEquals(Reserva.Status.COMPLETADA, reservaAtualizada.getStatus());
        verify(reservaGateway, times(1)).atualizarReserva(reserva);
    }
}
