package testCases.customParams

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.Instant

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable
import reportfolio.Reportfolio

public class ExportCustomParams extends Reportfolio {

	// Case 1 & 2 - Simple export of several custom params from a model (include param mentionned twice)
	@Keyword
	public String exportCustomParamsFromModel () {
		selenium = getSelenium()
		driver = DriverFactory.getWebDriver()
		String randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))
		String modelName = "Model\u00A0with\u00A0custom\u00A0params\u00A0" + randomNumber
		String reportName = "Test auto - Report - Export custom params " + randomNumber
		String advertiserName = "Test Advertiser"
		String templateName = "Template Insert a Table with custom params"
		String templateFormat = "xlsx"
		String sheetNumber = "1"
		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Export custom params") == false) {
			createFolder("Test auto - Export custom params")
		}
		String toast = createModel(modelName, "daily", templateName, templateFormat, sheetNumber, "APN Subreport", 1, "1", "0", ",", false)
		assert toast == templateName + "." + templateFormat + " has been successfully uploaded.\n5 custom params found in the XLS template :\nDesktop, Mobile, Profiling, Prospection, Retargeting"
		String[] customParamsLabels = toast.replaceFirst(templateName + "." + templateFormat + " has been successfully uploaded.\n5 custom params found in the XLS template :\n","").split("\\, ")
		String customParamsValue = "Custom param value"
		createReportFromModelWithCustomParams(modelName, reportName, advertiserName, customParamsLabels, customParamsValue)

		setupScheduleToday("daily")
		addRecipients(["test_qa@tradelab.fr"])
		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		String currentTime = Instant.now().getEpochSecond()
		manualUpload(apnCsvFullPath)

		String currentDate = generateCurrentDate()
		String downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
		String sheetContent = apnCsvExpectedContent
		checkReportDownloaded(downloadedReportPath, sheetNumber, sheetContent)
		sheetContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K \n1	 	All	 	 	 	 	 	 	 	 	 \n2	 	 	 	 	 	 	 	 	 	 	 \n3	 	 	 	 	 	 	 	 	 	 	 \n4	 	Device	Revenue	Impressions	CPM	 	 	 	 	 	 \n5	 	Custom param value 2	15	0.0	15	 	 	 	 	Custom param value 2	 \n6	 	Custom param value 1	15	0.0	15	 	 	 	 	Custom param value 1	 \n7	 	 	 	 	 	 	 	 	 	 	 \n8	 	 	 	 	 	 	 	 	 	 	 \n9	 	Stratgie	Revenue	Impressions	CPM	 	 	 	 	 	 \n10	 	Custom param value 3	0.0	0.0	7	 	 	 	 	 	 \n11	 	Custom param value 4	0.0	0.0	7	 	 	 	 	 	 \n12	 	Custom param value 5	0.0	0.0	7	 	 	 	 	 	 \n13	 	 	 	 	 	 	 	 	 	 	 \n14	 	 	 	 	 	 	 	 	 	 	 \n15	 	 	 	 	 	 	 	 	 	 	 \n16	 	 	 	 	 	 	 	 	 	 	 \n17	 	 	 	 	 	 	 	 	 	 	 \n18	 	 	 	 	 	 	 	 	 	 	 \n19	 	 	 	 	 	 	 	 	 	 	 \n20	 	 	 	 	 	 	 	 	 	 	 \n21	 	 	 	 	 	 	 	 	 	 	 \n22	 	 	 	 	 	 	 	 	 	 	 \n23	 	 	 	 	 	 	 	 	 	 	 \n24	 	 	 	 	 	 	 	 	 	 	 \n25	 	 	 	 	 	 	 	 	 	 	 \n26	 	 	 	 	 	 	 	 	 	 	 \n27	 	 	 	 	 	 	 	 	 	 	 \n28	 	 	 	 	 	 	 	 	 	 	 \n29	 	 	 	 	 	 	 	 	 	 	 \n30	 	 	 	 	 	 	 	 	 	 	 \n31	 	 	 	 	 	 	 	 	 	 	 \n32	 	 	 	 	 	 	 	 	 	 	 \n33	 	 	 	 	 	 	 	 	 	 	 \n34	 	 	 	 	 	 	 	 	 	 	 \n35	 	 	 	 	 	 	 	 	 	 	 \n36	 	 	 	 	 	 	 	 	 	 	 \n37	 	 	 	 	 	 	 	 	 	 	 \n38	 	 	 	 	 	 	 	 	 	 	 \n39	 	 	 	 	 	 	 	 	 	 	 \n40	 	 	 	 	 	 	 	 	 	 	 \n41	 	 	 	 	 	 	 	 	 	 	 \n42	 	 	 	 	 	 	 	 	 	 	 \n43	 	 	 	 	 	 	 	 	 	 	 \n44	 	 	 	 	 	 	 	 	 	 	 \n45	 	 	 	 	 	 	 	 	 	 	 \n46	 	 	 	 	 	 	 	 	 	 	 \n47	 	 	 	 	 	 	 	 	 	 	 \n48	 	 	 	 	 	 	 	 	 	 	 \n49	 	 	 	 	 	 	 	 	 	 	 \n50	 	 	 	 	 	 	 	 	 	 	 \n51	 	 	 	 	 	 	 	 	 	 	 \n52	 	 	 	 	 	 	 	 	 	 	 \n53	 	 	 	 	 	 	 	 	 	 	 \n54	 	 	 	 	 	 	 	 	 	 	 \n55	 	 	 	 	 	 	 	 	 	 	 \n56	 	 	 	 	 	 	 	 	 	 	 \n57	 	 	 	 	 	 	 	 	 	 	 \n58	 	 	 	 	 	 	 	 	 	 	 \n59	 	 	 	 	 	 	 	 	 	 	 \n60	 	 	 	 	 	 	 	 	 	 	 \n61	 	 	 	 	 	 	 	 	 	 	 \n62	 	 	 	 	 	 	 	 	 	 	 \n63	 	 	 	 	 	 	 	 	 	 	 \n64	 	 	 	 	 	 	 	 	 	 	 \n65	 	 	 	 	 	 	 	 	 	 	 \n66	 	 	 	 	 	 	 	 	 	 	 \n67	 	 	 	 	 	 	 	 	 	 	 \n68	 	 	 	 	 	 	 	 	 	 	 \n69	 	 	 	 	 	 	 	 	 	 	 \n70	 	 	 	 	 	 	 	 	 	 	 \n71	 	 	 	 	 	 	 	 	 	 	 \n72	 	 	 	 	 	 	 	 	 	 	 \n73	 	 	 	 	 	 	 	 	 	 	 \n74	 	 	 	 	 	 	 	 	 	 	 \n75	 	 	 	 	 	 	 	 	 	 	 \n76	 	 	 	 	 	 	 	 	 	 	 \n77	 	 	DV360	 	 	 	 	 	 	 	 \n78	 	 	Appnexus	 	 	 	 	 	 	 	 \n79	 	 	All	 	 	 	 	 	 	 	 "
		checkReportDownloaded(downloadedReportPath, "6", sheetContent)
		String reportFullName = "( " + advertiserName + " ) " + reportName
		checkMailReceivedSince("test_qa@tradelab.fr", "K93tumSFjsvsrBGdV6dJBA==", "New report " + reportFullName + " from " + GlobalVariable.username, currentTime)
	}

	// Case 3 - Export of several custom params with empty value from a model
	@Keyword
	public String exportEmptyCustomParamsFromModel () {
		selenium = getSelenium()
		driver = DriverFactory.getWebDriver()
		WebUI.navigateToUrl(baseUrl)
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/First folder name'), 30)
		String randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))
		String modelName = "Model\u00A0with\u00A0custom\u00A0params\u00A0" + randomNumber
		String reportName = "Test auto - Report - Export custom params " + randomNumber
		String advertiserName = "Test Advertiser"
		String templateName = "Template Insert a Table with custom params"
		String templateFormat = "xlsx"
		String sheetNumber = "3"
		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Export custom params") == false) {
			createFolder("Test auto - Export custom params")
		}
		String toast = createModel(modelName, "daily", templateName, templateFormat, sheetNumber, "DV360 Subreport", 5, "1", "12", ",", false)
		assert toast == templateName + "." + templateFormat + " has been successfully uploaded.\n5 custom params found in the XLS template :\nDesktop, Mobile, Profiling, Prospection, Retargeting"
		String[] customParamsLabels = toast.replaceFirst(templateName + "." + templateFormat + " has been successfully uploaded.\n5 custom params found in the XLS template :\n","").split("\\, ")
		String customParamsValue = ""
		createReportFromModelWithCustomParams(modelName, reportName, advertiserName, customParamsLabels, customParamsValue)

		setupScheduleToday("daily")
		addRecipients(["test_qa@tradelab.fr"])
		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		String currentTime = Instant.now().getEpochSecond()
		manualUpload(dv360CsvFullPath)

		String currentDate = generateCurrentDate()
		String downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
		String sheetContent = dv360CsvExpectedContent
		checkReportDownloaded(downloadedReportPath, sheetNumber, sheetContent)
		sheetContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K \n1	 	All	 	 	 	 	 	 	 	 	 \n2	 	 	 	 	 	 	 	 	 	 	 \n3	 	 	 	 	 	 	 	 	 	 	 \n4	 	Device	Revenue	Impressions	CPM	 	 	 	 	 	 \n5	 	 	15	0.0	15	 	 	 	 	 	 \n6	 	 	15	0.0	15	 	 	 	 	 	 \n7	 	 	 	 	 	 	 	 	 	 	 \n8	 	 	 	 	 	 	 	 	 	 	 \n9	 	Stratgie	Revenue	Impressions	CPM	 	 	 	 	 	 \n10	 	 	0.0	0.0	7	 	 	 	 	 	 \n11	 	 	0.0	0.0	7	 	 	 	 	 	 \n12	 	 	0.0	0.0	7	 	 	 	 	 	 \n13	 	 	 	 	 	 	 	 	 	 	 \n14	 	 	 	 	 	 	 	 	 	 	 \n15	 	 	 	 	 	 	 	 	 	 	 \n16	 	 	 	 	 	 	 	 	 	 	 \n17	 	 	 	 	 	 	 	 	 	 	 \n18	 	 	 	 	 	 	 	 	 	 	 \n19	 	 	 	 	 	 	 	 	 	 	 \n20	 	 	 	 	 	 	 	 	 	 	 \n21	 	 	 	 	 	 	 	 	 	 	 \n22	 	 	 	 	 	 	 	 	 	 	 \n23	 	 	 	 	 	 	 	 	 	 	 \n24	 	 	 	 	 	 	 	 	 	 	 \n25	 	 	 	 	 	 	 	 	 	 	 \n26	 	 	 	 	 	 	 	 	 	 	 \n27	 	 	 	 	 	 	 	 	 	 	 \n28	 	 	 	 	 	 	 	 	 	 	 \n29	 	 	 	 	 	 	 	 	 	 	 \n30	 	 	 	 	 	 	 	 	 	 	 \n31	 	 	 	 	 	 	 	 	 	 	 \n32	 	 	 	 	 	 	 	 	 	 	 \n33	 	 	 	 	 	 	 	 	 	 	 \n34	 	 	 	 	 	 	 	 	 	 	 \n35	 	 	 	 	 	 	 	 	 	 	 \n36	 	 	 	 	 	 	 	 	 	 	 \n37	 	 	 	 	 	 	 	 	 	 	 \n38	 	 	 	 	 	 	 	 	 	 	 \n39	 	 	 	 	 	 	 	 	 	 	 \n40	 	 	 	 	 	 	 	 	 	 	 \n41	 	 	 	 	 	 	 	 	 	 	 \n42	 	 	 	 	 	 	 	 	 	 	 \n43	 	 	 	 	 	 	 	 	 	 	 \n44	 	 	 	 	 	 	 	 	 	 	 \n45	 	 	 	 	 	 	 	 	 	 	 \n46	 	 	 	 	 	 	 	 	 	 	 \n47	 	 	 	 	 	 	 	 	 	 	 \n48	 	 	 	 	 	 	 	 	 	 	 \n49	 	 	 	 	 	 	 	 	 	 	 \n50	 	 	 	 	 	 	 	 	 	 	 \n51	 	 	 	 	 	 	 	 	 	 	 \n52	 	 	 	 	 	 	 	 	 	 	 \n53	 	 	 	 	 	 	 	 	 	 	 \n54	 	 	 	 	 	 	 	 	 	 	 \n55	 	 	 	 	 	 	 	 	 	 	 \n56	 	 	 	 	 	 	 	 	 	 	 \n57	 	 	 	 	 	 	 	 	 	 	 \n58	 	 	 	 	 	 	 	 	 	 	 \n59	 	 	 	 	 	 	 	 	 	 	 \n60	 	 	 	 	 	 	 	 	 	 	 \n61	 	 	 	 	 	 	 	 	 	 	 \n62	 	 	 	 	 	 	 	 	 	 	 \n63	 	 	 	 	 	 	 	 	 	 	 \n64	 	 	 	 	 	 	 	 	 	 	 \n65	 	 	 	 	 	 	 	 	 	 	 \n66	 	 	 	 	 	 	 	 	 	 	 \n67	 	 	 	 	 	 	 	 	 	 	 \n68	 	 	 	 	 	 	 	 	 	 	 \n69	 	 	 	 	 	 	 	 	 	 	 \n70	 	 	 	 	 	 	 	 	 	 	 \n71	 	 	 	 	 	 	 	 	 	 	 \n72	 	 	 	 	 	 	 	 	 	 	 \n73	 	 	 	 	 	 	 	 	 	 	 \n74	 	 	 	 	 	 	 	 	 	 	 \n75	 	 	 	 	 	 	 	 	 	 	 \n76	 	 	 	 	 	 	 	 	 	 	 \n77	 	 	DV360	 	 	 	 	 	 	 	 \n78	 	 	Appnexus	 	 	 	 	 	 	 	 \n79	 	 	All	 	 	 	 	 	 	 	 "
		checkReportDownloaded(downloadedReportPath, "6", sheetContent)
		String reportFullName = "( " + advertiserName + " ) " + reportName
		checkMailReceivedSince("test_qa@tradelab.fr", "K93tumSFjsvsrBGdV6dJBA==", "New report " + reportFullName + " from " + GlobalVariable.username, currentTime)
	}

	// Case 4 & 5 - Replace template in a basic report with new custom params
	@Keyword
	public String replaceTemplateWithNewCustomParams () {
		selenium = getSelenium()
		driver = DriverFactory.getWebDriver()
		WebUI.navigateToUrl(baseUrl)
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/First folder name'), 30)
		String randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))
		String modelName = "Model\u00A0with\u00A0custom\u00A0params\u00A0" + randomNumber
		String reportName = "Test auto - Report - Export custom params " + randomNumber
		String advertiserName = "Test Advertiser"
		String templateName = "Template Insert a Table with custom params"
		String templateFormat = "xlsx"
		String sheetNumber = "3"
		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Export custom params") == false) {
			createFolder("Test auto - Export custom params")
		}
		createReport(reportName, advertiserName)
		setupScheduleToday("daily")
		createSubreport("DV360 Subreport", 5)
		// First template
		uploadReportTemplate(templateFormat, templateName)
		setupReportXlsSettings(sheetNumber)
		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		String currentTime = Instant.now().getEpochSecond()
		manualUpload(dv360CsvFullPath)

		String currentReportUrl = WebUI.getUrl()

		String currentDate = generateCurrentDate()
		String downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
		String sheetContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L \n1	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No	29316317.0	EUR	982.0	1.0	0.602487	 	 \n2	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No	31514957.0	EUR	2678.0	0.0	2.183799	 	 \n3	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No_CPA	29316319.0	EUR	13858.0	2.0	10.666706	 	 \n4	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_Retargeting-6h_Google_Final Fantasy Fans_No	29316321.0	EUR	35.0	0.0	0.436678	 	 \n5	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_Retargeting-7j_Google_FFXIV Interest_No	29316349.0	EUR	284.0	0.0	1.143764	 	 \n6	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_x_Google_Final Fantasy Fans_No	29316353.0	EUR	6672.0	0.0	3.758819	 	 \n7	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No	29316361.0	EUR	124.0	0.0	0.952738	 	 \n8	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No	31514962.0	EUR	144.0	0.0	1.052764	 	 \n9	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1j_Webedia_PC Gamers / MMORPG Games Fans_No	29316362.0	EUR	388.0	0.0	2.459044	 	 \n10	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No	29316368.0	EUR	2653.0	0.0	1.710639	 	 \n11	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No_Surpression Placement Test	30731876.0	EUR	12.0	0.0	0.009859	 	 \n12	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_J-RPG and RPG Fans_No	29316372.0	EUR	6228.0	0.0	10.588579	 	 \n13	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_J-RPG and RPG Fans_No - Meet or beat	30087396.0	EUR	4435.0	0.0	0.922908	 	 \n14	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_Retargeting-30j_Webedia_Pack Gaming - IAB_Yes	29320180.0	EUR	6591.0	4.0	7.095674	 	 \n15	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No	31514950.0	EUR	7589.0	1.0	10.288911	 	 \n16	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No_CPA	29316367.0	EUR	37764.0	3.0	7.323307	 	 \n17	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_Pack Gaming - IAB_Yes	29320181.0	EUR	727.0	0.0	0.60003	 	 \n18	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	Boutique_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No	29543363.0	EUR	11936.0	2.0	5.494966	 	 \n19	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	Boutique_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No	29543364.0	EUR	1543.0	0.0	0.917072	 	 \n20	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	 	 	 	 	 	 	 	 "
		checkReportDownloaded(downloadedReportPath, sheetNumber, sheetContent)
		sheetContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K \n1	 	All	 	 	 	 	 	 	 	 	 \n2	 	 	 	 	 	 	 	 	 	 	 \n3	 	 	 	 	 	 	 	 	 	 	 \n4	 	Device	Revenue	Impressions	CPM	 	 	 	 	 	 \n5	 	 	15	0.0	15	 	 	 	 	 	 \n6	 	 	15	0.0	15	 	 	 	 	 	 \n7	 	 	 	 	 	 	 	 	 	 	 \n8	 	 	 	 	 	 	 	 	 	 	 \n9	 	Stratgie	Revenue	Impressions	CPM	 	 	 	 	 	 \n10	 	 	0.0	0.0	7	 	 	 	 	 	 \n11	 	 	0.0	0.0	7	 	 	 	 	 	 \n12	 	 	0.0	0.0	7	 	 	 	 	 	 \n13	 	 	 	 	 	 	 	 	 	 	 \n14	 	 	 	 	 	 	 	 	 	 	 \n15	 	 	 	 	 	 	 	 	 	 	 \n16	 	 	 	 	 	 	 	 	 	 	 \n17	 	 	 	 	 	 	 	 	 	 	 \n18	 	 	 	 	 	 	 	 	 	 	 \n19	 	 	 	 	 	 	 	 	 	 	 \n20	 	 	 	 	 	 	 	 	 	 	 \n21	 	 	 	 	 	 	 	 	 	 	 \n22	 	 	 	 	 	 	 	 	 	 	 \n23	 	 	 	 	 	 	 	 	 	 	 \n24	 	 	 	 	 	 	 	 	 	 	 \n25	 	 	 	 	 	 	 	 	 	 	 \n26	 	 	 	 	 	 	 	 	 	 	 \n27	 	 	 	 	 	 	 	 	 	 	 \n28	 	 	 	 	 	 	 	 	 	 	 \n29	 	 	 	 	 	 	 	 	 	 	 \n30	 	 	 	 	 	 	 	 	 	 	 \n31	 	 	 	 	 	 	 	 	 	 	 \n32	 	 	 	 	 	 	 	 	 	 	 \n33	 	 	 	 	 	 	 	 	 	 	 \n34	 	 	 	 	 	 	 	 	 	 	 \n35	 	 	 	 	 	 	 	 	 	 	 \n36	 	 	 	 	 	 	 	 	 	 	 \n37	 	 	 	 	 	 	 	 	 	 	 \n38	 	 	 	 	 	 	 	 	 	 	 \n39	 	 	 	 	 	 	 	 	 	 	 \n40	 	 	 	 	 	 	 	 	 	 	 \n41	 	 	 	 	 	 	 	 	 	 	 \n42	 	 	 	 	 	 	 	 	 	 	 \n43	 	 	 	 	 	 	 	 	 	 	 \n44	 	 	 	 	 	 	 	 	 	 	 \n45	 	 	 	 	 	 	 	 	 	 	 \n46	 	 	 	 	 	 	 	 	 	 	 \n47	 	 	 	 	 	 	 	 	 	 	 \n48	 	 	 	 	 	 	 	 	 	 	 \n49	 	 	 	 	 	 	 	 	 	 	 \n50	 	 	 	 	 	 	 	 	 	 	 \n51	 	 	 	 	 	 	 	 	 	 	 \n52	 	 	 	 	 	 	 	 	 	 	 \n53	 	 	 	 	 	 	 	 	 	 	 \n54	 	 	 	 	 	 	 	 	 	 	 \n55	 	 	 	 	 	 	 	 	 	 	 \n56	 	 	 	 	 	 	 	 	 	 	 \n57	 	 	 	 	 	 	 	 	 	 	 \n58	 	 	 	 	 	 	 	 	 	 	 \n59	 	 	 	 	 	 	 	 	 	 	 \n60	 	 	 	 	 	 	 	 	 	 	 \n61	 	 	 	 	 	 	 	 	 	 	 \n62	 	 	 	 	 	 	 	 	 	 	 \n63	 	 	 	 	 	 	 	 	 	 	 \n64	 	 	 	 	 	 	 	 	 	 	 \n65	 	 	 	 	 	 	 	 	 	 	 \n66	 	 	 	 	 	 	 	 	 	 	 \n67	 	 	 	 	 	 	 	 	 	 	 \n68	 	 	 	 	 	 	 	 	 	 	 \n69	 	 	 	 	 	 	 	 	 	 	 \n70	 	 	 	 	 	 	 	 	 	 	 \n71	 	 	 	 	 	 	 	 	 	 	 \n72	 	 	 	 	 	 	 	 	 	 	 \n73	 	 	 	 	 	 	 	 	 	 	 \n74	 	 	 	 	 	 	 	 	 	 	 \n75	 	 	 	 	 	 	 	 	 	 	 \n76	 	 	 	 	 	 	 	 	 	 	 \n77	 	 	DV360	 	 	 	 	 	 	 	 \n78	 	 	Appnexus	 	 	 	 	 	 	 	 \n79	 	 	All	 	 	 	 	 	 	 	 "
		checkReportDownloaded(downloadedReportPath, "6", sheetContent)
		String reportFullName = "( " + advertiserName + " ) " + reportName
		WebUI.navigateToUrl(currentReportUrl)
		WebUI.waitForPageLoad(30)
		// Replace template
		templateName = "Template Insert a Table with custom params 2"
		sheetNumber = "2"
		randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))
		renameReport(reportName.replaceFirst("\\d+", randomNumber), advertiserName)
		reportName = reportName.replaceFirst("\\d+", randomNumber)
		setupReportXlsSettings(sheetNumber)
		uploadReportTemplate(templateFormat, templateName)
		WebUI.delay(10)
		downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
		sheetContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L \n1	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No	29316317.0	EUR	982.0	1.0	0.602487	 	 \n2	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No	31514957.0	EUR	2678.0	0.0	2.183799	 	 \n3	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No_CPA	29316319.0	EUR	13858.0	2.0	10.666706	 	 \n4	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_Retargeting-6h_Google_Final Fantasy Fans_No	29316321.0	EUR	35.0	0.0	0.436678	 	 \n5	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_Retargeting-7j_Google_FFXIV Interest_No	29316349.0	EUR	284.0	0.0	1.143764	 	 \n6	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_x_Google_Final Fantasy Fans_No	29316353.0	EUR	6672.0	0.0	3.758819	 	 \n7	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No	29316361.0	EUR	124.0	0.0	0.952738	 	 \n8	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No	31514962.0	EUR	144.0	0.0	1.052764	 	 \n9	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1j_Webedia_PC Gamers / MMORPG Games Fans_No	29316362.0	EUR	388.0	0.0	2.459044	 	 \n10	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No	29316368.0	EUR	2653.0	0.0	1.710639	 	 \n11	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No_Surpression Placement Test	30731876.0	EUR	12.0	0.0	0.009859	 	 \n12	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_J-RPG and RPG Fans_No	29316372.0	EUR	6228.0	0.0	10.588579	 	 \n13	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_J-RPG and RPG Fans_No - Meet or beat	30087396.0	EUR	4435.0	0.0	0.922908	 	 \n14	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_Retargeting-30j_Webedia_Pack Gaming - IAB_Yes	29320180.0	EUR	6591.0	4.0	7.095674	 	 \n15	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No	31514950.0	EUR	7589.0	1.0	10.288911	 	 \n16	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No_CPA	29316367.0	EUR	37764.0	3.0	7.323307	 	 \n17	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_Pack Gaming - IAB_Yes	29320181.0	EUR	727.0	0.0	0.60003	 	 \n18	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	Boutique_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No	29543363.0	EUR	11936.0	2.0	5.494966	 	 \n19	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	Boutique_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No	29543364.0	EUR	1543.0	0.0	0.917072	 	 \n20	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	 	 	 	 	 	 	 	 "
		checkReportDownloaded(downloadedReportPath, sheetNumber, sheetContent)
		sheetContent = " 	A 	B 	C 	D 	E 	F 	G \n1	 	 	 	 	 	 	 \n2	 	 	 	 	 	 	 \n3	 	 	 	 	 	 	 \n4	 	 	 	 	 	 	 \n5	 	 	 	 	 	 	 \n6	 	 	 	 	 	#@test blank space2@#	 \n7	 	 	 	 	 	 	 \n8	 	 	 	 	 	 	 \n9	 	 	 	 	 	 	 \n10	 	 	 	 	 	 	 \n11	 	 	 	 	 	 	 \n12	 	 	 	 	 	 	 \n13	 	 	te	 	 	 	 \n14	 	 	 	 	 	 	 \n15	 	 	 	 	 	 	 \n16	 	 	 	 	 	 	 \n17	 	 	 	 	 	 	 \n18	 	 	 	 	 	 	 "
		checkReportDownloaded(downloadedReportPath, "3", sheetContent)
	}

	// Case 6 - Replace template from a model with new custom params
	@Keyword
	public String replaceTemplateFromModelWithNewCustomParams () {
		selenium = getSelenium()
		driver = DriverFactory.getWebDriver()
		WebUI.navigateToUrl(baseUrl)
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/First folder name'), 30)
		String randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))
		String modelName = "Model\u00A0with\u00A0custom\u00A0params\u00A0" + randomNumber
		String reportName = "Test auto - Report - Export custom params " + randomNumber
		String advertiserName = "Test Advertiser"
		String templateName = "Template Insert a Table with custom params"
		String templateFormat = "xlsx"
		String sheetNumber = "3"
		if(WebUI.getText(findTestObject('Object Repository/2. Folders list/First folder name')).equals("Test auto - Export custom params") == false) {
			createFolder("Test auto - Export custom params")
		}

		String toast = createModel(modelName, "daily", templateName, templateFormat, sheetNumber, "DV360 Subreport", 5, "1", "12", ",", false)
		assert toast == templateName + "." + templateFormat + " has been successfully uploaded.\n5 custom params found in the XLS template :\nDesktop, Mobile, Profiling, Prospection, Retargeting"
		String[] customParamsLabels = toast.replaceFirst(templateName + "." + templateFormat + " has been successfully uploaded.\n5 custom params found in the XLS template :\n","").split("\\, ")
		String customParamsValue = "Custom param value"
		createReportFromModelWithCustomParams(modelName, reportName, advertiserName, customParamsLabels, customParamsValue)

		setupScheduleToday("daily")
		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		String currentTime = Instant.now().getEpochSecond()
		manualUpload(dv360CsvFullPath)

		// Replace template
		templateName = "Template Insert a Table with custom params 2"
		sheetNumber = "2"
		randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))
		renameReport(reportName.replaceFirst("\\d+", randomNumber), advertiserName)
		reportName = reportName.replaceFirst("\\d+", randomNumber)
		setupReportXlsSettings(sheetNumber)
		uploadReportTemplate(templateFormat, templateName)
		WebUI.delay(10)
		String currentDate = generateCurrentDate()
		String downloadedReportPath = downloadReport(reportName, advertiserName, templateFormat, currentDate)
		String sheetContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L \n1	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No	29316317.0	EUR	982.0	1.0	0.602487	 	 \n2	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No	31514957.0	EUR	2678.0	0.0	2.183799	 	 \n3	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No_CPA	29316319.0	EUR	13858.0	2.0	10.666706	 	 \n4	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_Retargeting-6h_Google_Final Fantasy Fans_No	29316321.0	EUR	35.0	0.0	0.436678	 	 \n5	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_Retargeting-7j_Google_FFXIV Interest_No	29316349.0	EUR	284.0	0.0	1.143764	 	 \n6	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_x_Google_Final Fantasy Fans_No	29316353.0	EUR	6672.0	0.0	3.758819	 	 \n7	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No	29316361.0	EUR	124.0	0.0	0.952738	 	 \n8	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No	31514962.0	EUR	144.0	0.0	1.052764	 	 \n9	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1j_Webedia_PC Gamers / MMORPG Games Fans_No	29316362.0	EUR	388.0	0.0	2.459044	 	 \n10	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No	29316368.0	EUR	2653.0	0.0	1.710639	 	 \n11	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No_Surpression Placement Test	30731876.0	EUR	12.0	0.0	0.009859	 	 \n12	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_J-RPG and RPG Fans_No	29316372.0	EUR	6228.0	0.0	10.588579	 	 \n13	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_J-RPG and RPG Fans_No - Meet or beat	30087396.0	EUR	4435.0	0.0	0.922908	 	 \n14	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_Retargeting-30j_Webedia_Pack Gaming - IAB_Yes	29320180.0	EUR	6591.0	4.0	7.095674	 	 \n15	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No	31514950.0	EUR	7589.0	1.0	10.288911	 	 \n16	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No_CPA	29316367.0	EUR	37764.0	3.0	7.323307	 	 \n17	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_Pack Gaming - IAB_Yes	29320181.0	EUR	727.0	0.0	0.60003	 	 \n18	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	Boutique_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No	29543363.0	EUR	11936.0	2.0	5.494966	 	 \n19	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	Boutique_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No	29543364.0	EUR	1543.0	0.0	0.917072	 	 \n20	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	 	 	 	 	 	 	 	 "
		checkReportDownloaded(downloadedReportPath, sheetNumber, sheetContent)
		sheetContent = " 	A 	B 	C 	D 	E 	F 	G \n1	 	 	 	 	 	 	 \n2	 	 	 	 	 	 	 \n3	 	 	 	 	 	 	 \n4	 	 	 	 	 	 	 \n5	 	 	 	 	 	 	 \n6	 	 	 	 	 	#@test blank space2@#	 \n7	 	 	 	 	 	 	 \n8	 	 	 	 	 	 	 \n9	 	 	 	 	 	 	 \n10	 	 	 	 	 	 	 \n11	 	 	 	 	 	 	 \n12	 	 	 	 	 	 	 \n13	 	 	te	 	 	 	 \n14	 	 	 	 	 	 	 \n15	 	 	 	 	 	 	 \n16	 	 	 	 	 	 	 \n17	 	 	 	 	 	 	 \n18	 	 	 	 	 	 	 "
		checkReportDownloaded(downloadedReportPath, "3", sheetContent)
	}
}
