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
		WebUI.waitForElementNotPresent(findTestObject('1. Login Google/Loading progress'), 15)
		WebUI.waitForElementPresent(findTestObject('1. Login Google/Password input'), 5)
		WebUI.setEncryptedText(findTestObject('1. Login Google/Password input'), pwd)
		WebUI.click(findTestObject('1. Login Google/Password next button'))
		WebUI.delay(1)
		WebUI.waitForElementNotPresent(findTestObject('1. Login Google/Loading progress'), 15)
		WebUI.delay(1)
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
		baseUrl = 'https://' + GlobalVariable.environment
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
		selenium = getSelenium()
		int currentWindowIndex = WebUI.getWindowIndex()
		selenium.openWindow("","")
		WebUI.switchToWindowIndex(currentWindowIndex + 1)
	}

	def closeNewTab()
	{
		/**
		 * Closes the second tab and return to the first tab
		 */
		int currentWindowIndex = WebUI.getWindowIndex()
		WebUI.closeWindowIndex(currentWindowIndex)
		WebUI.switchToWindowIndex(currentWindowIndex-1)
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

	public static boolean isHourInInterval(String target, String start, String end)
	{

		/**
		 * Check if a date is in a range
		 *
		 * @param  target
		 * 		hour to check
		 * @param  start
		 * 		interval start
		 * @param  end
		 * 		interval end
		 * @return
		 * 		true if the given hour is between, false otherwise
		 */
		return ((target.compareTo(start) >= 0) && (target.compareTo(end) <= 0));
	}
}
