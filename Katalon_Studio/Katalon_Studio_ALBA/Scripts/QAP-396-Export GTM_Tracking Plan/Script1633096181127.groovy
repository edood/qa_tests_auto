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
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys


CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-357')

CustomKeywords.'project.Albatros.Start'()

String optionfromplan = "Click to select option"

if (GlobalVariable.isSelectTP == false && GlobalVariable.createTP["selectType"] == "Plan")
	optionfromplan = CustomKeywords.'project.Albatros.getTrackingPlanOptions'(GlobalVariable.createTP["sourceClient"], GlobalVariable.createTP["sourcePlan"])

String[] name = CustomKeywords.'project.Albatros.CreateSelectClientTp'(GlobalVariable.isSelectClient, GlobalVariable.newClientName, GlobalVariable.clientNameToSelect, GlobalVariable.isSelectTP, GlobalVariable.trackingPlanNameToSelect, GlobalVariable.tpSetup, GlobalVariable.createTP)
if(GlobalVariable.consentMode) {
	CustomKeywords.'project.Albatros.selectTrackingPlanOption'("Consent Mode")
	optionfromplan = "Consent Mode"
} else {
	CustomKeywords.'project.Albatros.selectTrackingPlanOption'("Click to select option")
	optionfromplan = "Click to select option"
}

if (GlobalVariable.isSelectTP == false)
	CustomKeywords.'project.Albatros.checkDetailBar'(GlobalVariable.tpSetup, optionfromplan)

File gtm = CustomKeywords.'project.Albatros.exportGTM'(name[0], name[1])
