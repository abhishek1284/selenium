package automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

public class SauceDemoCheckout {

    public static void main(String[] args) {

        // ✅ Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // standard wait

        try {
            // Step 1: Open SauceDemo
            driver.get("https://www.saucedemo.com/");

            // Step 2: Login
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();

            // Step 3: Add product to cart
            wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();

            // Step 4: Go to cart
            driver.findElement(By.className("shopping_cart_link")).click();

            // Step 5: Checkout
            wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();

            // Step 6: Fill billing/shipping info
            driver.findElement(By.id("first-name")).sendKeys("Abhishek");
            driver.findElement(By.id("last-name")).sendKeys("Pradhan");
            driver.findElement(By.id("postal-code")).sendKeys("44600");
            driver.findElement(By.id("continue")).click();

            // Step 7: Robust wait for Finish button (30 seconds)
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement finishButton = longWait.until(ExpectedConditions.elementToBeClickable(By.id("finish")));

            // Extra sleep for stability
            try {
                Thread.sleep(3000); // 3 seconds
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            finishButton.click();

            // Step 8: Verify checkout success
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));
            String message = successMsg.getText().trim();
            System.out.println("Confirmation Message: " + message);

            if (message.equalsIgnoreCase("THANK YOU FOR YOUR ORDER")) {
                System.out.println("✅ Checkout Successful!");
            } else {
                System.out.println("❌ Checkout Failed! Actual message: " + message);
                takeScreenshot(driver, "checkout_failed.png");
            }

        } catch (Exception e) {
            System.out.println("❌ Error occurred during test execution");
            e.printStackTrace();
            takeScreenshot(driver, "error_screenshot.png");
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    // ✅ Screenshot helper with unique timestamp
    public static void takeScreenshot(WebDriver driver, String fileName) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileWithTime = fileName.replace(".png", "_" + timestamp + ".png");

            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            File dest = new File(System.getProperty("user.dir") + "/" + fileWithTime);
            Files.copy(src.toPath(), dest.toPath());
            System.out.println("📸 Screenshot saved: " + fileWithTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
