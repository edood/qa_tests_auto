import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.By as By
import org.openqa.selenium.WebElement as WebElement

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium as WebDriverBackedSelenium

import internal.GlobalVariable as GlobalVariable
import summaryLine.SummaryLine as SummaryLine

//=== Day of the month example (Custom) : 1 ===
String startDay = ''

//=== Day of the month example (Custom) : 15 ===
String endDay = ''

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

CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('app-layout > mat-toolbar > button:nth-child(1) > span > i', 
    60)

CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-toolbar > button:nth-child(1) > span > i')

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

//KatalonRecorder Rearrangegrid
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

if (GlobalVariable.template == 'Line') {
    String mandatoryColumnsNumber = 16
    String revenueColumnPosition = 6
} else if (GlobalVariable.template == 'Creative') {
    String mandatoryColumnsNumber = 18
    String revenueColumnPosition = 8
} else if (((GlobalVariable.template == 'URL App') || (GlobalVariable.template == 'Exchange')) || (GlobalVariable.template == 'Inventory Source')) {
    String mandatoryColumnsNumber = 17
    String revenueColumnPosition = 7
}

testcase.selenium.click('css=.ag-icon-columns')

//KatalonRecorder Check report columns
CustomKeywords.'summaryLine.SummaryLine.CheckReportColumns'(testcase.selenium, GlobalVariable.highlighting, testcase.tmp)

// KatalonRecorder Search column
testcase.selenium.type('css=div.header > div.input-container > input', secondLineItemName)

Thread.sleep(2000)

//KatalonRecorder Check report columns
CustomKeywords.'summaryLine.SummaryLine.CheckReportColumns'(testcase.selenium, GlobalVariable.highlighting, testcase.tmp)


//KatalonRecorder Remove search
testcase.selenium.type('css=div.header > div.input-container > input', '')

Thread.sleep(2000)

//KatalonRecorder Edit line
if (GlobalVariable.template == 'Line') {
	testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(2) > button")
	WebUI.waitForElementPresent(new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "custom-columns-panel > div > div", true), 30)
	int numberOfCustomColumns = testcase.selenium.getCssCount("custom-columns-panel > div > div > ul > li > mat-icon.remove")
	
	testcase.selenium.click("css=div.ag-side-buttons > div:nth-child(3) > button")
	WebUI.waitForElementPresent(new TestObject("tempObject").addProperty("css", ConditionType.EQUALS, "meta-pixels-panel > div", true), 30)
	int numberOfMetaPixels = testcase.selenium.getCssCount("meta-pixels-panel > div > ul > li")
	int numberOfColumnsToRemove = numberOfCustomColumns + numberOfMetaPixels
	
    testcase.selenium.click('css=div.ag-side-buttons > div:nth-child(1) > button')

    List<WebElement> panelbox = testcase.driver.findElements(By.cssSelector('div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-unchecked:not(.ag-hidden)'))

    WebElement checkbox = panelbox.get(panelbox.size() - 1 - numberOfColumnsToRemove)

    checkbox.click()
	
	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-expanded', 60)
    testcase.selenium.click('css=div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-expanded')

    boolean isSetupExpanded = testcase.selenium.isElementPresent('css=.ag-header-expand-icon-collapsed:not(.ag-hidden)')

    while (isSetupExpanded == 0) {
        Thread.sleep(250)

        isSetupExpanded = testcase.selenium.isElementPresent('css=.ag-header-expand-icon-collapsed:not(.ag-hidden)')
    }
    
    int i = 0

    while (i < 2) {
        String oldStatus = testcase.selenium.getText(('css=div.ag-body-viewport div[row-index="' + i) + '"] > div[col-id^="status"]')

        testcase.selenium.doubleClick(('css=div.ag-body-viewport div[row-index="' + i) + '"] > div[col-id^="status"]')

        if (oldStatus == 'Active') {
            testcase.selenium.select(('css=div.ag-body-viewport div[row-index="' + i) + '"] > div[col-id^="status"] > div > select', 
                'value=Paused')
        } else {
            testcase.selenium.select(('css=div.ag-body-viewport div[row-index="' + i) + '"] > div[col-id^="status"] > div > select', 
                'value=Active')
        }
        
        i++
    }
    
    testcase.selenium.click('css=mat-slide-toggle > label > div')

    int numberOfResults = testcase.selenium.getCssCount('ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div > div > div')

    testcase.selenium.click('css=div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-collapsed > span')

	if (GlobalVariable.highlighting == true)
	{
	testcase.selenium.highlight('css=div.ag-pinned-left-header')
	testcase.selenium.highlight('css=.ag-floating-top > div.ag-pinned-left-floating-top')
	testcase.selenium.highlight('css=div.ag-body-viewport > div.ag-pinned-left-cols-container')
	}
    Thread.sleep(1000)

    testcase.selenium.runScript('document.querySelector("div.ag-pinned-right-header").style.display = "none"')

    testcase.selenium.runScript('document.querySelector(".ag-floating-top > div.ag-pinned-right-floating-top").style.display = "none"')

    testcase.selenium.runScript('document.querySelector("div.ag-body-viewport > div.ag-pinned-right-cols-container").style.display = "none"')

    testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(2) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)')

    testcase.selenium.runScript('document.querySelector("div.ag-body-viewport > div.ag-pinned-left-cols-container").style.display = "initial"')

    for (int second = 0; ; second++) {
        if (second >= 60) {
            throw 'timeout'
        }
        
        try {
            if (testcase.selenium.isVisible('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div[col-id^="liName"]')) {
                break
            }
        }
        catch (Exception e) {
        } 
        
        Thread.sleep(1000)
    }
    
    String firstEditedLineName = testcase.selenium.getText('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div[row-index="0"] > div[col-id^="liName"]')

    String secondEditedLineName = testcase.selenium.getText('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div[row-index="1"] > div[col-id^="liName"]')

    Thread.sleep(2000)

    testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(2) > span.ag-column-select-checkbox > span.ag-checkbox-checked:not(.ag-hidden)')

    testcase.selenium.runScript('document.querySelector("div.ag-body-viewport > div.ag-pinned-left-cols-container").style.display = "none"')

    testcase.selenium.click('css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')

    if (GlobalVariable.template == 'Line') {
        testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(5) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)')
    } else if (((GlobalVariable.template == 'URL App') || (GlobalVariable.template == 'Exchange')) || (GlobalVariable.template == 'Inventory Source')) {
        testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(6) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)')
    } else if (GlobalVariable.template == 'Creative') {
        testcase.selenium.click('css=div.ag-toolpanel-indent-0:nth-of-type(7) > span.ag-column-select-checkbox > span.ag-checkbox-unchecked:not(.ag-hidden)')
    }
}

//KatalonRecorder Check report columns
CustomKeywords.'summaryLine.SummaryLine.CheckReportColumns'(testcase.selenium, GlobalVariable.highlighting, testcase.tmp)

//KatalonRecorder Remove Edit Lines
if (GlobalVariable.template == 'Line') {
    testcase.selenium.click('css=mat-slide-toggle > label > div')
}

