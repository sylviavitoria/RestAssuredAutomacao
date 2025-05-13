package bdd.automation.api.steps;

import bdd.automation.api.TestApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@CucumberContextConfiguration
public class BaseStepDefinitions {

}