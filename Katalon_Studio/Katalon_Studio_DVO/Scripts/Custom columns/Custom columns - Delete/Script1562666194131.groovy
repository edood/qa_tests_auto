import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*
import com.kms.katalon.core.testobject.SelectorMethod as SelectorMethod
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium as WebDriverBackedSelenium
import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')

def driver = DriverFactory.getWebDriver()

String baseUrl = 'https://www.katalon.com/'

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

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/1. Top Bar/Menu button'))

String url = ('https://' + GlobalVariable.env + tiret + GlobalVariable.env2 + ".tradelab.fr/") + GlobalVariable.advertiser

WebUI.navigateToUrl(url)

WebUI.waitForPageLoad(30)

WebUI.delay(2)

WebUI.waitForElementPresent(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'), 30)

WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/1. Menu/li_Line'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 30)

WebUI.click(findTestObject('DV360 Optimizer/3. Grid/Custom columns tab'))

WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab first element remove icon'), 30)

WebUI.delay(2)

Number numberOfCustomColumns = selenium.getCssCount(findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab remove icons').getSelectorCollection()
	.get(SelectorMethod.CSS))

String regex = 'nth-child\\((\\d+)\\)'

while (numberOfCustomColumns > 0) {
    TestObject removeIcon = findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab first element remove icon')

    removeIcon.setSelectorValue(SelectorMethod.CSS, removeIcon.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(
            regex, ('nth-child(' + numberOfCustomColumns) + ')'))

    TestObject customTabElement = findTestObject('DV360 Optimizer/3. Grid/3. Custom Columns tab/Custom column tab first element name')

    customTabElement.setSelectorValue(SelectorMethod.CSS, customTabElement.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(
            regex, ('nth-child(' + numberOfCustomColumns) + ')'))

    String customTabElementText = WebUI.getText(customTabElement)

    if (customTabElementText.matches('1[0-9][0-9][0-9]')) {
		WebUI.delay(1)
		
        WebUI.click(removeIcon)
		
		WebUI.waitForElementNotPresent(removeIcon, 30)
    }
    
    numberOfCustomColumns = (numberOfCustomColumns - 1)
}

