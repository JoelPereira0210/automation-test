package societyApp.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import societyAppUtils.AdminLoginHelper;

import java.time.Duration;
import java.util.List;

public class AdminAllMembersView {
	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeClass
	public void setup() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		js = (JavascriptExecutor) driver;
		driver.manage().window().maximize();

		// ✅ Use centralized login
		AdminLoginHelper.login(driver);
	}

	@Test(priority = 1)
	public void viewAllMembers() throws InterruptedException {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table/tbody")));
		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
		System.out.println("Total Members in the Table: " + rows.size());

		for (int i = 0; i < rows.size(); i++) {
			WebElement row = driver.findElements(By.xpath("//table/tbody/tr")).get(i);
			List<WebElement> columns = row.findElements(By.tagName("td"));
			if (!columns.isEmpty()) {
				printMemberDetails(i, columns);
				viewMemberDetails(row);
			}
		}
	}

	private void printMemberDetails(int index, List<WebElement> columns) {
		System.out.println("Member " + (index + 1) + ":");
		System.out.println("Name: " + columns.get(0).getText().trim());
		System.out.println("Phone: " + columns.get(1).getText().trim());
		System.out.println("Role: " + columns.get(2).getText().trim());
		System.out.println("Status: " + columns.get(3).getText().trim());
		System.out.println("----------------------");
	}

	private void viewMemberDetails(WebElement row) throws InterruptedException {
		try {
			WebElement eyeIcon = wait.until(ExpectedConditions.elementToBeClickable(
					row.findElement(By.xpath(".//button[contains(@class, 'MuiButtonBase-root')]"))));
			js.executeScript("arguments[0].scrollIntoView(true);", eyeIcon);
			Thread.sleep(200);
			eyeIcon.click();
			Thread.sleep(500);
			clickTab("Registered Event");
			clickTab("Maintenance Fees");
			closeModal();
		} catch (Exception e) {
			System.out.println("Error viewing member details: " + e.getMessage());
		}
	}

	private void clickTab(String tabName) throws InterruptedException {
		WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//button[contains(@class, 'tab') and span[text()='" + tabName + "']]")));
		tab.click();
		Thread.sleep(500);
	}

	private void closeModal() throws InterruptedException {
		WebElement closeButton = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//*[name()='path' and contains(@d,'M20 11H7.8')]")));
		Thread.sleep(200);
		closeButton.click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".MuiDialog-root")));
		Thread.sleep(200);
	}

	@Test(priority = 2, dependsOnMethods = "viewAllMembers")
	public void editMemberRole() throws InterruptedException {
		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
		for (WebElement row : rows) {
			try {
				WebElement editButton = row.findElement(By.cssSelector("svg[stroke='currentColor']"));
				js.executeScript("arguments[0].scrollIntoView(true);", editButton);
				Thread.sleep(500);
				editButton.click();
				Thread.sleep(500);
				selectRoleAndUpdate("chairman", "Yes");
				return;
			} catch (NoSuchElementException ignored) {
			}
		}
		System.out.println("No edit button found.");
	}

	private void selectRoleAndUpdate(String role, String level) throws InterruptedException {
		selectDropdownOption("//div[contains(@class, 'MuiSelect-select')]", role);
		selectDropdownOption("(//div[@role='combobox'])[2]", level);
		clickUpdate();
	}

	private void selectDropdownOption(String xpath, String value) throws InterruptedException {
		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		dropdown.click();
		Thread.sleep(200);
		WebElement option = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[normalize-space()='" + value + "']")));
		option.click();
		Thread.sleep(100);
	}

	private void clickUpdate() throws InterruptedException {
		WebElement updateButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Update']")));
		updateButton.click();
		Thread.sleep(500);
		System.out.println("First member's role changed to Chairman.");
	}

	@AfterClass
	public void teardown() {
		driver.quit();
	}
}
