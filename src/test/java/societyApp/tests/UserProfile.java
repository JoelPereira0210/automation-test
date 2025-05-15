package societyApp.tests;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import societyAppUtils.UserLoginHelper;

public class UserProfile {
	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeClass
	public void setup() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		js = (JavascriptExecutor) driver;
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");
		wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

		// Use common User login helper
		UserLoginHelper.login(driver);
	}

	@Test(priority = 2)
	public void clickOnProfileButton() {
		try {
			System.out.println("Clicking on Profile button...");
			WebElement profileButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='button' and contains(@class, 'MuiButtonBase-root') and .//span[text()='Profile']]")));
			profileButton.click();
			System.out.println("Successfully clicked on Profile button.");
		} catch (Exception e) {
			System.err.println("Error clicking on Profile button: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 3)
	public void clickOnUpdateButton() {
		try {
			System.out.println("Clicking on the SVG button and then the Change button...");
			WebElement svgButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-1bph2s4-MuiSvgIcon-root'])[1]")));
			svgButton.click();
			System.out.println("Successfully clicked on the SVG button.");
			WebElement changeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[normalize-space()='Update'])[1]")));
			changeButton.click();
			System.out.println("Successfully clicked on the Change button.");
		} catch (Exception e) {
			System.err.println("Error clicking on buttons: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 4)
	public void editProfileDetails() {
		try {
			System.out.println("Editing profile details...");

			// Open the dropdown
			WebElement genderDropdown = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'css-19bb58m')]")));
			genderDropdown.click();
			Thread.sleep(500); // Allow options to load

			WebElement selectedOption = driver
					.findElement(By.xpath("//div[contains(@class, 'css-1o7s74n-singleValue')]"));
			String selectedText = selectedOption.getText();

			if (!selectedText.equals("Female")) {
				WebElement femaleOption = driver.findElement(By.xpath("//div[contains(text(), 'Female')]"));
				femaleOption.click();
			} else {
				System.out.println("Female option is already selected, skipping click.");
			}

			// Wait for the form to appear
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[contains(@class, 'MuiBox-root') and .//span[text()='Your Details']]")));

			// Update Flat Number
			updateInputField("//input[@name='flatNumber']", "456B");

			// Update Address
			updateInputField("//input[@name='address']", "Updated Address, Near Central Park");

			// Click on the first dropdown icon and select a random city
			selectRandomCityFromDropdown("(//div[contains(@class, 'rcsd-input')]//i[contains(@class, 'icon')])[1]");

			// Update Building Door Number
			updateInputField("//input[@name='buildingDoorNumber']", "789Z");

			// Update Street Name
			updateInputField("//input[@name='streetName']", "Main Street");

			// Click on the second dropdown icon and select a random city
			selectRandomCityFromDropdown("(//div[contains(@class, 'rcsd-input')]//i[contains(@class, 'icon')])[2]");

			// Update Pincode
			updateInputField("//input[@name='pincode']", "403001");

			// Click the "Update" button
			clickUpdateButton();

			// Sleep for 1000ms before checking for the Cancel button
			Thread.sleep(1000);

			// Check if the Cancel button is present
			List<WebElement> cancelButtons = driver.findElements(By.xpath("//button[.//span[text()='Cancel']]"));
			if (!cancelButtons.isEmpty()) {
				System.out.println("Cancel button found, clicking it...");
				cancelButtons.get(0).click();
			} else {
				System.out.println("Cancel button not found, continuing...");
			}

			System.out.println("Successfully edited profile details.");
		} catch (Exception e) {
			System.err.println("Error editing profile details: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 5)
	public void changePasswordWindowTest() {
		try {
			System.out.println("Navigating to Change Password window...");

			Thread.sleep(2000);
			// Step 1: Click the Profile button
			WebElement profileButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-1bph2s4-MuiSvgIcon-root'])[1]")));
			profileButton.click();
			System.out.println("Clicked on Profile button.");

			// Step 2: Click the Change button
			WebElement changeButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Change']]")));
			changeButton.click();
			System.out.println("Clicked on Change button.");

			// Step 3: Verify that the Change Password window appears
			WebElement changePasswordTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//h2[@id='change-password-title' and text()='Change Password']")));

			if (changePasswordTitle.isDisplayed()) {
				System.out.println("Change Password window appeared successfully.");
			} else {
				System.out.println("Failed to display Change Password window.");
			}

		} catch (Exception e) {
			System.err.println("Error in Change Password window test: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 6)
	public void changePasswordFlow() {
		try {
			System.out.println("Starting Change Password Flow...");

			// Step 1: Enter the old password
			WebElement oldPasswordField = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//input[@placeholder='Enter Enter old password']")));
			oldPasswordField.sendKeys("Rich@1234");
			System.out.println("Entered old password.");

			// Step 2: Click the eye icon to toggle password visibility
			WebElement eyeIcon = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[name()='path'])[31]")));
			eyeIcon.click();
			System.out.println("Clicked on Eye Icon.");

			// Step 3: Wait for 500ms
			Thread.sleep(500);

			// Step 4: Click the "Continue" button
			WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Continue']]")));
			continueButton.click();
			System.out.println("Clicked on Continue button.");

			Thread.sleep(1000);

			// Step 6: Click the "Forgot Password?" button
			WebElement forgotPasswordButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and text()='Forgot Password?']")));
			forgotPasswordButton.click();
			System.out.println("Clicked on Forgot Password button.");

			Thread.sleep(1000);

			// Step 8: Click the "Verify" button
			WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Verify']]")));
			verifyButton.click();
			System.out.println("Clicked on Verify button.");

			Thread.sleep(500);

			// Step 10: Click the Close button
			WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-1142vi7-MuiSvgIcon-root'])[1]")));
			closeButton.click();
			System.out.println("Clicked on Close button.");

			System.out.println("Change Password Flow executed successfully.");

		} catch (Exception e) {
			System.err.println("Error in Change Password Flow: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 7)
	public void changePhoneNumberFlow() {
		try {
			System.out.println("Starting Change Phone Number Flow...");

			// Step 1: Click the second "Change" button
			WebElement changeButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Change']])[2]")));
			changeButton.click();
			System.out.println("Clicked on the second Change button.");

			Thread.sleep(1000);

			// Step 3: Click the "Verify" button
			WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Verify']]")));
			verifyButton.click();
			System.out.println("Clicked on Verify button.");

			Thread.sleep(500);

			// Step 5: Click the Close button
			WebElement closeButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[name()='path'])[30]")));
			closeButton.click();
			System.out.println("Clicked on Close button.");

			System.out.println("Change Phone Number Flow executed successfully.");

		} catch (Exception e) {
			System.err.println("Error in Change Phone Number Flow: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void updateInputField(String xpath, String value) {
		try {
			WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			inputField.sendKeys(Keys.CONTROL + "a");
			inputField.sendKeys(Keys.DELETE);
			inputField.sendKeys(value);
			Thread.sleep(1000); // Allow value to reflect
		} catch (StaleElementReferenceException e) {
			System.out.println("Stale element detected while updating input. Trying again...");
			updateInputField(xpath, value); // Retry once
		} catch (Exception e) {
			System.out.println("Error updating field: " + e.getMessage());
		}
	}

	private void selectRandomCityFromDropdown(String dropdownXPath) {
		try {
			System.out.println("Opening the city dropdown...");

			// Click to open the dropdown using the passed XPath
			WebElement dropdownIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownXPath)));
			dropdownIcon.click();

			Thread.sleep(2000); // Wait for options to appear

			// Locate all city options
			List<WebElement> cityOptions = wait.until(ExpectedConditions
					.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='items ']//div[contains(@class, 'item')]")));

			if (!cityOptions.isEmpty()) {
				Random random = new Random();
				int randomIndex = random.nextInt(cityOptions.size());

				try {
					// Select a random city
					WebElement randomCity = cityOptions.get(randomIndex);
					wait.until(ExpectedConditions.elementToBeClickable(randomCity)).click();
					System.out.println("Selected City: " + randomCity.getText());

				} catch (StaleElementReferenceException e) {
					System.out.println("Stale element detected. Skipping city selection.");
				}
			} else {
				System.out.println("No city options found!");
			}

		} catch (Exception e) {
			System.err.println("Error selecting random city: " + e.getMessage());
		}
	}

	private void clickUpdateButton() {
		try {
			System.out.println("Clicking on the Update button...");

			WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(By
					.xpath("//button[@type='submit' and contains(@class, 'MuiButton-root')]//span[text()='Update']")));

			updateButton.click();
			System.out.println("Successfully clicked on the Update button.");

		} catch (Exception e) {
			System.err.println("Error clicking on the Update button: " + e.getMessage());
		}
	}

	@AfterClass
	public void logout() {

		try {
			System.out.println("Starting Logout Process...");

			// Step 1: Click on the logout icon
			WebElement logoutIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-colorInherit MuiIconButton-sizeMedium css-zylse7-MuiButtonBase-root-MuiIconButton-root']")));
			logoutIcon.click();
			System.out.println("Clicked on Logout Icon.");

			// Step 2: Click on the logout button
			WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-i4bv87-MuiSvgIcon-root'])[12]")));
			logoutButton.click();
			System.out.println("Clicked on Logout Button.");

			System.out.println("Logout successful.");

			driver.quit();

		} catch (Exception e) {
			System.err.println("Error during Logout: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
