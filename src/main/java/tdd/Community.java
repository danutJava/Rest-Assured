package tdd;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class Community {

    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void getDefaultCommunityStatusCode() {
        RestAssured
                .given().header("Content-Type", "application/json")
                .when().get("/community/1")
                .then().statusCode(OK);
    }


    @Test
    public void getCommunityName() {
        RestAssured
                .given().header("Content-Type", "application/json")
                .when().get("/community/2")
                .then().assertThat().body("communityName",equalTo("IT"));
    }

    @Test
    public void addCommunity() {

        String payload = "{\n" +
                "    \"communityName\": \"QA\"\n" +
                "}";

        RestAssured.
                given().header("Content-Type", "application/json").body(payload).
                when().post("/community").
                then().log().body().
                statusCode(OK).assertThat()
                .body("message", equalTo("New community was successfully added"));
    }

    @Test
    public void getBodyResponse() {
        RestAssured.
                given().header("Content-Type", "application/json").
                when().get("/communities").
                then().
                statusCode(OK)
                .body("communityName[0]", is("BENCH"));
    }

    @Test
    public void getAllCommunities() {
        RestAssured.
                given().header("Content-Type", "application/json").
                when().get("/communities").
                then().
                statusCode(OK).assertThat().log().body()
                .body("communityName", hasSize(2));
    }



    @Test
    public void postCommunityInvalidName() {

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("communityName", "X");


        RestAssured.
                given().header("Content-Type", "application/json").body(bodyRequest).
                when().post("/community").
                then().log().body().
                statusCode(BAD_REQUEST).assertThat()
                .body("errors.defaultMessage[0]", equalTo("Name has to be equal to or greater than 2 and less than 16 characters"));
    }

}
