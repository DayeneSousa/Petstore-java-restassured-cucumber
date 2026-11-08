package com.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class ListarPetsSteps {

    private Response response;

    @Given("que o usuario deseja listar todos os pets")
    public void queOUsuarioDesejaListarTodosOsPets() {


    }

    @When("ele envia uma requisicao GET para o endpoint {string}")
    public void eleEnviaUmaRequisicaoGetParaOEndpoint(String endpoint) {
        response = given()
                .when()
                .get("https://petstore.swagger.io/v2/pet/findByStatus?status=available");
    }

    @Then("ele deve receber uma resposta com status code {int} com a lista de pets")
    public void eleDeveReceberUmaRespostaComStatusCodeComListaDePets(Integer statusCode) {
        assertThat(response.getStatusCode(), equalTo(statusCode));

        int quantidadePets = response.jsonPath().getList("$").size();
        assertThat(quantidadePets, greaterThan(0));

        // Exibe no log a quantidade e a lista completa retornada pela API.
        System.out.println("Quantidade de pets retornados: " + quantidadePets);
        System.out.println("Lista de pets:\n" + response.getBody().asPrettyString());
    }
}
