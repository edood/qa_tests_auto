import com.kms.katalon.core.util.KeywordUtil
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
	
CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-329')
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'project.AutoPause.Start'()


// Create rule advertiser level with option warning email or not
Map ruleCreate = GlobalVariable.ruleCreate
CustomKeywords.'com.reusableComponents.DV360.goToDv360'(true)
ArrayList<String> IosIntel = CustomKeywords.'com.reusableComponents.DV360.getIosStatusmatchingProfile'(ruleCreate["Apply on"], ruleCreate["Reactivate IO"])
KeywordUtil.logInfo('IosIntel = ' + IosIntel)
CustomKeywords.'com.reusableComponents.DV360.checkDataStatus'(IosIntel)
WebUI.switchToWindowIndex(0)
String ruleName = CustomKeywords.'project.AutoPause.createNewRule'()
String[] ruleInfos = ["","",""]
boolean openGmail = false
int dv360Index = 1
int gmailIndex = 2
if(ruleCreate['Status'] == "Active") {
	ruleInfos = CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleName, ruleCreate, ruleCreate['Budget Value'], 12, 5, false, true, null, null, ruleCreate['Warning Email'], GlobalVariable.ruleCreate['Condition'], true, IosIntel, dv360Index)
} else {
	openGmail = true
}

// Edit the rule by adding or removing advertisers
Map ruleEdit = GlobalVariable.ruleEdit
WebUI.switchToWindowIndex(1)
IosIntel = CustomKeywords.'com.reusableComponents.DV360.getIosStatusmatchingProfile'(ruleCreate["Apply on"], ruleEdit["Reactivate IO"], ruleEdit, ruleInfos[1], ruleInfos[2])
KeywordUtil.logInfo('IosIntel = ' + IosIntel)
CustomKeywords.'com.reusableComponents.DV360.checkDataStatus'(IosIntel)
WebUI.switchToWindowIndex(0)
String ruleEditedName = CustomKeywords.'project.AutoPause.editRule'(ruleName)
String[] ruleEditedInfos = []
if(ruleEdit['Status'] == "Active") ruleEditedInfos = CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleEditedName, ruleEdit, ruleEdit['Budget Value'], 12, 5, false, openGmail, ruleInfos[1], ruleInfos[2], ruleEdit['Warning Email'], GlobalVariable.ruleEdit['Condition'], false, IosIntel, dv360Index, gmailIndex)

WebUI.switchToWindowIndex(0)
WebUI.refresh()
WebUI.waitForPageLoad(30)
Thread.sleep(500)
assert WebUI.waitForElementNotPresent(findTestObject('2. Schedule List/Progress bar'), 30)
CustomKeywords.'project.AutoPause.Search'(ruleEditedName)
CustomKeywords.'project.AutoPause.verifyBudget'(GlobalVariable.ruleCreate['Budget Type'], GlobalVariable.ruleCreate['Currency'], GlobalVariable.ruleEdit['Budget Value'])
if(ruleEdit['Status'] == "Active") CustomKeywords.'project.AutoPause.verifySpend'(GlobalVariable.ruleCreate['Currency'], GlobalVariable.ruleEdit['Budget Value'], ruleEditedInfos[0])

WebUI.switchToWindowIndex(0)
CustomKeywords.'project.AutoPause.deleteRule'(ruleEditedName)
CustomKeywords.'project.AutoPause.Search'(ruleName, "name", false)