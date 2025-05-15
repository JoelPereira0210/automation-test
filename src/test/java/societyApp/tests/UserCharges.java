package societyApp.tests;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import societyAppUtils.UserLoginHelper;

public class UserCharges {
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeClass
	public void setUp() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");

		// Login using UserLoginHelper
		UserLoginHelper.login(driver);
		driver.navigate().refresh();
		Thread.sleep(2000); // Allow page to load completely
	}

	@Test(priority = 1)
	public void navigateToMembers() {
		try {
			WebElement membersButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[contains(@class, 'MuiListItem-root') and .//span[text()='Charges/Fees']]")));
			membersButton.click();
			System.out.println("Navigated to Members Section.");
			Thread.sleep(2000);
		} catch (Exception e) {
			System.out.println("Error: Could not navigate to Members.");
			e.printStackTrace();
		}
	}

	@Test(priority = 3)
	public void handlePaymentProcess() throws InterruptedException {
		try {
			WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));
			List<WebElement> rows = table.findElements(By.tagName("tr"));

			if (rows.size() <= 1) {
				System.out.println("No charges available. Skipping payment process.");
				return;
			}

			System.out.println("Found " + (rows.size() - 1) + " charges. Proceeding with payment.");

			WebElement firstPayButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@type='button'])[5]")));
			firstPayButton.click();
			System.out.println("Clicked on 'Pay Now' button for the first row.");

			Thread.sleep(2000);

			WebElement formButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//span[@class='MuiTypography-root MuiTypography-text8 css-1yjc4mj-MuiTypography-root'][normalize-space()='Pay Now'])[1]")));

			((JavascriptExecutor) driver)
					.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", formButton);

			wait.until(ExpectedConditions.elementToBeClickable(formButton)).click();
			System.out.println("Clicked on the button in the form.");

			Thread.sleep(2000);

			WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"(//button[@class='MuiButtonBase-root MuiButton-root MuiButton-text MuiButton-textPrimary MuiButton-sizeMedium MuiButton-textSizeMedium MuiButton-colorPrimary MuiButton-root MuiButton-text MuiButton-textPrimary MuiButton-sizeMedium MuiButton-textSizeMedium MuiButton-colorPrimary css-1egtqk4-MuiButtonBase-root-MuiButton-root'])[1]")));
			cancelButton.click();
			System.out.println("Clicked on cancel button.");

			driver.navigate().refresh();

		} catch (Exception e) {
			System.out.println("Error: Payment process failed.");
			e.printStackTrace();
		}

	}

//	@Test(priority = 4)
//	public void closeCheckoutWindow() {
//		try {
//			Thread.sleep(5000);
//
//			driver.navigate().back();
//			Thread.sleep(500);
//
//			WebElement yesExitBtn = wait.until(
//					ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-testid='confirm-positive']")));
//			yesExitBtn.click();
//			System.out.println("Clicked on 'Yes, exit' button.");
//			Thread.sleep(500);
//
//			WebElement cancelBtn = wait.until(
//					ExpectedConditions.elementToBeClickable(By.xpath("//button[.//span[contains(text(), 'Cancel')]]")));
//			cancelBtn.click();
//			System.out.println("Clicked on 'Cancel' button.");
//			Thread.sleep(500);
//
//		} catch (Exception e) {
//			System.out.println("Error: Could not complete the checkout closing sequence.");
//			e.printStackTrace();
//		}
//	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
