import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import java.time.Instant as Instant
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-295')

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'project.AutoPause.Start'()
CustomKeywords.'project.AutoPause.selectPartner'(GlobalVariable.partnerName)

Map ruleCreate = GlobalVariable.ruleCreate
if (ruleCreate['Apply on'] == 'Insertion Order' && ruleCreate['Reactivate IO'] == true)
	CustomKeywords.'project.AutoPause.setRuleIOsActive'(ruleCreate['Insertion Orders'])

String ruleName = CustomKeywords.'project.AutoPause.createNewRule'(ruleCreate['Start Date'], ruleCreate['End Date'], ruleCreate['Warning Email'], "Active", "Insertion Order")

int minute = CustomKeywords.'com.reusableComponents.Common.getCurrentMinute'()
int attempts = CustomKeywords.'com.reusableComponents.Common.getPauseAttempts'(minute, 15, 5)
String epochSecond = ''
if ((minute == 0) || (minute == 10)) epochSecond = Instant.parse(Instant.now().toString().replaceFirst('\\d\\.\\d\\d\\dZ$', '0.000Z')).getEpochSecond() // Rule created around 10mins, so we check the emails from the last 10mins just in case
else epochSecond = CustomKeywords.'com.reusableComponents.Common.getEpochSecond'() // Rule not created around 10mins, so we can check the emails from now 

if (ruleCreate['Status'] == "Active") CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleName, ruleCreate, ruleCreate['Budget Value'], 12, 2, true, true, ruleCreate['Insertion Orders'], ruleCreate['Advertisers'], ruleCreate['Warning Email'], GlobalVariable.ruleCreate['Condition'], true)

WebUI.switchToWindowIndex(0)

CustomKeywords.'project.AutoPause.deleteRule'(ruleName, true)

CustomKeywords.'project.AutoPause.deleteRule'(ruleName)

epochSecond = CustomKeywords.'com.reusableComponents.Common.getEpochSecond'()
minute = CustomKeywords.'com.reusableComponents.Common.getCurrentMinute'()
attempts = CustomKeywords.'com.reusableComponents.Common.getPauseAttempts'(minute, 11, 5) // Max wait is 11 mins to check for the email -not- to come

CustomKeywords.'com.reusableComponents.Common.openNewTab'()
CustomKeywords.'com.reusableComponents.DV360.goToDv360'()
CustomKeywords.'com.reusableComponents.DV360.setIosActive'(ruleCreate['Insertion Orders'].split(","))
CustomKeywords.'com.reusableComponents.Common.closeNewTab'()

WebUI.switchToWindowIndex(1)

CustomKeywords.'com.reusableComponents.Gmail.waitForNewEmail'('', ruleCreate['Warning Email'], epochSecond, attempts, true)

CustomKeywords.'com.reusableComponents.Common.openNewTab'()
CustomKeywords.'com.reusableComponents.DV360.goToDv360'()
CustomKeywords.'com.reusableComponents.DV360.verifyIosActive'(ruleCreate['Insertion Orders'].split(","))
CustomKeywords.'com.reusableComponents.Common.closeNewTab'()

