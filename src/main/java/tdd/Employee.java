package tdd;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.core.Every;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class Employee {

    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void addEmployee() {

        Map<String, Object> bodyRequest = new HashMap<>();

        bodyRequest.put("firstName", "Danut");
        bodyRequest.put("lastName", "Cristea");
        bodyRequest.put("sex", "M");
        bodyRequest.put("email", "danut.cristea1@yahoo.com");
        bodyRequest.put("password", "parola");
        bodyRequest.put("phoneNumber", "0767123456");
        bodyRequest.put("salary", 2001);
        bodyRequest.put("bonus", true);


        RestAssured.
                given().header("Content-Type", "application/json").body(bodyRequest).
                when().post("/register/employee").
                then().log().body().
                statusCode(OK).assertThat()
                .body("message", equalTo("Successfully added new employee"));
    }

    @Test
    public void addManager() {

        Map<String, Object> bodyRequest = new HashMap<>();

        bodyRequest.put("firstName", "John");
        bodyRequest.put("lastName", "Robertson");
        bodyRequest.put("sex", "M");
        bodyRequest.put("email", "manager@yahoo.com");
        bodyRequest.put("password", "parola");
        bodyRequest.put("phoneNumber", "0767123456");
        bodyRequest.put("salary", 30000);
        bodyRequest.put("bonus", true);


        RestAssured.
                given().header("Content-Type", "application/json").body(bodyRequest).
                when().post("/register/manager").
                then().log().body().
                statusCode(OK).assertThat()
                .body("message", equalTo("Successfully added new manager"));
    }


    @Test
    public void updateEmployee() {

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("firstName", "Georgian");
        bodyRequest.put("lastName", "Cristea");
        bodyRequest.put("sex", "M");
        bodyRequest.put("email", "danut.cristea@yahoo.com");
        bodyRequest.put("password", "parola");
        bodyRequest.put("phoneNumber", "0767123456");
        bodyRequest.put("salary", 25000);
        bodyRequest.put("bonus", true);

        int employeeId = 8;

        RestAssured.
                given().header("Content-Type", "application/json").body(bodyRequest).
                when().put("/employee/update/{employeeId}", employeeId).
                then().log().body().
                statusCode(OK).assertThat()
                .body("message", equalTo("Employee's details were successfully updated"));
    }

    @Test
    public void getEmployeeInfoById() {
        int employeeId = 3;
        RestAssured
                .given().header("Content-Type", "application/json")
                .when().get("/employee/{id}", employeeId)
                .then()
                .statusCode(OK).assertThat().log().body()
                .body("firstName", equalTo("Danut"));
    }

    @Test
    public void removeEmployee() {
        int employeeId = 1;
        RestAssured
                .given().header("Content-Type", "application/json")
                .when().delete("/employee/{employeeId}", employeeId)
                .then()
                .statusCode(OK).assertThat().log().body()
                .body("message", equalTo("Employee was successfully removed"));
    }
    @Test
    public void getAllEmployees() {
        RestAssured.
                given().header("Content-Type", "application/json").
                when().get("/employees").
                then().
                statusCode(OK).log().all();
    }

    @Test
    public void checkAllSalariesGreaterThan() {
        RestAssured.
                given().header("Content-Type", "application/json").
                when().get("/employees").
                then().assertThat().body("salary", Every.everyItem(greaterThan(2000)));
    }
}
