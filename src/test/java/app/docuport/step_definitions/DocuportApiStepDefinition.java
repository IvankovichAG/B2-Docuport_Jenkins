package app.docuport.step_definitions;
import app.docuport.pages.HomePage;
import app.docuport.pages.LoginPage;
import app.docuport.pages.ProfilePage;
import app.docuport.utilities.*;
import io.cucumber.java.af.En;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class DocuportApiStepDefinition {

    String baseUrl = Environment.BASE_URL;
    String accessToken;
    public  static  final Logger  LOG = LogManager.getLogger();
    Response response;

    @Given("User logged in to Docuport api as advisor role")
    public void user_logged_in_to_Docuport_api_as_advisor_role() {
    String email = Environment.ADVISOR_EMAIL;
    String password = Environment.ADVISOR_PASSWORD;
    LOG.info("Authorizing advosor role: " + email + ", password: " + password);
    LOG.info("Envinronment base Url: " + baseUrl);
    accessToken = DocuportApiUtil.getAccessToken(email, password);

    if (accessToken == null) {
       // LOG.error("Could not authorize user in server ");
        // in Junit there is a method that shows error   ------- >    fail()
    } else {
      //  LOG.info("Access token: " + accessToken);
    }
    }

    @Given("User sends GET request to {string} with query param {string} email address")
    public void user_sends_GET_request_to_with_query_param_email_address(String endpoint, String param) {
        String email;
        if (param.equals("advisor")) {
            email = Environment.ADVISOR_EMAIL;
        } else {
            email = null;
        }

//        switch (param) {
//            case "advisor":
//                email = Environment.ADVISOR_EMAIL;
 //       break;
//        }
//
        response = given().accept(ContentType.JSON)
                .and().header("Authorization", accessToken)
                .and().queryParam("EmailAddress", email)
                .when().get(baseUrl + endpoint);
    }

    @Then("status code should be {int}")
    public void status_code_should_be(int expectedSC) {
        assertEquals(expectedSC, response.statusCode());
   // response.then().statusCode(expectedSC);     ---- > the same
    }

    @Then("content type is {string}")
    public void content_type_is(String expectedCT) {
        assertEquals(expectedCT, response.contentType());
    }

    @Then("role is {string}")
    public void role_is(String expectedRoleName) {

        assertEquals(expectedRoleName, response.path("items[0].roles[0].name"));
        JsonPath jsonPath = response.jsonPath();
        assertEquals(expectedRoleName, jsonPath.getString("items[0].roles[0].name"));
    }
    @Given("User logged in to Docuport app as advisor role")
    public void user_logged_in_to_Docuport_app_as_advisor_role() {
        Driver.getDriver().get(Environment.URL);
        LoginPage loginPage = new LoginPage();
        loginPage.login(Environment.ADVISOR_EMAIL, Environment.ADVISOR_PASSWORD);
        assertTrue(Driver.getDriver().getCurrentUrl().equals("https://beta.docuport.app/"));
    }

    @When("User goes to profile page")
    public void user_goes_to_profile_page() {
        HomePage homePage = new HomePage();
        homePage.goToProfilePage();
    }

    @Then("User should see same info on UI and API")
    public void user_should_see_same_info_on_UI_and_API() {
        ProfilePage profilePage = new ProfilePage();
        String fullName = profilePage.fullName.getText(); // Batch1 Group1
        String[] fN = fullName.split(" ");  // ["Batch1", "Group1"]
        String role = profilePage.role.getText();

        Map<String, String> uiUserDataMap = new HashMap<>();
        uiUserDataMap.put("role", role);
        uiUserDataMap.put("firstName", fN[0]);
        uiUserDataMap.put("lastName", fullName.split(" ")[1]);
        //uiUserDataMap.put("lastName", fN[1]);

        System.out.println("UI User Info: " + uiUserDataMap);


        String apiUserFirstName = response.path("items[0].firstName");
        String apiUserLastName = response.path("items[0].lastName");
        String apiUserRole = response.path("items[0].roles[0].displayName");

        Map <String, String> apiUserDataMap = new HashMap<>();
        apiUserDataMap.put("role", apiUserRole);
        apiUserDataMap.put("firstName", apiUserFirstName);
        apiUserDataMap.put("lastName", apiUserLastName);

        System.out.println("API User Info: " + apiUserDataMap);

        assertEquals(uiUserDataMap, apiUserDataMap);
    }

    @Given("User logged in to Docuport api as client role")
    public void user_logged_in_to_Docuport_api_as_client_role() {
        String email = Environment.CLIENT_EMAIL;
        String password = Environment.CLIENT_PASSWORD;
        LOG.info("Authorizing advosor role: " + email + ", password: " + password);
        LOG.info("Envinronment base Url: " + baseUrl);
        accessToken = DocuportApiUtil.getAccessToken(email, password);

        if (accessToken == null) {
             LOG.error("Could not authorize user in server ");
            // in Junit there is a method that shows error   ------- >    fail()
        } else {
              LOG.info("Access token: " + accessToken);
        }
    }
    Map <String, String> expData;
    @When("Users sends POST request to {string} with following info:")
    public void users_sends_POST_request_to_with_following_info(String endpoint, Map <String, String> datamap) {
        expData = datamap;
        response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().body(datamap)
                .and().header("Authorization", accessToken)
                .when().post(baseUrl + endpoint);
        response.prettyPrint();
        System.out.println(expData);
    }

    @Then("Database should persist same client info")
    public void database_should_persist_same_client_info() {
        String sqlQuery = "select client_type, name, first_name, last_name from document.clients where email_address = '" + Environment.CLIENT_EMAIL + "' AND name = '" + expData.get("name")+"'";
        DBUtils.runQuery(sqlQuery);
        List <Map <String, String>> actData = DBUtils.getAllRowAsListOfMap();
        System.out.println(actData);

        //assertEquals("DOES NOT MATCH", expData, actData);
        assertEquals(expData.get("clientType"), actData.get(0).get("client_type"));
        assertEquals(expData.get("name"), actData.get(0).get("name"));

        String sql2 = "delete from document.clients where name = 'Mouse5'";
        DBUtils.runQuery(sql2);
        //assertTrue(DBUtils.getRowCount() == 0);

    }







}
