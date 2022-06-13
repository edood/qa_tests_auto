import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable
import project.CPSettings

Date date = new Date()

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-243')
CustomKeywords.'project.Builder.Start'(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

CustomKeywords.'com.reusableComponents.DV360.logInToDv360'(true)
String currency = CustomKeywords.'com.reusableComponents.DV360.getAdvertiserCurrencyfromDV360'(GlobalVariable.advertiserName)
WebUI.switchToWindowIndex(0)
CustomKeywords.'project.Builder.load'('CP', '', '', '')
CPSettings CSetup = new CPSettings(CustomKeywords.'project.CPSettings.createCampaignForCurrencyCheck'(GlobalVariable.itemNames[0] + date.getTime(), "Paused", "3", currency, 0, 1))
CSetup.ID = CustomKeywords.'project.CPSettings.getID'(findTestObject('Object Repository/6. Objects/First Loaded Campaign'))
CustomKeywords.'project.CPSettings.VerifyBuilderCurrency'(findTestObject('6. Objects/First Loaded Campaign'), currency)

File sdfName = CustomKeywords.'project.Builder.download'()
ArrayList<String> FileToUpload = CustomKeywords.'project.Builder.unzip'(sdfName)

CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[1], "Campaign Id", "ext" + CSetup.ID)
CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[1], "Name", CSetup.Name)
CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[1], "Status", CSetup.Status)
CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[1], "Campaign Budget", CSetup.BudgetTotal)
CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[1], "Campaign Start Date", CSetup.Start)
CustomKeywords.'project.Builder.checkCsvByName'(FileToUpload[1], "Campaign End Date", CSetup.End)

CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(FileToUpload)
CustomKeywords.'project.CPSettings.DV360VerifyUpload'(CSetup, "Currency")
CustomKeywords.'com.reusableComponents.DV360.archiveObject'([CSetup.Name], "CP")
CustomKeywords.'project.Builder.deleteFile'(FileToUpload[0])
CustomKeywords.'project.Builder.deleteFile'(FileToUpload[1])
