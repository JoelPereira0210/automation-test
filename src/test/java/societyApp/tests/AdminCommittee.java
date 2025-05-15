package societyApp.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import societyAppUtils.AdminLoginHelper; // ✅ Added import

import java.util.*;
import java.time.Duration;

public class AdminCommittee {
	private WebDriver driver;
	private WebDriverWait wait;

	// Constructor to initialize WebDriver (for calling from AdminActions)
	public AdminCommittee() throws InterruptedException {
		// setUp(); // Initialize WebDriver when object is created
	}

	@BeforeClass
	public void setUp() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");

		// Login before executing tests
		// Wait for the element to be visible and clickable
//		WebElement tabElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tab:r0:1")));
//		tabElement.click();
//		driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("7498263271");
//		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Rich@123");
//		driver.findElement(By.cssSelector("button[type='submit']")).click();
//		Thread.sleep(4000); // Wait for login to complete
//		driver.navigate().refresh();
//		Thread.sleep(1000); // Wait for the refresh to complete

		// ✅ Centralized admin login
		AdminLoginHelper.login(driver);
	}

	@Test(priority = 1)
	public void navigateToCommitteeSection() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@role='button'])[2]"))).click();
		Thread.sleep(2000);
		System.out.println("Navigated to Committee section.");
	}

	@Test(priority = 2)
	public void verifyAddNewDesignationButton() throws InterruptedException {
		WebElement addNewDesignationButton = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//button[span[normalize-space()='Add New Designation']]")));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addNewDesignationButton);
		Thread.sleep(500); // Allow time for scrolling

		addNewDesignationButton.click();

		WebElement designationInput = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//input[@placeholder='Designation Name' and @name='designationName']")));

		designationInput.clear();
		designationInput.sendKeys("volunteer12345");

		// Step 1: Click the dropdown to expand options
		WebElement dropdown = wait
				.until(ExpectedConditions.elementToBeClickable(By.id("mui-component-select-numberOfPositions")));
		dropdown.click();

		// Step 2: Wait for the dropdown list to be visible
		WebElement dropdownList = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='listbox']")));

		// Step 3: Click on the option "5"
		WebElement optionFive = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@data-value='5']")));
		optionFive.click();

		Thread.sleep(500);
		driver.findElement(By.xpath("//span[normalize-space()='Add']")).click();
		Thread.sleep(2000);
		System.out.println("Add New Designation button clicked successfully.");

		driver.findElement(By.xpath("//span[normalize-space()='volunteer12']")).click();
		Thread.sleep(200);

		driver.findElement(By.xpath("//span[normalize-space()='Add volunteer12']")).click();
		Thread.sleep(500); // Wait for table to load (use WebDriverWait in actual implementation)

		// Find all table rows
		List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));

		// Print number of rows found
		System.out.println("Number of rows in table: " + rows.size());

		// Select the first two checkboxes if they exist
		for (int i = 0; i < Math.min(2, rows.size()); i++) {
			WebElement checkbox = wait.until(ExpectedConditions
					.elementToBeClickable(rows.get(i).findElement(By.cssSelector("input[type='checkbox']"))));
			if (!checkbox.isSelected()) {
				checkbox.click();
			}
		}
		driver.findElement(By.xpath("//span[normalize-space()='Add']")).click();
		Thread.sleep(200);
		driver.findElement(By.xpath("//button[normalize-space()='Yes']")).click();
	}

	@Test(priority = 3)
	public void viewTableList() throws InterruptedException {
		WebElement ulElement = driver
				.findElement(By.xpath("//ul[@class='MuiList-root MuiList-padding css-17zlt1e-MuiList-root']"));

		List<WebElement> listItems = ulElement.findElements(By.tagName("li"));

		for (WebElement listItem : listItems) {
			try {
				listItem.click();
				System.out.println("Clicked on: " + listItem.getText());

				Thread.sleep(100);

				WebElement tbody = driver.findElement(By.xpath("//table/tbody"));
				List<WebElement> rows = tbody.findElements(By.tagName("tr"));

				System.out.println("Number of rows in tbody: " + rows.size());

				WebElement editIcon = driver.findElement(By.xpath("//svg[g[@id='Edit']]"));
				editIcon.click();

				WebElement svgIcon = rows.get(0)
						.findElement(By.cssSelector("svg.MuiSvgIcon-root.MuiSvgIcon-fontSizeMedium"));

				wait.until(ExpectedConditions.elementToBeClickable(svgIcon)).click();
				Thread.sleep(200);
				driver.findElement(By.xpath("//input[@value='false']")).click();
				driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();

				Thread.sleep(2000);

				for (WebElement row : rows) {
					try {
						System.out.println("Row Content: " + row.getText());
						System.out.println("Clicked SVG icon in row.");
						Thread.sleep(100);
					} catch (Exception e) {
						System.out.println("Could not click SVG icon in row.");
					}
				}

			} catch (Exception e) {
				System.out.println("Could not click: " + listItem.getText());
			}
		}
		driver.quit();
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
