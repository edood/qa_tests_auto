import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.Instant

import com.kms.katalon.core.util.KeywordUtil
import project.OISettings

import internal.GlobalVariable as GlobalVariable


String io1 = GlobalVariable.itemNames[1] + "_" + Instant.now().getEpochSecond()
String io2 = GlobalVariable.itemNames[1] + "_2_" + Instant.now().getEpochSecond()
OISettings testcase = new OISettings(io1)

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-201')
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

testcase.Start(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)
testcase.refreshAdvertiser()
testcase.load('OI', '', '', '')
testcase.CreateOIfromLoadedCampaign()
testcase.EditSetting(findTestObject('Object Repository/6. Objects/Second OI span'))
testcase.QuickDuplicate('OI', findTestObject('Object Repository/6. Objects/Second OI'))
testcase.renameObj(findTestObject('Object Repository/6. Objects/Second OI'), io1, 'OI')
testcase.ShareSettings("Name", findTestObject('Object Repository/6. Objects/Third OI'), "QAWaveYoutube")
testcase.CheckSettingsApplied("Name", io1, findTestObject('Object Repository/6. Objects/Second OI'))
testcase.renameObj(findTestObject('Object Repository/6. Objects/Second OI'), io2, 'OI')
File sdfFile = testcase.download()
ArrayList<String> sdfPaths = testcase.unzip(sdfFile)
CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(sdfPaths)


String[] Uploadedlines = [io1, io2]
CustomKeywords.'com.reusableComponents.DV360.DisplayUploadedLine'(Uploadedlines)
testcase.DV360VerifyUpload(io1, 'OI Settings')
testcase.deleteFile(sdfPaths[0])
testcase.deleteFile(sdfPaths[1])
CustomKeywords.'com.reusableComponents.DV360.archiveObject'([io1], 'OI')