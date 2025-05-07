package bdd.automation.api.steps;

import bdd.automation.api.TestApplication;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;
import io.cucumber.docstring.DocString;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

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

    @DisplayName("Criar um novo personagem ninja")
    @Dado("que quero criar um novo personagem ninja")
    public void queQueroCriarUmNovoPersonagemNinja() {
        baseURI = "http://localhost";
        port = 8080;
        basePath = "/api/v1";
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

    @E("deve retornar status Created")
    public void deveRetornarStatusCreated() {
        response.then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED);
    }

    private int personagemId;

    @Dado("que existe um personagem cadastrado com ID {int}")
    public void queExisteUmPersonagemCadastradoComID(int id) {
        baseURI = "http://localhost";
        port = 8080;
        basePath = "/api/v1";
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

    @DisplayName("Verificar status code OK")
    @E("deve retornar status OK")
    public void deveRetornarStatusOK() {
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
}