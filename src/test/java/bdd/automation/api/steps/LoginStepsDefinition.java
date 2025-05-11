package bdd.automation.api.steps;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;

public class LoginStepsDefinition extends BaseStepDefinitions {
    private Response response;
    private String jsonBody;
    private String username;
    private String password;
    private String authToken;

    @Before
    public void setup() {
        baseURI = "http://localhost";
        port = 8080;
        basePath = "/api/v1";
    }

    @Dado("que existe um usuário cadastrado com nome {string} e senha {string}")
    public void queExisteUmUsuarioCadastradoComNomeESenha(String username, String password) {
        this.username = "admin";
        this.password = "admin123";

    }

    @Quando("faço uma requisição POST")
    public void facoUmaRequisicaoPOST() {
        jsonBody = String.format("""
            {
              "username": "%s",
              "password": "%s"
            }
            """, username, password);

        this.response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/auth/login");

        System.out.println("Resposta de login: " + response.asPrettyString());
    }

    @Então("devo receber status {int}")
    public void devoReceberStatus(int statusCode) {
        response.then()
                .statusCode(statusCode);
    }

    @E("a resposta deve conter um token JWT válido")
    public void aRespostaDeveConterUmTokenJWTValido() {
        response.then()
                .body("token", notNullValue())
                .body("token", startsWith("eyJ"));

        String token = response.jsonPath().getString("token");
        assertNotNull(token, "O token não deve ser nulo");

        String[] tokenParts = token.split("\\.");
        assertEquals(3, tokenParts.length, "O token JWT deve ter 3 partes");
    }

    @Dado("que realizei login com credenciais válidas")
    public void queRealizeiLoginComCredenciaisValidas() {

        this.username = "admin";
        this.password = "admin123";

        jsonBody = String.format("""
        {
          "username": "%s",
          "password": "%s"
        }
        """, username, password);

        Response loginResponse = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/auth/login");

        authToken = loginResponse.jsonPath().getString("token");


        loginResponse.then().statusCode(HttpStatus.SC_OK);
        assertNotNull(authToken, "O token não pode ser nulo");

        System.out.println("Login realizado com sucesso. Token: " + authToken);
    }

    @Quando("utilizo o token recebido para acessar {string}")
    public void utilizoOTokenRecebidoParaAcessar(String endpoint) {

        this.response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get(endpoint);

        System.out.println("Resposta ao acessar recurso protegido: " + response.asPrettyString());
    }

    //credencials invalidas
    @Dado("que tento fazer login com credenciais inválidas")
    public void queTentoFazerLoginComCredenciaisInvalidas() {
        // Utilizando credenciais que sabemos que são inválidas
        this.username = "usuario_inexistente";
        this.password = "senha_incorreta";
    }

    @Quando("faço uma requisição POST e os dados são credenciais inválidas")
    public void facoUmaRequisicaoPOSTComCredenciaisInvalidas() {
        jsonBody = String.format("""
            {
              "username": "%s",
              "password": "%s"
            }
            """, username, password);

        this.response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/auth/login");

        System.out.println("Resposta de login com credenciais inválidas: " + response.asPrettyString());
    }

    @E("a resposta deve conter a mensagem {string}")
    public void aRespostaDeveConterAMensagem(String mensagemEsperada) {
        response.then()
                .body("message", equalTo(mensagemEsperada));
    }

    @Quando("faço uma requisição POST e os dados são usuário inexistente")
    public void facoUmaRequisicaoPOSTComUsuarioInexistente() {
        jsonBody = """
        {
          "username": "usuario_que_nao_existe",
          "password": "qualquer_senha"
        }
        """;

        this.response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/auth/login");

        System.out.println("Resposta de login com usuário inexistente: " + response.asPrettyString());
    }

}