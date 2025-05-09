package bdd.automation.api.steps;

import bdd.automation.api.TestApplication;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;
import io.cucumber.docstring.DocString;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;

@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@CucumberContextConfiguration
public class PersonagemStepsDefinitions {

    private Response response;
    private String jsonBody;
    private int personagemId;

    @Before
    public void setup() {
        baseURI = "http://localhost";
        port = 8080;
        basePath = "/api/v1";
    }

    @DisplayName("Criar um novo personagem ninja")
    @Dado("que quero criar um novo personagem ninja")
    public void queQueroCriarUmNovoPersonagemNinja() {
    }

    @Quando("envio uma requisição POST para {string} com os dados:")
    public void envioUmaRequisicaoPostParaComOsDados(String path, DocString docString) {
        this.jsonBody = docString.getContent();
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(this.jsonBody)
                .when()
                .post(path);

        System.out.println("Resposta: " + response.asPrettyString());
    }

    @Então("o personagem deve ser criado com sucesso")
    public void oPersonagemDeveSerCriadoComSucesso() {
        response.then()
                .body("nome", equalTo("Naruto Uzumaki"))
                .body("idade", equalTo(17))
                .body("aldeia", equalTo("Aldeia da Folha"))
                .body("chakra", equalTo(100))
                .body("tipoNinja", equalTo("NinjaDeNinjutsu"))
                .body("jutsusDetalhes.Rasengan.dano", equalTo(70))
                .body("jutsusDetalhes.Rasengan.consumoDeChakra", equalTo(10))
                .body("jutsusDetalhes['Kage Bunshin'].dano", equalTo(40))
                .body("jutsusDetalhes['Kage Bunshin'].consumoDeChakra", equalTo(10));
    }

    @DisplayName("Buscar um personagem por ID")
    @Dado("que existe um personagem cadastrado com ID {int}")
    public void queExisteUmPersonagemCadastradoComID(int id) {
        personagemId = id;
    }

    @Quando("faço uma requisição GET para {string}")
    public void facoUmaRequisicaoGET(String path) {
        this.response = given()
                .when()
                .get(path);

        System.out.println("Resposta: " + response.asPrettyString());
    }

    @Então("devo receber os dados do personagem")
    public void devoReceberOsDadosDoPersonagem() {
        response.then()
                .body("id", equalTo(personagemId))
                .body("nome", notNullValue())
                .body("idade", notNullValue())
                .body("aldeia", notNullValue());
    }

    @E("deve retornar 200")
    public void deveRetornar200() {
        response.then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK);
    }

    @E("os dados devem corresponder ao personagem cadastrado")
    public void osDadosDevemCorresponderAoPersonagemCadastrado() {
        response.then()
                .body("nome", equalTo("Naruto Uzumaki"))
                .body("aldeia", equalTo("Aldeia da Folha"));
    }

    @Dado("que existem personagens cadastrados no sistema")
    public void que_existem_personagens_cadastrados_no_sistema() {

    }

    @Quando("solicito a página {int} com tamanho {int} de personagens")
    public void solicito_a_página_com_tamanho_de_personagens(Integer pagina, Integer tamanho) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("page", pagina)
                .param("size", tamanho)
                .when()
                .get("/personagens");

        System.out.println("Resposta da paginação: " + response.asPrettyString());
    }

    @Então("a resposta deve conter informações básicas de paginação")
    public void a_resposta_deve_conter_informações_básicas_de_paginação() {
        response.then()
                .statusCode(200)
                .body("pageable", notNullValue())
                .body("pageable.pageNumber", notNullValue())
                .body("pageable.pageSize", notNullValue())
                .body("content", notNullValue())
                .body("totalElements", notNullValue())
                .body("totalPages", notNullValue())
                .body("size", notNullValue())
                .body("number", notNullValue());

        System.out.println("Verificação de paginação concluída com sucesso!");

        response.then()
                .body("pageable.pageNumber", equalTo(0))
                .body("pageable.pageSize", equalTo(10));
    }

    @E("deve retornar status código 200")
    public void deveRetornarStatus200() {
        response.then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK);
    }/////////////

    @DisplayName("Atualizar personagem")
    @Quando("envio uma requisição PUT para atualizar")
    public void envioUmaRequisicaoPUTParaAtualizar() {
        jsonBody = """
            {
              "nome": "Naruto Uzumaki Atualizado",
              "idade": 19,
              "aldeia": "Aldeia da Folha",
              "chakra": 120,
              "jutsus": ["Rasengan", "Kage Bunshin", "Modo Sábio"]
            }
            """;

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonBody)
                .when()
                .put("/personagens/" + personagemId);

        System.out.println("Resposta da atualização: " + response.asPrettyString());
    }

    @Então("o personagem deve ser atualizado com sucesso")
    public void oPersonagemDeveSerAtualizadoComSucesso() {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("id", equalTo(personagemId));
    }

    @E("os dados atualizados devem estar corretos")
    public void osDadosAtualizadosDevemEstarCorretos() {
        response.then()
                .body("nome", equalTo("Naruto Uzumaki Atualizado"))
                .body("idade", equalTo(19))
                .body("chakra", equalTo(120))
                .body("jutsus", hasItems("Rasengan", "Kage Bunshin"));

        System.out.println("Jutsus retornados: " + response.jsonPath().getList("jutsus"));
    }

    @DisplayName("Adicionar jutsu a um personagem")
    @Quando("envio uma requisição POST para um novo jutsu")
    public void envioUmaRequisicaoPOSTParaUmNovoJutsu() {
        jsonBody = """
            {
              "nome": "Rasenshuriken",
              "dano": 90,
              "consumoDeChakra": 30
            }
            """;

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonBody)
                .when()
                .post("/personagens/" + personagemId + "/jutsus");

        System.out.println("Resposta da adição de jutsu: " + response.asPrettyString());
    }

    @Então("o jutsu deve ser adicionado com sucesso")
    public void oJutsuDeveSerAdicionadoComSucesso() {
        response.then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .body("id", equalTo(personagemId));
    }

    @E("o personagem deve possuir o novo jutsu")
    public void oPersonagemDevePossuirONovoJutsu() {
        Response getResponse = given()
                .when()
                .get("/personagens/" + personagemId);

        getResponse.then()
                .statusCode(HttpStatus.SC_OK)
                .body("jutsus", hasItem("Rasenshuriken"));

        getResponse.then()
                .body("jutsusDetalhes.Rasenshuriken.dano", equalTo(90))
                .body("jutsusDetalhes.Rasenshuriken.consumoDeChakra", equalTo(30));

        System.out.println("Jutsus do personagem após adição: " + getResponse.jsonPath().getMap("jutsusDetalhes"));
    }

    @DisplayName("Remover personagem")
    @Quando("envio uma requisição DELETE para {string}")
    public void envioUmaRequisicaoDELETEPara(String path) {
        response = given()
                .when()
                .delete(path);

        System.out.println("Resposta da remoção: " + response.asPrettyString());
    }

    @Então("o personagem deve ser removido com sucesso")
    public void oPersonagemDeveSerRemovidoComSucesso() {
        response.then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        System.out.println("Personagem " + personagemId + " foi removido com sucesso");
    }

    @E("deve retornar status NO_CONTENT")
    public void deveRetornarStatus204() {
        response.then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    // ----- CENÁRIO: Criar personagem com nome vazio -----
    @Quando("envio uma requisição com nome vazio")
    public void envioUmaRequisicaoComNomeVazio() {
        jsonBody = """
            {
              "tipoNinja": "NINJUTSU",
              "nome": "",
              "idade": 17,
              "aldeia": "Aldeia da Folha",
              "chakra": 100,
              "jutsus": {
                "Rasengan": {
                  "dano": 70,
                  "consumoDeChakra": 10
                }
              }
            }
            """;

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonBody)
                .when()
                .post("/personagens");

        System.out.println("Resposta: " + response.asPrettyString());
    }

    // ----- CENÁRIO: Criar personagem com idade inválida -----
    @Quando("envio uma requisição com idade inválida")
    public void envioUmaRequisicaoComIdadeInvalida() {
        jsonBody = """
            {
              "tipoNinja": "NINJUTSU",
              "nome": "Naruto Uzumaki",
              "idade": -5,
              "aldeia": "Aldeia da Folha",
              "chakra": 100,
              "jutsus": {
                "Rasengan": {
                  "dano": 70,
                  "consumoDeChakra": 10
                }
              }
            }
            """;

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonBody)
                .when()
                .post("/personagens");

        System.out.println("Resposta: " + response.asPrettyString());
    }

    // ----- CENÁRIO: Criar personagem com chakra negativo -----
    @Quando("envio uma requisição com chakra negativo")
    public void envioUmaRequisicaoComChakraNegativo() {
        jsonBody = """
            {
              "tipoNinja": "NINJUTSU",
              "nome": "Naruto Uzumaki",
              "idade": 17,
              "aldeia": "Aldeia da Folha",
              "chakra": -50,
              "jutsus": {
                "Rasengan": {
                  "dano": 70,
                  "consumoDeChakra": 10
                }
              }
            }
            """;

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonBody)
                .when()
                .post("/personagens");

        System.out.println("Resposta: " + response.asPrettyString());
    }

    // ----- CENÁRIO: Buscar personagem com ID inexistente -----
    @Quando("busco um personagem com ID inexistente")
    public void buscoUmPersonagemComIDInexistente() {
        response = given()
                .when()
                .get("/personagens/999");

        System.out.println("Resposta: " + response.asPrettyString());
    }

    @E("deve retornar mensagem de personagem não encontrado")
    public void deveRetornarMensagemDePersonagemNaoEncontrado() {
        response.then()
                .body(containsString("Personagem não encontrado"));
    }

    // ----- Métodos comuns usados em múltiplos cenários -----
    @Então("deve retornar status {int}")
    public void deveRetornarStatus(int statusCode) {
        response.then()
                .statusCode(statusCode);
    }

    @E("deve retornar a mensagem {string}")
    public void deveRetornarAMensagem(String mensagemErro) {
        response.then()
                .body(containsString(mensagemErro));
    }

    @E("o personagem não deve ser criado")
    public void oPersonagemNaoDeveSerCriado() {
        response.then()
                .body("id", nullValue());
    }
}