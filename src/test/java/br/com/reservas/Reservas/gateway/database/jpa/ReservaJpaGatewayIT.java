package br.com.reservas.Reservas.gateway.database.jpa;

import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.gateway.dto.ReservaDTO;
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
class ReservaJpaGatewayIT {

    @Autowired
    private ReservaJpaGateway reservaJpaGateway;

    @Test
    @Sql(scripts = {"/clean.sql"})
    void deveCriarReserva() {
        Reserva reserva = new Reserva(null, 1L, 4, "Cliente", LocalDateTime.now().plusDays(1), Reserva.Status.PENDENTE);

        reserva = reservaJpaGateway.criar(reserva);

        assertNotNull(reserva.getReservaId());
    }

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"})
    void deveAtualizarReserva() {
        LocalDateTime inicioReserva = LocalDateTime.now().plusDays(1);
        Reserva reservaAtualizar = new Reserva(1L, 1L, 5, "leo", inicioReserva, Reserva.Status.PENDENTE);

        ReservaDTO reservaAtualizada = reservaJpaGateway.atualizarReserva(reservaAtualizar);

        assertSame(5, reservaAtualizada.quantidadeLugares());
        assertEquals("leo", reservaAtualizada.nomeCliente());
        assertEquals(inicioReserva, reservaAtualizada.inicioReserva());
    }

    @Test
    @Sql(scripts = {"/clean.sql"})
    void deveLancarExcecaoAoAtualizarReservaInexistente() {
        Reserva reservaInexistente = new Reserva(999L, 1L, 1, "leo", LocalDateTime.now(), Reserva.Status.CANCELADA);

        assertThrows(RuntimeException.class, () -> reservaJpaGateway.atualizarReserva(reservaInexistente));
    }

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"})
    void deveContarReservasExcluindoStatusCanceladaECompletada() {
        LocalDateTime dataReserva = LocalDateTime.of(2024, 11, 23, 18, 30);

        Long totalReservas = reservaJpaGateway.contarReservas(3L, dataReserva);

        assertSame(6L, totalReservas);
    }

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"})
    void deveListarReservas() {
        LocalDateTime dataReserva = LocalDateTime.of(2024, 11, 23, 18, 30);
        List<ReservaDTO> reservas = reservaJpaGateway.listarReservas(3L, dataReserva);

        assertSame(2, reservas.size());
    }

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"})
    void deveRetornarReservaPorId() {

        ReservaDTO reserva = reservaJpaGateway.listarReserva(4L);

        assertNotNull(reserva);
    }

    @Test
    @Sql(scripts = {"/clean.sql"})
    void deveRetornarNullSeNaoExistirReservaComId() {
        ReservaDTO reserva = reservaJpaGateway.listarReserva(4L);

        assertNull(reserva);
    }
}
