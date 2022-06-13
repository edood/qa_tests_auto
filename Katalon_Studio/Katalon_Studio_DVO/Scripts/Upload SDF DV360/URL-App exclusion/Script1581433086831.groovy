import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*

import org.openqa.selenium.WebElement as WebElement

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

int[] rowCheckbox = GlobalVariable.itemsToExclude

int EXCLUDE_NUMBER = rowCheckbox.size()

WebUI.openBrowser('')

WebUI.waitForPageLoad(30)

WebUI.maximizeWindow()

String url = ((('https://' + GlobalVariable.env) + '-') + GlobalVariable.env2) + '.tradelab.fr/'

WebUI.navigateToUrl(url)

WebUI.delay(2)

WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

WebUI.click(findTestObject('Object Repository/1. Login/Login Button'))

WebUI.delay(2)

url = ((((('https://' + GlobalVariable.env) + '-') + GlobalVariable.env2) + '.tradelab.fr/') + GlobalVariable.advertiser)

WebUI.navigateToUrl(url)

WebUI.waitForPageLoad(30)

WebUI.delay(1)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Url-app'))

WebUI.delay(1)

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/3. Grid/Loading Progress Bar'), 60)

if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 30, FailureHandling.OPTIONAL) == true) {
    WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)
}

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)

CustomKeywords.'com.reusableComponents.OptimizerDV360.refreshAdvertiser'()

if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 30, FailureHandling.OPTIONAL) == true) {
	WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)
	WebUI.delay(2)
}

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30, FailureHandling.CONTINUE_ON_FAILURE)

CustomKeywords.'com.reusableComponents.OptimizerDV360.selectDateRange'(GlobalVariable.dateRange)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30, FailureHandling.STOP_ON_FAILURE)

WebUI.delay(2)

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/1. Filter column/First dimension filter button'))

WebUI.selectOptionByLabel(findTestObject('DV360 Optimizer/3. Grid/1. Filter column/Filter select'), 'Not contains', false)

WebUI.setText(findTestObject('DV360 Optimizer/3. Grid/1. Filter column/Filter value'), 'Low volume inventory')

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/1. Filter column/Apply button'))

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/1. Filter column/Close filter'))

String[] urlappName = new String[rowCheckbox.size()]

String[] linkedline = new String[rowCheckbox.size()]

TestObject urlappCheckbox = new TestObject('UrlAppCheckbox')

TestObject appName = new TestObject('appName')

TestObject lineName = new TestObject('lineName')

int onetime = 0

String lineNameSelector = null

String appNameSelector = null

int gridsize = 15

for (def row : rowCheckbox) {
    css = (('div[row-index="' + (row - 1)) + '"] > div> span > span.ag-selection-checkbox')

    if ((row / gridsize) >= 1) {
        int scrollnbr = gridsize

        while (scrollnbr < row) {
            WebUI.delay(1)

            WebUI.executeJavaScript(('document.querySelector(\'div[row-index="' + (scrollnbr - 1)) + '"]\').scrollIntoView({block: \'center\'})', 
                null)

            scrollnbr += gridsize
        }
    }
    
    lineNameSelector = (('div[row-index="' + (row - 1)) + '"] > div[col-id^="liName"]')

    appNameSelector = (('div[row-index="' + (row - 1)) + '"] > div[col-id^="urlAppName"]')

    if (onetime == 0) {
        urlappCheckbox.addProperty('css', ConditionType.EQUALS, css)

        lineName.addProperty('css', ConditionType.EQUALS, lineNameSelector)

        appName.addProperty('css', ConditionType.EQUALS, appNameSelector)
    } else {
        WebUI.modifyObjectProperty(urlappCheckbox, 'css', ConditionType.EQUALS.toString(), css, true)

        WebUI.modifyObjectProperty(lineName, 'css', ConditionType.EQUALS.toString(), lineNameSelector, true)

        WebUI.modifyObjectProperty(appName, 'css', ConditionType.EQUALS.toString(), appNameSelector, true)
    }
    
    (urlappName[onetime]) = WebUI.getText(appName)

    (linkedline[onetime]) = WebUI.getText(lineName)

    WebUI.click(urlappCheckbox)

    onetime++

    WebUI.executeJavaScript('document.querySelector(\'.ag-body-viewport\').scrollTo(0, 0);', null)
}

WebUI.delay(1)

KeywordUtil.logInfo(linkedline.toString())

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/BtnExclude'))

WebUI.delay(3)

String generatedfile = CustomKeywords.'com.reusableComponents.OptimizerDV360.download'()

String filecsv = CustomKeywords.'com.reusableComponents.OptimizerDV360.unzipCSVfile'(generatedfile)

CustomKeywords.'com.reusableComponents.OptimizerDV360.ConnectToDV360'(GlobalVariable.googleID, GlobalVariable.googlePassword)

CustomKeywords.'com.reusableComponents.OptimizerDV360.DV360Upload'(filecsv, linkedline[0])

int found = 0

int it_app = 0

String[] urlorapp = null

String result = null

for (def line : linkedline) {
    if ((it_app == 0) || ((it_app > 0) && ((linkedline[(it_app - 1)]) != (linkedline[it_app])))) {
        WebUI.click(findTestObject('Display Video 360/DV360 search button'))

        WebUI.setText(findTestObject('Display Video 360/input_Search dv360'), line)

        WebUI.waitForElementVisible(findTestObject('Display Video 360/Search suggestion'), 30)

        WebUI.click(findTestObject('Display Video 360/Search suggestion'), FailureHandling.STOP_ON_FAILURE)

        WebUI.delay(15)

        int it_targeting = 1

        TestObject targeting = new TestObject('targetting App & URLs')

        String targetingselector = ('div.targeting-section > targeting-editor daux-card-section:nth-child(' + it_targeting) + ')'

        targeting.addProperty('css', ConditionType.EQUALS, targetingselector + ' span')

        for (; targeting && (WebUI.getText(targeting) != 'Apps & URLs'); it_targeting++) {
            targetingselector = (('div.targeting-section > targeting-editor daux-card-section:nth-child(' + it_targeting) + ')')

            WebUI.modifyObjectProperty(targeting, 'css', ConditionType.EQUALS.toString(), targetingselector + ' span', true)
        }
        
        if (WebUI.getText(targeting) != 'Apps & URLs') {
            KeywordUtil.markFailedAndStop('Pas de targetting App & url section on dv')
        }
        
        KeywordUtil.logInfo(urlappName.toString())

        WebUI.executeJavaScript(('document.querySelector(\'' + targetingselector) + ' div.card-section-content edit-fab > material-fab\').scrollIntoView({block: \'center\'})', null)

        WebUI.executeJavaScript(('document.querySelector(\'' + targetingselector) + ' div.card-section-content edit-fab > material-fab\').click()', null)

        WebUI.waitForElementVisible(findTestObject('Display Video 360/DV360 App Editor'), 30)
    }
    
    urlorapp = (urlappName[it_app]).split(' ')

    if (urlorapp.length != 2) {
        WebUI.executeJavaScript('document.querySelector(\'dynamic-component > lobby-section > div > div > div > picker-tree:nth-child(4) > div\').click()', null)
    } else {
        WebUI.executeJavaScript('document.querySelector(\'dynamic-component > lobby-section > div > div > div > picker-tree:nth-child(3) > div\').click()', null)
    }
    
    WebUI.delay(2)

    WebUI.waitForElementVisible(findTestObject('Object Repository/Display Video 360/DV360 url app editor search input not readable'), 30)

    WebUI.click(findTestObject('Object Repository/Display Video 360/DV360 url app editor search input not readable'))

    WebUI.setText(findTestObject('Object Repository/Display Video 360/DV360 url app editor search input'), urlappName[it_app])

    WebUI.waitForElementVisible(findTestObject('Object Repository/Display Video 360/loading search'), 30)

    if (urlorapp.length != 2) {
        List<WebElement> ResultSearch = WebUI.findWebElements(findTestObject('Object Repository/Display Video 360/DV360 urlapp Editor Search Result'), 30)

        result = (ResultSearch[0]).getText()
    } else {
        result = (urlorapp[0])
    }
    
    List<WebElement> ListExcluded = WebUI.findWebElements(findTestObject('Object Repository/DV360 Optimizer/Exclude app list'), 30)


    found = 0

    for (def app : ListExcluded) {
        if (app.getText() == result) {
            found = 1
            KeywordUtil.logInfo(('urlapp ' + (urlappName[it_app])) + ' was excluded')
            break
        }
    }
    
    if (found == 0) {
        String errormessage = 'urlapp not excluded :' + (urlappName[it_app])
        KeywordUtil.markFailedAndStop(errormessage)
    }
    
    if (((it_app + 1) < linkedline.length) && ((linkedline[(it_app + 1)]) != (linkedline[it_app]))) {
        WebUI.click(findTestObject('Display Video 360/Cancel button urlapp'))
        WebUI.delay(2)
	} else {  
  		WebUI.click(findTestObject('Display Video 360/Previous Arrow UrlApp Editor'))
        WebUI.delay(1)
    }
    
    it_app++
}

String path = ((System.getProperty('user.home') + File.separator) + 'Downloads') + File.separator

CustomKeywords.'com.reusableComponents.OptimizerDV360.deleteFile'(filecsv)

CustomKeywords.'com.reusableComponents.OptimizerDV360.deleteFile'(path + generatedfile)

