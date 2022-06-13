package com.reusableComponents
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.jboss.aerogear.security.otp.Totp
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import internal.GlobalVariable

public class Common
{
	public WebDriver driver;
	public String baseUrl;
	public WebDriverBackedSelenium selenium;

	@Keyword
	def Googleauthentificator()
	{
		/**
		 * This function log in google with the 2FA google authentificator
		 * the global variable GoogleAuthentificatorKey is requiered
		 */

		WebUI.maximizeWindow()
		assert WebUI.waitForElementVisible(findTestObject('1. Login Google/2FA header'), 30) : "2FA popup not visible"
		KeywordUtil.logInfo('2FA header = ' + WebUI.getText(findTestObject('1. Login Google/2FA header')))
		assert WebUI.getText(findTestObject('1. Login Google/2FA header')) == 'Validation en deux étapes' : "Connection failed"
		WebUI.delay(3)
		selenium = getSelenium()
		if (selenium.isElementPresent(getSelector(findTestObject('1. Login Google/Try another way span'))) == true)
		{
			String result = WebUI.getText(findTestObject('1. Login Google/Try another way span'))
			if (result == 'Essayer une autre méthode')
			{
				KeywordUtil.logInfo('authentificator not visible')
				assert WebUI.verifyElementVisible(findTestObject('1. Login Google/Try another way'))
				WebUI.click(findTestObject('1. Login Google/Try another way'))
				assert WebUI.waitForElementVisible(findTestObject('1. Login Google/Google Authentificator'), 30) : "The connection google authentificator is not available for this account"
			}
			KeywordUtil.logInfo('result = ' + result)
		}
		ArrayList<WebElement> ConnectionMethod = driver.findElements(By.cssSelector(findTestObject('1. Login Google/other authentification method').getSelectorCollection().get(SelectorMethod.CSS)))
		for (def elem in ConnectionMethod)
		{
			if (elem.getText().contains('Google Authenticator') == true)
				elem.click()
		}
		WebUI.delay(1)
		WebUI.waitForElementNotPresent(findTestObject('1. Login Google/Loading progress'), 15)
		assert WebUI.waitForElementVisible(findTestObject('1. Login Google/input tel'), 30) : "Input to submit code is not visible"
		try
		{
			Totp auth = new Totp(GlobalVariable.GoogleAuthentificatorKey)
			String code = auth.now()
			KeywordUtil.logInfo('code to submit = ' + code)
			WebUI.sendKeys(findTestObject('1. Login Google/input tel'), code)
			WebUI.click(findTestObject('1. Login Google/Totp next'))
			WebUI.delay(3)
		}
		catch (Exception e)
		{
			KeywordUtil.logInfo(e.getMessage())
		}
	}

	@Keyword
	def googleSignIn(String id, String pwd)
	{
		/**
		 * This function log the user with google sign in
		 * @param id
		 * The google id to be set
		 * @param pwd
		 * The google password to be set
		 */

		assert WebUI.waitForElementVisible(findTestObject('1. Login Google/popup email'), 30) : "Email popup is not visible"
		WebUI.setText(findTestObject('1. Login Google/Email input'), id)
		WebUI.click(findTestObject('1. Login Google/Next'))
		WebUI.delay(1)
		WebUI.waitForElementNotPresent(findTestObject('1. Login Google/Loading progress'), 30)
		WebUI.waitForElementPresent(findTestObject('1. Login Google/Password input'), 5)
		WebUI.delay(1)
		WebUI.setEncryptedText(findTestObject('1. Login Google/Password input'), pwd)
		WebUI.click(findTestObject('1. Login Google/Password next button'))
		WebUI.delay(5)
	}


	public TestObject changeSelector(TestObject testObject, String regex, String replacement)
	{
		/**
		 * Return a TestObject after changing its selector (css)
		 * 
		 * @param testObject		TestObject to change selector from
		 */
		testObject.setSelectorValue(SelectorMethod.CSS, testObject.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, replacement))
		return testObject
	}

	public TestObject changeSelectorXpath(TestObject testObject, String regex, String replacement)
	{
		/**
		 * Return a TestObject after changing its selector (xpath)
		 * 
		 * @param testObject		TestObject to return xpath selector from
		 */
		testObject.setSelectorValue(SelectorMethod.XPATH, testObject.getSelectorCollection().get(SelectorMethod.XPATH).replaceFirst(regex, replacement))
		return testObject
	}

	@Keyword
	public String getSelector(TestObject testObject)
	{
		/**
		 * Returns the css selector of a TestObject ("css=selector")
		 * 
		 * @param testObject		TestObject to return css selector from
		 */
		return "css=" + testObject.getSelectorCollection().get(SelectorMethod.CSS)
	}

	public String getOnlySelector(TestObject testObject)
	{
		/**
		 * Returns the css selector of a TestObject without "css="
		 * 
		 * @param testObject		TestObject to return selector from
		 */
		return testObject.getSelectorCollection().get(SelectorMethod.CSS)
	}

	WebDriverBackedSelenium getSelenium()
	{
		/**
		 * Returns a WebDriverBackedSelenium object in order to use selenium methods.
		 */
		driver = DriverFactory.getWebDriver()
		baseUrl = 'https://' + GlobalVariable.env
		return selenium = new WebDriverBackedSelenium(driver, baseUrl)
	}

	WebDriver getWebDriver()
	{
		return driver = DriverFactory.getWebDriver()
	}

	def openNewTab()
	{
		/**
		 * Open a new tab and switch to it.
		 */
		selenium.openWindow("")
		WebUI.switchToWindowIndex(1)
	}

	def closeNewTab()
	{
		/**
		 * Closes the second tab and return to the first tab
		 */
		WebUI.closeWindowIndex(1)
		WebUI.switchToWindowIndex(0)
	}


	def setText(TestObject testObject, String text)
	{
		/**
		 * Set text and verify input value
		 *
		 * @param testObject			TestObject to set text into
		 * @param text				Text to set
		 */

		WebUI.setText(testObject, text)
		Thread.sleep(1000)

		String objectValue = WebUI.getAttribute(testObject, "value")
		String objectName = testObject.getObjectId().split('/').last()
		assert objectValue == text : "'" + objectName + "' text is not correct. Displayed : '" + objectValue + "'; Expected : '"+text+"'"
	}

	@Keyword
	def assertText(TestObject testObject, expectedText)
	{
		/**
		 * Assert text of a test object
		 * 
		 * @param testObject		TestObject to assert text from
		 * @param expectedText		Expected text of the Test Object
		 */
		String objectText = WebUI.getText(testObject)
		String objectName = testObject.getObjectId().split('/').last()
		assert objectText == expectedText : "'" + objectName + "' text is not correct. Displayed : '" + objectText + "'; Expected : '"+expectedText+"'"
	}

	@Keyword
	def assertValue(TestObject testObject, expectedValue)
	{
		/**
		 * Assert text of a test object
		 *
		 * @param testObject		TestObject to assert value from
		 * @param expectedText		Expected value of the Test Object
		 */
		String objectValue = WebUI.getAttribute(testObject, "value")
		String objectName = testObject.getObjectId().split('/').last()
		assert objectValue == expectedValue : "'" + objectName + "' value is not correct. Displayed : '" + objectValue + "'; Expected : '"+expectedValue+"'"
	}

	@Keyword
	def waitVisibleAndClick(TestObject testObject, int timeout = 30)
	{
		/**
		 * Wait for an object to be visible and click on it
		 *
		 * @param testObject			TestObject to click on after the wait
		 * @param timeout			Time to wait for the object to be visible in seconds
		 */
		String objectName = testObject.getObjectId().split('/').last()
		assert WebUI.waitForElementVisible(testObject, timeout) : "\n\n" + objectName + " is not visible after " + timeout + " seconds.\n\n"
		WebUI.click(testObject)
	}

	@Keyword
	def waitPresent(TestObject testObject, int timeoutMilliseconds = 5000, boolean isAssert = false)
	{
		/**
		 * Wait for an object to be present without returning an error, unless
		 *
		 * @param testObject			TestObject to click on
		 * @param timeout			Time to wait for the object to be present in seconds
		 */

		assert timeoutMilliseconds > 0
		selenium = getSelenium()
		boolean isElementPresent = selenium.isElementPresent(getSelector(testObject))
		while(!isElementPresent || timeoutMilliseconds > 0)
		{
			isElementPresent = selenium.isElementPresent(getSelector(testObject))
			Thread.sleep(250)
			timeoutMilliseconds -= 250
		}
		String objectName = testObject.getObjectId().split('/').last()
		if(isAssert)
			assert isElementPresent : "\n\n" + objectName + " is not present after " + (timeoutMilliseconds / 1000) + " seconds.\n\n"
	}

	@Keyword
	public void deleteFile(String fileFullPath)
	{
		/**
		 * Delete a file on the user's computer
		 * 
		 * @param fileFullPath		File full path (eg. "C:\Users\xxx\Downloads")
		 */
		File file = new File(fileFullPath)
		assert file.delete() == true
		WebUI.delay(1)
		KeywordUtil.markPassed("File deleted : " + file.name)
	}
	public int getRandomArbitrary(min, max)
	{
		return Math.random() * (max - min) + min;
	}
}
