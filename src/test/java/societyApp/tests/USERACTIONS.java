package societyApp.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class USERACTIONS {
	private UserProfile userProfile;
	private UserMembers userMembers;
	private UserCommittee userCommittee;
	private UserEvents userEvents;
	private UserCharges userCharges;
	private UserChat userChat;

	@BeforeClass
	public void setup() throws InterruptedException {
		userProfile = new UserProfile();
		userMembers = new UserMembers();
		userCommittee = new UserCommittee();
		userEvents = new UserEvents();
		userCharges = new UserCharges();
		userChat = new UserChat();
	}

	@Test(priority = 1)
	public void executeUserProfileTests() throws InterruptedException {
		userProfile.setup();
		userProfile.clickOnProfileButton();
		userProfile.clickOnUpdateButton();
		userProfile.editProfileDetails();
		userProfile.changePasswordWindowTest();
		userProfile.changePasswordFlow();
		userProfile.changePhoneNumberFlow();
		userProfile.logout();
	}

	@Test(priority = 2)
	public void executeUserMembersTests() throws InterruptedException {
		userMembers.setUp();
		userMembers.navigateToMembers();
		userMembers.sortMembersList();
		userMembers.clickSortIcon();
		userMembers.tearDown();
	}

	@Test(priority = 3)
	public void executeUserCommitteeTests() throws InterruptedException {
		userCommittee.setUp();
		userCommittee.navigateToMembers();
		userCommittee.searchAndClear();
		userCommittee.sortMembersList();
		userCommittee.clickAllListItems();
		userCommittee.tearDown();
	}

	@Test(priority = 4)
	public void executeUserEventsTests() throws InterruptedException {
		userEvents.setUp();
		userEvents.navigateToEvents();
		userEvents.countEventCards();
		userEvents.viewEventDetails();
		userEvents.findAndClickRegisterText();
		userEvents.tearDown();
	}

	@Test(priority = 5)
	public void executeUserChargesTests() throws InterruptedException {
		userCharges.setUp();
		userCharges.navigateToMembers();
		userCharges.handlePaymentProcess();
		userCharges.tearDown();
	}

	@Test(priority = 6)
	public void executeUserChatTests() throws InterruptedException {
		userChat.setUp();
		userChat.navigateToChat();
		userChat.sendMessageInChat();
		userChat.uploadFile();
		userChat.tearDown();
	}
}
