package societyApp.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import societyAppUtils.LoginHelper;

import java.time.Duration;
import java.util.List;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class UpdateAllSubscriptions {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeTest
	public void setup() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		js = (JavascriptExecutor) driver;

		// Login to the application
		LoginHelper.login(driver);
		System.out.println("Logged in successfully.");
	}

	@Test(priority = 1)
	public void findSubscriptions() {
        // Find all subscription cards
        List<WebElement> cards = driver.findElements(By.cssSelector(".MuiBox-root.css-sagz8e"));
        System.out.println("Total subscriptions found: " + cards.size());
    }

    @Test(priority = 2)
    public void updateSubscriptions() throws InterruptedException {
        // Call the method to update subscriptions
        updateAllSubscriptions(driver, wait, js);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed.");
        }
    }

    // Method to update all subscriptions
    private void updateAllSubscriptions(WebDriver driver, WebDriverWait wait, JavascriptExecutor js) throws InterruptedException {
        List<WebElement> cards = driver.findElements(By.cssSelector(".MuiBox-root.css-sagz8e"));

        for (int i = 1; i <= cards.size(); i++) {
            // Locate and click the "Edit" button for the current subscription
            WebElement editButton = wait.until(ExpectedConditions
                    .elementToBeClickable(By.xpath("(//button[span[contains(text(), 'Edit')]])[" + i + "]")));

            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", editButton);
            editButton.click();
            Thread.sleep(500);

            // Scroll down to ensure the form fields are visible
            js.executeScript("window.scrollBy(0,300);");
            Thread.sleep(500);

            // Locate the "Plan Name" and "Amount" fields
            WebElement nameField = wait.until(ExpectedConditions
                    .presenceOfElementLocated(By.cssSelector("input[aria-label='Plan Name'], input[name='planName']")));

            WebElement priceField = wait.until(ExpectedConditions
                    .presenceOfElementLocated(By.cssSelector("input[aria-label='Amount'], input[name='amount']")));

            // Print current values
            System.out.println("Before Update - Plan Name: " + nameField.getDomProperty("value") + ", Amount: "
                    + priceField.getDomProperty("value"));

            // Update the "Plan Name" and "Amount" fields
            nameField.sendKeys(Keys.chord(Keys.CONTROL, "a"), "Updated Plan " + i); // Select all and replace
            priceField.sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(999 + i * 100));

            // Click the "Update" button
            WebElement updateButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[span[contains(text(), 'Update')]]")));
            updateButton.click();

            System.out.println("Updated subscription " + i);
            Thread.sleep(500);
        }

        System.out.println("All subscriptions updated successfully.");
    }
}