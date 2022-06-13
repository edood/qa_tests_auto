import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import editTargeting.Targeting
import editTargeting.targetObject
import internal.GlobalVariable as GlobalVariable

int EXCLUDE_NUMBER = 1
int[] rowCheckbox = [3]
Targeting testcase = new Targeting()
WebUI.openBrowser('')

WebUI.waitForPageLoad(30)

WebUI.maximizeWindow()

testcase.driver = DriverFactory.getWebDriver()

String url = ('https://' + GlobalVariable.env) + '-' + GlobalVariable.env2 + '.tradelab.fr/'

WebUI.navigateToUrl(url)

WebUI.delay(2)

WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

WebUI.click(findTestObject('Object Repository/1. Login/Login Button'))

WebUI.delay(2)

url = ((('https://' + GlobalVariable.env) + '-' + GlobalVariable.env2 + '.tradelab.fr/') + GlobalVariable.advertiser)

WebUI.navigateToUrl(url)

WebUI.waitForPageLoad(30)
WebUI.delay(1)

CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav-container > mat-sidenav > div > nav > ul > li:nth-child(2)')

WebUI.delay(1)

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/3. Grid/Loading Progress Bar'), 60)

if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 10, FailureHandling.OPTIONAL) == true) {
	WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)
}

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)

testcase.refreshAdvertiser()

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30, FailureHandling.CONTINUE_ON_FAILURE)

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Lock advertiser message'), 15)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Menu button'))

WebUI.delay(1)

CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav-container > mat-sidenav > div > nav > ul > li:nth-child(2)')

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30, FailureHandling.CONTINUE_ON_FAILURE)

testcase.selectDateRange(GlobalVariable.dateRange)

if (WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30, FailureHandling.STOP_ON_FAILURE) == false)
	KeywordUtil.markFailedAndStop('data are not displayed on the optimizer')

WebUI.delay(2)

String[] CreativeName = new String[EXCLUDE_NUMBER]
String[] linkedline = new String[EXCLUDE_NUMBER]

TestObject CreativeCheckbox = new TestObject('CreativeCheckbox')
TestObject creaName = new TestObject('creaName')
TestObject lineName = new TestObject('lineName')

int onetime = 0

String lineNameSelector = null
String CreaNameSelector = null
int gridsize = 15
for (def row : rowCheckbox) 
{
	css = 'div[row-index="' + (row - 1) + '"] > div> span > span.ag-selection-checkbox'	
	if (row / gridsize >= 1)
	{
		int scrollnbr = gridsize
		while (scrollnbr < row)
		{
			WebUI.delay(1)
			WebUI.executeJavaScript('document.querySelector(\'div[row-index=\"' + (scrollnbr - 1) + '\"]\').scrollIntoView({block: \'center\'})', null)
			scrollnbr += gridsize
		}
	}
	
	lineNameSelector = 'div[row-index=\"' + (row - 1) + '\"] > div[col-id^=\"liName\"]'
	CreaNameSelector = 'div[row-index=\"' + (row - 1) + '\"] > div[col-id^=\"creativeName\"]'

	println("LineSelector = " + lineNameSelector)
	println("CreaSelector = " + CreaNameSelector)
	
	if (onetime == 0) 
	{
		CreativeCheckbox.addProperty('css', ConditionType.EQUALS, css)
		lineName.addProperty('css', ConditionType.EQUALS, lineNameSelector)
		creaName.addProperty('css', ConditionType.EQUALS, CreaNameSelector)
	} 
	else
	 {
		WebUI.modifyObjectProperty(CreativeCheckbox, 'css', ConditionType.EQUALS.toString(), css, true)
		WebUI.modifyObjectProperty(lineName, 'css', ConditionType.EQUALS.toString(), lineNameSelector, true)
		WebUI.modifyObjectProperty(creaName, 'css', ConditionType.EQUALS.toString(), CreaNameSelector, true)
	}

	println("Creagettext = " + WebUI.getText(creaName))
	println("linegettext = " + WebUI.getText(lineName))
	
	CreativeName[onetime] = WebUI.getText(creaName)
	linkedline[onetime] = WebUI.getText(lineName)

	WebUI.click(CreativeCheckbox)

	onetime++
	WebUI.executeJavaScript('document.querySelector(\'.ag-body-viewport\').scrollTo(0, 0);', null)
}

WebUI.delay(1)

println(linkedline)
println(CreativeName)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/BtnExclude'))

WebUI.delay(5)
WebUI.delay(EXCLUDE_NUMBER * 2)

String generatedfile = testcase.download()


if (WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Toast message'), 30, FailureHandling.OPTIONAL) == true)
KeywordUtil.markFailedAndStop(WebUI.getText(findTestObject('DV360 Optimizer/Toast message')))

LinkedList<targetObject> result = []
for (int i = 0; i < EXCLUDE_NUMBER; i++)
{
	def save = new targetObject()
	String[] str = CreativeName[i].split("\\(")
	if (str[1][0].isNumber() == true)
	save.name = str[0]
	else
	save.name = str[0] + "(" + str[1]
	if (str[1][0].isNumber() == true)
	save.ID = str[1]
	else
	save.ID = str[2]
	save.ID = save.ID.replace(')', '')
	save.line = linkedline[i]
	save.target = "Creative"
	save.type = "Exclude"
	save.parent = ''
	result.add(save)
}

String filecsv = testcase.unzipCSVfileAndCheck(generatedfile, result)
LinkedList<String[]> beforeupload = []
LinkedList<String[]> afterupload = []
testcase.ConnectToDV360(GlobalVariable.googleID, GlobalVariable.googlePassword)
beforeupload = testcase.RecordTargeting(result, true)

for (def strat in testcase.line)
{
	strat.creative_count_bef += strat.creatives.size()
	for (def str in strat.creatives)
	{
		println('creative before name = ' + str.ID)
	}
}

if (testcase.DV360Upload(filecsv, testcase.line[0].name) == true)
{
	afterupload = testcase.RecordTargeting(result, false)
	for (def strat in testcase.line)
	{
		for (def str in strat.creatives)
		{
			println('creative after name = ' + str.ID)
		}
	}
	testcase.CheckTargeting(result, beforeupload, afterupload, "Creative")
}
println("end")
String path = System.getProperty("user.home") + File.separator + "Downloads" + File.separator
testcase.deleteFile(filecsv)
testcase.deleteFile(path + generatedfile)
