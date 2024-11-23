package br.com.reservas.Reservas.controller;

import br.com.reservas.Reservas.controller.json.AtualizarReservaJson;
import br.com.reservas.Reservas.controller.json.CriarAvaliacaoJson;
import br.com.reservas.Reservas.controller.json.CriarReservaJson;
import br.com.reservas.Reservas.domain.Avaliacao;
import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.usecase.CriarAvaliacaoUsecase;
import br.com.reservas.Reservas.usecase.CriarReservaUsecase;
import br.com.reservas.Reservas.usecase.GerenciarReservasUsecase;
import br.com.reservas.Reservas.usecase.ListarReservasUsecase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservasController.class)
class ReservasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CriarReservaUsecase criarReservaUsecase;

    @MockBean
    private ListarReservasUsecase listarReservasUsecase;

    @MockBean
    private GerenciarReservasUsecase gerenciarReservasUsecase;

    @MockBean
    private CriarAvaliacaoUsecase criarAvaliacaoUsecase;

    @Test
    void criarReserva_DeveRetornarStatusCreatedQuandoDadosValidos() throws Exception {
        // Arrange
        CriarReservaJson criarReservaJson = new CriarReservaJson(
                1L,
                4,
                "Cliente Teste",
                LocalDateTime.parse("2024-12-01T18:00:00")
        );
        Reserva reserva = new Reserva();
        reserva.setReservaId(1L);
        reserva.setStatus(Reserva.Status.PENDENTE);
        reserva.setInicioReserva(LocalDateTime.parse("2024-12-01T18:00:00"));
        reserva.setNomeCliente("Cliente Teste");

        when(criarReservaUsecase.criar(any())).thenReturn(reserva);

        // Act & Assert
        mockMvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarReservaJson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(criarReservaUsecase, times(1)).criar(any());
    }

    @Test
    void criarAvaliacao_DeveRetornarStatusCreatedQuandoDadosValidos() throws Exception {
        // Arrange
        CriarAvaliacaoJson criarAvaliacaoJson = new CriarAvaliacaoJson(
                1L,
                Avaliacao.Satisfacao.PERFEITO,
                "Comentario Teste"
        );
        Avaliacao avaliacao = new Avaliacao();

        avaliacao.setAvaliacaoId(1L);
        avaliacao.setReserva(new Reserva());
        avaliacao.setComentario("Comentario Teste");
        avaliacao.setSatisfacao(Avaliacao.Satisfacao.PERFEITO);

        when(criarAvaliacaoUsecase.criar(any())).thenReturn(avaliacao);

        // Act & Assert
        mockMvc.perform(post("/api/v1/reservas/avaliar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarAvaliacaoJson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
        verify(criarAvaliacaoUsecase, times(1)).criar(any());
    }

    @Test
    void listarReservas_DeveRetornarStatusOkComDados() throws Exception {
        // Arrange
        String requestParams = "?restauranteId=2&dataReserva=2024-11-20T15:30:00";
        List<Reserva> reservas = List.of(new Reserva(1L, 1L, 10, "Leo", LocalDateTime.parse("2024-11-20T15:30:00"), Reserva.Status.PENDENTE));

        when(listarReservasUsecase.listarReservas(2L, LocalDateTime.parse("2024-11-20T15:30:00"))).thenReturn(reservas);

        // Act & Assert
        mockMvc.perform(get("/api/v1/reservas" + requestParams)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservas[0].reservaId").value(1))
                .andExpect(jsonPath("$.reservas[0].restauranteId").value(1))
                .andExpect(jsonPath("$.reservas[0].quantidadeLugares").value(10))
                .andExpect(jsonPath("$.reservas[0].nomeCliente").value("Leo"))
                .andExpect(jsonPath("$.reservas[0].inicioReserva").value("2024-11-20T15:30:00"))
                .andExpect(jsonPath("$.reservas[0].status").value("PENDENTE"));

        verify(listarReservasUsecase, times(1)).listarReservas(2L, LocalDateTime.parse("2024-11-20T15:30:00"));
    }

    @Test
    void atualizarReserva_DeveRetornarStatusOkComDados() throws Exception {
        // Arrange
        AtualizarReservaJson atualizarReservaJson = new AtualizarReservaJson(1L, 1L, 10, "Leo", LocalDateTime.parse("2024-11-20T15:30:00"), Reserva.Status.PENDENTE);
        Reserva reserva = new Reserva(1L, 1L, 10, "Leo", LocalDateTime.parse("2024-11-20T15:30:00"), Reserva.Status.PENDENTE);

        when(gerenciarReservasUsecase.atualizarReserva(any())).thenReturn(reserva);

        // Act & Assert
        mockMvc.perform(put("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(atualizarReservaJson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reserva.reservaId").value(1))
                .andExpect(jsonPath("$.reserva.restauranteId").value(1))
                .andExpect(jsonPath("$.reserva.quantidadeLugares").value(10))
                .andExpect(jsonPath("$.reserva.nomeCliente").value("Leo"))
                .andExpect(jsonPath("$.reserva.inicioReserva").value("2024-11-20T15:30:00"))
                .andExpect(jsonPath("$.reserva.status").value("PENDENTE"));

        verify(gerenciarReservasUsecase, times(1)).atualizarReserva(any());
    }
}
