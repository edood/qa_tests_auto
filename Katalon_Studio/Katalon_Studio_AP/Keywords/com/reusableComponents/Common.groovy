package com.reusableComponents

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.Instant

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

	public String getOnlySelectorXpath(TestObject testObject)
	{
		/**
		 * Returns the xpath selector of a TestObject without "xpath="
		 *
		 * @param testObject		TestObject to return selector from
		 */
		return testObject.getSelectorCollection().get(SelectorMethod.XPATH)
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

	@Keyword
	def openNewTab()
	{
		/**
		 * Open a new tab and switch to it.
		 */
		selenium = getSelenium()
		int lastWindowIndex = selenium.getAllWindowTitles().length-1
		int currentWindowIndex = WebUI.getWindowIndex()
		WebUI.switchToWindowIndex(lastWindowIndex)
		selenium.openWindow("","")
		WebUI.switchToWindowIndex(lastWindowIndex + 1)
	}

	@Keyword
	def closeNewTab()
	{
		/**
		 * Closes a tab and return to the closest tab to the left
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

	@Keyword
	def Googleauthentificator()
	{
		/**
		 * This function log in google with the 2FA google authentificator
		 * the global variable GoogleAuthentificatorKey is requiered
		 */

		WebUI.maximizeWindow()
		Thread.sleep(500)
		assert WebUI.waitForElementNotPresent(findTestObject('0. Login Google/Loading progress'), 15) : "Loading bar is still present after 15 seconds"
		assert WebUI.waitForElementVisible(findTestObject('0. Login Google/2FA header'), 30) : "2FA popup not visible"
		KeywordUtil.logInfo('2FA header = ' + WebUI.getText(findTestObject('0. Login Google/2FA header')))
		assert WebUI.getText(findTestObject('0. Login Google/2FA header')) == 'Validation en deux étapes' : "Connection failed"
		WebUI.delay(3)
		selenium = getSelenium()
		if (selenium.isElementPresent(getSelector(findTestObject('0. Login Google/Try another way span'))) == true)
		{
			String result = WebUI.getText(findTestObject('0. Login Google/Try another way span'))
			if (result == 'Essayer une autre méthode')
			{
				KeywordUtil.logInfo('authentificator not visible')
				assert WebUI.verifyElementVisible(findTestObject('0. Login Google/Try another way'))
				WebUI.click(findTestObject('0. Login Google/Try another way'))
				assert WebUI.waitForElementVisible(findTestObject('0. Login Google/Google Authentificator'), 30) : "The connection google authentificator is not available for this account"
			}
			KeywordUtil.logInfo('result = ' + result)
		}
		ArrayList<WebElement> ConnectionMethod = driver.findElements(By.cssSelector(findTestObject('0. Login Google/other authentification method').getSelectorCollection().get(SelectorMethod.CSS)))
		for (def elem in ConnectionMethod)
		{
			if (elem.getText().contains('Google Authenticator') == true)
				elem.click()
		}
		assert WebUI.waitForElementVisible(findTestObject('0. Login Google/input tel'), 30) : "Input to submit code is not visible"
		try
		{
			Totp auth = new Totp(GlobalVariable.GoogleAuthentificatorKey)
			String code = auth.now()
			KeywordUtil.logInfo('code to submit = ' + code)
			WebUI.sendKeys(findTestObject('0. Login Google/input tel'), code)
			WebUI.click(findTestObject('0. Login Google/Totp next'))
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

		assert WebUI.waitForElementVisible(findTestObject('0. Login Google/popup email'), 30) : "Email popup is not visible"
		WebUI.setText(findTestObject('0. Login Google/Email input'), id)
		WebUI.click(findTestObject('0. Login Google/Next'))
		WebUI.delay(3)
		WebUI.setEncryptedText(findTestObject('0. Login Google/Password type password'), pwd)
		WebUI.click(findTestObject('0. Login Google/Password next button'))
		WebUI.delay(5)
	}

	@Keyword
	def getEpochSecond()
	{
		/**
		 * Returns the current date in epoch second format
		 */
		return Instant.now().getEpochSecond()
	}

	@Keyword
	def getCurrentMinute()
	{
		/**
		 * Returns the current minute modulo 10
		 *
		 * @returns 0 to 9
		 */

		return Calendar.getInstance().get(Calendar.MINUTE) % 10
	}

	@Keyword
	def getPauseAttempts(int startMinute, int maxMinutes, int secondsToPause)
	{
		/**
		 * Returns the number of attempts to achieve a pause depending on the number of division (eg. 12 pauses of 5 seconds are needed to wait a minute)
		 *
		 * @param startMinute			Minute to start counting from
		 * @param maxMinutes			Maximum number of minutes to wait
		 * @param secondsToPause		Number of seconds to wait between each attempt
		 */
		int minuteLeft = maxMinutes - startMinute
		return (minuteLeft * 60) / secondsToPause
	}
}
