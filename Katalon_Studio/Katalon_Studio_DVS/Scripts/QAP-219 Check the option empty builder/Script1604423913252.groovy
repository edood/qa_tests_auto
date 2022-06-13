import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.reusableComponents.TargetObject as TargetObject
import project.Targeting
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-219')
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'project.Builder.Start'(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

CustomKeywords.'project.Builder.refreshAdvertiser'()

Targeting testcase = new Targeting()

LinkedList<TargetObject> target = []

String io_test = GlobalVariable.items[1]
String line_test = GlobalVariable.items[2]
int[] tabLi = [ 0, 0]
int[] tabIo = [ 0, 0]
int[] ObjectToInclude = [1,2]
int[] ObjectToExclude = [1]

int includedPixels = 0


int[] audienceObjectsToInclude = testcase.getAudienceObjectsToTarget("Include", ObjectToInclude)



CustomKeywords.'project.Builder.load'('OI', '', '', '')
assert WebUI.waitForElementPresent(findTestObject('Object Repository/6. Objects/First Loaded Insertion Order'), 5) : "Io object is not visible"
WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Insertion Order'))
WebUI.delay(1)
WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Audience category'))
tabIo = CustomKeywords.'project.Builder.checkNumberOfIncludedExcludedAudiences'()

target = testcase.EditTargeting("Audience", 'Insertion Order', 'Include', audienceObjectsToInclude, target)

CustomKeywords.'project.Builder.removeAllExcludedElements'()

target = testcase.EditTargeting("Audience", 'Insertion Order', 'Exclude', ObjectToExclude, target)

CustomKeywords.'project.Builder.load'('LI', '', '', '')
assert WebUI.waitForElementPresent(findTestObject('Object Repository/6. Objects/First Loaded Line Item'), 5) : "Li object is not visible"
WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Line Item'))
WebUI.delay(1)

WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Audience category'))

tabLi = CustomKeywords.'project.Builder.checkNumberOfIncludedExcludedAudiences'()
target = testcase.EditTargeting("Audience", 'Line Item', 'Include', audienceObjectsToInclude, target)

CustomKeywords.'project.Builder.removeAllExcludedElements'()

target = testcase.EditTargeting("Audience", 'Line Item', 'Exclude', ObjectToExclude, target)

includedPixels = CustomKeywords.'project.Builder.checkNumberOfIncludedConversionPixels'()

target = testcase.EditTargeting("Conversion Pixel", 'Line Item', 'Include', audienceObjectsToInclude, target)

CustomKeywords.'project.Builder.checkObjectStatus'(["LOADED", "EDITED", "EDITED"])

File sdfFile = CustomKeywords.'project.Builder.download'()

CustomKeywords.'project.Builder.emptyBuilder'()

CustomKeywords.'project.Builder.load'('OI', '', '', '')
CustomKeywords.'project.Builder.load'('LI', '', '', '')
CustomKeywords.'project.Builder.checkObjectStatus'(["LOADED", "LOADED", "LOADED"])

assert WebUI.waitForElementPresent(findTestObject('Object Repository/6. Objects/First Loaded Insertion Order'), 5) : "Io object is not visible"
WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Insertion Order'))
WebUI.delay(1)
WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Audience category'))

boolean tabIoResult = Arrays.equals(tabIo, CustomKeywords.'project.Builder.checkNumberOfIncludedExcludedAudiences'())

assert tabIoResult : "Excluded or included IO items are not the same at the start and at the end of the test "
KeywordUtil.markPassed("Number of included and excluded Io items are the same before and after the test")

assert WebUI.waitForElementPresent(findTestObject('Object Repository/6. Objects/First Loaded Line Item'), 5) : "Li object is not visible"
WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Line Item'))
WebUI.delay(1)
WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Audience category'))

boolean tabLIResult = Arrays.equals(tabLi, CustomKeywords.'project.Builder.checkNumberOfIncludedExcludedAudiences'())

assert tabLIResult : "Excluded or included Li items are not the same at the start and at the end of the test "
KeywordUtil.markPassed("Number of included and excluded Li items are the same before and after the test")

assert includedPixels == CustomKeywords.'project.Builder.checkNumberOfIncludedConversionPixels'() : "Included pixels are not the same before and after the test"
KeywordUtil.markPassed("Included pixels are the same before and after the test")

//delete only zip file
String fullPath = sdfFile.absolutePath

testcase.deleteFile(fullPath)





