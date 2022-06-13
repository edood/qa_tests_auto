import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import project.Builder

import internal.GlobalVariable

Builder testcase = new Builder()
String item = GlobalVariable.items[2]
Map templateFiltersToUse = [:]
Map defaultTemplateFilters = [('Strategy') : 'Prospection,Profiling,Retargeting', 
	('Goal') : 'Generic,Visit,Lead/Sale,Viewed video,Visibility,Reach on target', 
	('Optimization') : 'Generic,PC + PV,PC', 
	('Delivery type') : 'Video,Display,Audio', 
	('Format') : 'Video,IAB,Native,Interstitial,Skin,Masthead,Audio', 
	('Device') : 'Desktop,Mobile', 
	('Environment') : 'Web,In-App', 
	('Geolocation') : 'default', 
	('Daily budget') : 'default,default']

// If global variable templateFilters is empty, we use the default Map
boolean isTemplateFiltersFilled = false
if(GlobalVariable.templateFilters != [""]) {
	for(def filterColumn in GlobalVariable.templateFilters) {
		if(filterColumn.value.split(",") != [""]) {
			isTemplateFiltersFilled = true
}	}	}
if(isTemplateFiltersFilled == false) templateFiltersToUse = defaultTemplateFilters
else templateFiltersToUse = GlobalVariable.templateFilters

// Start test case
CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-242')
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
testcase.Start(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

def selenium = testcase.getSelenium()
testcase.refreshAdvertiser()
testcase.load('LI', '', '', '')

// Reopening the Template filters
testcase.goToCreator()
testcase.selectCreatorDimension("Line")
testcase.openTemplateFilters()
for(def filterColumn in templateFiltersToUse) {
	if(filterColumn.value.split(",") != [""]) {
		if(filterColumn.key == "Daily budget" && !selenium.isElementPresent(testcase.getSelector(findTestObject('Object Repository/4. Creator/Template Filters/Daily Budget Search Low input'))))
			continue
		testcase.selectTemplateFilterType()		
		Map selectedTemplateFiltersMap = testcase.selectTemplateFilters(filterColumn)
		testcase.closeTemplateFilters("Chevron")
		testcase.checkFilteredTemplatesViaGCP(selectedTemplateFiltersMap)
		testcase.openTemplateFilters()
		testcase.resetTemplateFilters()
}	}

for(def filterColumn in templateFiltersToUse) {
	if(filterColumn.key == "Daily budget" && !selenium.isElementPresent(testcase.getSelector(findTestObject('Object Repository/4. Creator/Template Filters/Daily Budget Search Low input'))))
		KeywordUtil.markErrorAndStop("There is no Daily budget to set, and it is the only filter filled in the global variable.")
	if(filterColumn.value.split(",") != [""]) {
		Map selectedTemplateFiltersMap = testcase.selectTemplateFilters(filterColumn)
		testcase.closeTemplateFilters("Chevron")
		testcase.openTemplateFilters()
		testcase.checkTemplateFilters(selectedTemplateFiltersMap)
		break
}	}

// Reset
testcase.resetTemplateFilters()

// Closing the template filters
testcase.closeTemplateFilters("Overlay")
testcase.emptyBuilder()

// Template filters button not displayed
testcase.goToCreator()
testcase.selectCreatorDimension("Campaign")
assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/4. Creator/Template Filters button'), 2)