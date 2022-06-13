import internal.GlobalVariable
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'("QAP-299")
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'project.AutoPause.Start'()
CustomKeywords.'project.AutoPause.selectPartner'(GlobalVariable.partnerName)

// Check of the advertisers objects status
Map ruleCreate = GlobalVariable.ruleCreate
Map ruleEdit = GlobalVariable.ruleEdit

// Reactivate
if (ruleCreate['Apply on'] == 'Insertion Order' && ruleCreate['Reactivate IO'] == true)
	CustomKeywords.'project.AutoPause.setRuleIOsActive'(ruleCreate['Insertion Orders'])

// Creation
WebUI.switchToWindowIndex(0)
String ruleName = CustomKeywords.'project.AutoPause.createNewRule'()

String[] ruleInfos = ["","",""]
boolean openGmail = false
boolean openDV360 = true
ArrayList<String> IosIntel = new ArrayList<String>()
int dv360Index = 2
int gmailIndex = 1 
if(ruleCreate['Status'] == "Active") ruleInfos = CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleName, ruleCreate, ruleCreate['Budget Value'], 15, 5, openDV360, true, null, null, ruleCreate['Warning Email'], GlobalVariable.ruleCreate['Condition'], true)
else {
	openGmail = true
	dv360Index = 1
	gmailIndex = 2	
}

// Reactivate
if (ruleCreate['Apply on'] == 'Insertion Order' && ruleEdit['Reactivate IO'] == true)
	CustomKeywords.'project.AutoPause.setRuleIOsActive'(ruleEdit['Insertion Orders'])

//Edition
WebUI.switchToWindowIndex(0)
String ruleEditedName = CustomKeywords.'project.AutoPause.editRule'(ruleName)
// Active/Pause Object
CustomKeywords.'com.reusableComponents.Common.openNewTab'()
CustomKeywords.'com.reusableComponents.DV360.goToDv360'()
CustomKeywords.'com.reusableComponents.DV360.setIosActive'(ruleCreate['Insertion Orders'].split(","))
if(ruleEdit['Status'] == "Active") {
	WebUI.switchToWindowIndex(0)
    CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleEditedName, ruleEdit, ruleEdit['Budget Value'], 16, 5, false, openGmail, ruleInfos[1], ruleInfos[2], ruleEdit['Warning Email'], GlobalVariable.ruleEdit['Condition'], false, IosIntel, dv360Index, gmailIndex)
	
	WebUI.switchToWindowIndex(dv360Index)
	CustomKeywords.'com.reusableComponents.DV360.setIosActive'(ruleEdit['Insertion Orders'].split(","))
}
// Delete rule
WebUI.switchToWindowIndex(0)
CustomKeywords.'project.AutoPause.deleteRule'(ruleEditedName)
CustomKeywords.'project.AutoPause.Search'(ruleName, "name", false)
