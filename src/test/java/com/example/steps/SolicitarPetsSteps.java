package com.example.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class SolicitarPetsSteps {

    private static final String BASE_URL = "https://petstore.swagger.io";
    private String payload;
    private String responseBody;
    private Response response;

    @Given("que o usuario deseja solicitar um pet")
    public void queOUsuarioDesejaSolicitarUmPet() {
        // Contexto do cenário preparado; a requisição é enviada no passo When.
    }

    @When("ele envia uma requisicao POST para o endpoint {string}")
    public void eleEnviaUmaRequisicaoPOSTParaOEndpoint(String endpointSolicitarPets) {
        payload = carregarFixture("fixtures/solicitar-pet.json");

        response = given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(BASE_URL + endpointSolicitarPets);

        responseBody = response.getBody().asString();
    }

    @Then("ele deve receber uma resposta com status code {int} com a confirmacao da solicitacao")
    public void eleDeveReceberUmaRespostaComStatusCodeComAConfirmacaoDaSolicitacao(int statusCode) {
        assertThat(response.getStatusCode(), equalTo(statusCode));
        assertThat(response.jsonPath().get("id"), notNullValue());
        assertThat(response.jsonPath().getInt("petId"), equalTo(12));
    }

    @And("deve exibir o Body da resposta com os detalhes do pet solicitado")
    public void deveExibirOBodyDaRespostaComOsDetalhesDoPetSolicitado() {
        System.out.println("Body da resposta da solicitacao:\n" + responseBody);
    }

    private String carregarFixture(String caminhoNoResources) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(caminhoNoResources)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Fixture nao encontrada: " + caminhoNoResources);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler fixture: " + caminhoNoResources, e);
        }
    }
}
