package societyApp.tests;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

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

import societyAppUtils.UserLoginHelper;

public class UserEvents {
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeClass
	public void setUp() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");

		// Common user login
		UserLoginHelper.login(driver);
		driver.navigate().refresh();
		Thread.sleep(2000); // Allow page to load completely
	}

	@Test(priority = 1)
	public void navigateToEvents() {
		try {
			WebElement eventsButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[contains(@class, 'MuiListItem-root') and .//span[text()='Events']]")));
			eventsButton.click();
			System.out.println("Navigated to Events Section.");
			Thread.sleep(2000); // Allow event section to load
		} catch (Exception e) {
			System.out.println("Error: Could not navigate to Events.");
		}
	}

	@Test(priority = 2)
	public void countEventCards() {
		try {
			List<WebElement> eventCards = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.MuiCard-root")));

			int eventCount = eventCards.size();
			System.out.println("Total number of event cards: " + eventCount);
		} catch (Exception e) {
			System.out.println("Error: Could not count event cards.");
		}
	}

	@Test(priority = 3)
	public void viewEventDetails() {
		try {
			List<WebElement> eventCards = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.MuiCard-root")));

			if (eventCards.isEmpty()) {
				System.out.println("No events found.");
				return;
			}

			WebElement firstEventInfoIcon = eventCards.get(0)
					.findElement(By.cssSelector("svg[data-testid='InfoOutlinedIcon']"));
			firstEventInfoIcon.click();
			System.out.println("Clicked on event info.");

			Thread.sleep(2000);

			String eventTitle = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiTypography-text1")))
					.getText();
			String registerBy = driver.findElement(By.cssSelector("span.css-ym2ifz-MuiTypography-root")).getText();
			String date1 = driver.findElements(By.cssSelector("span.css-1rmg9v4-MuiTypography-root")).get(0).getText();
			String date2 = driver.findElements(By.cssSelector("span.css-1rmg9v4-MuiTypography-root")).get(1).getText();
			String time = driver.findElement(By.cssSelector("span.css-xsyk7g-MuiTypography-root")).getText();
			String description = driver.findElement(By.cssSelector("span.css-v1vmqx-MuiTypography-root")).getText();

			System.out.println("Event Title: " + eventTitle);
			System.out.println("Register By: " + registerBy);
			System.out.println("Date: " + date1 + " - " + date2);
			System.out.println("Time: " + time);
			System.out.println("Description: " + description);

			Thread.sleep(2000);

			WebElement backButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.cssSelector("svg[data-testid='ArrowBackIcon']")));
			backButton.click();
			System.out.println("Returned to event list.");
		} catch (Exception e) {
			System.out.println("Error: Could not view event details.");
		}
	}

	@Test(priority = 4)
	public void findAndClickRegisterText() {
		try {
			List<WebElement> eventCards = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.MuiCard-root")));

			System.out.println("Total event cards found: " + eventCards.size());

			for (WebElement card : eventCards) {
				try {
					WebElement eventNameElement = wait.until(
							ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.MuiTypography-text2")));
					String eventName = eventNameElement.getText().trim();

					WebElement infoButton = card.findElement(By.cssSelector("button.MuiIconButton-root"));

					WebElement statusElement = infoButton.findElement(
							By.xpath("./preceding-sibling::span[contains(@class, 'MuiTypography-text6')]"));

					String statusText = statusElement.getText().trim();
					System.out.println("Event: " + eventName + " | Status: " + statusText);

					if (statusText.equalsIgnoreCase("Register")) {
						wait.until(ExpectedConditions.elementToBeClickable(statusElement)).click();
						System.out.println("Clicked on 'Register' for event: " + eventName);

						Thread.sleep(1500);

						try {
							WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
									"(//button[@class='MuiButtonBase-root MuiButton-root MuiButton-text MuiButton-textPrimary MuiButton-sizeMedium MuiButton-textSizeMedium MuiButton-colorPrimary MuiButton-root MuiButton-text MuiButton-textPrimary MuiButton-sizeMedium MuiButton-textSizeMedium MuiButton-colorPrimary css-1tntcjk-MuiButtonBase-root-MuiButton-root'])[1]")));
							cancelButton.click();
							System.out.println("Clicked on cancel button for event: " + eventName);
							Thread.sleep(1000);
						} catch (Exception e) {
							System.out.println("Cancel button click failed or not found. Proceeding to next button.");
						}

						try {
							WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
									"//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeMedium css-t2u3ip-MuiButtonBase-root-MuiIconButton-root']")));
							nextButton.click();
							System.out.println("Clicked on next button after cancel.");
						} catch (Exception e) {
							System.out.println("Next button click failed. Trying JavaScript click as a fallback.");
							WebElement nextButton = driver.findElement(By.xpath(
									"//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeMedium css-t2u3ip-MuiButtonBase-root-MuiIconButton-root']"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextButton);
						}

						return;
					}

				} catch (NoSuchElementException ne) {
					System.out.println("Skipping card due to missing elements: " + ne.getMessage());
				}
			}

			System.out.println("No available events to register.");

		} catch (Exception e) {
			System.out.println("Error: Could not process event cards. " + e.getMessage());
		}

		try {
			WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(
					"button.MuiButtonBase-root.MuiIconButton-root.MuiIconButton-sizeMedium.css-t2u3ip-MuiButtonBase-root-MuiIconButton-root")));
			closeButton.click();
			System.out.println("Clicked on the Close button.");
		} catch (Exception e) {
			System.out.println("Error: Could not locate or click the Close button.");
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
