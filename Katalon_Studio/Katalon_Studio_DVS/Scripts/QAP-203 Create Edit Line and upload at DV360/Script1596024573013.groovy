import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.bouncycastle.jcajce.provider.asymmetric.ec.GMSignatureSpi.sm3WithSM2

import project.LineSettings

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-203')
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
String uniquename1 = GlobalVariable.itemNames[2] + new Date().getTime()
String uniquename2 = 'QAP-203_test_auto_' + new Date().getTime()
LineSettings testcase = new LineSettings(uniquename2)

testcase.Start(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)
testcase.refreshAdvertiser()
testcase.load('LI', '', '', '')
testcase.CreateLinefromLoadedCampaignLoadedOI(0, 1, uniquename1, uniquename2)
testcase.QuickDuplicate('LI', findTestObject('Object Repository/6. Objects/Second LI created'))
testcase.AssignCreatives(GlobalVariable.type[0], '', findTestObject('Object Repository/6. Objects/Specific LI Duplicated Object'))
testcase.ShareSettings('creative_assignments', findTestObject('Object Repository/6. Objects/Specific LI Duplicated Object'), uniquename1)
testcase.CheckSettingsApplied('creative_assignments', uniquename1, findTestObject('Object Repository/6. Objects/Specific LI Duplicated Object'))
String uniquename3 = 'QAP-203_test_auto_' + new Date().getTime()
testcase.renameObj(findTestObject('Object Repository/6. Objects/Specific LI Duplicated Object'), uniquename3, 'LI')

File sdfFile = testcase.download()
ArrayList<String> FileToUpload = testcase.unzip(sdfFile)
CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(FileToUpload)

testcase.deleteFile(FileToUpload[0])
testcase.deleteFile(FileToUpload[1])

CustomKeywords.'com.reusableComponents.DV360.archiveObject'([uniquename1, uniquename2, uniquename3], 'LI')
