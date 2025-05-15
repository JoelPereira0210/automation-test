package societyApp.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdminActions {

	private AdminProfile adminProfile;
	private AdminAllMembersView adminAllMembersView;
	private AdminNewMembers adminNewMembers;
	private AdminEvents adminEvents;
	private AdminCharges adminCharges; // Added AdminCharges
	private AdminChat adminChat; // Added AdminChat
	// private AdminCommittee adminCommittee; // Uncomment if needed

	@BeforeMethod
	public void setUp() {
		adminProfile = new AdminProfile();
		adminAllMembersView = new AdminAllMembersView();
		adminNewMembers = new AdminNewMembers();
		adminEvents = new AdminEvents();
		adminCharges = new AdminCharges(); // Initialize AdminCharges
		adminChat = new AdminChat(); // Initialize AdminChat
		// adminCommittee = new AdminCommittee(); // Uncomment if needed
	}

	@AfterMethod
	public void tearDown() {
		System.out.println("Test execution completed.");
	}

	@Test(priority = 1)
	public void executeAdminProfileActions() throws InterruptedException {
		adminProfile.setUp();
		adminProfile.clickOnProfileButton();
		adminProfile.clickOnUpdateButton();
		adminProfile.editProfileDetails();
		adminProfile.changePasswordWindowTest();
		adminProfile.changePasswordFlow();
		adminProfile.changePhoneNumberFlow();
		adminProfile.openSocietyProfile();
		adminProfile.verifyButtonsClickable();
		adminProfile.logout(); // Ensure WebDriver quits

		System.out.println("Admin profile actions executed successfully.");
	}

	@Test(priority = 2)
	public void executeAdminMemberActions() throws InterruptedException {
		adminAllMembersView.setup();
		adminAllMembersView.viewAllMembers();
		adminAllMembersView.editMemberRole();
		adminAllMembersView.teardown(); // Ensure WebDriver quits

		System.out.println("Admin member actions executed successfully.");
	}

	@Test(priority = 3)
	public void executeAdminNewMembers() throws Exception {
		adminNewMembers.addNewMembers();
		System.out.println("Admin new member actions executed successfully.");
	}

	@Test(priority = 5)
	public void executeAdminEventActions() throws Exception {
		adminEvents.setUp();
		adminEvents.navigateToEventsSection();
		adminEvents.createEvent();
		adminEvents.viewFirstTwoEventDetails();
		adminEvents.navigateToEventsSection();
		adminEvents.editFirstEventCard();
		adminEvents.teardown();

		System.out.println("Admin event actions executed successfully.");
	}

	@Test(priority = 6)
	public void executeAdminChargesActions() throws Exception {
		adminCharges.setUp();
		adminCharges.testChargesSection();
		System.out.println("Admin charges actions executed successfully.");
	}

	@Test(priority = 7)
	public void executeAdminChatActions() throws Exception {
		adminChat.setUp();
		adminChat.navigateToChat();
		adminChat.sendMessageInChat();
		adminChat.uploadFile();
		adminChat.tearDown();
		System.out.println("Admin chat actions executed successfully.");
	}

	// @Test(priority = 4)
	// public void executeAdminCommitteeActions() throws Exception {
	// adminCommittee.setUp(); // Initialize WebDriver
	// adminCommittee.navigateToCommitteeSection();
	// adminCommittee.verifyAddNewDesignationButton();
	// adminCommittee.viewTableList();
	// adminCommittee.tearDown(); // Quit WebDriver
	// System.out.println("Admin committee actions executed successfully.");
	// }
}
