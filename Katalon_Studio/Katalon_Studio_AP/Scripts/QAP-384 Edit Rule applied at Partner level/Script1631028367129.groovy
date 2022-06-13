import internal.GlobalVariable
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.util.Map

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'("QAP-384")
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'project.AutoPause.Start'()
CustomKeywords.'project.AutoPause.selectPartner'(GlobalVariable.partnerName)

// Check of the advertisers objects status
Map ruleCreate = GlobalVariable.ruleCreate
Map ruleEdit = GlobalVariable.ruleEdit

String ruleName = CustomKeywords.'project.AutoPause.createNewRule'()

String[] newItems = CustomKeywords.'project.AutoPause.getNewItems'(ruleName, ruleCreate, false, ruleCreate['Insertion Orders'], ruleCreate['Advertisers'], ruleCreate['Warning Email'], ruleCreate['Condition'], true)

// Reactivate
if (ruleCreate['Apply on'] == 'Insertion Order' && ruleEdit['Reactivate IO'] == true)
	CustomKeywords.'project.AutoPause.setRuleIOsActive'(ruleEdit['Insertion Orders'])

//Edition
WebUI.switchToWindowIndex(0)
String ruleEditedName = CustomKeywords.'project.AutoPause.editRule'(ruleName)

if (GlobalVariable.CheckEmail && ruleEdit['Status'] == "Active")
	CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleEditedName, ruleEdit, ruleEdit['Budget Value'], 15, 5, true, true, newItems[0], newItems[1], ruleEdit['Warning Email'], newItems[2])

// Delete rule
WebUI.switchToWindowIndex(0)
CustomKeywords.'project.AutoPause.deleteRule'(ruleEditedName)
CustomKeywords.'project.AutoPause.Search'(ruleName, "name", false)
