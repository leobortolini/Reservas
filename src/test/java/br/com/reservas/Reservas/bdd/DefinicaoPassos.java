package br.com.reservas.Reservas.bdd;

import br.com.reservas.Reservas.controller.json.AtualizarReservaJson;
import br.com.reservas.Reservas.controller.json.AtualizarReservaResponseJson;
import br.com.reservas.Reservas.controller.json.CriarReservaJson;
import br.com.reservas.Reservas.controller.json.CriarReservaResponseJson;
import br.com.reservas.Reservas.domain.Avaliacao;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

public class DefinicaoPassos {

    private Response response;

    private CriarReservaResponseJson reserva;

    private Avaliacao avaliacao;

    private String endpoint = "http://localhost:8080/api/v1/reservas";


    @Quando("criar nova reserva")
    public CriarReservaResponseJson criarNovaReserva() {
        CriarReservaJson criarReservaJson = new CriarReservaJson(
                1L,
                4,
                "Cliente Teste",
                LocalDateTime.parse("2024-12-01T18:00:00")
        );

        response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(criarReservaJson).when().post(endpoint);

        return response.then().extract().as(CriarReservaResponseJson.class);
    }

    @Então("a reserva é criada com sucesso")
    public void reservaCriada() {
        response.then().statusCode(HttpStatus.CREATED.value()).body("$", hasKey("id"));
    }

    @Então("atualizo reserva")
    public AtualizarReservaResponseJson atualizarReserva() {
        AtualizarReservaJson atualizarReservaJson = new AtualizarReservaJson(reserva.id(),
                1L, 10,
                "João Silva", LocalDateTime.parse("2024-11-20T15:30:00"),
                br.com.reservas.Reservas.domain.Reserva.Status.PENDENTE);

        response = given().contentType(MediaType.APPLICATION_JSON_VALUE).put(endpoint);

        return response.then().extract().as(AtualizarReservaResponseJson.class);
    }
}
