Feature: Listar Pets

  Scenario: Listar todos os pets 12
    Given que o usuario deseja listar todos os pets
    When ele envia uma requisicao GET para o endpoint "/pets"
    Then ele deve receber uma resposta com status code 200 com a lista de pets
