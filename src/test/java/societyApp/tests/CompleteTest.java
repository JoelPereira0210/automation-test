package societyApp.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CompleteTest {

	public static void main(String[] args) {

		WebDriver driver = new ChromeDriver();
		int randomNumber = (int) (Math.random() * 99) + 1; // Generates a random number between 1 and 99
		int randomNumber1 = (int) (Math.random() * 999) + 1; // Generates a random number between 1 and 99
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");
		driver.findElement(By.id("tab:r0:1")).click();

//		driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("9130681854");
//		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("superAdmin@101");
////		driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("7498263271");
////		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Rich@123");
//		driver.findElement(By.cssSelector("button[type='submit']")).click();
//
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//		driver.findElement(By.xpath("//button[normalize-space()='Create Subscription']")).click();
//		driver.findElement(By.cssSelector("input[placeholder='Plan Name']")).sendKeys("Plan" + Math.random());
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//		driver.findElement(By.cssSelector("textarea[placeholder='Enter Plan Description']"))
//				.sendKeys("Description" + Math.random());
//		driver.findElement(By.cssSelector("input[placeholder='Duration']")).sendKeys("" + randomNumber);
//		driver.findElement(By.cssSelector("input[placeholder='Max Users']")).sendKeys("" + randomNumber1);
//		// Input field for 'Amount'
//		driver.findElement(By.cssSelector("input[placeholder='Amount']")).sendKeys("100");
//
//		// Click on 'Publish' button
//		driver.findElement(By.xpath("//button[normalize-space()='Publish']")).click();
//		// Wait for the button with SVG icon to be clickable
//
//		wait.until(
//				ExpectedConditions.elementToBeClickable(By.cssSelector("button svg[data-testid='AccountCircleIcon']")))
//				.click();
//
//		// Wait for the entire section to be clickable and then click it
//		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiButtonBase-root.MuiListItemButton-root.MuilistItemButton-gutters"))).click();
//		// Wait and Click the Sign Out Button
//		WebElement signOutButton = wait
//				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Sign Out']")));
//		signOutButton.click();

		driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("7498263271");
		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Rich@123");
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		// Wait for 4 seconds
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		driver.navigate().refresh();

		WebElement MembersButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Members']")));
		MembersButton.click();

	}

}
