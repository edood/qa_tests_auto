package testCases

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable
import reportfolio.Reportfolio

public class ManualUpload extends Reportfolio {

	@Keyword
	public void manualUploadSuccessToast(String templateFormat) {
		selenium = getSelenium()
		String reportName = "Test auto - Success Toast"
		String advertiserName = "Test Advertiser"

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Manual Upload") == false) {
			createFolder("Test auto - Manual Upload")
		}

		createReport(reportName, advertiserName)
		createSubreport("APN Subreport", 1)
		setupScheduleToday("daily")
		uploadReportTemplate(templateFormat, "Template")
		setupReportXlsSettings("8")
		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		manualUpload(apnCsvFullPath)
	}

	@Keyword
	public void manualUploadFailureToast() {
		selenium = getSelenium()
		String reportName = "Test auto - Failure Toast"
		String advertiserName = "Test Advertiser"

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Manual Upload") == false) {
			createFolder("Test auto - Manual Upload")
		}

		createReport(reportName, advertiserName)
		createSubreport("APN Subreport", 1)
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/6. CSV Preview/Upload CSV input'), apnCsvFullPath)
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("File upload failure, please retry", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
	}

	@Keyword
	public void manualUploadClosedIteration(String templateFormat) {
		selenium = getSelenium()
		String reportName = "Test auto - Closed Iteration"
		String advertiserName = "Test Advertiser"

		changeSetupDates("P0D")

		WebUI.navigateToUrl(baseUrl)
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)
		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Manual Upload") == false) {
			createFolder("Test auto - Manual Upload")
		}
		String currentUrl = createAndSetupReport(baseUrl, reportName, advertiserName, templateFormat, "daily", "APN Subreport", 1, apnCsvFullPath)
		String currentDate = generateCurrentDate()
		WebUI.delay(3)
		changeSetupDates("P1D")
		currentUrl = modifyCurrentReport(currentUrl, "DV360 Subreport", 5, dv360CsvFullPath)

		WebUI.navigateToUrl(currentUrl)
		WebUI.waitForPageLoad(30)

		String downloadedReportPath = ""
		if(GlobalVariable.downloadReport == true) {
			downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
			// We can only check xlsx files
			if(templateFormat.equals("xlsm")) {
				String fileName = downloadedReportPath.replaceAll(".*\\\\(.*)\\.xls.\$", "\$1")
				renameFile(downloadFolderPath, fileName + ".xlsm", fileName + ".xlsx")
				downloadedReportPath = downloadedReportPath.replaceFirst("xlsm", "xlsx")
			}
			checkReportDownloaded(downloadedReportPath, "8", apnCsvExpectedContent)
			checkReportDownloaded(downloadedReportPath, "9", dv360CsvExpectedContent)
			deleteFile(downloadedReportPath)
		}

		changeSetupDates("P0D")
	}

	@Keyword
	public void manualUploadViaUrlApn(String templateFormat) {
		selenium = getSelenium()
		String reportName = "Test auto - Manual Upload - APN Via URL"
		String advertiserName = "Test Advertiser"

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Manual Upload") == false) {
			createFolder("Test auto - Manual Upload")
		}

		createReport(reportName, advertiserName)
		createSubreport("APN Subreport", 1)
		setupScheduleToday("daily")
		uploadReportTemplate(templateFormat, "Template")
		setupReportXlsSettings("8")

		String currentURL = WebUI.getUrl()
		String uploaderURL = currentURL.replaceFirst("(.*)(\\/folders.*)", '$1/uploader$2/iteration/1')
		selenium.open(uploaderURL)
		String currentDate = generateCurrentDate()
		WebUI.waitForElementPresent(selectorToTestObject("app-uploader > div > mat-card > mat-card-content > section"), 30)
		if(GlobalVariable.env.equals("Staging")) {
			String uploaderWording = "Hi,\nIn order to build the daily report ( " + advertiserName +" ) " + reportName + " of the " + currentDate + ",\nmay you please upload the CSV file coming from AppNexus\nand needed to complete the APN Subreport datasource.\nThanks,"
			assert WebUI.verifyEqual(uploaderWording, selenium.getText("css=app-uploader > div > mat-card > mat-card-content > section")) == true
		} else {
			String uploaderWording = "Hi,\nIn order to build the daily report ( " + advertiserName +" ) " + reportName + " of the " + currentDate + ",\nmay you please upload the CSV file coming from Appnexus\nand needed to complete the APN Subreport datasource.\nThanks,"
			assert WebUI.verifyEqual(uploaderWording, selenium.getText("css=app-uploader > div > mat-card > mat-card-content > section")) == true
		}
		WebUI.uploadFile(selectorToTestObject("file-upload-btn > button > span > input"), apnCsvFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("File well uploaded.", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
		selenium.open(currentURL)
		WebUI.waitForPageLoad(30)
		String csvPreviewFirstLines = "day advertiser_id advertiser_name insertion_order_id insertion_order_name line_item_id line_item_name imps clicks revenue tradelabel\n2019-11-05 845763 Boursorama 3108427 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19 9725057 PROSPECTION_ALI_CPA_MOBILE 450250 64 204,877435\n2019-11-05 845763 Boursorama 3108427 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19 6694498 PROSPECTION_PUBLISHER_MOBILE 230550 40 116,174718\n2019-11-05 845763 Boursorama 3108427 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19 8016072 PROSPECTION_MOBILE_MMO 172410 42 79,121204\n2019-11-05 845763 Boursorama 3108508 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19 2791000 PROSPECTION_AUTOMATIONVISITE_DESKTOP 133539 17 126,471034\n2019-11-05 845763 Boursorama 3113824 OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Novembre19 10044300 PROSPECTION_BIDDER_DESKTOP 83853 26 80,558281\n2019-11-05 845763 Boursorama 3108508 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19 3936328 PROSPECTION_APB_DESKTOP 82437 42 87,334454\n2019-11-05 845763 Boursorama 3108508 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19 7662574 PROSPECTION_AUTOMATION-LEAD_DESKTOP 61555 3 60,85293\n2019-11-05 845763 Boursorama 3108508 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19 8023288 PROSPECTION_AUTOMATION-LEADv2_DESKTOP 57821 10 61,283288\n2019-11-05 845763 Boursorama 3108508 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19 9650618 PROSPECTION_APB_DESKTOP_NEW 29638 12 49,251197\n2019-11-05 845763 Boursorama 3108427 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19 9106179 PROFILING_MOBILE 26957 7 43,264773"
		String csvPreviewSecondLines = "2019-11-05 845763 Boursorama 2842154 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19 9725057 PROSPECTION_ALI_CPA_MOBILE 0 0 0\n2019-11-05 845763 Boursorama 2842154 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19 9760094 PROSPECTION_NATIVE 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 2791000 PROSPECTION_AUTOMATIONVISITE_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 3936328 PROSPECTION_APB_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 7662574 PROSPECTION_AUTOMATION-LEAD_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 8023288 PROSPECTION_AUTOMATION-LEADv2_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 8724447 PROFILING_ULTIM_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 9650618 PROSPECTION_APB_DESKTOP_NEW 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 9760155 PROSPECTION_NATIVE_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842170 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Octobre19 4612927 PROSPECTION_BIDDER_DESKTOP 0 0 0"
		checkCsvPreview("CSV of iteration " + currentDate + " - Total rows : 24", csvPreviewFirstLines, csvPreviewSecondLines)
		String downloadedReportPath = ""
		if(GlobalVariable.downloadReport == true) {
			downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
			// We can only check xlsx files
			if(templateFormat.equals("xlsm")) {
				String fileName = downloadedReportPath.replaceAll(".*\\\\(.*)\\.xls.\$", "\$1")
				renameFile(downloadFolderPath, fileName + ".xlsm", fileName + ".xlsx")
				downloadedReportPath = downloadedReportPath.replaceFirst("xlsm", "xlsx")
			}
			checkReportDownloaded(downloadedReportPath, "8", apnCsvExpectedContent)
			deleteFile(downloadedReportPath)
		}
		selenium.open(currentURL)
	}

	@Keyword
	public void manualUploadViaUrlDv360(String templateFormat) {
		selenium = getSelenium()
		String reportName = "Test auto - Manual Upload - DV360 Via URL"
		String advertiserName = "Test Advertiser"

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Manual Upload") == false) {
			createFolder("Test auto - Manual Upload")
		}

		createReport(reportName, advertiserName)
		createSubreport("DV360 Subreport", 5)
		setupScheduleToday("daily")
		uploadReportTemplate(templateFormat, "Template")
		setupCsvSettings("1", "13", ",", false)
		setupReportXlsSettings("9")

		String currentURL = WebUI.getUrl()
		String uploaderURL = currentURL.replaceFirst("(.*)(\\/folders.*)", '$1/uploader$2/iteration/1')
		selenium.open(uploaderURL)
		String currentDate = generateCurrentDate()
		WebUI.waitForElementPresent(selectorToTestObject("app-uploader > div > mat-card > mat-card-content > section"), 30)

		String uploaderWording = "Hi,\nIn order to build the daily report ( " + advertiserName +" ) " + reportName + " of the " + currentDate + ",\nmay you please upload the CSV file coming from Google DV360\nand needed to complete the DV360 Subreport datasource.\nThanks,"
		assert WebUI.verifyEqual(uploaderWording, selenium.getText("css=app-uploader > div > mat-card > mat-card-content > section")) == true

		WebUI.uploadFile(selectorToTestObject("file-upload-btn > button > span > input"), dv360CsvFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("File well uploaded.", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
		selenium.open(currentURL)
		WebUI.waitForPageLoad(30)
		String csvPreviewFirstLines = "Date Advertiser Insertion Order ID Insertion Order Line Item Line Item ID Advertiser Currency Impressions Clicks Revenue (Adv Currency) Tradelabel\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No 29316317 EUR 982 1 0.602487\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No 31514957 EUR 2678 0 2.183799\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No_CPA 29316319 EUR 13858 2 10.666706\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Keywords_Retargeting-6h_Google_Final Fantasy Fans_No 29316321 EUR 35 0 0.436678\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Keywords_Retargeting-7j_Google_FFXIV Interest_No 29316349 EUR 284 0 1.143764\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Keywords_x_Google_Final Fantasy Fans_No 29316353 EUR 6672 0 3.758819\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No 29316361 EUR 124 0 0.952738\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No 31514962 EUR 144 0 1.052764\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Profiling_Retargeting-1j_Webedia_PC Gamers / MMORPG Games Fans_No 29316362 EUR 388 0 2.459044\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No 29316368 EUR 2653 0 1.710639"
		String csvPreviewSecondLines = "2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Keywords_x_Google_Platinum Games 31418002 EUR 139 1 1.761529\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Profiling_Retargeting-1j_Webedia_PC Gamers / MMORPG Games Fans_No 31391192 EUR 30 0 0.228780\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No 31391193 EUR 288 2 0.451342\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No_Best Placement 31391194 EUR 145 0 2.262644\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No_Surpression Placement Test 31391195 EUR 87 1 2.595618\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Prospecting_Retargeting-30j_Webedia_Pack Gaming - IAB_Yes 31391198 EUR 3231 0 3.707637\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No 31391199 EUR 10123 0 4.321461\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Prospecting_x_Webedia_NieR:Automata Fans 31417996 EUR 1164 0 2.116019\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Prospecting_x_Webedia_Pack Gaming - IAB_Yes 31391200 EUR 139 0 0.615344\n156030 38 127.449848"
		checkCsvPreview("CSV of iteration " + currentDate + " - Total rows : 64", csvPreviewFirstLines, csvPreviewSecondLines)
		String downloadedReportPath = ""
		if(GlobalVariable.downloadReport == true) {
			downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
			// We can only check xlsx files
			if(templateFormat.equals("xlsm")) {
				String fileName = downloadedReportPath.replaceAll(".*\\\\(.*)\\.xls.\$", "\$1")
				renameFile(downloadFolderPath, fileName + ".xlsm", fileName + ".xlsx")
				downloadedReportPath = downloadedReportPath.replaceFirst("xlsm", "xlsx")
			}
			checkReportDownloaded(downloadedReportPath, "9", dv360CsvExpectedContent)
			deleteFile(downloadedReportPath)
		}
		selenium.open(currentURL)
	}

	@Keyword
	public void manualUploadApn(String templateFormat) {
		selenium = getSelenium()
		String reportName = "Test auto - Manual Upload - APN Daily"
		String advertiserName = "Test Advertiser"

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)
		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Manual Upload") == false) {
			createFolder("Test auto - Manual Upload")
		}

		createAndSetupReport("", reportName, advertiserName, templateFormat, "daily", "APN Subreport", 1, apnCsvFullPath)

		String currentURL = WebUI.getUrl()
		String currentDate = generateCurrentDate()
		String csvPreviewInfo = "CSV of iteration " + currentDate + " - Total rows : 24"
		String csvPreviewFirstLines = "day advertiser_id advertiser_name insertion_order_id insertion_order_name line_item_id line_item_name imps clicks revenue tradelabel\n2019-11-05 845763 Boursorama 3108427 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19 9725057 PROSPECTION_ALI_CPA_MOBILE 450250 64 204,877435\n2019-11-05 845763 Boursorama 3108427 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19 6694498 PROSPECTION_PUBLISHER_MOBILE 230550 40 116,174718\n2019-11-05 845763 Boursorama 3108427 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19 8016072 PROSPECTION_MOBILE_MMO 172410 42 79,121204\n2019-11-05 845763 Boursorama 3108508 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19 2791000 PROSPECTION_AUTOMATIONVISITE_DESKTOP 133539 17 126,471034\n2019-11-05 845763 Boursorama 3113824 OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Novembre19 10044300 PROSPECTION_BIDDER_DESKTOP 83853 26 80,558281\n2019-11-05 845763 Boursorama 3108508 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19 3936328 PROSPECTION_APB_DESKTOP 82437 42 87,334454\n2019-11-05 845763 Boursorama 3108508 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19 7662574 PROSPECTION_AUTOMATION-LEAD_DESKTOP 61555 3 60,85293\n2019-11-05 845763 Boursorama 3108508 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19 8023288 PROSPECTION_AUTOMATION-LEADv2_DESKTOP 57821 10 61,283288\n2019-11-05 845763 Boursorama 3108508 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19 9650618 PROSPECTION_APB_DESKTOP_NEW 29638 12 49,251197\n2019-11-05 845763 Boursorama 3108427 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19 9106179 PROFILING_MOBILE 26957 7 43,264773"
		String csvPreviewSecondLines = "2019-11-05 845763 Boursorama 2842154 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19 9725057 PROSPECTION_ALI_CPA_MOBILE 0 0 0\n2019-11-05 845763 Boursorama 2842154 OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19 9760094 PROSPECTION_NATIVE 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 2791000 PROSPECTION_AUTOMATIONVISITE_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 3936328 PROSPECTION_APB_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 7662574 PROSPECTION_AUTOMATION-LEAD_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 8023288 PROSPECTION_AUTOMATION-LEADv2_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 8724447 PROFILING_ULTIM_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 9650618 PROSPECTION_APB_DESKTOP_NEW 0 0 0\n2019-11-05 845763 Boursorama 2842164 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19 9760155 PROSPECTION_NATIVE_DESKTOP 0 0 0\n2019-11-05 845763 Boursorama 2842170 OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Octobre19 4612927 PROSPECTION_BIDDER_DESKTOP 0 0 0"
		checkCsvPreview(csvPreviewInfo, csvPreviewFirstLines, csvPreviewSecondLines)
		String downloadedReportPath = ""
		if(GlobalVariable.downloadReport == true) {
			downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
			// We can only check xlsx files
			if(templateFormat.equals("xlsm")) {
				String fileName = downloadedReportPath.replaceAll(".*\\\\(.*)\\.xls.\$", "\$1")
				renameFile(downloadFolderPath, fileName + ".xlsm", fileName + ".xlsx")
				downloadedReportPath = downloadedReportPath.replaceFirst("xlsm", "xlsx")
			}
			checkReportDownloaded(downloadedReportPath, "8", apnCsvExpectedContent)
			deleteFile(downloadedReportPath)
		}
		selenium.open(currentURL)
	}

	@Keyword
	public void manualUploadDv360(String templateFormat) {
		selenium = getSelenium()
		String reportName = "Test auto - Manual Upload - DV360 Weekly"
		String advertiserName = "Test Advertiser"

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)
		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Manual Upload") == false) {
			createFolder("Test auto - Manual Upload")
		}

		createAndSetupReport("", reportName, advertiserName, templateFormat, "weekly", "DV360 Subreport", 5, dv360CsvFullPath)

		String currentURL = WebUI.getUrl()
		String currentDate = generateCurrentDate()
		String csvPreviewInfo = "CSV of iteration " + currentDate + " - Total rows : 64"
		String csvPreviewFirstLines = "Date Advertiser Insertion Order ID Insertion Order Line Item Line Item ID Advertiser Currency Impressions Clicks Revenue (Adv Currency) Tradelabel\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No 29316317 EUR 982 1 0.602487\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No 31514957 EUR 2678 0 2.183799\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No_CPA 29316319 EUR 13858 2 10.666706\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Keywords_Retargeting-6h_Google_Final Fantasy Fans_No 29316321 EUR 35 0 0.436678\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Keywords_Retargeting-7j_Google_FFXIV Interest_No 29316349 EUR 284 0 1.143764\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Keywords_x_Google_Final Fantasy Fans_No 29316353 EUR 6672 0 3.758819\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No 29316361 EUR 124 0 0.952738\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No 31514962 EUR 144 0 1.052764\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Profiling_Retargeting-1j_Webedia_PC Gamers / MMORPG Games Fans_No 29316362 EUR 388 0 2.459044\n2019/11/06 SQUARE ENIX 10307151 Square Enix_Display_IAB_Multi_Free-Trial Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No 29316368 EUR 2653 0 1.710639"
		String csvPreviewSecondLines = "2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Keywords_x_Google_Platinum Games 31418002 EUR 139 1 1.761529\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Profiling_Retargeting-1j_Webedia_PC Gamers / MMORPG Games Fans_No 31391192 EUR 30 0 0.228780\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No 31391193 EUR 288 2 0.451342\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No_Best Placement 31391194 EUR 145 0 2.262644\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No_Surpression Placement Test 31391195 EUR 87 1 2.595618\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Prospecting_Retargeting-30j_Webedia_Pack Gaming - IAB_Yes 31391198 EUR 3231 0 3.707637\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No 31391199 EUR 10123 0 4.321461\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Prospecting_x_Webedia_NieR:Automata Fans 31417996 EUR 1164 0 2.116019\n2019/11/06 SQUARE ENIX 11288564 Square Enix_Display_IAB_Multi_Opé-spéciale - Patch5.1 Opé-spéciale_IAB_Multi_Prospecting_x_Webedia_Pack Gaming - IAB_Yes 31391200 EUR 139 0 0.615344\n156030 38 127.449848"
		checkCsvPreview(csvPreviewInfo, csvPreviewFirstLines, csvPreviewSecondLines)
		String downloadedReportPath = ""
		if(GlobalVariable.downloadReport == true) {
			downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
			// We can only check xlsx files
			if(templateFormat.equals("xlsm")) {
				String fileName = downloadedReportPath.replaceAll(".*\\\\(.*)\\.xls.\$", "\$1")
				renameFile(downloadFolderPath, fileName + ".xlsm", fileName + ".xlsx")
				downloadedReportPath = downloadedReportPath.replaceFirst("xlsm", "xlsx")
			}
			checkReportDownloaded(downloadedReportPath, "9", dv360CsvExpectedContent)
			deleteFile(downloadedReportPath)
		}
		selenium.open(currentURL)
	}

	@Keyword
	public void manualUploadCm(String templateFormat) {
		selenium = getSelenium()
		String reportName = "Test auto - Manual Upload - CM Every 2 weeks"
		String advertiserName = "Test Advertiser"

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)
		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Manual Upload") == false) {
			createFolder("Test auto - Manual Upload")
		}

		createAndSetupReport("", reportName, advertiserName, templateFormat, "every 2 weeks", "CM Subreport", 4, cmCsvFullPath)

		String currentURL = WebUI.getUrl()
		String currentDate = generateCurrentDate()
		String csvPreviewInfo = "CSV of iteration " + currentDate + " - Total rows : 55"
		String csvPreviewFirstLines = "Campaign Campaign ID Placement Date Placement external ID Placement ID Impressions Clicks Click Rate Active View: Viewable Impressions Active View: Measurable Impressions Active View: Eligible Impressions Total Conversions View-through Conversions Click-through Conversions RM conversions : SG_ClickToDownload: Click-through Conversions RM conversions : SG_ClickToDownload: Total Conversions RM conversions : SG_ClickToDownload: View-through Conversions RM conversions : SG_FirstDeposit: Click-through Conversions RM conversions : SG_FirstDeposit: Total Conversions RM conversions : SG_FirstDeposit: View-through Conversions RM conversions : SG_FirstHandCasino: Click-through Conversions RM conversions : SG_FirstHandCasino: Total Conversions RM conversions : SG_FirstHandCasino: View-through Conversions RM conversions : SG_FirstHandPoker: Click-through Conversions RM conversions : SG_FirstHandPoker: Total Conversions RM conversions : SG_FirstHandPoker: View-through Conversions RM conversions : SG_FirstHandSports: Click-through Conversions RM conversions : SG_FirstHandSports: Total Conversions RM conversions : SG_FirstHandSports: View-through Conversions RM conversions : SG_Install: Click-through Conversions RM conversions : SG_Install: Total Conversions RM conversions : SG_Install: View-through Conversions RM conversions : SG_NewAccount: Click-through Conversions RM conversions : SG_NewAccount: Total Conversions RM conversions : SG_NewAccount: View-through Conversions RM conversions : SG_SubsequentDeposit: Click-through Conversions RM conversions : SG_SubsequentDeposit: Total Conversions RM conversions : SG_SubsequentDeposit: View-through Conversions\nBS FR ALWAYS ON 2019 22450063 BS-FR-AND-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X 2019-05-02 16044513 243731813 2 0 0.00 2 2 2 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nBS FR ALWAYS ON 2019 22450063 BS-FR-AND-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X 2019-05-09 16044513 243731813 1 0 0.00 1 1 1 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nBS FR ALWAYS ON 2019 22450063 BS-FR-AND-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X 2019-05-13 16044513 243731813 4 1 25.00 4 4 4 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nBS FR ALWAYS ON 2019 22450063 BS-FR-iOS-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X 2019-05-02 16044455 244058808 1 0 0.00 1 1 1 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nBS FR ALWAYS ON 2019 22450063 BS-FR-iOS-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X 2019-05-09 16044455 244058808 1 0 0.00 1 1 1 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nBS FR ALWAYS ON 2019 22450063 BS-FR-iOS-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X 2019-05-13 16044455 244058808 4 3 75.00 4 4 4 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X 2019-04-20 16043499 243869100 31235 48 0.15 16566 30490 30558 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X 2019-04-21 16043499 243869100 33139 89 0.27 17481 32182 32273 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X 2019-04-22 16043499 243869100 34701 66 0.19 18402 33926 34008 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X 2019-04-23 16043499 243869100 29444 56 0.19 14293 28617 28697 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00"
		String csvPreviewSecondLines = "PS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-P-X 2019-04-28 16043523 243869094 0 0 0.00 0 0 0 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X 2019-04-20 16044125 243869091 361 0 0.00 195 360 361 1.00 1.00 0.00 0.00 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X 2019-04-21 16044125 243869091 550 8 1.45 321 546 549 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X 2019-04-22 16044125 243869091 473 6 1.27 254 471 471 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X 2019-04-23 16044125 243869091 327 3 0.92 146 325 326 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X 2019-04-24 16044125 243869091 413 8 1.94 222 412 413 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X 2019-04-25 16044125 243869091 171 1 0.58 79 170 170 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X 2019-04-26 16044125 243869091 0 0 0.00 0 0 0 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00\nPS BR FRIENDS 2019 22504307 PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X 2019-04-30 16044125 243869091 0 0 0.00 0 0 0 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 1.00 1.00 0.00 0.00 0.00 0.00 0.00 0.00\nGrand Total: --- --- --- --- --- 94384605 87185 0.09 47399586 87196125 89461295 18578.00 17777.00 801.00 608.00 9682.00 9074.00 4.00 265.00 261.00 3.00 140.00 137.00 6.00 231.00 225.00 4.00 210.00 206.00 87.00 3362.00 3275.00 14.00 644.00 630.00 75.00 4044.00 3969.00"
		checkCsvPreview(csvPreviewInfo, csvPreviewFirstLines, csvPreviewSecondLines)
		String downloadedReportPath = ""
		if(GlobalVariable.downloadReport == true) {
			downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
			// We can only check xlsx files
			if(templateFormat.equals("xlsm")) {
				String fileName = downloadedReportPath.replaceAll(".*\\\\(.*)\\.xls.\$", "\$1")
				renameFile(downloadFolderPath, fileName + ".xlsm", fileName + ".xlsx")
				downloadedReportPath = downloadedReportPath.replaceFirst("xlsm", "xlsx")
			}
			checkReportDownloaded(downloadedReportPath, "10", cmCsvExpectedContent)
			deleteFile(downloadedReportPath)
		}
		selenium.open(currentURL)
	}

	@Keyword
	public void manualUploadGa(String templateFormat) {
		selenium = getSelenium()
		String reportName = "Test auto - Manual Upload - GA Daily"
		String advertiserName = "Test Advertiser"

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)
		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Manual Upload") == false) {
			createFolder("Test auto - Manual Upload")
		}

		createAndSetupReport("", reportName, advertiserName, templateFormat, "daily", "GA Subreport", 3, gaCsvFullPath)

		String currentURL = WebUI.getUrl()
		String currentDate = generateCurrentDate()
		String csvPreviewInfo = "CSV of iteration " + currentDate + " - Total rows : 38"
		String csvPreviewFirstLines = "Libellé d'événement Minute Nombre total d'événements Événements uniques Valeur de l'événement Valeur moyenne\ncode:RF_ERR1,email:debug.tradelab@gmail.com 05 43 8 0 0,00\ncode:RF_ERR1,email:debug.tradelab@gmail.com 20 26 3 0 0,00\ncode:RF_ERR1,email:debug.tradelab@gmail.com 50 25 1 0 0,00\ncode:RF_ERR1,email:debug.tradelab@gmail.com 15 21 1 0 0,00\ncode:RF_ERR1,email:debug.tradelab@gmail.com 10 19 0 0 0,00\ncode:RF_ERR1,email:debug.tradelab@gmail.com 00 7 1 0 0,00\ncode:RF_ERR1,email:debug.tradelab+59b210f119ab12817aaf44df4197aad4@gmail.com 05 7 5 0 0,00\ncode:RF_ERR1,email:debug.tradelab@gmail.com 30 6 0 0 0,00\ncode:RF_ERR1,email:debug.tradelab@gmail.com 55 5 1 0 0,00\ncode:RF_ERR1,email:debug.tradelab+59b210f119ab12817aaf44df4197aad4@gmail.com 10 5 2 0 0,00"
		String csvPreviewSecondLines = "code:RF_ERR1,email:debug.tradelab@gmail.com 01 1 0 0 0,00\ncode:RF_ERR1,email:debug.tradelab@gmail.com 44 1 1 0 0,00\ncode:RF_ERR1,email:debug.tradelab@gmail.com 45 1 1 0 0,00\ncode:RF_ERR1,email:debug.tradelab+59b210f119ab12817aaf44df4197aad4@gmail.com 15 1 0 0 0,00\ncode:RF_ERR1,email:debug.tradelab+59b210f119ab12817aaf44df4197aad4@gmail.com 20 1 0 0 0,00\ncode:RF_ERR1,email:debug.tradelab+5c3c7ee14bc02a0cb2f52a2be56a8c8d@gmail.com 20 1 1 0 0,00\ncode:RF_ERR1,email:debug.tradelab+5c3c7ee14bc02a0cb2f52a2be56a8c8d@gmail.com 50 1 0 0 0,00\ncode:RF_ERR1,email:debug.tradelab+eac2b4652e5b7c1a3f39e958ce264197@gmail.com 44 1 1 0 0,00\ncode:RF_ERR1,email:debug.tradelab+eac2b4652e5b7c1a3f39e958ce264197@gmail.com 49 1 1 0 0,00\ncode:RF_ERR1,email:debug.tradelab+eac2b4652e5b7c1a3f39e958ce264197@gmail.com 55 1 1 0 0,00"
		checkCsvPreview(csvPreviewInfo, csvPreviewFirstLines, csvPreviewSecondLines)
		String downloadedReportPath = ""
		if(GlobalVariable.downloadReport == true) {
			downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
			// We can only check xlsx files
			if(templateFormat.equals("xlsm")) {
				String fileName = downloadedReportPath.replaceAll(".*\\\\(.*)\\.xls.\$", "\$1")
				renameFile(downloadFolderPath, fileName + ".xlsm", fileName + ".xlsx")
				downloadedReportPath = downloadedReportPath.replaceFirst("xlsm", "xlsx")
			}
			checkReportDownloaded(downloadedReportPath, "11", gaCsvExpectedContent)
			deleteFile(downloadedReportPath)
		}
		selenium.open(currentURL)
	}

	@Keyword
	public void manualUploadEul(String templateFormat) {
		selenium = getSelenium()
		String reportName = "Test auto - Manual Upload - EUL Monthly"
		String advertiserName = "Test Advertiser"

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)
		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Manual Upload") == false) {
			createFolder("Test auto - Manual Upload")
		}

		createAndSetupReport("", reportName, advertiserName, templateFormat, "monthly", "EUL Subreport", 2, eulCsvFullPath)

		String currentURL = WebUI.getUrl()
		String currentDate = generateCurrentDate()
		String csvPreviewInfo = "CSV of iteration " + currentDate + " - Total rows : 33"
		String csvPreviewFirstLines = "nom impressions visiteurs uniques journaliers nouveaux VU nouveau VU/VU taux de rebond devis réel devis devis (post-clic) devis (post-impression) devis (revisite) ventes réelles ventes ventes (post-clic) ventes (revisite) ventes (post-impression) paniers commencés visites temps passé/visite impression visible taux de visibilité pourcentages d'impressions visibles pourcentage d'impressions non mesurées\nx1global 281837 12710 1637 25.56 % 47.59 % 224 224 19 203 2 87 87 7 2 78 59 26709 6m.4s. 1355 0.66 % 0.48 % 0.18 %\nx2Tradelab-Tradelab-Strat 281832 12676 1635 25.59 % 47.63 % 224 224 19 203 2 87 87 7 2 78 58 26653 6m.4s. 1355 0.66 % 0.48 % 0.17 %\nx3Tradelab-Tradelab-Teads-RG-Mobile 0 1 0 0% 0% 0 0 0 0 0 0 0 0 0 0 0 1 28s. 0 0% 0% 0%\nx4Tradelab-Tradelab-Strat-Device-Mobile 5 33 2 13.33 % 29.09 % 0 0 0 0 0 0 0 0 0 0 1 55 6m.39s. 0 60.00 % 0% 60.00 %\nx5global 281837 12710 1637 25.56 % 47.59 % 224 224 19 203 2 87 87 7 2 78 59 26709 6m.4s. 1355 0.66 % 0.48 % 0.18 %\nx6Tradelab-Tradelab-Strat 281832 12676 1635 25.59 % 47.63 % 224 224 19 203 2 87 87 7 2 78 58 26653 6m.4s. 1355 0.66 % 0.48 % 0.17 %\nx7global 281837 12710 1637 25.56 % 47.59 % 224 224 19 203 2 87 87 7 2 78 59 26709 6m.4s. 1355 0.66 % 0.48 % 0.18 %\nx8Tradelab-Tradelab-Teads-RG-Mobile 0 1 0 0% 0% 0 0 0 0 0 0 0 0 0 0 0 1 28s. 0 0% 0% 0%\nx9Tradelab-Tradelab-Strat-Device-Mobile 5 33 2 13.33 % 29.09 % 0 0 0 0 0 0 0 0 0 0 1 55 6m.39s. 0 60.00 % 0% 60.00 %\nx10global 281837 12710 1637 25.56 % 47.59 % 224 224 19 203 2 87 87 7 2 78 59 26709 6m.4s. 1355 0.66 % 0.48 % 0.18 %"
		String csvPreviewSecondLines = "z10global 281837 12710 1637 25.56 % 47.59 % 224 224 19 203 2 87 87 7 2 78 59 26709 6m.4s. 1355 0.66 % 0.48 % 0.18 %\nz9global 281837 12710 1637 25.56 % 47.59 % 224 224 19 203 2 87 87 7 2 78 59 26709 6m.4s. 1355 0.66 % 0.48 % 0.18 %\nz8Tradelab-Tradelab-Strat 281832 12676 1635 25.59 % 47.63 % 224 224 19 203 2 87 87 7 2 78 58 26653 6m.4s. 1355 0.66 % 0.48 % 0.17 %\nz7Tradelab-Tradelab-Teads-RG-Mobile 0 1 0 0% 0% 0 0 0 0 0 0 0 0 0 0 0 1 28s. 0 0% 0% 0%\nz6Tradelab-Tradelab-Strat-Device-Mobile 5 33 2 13.33 % 29.09 % 0 0 0 0 0 0 0 0 0 0 1 55 6m.39s. 0 60.00 % 0% 60.00 %\nz5global 281837 12710 1637 25.56 % 47.59 % 224 224 19 203 2 87 87 7 2 78 59 26709 6m.4s. 1355 0.66 % 0.48 % 0.18 %\nz4Tradelab-Tradelab-Strat 281832 12676 1635 25.59 % 47.63 % 224 224 19 203 2 87 87 7 2 78 58 26653 6m.4s. 1355 0.66 % 0.48 % 0.17 %\nz3Tradelab-Tradelab-Teads-RG-Mobile 0 1 0 0% 0% 0 0 0 0 0 0 0 0 0 0 0 1 28s. 0 0% 0% 0%\nz2Tradelab-Tradelab-Strat-Device-Mobile 5 33 2 13.33 % 29.09 % 0 0 0 0 0 0 0 0 0 0 1 55 6m.39s. 0 60.00 % 0% 60.00 %\nz1global 281837 12710 1637 25.56 % 47.59 % 224 224 19 203 2 87 87 7 2 78 59 26709 6m.4s. 1355 0.66 % 0.48 % 0.18 %"
		checkCsvPreview(csvPreviewInfo, csvPreviewFirstLines, csvPreviewSecondLines)
		String downloadedReportPath = ""
		if(GlobalVariable.downloadReport == true) {
			downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
			// We can only check xlsx files
			if(templateFormat.equals("xlsm")) {
				String fileName = downloadedReportPath.replaceAll(".*\\\\(.*)\\.xls.\$", "\$1")
				renameFile(downloadFolderPath, fileName + ".xlsm", fileName + ".xlsx")
				downloadedReportPath = downloadedReportPath.replaceFirst("xlsm", "xlsx")
			}
			checkReportDownloaded(downloadedReportPath, "12", eulCsvExpectedContent)
			deleteFile(downloadedReportPath)
		}
		selenium.open(currentURL)
	}

	private String createAndSetupReport(String currentUrl, String reportName, String advertiserName, String templateFormat, String scheduleFrequency, String subreportName, int datasourceNumber, String csvFullPath) {
		selenium = getSelenium()
		if(currentUrl.equals("") == false) {
			WebUI.navigateToUrl(currentUrl)
			WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)
		}

		createReport(reportName, advertiserName)
		createSubreport(subreportName, datasourceNumber)
		setupScheduleToday(scheduleFrequency)
		uploadReportTemplate(templateFormat, "Template")
		if(subreportName.equals("APN Subreport") == true) {
			setupReportXlsSettings("8")
		} else if(subreportName.equals("DV360 Subreport") == true) {
			setupReportXlsSettings("9")
			setupCsvSettings("1", "13", ",", false)
		} else if(subreportName.equals("CM Subreport") == true) {
			setupCsvSettings("46", "0", ",", false)
			setupReportXlsSettings("10")
		} else if(subreportName.equals("GA Subreport") == true) {
			setupReportXlsSettings("11")
		} else if(subreportName.equals("EUL Subreport") == true) {
			setupReportXlsSettings("12")
		}

		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		manualUpload(csvFullPath)

		return WebUI.getUrl()
	}

	private String modifyCurrentReport(String currentUrl, String subreportName, int datasourceNumber, String csvFullPath) {
		selenium = getSelenium()
		WebUI.navigateToUrl(currentUrl)

		createSubreport(subreportName, datasourceNumber)
		//changeScheduleDate(nthDateFromToday)

		setupCsvSettings("1", "13", ",", false)
		setupReportXlsSettings("8")
		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		manualUpload(csvFullPath)

		return WebUI.getUrl()
	}

	private void checkCsvPreview(String csvPreviewInfo, String csvPreviewFirstLines, String csvPreviewSecondLines) {
		WebUI.waitForElementPresent(findTestObject('Object Repository/3. Report/6. CSV Preview/CSV Preview info'), 30)

		assert WebUI.verifyEqual(selenium.getText("css=div.preview-info"), csvPreviewInfo) == true
		assert WebUI.verifyEqual(selenium.getText("css=table.preview-table:nth-of-type(1)"), csvPreviewFirstLines) == true
		if(csvPreviewSecondLines != "" && csvPreviewSecondLines != null) {
			assert WebUI.verifyElementPresent(findTestObject('Object Repository/3. Report/6. CSV Preview/CSV Preview spliter'), 5) == true
			assert WebUI.verifyEqual(selenium.getText("css=table.preview-table:nth-of-type(2)"), csvPreviewSecondLines) == true
		}
	}
}
