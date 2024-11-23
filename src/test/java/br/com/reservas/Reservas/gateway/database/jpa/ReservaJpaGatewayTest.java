package br.com.reservas.Reservas.gateway.database.jpa;

import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.ErroAoAcessarRepositorioException;
import br.com.reservas.Reservas.gateway.database.jpa.entity.ReservaEntity;
import br.com.reservas.Reservas.gateway.database.jpa.repository.ReservaRepository;
import br.com.reservas.Reservas.gateway.dto.ReservaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaJpaGatewayTest {

    @InjectMocks
    private ReservaJpaGateway reservaJpaGateway;

    @Mock
    private ReservaRepository reservaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void contarReservas_DeveRetornarQuantidadeDeLugares() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime data = LocalDateTime.now();
        List<Reserva.Status> excludedStatuses = Arrays.asList(Reserva.Status.CANCELADA, Reserva.Status.COMPLETADA);
        Long quantidadeEsperada = 20L;

        when(reservaRepository.somarQuantidadeLugares(restauranteId, data, excludedStatuses)).thenReturn(quantidadeEsperada);

        // Act
        Long quantidade = reservaJpaGateway.contarReservas(restauranteId, data);

        // Assert
        assertEquals(quantidadeEsperada, quantidade);
        verify(reservaRepository, times(1)).somarQuantidadeLugares(restauranteId, data, excludedStatuses);
    }

    @Test
    void criar_DeveRetornarIdDaReservaCriada() {
        // Arrange
        Reserva reserva = new Reserva(null, 1L, 4, "Cliente", LocalDateTime.now(), Reserva.Status.PENDENTE);
        ReservaEntity reservaEntity = ReservaEntity.toEntity(reserva);
        reservaEntity.setId(1L);

        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reservaEntity);

        // Act
        reserva = reservaJpaGateway.criar(reserva);

        // Assert
        assertEquals(1L, reserva.getReservaId());
        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
    }

    @Test
    void criar_DeveLancarErroAoAcessarRepositorioException_QuandoOcorreErro() {
        // Arrange
        Reserva reserva = new Reserva(null, 1L, 4, "Cliente", LocalDateTime.now(), Reserva.Status.PENDENTE);

        when(reservaRepository.save(any(ReservaEntity.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        // Act & Assert
        assertThrows(ErroAoAcessarRepositorioException.class, () -> reservaJpaGateway.criar(reserva));
        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
    }

    @Test
    void atualizarReserva_DeveRetornarReservaAtualizada() {
        // Arrange
        Reserva reserva = new Reserva(1L, 1L, 4, "Cliente", LocalDateTime.now(), Reserva.Status.PENDENTE);
        ReservaEntity reservaEntity = ReservaEntity.toEntity(reserva);
        reservaEntity.setId(1L);

        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reservaEntity);
        when(reservaRepository.findById(any(Long.class))).thenReturn(Optional.of(mock(ReservaEntity.class)));

        // Act
        ReservaDTO reservaDTO = reservaJpaGateway.atualizarReserva(reserva);

        // Assert
        assertNotNull(reservaDTO);
        assertEquals(reserva.getReservaId(), reservaDTO.reservaId());
        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
    }

    @Test
    void listarReservas_DeveRetornarListaDeReservas() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime data = LocalDateTime.now();
        ReservaEntity reservaEntity = new ReservaEntity(1L, restauranteId, 4, "Cliente", data, Reserva.Status.PENDENTE);

        when(reservaRepository.findAllByRestauranteIdAndInicioReserva(restauranteId, data)).thenReturn(List.of(reservaEntity));

        // Act
        List<ReservaDTO> reservas = reservaJpaGateway.listarReservas(restauranteId, data);

        // Assert
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals(reservaEntity.getId(), reservas.get(0).reservaId());
        verify(reservaRepository, times(1)).findAllByRestauranteIdAndInicioReserva(restauranteId, data);
    }

    @Test
    void listarReserva_DeveRetornarReserva_QuandoEncontrada() {
        // Arrange
        Long reservaId = 1L;
        ReservaEntity reservaEntity = new ReservaEntity(reservaId, 1L, 4, "Cliente", LocalDateTime.now(), Reserva.Status.PENDENTE);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaEntity));

        // Act
        ReservaDTO reservaDTO = reservaJpaGateway.listarReserva(reservaId);

        // Assert
        assertNotNull(reservaDTO);
        assertEquals(reservaId, reservaDTO.reservaId());
        verify(reservaRepository, times(1)).findById(reservaId);
    }

    @Test
    void listarReserva_DeveRetornarNull_QuandoNaoEncontrada() {
        // Arrange
        Long reservaId = 1L;

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());

        // Act
        ReservaDTO reservaDTO = reservaJpaGateway.listarReserva(reservaId);

        // Assert
        assertNull(reservaDTO);
        verify(reservaRepository, times(1)).findById(reservaId);
    }
}
