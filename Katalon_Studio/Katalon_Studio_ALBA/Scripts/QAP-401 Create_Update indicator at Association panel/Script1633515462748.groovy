import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-401')

CustomKeywords.'project.Albatros.Start'()

WebUI.delay(5)

String clientname = CustomKeywords.'project.Albatros.CreateSelectClientTp'(GlobalVariable.isSelectClient, GlobalVariable.newClientName, GlobalVariable.clientNameToSelect, GlobalVariable.isSelectTP, GlobalVariable.trackingPlanNameToSelect, GlobalVariable.tpSetup, GlobalVariable.createTP)
String keys = GlobalVariable.createAssociation
Map editIndicator = GlobalVariable.editIndicator
Map indicatorsFilter = GlobalVariable.indicatorsFilter

CustomKeywords.'project.Albatros.filterIndicators'(indicatorsFilter['Primary'], indicatorsFilter['Module'])

CustomKeywords.'project.Albatros.selectIndicators'()
CustomKeywords.'project.Albatros.selectAssociatedIndicators'()
CustomKeywords.'project.Albatros.removeAssociation'()

WebUI.delay(1)
WebUI.click(findTestObject('Object Repository/8. Indicators List/All indicators'))
WebUI.delay(1)
WebUI.click(findTestObject('Object Repository/8. Indicators List/All indicators'))

ArrayList<Integer> indicators = CustomKeywords.'project.Albatros.selectIndicators'()
CustomKeywords.'project.Albatros.createAssociation'()
CustomKeywords.'project.Albatros.checkAssociationDisplayed'(indicators, keys)
CustomKeywords.'project.Albatros.searchAtAssociationPanel'(keys)

WebUI.delay(1)
WebUI.click(findTestObject('8. Indicators List/Key name association'))
boolean isDualTracking = CustomKeywords.'project.Albatros.checkIsDualtracking'("AssociationPanel")

CustomKeywords.'project.Albatros.editAssociated'(editIndicator, isDualTracking)
WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Save Changes button'))
assert WebUI.waitForElementNotPresent(findTestObject("Object Repository/8. Indicators List/2. Edit association/Sidenav visible"), 10)

CustomKeywords.'project.Albatros.searchAtAssociationPanel'(editIndicator['Key'])

CustomKeywords.'project.Albatros.checkAssociatedVariable'()

if (indicatorsFilter['Primary'] == "Variable")
	{

		CustomKeywords.'project.Albatros.filterIndicators'("Event", indicatorsFilter['Module'])
		
		CustomKeywords.'project.Albatros.searchAtIndicatorPanel'(editIndicator['Key'])
		WebUI.delay(2)
		WebUI.click(findTestObject('8. Indicators List/Key name indicator'))
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/8. Indicators List/2. Edit association/Sidenav visible'), 5)
		WebUI.delay(2)
		isDualTracking = CustomKeywords.'project.Albatros.checkIsDualtracking'("IndicatorPanel", "Event")

		WebUI.click(findTestObject('Object Repository/8. Indicators List/Arrow sidenav'))
		
		WebUI.delay(1)
		WebUI.click(findTestObject('Object Repository/8. Indicators List/All indicators'))
		CustomKeywords.'project.Albatros.searchAtAssociationPanel'(editIndicator['Key'])
		if (isDualTracking)
			{
				assert WebUI.waitForElementPresent(findTestObject('Object Repository/8. Indicators List/Event action label'), 5): "Event action and event label icon are not visible for the edited key"
				KeywordUtil.logInfo("Event action and event label icon are visible for the key")
			}
		
	}

