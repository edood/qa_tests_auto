import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium
import editedLine.EditedLine
import internal.GlobalVariable as GlobalVariable

//=== Day of the month example (Custom) : 1 ===
String startDay = ''

//=== Day of the month example (Custom) : 15 ===
String endDay = ''

//Katalon recorder Setup + Login testcase
EditedLine testcase = new EditedLine()
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

testcase.selenium.waitForPageToLoad("30")

boolean isLoggedIn = testcase.selenium.isElementPresent('css=body > app-root > app-layout > mat-toolbar > button:nth-child(4)')

if (isLoggedIn == true)
{
    testcase.selenium.open((('https://' + GlobalVariable.env) + '-') + currentWebsite)

    Thread.sleep(2000)
	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('body > app-root > app-layout > mat-toolbar > button:nth-child(4)', 60)	    
    CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('body > app-root > app-layout > mat-toolbar > button:nth-child(4)')

	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('#cdk-overlay-0 > div > div > button', 60)	    
    CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('#cdk-overlay-0 > div > div > button')

	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'("button.validation-login", 60)
WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)
WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)
WebUI.click(findTestObject('Object Repository/1. Login/Login Button'))
WebUI.delay(2)
}
 else {
    testcase.selenium.open((((('https://' + GlobalVariable.env) + '-') + currentWebsite) + '/') + page)

    Thread.sleep(2000)

    CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'("button.validation-login", 60)
  WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)
WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)
    CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('app-login > div > section > form > button')
}

Thread.sleep(2000)
testcase.selenium.waitForPageToLoad("30")

testcase.selenium.open('https://' + GlobalVariable.env + '-' + currentWebsite + '/' + GlobalVariable.advertiser)

if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 30, FailureHandling.OPTIONAL) == true) {
    WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)
}
	CustomKeywords.'editedLine.EditedLine.waitforCssSelectorVisible'('app-layout > mat-toolbar > button:nth-child(1) > span > i', 60)
//	CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-toolbar > button:nth-child(1) > span > i')

if (GlobalVariable.template == 'Line') 
{
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

// KatalonRecorder Go to line template testcase
CustomKeywords.'editedLine.EditedLine.gotoTemplate'(1)

/*Workaround because no data
CustomKeywords.'workaround.UploadGrid.UploadFakeGrid'("C:\\Users\\canceau\\Downloads\\Line_template_20190704_130630_618103548_2583783868 v2.csv")
WebUI.delay(5)*/

//KatalonRecorder Check grid testcase
CustomKeywords.'editedLine.EditedLine.Checkgrid'(testcase.selenium, GlobalVariable.dateRange)

//KatalonRecorder Edit line testcase
CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('div.ag-pinned-right-header > div > div > div > span.ag-header-expand-icon-expanded > span')

boolean isSetupExpanded = testcase.selenium.isElementPresent('css=.ag-header-expand-icon-collapsed:not(.ag-hidden)')

while (isSetupExpanded == 0) {
    Thread.sleep(250)

  isSetupExpanded = testcase.selenium.isElementPresent('css=.ag-header-expand-icon-collapsed:not(.ag-hidden)')
}

int i = 0

Number numberOfResults = testcase.selenium.getCssCount('ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(2) > div > div > div')

int[] halfResultsIndexes = [Math.floor(numberOfResults / 2), (Math.floor(numberOfResults / 2) + 1)]

while (i < 2) {
	int currentLineIndex = halfResultsIndexes[i]
	WebUI.delay(1)
    String oldStatus = testcase.selenium.getText(('css=div.ag-body-viewport div[row-index="' + currentLineIndex) + '"] > div[col-id^="status"]')

    testcase.selenium.doubleClick(('css=div.ag-body-viewport div[row-index="' + currentLineIndex) + '"] > div[col-id^="status"]')

    if (oldStatus == 'Active') {
        testcase.selenium.select(('css=div.ag-body-viewport div[row-index="' + currentLineIndex) + '"] > div[col-id^="status"] > div > select', 
            'value=Paused')
    } else {
        testcase.selenium.select(('css=div.ag-body-viewport div[row-index="' + currentLineIndex) + '"] > div[col-id^="status"] > div > select', 
            'value=Active')
    }
    
    i++
}

CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-slide-toggle > label > div')

numberOfResults = testcase.selenium.getCssCount('ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div > div > div')

CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('div.ag-pinned-right-header > div > div > div > span:nth-child(3) > span')

String firstEditedLineName = testcase.selenium.getText('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div[row-index="0"] > div[col-id^="liName"]')

String secondEditedLineName = testcase.selenium.getText('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div[row-index="1"] > div[col-id^="liName"]')

testcase.selenium.doubleClick('css=div.ag-header-cell[col-id^="liName"] > div.ag-header-cell-resize')

testcase.selenium.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div[row-index="0"] > div[col-id^="liName"]')

testcase.selenium.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div[row-index="1"] > div[col-id^="liName"]')

Thread.sleep(2000)

// KatalonRecorder Go to Creative Template testcase
CustomKeywords.'editedLine.EditedLine.gotoTemplate'(2)

//KatalonRecorder Check grid testcase

CustomKeywords.'editedLine.EditedLine.Checkgrid'(testcase.selenium, GlobalVariable.dateRange)

// Check Cross-Template Edited Lines
CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('div[col-id^="liName"] > div.ag-cell-label-container > div')

boolean canScroll = true

Number numberOfRows = testcase.selenium.getCssCount('ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div')

 index = 0

while (index < numberOfRows && canScroll == 1) 
{
   canScroll = testcase.selenium.isElementPresent(('css=div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + 
        index) + '\']')

    if (canScroll == 1) {
        if (index % 25 == 0 && canScroll == 1) {
            testcase.selenium.runScript(('document.querySelector("div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + 
                index) + '\']").scrollIntoView()')
        }
        
        testcase.selenium.highlight(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-pinned-left-cols-container > div[row-index="' + 
            index) + '"] > div[col-id^="liName"]')

        String currentLineItemName = testcase.selenium.getText(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-pinned-left-cols-container > div[row-index="' + 
            index) + '"] > div[col-id^="liName"]')

        String isFirstEditedLineName = testcase.selenium.getEval('storedVars[\'currentLineItemName\'] == storedVars[\'firstEditedLineName\']')

        String isSecondEditedLineName = testcase.selenium.getEval('storedVars[\'currentLineItemName\'] == storedVars[\'secondEditedLineName\']')

        if (isFirstEditedLineName == 0) {
            assertEquals('true', testcase.selenium.getEval(isSecondEditedLineName))
        }
        
      index++
    }
}

testcase.selenium.runScript('document.querySelector(\'.ag-body-viewport\').scrollTo(0, 0);')

//KatalonRecorder Check grid testcase
CustomKeywords.'editedLine.EditedLine.Checkgrid'(testcase.selenium, GlobalVariable.dateRange)

//KatalonRecorder Go to url/app template
CustomKeywords.'editedLine.EditedLine.gotoTemplate'(3)

//KatalonRecorder Check grid testcase
CustomKeywords.'editedLine.EditedLine.Checkgrid'(testcase.selenium, GlobalVariable.dateRange)

//KatalonRecorder Check Cross Template Edited Lines testcase
CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('div[col-id^="liName"] > div.ag-cell-label-container > div')

canScroll = true

numberOfRows = testcase.selenium.getCssCount('ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div')

index = 0

while (index < numberOfRows && canScroll == 1) {
    canScroll = testcase.selenium.isElementPresent(('css=div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + 
        index) + '\']')

    if (canScroll == 1) {
        if (index % 25 == 0 && canScroll == 1) {
            testcase.selenium.runScript(('document.querySelector("div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + 
                index) + '\']").scrollIntoView()')
        }
        
        testcase.selenium.highlight(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + 
            index) + '"] > div[col-id^="liName"]')

        currentLineItemName = testcase.selenium.getText(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + 
            index) + '"] > div[col-id^="liName"]')

        isFirstEditedLineName = testcase.selenium.getEval('storedVars[\'currentLineItemName\'] == storedVars[\'firstEditedLineName\']')

        isSecondEditedLineName = testcase.selenium.getEval('storedVars[\'currentLineItemName\'] == storedVars[\'secondEditedLineName\']')

        if (isFirstEditedLineName == 0) {
            assertEquals('true', testcase.selenium.getEval(isSecondEditedLineName))
        }
        
        index++
    }
}

testcase.selenium.runScript('document.querySelector(\'.ag-body-viewport\').scrollTo(0, 0);')

//KatalonRecorder go to exchange template testcase
CustomKeywords.'editedLine.EditedLine.gotoTemplate'(4)

//KatalonRecorder Check grid testcase
CustomKeywords.'editedLine.EditedLine.Checkgrid'(testcase.selenium, GlobalVariable.dateRange)

//KatalonRecorder Check Cross Template Edited Lines testcase
CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('div[col-id^="liName"] > div.ag-cell-label-container > div')

canScroll = true

numberOfRows = testcase.selenium.getCssCount('ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div')

index = 0

while (index < numberOfRows && canScroll == 1) {
    canScroll = testcase.selenium.isElementPresent(('css=div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + 
        index) + '\']')

    if (canScroll == 1) {
        if (index % 25 == 0 && canScroll == 1) {
            testcase.selenium.runScript(('document.querySelector("div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + 
                index) + '\']").scrollIntoView()')
        }
        
        testcase.selenium.highlight(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + 
            index) + '"] > div[col-id^="liName"]')

        currentLineItemName = testcase.selenium.getText(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + 
            index) + '"] > div[col-id^="liName"]')

        isFirstEditedLineName = testcase.selenium.getEval('storedVars[\'currentLineItemName\'] == storedVars[\'firstEditedLineName\']')

        isSecondEditedLineName = testcase.selenium.getEval('storedVars[\'currentLineItemName\'] == storedVars[\'secondEditedLineName\']')

        if (isFirstEditedLineName == 0) {
            assertEquals('true', testcase.selenium.getEval(isSecondEditedLineName))
        }
        
        index++
    }
}

testcase.selenium.runScript('document.querySelector(\'.ag-body-viewport\').scrollTo(0, 0);')

// KatalonRecorder Go to Inventory Source template testcase
CustomKeywords.'editedLine.EditedLine.gotoTemplate'(5)

//KatalonRecorder Check grid testcase
CustomKeywords.'editedLine.EditedLine.Checkgrid'(testcase.selenium, GlobalVariable.dateRange)

//KatalonRecorder Check Cross Template Edited Lines testcase
CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('div[col-id^="liName"] > div.ag-cell-label-container > div')

canScroll = true

numberOfRows = testcase.selenium.getCssCount('ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div')

index = 0

while (index < numberOfRows && canScroll == 1) {
    canScroll = testcase.selenium.isElementPresent(('css=div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + 
        index) + '\']')

    if (canScroll == 1) {
        if (index % 25 == 0 && canScroll == 1) {
            testcase.selenium.runScript(('document.querySelector("div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + 
                index) + '\']").scrollIntoView()')
        }
        
        testcase.selenium.highlight(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + 
            index) + '"] > div[col-id^="liName"]')

        currentLineItemName = testcase.selenium.getText(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + 
            index) + '"] > div[col-id^="liName"]')

        isFirstEditedLineName = testcase.selenium.getEval('storedVars[\'currentLineItemName\'] == storedVars[\'firstEditedLineName\']')

        isSecondEditedLineName = testcase.selenium.getEval('storedVars[\'currentLineItemName\'] == storedVars[\'secondEditedLineName\']')

        if (isFirstEditedLineName == 0) {
            assertEquals('true', testcase.selenium.getEval(isSecondEditedLineName))
        }
        
        index++
    }
}

testcase.selenium.runScript('document.querySelector(\'.ag-body-viewport\').scrollTo(0, 0);')

// Go to Line template testcase
CustomKeywords.'editedLine.EditedLine.gotoTemplate'(1)

/*Workaround because no data
CustomKeywords.'workaround.UploadGrid.UploadFakeGrid'("C:\\Users\\canceau\\Downloads\\Line_template_20190704_130630_618103548_2583783868 v2.csv")
WebUI.delay(5)*/

//KatalonRecorder Check grid testcase
CustomKeywords.'editedLine.EditedLine.Checkgrid'(testcase.selenium, GlobalVariable.dateRange)

//KatalonRecorder Check Edited lines still present testcase
testcase.selenium.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2)')

assertEquals(firstEditedLineName, testcase.selenium.getText('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2)'))

testcase.selenium.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2)')

assertEquals(secondEditedLineName, testcase.selenium.getText('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2)'))

//KatalonRecorder Remove edit lines testcase
if (GlobalVariable.template == 'Line') {
    CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-slide-toggle > label > div')
}

