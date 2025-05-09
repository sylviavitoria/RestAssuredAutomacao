# language: pt

Funcionalidade: Gerenciamento de Personagens do Naruto

  Cenário: Criar personagem com dados válidos
    Dado que quero criar um novo personagem ninja
    Quando envio uma requisição POST para "/personagens" com os dados:
    """json
    {
      "tipoNinja": "NINJUTSU",
      "nome": "Naruto Uzumaki",
      "idade": 17,
      "aldeia": "Aldeia da Folha",
      "chakra": 100,
      "jutsus": {
        "Rasengan": {
          "dano": 70,
          "consumoDeChakra": 10
        },
        "Kage Bunshin": {
          "dano": 40,
          "consumoDeChakra": 10
        }
      }
    }
    """
    Então o personagem deve ser criado com sucesso
    E deve retornar status 201

  Cenário: Buscar personagem por ID
    Dado que existe um personagem cadastrado com ID 1
    Quando faço uma requisição GET para "/personagens/1"
    Então devo receber os dados do personagem
    E deve retornar 200
    E os dados devem corresponder ao personagem cadastrado

  Cenário: Listar personagens com paginação
    Dado que existem personagens cadastrados no sistema
    Quando solicito a página 0 com tamanho 10 de personagens
    Então a resposta deve conter informações básicas de paginação
    E deve retornar status código 200

  Cenário: Atualizar informações de um personagem
    Dado que existe um personagem cadastrado com ID 1
    Quando envio uma requisição PUT para atualizar
    Então o personagem deve ser atualizado com sucesso
    E deve retornar status 200
    E os dados atualizados devem estar corretos

  Cenário: Adicionar jutsu a um personagem
    Dado que existe um personagem cadastrado com ID 1
    Quando envio uma requisição POST para um novo jutsu
    Então o jutsu deve ser adicionado com sucesso
    E deve retornar status 201
    E o personagem deve possuir o novo jutsu

  Cenário: Remover personagem existente
    Dado que existe um personagem cadastrado com ID 1
    Quando envio uma requisição DELETE para "/personagens/1"
    Então o personagem deve ser removido com sucesso
    E deve retornar status 204

  Cenário: Criar personagem com nome vazio
    Dado que quero criar um novo personagem ninja
    Quando envio uma requisição com nome vazio
    Então deve retornar status 400
    E deve retornar a mensagem "Nome não pode ser vazio ou nulo"
    E o personagem não deve ser criado

  Cenário: Criar personagem com idade inválida
    Dado que quero criar um novo personagem ninja
    Quando envio uma requisição com idade inválida
    Então deve retornar status 400
    E deve retornar a mensagem "Idade deve ser maior que zero"
    E o personagem não deve ser criado

  Cenário: Criar personagem com chakra negativo
    Dado que quero criar um novo personagem ninja
    Quando envio uma requisição com chakra negativo
    Então deve retornar status 400
    E deve retornar a mensagem "Chakra não pode ser negativo"
    E o personagem não deve ser criado

  Cenário: Buscar personagem com ID inexistente
    Dado que quero criar um novo personagem ninja
    Quando busco um personagem com ID inexistente
    Então deve retornar status 404
    E deve retornar mensagem de personagem não encontrado

