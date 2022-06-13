import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.Common

import internal.GlobalVariable as GlobalVariable

String sheetTitle
boolean createindic = false
Map indicatorsFilter = GlobalVariable.indicatorsFilter
Map editIndicator = GlobalVariable.editIndicator


CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-399')

CustomKeywords.'project.Albatros.Start'()

String clientname = CustomKeywords.'project.Albatros.CreateSelectClientTp'(GlobalVariable.isSelectClient, GlobalVariable.newClientName, GlobalVariable.clientNameToSelect, GlobalVariable.isSelectTP, GlobalVariable.trackingPlanNameToSelect, GlobalVariable.tpSetup, GlobalVariable.createTP)

CustomKeywords.'project.Albatros.filterIndicators'(indicatorsFilter['Primary'], indicatorsFilter['Module'])

CustomKeywords.'project.Albatros.checkRandomIndicatorPopupAndEdit'()

// In case we edited the indicator to a different module
if(editIndicator['Module'] != indicatorsFilter['Module'])
	CustomKeywords.'project.Albatros.filterIndicators'(indicatorsFilter['Primary'], editIndicator['Module'])

ArrayList<String> indicatorsElements = WebUI.findWebElements(findTestObject('Object Repository/8. Indicators List/Indicators'), 1).collect { it.getText() }

String searchKey = GlobalVariable.searchColumn
CustomKeywords.'project.Albatros.searchAtIndicatorPanel'(editIndicator[searchKey])
CustomKeywords.'project.Albatros.checkIndicatorSearchResult'(searchKey, editIndicator[searchKey])

WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator Search Bar close button'))
Common common = new Common()
common.assertValue(findTestObject('Object Repository/8. Indicators List/Indicator Search Bar'), "")
WebUI.delay(1)

ArrayList<String> newindicatorsElements = WebUI.findWebElements(findTestObject('Object Repository/8. Indicators List/Indicators'), 1).collect { it.getText() }

for(int i = 0 ; i < newindicatorsElements.size() ; i++)
{
	assert indicatorsElements.get(i) == newindicatorsElements.get(i) : "\n\nThe list of indicators has changed after the search at line " + (i+1) + ".\nBefore : " + indicatorsElements.get(i) + "\nAfter : " + newindicatorsElements.get(i) + "\n\n"
}