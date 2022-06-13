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

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-318')

assert GlobalVariable.newClientName != '' : 'Global variable newClientName should not be empty, please fill it.'

CustomKeywords.'project.Albatros.Start'()

assert WebUI.waitForElementPresent(findTestObject('5. Homepage/Welcome header'), 10) : 'Login failed'

CustomKeywords.'project.Albatros.goToClientManagement'()

CustomKeywords.'project.Albatros.checkClientListSort'(['Name'], [''], true)

CustomKeywords.'project.Albatros.createClient'(GlobalVariable.newClientName, true)

String newClientName = CustomKeywords.'project.Albatros.createClient'(GlobalVariable.newClientName, false)

CustomKeywords.'project.Albatros.searchClient'('3ù*&é\\^--//Y:/:', true)

CustomKeywords.'project.Albatros.editClientName'(newClientName, GlobalVariable.editClientName)

CustomKeywords.'project.Albatros.checkClientListSort'(['ID', 'Name'], ['Ascending', 'Descending'])

