import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'com.reusableComponents.CommonKeywords.login'(false)

String templateFormat = 'xlsx'

String tiret = '-'

if (((GlobalVariable.env != 'dev') && (GlobalVariable.env != 'staging')) && (GlobalVariable.env != 'prod')) {
    GlobalVariable.env2 = 'saas'
} else if (GlobalVariable.env == 'prod') {
    tiret = ''

    GlobalVariable.env = ''
}

ArrayList<ArrayList> reports = [][[]]

reports.add(CustomKeywords.'testCases.DownloadXLS.downloadXlsApn'(templateFormat, 'daily'))

WebUI.navigateToUrl((((('https://' + GlobalVariable.env) + tiret) + GlobalVariable.env2) + '.tradelab.fr') + '/folders')

reports.add(CustomKeywords.'testCases.DownloadXLS.downloadXlsDV360'(templateFormat, 'weekly'))

WebUI.navigateToUrl((((('https://' + GlobalVariable.env) + tiret) + GlobalVariable.env2) + '.tradelab.fr') + '/folders')

reports.add(CustomKeywords.'testCases.DownloadXLS.downloadXlsCM'(templateFormat, 'monthly'))

not_run: WebUI.navigateToUrl((((('https://' + GlobalVariable.env) + tiret) + GlobalVariable.env2) + '.tradelab.fr') + '/folders')

not_run: reports.add(CustomKeywords.'testCases.DownloadXLS.downloadXlsEUL'(templateFormat, 'every 2 weeks'))

not_run: WebUI.navigateToUrl((((('https://' + GlobalVariable.env) + tiret) + GlobalVariable.env2) + '.tradelab.fr') + '/folders')

not_run: reports.add(CustomKeywords.'testCases.DownloadXLS.downloadXlsGA'(templateFormat, 'daily'))

CustomKeywords.'testCases.DownloadXLS.verifyReports'(reports, templateFormat)

