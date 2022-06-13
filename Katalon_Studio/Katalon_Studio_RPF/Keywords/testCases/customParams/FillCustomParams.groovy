package testCases.customParams

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import reportfolio.Reportfolio

public class FillCustomParams extends Reportfolio {

	// RPF-85
	@Keyword
	public void fillCustomParams () {
		selenium = getSelenium()
		driver = DriverFactory.getWebDriver()
		String randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))
		createFolder("Fill custom params")
		String modelName = "Model\u00A0with\u00A0custom\u00A0params\u00A0" + randomNumber
		String templateName = "Template Insert a Table with custom params"
		String templateFormat = "xlsx"

		String toast = createModel(modelName, "weekly", templateName, templateFormat, "2", "Subreport from model with custom params", 1, "2", "3", ";", true)
		assert toast == templateName + "." + templateFormat + " has been successfully uploaded.\n5 custom params found in the XLS template :\nDesktop, Mobile, Profiling, Prospection, Retargeting"
		String[] customParamsLabels = toast.replaceFirst(templateName + "." + templateFormat + " has been successfully uploaded.\n5 custom params found in the XLS template :\n","").split("\\, ")
		String customParamsValue = "Custom Param value"
		WebUI.refresh()
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		// Case 1, 6, 7 & 8 - Report from model with custom params - Creation, display & behavior checks
		createReportFromModelWithCustomParams(modelName, "Report from model - with custom params", "Test", customParamsLabels, customParamsValue)
		checkReportFromModelWithCustomParams()


		// Case 3 - Report from model with empty custom params - Creation, display & behavior checks
		customParamsValue = ""
		createReportFromModelWithCustomParams(modelName, "Report from model - with custom params not mandatory", "Test", customParamsLabels, customParamsValue)
		checkReportFromModelWithCustomParams()


		// Case 4 & 5 - "Next" button appears only when the folder and report name are filled
		// Finding model with custom params
		WebUI.click(findTestObject('Object Repository/2. Folders list/New Report button'))
		String currentModelName = WebUI.getText(findTestObject('Object Repository/2. Folders list/First model button'))
		int i = 1
		WebUI.delay(2)
		String numberOfModelsSelector = findTestObject('Object Repository/2. Folders list/First model button').getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(":nth-child\\(\\d\\)", "")
		WebUI.delay(2)
		int numberOfModels = selenium.getCssCount(numberOfModelsSelector)
		WebUI.delay(2)
		while(i < numberOfModels && !currentModelName.replaceAll("\\n\\?", "").equals(modelName.replaceAll("\u00A0", " "))) {
			i++
			TestObject currentModel = changeSelector(findTestObject('Object Repository/2. Folders list/First model button'), "nth-child\\(\\d\\)", "nth-child(" + i + ")")
			WebUI.focus(currentModel)
			currentModelName = WebUI.getText(currentModel)
		}
		if( i != numberOfModels) {
			WebUI.click(changeSelector(findTestObject('Object Repository/2. Folders list/First model button'), "nth-child\\(\\d\\)", "nth-child(" + i + ") > span > button"))
			// Checking first modal display
			WebUI.waitForElementVisible(findTestObject('2. Folders list/Modal/Modal label'), 5)
			assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal label'), "Create New Report\n" + modelName.replaceAll("\u00A0", " ")) == true
			assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal 1st position select'), "Folder") == true
			WebUI.click(findTestObject('2. Folders list/Modal/Modal 1st position select'))
			Thread.sleep(500);
			// "Next" button is disabled
			assert WebUI.verifyElementHasAttribute(findTestObject('2. Folders list/Modal/Basic Report Save button'), "disabled", 5) == true
			// Select folder
			WebUI.click(findTestObject('2. Folders list/Modal/Overlay First option'))
			WebUI.waitForElementVisible(findTestObject('2. Folders list/Modal/Modal 2nd position input'), 30)
			// Check display
			assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal 2nd position label'), "Report Name") == true
			assert WebUI.verifyElementPresent(findTestObject('2. Folders list/Modal/Cancel button'), 2) == true
			String buttonLabel = WebUI.getText(findTestObject('2. Folders list/Modal/Basic Report Save button'))
			assert buttonLabel == "Next"
			// Fill report name
			WebUI.setText(findTestObject('2. Folders list/Modal/Modal 2nd position input'), "Report from model - check first modal behavior")
			// "Next" button is enabled
			assert WebUI.verifyElementNotHasAttribute(findTestObject('2. Folders list/Modal/Basic Report Save button'), "disabled", 5) == true
			WebUI.click(findTestObject('2. Folders list/Modal/Cancel button'))
		} else {
			WebUI.executeJavaScript("alert(\"Model not found\")", null)
			KeywordUtil.markFailed("Model not found")
		}


		// Case 2 - Report from model without custom params - Creation, display & behavior checks
		modelName = "Model\u00A0without\u00A0custom\u00A0params\u00A0" + randomNumber
		templateName = "Template"
		createModel(modelName, "weekly", templateName, templateFormat, "9", "Subreport from model without custom params", 1, "4", "5", ";", false)
		WebUI.refresh()
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)
		customParamsValue = ""
		createReportFromModel(modelName, "Report from model - without custom params", "Test")
		checkReportFromModelWithoutCustomParams()


		// Case 9 - Check new basic report modal
		createReport("Basic report", "Test")
	}

	private void checkReportFromModelWithCustomParams () {
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/3. XLS Settings/Template name'), "Template Insert a Table with custom params.xlsx") == true
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), 5)
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "value", "2", 2) == true
		WebUI.click(findTestObject('2. Folders list/Modal/Cancel button'))
		WebUI.delay(1)
		WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Schedule button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/1. Schedule/Frequency select'), 2)
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/1. Schedule/Frequency select'), "weekly") == true
		WebUI.click(findTestObject('2. Folders list/Modal/Cancel button'))
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/4. Subreports/Subreport name'), "Subreport from model with custom params") == true
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/CSV Settings button'))
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/3. Report/5. CSV Settings/Start from row input'), "value", "2", 2) == true
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/3. Report/5. CSV Settings/Exclude last'), "value", "3", 2) == true
		assert WebUI.verifyOptionSelectedByValue(findTestObject('Object Repository/3. Report/5. CSV Settings/Separator select'), ";", false, 2) == true
		assert WebUI.verifyElementChecked(findTestObject('Object Repository/3. Report/5. CSV Settings/Cumulative checkbox'), 2) == true
		WebUI.focus(findTestObject('Object Repository/3. Report/5. CSV Settings/Cumulative label'))
	}

	private void checkReportFromModelWithoutCustomParams () {
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/3. XLS Settings/Template name'), "Template.xlsx") == true
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), 5)
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "value", "9", 2) == true
		WebUI.click(findTestObject('2. Folders list/Modal/Cancel button'))
		WebUI.delay(1)
		WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Schedule button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/1. Schedule/Frequency select'), 2)
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/1. Schedule/Frequency select'), "weekly") == true
		WebUI.click(findTestObject('2. Folders list/Modal/Cancel button'))
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/4. Subreports/Subreport name'), "Subreport from model without custom params") == true
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/CSV Settings button'))
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/3. Report/5. CSV Settings/Start from row input'), "value", "4", 2) == true
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/3. Report/5. CSV Settings/Exclude last'), "value", "5", 2) == true
		assert WebUI.verifyOptionSelectedByValue(findTestObject('Object Repository/3. Report/5. CSV Settings/Separator select'), ";", false, 2) == true
		assert WebUI.verifyElementNotChecked(findTestObject('Object Repository/3. Report/5. CSV Settings/Cumulative checkbox'), 2) == true
		WebUI.focus(findTestObject('Object Repository/3. Report/5. CSV Settings/Cumulative label'))
	}
}
