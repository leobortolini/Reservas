package br.com.reservas.Reservas.controller;

import br.com.reservas.Reservas.controller.json.*;
import br.com.reservas.Reservas.domain.Avaliacao;
import br.com.reservas.Reservas.gateway.rest.json.QuantidadeDeLugaresResponseJson;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class ReservasControllerIT {

    @LocalServerPort
    private int port;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        QuantidadeDeLugaresResponseJson responseJson = new QuantidadeDeLugaresResponseJson(1L, 1000L);

        when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(QuantidadeDeLugaresResponseJson.class)))
                .thenReturn(new ResponseEntity<>(responseJson, HttpStatus.OK));
    }

    @Nested
    class Reserva {
        @Test
        void criarReserva_DeveRetornarStatusCreatedQuandoDadosValidos() {
            CriarReservaJson criarReservaJson = new CriarReservaJson(
                    1L,
                    4,
                    "Cliente Teste",
                    LocalDateTime.parse("2024-12-01T18:00:00")
            );

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(criarReservaJson)
                    .when()
                    .post("/api/v1/reservas")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("$", hasKey("id"));
        }

        @Test
        @Sql(scripts = {"/clean.sql", "/reservas.sql"})
        void listarReservas_DeveRetornarStatusOkComDados() {
            ListarReservasResponseJson extractableResponse = given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/api/v1/reservas?restauranteId=1&dataReserva=2024-11-21T19:00:00")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("reservas")).extract().as(ListarReservasResponseJson.class);

            assertFalse(extractableResponse.reservas().isEmpty());
            assertSame(1, extractableResponse.reservas().size());
            assertSame(1, extractableResponse.reservas().size());
            assertEquals("João Silva", extractableResponse.reservas().getFirst().getNomeCliente());
        }

        @Test
        @Sql(scripts = {"/clean.sql", "/reservas.sql"})
        void atualizarReserva_DeveRetornarReservaAtualizada() {
            AtualizarReservaJson atualizarReservaJson = new AtualizarReservaJson(1L,
                    1L, 10,
                    "João Silva", LocalDateTime.parse("2024-11-20T15:30:00"),
                    br.com.reservas.Reservas.domain.Reserva.Status.PENDENTE);

            AtualizarReservaResponseJson atualizarReservaResponseJson = given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(atualizarReservaJson)
                    .when()
                    .put("/api/v1/reservas")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("reserva")).extract().as(AtualizarReservaResponseJson.class);

            assertEquals(LocalDateTime.parse("2024-11-20T15:30:00"), atualizarReservaResponseJson.reserva().getInicioReserva());
            assertSame(10, atualizarReservaResponseJson.reserva().getQuantidadeLugares());
        }

        @Test
        @Sql(scripts = {"/clean.sql", "/reservas.sql"})
        void avaliarReserva_DeveRetornarIdDaAvaliacao() {
            CriarAvaliacaoJson criarAvaliacaoJson = new CriarAvaliacaoJson(
                    2L,
                    Avaliacao.Satisfacao.PERFEITO,
                    "Comentario Teste");

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(criarAvaliacaoJson)
                    .when()
                    .post("/api/v1/reservas/avaliar")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("$", hasKey("id"));
        }
    }
}
