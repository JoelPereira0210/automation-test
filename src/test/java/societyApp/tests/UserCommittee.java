package societyApp.tests;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import societyAppUtils.UserLoginHelper;

public class UserCommittee {
	private WebDriver driver;
	private WebDriverWait wait;
	private static final String SEARCH_TEXT = "oliver";

	@BeforeClass
	public void setUp() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");

		// Login using helper
		UserLoginHelper.login(driver);
		driver.navigate().refresh();
		Thread.sleep(2000);
	}

	@Test(priority = 1)
	public void navigateToMembers() {
		try {
			WebElement membersButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[contains(@class, 'MuiListItem-root') and .//span[text()='Committee']]")));
			membersButton.click();
			System.out.println("Navigated to Committee Section.");
			Thread.sleep(2000);
		} catch (Exception e) {
			System.out.println("Error: Could not navigate to Committee.");
			e.printStackTrace();
		}
	}

	@Test(priority = 2)
	public void searchAndClear() {
		try {
			WebElement searchField = wait
					.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[placeholder='Search']")));
			searchField.sendKeys(SEARCH_TEXT);
			System.out.println("Typed '" + SEARCH_TEXT + "' in the search field.");
			Thread.sleep(1500);
			WebElement cancelIcon = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[name()='path'])[16]")));
			cancelIcon.click();
			System.out.println("Clicked on the cancel (clear) icon.");
		} catch (Exception e) {
			System.out.println("Error: Could not complete search and clear.");
			e.printStackTrace();
		}
	}

	@Test(priority = 3)
	public void sortMembersList() {
		try {
			// Define the XPath for the Sort button
//			String sortButtonXpath = "//div[@class='filter-table-sort-desktop-view MuiBox-root css-7kle9u']//*[name()='svg'][contains(@class, 'MuiSvgIcon-root')]";
//			WebElement sortButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(sortButtonXpath)));
//			List<String> sortActions = Arrays.asList("A-Z", "Z-A", "Clear");
//			for (String action : sortActions) {
//				sortButton.click();
//				System.out.println("Clicked on Sort button.");
//				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[contains(@class, 'MuiList-root')]")));
//				System.out.println("Dropdown appeared.");
//				clickSortOption(action);
//				Thread.sleep(2000);
//				waitForSortingToComplete();
//			}
		} catch (Exception e) {
			System.out.println("Error: Could not sort Members list.");
			e.printStackTrace();
		}
	}

	@Test(priority = 4)
	public void clickAllListItems() {
		try {
			WebElement containerDiv = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.MuiBox-root.css-14r3cv3")));
			WebElement ulElement = containerDiv.findElement(By.cssSelector("ul.MuiList-root"));
			List<WebElement> listItems = ulElement.findElements(By.cssSelector("li.MuiListItem-root"));

			if (listItems.isEmpty()) {
				System.out.println("No list items found inside the div.");
				return;
			}

			System.out.println("Total list items found: " + listItems.size());
			for (WebElement listItem : listItems) {
				wait.until(ExpectedConditions.elementToBeClickable(listItem)).click();
				System.out.println("Clicked on: " + listItem.getText());
				Thread.sleep(1500);
			}
		} catch (Exception e) {
			System.out.println("Error: Could not process list items.");
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
