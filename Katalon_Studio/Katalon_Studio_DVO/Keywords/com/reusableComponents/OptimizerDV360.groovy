package com.reusableComponents
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium as WebDriverBackedSelenium

import editTargeting.targetObject
import internal.GlobalVariable
import creative.Creative

class Exchange {
	String name
	String line
	boolean updated
	boolean checked
	int actual_included_subexchange
	int total_included_subexchange

	public Exchange() {
		name = ''
		line = ''
		checked = true
		updated = false
		actual_included_subexchange = 0;
		total_included_subexchange = 0;
	}

	public Exchange(Exchange other) {
		name = other.name
		line = other.line
		checked = other.checked
		updated = other.updated
		actual_included_subexchange = other.actual_included_subexchange
		total_included_subexchange = other.total_included_subexchange
	}
}

class Line {
	public String name
	public Exchange[] parent = []
	int[] count_exchange
	String InventorySourceSummary
	public Creative[] creatives
	public creative_count_bef
	public creative_count_aft

	public Line() {
		name = ''
		count_exchange = [0, 0]
		InventorySourceSummary = ""
		creatives = []
		creative_count_aft = 0
		creative_count_bef = 0
	}

	public Line(String str) {
		name = str
		count_exchange = [0, 0]
		InventorySourceSummary = ""
		creatives = []
		creative_count_aft = 0
		creative_count_bef = 0
	}
}

class OptimizerDV360 {
	public WebDriver driver;
	public String baseUrl;
	public WebDriverBackedSelenium selenium;
	public TestObject tmp;
	public Line[] line = []

	public OptimizerDV360() {
		tmp = new TestObject("tmp")
		tmp.addProperty('css', ConditionType.EQUALS, '')
		tmp.addProperty('xpath', ConditionType.EQUALS, '')
	}

	@Keyword
	def clickObjectbyCss(String cssSelector) {
		WebUI.delay(1)
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), cssSelector, true)
		WebUI.delay(1)
		WebUI.click(tmp)
	}

	@Keyword
	def waitforCssSelectorVisible(String CssSelector, int timeout) {
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), CssSelector, true)
		assert WebUI.waitForElementVisible(tmp, timeout) == true
		WebUI.delay(1)
	}

	public TestObject selectorToTestObject(String selector) {
		WebUI.removeObjectProperty(tmp, 'xpath')
		WebUI.removeObjectProperty(tmp, 'css')
		tmp.addProperty('css', ConditionType.EQUALS, selector, true)
		return tmp
	}

	public TestObject xpathSelectorToTestObject(String selector) {
		WebUI.removeObjectProperty(tmp, 'css')
		WebUI.removeObjectProperty(tmp, 'xpath')
		tmp.addProperty('xpath', ConditionType.EQUALS, selector, true)
		return tmp
	}

	public static TestObject changeSelector(TestObject testObject, String regex, String replacement) {
		testObject.setSelectorValue(SelectorMethod.CSS, testObject.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, replacement))
		return testObject
	}

	public static String getSelector(TestObject testObject) {
		return "css=" + testObject.getSelectorCollection().get(SelectorMethod.CSS)
	}

	public WebDriverBackedSelenium getSelenium() {
		driver = DriverFactory.getWebDriver()
		baseUrl = 'https://' + GlobalVariable.env + '-' + GlobalVariable.env2 + '.tradelab.fr' + '/'
		selenium = new WebDriverBackedSelenium(driver, baseUrl)

		return selenium
	}

	@Keyword
	def start(String strurl) {
		String tiret = '-'
		String url = ('https://' + GlobalVariable.env) + tiret + GlobalVariable.env2 + '.tradelab.fr/'

		if (strurl == null)
			strurl = url

		WebUI.openBrowser(strurl)
		driver = DriverFactory.getWebDriver()
		selenium = new WebDriverBackedSelenium(driver, strurl)
		WebUI.maximizeWindow()


		if (((GlobalVariable.env != 'dev') && (GlobalVariable.env != 'staging')) && (GlobalVariable.env != 'prod')) {
			GlobalVariable.env2 = 'saas'
		} else if (GlobalVariable.env == 'prod') {
			tiret = ''
			GlobalVariable.env = ''
		}

		String currentWebsite = GlobalVariable.env2 + '.tradelab.fr'

		String page = 'login'

		String optionsString = (('path=/, domain=' + GlobalVariable.env) + '-') + currentWebsite

		selenium.open('https://' + GlobalVariable.env + tiret + currentWebsite + '/' + page)

		Thread.sleep(2000)

		selenium.waitForPageToLoad('30')

		waitforCssSelectorVisible('button.validation-login', 60)

		WebUI.setText(findTestObject('Object Repository/1. Login/Login'), GlobalVariable.username)

		WebUI.setText(findTestObject('Object Repository/1. Login/Password'), GlobalVariable.password)

		clickObjectbyCss('app-login > div > section > form > button')

		Thread.sleep(2000)
		selenium.waitForPageToLoad('30')
		selenium.open('https://' + GlobalVariable.env + tiret + currentWebsite + '/' + GlobalVariable.advertiser)

		if (WebUI.verifyElementPresent(findTestObject('DV360 Optimizer/Toast message'), 30) == true) {
			assert WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30) == true
		}

		waitforCssSelectorVisible('app-layout > mat-toolbar > button:nth-child(1) > span > i', 60)

		if (GlobalVariable.template == 'Line') {
			waitforCssSelectorVisible('mat-sidenav > div > nav > ul > li:nth-child(1)', 60)
			clickObjectbyCss('mat-sidenav > div > nav > ul > li:nth-child(1)')
		}
		else if (GlobalVariable.template == 'Creative') {
			waitforCssSelectorVisible('mat-sidenav > div > nav > ul > li:nth-child(2)', 60)
			clickObjectbyCss('mat-sidenav > div > nav > ul > li:nth-child(2)')
		}
		else if (GlobalVariable.template == 'URL App') {
			waitforCssSelectorVisible('mat-sidenav > div > nav > ul > li:nth-child(3)', 60)
			clickObjectbyCss('mat-sidenav > div > nav > ul > li:nth-child(3)')
		}
		else if (GlobalVariable.template == 'Exchange') {
			waitforCssSelectorVisible('mat-sidenav > div > nav > ul > li:nth-child(4)', 60)
			clickObjectbyCss('mat-sidenav > div > nav > ul > li:nth-child(4)')
		}
		else if (GlobalVariable.template == 'Inventory Source') {
			waitforCssSelectorVisible('mat-sidenav > div > nav > ul > li:nth-child(5)', 60)
			clickObjectbyCss('mat-sidenav > div > nav > ul > li:nth-child(5)')
		}
		KeywordUtil.markPassed("Optimizer started successfully")
	}

	@Keyword
	def selectDateRange(String dateRange) {
		WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Daterange selector'))
		WebUI.delay(1)
		if (dateRange == '5') {
			clickObjectbyCss('mat-option[value="custom"]')
			WebUI.delay(2)

			WebUI.click(findTestObject('Object Repository/DV360 Optimizer/1. Top Bar/Date Range period button'))
			clickObjectbyCss('td[aria-label="' + GlobalVariable.startYear + '"]')
			clickObjectbyCss('td[aria-label="' + GlobalVariable.startMonth + " " + GlobalVariable.startYear + '"]')
			clickObjectbyCss('td[aria-label*="' + GlobalVariable.startMonth + " " + GlobalVariable.startDay + ', ' + Calendar.getInstance().get(Calendar.YEAR) + '"]')
			clickObjectbyCss('td[aria-label*="' + GlobalVariable.startMonth + " " + GlobalVariable.endDay + ', ' + Calendar.getInstance().get(Calendar.YEAR) + '"]')
			clickObjectbyCss('div.cdk-overlay-backdrop.cdk-overlay-transparent-backdrop.cdk-overlay-backdrop-showing')
		}
		else
			clickObjectbyCss('body > div.cdk-overlay-container > div.cdk-overlay-connected-position-bounding-box > div mat-option:nth-child(' + dateRange + ')')
		WebUI.delay(2)
		WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Daterange-Interval-Validator'))
	}

	@Keyword
	def selectAdvertiser(String id) {
		selenium.open('https://' + GlobalVariable.env + '-' + GlobalVariable.env2 + '.tradelab.fr' + '/' + id)
		WebUI.delay(2)
	}

	@Keyword
	def logout() {
		clickObjectbyCss('mat-toolbar > button:nth-child(4)')
		waitforCssSelectorVisible('div[class="cdk-overlay-pane"] > div > div > button', 30)
		clickObjectbyCss('div[class="cdk-overlay-pane"] > div > div:nth-child(1) > button')
		WebUI.delay(2)
	}

	@Keyword
	def login(String username, String password) {
		waitforCssSelectorVisible('button.validation-login', 30)
		WebUI.setText(findTestObject('Object Repository/1. Login/Login'), username)
		WebUI.setText(findTestObject('Object Repository/1. Login/Password'), password)
		clickObjectbyCss('app-login > div > section > form > button')
		WebUI.delay(2)
	}

	@Keyword
	int templateToInt(String str) {
		if (str == null) {
			if (GlobalVariable.template == 'Line') {
				return (1)
			}
			else if (GlobalVariable.template == 'Creative') {
				return (2)
			}
			else if (GlobalVariable.template == 'URL App') {
				return (3)
			}
			else if (GlobalVariable.template == 'Exchange') {
				return (4)
			}
			else if (GlobalVariable.template == 'Inventory Source') {
				return (5)
			}
		}
		else if (str == 'Line') {
			return (1)
		}
		else if (str == 'Creative') {
			return (2)
		}
		else if (str == 'URL App') {
			return (3)
		}
		else if (str == 'Exchange') {
			return (4)
		}
		else if (str == 'Inventory Source') {
			return (5)
		}
		return (0)
	}

	@Keyword
	def refreshAdvertiserFromHomepage() {
		selenium = getSelenium()

		String 	tiret = '-'

		if (((GlobalVariable.env != 'dev') && (GlobalVariable.env != 'staging')) && (GlobalVariable.env != 'prod')) {
			GlobalVariable.env2 = 'saas'
		} else if (GlobalVariable.env == 'prod') {
			tiret = ''
			GlobalVariable.env = ''
		}

		String currentWebsite = GlobalVariable.env2 + '.tradelab.fr'

		WebUI.navigateToUrl('https://' + GlobalVariable.env + tiret + currentWebsite + '/' + GlobalVariable.advertiser)
		WebUI.waitForPageLoad(30)
		assert WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/1. Top Bar/Synchro button'), 30) == true
		WebUI.delay(5)
		WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Synchro button'))
		assert WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Toast message'), 240) == true
		assert WebUI.verifyElementText(findTestObject('DV360 Optimizer/Toast message'), 'Data synchronization successfull. The page will be automatically refreshed in 5 seconds') == true
		WebUI.delay(10)

		KeywordUtil.markPassed("Refresh successful")
	}

	@Keyword
	def refreshAdvertiser() {
		selenium = getSelenium()
		if (selenium.isElementPresent(getSelector(findTestObject('DV360 Optimizer/Toast message'))) == true) {
			assert WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30) == true
			WebUI.delay(1)
		}

		WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Synchro button'))
		assert WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Lock advertiser message'), 30) == true
		assert WebUI.verifyElementText(findTestObject('DV360 Optimizer/Lock advertiser message'), 'Advertiser\'s data is currently being synchronized\nYou will be able to edit data for this advertiser once data synchronization will be completed.') == true
		assert WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/Toast message'), 240) == true
		assert WebUI.verifyElementText(findTestObject('DV360 Optimizer/Toast message'), 'Data synchronization successfull. The page will be automatically refreshed in 5 seconds') == true
		KeywordUtil.markPassed("Refresh successful")
	}

	@Keyword
	def gotoTemplate(int number) {
		WebUI.delay(1)
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'mat-toolbar > button:nth-child(1) > span > i', true)
		WebUI.click(tmp)
		WebUI.delay(1)
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), "mat-sidenav > div > nav > ul > li:nth-child("+ number+ ")", true)
		WebUI.click(tmp)
		KeywordUtil.markPassed("Access to template successfully")
	}

	@Keyword
	def selectTemplate(int number) {
		WebUI.delay(1)
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), "mat-sidenav > div > nav > ul > li:nth-child("+ number+ ")", true)
		WebUI.click(tmp)
		WebUI.delay(2)
		KeywordUtil.markPassed("template selected successfully")
	}

	@Keyword
	def addGroupbyTemplateDimension(String template) {
		switch (template) {
			case "Line":
				clickObjectbyCss('div[col-id="oiName"] > div.ag-cell-label-container > span')
				break;

			case "Creative":
				clickObjectbyCss('div[col-id="creativeName"]> div.ag-cell-label-container > span')
				break;

			case "URL App":
				clickObjectbyCss('div[col-id="urlAppName"] > div.ag-cell-label-container > span')
				break;

			case "Exchange":
				clickObjectbyCss('div[col-id="exchangeName"] > div.ag-cell-label-container > span')
				break;

			case "Inventory Source":
				clickObjectbyCss('div[col-id="inventorySourceName"] > div.ag-cell-label-container > span')
				break;
		}

		waitforCssSelectorVisible('div.ag-tab-body > div > div:nth-child(6)', 30)
		clickObjectbyCss('div.ag-tab-body > div > div:nth-child(6)')
		WebUI.delay(2)
		KeywordUtil.markPassed("Group added successfully")
	}

	@Keyword
	def hideColumns(int col) {
		clickObjectbyCss('div.ag-side-bar > div.ag-side-buttons > div:nth-child(1) > button')
		WebUI.delay(1)
		clickObjectbyCss('div:nth-child(' + col + ') > span[class="ag-column-select-checkbox"]')
		WebUI.delay(1)
		clickObjectbyCss('div.ag-side-bar > div.ag-side-buttons > div:nth-child(1) > button')
		WebUI.delay(1)
	}

	@Keyword
	def resizeAll() {
		clickObjectbyCss('div:nth-child(2) > div:nth-child(2) > div.ag-cell-label-container > span > span')
		waitforCssSelectorVisible('div.ag-tab-body > div > div:nth-of-type(4) > span:nth-child(2)', 30)
		clickObjectbyCss('div.ag-tab-body > div > div:nth-of-type(4) > span:nth-child(2)')
		WebUI.delay(1)
	}

	@Keyword
	def changeOrderColumns(int col, int offsetX, int offsetY) {
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div.ag-header-viewport > div > div:nth-child(2) > div:nth-child(' + col + ')', true)
		WebUI.dragAndDropByOffset(tmp, offsetX, offsetY)
	}

	@Keyword // améliorer la fonction pour la rendre générique avec à n'importe quelle nom de colonne
	def sortColumn(String columname)
	{
		WebUI.delay(1)
		clickObjectbyCss('div.ag-header-viewport > div > div:nth-child(2) > div:nth-child(2) > div.ag-cell-label-container > div')
		WebUI.delay(1)
	}

	@Keyword
	String download()
	{
		String advertiserName = WebUI.getAttribute(findTestObject('10. DV360/Advertiser Name'), 'value')
		String sdfName = advertiserName + "-sdf.zip"
		KeywordUtil.logInfo('SDF name = ' + sdfName)
		String sdfPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + sdfName
		File sdfFile = new File(sdfPath)

		// Delete SDF if one already exists with the same name
		if(sdfFile.exists() == true)
			deleteFile(sdfPath)

		WebUI.delay(3)
		WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Apply optimization button'))
		WebUI.delay(3)

		int attempts = 30
		while(sdfFile.exists() == false && attempts > 0) {
			WebUI.delay(1)
			attempts--
		}
		assert sdfFile.exists() == true
		return (sdfName)
	}

	@Keyword
	String unzipCSVfile(String filename) {
		WebUI.navigateToUrl('http://www.ezyzip.com/decompresser-des-fichiers-en-ligne.html')
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/11. Misc/ezyzip.com/Extract File upload input'), 30) == true
		String sdfPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + filename
		WebUI.uploadFile(findTestObject('Object Repository/11. Misc/ezyzip.com/Extract File upload input'), sdfPath)
		WebUI.click(findTestObject('Object Repository/11. Misc/ezyzip.com/Extract button'))
		assert WebUI.waitForElementVisible(findTestObject('11. Misc/ezyzip.com/First save file button'), 30) == true
		String extractedLiFileName = WebUI.getText(findTestObject('11. Misc/ezyzip.com/Extracted line item file name')).replaceFirst("\\.csv(\n?.*)+", ".csv")
		String downloadedLiCsvPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + extractedLiFileName

		// Download unzipped files
		WebUI.click(findTestObject('Object Repository/11. Misc/ezyzip.com/First save file button'))
		WebUI.delay(1)

		return downloadedLiCsvPath
	}

	String unzipCSVfileAndCheck(String filename, LinkedList<targetObject> change) {

		for (def stylo in change) {
			println(stylo.ID + " ; " + stylo.name + " ; " + stylo.target + " ; " + stylo.type + " ; " + stylo.line)
		}

		String downloadedLiCsvPath = unzipCSVfile(filename)


		WebUI.navigateToUrl('http://www.convertcsv.com/csv-to-html.htm')
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/11. Misc/convertcsv.com/Choose file input'), 30) == true
		WebUI.scrollToElement(findTestObject('Object Repository/11. Misc/convertcsv.com/Step 1 title'), 2)
		WebUI.click(findTestObject('Object Repository/11. Misc/convertcsv.com/Choose file button'))

		//Check line results
		WebUI.scrollToElement(findTestObject('Object Repository/11. Misc/convertcsv.com/Step 1 title'), 2)
		WebUI.delay(1)
		WebUI.uploadFile(findTestObject('Object Repository/11. Misc/convertcsv.com/Choose file input'), downloadedLiCsvPath)
		WebUI.delay(2)
		WebUI.scrollToElement(findTestObject('Object Repository/11. Misc/convertcsv.com/Result table'), 2)
		WebUI.click(findTestObject('11. Misc/convertcsv.com/Results/Line item/LI Name'))
		WebUI.delay(1)

		return (downloadedLiCsvPath)
	}

	@Keyword
	public void deleteFile(String fileFullPath) {
		File file = new File(fileFullPath)
		assert file.delete() == true
		KeywordUtil.markPassed("File deleted : " + file.name)
	}

	@Keyword
	def ConnectToDV360(String id, String password)
	{
		WebUI.navigateToUrl('https://displayvideo.google.com/#ng_nav/overview')
		WebUI.waitForPageLoad(30)
		WebUI.delay(2)
		WebUI.setText(findTestObject('1. Login/Login type email'), id)
		WebUI.sendKeys(findTestObject('1. Login/Login type email'), '\n')
		WebUI.delay(3)
		WebUI.setEncryptedText(findTestObject('1. Login/Password type password'), password)
		WebUI.sendKeys(findTestObject('1. Login/Password type password'), '\n')
		WebUI.delay(10)
		KeywordUtil.markPassed("Logged in to Google")
	}

	@Keyword
	boolean DV360Upload(String filecsv, String line)
	{
		selenium = getSelenium()
		WebUI.click(findTestObject('10. DV360/DV360 search button'))
		WebUI.setText(findTestObject('10. DV360/input_Search dv360'), line)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Search suggestion'), 30) == true
		WebUI.click(findTestObject('10. DV360/Search suggestion'))
		WebUI.delay(15)
		WebUI.click(findTestObject('10. DV360/OI link'))
		WebUI.delay(5)
		if (selenium.isElementPresent(getSelector(findTestObject('10. DV360/Popup maybe'))) == true)
			WebUI.click(findTestObject('10. DV360/Popup maybe'))
		WebUI.delay(1)
		WebUI.click(findTestObject('10. DV360/OI option'))
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/OI button Upload'), 30) == true
		WebUI.click(findTestObject('10. DV360/OI button Upload'))
		WebUI.delay(2)
		assert WebUI.waitForElementPresent(findTestObject('10. DV360/Upload Input'), 10) == true
		println(filecsv)
		WebUI.uploadFile(findTestObject('10. DV360/Upload Input'), filecsv)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Uploaded file'), 30) == true
		assert WebUI.verifyElementVisible(findTestObject('10. DV360/Uploaded file')) == true
		WebUI.click(findTestObject('10. DV360/Upload Button'))

		KeywordUtil.markPassed("Uploading SDF")
		int maxWait = 120

		while (maxWait > 0 && selenium.isElementPresent(getSelector(findTestObject('10. DV360/Success Upload'))) == false) {
			WebUI.delay(1)
			maxWait--
		}

		if (selenium.isElementPresent(getSelector(findTestObject('10. DV360/Success Upload'))) == false)
		{
			WebUI.click(findTestObject('10. DV360/Upload expand error'))
			String str = WebUI.getText(findTestObject('10. DV360/Upload error')) + WebUI.getText(findTestObject('10. DV360/Upload error main'))
			KeywordUtil.markErrorAndStop(str)
		}

		assert WebUI.verifyElementVisible(findTestObject('10. DV360/Success Upload'))
		WebUI.refresh()
		WebUI.delay(5)

		KeywordUtil.markPassed("SDF uploaded")
		return (true)
	}

	// Retourne un string commençant par une majuscule et suivi de minuscules
	protected static String toUpperLowerCase(String string) {
		return string.charAt(0).toUpperCase().toString() + string.substring(1).toLowerCase()
	}
}