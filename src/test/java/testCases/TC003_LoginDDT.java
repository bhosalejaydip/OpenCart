package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

public class TC003_LoginDDT extends BaseClass {

@Test(dataProvider="LoginData", dataProviderClass=DataProviders.class,groups="Datadriven")

public void verify_loginDDT(String email,String pwd,String exp)
{

logger.info("****Starting TC003_LoginDDT****");

try
{
HomePage hp = new HomePage(driver);
hp.clickMyAccount();
hp.clickLogin();

LoginPage lp = new LoginPage(driver);
lp.setEmail(email);
lp.setPassword(pwd);
lp.clickLogin();

MyAccountPage macc = new MyAccountPage(driver);
boolean targetPage = macc.isMyAccountPageExists();

exp = exp.trim();   // remove extra spaces from Excel

if(exp.equalsIgnoreCase("valid"))
{
    if(targetPage==true)
    {
        macc.clickLogout();
        Assert.assertTrue(true);
    }
    else
    {
        Assert.assertTrue(false);
    }
}

else if(exp.equalsIgnoreCase("invalid"))
{
    if(targetPage==true)
    {
        macc.clickLogout();
        Assert.assertTrue(false);
    }
    else
    {
        Assert.assertTrue(true);
    }
}

}
catch(Exception e)
{
Assert.fail();
}

logger.info("***Finished TC003_LoginDDT*****");

}

}