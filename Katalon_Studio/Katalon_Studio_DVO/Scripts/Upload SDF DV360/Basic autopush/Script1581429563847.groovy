import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

int SETUP_COLUMN_NUMBER = 9

int MAIL_NUMBER = 21

WebUI.openBrowser('')

WebUI.waitForPageLoad(30)

WebUI.maximizeWindow()

String url = ('https://' + GlobalVariable.env) + '-' + GlobalVariable.env2 + '.tradelab.fr/'

WebUI.navigateToUrl(url)

WebUI.delay(2)

WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

WebUI.click(findTestObject('Object Repository/1. Login/Login Button'))

WebUI.delay(2)

url += GlobalVariable.advertiser

WebUI.navigateToUrl(url)

WebUI.waitForPageLoad(30)

if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 30, FailureHandling.OPTIONAL) == true) {
    WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)
}


WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Synchro button'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Lock advertiser message'), 30)

WebUI.verifyElementText(findTestObject('DV360 Optimizer/Lock advertiser message'), 'Advertiser\'s data is currently being synchronized\nYou will be able to edit data for this advertiser once data synchronization will be completed.')

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Toast message'), 180)

WebUI.verifyElementText(findTestObject('DV360 Optimizer/Toast message'), 'Data synchronization successfull. The page will be automatically refreshed in 5 seconds')

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)

WebUI.delay(3)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Lock advertiser message'), 15)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Menu button'))

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)

String line = WebUI.getText(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))

WebUI.click(findTestObject('DV360 Optimizer/Expand Setup tab'))

WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridSetupFirstRowFirstCol'))

WebUI.delay(2)

WebUI.click(findTestObject('DV360 Optimizer/2. Setup/Basic autopush/Select paused'))

WebUI.waitForElementNotHasAttribute(findTestObject('DV360 Optimizer/1. Top Bar/Apply optimization button'), 'disabled', 30, FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Apply optimization button'))

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Apply optimization button'))

if (GlobalVariable.autopush == true)
{
WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Lock advertiser message'), 30)

WebUI.verifyElementAttributeValue(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Status'), 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height ag-cell-focus ag-cell-first-right-pinned edited-cell is-locked ag-cell-range-selected ag-cell-range-selected-1 ag-cell-value', 
    30)

WebUI.verifyElementAttributeValue(findTestObject('DV360 Optimizer/2. Setup/Basic autopush/SetupBudgetType'), 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value', 
    30)

WebUI.verifyElementAttributeValue(findTestObject('DV360 Optimizer/2. Setup/Basic autopush/SetupBudget'), 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value', 
    30)

WebUI.verifyElementAttributeValue(findTestObject('DV360 Optimizer/2. Setup/Basic autopush/SetupPacing'), 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value', 
    30)

WebUI.verifyElementAttributeValue(findTestObject('DV360 Optimizer/2. Setup/Basic autopush/SetupPacingAmount'), 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value', 
    30)

WebUI.verifyElementAttributeValue(findTestObject('DV360 Optimizer/2. Setup/Basic autopush/SetupPacingRate'), 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value', 
    30)

WebUI.verifyElementAttributeValue(findTestObject('DV360 Optimizer/2. Setup/Basic autopush/SetupBidStrategyType'), 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value', 
    30)

WebUI.verifyElementAttributeValue(findTestObject('DV360 Optimizer/2. Setup/Basic autopush/SetupBidStrategyValue'), 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value', 
    30)

WebUI.verifyElementAttributeValue(findTestObject('DV360 Optimizer/2. Setup/Basic autopush/SetupBidStrategyUnit'), 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value', 
    30)

WebUI.verifyElementAttributeValue(findTestObject('DV360 Optimizer/2. Setup/Basic autopush/SetupBidStrategyLimit'), 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value', 
    30)

String expectedLockAdvertiserMessage = 'Optimizations are currently being applied on DV.\nYou will be able to edit data for this advertiser once all optimizations have been taken into account.'

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))

lockAdvertiserMessage = WebUI.getText(findTestObject('DV360 Optimizer/Lock advertiser message'))

WebUI.verifyElementText(findTestObject('DV360 Optimizer/Lock advertiser message'), 'Optimizations are currently being applied on DV.\nYou will be able to edit data for this advertiser once all optimizations have been taken into account.')

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Toast message'), 900)

WebUI.verifyElementText(findTestObject('DV360 Optimizer/Toast message'), 'Optimizations files have been successfully uploaded on DV. Please check your email for more informations. You can now edit data for this advertiser. Refresh in 10 seconds')

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)

WebUI.delay(3)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Expand Setup tab'), 30)

WebUI.delay(3)

WebUI.click(findTestObject('DV360 Optimizer/Expand Setup tab'))

WebUI.delay(3)

TestObject setupobj = new TestObject('setup')

String setupselector = 'div.ag-pinned-right-cols-container > div.ag-row.ag-row-no-focus.ag-row-even.ag-row-level-0.ag-row-position-absolute.ag-row-first > div:nth-child(1)'

setupobj.addProperty('css', ConditionType.EQUALS, setupselector)

String locked = WebUI.getAttribute(setupobj, 'class')

for (int it_setup = 1; ((locked != null) && !(locked.equals('ag-cell ag-cell-not-inline-editing ag-cell-with-height ag-cell-focus ag-cell-first-right-pinned edited-cell ag-cell-range-selected ag-cell-range-selected-1 ag-cell-value'))) && 
(it_setup < SETUP_COLUMN_NUMBER); it_setup++) {
    if (locked.equals('ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value') || locked.equals(
        'ag-cell ag-cell-not-inline-editing ag-cell-with-height ag-cell-focus ag-cell-first-right-pinned edited-cell is-locked ag-cell-range-selected ag-cell-range-selected-1 ag-cell-value')) {
        println('Setup is not unlocked')

        return null
    }
    
    setupselector = (('div.ag-pinned-right-cols-container > div.ag-row.ag-row-no-focus.ag-row-even.ag-row-level-0.ag-row-position-absolute.ag-row-first > div:nth-child(' + 
    it_setup) + ')')

    setupobj = WebUI.modifyObjectProperty(setupobj, 'css', ConditionType.EQUALS.toString(), setupselector, true)

    locked = WebUI.getAttribute(setupobj, 'class')
}

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)

WebUI.verifyElementNotPresent(findTestObject('DV360 Optimizer/Lock advertiser message'), 30)

WebUI.navigateToUrl('www.gmail.com')

WebUI.waitForPageLoad(30)

WebUI.setText(findTestObject('Login Google/input_Utiliser votre compte Google_identifier'), GlobalVariable.googleID)

WebUI.click(findTestObject('Login Google/google next button'), FailureHandling.STOP_ON_FAILURE)

WebUI.waitForElementVisible(findTestObject('Login Google/input_Trop de tentatives infructueuses_password'), 
    10)

WebUI.setEncryptedText(findTestObject('Login Google/input_Trop de tentatives infructueuses_password'), 
    GlobalVariable.googlePassword)

WebUI.click(findTestObject('Login Google/span_Suivant'))

WebUI.delay(4)

int i = 1

String cssselector = ('tbody > tr:nth-child(' + i) + ') > td:nth-child(5) > div:nth-child(2) > span > span'

TestObject test = new TestObject('mail_sender')

test.addProperty('css', ConditionType.EQUALS, cssselector)

WebUI.waitForElementVisible(findTestObject('Gmail/div_mail_received'), 
    300)

WebUI.delay(2)

String Title = WebUI.getText(findTestObject('Gmail/mail_sender'))

for (i = 1; !(Title.equals('dbm_automation')); i++) {
    if (i == MAIL_NUMBER) {
        int ko = i - 1

        println(('No mail from dbm_automation in the most recent ' + ko) + ' mails')

        break
    }
    
    cssselector = (('tbody > tr:nth-child(' + i) + ') > td:nth-child(5) > div:nth-child(2) > span > span')

    WebUI.modifyObjectProperty(test, 'css', ConditionType.EQUALS.toString(), cssselector, true)

    Title = WebUI.getAttribute(test, 'Name')
}

if (i < (MAIL_NUMBER + 1)) {
    WebUI.click(test)

    String ResultModified = WebUI.getText(findTestObject('Gmail/li_Modified - 0'))

    String ResultError = WebUI.getText(findTestObject('Gmail/li_Errors - 1'))

    WebUI.delay(1)

    if (ResultError.equals('Errors - 1')) {
        println('Upload was not successfull')

        return null
    }
    
    if (ResultModified.equals('Modified - 1')) {
        WebUI.navigateToUrl('https://displayvideo.google.com/#ng_nav/overview')

        WebUI.acceptAlert(FailureHandling.OPTIONAL)

        WebUI.waitForPageLoad(30)

        WebUI.delay(4)

        WebUI.click(findTestObject('Display Video 360/DV360 search button'))

        WebUI.setText(findTestObject('Display Video 360/input_Search dv360'), line)

        WebUI.waitForElementVisible(findTestObject('Display Video 360/Search suggestion'), 30)

        WebUI.click(findTestObject('Display Video 360/Search suggestion'), FailureHandling.STOP_ON_FAILURE)

        WebUI.waitForPageLoad(30)

        WebUI.verifyElementText(findTestObject('Display Video 360/span_Active'), 'Paused')
    }
}   
    println('end')
}