import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'("")

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'project.AutoPause.Start'()

CustomKeywords.'project.AutoPause.randomSearch'()
CustomKeywords.'project.AutoPause.checkSort'()

WebUI.click(findTestObject('Object Repository/2. Schedule List/Passed Tab button'))

CustomKeywords.'project.AutoPause.randomSearch'()
CustomKeywords.'project.AutoPause.checkSort'()

WebUI.click(findTestObject('Object Repository/2. Schedule List/Future Tab button'))

CustomKeywords.'project.AutoPause.randomSearch'()
CustomKeywords.'project.AutoPause.checkSort'()