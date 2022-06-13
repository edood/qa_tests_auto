package testCases

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.Duration
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable
import reportfolio.Reportfolio

public class DownloadXLS extends Reportfolio {

	@Keyword
	public ArrayList<String> downloadXlsApn(String templateFormat, String scheduleFrequency) {
		String reportName = "Test auto - Download XLS - APN " + scheduleFrequency.substring(0, 1).toUpperCase() + scheduleFrequency.substring(1)
		String advertiserName = "Test Advertiser"
		String subreportEmail = createAndSetupReport(reportName, advertiserName, templateFormat, "8", scheduleFrequency, "APN Subreport", 1)
		String reportUrl = WebUI.getUrl()
		createReportApn(subreportEmail)

		ArrayList<String> report = [reportUrl, reportName, advertiserName]
		return report
	}

	@Keyword
	public ArrayList<String> downloadXlsDV360(String templateFormat, String scheduleFrequency) {
		String reportName = "Test auto - Download XLS - DV360 " + scheduleFrequency.substring(0, 1).toUpperCase() + scheduleFrequency.substring(1)
		String advertiserName = "Test Advertiser"
		String subreportEmail = createAndSetupReport(reportName, advertiserName, templateFormat, "9", scheduleFrequency, "DV360 Subreport", 5)
		setupCsvSettings("1", "13", ",", false)
		String reportUrl = WebUI.getUrl()
		createReportDV360(subreportEmail)

		ArrayList<String> report = [reportUrl, reportName, advertiserName]
		return report
	}

	@Keyword
	public ArrayList<String> downloadXlsCM(String templateFormat, String scheduleFrequency) {
		String reportName = "Test auto - Download XLS - CM " + scheduleFrequency.substring(0, 1).toUpperCase() + scheduleFrequency.substring(1)
		String advertiserName = "Test Advertiser"
		String subreportEmail = createAndSetupReport(reportName, advertiserName, templateFormat, "10", scheduleFrequency, "CM Subreport", 4)
		setupCsvSettings("11", "0", ",", false)
		String reportUrl = WebUI.getUrl()
		createReportCM(subreportEmail)

		ArrayList<String> report = [reportUrl, reportName, advertiserName]
		println report
		return report
	}

	@Keyword
	public ArrayList<String> downloadXlsEUL(String templateFormat, String scheduleFrequency) {
		String reportName = "Test auto - Download XLS - EUL " + scheduleFrequency.substring(0, 1).toUpperCase() + scheduleFrequency.substring(1)
		String advertiserName = "Test Advertiser"
		String subreportEmail = createAndSetupReport(reportName, advertiserName, templateFormat, scheduleFrequency, "EUL Subreport", 2)
		String reportUrl = WebUI.getUrl()
		//createReportEUL(subreportEmail)

		//ArrayList<String> report = [reportUrl, reportName, advertiserName]
		//return report
	}

	@Keyword
	public ArrayList<String> downloadXlsGA(String templateFormat, String scheduleFrequency) {
		String reportName = "Test auto - Download XLS - GA " + scheduleFrequency.substring(0, 1).toUpperCase() + scheduleFrequency.substring(1)
		String advertiserName = "Test Advertiser"
		String subreportEmail = createAndSetupReport(reportName, advertiserName, templateFormat, scheduleFrequency, "GA Subreport", 3)
		String reportUrl = WebUI.getUrl()
		//createReportGA(subreportEmail)
		ArrayList<String> report = [reportUrl, reportName, advertiserName]
		return report
	}

	@Keyword
	public void verifyReports(List<List<String>> reports, String templateFormat) {
		String downloadedReportPath = ""

		for(report in reports) {
			WebUI.navigateToUrl(report.get(0))
			WebUI.waitForPageLoad(30)
			String currentDate = generateCurrentDate()
			downloadedReportPath = downloadReport(report.get(1), report.get(2), templateFormat, currentDate)


			// We can only check xlsx files
			if(templateFormat.equals("xlsm")) {
				String fileName = "( " + report.get(2) + " ) " + report.get(1) + " (" + currentDate + ")"
				renameFile(downloadFolderPath, fileName + ".xlsm", fileName + ".xlsx")
				downloadedReportPath = downloadedReportPath.replaceFirst("xlsm", "xlsx")
			}

			if(downloadedReportPath.contains("APN")) {
				checkReportDownloaded(downloadedReportPath, "8", apnMailCsvExpectedContent)
			} else if (downloadedReportPath.contains("DV360")) {
				checkReportDownloaded(downloadedReportPath, "9", dv360MailCsvExpectedContent)
			} else if (downloadedReportPath.contains("CM")) {
				checkReportDownloaded(downloadedReportPath, "10", cmMailCsvExpectedContent)
			}
			deleteFile(downloadedReportPath)
		}
	}

	private String createAndSetupReport(String reportName, String advertiserName, String templateFormat, String xlsSheetNumber, String scheduleFrequency, String subreportName, int datasourceNumber) {
		selenium = getSelenium()
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Download XLS") == false) {
			createFolder("Test auto - Download XLS")
		}

		createReport(reportName, advertiserName)
		createSubreport(subreportName, datasourceNumber)
		setupScheduleToday(scheduleFrequency)
		uploadReportTemplate(templateFormat, "Template for Mail Upload")
		setupReportXlsSettings(xlsSheetNumber)

		selenium.click("css=document-setup > mat-card > mat-card-content > div:nth-child(1) > button:nth-of-type(2)")
		WebUI.waitForElementVisible(selectorToTestObject("section.display-sheets > table > tbody > tr:nth-child(1) > td.mat-column-receipt_email > div > div > span"), 30)
		Thread.sleep(1000)
		selenium.click("css=section.display-sheets > table > tbody > tr:nth-child(1) > td.mat-column-receipt_email > div > div > span")
		WebUI.waitForElementVisible(selectorToTestObject("div > snack-bar-container"), 30)
		String subreportEmail = selenium.getText("css=div > snack-bar-container")
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
		selenium.click("css=button[aria-label=\"Close dialog\"]")
		return subreportEmail.replace(" has been copied !", "")
	}

	private void createReportApn(String subreportEmail) {
		selenium = getSelenium()
		selenium.open("https://console.appnexus.com/login")
		WebUI.waitForElementPresent(selectorToTestObject("#login-form fieldset button"), 30)
		if(selenium.isElementPresent("css=.CloseIcon") == true) {
			selenium.click("css=.CloseIcon")
		}
		WebUI.waitForElementVisible(selectorToTestObject("#login-form fieldset button"), 30)
		selenium.type("css=input#anxs-login-username", GlobalVariable.apnLogin)
		WebUI.setEncryptedText(selectorToTestObject("input#anxs-login-password"), GlobalVariable.apnPassword)
		selenium.click("css=#login-form fieldset button")
		selenium.open("https://console.appnexus.com/report/network/your-reports")
		WebUI.waitForElementVisible(selectorToTestObject("button[name=\"modify-saved-report\"]"), 30)
		selenium.click("css=button[name=\"modify-saved-report\"]")
		Thread.sleep(1000)
		selenium.click("css=li[data-key=\"EXPORT_TO_EMAIL\"] > label > span")
		WebUI.clearText(selectorToTestObject("input[name=\"export-email-addresses\"]"))
		Thread.sleep(500)
		WebUI.sendKeys(selectorToTestObject("input[name=\"export-email-addresses\"]"), subreportEmail)
		Thread.sleep(500)
		assert WebUI.getAttribute(selectorToTestObject("input[name=\"export-email-addresses\"]"), "value").equals(subreportEmail) == true
		Thread.sleep(1000)
		selenium.click("css=fieldset.report-submit > div.report-submit-controls > button")
		WebUI.waitForElementVisible(selectorToTestObject("section[type=\"EXPORT_TO_EMAIL_ready\"]"), 30)
	}

	private void createReportDV360(String subreportEmail) {
		selenium = getSelenium()
		WebUI.navigateToUrl("https://displayvideo.google.com/analytics/dfa/?inIframe=1&hl=en_US#reports/combinedList/0:138402/?_u.dbmPartnerId=362434")
		WebUI.waitForPageLoad(30)
		WebUI.delay(3)
		if(selenium.isElementPresent("css=#identifierId") == true) {
			loginToGoogle()
			Thread.sleep(2000)
			WebUI.navigateToUrl("https://displayvideo.google.com/analytics/dfa/?inIframe=1&hl=en_US#reports/combinedList/0:138402/?_u.dbmPartnerId=362434")
		}
		WebUI.waitForElementVisible(selectorToTestObject("div.navi-bar > tab-button:nth-of-type(2)"), 30)

		selenium.click("css=div.navi-bar > tab-button:nth-of-type(2)")
		WebUI.waitForElementVisible(selectorToTestObject("div.nameCellEnabled"), 30)

		selenium.click("css=div.nameCellEnabled")
		WebUI.waitForElementVisible(selectorToTestObject(".ID-addPeople"), 30)
		WebUI.scrollToElement(selectorToTestObject(".ID-addPeople"), 5)

		if(selenium.isElementPresent("css=div.C_DFA_DELIVERY_SHAREWITH_PANEL > div.ACTION-clearAll") == true) {
			WebElement sharePanel = driver.findElement(By.cssSelector("div.C_DFA_DELIVERY_SHAREWITH_PANEL"))
			WebElement clearSharePanel = driver.findElement(By.cssSelector("div.C_DFA_DELIVERY_SHAREWITH_PANEL"))
			Actions builder = new Actions(driver)
			Actions hoverOverSharePanel = builder.moveToElement(sharePanel)
			hoverOverSharePanel.perform()
			WebUI.executeJavaScript("document.querySelector(\"div.C_DFA_DELIVERY_SHAREWITH_PANEL > div.ACTION-clearAll\").click()", null)
		}

		selenium.click("css=.ID-addPeople")
		WebUI.waitForElementVisible(selectorToTestObject(".ID-fakeAddressInput"), 30)

		selenium.click("css=.ID-fakeAddressInput")
		WebUI.waitForElementVisible(selectorToTestObject(".ID-realAddressInput"), 30)

		WebUI.sendKeys(selectorToTestObject(".ID-realAddressInput"), subreportEmail)
		Thread.sleep(500)
		selenium.click("css=div.C_DFA_DELIVERY_SHAREWITH_POPUP_ADD_BUTTONS > .ID-addButton")
		Thread.sleep(1500)
		selenium.click("css=.ACTION-done")
		Thread.sleep(500)
		selenium.click("css=.ACTION-saveAndRun")
		WebUI.waitForElementVisible(selectorToTestObject(".A_MESSAGEBOX_SUCCESS"), 30)
		WebUI.waitForElementVisible(selectorToTestObject("div.particle-table-row:nth-child(2) download-cell mat-icon-button:not([style])"), 30)
	}

	private void createReportCM(String subreportEmail) {
		selenium = getSelenium()
		WebUI.navigateToUrl("https://ddm.google.com/analytics/dfa/?defaultDs=5166043%3A572403")
		WebUI.waitForPageLoad(30)
		WebUI.delay(3)
		if(selenium.isElementPresent("css=#identifierId") == true) {
			loginToGoogle()
			Thread.sleep(2000)
			WebUI.navigateToUrl("https://ddm.google.com/analytics/dfa/?defaultDs=5166043%3A572403")
		}
		WebUI.waitForElementVisible(selectorToTestObject("div.navi-bar > tab-button:nth-of-type(2)"), 30)

		selenium.click("css=div.navi-bar > tab-button:nth-of-type(2)")
		WebUI.waitForElementVisible(selectorToTestObject("div.nameCellEnabled"), 30)

		selenium.click("css=div.nameCellEnabled")
		WebUI.waitForElementVisible(selectorToTestObject(".ID-addPeople"), 30)
		WebUI.scrollToElement(selectorToTestObject(".ID-addPeople"), 5)

		if(selenium.isElementPresent("css=div.C_DFA_DELIVERY_SHAREWITH_PANEL > div.ACTION-clearAll") == true) {
			WebElement sharePanel = driver.findElement(By.cssSelector("div.C_DFA_DELIVERY_SHAREWITH_PANEL"))
			WebElement clearSharePanel = driver.findElement(By.cssSelector("div.C_DFA_DELIVERY_SHAREWITH_PANEL"))
			Actions builder = new Actions(driver)
			Actions hoverOverSharePanel = builder.moveToElement(sharePanel)
			hoverOverSharePanel.perform()
			WebUI.executeJavaScript("document.querySelector(\"div.C_DFA_DELIVERY_SHAREWITH_PANEL > div.ACTION-clearAll\").click()", null)
		}

		selenium.click("css=.ID-addPeople")
		WebUI.waitForElementVisible(selectorToTestObject(".ID-fakeAddressInput"), 30)

		selenium.click("css=.ID-fakeAddressInput")
		WebUI.waitForElementVisible(selectorToTestObject(".ID-realAddressInput"), 30)

		WebUI.sendKeys(selectorToTestObject(".ID-realAddressInput"), subreportEmail)
		Thread.sleep(500)
		selenium.click("css=div.C_DFA_DELIVERY_SHAREWITH_POPUP_ADD_BUTTONS > .ID-addButton")
		Thread.sleep(1500)
		selenium.click("css=.ACTION-done")
		Thread.sleep(500)
		selenium.click("css=.ACTION-saveAndRun")
		WebUI.waitForElementVisible(selectorToTestObject(".A_MESSAGEBOX_SUCCESS"), 30)
		WebUI.waitForElementVisible(selectorToTestObject("div.particle-table-row:nth-child(2) download-cell mat-icon-button:not([style])"), 30)
	}
}
