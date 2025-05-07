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
    E deve retornar status Created

  Cenário: Buscar personagem por ID
    Dado que existe um personagem cadastrado com ID 1
    Quando faço uma requisição GET para "/personagens/1"
    Então devo receber os dados do personagem
    E deve retornar status OK
    E os dados devem corresponder ao personagem cadastrado