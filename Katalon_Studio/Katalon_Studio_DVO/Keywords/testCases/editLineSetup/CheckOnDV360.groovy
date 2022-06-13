package testCases.editLineSetup

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.OptimizerDV360

public class CheckOnDV360 extends OptimizerDV360 {

	@Keyword
	public void checkSetupValuesOnDV360(ArrayList<ArrayList<String>> linesAndValuesToCheck) {
		driver = DriverFactory.getWebDriver()
		selenium = getSelenium()
		String bidLimitRegex = "Limit "
		String budgetRegex = "Amount "
		String pacingAmountRegex = "Daily "
		String bidStrategyBeatValueRegex = "Beat CP[A|C] "
		String bidStrategyFixedValueRegex = "Fixed "
		String bidStrategyOptimizeValueRegex = "Optimize vCPM "

		for (ArrayList<String> line : linesAndValuesToCheck) {
			WebUI.click(findTestObject('Display Video 360/DV360 search button'))
			WebUI.setText(findTestObject('Display Video 360/input_Search dv360'), line.get(0))
			WebUI.waitForElementVisible(findTestObject('Display Video 360/Search suggestion'), 30)
			WebUI.click(findTestObject('Display Video 360/Search suggestion'), FailureHandling.STOP_ON_FAILURE)
			WebUI.waitForPageLoad(30)
			WebUI.waitForElementVisible(findTestObject('Display Video 360/span_Active'), 30)
			WebUI.delay(3)
			for (String value : line) {
				if (value != line.get(0)) {
					if (value == "Active" || value == "Paused") {
						assert WebUI.verifyElementText(findTestObject('Display Video 360/span_Active'), value) == true
						selenium.highlight('css=' + findTestObject('Display Video 360/span_Active').getSelectorCollection().get(SelectorMethod.CSS))
						Thread.sleep(500)
					}
					if (value.contains("Amount")) {
						assert WebUI.verifyElementPresent(findTestObject('Display Video 360/Line Item fields/Budget Type Amount Checked'), 1) == true
						WebElement budgetTypeAmountValueElement = WebUiCommonHelper.findWebElement(findTestObject('Display Video 360/Line Item fields/Budget Type Amount value'), 3)
						String budgetTypeAmountValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(budgetTypeAmountValueElement))
						assert WebUI.verifyEqual(budgetTypeAmountValue, value.replaceAll(budgetRegex, '')) == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Add Targeting button'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Budget Type Amount Checked').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Budget Type Amount value').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value == "Unlimited") {
						assert WebUI.verifyElementPresent(findTestObject('Display Video 360/Line Item fields/Budget Type Unlimited Checked'), 1) == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Add Targeting button'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Budget Type Unlimited Checked').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value.contains("Daily")) {
						assert WebUI.verifyElementText(findTestObject('Display Video 360/Line Item fields/Pacing text'), 'Daily') == true
						WebElement pacingAmountValueElement = WebUiCommonHelper.findWebElement(findTestObject('Display Video 360/Line Item fields/Pacing Rate value'), 3)
						String pacingAmountValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(pacingAmountValueElement))
						assert WebUI.verifyEqual(pacingAmountValue, value.replaceAll(pacingAmountRegex, '')) == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Add Targeting button'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Pacing text').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Pacing Rate value').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value == "Flight") {
						assert WebUI.verifyElementText(findTestObject('Display Video 360/Line Item fields/Pacing text'), 'Flight') == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Add Targeting button'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Pacing text').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value == "ASAP") {
						assert WebUI.verifyElementText(findTestObject('Display Video 360/Line Item fields/Pacing Rate text'), 'ASAP') == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Add Targeting button'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Pacing Rate text').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value == "Even") {
						assert WebUI.verifyElementText(findTestObject('Display Video 360/Line Item fields/Pacing Rate text'), 'Even') == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Add Targeting button'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Pacing Rate text').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value == "Ahead") {
						assert WebUI.verifyElementText(findTestObject('Display Video 360/Line Item fields/Pacing Rate text'), 'Ahead') == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Add Targeting button'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Pacing Rate text').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value == "Minimize CPC'") {
						assert WebUI.verifyElementPresent(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Minimize Maximize Checked'), 1) == true
						assert WebUI.verifyElementText(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Minimize Maximize text'), 'Minimize CPC') == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Budget and Pacing section'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Minimize Maximize Checked').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Minimize Maximize text').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value == "Minimize CPA'") {
						assert WebUI.verifyElementPresent(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Minimize Maximize Checked'), 1) == true
						assert WebUI.verifyElementText(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Minimize Maximize text'), 'Minimize CPA') == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Budget and Pacing section'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Minimize Maximize Checked').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Minimize Maximize text').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value.contains("Beat CPC")) {
						assert WebUI.verifyElementPresent(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat Checked'), 1) == true
						assert WebUI.verifyElementText(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat text'), 'CPC') == true
						WebElement bidStrategyValueElement = WebUiCommonHelper.findWebElement(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat Value'), 3)
						String bidStrategyValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyValueElement))
						assert WebUI.verifyEqual(bidStrategyValue, value.replaceAll(bidStrategyBeatValueRegex, '')) == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Budget and Pacing section'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat Checked').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat text').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat Value').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value.contains("Beat CPA")) {
						assert WebUI.verifyElementPresent(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat Checked'), 1) == true
						assert WebUI.verifyElementText(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat text'), 'CPA') == true
						WebElement bidStrategyValueElement = WebUiCommonHelper.findWebElement(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat Value'), 3)
						String bidStrategyValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyValueElement))
						assert WebUI.verifyEqual(bidStrategyValue, value.replaceAll(bidStrategyBeatValueRegex, '')) == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Budget and Pacing section'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat Checked').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat text').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Beat Value').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value.contains("Optimize vCPM")) {
						assert WebUI.verifyElementPresent(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Optimize vCPM Checked'), 1) == true
						WebElement bidStrategyValueElement = WebUiCommonHelper.findWebElement(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Optimize vCPM value'), 3)
						String bidStrategyValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyValueElement))
						assert WebUI.verifyEqual(bidStrategyValue, value.replaceAll(bidStrategyOptimizeValueRegex, '')) == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Budget and Pacing section'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Optimize vCPM Checked').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Optimize vCPM value').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value.contains("Fixed")) {
						assert WebUI.verifyElementPresent(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Fixed Checked'), 1) == true
						WebElement bidStrategyValueElement = WebUiCommonHelper.findWebElement(findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Fixed value'),3)
						String bidStrategyValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyValueElement))
						assert WebUI.verifyEqual(bidStrategyValue, value.replaceAll(bidStrategyFixedValueRegex, '')) == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Budget and Pacing section'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Fixed Checked').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Type Fixed value').getSelectorCollection().get(SelectorMethod.CSS))
					}
					if (value.contains("Limit") && !value.contains("Limit 0")) {
						assert WebUI.verifyElementPresent(findTestObject('Display Video 360/Line Item fields/Bid Strategy Limit Checked'), 1) == true
						WebElement bidStrategyLimitValueElement = WebUiCommonHelper.findWebElement(findTestObject('Display Video 360/Line Item fields/Bid Strategy Limit value'), 3)
						String bidStrategyLimitValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyLimitValueElement))
						assert WebUI.verifyEqual(bidStrategyLimitValue, value.replaceAll(bidLimitRegex, '')) == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Budget and Pacing section'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Limit Checked').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Limit value').getSelectorCollection().get(SelectorMethod.CSS))
					} else if (value.contains("Limit 0")) {
						assert WebUI.verifyElementPresent(findTestObject('Display Video 360/Line Item fields/Bid Strategy Limit Unchecked'), 1) == true
						WebElement bidStrategyLimitValueElement = WebUiCommonHelper.findWebElement(findTestObject('Display Video 360/Line Item fields/Bid Strategy Limit value'), 3)
						String bidStrategyLimitValue = WebUI.executeJavaScript('return arguments[0].value', Arrays.asList(bidStrategyLimitValueElement))
						assert WebUI.verifyEqual(bidStrategyLimitValue, value.replaceAll(bidLimitRegex, '')) == true
						WebUI.scrollToElement(findTestObject('Display Video 360/Budget and Pacing section'), 30)
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Limit Unchecked').getSelectorCollection().get(SelectorMethod.CSS))
						selenium.highlight('css=' + findTestObject('Display Video 360/Line Item fields/Bid Strategy Limit value').getSelectorCollection().get(SelectorMethod.CSS))
					}

					KeywordUtil.markPassed("Checked " + value)
				}
				Thread.sleep(250)
			}
			KeywordUtil.markPassed("Finished checking " + line)
			Thread.sleep(250)
		}
		KeywordUtil.markPassed("Finished checking all lines !")
	}
}
