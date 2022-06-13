import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable
import project.Builder

String io_test = GlobalVariable.items[1]
boolean hotspot = GlobalVariable.hotspot

Builder testcase = new Builder()


CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-270')
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()


if (hotspot)
{
KeywordUtil.logInfo("Hotspot = true")
CustomKeywords.'project.Builder.Start'(hotspot)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)
CustomKeywords.'project.Builder.checkHotspots'(io_test, hotspot)
assert WebUI.waitForElementPresent(findTestObject('Object Repository/6. Objects/Empty builder button'), 30)
WebUI.click(findTestObject('Object Repository/6. Objects/Empty builder button'))
WebUI.click(findTestObject('Object Repository/6. Objects/Empty builder yes button'))
	
hotspot = false

	for(int i =0; i<2; i++)
	{
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/6. Objects/Tradelab logo'), 30)
		WebUI.click(findTestObject('Object Repository/6. Objects/Tradelab logo'))
		assert WebUI.waitForElementPresent(findTestObject('2. Homepage/Advertiser Input'), 30)
		WebUI.executeJavaScript("document.querySelector('"+testcase.getOnlySelector(findTestObject('Object Repository/6. Objects/Hide Display hotspot button'))+"').click()", null)
		String hiddenOrDisplayedHotspots = WebUI.executeJavaScript("return document.querySelector('"+testcase.getOnlySelector(findTestObject('Object Repository/6. Objects/Hide Display hotspot button'))+"').textContent", null)
		KeywordUtil.logInfo(hiddenOrDisplayedHotspots.trim())
		testcase.selectadvertiser(GlobalVariable.advertiserName)
		CustomKeywords.'project.Builder.checkHotspots'(io_test, hotspot)
		hotspot = true
	}
} else 
{
	KeywordUtil.logInfo("Hotspot = false")
	CustomKeywords.'project.Builder.Start'(hotspot)
	assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Logout'), 30)
	WebUI.executeJavaScript("document.querySelector('"+testcase.getOnlySelector(findTestObject('Object Repository/2. Homepage/Logout'))+"').click()", null)
	
}
