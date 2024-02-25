package app.docuport.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"json:target/cucumber.json",
                "html:target/cucumber/report.html",
                "junit:target/junit/junit-report.xml"},
        features = "src/test/resources/features/",
        glue = "app/docuport/step_definitions/",
       // publish = true,
        tags = "@ui",
        dryRun = false
)
public class CukesRunner {
}


