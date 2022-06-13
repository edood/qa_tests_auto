package com.reusableComponents
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import internal.GlobalVariable

class CommonKeywords {
	public static TestObject tmp;
	public WebDriver driver;
	public String baseUrl;
	public WebDriverBackedSelenium selenium;

	public CommonKeywords() {
		tmp = new TestObject("tmp")
		tmp.addProperty('css', ConditionType.EQUALS, '')
		tmp.addProperty('xpath', ConditionType.EQUALS, '')
	}
	
	public WebDriverBackedSelenium getSelenium() {
		/**
		 * Get a selenium object to user methods from
		 *
		 * @author fbalhier
		 */

		driver = DriverFactory.getWebDriver()
		baseUrl = 'https://' + GlobalVariable.env + '-' + GlobalVariable.env2 + '.tradelab.fr' + '/folders'
		selenium = new WebDriverBackedSelenium(driver, baseUrl)

		return selenium
	}

	@Keyword
	def clickObjectbyCss(String cssSelector) {
		WebUI.delay(1)
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), cssSelector, true)
		WebUI.delay(1)
		WebUI.click(tmp)
	}

	@Keyword
	def waitforCssSelectorVisible(String CssSelector, int timeout) {
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), CssSelector, true)
		WebUI.waitForElementVisible(tmp, timeout)
		WebUI.delay(1)
	}

	public static TestObject selectorToTestObject(String selector) {
		WebUI.removeObjectProperty(tmp, 'xpath')
		WebUI.removeObjectProperty(tmp, 'css')
		tmp.addProperty('css', ConditionType.EQUALS, selector, true)
		return tmp
	}

	public static TestObject xpathSelectorToTestObject(String selector) {
		WebUI.removeObjectProperty(tmp, 'css')
		WebUI.removeObjectProperty(tmp, 'xpath')
		tmp.addProperty('xpath', ConditionType.EQUALS, selector, true)
		return tmp
	}

	public static TestObject changeSelector(TestObject testObject, String regex, String replacement) {
		testObject.setSelectorValue(SelectorMethod.CSS, testObject.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, replacement))
		return testObject
	}

	public static String getSelector(TestObject testObject) {
		return "css=" + testObject.getSelectorCollection().get(SelectorMethod.CSS)
	}

	@Keyword
	def login(Boolean isLogoutInTwoSteps) {

		String tiret = '-'
		if (((GlobalVariable.env != 'dev') && (GlobalVariable.env != 'staging')) && (GlobalVariable.env != 'prod')) {
			GlobalVariable.env2 = 'saas'
		} else if (GlobalVariable.env == 'prod') {
			tiret = ''
			GlobalVariable.env = ''
		}
		String page = 'login'
		String url = 'https://' + GlobalVariable.env + tiret + GlobalVariable.env2 + '.tradelab.fr' + '/' + page
		WebUI.openBrowser(url)
		WebUI.maximizeWindow()
		WebUI.delay(2)
		WebUI.waitForPageLoad(30)

		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver, url)

		boolean isLoggedIn = selenium.isElementPresent(findTestObject('1. Login/Logged in').getSelectorCollection().get(SelectorMethod.CSS))
		if (isLoggedIn == true) {
			logout(isLogoutInTwoSteps)
			WebUI.setText(findTestObject('1. Login/Username input'), GlobalVariable.username)
			WebUI.setEncryptedText(findTestObject('1. Login/Password input'), GlobalVariable.password)
			WebUI.click(findTestObject('1. Login/Login button'))
		}
		else {
			WebUI.waitForElementVisible(findTestObject('1. Login/Username input'), 5)
			WebUI.setText(findTestObject('1. Login/Username input'), GlobalVariable.username)
			WebUI.setEncryptedText(findTestObject('1. Login/Password input'), GlobalVariable.password)
			WebUI.click(findTestObject('1. Login/Login button'))
		}
		WebUI.delay(2)
		WebUI.waitForPageLoad(30)
	}

	@Keyword
	def loginToGoogle() {
		WebUI.navigateToUrl('https://accounts.google.com/signin/v2/identifier?flowName=GlifWebSignIn&flowEntry=ServiceLogin')
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementPresent(findTestObject('6. Login Google/Google Identifier input'), 30)
		WebUI.setText(findTestObject('6. Login Google/Google Identifier input'), GlobalVariable.googleID)
		WebUI.click(findTestObject('6. Login Google/Next button 1'))
		WebUI.waitForElementVisible(findTestObject('6. Login Google/Google password input'), 10)
		WebUI.setEncryptedText(findTestObject('6. Login Google/Google password input'), GlobalVariable.googlePassword)
		WebUI.click(findTestObject('6. Login Google/Next button 2'))
		WebUI.waitForPageLoad(30)
		WebUI.delay(1)
	}

	@Keyword
	def loginToGoogle(String googleID, String googlePassword) {
		WebUI.navigateToUrl('https://accounts.google.com/signin/v2/identifier?flowName=GlifWebSignIn&flowEntry=ServiceLogin')
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementPresent(findTestObject('6. Login Google/Google Identifier input'), 30)
		WebUI.setText(findTestObject('6. Login Google/Google Identifier input'), googleID)
		WebUI.click(findTestObject('6. Login Google/Next button 1'))
		WebUI.waitForElementVisible(findTestObject('6. Login Google/Google password input'), 10)
		WebUI.setEncryptedText(findTestObject('6. Login Google/Google password input'), googlePassword)
		WebUI.click(findTestObject('6. Login Google/Next button 2'))
		WebUI.waitForPageLoad(30)
		WebUI.delay(1)
	}

	@Keyword
	def logout(boolean isLogoutInTwoSteps) {
		if(isLogoutInTwoSteps == true) {
			WebUI.click(findTestObject('1. Login/Logout menu'))
		}
		WebUI.waitForElementVisible(findTestObject('1. Login/Logout button'), 5)
		WebUI.click(findTestObject('1. Login/Logout button'))
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementPresent(findTestObject('1. Login/Username input'), 5)
	}
}