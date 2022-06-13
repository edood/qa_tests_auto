import project.Builder 
import internal.GlobalVariable
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.testobject.SelectorMethod
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.util.KeywordUtil

Builder testcase = new Builder()
Date timestamp = new Date()
Long uniquename = timestamp.getTime()
String linerenamed = 'renamed_' + uniquename

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-221')
testcase.Start(false)
WebUI.delay(1)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

WebUI.delay(10)
testcase.refreshAdvertiser()
testcase.load('LI', '', '', '')
testcase.checkLoadedStatus()
ArrayList<String> CampaignSetup = testcase.createNewCampaign("QAP-221 Campaign Auto test_" + uniquename, "Active", 0, 1)
testcase.CreateOIfromCampaign(CampaignSetup.get(0), "QAP-221 OI Auto")
testcase.renameObj(findTestObject('Object Repository/6. Objects/First Loaded Line Item'), linerenamed, "LI")
testcase.VerifyStatus(2, 1, 2)
testcase.VerifyLoadedTag()

testcase.VerifyEditedTag()
WebUI.click(findTestObject('Object Repository/6. Objects/Tag LI'))
WebDriver driver = DriverFactory.getWebDriver()
ArrayList<WebElement> Alltypes = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/All object type').getSelectorCollection().get(SelectorMethod.CSS)))
for (def elem in Alltypes)
{
	if (elem.isDisplayed() == true)
		assert elem.getText().trim() == 'LI' : "CP or OI should not be displayed"
}


testcase.VerifyCreatedTag()
WebUI.click(findTestObject('Object Repository/6. Objects/Tag Cp'))
WebUI.click(findTestObject('Object Repository/6. Objects/Tag OI'))
driver = DriverFactory.getWebDriver()
Alltypes = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/All object type').getSelectorCollection().get(SelectorMethod.CSS)))
for (def elem in Alltypes)
{
	if (elem.isDisplayed() == true)
		assert elem.getText().trim() != 'LI' : "LI should not be displayed"
}


File sdfFile = testcase.download()

testcase.selenium.openWindow("","")
WebUI.switchToWindowIndex(1)

ArrayList<String> sdfpaths = CustomKeywords.'project.Builder.unzip'(sdfFile)

if (CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(sdfpaths) == false)
{
	KeywordUtil.markFailedAndStop('Upload failed')
	return 
}

WebUI.switchToWindowIndex(0)

testcase.logout()
CustomKeywords.'project.Builder.login'(GlobalVariable.googleID, GlobalVariable.googlePassword, GlobalVariable.checkHref, false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)
WebUI.delay(10)
testcase.refreshAdvertiser()
testcase.load('LI', '', '', linerenamed)
testcase.checkLoadedStatus()

WebUI.switchToWindowIndex(1)

CustomKeywords.'com.reusableComponents.DV360.archiveObject'([CampaignSetup.get(0)], "CP")

testcase.deleteFile(sdfpaths[0])
testcase.deleteFile(sdfpaths[1])
testcase.deleteFile(sdfpaths[2])
testcase.deleteFile(sdfpaths[3])
