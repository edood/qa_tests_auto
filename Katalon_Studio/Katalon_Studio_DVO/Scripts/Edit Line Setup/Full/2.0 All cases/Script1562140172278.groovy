import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.SelectorMethod as SelectorMethod
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
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
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil

WebUI.openBrowser('')

WebUI.waitForPageLoad(30)

WebUI.maximizeWindow()

WebUI.navigateToUrl(((('https://' + GlobalVariable.env) + '-') + GlobalVariable.env2) + '.tradelab.fr/')

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

boolean[] linesAreEdited = [true, true, false, true, false, false, true, false, true, true, true, true, true, true, false
    , true, true, false, true, true, true, true, true, true, false, true, true, false, true, true, true, true, true, true
    , true, true, true, false, true, true, true, true, false, true, true, true, true, false, true, true, true, true, false
    , true, false, false, true, true, true, true, true, true, true, true, false, true, true, true, true, true, true, true
    , true, true, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true
    , false, true, true, true, true, true, true, true, true, true, false, true, false, false, true, true, false, false, true
    , true, true, true, false, true, true, true, false, false, true, true, true, true, false, true, true, true, false, false
    , true, true, false, true, true, true, true, true, false, false, true, true, true, true, true, true, true, true, false
    , false, true, true, true, true, false, true, true, true, false, false, true, true, true, true, false, true, true, true
    , false, false, true, true, false, true, false, false, true, true, false, false, true, true, true, true, false, true
    , true, true, false, false, true, true, true, true, true, true, true, true, false, false, true, true, true, true, true
    , true, true, true, false, false, true, true, false, true, true, true, true, true, false, false, true, true, true, true
    , true, true, true, true, false, false]

WebUI.delay(2)

WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

WebUI.click(findTestObject('Object Repository/1. Login/Login Button'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/1. Top Bar/Menu button'))

WebUI.delay(4)

String url = (((('https://' + GlobalVariable.env) + '-') + GlobalVariable.env2) + '.tradelab.fr/') + GlobalVariable.advertiser

WebUI.navigateToUrl(url)

WebUI.waitForPageLoad(30)

if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 30, FailureHandling.OPTIONAL) == true) {
    WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)
}

WebUI.waitForElementPresent(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'), 30)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'))

//int daterange = 4
//
//String option = 'body > div.cdk-overlay-container > div.cdk-overlay-connected-position-bounding-box > div > div > mat-option:nth-child(' + daterange + ')'
// 
//TestObject daterangeDynamicOption = new TestObject('DaterangeOption')
// 
//daterangeDynamicOption.addProperty('css', ConditionType.EQUALS, option)
//
//WebUI.delay(2)
//
//WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/1. Top Bar/Daterange selector'), 30)
//
//WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Daterange selector'))
// 
//WebUI.delay(1)
// 
//WebUI.click(daterangeDynamicOption)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)

String regex = 'row-index=\\"(\\d+)\\"'

int currentRowIndex = 2

TestObject lineItemNameObject = findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow')

String[][] linesAndValuesToCheck = null

linesAndValuesToCheck = new String[23][9]

for (currentRowIndex; currentRowIndex < 25; currentRowIndex++) {
    lineItemNameObject.setSelectorValue(SelectorMethod.CSS, lineItemNameObject.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(
            regex, ('row-index="' + currentRowIndex) + '"'))

    lineItemName = WebUI.getText(lineItemNameObject)

    ((linesAndValuesToCheck[(currentRowIndex - 2)])[0]) = lineItemName //	println linesAndValuesToCheck[currentRowIndex - 2][0]
}

(linesAndValuesToCheck[0]) = [(linesAndValuesToCheck[0])[0], 'Paused', 'Unlimited', 'Flight', 'Asap', 'Minimize CPC', 'Limit 1.00']

(linesAndValuesToCheck[1]) = [(linesAndValuesToCheck[1])[0], 'Active', 'Amount 1', 'Flight', 'Ahead', 'Minimize CPA']

(linesAndValuesToCheck[2]) = [(linesAndValuesToCheck[2])[0], 'Active', 'Amount 1', 'Flight', 'Even', 'Minimize CPA']

(linesAndValuesToCheck[3]) = [(linesAndValuesToCheck[3])[0], 'Active', 'Amount 1', 'Daily 1', 'Asap', 'Minimize CPA', 'Limit 1.00']

(linesAndValuesToCheck[4]) = [(linesAndValuesToCheck[4])[0], 'Paused', 'Unlimited', 'Daily 1', 'Even', 'Minimize CPC']

(linesAndValuesToCheck[5]) = [(linesAndValuesToCheck[5])[0], 'Paused', 'Unlimited', 'Flight', 'Asap', 'Beat CPC 1']

(linesAndValuesToCheck[6]) = [(linesAndValuesToCheck[6])[0], 'Active', 'Amount 1', 'Flight', 'Ahead', 'Beat CPA 1', 'Limit 1.00']

(linesAndValuesToCheck[7]) = [(linesAndValuesToCheck[7])[0], 'Active', 'Amount 1', 'Flight', 'Even', 'Beat CPA 1']

(linesAndValuesToCheck[8]) = [(linesAndValuesToCheck[8])[0], 'Active', 'Amount 1', 'Daily 1', 'Asap', 'Beat CPA 1']

(linesAndValuesToCheck[9]) = [(linesAndValuesToCheck[9])[0], 'Paused', 'Unlimited', 'Daily 1', 'Even', 'Beat CPC 1', 'Limit 1.00']

(linesAndValuesToCheck[10]) = [(linesAndValuesToCheck[10])[0], 'Paused', 'Unlimited', 'Flight', 'Asap', 'Optimize 1']

(linesAndValuesToCheck[11]) = [(linesAndValuesToCheck[11])[0], 'Active', 'Amount 1', 'Flight', 'Ahead', 'Optimize 1']

(linesAndValuesToCheck[12]) = [(linesAndValuesToCheck[12])[0], 'Active', 'Amount 1', 'Flight', 'Even', 'Optimize 1']

(linesAndValuesToCheck[13]) = [(linesAndValuesToCheck[13])[0], 'Paused', 'Unlimited', 'Daily 1', 'Asap', 'Optimize 1']

(linesAndValuesToCheck[14]) = [(linesAndValuesToCheck[14])[0], 'Active', 'Amount 1', 'Daily 1', 'Even', 'Optimize 1']

(linesAndValuesToCheck[15]) = [(linesAndValuesToCheck[15])[0], 'Active', 'Amount 1', 'Flight', 'Asap', 'Fixed 1']

(linesAndValuesToCheck[16]) = [(linesAndValuesToCheck[16])[0], 'Paused', 'Amount 1', 'Flight', 'Even', 'Fixed 1']

(linesAndValuesToCheck[17]) = [(linesAndValuesToCheck[17])[0], 'Active', 'Unlimited', 'Flight', 'Asap', 'Fixed 1']

(linesAndValuesToCheck[18]) = [(linesAndValuesToCheck[18])[0], 'Paused', 'Amount 1', 'Flight', 'Ahead', 'Fixed 1']

(linesAndValuesToCheck[19]) = [(linesAndValuesToCheck[19])[0], 'Active', 'Amount 1', 'Daily 1', 'Asap', 'Fixed 1']

(linesAndValuesToCheck[20]) = [(linesAndValuesToCheck[20])[0], 'Paused', 'Amount 1', 'Daily 1', 'Asap', 'Fixed 1']

(linesAndValuesToCheck[21]) = [(linesAndValuesToCheck[21])[0], 'Active', 'Unlimited', 'Daily 1', 'Even', 'Fixed 1']

(linesAndValuesToCheck[22]) = [(linesAndValuesToCheck[22])[0], 'Paused', 'Amount 1', 'Daily 1', 'Even', 'Fixed 1']

//WebUI.navigateToUrl('https://displayvideo.google.com/#ng_nav/overview')
//
//WebUI.setText(findTestObject('Login Google/input_Utiliser votre compte Google_identifier'), GlobalVariable.googleID)
//
//WebUI.click(findTestObject('Login Google/google next button'), FailureHandling.STOP_ON_FAILURE)
//
//WebUI.waitForElementVisible(findTestObject('Login Google/input_Trop de tentatives infructueuses_password'),
//	10)
//
//WebUI.setEncryptedText(findTestObject('Login Google/input_Trop de tentatives infructueuses_password'),
//	GlobalVariable.googlePassword)
//
//WebUI.click(findTestObject('Login Google/span_Suivant'))
//
//WebUI.delay(4)
//
//WebUI.waitForPageLoad(30)
//
//WebUI.delay(4)
//
//CustomKeywords.'com.editLineSetup.checkOnDV360.checkSetupValuesOnDV360'(linesAndValuesToCheck)

//for (lineandvalue in linesAndValuesToCheck){
//	println lineandvalue
//}
//int n = 23
//String[][] b = new String[n][9];
//int j = n
//for (int i = 0; i < n; i++) {
//	b[j - 1] = linesAndValuesToCheck[i];
//	j = j - 1;
//}
//
//linesAndValuesToCheck = b
/*printing the reversed array*/
//System.out.println("Reversed array is: \n");
//for (int k = 0; k < n; k++) {
//	System.out.println(linesAndValuesToCheck[k]);
//}
if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 1, FailureHandling.OPTIONAL) == true) {
    WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)
}

WebUI.click(findTestObject('DV360 Optimizer/Expand Setup tab'))

CustomKeywords.'com.editLineSetup.editLineSetup.editLineSetupFull'()

WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))

WebUI.waitForElementNotHasAttribute(findTestObject('DV360 Optimizer/1. Top Bar/Apply optimization button'), 'disabled', 30, FailureHandling.STOP_ON_FAILURE)

boolean isToastPresent = WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 1, FailureHandling.OPTIONAL)

while (isToastPresent == true) {
    WebUI.delay(1)

    isToastPresent = WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 1, FailureHandling.OPTIONAL)
}

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Apply optimization button'))

if (GlobalVariable.autopush == true)
{

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Lock advertiser message'), 30)

String expectedLockAdvertiserMessage = 'Optimizations are currently being applied on DV.\nYou will be able to edit data for this advertiser once all optimizations have been taken into account.'

String lockAdvertiserMessage = WebUI.getText(findTestObject('DV360 Optimizer/Lock advertiser message'))

for (attempts = 1; (attempts < 60) && !(lockAdvertiserMessage.equals(expectedLockAdvertiserMessage)); attempts++) {
    WebUI.click(findTestObject('DV360 Optimizer/2. Setup/Setup Header'))

    WebUI.delay(1)

    lockAdvertiserMessage = WebUI.getText(findTestObject('DV360 Optimizer/Lock advertiser message'))
}

WebUI.verifyElementText(findTestObject('DV360 Optimizer/Lock advertiser message'), 'Optimizations are currently being applied on DV.\nYou will be able to edit data for this advertiser once all optimizations have been taken into account.')

WebUI.waitForElementPresent(findTestObject('DV360 Optimizer/Lock advertiser message'), 30)

CustomKeywords.'com.editLineSetup.editLineSetup.verifyGridLocked'(linesAreEdited)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Toast message'), 900)

WebUI.verifyElementText(findTestObject('DV360 Optimizer/Toast message'), 'Optimizations files have been successfully uploaded on DV. Please check your email for more informations. You can now edit data for this advertiser. Refresh in 10 seconds')

WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30)

WebUI.delay(3)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)

WebUI.verifyElementNotPresent(findTestObject('DV360 Optimizer/Lock advertiser message'), 30)

WebUI.click(findTestObject('DV360 Optimizer/Expand Setup tab'))

CustomKeywords.'com.editLineSetup.editLineSetup.verifyLineSetupFull'()

int MAIL_NUMBER = 21

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

    WebUI.waitForElementVisible(findTestObject('Gmail/li_Errors - 1'), 
        30)

    WebUI.delay(2)

    WebUI.click(findTestObject('Gmail/li_Modified - 0'))

    String ResultModified = WebUI.getText(findTestObject('Gmail/li_Modified - 0'))

    WebUI.delay(2)

    WebUI.click(findTestObject('Gmail/li_Errors - 1'))

    String ResultError = WebUI.getText(findTestObject('Gmail/li_Errors - 1'))

    WebUI.delay(1)

    if (!(ResultError.equals('Errors - 0'))) {
        KeywordUtil.markFailedAndStop('Upload was not successfull')
    }
    
    if (ResultModified.equals('Modified - 23')) {
        WebUI.navigateToUrl('https://displayvideo.google.com/#ng_nav/overview')

        WebUI.acceptAlert(FailureHandling.OPTIONAL)

        WebUI.waitForPageLoad(30)

        WebUI.delay(4)

        CustomKeywords.'com.editLineSetup.checkOnDV360.checkSetupValuesOnDV360'(linesAndValuesToCheck)
    }
	}
}
