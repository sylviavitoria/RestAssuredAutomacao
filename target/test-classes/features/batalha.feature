# language: pt

Funcionalidade: Sistema de Batalhas entre Ninjas

  Cenário: Batalha utilizando jutsu avançado
    Dado que existe um ninja atacante "Sasuke" com chakra 100
    E que existe um ninja defensor "Itachi" com vida 100
    E que o atacante possui o jutsu "Amaterasu" com dano 70 e consumo 10
    Quando o atacante usa "Amaterasu" contra o defensor
    Então o ataque deve ser bem sucedido
    E o log deve conter "Sasuke usa Amaterasu"
    E o chakra do atacante deve ser reduzido em 10
    E a vida do defensor deve ser reduzida para 30 se não conseguir desviar

  Cenário: Atacar com jutsu inexistente
    Dado que existe um ninja atacante "Rock Lee"
    Quando tento usar o jutsu "JutsuInexistente"
    Então deve retornar erro "Jutsu não encontrado"
    E nenhum dano deve ser causado

  Cenário: Atacar a si mesmo
    Dado que existe um ninja "Naruto"
    Quando tento atacar o mesmo ninja
    Então deve retornar erro "Não é possível atacar a si mesmo"
