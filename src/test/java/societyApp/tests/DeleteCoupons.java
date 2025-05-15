package societyApp.tests;

import societyAppUtils.LoginHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class DeleteCoupons {
	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		LoginHelper.login(driver);

		try {
			driver.findElement(By.xpath("//span[text()='Coupons']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("css-1rgx2cw")));

			List<WebElement> coupons = driver.findElements(By.cssSelector(".MuiBox-root.css-1rgx2cw"));
			System.out.println("Total coupons found: " + coupons.size());

			while (!coupons.isEmpty()) {
				WebElement firstCoupon = coupons.get(0);

				WebElement menuButton = firstCoupon.findElement(By.cssSelector("button[aria-label='more']"));
				js.executeScript("arguments[0].scrollIntoView(true);", menuButton);
				Thread.sleep(100);
				menuButton.click();
				Thread.sleep(100);

				WebElement viewCoupon = wait
						.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[text()='View Coupon']")));
				viewCoupon.click();
				Thread.sleep(100);

				js.executeScript("window.scrollBy(0, 700);");
				Thread.sleep(100);

				WebElement deleteButton = wait
						.until(ExpectedConditions.elementToBeClickable(By.xpath("//button/span[text()='Delete']")));
				js.executeScript("arguments[0].scrollIntoView(true);", deleteButton);
				Thread.sleep(100);
				deleteButton.click();
				Thread.sleep(500);

				coupons = driver.findElements(By.cssSelector(".MuiBox-root.css-1rgx2cw"));
			}

			System.out.println("All coupons deleted successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();
		}
	}
}
