import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.Instant

import org.openqa.selenium.Keys

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.DV360

import internal.GlobalVariable
import project.Builder

Builder testcase = new Builder()
DV360 dv360 = new DV360()

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-220')

testcase.Start(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

testcase.refreshAdvertiser()
testcase.load('LI', '', '', '')
String newLineName = 'Computed Daily Budget Line ' + Instant.now().getEpochSecond()
WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Insertion Order'))
// Creates a new line item, verifies the computed budget in the next step
String[] createdItems = testcase.CreateCpIoLineWithFiltersCheckComputing("new", 0, 1)
WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Insertion Order'))
testcase.modifyIoFieldValue("budget interval budget", Keys.chord(Keys.SHIFT, Keys.ARROW_UP)+"2")

File sdfFile = testcase.download()
ArrayList<String> FileToUpload = testcase.unzip(sdfFile)

dv360.DV360Upload(FileToUpload)

testcase.deleteFile(FileToUpload[0])
testcase.deleteFile(FileToUpload[1])

dv360.archiveObject([createdItems[0]], 'CP')

