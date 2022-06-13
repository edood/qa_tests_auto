import internal.GlobalVariable
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'("QAP-390")
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'project.AutoPause.Start'()
CustomKeywords.'project.AutoPause.selectPartner'(GlobalVariable.partnerName)

// Check of the advertisers objects status
Map ruleCreate = GlobalVariable.ruleCreate
Map ruleDuplicate = GlobalVariable.ruleDuplicate

// Creation
WebUI.switchToWindowIndex(0)
String ruleName = CustomKeywords.'project.AutoPause.createNewRule'()

ArrayList<String> IosIntel = new ArrayList<String>()
String[] newItems = CustomKeywords.'project.AutoPause.getNewItems'(ruleName, ruleCreate, false, ruleCreate['Insertion Orders'], ruleCreate['Advertisers'], ruleCreate['Warning Email'], GlobalVariable.ruleCreate['Condition'], true)

// Reactivate
if (ruleCreate['Apply on'] == 'Insertion Order' && ruleDuplicate['Reactivate IO'] == true)
	CustomKeywords.'project.AutoPause.setRuleIOsActive'(ruleDuplicate['Insertion Orders'])

// Duplication
WebUI.switchToWindowIndex(0)
String ruleDuplicatedName = CustomKeywords.'project.AutoPause.duplicateRule'(ruleName)

int dv360Index = 2
int gmailIndex = 1
if(GlobalVariable.CheckEmail && ruleDuplicate['Status'] == "Active") CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleDuplicatedName, ruleDuplicate, ruleDuplicate['Budget Value'], 16, 5, true, true, newItems[0], newItems[1], ruleDuplicate['Warning Email'], GlobalVariable.ruleCreate['Condition'], false, IosIntel, dv360Index, gmailIndex)

// Delete rule
WebUI.switchToWindowIndex(0)
CustomKeywords.'project.AutoPause.deleteRule'(ruleName)
CustomKeywords.'project.AutoPause.Search'(ruleName, "name", false)
CustomKeywords.'project.AutoPause.deleteRule'(ruleDuplicatedName)
CustomKeywords.'project.AutoPause.Search'(ruleDuplicatedName, "name", false)
