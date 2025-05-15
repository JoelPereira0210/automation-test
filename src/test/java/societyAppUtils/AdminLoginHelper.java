package societyAppUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdminLoginHelper {
	public static void login(WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get("http://localhost:3000/signup");

		wait.until(ExpectedConditions.elementToBeClickable(By.id("tab:r0:1"))).click();

		driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("7498263271");
		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Rich@123");
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		Thread.sleep(4000); // wait for login to finish
		driver.navigate().refresh();
		Thread.sleep(2000); // wait for the refreshed page to fully load
	}
}
