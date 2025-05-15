package societyApp.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import societyAppUtils.AdminLoginHelper; // ✅ Added centralized login helper

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

public class AdminEvents {
	private WebDriver driver;
	private WebDriverWait wait;
	private JSONObject eventData;
	private JavascriptExecutor js;

	private static final String EVENT_START_DATE = "April 2025";
	private static final String EVENT_START_DAY = "25";

	private static final String EVENT_END_DATE = "April 2025";
	private static final String EVENT_END_DAY = "27";

	private static final String EVENT_REMINDER_DATE = "April 2025";
	private static final String EVENT_REMINDER_DAY = "28";

	@BeforeClass
	public void setUp() throws Exception {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");
		js = (JavascriptExecutor) driver;

		// Login before executing tests
//		wait.until(ExpectedConditions.elementToBeClickable(By.id("tab:r0:1"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Mobile Number']")))
//				.sendKeys("7498263271");
//		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Rich@123");
//		driver.findElement(By.cssSelector("button[type='submit']")).click();
//		Thread.sleep(4000); // Wait for login to complete
//		driver.navigate().refresh();
//		Thread.sleep(1000); // Ensure correct page state

		// ✅ Use shared Admin login
		AdminLoginHelper.login(driver);

		// Read JSON file for event details
		JSONParser parser = new JSONParser();
		try {
			FileReader reader = new FileReader("src/test/resources/eventDetails.json");
			eventData = (JSONObject) parser.parse(reader);
			reader.close();
		} catch (IOException | ParseException e) {
			System.out.println("Error reading JSON file: " + e.getMessage());
			throw new RuntimeException("Failed to load event details.");
		}
	}

	@Test(priority = 1)
	public void navigateToEventsSection() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@role='button'])[3]"))).click();
		Thread.sleep(500);
		System.out.println("Navigated to Events section.");
	}

	@Test(priority = 2)
	public void createEvent() throws Exception {
		wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[contains(@class, 'tabs')]//button[contains(., 'Create New Event')]"))).click();
		Thread.sleep(1000); // Wait for modal to open

		// Fill event details
		driver.findElement(By.id("eventName")).sendKeys(eventData.get("eventName").toString());
		driver.findElement(By.cssSelector(".ql-editor")).sendKeys(eventData.get("eventDescription").toString());

		// Select Dates using variables
		selectDateFromDatePicker("//input[@id=':r2:' and @placeholder='MM/DD/YYYY']", EVENT_START_DATE,
				EVENT_START_DAY);
		selectDateFromDatePicker("//input[@id=':r5:' and @placeholder='MM/DD/YYYY']", EVENT_END_DATE, EVENT_END_DAY);
		selectDateFromDatePicker("//input[@id=':r8:' and @placeholder='MM/DD/YYYY']", EVENT_REMINDER_DATE,
				EVENT_REMINDER_DAY);

		Thread.sleep(3000); // Small wait to stabilize UI
		// Click on the time input field
		WebElement timeInput = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@id=':rb:'])[1]")));
		timeInput.click();

		// Wait for the clock UI to appear
		Thread.sleep(2000);
		WebElement hourSelection = wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//span[@role='option' and @aria-label='2 hours']")));

		Actions actions = new Actions(driver);
		actions.moveToElement(hourSelection).click().perform();

		// Click on the OK button
		WebElement okButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[normalize-space()='OK'])[1]")));
		okButton.click();

		Thread.sleep(3000); // Wait for changes to reflect

		// Click on the second time input field
		WebElement secondTimeInput = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@id=':re:'])[1]")));
		secondTimeInput.click();

		// Wait for the clock UI to appear
		Thread.sleep(2000);

		// Select "3 hours"
		WebElement hourSelection1 = wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//span[@role='option' and @aria-label='3 hours']")));

		actions.moveToElement(hourSelection1).click().perform();

		// Click on the OK button
		WebElement okButton1 = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[normalize-space()='OK'])[1]")));
		okButton1.click();

		Thread.sleep(3000); // Wait for changes to reflect

		// Click Continue button
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Continue']"))).click();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Continue']"))).click();
		Thread.sleep(500);

		// Set max number of people
		driver.findElement(By.xpath("(//input[@placeholder='Enter Max number of people for event'])[1]"))
				.sendKeys("200");
		Thread.sleep(400);

		// Click checkbox
		driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")).click();

		// Click Continue
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Continue']"))).click();

		// Click Publish Event
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[normalize-space()='Publish Event'])[1]")))
				.click();
	}

	@Test(priority = 3)
	public void viewFirstTwoEventDetails() throws InterruptedException {
		// Locate the first two event cards
		for (int i = 1; i <= 1; i++) {
			try {
				// Wait for the MoreHorizIcon (three dots) to be present and clickable
				WebElement moreHorizIcon = wait
						.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@aria-label='more'])")));

				// Scroll into view and click the MoreHorizIcon
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", moreHorizIcon);
				Thread.sleep(500); // Small delay after scrolling
				moreHorizIcon.click();

				// Wait for the dropdown menu to appear
				Thread.sleep(500);

				// Wait for the "View Event" option to be clickable and click it
				WebElement viewEventOption = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//ul[@role='menu']//li[contains(text(), 'View Event')]")));
				viewEventOption.click();

				// Wait for the event details page to load
				Thread.sleep(2000);

				// Navigate back to the events section
				driver.navigate().back();
				Thread.sleep(500);
			} catch (StaleElementReferenceException e) {
				// If the element is stale, retry locating and interacting with it
				System.out.println("StaleElementReferenceException caught. Retrying...");
				i--; // Retry the same index
				Thread.sleep(500); // Wait before retrying
			}
		}

//		// Delete the first event card
//		deleteFirstEventCard();
	}

	@Test(priority = 4)
	public void navigateToOtherSections() throws InterruptedException {
		// Navigate to the "Past" section
		WebElement pastTab = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//button[contains(@class, 'tab')]//span[contains(text(), 'Past')]")));
		pastTab.click();
		System.out.println("Navigated to Past section.");
		Thread.sleep(500); // Wait for the page to update

		// Navigate to the "Draft" section
		WebElement draftTab = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//button[contains(@class, 'tab')]//span[contains(text(), 'Draft')]")));
		draftTab.click();
		System.out.println("Navigated to Draft section.");
		Thread.sleep(500); // Wait for the page to update

		// Navigate to the "Deleted" section
		WebElement deletedTab = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//button[contains(@class, 'tab')]//span[contains(text(), 'Deleted')]")));
		deletedTab.click();
		System.out.println("Navigated to Deleted section.");
		Thread.sleep(500); // Wait for the page to update

		WebElement upcomingTab = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//button[contains(@class, 'tab')]//span[contains(text(), 'Upcoming')]")));
		upcomingTab.click();
		System.out.println("Navigated to Upcoming section.");
		Thread.sleep(500); // Wait for the page to update
	}

	@Test(priority = 5)
	public void editFirstEventCard() throws InterruptedException {
		try {
			// Wait for the MoreHorizIcon (three dots) on the first event card to be present
			// and clickable
			WebElement moreHorizIcon = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@aria-label='more'])[1]")));

			// Scroll into view and click the MoreHorizIcon
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", moreHorizIcon);
			Thread.sleep(500); // Small delay after scrolling
			moreHorizIcon.click();

			// Wait for the dropdown menu to appear
			Thread.sleep(500);

			// Wait for the "Edit Event" option to be clickable and click it
			WebElement editEventOption = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//ul[@role='menu']//li[contains(text(), 'Edit Event')]")));
			editEventOption.click();

			// Wait for the edit event page to load
			Thread.sleep(2000);

			// Clear the existing description and add a new description
			WebElement descriptionField = wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//div[@class='ql-editor' and @contenteditable='true']")));
			descriptionField.clear();
			descriptionField.sendKeys("Updated event description");

			// Click the "Continue" button
			WebElement continueButton1 = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), 'Continue')]")));
			continueButton1.click();
			Thread.sleep(500);

			// Click the "Continue" button again
			WebElement continueButton2 = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), 'Continue')]")));
			continueButton2.click();
			Thread.sleep(500);

//			// Click the "Paid Event" option
//			WebElement paidEventOption = wait
//					.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), 'Paid Event')]")));
//			paidEventOption.click();
//			Thread.sleep(500);

			// Locate the "Max Attendees" field
			WebElement maxAttendeesField = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//input[@placeholder='Enter Max number of people for event']")));

			// Click the field to ensure focus
			maxAttendeesField.click();

			// Select all text (Ctrl + A) and delete it
			maxAttendeesField.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Select all text
			maxAttendeesField.sendKeys(Keys.DELETE); // Delete selected text

			// Enter new value
			maxAttendeesField.sendKeys("500");

			// Click the "Continue" button
			WebElement continueButton3 = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), 'Continue')]")));
			continueButton3.click();
			Thread.sleep(500);

			// Click the "Update Event" button
			WebElement updateEventButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), 'Update Event')]")));
			updateEventButton.click();
			Thread.sleep(500);

			System.out.println("First event card updated successfully.");
		} catch (StaleElementReferenceException e) {
			// If the element is stale, retry locating and interacting with it
			System.out.println("StaleElementReferenceException caught while editing the first event card. Retrying...");
			editFirstEventCard(); // Retry the edit process
		}
	}

//	private void deleteFirstEventCard() throws InterruptedException {
//		try {
//			// Wait for the MoreHorizIcon (three dots) on the first event card to be present
//			// and clickable
//			WebElement moreHorizIcon = wait
//					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@aria-label='more'])[1]")));
//
//			// Scroll into view and click the MoreHorizIcon
//			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", moreHorizIcon);
//			Thread.sleep(500); // Small delay after scrolling
//			moreHorizIcon.click();
//
//			// Wait for the dropdown menu to appear
//			Thread.sleep(500);
//
//			// Wait for the "Delete Event" option to be clickable and click it
//			WebElement deleteEventOption = wait.until(ExpectedConditions
//					.elementToBeClickable(By.xpath("//ul[@role='menu']//li[contains(text(), 'Delete Event')]")));
//			deleteEventOption.click();
//
//			// Wait for the confirmation popup to appear
//			Thread.sleep(1000);
//
//			// Click the "Yes, Delete" button in the confirmation popup
//			WebElement confirmDeleteButton = wait.until(
//					ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Yes, Delete')]")));
//			confirmDeleteButton.click();
//
//			// Wait for the deletion to complete
//			Thread.sleep(2000);
//			System.out.println("First event card deleted successfully.");
//		} catch (StaleElementReferenceException e) {
//			// If the element is stale, retry locating and interacting with it
//			System.out
//					.println("StaleElementReferenceException caught while deleting the first event card. Retrying...");
//			deleteFirstEventCard(); // Retry the deletion
//		}
//	}

	private void selectDateFromDatePicker(String datePickerXPath, String expectedMonthYear, String day)
			throws InterruptedException {
		WebElement datePickerInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(datePickerXPath)));

		// Scroll to the element before interacting
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", datePickerInput);
		Thread.sleep(500); // Small delay after scrolling

		// Use JavaScript click instead of Selenium click
		js.executeScript("arguments[0].click();", datePickerInput);
		Thread.sleep(500); // Wait for the date picker to open

		// Wait for the calendar popup to appear
		WebElement monthYearLabel = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiPickersCalendarHeader-label")));

		// Loop until the correct month and year are displayed
		while (!monthYearLabel.getText().contains(expectedMonthYear)) {
			WebElement nextButton = driver.findElement(By.cssSelector("[aria-label='Next month']"));
			js.executeScript("arguments[0].click();", nextButton);
			Thread.sleep(500);
			monthYearLabel = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiPickersCalendarHeader-label")));
		}

		// Select the correct day
		WebElement dateToSelect = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//button[contains(@class, 'MuiPickersDay-root') and text()='" + day + "']")));
		js.executeScript("arguments[0].click();", dateToSelect);
		Thread.sleep(500);

		// Click "OK" if available
		try {
			WebElement okButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'OK')]")));
			js.executeScript("arguments[0].click();", okButton);
		} catch (Exception e) {
			System.out.println("OK button not found or not clickable: " + e.getMessage());
		}
	}

	@AfterClass
	public void teardown() {
		driver.quit();
	}
}
