import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-271')
String line1 = GlobalVariable.itemNames[2] + new Date().getTime()
String line2 = 'QAP-271_autonamed_' + new Date().getTime()
ArrayList<TestObject> lineselected
ArrayList<String> linenames = []
ArrayList<String> linenamesset = []

CustomKeywords.'project.Builder.Start'(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)
CustomKeywords.'com.reusableComponents.DV360.logInToDv360'(true)
CustomKeywords.'com.reusableComponents.DV360.dvSetLineStatusActive'(GlobalVariable.items[2])
WebUI.switchToWindowIndex(0)
CustomKeywords.'project.Builder.refreshAdvertiser'()
CustomKeywords.'project.Builder.load'('LI', '', '', '')
CustomKeywords.'project.Builder.SelectAllLinePanel'()
CustomKeywords.'project.Builder.VerifySelectAllLinePanel'('true', "LOADED")
for (def elem in GlobalVariable.lineTemplateNames)
	linenamesset.add(elem + new Date().getTime())
KeywordUtil.logInfo('linenameset = ' + linenamesset)
CustomKeywords.'project.Builder.CreateLinefromLoadedCampaignLoadedOI'(0, 1, GlobalVariable.lineTemplateNames, linenamesset)

for (def elem in linenamesset)
	CustomKeywords.'project.Builder.VerifyPanelLine'('', ["false","CREATED"], elem)
CustomKeywords.'project.Builder.SelectAllLinePanel'()
CustomKeywords.'project.Builder.VerifySelectAllLinePanel'('false', null)
lineselected = CustomKeywords.'project.Builder.SelectLinePanel'('tag', 'CREATED')
lineselected += CustomKeywords.'project.Builder.SelectLinePanel'('name', GlobalVariable.items[2])
	
for (def elem in lineselected)
	KeywordUtil.logInfo('linenameselected = ' + WebUI.getText(elem))

CustomKeywords.'project.Builder.LinePanelActivate'()
CustomKeywords.'project.Builder.CheckLinePanelStatus'('Active', lineselected)
CustomKeywords.'project.Builder.LinePanelDuplicate'()
CustomKeywords.'project.Builder.CheckLinePanelDuplicated'(lineselected)
CustomKeywords.'project.Builder.SelectAllLinePanel'()
CustomKeywords.'project.Builder.SelectAllLinePanel'()
CustomKeywords.'project.Builder.VerifySelectAllLinePanel'('true', null)
lineselected = CustomKeywords.'project.Builder.GetSelectedLinePanel'()
CustomKeywords.'project.Builder.LinePanelPause'()
CustomKeywords.'project.Builder.VerifyPanelLine'('tag', ["EDITED"], GlobalVariable.items[2])
CustomKeywords.'project.Builder.CheckLinePanelStatus'('Paused', lineselected)

File sdfFile = CustomKeywords.'project.Builder.download'()
ArrayList<String> sdfPaths = CustomKeywords.'project.Builder.unzip'(sdfFile)
KeywordUtil.logInfo('lineselected = ' + lineselected)
for (int i = 0;  i < lineselected.size(); i++)
{
	CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], i + 1, "Status", 'Paused')
	linenames.add(WebUI.getText(lineselected[i]).replaceFirst(/ \(\d+\)/, ''))
}
linenames.remove(0)
CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(sdfPaths)
CustomKeywords.'com.reusableComponents.Common.deleteFile'(sdfPaths[0])
CustomKeywords.'com.reusableComponents.Common.deleteFile'(sdfPaths[1])
KeywordUtil.logInfo('linesnames to archive = ' + linenames)
CustomKeywords.'com.reusableComponents.DV360.archiveObject'(linenames, 'LI')
CustomKeywords.'project.Builder.gotoBuilder'()
CustomKeywords.'project.Builder.refreshAdvertiser'()
CustomKeywords.'project.Builder.load'('LI', '', '', '')
CustomKeywords.'project.Builder.SelectAllLinePanel'()
CustomKeywords.'project.Builder.VerifySelectAllLinePanel'('true', "LOADED")
CustomKeywords.'project.Builder.LinePanelActivate'()
CustomKeywords.'project.Builder.VerifySelectAllLinePanel'(null, "EDITED")
lineselected = CustomKeywords.'project.Builder.GetSelectedLinePanel'()
CustomKeywords.'project.Builder.CheckLinePanelStatus'('Active', lineselected)

sdfFile = CustomKeywords.'project.Builder.download'()
sdfPaths = CustomKeywords.'project.Builder.unzip'(sdfFile)
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Status", 'Active')
CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(sdfPaths)
CustomKeywords.'com.reusableComponents.Common.deleteFile'(sdfPaths[0])
CustomKeywords.'com.reusableComponents.Common.deleteFile'(sdfPaths[1])
