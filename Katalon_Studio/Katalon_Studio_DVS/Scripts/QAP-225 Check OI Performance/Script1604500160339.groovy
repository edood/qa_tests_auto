import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import project.OISettings as OISettings
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.testobject.SelectorMethod
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver



Long uniquename = new Date().getTime()
Random kpivalue = new Random()
String record_the_name = 'QAP-225 OI auto_' + uniquename

OISettings testcase = new OISettings(record_the_name)

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-225')
testcase.Start(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

testcase.refreshAdvertiser()
testcase.load('OI', '', '', '')
testcase.CreateOIfromCampaign(GlobalVariable.items[0], record_the_name, GlobalVariable.ioTemplateNames[0], "Paused", 0, 1)

WebUI.click(findTestObject('Object Repository/6. Objects/Second OI span'))
WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))
String record_the_type = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), 'value')
String record_the_value = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), 'value')

testcase.VerifyCreatedTag()

WebDriver driver = DriverFactory.getWebDriver()
ArrayList<WebElement> Alltypes = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/All object type').getSelectorCollection().get(SelectorMethod.CSS)))
for (def elem in Alltypes)
{
	if (elem.isDisplayed() == true)
		assert elem.getText().trim() != 'LI' : "LI should not be displayed"
}

ArrayList<String> GoalKpi = ['CPM', 'CPV', 'CPA', 'CPC', 'CPIAVC', 'CTR', '% Viewability', 'Other']

testcase.SetPerformance(GoalKpi, GlobalVariable.Performance, record_the_name)
if (GlobalVariable.Performance[1] == '')
{
	
	testcase.OIToArchive.push(record_the_name)
	testcase.display.push(record_the_type)
	testcase.display.push(record_the_value)
}

for (def elem in testcase.display) 
	KeywordUtil.logInfo('goal settings : ' + elem)

for (def elem in testcase.OIToArchive)
	KeywordUtil.logInfo('OI name order : ' + elem)

KeywordUtil.logInfo('record the name : ' + record_the_name)
	
	
WebUI.delay(2)
File sdfFile = testcase.download()

ArrayList<String> sdfpaths= CustomKeywords.'project.Builder.unzip'(sdfFile)

CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(sdfpaths)

KeywordUtil.logInfo('display size = ' + testcase.display.size())

if (testcase.display.size() > 9)
{
	testcase.display[12] = testcase.display[12].substring(2) + ' %'
	testcase.display[14] = testcase.display[14] + '...'
}
else if (GlobalVariable.Performance[1] == '% Viewability')
{
	testcase.display[0] = testcase.display[0].substring(2) + ' %'
	
}
else if (GlobalVariable.Performance[1] == 'Other')
	testcase.display[0] = testcase.display[0] + '...'
	
int j = 0

for (int i = 0; i < testcase.OIToArchive.size(); i++)
{
		testcase.PerformanceGoalType = testcase.display[j]
		testcase.DV360VerifyUpload(testcase.OIToArchive[i], 'PerformanceGoalType')
		j++
		testcase.PerformanceGoalValue = testcase.display[j]
		testcase.DV360VerifyUpload(testcase.OIToArchive[i], 'PerformanceGoalValue')
		j++
}

WebUI.navigateToUrl(GlobalVariable.env)
WebUI.waitForPageLoad(30)
assert WebUI.waitForElementVisible(findTestObject('2. Homepage/Advertiser Input'), 60) == true
assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Grid'), 30) == true
WebUI.delay(2)
assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Grid'), 30) == true
testcase.selectadvertiser(GlobalVariable.advertiserName)
WebUI.delay(1)
assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Grid'), 30) == true
testcase.refreshAdvertiser()

for (def OI in testcase.OIToArchive)
	testcase.load('OI', '', OI, '')

testcase.VerifyLoadedTag()

WebUI.navigateToUrl('https://displayvideo.google.com/#ng_nav/overview')
testcase.deleteFile(sdfpaths[0])
testcase.deleteFile(sdfpaths[1])
KeywordUtil.logInfo('OIToArchive.size() = ' + testcase.OIToArchive.size())
CustomKeywords.'com.reusableComponents.DV360.archiveObject'(testcase.OIToArchive, 'OI')