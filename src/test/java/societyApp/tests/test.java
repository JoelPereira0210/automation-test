package societyApp.tests;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import societyAppUtils.LoginHelper;

public class test {
	private WebDriver driver;
	private WebDriverWait wait;
	private final String mobileNumber = "9876543213";

	@BeforeClass
	public void setup() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		// Login before running test cases
		LoginHelper.login(driver);

		// Navigate to Landing Cards
		driver.findElement(By.xpath("//span[text()='Landing Cards']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"(//div[@class='MuiTypography-root MuiTypography-h6 MuiTypography-noWrap css-1axs5ja-MuiTypography-root'])[1]")));
	}

	@Test(priority = 3)
	public void testDeleteRandomLandingCards() throws InterruptedException {
		// Locate the table container div
		WebElement tableContainer = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='MuiBox-root css-auuar5']")));

		// Locate the table body
		WebElement tableBody = tableContainer.findElement(By.tagName("tbody"));

		// Get all rows inside the tbody
		List<WebElement> rows = tableBody.findElements(By.tagName("tr"));

		// Ensure there are at least 4 records to delete
		if (rows.size() < 4) {
			System.out.println("Not enough records to perform deletion.");
			return;
		}

		// Select 4 random rows
		Random random = new Random();
		for (int i = 0; i < 2; i++) {
			int randomIndex = random.nextInt(rows.size()); // Get a random index
			WebElement row = rows.get(randomIndex);

			// Locate the delete button inside the selected row
			WebElement deleteButton = row.findElement(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeSmall css-ptiqhd-MuiSvgIcon-root'])[3]"));

			// Click the delete button
			deleteButton.click();
			System.out.println("Clicked delete button for record: " + (randomIndex + 1));

			// Wait for confirmation dialog and click "Yes, Delete"
			WebElement confirmDeleteButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Yes, Delete']")));
			confirmDeleteButton.click();
			System.out.println("Confirmed deletion.");

			// Wait for deletion to process
			Thread.sleep(2000);

			// Refresh the table elements after each deletion
			rows = tableBody.findElements(By.tagName("tr"));
		}
	}

}
