package societyApp.tests;

import societyAppUtils.LoginHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class LandingCards {
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

	@Test(priority = 1)
	public void testCreateLandingCards() throws IOException, InterruptedException {
		// Load JSON test data
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonData = objectMapper.readTree(new File("src/test/resources/landing_card_data.json"));

		for (JsonNode cardData : jsonData) {
			// Extract fields from JSON
			String cardTitle = cardData.get("cardTitle").asText();
			String cardSubTitle = cardData.get("cardSubTitle").asText();
			String cardDescription = cardData.get("cardDescription").asText();
			String buttonText = cardData.get("buttonText").asText();

			// Click button to open the form
			WebElement openFormButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//span[@class='MuiTypography-root MuiTypography-text8 css-tbffdv-MuiTypography-root'])[1]")));
			openFormButton.click();

			// Wait for form to appear
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Create Landing Card']")));

			// Select Type (Dropdown)
			WebElement typeDropdown = driver.findElement(By.xpath(
					"(//div[@class='MuiInputBase-root MuiOutlinedInput-root MuiInputBase-colorPrimary MuiInputBase-formControl css-7boxz9-MuiInputBase-root-MuiOutlinedInput-root-MuiSelect-root'])[1]"));
			typeDropdown.click();

			// Wait for dropdown options to load
			List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
					"//ul[@class='MuiList-root MuiList-padding MuiMenu-list css-6hp17o-MuiList-root-MuiMenu-list']/li")));

			// Select a random option
			Random random = new Random();
			WebElement selectedOption = options.get(random.nextInt(options.size()));
			String selectedType = selectedOption.getText();
			selectedOption.click();

			// Fill out the remaining form
			driver.findElement(By.name("cardTitle")).sendKeys(cardTitle);
			driver.findElement(By.name("cardSubTitle")).sendKeys(cardSubTitle);
			driver.findElement(By.xpath("//div[@class='ql-editor ql-blank']")).sendKeys(cardDescription);
			driver.findElement(By.name("buttonText")).sendKeys(buttonText);

			// If "Contact us" is selected, enter mobile number
			if (selectedType.equalsIgnoreCase("Contact us")) {
				WebElement mobileNumberField = wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//input[@placeholder='Mobile Number']")));
				mobileNumberField.sendKeys(mobileNumber);
				System.out.println("Entered Mobile Number: " + mobileNumber);
			}

			// Submit the form
			WebElement submitButton = driver.findElement(By.xpath("(//button[normalize-space()='Create'])[1]"));
			submitButton.click();

			System.out.println("Successfully created Landing Card: " + cardTitle + " (Type: " + selectedType + ")");

			// Wait before adding the next card (to avoid UI lag issues)
			Thread.sleep(2000);
		}
	}

	@Test(priority = 2)
	public void testVerifyLandingCardsTableNotEmpty() {
		// Locate the table container div
		WebElement tableContainer = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='MuiBox-root css-auuar5']")));

		// Locate the table body
		WebElement tableBody = tableContainer.findElement(By.tagName("tbody"));

		// Get all rows inside the tbody
		List<WebElement> rows = tableBody.findElements(By.tagName("tr"));

		// Assert that the table has at least one row
		Assert.assertFalse(rows.isEmpty(), "Landing Cards table is empty, no records found!");

		System.out.println("Landing Cards table has records.");
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
		for (int i = 0; i < 4; i++) {
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

	@Test(priority = 4)
	public void testViewLandingCard() throws InterruptedException {
		// Locate the table container div
		WebElement tableContainer = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='MuiBox-root css-auuar5']")));

		// Locate the table body
		WebElement tableBody = tableContainer.findElement(By.tagName("tbody"));

		// Get all rows inside the tbody
		List<WebElement> rows = tableBody.findElements(By.tagName("tr"));

		// Ensure there is at least 1 record to view
		if (rows.isEmpty()) {
			System.out.println("No records found to view.");
			return;
		}

		// Locate the first row
		WebElement firstRow = rows.get(0);

		// Locate the view button inside the first row
		WebElement viewButton = firstRow.findElement(By.xpath(
				"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeSmall css-ptiqhd-MuiSvgIcon-root'])[1]"));

		// Click the view button
		viewButton.click();
		System.out.println("Opened view form for the first record.");

		// Wait for the form to appear
		WebElement viewForm = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiPaper-root")));

		Thread.sleep(1000); // Allow UI to stabilize

		// Locate the target element inside the form
		WebElement buttonTextElement = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("(//span[normalize-space()='Button Text'])[1]")));

		// Scroll inside the form until "Button Text" is visible
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", buttonTextElement);

		// Wait to ensure scrolling is completed
		Thread.sleep(2000);

		// Click anywhere on the screen (outside the form) to close it
		Actions actions = new Actions(driver);
		actions.moveByOffset(10, 10).click().perform();
		System.out.println("Closed the form by clicking outside.");
	}

	@Test(priority = 5)
	public void testEditLandingCard() throws InterruptedException {
		// Locate the table container div
		WebElement tableContainer = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='MuiBox-root css-auuar5']")));

		// Locate the table body
		WebElement tableBody = tableContainer.findElement(By.tagName("tbody"));

		// Get all rows inside the tbody
		List<WebElement> rows = tableBody.findElements(By.tagName("tr"));

		// Ensure there is at least 1 record to edit
		if (rows.isEmpty()) {
			System.out.println("No records found to edit.");
			return;
		}

		// Locate the first row
		WebElement firstRow = rows.get(0);

		// Locate the edit button inside the first row
		WebElement editButton = firstRow.findElement(By.xpath(
				"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeSmall css-ptiqhd-MuiSvgIcon-root'])[2]"));

		// Click the edit button
		editButton.click();
		System.out.println("Opened edit form for the first record.");

		// Wait for the edit form to appear
		WebElement editForm = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiPaper-root")));

		Thread.sleep(1000); // Allow UI to stabilize

		// Locate the "Update" button inside the form
		WebElement updateButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//button[contains(@class, 'MuiButton-containedPrimary') and text()='Update']")));

		// Scroll inside the form until "Update" button is visible
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", updateButton);

		// Wait to ensure scrolling is completed
		Thread.sleep(2000);

		// Click the "Update" button
		updateButton.click();
		System.out.println("Clicked the Update button.");

		// Wait for the form to close
		Thread.sleep(1000);

		// Click anywhere on the screen (outside the form) to close it
		Actions actions = new Actions(driver);
		actions.moveByOffset(10, 10).click().perform();
		System.out.println("Closed the edit form by clicking outside.");
	}

	@Test(priority = 6)
	public void switchToLandingSlider() throws InterruptedException {

		// Locate the table container div
		driver.findElement(By.xpath("//span[text()='Landing Sliders']")).click();

	}

	@Test(priority = 7)
	public void createLandingSliders() throws InterruptedException, IOException {
		// Click on 'Landing Sliders' section
		driver.findElement(By.xpath("//span[text()='Landing Sliders']")).click();
		Thread.sleep(2000);

		// Load JSON test data
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonData = objectMapper.readTree(new File("src/test/resources/landing_slider_data.json"));

		// Loop through JSON array
		for (JsonNode sliderData : jsonData) {
			// Extract fields from JSON
			String cardTitle = sliderData.get("cardTitle").asText();
			String cardSubTitle = sliderData.get("cardSubTitle").asText();
			String cardDescription = sliderData.get("cardDescription").asText();
			String highlightText = sliderData.get("highlightText").asText();
			String source = sliderData.get("source").asText();

			// Click 'Create Slider Card' button for each slider
			driver.findElement(By.xpath("//span[text()='Create Slider Card']")).click();
			Thread.sleep(2000);

			// Enter values into form fields
			driver.findElement(By.name("cardTitle")).sendKeys(cardTitle);
			driver.findElement(By.name("cardSubTitle")).sendKeys(cardSubTitle);
			driver.findElement(By.className("ql-editor")).sendKeys(cardDescription); // Quill editor
			driver.findElement(By.name("highlightText")).sendKeys(highlightText);
			driver.findElement(By.name("source")).sendKeys(source);

			// Handle dropdown selection for Linked Landing Card
			WebElement dropdown = driver.findElement(By.id("mui-component-select-linkedLandingCardId"));
			dropdown.click();

			// Wait for dropdown items to be visible
			List<WebElement> options = wait.until(ExpectedConditions
					.presenceOfAllElementsLocatedBy(By.xpath("//ul[contains(@class, 'MuiMenu-list')]/li")));

			// Select a random option
			if (!options.isEmpty()) {
				Random random = new Random();
				int randomIndex = random.nextInt(options.size()); // Get random index
				options.get(randomIndex).click(); // Click the random option
			}

			// Submit the form
			driver.findElement(By.xpath("(//button[normalize-space()='Create'])[1]")).click();
			Thread.sleep(3000);
		}
	}

	@Test(priority = 8)
	public void testPagination() throws InterruptedException {
		// Click on 'Landing Sliders' section again
		driver.findElement(By.xpath("//span[text()='Landing Sliders']")).click();
		Thread.sleep(2000);

		// Click through pagination
		PaginationClickUntilUnclickable();
	}

	private void PaginationClickUntilUnclickable() throws InterruptedException {
		By buttonLocator = By.xpath("(//div[contains(@class, 'MuiBox-root css-67tg06')]//button)[last()]");
		By buttonLocatorFirst = By.xpath("(//div[contains(@class, 'MuiBox-root css-67tg06')]//button)[1]");
		JavascriptExecutor js = (JavascriptExecutor) driver;

		while (true) {
			try {
				WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));

				// Scroll into view to make sure it's visible
				js.executeScript("arguments[0].scrollIntoView({block: 'center'});", button);

				// Try normal click first
				try {
					button.click();
				} catch (ElementClickInterceptedException e) {
					System.out.println("Click intercepted! Using JavaScript click.");
					js.executeScript("arguments[0].click();", button);
				}

				Thread.sleep(500); // Wait for UI to update before clicking again
			} catch (TimeoutException | NoSuchElementException e) {
				System.out.println("Button is no longer clickable. Stopping the function.");
				break;
			}
		}

		while (true) {
			try {
				WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonLocatorFirst));

				// Scroll into view to make sure it's visible
				js.executeScript("arguments[0].scrollIntoView({block: 'center'});", button);

				// Try normal click first
				try {
					button.click();
				} catch (ElementClickInterceptedException e) {
					System.out.println("Click intercepted! Using JavaScript click.");
					js.executeScript("arguments[0].click();", button);
				}

				Thread.sleep(500); // Wait for UI to update before clicking again
			} catch (TimeoutException | NoSuchElementException e) {
				System.out.println("Button is no longer clickable. Stopping the function.");
				break;
			}
		}
	}

	@Test(priority = 9)
	public void verifyTableRowsPresence() throws InterruptedException {
		// Locate the table container div
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement tableContainer = driver.findElement(By.className("css-auuar5"));

		// Find all rows in the tbody of the table
		List<WebElement> tableRows = tableContainer.findElements(By.xpath(".//tbody/tr"));

		// Check if rows exist in the table
		if (!tableRows.isEmpty()) {
			System.out.println("Table contains " + tableRows.size() + " row(s).");
		} else {
			System.out.println("Table is empty.");
		}

		// Assert that there is at least one row
		Assert.assertFalse(tableRows.isEmpty(), "The table is empty!");

		if (tableRows.size() > 0) {
			System.out.println("Rows found in table: " + tableRows.size());

			// Step 1: Click on the first "View" button
			WebElement viewButton1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeSmall css-ptiqhd-MuiSvgIcon-root'])[1]")));
			viewButton1.click();

			// Wait for the form to appear
			Thread.sleep(2000);

			Actions actions = new Actions(driver);
			actions.moveByOffset(10, 10).click().perform();
			Thread.sleep(2000);

			// Step 2: Click on the second "View" button
			WebElement viewButton2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeSmall css-ptiqhd-MuiSvgIcon-root'])[2]")));
			viewButton2.click();
			Thread.sleep(2000);

			// Scroll to "Update" button and click it
			WebElement updateButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("(//button[normalize-space()='Update'])[1]")));
			js.executeScript("arguments[0].scrollIntoView(true);", updateButton);
			updateButton.click();
			Thread.sleep(2000);

			// Step 3: Click on the third "Delete" button
			WebElement viewButton3 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeSmall css-ptiqhd-MuiSvgIcon-root'])[3]")));
			viewButton3.click();
			Thread.sleep(2000);

			// Step 4: Click on the same third "Delete" button after confirmation deletion
			// appears
			WebElement confirmDelete = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("(//button[normalize-space()='Yes, Delete'])[1]")));
			confirmDelete.click();
			Thread.sleep(2000);

			System.out.println("Test Case Passed");

		} else {
			System.out.println("No rows found in the table. Skipping test case.");
		}
	}

	@AfterClass
	public void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
