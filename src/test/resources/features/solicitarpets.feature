Feature: Solicitar Pets

  Scenario: Solicitar um pet 12
    Given que o usuario deseja solicitar um pet
    When ele envia uma requisicao POST para o endpoint "/v2/store/order"
    Then ele deve receber uma resposta com status code 200 com a confirmacao da solicitacao
        And deve exibir o Body da resposta com os detalhes do pet solicitado