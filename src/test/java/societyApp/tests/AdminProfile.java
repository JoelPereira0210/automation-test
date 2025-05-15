package societyApp.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import societyAppUtils.AdminLoginHelper; // ✅ Import centralized admin login

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class AdminProfile {
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeClass
	public void setUp() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");

		// Login process
		// Wait for the element to be visible and clickable
//		WebElement tabElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tab:r0:1")));
//		tabElement.click();
//		driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("7498263271");
//		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Rich@123");
//		driver.findElement(By.cssSelector("button[type='submit']")).click();
//		Thread.sleep(4000);
//		driver.navigate().refresh();
//		Thread.sleep(2000);

		// ✅ Use shared admin login
		AdminLoginHelper.login(driver);
	}

	@Test(priority = 1)
	public void clickOnProfileButton() {
		try {
			System.out.println("Clicking on Profile button...");

			WebElement profileButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//div[@role='button' and contains(@class, 'MuiButtonBase-root') and .//span[text()='Profile']]")));
			profileButton.click();

			System.out.println("Successfully clicked on Profile button.");
		} catch (Exception e) {
			System.err.println("Error clicking on Profile button: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 2)
	public void clickOnUpdateButton() {
		try {
			System.out.println("Clicking on the SVG button and then the Change button...");

			WebElement svgButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-1bph2s4-MuiSvgIcon-root'])[1]")));
			svgButton.click();
			System.out.println("Successfully clicked on the SVG button.");

			WebElement changeButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("(//span[normalize-space()='Update'])[1]")));
			changeButton.click();
			System.out.println("Successfully clicked on the Change button.");

		} catch (Exception e) {
			System.err.println("Error clicking on buttons: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 3)
	public void editProfileDetails() {
		try {
			System.out.println("Editing profile details...");

			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[contains(@class, 'MuiBox-root') and .//span[text()='Your Details']]")));

			updateInputField("//input[@name='flatNumber']", "456B");
			updateInputField("//input[@name='address']", "Updated Address, Near Central Park");
			updateInputField("//input[@name='buildingDoorNumber']", "789Z");
			updateInputField("//input[@name='streetName']", "Main Street");

			selectRandomCityFromDropdown();
			updateInputField("//input[@name='pincode']", "403001");

			clickUpdateButton();

			System.out.println("Successfully edited profile details.");
		} catch (Exception e) {
			System.err.println("Error editing profile details: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 4)
	public void changePasswordWindowTest() {
		try {
			System.out.println("Navigating to Change Password window...");

			WebElement profileButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-1bph2s4-MuiSvgIcon-root'])[1]")));
			profileButton.click();

			WebElement changeButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Change']]")));
			changeButton.click();

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

	@Test(priority = 5)
	public void changePasswordFlow() {
		try {
			System.out.println("Starting Change Password Flow...");

			WebElement oldPasswordField = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//input[@placeholder='Enter Enter old password']")));
			oldPasswordField.sendKeys("Rich@1234");

			WebElement eyeIcon = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[name()='path'])[31]")));
			eyeIcon.click();

			Thread.sleep(500);

			WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Continue']]")));
			continueButton.click();

			Thread.sleep(1000);

			WebElement forgotPasswordButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and text()='Forgot Password?']")));
			forgotPasswordButton.click();

			Thread.sleep(1000);

			WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Verify']]")));
			verifyButton.click();

			Thread.sleep(500);

			WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-1142vi7-MuiSvgIcon-root'])[1]")));
			closeButton.click();

			System.out.println("Change Password Flow executed successfully.");

		} catch (Exception e) {
			System.err.println("Error in Change Password Flow: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 6)
	public void changePhoneNumberFlow() {
		try {
			System.out.println("Starting Change Phone Number Flow...");

			WebElement changeButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Change']])[2]")));
			changeButton.click();

			Thread.sleep(1000);

			WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Verify']]")));
			verifyButton.click();

			Thread.sleep(500);

			WebElement closeButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[name()='path'])[30]")));
			closeButton.click();

			System.out.println("Change Phone Number Flow executed successfully.");

		} catch (Exception e) {
			System.err.println("Error in Change Phone Number Flow: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 7)
	public void openSocietyProfile() {
		try {
			System.out.println("Starting Society Profile Navigation Test...");

			WebElement societyProfile = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-1bph2s4-MuiSvgIcon-root'])[2]")));
			societyProfile.click();

			System.out.println("Society Profile Navigation executed successfully.");

		} catch (Exception e) {
			System.err.println("Error in Society Profile Navigation: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 8)
	public void verifyButtonsClickable() {
		try {
			WebElement darkModeButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Dark']]")));
			darkModeButton.click();

			Thread.sleep(500);

			WebElement lightModeButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Light']]")));
			lightModeButton.click();

			WebElement upgradeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//button[contains(@class, 'MuiButtonBase-root') and .//span[text()='Upgrade your plan']]")));
			upgradeButton.click();

			System.out.println("Button Clickability Test executed successfully.");

		} catch (Exception e) {
			System.err.println("Error in Button Clickability Test: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void updateInputField(String xpath, String value) {
		try {
			WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			inputField.sendKeys(Keys.CONTROL + "a");
			inputField.sendKeys(Keys.DELETE);
			inputField.sendKeys(value);
			Thread.sleep(1000);
		} catch (StaleElementReferenceException e) {
			System.out.println("Stale element detected while updating input. Trying again...");
			updateInputField(xpath, value);
		} catch (Exception e) {
			System.out.println("Error updating field: " + e.getMessage());
		}
	}

	private void selectRandomCityFromDropdown() {
		try {
			WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//div[contains(@class, 'rcsd-input')]//i[contains(@class, 'icon')])[2]")));
			dropdown.click();
			Thread.sleep(2000);

			List<WebElement> cityOptions = wait.until(ExpectedConditions
					.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='items ']//div[contains(@class, 'item')]")));

			if (!cityOptions.isEmpty()) {
				Random random = new Random();
				WebElement city = cityOptions.get(random.nextInt(cityOptions.size()));
				wait.until(ExpectedConditions.elementToBeClickable(city)).click();
				System.out.println("Selected City: " + city.getText());
			} else {
				System.out.println("No city options found!");
			}
		} catch (Exception e) {
			System.err.println("Error selecting random city: " + e.getMessage());
		}
	}

	private void clickUpdateButton() {
		try {
			WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(By
					.xpath("//button[@type='submit' and contains(@class, 'MuiButton-root')]//span[text()='Update']")));
			updateButton.click();
		} catch (Exception e) {
			System.err.println("Error clicking on the Update button: " + e.getMessage());
		}
	}

	@AfterClass
	public void logout() {
		try {
			WebElement logoutIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//button[@type='button']//div[@class='MuiAvatar-root MuiAvatar-circular MuiAvatar-colorDefault css-jlrzoc-MuiAvatar-root']")));
			logoutIcon.click();

			WebElement logoutButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@role='button'])[15]")));
			logoutButton.click();

			System.out.println("Logout successful.");
			driver.quit();

		} catch (Exception e) {
			System.err.println("Error during Logout: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
