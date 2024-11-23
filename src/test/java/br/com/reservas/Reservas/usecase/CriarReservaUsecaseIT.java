package br.com.reservas.Reservas.usecase;

import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.RestauranteNaoEncontradoException;
import br.com.reservas.Reservas.exception.RestauranteSemLugaresDisponiveisException;
import br.com.reservas.Reservas.gateway.RestauranteGateway;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class CriarReservaUsecaseIT {

    @Autowired
    private CriarReservaUsecase criarReservaUsecase;

    @MockBean
    private RestauranteGateway restauranteGateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Sql(scripts = "/clean.sql")
    void deveCriarReservaComSucesso() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime inicioReserva = LocalDateTime.of(2024, 11, 24, 12, 0);
        Reserva reserva = new Reserva(null, restauranteId, 4, "Cliente Teste", inicioReserva, null);

        when(restauranteGateway.quantidadeDeLugares(restauranteId, inicioReserva)).thenReturn(20L);

        // Act
        Reserva reservaId = criarReservaUsecase.criar(reserva);

        // Assert
        assertNotNull(reservaId);
        assertTrue(reserva.getReservaId() > 0);

        verify(restauranteGateway, times(1)).quantidadeDeLugares(restauranteId, inicioReserva);
    }

    @Test
    @Sql(scripts = { "/clean.sql", "/reservas.sql"})
    void deveLancarExcecaoParaRestauranteSemDisponibilidade() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime inicioReserva = LocalDateTime.of(2024, 11, 21, 19, 0);
        Reserva reserva = new Reserva(null, restauranteId, 15, "Cliente Sem Lugar", inicioReserva, null);

        when(restauranteGateway.quantidadeDeLugares(restauranteId, inicioReserva)).thenReturn(14L);

        // Act & Assert
        assertThrows(RestauranteSemLugaresDisponiveisException.class, () -> criarReservaUsecase.criar(reserva));

        verify(restauranteGateway, times(1)).quantidadeDeLugares(restauranteId, inicioReserva);
    }

    @Test
    @Sql(scripts = "/clean.sql")
    void deveLancarExcecaoParaRestauranteNaoEncontrado() {
        // Arrange
        Long restauranteId = 99L;
        LocalDateTime inicioReserva = LocalDateTime.of(2024, 11, 24, 12, 0);
        Reserva reserva = new Reserva(null, restauranteId, 4, "Cliente Restaurante Inexistente", inicioReserva, null);

        when(restauranteGateway.quantidadeDeLugares(restauranteId, inicioReserva)).thenReturn(null);

        // Act & Assert
        assertThrows(RestauranteNaoEncontradoException.class, () -> criarReservaUsecase.criar(reserva));

        verify(restauranteGateway, times(1)).quantidadeDeLugares(restauranteId, inicioReserva);
    }
}
