package com.example.steps;

import groovy.json.JsonOutput;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class AlterarDadosPetSteps {

    private static final String BASE_URL = "https://petstore.swagger.io";

    private Response response;
    private String responseBody;

    @Given("que o usuario deseja alterar os dados de um pet existente")
    public void queOUsuarioDesejaAlterarOsDadosDeUmPetExistente() {
        // Contexto preparado; a alteracao acontece no passo When.
    }

    @When("ele envia uma requisicao PUT para o endpoint {string} com os novos dados do pet")
    public void eleEnviaUmaRequisicaoPUTParaOEndpointComOsNovosDadosDoPet(String endpointAlterarDadosPet) {
        String jsonBase = carregarFixture("fixtures/alterardadospet.json");
        Map<String, Object> payload = JsonPath.from(jsonBase).getMap("$");

        // Atualiza campos dinamicamente antes do PUT.
        payload.put("id", 12);
        payload.put("name", "pet-atualizado");
        payload.put("status", "sold");

        String payloadAtualizado = JsonOutput.toJson(payload);

        response = given()
                .contentType("application/json")
                .body(payloadAtualizado)
                .when()
                .put(BASE_URL + endpointAlterarDadosPet);

        responseBody = response.getBody().asString();
    }

    @Then("ele deve receber uma resposta com status code {int} confirmando a alteracao dos dados do pet")
    public void eleDeveReceberUmaRespostaComStatusCodeConfirmandoAAlteracaoDosDadosDoPet(int statusCode) {
        assertThat(response.getStatusCode(), equalTo(statusCode));
        assertThat(response.jsonPath().get("id"), notNullValue());
        assertThat(response.jsonPath().getInt("id"), equalTo(12));
    }

    @And("deve exibir o Body da resposta com os detalhes atualizados do pet")
    public void deveExibirOBodyDaRespostaComOsDetalhesAtualizadosDoPet() {
        System.out.println("A alteracao foi realizada com sucesso:\n" + responseBody);
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
