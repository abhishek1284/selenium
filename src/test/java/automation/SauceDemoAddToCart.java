package automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SauceDemoAddToCart {

    public static void main(String[] args) throws InterruptedException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1. Login
            driver.get("https://www.saucedemo.com/");
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();

            wait.until(ExpectedConditions.urlContains("inventory"));
            System.out.println("✅ Login Successful!");

            // 2. Clear cart if any
            List<WebElement> removeButtons = driver.findElements(By.xpath("//button[text()='Remove']"));
            for (WebElement removeBtn : removeButtons) {
                if (removeBtn.isDisplayed() && removeBtn.isEnabled()) {
                    removeBtn.click();
                    Thread.sleep(300);
                }
            }
            System.out.println("🧹 Cart cleared!");

            // 3. Add 2 different items
            int itemsAdded = 0;
            while (itemsAdded < 2) {
                // Re-find buttons each iteration
                List<WebElement> addButtons = driver.findElements(By.xpath("//button[contains(@id,'add-to-cart')]"));

                boolean clicked = false;
                for (WebElement btn : addButtons) {
                    if (btn.isDisplayed() && btn.isEnabled() && btn.getText().equals("Add to cart")) {
                        String productId = btn.getAttribute("id"); // for logging
                        wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
                        itemsAdded++;
                        System.out.println("✅ Added item " + itemsAdded + " to cart: " + productId);
                        clicked = true;

                        // Wait until badge updates
                        int retries = 0;
                        while (retries < 10) {
                            List<WebElement> badges = driver.findElements(By.className("shopping_cart_badge"));
                            int count = badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
                            if (count >= itemsAdded) break;

                            Thread.sleep(300);
                            retries++;
                        }
                        Thread.sleep(300);
                        break; // break inner loop to re-find buttons
                    }
                }

                if (!clicked) {
                    System.out.println("❌ Could not find more available items to add.");
                    break;
                }
            }

            // 4. Final cart verification
            List<WebElement> badges = driver.findElements(By.className("shopping_cart_badge"));
            int finalCount = badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());

            if (finalCount >= 2) {
                System.out.println("✅ Cart has " + finalCount + " items. Items added successfully!");
            } else {
                System.out.println("❌ Cart does not have expected number of items. Current count: " + finalCount);
            }

            Thread.sleep(5000);

        } finally {
            driver.quit();
        }
    }
}

