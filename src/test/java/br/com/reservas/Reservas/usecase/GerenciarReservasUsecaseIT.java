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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class GerenciarReservasUsecaseIT {

    @Autowired
    private GerenciarReservasUsecase gerenciarReservasUsecase;

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"})
    void deveAtualizarReservaComSucesso() {
        // Arrange
        Long reservaId = 1L;
        Reserva reserva = new Reserva(reservaId, 1L, 6, "Cliente Atualizado", LocalDateTime.of(2024, 11, 23, 18, 0), Reserva.Status.INICIADA);

        // Act
        Reserva reservaAtualizada = gerenciarReservasUsecase.atualizarReserva(reserva);

        // Assert
        assertNotNull(reservaAtualizada);
        assertEquals(reservaId, reservaAtualizada.getReservaId());
        assertEquals(6, reservaAtualizada.getQuantidadeLugares());
        assertEquals("Cliente Atualizado", reservaAtualizada.getNomeCliente());
        assertEquals(Reserva.Status.INICIADA, reservaAtualizada.getStatus());
    }

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"})
    void deveLancarExcecaoQuandoReservaNaoExiste() {
        // Arrange
        Long reservaId = 999L;
        Reserva reserva = new Reserva(reservaId, 1L, 6, "Cliente Atualizado", LocalDateTime.of(2024, 11, 23, 18, 0), Reserva.Status.INICIADA);

        // Act & Assert
        assertThrows(ReservaNaoEncontradaException.class, () -> gerenciarReservasUsecase.atualizarReserva(reserva));
    }
}
