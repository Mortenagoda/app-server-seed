package dk.vandborgandersen;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import javax.ws.rs.core.Response.Status;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import dk.vandborgandersen.life.exposure.model.AddShoppingListItemRepresentation;
import dk.vandborgandersen.life.exposure.model.AddShoppingListRepresentation;
import org.glassfish.grizzly.http.server.HttpServer;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * Server test suite.
 *
 * @author mortena@gmail.com
 */

public class ServerMainTest {

    private HttpServer httpServer;

    @Before
    public void startServer() throws IOException {
        httpServer = ServerMain.startServer();
    }

    @After
    public void stopServer() {
        httpServer.shutdown();
    }

    @Test
    public void testServer() {
        Response response =
            given().formParam("email", "admin@vandborgandersen.dk").formParam("password", "password").post("http://localhost:9990/auth");
        String accessToken = response.body().jsonPath().get("accessToken");
        Assert.notNull(accessToken);

        given(getRequestSpec(accessToken)).when().body(new AddShoppingListRepresentation("MyList")).post("http://localhost:9990/shoppinglists")
            .then().statusCode(201).header("Location", is("http://localhost:9990/shoppinglists/MyList"));
        given(getRequestSpec(accessToken)).when().body(new AddShoppingListRepresentation("MyList")).post("http://localhost:9990/shoppinglists")
            .then().statusCode(201).header("Location", is("http://localhost:9990/shoppinglists/MyList_1"));
        given(getRequestSpec(accessToken)).when().body(new AddShoppingListRepresentation("MySecondList"))
            .post("http://localhost:9990/shoppinglists")
            .then().statusCode(201).header("Location", is("http://localhost:9990/shoppinglists/MySecondList"));

        given(getRequestSpec(accessToken)).when().get("http://localhost:9990/shoppinglists/MyList").then().statusCode(200)
            .body("name", equalTo("MyList"));

        AddShoppingListItemRepresentation addItemObj = new AddShoppingListItemRepresentation();
        addItemObj.setName("Apples");
        addItemObj.setAmount(10);

        given(getRequestSpec(accessToken)).when().body(addItemObj)
            .post("http://localhost:9990/shoppinglists/MyList/items").then()
            .statusCode(Status.CREATED.getStatusCode())
            .header("Location", "http://localhost:9990/shoppinglists/MyList/items/Apples");

        given(getRequestSpec(accessToken)).when().get("http://localhost:9990/shoppinglists/MyList/items/Apples")
            .then().statusCode(200)
            .body("name", equalTo("Apples"))
            .body("amount", equalTo(10))
            .body("purchased", equalTo(false));

        addItemObj = new AddShoppingListItemRepresentation();
        addItemObj.setName("Apples");
        addItemObj.setAmount(5);

        given(getRequestSpec(accessToken)).when().body(addItemObj)
            .post("http://localhost:9990/shoppinglists/MyList/items").then()
            .statusCode(Status.CREATED.getStatusCode())
            .header("Location", "http://localhost:9990/shoppinglists/MyList/items/Apples_1");

        given(getRequestSpec(accessToken)).when().get("http://localhost:9990/shoppinglists/MyList/items/Apples_1")
            .then().statusCode(200)
            .body("name", equalTo("Apples"))
            .body("amount", equalTo(5))
            .body("purchased", equalTo(false));

        given(getRequestSpec(accessToken)).when().get("http://localhost:9990/shoppinglists/MyList").then()
            .statusCode(200)
            .body("_embedded.items[0].name", equalTo("Apples"))
            .body("_embedded.items[0].amount", equalTo(10))
            .body("_embedded.items[1].name", equalTo("Apples"))
            .body("_embedded.items[1].amount", equalTo(5));

        given(getRequestSpec(accessToken)).when().delete("http://localhost:9990/shoppinglists/MyList/items/Apples_1").then()
            .statusCode(Status.ACCEPTED.getStatusCode());

        given(getRequestSpec(accessToken)).when().get("http://localhost:9990/shoppinglists/MyList").then()
            .statusCode(Status.OK.getStatusCode())
            .body("_embedded.items*._links.self.href", hasItem("http://localhost:9990/shoppinglists/MyList/items/Apples"))
            .body("_embedded.items*._links.self.href", not(hasItem("http://localhost:9990/shoppinglists/MyList/items/Apples_1")));

        given(getRequestSpec(accessToken)).when().get("http://localhost:9990/shoppinglists/").then()
            .statusCode(200)
            .body("_embedded.shoppingLists*.name", CoreMatchers.hasItems("MyList", "MyList", "MySecondList"))
            .body("_embedded.shoppingLists*.name", CoreMatchers.not(is("MyNotExistingList")));

        given(getRequestSpec(accessToken)).when().delete("http://localhost:9990/shoppinglists/MySecondList").then()
            .statusCode(Status.ACCEPTED.getStatusCode());

        given(getRequestSpec(accessToken)).when().get("http://localhost:9990/shoppinglists").then()
            .statusCode(200)
            .body("_embedded.shoppingLists*.name", CoreMatchers.not(is("MySecondList")));
    }

    private RequestSpecification getRequestSpec(String accessToken) {
        return new RequestSpecBuilder().addHeader("X-Auth-Token", "Bearer " + accessToken)
            .addHeader("Accept", "application/hal+json")
            .addHeader("Content-Type", "application/json").build();
    }
}
