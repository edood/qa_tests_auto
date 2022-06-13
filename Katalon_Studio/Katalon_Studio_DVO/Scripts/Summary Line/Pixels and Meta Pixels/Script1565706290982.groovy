import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.WebElement

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import internal.GlobalVariable as GlobalVariable
import summaryLine.SummaryLine

//=== Day of the month example (Custom) : 1 ===
String startDay = ''

//=== Day of the month example (Custom) : 15 ===
String endDay = ''

//Katalon recorder Setup + Login testcase
SummaryLine testcase = new SummaryLine()

//testcase.start('https://shared5-saas.tradelab.fr')

WebUI.openBrowser('https://staging-optimizerdv360.tradelab.fr')

testcase.driver = DriverFactory.getWebDriver()

testcase.baseUrl = 'https://staging-optimizerdv360.tradelab.fr'

testcase.selenium = new WebDriverBackedSelenium(testcase.driver, testcase.baseUrl)

WebUI.maximizeWindow()

String tiret = '-'
String url = ''
if (((GlobalVariable.env != 'dev') && (GlobalVariable.env != 'staging')) && (GlobalVariable.env != 'prod')) {
	url = GlobalVariable.env2 = 'saas'
} else if (GlobalVariable.env == 'prod') {
	tiret = ''
}

String currentWebsite = GlobalVariable.env2 + '.tradelab.fr'

String page = 'login'

String optionsString = (('path=/, domain=' + GlobalVariable.env) + '-') + currentWebsite

testcase.selenium.open((((('https://' + GlobalVariable.env) + '-') + currentWebsite) + '/') + page)

Thread.sleep(2000)

testcase.selenium.waitForPageToLoad('30')

boolean isLoggedIn = testcase.selenium.isElementPresent('css=body > app-root > app-layout > mat-toolbar > button:nth-child(4)')

if (isLoggedIn == true) {
	testcase.selenium.open((('https://' + GlobalVariable.env) + '-') + currentWebsite)

	Thread.sleep(2000)

	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('body > app-root > app-layout > mat-toolbar > button:nth-child(4)',
		60)

	CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('body > app-root > app-layout > mat-toolbar > button:nth-child(4)')

	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('#cdk-overlay-0 > div > div > button', 60)

	CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('#cdk-overlay-0 > div > div > button')

	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('button.validation-login', 60)

	WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

	WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

	WebUI.click(findTestObject('Object Repository/1. Login/Login Button'))

	WebUI.delay(2)
} else {
	testcase.selenium.open((((('https://' + GlobalVariable.env) + '-') + currentWebsite) + '/') + page)

	Thread.sleep(2000)

	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('button.validation-login', 60)

	WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

	WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

	CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('app-login > div > section > form > button')
}

Thread.sleep(2000)

testcase.selenium.waitForPageToLoad('30')

testcase.selenium.open('https://' + GlobalVariable.env + '-' + currentWebsite + '/' + GlobalVariable.advertiser)

if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 30, FailureHandling.OPTIONAL) == true) {
	WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)
}

CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('app-layout > mat-toolbar > button:nth-child(1) > span > i',
	60)

//CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-toolbar > button:nth-child(1) > span > i')

if (GlobalVariable.template == 'Line') {
	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('mat-sidenav > div > nav > ul > li:nth-child(1)', 60)

	CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav > div > nav > ul > li:nth-child(1)')
} else if (GlobalVariable.template == 'Creative') {
	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('mat-sidenav > div > nav > ul > li:nth-child(2)', 60)

	CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav > div > nav > ul > li:nth-child(2)')
} else if (GlobalVariable.template == 'URL App') {
	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('mat-sidenav > div > nav > ul > li:nth-child(3)', 60)

	CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav > div > nav > ul > li:nth-child(3)')
} else if (GlobalVariable.template == 'Exchange') {
	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('mat-sidenav > div > nav > ul > li:nth-child(4)', 60)

	CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav > div > nav > ul > li:nth-child(4)')
} else if (GlobalVariable.template == 'Inventory Source') {
	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('mat-sidenav > div > nav > ul > li:nth-child(5)', 60)

	CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav > div > nav > ul > li:nth-child(5)')
}

//KatalonRecorder Checkgrid Testcase
CustomKeywords.'editedLine.EditedLine.Checkgrid'(testcase.selenium, GlobalVariable.dateRange)



//KatalonRecorder Rearrange Grid Testcase
CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2)', 60)
String secondLineItemName = testcase.selenium.getText('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2)')

secondLineItemName = secondLineItemName[1].matches('/(.*) \\(\\d*\\)/')

testcase.selenium.click('css=.ag-icon-columns')

int numberOfSelectedColumns = testcase.selenium.getCssCount('div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')

while (numberOfSelectedColumns > 0) {
	testcase.selenium.click('css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')

	numberOfSelectedColumns--
}

if (GlobalVariable.template == 'Line') {
	testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)')
} else if (((GlobalVariable.template == 'URL App') || (GlobalVariable.template == 'Exchange')) || (GlobalVariable.template == 'Inventory Source')) {
	testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(6) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)')
} else if (GlobalVariable.template == 'Creative') {
	testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(7) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)')
}

testcase.selenium.click('css=div.ag-header-viewport > div > div:nth-child(2) > div:nth-child(1) > div > span > span')

testcase.selenium.click('css=ag-grid-angular > div > div.ag-theme-balham > div > div > div.ag-tab-body > div > div.ag-menu-option:nth-of-type(4)')

testcase.selenium.doubleClick('css=div.ag-header-cell > div.ag-header-cell-resize')

testcase.selenium.runScript('document.querySelector("div.ag-pinned-left-header").style.display = "none"')

testcase.selenium.runScript('document.querySelector(".ag-floating-top > div.ag-pinned-left-floating-top").style.display = "none"')

testcase.selenium.runScript('document.querySelector("div.ag-body-viewport > div.ag-pinned-left-cols-container").style.display = "none"')

if (GlobalVariable.template == 'Line') {
	testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)')
} else if (((GlobalVariable.template == 'URL App') || (GlobalVariable.template == 'Exchange')) || (GlobalVariable.template == 'Inventory Source')) {
	testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(6) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)')
} else if (GlobalVariable.template == 'Creative') {
	testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(7) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)')
}

if (GlobalVariable.template == 'Line') {
	testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)')
} else if (((GlobalVariable.template == 'URL App') || (GlobalVariable.template == 'Exchange')) || (GlobalVariable.template == 'Inventory Source')) {
	testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(6) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)')
} else if (GlobalVariable.template == 'Creative') {
	testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(7) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)')
}

int mandatoryColumnsNumber
int revenueColumnPosition

if (GlobalVariable.template == 'Line') 
{
 mandatoryColumnsNumber = 16
revenueColumnPosition = 6
}
 else if (GlobalVariable.template == 'Creative') 
{
mandatoryColumnsNumber = 18
 revenueColumnPosition = 8
} else if (GlobalVariable.template == 'URL App' || GlobalVariable.template == 'Exchange' || GlobalVariable.template == 'Inventory Source') 
{
 mandatoryColumnsNumber = 17
 revenueColumnPosition = 7
}

testcase.selenium.click('css=.ag-icon-columns')

//KatalonRecorder Display pixel testcase
boolean totalConvHasData = false
boolean pvConvHasData = false
boolean pcConvHasData = false
boolean cpaHasData = false
boolean cpaPcHasData = false
int inc = 1
int pixelPosition
while (totalConvHasData != true || pvConvHasData != true || pcConvHasData != true || cpaHasData != true || cpaPcHasData != true)
{
testcase.selenium.click("css=.ag-icon-columns")
Number numberOfOpened = testcase.selenium.getCssCount("div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)")
while (numberOfOpened > 1)
{
testcase.selenium.click("css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden) > span")
numberOfOpened--
}


int pixelNumber = inc

pixelPosition = mandatoryColumnsNumber + 6 * pixelNumber

testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(" + pixelPosition + ") > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden) > span")
testcase.selenium.click("css=.ag-icon-columns")
if (GlobalVariable.highlighting == true)
{
testcase.selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body> div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(1)")
testcase.selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body> div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(2)")
testcase.selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body> div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(3)")
testcase.selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body> div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(4)")
testcase.selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body> div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(5)")
}

WebUI.modifyObjectProperty(testcase.tmp, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body> div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(1)', true)
List<WebElement> Welements = WebUI.findWebElements(testcase.tmp, 30)
totalConvHasData = Welements[0].getText() != 0

WebUI.modifyObjectProperty(testcase.tmp, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body> div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(2)', true)
Welements = WebUI.findWebElements(testcase.tmp, 30)
pvConvHasData = Welements[0].getText() != 0

WebUI.modifyObjectProperty(testcase.tmp, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body> div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(3)', true)
Welements = WebUI.findWebElements(testcase.tmp, 30)
pcConvHasData = Welements[0].getText() != 0

WebUI.modifyObjectProperty(testcase.tmp, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body> div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(4)', true)
Welements = WebUI.findWebElements(testcase.tmp, 30)
cpaHasData = Welements[0].getText() != 0

WebUI.modifyObjectProperty(testcase.tmp, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body> div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(5)', true)
Welements = WebUI.findWebElements(testcase.tmp, 30)
cpaPcHasData = Welements[0].getText() != 0

inc++

}
println(pixelPosition)
testcase.selenium.click("css=.ag-icon-columns")
String pixelName = testcase.selenium.getText("css=div.ag-toolpanel-indent-0:nth-of-type(" + pixelPosition + ") > span.ag-column-tool-panel-column-group")
println(pixelName)
String backup = pixelName
String pixelId = pixelName.replaceAll(/.* \\((\\d*)\\)/, "\$1")
println(pixelId)
pixelName = pixelName.replaceAll(/(.*) \\(\\d*\\)/, "\$1")
testcase.selenium.click("css=div.ag-column-tool-panel-column.ag-toolpanel-indent-1:nth-of-type(" + revenueColumnPosition + ") > span > span:nth-of-type(2)")
testcase.selenium.click("css=.ag-icon-columns")

//KatalonRecorder Check report columns Testcase
CustomKeywords.'summaryLine.SummaryLine.CheckReportColumnsPixelsandMetaPixels'(testcase.selenium, GlobalVariable.dateRange, GlobalVariable.highlighting, testcase.tmp)

//KatalonRecorder Create Meta Pixel testcase
testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(3) > button")
int numberOfMetaPixels = testcase.selenium.getCssCount("meta-pixels-panel > div > ul > li")
int newMetaPixelPostion = numberOfMetaPixels + 1
testcase.selenium.click("css=div.pixels-selector > mat-select > div.mat-select-trigger")
CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('ngx-mat-select-search', 60)

numberOfPixels = testcase.selenium.getCssCount("div.mat-select-search-panel > mat-option")
int currentPixel = 1
while (currentPixel < numberOfPixels)
{
	testcase.selenium.click("css=div.mat-select-search-panel > mat-option:nth-of-type(" + currentPixel + ") > mat-pseudo-checkbox")
	currentPixel++
}
testcase.selenium.click("css=div.cdk-overlay-backdrop")
testcase.selenium.click("css=div.meta-pixel-creator > button")
boolean isMetaPixelCreated = testcase.selenium.isElementPresent("css=meta-pixels-panel > div > ul > li:nth-child(" + newMetaPixelPostion + ") > contenteditable > div > i")
while (isMetaPixelCreated == false)
{
Thread.sleep(2500);
isMetaPixelCreated = testcase.selenium.isElementPresent("css=meta-pixels-panel > div > ul > li:nth-child(" + newMetaPixelPostion + ") > contenteditable > div > i")
}
CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'("meta-pixels-panel > div > ul > li:nth-child(" + newMetaPixelPostion + ") > contenteditable > div > i")
WebUI.modifyObjectProperty(testcase.tmp, 'css', ConditionType.EQUALS.toString(), "meta-pixels-panel > div > ul > li:nth-child(" + newMetaPixelPostion + ") > contenteditable > div > input", true)
WebUI.sendKeys(testcase.tmp, "Test Auto Meta Pixel\n")

CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'(".ag-icon-columns")

int numberOfOpened = testcase.selenium.getCssCount("div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)")
println("numberOfOpened : " + numberOfOpened)
while (numberOfOpened > 2)
{
Thread.sleep(10000);
testcase.selenium.click("css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden) > span")
numberOfOpened--
}
Thread.sleep(3000);
println('div.ag-column-tool-panel-column.ag-toolpanel-indent-1:nth-of-type(' + revenueColumnPosition + ') > span > span:nth-of-type(2)')
CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'("div.ag-column-tool-panel-column.ag-toolpanel-indent-1:nth-of-type(" + revenueColumnPosition + ") > span:nth-child(1)")
WebUI.delay(2)
CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'("div.ag-column-tool-panel-column.ag-toolpanel-indent-1:nth-of-type(" + revenueColumnPosition + ") > span:nth-child(1)")

//KatalonRecord Check report columns testcase
CustomKeywords.'summaryLine.SummaryLine.CheckReportColumnsPixelsandMetaPixels'(testcase.selenium, GlobalVariable.dateRange, GlobalVariable.highlighting, testcase.tmp)

println("end")
