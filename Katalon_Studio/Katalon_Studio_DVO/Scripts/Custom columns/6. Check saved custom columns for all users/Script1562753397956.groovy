import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*

import org.openqa.selenium.WebElement as WebElement

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.SelectorMethod as SelectorMethod
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.OptimizerDV360
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium as WebDriverBackedSelenium

import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')
OptimizerDV360 testcase = new OptimizerDV360()
def driver = DriverFactory.getWebDriver()

String baseUrl = 'https://www.katalon.com/'

selenium = new WebDriverBackedSelenium(driver, baseUrl)

WebUI.waitForPageLoad(30)

WebUI.maximizeWindow()

String tiret = '-'
if (((GlobalVariable.env != 'dev') && (GlobalVariable.env != 'staging')) && (GlobalVariable.env != 'prod')) {
	GlobalVariable.env2 = 'saas'
} else if (GlobalVariable.env == 'prod') {
	tiret = ''
	GlobalVariable.env = ''
}

WebUI.navigateToUrl('https://' + GlobalVariable.env + tiret + GlobalVariable.env2 + ".tradelab.fr")

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

WebUI.delay(2)

WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

WebUI.click(findTestObject('Object Repository/1. Login/Login Button'))

WebUI.delay(2)

String url = ('https://' + GlobalVariable.env + tiret + GlobalVariable.env2 + ".tradelab.fr/") + GlobalVariable.advertiser

WebUI.navigateToUrl(url)

WebUI.waitForPageLoad(30)

WebUI.delay(2)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Menu button'))

WebUI.waitForElementPresent(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'), 30)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30, FailureHandling.STOP_ON_FAILURE)
WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/3. Grid/Loading Progress Bar'), 300)

WebUI.click(findTestObject('DV360 Optimizer/9. Config/Config menu'))

WebUI.click(findTestObject('DV360 Optimizer/9. Config/Config select'))

WebUI.click(findTestObject('DV360 Optimizer/9. Config/First config'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)
WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/3. Grid/Loading Progress Bar'), 300)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/Columns tab'))

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/Columns tab'))


testcase.selectDateRange(GlobalVariable.dateRange)

int interval = 2

String intervalOption = 'body > div.cdk-overlay-container > div.cdk-overlay-connected-position-bounding-box > div > div > mat-option:nth-child(' + interval + ')'

TestObject intervalDynamicOption = new TestObject('IntervalOption')

intervalDynamicOption.addProperty('css', ConditionType.EQUALS, intervalOption)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Interval selector'))

WebUI.delay(1)

WebUI.click(intervalDynamicOption)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Daterange-Interval-Validator'))

Number numberOfSelectedColumns = selenium.getCssCount('div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')

while (numberOfSelectedColumns > 0) {
	selenium.click('css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')

	numberOfSelectedColumns = (numberOfSelectedColumns - 1)
}

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/Custom columns tab'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab first element remove icon'),
		30)

numberOfSavedCustomColumns = 20

Number numberOfCustomColumns = selenium.getCssCount(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab remove icons').getSelectorCollection().get(
		SelectorMethod.CSS))

String regex = 'nth-child\\((\\d+)\\)'

while (numberOfCustomColumns > 0) {
	TestObject removeIcon = findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab first element remove icon')

	removeIcon.setSelectorValue(SelectorMethod.CSS, removeIcon.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(
			regex, ('nth-child(' + numberOfCustomColumns) + ')'))

	TestObject customTabElement = findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab first element name')

	customTabElement.setSelectorValue(SelectorMethod.CSS, customTabElement.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(
			regex, ('nth-child(' + numberOfCustomColumns) + ')'))

	String customTabElementText = WebUI.getText(customTabElement)

	TestObject customTabEditButton = findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab First edit button')

	customTabEditButton.setSelectorValue(SelectorMethod.CSS, customTabEditButton.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(
			regex, ('nth-child(' + numberOfCustomColumns) + ')'))

	if (customTabElementText.matches('1[0-9][0-9][0-9]')) {
		selenium.highlight('css=' + customTabElement.getSelectorCollection().get(SelectorMethod.CSS))

		WebElement element = WebUiCommonHelper.findWebElement(customTabEditButton, 30)

		WebUI.executeJavaScript('arguments[0].setAttribute("style","display: block")', Arrays.asList(element))

		WebUI.doubleClick(customTabElement)

		WebUI.click(customTabEditButton)

		WebUI.click(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab Edit Column button'))

		numberOfSavedCustomColumns = (numberOfSavedCustomColumns - 1)
	}

	numberOfCustomColumns = (numberOfCustomColumns - 1)
}

assert numberOfSavedCustomColumns == 0

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/button_profile'))

WebUI.waitForElementVisible(findTestObject('1. Login/Logout button'), 5)

WebUI.click(findTestObject('1. Login/Logout button'))

GlobalVariable.username = "canceau"

WebUI.waitForElementVisible(findTestObject('Object Repository/1. Login/Login'), 30)

WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

WebUI.click(findTestObject('Object Repository/1. Login/Login Button'))

WebUI.delay(2)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/1. Top Bar/Menu button'))

url = (((('https://' + GlobalVariable.env) + '-') + GlobalVariable.env2) + '.tradelab.fr/') + GlobalVariable.advertiser

WebUI.navigateToUrl(url)

WebUI.waitForPageLoad(30)

WebUI.delay(2)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Menu button'))

WebUI.waitForElementPresent(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'), 30)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)
WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/3. Grid/Loading Progress Bar'), 300)



WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/Columns tab'))

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/Columns tab'))

testcase.selectDateRange(GlobalVariable.dateRange)

WebUI.delay(1)

WebUI.click(intervalDynamicOption)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Daterange-Interval-Validator'))
WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)
WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/3. Grid/Loading Progress Bar'), 300)


numberOfSelectedColumns = selenium.getCssCount('div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')

while (numberOfSelectedColumns > 0) {
	selenium.click('css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')

	numberOfSelectedColumns = (numberOfSelectedColumns - 1)
}

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/Custom columns tab'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab first element remove icon'),
		30)

numberOfSavedCustomColumns = 20

numberOfCustomColumns = selenium.getCssCount(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab remove icons').getSelectorCollection().get(
		SelectorMethod.CSS))

while (numberOfCustomColumns > 0) {
	removeIcon = findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab first element remove icon')

	removeIcon.setSelectorValue(SelectorMethod.CSS, removeIcon.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(
			regex, ('nth-child(' + numberOfCustomColumns) + ')'))

	customTabElement = findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab first element name')

	customTabElement.setSelectorValue(SelectorMethod.CSS, customTabElement.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(
			regex, ('nth-child(' + numberOfCustomColumns) + ')'))

	customTabElementText = WebUI.getText(customTabElement)

	customTabEditButton = findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab First edit button')

	customTabEditButton.setSelectorValue(SelectorMethod.CSS, customTabEditButton.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(
			regex, ('nth-child(' + numberOfCustomColumns) + ')'))

	if (customTabElementText.matches('1[0-9][0-9][0-9]')) {
		selenium.highlight('css=' + customTabElement.getSelectorCollection().get(SelectorMethod.CSS))

		WebElement element = WebUiCommonHelper.findWebElement(customTabEditButton, 30)

		WebUI.executeJavaScript('arguments[0].setAttribute("style","display: block")', Arrays.asList(element))

		WebUI.doubleClick(customTabElement)

		WebUI.click(customTabEditButton)

		WebUI.click(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab Edit Column button'))

		numberOfSavedCustomColumns = (numberOfSavedCustomColumns - 1)
	}

	numberOfCustomColumns = (numberOfCustomColumns - 1)
}

assert numberOfSavedCustomColumns == 0
