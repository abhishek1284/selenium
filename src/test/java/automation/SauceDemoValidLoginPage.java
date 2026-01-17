package automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SauceDemoValidLoginPage {

    public static void main(String[] args) {

        // Setup Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // open browser maximized

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1. Navigate to Login Page
            driver.get("https://www.saucedemo.com/");

            // 2. Enter valid user credentials
            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
            WebElement password = driver.findElement(By.id("password"));
            WebElement loginBtn = driver.findElement(By.id("login-button"));

            username.sendKeys("standard_user"); // valid username
            password.sendKeys("secret_sauce");   // valid password
            loginBtn.click();

            // 3. Verify successful login by checking URL and page elements
            wait.until(ExpectedConditions.urlContains("inventory"));
            String currentUrl = driver.getCurrentUrl();

            if(currentUrl.contains("inventory")) {
                System.out.println("✅ Login Successful! User is redirected to inventory page.");
            } else {
                System.out.println("❌ Login Failed! User is NOT redirected to inventory page.");
            }

            // Optional: Verify an element on inventory page
            WebElement inventoryTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));
            System.out.println("Inventory Page Title: " + inventoryTitle.getText());

            // Hold browser for 1 min for demo
            Thread.sleep(60000); // 60 seconds

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
