package societyApp.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import societyAppUtils.AdminLoginHelper; // ✅ Added helper import

import java.time.Duration;
import java.nio.file.Paths;

public class AdminChat {
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeClass
	public void setUp() throws InterruptedException {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/signup");

		// Login process
		// Wait for the element to be visible and clickable
//		WebElement tabElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tab:r0:1")));
//		tabElement.click();		
//		driver.findElement(By.cssSelector("input[placeholder='Mobile Number']")).sendKeys("7498263271");
//		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Rich@123");
//		driver.findElement(By.cssSelector("button[type='submit']")).click();
//		Thread.sleep(4000); // Wait for login to complete
//		driver.navigate().refresh();
//		Thread.sleep(2000); // Allow page to load completely

		// ✅ Centralized login call (replaces above actual login logic)
		AdminLoginHelper.login(driver);

		// Navigate to Chat Section
		navigateToChat();
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
			// Locate the chat text area
			WebElement chatTextArea = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Send Your Message...']")));

			// Enter a message in the text area
			chatTextArea.sendKeys("Hello, this is an automated test message.");
			System.out.println("Message entered in chat.");

			// Click the Send button
			WebElement sendButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@type='submit'])[1]")));
			sendButton.click();
			System.out.println("Message sent by clicking the Send button.");

			Thread.sleep(2000); // Allow message to send
		} catch (Exception e) {
			System.out.println("Error: Could not send a message in Chat.");
		}
	}

	@Test(priority = 3)
	public void uploadFile() {
		try {
			// Step 1: Click on the attachment button (image button)
			WebElement attachmentButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='/images/attachmentIcon.png']"))); // Click
																													// attachment
																													// icon
			attachmentButton.click();
			System.out.println("Clicked attachment button.");

			Thread.sleep(2000); // Wait for file upload dialog to open

			// Step 2: Locate the file input field (which becomes visible after clicking
			// attachment)
			WebElement fileInput = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));

			// Step 3: Provide the file path
			String filePath = Paths.get(
					"C:\\Users\\hdsof\\Downloads\\74936-Software Development and Services Projects - CMMI Development, Services - Maturity Level - 3.png")
					.toString();
			fileInput.sendKeys(filePath);
			System.out.println("File uploaded successfully.");

			Thread.sleep(2000); // Give some time for the upload to complete

//			// Step 4: Click the 'Upload' button (if applicable)
//			WebElement uploadButton = wait
//					.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Upload')]"))); // Adjust if needed
//			uploadButton.click();
//			System.out.println("Clicked Upload button.");

		} catch (Exception e) {
			System.out.println("Error: Could not upload file.");
			e.printStackTrace();
		}
	}

	// Close the driver after execution
	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
