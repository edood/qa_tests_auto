import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.text.DecimalFormat

import org.openqa.selenium.WebDriver as WebDriver

import com.reusableComponents.TargetObject as TargetObject
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import project.Builder

import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-223')
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'project.Builder.Start'(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

CustomKeywords.'project.Builder.refreshAdvertiser'()
WebDriver driver = DriverFactory.getWebDriver()
DecimalFormat df = new DecimalFormat("0.##")
Builder testcase = new Builder()


LinkedList<TargetObject> target = []

String io_test = GlobalVariable.items[1]
String line_test = GlobalVariable.items[2]
String goalKpiValue = "5"
String bidStrategyValue = "0.5489"
String bidStrategyValueRounded = df.format(Float.parseFloat(bidStrategyValue)).replace(',', '.')

int totalLineChecked
int totalLineInCsv


//check first 100 lines and create objects
totalLineChecked = CustomKeywords.'project.Builder.createNewLineGeneric'("New", "New", 'OI', 1, '100', io_test)
WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))

//Check second 100 lines and create objects
totalLineChecked += CustomKeywords.'project.Builder.createNewLineGeneric'("New", "New", 'OI', 1, '100', io_test)
WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))

//Check all lines and create objects
totalLineChecked += CustomKeywords.'project.Builder.createNewLineGeneric'("New", "New", 'OI', 1, 'All', io_test)

WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))

WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Campaign'))

testcase.modifyCampaignFieldValue('goal kpi', 'Other')
testcase.modifyCampaignFieldValue('goal kpi value', goalKpiValue)
WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Insertion Order'))

CustomKeywords.'project.Builder.modifyIoFieldValue'('performance goal type', 'Other')
CustomKeywords.'project.Builder.modifyIoFieldValue'('performance goal value', '')
WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Line Item'))

CustomKeywords.'project.Builder.modifyLiFieldValue'('bid strategy type', "Beat")
CustomKeywords.'project.Builder.modifyLiFieldValue'('bid strategy value', bidStrategyValue)
CustomKeywords.'project.Builder.modifyLiFieldValue'('bid strategy unit', "CPA")

File sdfName = CustomKeywords.'project.Builder.download'()
ArrayList<String> FileToUpload= CustomKeywords.'project.Builder.unzip'(sdfName)

totalLineInCsv = CustomKeywords.'project.Builder.csvCountLine'(FileToUpload[3])

//We check if the number of created line is the same in builder and in the CSV
assert totalLineChecked.equals(totalLineInCsv) : "Not the same number of lines in CSV and lines created in builder"

//We check values added in the first cp, oi and li are correct
CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[1], "Campaign Goal KPI", "Other")
CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[1], "Campaign Goal KPI Value", goalKpiValue)

CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[2], "Performance Goal Type", "Other")
CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[2], "Performance Goal Value", "")

CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[3], "Bid Strategy Value", bidStrategyValueRounded)

testcase.deleteFile(FileToUpload[0])
testcase.deleteFile(FileToUpload[1])
testcase.deleteFile(FileToUpload[2])
testcase.deleteFile(FileToUpload[3])
