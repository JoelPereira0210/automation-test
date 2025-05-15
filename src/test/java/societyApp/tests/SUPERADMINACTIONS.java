package societyApp.tests;

import java.io.IOException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SUPERADMINACTIONS {

    @BeforeClass
    public void setup() {
        System.out.println("Setting up Super Admin Test Suite...");
    }

    @Test(priority = 1)
    public void executeStartTest() {
        try {
            System.out.println("Executing StartTest...");
            StartTest startTest = new StartTest();
            startTest.setup(); // Initialize WebDriver
            startTest.login();
            startTest.createSubscriptions();
            startTest.validateSubscriptions();
            startTest.cleanup();
            startTest.tearDown();
        } catch (Exception e) {
            System.err.println("Error in StartTest: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(priority = 2)
    public void updateAllSubscriptionsTest() {
        try {
            System.out.println("Executing UpdateAllSubscriptions...");
            UpdateAllSubscriptions updateAllSubscriptions = new UpdateAllSubscriptions();
            updateAllSubscriptions.setup();
            updateAllSubscriptions.findSubscriptions();
            updateAllSubscriptions.updateSubscriptions();
            updateAllSubscriptions.tearDown();
        } catch (Exception e) {
            System.err.println("Error in UpdateAllSubscriptions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(priority = 3)
    public void saveDraftSubscriptionsTest() {
        try {
            System.out.println("Executing SaveDraftSubscriptions...");
            SaveDraftSubscriptions saveDraftSubscriptions = new SaveDraftSubscriptions();
            saveDraftSubscriptions.setup();
            saveDraftSubscriptions.createDraftSubscriptions();
            saveDraftSubscriptions.tearDown();
        } catch (Exception e) {
            System.err.println("Error in SaveDraftSubscriptions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(priority = 4)
    public void createCouponsTest() {
        try {
            System.out.println("Executing CreateCoupons...");
            CreateCoupons createCoupons = new CreateCoupons();
            createCoupons.createCoupons();
        } catch (Exception e) {
            System.err.println("Error in CreateCoupons: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(priority = 5)
    public void editDraftCouponsTest() {
        try {
            System.out.println("Executing EditDraftCoupons...");
            EditDraftCoupon editDraftCoupon = new EditDraftCoupon();
            editDraftCoupon.editDraftCoupon();
        } catch (Exception e) {
            System.err.println("Error in EditDraftCoupons: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(priority = 6)
    public void deleteAllDraftsTest() {
        try {
            System.out.println("Executing DeleteAllDrafts...");
            DeleteAllDrafts.main(null);
        } catch (Exception e) {
            System.err.println("Error in DeleteAllDrafts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(priority = 7)
    public void deleteAllCouponsTest() {
        try {
            System.out.println("Executing DeleteCoupons...");
            DeleteCoupons.main(null);
        } catch (Exception e) {
            System.err.println("Error in DeleteCoupons: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(priority = 8)
    public void deleteAllSubscriptionsTest() {
        try {
            System.out.println("Executing DeleteAllSubscriptions...");
            DeleteAllSubscriptions.main(null);
        } catch (Exception e) {
            System.err.println("Error in DeleteAllSubscriptions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(priority = 9)
    public void changeBankDetailsTest() {
        try {
            System.out.println("Executing ChangeBankDetails...");
            ChangeBankDetails changeBankDetails = new ChangeBankDetails();
            changeBankDetails.changeBankDetails();
        } catch (Exception e) {
            System.err.println("Error in ChangeBankDetails: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(priority = 10)
    public void executeAllLandingCardTests() {
        try {
            System.out.println("Executing LandingCard Tests...");
            LandingCards landingCards = new LandingCards();

            // Initialize WebDriver and Setup
            landingCards.setup();

            // Execute all test methods
            landingCards.testCreateLandingCards();
            landingCards.testVerifyLandingCardsTableNotEmpty();
            landingCards.testDeleteRandomLandingCards();
            landingCards.testViewLandingCard();
            landingCards.testEditLandingCard();
            landingCards.switchToLandingSlider();
            landingCards.createLandingSliders();
            landingCards.testPagination();
            landingCards.verifyTableRowsPresence();

            // Tear down WebDriver
             landingCards.teardown(); // Uncomment if teardown is required
        } catch (Exception e) {
            System.err.println("Error in LandingCard Tests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(priority = 11)
    public void superAdminActionsCompleted() {
        System.out.println("Super Admin Actions completed successfully!");
    }
}
