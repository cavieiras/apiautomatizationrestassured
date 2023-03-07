package org.example.Pets;

import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import  static io.restassured.RestAssured.given;

import org.junit.jupiter.api.*;

import com.github.javafaker.Faker;

import static org.hamcrest.Matchers.*;

import static io.restassured.module.jsv.JsonSchemaValidator.*;

import org.example.Entities.Pets;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class PetsTests {
    
    private static Pets pet;
    private static Faker fakeAnimal;
    public static RequestSpecification request;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        fakeAnimal = new Faker();
        pet = new Pets(fakeAnimal.name().name(),fakeAnimal.internet().url());
    }
    
    @BeforeEach
    void setRequest(){

        request = given()
                .header("api-key", "special-key")
                .contentType(ContentType.JSON);
    }

    @Test
    @Order(1)
    public void CreateNewPet_WithValidData_ReturnOk(){
        request
            .body(pet)
            .when()
            .post("/pet")
            .then()
            .assertThat().statusCode(200); //a partir daqui é bem parecido com o PostMan
    //         .body("code", equalTo(200))              //os requisitos foram pegos dali.
    //         .body("type",equalTo("unknown"))        //escrito com linguagem BDD
    //         .body("message", isA(String.class))
    //         .body("size()",equalTo(3));
    }

    @Test
    @Order(2)
    public void GetPetByName_ReturnOk(){
        request
                .when()
                .get("/pet/" + pet.getName())
                .then()
                .assertThat().statusCode(200).and().time(lessThan(2000L))
                .and().body("name", equalTo(pet.getName()));
    }

    
}
