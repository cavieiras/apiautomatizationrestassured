package org.example.User;

import io.restassured.RestAssured;
//import io.restassured.config.RestAssuredConfig;
//import io.restassured.filter.log.ErrorLoggingFilter;
//import io.restassured.filter.log.RequestLoggingFilter;
//import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestLogSpecification;
import io.restassured.specification.RequestSpecification;

import org.junit.jupiter.api.*;

import com.github.javafaker.Faker;

import static io.restassured.RestAssured.given;
import static io.restassured.config.LogConfig.logConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;

import org.example.Entities.User;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UserTests {

    private static User user; //variável que será reaproveitada apenas para a classe User
    
    public static Faker faker; //criação do fake

    public static RequestSpecification request; //criação do request
    
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        //vai ser compartilhada (acessível) para todos os testes, já que é public.   

        faker = new Faker();

//criação de um usuário
//deixando a criação do usuário na tag BeforeAll, ele vai usar o mesmo
//usuário para todos os testes. 
//Se colocar o faker se usuário no BeforeEach, ele vai criar um usuário
//diferente para cada instancia de teste (COMPORTAMENTOS DIFERENTES)

        user = new User(faker.name().username(),
                faker.name().firstName(),  
                faker.name().lastName(),
                faker.internet().safeEmailAddress(),
                faker.internet().password(8,10), 
                faker.phoneNumber().toString());
    }

//É possível 'setar' uma requisição inicial
    @BeforeEach
    void setRequest(){
        request = given().config(RestAssured.config().logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
            .header("api-key", "special-key")
            .contentType(ContentType.JSON); //.header("Content-Type", "application/json"); (pode ser escrito assim tbm)

    }

//TESTES
//estrutura básica da liguagem
//given().header().when().get().then().assertThat();
//mas o given e o header já tem, então será escrito da seguinte maneira:

    @Test
    @Order(1)
    public void CreateNewUser_WithValidData_ReturnOK (){
        request
            .body(user)
            .when()
            .post("/user")
            .then()
            .assertThat().statusCode(200).and() //a partir daqui é bem parecido com o PostMan
            .body("code", equalTo(200))              //os requisitos foram pegos dali.
            .body("type",equalTo("unknown"))        //escrito com linguagem BDD
            .body("message", isA(String.class))
            .body("size()",equalTo(3));
    }

    @Test
    @Order(2)
    public void GetLogin_ValidUser_ReturnOK(){
        request 
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .when()
                .get("/user/login")
                .then()
                .assertThat().statusCode(200).and()
                .time(lessThan(3000L))
                .and().body(matchesJsonSchemaInClasspath("loginResponseSchema.json"));
    }

    @Test
    @Order(3)
    void GetUserByUsername_userIsValid_ReturnOK(){
        request
                .when()
                .get("/user/" + user.getUsername())
                .then()
                .assertThat().statusCode(200).and().time(lessThan(2000L))
                .and().body("firstName", equalTo(user.getFirstName()));
    
        //TODO: SCHEMA VALIDATION

    }

    @Test
    @Order(4)
    void DeleteUser_UserExists_ReturnOk(){
        request 
                .when()
                .delete("/user/" + user.getUsername())
                .then()
                .assertThat().statusCode(200).and()
                .time(lessThan(2000L))
                .log();
    }
    
}
