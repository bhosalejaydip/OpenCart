package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObject.AccountRegistrationPage;
import pageObject.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass {

	@Test(groups={"Regression","Master"})
	public void verify_acount_Registration()
	{
		logger.info("**** Starting TC001_AccountRegistration ******");

		try
		{
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			logger.info("Clicked on MyAccount Link");

			hp.clickRegister();
			logger.info("Clicked on Register Link");

			AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
			logger.info("Providing Customer details");

			regpage.setFirstName(randomeString().toUpperCase());
			regpage.setLastName(randomeString().toUpperCase());
			regpage.setEmail(randomeString()+"@gmail.com");
			regpage.setTelephone(randomeNumber());

			String password = randomealphanNumberic();

			regpage.setPassword(password);
			regpage.setConfirmPassword(password);

			regpage.setPrivacyPolicy();
			regpage.clickContinue();

			logger.info("Validating expected message..");

			String confmsg = regpage.getConfirmationMsg();

			if(confmsg.equals("Your Account Has Been Created!"))
			{
				logger.info("Test passed");
				Assert.assertTrue(true);
			}
			else
			{
				logger.error("Test failed - Confirmation message not matched");
				logger.debug("Debug logs.......");
				Assert.assertTrue(false);
			}
		}
		catch(Exception e)
		{
			logger.error("Test failed: " + e.getMessage());
			Assert.fail();
		}

		logger.info("*** End the TC001_AccountRegistration***");
	}
}