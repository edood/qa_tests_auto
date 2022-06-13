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
import project.Builder

public class DV360 extends Builder
{
	@Keyword
	def goToDv360()
	{
		KeywordUtil.logInfo("Navigating to DV360")
		WebUI.navigateToUrl('https://displayvideo.google.com/#ng_nav/overview')
		WebUI.waitForPageLoad(30)
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
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/DV360 search button'))) == false)
		{
			WebUI.setText(findTestObject('1. Login/Login type email'), GlobalVariable.googleID)
			WebUI.sendKeys(findTestObject('1. Login/Login type email'), Keys.chord(Keys.ENTER))
			Thread.sleep(500)
			WebUI.waitForElementNotPresent(findTestObject('1. Login/Loading progress'), 15)
			WebUI.waitForElementVisible(findTestObject('1. Login/Password type password'), 15)
			WebUI.setEncryptedText(findTestObject('1. Login/Password type password'), GlobalVariable.googlePassword)
			WebUI.sendKeys(findTestObject('1. Login/Password type password'), Keys.chord(Keys.ENTER))
			Thread.sleep(500)
			WebUI.waitForElementNotPresent(findTestObject('1. Login/Loading progress'), 15)
			WebUI.waitForPageLoad(30)
			if(selenium.isElementPresent(getSelector(findTestObject('1. Login/GoogleSignIn/2FA header'))))
				Googleauthentificator()
		} else
		{
			KeywordUtil.logInfo("Already logged in")
		}
	}
	@Keyword
	def dv360SearchItem(String itemToSearch)
	{
		selenium = getSelenium()

		WebUI.waitForElementVisible(findTestObject('10. DV360/DV360 search button'), 60)
		while(!selenium.isElementPresent(getSelector(findTestObject('10. DV360/Search suggestion'))))
		{
			WebUI.click(findTestObject('10. DV360/DV360 search button'))
			WebUI.waitForElementVisible(findTestObject('10. DV360/input_Search dv360'), 5)
			WebUI.setText(findTestObject('10. DV360/input_Search dv360'), itemToSearch)
			WebUI.waitForElementVisible(findTestObject('10. DV360/Search suggestion'), 10, FailureHandling.OPTIONAL)
		}
		WebUI.click(findTestObject('10. DV360/Search suggestion'), FailureHandling.STOP_ON_FAILURE)
	}

	@Keyword
	boolean DV360Upload(String FileToUpload)
	{
		/**
		 * This function uploads a file on dv360 and verifies if it is successful or failed
		 *
		 * @param FileToUpload
		 *
		 * @return true or false depending on upload result
		 *
		 **/

		logInToDv360(false)
		dv360SearchItem(GlobalVariable.advertiserName)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/SDF option'), 30) == true
		WebUI.delay(1)
		WebUI.click(findTestObject('10. DV360/SDF option'))
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/SDF Upload button'), 30) == true
		WebUI.click(findTestObject('10. DV360/SDF Upload button'))
		assert WebUI.waitForElementPresent(findTestObject('10. DV360/Upload Input'), 30) == true
		WebUI.uploadFile(findTestObject('10. DV360/Upload Input'), FileToUpload)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Uploaded file'), 30) == true
		assert WebUI.verifyElementVisible(findTestObject('10. DV360/Uploaded file'), FailureHandling.STOP_ON_FAILURE) == true
		WebUI.click(findTestObject('10. DV360/Upload Button'))
		Thread.sleep(500)
		assert WebUI.waitForElementNotPresent(findTestObject('10. DV360/Upload loading dialog'), 600) == true


		int maxWait = 360

		WebDriver driver = DriverFactory.getWebDriver()
		String baseUrl = 'https://displayvideo.google.com/#ng_nav/overview'
		WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver, baseUrl)

		while (maxWait > 0 && selenium.isElementPresent(getSelector(findTestObject('10. DV360/Success Upload'))) == false)
		{
			WebUI.delay(1)
			maxWait--
		}

		if (selenium.isElementPresent(getSelector(findTestObject('10. DV360/Success Upload'))) == false)
		{
			WebUI.click(findTestObject('10. DV360/Upload expand error'))
			String str = WebUI.getText(findTestObject('10. DV360/Upload error')) + WebUI.getText(findTestObject('10. DV360/Upload error main'))
			KeywordUtil.markErrorAndStop(str)
		}

		assert WebUI.verifyElementVisible(findTestObject('10. DV360/Success Upload'))
		WebUI.click(findTestObject('Object Repository/10. DV360/Upload Close Modal button'))

		return (true)
	}
	@Keyword
	boolean DV360Upload(ArrayList<String> FilesToUpload)
	{
		/**
		 * This function uploads a list of file on dv360 and verifies if it is successful or failed
		 * Skips the first element (zip file)
		 *
		 * @param FilesToUpload
		 *
		 * @return true or false depending on upload result
		 *
		 **/

		logInToDv360(false)
		dv360SearchItem(GlobalVariable.advertiserName)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/SDF option'), 30) == true
		WebUI.delay(1)
		WebUI.click(findTestObject('10. DV360/SDF option'))
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/SDF Upload button'), 30) == true
		WebUI.click(findTestObject('10. DV360/SDF Upload button'))
		assert WebUI.waitForElementPresent(findTestObject('10. DV360/Upload Input'), 30) == true
		int i = 0
		for (; i < FilesToUpload.size() && FilesToUpload[i] != ""; i++)
		{
			if (i == 1)
			{
				WebUI.uploadFile(findTestObject('10. DV360/Upload Input'), FilesToUpload[i])
				KeywordUtil.logInfo('File uploaded : ' + FilesToUpload[i])
			}
			else if (i > 0 && i < FilesToUpload.size() && i != 1) // Skips the first element (zip file)
			{
				assert WebUI.waitForElementPresent(findTestObject('10. DV360/Upload several file input'), 10) == true : 'hidden button upload more file not present'
				WebUI.uploadFile(findTestObject('10. DV360/Upload several file input'), FilesToUpload[i])
				KeywordUtil.logInfo('File uploaded : ' + FilesToUpload[i])
			}
		}
		i--
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Uploaded file'), 30) == true : "Uploaded file not visible before timeout"
		WebUI.click(findTestObject('10. DV360/Upload Button'))
		Thread.sleep(500)
		assert WebUI.waitForElementNotPresent(findTestObject('10. DV360/Upload loading dialog'), 600) == true

		int maxWait = 360

		String baseUrl = 'https://displayvideo.google.com/#ng_nav/overview'
		WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver, baseUrl)

		while (maxWait > 0 && selenium.isElementPresent(getSelector(findTestObject('10. DV360/Success Upload'))) == false)
		{
			WebUI.delay(1)
			maxWait--
		}

		if (selenium.isElementPresent(getSelector(findTestObject('10. DV360/Success Upload'))) == false)
		{
			//	WebUI.click(findTestObject('10. DV360/Upload expand error'))
			//	String str = WebUI.getText(findTestObject('10. DV360/Upload error')) + WebUI.getText(findTestObject('10. DV360/Upload error main'))
			//	KeywordUtil.markErrorAndStop(str)
		}

		assert WebUI.verifyElementVisible(findTestObject('10. DV360/Success Upload'))
		WebUI.click(findTestObject('Object Repository/10. DV360/Upload Close Modal button'))

		return (true)
	}

	@Keyword
	def dvSetLineFlightDateSameAsIO(boolean logInToGoogle, String lineToSearch)
	{
		if(logInToGoogle == true)
		{
			logInToDv360(true)
		}
		goToDv360()
		dv360SearchItem(lineToSearch)

		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Flight date Same as IO checkbox Enabled'), 30) == true : "Same as Insertion Order checkbox is disable, please create an active budget interval on the Insertion Order"
		WebUI.sendKeys(findTestObject('Object Repository/10. DV360/Line Item fields/Frequency Capping limit value'), Keys.chord(Keys.PAGE_UP))
		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/Flight date Same as IO checkbox Checked'))) == false)
		{
			WebUI.click(findTestObject('10. DV360/Line Item fields/Flight date Same as IO checkbox Enabled'))
			WebUI.click(findTestObject('10. DV360/Line Item fields/Save button'))
			WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Changes saved message'), 30)
			assert WebUI.verifyElementText(findTestObject('10. DV360/Line Item fields/Changes saved message'), "Changes saved") == true : "Save message is incorrect"
		}
		WebUI.delay(1)
	}

	@Keyword
	def dvSetLineStatusPaused()
	{
		selenium = getSelenium()
		if(selenium.getText(getSelector(findTestObject('10. DV360/Line Item fields/Status button'))) != "Paused")
		{
			WebUI.focus(findTestObject('10. DV360/Line Item fields/Status button'))
			WebUI.click(findTestObject('10. DV360/Line Item fields/Status button'))
			WebUI.waitForElementPresent(findTestObject('Object Repository/10. DV360/Line Item fields/Status Paused'), 30)
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/Status Paused'))
			WebUI.delay(1)
			WebUI.click(findTestObject('10. DV360/Line Item fields/Save button'))
			assert WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Changes saved message'), 30) == true : "Line was not saved"
			assert WebUI.verifyElementText(findTestObject('10. DV360/Line Item fields/Changes saved message'), "Changes saved") == true : "Save message is incorrect"
		}
		WebUI.delay(1)
	}

	@Keyword
	def dvSetLineStatusActive(String line)
	{
		/**
		 * this function set a line to active
		 * @param line
		 * the line to be set active on dv360
		 */
		dv360SearchItem(line)
		Thread.sleep(500)
		if(WebUI.getText(findTestObject('10. DV360/Line Item fields/Status button')) != "Active")
		{
			WebUI.focus(findTestObject('10. DV360/Line Item fields/Status button'))
			WebUI.click(findTestObject('10. DV360/Line Item fields/Status button'))
			WebUI.waitForElementPresent(findTestObject('Object Repository/10. DV360/Line Item fields/Status Active'), 30)
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/Status Active'))
			WebUI.delay(1)
			WebUI.click(findTestObject('10. DV360/Line Item fields/Save button'))
			assert WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Changes saved message'), 30) == true : "Line was not saved"
			assert WebUI.verifyElementText(findTestObject('10. DV360/Line Item fields/Changes saved message'), "Changes saved") == true : "Save message is incorrect"
		}
		WebUI.delay(1)
	}

	@Keyword
	float dvGetLineBidStrategyOrSetFixed(boolean logInToGoogle)
	{
		return dvGetLineBidStrategyOrSetFixed(logInToGoogle, GlobalVariable.items[2])
	}
	@Keyword
	float dvGetLineBidStrategyOrSetFixed(boolean logInToGoogle, String lineToSearch)
	{
		float bidValue = 1.00f

		if(logInToGoogle == true)
		{
			logInToDv360(false)
		}
		goToDv360()
		dv360SearchItem(lineToSearch)

		WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Add Targeting button'), 30)
		WebUI.scrollToElement(findTestObject('10. DV360/Line Item fields/Add Targeting button'), 1)
		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed Checked'))) == false)
		{
			WebUI.check(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed checkbox'))
			WebUI.setText(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed value'), "1")
			WebUI.click(findTestObject('10. DV360/Line Item fields/Save button'))
			WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Changes saved message'), 30)
		} else
		{
			WebUI.focus(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed value'))
			bidValue = Float.valueOf(WebUI.getAttribute(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed value'), "value"))
		}
		WebUI.delay(1)

		return bidValue
	}
	@Keyword
	float dvSetLineBidStrategyFixed(boolean logInToGoogle, String lineToSearch, String newBidValue)
	{
		float bidValue = 1.00f

		if(logInToGoogle == true)
		{
			logInToDv360(false)
		}
		goToDv360()
		dv360SearchItem(lineToSearch)

		WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Add Targeting button'), 30)
		WebUI.scrollToElement(findTestObject('10. DV360/Line Item fields/Add Targeting button'), 1)
		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed Checked'))) == false)
		{
			WebUI.check(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed checkbox'))
		}

		WebUI.setText(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed value'), "")
		WebUI.sendKeys(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed value'), Keys.chord(Keys.CONTROL, 'a'))
		WebUI.sendKeys(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed value'), Keys.chord(Keys.BACK_SPACE))
		WebUI.setText(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed value'), newBidValue)
		WebUI.click(findTestObject('10. DV360/Line Item fields/Save button'))
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Changes saved message'), 30) == true : "Line was not saved"
		WebUI.delay(1)

		bidValue = Float.valueOf(WebUI.getAttribute(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed value'), "value"))

		return bidValue
	}

	@Keyword
	public void checkSetupValuesOnDV360(ArrayList<ArrayList<String>> itemsAndValuesToCheck)
	{
		selenium = getSelenium()
		String cpBudgetRegex = "Budget "
		String cpStartDateRegex = "Start Date "
		String cpEndDateRegex = "End Date "
		String cpGoalRegex = "Goal  "

		String ioBudgetIntervalsRegex = "Budget Intervals "
		String ioFrequencyCappingRegex = "Frequency Capping "
		String ioFrequencyExposureRegex = "Frequency Exposure "
		String ioFrequencyPeriodRegex = "Frequency Period "
		String ioFrequencyAmountRegex = "Frequency Amount "


		String budgetRegex = "Amount "
		String pacingAmountRegex = "Daily "
		String bidStrategyBeatValueRegex = "Beat CP[A|C] "
		String bidStrategyFixedValueRegex = "Fixed "
		String bidStrategyOptimizeValueRegex = "Optimize "
		String bidLimitRegex = "Limit "

		for (item in itemsAndValuesToCheck)
		{

			dv360SearchItem(item.get(0))
			WebUI.verifyElementPresent(findTestObject('10. DV360/Loading bar'), 15)
			assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 30) == true
			for (value in item)
			{
				if(item.get(1) == "Line Item")
				{
					if (value != item.get(0))
					{
						if (value == "Active" || value == "Paused")
						{
							assert WebUI.verifyElementText(findTestObject('10. DV360/span_Active'), value) == true : "Status does not match"
							selenium.highlight('css=' + findTestObject('10. DV360/span_Active').getSelectorCollection().get(SelectorMethod.CSS))
							Thread.sleep(500)
						}
						if (value.contains("Amount"))
						{
							assert WebUI.verifyElementPresent(findTestObject('10. DV360/Line Item fields/Budget Type Amount Checked'), 1) == true : "Budget Type is not 'Amount'"
							WebElement budgetTypeAmountValueElement = WebUiCommonHelper.findWebElement(findTestObject('10. DV360/Line Item fields/Budget Type Amount value'), 3)
							String budgetTypeAmountValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(budgetTypeAmountValueElement))
							assert WebUI.verifyEqual(budgetTypeAmountValue, value.replaceAll(budgetRegex, '')) == true : "Budget value does not match"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Budget Type Amount Checked').getSelectorCollection().get(SelectorMethod.CSS))
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Budget Type Amount value').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value == "Unlimited")
						{
							assert WebUI.verifyElementPresent(findTestObject('10. DV360/Line Item fields/Budget Type Unlimited Checked'), 1) == true : "Budget Type is not 'Unlimited'"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Budget Type Unlimited Checked').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value.contains("Daily"))
						{
							assert WebUI.verifyElementText(findTestObject('10. DV360/Line Item fields/Pacing text'), 'Daily') == true : "Pacing is not 'Daily'"
							WebElement pacingAmountValueElement = WebUiCommonHelper.findWebElement(findTestObject('Object Repository/10. DV360/Line Item fields/Pacing Rate value'), 3)
							String pacingAmountValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(pacingAmountValueElement))
							assert WebUI.verifyEqual(pacingAmountValue, value.replaceAll(pacingAmountRegex, '')) : "Pacing amount does not match"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Pacing text').getSelectorCollection().get(SelectorMethod.CSS))
							selenium.highlight('css=' + findTestObject('Object Repository/10. DV360/Line Item fields/Pacing Rate value').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value == "Flight")
						{
							assert WebUI.verifyElementText(findTestObject('10. DV360/Line Item fields/Pacing text'), 'Flight') == true : "Pacing is not 'Flight'"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Pacing text').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value == "ASAP")
						{
							assert WebUI.verifyElementText(findTestObject('10. DV360/Line Item fields/Pacing Rate text'), 'ASAP') == true : "Pacing rate is not 'ASAP'"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Pacing Rate text').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value == "Even")
						{
							assert WebUI.verifyElementText(findTestObject('10. DV360/Line Item fields/Pacing Rate text'), 'Even') == true : "Pacing rate is not 'Even'"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Pacing Rate text').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value == "Ahead")
						{
							assert WebUI.verifyElementText(findTestObject('10. DV360/Line Item fields/Pacing Rate text'), 'Ahead') == true : "Pacing rate is not 'Ahead'"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Pacing Rate text').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value.contains("Beat CPC"))
						{
							assert WebUI.verifyElementPresent(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat Checked'), 1) == true : "'Maximize clicks' option is not selected"
							assert WebUI.verifyElementPresent(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat value'), 1) == true : "Beat CPC value is not present. Is 'Maximize conversions' really selected ?"
							WebElement bidStrategyValueElement = WebUiCommonHelper.findWebElement(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat Value'), 3)
							String bidStrategyValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyValueElement))
							assert WebUI.verifyEqual(bidStrategyValue, value.replaceAll(bidStrategyBeatValueRegex, '')) == true : "Bid strategy Beat CPC value does not match"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat Checked').getSelectorCollection().get(SelectorMethod.CSS))
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat text').getSelectorCollection().get(SelectorMethod.CSS))
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat Value').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value.contains("Beat CPA"))
						{
							assert WebUI.verifyElementPresent(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat selected'), 1) == true : "'Maximize conversions' option is not selected"
							assert WebUI.verifyElementPresent(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat value'), 1) == true : "Beat CPA value is not present. Is 'Maximize conversions' really selected ?"
							WebElement bidStrategyValueElement = WebUiCommonHelper.findWebElement(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat Value'), 3)
							String bidStrategyValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyValueElement))
							assert WebUI.verifyEqual(bidStrategyValue, value.replaceAll(bidStrategyBeatValueRegex, '')) == true : "Bid strategy Beat CPA value does not match"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat Checked').getSelectorCollection().get(SelectorMethod.CSS))
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat text').getSelectorCollection().get(SelectorMethod.CSS))
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Type Beat Value').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value.contains("Optimize"))
						{
							assert WebUI.verifyElementPresent(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Optimize vCPM selected'), 1) == true : "'Maximize viewable impressions' option  is not selected"
							assert WebUI.verifyElementPresent(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Optimize vCPM value'), 1) == true : "Optimize vCPM value is not present. Is 'Maximize viewable impressions' really selected ?"
							WebElement bidStrategyValueElement = WebUiCommonHelper.findWebElement(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Optimize vCPM value'), 3)
							String bidStrategyValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyValueElement))
							assert WebUI.verifyEqual(bidStrategyValue, value.replaceAll(bidStrategyOptimizeValueRegex, '')) == true : "Bid strategy Optimize vCPM value does not match"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Type Optimize vCPM selected').getSelectorCollection().get(SelectorMethod.CSS))
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Type Optimize vCPM value').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value.contains("Fixed"))
						{
							assert WebUI.verifyElementPresent(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed Checked'), 1) == true : "'Fixed bid' checkbox is not checked"
							WebElement bidStrategyValueElement = WebUiCommonHelper.findWebElement(findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed value'), 3)
							String bidStrategyValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyValueElement))
							assert WebUI.verifyEqual(bidStrategyValue, value.replaceAll(bidStrategyFixedValueRegex, '')) == true : "Bid strategy fixed value does not match"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed Checked').getSelectorCollection().get(SelectorMethod.CSS))
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Type Fixed value').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value.contains("Limit"))
						{
							assert WebUI.verifyElementPresent(findTestObject('10. DV360/Line Item fields/Bid Strategy Limit Checked'), 1) == true : "'Do not exceed average' checkbox is not checked"
							WebElement bidStrategyLimitValueElement = WebUiCommonHelper.findWebElement(findTestObject('10. DV360/Line Item fields/Bid Strategy Limit value'), 3)
							String bidStrategyLimitValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyLimitValueElement))
							assert WebUI.verifyEqual(bidStrategyLimitValue, value.replaceAll(bidLimitRegex, '')) == true : "Bid strategy limit does not match"
							WebUI.scrollToElement(findTestObject('10. DV360/Budget and Pacing section'), 30)
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Limit Checked').getSelectorCollection().get(SelectorMethod.CSS))
							selenium.highlight('css=' + findTestObject('10. DV360/Line Item fields/Bid Strategy Limit value').getSelectorCollection().get(SelectorMethod.CSS))
						}
						if (value.contains("Same as IO"))
						{
							assert WebUI.verifyElementPresent(findTestObject('Object Repository/10. DV360/Line Item fields/Flight date Same as IO checkbox Checked'), 1) == true : "Same as insertion order checkbox is not checked"
							selenium.highlight('css=' + findTestObject('Object Repository/10. DV360/Line Item fields/Flight date Same as IO checkbox Checked').getSelectorCollection().get(SelectorMethod.CSS))
						}
					}
					Thread.sleep(250)
				} else if (item.get(7) == "Campaign")
				{
				} else if (item.get(7) == "Insertion Order")
				{
				}
			}
		}
	}

	@Keyword
	def DisplayUploadedLine(String[] lines)
	{
		selenium = getSelenium()
		if (lines == null || lines.size() == 0 || lines[0].equals('') == true)
			KeywordUtil.markFailedAndStop('No line specified to verify')
		WebUI.click(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab'))
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Item List/Remove Filter button'), 5) == true

		WebDriver driver = DriverFactory.getWebDriver()

		for (def line in lines)
		{
			int found = 0;

			while(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Item List/Remove Filter button')))) {
				WebUI.click(findTestObject('10. DV360/Item List/Remove Filter button'))
				WebUI.delay(1)
				assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 10) == true
			}
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/OI list'), 10) == true : "OI list not visible"
			WebUI.setText(findTestObject('Object Repository/10. DV360/Item List/Filter input'), line)
			WebUI.sendKeys(findTestObject('10. DV360/Item List/Filter input'), Keys.chord(Keys.ENTER))
			Thread.sleep(500)
			assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Filter loader'), 10)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/OI list'), 10) == true : "OI list not visible"
			WebUI.delay(5)
			int numberOfIOs = selenium.getCssCount(getSelector(findTestObject('Object Repository/10. DV360/Insertion Order/OI list')).replaceFirst("css=", ""))

			for (int i = 3; i <= numberOfIOs+2; i++)
			{
				if (WebUI.getText(changeSelector(findTestObject('Object Repository/10. DV360/Insertion Order/First IO name index 3'), "nth-of-type\\(\\d+\\)", "nth-of-type(" + i + ")")) == line)
				{
					KeywordUtil.logInfo(line + " was uploaded")
					found = 1;
					break;
				}
			}
			if (found == 0)
				KeywordUtil.markFailedAndStop(line + ' was not uploaded')
		}
		WebUI.click(findTestObject('10. DV360/Item List/Remove Filter button'))

	}

	@Keyword
	def VerifyName(String itemType, String correctname)
	{
		/**
		 * this function verify the name
		 *
		 * @param itemType
		 * @param correctname
		 * The name uploaded
		 */

		KeywordUtil.logInfo("Name uploaded is: " + correctname)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/'+itemType+'/Name'), 'value').equals(correctname) == true : "Name are different"
	}
	@Keyword
	def VerifyStatus(String correctstatus)
	{
		/**
		 * this function verify the status
		 *
		 * @param correctstatus
		 * The status uploaded
		 */
		KeywordUtil.logInfo("Status uploaded is: " + correctstatus)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Campaign/Campaign Status'), 'innerText').equals(correctstatus) == true : "Status are different"
	}
	@Keyword
	def VerifyTotalBudget(String correcttotalbudget)
	{
		/**
		 * this function verify the campaign budget
		 *
		 * @param correcttotalbudget
		 * The totalbudget uploaded
		 */
		KeywordUtil.logInfo("TotalBudget uploaded is: " + correcttotalbudget)
		if (correcttotalbudget != "")
		{
			if (Float.valueOf(correcttotalbudget) % 1 != 0)
				correcttotalbudget = correcttotalbudget.replaceAll("0+\$", "")
		}
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Campaign/Campaign TotalBudget'), 'value').equals(correcttotalbudget) == true : "Total budget are different"
	}
	@Keyword
	def VerifyGoal(String correctgoal)
	{
		/**
		 * this function verify the campaign goal
		 *
		 * @param correctgoal
		 * The goal uploaded
		 */
		KeywordUtil.logInfo("Goal uploaded is: " + correctgoal)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Campaign/Campaign Goal'), 'innerText').equals(correctgoal) == true : "Goal are different"
	}
	@Keyword
	def VerifyGoalKPI(String correctgoalkpi)
	{
		/**
		 * this function verify the campaign goalkpi
		 *
		 * @param correctgoalkpi
		 * The goalkpi uploaded
		 */
		if (correctgoalkpi == 'Other')
			correctgoalkpi = 'Other...'
		KeywordUtil.logInfo("GoalKPI uploaded is: " + correctgoalkpi)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Campaign/Campaign GoalKPI'), 'innerText').equals(correctgoalkpi) == true : "GoalKPI are different"
	}
	@Keyword
	def VerifyGoalKPIValue(String correctgoalkpivalue)
	{
		/**
		 * this function verify the campaign goal kpi value
		 *
		 * @param correctgoalkpivalue
		 * The goal kpi value uploaded
		 */
		KeywordUtil.logInfo("GoalKpiValue uploaded is: " + correctgoalkpivalue)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Campaign/Campaign GoalKPIValue'), 'value').equals(correctgoalkpivalue) == true : "GoalKPIValue are different"
	}
	@Keyword
	def VerifyStartDate(String correctstartdate)
	{
		/**
		 * this function verify the campaign start date
		 *
		 * @param correctstartdate
		 * The start date uploaded
		 */

		correctstartdate = DV360FormatDate(correctstartdate)
		KeywordUtil.logInfo("Start date uploaded is: " + correctstartdate)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Campaign/Campaign Start Date'), 'innerText').equals(correctstartdate) == true : "Start date are different"
	}
	@Keyword
	def VerifyEndDate(String correctenddate)
	{
		/**
		 * this function verify the campaign end date
		 *
		 * @param correctenddate
		 * The end date uploaded
		 */

		correctenddate = DV360FormatDate(correctenddate)
		KeywordUtil.logInfo("End date uploaded is: " + correctenddate)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Campaign/Campaign End Date'), 'innerText').equals(correctenddate) == true : "End date are different"
	}
	@Keyword
	def VerifyCurrency(String correctcurrency)
	{
		/**
		 * this function verify the campaign currency
		 *
		 * @param correctcurrency
		 * The currency uploaded
		 */
		KeywordUtil.logInfo("Currency uploaded is: " + correctcurrency)

		assert convertcurrencydv360(WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Campaign/Campaign Currency'), 'innerText')).equals(correctcurrency) == true : "Currency are different"
	}

	@Keyword
	def VerifyBudget(ArrayList<String> correctbudget)
	{
		/**
		 *  this function verify the budget
		 *
		 *  @param correctbudget
		 *  An array of all the budget interval with their value, their start date and their end date for each budget
		 *
		 */

		for (def elem in correctbudget)
			KeywordUtil.logInfo(elem)

		WebDriver driver = DriverFactory.getWebDriver()
		ArrayList<WebElement> budgets = driver.findElements(By.cssSelector(findTestObject('Object Repository/10. DV360/Insertion Order/All Budgets').getSelectorCollection().get(SelectorMethod.CSS)))

		if (budgets.size() == 0)
			KeywordUtil.markFailedAndStop('No budget interval uploaded')

		for (int j = 1; j < budgets.size() || j == 1; j++)
		{
			int i = 1;
			for (; i < 4; i++)
			{
				if (i == 1)
				{
					this.tmp = this.changeSelector(findTestObject('Object Repository/10. DV360/Insertion Order/dv budget'), "div:nth-child\\(0\\)", "div:nth-child("+ j +")")
					this.tmp = this.changeSelector(this.tmp, "material-input:nth-child\\(0\\) input", "material-input:nth-child("+ i +") input")
				}
				else if (i == 2)
					this.tmp = this.changeSelector(findTestObject('Object Repository/10. DV360/Insertion Order/dv budget start date'), "div:nth-child\\(0\\)", "div:nth-child("+ j +")")
				else if (i == 3)
					this.tmp = this.changeSelector(findTestObject('Object Repository/10. DV360/Insertion Order/dv budget end date'), "div:nth-child\\(0\\)", "div:nth-child("+ j +")")

				String result = ''
				if (i == 1)
					result = WebUI.getAttribute(this.tmp, 'value')
				else
				{
					result = WebUI.getAttribute(this.tmp, 'innerText')
					String[] resultsplitted = result.split(',')
					String[] resultsplittedtwice = resultsplitted[0].split(' ')
					result = String.format("%02d", Integer.valueOf(resultsplittedtwice[1])) + ' ' + resultsplittedtwice[0] + resultsplitted[1]
				}
				assert correctbudget[(j - 1) * 3 + (i - 1)].equals(result) == true : "Budget are different " + correctbudget[(j - 1) * 3 + (i - 1)] + " =/= " + result
			}
		}
	}
	@Keyword
	def VerifyPacing(String correctpacing)
	{
		/**
		 * this function verify the pacing
		 *
		 * @param correctpacing
		 * The pacing uploaded
		 */

		KeywordUtil.logInfo("Pacing uploaded is: " + correctpacing)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/Pacing'), 'innerText').equals(correctpacing) == true : "Pacing are different"
	}
	@Keyword
	def VerifyPacingRate(String correctpacingrate)
	{
		/**
		 * this function verify the pacing rate
		 *
		 * @param correctpacingrate
		 * The pacing rate uploaded
		 */
		KeywordUtil.logInfo("Pacing Rate uploaded is: " + correctpacingrate)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/PacingRate'), 'innerText').equals(correctpacingrate) == true : "PacingRate are different"
	}
	@Keyword
	def VerifyPacingAmount(String correctpacingamount)
	{
		/**
		 * this function verify the pacing amount
		 *
		 * @param correctpacingamount
		 * The pacing amount uploaded
		 */

		KeywordUtil.logInfo("Pacing Amount uploaded is: " + correctpacingamount)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/PacingAmount'), 'value').equals(correctpacingamount) == true : "PacingAmount are different"
	}
	@Keyword
	def VerifyPerformanceGoalType(String correctperformancegoaltype)
	{
		/**
		 * this function verify the performance goal type
		 *
		 * @param correctperformancegoaltype
		 * The performance goal type uploaded
		 */
		KeywordUtil.logInfo("Performance Goal Type uploaded is: " + correctperformancegoaltype)
		if(correctperformancegoaltype == "% Viewability" || correctperformancegoaltype == "Viewability %") correctperformancegoaltype = "Viewability %"
		else if(correctperformancegoaltype == "Other" || correctperformancegoaltype == "Other...") correctperformancegoaltype = "Other..."
		else correctperformancegoaltype = "(" + correctperformancegoaltype + ")"
		String displayedPerformanceGoalType = WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/PerformanceGoalType'), 'innerText')
		assert displayedPerformanceGoalType.contains(correctperformancegoaltype) == true : "Performance Goal Type are different : Expected to contain : " + correctperformancegoaltype + ", Displayed : " + displayedPerformanceGoalType
	}
	@Keyword
	def VerifyPerformanceValue(String correctperformancegoalvalue, String performanceGoalType)
	{
		/**
		 * this function verify the performance goal value
		 *
		 * @param correctperformancegoalvalue
		 * The performance goal value uploaded
		 */

		KeywordUtil.logInfo("Performance goal value uploaded is: " + correctperformancegoalvalue)
		if (correctperformancegoalvalue != '' && Double.parseDouble(correctperformancegoalvalue) % 1 == 0)
		{
			int truncated = correctperformancegoalvalue as int
			KeywordUtil.logInfo('truncated = ' + truncated)
			assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/PerformanceGoalValue'), 'value').equals(truncated.toString()) == true : "Performance Goal integer Value are different"
		}
		else if (performanceGoalType == 'Other...')
			assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/PerformanceGoalValue'), 'value').equals(correctperformancegoalvalue) == true : "Performance Goal other Value are different"
		else if (Double.parseDouble(correctperformancegoalvalue) % 1 != 0 && performanceGoalType != 'Other')
		{
			KeywordUtil.logInfo("Performance goal value dv360 formatted is: " + Double.parseDouble(correctperformancegoalvalue).round(2))
			assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/PerformanceGoalValue'), 'value').equals(Double.parseDouble(correctperformancegoalvalue).round(2).toString()) == true : "Performance Goal Value are different"
		}
	}
	@Keyword
	def VerifyFrequency(boolean isfrequencyactive)
	{
		/**
		 * this function verify the frequency
		 *
		 * @param isfrequencyactive
		 * The frequency uploaded
		 */

		if(isfrequencyactive)
			assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/Frequency'), 'class').contains('checked') == true : "Frequency is not activated"
		else if (!isfrequencyactive)
			assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/Frequency'), 'class').contains('checked') == false : "Frequency is activated"
		else
			KeywordUtil.markErrorAndStop("The expected frequency is not set in parameter.")
	}
	@Keyword
	def VerifyFrequencyExposure(String correctfrequencyexposure)
	{
		/**
		 * this function verify the FrequencyExposure
		 *
		 * @param correctfrequencyexposure
		 * The frequency exposure uploaded
		 */
		KeywordUtil.logInfo("Frequency exposure uploaded is: " + correctfrequencyexposure)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/FrequencyExposure'), 'value').equals(correctfrequencyexposure) == true : "Frequency exposure are different"
	}
	@Keyword
	def VerifyPeriod(String correctperiod)
	{
		/**
		 * this function verify the Period
		 *
		 * @param correctperiod
		 * The period uploaded
		 */
		KeywordUtil.logInfo("Period uploaded is: " + correctperiod)
		assert WebUI.getAttribute(findTestObject('Object Repository/10. DV360/Insertion Order/Period'), 'innerText').equals(correctperiod) == true : "Period are different"
	}

	@Keyword
	def checkTargetingOnDv(String line_test, LinkedList<TargetObject> results)
	{
		/**
		 * Check include/exclude items on DV for a specific targeting
		 *
		 * @param line_test
		 * 		Line item or Insertion Order
		 * @param results
		 * 		List of TargetObject, corresponding to items that will be edited and stored in this list
		 *
		 */

		KeywordUtil.logInfo("Checking Includes/excludes on DV")

		if ((GlobalVariable.targetting == 'Audience') || ((GlobalVariable.targetting == '') && !(GlobalVariable.doNotTest.contains('Audience'))))
			checkIncludedAudiencesOnDV360(line_test, GlobalVariable.type[0], results)
		if ((GlobalVariable.targetting == 'Affinity & In Market') || ((GlobalVariable.targetting == '') && !(GlobalVariable.doNotTest.contains('Affinity & In Market'))))
			checkAffinityAndInMarketOnDV360(line_test, GlobalVariable.type[0], results)
		if ((GlobalVariable.targetting == 'Inventory Source') || ((GlobalVariable.targetting == '') && !(GlobalVariable.doNotTest.contains('Inventory Source'))))
			checkInventorySourceOnDV360(line_test, GlobalVariable.type[0], results)
		if ((GlobalVariable.targetting == "Geography") || ((GlobalVariable.targetting == '') && !(GlobalVariable.doNotTest.contains("Geography"))))
			checkGeographyOnDV360(line_test, GlobalVariable.type[0], results)
		if ((GlobalVariable.targetting == 'Keyword') || ((GlobalVariable.targetting == '') && !(GlobalVariable.doNotTest.contains('Keyword'))))
			checkKeywordsOnDV360(line_test, GlobalVariable.type[0], results)
		if ((GlobalVariable.targetting == 'Custom Affinity') || ((GlobalVariable.targetting == '') && !(GlobalVariable.doNotTest.contains('Custom Affinity'))))
			checkCustomAffinityOnDV360(line_test, GlobalVariable.type[0], results)
		if ((GlobalVariable.targetting == 'Category') || ((GlobalVariable.targetting == '') && !(GlobalVariable.doNotTest.contains('Category'))))
			checkCategoryOnDV360(line_test, GlobalVariable.type[0], results)
		if ((GlobalVariable.targetting == 'Conversion Pixel') || ((GlobalVariable.targetting == '') && !(GlobalVariable.doNotTest.contains('Conversion Pixel'))))
			checkConversionPixelOnDV360(line_test, GlobalVariable.type[0], results)
	}
	def checkAffinityAndInMarketOnDV360 (String itemId, String type, LinkedList<TargetObject> change)
	{
		/**
		 * Check that affinities and in-markets on DV360 match the array passed in parameters
		 *
		 * @param itemId : DV360 Item to search (ID or name)
		 * @param type : Type of edition to check : "Exclude" or "Include"
		 * @param change : List of TargetObject, corresponding to items that were be edited and stored in this list
		 */

		if(type == "")
		{
			checkAffinityAndInMarketOnDV360(itemId, "Include", change)
			checkAffinityAndInMarketOnDV360(itemId, "Exclude", change)
			return true
		}

		KeywordUtil.logInfo("Checking Affinity & In-Market targeting " + type + "d")

		selenium = getSelenium()

		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/DV360 search button'))) == false) logInToDv360(true)
		dv360SearchItem(itemId)
		WebUI.delay(1)
		assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 30) == true

		// La recherche d'une Insertion Order n'accède pas directement à la bonne page
		boolean isInsertionOrder = selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')))
		if(isInsertionOrder == true) {
			WebUI.executeJavaScript("document.querySelector('" + getOnlySelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')) + "').click()", null)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/name'), 60) == true : "OI tab not accessible before timeout"
		}

		// On ferme la fenêtre de sondage si elle est présente
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Survey close button'))))
			WebUI.click(findTestObject('10. DV360/Survey close button'))

		// Vérification des affinity et in-market dans le menu édition
		WebUI.executeJavaScript("document.querySelector('"+getSelector(findTestObject('Object Repository/10. DV360/Pagination/Page scroll')).replaceFirst("css=","")+"').scrollTo(0,0)", null)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/Audience List edit button'), 30) == true
		while(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Edit menu panel')))) {
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/Audience List edit button'))
			WebUI.delay(1)
		}
		// Parfois les affinity et in-market sont réduites car en grande quantité
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Audience List expand button'))))
			WebUI.click(findTestObject('10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Audience List expand button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Edit menu panel'), 30) == true

		int selectorOffset = 0
		if(selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Segment Group Title'))) == true)
			selectorOffset = 2

		// On ne vérifie que les affinity et in-market
		LinkedList<TargetObject> currentAffinityAndInMarketToCheck = []
		for(def item in change) {
			if(item.target == "Affinity & In Market" && item.type == type)
				currentAffinityAndInMarketToCheck.add(item)
		}

		int currentAudienceInc = 0
		int currentAudienceToCheck = 1
		boolean isAffinityInMarketFound = false
		String regex1 = "nth-child\\(\\d+\\)"
		TestObject audienceID = changeSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/First Affinity or In-Market ID'), 'include', type.toLowerCase())
		TestObject deleteAffinityInMarketbutton = findTestObject('10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Delete Included Affinity and In-Market button')
		if(type == "Exclude") {
			audienceID = changeSelector(audienceID, 'include', 'exclude')
			deleteAffinityInMarketbutton = changeSelector(deleteAffinityInMarketbutton, 'include', 'exclude')
		}

		WebUI.scrollToElement(changeSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Include section'), 'include', type.toLowerCase()), 30)

		for(currentAudienceInc; currentAudienceInc < currentAffinityAndInMarketToCheck.size(); currentAudienceInc++) {
			KeywordUtil.logInfo("Checking current Affinity/In-Market ID : " + currentAffinityAndInMarketToCheck[currentAudienceInc].ID)
			for(currentAudienceToCheck = 1; currentAudienceToCheck <= currentAffinityAndInMarketToCheck.size(); currentAudienceToCheck++) {
				audienceID = changeSelector(audienceID, regex1, "nth-child\\("+ (currentAudienceToCheck+1) +")")
				KeywordUtil.logInfo("Comparing to current Affinity/In-Market ID displayed : " + WebUI.getText(audienceID))
				if(WebUI.getText(audienceID) == currentAffinityAndInMarketToCheck[currentAudienceInc].ID) {
					KeywordUtil.logInfo("Affinity/In-Market n°" + currentAffinityAndInMarketToCheck[currentAudienceInc].ID + " was found")
					isAffinityInMarketFound = true
					break;
				}
			}
			assert isAffinityInMarketFound == true : "Affinity/In-Market n°" + currentAffinityAndInMarketToCheck[currentAudienceInc].ID + " was not found"
			isAffinityInMarketFound = false
		}

		KeywordUtil.markPassed("Affinities/In-Markets are correctly " + type.toLowerCase() + "d")


		WebUI.click(findTestObject('10. DV360/Line Item fields/0. Common/1. Edit menu/Edit Menu Apply button'))
		WebUI.delay(1)
		while(selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/Dismiss button'))) == true)
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/Dismiss button'))
	}
	def checkInventorySourceOnDV360 (String itemId, String type, LinkedList<TargetObject> change) {
		/**
		 * Check that inventories on DV360 match the array passed in parameters
		 *
		 * @param itemId : DV360 Item to search (ID or name)
		 * @param type : Type of edition to check : "Exclude" or "Include"
		 * @param change : List of TargetObject, corresponding to items that were be edited and stored in this list
		 */

		if(type == "") {
			checkInventorySourceOnDV360(itemId, "Include", change)
			checkInventorySourceOnDV360(itemId, "Exclude", change)
			return true
		}

		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/DV360 search button'))) == false) logInToDv360(true)
		dv360SearchItem(itemId)
		WebUI.delay(1)
		assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 30) == true

		// La recherche d'une Insertion Order n'accède pas directement à la bonne page
		boolean isInsertionOrder = selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')))
		if(isInsertionOrder == true) {
			WebUI.executeJavaScript("document.querySelector('" + getOnlySelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')) + "').click()", null)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/name'), 60) == true : "OI tab not accessible before timeout"
		}

		// Vérification des inventory dans le menu édition
		// On ne vérifie que les inventory
		LinkedList<TargetObject> currentExchangeToCheck = []
		LinkedList<TargetObject> currentPrivateDealsToCheck = []
		for(def item in change) {
			if(item.target == "Inventory Source" && item.subTarget == "Exchange" && item.type == type)
				currentExchangeToCheck.add(item)
		}
		for(def item2 in change) {
			if(item2.target == "Inventory Source" && item2.subTarget == "Private Deal" && item2.type == type)
				currentPrivateDealsToCheck.add(item2)
		}

		int currentExchangeInc = 0
		int currentPrivateDealInc = 0
		boolean isInventoryFound = false

		WebUI.executeJavaScript("document.querySelector('"+getSelector(findTestObject('Object Repository/10. DV360/Pagination/Page scroll')).replaceFirst("css=","")+"').scrollTo(0,0)", null)
		WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Edit Public Inventories button'), 30)

		while(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter button'))) == false) {
			WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Edit Public Inventories button'))
			WebUI.delay(1)
		}

		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter button'), 30)
		WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter button'))
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter input'), 30)

		// Check Exchanges
		for(currentExchangeInc; currentExchangeInc < currentExchangeToCheck.size(); currentExchangeInc++) {
			WebUI.sendKeys(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter input'), currentExchangeToCheck[currentExchangeInc].ID)
			WebUI.sendKeys(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter input'), Keys.chord(Keys.ENTER))
			WebUI.delay(1)

			boolean isExchangeChecked = selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/First Public Inventory checked')))
			if(currentExchangeToCheck[currentExchangeInc].type == "Include")
				assert isExchangeChecked == true : currentExchangeToCheck[currentExchangeInc].subTarget + " n°" + currentExchangeToCheck[currentExchangeInc].ID + " was not found or is not included"
			else if(currentExchangeToCheck[currentExchangeInc].type == "Exclude")
				assert isExchangeChecked == false : currentExchangeToCheck[currentExchangeInc].subTarget + " n°" + currentExchangeToCheck[currentExchangeInc].ID + " was not found or is included"

			KeywordUtil.logInfo("Inventory " + currentExchangeToCheck[currentExchangeInc].ID + " was found")

			WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter Delete button'))
			WebUI.delay(2)
		}

		// On coche tous les exchanges à la fin du test pour qu'ils soient disponibles dans builder (via remove include) la prochaine fois
		if(type == "Exclude" || GlobalVariable.type[0] == "Include") {
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/1. Inventory source/Public Inventory/Select All checkbox'))
			WebUI.delay(1)
			WebUI.click(findTestObject('10. DV360/Line Item fields/0. Common/1. Edit menu/Edit Menu Apply button'))
		} else {
			WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Close Editor button'))
		}

		WebUI.delay(2)
		while(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals Filter input'))) == false) {
			WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals button'))
			WebUI.delay(2)
		}

		// Check Private Deals
		for(currentPrivateDealInc; currentPrivateDealInc < currentPrivateDealsToCheck.size(); currentPrivateDealInc++) {
			WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals Filter input'), 30)

			WebUI.sendKeys(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals Filter input'), currentPrivateDealsToCheck[currentPrivateDealInc].ID)
			WebUI.sendKeys(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals Filter input'), Keys.chord(Keys.ENTER))
			WebUI.delay(1)
			assert WebUI.waitForElementNotPresent(findTestObject('10. DV360/Filter loader'), 30)

			boolean isPrivateDealChecked = selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals first row selected')))
			if(currentPrivateDealsToCheck[currentPrivateDealInc].type == "Include")
				assert isPrivateDealChecked == true : currentPrivateDealsToCheck[currentPrivateDealInc].subTarget + " " + currentPrivateDealsToCheck[currentPrivateDealInc].ID + " was not found or is not included"
			else if(currentPrivateDealsToCheck[currentPrivateDealInc].type == "Exclude")
				assert isPrivateDealChecked == false : currentPrivateDealsToCheck[currentPrivateDealInc].subTarget + " " + currentPrivateDealsToCheck[currentPrivateDealInc].ID + " was not found or is included"

			KeywordUtil.logInfo("Inventory " + currentPrivateDealsToCheck[currentPrivateDealsToCheck].ID + " was found")
		}

		// On supprime les Private Deals le temps que le bug DVS-295 soit corrigé
		if(type == "Exclude"|| GlobalVariable.type[0] == "Include") {
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals Selected button'))
			WebUI.delay(1)
			assert WebUI.waitForElementNotPresent(findTestObject('10. DV360/Filter loader'), 30)
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals Select All checkbox'))
			WebUI.click(findTestObject('10. DV360/Line Item fields/0. Common/1. Edit menu/Edit Menu Apply button'))
			WebUI.delay(1)
			WebUI.click(findTestObject('10. DV360/Line Item fields/Save button'))
			WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Changes saved message'), 30)
			while(selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/Dismiss button'))) == true)
				WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/Dismiss button'))
		} else {
			WebUI.click(findTestObject('10. DV360/Line Item fields/0. Common/1. Edit menu/Edit Menu Apply button'))
			WebUI.delay(1)
		}

		KeywordUtil.markPassed("Inventories are correctly " + type.toLowerCase() + "d")
	}
	def checkIncludedAudiencesOnDV360 (String itemId, String type, LinkedList<TargetObject> change) {
		/**
		 * Check that included audiences on DV360 match the array passed in parameters
		 *
		 * @param itemId : DV360 Item to search (ID or name)
		 * @param type : Type of edition : "Exclude" or "Include"
		 * @param change : List of TargetObject, corresponding to items that were be edited and stored in this list
		 */

		if(type == "") {
			checkIncludedAudiencesOnDV360(itemId, "Include", change)
			checkIncludedAudiencesOnDV360(itemId, "Exclude", change)
			return true
		}

		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/DV360 search button'))) == false) logInToDv360(true)
		dv360SearchItem(itemId)
		WebUI.delay(1)
		assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 30) == true

		// La recherche d'une Insertion Order n'accède pas directement à la bonne page
		boolean isInsertionOrder = selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')))
		if(isInsertionOrder == true) {
			WebUI.executeJavaScript("document.querySelector('" + getOnlySelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')) + "').click()", null)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/name'), 60) == true : "OI tab not accessible before timeout"
		}

		// On ferme la fenêtre de sondage si elle est présente
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Survey close button'))))
			WebUI.click(findTestObject('10. DV360/Survey close button'))

		// Vérification des audiences ciblées dans le menu édition
		WebUI.executeJavaScript("document.querySelector('"+getSelector(findTestObject('Object Repository/10. DV360/Pagination/Page scroll')).replaceFirst("css=","")+"').scrollTo(0,0)", null)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/Audience List edit button'), 30) == true
		while(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Edit menu panel')))) {
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/Audience List edit button'))
			WebUI.delay(1)
		}
		// Parfois les audiences sont réduites car en grande quantité
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Audience List expand button'))))
			WebUI.click(findTestObject('10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Audience List expand button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Edit menu panel'), 30) == true

		int selectorOffset = 0
		if(selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Segment Group Title'))) == true)
			selectorOffset = 2

		// On ne vérifie que les audiences
		LinkedList<TargetObject> currentAudiencesToCheck = []
		for(def item in change) {
			if(item.target == "Audience" && item.type == type)
				currentAudiencesToCheck.add(item)
		}

		int currentAudienceInc = 0
		int currentGroupInc = 1
		int currentAudienceToCheck = 1
		boolean isAudienceFound = false
		int[] audienceIncludeGroupsToCheck = GlobalVariable.audienceIncludeGroups
		String[] audienceTabs = ["Audience Advertiser", "1P", "3P"]
		String regex1 = "nth-child\\(\\d+\\)"
		String regex2 = "nth-of-type\\(\\d+\\)"
		TestObject audienceID = changeSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/First Audience ID'), 'include', type.toLowerCase())
		TestObject deleteAudiencebutton = findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Delete Included Audience button')
		if(type == "Exclude") {
			audienceID = changeSelector(audienceID, 'audience-segment-group-component > div > div:nth-child\\(3\\)', '')
			deleteAudiencebutton = changeSelector(deleteAudiencebutton, 'include', 'exclude')
			audienceIncludeGroupsToCheck = [currentAudiencesToCheck.size()]
		}

		WebUI.scrollToElement(changeSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Include section'), 'include', type.toLowerCase()), 30)
		for(int audienceNumber in audienceIncludeGroupsToCheck) {
			int storeCurrentAudienceToCheck = audienceNumber
			for(audienceNumber; audienceNumber > 0; audienceNumber--) {
				KeywordUtil.logInfo("Current group : " + currentGroupInc)
				currentAudienceToCheck = storeCurrentAudienceToCheck
				for(currentAudienceToCheck; currentAudienceToCheck > 0; currentAudienceToCheck--) {
					if(type == "Include")
						audienceID = changeSelector(audienceID, regex1, "nth-child\\("+ (currentGroupInc+selectorOffset) +")")
					audienceID = changeSelector(audienceID, regex2, "nth-of-type\\("+ (currentAudienceToCheck+1) +")")
					KeywordUtil.logInfo("Checking current audience ID : " + WebUI.getText(audienceID))
					if(WebUI.getText(audienceID) == currentAudiencesToCheck[currentAudienceInc].ID) {
						KeywordUtil.logInfo("Audience n°" + currentAudiencesToCheck[currentAudienceInc].ID + " was found")
						isAudienceFound = true
						currentAudienceInc++
						break;
					}
				}
				assert isAudienceFound == true : "Audience n°" + currentAudiencesToCheck[currentAudienceInc].ID + " was not found"
				isAudienceFound = false
			}
			currentGroupInc++
		}

		KeywordUtil.markPassed("Audiences are correctly " + type.toLowerCase() + "d")

		// On supprime les audiences le temps que le bug DVS-295 soit corrigé
		while(selenium.isElementPresent(getSelector(deleteAudiencebutton)) == true) {
			WebUI.click(deleteAudiencebutton)
			WebUI.delay(1)
		}

		WebUI.click(findTestObject('10. DV360/Line Item fields/0. Common/1. Edit menu/Edit Menu Apply button'))
		WebUI.delay(1)
		WebUI.click(findTestObject('10. DV360/Line Item fields/Save button'))
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Changes saved message'), 30)
		while(selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/Dismiss button'))) == true)
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/Dismiss button'))
	}
	def checkKeywordsOnDV360(String itemId, String type, LinkedList<TargetObject> change) {
		/**
		 * Check that keywords on DV360 match the array passed in parameters
		 *
		 * @param itemId : DV360 Item to search (ID or name)
		 * @param type : Type of edition to check : "Exclude" or "Include"
		 * @param change : List of TargetObject, corresponding to items that were be edited and stored in this list
		 */

		if(type == "") {
			checkKeywordsOnDV360(itemId, "Include", change)
			checkKeywordsOnDV360(itemId, "Exclude", change)
			return true
		}

		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/DV360 search button'))) == false) logInToDv360(true)
		dv360SearchItem(itemId)
		WebUI.delay(1)
		assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 30) == true

		// La recherche d'une Insertion Order n'accède pas directement à la bonne page
		boolean isInsertionOrder = selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')))
		if(isInsertionOrder == true) {
			WebUI.executeJavaScript("document.querySelector('" + getOnlySelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')) + "').click()", null)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/name'), 60) == true : "OI tab not accessible before timeout"
		}

		// On ferme la fenêtre de sondage si elle est présente
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Survey close button'))))
			WebUI.click(findTestObject('10. DV360/Survey close button'))

		// On ne vérifie que les keywords
		LinkedList<TargetObject> currentKeywordsToCheck = []
		for(def item in change) {
			if(item.target == "Keyword" && item.type == type)
				currentKeywordsToCheck.add(item)
		}

		// On clique sur le bouton show more si on plus de 5 éléments
		WebUI.executeJavaScript("document.querySelector('"+getSelector(findTestObject('Object Repository/10. DV360/Pagination/Page scroll')).replaceFirst("css=","")+"').scrollTo(0,0)", null)
		if(currentKeywordsToCheck.size() > 5) {
			while(selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/3. Keywords/Show more button')))) {
				// On utilise le click javascript car WebUI va scroller de manière incorrecte et cliquer sur le header de DV
				WebUI.executeJavaScript("document.querySelector('" + findTestObject('Object Repository/10. DV360/Line Item fields/3. Keywords/Show more button').getSelectorCollection().get(SelectorMethod.CSS) + "').click()", null)
				WebUI.delay(1)
			}
		}

		// Vérification des keywords ciblés dans le menu édition
		TestObject keywordName
		if(type == "Include") {
			keywordName = findTestObject('Object Repository/10. DV360/Line Item fields/3. Keywords/First Include Keyword')
		} else if (type == "Exclude") {
			keywordName = findTestObject('Object Repository/10. DV360/Line Item fields/3. Keywords/First Exclude Keyword')
		}
		int currentKeywordInc = 0
		int currentKeywordToCheck = 1
		boolean isKeywordFound = false

		for(currentKeywordInc; currentKeywordInc < currentKeywordsToCheck.size(); currentKeywordInc++) {
			KeywordUtil.logInfo("Checking current keyword name : " + currentKeywordsToCheck[currentKeywordInc].name)
			for(currentKeywordToCheck = 1; currentKeywordToCheck <= currentKeywordsToCheck.size(); currentKeywordToCheck++) {
				keywordName = changeSelector(keywordName, "nth-child\\(\\d\\)", "nth-child\\("+ (currentKeywordToCheck) +")")
				KeywordUtil.logInfo("Comparing to current keyword name displayed : " + WebUI.getText(keywordName))
				if(WebUI.getText(keywordName) == currentKeywordsToCheck[currentKeywordInc].name) {
					KeywordUtil.logInfo("Keyword " + currentKeywordsToCheck[currentKeywordInc].name + " was found")
					isKeywordFound = true
					break;
				}
			}
			assert isKeywordFound == true : "Keyword " + currentKeywordsToCheck[currentKeywordInc].name + " was not found"
			isKeywordFound = false
		}

		KeywordUtil.markPassed("Keywords are correctly " + type.toLowerCase() + "d")
	}
	def checkCategoryOnDV360(String itemId, String type, LinkedList<TargetObject> change) {
		/**
		 * Check that categories on DV360 match the array passed in parameters
		 *
		 * @param itemId : DV360 Item to search (ID or name)
		 * @param type : Type of edition to check : "Exclude" or "Include"
		 * @param change : List of TargetObject, corresponding to items that were be edited and stored in this list
		 */

		if(type == "") {
			checkCategoryOnDV360(itemId, "Include", change)
			checkCategoryOnDV360(itemId, "Exclude", change)
			return true
		}

		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/DV360 search button'))) == false) logInToDv360(true)
		dv360SearchItem(itemId)
		WebUI.delay(1)
		assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 30) == true

		// La recherche d'une Insertion Order n'accède pas directement à la bonne page
		boolean isInsertionOrder = selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')))
		if(isInsertionOrder == true) {
			WebUI.executeJavaScript("document.querySelector('" + getOnlySelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')) + "').click()", null)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/name'), 60) == true : "OI tab not accessible before timeout"
		}

		// On ferme la fenêtre de sondage si elle est présente
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Survey close button'))))
			WebUI.click(findTestObject('10. DV360/Survey close button'))

		// On ne vérifie que les category
		LinkedList<TargetObject> currentCategoriesToCheck = []
		for(def item in change) {
			if(item.target == "Category" && item.type == type)
				currentCategoriesToCheck.add(item)
		}

		// On clique sur le bouton show more si on plus de 5 éléments
		if(currentCategoriesToCheck.size() > 5) {
			while(selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/4. Category/Show more button')))) {
				// On utilise le click javascript car WebUI va scroller de manière incorrecte et cliquer sur le header de DV
				WebUI.executeJavaScript("document.querySelector('" + getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/4. Category/Show more button')) + "').click()", null)
				WebUI.delay(1)
			}
		}

		// Vérification des category ciblés dans le menu édition
		TestObject categoryName
		if(type == "Include") {
			categoryName = findTestObject('10. DV360/Line Item fields/4. Category/First Include Category')
		} else if (type == "Exclude") {
			categoryName = findTestObject('10. DV360/Line Item fields/4. Category/First Exclude Category')
		}

		boolean isCategoryFound = false

		for(int currentCategoryInc = 0; currentCategoryInc < currentCategoriesToCheck.size(); currentCategoryInc++) {
			KeywordUtil.logInfo("Checking current category name : " + currentCategoriesToCheck[currentCategoryInc].name)
			for(int currentCategoryToCheck = 1; currentCategoryToCheck <= currentCategoriesToCheck.size(); currentCategoryToCheck++) {
				categoryName = changeSelector(categoryName, "nth-child\\(\\d\\)", "nth-child\\("+ (currentCategoryToCheck) +")")
				KeywordUtil.logInfo("Comparing to current category name displayed : " + WebUI.getText(categoryName))
				if(WebUI.getText(categoryName) =~ currentCategoriesToCheck[currentCategoryInc].name + "\$") {
					KeywordUtil.logInfo("Category " + currentCategoriesToCheck[currentCategoryInc].name + " was found")
					isCategoryFound = true
					break;
				}
			}
			assert isCategoryFound == true : "Category " + currentCategoriesToCheck[currentCategoryInc].name + " was not found"
			isCategoryFound = false
		}

		KeywordUtil.markPassed("Category are correctly " + type.toLowerCase() + "d")
	}
	def checkConversionPixelOnDV360 (String itemId, String type, LinkedList<TargetObject> change) {
		/**
		 * Check that included pixel on DV360 match the array passed in parameters
		 *
		 * @param itemId : DV360 Item to search (ID or name)
		 * @param type : Type of edition : "Exclude" or "Include"
		 * @param change : List of TargetObject, corresponding to items that were be edited and stored in this list
		 */

		if(type == "")
			type = "Include"

		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/DV360 search button'))) == false) logInToDv360(true)
		dv360SearchItem(itemId)
		WebUI.delay(1)
		assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 30) == true

		// La recherche d'une Insertion Order n'accède pas directement à la bonne page
		boolean isInsertionOrder = selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')))
		if(isInsertionOrder == true) {
			WebUI.executeJavaScript("document.querySelector('" + getOnlySelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')) + "').click()", null)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/name'), 60) == true : "OI tab not accessible before timeout"
		}

		// On ferme la fenêtre de sondage si elle est présente
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Survey close button'))))
			WebUI.click(findTestObject('10. DV360/Survey close button'))

		// On ne vérifie que les audiences
		LinkedList<TargetObject> currentConvPixelToCheck = []
		for(def item in change) {
			if(item.target == "Conversion Pixel" && item.type == type)
				currentConvPixelToCheck.add(item)
		}
		KeywordUtil.logInfo('size current conv pixel = ' + currentConvPixelToCheck.size())

		List<WebElement> IncludedConversion = driver.findElements(By.cssSelector(findTestObject('Object Repository/10. DV360/Line Item fields/5. Conversion Pixel/Pixel ID list').getSelectorCollection().get(SelectorMethod.CSS)))
		KeywordUtil.logInfo('size ' + type + 'd conv pixel = ' + IncludedConversion.size())
		for (int dest = 0; dest < currentConvPixelToCheck.size(); dest++)
		{
			int found = 0
			int i = 0
			for (; i < IncludedConversion.size(); i++)
			{
				KeywordUtil.logInfo('elem en cours= ' + IncludedConversion[i].getText())
				KeywordUtil.logInfo('elem en cours formatté = ' + IncludedConversion[i].getText().trim())
				KeywordUtil.logInfo('elem destination= ' + currentConvPixelToCheck[dest].ID)
				if (IncludedConversion[i].getText().trim() == currentConvPixelToCheck[dest].ID)
				{
					KeywordUtil.logInfo('element found = ' + currentConvPixelToCheck[dest].ID)
					found = 1
					break
				}
			}
			if (found == 0)
			{
				KeywordUtil.markFailedAndStop('Conversion Pixel ' + currentConvPixelToCheck[dest].ID + ' not found on dv')
			}
		}

		KeywordUtil.markPassed("Conversion Pixel are correctly " + type.toLowerCase() + 'd')
	}
	def checkCustomAffinityOnDV360 (String itemId, String type, LinkedList<TargetObject> change) {
		/**
		 * Check that included audiences on DV360 match the array passed in parameters
		 *
		 * @param itemId : DV360 Item to search (ID or name)
		 * @param type : Type of edition : "Exclude" or "Include"
		 * @param change : List of TargetObject, corresponding to items that were be edited and stored in this list
		 */

		if(type == "")
			type = "Include"

		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/DV360 search button'))) == false) logInToDv360(true)
		dv360SearchItem(itemId)
		WebUI.delay(1)
		assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 30) == true

		// La recherche d'une Insertion Order n'accède pas directement à la bonne page
		boolean isInsertionOrder = selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')))
		if(isInsertionOrder == true) {
			WebUI.executeJavaScript("document.querySelector('" + getOnlySelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')) + "').click()", null)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/name'), 60) == true : "OI tab not accessible before timeout"
		}

		// On ferme la fenêtre de sondage si elle est présente
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Survey close button'))))
			WebUI.click(findTestObject('10. DV360/Survey close button'))

		// Vérification des audiences ciblées dans le menu édition
		WebUI.executeJavaScript("document.querySelector('"+getSelector(findTestObject('Object Repository/10. DV360/Pagination/Page scroll')).replaceFirst("css=","")+"').scrollTo(0,0)", null)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/Audience List edit button'), 30) == true
		while(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Edit menu panel')))) {
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/Audience List edit button'))
			WebUI.delay(1)
		}
		// Parfois les audiences sont réduites car en grande quantité
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Audience List expand button'))))
			WebUI.click(findTestObject('10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Audience List expand button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Edit menu panel'), 30) == true

		int selectorOffset = 0
		if(selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/1. Edit menu/Segment Group Title'))) == true)
			selectorOffset = 2

		// On ne vérifie que les audiences
		LinkedList<TargetObject> currentCustomAffinityToCheck = []
		for(def item in change) {
			if(item.target == "Custom Affinity" && item.type == type)
				currentCustomAffinityToCheck.add(item)
		}
		KeywordUtil.logInfo('size current custom affi = ' + currentCustomAffinityToCheck.size())

		List<WebElement> IncludedAffinities = driver.findElements(By.cssSelector(findTestObject('Object Repository/10. DV360/Line Item fields/2. Audience Lists/Custom Affinity included').getSelectorCollection().get(SelectorMethod.CSS)))
		KeywordUtil.logInfo('size ' + type + 'd custom affi = ' + IncludedAffinities.size())
		for (int dest = 0; dest < currentCustomAffinityToCheck.size(); dest++)
		{
			int found = 0
			int i = 0
			for (; i < IncludedAffinities.size(); i++)
			{
				KeywordUtil.logInfo('elem en cours= ' + IncludedAffinities[i].getText())
				KeywordUtil.logInfo('elem en cours formatté = ' + IncludedAffinities[i].getText().trim())
				KeywordUtil.logInfo('elem destination= ' + currentCustomAffinityToCheck[dest].name)
				if (IncludedAffinities[i].getText().trim() == currentCustomAffinityToCheck[dest].name)
				{
					KeywordUtil.logInfo('element found = ' + currentCustomAffinityToCheck[dest].name)
					found = 1
					break
				}
			}
			if (found == 0)
			{
				KeywordUtil.markFailedAndStop('Custom Affinity ' + currentCustomAffinityToCheck[dest].name + ' not found on dv')
			}
		}

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Targeting/Audience List Cancel button'), 30) == true
		WebUI.click(findTestObject('Object Repository/10. DV360/Targeting/Audience List Cancel button'))


		KeywordUtil.markPassed("Custom Affinity are correctly " + type.toLowerCase() + 'd')

	}
	def checkGeographyOnDV360(String itemId, String type, LinkedList<TargetObject> change) {
		/**
		 * Check Geography on DV360 match the array passed in parameters
		 *
		 * @param itemId : DV360 Item to search (ID or name)
		 * @param type : Type of edition to check : "Exclude" or "Include" or both
		 * @param change : List of TargetObject, corresponding to items that were be edited and stored in this list
		 */

		if(type == "") {
			checkGeographyOnDV360(itemId, "Include", change)
			checkGeographyOnDV360(itemId, "Exclude", change)
			return true
		}

		KeywordUtil.logInfo("Checking Geography targeting " + type + "d")

		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/DV360 search button'))) == false) logInToDv360(true)
		dv360SearchItem(itemId)
		WebUI.delay(1)
		assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 30) == true

		// La recherche d'une Insertion Order n'accède pas directement à la bonne page
		boolean isInsertionOrder = selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')))
		if(isInsertionOrder == true) {
			WebUI.click(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab'))
			WebUI.verifyElementPresent(findTestObject('10. DV360/Loading bar'), 3)
			assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 30) == true
		}

		// On ferme la fenêtre de sondage si elle est présente
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Survey close button'))))
			WebUI.click(findTestObject('10. DV360/Survey close button'))

		// On ne vérifie que les geographies
		LinkedList<TargetObject> currentGeographyToCheck = []
		ArrayList<String> itemFromDV= new ArrayList()

		for(def item in change) {
			if(item.target == "Geography" && item.type == type)
				currentGeographyToCheck.add(item.name)
		}

		ArrayList<String> currentGeographyToCheckList = new ArrayList<String>(currentGeographyToCheck)
		ArrayList<WebElement> Excluderegion = driver.findElements(By.cssSelector(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/Exclude region').getSelectorCollection().get(SelectorMethod.CSS)))
		ArrayList<WebElement> Includeregion = driver.findElements(By.cssSelector(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/Include region').getSelectorCollection().get(SelectorMethod.CSS)))

		int numberOfIncludesOnDv = Includeregion.size
		int numberOfExcludesOnDv = Excluderegion.size
		boolean flag = false

		if (type == "Include") {
			if (WebUI.waitForElementPresent(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/Include/Show more button'), 2)){
				// On utilise le click javascript car WebUI va scroller de manière incorrecte et cliquer sur le header de DV
				WebUI.executeJavaScript("document.querySelector('" + findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/Include/Show more button').getSelectorCollection().get(SelectorMethod.CSS) + "').click()", null)
				WebUI.delay(1)
			}
			for(int i=1; i<numberOfIncludesOnDv+1; i++){
				//assert numberOfIncludesOnDv.equals(change.size) : "Included items are failed. " + change.size + " included are expected"
				itemFromDV.add(WebUI.getText(changeSelector(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/Include region'),"div > div","div > div:nth-child("+(i)+")")).trim())
			}
			for(int k=0; k<numberOfIncludesOnDv; k++){
				for(int l=0; l<numberOfIncludesOnDv; l++){
					if (itemFromDV[k].contains(currentGeographyToCheckList[l]))
					{
						flag = true
						KeywordUtil.markPassed(currentGeographyToCheck[l]+" is included in DV360")
					} else if (l==numberOfIncludesOnDv-1 && flag == false){
						KeywordUtil.markFailed(currentGeographyToCheck[l]+" is not included in DV360")
					}
				}
			}
		} else if (type == "Exclude"){
			if (WebUI.waitForElementPresent(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/Exclude/Show more button'), 2)){
				// On utilise le click javascript car WebUI va scroller de manière incorrecte et cliquer sur le header de DV
				WebUI.executeJavaScript("document.querySelector('" + findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/Exclude/Show more button').getSelectorCollection().get(SelectorMethod.CSS) + "').click()", null)
				WebUI.delay(1)
			}
			for(int j=1; j<numberOfExcludesOnDv+1; j++){
				itemFromDV.add(WebUI.getText(changeSelector(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/Exclude region'),"div > div","div > div:nth-child("+(j)+")")).trim())
			}
			for(int k=0; k<numberOfExcludesOnDv; k++){
				for(int l=0; l<numberOfExcludesOnDv; l++){
					if (itemFromDV[k].contains(currentGeographyToCheckList[l]))
					{
						flag = true
						KeywordUtil.markPassed(currentGeographyToCheck[l]+" is Excluded in DV360")
					} else if (l==numberOfExcludesOnDv-1 && flag == false){
						KeywordUtil.markFailed(currentGeographyToCheck[l]+" is not Excluded in DV360")
					}
				}
			}
		}

		// Reset geography to France to avoid targeting other countries
		if(type == "Exclude"|| GlobalVariable.type[0] == "Include") {
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/Geography edit button'), 30)
			WebUI.executeJavaScript("document.querySelector('"+getSelector(findTestObject('Object Repository/10. DV360/Pagination/Page scroll')).replaceFirst("css=","")+"').scrollTo(0,0)", null)
			int attempts = 0
			while(attempts <= 10 && selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/1. Edit menu/Clear button'))) == false) {
				WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/Geography edit button'))
				WebUI.delay(1)
				attempts++
			}
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/1. Edit menu/Clear button'), 30) : "The Geography edit button could not be clicked"
			WebUI.delay(1)
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/1. Edit menu/Clear button'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/1. Edit menu/Clear Geography Yes button'), 30)
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/1. Edit menu/Clear Geography Yes button'))
			WebUI.setText(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/1. Edit menu/Geography search input'), "France")
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/1. Edit menu/First Suggested Geography Include button'), 30)
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/1. Edit menu/First Suggested Geography Include button'))
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/3. Geography/1. Edit menu/Suggestion Done button'))
			WebUI.delay(1)
			WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/0. Common/1. Edit menu/Edit Menu Apply button'))
			WebUI.delay(1)
			WebUI.click(findTestObject('10. DV360/Line Item fields/Save button'))
			assert WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/Changes saved message'), 30)
			while(selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/Dismiss button'))) == true)
				WebUI.click(findTestObject('Object Repository/10. DV360/Line Item fields/Dismiss button'))
		}
	}

	@Keyword
	int getReadyExchangeIndex(String itemId, List<String> exchangeNames) {
		selenium = getSelenium()

		logInToDv360(true)
		dv360SearchItem(itemId)
		WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Edit Public Inventories button'), 30)

		int exchangeIndex = -1
		while(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter button'))) == false) {
			WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Edit Public Inventories button'))
			WebUI.delay(1)
		}
		WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter button'), 30)
		WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter button'))
		WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter input'), 30)
		int inc = 0
		while (inc < exchangeNames.size() && exchangeIndex == -1) {
			if(!exchangeNames.get(inc).equals("Skip")) {
				WebUI.sendKeys(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter input'), exchangeNames.get(inc))
				WebUI.sendKeys(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter input'), Keys.chord(Keys.ENTER))
				WebUI.waitForElementPresent(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Filters applied'), 2)

				boolean isExchangeMatched = false
				boolean isExchangePresent = selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/First Public Inventory name')))
				if (isExchangePresent == true) {
					isExchangeMatched = WebUI.getText(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/First Public Inventory name')).replaceFirst(" \\(.*\\)\$", "").equals(exchangeNames.get(inc))
				}
				boolean isExchangeChecked = selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/First Public Inventory checked')))
				if(GlobalVariable.type[0].equals("Include")) {
					if(isExchangeMatched == true && isExchangeChecked == true) {

					} else if(isExchangeMatched == true && isExchangeChecked == false) {
						exchangeIndex = exchangeNames.indexOf(exchangeNames.get(inc))
					} else {

					}
				} else if (GlobalVariable.type[0].equals("Exclude")) {
					if(isExchangeMatched == true && isExchangeChecked == true) {
						exchangeIndex = exchangeNames.indexOf(exchangeNames.get(inc))
					} else if(isExchangeMatched == true && isExchangeChecked == false) {

					}
				} else {
					KeywordUtil.markFailedAndStop("Check the global variable 'type' is either 'Include' or 'Exclude'")
				}
				WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter Delete button'))
				Thread.sleep(1250)
			}
			inc++
		}
		WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Close Editor button'))
		WebUI.switchToWindowIndex(0)

		return exchangeIndex
	}
	@Keyword
	int getReadyPrivateDealIndex(String itemId, List<String> privateDealNames) {
		selenium = getSelenium()

		WebUI.switchToWindowIndex(1)
		dv360SearchItem(itemId)
		WebUI.waitForElementVisible(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals button'), 30)

		boolean isPrivateDealExist = false
		boolean isPrivateDealReady = false

		while(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals Filter input'))) == false) {
			WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals button'))
			WebUI.delay(1)
		}

		int privateDealIndex = -1
		int inc = 0
		while (inc < privateDealNames.size() && isPrivateDealReady == false && privateDealIndex == -1) {
			if(!privateDealNames.get(inc).equals("Skip")) {
				WebUI.sendKeys(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals Filter input'), privateDealNames.get(inc))
				WebUI.sendKeys(findTestObject('10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals Filter input'), Keys.chord(Keys.ENTER))
				Thread.sleep(500)
				assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Filter loader'), 20)


				isPrivateDealExist = selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals first row')))
				if(isPrivateDealExist == true) {
					isPrivateDealReady = selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Line Item fields/1. Inventory source/Private Deals/Private Deals first row selected')))
					if((GlobalVariable.type[1].equals("Exclude") && isPrivateDealReady == true) || (GlobalVariable.type[1].equals("Include") && isPrivateDealReady == false)) {
						privateDealIndex =  privateDealNames.indexOf(privateDealNames.get(inc))
					} else if (!GlobalVariable.type[1].equals("Exclude") && !GlobalVariable.type[1].equals("Include")) {
						KeywordUtil.markFailedAndStop("Check the global variable 'type2' is either 'Include' or 'Exclude'")
					}
				}

				WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Public Inventory/Public Inventory Filter Delete button'))
				WebUI.delay(2)
			}
			inc++
		}

		if (privateDealIndex == -1) {
			KeywordUtil.markFailedAndStop("There are no Private Deal to " + GlobalVariable.type[1].toString().toLowerCase())
		}

		WebUI.click(findTestObject('10. DV360/Line Item fields/1. Inventory source/Close Editor button'))

		WebUI.switchToWindowIndex(0)
		return privateDealIndex
	}

	@Keyword
	String convertcurrencydv360(String original)
	{
		/**
		 * this function convert the string from the currency accronym to the symbol or from the currency symbol to the accronym
		 * @param original
		 * The string to be converted
		 * @return String
		 * The string converted
		 */

		KeywordUtil.logInfo('Currency before conversion: ' + original)
		switch(original)
		{
			case "€":
				original = 'EUR'
				break

			case "R\$":
				original = 'BRL'
				break

			case "\$":
				original = 'USD'
				break
			case "£":
				original = 'GBP'
				break
			case "EUR":
				original = '€'
				break
			case "BRL":
				original = 'R\$'
				break
			case "USD":
				original = '\$'
				break
			case "GBP":
				original = '£'
				break
			default:
				KeywordUtil.markFailedAndStop('Currency not recognised: ' + original)
				break
		}

		KeywordUtil.logInfo('Currency after conversion: ' + original)
		return original
	}

	String getAdvertiserCurrencyfromDV360(String Advertisername)
	{
		/**
		 * this function get the currency from dv360
		 * @param Advertisername
		 * 		the name of the advertiser to get the currency
		 * @return String
		 * 		the devise of the currency used
		 */
		this.dv360SearchItem(Advertisername)
		WebUI.waitForElementVisible(findTestObject('10. DV360/Advertiser Settings/Advertiser Settings button'), 30)
		WebUI.click(findTestObject('10. DV360/Advertiser Settings/Advertiser Settings button'))
		WebUI.click(findTestObject('10. DV360/Advertiser Settings/Basic Details button'))
		WebUI.waitForElementVisible(findTestObject('10. DV360/Advertiser Settings/Currency span'), 15)
		String currency = WebUI.getText(findTestObject('10. DV360/Advertiser Settings/Currency span'))

		def currencysplit = currency.split('\\(')
		currencysplit[1] = currencysplit[1].trim().replace(')', '')

		KeywordUtil.logInfo('Advertiser Currency is ' + currencysplit[1])
		return (currencysplit[1])
	}

	String DV360FormatDate(String date)
	{
		/**
		 * this function format a date to the dv360 format
		 *
		 * @param date
		 * The date uploaded
		 */

		String[] datesplit = date.split(' ')
		int day = datesplit[0].toInteger()
		date = datesplit[1] + ' ' + day.toString() + ', ' + datesplit[2]
		return date
	}

	@Keyword
	def archiveObject(ArrayList<String> ObjectID, String type) {
		/**
		 * This function search a list of object name on dv360 to archive it.
		 * Search the object name in the search bar
		 * take the first suggestion
		 * Navigate to the parent page
		 * Remove the active filter
		 * Search object by ID
		 * archive selected object
		 * Verify object is not visible anymore
		 *
		 * It handles Campaign, Insertion Order or Line
		 *
		 * @param ObjectID : the list of ID of object to archive
		 * @param type: the type of the object (CP, OI, LI)
		 */

		selenium = getSelenium()

		for (def elem in ObjectID) {
			if (type == 'LI' && selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Item List/New Line Item button'))) == false) {
				dv360SearchItem(elem)
				WebUI.delay(1)
				assert WebUI.waitForElementVisible(findTestObject('10. DV360/OI link'), 30)== true
				WebUI.click(findTestObject('10. DV360/OI link'))
			} else if (type == 'OI' && selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Item List/New Insertion Order button'))) == false) {
				dv360SearchItem(elem)
				WebUI.delay(1)
				assert WebUI.waitForElementVisible(findTestObject('10. DV360/Overview link'), 30) == true
				WebUI.click(findTestObject('10. DV360/Overview link'))
				WebUI.verifyElementPresent(findTestObject('10. DV360/Insertion Order/Insertion Order tab'), 3)
				assert WebUI.waitForElementVisible(findTestObject('10. DV360/Insertion Order/Insertion Order tab'), 30) == true
				WebUI.click(findTestObject('10. DV360/Insertion Order/Insertion Order tab'))
			} else if (type == 'CP' && selenium.isElementPresent(getSelector(findTestObject('Object Repository/10. DV360/Item List/New Campaign button'))) == false) {
				dv360SearchItem(elem)
				assert WebUI.waitForElementVisible(findTestObject('10. DV360/Campaign link'), 30)== true
				WebUI.click(findTestObject('10. DV360/Campaign link'))
			}
			WebUI.delay(1)
			assert WebUI.waitForElementVisible(findTestObject('10. DV360/Item List/Filter input'), 10) == true
			if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Item List/Remove Filter button')))) {
				WebUI.click(findTestObject('10. DV360/Item List/Remove Filter button'))
				WebUI.delay(1)
				assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 10) == true
			}
			WebUI.scrollToElement(findTestObject('10. DV360/Item List/Filter input'), 20)
			Thread.sleep(500)
			WebUI.setText(findTestObject('10. DV360/Item List/Filter input'), elem)
			WebUI.sendKeys(findTestObject('10. DV360/Item List/Filter input'), Keys.chord(Keys.ENTER))
			if (type == 'CP') {
				Thread.sleep(500)
				assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Filter loader'), 30) == true
			} else {
				assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 10) == true
				WebUI.delay(1)
			}
			assert WebUI.waitForElementVisible(findTestObject('10. DV360/Item List/First Checkbox'), 30) == true : elem + " object was not found in the list"
			WebUI.scrollToElement(findTestObject('10. DV360/Item List/First Checkbox'), 20)
			Thread.sleep(500)
			assert WebUI.waitForElementClickable(findTestObject('10. DV360/Item List/First Checkbox'), 20) == true : "First checkbox is not clickable after 20 sec"
			WebUI.click(findTestObject('10. DV360/Item List/First Checkbox'))

			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Item List/Action Menu button'), 30) == true
			WebUI.click(findTestObject('Object Repository/10. DV360/Item List/Action Menu button'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Item List/Action menu/Archive button'), 30) == true
			WebUI.click(findTestObject('Object Repository/10. DV360/Item List/Action Menu/Archive button'))
			if (type != 'CP') {
				WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Item List/Action Menu/OK, archive button'), 30)
				WebUI.click(findTestObject('Object Repository/10. DV360/Item List/Action Menu/OK, archive button'))
			}
			Thread.sleep(500)
			assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Filter loader'), 60) == true
			assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Item List/List First Element checkbox'), 30) == true
			KeywordUtil.logInfo(type + " " + elem +" has been archived")
		}
		WebUI.refresh()
	}
}