package com.reusableComponents

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import internal.GlobalVariable
import project.SafetyNet

public class DV360 extends SafetyNet
{
	@Keyword
	def goToDv360()
	{
		KeywordUtil.logInfo("Navigating to DV360")
		WebUI.navigateToUrl('https://displayvideo.google.com/#ng_nav/overview')
		WebUI.waitForPageLoad(30)
		WebUI.delay(2)
	}

	@Keyword
	def logInToDv360(boolean newTab)
	{
		selenium = getSelenium()
		if(newTab == true)
		{
			selenium.openWindow("","")
			WebUI.switchToWindowIndex(1)
		}
		WebUI.navigateToUrl('https://displayvideo.google.com/#ng_nav/overview')
		WebUI.waitForPageLoad(30)
		WebUI.delay(2)
		if(selenium.isElementPresent(getSelector(findTestObject('2. DV360/DV360 search button'))) == false)
		{
			WebUI.setText(findTestObject('1. Login Google/Email input'), GlobalVariable.googleID)
			WebUI.sendKeys(findTestObject('1. Login Google/Email input'), Keys.chord(Keys.ENTER))
			WebUI.delay(1)
			WebUI.waitForElementNotVisible(findTestObject('1. Login Google/Loading progress'), 15)
			WebUI.waitForElementVisible(findTestObject('1. Login Google/Password input'), 15)
			WebUI.setEncryptedText(findTestObject('1. Login Google/Password input'), GlobalVariable.googlePassword)
			WebUI.sendKeys(findTestObject('1. Login Google/Password input'), Keys.chord(Keys.ENTER))
			WebUI.delay(10)
		} else
		{
			KeywordUtil.logInfo("Already logged in")
		}
	}
	@Keyword
	def dv360SearchItem(String itemToSearch)
	{
		selenium = getSelenium()

		WebUI.waitForElementVisible(findTestObject('2. DV360/DV360 search button'), 60)
		while(!selenium.isElementPresent(getSelector(findTestObject('2. DV360/Search suggestion'))))
		{
			WebUI.click(findTestObject('2. DV360/DV360 search button'))
			WebUI.setText(findTestObject('2. DV360/input_Search dv360'), itemToSearch)
			WebUI.waitForElementVisible(findTestObject('2. DV360/Search suggestion'), 10, FailureHandling.OPTIONAL)
		}
		WebUI.click(findTestObject('2. DV360/Search suggestion'), FailureHandling.STOP_ON_FAILURE)
		WebUI.delay(5)
		assert WebUI.verifyElementNotPresent(findTestObject('2. DV360/Item List/List Loading bar'), 30) == true
	}
}