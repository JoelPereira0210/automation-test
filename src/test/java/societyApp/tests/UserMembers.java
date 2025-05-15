package societyApp.tests;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.*;

import societyAppUtils.UserLoginHelper;

public class UserMembers {
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
	public void navigateToMembers() {
		try {
			WebElement membersButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[contains(@class, 'MuiListItem-root') and .//span[text()='Members']]")));
			membersButton.click();
			System.out.println("Navigated to Members Section.");
			Thread.sleep(2000);
		} catch (Exception e) {
			System.out.println("Error: Could not navigate to Members.");
			e.printStackTrace();
		}
	}

	@Test(priority = 2)
	public void sortMembersList() {
		try {
			String sortButtonXpath = "//div[@class='filter-table-sort-desktop-view MuiBox-root css-7kle9u']//*[name()='svg'][contains(@class, 'MuiSvgIcon-root')]";
			WebElement sortButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(sortButtonXpath)));
			List<String> sortActions = Arrays.asList("A-Z", "Z-A", "Clear");

			for (String action : sortActions) {
				sortButton.click();
				System.out.println("Clicked on Sort button.");
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//ul[contains(@class, 'MuiList-root')]")));
				System.out.println("Dropdown appeared.");
				clickSortOption(action);
				Thread.sleep(2000);
				waitForSortingToComplete();
			}
		} catch (Exception e) {
			System.out.println("Error: Could not sort Members list.");
			e.printStackTrace();
		}
	}

	@Test(priority = 3)
	public void clickSortIcon() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			String primarySortIconXpath = "//div[@class='MuiBox-root css-7kle9u']//*[name()='svg']";
			String fallbackSortIconXpath = "(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-fzzu4u-MuiSvgIcon-root'])[1]";
			String sortingContainerXpath = "//div[contains(@class, 'MuiPaper-root MuiPaper-elevation3')]";
			String sortingOptionsXpath = sortingContainerXpath + "//div[@role='button']";

			WebElement sortIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(primarySortIconXpath)));
			sortIcon.click();
			Thread.sleep(500);
			sortIcon.click();

			// Keep commented code intact as per instruction
			// ...
		} catch (Exception e) {
			System.out.println("Error: Could not click on the Sort Icon.");
			e.printStackTrace();
		}
	}

	private void clickSortOption(String optionText) {
		try {
			List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
					By.xpath("//ul[contains(@class, 'MuiList-root')]//div[@role='button']")));
			for (WebElement option : options) {
				if (option.getText().equals(optionText)) {
					option.click();
					System.out.println("Clicked on option: " + optionText);
					return;
				}
			}
		} catch (Exception e) {
			System.out.println("Error clicking on option: " + optionText);
			e.printStackTrace();
		}
	}

	private void waitForSortingToComplete() {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='loading-spinner']")));
			System.out.println("Sorting completed.");
		} catch (Exception e) {
			System.out.println("Error waiting for sorting completion.");
		}
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
