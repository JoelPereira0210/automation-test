package societyAppUtils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class LoginHelper {
	public static void login(WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");

		wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

		// Perform login
		// Wait for the element to be visible and clickable
		WebElement tabElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tab:r0:1")));

		// Click the element
		tabElement.click();
		driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("9130681854");
		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("superAdmin@101");
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.MuiButton-root")));

		Thread.sleep(2000);
		driver.navigate().refresh();
		Thread.sleep(500);
	}
}
