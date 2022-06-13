package testCases

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable
import reportfolio.Reportfolio

public class GenerateSeparatedFiles extends Reportfolio {

	private String templateFileName = ""
	private String apnCsvFileName = ""
	private String dv360CsvFileName = ""
	private String gcmCsvFileName = ""
	private String gaCsvFileName = ""
	private String eulerianCsvFileName = ""

	@Keyword
	public void generateSeparatedFilesOneSubreport(String templateFormat) {
		selenium = getSelenium()
		String randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))
		String reportName = "Test auto - Report - Generate separated files"
		String advertiserName = "Test Advertiser"

		createFolder("Test auto - Generate separated files")
		createReport(reportName + randomNumber, advertiserName)
		String reportFullName = WebUI.getText(findTestObject('Object Repository/3. Report/Report name'))

		createSubreport("APN Subreport", 1)

		setupScheduleToday("Daily")

		setupXlsSettingsGenerateSeparatedFiles(templateFormat)

		selenium.refresh()
		WebUI.waitForPageLoad(30)

		WebUI.uploadFile(findTestObject('Object Repository/3. Report/6. CSV Preview/Upload CSV input'), apnCsvFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/6. CSV Preview/CSV Preview info'), 30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("File well uploaded.", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)

		String currentDate = generateCurrentDate()
		int remainingAttempts = 40
		boolean canDownloadReport = selenium.isElementPresent("css=div.cdk-overlay-pane > div > div > button[aria-disabled=\"false\"]")
		while(canDownloadReport == false && remainingAttempts > 0) {
			Thread.sleep(5000);
			selenium.refresh()
			WebUI.waitForPageLoad(30)
			WebUI.waitForElementPresent(findTestObject('Object Repository/3. Report/7. Download report/Download button'))
			WebUI.click(findTestObject('Object Repository/3. Report/7. Download report/Download button'))
			WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/7. Download report/Inactive Download button'), 30)
			canDownloadReport = selenium.isElementPresent("css=div.cdk-overlay-pane > div > div > button[aria-disabled=\"false\"]")
			remainingAttempts = remainingAttempts - 1
		}
		WebUI.click(findTestObject('Object Repository/3. Report/7. Download report/Active Download button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/Toast message'), 30)
		assert WebUI.verifyEqual(reportFullName + " (" + currentDate + ") has been successfully download.", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)

		selenium.open("https://stuk.github.io/jszip/documentation/examples/read-local-file-api.html")
		WebUI.waitForElementVisible(findTestObject('Object Repository/8. Tool for checking zip content/Upload file input'), 30)
		WebUI.uploadFile(findTestObject('Object Repository/8. Tool for checking zip content/Upload file input'), System.getProperty("user.home") + sep + "Downloads" + sep + reportFullName + " (" + currentDate + ").zip")
		WebUI.waitForElementVisible(findTestObject('Object Repository/8. Tool for checking zip content/1st result'), 30)
		WebUI.focus(findTestObject('Object Repository/8. Tool for checking zip content/1st result'))
		assert WebUI.verifyEqual(templateFileName, WebUI.getText(findTestObject('Object Repository/8. Tool for checking zip content/1st result'))) == true
		WebUI.focus(findTestObject('Object Repository/8. Tool for checking zip content/2nd result'))
		assert WebUI.verifyEqual(apnCsvFileName, WebUI.getText(findTestObject('Object Repository/8. Tool for checking zip content/2nd result'))) == true
	}

	@Keyword
	public void generateSeparatedFilesOneSubreportPerDatasource(String templateFormat) {
		selenium = getSelenium()
		WebUI.delay(2)
		WebUI.navigateToUrl(baseUrl)
		WebUI.waitForPageLoad(30)
		String randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))

		// Wait for folders to load because new report modal is broken before
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		//createFolder("Test auto - Generate separated files")
		String reportName = "Test auto - Report - Generate separated files"
		String advertiserName = "Test Advertiser"
		createReport(reportName + randomNumber, advertiserName)
		String reportFullName = WebUI.getText(findTestObject('Object Repository/3. Report/Report name'))

		String[] subreportName = ["APN Subreport", "Eulerian Subreport", "GA Subreport", "GCM Subreport", "DV360 Subreport"]

		createAllSubreports(subreportName)

		setupScheduleToday("Daily")

		setupXlsSettingsGenerateSeparatedFiles(templateFormat)

		selenium.refresh()
		WebUI.waitForPageLoad(30)
		Thread.sleep(1500);
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/6. CSV Preview/Upload CSV input'), dv360CsvFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/6. CSV Preview/CSV Preview info'), 30)
		Thread.sleep(750);
		WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/Subreports/Subreport 4'))
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/6. CSV Preview/Upload CSV input'), cmCsvFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/6. CSV Preview/CSV Preview info'), 30)
		Thread.sleep(750);
		WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/Subreports/Subreport 3'))
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/6. CSV Preview/Upload CSV input'), gaCsvFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/6. CSV Preview/CSV Preview info'), 30)
		Thread.sleep(750);
		WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/Subreports/Subreport 2'))
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/6. CSV Preview/Upload CSV input'), eulCsvFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/6. CSV Preview/CSV Preview info'), 30)
		Thread.sleep(750);
		WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/Subreports/Subreport 1'))
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/6. CSV Preview/Upload CSV input'), apnCsvFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/6. CSV Preview/CSV Preview info'), 30)
		Thread.sleep(750);

		String currentDate = generateCurrentDate()
		int waitForReport = 500
		int remainingAttempts = 40
		boolean canDownloadReport = selenium.isElementPresent("css="+ findTestObject('Object Repository/3. Report/7. Download report/Active Download button').getSelectorCollection().get(SelectorMethod.CSS))
		while(canDownloadReport == false && remainingAttempts > 0) {
			Thread.sleep(waitForReport);
			selenium.refresh()
			WebUI.waitForPageLoad(30)
			WebUI.click(findTestObject('Object Repository/3. Report/7. Download report/Download button'))
			WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/7. Download report/Overlay button'), 30)
			findTestObject('Object Repository/3. Report/7. Download report/Active Download button')
			canDownloadReport = selenium.isElementPresent("css="+ findTestObject('Object Repository/3. Report/7. Download report/Active Download button').getSelectorCollection().get(SelectorMethod.CSS))
			remainingAttempts = remainingAttempts - 1
		}

		WebUI.click(findTestObject('Object Repository/3. Report/7. Download report/Active Download button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/Toast message'), 30)
		assert WebUI.verifyEqual(reportFullName + " (" + currentDate + ") has been successfully download.", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)

		WebUI.navigateToUrl("https://stuk.github.io/jszip/documentation/examples/read-local-file-api.html")
		WebUI.waitForElementVisible(findTestObject('Object Repository/8. Tool for checking zip content/Upload file input'), 30)
		WebUI.uploadFile(findTestObject('Object Repository/8. Tool for checking zip content/Upload file input'), System.getProperty("user.home") + sep + "Downloads" + sep + "( " + advertiserName + " ) " + reportName + randomNumber + " (" + currentDate + ").zip")
		WebUI.focus(findTestObject('Object Repository/8. Tool for checking zip content/1st result'))
		assert WebUI.verifyEqual(templateFileName, WebUI.getText(findTestObject('Object Repository/8. Tool for checking zip content/1st result'))) == true
		WebUI.focus(findTestObject('Object Repository/8. Tool for checking zip content/2nd result'))
		assert WebUI.verifyEqual(apnCsvFileName, WebUI.getText(findTestObject('Object Repository/8. Tool for checking zip content/2nd result'))) == true
		WebUI.focus(findTestObject('Object Repository/8. Tool for checking zip content/3rd result'))
		assert WebUI.verifyEqual(eulerianCsvFileName, WebUI.getText(findTestObject('Object Repository/8. Tool for checking zip content/3rd result'))) == true
		WebUI.focus(findTestObject('Object Repository/8. Tool for checking zip content/4th result'))
		assert WebUI.verifyEqual(gaCsvFileName, WebUI.getText(findTestObject('Object Repository/8. Tool for checking zip content/4th result'))) == true
		WebUI.focus(findTestObject('Object Repository/8. Tool for checking zip content/5th result'))
		assert WebUI.verifyEqual(gcmCsvFileName, WebUI.getText(findTestObject('Object Repository/8. Tool for checking zip content/5th result'))) == true
		WebUI.focus(findTestObject('Object Repository/8. Tool for checking zip content/6th result'))
		assert WebUI.verifyEqual(dv360CsvFileName, WebUI.getText(findTestObject('Object Repository/8. Tool for checking zip content/6th result'))) == true
	}

	private String setupXlsSettingsGenerateSeparatedFiles(String templateFormat) {
		String templateFullPath =  csvAndTemplateFolderPath + "Template." + templateFormat.toLowerCase()
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/3. XLS Settings/Upload Template input'), templateFullPath)

		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("Template." + templateFormat.toLowerCase() +" has been successfully uploaded.", WebUI.getText(findTestObject('3. Report/Toast message'))) == true

		WebUI.waitForElementNotPresent(findTestObject('3. Report/Template Toast message'), 30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'), 30)
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'))
		WebUI.waitForElementVisible(findTestObject('3. Report/3. XLS Settings/Generate Separated Files/Generate Separated Files switch'), 30)
		WebUI.click(findTestObject('3. Report/3. XLS Settings/Generate Separated Files/Generate Separated Files switch'))
		WebUI.focus(findTestObject('Object Repository/3. Report/3. XLS Settings/Generate Separated Files/GSF 1st file name'))
		templateFileName = selenium.getText("css=app-document-settings > mat-dialog-content > section.display-sheets > ul > li:nth-child(1)")
		WebUI.focus(findTestObject('Object Repository/3. Report/3. XLS Settings/Generate Separated Files/GSF 2nd file name'))
		apnCsvFileName = selenium.getText("css=app-document-settings > mat-dialog-content > section.display-sheets > ul > li:nth-child(2)")
		int numberOfSubreports = selenium.getCssCount("app-document-settings > mat-dialog-content > section.display-sheets > ul > li")

		if(numberOfSubreports > 5) {
			WebUI.focus(findTestObject('Object Repository/3. Report/3. XLS Settings/Generate Separated Files/GSF 3rd file name'))
			gcmCsvFileName = selenium.getText("css=app-document-settings > mat-dialog-content > section.display-sheets > ul > li:nth-child(3)")
			WebUI.focus(findTestObject('Object Repository/3. Report/3. XLS Settings/Generate Separated Files/GSF 4th file name'))
			dv360CsvFileName = selenium.getText("css=app-document-settings > mat-dialog-content > section.display-sheets > ul > li:nth-child(4)")
			WebUI.focus(findTestObject('Object Repository/3. Report/3. XLS Settings/Generate Separated Files/GSF 5th file name'))
			eulerianCsvFileName = selenium.getText("css=app-document-settings > mat-dialog-content > section.display-sheets > ul > li:nth-child(5)")
			WebUI.focus(findTestObject('Object Repository/3. Report/3. XLS Settings/Generate Separated Files/GSF 6th file name'))
			gaCsvFileName = selenium.getText("css=app-document-settings > mat-dialog-content > section.display-sheets > ul > li:nth-child(6)")
		}
		WebUI.click(findTestObject('2. Folders list/Modal/Basic Report Save button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("Settings updated sucessfully", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
	}
}
