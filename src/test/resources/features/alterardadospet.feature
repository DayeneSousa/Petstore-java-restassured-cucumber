Feature: Alterar Dados do Pet

  Scenario: Alterar os dados de um pet existente
    Given que o usuario deseja alterar os dados de um pet existente
    When ele envia uma requisicao PUT para o endpoint "/v2/pet" com os novos dados do pet
    Then ele deve receber uma resposta com status code 200 confirmando a alteracao dos dados do pet
        And deve exibir o Body da resposta com os detalhes atualizados do pet