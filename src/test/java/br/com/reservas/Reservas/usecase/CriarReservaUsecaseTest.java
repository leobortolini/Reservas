package br.com.reservas.Reservas.usecase;

import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.RestauranteNaoEncontradoException;
import br.com.reservas.Reservas.exception.RestauranteSemLugaresDisponiveisException;
import br.com.reservas.Reservas.gateway.ReservaGateway;
import br.com.reservas.Reservas.gateway.RestauranteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CriarReservaUsecaseTest {

    @InjectMocks
    private CriarReservaUsecase criarReservaUsecase;

    @Mock
    private ReservaGateway reservaGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criar_DeveRetornarIdDaReserva_QuandoReservaValida() {
        // Arrange
        Reserva reserva = new Reserva(null, 1L, 4, "Cliente Teste", LocalDateTime.now(), null);
        when(restauranteGateway.quantidadeDeLugares(anyLong(), any())).thenReturn(10L);
        when(reservaGateway.contarReservas(anyLong(), any())).thenReturn(2L);
        Reserva reservaComId = new Reserva();
        reservaComId.setReservaId(1L);
        when(reservaGateway.criar(any(Reserva.class))).thenReturn(reservaComId);

        // Act
        Reserva novaReserva = criarReservaUsecase.criar(reserva);

        // Assert
        assertEquals(1L, novaReserva.getReservaId());
        verify(restauranteGateway, times(1)).quantidadeDeLugares(1L, reserva.getInicioReserva());
        verify(reservaGateway, times(1)).contarReservas(1L, reserva.getInicioReserva());
        verify(reservaGateway, times(1)).criar(reserva);
    }

    @Test
    void criar_DeveRetornarIdDaReserva_QuandoCriaPrimeiraReservaValida() {
        // Arrange
        Reserva reserva = new Reserva(null, 1L, 4, "Cliente Teste", LocalDateTime.now(), null);
        when(restauranteGateway.quantidadeDeLugares(anyLong(), any())).thenReturn(10L);
        when(reservaGateway.contarReservas(anyLong(), any())).thenReturn(null);
        Reserva reservaComId = new Reserva();
        reservaComId.setReservaId(1L);
        when(reservaGateway.criar(any(Reserva.class))).thenReturn(reservaComId);

        // Act
        Reserva novaReserva = criarReservaUsecase.criar(reserva);

        // Assert
        assertEquals(1L, novaReserva.getReservaId());
        verify(restauranteGateway, times(1)).quantidadeDeLugares(1L, reserva.getInicioReserva());
        verify(reservaGateway, times(1)).contarReservas(1L, reserva.getInicioReserva());
        verify(reservaGateway, times(1)).criar(reserva);
    }

    @Test
    void criar_DeveLancarRestauranteNaoEncontradoException_QuandoCapacidadeTotalForNula() {
        // Arrange
        Reserva reserva = new Reserva(null, 1L, 4, "Cliente Teste", LocalDateTime.now(), null);
        when(restauranteGateway.quantidadeDeLugares(anyLong(), any())).thenReturn(null);

        // Act & Assert
        assertThrows(RestauranteNaoEncontradoException.class, () -> criarReservaUsecase.criar(reserva));
        verify(restauranteGateway, times(1)).quantidadeDeLugares(1L, reserva.getInicioReserva());
        verifyNoInteractions(reservaGateway);
    }

    @Test
    void criar_DeveLancarRestauranteSemLugaresDisponiveisException_QuandoNaoHouverLugaresDisponiveis() {
        // Arrange
        Reserva reserva = new Reserva(null, 1L, 4, "Cliente Teste", LocalDateTime.now(), null);
        when(restauranteGateway.quantidadeDeLugares(anyLong(), any())).thenReturn(5L);
        when(reservaGateway.contarReservas(anyLong(), any())).thenReturn(4L);

        // Act & Assert
        assertThrows(RestauranteSemLugaresDisponiveisException.class, () -> criarReservaUsecase.criar(reserva));
        verify(restauranteGateway, times(1)).quantidadeDeLugares(1L, reserva.getInicioReserva());
        verify(reservaGateway, times(1)).contarReservas(1L, reserva.getInicioReserva());
        verifyNoMoreInteractions(reservaGateway);
    }

    @Test
    void criar_DeveConfigurarStatusComoPending_QuandoReservaCriadaComSucesso() {
        // Arrange
        Reserva reserva = new Reserva(null, 1L, 4, "Cliente Teste", LocalDateTime.now(), null);
        when(restauranteGateway.quantidadeDeLugares(anyLong(), any())).thenReturn(10L);
        when(reservaGateway.contarReservas(anyLong(), any())).thenReturn(2L);
        Reserva reservaComId = new Reserva();
        reservaComId.setReservaId(1L);
        when(reservaGateway.criar(any(Reserva.class))).thenReturn(reservaComId);

        // Act
        criarReservaUsecase.criar(reserva);

        // Assert
        assertEquals(Reserva.Status.PENDENTE, reserva.getStatus());
        verify(restauranteGateway, times(1)).quantidadeDeLugares(1L, reserva.getInicioReserva());
        verify(reservaGateway, times(1)).contarReservas(1L, reserva.getInicioReserva());
        verify(reservaGateway, times(1)).criar(reserva);
    }

}
