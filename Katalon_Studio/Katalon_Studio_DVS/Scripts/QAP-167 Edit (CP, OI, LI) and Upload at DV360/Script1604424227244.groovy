import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.text.SimpleDateFormat as SimpleDateFormat
import java.time.Month as Month

import org.openqa.selenium.By as By
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.WebDriver as WebDriver

import com.reusableComponents.TargetObject as TargetObject
import com.kms.katalon.core.testobject.SelectorMethod as SelectorMethod
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.DV360
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium as WebDriverBackedSelenium

import project.Targeting
import internal.GlobalVariable as GlobalVariable
import project.Builder as Builder

Builder testcase = new Builder()
DV360 dv360 = new DV360()
Targeting targeting = new Targeting()

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-167')
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

// Log in
String baseUrl = testcase.Start(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

// Refresh advertiser
testcase.refreshAdvertiser()

// Load a Campaign, Insertion Order and Line item
testcase.load('LI', '', '', '')

// Initialize array to check
String liId = WebUI.getText(findTestObject('Object Repository/6. Objects/First Loaded Line Item')).replaceFirst('.*\\((\\d+)\\)', '$1')

// Modify Campaign Budget
testcase.selectItem("Campaign", 1)
String currentCpBudgetValue = WebUI.getAttribute(findTestObject('5. Editor/1. Settings/1. General/Campaign/Campaign Budget'), 'value')
float currentCpBudget
String cpBudgetToCheck = ''
if (currentCpBudgetValue != '') {
    currentCpBudget = Float.parseFloat(currentCpBudgetValue)
    float cpBudgetValue = currentCpBudget
    if ((5 > cpBudgetValue) && (cpBudgetValue > 0)) { cpBudgetValue++ } 
	else { cpBudgetValue-- }	
	cpBudgetToCheck = cpBudgetValue.toString().replaceFirst('\\.0+$', '')
} else {
    cpBudgetToCheck = '1'
}
testcase.modifyCampaignFieldValue("budget", cpBudgetToCheck)


// Modify Campaign End date
String cpEndDateValue = testcase.addDate('Campaign', 'End', 1)
WebUI.doubleClick(findTestObject('5. Editor/1. Settings/1. General/Campaign/End Date button'))
String cpCurrentEndMonth = WebUI.getText(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Header Month'))
String cpEndDateToCheck = cpEndDateValue.replaceAll('(\\d+) (.+) (\\d+)', '$2\\/$1\\/$3 23:59')
cpEndDateToCheck = cpEndDateToCheck.replaceAll('([a-zA-Z]+)', String.format('%02d', Month.valueOf(cpCurrentEndMonth.toUpperCase()).getValue()))


// Modify Campaign Goal (random)
WebUI.doubleClick(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))
String currentGoal = WebUI.getAttribute(findTestObject('5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal'), 'value')
int randomIndex = 0
if (currentGoal.equals('Raise awareness of my brand or product')) {
    randomIndex = (new Random().nextInt((3 - 1) + 1) + 1)

   CustomKeywords.'project.Builder.xsetOption'(findTestObject('5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal'), randomIndex, 'index')
} else {
    CustomKeywords.'project.Builder.xsetOption'(findTestObject('5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal'), 0, 'index')
}
String cpGoalToCheck = WebUI.getAttribute(findTestObject('5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal'), 'value')


// Add Insertion Order Budget interval
String budgetIntervalsToCheck = testcase.addBudgetIntervalNoOverlap(1)

// Switch Insertion Order Frequency Capping
String[] frequencyCappingToCheck = testcase.setRandomFrequencyCapping()

// Modify Insertion Order Inventory
String[] ioInventoriesToCheck = targeting.editIoInventory()

// Modify Line item Inventory
String[] liInventoriesToCheck = targeting.editLineInventory(liId)

// Download SDF file
File sdfFile = testcase.download()

// Unzip SDF file
ArrayList<String> sdfPaths = testcase.unzip(sdfFile)
WebUI.delay(3)

// Budget, End date, Goal
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Campaign Budget", cpBudgetToCheck)
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Campaign End Date", cpEndDateToCheck)
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Campaign Goal", cpGoalToCheck)


// Budget segment
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[2], "Budget Segments", budgetIntervalsToCheck)

// Frequency capping
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[2], "Frequency Enabled", frequencyCappingToCheck[0])
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[2], "Frequency Exposures", frequencyCappingToCheck[1])
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[2], "Frequency Period", frequencyCappingToCheck[2])
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[2], "Frequency Amount", frequencyCappingToCheck[3])

// Inventory IO
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[2], "Inventory Source Targeting - Include", ioInventoriesToCheck[0])
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[2], "Inventory Source Targeting - Exclude", ioInventoriesToCheck[1])

// Inventory LI
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[3], "Inventory Source Targeting - Include", liInventoriesToCheck[0])
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[3], "Inventory Source Targeting - Exclude", liInventoriesToCheck[1])

WebUI.switchToWindowIndex(1)
dv360.dv360SearchItem(GlobalVariable.advertiserName)
dv360.DV360Upload(sdfPaths)

testcase.deleteFile(sdfPaths[0])
testcase.deleteFile(sdfPaths[1])
testcase.deleteFile(sdfPaths[2])
testcase.deleteFile(sdfPaths[3])