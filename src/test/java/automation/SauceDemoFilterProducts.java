package automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SauceDemoFilterProducts {

    public static void main(String[] args) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1. Login
            driver.get("https://www.saucedemo.com/");
            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
            WebElement password = driver.findElement(By.id("password"));
            WebElement loginBtn = driver.findElement(By.id("login-button"));
            username.sendKeys("standard_user");
            password.sendKeys("secret_sauce");
            loginBtn.click();

            wait.until(ExpectedConditions.urlContains("inventory"));
            System.out.println("✅ Login Successful!");

            // 2. Apply Name filter (Z to A)
            WebElement sortDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product_sort_container")));
            sortDropdown.click();

            WebElement nameDesc = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("option[value='za']")));
            nameDesc.click();
            Thread.sleep(2000);

            // Verify Name descending
            List<WebElement> itemNames = driver.findElements(By.className("inventory_item_name"));
            List<String> names = new ArrayList<>();
            for (WebElement item : itemNames) {
                names.add(item.getText());
            }

            boolean isDescending = true;
            for (int i = 0; i < names.size() - 1; i++) {
                if (names.get(i).compareTo(names.get(i + 1)) < 0) {
                    isDescending = false;
                    break;
                }
            }
            System.out.println(isDescending ? "✅ Name filter applied correctly" : "❌ Name filter failed");

            // 3. Apply Price filter (Low to High)
            sortDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product_sort_container")));
            sortDropdown.click();

            WebElement priceLowHigh = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("option[value='lohi']")));
            priceLowHigh.click();
            Thread.sleep(2000);

            // Verify Price Low to High
            List<WebElement> itemPrices = driver.findElements(By.className("inventory_item_price"));
            List<Double> prices = new ArrayList<>();
            for (WebElement price : itemPrices) {
                prices.add(Double.parseDouble(price.getText().replace("$", "")));
            }

            boolean isLowToHigh = true;
            for (int i = 0; i < prices.size() - 1; i++) {
                if (prices.get(i) > prices.get(i + 1)) {
                    isLowToHigh = false;
                    break;
                }
            }
            System.out.println(isLowToHigh ? "✅ Price filter applied correctly" : "❌ Price filter failed");

            Thread.sleep(60000); // keep browser open for demo

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
