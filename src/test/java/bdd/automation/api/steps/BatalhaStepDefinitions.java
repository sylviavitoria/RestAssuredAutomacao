package bdd.automation.api.steps;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class BatalhaStepDefinitions extends BaseStepDefinitions {
    private Response response;
    private String nomeAtacante;
    private String nomeDefensor;
    private int chakraInicial;
    private int vidaInicial;
    private String nomeJutsu;
    private int danoJutsu;
    private int consumoJutsu;
    private Long atacanteId;
    private Long defensorId;

    @Before
    public void setup() {
        baseURI = "http://localhost";
        port = 8080;
        basePath = "/api/v1";
    }

    // ----- CENÁRIO: Batalha utilizando jutsu avançado ----
    @Dado("que existe um ninja atacante {string} com chakra {int}")
    public void queExisteUmNinjaAtacanteComChakra(String nome, int chakra) {
        this.nomeAtacante = nome;
        this.chakraInicial = chakra;
    }

    @E("que existe um ninja defensor {string} com vida {int}")
    public void queExisteUmNinjaDefensorComVida(String nome, int vida) {
        this.nomeDefensor = nome;
        this.vidaInicial = vida;
    }

    @E("que o atacante possui o jutsu {string} com dano {int} e consumo {int}")
    public void queOAtacantePossuiOJutsuComDanoEConsumo(String jutsu, int dano, int consumo) {
        this.nomeJutsu = jutsu;
        this.danoJutsu = dano;
        this.consumoJutsu = consumo;
    }

    @Quando("o atacante usa {string} contra o defensor")
    public void oAtacanteUsaContraODefensor(String jutsu) {
        String jsonAtacante = String.format("""
    {
      "tipoNinja": "NINJUTSU",
      "nome": "%s",
      "idade": 21,
      "aldeia": "Aldeia Oculta da Folha",
      "chakra": %d,
      "jutsus": {
        "%s": {
          "dano": %d,
          "consumoDeChakra": %d
        }
      }
    }
    """, nomeAtacante, chakraInicial, nomeJutsu, danoJutsu, consumoJutsu);

        Response respostaAtacante = given()
                .contentType(ContentType.JSON)
                .body(jsonAtacante)
                .when()
                .post("/personagens");

        atacanteId = respostaAtacante.jsonPath().getLong("id");

        String jsonDefensor = String.format("""
    {
      "tipoNinja": "GENJUTSU",
      "nome": "%s",
      "idade": 26,
      "aldeia": "Akatsuki",
      "chakra": 100,
      "vida": %d,
      "jutsus": {
        "Tsukuyomi": {
          "dano": 80,
          "consumoDeChakra": 10
        }
      }
    }
    """, nomeDefensor, vidaInicial);

        Response respostaDefensor = given()
                .contentType(ContentType.JSON)
                .body(jsonDefensor)
                .when()
                .post("/personagens");

        defensorId = respostaDefensor.jsonPath().getLong("id");

        this.response = given()
                .contentType(ContentType.JSON)
                .when()
                .post("/batalhas/atacar/{atacanteId}/{defensorId}?nomeJutsu={nomeJutsu}",
                        atacanteId, defensorId, jutsu);

    }

    @Então("o ataque deve ser bem sucedido")
    public void o_ataque_deve_ser_bem_sucedido() {
        response.then()
                .statusCode(200)
                .body("sucesso", equalTo(true));
    }

    @Então("o log deve conter {string}")
    public void o_log_deve_conter(String texto) {
        response.then()
                .body("log", hasItem(containsString(texto)));

        String logCompleto = response.jsonPath().getString("log");
        assertTrue(logCompleto.contains(texto),
                "O log de batalha deve conter: '" + texto + "', mas encontrado: '" + logCompleto + "'");
    }

    @E("o chakra do atacante deve ser reduzido em {int}")
    public void oChakraDoAtacanteDeveSerReduzidoEm(int reducaoEsperada) {
        int chakraRestante = response.jsonPath().getInt("statusAtacante.chakra");
        int chakraConsumido = chakraInicial - chakraRestante;

        assertEquals(reducaoEsperada, chakraConsumido,
                "O chakra do atacante deve ser reduzido em " + reducaoEsperada);

    }

    @Então("a vida do defensor deve ser reduzida para {int} se não conseguir desviar")
    public void a_vida_do_defensor_deve_ser_reduzida_para_se_não_conseguir_desviar(Integer vidaEsperada) {
        int vidaRestante = response.jsonPath().getInt("statusDefensor.vida");

        assertEquals(vidaEsperada, vidaRestante,
                "A vida do defensor deve ser " + vidaEsperada);

    }

    // ----- CENÁRIO: Atacar com jutsu inexistente ----
    @Dado("que existe um ninja atacante {string}")
    public void queExisteUmNinjaAtacante(String nome) {
        this.nomeAtacante = nome;

        String jsonAtacante = String.format("""
        {
          "tipoNinja": "TAIJUTSU",
          "nome": "%s",
          "idade": 16,
          "aldeia": "Aldeia Oculta da Folha",
          "chakra": 100,
          "vida": 100,
          "jutsus": {
            "Konoha Senpu": {
              "dano": 60,
              "consumoDeChakra": 10
            }
          }
        }
        """, nomeAtacante);

        Response respostaAtacante = given()
                .contentType(ContentType.JSON)
                .body(jsonAtacante)
                .when()
                .post("/personagens");

        this.atacanteId = respostaAtacante.jsonPath().getLong("id");

        String jsonDefensor = """
        {
          "tipoNinja": "NINJUTSU",
          "nome": "Naruto Uzumaki",
          "idade": 17,
          "aldeia": "Aldeia Oculta da Folha",
          "chakra": 100,
          "vida": 100,
          "jutsus": {
            "Rasengan": {
              "dano": 70,
              "consumoDeChakra": 10
            }
          }
        }
        """;

        Response respostaDefensor = given()
                .contentType(ContentType.JSON)
                .body(jsonDefensor)
                .when()
                .post("/personagens");

        this.defensorId = respostaDefensor.jsonPath().getLong("id");
    }

    @Quando("tento usar o jutsu {string}")
    public void tentoUsarOJutsu(String nomeJutsu) {
        this.response = given()
                .contentType(ContentType.JSON)
                .when()
                .post("/batalhas/atacar/{atacanteId}/{defensorId}?nomeJutsu={nomeJutsu}",
                        atacanteId, defensorId, nomeJutsu);
    }

    @Então("deve retornar erro {string}")
    public void deveRetornarErro(String mensagemErro) {
        response.then()
                .statusCode(anyOf(is(200), is(400), is(404)))
                .body(containsString(mensagemErro));
    }

    @E("nenhum dano deve ser causado")
    public void nenhumDanoDeveSerCausado() {
        if (response.getBody().asString().contains("statusDefensor")) {
            response.then()
                    .body("statusDefensor.vida", either(is(100)).or(is(nullValue())));
        }

        if (response.getBody().asString().contains("sucesso")) {
            response.then()
                    .body("sucesso", either(is(false)).or(is(nullValue())));
        }
    }

    // ----- CENÁRIO: Atacar a si mesmo ----
    @Dado("que existe um ninja {string}")
    public void queExisteUmNinja(String nome) {
        this.nomeAtacante = nome;

        String jsonNinja = String.format("""
    {
      "tipoNinja": "NINJUTSU",
      "nome": "%s",
      "idade": 17,
      "aldeia": "Aldeia Oculta da Folha",
      "chakra": 100,
      "vida": 100,
      "jutsus": {
        "Rasengan": {
          "dano": 70,
          "consumoDeChakra": 10
        }
      }
    }
    """, nomeAtacante);

        Response respostaNinja = given()
                .contentType(ContentType.JSON)
                .body(jsonNinja)
                .when()
                .post("/personagens");

        this.atacanteId = respostaNinja.jsonPath().getLong("id");
    }

    @Quando("tento atacar o mesmo ninja")
    public void tentoAtacarOMesmoNinja() {
        this.response = given()
                .contentType(ContentType.JSON)
                .when()
                .post("/batalhas/atacar/{atacanteId}/{defensorId}?nomeJutsu={nomeJutsu}",
                        atacanteId, atacanteId, "Rasengan");
    }
}