package automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class SauceDemoInvalidLoginTest {

    public static void main(String[] args) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);

        try {
            // 1. Navigate to Login Page
            driver.get("https://www.saucedemo.com/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 2. Fill Login Form with Locked User
            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
            WebElement password = driver.findElement(By.id("password"));
            WebElement loginBtn = driver.findElement(By.id("login-button"));

            username.sendKeys("locked_out_user");
            password.sendKeys("secret_sauce");
            loginBtn.click();

            // 3. Verify Error Message
            WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("h3[data-test='error']")
            ));

            String actualMessage = errorMsg.getText();
            String expectedMessage = "Epic sadface: Sorry, this user has been locked out.";

            if (actualMessage.equals(expectedMessage)) {
                System.out.println("✅ Test Passed");
                System.out.println("Message: " + actualMessage);
            } else {
                System.out.println("❌ Test Failed");
                System.out.println("Expected: " + expectedMessage);
                System.out.println("Actual: " + actualMessage);
            }

            // ✅ Hold browser for 3 minutes (180000 ms)
            System.out.println("⏳ Waiting for 3 minutes before closing...");
            Thread.sleep(180000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
