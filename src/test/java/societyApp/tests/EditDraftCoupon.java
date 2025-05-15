package societyApp.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import societyAppUtils.LoginHelper;

import java.time.Duration;
import java.util.List;

import org.testng.annotations.Test;

public class EditDraftCoupon {
	@Test
	public void editDraftCoupon() throws Exception {
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		LoginHelper.login(driver);

		WebElement couponsButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Coupons']")));
		couponsButton.click();
		Thread.sleep(500);

		WebElement draftTab = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Draft']]")));
		draftTab.click();
		Thread.sleep(500);

		// Step 4: Count and print the number of draft coupons
		List<WebElement> draftCoupons = driver.findElements(By.xpath("//div[contains(@class, 'MuiCard-root')]"));
		int draftCount = draftCoupons.size();
		System.out.println("Total Draft Coupons: " + draftCount);

		while (true) {
			try {

				WebElement firstDraftCoupon = wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//div[contains(@class, 'MuiCard-root')])[1]")));

				System.out.println("Processing a Draft Coupon...");

				WebElement moreButton = wait.until(ExpectedConditions.elementToBeClickable(
						firstDraftCoupon.findElement(By.xpath(".//button[contains(@aria-label, 'more')]"))));
				moreButton.click();
				Thread.sleep(400);

				WebElement viewCouponOption = wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'View Coupon')]")));
				viewCouponOption.click();
				Thread.sleep(400);

				WebElement couponDescInput = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.name("couponDescription")));
				couponDescInput.clear();
				couponDescInput.sendKeys("Updated Description - Special Offer!");

				WebElement publishButton = wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath("//button[span[text()='Publish']]")));
				js.executeScript("arguments[0].scrollIntoView(true);", publishButton);
				Thread.sleep(100);

				wait.until(ExpectedConditions.elementToBeClickable(publishButton)).click();
				System.out.println("Draft Coupon Published Successfully!");

				Thread.sleep(500);
			} catch (TimeoutException e) {
				System.out.println("No more draft coupons found. Exiting...");
				break;
			}
		}

		System.out.println("All Draft Coupons Published Successfully!");
		driver.quit();
	}
}
