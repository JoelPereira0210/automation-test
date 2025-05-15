package societyApp.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.json.JSONArray;
import org.json.JSONObject;

import societyAppUtils.AdminLoginHelper; // ✅ centralized login import

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.testng.annotations.Test;

public class AdminNewMembers {
	@Test
	public void addNewMembers() throws Exception {
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");

		wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

		// Perform login
//		login(driver, wait);
		AdminLoginHelper.login(driver); // ✅ replaced old login method
		Thread.sleep(3000);

		// Refresh after login to reset page state
		driver.navigate().refresh();
		Thread.sleep(3000);

		driver.findElement(By.xpath("//span[text()='Add New Member']")).click();

		// Load and parse the JSON file
		JSONArray testCases = loadTestCases();

		// Iterate over the test cases
		for (int i = 0; i < testCases.length(); i++) {
			JSONObject testCase = testCases.getJSONObject(i);
			processTestCase(driver, wait, testCase, i);
		}

		// Handle checkboxes and remove action
		handleCheckboxesAndRemove(driver);

		// Click the first checkbox
		WebElement firstCheckboxElement = driver.findElement(By.cssSelector("input[type='checkbox']"));
		if (!firstCheckboxElement.isSelected()) {
			firstCheckboxElement.click(); // Click the first checkbox if it's not already selected
		}

		// Find the Submit button by its visible text and click it
		WebElement submitButton = driver.findElement(By.xpath("//span[text()='Submit']/ancestor::button"));
		submitButton.click();

		driver.navigate().refresh();
		Thread.sleep(3000);

		driver.quit();
	}

	// Method for login process
//	private void login(WebDriver driver, WebDriverWait wait) {
//		// Wait for the element to be visible and clickable
//		WebElement tabElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tab:r0:1")));
//
//		// Click the element
//		tabElement.click();		
//		driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("7498263271");
//		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Rich@123");
//		driver.findElement(By.cssSelector("button[type='submit']")).click();
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

	// Load the JSON test cases
	private JSONArray loadTestCases() throws IOException {
		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/adminInput.json")));
		return new JSONArray(content);
	}

	// Process each test case
	private void processTestCase(WebDriver driver, WebDriverWait wait, JSONObject testCase, int index)
			throws InterruptedException {
		// Fill fields and validate
		fillAndValidateField(driver, wait, By.cssSelector(".PhoneInput input"), testCase.getString("mobile"), "Mobile",
				index);
		fillAndValidateField(driver, wait, By.cssSelector("input[name='firstName']"), testCase.getString("firstName"),
				"First Name", index);
		fillAndValidateField(driver, wait, By.cssSelector("input[name='lastName']"), testCase.getString("lastName"),
				"Last Name", index);

		// Submit the form
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		Thread.sleep(1000); // Wait for form submission processing

		// Capture and print validation error messages
		handleValidationErrors(driver, index);

		// Allow time for fields to reset or form to clear
		Thread.sleep(500);
	}

	// Fill a field, validate, and print logs
	private void fillAndValidateField(WebDriver driver, WebDriverWait wait, By locator, String value, String fieldName,
			int index) {
		WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		clearField(inputField);
		System.out.println("Filling " + fieldName + ": " + value);
		inputField.sendKeys(value);
	}

	// Method to clear the input field
	private void clearField(WebElement field) {
		field.sendKeys(Keys.CONTROL + "a"); // Select all text
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		field.sendKeys(Keys.CONTROL + "x"); // Cut (clear) the selected text
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Handle validation errors after form submission
	private void handleValidationErrors(WebDriver driver, int index) {
		List<WebElement> errorMessages = driver.findElements(By.cssSelector(".MuiTypography-root.MuiTypography-body2"));
		boolean isValid = true;
		for (WebElement errorMessage : errorMessages) {
			if (!errorMessage.getText().isEmpty()) {
				System.out.println("Validation Error for Test Case " + (index + 1) + ": " + errorMessage.getText());
				isValid = false;
			}
		}
		System.out.println(isValid ? "Test Case " + (index + 1) + " Passed: No validation errors."
				: "Test Case " + (index + 1) + " Failed: Validation errors present.");
	}

	// Handle checkboxes and remove action
	private void handleCheckboxesAndRemove(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement firstCheckbox = driver.findElement(By.cssSelector("input[type='checkbox']"));
		js.executeScript("arguments[0].scrollIntoView(true);", firstCheckbox);

		List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
		if (checkboxes.size() >= 4) {
			checkboxes.get(1).click();
			checkboxes.get(2).click();
		}

		WebElement removeButton = driver.findElement(By.xpath("//span[text()='Remove']"));
		removeButton.click();
	}
}
