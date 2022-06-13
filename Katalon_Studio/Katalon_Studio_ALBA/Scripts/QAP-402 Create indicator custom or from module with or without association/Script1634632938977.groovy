import internal.GlobalVariable

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys


CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-402')
CustomKeywords.'project.Albatros.Start'()

WebUI.delay(5)
String[] name = CustomKeywords.'project.Albatros.CreateSelectClientTp'(GlobalVariable.isSelectClient, GlobalVariable.newClientName, GlobalVariable.clientNameToSelect, GlobalVariable.isSelectTP, GlobalVariable.trackingPlanNameToSelect, GlobalVariable.tpSetup, GlobalVariable.createTP)
String indicatorFilters = GlobalVariable.createfromModule['indicatorFilters']
CustomKeywords.'project.Albatros.deleteIndicator'(GlobalVariable.indicatorsFilter)
CustomKeywords.'project.Albatros.deleteIndicatorPlan'()


//QAP-403

WebUI.delay(5)

int nbindicatorbefore = CustomKeywords.'project.Albatros.getTrackingPlanIndicatorCount'()
CustomKeywords.'project.Albatros.filterIndicators'(indicatorFilters, "All", false)
WebUI.click(findTestObject('8. Indicators List/Button add primary'))
assert WebUI.waitForElementVisible(findTestObject('11. Add Indicator Panel/Search input'), 5) == true : "Panel Add indicators not displayed after 5sec"
Map AddedIndicator = CustomKeywords.'project.Albatros.addIndicator'(GlobalVariable.createfromModule)
assert WebUI.waitForElementVisible(findTestObject('5. Homepage/Snackbar message'), 20) == true : "Popup not displayed after 20 second"
assert WebUI.waitForElementNotPresent(findTestObject('5. Homepage/Snackbar message'), 5) == true : "Popup still displayed after 5 second"
int nbindicatorafter = CustomKeywords.'project.Albatros.getTrackingPlanIndicatorCount'()
assert nbindicatorbefore < nbindicatorafter : "Indicator were not added"
CustomKeywords.'project.Albatros.checkIndicatorList'(AddedIndicator)


//QAP-406
if (GlobalVariable.createfromModule["allModules"])
	KeywordUtil.logInfo("All modules is selected. QAP-406 is not taken account")
else {
	String indicatorFiltersForAssociation = GlobalVariable.createfromAssociated['indicatorFilters']
	CustomKeywords.'project.Albatros.filterIndicators'(indicatorFiltersForAssociation, "All")
	CustomKeywords.'project.Albatros.selectIndicators'(false, 1)
	CustomKeywords.'project.Albatros.selectAssociatedIndicators'(false, 1)
	
	CustomKeywords.'project.Albatros.createAssociationButton'()
	assert WebUI.waitForElementPresent(findTestObject('Object Repository/8. Indicators List/1. Create association/1. Create New/Create new button'), 5)
	
	WebUI.click(findTestObject('Object Repository/8. Indicators List/1. Create association/1. Create New/Create new button'))
	Map AddedIndicatorAssociation = CustomKeywords.'project.Albatros.addIndicator'(GlobalVariable.createfromAssociated)
	assert WebUI.waitForElementPresent(findTestObject('Object Repository/8. Indicators List/Indicator cells'), 10)
	assert WebUI.waitForElementPresent(findTestObject('Object Repository/8. Indicators List/Associated cells'), 10)
	CustomKeywords.'project.Albatros.checkAssociationListForNewAssociation'(AddedIndicatorAssociation)
	
}