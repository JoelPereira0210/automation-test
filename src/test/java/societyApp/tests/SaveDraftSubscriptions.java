package societyApp.tests;

import societyAppModels.SubscriptionData;
import societyAppUtils.JsonDataReader;
import societyAppUtils.LoginHelper;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SaveDraftSubscriptions {
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeTest
	public void setup() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		// Login first
		LoginHelper.login(driver);
	}

	@Test
	public void createDraftSubscriptions() throws Exception {
		List<SubscriptionData> subscriptions = JsonDataReader.getSubscriptions();

		for (SubscriptionData sub : subscriptions) {
			System.out.println("\nProcessing Subscription: " + sub.planName);

			// Wait for the page to fully load
			waitUntilPageLoads(driver);

			// Attempt to click "Create Subscription"
			if (!clickCreateSubscriptionButton()) {
				continue;
			}

			// Fill the form fields
			enterFormData(sub);

			// Perform client-side validation
			if (!isValidSubscription(sub)) {
				System.out.println("Validation failed! Skipping record...");
				closeForm();
				continue;
			}

			// Click "Publish"
			WebElement publishButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Save Draft']")));
			publishButton.click();

			Thread.sleep(1000); // Allow time for validation errors or success pop-up

			// Handle Success or Validation Errors
			if (isSuccessPopupPresent()) {
				System.out.println("Subscription Created Successfully!");
				closeSuccessPopup();
			} else {
				handleValidationErrors(); // Ensures form closes and moves to next record
			}
		}

		System.out.println("Test Completed Successfully!");
	}

	private boolean clickCreateSubscriptionButton() {
		try {
			WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[contains(@class, 'tabs')]/button[contains(., 'Create Subscription')]")));
			button.click();
			return true;
		} catch (TimeoutException e) {
			System.out.println("'Create Subscription' button not found or not clickable. Skipping record...");
			return false;
		}
	}

	private void enterFormData(SubscriptionData sub) {
		enterText(By.cssSelector("input[placeholder='Plan Name']"), sub.planName);
		enterText(By.cssSelector("textarea[placeholder='Enter Plan Description']"), sub.description);
		enterText(By.cssSelector("input[placeholder='Duration']"), sub.duration);
		enterText(By.cssSelector("input[placeholder='Max Users']"), sub.maxUsers);
		enterText(By.cssSelector("input[placeholder='Amount']"), sub.amount);
	}

	private void enterText(By locator, String value) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		element.clear();
		element.sendKeys(value);
	}

	private boolean isValidSubscription(SubscriptionData sub) {
		// Validate Plan Name
		if (sub.planName == null || sub.planName.trim().isEmpty()) {
			System.out.println("Plan Name is required.");
			return false;
		}

		// Validate Description
		if (sub.description == null || sub.description.trim().isEmpty()) {
			System.out.println("Description is required.");
			return false;
		}

		// Validate Duration (Only positive integers allowed)
		if (!sub.duration.matches("\\d+")) {
			System.out.println("Duration must be a valid positive integer.");
			return false;
		}
		if (Integer.parseInt(sub.duration) <= 0) {
			System.out.println("Duration must be greater than 0.");
			return false;
		}

		// Validate Max Users (Only positive integers allowed, at least 1 user)
		if (!sub.maxUsers.matches("\\d+")) {
			System.out.println("Max Users must be a valid positive integer.");
			return false;
		}
		if (Integer.parseInt(sub.maxUsers) < 1) {
			System.out.println("Max Users must be at least 1.");
			return false;
		}

		// Validate Amount (Only positive decimal numbers, up to 2 decimal places)
		if (!sub.amount.matches("^\\d+(\\.\\d{1,2})?$")) {
			System.out.println("Amount must be a valid decimal number (up to 2 decimal places).");
			return false;
		}
		if (Double.parseDouble(sub.amount) <= 0) {
			System.out.println("Amount must be greater than 0.");
			return false;
		}

		System.out.println("Subscription data is valid.");
		return true;
	}

	private void handleValidationErrors() {
		List<WebElement> errorMessages = driver.findElements(By.xpath("//p[contains(@class, 'MuiTypography-root')]"));

		if (!errorMessages.isEmpty()) {
			System.out.println("Validation Errors Found:");
			for (WebElement error : errorMessages) {
				System.out.println("  - " + error.getText());
				try {
					Thread.sleep(500); // Ensure UI stability before clicking
					error.click();
				} catch (Exception e) {
					System.out.println("Could not click validation error: " + error.getText());
				}
			}
		}

		closeForm(); // Ensure the form closes before moving to next record
	}

	private void closeForm() {
		try {
			// Locate the close button (Try both approaches)
			WebElement closeButton = driver.findElement(By.xpath("(//*[name()='path'])[22]"));
			if (closeButton.isDisplayed() && closeButton.isEnabled()) {
				closeButton.click();
			} else {
				System.out.println("Close button not interactable, trying alternative method...");
				WebElement cancelButton = driver
						.findElement(By.xpath("//div[@class='MuiBox-root css-0']//*[name()='svg']"));
				cancelButton.click();
			}

			Thread.sleep(1000); // Allow UI to process
			System.out.println("Form closed. Moving to the next record...");
		} catch (NoSuchElementException e) {
			System.out.println("Close button not found. Skipping to next record...");
		} catch (Exception e) {
			System.out.println("Unexpected error while closing form: " + e.getMessage());
		}
	}

	private boolean isSuccessPopupPresent() {
		try {
			WebElement successMessage = driver
					.findElement(By.xpath("//div[contains(text(), 'Subscription created successfully')]"));
			return successMessage.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private void closeSuccessPopup() {
		try {
			WebElement closeButton = driver.findElement(By.xpath("//button[contains(text(), 'OK')]"));
			closeButton.click();
		} catch (NoSuchElementException e) {
			System.out.println("No close button found for success popup.");
		}
	}

	private void waitUntilPageLoads(WebDriver driver) {
		new WebDriverWait(driver, Duration.ofSeconds(20)).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").equals("complete"));
	}

	@AfterTest
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			System.out.println("Browser closed.");
		}
	}
}
