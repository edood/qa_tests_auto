import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import internal.GlobalVariable as GlobalVariable

// Open the SaaS for specified environment
WebUI.openBrowser('')
WebUI.waitForPageLoad(30)
WebUI.maximizeWindow()
String tiret = '-'
if (((GlobalVariable.env != 'dev') && (GlobalVariable.env != 'staging')) && (GlobalVariable.env != 'prod')) {
	url = GlobalVariable.env2 = 'saas' 
} else if (GlobalVariable.env == 'prod') {
	tiret = ''
}
String url = ('https://' + GlobalVariable.env) + tiret + GlobalVariable.env2 + '.tradelab.fr/'
WebUI.navigateToUrl(url)
WebUI.waitForPageLoad(30)

// Prepare to highlight elements for specific methods
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

// Login to the SaaS
WebUI.waitForElementVisible(findTestObject('1. Login/Login'), GlobalVariable.username)
WebUI.setText(findTestObject('1. Login/Login'), GlobalVariable.username)
WebUI.setText(findTestObject('1. Login/Password'), GlobalVariable.password)
WebUI.click(findTestObject('1. Login/Login Button'))

WebUI.waitForElementVisible(findTestObject('Object Repository/2. SaaS/Menu'), 30)

// Creating Schedule
WebUI.navigateToUrl(url + "scheduler")
WebUI.waitForPageLoad(30)

WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/Add Schedule button'), 30)
WebUI.setText(findTestObject('Object Repository/2. SaaS/Advertiser input'), GlobalVariable.advertiser)
WebUI.waitForElementVisible(findTestObject('Object Repository/2. SaaS/Advertiser autocomplete'), 30)
WebUI.click(findTestObject('Object Repository/2. SaaS/Advertiser autocomplete'))

WebUI.delay(3)
WebUI.waitForElementNotPresent(findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/Progress bar'), 30)

CustomKeywords.'scheduler.scheduleEditor.deleteSchedules'(GlobalVariable.deleteSchedules)

if(GlobalVariable.auto == false) {
	CustomKeywords.'scheduler.scheduleEditor.createSchedule'(GlobalVariable.metric, GlobalVariable.operator, GlobalVariable.value, "Test Auto URL/App exclusion by metric " + GlobalVariable.metric, GlobalVariable.scheduleDelay)
} else {
	CustomKeywords.'scheduler.scheduleEditor.createSchedule'(["CPA"], "greater than", "0", "Test Auto URL/App exclusion by metric CPA", GlobalVariable.scheduleDelay)
	WebUI.refresh()
	WebUI.waitForPageLoad(30)
	CustomKeywords.'scheduler.scheduleEditor.createSchedule'(["CPA PC"], "less than", "1", "Test Auto URL/App exclusion by metric CPA PC", GlobalVariable.scheduleDelay)
	WebUI.refresh()
	WebUI.waitForPageLoad(30)
	CustomKeywords.'scheduler.scheduleEditor.createSchedule'(["Spend"], "greater than or equal", "0", "Test Auto URL/App exclusion by metric Spend", GlobalVariable.scheduleDelay)
	WebUI.refresh()
	WebUI.waitForPageLoad(30)
	CustomKeywords.'scheduler.scheduleEditor.createSchedule'(["CPA PV"], "less than or equal", "1", "Test Auto URL/App exclusion by metric CPA PV", GlobalVariable.scheduleDelay)
	WebUI.refresh()
	WebUI.waitForPageLoad(30)
	CustomKeywords.'scheduler.scheduleEditor.createSchedule'(["CPA PV"], "equal", "0", "Test Auto URL/App exclusion by metric CPA PV", GlobalVariable.scheduleDelay)
}

CustomKeywords.'scheduler.scheduleEditor.deleteSchedules'(GlobalVariable.deleteSchedules)