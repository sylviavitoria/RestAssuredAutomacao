# 🌀 Projeto de Testes de API - Naruto Ninja REST

Este projeto tem como objetivo testar uma API REST de gerenciamento de personagens do universo Naruto, utilizando **RestAssured** — uma biblioteca Java para testes automatizados de APIs REST — em conjunto com **BDD (Behavior Driven Development)**, uma abordagem de desenvolvimento orientada ao comportamento que permite descrever testes de forma legível e próxima da linguagem natural.

---

## 🔧 Tecnologias Utilizadas

- Java  
- RestAssured  
- JUnit  
- Cucumber (para BDD)  
- Maven (gerenciador de dependências)  

---

## 🧪 Funcionalidades Testadas

### 📁 Funcionalidade: Gerenciamento de Personagens do Naruto

Testes relacionados à criação, listagem, atualização e remoção de personagens ninjas:

```gherkin
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
    Quando busco um personagem com ID inexistente
    Então deve retornar status 404
    E deve retornar mensagem de personagem não encontrado
```

---

### 🔐 Funcionalidade: Autenticação de Usuários

```gherkin
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
    Quando faço uma requisição POST e os dados são de usuário inexistente
    Então devo receber status 401
    E a resposta deve conter a mensagem "Credenciais inválidas"
```

---

### ⚔️ Funcionalidade: Sistema de Batalhas entre Ninjas

```gherkin
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
```

---

## 🗂️ Organização dos Arquivos

```
src/
└── test/
    └── java/
        └── bdd/
            └── automation/
                └── api/
                    ├── steps → Definições dos passos do BDD
                    └── CucumberRunner.java → Classe runner do Cucumber
    └── resources/
        └── features → Arquivos .feature

pom.xml → Dependências RestAssured + Cucumber
```

---

## ▶️ Executando os Testes

### 1. Clone o repositório da API
```bash
git clone https://github.com/sylviavitoria/DesafioNaruto.git
```
### e o repositório da automação
```bash
https://github.com/sylviavitoria/RestAssuredAutomacao
```

### 2. Siga as intruções do repositório da API Naruto, onde vai fazer as configurações essencias para rodar a aplição

3. Rode a API com:

```bash
mvn spring-boot:run
```

> ⚠️ **Importante:** Rodar localmente com `mvn spring-boot:run` garante melhor resultado do que via Docker.

4. Baixe o plugin do Cucumber no seu IDE, aconselhado usar IntelliJ IDEA.
4. Execute os testes clicando no botão de execução do arquivo `CucumberRunner.java`.

---
