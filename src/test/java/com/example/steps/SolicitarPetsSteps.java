package com.example.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class SolicitarPetsSteps {

    private static final String BASE_URL = System.getProperty(
            "base.url",
            System.getenv().getOrDefault("BASE_URL", "https://petstore.swagger.io")
    );

    private static final String FIXTURE_DUAS_REQUISICOES = "fixtures/solicitarpets_02.json";

    private final List<Response> responses = new ArrayList<>();
    private final List<Integer> statusCodes = new ArrayList<>();
    private int quantidadeExecucoes;
    private int petIdEsperado;
    private String responseBody;
    private Response response;

    @Given("que o usuario deseja solicitar um pet")
    public void queOUsuarioDesejaSolicitarUmPet() {
        // Contexto do cenário preparado; a requisição é enviada no passo When.
    }

    @When("ele envia uma requisicao POST para o endpoint {string}")
    public void eleEnviaUmaRequisicaoPOSTParaOEndpoint(String endpointSolicitarPets) {
        enviarRequisicoes(endpointSolicitarPets, 2);
    }

    @When("ele envia {int} requisicoes POST para o endpoint {string}")
    public void eleEnviaRequisicoesPOSTParaOEndpoint(int quantidade, String endpointSolicitarPets) {
        enviarRequisicoes(endpointSolicitarPets, quantidade);
    }

    private void enviarRequisicoes(String endpointSolicitarPets, int quantidade) {
        if (quantidade < 1) {
            throw new IllegalArgumentException("A quantidade de requisicoes deve ser maior que zero.");
        }

        String payload = carregarFixture(FIXTURE_DUAS_REQUISICOES);
        responses.clear();
        statusCodes.clear();
        quantidadeExecucoes = quantidade;
        petIdEsperado = JsonPath.from(payload).getInt("petId");

        for (int i = 0; i < quantidadeExecucoes; i++) {
            response = given()
                    .contentType("application/json")
                    .body(payload)
                    .when()
                    .post(BASE_URL + endpointSolicitarPets);

            responses.add(response);
            statusCodes.add(response.getStatusCode());
            responseBody = response.getBody().asString();
        }
    }

    @Then("ele deve receber uma resposta com status code {int} com a confirmacao da solicitacao")
    public void eleDeveReceberUmaRespostaComStatusCodeComAConfirmacaoDaSolicitacao(int statusCode) {
        assertThat(responses.size(), equalTo(quantidadeExecucoes));
        assertThat(statusCodes.size(), equalTo(quantidadeExecucoes));

        for (int i = 0; i < quantidadeExecucoes; i++) {
            assertThat(statusCodes.get(i), equalTo(statusCode));
            assertThat(responses.get(i).jsonPath().get("id"), notNullValue());
            assertThat(responses.get(i).jsonPath().getInt("petId"), equalTo(petIdEsperado));
        }
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
