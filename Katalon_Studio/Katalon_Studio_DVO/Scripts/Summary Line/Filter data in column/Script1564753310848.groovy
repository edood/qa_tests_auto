import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.util.regex.Matcher
import java.util.regex.Pattern

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium as WebDriverBackedSelenium

import internal.GlobalVariable as GlobalVariable
import summaryLine.SummaryLine as SummaryLine

//=== Template : Line, Creative, URL/App, Exchange, Inventory Source ===

//--- With Highlight = true; Without Highlight = false ---//
boolean highlighting = GlobalVariable.highlighting

//Katalon recorder Setup + Login testcase
SummaryLine testcase = new SummaryLine()

WebUI.openBrowser('https://staging-optimizerdv360.tradelab.fr')

testcase.driver = DriverFactory.getWebDriver()

testcase.baseUrl = 'https://staging-optimizerdv360.tradelab.fr'

testcase.selenium = new WebDriverBackedSelenium(testcase.driver, testcase.baseUrl)

WebUI.maximizeWindow()

String currentWebsite = 'optimizerdv360.tradelab.fr'

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

testcase.selenium.open((((('https://' + GlobalVariable.env) + '-') + currentWebsite) + '/') + GlobalVariable.advertiser)

if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 30, FailureHandling.OPTIONAL) == true) {
    WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)
}

int ioColumnPosition = 0

if (GlobalVariable.template == 'Line') {
    CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('mat-sidenav > div > nav > ul > li:nth-child(1)', 60)

    CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav > div > nav > ul > li:nth-child(1)')
	
	ioColumnPosition = 17
} else if (GlobalVariable.template == 'Creative') {
    CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('mat-sidenav > div > nav > ul > li:nth-child(2)', 60)

    CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav > div > nav > ul > li:nth-child(2)')
	
	ioColumnPosition = 17
} else if (GlobalVariable.template == 'URL App') {
    CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('mat-sidenav > div > nav > ul > li:nth-child(3)', 60)

    CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav > div > nav > ul > li:nth-child(3)')
	
	ioColumnPosition = 18
} else if (GlobalVariable.template == 'Exchange') {
    CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('mat-sidenav > div > nav > ul > li:nth-child(4)', 60)

    CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav > div > nav > ul > li:nth-child(4)')
	
	ioColumnPosition = 18
} else if (GlobalVariable.template == 'Inventory Source') {
    CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('mat-sidenav > div > nav > ul > li:nth-child(5)', 60)

    CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav > div > nav > ul > li:nth-child(5)')
	
	ioColumnPosition = 18
}

//KatalonRecorder Checkgrid Testcase
CustomKeywords.'editedLine.EditedLine.Checkgrid'(testcase.selenium, GlobalVariable.dateRange)

//KatalonRecorder Rearrange Grid Testcase
CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2)', 
    60)

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

int index = 0
int futureIndex = 0

if (GlobalVariable.template == 'Line') {
	testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(2) > button")
	WebUI.waitForElementPresent(new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "custom-columns-panel > div > div", true), 30)
	int numberOfCustomColumns = testcase.selenium.getCssCount("custom-columns-panel > div > div > ul > li > mat-icon.remove")
	
	testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(3) > button")
	WebUI.waitForElementPresent(new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "meta-pixels-panel > div", true), 30)
	int numberOfMetaPixels = testcase.selenium.getCssCount("meta-pixels-panel > div > ul > li")
	int numberOfColumnsToRemove = numberOfCustomColumns + numberOfMetaPixels
	
	testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")	
	index = testcase.selenium.getCssCount("div.ag-primary-cols-list-panel > div")
	int inc = 0
	boolean checkboxExists = false
	while(inc < numberOfColumnsToRemove + 1) {
		checkboxExists = testcase.selenium.isElementPresent("css=div.ag-primary-cols-list-panel > div:nth-of-type(" + index + ") > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-unchecked:not(.ag-hidden)")
		if (checkboxExists == true) {
			testcase.selenium.click("css=div.ag-primary-cols-list-panel > div:nth-of-type(" + index + ") > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-unchecked:not(.ag-hidden)")
			inc++
		}
		index--
	}
	futureIndex = index
	checkboxExists = false
	index = testcase.selenium.getCssCount("div.ag-primary-cols-list-panel > div")
	while(numberOfColumnsToRemove > 0) {
		checkboxExists = testcase.selenium.isElementPresent("css=div.ag-primary-cols-list-panel > div:nth-of-type(" + index + ") > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)")
		if (checkboxExists == true) {
			testcase.selenium.click("css=div.ag-primary-cols-list-panel > div:nth-of-type(" + index + ") > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)")
			numberOfColumnsToRemove--
		}
		index--
	}
	testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-right-header\").style.display = \"none\"")
	testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-right-floating-top\").style.display = \"none\"")
	testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-right-cols-container\").style.display = \"none\"")
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

int mandatoryColumnsNumber = 0

int revenueColumnPosition = 0

if (GlobalVariable.template == 'Line') {
    mandatoryColumnsNumber = 16

    revenueColumnPosition = 6
} else if (GlobalVariable.template == 'Creative') {
    mandatoryColumnsNumber = 18

    revenueColumnPosition = 8
} else if (((GlobalVariable.template == 'URL App') || (GlobalVariable.template == 'Exchange')) || (GlobalVariable.template == 'Inventory Source')) {
    mandatoryColumnsNumber = 17

    revenueColumnPosition = 7
}

testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")

//KatalonRecorder Filter column CPA Testcase
Number numberOfResults = testcase.selenium.getCssCount("ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(2) > div > div > div")
String halfResults = Math.round(numberOfResults / 2)
String cpaValue = testcase.selenium.getText("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"" + halfResults + "\"] > div:nth-child(7)")
cpaValue = cpaValue - 0.01
testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(3) > div:nth-child(7) > div:nth-child(2) > button")
testcase.selenium.select("css=div.ag-filter > div > div > div:nth-child(1) > select", "Greater than or equals")
testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div > input", cpaValue)
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "focus")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "input")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "blur")
Thread.sleep(1500)
testcase.selenium.click("css=#applyButton")
testcase.selenium.click("css=div.ag-menu > div > div > span")

//KatalonRecorder Check report columns Testcase
CustomKeywords.'summaryLine.SummaryLine.checkReportColumnsFilterData'(testcase.selenium, GlobalVariable.dateRange, 16, highlighting)

//KatalonRecorder Remove Filter CPA Testcase
testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(3) > div:nth-child(7) > div:nth-child(2) > button")
testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "focus")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "input")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "blur")
Thread.sleep(1500)
testcase.selenium.click("css=#applyButton")
testcase.selenium.click("css=div.ag-menu > div > div > span")

if (GlobalVariable.template == 'Line') {
	//KatalonRecorder Filter column Pacing amount Testcase
	int pause1500 = 0
	int pause3000 = 0
	if(GlobalVariable.template.equals("Line")) {
		pause1500 = 1500
		pause3000 = 3000
		testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-right-header\").style.display = \"initial\"")
		testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-right-floating-top\").style.display = \"initial\"")
		testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-right-cols-container\").style.display = \"initial\"")
		testcase.selenium.click("css=div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-expanded > span")
		numberOfResults = testcase.selenium.getCssCount("ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(2) > div > div > div")
		halfResults = Math.round(numberOfResults / 2)
		TestObject toRename = new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(5)  > div.ag-cell-label-container > div.ag-header-cell-label", true)
		WebUI.waitForElementVisible(toRename, 30)
		
		testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(5)  > div.ag-cell-label-container > div.ag-header-cell-label")
		toRename = new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-pinned-right-cols-container > div[row-index=\"" + halfResults + "\"] > div:nth-child(4)", true)
		WebUI.waitForElementVisible(toRename, 30)
		
		String cpaPcValue = testcase.selenium.getText("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-pinned-right-cols-container > div[row-index=\"" + halfResults + "\"] > div:nth-child(4)")
		cpaPcValue = Integer.parseInt(cpaPcValue.replace('€', '')) + 0.01
		testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(5)  > div.ag-cell-label-container > div.ag-header-cell-label")
		testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(5)  > div.ag-cell-label-container > div.ag-header-cell-label")
		testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(3) > div:nth-child(5) > div:nth-child(2) > button")
		testcase.selenium.select("css=div.ag-filter > div > div > div:nth-child(1) > select", "Less than")
		testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div > input", cpaPcValue)
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "focus")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "input")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "blur")
		Thread.sleep(pause1500)
		testcase.selenium.click("css=#applyButton")
		testcase.selenium.click("css=div.ag-menu > div > div > span")
		Thread.sleep(pause3000)
		testcase.selenium.click("css=div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-collapsed > span")
		testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-right-header\").style.display = \"none\"")
		testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-right-floating-top\").style.display = \"none\"")
		testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-right-cols-container\").style.display = \"none\"")
		testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
		testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)")
		toRename = new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "div.ag-center-cols-container > div:last-of-type > div", true)
		WebUI.waitForElementNotPresent(toRename, 30)
		testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)")
		WebUI.waitForElementPresent(new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "div.ag-center-cols-container > div:last-of-type > div", true), 30)
		testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
	}
	
	//KatalonRecorder Check report columns Testcase
	CustomKeywords.'summaryLine.SummaryLine.checkReportColumnsFilterData'(testcase.selenium, GlobalVariable.dateRange, 16, highlighting)
	
	//KatalonRecorder Remove Filter Pacing Amount Testcase
	pause1500 = 0
	if(GlobalVariable.template.equals("Line")) {
		pause1500 = 1500
		testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-right-header\").style.display = \"initial\"")
		testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-right-floating-top\").style.display = \"initial\"")
		testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-right-cols-container\").style.display = \"initial\"")
		testcase.selenium.click("css=div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-expanded > span")
		testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(3) > div:nth-child(5) > div:nth-child(2) > button")
		testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "")
		Thread.sleep(pause1500)
		testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "focus")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "input")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "blur")
		testcase.selenium.click("css=#applyButton")
		testcase.selenium.click("css=div.ag-menu > div > div > span")
		testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
		testcase.selenium.click("css=div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-collapsed > span")
		testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
	}

	//KatalonRecorder Filter column Bid strategy value Testcase
	pause1500 = 0
	pause3000 = 0
	if(GlobalVariable.template.equals("Line")) {
		pause1500 = 1500
		pause3000 = 3000
		testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-right-header\").style.display = \"initial\"")
		testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-right-floating-top\").style.display = \"initial\"")
		testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-right-cols-container\").style.display = \"initial\"")
		testcase.selenium.click("css=div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-expanded > span")
		numberOfResults = testcase.selenium.getCssCount("ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(2) > div > div > div")
		halfResults = Math.round(numberOfResults / 2)
		toRename = new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(8) > div.ag-cell-label-container > div.ag-header-cell-label", true)
		WebUI.waitForElementVisible(toRename, 30)
		
		toRename = new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-pinned-right-cols-container > div[row-index=\"" + halfResults + "\"] > div:nth-child(7)", true)
		WebUI.waitForElementVisible(toRename, 30)
		
		testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(8) > div.ag-cell-label-container > div.ag-header-cell-label")
		String bidStrategyValueFirstValue = testcase.selenium.getText("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-pinned-right-cols-container > div[row-index=\"" + halfResults + "\"] > div:nth-child(7)")
		bidStrategyValueFirstValue = Float.parseFloat(bidStrategyValueFirstValue.replace('€', '')) - 0.01
		testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(8) > div.ag-cell-label-container > div.ag-header-cell-label")
		String bidStrategyValueSecondValue = testcase.selenium.getText("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-pinned-right-cols-container > div[row-index=\"0\"] > div:nth-child(7)")
		bidStrategyValueSecondValue = Float.parseFloat(bidStrategyValueSecondValue.replace('€', '')) - 0.01
		testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-pinned-right-cols-container > div[row-index=\"" + halfResults + "\"] > div:nth-child(8)")
		testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(3) > div:nth-child(8) > div:nth-child(2) > button")
		testcase.selenium.select("css=div.ag-filter > div > div > div:nth-child(1) > select", "In range")
		testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(1) > input", bidStrategyValueFirstValue)
		testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(2) > input", bidStrategyValueSecondValue)
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(1) > input", "focus")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(1) > input", "input")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(2) > input", "focus")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(2) > input", "input")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(2) > input", "blur")
		Thread.sleep(pause1500)
		testcase.selenium.click("css=#applyButton")
		testcase.selenium.click("css=div.ag-menu > div > div > span")
		testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
		Thread.sleep(pause3000)
		testcase.selenium.click("css=div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-collapsed > span")
		testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-right-header\").style.display = \"none\"")
		testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-right-floating-top\").style.display = \"none\"")
		testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-right-cols-container\").style.display = \"none\"")
		testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)")
		toRename = new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "div.ag-center-cols-container > div:last-of-type > div", true)
		WebUI.waitForElementNotPresent(toRename, 30)
		testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)")
		WebUI.waitForElementPresent(new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "div.ag-center-cols-container > div:last-of-type > div", true), 30)
		testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
	}
	
	//KatalonRecorder Check report columns Testcase
	CustomKeywords.'summaryLine.SummaryLine.checkReportColumnsFilterData'(testcase.selenium, GlobalVariable.dateRange, 16, highlighting)
	
	//KatalonRecorder Remove Filter Bid strategy value Testcase
	pause1500 = 0
	if(GlobalVariable.template.equals("Line")) {
		pause1500 = 1500
		testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-right-header\").style.display = \"initial\"")
		testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-right-floating-top\").style.display = \"initial\"")
		testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-right-cols-container\").style.display = \"initial\"")
		testcase.selenium.click("css=div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-expanded > span")
		testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(3) > div:nth-child(3) > div:nth-child(8) > div:nth-child(2) > button")
		testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(1) > input", "")
		testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(2) > input", "")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(1) > input", "focus")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(1) > input", "input")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(2) > input", "focus")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(2) > input", "input")
		testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(2) > input", "blur")
		Thread.sleep(pause1500)
		testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(1) > input", "")
		testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div:nth-of-type(2) > input", "")
		testcase.selenium.click("css=#applyButton")
		testcase.selenium.click("css=div.ag-menu > div > div > span")
		testcase.selenium.click("css=div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-collapsed > span")
		testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
		futureIndex++
		testcase.selenium.click("css=div.ag-primary-cols-list-panel > div:nth-of-type(" + futureIndex + ") > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)")
		testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
	}
}

//KatalonRecorder Filter column Dimension 1 Testcase

testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
testcase.selenium.click("css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-unchecked:not(.ag-hidden)")
testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-left-header\").style.display = \"initial\"")
testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-left-floating-top\").style.display = \"initial\"")
testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-left-cols-container\").style.display = \"initial\"")
String firstLineItemName = testcase.selenium.getText("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div[row-index=\"0\"]")
Pattern pattern = Pattern.compile("(.*) \\(\\d*\\)")
Matcher matcher = pattern.matcher(firstLineItemName)
matcher.find()
String truncatedFirstLineItemName = matcher.group(1)
testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(2) > button")
testcase.selenium.select("css=div.ag-filter > div > div > div:nth-child(1) > select", "Contains")
testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div > input", truncatedFirstLineItemName)
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "focus")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "input")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "blur")
Thread.sleep(1500)
testcase.selenium.click("css=#applyButton")
testcase.selenium.click("css=div.ag-menu > div > div > span")
Thread.sleep(3000)
testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-left-header\").style.display = \"none\"")
testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-left-floating-top\").style.display = \"none\"")
testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-left-cols-container\").style.display = \"none\"")
//testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
testcase.selenium.click("css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)")
if(GlobalVariable.template.equals("Line")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)")
} else if(GlobalVariable.template.equals("URL App") || GlobalVariable.template.equals("Exchange") || GlobalVariable.template.equals("Inventory Source")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(6) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)")
} else if(GlobalVariable.template.equals("Creative")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(7) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)")
}
toRename = new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "div.ag-center-cols-container > div:last-of-type > div", true)
WebUI.waitForElementNotPresent(toRename, 30)

if(GlobalVariable.template.equals("Line")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)")
} else if(GlobalVariable.template.equals("URL App") || GlobalVariable.template.equals("Exchange") || GlobalVariable.template.equals("Inventory Source")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(6) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)")
} else if(GlobalVariable.template.equals("Creative")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(7) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)")
}
WebUI.waitForElementPresent(new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "div.ag-center-cols-container > div:last-of-type > div", true), 30)
testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")

//KatalonRecorder Check report columns Testcase
CustomKeywords.'summaryLine.SummaryLine.checkReportColumnsFilterData'(testcase.selenium, GlobalVariable.dateRange, 16, highlighting)

//KatalonRecorder Remove Filter Dimension 1 Testcase
testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
testcase.selenium.click("css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-unchecked:not(.ag-hidden)")
testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-left-header\").style.display = \"initial\"")
testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-left-floating-top\").style.display = \"initial\"")
testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-left-cols-container\").style.display = \"initial\"")
testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(2) > button")
testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "focus")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "input")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "blur")
Thread.sleep(1500)
testcase.selenium.click("css=#applyButton")
testcase.selenium.click("css=div.ag-menu > div > div > span")
testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-left-header\").style.display = \"none\"")
testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-left-floating-top\").style.display = \"none\"")
testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-left-cols-container\").style.display = \"none\"")
testcase.selenium.click("css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)")

//KatalonRecorder Filter column Dimension 2 Testcase
testcase.selenium.click("css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-unchecked:not(.ag-hidden)")
testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-left-header\").style.display = \"initial\"")
testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-left-floating-top\").style.display = \"initial\"")
testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-left-cols-container\").style.display = \"initial\"")
String firstIOName = testcase.selenium.getText("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div[row-index=\"0\"] > div[col-id^=\"oiName\"]")
pattern = Pattern.compile("(.*) \\(\\d*\\)")
matcher = pattern.matcher(firstIOName)
matcher.find()
String truncatedFirstIOName = matcher.group(1)
testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(2) > div > div:nth-child(3) > div:nth-of-type(" + ioColumnPosition + ") > div:nth-child(2) > button")
testcase.selenium.select("css=div.ag-filter > div > div > div:nth-child(1) > select", "Not contains")
testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div > input", truncatedFirstIOName)
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "focus")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "input")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "blur")
Thread.sleep(1500)
testcase.selenium.click("css=#applyButton")
testcase.selenium.click("css=div.ag-menu > div > div > span")
Thread.sleep(3000)
testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-left-header\").style.display = \"none\"")
testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-left-floating-top\").style.display = \"none\"")
testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-left-cols-container\").style.display = \"none\"")
testcase.selenium.click("css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)")
if(GlobalVariable.template.equals("Line")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)")
} else if(GlobalVariable.template.equals("URL App") || GlobalVariable.template.equals("Exchange") || GlobalVariable.template.equals("Inventory Source")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(6) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)")
} else if(GlobalVariable.template.equals("Creative")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(7) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)")
}
toRename = new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "div.ag-center-cols-container > div:last-of-type > div", true)
WebUI.waitForElementNotPresent(toRename, 30)

if(GlobalVariable.template.equals("Line")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)")
} else if(GlobalVariable.template.equals("URL App") || GlobalVariable.template.equals("Exchange") || GlobalVariable.template.equals("Inventory Source")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(6) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)")
} else if(GlobalVariable.template.equals("Creative")) {
testcase.selenium.click("css=div.ag-toolpanel-indent-0:nth-of-type(7) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)")
}
boolean isResultPresent = testcase.selenium.isElementPresent('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(1)')
if(isResultPresent == true) {
WebUI.waitForElementPresent(new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "div.ag-center-cols-container > div:last-of-type > div", true), 30)
testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")


//KatalonRecorder Check report columns Testcase
	CustomKeywords.'summaryLine.SummaryLine.checkReportColumnsFilterData'(testcase.selenium, GlobalVariable.dateRange, 16, highlighting)
} else {
	testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
}

//KatalonRecorder Remove Filter Dimension 2 Testcase
testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(1) > button")
testcase.selenium.click("css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-unchecked:not(.ag-hidden)")
testcase.selenium.runScript("document.querySelector(\"div.ag-pinned-left-header\").style.display = \"initial\"")
testcase.selenium.runScript("document.querySelector(\".ag-floating-top > div.ag-pinned-left-floating-top\").style.display = \"initial\"")
testcase.selenium.runScript("document.querySelector(\"div.ag-body-viewport > div.ag-pinned-left-cols-container\").style.display = \"initial\"")
testcase.selenium.click("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(1) > div:nth-child(2) > div > div:nth-child(3) > div:nth-of-type(" + ioColumnPosition + ") > div:nth-child(2) > button")
testcase.selenium.type("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "focus")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "input")
testcase.selenium.fireEvent("css=div.ag-filter > div > div > div:nth-child(2) > div > input", "blur")
Thread.sleep(1500)
testcase.selenium.click("css=#applyButton")
testcase.selenium.click("css=div.ag-menu > div > div > span")
