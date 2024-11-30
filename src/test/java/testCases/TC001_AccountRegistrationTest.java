package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;

public class TC001_AccountRegistrationTest extends BaseClass {

	
	
	
@Test(groups={"Regression", "Master"})
	public void verify_account_registration() {
	logger.info("***** Starting TC001_AccountRegistrationTest  ****");
	logger.debug("This is a debug log message");
	
	try {
		HomePage hp = new HomePage(driver);
		hp.clickMyAccount();
		logger.info("Clicked on MyAccount Link.. ");
		hp.clickRegister();
		logger.info("Clicked on Register Link.. ");
		
		AccountRegistrationPage repage =  new AccountRegistrationPage(driver);
		
		logger.info("Providing customer details...");
		repage.setFirstName(randomString().toUpperCase());
		logger.info("first name.. ");
		
		repage.setLastName(randomString().toUpperCase());
		logger.info("last name.. ");
		repage.setEmail(randomString()+"@gmail.com");
		logger.info("email.. ");
		repage.setTelephone(randomNumber());
		logger.info("tele phone.. ");
		String password = randomAlphaNumeric();
		
		repage.setPassword(password);
		repage.setConfirmPassword(password);
		repage.setPrivacyPolicy();
		repage.clickContinue();
		logger.info("Validating expected message..");
		
		String confms = repage.getConfirmationMsg();
		
		if(confms.equals("Your Account Has Been Created!")) {
			Assert.assertTrue(true);
			logger.info("Test passed");
		}else {
			
			logger.error("Test failed");
			Assert.assertTrue(false);
		}

		
	}catch(Exception e){

		Assert.fail("Test failed: " + e.getMessage());
	}
	finally 
	{
	logger.info("***** Finished TC001_AccountRegistrationTest *****");
	}
		
	}


}
