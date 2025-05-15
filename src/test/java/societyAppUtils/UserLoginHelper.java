package societyAppUtils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserLoginHelper {

	/**
	 * Performs login for USER using common credentials
	 * Mobile: 9876543212
	 * Password: Rich@123
	 */
	public static void login(WebDriver driver) throws InterruptedException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    // Wait for the element to be visible and clickable
	    WebElement tabElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tab:r0:1")));

	    // Click the element
	    tabElement.click();
	    driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("9876543212");
	    driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Rich@123");
	    driver.findElement(By.cssSelector("button[type='submit']")).click();

	    Thread.sleep(2000); // wait for login to complete
	    driver.navigate().refresh();
	    Thread.sleep(1000);
	}

}