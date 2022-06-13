import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.openqa.selenium.Keys

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'("QAP-264")
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'project.AutoPause.Start'()

Map ruleCreate = GlobalVariable.ruleCreate
if (ruleCreate['Apply on'] == 'Insertion Order' && ruleCreate['Reactivate IO'] == true)
	CustomKeywords.'project.AutoPause.setRuleIOsActive'(ruleCreate['Insertion Orders'])

String rule = CustomKeywords.'project.AutoPause.createNewRule'()
CustomKeywords.'project.AutoPause.Search'(rule)
String ruleID = WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/IDs'))

String[] ruleInfos = ["","",""]
if (GlobalVariable.CheckEmail && ruleCreate['Status'] == "Active")
	ruleInfos = CustomKeywords.'project.AutoPause.waitForRuleEmailAndCheckDvStatus'(rule, ruleCreate, ruleCreate['Budget Value'], 15, 5, true, true, ruleCreate['Insertion Orders'], ruleCreate['Advertisers'], ruleCreate['Warning Email'], GlobalVariable.ruleCreate['Condition'], true)

if (GlobalVariable.CheckRuleList && ruleCreate['Apply on'] != 'Partner') {
	CustomKeywords.'project.AutoPause.returnToAutoPause'()
	CustomKeywords.'project.AutoPause.Search'(rule)
	DateTimeFormatter dateFormatterParse = DateTimeFormatter.ofPattern("d/M/yyyy")
	DateTimeFormatter dateFormatterFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
	String startDate = dateFormatterFormat.format(LocalDate.parse(ruleCreate['Start Date'].replaceAll("^(\\d+)/(\\d+)", "\$2/\$1"), dateFormatterParse))
	String endDate = dateFormatterFormat.format(LocalDate.parse(ruleCreate['End Date'].replaceAll("^(\\d+)/(\\d+)", "\$2/\$1"), dateFormatterParse))
	CustomKeywords.'project.AutoPause.checkColumnValue'("Analysis Period", startDate +" to " + endDate)
	CustomKeywords.'project.AutoPause.checkColumnValue'("ID", ruleID)
	CustomKeywords.'project.AutoPause.checkColumnValue'("Name", rule)
	CustomKeywords.'project.AutoPause.checkColumnValue'("Threshold", ruleCreate['Threshold'] + "%")
	
	if(ruleCreate['Apply on'] == "Partner") {
		ArrayList<String> conditions = ruleCreate['Condition'].split(";")
		ArrayList<String> currencies = new ArrayList<String>()
		ArrayList<String> budgetTypes = new ArrayList<String>()
		for (String condition in conditions) {
			currencies.add(condition.split(",")[2])
			budgetTypes.add(condition.split(",")[1])
		}
		CustomKeywords.'project.AutoPause.verifyAdvertiserList'(ruleCreate['Apply on'], [], conditions, currencies, budgetTypes)
	} else {
		ArrayList<String> insertionOrders = ruleCreate["Insertion Orders"].split(",")
		ArrayList<String> advertisers = ruleCreate["Advertisers"].split(",")
		CustomKeywords.'project.AutoPause.verifyAdvertiserList'(ruleCreate['Apply on'], insertionOrders, advertisers, [], [])
	}
	
	if(GlobalVariable.CheckEmail && ruleCreate['Status'] == "Active")
		CustomKeywords.'project.AutoPause.verifySpend'(ruleCreate['Currency'], ruleCreate['Budget Value'], ruleInfos[0])

	WebUI.sendKeys(findTestObject('2. Schedule List/Search bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
	Thread.sleep(500)
	String[] creators = [GlobalVariable.googleID]
	CustomKeywords.'project.AutoPause.filterCreator'(creators)
	CustomKeywords.'project.AutoPause.Search'(rule)
	CustomKeywords.'project.AutoPause.Search'(ruleID, "ID")
	WebUI.sendKeys(findTestObject('2. Schedule List/Search bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
	Thread.sleep(500)
	CustomKeywords.'project.AutoPause.checkSort'()
}

WebUI.switchToWindowIndex(0)
CustomKeywords.'project.AutoPause.deleteRule'(rule)
CustomKeywords.'project.AutoPause.Search'(rule, "name", false)