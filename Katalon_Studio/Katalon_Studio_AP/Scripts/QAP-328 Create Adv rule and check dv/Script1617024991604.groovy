
import java.time.Instant as Instant

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'("QAP-328")
Map ruleCreate = GlobalVariable.ruleCreate
boolean warningemail = ruleCreate["Warning Email"]

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'project.AutoPause.Start'(true)

CustomKeywords.'com.reusableComponents.DV360.goToDv360'(true)
ArrayList<String> IosIntel = CustomKeywords.'com.reusableComponents.DV360.getIosStatusmatchingProfile'(ruleCreate["Apply on"], ruleCreate["Reactivate IO"])
KeywordUtil.logInfo('IosIntel = ' + IosIntel)
CustomKeywords.'com.reusableComponents.DV360.checkDataStatus'(IosIntel)

WebUI.switchToWindowIndex(0)
String ruleName = CustomKeywords.'project.AutoPause.createNewRule'(ruleCreate['Start Date'], ruleCreate['End Date'], warningemail, "Active", ruleCreate['Apply on'])

ArrayList<String> list = CustomKeywords.'project.AutoPause.checkadvertiserpopup'('Name', ruleName, ruleCreate['Apply on'])
KeywordUtil.logInfo('list = ' + list)

String[] list_name = []
for (String name in list)
	list_name += name.replaceFirst(" \\(\\d+\\)", "")

CustomKeywords.'project.AutoPause.filter'(list_name)
CustomKeywords.'project.AutoPause.Verifyfilter'(list_name)
CustomKeywords.'project.AutoPause.Search'(ruleName, "name")

CustomKeywords.'project.AutoPause.randomSearch'()
CustomKeywords.'project.AutoPause.checkSort'()

String[] ruleTwoInfos = CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(ruleName, ruleCreate, ruleCreate['Budget Value'], 12, 2, true, true, ruleCreate['Insertion Orders'], ruleCreate['Advertisers'], ruleCreate['Warning Email'], GlobalVariable.ruleCreate['Condition'], true)

ArrayList<String> OIemail = new ArrayList<String>()
OIemail.addAll(ruleTwoInfos[1].split(","))

KeywordUtil.logInfo('ruleTwoInfos = '+ ruleTwoInfos)
KeywordUtil.logInfo('IosIntel of begining = ' + IosIntel)
CustomKeywords.'project.AutoPause.checkpausedOI'(IosIntel, OIemail)

if (GlobalVariable.grafana == true)
	WebUI.switchToWindowIndex(3)

CustomKeywords.'com.reusableComponents.DV360.verifyautopauseondv'(OIemail, IosIntel, warningemail)

WebUI.switchToWindowIndex(0)
CustomKeywords.'project.AutoPause.deleteRule'(ruleName)
CustomKeywords.'project.AutoPause.Search'(ruleName, "name", false)
