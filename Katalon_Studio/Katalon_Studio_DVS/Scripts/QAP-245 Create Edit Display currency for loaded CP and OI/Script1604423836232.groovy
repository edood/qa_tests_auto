import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

String[] IO = (GlobalVariable.items[1]).split(";")
String currencyIoBudget

String liName
String liName1

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-245')
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'project.Builder.Start'(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

CustomKeywords.'project.Builder.refreshAdvertiser'()

CustomKeywords.'project.Builder.load'("OI", "", IO[0], "")
CustomKeywords.'project.Builder.createNewLineGeneric'("Loaded", "Loaded", "Oi example", 1, "1", IO[0])

liName = 'QAP-245_test_auto_' + new Date().getTime()
CustomKeywords.'project.Builder.xsetText'(findTestObject('Object Repository/4. Creator/Line/1. Next step/First Name'), liName)
WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))

WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Insertion Order'))
assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget currency'), 10)
WebUI.delay(2)
currencyIoBudget =  WebUI.getText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget currency'))
CustomKeywords.'com.reusableComponents.HighlightElement.on1'(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget currency'))

CustomKeywords.'project.Builder.compareCurrencyWithDB'(currencyIoBudget, IO[0])

WebUI.click(findTestObject('Object Repository/5. Editor/Builder Close Editor button'))

if(IO.size() > 1){
	CustomKeywords.'project.Builder.load'("OI", "", IO[1], "")
	CustomKeywords.'project.Builder.createNewLineGeneric'("Loaded", "Loaded", "Oi example2", 2, "1", IO[1])

	liName1 = 'QAP-245_test_auto_' + new Date().getTime()
	CustomKeywords.'project.Builder.xsetText'(findTestObject('Object Repository/4. Creator/Line/1. Next step/First Name'), liName1)
	WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))
	WebUI.delay(1)
	WebUI.click(findTestObject('Object Repository/6. Objects/Second OI span'))
	assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget currency'), 10)
	currencyIoBudget =  WebUI.getText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget currency'))
	CustomKeywords.'com.reusableComponents.HighlightElement.on1'(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget currency'))
	CustomKeywords.'project.Builder.compareCurrencyWithDB'(currencyIoBudget, IO[1])
	WebUI.click(findTestObject('Object Repository/5. Editor/Builder Close Editor button'))
}

File sdfFile = CustomKeywords.'project.Builder.download'()
ArrayList<String> sdfPaths = CustomKeywords.'project.Builder.unzip'(sdfFile)

CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(sdfPaths)

CustomKeywords.'project.Builder.deleteFile'(sdfPaths[0])
CustomKeywords.'project.Builder.deleteFile'(sdfPaths[1])

CustomKeywords.'com.reusableComponents.DV360.archiveObject'([liName], 'LI')
if(IO.size() > 1){
	CustomKeywords.'com.reusableComponents.DV360.archiveObject'([liName1], 'LI')
}
