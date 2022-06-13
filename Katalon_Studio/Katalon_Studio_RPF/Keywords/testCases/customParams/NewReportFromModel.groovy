package testCases.customParams

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.Keys

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import reportfolio.Reportfolio

public class NewReportFromModel extends Reportfolio {

	// RPF-84
	@Keyword
	public void newReportFromModel () {
		selenium = getSelenium()
		driver = DriverFactory.getWebDriver()
		String randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))
		WebUI.delay(2)
		String folderName = "New report from model"
		createFolder(folderName)
		String modelName = "Model\u00A0for\u00A0automation\u00A0" + randomNumber
		String templateName = "Template"
		String templateFormat = "xlsx"

		createModel(modelName, "weekly", templateName, templateFormat, "8", "Subreport from model RPF-84", 1, "7", "8", "tab", true)
		WebUI.refresh()
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		// Case 1 - Verify at Reportfolio that any user has access to all created models
		WebUI.click(findTestObject('Object Repository/2. Folders list/New Report button'))
		WebUI.delay(1)
		assert WebUI.verifyElementPresent(findTestObject('Object Repository/2. Folders list/First model button'), 2) == true

		// Case 2 - Verify at Reportfolio, the name of the first model list
		assert WebUI.verifyElementText(findTestObject('Object Repository/2. Folders list/Basic report button'), "Basic report") == true

		WebUI.click(findTestObject('Object Repository/2. Folders list/Overlay backdrop'))

		// Case 3 - Verify at Reportfolio,the behaviour if the user select the "Basic report"
		// Case 4 - Verify at Reporfolio the display of the modal for a new basic report
		createReport("New report without a model", "Test Advertiser")
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/Report name'), "( Test Advertiser ) New report without a model") == true
		assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/3. Report/Model name'), 30) == true

		// Case 5 - Verify at Reporfolio the display of the modal for a new report from model
		createReportFromModel(modelName, "New report from a model", "Test Advertiser 2")
		WebUI.delay(2)

		// Case 6 - Verify at Reportfolio, the user can create a report from a model
		String reportFromModelUrl = WebUI.getUrl()
		checkReportFromModel()

		// Case 7 -  Verify at Reportfolio, the display of the report created from a model
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/Report name'), "( Test Advertiser 2 ) New report from a model") == true
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/Model name'), modelName.replaceAll("\u00A0", " ") + "\n?") == true

		// Case 8 - Verify at Reporfolio the user can update the Advertiser of a report
		WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
		WebUI.delay(1)
		WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( Test Advertiser 3 ) New report from a model")
		WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/Report Name'), "( Test Advertiser 3 ) New report from a model") == true
		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("Modification successfully saved", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)

		// Case 9 - Verify at Reportfolio, the list of model when a model was deleted
		deleteModel(folderName, "Model for automation " + randomNumber)
		WebUI.refresh()
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)
		WebUI.click(findTestObject('Object Repository/2. Folders list/New Report button'))

		TestObject currentModel = findTestObject('Object Repository/2. Folders list/First model button')
		int i = 2
		int numberOfModels = selenium.getCssCount("div.cdk-overlay-pane > div > div > button").intValue()
		String currentModelName = ""
		while(i < numberOfModels) {
			currentModelName = WebUI.getText(changeSelector(currentModel, "child\\(\\d+\\)", "child("+ i +"")).replaceAll("\\n\\?", "")
			assert WebUI.verifyNotEqual(currentModelName, "Model for automation " + randomNumber) == true
			i++
		}
		WebUI.delay(1)

		// Case 10 - Verify at Reportfolio, the behavior of the report when the report's model has been deleted
		WebUI.navigateToUrl(reportFromModelUrl)
		WebUI.waitForPageLoad(30)
		WebUI.delay(1)
		checkReportFromModel()
	}

	private static void checkReportFromModel () {
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/3. XLS Settings/Template name'), "Template.xlsx") == true
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), 5)
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "value", "8", 2) == true
		WebUI.click(findTestObject('2. Folders list/Modal/Cancel button'))
		WebUI.delay(1)
		WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Schedule button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/1. Schedule/Frequency select'), 2)
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/1. Schedule/Frequency select'), "weekly") == true
		WebUI.click(findTestObject('2. Folders list/Modal/Cancel button'))
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/4. Subreports/Subreport name'), "Subreport from model RPF-84") == true
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/CSV Settings button'))
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/3. Report/5. CSV Settings/Start from row input'), "value", "7", 2) == true
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/3. Report/5. CSV Settings/Exclude last'), "value", "8", 2) == true
		assert WebUI.verifyOptionSelectedByValue(findTestObject('Object Repository/3. Report/5. CSV Settings/Separator select'), "	", false, 2) == true
		assert WebUI.verifyElementChecked(findTestObject('Object Repository/3. Report/5. CSV Settings/Cumulative checkbox'), 2) == true
		WebUI.focus(findTestObject('Object Repository/3. Report/5. CSV Settings/Cumulative label'))
	}
}
