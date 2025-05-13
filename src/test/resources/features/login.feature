# language: pt

Funcionalidade: Autenticação de Usuários

  Cenário: Login com credenciais válidas
    Dado que existe um usuário cadastrado com nome "admin" e senha "admin123"
    Quando faço uma requisição POST
    Então devo receber status 200
    E a resposta deve conter um token JWT válido

  Cenário: Retorno de token JWT válido
    Dado que realizei login com credenciais válidas
    Quando utilizo o token recebido para acessar "/personagens"
    Então devo receber status 200


  Cenário: Login com credenciais inválidas
    Dado que tento fazer login com credenciais inválidas
    Quando faço uma requisição POST e os dados são credenciais inválidas
    Então devo receber status 401
    E a resposta deve conter a mensagem "Credenciais inválidas"

  Cenário: Login com usuário inexistente
    Quando faço uma requisição POST e os dados são usuário inexistente
    Então devo receber status 401
    E a resposta deve conter a mensagem "Credenciais inválidas"




