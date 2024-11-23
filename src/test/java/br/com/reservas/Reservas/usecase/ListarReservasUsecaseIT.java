package br.com.reservas.Reservas.usecase;

import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.ReservaNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class ListarReservasUsecaseIT {

    @Autowired
    private ListarReservasUsecase listarReservasUsecase;

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deveListarReservasComSucesso() {
        // Arrange
        Long restauranteId = 2L;
        LocalDateTime inicioReserva = LocalDateTime.of(2024, 11, 22, 20, 0);

        // Act
        List<Reserva> reservas = listarReservasUsecase.listarReservas(restauranteId, inicioReserva);

        // Assert
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
        assertEquals(1, reservas.size());
        assertEquals("Maria Oliveira", reservas.get(0).getNomeCliente());
        assertSame(Reserva.Status.INICIADA, reservas.get(0).getStatus());
    }

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deveLancarExcecaoQuandoReservaNaoExistir() {
        // Arrange
        Long reservaId = 999L;

        // Act & Assert
        assertThrows(ReservaNaoEncontradaException.class, () -> listarReservasUsecase.listarReserva(reservaId));
    }
}
