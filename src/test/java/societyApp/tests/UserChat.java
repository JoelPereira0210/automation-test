package societyApp.tests;

import java.nio.file.Paths;
import java.time.Duration;

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

public class UserChat {
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeClass
	public void setUp() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");

		// Login process using helper
		UserLoginHelper.login(driver);
		driver.navigate().refresh();
		Thread.sleep(2000); // Allow page to load completely
	}

	@Test(priority = 1)
	public void navigateToChat() {
		try {
			WebElement chatButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[contains(@class, 'MuiListItem-root') and .//span[text()='Chat']]")));
			chatButton.click();
			System.out.println("Navigated to Chat Section.");
			Thread.sleep(2000); // Allow chat section to load
		} catch (Exception e) {
			System.out.println("Error: Could not navigate to Chat.");
		}
	}

	@Test(priority = 2)
	public void sendMessageInChat() {
		try {
			WebElement chatTextArea = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Send Your Message...']")));
			chatTextArea.sendKeys("Hello, this is an automated test message.");
			System.out.println("Message entered in chat.");

			WebElement sendButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@type='submit'])[1]")));
			sendButton.click();
			System.out.println("Message sent by clicking the Send button.");

			Thread.sleep(2000);
		} catch (Exception e) {
			System.out.println("Error: Could not send a message in Chat.");
		}
	}

	@Test(priority = 3)
	public void uploadFile() {
		try {
			WebElement attachmentButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='/images/attachmentIcon.png']")));
			attachmentButton.click();
			System.out.println("Clicked attachment button.");

			Thread.sleep(2000);

			WebElement fileInput = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));

//			String filePath = Paths.get(
//					"C:\\Users\\hdsof\\Downloads\\74936-Software Development and Services Projects - CMMI Development, Services - Maturity Level - 3.png")
//					.toString();
//			fileInput.sendKeys(filePath);
//			System.out.println("File uploaded successfully.");
//
//			Thread.sleep(2000);

//			WebElement uploadButton = wait
//					.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Upload')]")));
//			uploadButton.click();
//			System.out.println("Clicked Upload button.");

		} catch (Exception e) {
			System.out.println("Error: Could not upload file.");
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
