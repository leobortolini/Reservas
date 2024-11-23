package br.com.reservas.Reservas.bdd;

import br.com.reservas.Reservas.controller.json.*;
import br.com.reservas.Reservas.domain.Avaliacao;
import br.com.reservas.Reservas.domain.Reserva;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.*;

public class DefinicaoPassos {

    private Response response;

    private CriarReservaResponseJson reserva;

    private String endpoint = "http://localhost:8080/api/v1/reservas";

    @Quando("criar nova reserva")
    public void criarNovaReserva() {
        CriarReservaJson criarReservaJson = new CriarReservaJson(
                2L,
                4,
                "Cliente Teste",
                LocalDateTime.parse("2024-12-01T18:00:00")
        );

        response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(criarReservaJson).when().post(endpoint);
        reserva = response.then().extract().as(CriarReservaResponseJson.class);
    }

    @Então("a reserva é criada com sucesso")
    public void reservaCriada() {
        reserva = response.then().statusCode(HttpStatus.CREATED.value()).body("$", hasKey("id")).extract().as(CriarReservaResponseJson.class);
        System.out.println();
    }

    @Dado("que uma reserva exista")
    public void reservaExista() {
        criarNovaReserva();
    }

    @Quando("requisitar atualização da reserva")
    public void requisitarAtualizacaoDaReserva() {
        AtualizarReservaJson atualizarReservaJson = new AtualizarReservaJson(reserva.id(),
                1L, 10,
                "João Silva", LocalDateTime.parse("2024-11-20T15:30:00"),
                Reserva.Status.INICIADA);

        response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(atualizarReservaJson).when().put(endpoint);
    }

    @Então("a reserva é atualizada com sucesso")
    public void atualizarReserva() {
        response.then().statusCode(HttpStatus.OK.value()).body("$", hasKey("reserva"));
    }

    @Dado("que mais de uma reserva exista")
    public void criarMultiplasReservas() {
        criarNovaReserva();
        criarNovaReserva();
        criarNovaReserva();
    }

    @Quando("listar as reservas")
    public void listarReservas() {
        response = given().contentType(MediaType.APPLICATION_JSON_VALUE).when().get(endpoint + "?restauranteId=2&dataReserva=2024-12-01T18:00:00");
    }

    @Então("as reservas são listadas")
    public void conferirListaDeReservas() {
        ListarReservasResponseJson reservas = response.then().extract().as(ListarReservasResponseJson.class);

        assertFalse(reservas.reservas().isEmpty());
        assertTrue(reservas.reservas().size() > 1);
    }

    @Dado("que uma reserva esteja iniciada")
    public void iniciarReservaPendente() {
        criarNovaReserva();
        requisitarAtualizacaoDaReserva();
    }

    @Quando("avaliar a reserva")
    public void avaliarReserva() {
        CriarAvaliacaoJson criarAvaliacaoJson = new CriarAvaliacaoJson(
                reserva.id(),
                Avaliacao.Satisfacao.PERFEITO,
                "Comentario Teste");

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(criarAvaliacaoJson)
                .when()
                .post(endpoint + "/avaliar");
    }

    @Então("a reserva é avaliada com sucesso")
    public void conferirAvaliacao() {
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("$", hasKey("id"));
    }

}
