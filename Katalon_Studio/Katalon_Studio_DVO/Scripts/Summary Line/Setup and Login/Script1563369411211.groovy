import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.WebElement

import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium as WebDriverBackedSelenium

import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('https://www.katalon.com/')

def driver = DriverFactory.getWebDriver()

String currentWebsite = GlobalVariable.env2 + '.tradelab.fr'

String baseUrl = ((('https://' + GlobalVariable.env) + '-') + currentWebsite) + '/'

selenium = new WebDriverBackedSelenium(driver, baseUrl)

WebUI.waitForPageLoad(30)

WebUI.maximizeWindow()

WebUI.navigateToUrl(((('https://' + GlobalVariable.env) + '-') + GlobalVariable.env2) + '.tradelab.fr/')

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

WebUI.delay(2)

WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

WebUI.click(findTestObject('Object Repository/1. Login/Login Button'))

WebUI.delay(2)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/1. Top Bar/Menu button'))

String url = (((('https://' + GlobalVariable.env) + '-') + GlobalVariable.env2) + '.tradelab.fr/') + GlobalVariable.advertiser

WebUI.navigateToUrl(url)

WebUI.waitForPageLoad(30)

WebUI.delay(2)

TestObject menu = new TestObject('menu').addProperty('css', ConditionType.EQUALS, 'mat-toolbar > button:nth-child(1) > span > i', 
    true)

WebUI.waitForElementVisible(menu, 30)

selenium.click('css=mat-toolbar > button:nth-child(1) > span > i')

if (GlobalVariable.template.equals('Line')) {
    TestObject lineTemplate = new TestObject('lineTemplate').addProperty('css', ConditionType.EQUALS, 'mat-sidenav > div > nav > ul > li:nth-child(1)', 
        true)

    WebUI.waitForElementVisible(lineTemplate, 30)

    selenium.click('css=mat-sidenav > div > nav > ul > li:nth-child(1)')
} else if (GlobalVariable.template.equals('Creative')) {
    TestObject creativeTemplate = new TestObject('creativeTemplate').addProperty('css', ConditionType.EQUALS, 'mat-sidenav > div > nav > ul > li:nth-child(2)', 
        true)

    WebUI.waitForElementVisible(creativeTemplate, 30)

    selenium.click('css=mat-sidenav > div > nav > ul > li:nth-child(2)')
} else if (GlobalVariable.template.equals('URL App')) {
    TestObject urlAppTemplate = new TestObject('urlAppTemplate').addProperty('css', ConditionType.EQUALS, 'mat-sidenav > div > nav > ul > li:nth-child(3)', 
        true)

    WebUI.waitForElementVisible(urlAppTemplate, 30)

    selenium.click('css=mat-sidenav > div > nav > ul > li:nth-child(3)')
} else if (GlobalVariable.template.equals('Exchange')) {
    TestObject exchangeTemplate = new TestObject('exchangeTemplate').addProperty('css', ConditionType.EQUALS, 'mat-sidenav > div > nav > ul > li:nth-child(4)', 
        true)

    WebUI.waitForElementVisible(exchangeTemplate, 30)

    selenium.click('css=mat-sidenav > div > nav > ul > li:nth-child(4)')
} else if (GlobalVariable.template.equals('Inventory Source')) {
    TestObject inventorySourceTemplate = new TestObject('inventorySourceTemplate').addProperty('css', ConditionType.EQUALS, 
        'mat-sidenav > div > nav > ul > li:nth-child(4)', true)

    WebUI.waitForElementVisible(inventorySourceTemplate, 30)

    selenium.click('css=mat-sidenav > div > nav > ul > li:nth-child(5)')
}

int attempts = 0

int maxAttempts = 20

Thread.sleep(1500)

boolean isGridLoading = selenium.isElementPresent('css=app-progressbar > mat-progress-bar')

while (isGridLoading == true) {
    Thread.sleep(250)

    isGridLoading = selenium.isElementPresent('css=app-progressbar > mat-progress-bar')
}

boolean noRowsToShow = true

while (noRowsToShow == true) {
    boolean isErrorPresent = selenium.isElementPresent('css=snack-bar-container.mat-snack-bar-error > simple-snack-bar')

    while (isErrorPresent == true) {
        Thread.sleep(250)

        isErrorPresent = selenium.isElementPresent('css=snack-bar-container.mat-snack-bar-error > simple-snack-bar')
    }
    
    if (GlobalVariable.dateRange != 3) {
        selenium.click('css=mat-sidenav-content > div > templates > div > context > div > date-selector > div > div.date-range-selector > mat-select > div')

        selenium.click(('css=body > div.cdk-overlay-container > div.cdk-overlay-connected-position-bounding-box > div > div > mat-option:nth-child(' + 
            GlobalVariable.dateRange) + ')')

        if (GlobalVariable.dateRange == 5) {
            String currentDate = selenium.getAttribute('css=#sat-datepicker-0 > div > sat-month-view > table > tbody > tr > td:nth-child(7)@aria-label')

            String calendarStartDay = currentDate.replaceFirst('\\d+', Integer.toString(GlobalVariable.startDay))

            String calendarEndDay = currentDate.replaceFirst('\\d+', Integer.toString(GlobalVariable.endDay))

            selenium.click(('css=#sat-datepicker-0 > div > sat-month-view > table > tbody > tr > td[aria-label="' + calendarStartDay) + 
                '"] > div')

            Thread.sleep(750)

            selenium.click(('css=#sat-datepicker-0 > div > sat-month-view > table > tbody > tr > td[aria-label="' + calendarEndDay) + 
                '"] > div')

            Thread.sleep(750)
        }
        
        WebUI.executeJavaScript("document.querySelector('templates > div > context > div > date-selector > div > button').click()", [])

        Thread.sleep(1500)

        isErrorPresent = selenium.isElementPresent('css=snack-bar-container.mat-snack-bar-error > simple-snack-bar')

        while (isErrorPresent == true) {
            Thread.sleep(250)

            isErrorPresent = selenium.isElementPresent('css=snack-bar-container.mat-snack-bar-error > simple-snack-bar')
        }
        
        isGridLoading = selenium.isElementPresent('css=app-progressbar > mat-progress-bar')

        while ((isGridLoading == true) && (' + attempts + ' < +(maxAttempts))) {
            Thread.sleep(250)

            isGridLoading = selenium.isElementPresent('css=app-progressbar > mat-progress-bar')

            attempts = (attempts + 1)
        }
    }
    
    noRowsToShow = selenium.isElementPresent('css=span.ag-overlay-no-rows-center')

    if (noRowsToShow == true) {
        selenium.refresh()
    }
}
