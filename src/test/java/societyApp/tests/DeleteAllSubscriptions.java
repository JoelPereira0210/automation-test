package societyApp.tests;

import societyAppUtils.LoginHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DeleteAllSubscriptions {
	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		LoginHelper.login(driver);

		List<WebElement> cards = driver.findElements(By.cssSelector(".MuiBox-root.css-sagz8e"));
		System.out.println("Total subscriptions before deletion: " + cards.size());

		while (!cards.isEmpty()) {
			try {
				WebElement editButton = wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("//button[span[contains(text(), 'Edit')]]")));
				editButton.click();
				Thread.sleep(300);

				js.executeScript("window.scrollBy(0,300);");
				Thread.sleep(300);

				WebElement deleteButton = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//button[span[contains(text(), 'Delete')]]")));
				deleteButton.click();

				try {
					driver.switchTo().alert().accept();
				} catch (NoAlertPresentException e) {

				}

				System.out.println("Deleted one subscription.");
				Thread.sleep(500);

				cards = driver.findElements(By.cssSelector(".MuiBox-root.css-sagz8e"));
			} catch (Exception e) {
				System.out.println("Error deleting subscription: " + e.getMessage());
				break;
			}
		}

		System.out.println("Total subscriptions after deletion: " + cards.size());

		driver.quit();
	}
}
