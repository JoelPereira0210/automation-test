package societyApp.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.json.JSONArray;
import org.json.JSONObject;

import societyAppUtils.AdminLoginHelper; // ✅ Import the helper class

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import org.testng.annotations.*;

public class AdminCharges {
	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeClass
	public void setUp() throws Exception {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		js = (JavascriptExecutor) driver;
		driver.manage().window().maximize();

		// ✅ Use shared Admin login
		AdminLoginHelper.login(driver);

		// ✅ Navigate to Charges/Fees section
		WebElement chargesFeesTab = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[contains(@class, 'MuiListItem-root') and .//span[text()='Charges/Fees']]")));
		chargesFeesTab.click();

		System.out.println("Successfully navigated to Charges/Fees section.");
	}

	@Test
	public void testChargesSection() throws IOException, InterruptedException {
		// Load and parse the JSON file
		JSONArray chargesData = loadChargesData();

		// Step 1: Create all charges first
		for (int i = 0; i < chargesData.length(); i++) {
			JSONObject charge = chargesData.getJSONObject(i);
			clickCreateChargeButton();
			fillChargeForm(charge);
			submitChargeForm();
			Thread.sleep(2000); // Ensure charge is created before proceeding
		}
		for (int i = 0; i < chargesData.length(); i++) {
			JSONObject charge = chargesData.getJSONObject(i);
			clickCreateChargeButton();
			fillChargeForm(charge);
			submitChargeFormDraft();
			Thread.sleep(2000); // Ensure charge is created before proceeding
		}

		// Step 2: After all charges are created, process each row
		processAllRows(chargesData.length());
		deleteCharges(chargesData.length()); // Deletes all created charges
		PaginationclickUntilUnclickable(); // validdating the pagination button at the bottom
		navigateToPastSection();
		navigateToDraftAndPublish();
		deleteChargesDeleted(); // Deletes all created charges

		Thread.sleep(10000);
		driver.quit();

	}

	private void processAllRows(int rowCount) throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr")));

		for (int i = 1; i <= rowCount; i++) {
			try {
				// Locate and click "More Options" button
				WebElement moreOptions = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-i4bv87-MuiSvgIcon-root'])[8]")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", moreOptions);
				Thread.sleep(500);
				moreOptions.click();
				Thread.sleep(1000);

				// Click "View Details"
				WebElement viewDetails = wait.until(ExpectedConditions.elementToBeClickable(
						By.xpath("(//li[@role='menuitem'][normalize-space()='View Details'])[10]")));
				viewDetails.click();
				Thread.sleep(2000);

				// Close the View Details modal
				WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-i4bv87-MuiSvgIcon-root'])[12]")));
				closeButton.click();
				Thread.sleep(1000);

				// Re-locate and click "More Options" button again
				moreOptions = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-i4bv87-MuiSvgIcon-root'])[8]")));
				moreOptions.click();
				Thread.sleep(1000);

				// Click "Edit"
				WebElement editOption = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("(//li[@role='menuitem'][normalize-space()='Edit'])[10]")));
				editOption.click();
				Thread.sleep(2000);

				// Modify Name Field
				WebElement nameField = wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.cssSelector("input[placeholder='Enter fee name']")));
				nameField.sendKeys(Keys.CONTROL + "a");
				nameField.sendKeys(Keys.DELETE);
				nameField.sendKeys("Updated Fee Name");

				// Modify Description Field
				WebElement descField = wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.cssSelector("textarea[placeholder='Enter fee description']")));
				descField.sendKeys(Keys.CONTROL + "a");
				descField.sendKeys(Keys.DELETE);
				descField.sendKeys("Updated Fee Description");

				// Click "Save Changes"
				WebElement saveChangesBtn = wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("//button[.//span[text()='save Changes']]")));
				saveChangesBtn.click();
				Thread.sleep(1000);

				System.out.println("Updated and saved changes for row " + i);

				// Re-locate and click "View Details" again
				viewDetails = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-i4bv87-MuiSvgIcon-root'])[8]")));
				viewDetails.click();
				Thread.sleep(1000);

				// Click on the last <li> inside the menu
				WebElement lastLiElement = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("(//ul[contains(@class, 'MuiMenu-list')]//li)[last()]")));
				lastLiElement.click();
				Thread.sleep(1000);

				// click on the export button
				WebElement exportButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"(//span[@class='MuiTypography-root MuiTypography-text8 css-10rgyz8-MuiTypography-root'])[1]")));
				exportButton.click();
				Thread.sleep(1000);

				// click on export as PDF
				driver.findElement(By.xpath("(//li[normalize-space()='Export as CSV'])[1]")).click();
				Thread.sleep(500);
				driver.findElement(By.xpath("(//div[@role='button'])[4]")).click();

			} catch (StaleElementReferenceException e) {
				System.out.println("StaleElementReferenceException caught for row " + i + ". Retrying...");
				i--; // Retry the same row
				Thread.sleep(1000); // Wait before retrying
			} catch (TimeoutException e) {
				System.out.println("Timeout while processing row " + i + ": " + e.getMessage());
			}
		}
	}

	private JSONArray loadChargesData() throws IOException {
		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/chargesData.json")));
		return new JSONArray(content);
	}

	private void clickCreateChargeButton() throws InterruptedException {
		// Click the "Create Charge" button
		Thread.sleep(1000);
		driver.findElement(By.xpath(
				"(//button[@class='MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButton-colorPrimary MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButton-colorPrimary css-i6kk2x-MuiButtonBase-root-MuiButton-root'])[1]"))
				.click();
		Thread.sleep(1000); // Wait for the form to open
		System.out.println("Clicked on Create Charge button.");
	}

	private void fillChargeForm(JSONObject charge) throws InterruptedException {
		// Fill the form fields
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Enter fee name']")))
				.sendKeys(charge.getString("name"));
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector("textarea[placeholder='Enter fee description']")))
				.sendKeys(charge.getString("description"));

		// Convert date format from MM/DD/YYYY to "Month YYYY" and extract day
		String[] dateParts = charge.getString("dueDate").split("/");
		String monthYear = convertMonthNumberToName(dateParts[0]) + " " + dateParts[2];
		String day = dateParts[1];

		// Select the due date using the date picker
		selectDateFromDatePicker(
				"(//fieldset[@class='MuiOutlinedInput-notchedOutline css-1d3z3hw-MuiOutlinedInput-notchedOutline'])[1]",
				monthYear, day);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Enter amount']")))
				.sendKeys(charge.getInt("amount") + "");

		// Select Fee Type
		WebElement feeTypeInput = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".react-dropdown-select-input")));
		feeTypeInput.sendKeys(charge.getString("feeType"));
		Thread.sleep(1000); // Wait for options to load

		// Click on dropdown selection again
		WebElement dropdownSelection = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"//div[@class='react-dropdown-select-content react-dropdown-select-type-single css-jznujr-ContentComponent e1gn6jc30']")));
		dropdownSelection.click();
		Thread.sleep(500);

		// Click on "Add New"
		WebElement addNewOption = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@role='option']")));
		addNewOption.click();
		Thread.sleep(500);

		System.out.println("Fee type selected and added successfully.");

		Thread.sleep(1000);
	}

	private void submitChargeForm() {
		// Click the "Publish" button
		WebElement publishButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Publish']/ancestor::button")));
		publishButton.click();

		System.out.println("Charge form saved as draft.");
	}

	private void submitChargeFormDraft() {
		// Click the "Publish" button
		WebElement publishButton = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Save Draft']/ancestor::button")));
		publishButton.click();

		System.out.println("Charge form saved as draft.");
	}

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

	private void deleteCharges(int deleteCount) throws InterruptedException {
		for (int i = 0; i < deleteCount; i++) {
			try {
				// Locate and click "More Options" button for the current row
				WebElement moreOptions = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-i4bv87-MuiSvgIcon-root'])[8]")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", moreOptions);
				Thread.sleep(500);
				moreOptions.click();
				Thread.sleep(1000);

				// Click "Delete" option
				WebElement deleteOption = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("(//li[@role='menuitem'][normalize-space()='Delete'])[10]")));
				deleteOption.click();
				Thread.sleep(1000);

				// Confirm deletion by clicking "Yes, Delete" button
				WebElement confirmDelete = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("(//button[normalize-space()='Yes, Delete'])[1]")));
				confirmDelete.click();
				Thread.sleep(2000);

				System.out.println("Deleted charge for row " + (i + 1));

			} catch (StaleElementReferenceException e) {
				System.out.println(
						"StaleElementReferenceException caught while deleting row " + (i + 1) + ". Retrying...");
				i--; // Retry the same row
				Thread.sleep(1000); // Wait before retrying
			} catch (TimeoutException e) {
				System.out.println("Timeout while deleting row " + (i + 1) + ": " + e.getMessage());
			}
		}
	}

	private void deleteChargesDeleted() throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Wait up to 10 seconds
		WebElement deletedButton = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//span[contains(@class, 'MuiTypography-root') and text()='Deleted']")));
		deletedButton.click();
		for (int i = 0; i < 2; i++) {
			try {
				// Locate and click "More Options" button for the current row
				WebElement moreOptions = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-i4bv87-MuiSvgIcon-root'])[8]")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", moreOptions);
				Thread.sleep(500);
				moreOptions.click();
				Thread.sleep(1000);

				// Click "Delete" option
				WebElement deleteOption = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("(//li[@role='menuitem'][normalize-space()='Delete'])[10]")));
				deleteOption.click();
				Thread.sleep(1000);

				// Confirm deletion by clicking "Yes, Delete" button
				WebElement confirmDelete = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("(//button[normalize-space()='Yes, Delete'])[1]")));
				confirmDelete.click();
				Thread.sleep(2000);

				System.out.println("Deleted charge for row " + (i + 1));

			} catch (StaleElementReferenceException e) {
				System.out.println(
						"StaleElementReferenceException caught while deleting row " + (i + 1) + ". Retrying...");
				i--; // Retry the same row
				Thread.sleep(1000); // Wait before retrying
			} catch (TimeoutException e) {
				System.out.println("Timeout while deleting row " + (i + 1) + ": " + e.getMessage());
			}
		}

	}

	private void PaginationclickUntilUnclickable() throws InterruptedException {
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

	public void navigateToPastSection() throws InterruptedException {
		// Click on the "Past" button
		By pastButton = By.xpath("(//button[contains(@class,'tab')])[2]");
		wait.until(ExpectedConditions.elementToBeClickable(pastButton)).click();
		System.out.println("Clicked on 'Past' button.");

		// Click on the "More" button
		By moreButtonPast = By.xpath(
				"//button[contains(@class, 'MuiIconButton-root') and contains(@class, 'css-78trlr-MuiButtonBase-root-MuiIconButton-root')]");
		wait.until(ExpectedConditions.elementToBeClickable(moreButtonPast)).click();
		System.out.println("Clicked on 'More' button.");

		// Click on the first <li> element under the <ul> (View Details)
		By firstMenuItem = By.xpath("(//li[@role='menuitem'][normalize-space()='View Details'])[10]");
		wait.until(ExpectedConditions.elementToBeClickable(firstMenuItem)).click();
		System.out.println("Clicked on 'View Details' menu item.");

		// Wait for 500ms
		Thread.sleep(500);

		// Click on the "Close" button
		By closeButton = By.xpath(
				"(//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeMedium css-t2u3ip-MuiButtonBase-root-MuiIconButton-root'])[1]");
		wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
		System.out.println("Clicked on 'Close' button.");

		// Click on the "More" button again
		wait.until(ExpectedConditions.elementToBeClickable(moreButtonPast)).click();
		System.out.println("Clicked on 'More' button again.");

		// Click on "Edit" option from the menu
		By editMenuItem = By.xpath("(//li[@role='menuitem'][normalize-space()='Edit'])[10]");
		wait.until(ExpectedConditions.elementToBeClickable(editMenuItem)).click();
		System.out.println("Clicked on 'Edit' menu item.");

		// Wait for the form to appear
		Thread.sleep(1000);

		// Update the "Name" field using select-all and backspace
		By nameField = By.xpath("//input[@name='name']");
		WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));
		nameInput.sendKeys(Keys.CONTROL + "a");
		nameInput.sendKeys(Keys.BACK_SPACE);
		nameInput.sendKeys("Updated Fee Name");
		System.out.println("Updated 'Name' field.");

		// Update the "Description" field using select-all and backspace
		By descriptionField = By.xpath("//textarea[@name='description']");
		WebElement descriptionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionField));
		descriptionInput.sendKeys(Keys.CONTROL + "a");
		descriptionInput.sendKeys(Keys.BACK_SPACE);
		descriptionInput.sendKeys("Updated Fee Description");
		System.out.println("Updated 'Description' field.");

		// Click on the "Save Changes" button
		By saveChangesButton = By.xpath("//button[.//span[text()='save Changes']]");
		wait.until(ExpectedConditions.elementToBeClickable(saveChangesButton)).click();
		System.out.println("Clicked on 'Save Changes' button.");

		wait.until(ExpectedConditions.elementToBeClickable(moreButtonPast)).click();
		System.out.println("Clicked on 'More' button again.");

		// Click on "Payment Status" option
		By paymentStatusOption = By.xpath("(//li[@role='menuitem'][normalize-space()='Payment Status'])[10]");
		wait.until(ExpectedConditions.elementToBeClickable(paymentStatusOption)).click();
		System.out.println("Clicked on 'Payment Status' option.");

		// Click on "Export" button
		By exportButton = By.xpath("//button[.//span[text()='Export']]");
		wait.until(ExpectedConditions.elementToBeClickable(exportButton)).click();
		System.out.println("Clicked on 'Export' button.");

		driver.findElement(By.xpath("(//li[normalize-space()='Export as CSV'])[1]")).click();

		// Sleep for 1 second
		Thread.sleep(1000);

		// Click on Back Arrow icon
		By backArrow = By.cssSelector(".MuiSvgIcon-root.MuiSvgIcon-fontSizeLarge.css-1tfx1ua-MuiSvgIcon-root");
		wait.until(ExpectedConditions.elementToBeClickable(backArrow)).click();
		System.out.println("Clicked on 'Back Arrow' icon.");
	}

	public void navigateToDraftAndPublish() throws InterruptedException {
		// Click on "Draft" tab
		By draftTab = By.xpath("//button[.//span[text()='Draft']]");
		wait.until(ExpectedConditions.elementToBeClickable(draftTab)).click();
		System.out.println("Clicked on 'Draft' tab.");

		// Click on "More Options" button
		By moreOptions = By.xpath("(//button[@type='button'])[8]");
		wait.until(ExpectedConditions.elementToBeClickable(moreOptions)).click();
		System.out.println("Clicked on 'More Options' button.");

		// Wait for the dropdown menu to appear
		By dropdownMenu = By.xpath("//ul[contains(@class, 'MuiList-root')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownMenu));

		// Wait for the second <li> ("Edit") to be visible and clickable
		By editOption = By.xpath("(//ul[contains(@class, 'MuiList-root')]//li)[2]");
		WebElement editElement = wait.until(ExpectedConditions.elementToBeClickable(editOption));

		// Use JavaScript to click if normal click fails
		try {
			editElement.click();
			System.out.println("Clicked on 'Edit' option successfully.");
		} catch (ElementClickInterceptedException e) {
			System.out.println("ElementClickInterceptedException occurred. Trying JavaScript click...");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", editElement);
		}

		// Wait for form to load
		Thread.sleep(1000);

		// Click on the "Publish" button
		By publishButton = By.xpath("//button[.//span[text()='Publish']]");
		wait.until(ExpectedConditions.elementToBeClickable(publishButton)).click();
		System.out.println("Clicked on 'Publish' button.");
	}

	private String convertMonthNumberToName(String monthNumber) {
		switch (monthNumber) {
		case "01":
			return "January";
		case "02":
			return "February";
		case "03":
			return "March";
		case "04":
			return "April";
		case "05":
			return "May";
		case "06":
			return "June";
		case "07":
			return "July";
		case "08":
			return "August";
		case "09":
			return "September";
		case "10":
			return "October";
		case "11":
			return "November";
		case "12":
			return "December";
		default:
			throw new IllegalArgumentException("Invalid month number: " + monthNumber);
		}
	}
}
