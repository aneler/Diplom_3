package api;

//import com.elena.json.*;
import io.restassured.response.Response;
import json.User;

import java.util.List;
import static utils.URL.*;
import static io.restassured.RestAssured.given;

public class ApiEndpoints {
    public Response sendRegisterRequest(User user){
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(REGISTER_USER);
        return response;
    }
    public Response sendDeleteUserRequest(String token){
        Response response = given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete(USER);
        return response;
    }
}
