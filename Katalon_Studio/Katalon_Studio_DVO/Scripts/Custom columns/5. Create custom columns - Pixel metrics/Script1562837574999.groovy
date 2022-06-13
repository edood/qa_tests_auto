import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium as WebDriverBackedSelenium
import com.reusableComponents.OptimizerDV360
import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')
def driver = DriverFactory.getWebDriver()
String baseUrl = 'https://www.katalon.com/'

OptimizerDV360 testcase = new OptimizerDV360()

selenium = new WebDriverBackedSelenium(driver, baseUrl)

WebUI.waitForPageLoad(30)

WebUI.maximizeWindow()

String tiret = '-'
if (((GlobalVariable.env != 'dev') && (GlobalVariable.env != 'staging')) && (GlobalVariable.env != 'prod')) {
	GlobalVariable.env2 = 'saas'
} else if (GlobalVariable.env == 'prod') {
	tiret = ''
	GlobalVariable.env = ''
}

WebUI.navigateToUrl('https://' + GlobalVariable.env + tiret + GlobalVariable.env2 + ".tradelab.fr")

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

WebUI.delay(2)

WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

WebUI.click(findTestObject('Object Repository/1. Login/Login Button'))

WebUI.delay(2)

String url = ('https://' + GlobalVariable.env + tiret + GlobalVariable.env2 + ".tradelab.fr/") + GlobalVariable.advertiser

WebUI.navigateToUrl(url)

WebUI.waitForPageLoad(30)

WebUI.delay(2)

WebUI.waitForElementPresent(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'), 30)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/Columns tab'))

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/Columns tab'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))

testcase.selectDateRange(GlobalVariable.dateRange)

//
//int interval = 2
//
//String intervalOption = 'body > div.cdk-overlay-container > div.cdk-overlay-connected-position-bounding-box > div > div > mat-option:nth-child(' + interval + ')'
// 
//TestObject intervalDynamicOption = new TestObject('IntervalOption')
// 
//intervalDynamicOption.addProperty('css', ConditionType.EQUALS, intervalOption)
//
//WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Interval selector'))
// 
//WebUI.delay(1)
//
//WebUI.click(intervalDynamicOption)
 
//WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Daterange-Interval-Validator'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30, FailureHandling.STOP_ON_FAILURE)
WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/3. Grid/Loading Progress Bar'), 300)

WebUI.click(findTestObject('DV360 Optimizer/9. Config/Config menu'))

WebUI.click(findTestObject('DV360 Optimizer/9. Config/Config select'))

WebUI.click(findTestObject('DV360 Optimizer/9. Config/First config'))
 
WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30, FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/Columns tab'))

Number numberOfSelectedColumns = selenium.getCssCount('div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')

while(numberOfSelectedColumns > 0) {
	selenium.click('css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')
	
	numberOfSelectedColumns = numberOfSelectedColumns - 1
}

selenium.click('css=div.ag-toolpanel-indent-1:nth-of-type(6) > span.ag-column-select-checkbox:nth-of-type(1) > span.ag-checkbox-unchecked:not(.ag-hidden)')

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/Custom columns tab'))

Thread.sleep(500)

Number numberOfCustomColumns = selenium.getCssCount(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab remove icons').getSelectorCollection()
	.get(SelectorMethod.CSS))

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/Columns tab'))

println numberOfCustomColumns

int customColumnName = 1020

int i = 19
int j = 20
int k = 5
int l = 1
int m = 1
float calculatedSummary = 0
float summaryDiff = 0
float threshold = 0
int increment = 0
String currentLine = 0
float customColumnSummary = 0.00f
String firstValue = ""
String secondValue = ""

while(i < 25) {
		while(k < 6) {
			if(j !=  25) {
				l = 6 + i + numberOfCustomColumns * 2
				m = 6 + j + numberOfCustomColumns * 2
				selenium.click('css=div.ag-toolpanel-indent-1 > span.ag-column-select-checkbox:nth-of-type(1) > span.ag-checkbox-checked:not(.ag-hidden)')
				
				boolean isSecondColumnPresent = selenium.isElementPresent('css=div.ag-toolpanel-indent-1 > span.ag-column-select-checkbox:nth-of-type(1) > span.ag-checkbox-checked:not(.ag-hidden)')
				
				if(isSecondColumnPresent == true) {
					selenium.click('css=div.ag-toolpanel-indent-1 > span.ag-column-select-checkbox:nth-of-type(1) > span.ag-checkbox-checked:not(.ag-hidden)')
				}
					
				if(m != l) {
					selenium.click('css=div.ag-toolpanel-indent-1:nth-of-type(' + m + ') > span.ag-column-select-checkbox:nth-of-type(1) > span.ag-checkbox-unchecked:not(.ag-hidden)')
				}
				
				selenium.click(('css=div.ag-toolpanel-indent-1:nth-of-type(' + l) + ') > span.ag-column-select-checkbox:nth-of-type(1) > span.ag-checkbox-unchecked:not(.ag-hidden)')
				
				WebUI.click(findTestObject('DV360 Optimizer/3. Grid/Custom columns tab'))
				
				selenium.type('css=div.name-custom-columns > input', customColumnName.toString())
				
				selenium.fireEvent('css=div.name-custom-columns > input', 'input')
				
				selenium.click('css=div.metric-selector:nth-of-type(2) > mat-select > div')
				
				Thread.sleep(500)
				
				selenium.click(('css=div.cdk-overlay-pane > div > mat-option:nth-of-type(' + l - 6) + ')')
				
				selenium.click('css=custom-columns-panel')
				
				selenium.click('css=div.metric-selector:nth-of-type(3) > mat-select > div')
				
				Thread.sleep(500)
				
				String operator = selenium.getText(('css=div.cdk-overlay-pane > div > mat-option:nth-of-type(' + k) + ')')
				
				selenium.click(('css=div.cdk-overlay-pane > div > mat-option:nth-of-type(' + k) + ')')
				
				selenium.click('css=custom-columns-panel')
				
				selenium.click('css=div.metric-selector:nth-of-type(4) > mat-select > div')
				
				Thread.sleep(500)
				
				selenium.click(('css=div.cdk-overlay-pane > div > mat-option:nth-of-type(' + m - 6) + ')')
				
				selenium.click('css=custom-columns-panel')
				
				selenium.click('css=div.custom-columns-creator > button')
				
				customColumnSummary = 0.00f
				
				if(i != j && WebUI.getText(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Third Column Summary Line')) != '-') {
					customColumnSummary = Float.parseFloat(WebUI.getText(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Third Column Summary Line')))				
				} else if (WebUI.getText(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Second Column Summary Line')) != '-') {
					customColumnSummary = Float.parseFloat(WebUI.getText(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Second Column Summary Line')))			
				}
				
				calculatedSummary = 0			
				summaryDiff = 0			
				threshold = 0			
				increment = 0
				canScroll = true
				
	//			while(increment > -1 && canScroll == true) {
	//				canScroll = selenium.isElementPresent(('css=div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + increment) + '\']')
	//
	//				if(canScroll == true) {
	//					if(increment % 25 == 0 && canScroll == true) {					
	//						currentLine = 0						
	//						selenium.runScript(('document.querySelector("div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + increment) 
	//							+ '\']").scrollIntoView()')					
	//					}
						
						firstValue = ""
						secondValue = ""
						
						if(i != j) {
	//						selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"" + increment + "\"] > div:nth-child(1)")
	//						selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"" + increment + "\"] > div:nth-child(2)")
	//						firstValue = selenium.getEval(('parseFloat(parseFloat(document.querySelector(\'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + 
	//						    increment) + '"] > div:nth-of-type(1)\').innerText).toFixed(2))')
							firstValue = Float.parseFloat(WebUI.getText(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/First Column Summary Line')))
							secondValue = Float.parseFloat(WebUI.getText(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Second Column Summary Line')))
						} else {					
	//						selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"" + increment + "\"] > div:nth-of-type(1)")
	//						firstValue = selenium.getEval(('parseFloat(parseFloat(document.querySelector(\'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + 
	//					    increment) + '"] > div:nth-of-type(1)\').innerText).toFixed(2))')
							firstValue = Float.parseFloat(WebUI.getText(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/First Column Summary Line')))
							secondValue = firstValue
						}
						
//						println firstValue
//						println secondValue
						
						calculatedSummary = Float.parseFloat(selenium.getEval('var mathItUp = { \'+\': function (x, y) { return x*1 + y*1},     \'-\': function (x, y) { return x*1 - y*1 },     \'/\': function (x, y) { if (y != 0) {return x*1 / y*1 } else { return 0}; },     \'%\': function (x, y) { if (y != 0) {return x*1 / y*1 * 100} else { return 0};},     \'*\': function (x, y) { return x*1 * y*1 } }; mathItUp[\''
							 + operator + '\'](' + firstValue + ', ' + secondValue + ')'))
//						println calculatedSummary
						increment++
	//				}
	//			}
				
				summaryDiff = customColumnSummary - calculatedSummary
				if (customColumnSummary == 0) {
					threshold = 0.02
				} else {
					threshold = Math.abs(customColumnSummary * 0.02 + 0.02)
				}
	
				println "Custom column Summary Line : " + customColumnSummary 
				println "Calculated Summary : " + calculatedSummary
				println threshold + " <= " + summaryDiff + " && " + summaryDiff + " <= " + threshold
				
				assertEquals(true, -1 * threshold <= summaryDiff && summaryDiff <= threshold)
				
				if(i != j) {
					selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-of-type(3)")
				} else {
					selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-of-type(2)")
				}
				
				Thread.sleep(500)
				
	//			selenium.runScript('document.querySelector(\'.ag-body-viewport\').scrollTo(0, 0);')
	//			
	//			Thread.sleep(1500)
				
				selenium.click('css=div.ag-side-button:nth-of-type(1) > button')
								
				numberOfSelectedColumns = selenium.getCssCount('div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')
				
				while(numberOfSelectedColumns > 0) {				
					selenium.click('css=div.ag-toolpanel-indent-0 > span.ag-column-select-checkbox:nth-of-type(2) > span.ag-checkbox-checked:not(.ag-hidden)')
					
					numberOfSelectedColumns--
				}
//				println customColumnName
//				println k
				
				customColumnName++
				k++
				j++
				i--
			} else if (j == 14) {
				j = 17
			} else {
				j++
			}
		}
		i++
}