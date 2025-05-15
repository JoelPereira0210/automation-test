package societyApp.tests;

import societyAppModels.CouponData;
import societyAppUtils.JsonDataReaderCoupon;
import societyAppUtils.LoginHelper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;

public class CreateCoupons {
	@Test
	public void createCoupons() throws Exception {
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		LoginHelper.login(driver);

		WebElement couponsButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Coupons']")));
		couponsButton.click();

		List<CouponData> coupons = JsonDataReaderCoupon.getCoupons();

		for (int i = 0; i < coupons.size(); i++) {
			CouponData coupon = coupons.get(i);

			if (!isValidCoupon(coupon)) {
				System.out.println("Skipping invalid coupon: " + coupon.couponName);
				continue;
			}

			WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[contains(@class, 'tabs')]/button[contains(., 'Create Coupon')]")));
			button.click();

			WebElement couponNameInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("couponName")));
			couponNameInput.sendKeys(coupon.couponName);

			WebElement couponDesc = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.name("couponDescription")));
			couponDesc.sendKeys(coupon.description);

			WebElement maxUsesInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("maxUses")));
			js.executeScript("arguments[0].scrollIntoView(true);", maxUsesInput);
			Thread.sleep(500);
			maxUsesInput.sendKeys(String.valueOf(coupon.maxUsage));

			WebElement societyNameInput = driver.findElement(By.name("societyId"));
			if (!coupon.societyId.isEmpty()) {
				societyNameInput.sendKeys(coupon.societyId);
			}

			WebElement percentageInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("percentage")));
			percentageInput.sendKeys(String.valueOf(coupon.discount));

			// Date picker: Select today's date + 10 days
			LocalDate targetDate = LocalDate.now().plusDays(10);
			String targetMonthYear = targetDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " "
					+ targetDate.getYear();
			String targetDay = String.valueOf(targetDate.getDayOfMonth());

			WebElement datePickerInput = driver.findElement(By.xpath("//input[@placeholder='MM/DD/YYYY']"));
			datePickerInput.click();
			Thread.sleep(500);

			// Navigate to the correct month
			while (true) {
				WebElement currentMonth = driver.findElement(By.cssSelector(".MuiPickersCalendarHeader-label"));
				if (currentMonth.getText().trim().equalsIgnoreCase(targetMonthYear)) {
					break;
				}
				WebElement nextButton = driver.findElement(By.cssSelector("[aria-label='Next month']"));
				if (nextButton.isEnabled()) {
					nextButton.click();
					Thread.sleep(500);
				} else {
					System.out.println("Next button is disabled. Cannot reach " + targetMonthYear);
					break;
				}
			}

			// Select the target date
			try {
				WebElement dateToSelect = driver.findElement(
						By.xpath("//button[contains(@class, 'MuiPickersDay-root') and text()='" + targetDay + "']"));
				js.executeScript("arguments[0].click();", dateToSelect);
			} catch (NoSuchElementException e) {
				System.out.println("Could not find day: " + targetDay);
			}

			try {
				WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
						By.xpath("//button[contains(@class, 'MuiButtonBase-root') and text()='OK']")));
				js.executeScript("arguments[0].click();", okButton);
			} catch (Exception e) {
				System.out.println("OK button not found or not clickable: " + e.getMessage());
			}

			// Alternate between Publish and Save as Draft
			if (i % 2 == 0) {
				WebElement publishButton = driver.findElement(By.xpath("//button[span[text()='Publish']]"));
				publishButton.click();
				System.out.println("Trying to Publish: " + coupon.couponName);
			} else {
				WebElement draftButton = driver.findElement(By.xpath("//button[span[text()='Save Draft']]"));
				draftButton.click();
				System.out.println("Trying to Save Draft: " + coupon.couponName);
			}

			// Wait for validation messages
			Thread.sleep(1000);
			List<WebElement> validationMessages = driver
					.findElements(By.xpath("//p[contains(@class, 'MuiTypography-body2')]"));

			if (!validationMessages.isEmpty()) {
				System.out.println("Validation errors found:");
				for (WebElement errorMessage : validationMessages) {
					System.out.println(errorMessage.getText());
				}

				WebElement closeButton = wait
						.until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[name()='path'])[22]")));
				closeButton.click();
				Thread.sleep(500);
				continue;
			}
		}

		System.out.println("All coupons processed successfully");
		driver.quit();
	}

	private boolean isValidCoupon(CouponData coupon) {
		if (coupon.couponName == null || coupon.couponName.trim().isEmpty()) {
			System.out.println("Coupon Name is required.");
			return false;
		}

		if (coupon.description == null || coupon.description.trim().isEmpty()) {
			System.out.println("Coupon Description is required.");
			return false;
		}

		if (!String.valueOf(coupon.maxUsage).matches("\\d+") || coupon.maxUsage < 1) {
			System.out.println("Max Uses must be a positive integer.");
			return false;
		}

		if (!String.valueOf(coupon.discount).matches("\\d+") || coupon.discount < 1 || coupon.discount > 99) {
			System.out.println("Percentage must be between 1 and 99.");
			return false;
		}

		System.out.println("Coupon data is valid: " + coupon.couponName);
		return true;
	}
}
