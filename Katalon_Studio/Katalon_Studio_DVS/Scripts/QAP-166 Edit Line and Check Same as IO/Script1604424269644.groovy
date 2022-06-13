import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.DV360

import internal.GlobalVariable as GlobalVariable
import project.Builder

Builder testcase = new Builder()
DV360 dv360 = new DV360()

itemsAndValuesToCheck = new ArrayList<>()
itemsAndValuesToCheck.add(new ArrayList<String>())

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-166')

// Prepare to highlight elements for specific methods
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

// Log in
testcase.Start(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

// Check line settings on DV
dv360.dvSetLineFlightDateSameAsIO(true, GlobalVariable.items[2])
dv360.dvSetLineStatusPaused()
testcase.closeNewTab()

// Refresh advertiser
testcase.refreshAdvertiser()

// Load a Campaign, Insertion Order and Line item
testcase.load('LI', '', '', '')

// Check Same as Insertion Order
testcase.checkLineSameAsIO()

// Edit line fields
testcase.modifyLiFieldValue("status", "Active")

String currentBudgetType = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Budget Type'), 'value')
String budgetTypeToCheck = ""
if (currentBudgetType == 'Amount') {
    testcase.modifyLiFieldValue("budget type", "Unlimited")
	budgetTypeToCheck = "Unlimited"
} else {
    testcase.modifyLiFieldValue("budget type", "Amount")
    testcase.modifyLiFieldValue("lifetime budget", "1")
	budgetTypeToCheck = "Amount 1"
}

String currentPacing = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Pacing'), 'value')
String pacingToCheck = ""
String pacingRateToCheck = ""
String pacingAmountToCheck = ""
if (currentPacing == 'Daily') {
    testcase.modifyLiFieldValue("pacing", "Flight")
    testcase.modifyLiFieldValue("pacing rate", "ASAP")
	pacingToCheck = "Flight"
	pacingRateToCheck = "ASAP"
	pacingAmountToCheck = ""
} else {
    testcase.modifyLiFieldValue("pacing", "Daily")
    testcase.modifyLiFieldValue("pacing rate", "ASAP")
    testcase.modifyLiFieldValue("daily budget", "1")
	pacingToCheck = "Daily"
	pacingRateToCheck = "ASAP"
	pacingAmountToCheck = " 1"
}

WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))
String currentName = WebUI.getText(findTestObject('Object Repository/6. Objects/First Loaded Line Item'))
itemsAndValuesToCheck.get(0).addAll([currentName, 'Line Item', "Active", "Same as IO", budgetTypeToCheck, pacingToCheck + pacingAmountToCheck, pacingRateToCheck])
KeywordUtil.logInfo(itemsAndValuesToCheck.get(0).toString())

// Download SDF
File sdfName = testcase.download()
ArrayList<String> sdfPaths = testcase.unzip(sdfName)

// Status
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Status", "Active")

// Start Date
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Start Date", "Same as Insertion Order")

// End Date
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "End Date", "Same as Insertion Order")
// Budget type

if (budgetTypeToCheck == "Amount 1") { budgetTypeToCheck = "Amount" }
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Budget Type", budgetTypeToCheck)

// Pacing
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Pacing", pacingToCheck)

// Pacing rate
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Pacing Rate", pacingRateToCheck)

// Pacing amount
if (pacingAmountToCheck == "") { pacingAmountToCheck = "0" }
else if (pacingAmountToCheck == " 1") { pacingAmountToCheck = "1" }
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Pacing Amount", pacingAmountToCheck)
CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(sdfPaths)

dv360.checkSetupValuesOnDV360(itemsAndValuesToCheck)

testcase.deleteFile(sdfPaths[0])
testcase.deleteFile(sdfPaths[1])

