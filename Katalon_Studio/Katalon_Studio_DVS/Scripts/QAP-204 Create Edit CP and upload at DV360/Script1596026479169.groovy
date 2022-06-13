import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.text.DateFormatSymbols

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import project.Builder
import project.CPSettings
import com.reusableComponents.DV360

import internal.GlobalVariable

DV360 dv360 = new DV360()
Builder testcase = new Builder()

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-204')

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'project.Builder.Start'(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

// Refresh advertiser
testcase.refreshAdvertiser()

Date date = new Date()
Calendar calendar = GregorianCalendar.getInstance()
calendar.setTime(date)
String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()

// Load Campaign
testcase.load('CP', '', '', '')

// Create new Campaign
ArrayList<String> newCampaignSetup = testcase.createNewCampaign("Test_Auto_Campaign_Create_And_Edit_" + date.getTime(), "Paused", 0, 1)

// Record Start and End date
String Start = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Campaign Start Date input'), 'value')
String End = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Campaign End Date input'), 'value')

// Modify Goal KPI value
String newCampaignId = WebUI.getText(testcase.changeSelector(findTestObject('Object Repository/6. Objects/First Loaded Campaign'), "nth-child\\(1\\)", "nth-child(" + 2 + ")")).replaceFirst(".*\\((\\d+)\\)", "\$1")
WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))
Random rnd = new Random()
int inc = rnd.nextInt(6) + 1
String currentGoalKPI = WebUI.getText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI name'))
TestObject currentOption = testcase.changeSelector(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI name'), "\$", " > option:nth-child(" + inc + ")")
String currentOptionValue = WebUI.getAttribute(currentOption, "value")
while (currentGoalKPI.equals(currentOptionValue)) {
	currentOption = testcase.changeSelector(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI name'), "\$", " > option:nth-child(" + inc + ")")
	currentOptionValue = WebUI.getAttribute(currentOption, "value")
	inc = rnd.nextInt(6) + 1
}
testcase.modifyCampaignFieldValue("Goal KPI", currentOptionValue)

// Clear end date
WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Campaign'))


// Share Settings
testcase.campaignShareSettings("Name", 1, 2)
String newCampaignName = "Test_Auto_Campaign_Created_" + date.getTime()
// Change new campaign Name after sharing
testcase.modifyCampaignFieldValue("Name", newCampaignName)

// Quick Duplicate
testcase.campaignQuickDuplicate()
String duplicateCampaignId = WebUI.getText(testcase.changeSelector(findTestObject('Object Repository/6. Objects/First Loaded Campaign'), "nth-child\\(1\\)", "nth-child(" + 3 + ")")).replaceFirst(".*\\((\\d+)\\)", "\$1")
String duplicateCampaignName = "Test_Auto_Campaign_Duplicated_" + date.getTime() + ' - Copy'
// Change duplicated campaign Name
testcase.modifyCampaignFieldValue("Name", duplicateCampaignName)
// Download SDF
File sdfFile = testcase.download()
ArrayList<String> sdfPaths= CustomKeywords.'project.Builder.unzip'(sdfFile)


// ID
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Campaign Id", "ext"+newCampaignId)
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Campaign Id", "ext"+duplicateCampaignId)

// Name
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Name", newCampaignName)
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Name", duplicateCampaignName)

// Status
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Status", newCampaignSetup.get(1))
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Status", newCampaignSetup.get(1))

// Default Goal
String goal = "Raise awareness of my brand or product"
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Campaign Goal", goal)
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Campaign Goal", goal)

// Goal KPI
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Campaign Goal KPI", currentOptionValue)
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Campaign Goal KPI", currentOptionValue)

// Default Goal KPI Value
String goalKPIValue = "50"
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Campaign Goal KPI Value", goalKPIValue)
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Campaign Goal KPI Value", goalKPIValue)

// Budget
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Campaign Budget", newCampaignSetup.get(2))
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Campaign Budget", newCampaignSetup.get(2))

// Start Date
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Campaign Start Date", newCampaignSetup.get(3))
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Campaign Start Date", newCampaignSetup.get(3))

// End Date
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Campaign End Date", newCampaignSetup.get(4))
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Campaign End Date", newCampaignSetup.get(4))

// Frequency Enabled
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Frequency Enabled", "False")
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Frequency Enabled", "False")

// Frequency Exposures
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Frequency Exposures", "0")
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Frequency Exposures", "0")

// Frequency Period
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Frequency Period", "Minutes")
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Frequency Period", "Minutes")

// Frequency Amount
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Frequency Amount", "0")
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Frequency Amount", "0")

// Default Inventory Source Targeting - Include
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Inventory Source Targeting - Include", "1; 6; 8; 9; 10; 2; 11; 12; 13; 16; 17; 19; 20; 21; 23; 27; 29; 30; 31; 34; 35; 36; 37; 38; 41; 42; 43; 44; 46; 48; 50; 51; 52; 56; 60; 67;")
CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], 2, "Inventory Source Targeting - Include", "1; 6; 8; 9; 10; 2; 11; 12; 13; 16; 17; 19; 20; 21; 23; 27; 29; 30; 31; 34; 35; 36; 37; 38; 41; 42; 43; 44; 46; 48; 50; 51; 52; 56; 60; 67;")

dv360.DV360Upload(sdfPaths)

// TODO : Check data on DV360
CPSettings CP = new CPSettings(newCampaignName, newCampaignSetup.get(1), Start, End, newCampaignSetup.get(2), goal, currentOptionValue, goalKPIValue)
CP.DV360VerifyUpload(CP, 'CP Settings')

// Deleting SDF and CSV in order not to pollute download folder
testcase.deleteFile(sdfPaths[0])
testcase.deleteFile(sdfPaths[1])

// Archiving campaigns in order not to pollute advertiser
dv360.archiveObject([newCampaignName, duplicateCampaignName], "CP")
