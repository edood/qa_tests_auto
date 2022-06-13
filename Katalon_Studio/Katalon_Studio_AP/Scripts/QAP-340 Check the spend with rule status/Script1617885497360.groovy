import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-340')
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'project.AutoPause.Start'()

Map ruleCreate = GlobalVariable.ruleCreate
Map ruleEdit = GlobalVariable.ruleEdit
Map ruleDuplicate = GlobalVariable.ruleDuplicate

//Future rule with Spend N/A
if (ruleCreate['Apply on'] == 'Insertion Order' && ruleCreate['reactiveOI'] == true)
	CustomKeywords.'project.AutoPause.setRuleIOsActive'(ruleCreate['Insertion Orders'])
String nextDay = new Date().plus(1).format('M/d/yyyy')
String nextMonth = new Date().plus(30).format('M/d/yyyy')
String ruleOneName = CustomKeywords.'project.AutoPause.createNewRule'(nextDay, nextMonth)



//Spend Check with the status Paused rule
String[] ruleOneItems = CustomKeywords.'project.AutoPause.getNewItems'(ruleOneName, ruleCreate, false, ruleCreate['Insertion Orders'], ruleCreate['Advertisers'], true, ruleCreate['Condition'], true)
String lastYear = new Date().plus(-365).format('M/d/yyyy')
ruleOneName = CustomKeywords.'project.AutoPause.editRule'(ruleOneName, lastYear, ruleEdit["End Date"], ruleEdit["Warning Email"], "Paused")



//Duplicate / Spend value  with edition status
ruleOneItems = CustomKeywords.'project.AutoPause.getNewItems'(ruleOneName, ruleEdit, false, ruleOneItems[0], ruleOneItems[1], true, ruleEdit['Condition'])

if (ruleCreate['Apply on'] == 'Insertion Order' && ruleDuplicate['reactiveOI'] == true)
	CustomKeywords.'project.AutoPause.setRuleIOsActive'(ruleOneItems[0] + "," + ruleDuplicate['Insertion Orders'])
String ruleTwoName = CustomKeywords.'project.AutoPause.duplicateRule'(ruleOneName, ruleDuplicate["Start Date"], ruleDuplicate["End Date"], true, ruleDuplicate["Status"])
// Warning email forced to true
String[] ruleTwoInfos = CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleTwoName, ruleDuplicate, ruleDuplicate['Budget Value'], 12, 5, true, true, ruleOneItems[0], ruleOneItems[1], true)


// And I return at auto pause
WebUI.switchToWindowIndex(0)
WebUI.refresh()
WebUI.waitForPageLoad(30)
Thread.sleep(500)
assert WebUI.waitForElementNotPresent(findTestObject('2. Schedule List/Progress bar'), 30)
CustomKeywords.'project.AutoPause.Search'(ruleOneName)
assert WebUI.getText(findTestObject('2. Schedule List/1. Table/Spends')) == "NA"


// Reuse the edition variable
if (ruleCreate['Apply on'] == 'Insertion Order' && ruleEdit['reactiveOI'] == true)
	CustomKeywords.'project.AutoPause.setRuleIOsActive'(ruleTwoInfos[1] + "," + ruleEdit['Insertion Orders'])
CustomKeywords.'project.AutoPause.Search'(ruleTwoName)
CustomKeywords.'project.AutoPause.verifySpend'(ruleCreate['Currency'],ruleDuplicate['Budget Value'], ruleTwoInfos[0])
ruleTwoName = CustomKeywords.'project.AutoPause.editRule'(ruleTwoName, ruleEdit["Start Date"], ruleEdit["End Date"], true, ruleDuplicate["Status"], ruleEdit, ruleDuplicate['Budget Value'], ruleDuplicate['Threshold'], ruleTwoName + "_edited")
// Warning email forced to true
String[] editedRuleTwoInfos = CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleTwoName, ruleEdit, ruleDuplicate['Budget Value'], 12, 5, false, false, ruleTwoInfos[1], ruleTwoInfos[2], true, GlobalVariable.ruleEdit['Condition'])

WebUI.switchToWindowIndex(0)
WebUI.refresh()
WebUI.waitForPageLoad(30)
Thread.sleep(500)
assert WebUI.waitForElementNotPresent(findTestObject('2. Schedule List/Progress bar'), 30)
CustomKeywords.'project.AutoPause.Search'(ruleTwoName)
CustomKeywords.'project.AutoPause.verifySpend'(ruleCreate['Currency'], ruleDuplicate['Budget Value'], editedRuleTwoInfos[0])

if (ruleCreate['Apply on'] == 'Insertion Order' && ruleEdit['reactiveOI'] == true)
	CustomKeywords.'project.AutoPause.setRuleIOsActive'(ruleOneItems[0]) // No need to add ruleEdit items, we returned them already after the first edition
CustomKeywords.'project.AutoPause.setRuleStatus'("Paused", ruleTwoName)
CustomKeywords.'project.AutoPause.setRuleStatus'("Active", ruleOneName)

String[] ruleOneInfos = CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleOneName, ruleEdit, ruleEdit['Budget Value'], 12, 5, true, false, ruleOneItems[0], ruleOneItems[1], ruleEdit["Warning Email"], GlobalVariable.ruleEdit['Condition'])

WebUI.switchToWindowIndex(0)
WebUI.refresh()
WebUI.waitForPageLoad(30)
Thread.sleep(500)
assert WebUI.waitForElementNotPresent(findTestObject('2. Schedule List/Progress bar'), 30)
CustomKeywords.'project.AutoPause.Search'(ruleOneName)
CustomKeywords.'project.AutoPause.verifySpend'(ruleCreate['Currency'], ruleEdit['Budget Value'], ruleOneInfos[0])
CustomKeywords.'project.AutoPause.Search'(ruleTwoName)
CustomKeywords.'project.AutoPause.verifySpend'(ruleCreate['Currency'], ruleDuplicate['Budget Value'], editedRuleTwoInfos[0])

if(ruleCreate['Apply on'] == "Insertion Order" && ruleEdit["Warning Email"] == false) {
	WebUI.switchToWindowIndex(2)
	CustomKeywords.'com.reusableComponents.DV360.setIosActive'(ruleOneInfos[1].split(','))
	WebUI.switchToWindowIndex(0)
}

CustomKeywords.'project.AutoPause.deleteRule'(ruleOneName)
CustomKeywords.'project.AutoPause.deleteRule'(ruleTwoName)