package societyApp.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import societyAppUtils.LoginHelper;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

@Test
public class ChangeBankDetails {

	public void changeBankDetails() throws Exception {
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		LoginHelper.login(driver);

		// WebElement accountNumber =
		// driver.findElement(By.xpath("(//input[@placeholder='Bank Account
		// Number'])[1]"));

		WebElement bankDetailsButton = driver.findElement(By.xpath(
				"//div[contains(@class, 'MuiButtonBase-root') and contains(@class, 'MuiListItem-root')][.//span[text()='Bank Details']]"));
		bankDetailsButton.click();

		Thread.sleep(500);
		WebElement changeButton = driver.findElement(By.xpath("//button[span[contains(text(), 'Change')]]"));
		changeButton.click();
		Thread.sleep(100);

		String oldAccountNumber = driver.findElement(By.name("bankAccountNumber")).getDomProperty("value");
		String oldBankName = driver.findElement(By.name("bank")).getDomProperty("value");
		String oldIfsc = driver.findElement(By.name("ifscCode")).getDomProperty("value");

		System.out.println("Old Account Number: " + oldAccountNumber);
		System.out.println("Old Bank Name: " + oldBankName);
		System.out.println("Old IFSC Code: " + oldIfsc);

		WebElement accNumField = driver.findElement(By.name("bankAccountNumber"));
		accNumField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
		accNumField.sendKeys("105000659535011");

		WebElement accNameField = driver.findElement(By.name("accountName"));
		accNameField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
		accNameField.sendKeys("Richa Rajan Kerkar");

		WebElement bankField = driver.findElement(By.name("bank"));
		bankField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
		bankField.sendKeys("HDFC Bank");

		WebElement branchField = driver.findElement(By.name("branchName"));
		branchField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
		branchField.sendKeys("Bicholim Goa");

		WebElement ifscField = driver.findElement(By.name("ifscCode"));
		ifscField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
		ifscField.sendKeys("HDFC0014");

		System.out.println("Before clicking update button");
		Thread.sleep(400);

		WebElement updateButton = driver.findElement(By.xpath("//button[.//span[text()='Update']]"));
		updateButton.click();

		System.out.println("After clicking update button");

		Thread.sleep(1000);

		String newAccountNumber = driver.findElement(By.name("bankAccountNumber")).getDomProperty("value");
		String newBankName = driver.findElement(By.name("bank")).getDomProperty("value");
		String newIfsc = driver.findElement(By.name("ifscCode")).getDomProperty("value");

		System.out.println("New Account Number: " + newAccountNumber);
		System.out.println("New Bank Name: " + newBankName);
		System.out.println("New IFSC Code: " + newIfsc);

		driver.quit();
	}
}
