package testCases

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.Instant

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import reportfolio.Reportfolio

public class DownloadCumulativeIterations extends Reportfolio {

	@Keyword
	public void downloadCumulativeIterationOne() {
		selenium = getSelenium()
		String page = "folders"
		String templateFormat = "xlsx"
		String templateFullPath = csvAndTemplateFolderPath + "Template Insert a Table with custom params." + templateFormat
		changeSetupDates("P0D")
		selenium.open(baseUrl)
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Download Cumulative Iterations") == false) {
			createFolder("Test auto - Download Cumulative Iterations")
		}

		selenium.click("css=button[id=\"button-new-report\"]")
		WebUI.waitForElementVisible(selectorToTestObject("button[id=\"button-new-basic-report\"]"), 30)
		Thread.sleep(1000)
		selenium.click("css=button[id=\"button-new-basic-report\"]")
		Thread.sleep(1000)
		selenium.click("css=mat-dialog-content div.mat-form-field-flex > div > mat-select")
		Thread.sleep(500)
		selenium.click("css=div[class=\"cdk-overlay-pane\"] > div > mat-option:nth-child(1)")
		WebUI.waitForElementVisible(selectorToTestObject("div.mat-form-field-infix > input.mat-input-element"), 30)

		String randomNumber = Math.floor(10000 + Math.random() * 90000)
		WebUI.clearText(selectorToTestObject("div.mat-form-field-infix > input.mat-input-element"))
		WebUI.delay(1)
		String reportName = "Test auto - Report - Download Cumulative Iterations - One Iteration"
		WebUI.sendKeys(selectorToTestObject("div.mat-form-field-infix > input.mat-input-element"), reportName)
		selenium.click("css=app-new-document-dialog > form > mat-dialog-actions > button.mat-raised-button.mat-accent > span")
		Thread.sleep(1000)
		WebUI.waitForElementVisible(selectorToTestObject("span.no-source"), 30)

		Thread.sleep(1500)
		selenium.click("css=subreport > div > header > div > button > span > i")
		WebUI.waitForElementVisible(selectorToTestObject("div.mat-menu-content > button:nth-child(1)"), 30)
		Thread.sleep(1000)
		selenium.click("css=div.mat-menu-content > button:nth-child(1)")
		WebUI.waitForElementPresent(selectorToTestObject("subreport > div > header > tab-sheet:nth-child(1) > div > div.title > contenteditable > div > i"), 30)

		selenium.click("css=subreport > div > header > tab-sheet:nth-child(1) > div > div.title > contenteditable > div > i")
		WebUI.sendKeys(selectorToTestObject("subreport > div > header > tab-sheet:nth-child(1) > div > div.title > contenteditable > div > input"), "Download Cumulative Iterations - One Iteration")
		selenium.fireEvent("css=subreport > div > header > tab-sheet:nth-child(1) > div > div.title > contenteditable > div > input", "blur")
		selenium.click("css=document-setup > mat-card > mat-card-content > div.option.schedule > button")
		WebUI.waitForElementPresent(selectorToTestObject("sat-datepicker-toggle > button"), 30)
		selenium.click("css=sat-datepicker-toggle > button")
		WebUI.waitForElementVisible(selectorToTestObject(".mat-calendar-body-today"), 30)
		selenium.click("css=.mat-calendar-body-today")
		Thread.sleep(1000)
		selenium.click("css=.mat-calendar-body-today")
		WebUI.waitForElementVisible(selectorToTestObject("document-scheduler > div.mat-dialog-actions > button.mat-raised-button.mat-accent"), 30)
		selenium.click("css=document-scheduler > div.mat-dialog-actions > button.mat-raised-button.mat-accent")
		WebUI.waitForElementVisible(selectorToTestObject("mat-select[aria-label=\"No report yet\"] > div > div > span > span"), 30)

		WebUI.uploadFile(selectorToTestObject("document-setup > mat-card > mat-card-content > div:nth-child(3) > form > button > span > input"), templateFullPath)
		WebUI.waitForElementVisible(selectorToTestObject("document-setup > mat-card > mat-card-content > div:nth-child(3) > button:nth-of-type(2)"), 30)

		selenium.click("css=document-setup > mat-card > mat-card-content > div:nth-child(3) > button:nth-of-type(2)")
		WebUI.clearText(selectorToTestObject("section.display-sheets > table > tbody > tr:nth-child(1) > td.mat-cell.cdk-column-xls_sheet_index.mat-column-xls_sheet_index.ng-star-inserted > input"))
		WebUI.delay(1)
		String sheetNumber = "1"
		WebUI.sendKeys(selectorToTestObject("section.display-sheets > table > tbody > tr:nth-child(1) > td.mat-cell.cdk-column-xls_sheet_index.mat-column-xls_sheet_index.ng-star-inserted > input"), sheetNumber)
		selenium.fireEvent("css= td.cdk-column-xls_sheet_index.mat-column-xls_sheet_index > input", "change")
		selenium.click("css=app-document-settings > mat-dialog-actions > button.mat-raised-button.mat-accent")
		Thread.sleep(3000)
		selenium.click("css=app-document > section > subreport > div > sheet > mat-card > div > div > button > span > span")
		selenium.click("css=#cumulative")
		selenium.click("css=app-document > section > subreport > div > sheet > mat-card > csv-settings > div > div > form > div:nth-child(2) > button")
		Thread.sleep(3000)
		selenium.refresh()
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementVisible(selectorToTestObject("app-document > section > subreport > div > sheet > mat-card > div > div > button > span > span"), 30)
		selenium.click("css=app-document > section > subreport > div > sheet > mat-card > div > div > button > span > span")
		String currentTime = Instant.now().getEpochSecond()
		WebUI.uploadFile(selectorToTestObject("input#fileUpload"), apnCsvFullPath)
		WebUI.waitForElementVisible(selectorToTestObject("div.preview-info"), 30)
		String currentDate = generateCurrentDate()
		String downloadedReportPath = downloadReport(reportName, "", templateFormat, currentDate)
		checkReportDownloaded(downloadedReportPath, sheetNumber, apnCsvExpectedContent)
	}

	@Keyword
	public void downloadCumulativeIterationThree() {
		selenium = getSelenium()
		String page = "folders"
		String templateFormat = "xlsx"
		String templateFullPath = csvAndTemplateFolderPath + "Template Insert a Table with custom params." + templateFormat
		changeSetupDates("P0D")
		selenium.open(baseUrl)
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Download Cumulative Iterations") == false) {
			createFolder("Test auto - Download Cumulative Iterations")
		}

		selenium.click("css=button[id=\"button-new-report\"]")
		WebUI.waitForElementVisible(selectorToTestObject("button[id=\"button-new-basic-report\"]"), 30)
		Thread.sleep(1000)
		selenium.click("css=button[id=\"button-new-basic-report\"]")
		Thread.sleep(1000)
		selenium.click("css=mat-dialog-content div.mat-form-field-flex > div > mat-select")
		Thread.sleep(500)
		selenium.click("css=div[class=\"cdk-overlay-pane\"] > div > mat-option:nth-child(1)")
		WebUI.waitForElementVisible(selectorToTestObject("div.mat-form-field-infix > input.mat-input-element"), 30)

		WebUI.clearText(selectorToTestObject("div.mat-form-field-infix > input.mat-input-element"))
		WebUI.delay(1)
		String reportName = "Test auto - Report - Download Cumulative Iterations - Three Iterations"
		WebUI.sendKeys(selectorToTestObject("div.mat-form-field-infix > input.mat-input-element"), reportName)
		selenium.click("css=app-new-document-dialog > form > mat-dialog-actions > button.mat-raised-button.mat-accent > span")
		Thread.sleep(1000)
		WebUI.waitForElementVisible(selectorToTestObject("span.no-source"), 30)

		Thread.sleep(1500)
		selenium.click("css=subreport > div > header > div > button > span > i")
		WebUI.waitForElementVisible(selectorToTestObject("div.mat-menu-content > button:nth-child(1)"), 30)
		Thread.sleep(1000)
		selenium.click("css=div.mat-menu-content > button:nth-child(1)")
		WebUI.waitForElementPresent(selectorToTestObject("subreport > div > header > tab-sheet:nth-child(1) > div > div.title > contenteditable > div > i"), 30)

		selenium.click("css=subreport > div > header > tab-sheet:nth-child(1) > div > div.title > contenteditable > div > i")
		WebUI.sendKeys(selectorToTestObject("subreport > div > header > tab-sheet:nth-child(1) > div > div.title > contenteditable > div > input"), "Download Cumulative Iterations - Three Iterations")
		selenium.fireEvent("css=subreport > div > header > tab-sheet:nth-child(1) > div > div.title > contenteditable > div > input", "blur")
		selenium.click("css=document-setup > mat-card > mat-card-content > div.option.schedule > button")
		WebUI.waitForElementPresent(selectorToTestObject("sat-datepicker-toggle > button"), 30)
		selenium.click("css=sat-datepicker-toggle > button")
		WebUI.waitForElementVisible(selectorToTestObject(".mat-calendar-body-today"), 30)
		selenium.click("css=.mat-calendar-body-today")
		Thread.sleep(1000)
		selenium.click("css=.mat-calendar-body-today")
		WebUI.waitForElementVisible(selectorToTestObject("document-scheduler > div.mat-dialog-actions > button.mat-raised-button.mat-accent"), 30)
		selenium.click("css=document-scheduler > div.mat-dialog-actions > button.mat-raised-button.mat-accent")
		WebUI.waitForElementVisible(selectorToTestObject("mat-select[aria-label=\"No report yet\"] > div > div > span > span"), 30)

		WebUI.uploadFile(selectorToTestObject("document-setup > mat-card > mat-card-content > div:nth-child(3) > form > button > span > input"), templateFullPath)
		WebUI.waitForElementVisible(selectorToTestObject("document-setup > mat-card > mat-card-content > div:nth-child(3) > button:nth-of-type(2)"), 30)

		selenium.click("css=document-setup > mat-card > mat-card-content > div:nth-child(3) > button:nth-of-type(2)")
		WebUI.clearText(selectorToTestObject("section.display-sheets > table > tbody > tr:nth-child(1) > td.mat-cell.cdk-column-xls_sheet_index.mat-column-xls_sheet_index.ng-star-inserted > input"))
		WebUI.delay(1)
		String sheetNumber = "1"
		WebUI.sendKeys(selectorToTestObject("section.display-sheets > table > tbody > tr:nth-child(1) > td.mat-cell.cdk-column-xls_sheet_index.mat-column-xls_sheet_index.ng-star-inserted > input"), sheetNumber)
		selenium.fireEvent("css= td.cdk-column-xls_sheet_index.mat-column-xls_sheet_index > input", "change")
		selenium.click("css=app-document-settings > mat-dialog-actions > button.mat-raised-button.mat-accent")
		Thread.sleep(3000)
		selenium.click("css=app-document > section > subreport > div > sheet > mat-card > div > div > button > span > span")
		selenium.click("css=#cumulative")
		selenium.click("css=app-document > section > subreport > div > sheet > mat-card > csv-settings > div > div > form > div:nth-child(2) > button")
		Thread.sleep(1500)
		selenium.refresh()
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementVisible(selectorToTestObject("app-document > section > subreport > div > sheet > mat-card > div > div > button > span > span"), 30)
		selenium.click("css=app-document > section > subreport > div > sheet > mat-card > div > div > button > span > span")
		WebUI.uploadFile(selectorToTestObject("input#fileUpload"), apnCsvFullPath)
		WebUI.waitForElementVisible(selectorToTestObject("div.preview-info"), 30)

		Thread.sleep(750)
		String currentReportUrl = WebUI.getUrl()
		changeSetupDates("P1D")
		selenium.open(currentReportUrl)
		WebUI.waitForElementVisible(selectorToTestObject("div.option.schedule > label > i"), 30)

		selenium.click("css=div.option.schedule > label > i")
		WebUI.waitForElementNotPresent(selectorToTestObject("div.option.schedule > button:nth-of-type(2)"), 30)

		selenium.click("css=document-setup > mat-card > mat-card-content > div.option.schedule > button")
		WebUI.waitForElementPresent(selectorToTestObject("sat-datepicker-toggle > button"), 30)
		selenium.click("css=sat-datepicker-toggle > button")
		Date date = new Date()
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date)
		Date dateTomorrow = new Date()
		Calendar c = Calendar.getInstance()
		c.setTime(dateTomorrow)
		c.add(Calendar.DATE, 1)
		String tomorrow = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + 1)
		boolean isMonthChange = calendar.get(Calendar.DAY_OF_MONTH) + 1 != c.get(Calendar.DAY_OF_MONTH)
		String tomorrowNumeric = calendar.get(Calendar.YEAR).toString() + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH) + 1)
		if(isMonthChange == true) {
			WebUI.waitForElementPresent(selectorToTestObject("button.mat-calendar-next-button"), 30)
			selenium.click("css=button.mat-calendar-next-button")
			tomorrow = c.get(Calendar.DAY_OF_MONTH)
			tomorrowNumeric = calendar.get(Calendar.YEAR).toString() + "-" + String.format("%02d", c.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", c.get(Calendar.DAY_OF_MONTH))
		}
		WebUI.waitForElementVisible(selectorToTestObject("[aria-label*=\"" + tomorrow + ",\"]"), 30)
		selenium.click("css=[aria-label*=\"" + tomorrow + ",\"]")
		WebUI.waitForElementVisible(selectorToTestObject("[aria-label*=\"" + tomorrow + ",\"]"), 30)
		selenium.click("css=[aria-label*=\"" + tomorrow + ",\"]")
		WebUI.waitForElementVisible(selectorToTestObject("document-scheduler > div.mat-dialog-actions > button.mat-raised-button.mat-accent"), 30)
		selenium.click("css=document-scheduler > div.mat-dialog-actions > button.mat-raised-button.mat-accent")
		WebUI.waitForElementVisible(xpathSelectorToTestObject("//*[text()='Report " + tomorrowNumeric + "']"), 30)

		WebUI.uploadFile(selectorToTestObject("input#fileUpload"), apnCsvTwoFullPath)
		WebUI.waitForElementVisible(selectorToTestObject("div.preview-info"), 30)

		Thread.sleep(750)
		changeSetupDates("P2D")
		selenium.open(currentReportUrl)
		WebUI.waitForElementVisible(selectorToTestObject("div.option.schedule > label > i"), 30)

		selenium.click("css=div.option.schedule > label > i")
		WebUI.waitForElementNotPresent(selectorToTestObject("div.option.schedule > button:nth-of-type(2)"), 30)

		selenium.click("css=document-setup > mat-card > mat-card-content > div.option.schedule > button")
		WebUI.waitForElementPresent(selectorToTestObject("sat-datepicker-toggle > button"), 30)
		selenium.click("css=sat-datepicker-toggle > button")
		c.add(Calendar.DATE, 1)
		String dayAfterTomorrow = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + 2)
		String dayAfterTomorrowNumeric = calendar.get(Calendar.YEAR).toString() + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH) + 2)
		isMonthChange = calendar.get(Calendar.DAY_OF_MONTH) + 2 != c.get(Calendar.DAY_OF_MONTH)
		if(isMonthChange == true) {
			WebUI.waitForElementPresent(selectorToTestObject("button.mat-calendar-next-button"), 30)
			selenium.click("css=button.mat-calendar-next-button")
			dayAfterTomorrow = c.get(Calendar.DAY_OF_MONTH)
			dayAfterTomorrowNumeric = calendar.get(Calendar.YEAR).toString() + "-" + String.format("%02d", c.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", c.get(Calendar.DAY_OF_MONTH))
		}
		WebUI.waitForElementVisible(selectorToTestObject("[aria-label*=\"" + dayAfterTomorrow + ",\"]"), 30)
		selenium.click("css=[aria-label*=\"" + dayAfterTomorrow + ",\"]")
		WebUI.waitForElementVisible(selectorToTestObject("[aria-label*=\"" + dayAfterTomorrow + ",\"]"), 30)
		selenium.click("css=[aria-label*=\"" + dayAfterTomorrow + ",\"]")
		selenium.click("css=document-scheduler > div.mat-dialog-actions > button.mat-raised-button.mat-accent")
		WebUI.waitForElementVisible(xpathSelectorToTestObject("//*[text()='Report " + dayAfterTomorrowNumeric + "']"), 30)
		String currentTime = Instant.now().getEpochSecond()
		WebUI.uploadFile(selectorToTestObject("input#fileUpload"), apnCsvThreeFullPath)
		WebUI.waitForElementVisible(selectorToTestObject("div.preview-info"), 30)

		changeSetupDates("P0D")
		selenium.open(currentReportUrl)
		date = new Date()
		calendar = GregorianCalendar.getInstance()
		calendar.setTime(date)
		String currentDate = calendar.get(Calendar.YEAR).toString() + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH) + 2)

		String downloadedReportPath = downloadReport(reportName, "", templateFormat, currentDate)
		String sheetContent = apnCsvExpectedContent + "\n25	First line of second CSV	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	9725057.0	PROSPECTION_ALI_CPA_MOBILE	450250.0	64.0	204.877435	 	 \n26	43774.0	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	6694498.0	PROSPECTION_PUBLISHER_MOBILE	230550.0	40.0	116.174718	 	 \n27	43774.0	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	8016072.0	PROSPECTION_MOBILE_MMO	172410.0	42.0	79.121204	 	 \n28	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	2791000.0	PROSPECTION_AUTOMATIONVISITE_DESKTOP	133539.0	17.0	126.471034	 	 \n29	43774.0	845763.0	Boursorama	3113824.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Novembre19	10044300.0	PROSPECTION_BIDDER_DESKTOP	83853.0	26.0	80.558281	 	 \n30	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	3936328.0	PROSPECTION_APB_DESKTOP	82437.0	42.0	87.334454	 	 \n31	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	7662574.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP	61555.0	3.0	60.85293	 	 \n32	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	8023288.0	PROSPECTION_AUTOMATION-LEADv2_DESKTOP	57821.0	10.0	61.283288	 	 \n33	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	9650618.0	PROSPECTION_APB_DESKTOP_NEW	29638.0	12.0	49.251197	 	 \n34	43774.0	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	9106179.0	PROFILING_MOBILE	26957.0	7.0	43.264773	 	 \n35	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	8724447.0	PROFILING_ULTIM_DESKTOP	8436.0	2.0	17.516538	 	 \n36	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	6694498.0	PROSPECTION_PUBLISHER_MOBILE	0.0	0.0	0.0	 	 \n37	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	8016072.0	PROSPECTION_MOBILE_MMO	0.0	0.0	0.0	 	 \n38	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9106179.0	PROFILING_MOBILE	0.0	0.0	0.0	 	 \n39	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9725057.0	PROSPECTION_ALI_CPA_MOBILE	0.0	0.0	0.0	 	 \n40	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9760094.0	PROSPECTION_NATIVE	0.0	0.0	0.0	 	 \n41	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	2791000.0	PROSPECTION_AUTOMATIONVISITE_DESKTOP	0.0	0.0	0.0	 	 \n42	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	3936328.0	PROSPECTION_APB_DESKTOP	0.0	0.0	0.0	 	 \n43	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	7662574.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP	0.0	0.0	0.0	 	 \n44	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	8023288.0	PROSPECTION_AUTOMATION-LEADv2_DESKTOP	0.0	0.0	0.0	 	 \n45	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	8724447.0	PROFILING_ULTIM_DESKTOP	0.0	0.0	0.0	 	 \n46	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9650618.0	PROSPECTION_APB_DESKTOP_NEW	0.0	0.0	0.0	 	 \n47	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9760155.0	PROSPECTION_NATIVE_DESKTOP	0.0	0.0	0.0	 	 \n48	Last line of second CSV	845763.0	Boursorama	2842170.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Octobre19	4612927.0	PROSPECTION_BIDDER_DESKTOP	0.0	0.0	0.0	 	 \n49	First line of third CSV	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	9725057.0	PROSPECTION_ALI_CPA_MOBILE	450250.0	64.0	204.877435	 	 \n50	43774.0	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	6694498.0	PROSPECTION_PUBLISHER_MOBILE	230550.0	40.0	116.174718	 	 \n51	43774.0	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	8016072.0	PROSPECTION_MOBILE_MMO	172410.0	42.0	79.121204	 	 \n52	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	2791000.0	PROSPECTION_AUTOMATIONVISITE_DESKTOP	133539.0	17.0	126.471034	 	 \n53	43774.0	845763.0	Boursorama	3113824.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Novembre19	10044300.0	PROSPECTION_BIDDER_DESKTOP	83853.0	26.0	80.558281	 	 \n54	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	3936328.0	PROSPECTION_APB_DESKTOP	82437.0	42.0	87.334454	 	 \n55	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	7662574.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP	61555.0	3.0	60.85293	 	 \n56	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	8023288.0	PROSPECTION_AUTOMATION-LEADv2_DESKTOP	57821.0	10.0	61.283288	 	 \n57	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	9650618.0	PROSPECTION_APB_DESKTOP_NEW	29638.0	12.0	49.251197	 	 \n58	43774.0	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	9106179.0	PROFILING_MOBILE	26957.0	7.0	43.264773	 	 \n59	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	8724447.0	PROFILING_ULTIM_DESKTOP	8436.0	2.0	17.516538	 	 \n60	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	6694498.0	PROSPECTION_PUBLISHER_MOBILE	0.0	0.0	0.0	 	 \n61	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	8016072.0	PROSPECTION_MOBILE_MMO	0.0	0.0	0.0	 	 \n62	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9106179.0	PROFILING_MOBILE	0.0	0.0	0.0	 	 \n63	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9725057.0	PROSPECTION_ALI_CPA_MOBILE	0.0	0.0	0.0	 	 \n64	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9760094.0	PROSPECTION_NATIVE	0.0	0.0	0.0	 	 \n65	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	2791000.0	PROSPECTION_AUTOMATIONVISITE_DESKTOP	0.0	0.0	0.0	 	 \n66	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	3936328.0	PROSPECTION_APB_DESKTOP	0.0	0.0	0.0	 	 \n67	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	7662574.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP	0.0	0.0	0.0	 	 \n68	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	8023288.0	PROSPECTION_AUTOMATION-LEADv2_DESKTOP	0.0	0.0	0.0	 	 \n69	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	8724447.0	PROFILING_ULTIM_DESKTOP	0.0	0.0	0.0	 	 \n70	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9650618.0	PROSPECTION_APB_DESKTOP_NEW	0.0	0.0	0.0	 	 \n71	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9760155.0	PROSPECTION_NATIVE_DESKTOP	0.0	0.0	0.0	 	 \n72	Last line of third CSV	845763.0	Boursorama	2842170.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Octobre19	4612927.0	PROSPECTION_BIDDER_DESKTOP	0.0	0.0	0.0	 	 "
		checkReportDownloaded(downloadedReportPath, sheetNumber, sheetContent)
	}
}
