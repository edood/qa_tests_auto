package project

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.text.SimpleDateFormat

import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.Color

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.Common
import com.reusableComponents.HighlightElement

import internal.GlobalVariable

public class SafetyNet extends Common
{

	HighlightElement on = new HighlightElement()
	String SafetyNetEventColor = "#1a73f0"
	String SafetyNetVariableColor = "#00ccb0"

	public SafetyNet()
	{
	}

	@Keyword
	public String Start()
	{
		/**
		 * Log in to SafetyNet for specified environment
		 *
		 */
		String baseUrl = 'https://' + GlobalVariable.env
		WebUI.openBrowser(baseUrl)
		WebUI.maximizeWindow()
		login(GlobalVariable.googleID, GlobalVariable.googlePassword)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Homepage/Account menu button'), 30) : "Account menu button is not displayed after 5 second"
		selenium = getSelenium()
		boolean isNoAccount = selenium.isElementPresent(getSelector(findTestObject('Object Repository/3. Homepage/No account message')))
		if(isNoAccount) {
			KeywordUtil.logInfo('Checking homepage no account message')
			String noAccountMessage = WebUI.getText(findTestObject('Object Repository/3. Homepage/No account message'))
			assert noAccountMessage == "You haven't been added to any account yet" : "No account message is not correct : " + noAccountMessage
		}
		return baseUrl
	}

	@Keyword
	def login(String id, String pwd)
	{
		/**
		 * Log in to SafetyNet
		 * 
		 * @param id
		 * 		The id of the user
		 * @param pwd
		 * 		The encrypted password of the user
		 */


		KeywordUtil.logInfo('Checking login tip...')
		assert WebUI.waitForElementVisible(findTestObject('1. Login Google/tip'), 5) : "Tip message is not displayed after 5 second"
		String tiptext = WebUI.getText(findTestObject('1. Login Google/tip'))
		assert tiptext.trim() == "If you can’t access the page, please contact the authentifier team on the Slack Channel #prod_authentifier_support or send an email by clicking here." : "Tip message has changed : " + tiptext
		assert WebUI.verifyElementAttributeValue(findTestObject('1. Login Google/Slack channel link'), 'href', 'slack://channel?team=T4A2YGWSF&id=CNTQWDJQP', 5) : "Channel Slack link is not correct"
		assert WebUI.verifyElementAttributeValue(findTestObject('1. Login Google/here link'), 'href', 'mailto:authentifier@jellyfish.com?subject=Add%20my%20email%20to%20Authentifier&body=Hello,%0D%0A%0D%0Aplease%20add%20me%20to%20the%20Authentifier%20in%20order%20to%20access%20%22Safetynet%22.%0D%0A%0D%0AKind%20Regards,', 5) : "Email link is not correct"

		driver = DriverFactory.getWebDriver()
		String parentWindow = driver.getWindowHandle()
		WebUI.click(findTestObject('1. Login Google/button'))
		Set<String> handles = driver.getWindowHandles()
		for(String windowHandle  : handles)
		{
			if(!windowHandle.equals(parentWindow))
			{
				driver.switchTo().window(windowHandle);
				googleSignIn(id, pwd)
				driver.switchTo().window(parentWindow);
				handles = driver.getWindowHandles()
				if (handles.size() == 1)
					return
				driver.switchTo().window(windowHandle);
				Googleauthentificator()
				driver.switchTo().window(parentWindow);
			}
		}
	}

	@Keyword
	def logout()
	{
		/**
		 * Log out of SafetyNet
		 */
		WebUI.waitForElementVisible(findTestObject('3. Homepage/Logout button'), 5)
		WebUI.click(findTestObject('3. Homepage/Logout button'))
		assert WebUI.waitForElementVisible(findTestObject('1. Login Google/tip'), 5) : "Login page is not displayed after 5 second"
	}
}
