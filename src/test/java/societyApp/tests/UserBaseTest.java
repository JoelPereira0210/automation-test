package societyApp.tests;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserBaseTest {
	protected WebDriver driver;
	protected WebDriverWait wait;

	public void setUp() {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");
		wait.until(webDriver -> "complete"
				.equals(((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return document.readyState")));
	}

	public void login() throws InterruptedException {
		System.out.println("Logging in...");
		// Wait for the element to be visible and clickable
		WebElement tabElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tab:r0:1")));

		// Click the element
		tabElement.click();			driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("9876543212");
		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Rich@123");
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		Thread.sleep(5000);
		driver.navigate().refresh();
		Thread.sleep(1000);
		System.out.println("Login successful!");
	}
}
