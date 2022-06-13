import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import editTargeting.Targeting as Targeting
import editTargeting.targetObject as targetObject
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint



int[] rowCheckbox = [5]
int EXCLUDE_NUMBER = 1
Targeting testcase = new Targeting()

WebUI.openBrowser('')

WebUI.waitForPageLoad(30)

WebUI.maximizeWindow()

String tiret = '-'

String url = ''

if (((GlobalVariable.env != 'dev') && (GlobalVariable.env != 'staging')) && (GlobalVariable.env != 'prod')) {
    url = (GlobalVariable.env2 = 'saas')
} else if (GlobalVariable.env == 'prod') {
    tiret = ''
}

url = (((('https://' + GlobalVariable.env) + tiret) + GlobalVariable.env2) + '.tradelab.fr/')

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

CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav-container > mat-sidenav > div > nav > ul > li:nth-child(5)')

WebUI.delay(1)

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/3. Grid/Loading Progress Bar'), 60)

if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 30, FailureHandling.OPTIONAL) == true) {
    WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)
}

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)

testcase.refreshAdvertiser()

WebUI.delay(3)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30, FailureHandling.CONTINUE_ON_FAILURE)

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Lock advertiser message'), 15)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Menu button'))

WebUI.delay(1)

CustomKeywords.'editedLine.EditedLine.clickObjectbyCss'('mat-sidenav-container > mat-sidenav > div > nav > ul > li:nth-child(5)')

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 60, FailureHandling.CONTINUE_ON_FAILURE)

testcase.selectDateRange(GlobalVariable.dateRange)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 180, FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/1. Filter column/First dimension filter button'))

WebUI.selectOptionByLabel(findTestObject('DV360 Optimizer/3. Grid/1. Filter column/Filter select'), "Not contains", false)

WebUI.setText(findTestObject('DV360 Optimizer/3. Grid/1. Filter column/Filter value'), "Unknown or Unpermissioned Inventory Source")

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/1. Filter column/Apply button'))

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/1. Filter column/Close filter'))

testcase.hideColumns(5)

String[] inventorySourceName = new String[EXCLUDE_NUMBER]

String[] linkedline = new String[EXCLUDE_NUMBER]

String[] exchange = new String[EXCLUDE_NUMBER]

TestObject inventorySourceCheckbox = new TestObject('inventorySourceCheckbox')

TestObject inventoryName = new TestObject('inventoryName')

TestObject lineName = new TestObject('lineName')

TestObject exchangeName = new TestObject('exchangeName')

int onetime = 0

String lineNameSelector = null

String inventorySourceNameSelector = null

String exchangeNameSelector = null

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

    inventorySourceNameSelector = (('div[row-index="' + (row - 1)) + '"] > div[col-id^="inventorySourceName"]')

    exchangeNameSelector = (('div[row-index="' + (row - 1)) + '"] > div[col-id^="exchangeName"]')

    println('LineSelector = ' + lineNameSelector)

    println('inventorySourceNameSelector = ' + inventorySourceNameSelector)

    println('exchangeNameSelector = ' + exchangeNameSelector)

    if (onetime == 0) {
        inventorySourceCheckbox.addProperty('css', ConditionType.EQUALS, css)

        lineName.addProperty('css', ConditionType.EQUALS, lineNameSelector)

        inventoryName.addProperty('css', ConditionType.EQUALS, inventorySourceNameSelector)

        exchangeName.addProperty('css', ConditionType.EQUALS, exchangeNameSelector)
    } else {
        WebUI.modifyObjectProperty(inventorySourceCheckbox, 'css', ConditionType.EQUALS.toString(), css, true)

        WebUI.modifyObjectProperty(lineName, 'css', ConditionType.EQUALS.toString(), lineNameSelector, true)

        WebUI.modifyObjectProperty(inventoryName, 'css', ConditionType.EQUALS.toString(), inventorySourceNameSelector, true)

        WebUI.modifyObjectProperty(exchangeName, 'css', ConditionType.EQUALS.toString(), exchangeNameSelector, true)
    }
    
    println('inventoryGetText = ' + WebUI.getText(inventoryName))

    println('lineGetText = ' + WebUI.getText(lineName))

    println('exchangeGetText = ' + WebUI.getText(exchangeName))

    (inventorySourceName[onetime]) = WebUI.getText(inventoryName)

    (linkedline[onetime]) = WebUI.getText(lineName)

    (exchange[onetime]) = WebUI.getText(exchangeName)

    WebUI.click(inventorySourceCheckbox)

    onetime++

    WebUI.executeJavaScript('document.querySelector(\'.ag-body-viewport\').scrollTo(0, 0);', null)
}

WebUI.delay(1)

if (GlobalVariable.username.equals('fbalhier')) {
    WebUI.executeJavaScript('document.querySelector(\'div.input-container\').style = \'flex : 1\'', null)
}

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/BtnExclude'))

WebUI.delay(5)

WebUI.delay(EXCLUDE_NUMBER * 2)

String generatedfile = testcase.download()

if (WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Toast message'), 30, FailureHandling.OPTIONAL) == true) {
    KeywordUtil.markFailedAndStop(WebUI.getText(findTestObject('DV360 Optimizer/Toast message')))
}

LinkedList<targetObject> result = []

for (int i = 0; i < EXCLUDE_NUMBER; i++) {
    def save = new targetObject()

    String[] str = inventorySourceName[i].split('\\(')

    if ((str[1])[0].isNumber() == true) {
        save.name = (str[0])
    } else {
        save.name = (((str[0]) + '(') + (str[1]))
    }
    
    if ((str[1])[0].isNumber() == true) {
        save.ID = (str[1])
    } else {
        save.ID = (str[2])
    }
    
    save.ID = save.ID.replace(')', '')

    save.line = (linkedline[i])

    save.target = 'Inventory Source'

    save.type = 'Exclude'

    save.parent = (exchange[i])

    result.add(save)
}

String filecsv = testcase.unzipCSVfileAndCheck(generatedfile, result)

LinkedList<targetObject> beforeupload = []

LinkedList<targetObject> afterupload = []

testcase.ConnectToDV360(GlobalVariable.googleID, GlobalVariable.googlePassword)

beforeupload = testcase.RecordTargeting(result, true)

if (testcase.DV360Upload(filecsv, result[0].line) == true) {
    afterupload = testcase.RecordTargeting(result, false)

    println('display content beforeupload :')

    for (def test : beforeupload) {
        println(test)
    }
    
    println('Fin display content beforeupload')

    println('display content afterupload :')

    for (def test : afterupload) {
        println(test)
    }
    
    println('Fin display content afterupload')

    testcase.CheckTargeting(result, beforeupload, afterupload, "Inventory Source")
}

String path = System.getProperty("user.home") + File.separator + "Downloads" + File.separator
testcase.deleteFile(filecsv)
testcase.deleteFile(path + generatedfile)
