package app.docuport.pages;

import app.docuport.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

   // @FindBy (xpath = "//button[@role='button']")//span[contains(@class,'gray')]//../../..
//    @FindBy (xpath = "//span[contains(@class,'gray')]//../../..")
//    public WebElement selfButton;

    @FindBy (linkText = "Profile")
    public WebElement profileButton;

    @FindBy (xpath = "(//span[@class='v-btn__content'])[2]")
    public WebElement hamburgerButton;


    //List<WebElement> sideNavigationBar;

    //List<WebElement> selfDropDownList;


    public void goToProfilePage() throws InterruptedException {

//        Actions actions = new Actions(Driver.getDriver());
//        actions.moveToElement(selfButton).perform();
        Thread.sleep(5000);
        WebElement selfButton = Driver.getDriver().findElement(By.xpath("//span[contains(@class,'gray')]//../../.."));
        selfButton.click();
        Thread.sleep(5000);
        profileButton.click();

    }







}
