package testCases

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable
import reportfolio.Reportfolio

public class DeleteReport extends Reportfolio {

	private static String reportID = ""

	@Keyword
	public void deleteReport() {
		selenium = getSelenium()
		
		createFolder("Test auto - Delete Report")
		createReport("Report to Delete", "Test")
//		WebUI.click(findTestObject('2. Folders list/Open first folder button'))
//		int numberOfFolders = selenium.getCssCount("folders-list > folder-element").intValue()
//		numberOfFolders  = numberOfFolders + 2
//		int inc = 3
//		String reportType = ""
//		Thread.sleep(500);
//		boolean isReportPresent = selenium.isElementPresent("css=folders-list > folder-element:nth-child(3) > div > div.folder-content.open > div:nth-child(1) > span > a")
//		if (isReportPresent == true) reportType = selenium.getText("css=folders-list > folder-element:nth-child(" + inc.toString() + ") > div > div.folder-content.open > div:nth-child(1) > span > a > i")
//		while((isReportPresent == false || reportType != 'description') && inc < numberOfFolders + 2) {
//			clickObjectbyCss("folders-list > folder-element:nth-child(" + inc.toString() + ") > div > div.folder-header > button > span > i")
//			inc = inc + 1
//			clickObjectbyCss("folders-list > folder-element:nth-child(" + inc.toString() + ") > div > div.folder-header > button > span > i")
//			Thread.sleep(500);
//			isReportPresent = selenium.isElementPresent("css=folders-list > folder-element:nth-child(" + inc.toString() + ") > div > div.folder-content.open > div:nth-child(1) > span > a")
//			if (isReportPresent == true) reportType = selenium.getText("css=folders-list > folder-element:nth-child(" + inc.toString() + ") > div > div.folder-content.open > div:nth-child(1) > span > a > i")
//		}
//
//		WebUI.waitForElementVisible(findTestObject('2. Folders list/Select First Report button'), 30)
//		WebUI.click(findTestObject('2. Folders list/Select First Report button'))
//		WebUI.waitForElementVisible(findTestObject('3. Report/9. Delete Report/Delete Report button'), 30)
		String url = selenium.getLocation()
		reportID = url.split('\\/')[6]
		WebUI.click(findTestObject('3. Report/9. Delete Report/Delete Report button'))
		WebUI.click(findTestObject('Object Repository/3. Report/9. Delete Report/Delete Report Confirm button'))
		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("Report successfully deleted", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
	}

	@Keyword
	public void checkReportDeletedInDatabase() {
		selenium = getSelenium()
		String dbUrl = 'https://' + GlobalVariable.env + '-' + GlobalVariable.env2 + '.tradelab.fr' + "/tvflNDOqWgcZ/index.php?server=2&token=e01e8392215c0a5f4a3e5bbfcb54d0c5&old_usr=tradelab_dev"
		selenium.open(dbUrl)
		selenium.waitForPageToLoad("50")
		WebUI.sendKeys(findTestObject('Object Repository/9. Database/DB Username input'), GlobalVariable.dbLogin)
		Thread.sleep(500);
		WebUI.setEncryptedText(findTestObject('Object Repository/9. Database/DB Password input'), GlobalVariable.dbPassword)
		boolean isStaging = selenium.isElementPresent("id=select_server")
		if(isStaging == true) {
			WebUI.click(findTestObject('Object Repository/9. Database/Server select'))
			WebUI.click(findTestObject('Object Repository/9. Database/Second server'))
		}
		WebUI.click(findTestObject('Object Repository/9. Database/DB Login button'))
		WebUI.click(findTestObject('Object Repository/9. Database/SQL tab'))
		WebUI.waitForElementPresent(findTestObject('Object Repository/9. Database/SQL textarea'), 30)

		Thread.sleep(1500);
		WebUI.sendKeys(findTestObject('Object Repository/9. Database/SQL textarea'), "SELECT * FROM tradelab_dev.folio_document where folio_document_id = " + reportID + ";")
		WebUI.click(findTestObject('Object Repository/9. Database/Execute SQL button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/9. Database/Success message'), 30)
		String resultmysql = WebUI.getText(findTestObject('Object Repository/9. Database/Success message'))
		String result = resultmysql.split('\\.')[0]
		assert WebUI.verifyEqual(result, "MySQL a retourné un résultat vide (aucune ligne)") == true
		WebUI.refresh()
		WebUI.waitForElementVisible(findTestObject('Object Repository/9. Database/SQL tab'), 30)
		WebUI.click(findTestObject('Object Repository/9. Database/SQL tab'))
		WebUI.waitForElementPresent(findTestObject('Object Repository/9. Database/SQL textarea'), 30)

		Thread.sleep(1500);
		WebUI.sendKeys(findTestObject('Object Repository/9. Database/SQL textarea'), "SELECT * FROM tradelab_dev.folio_document_iteration where folio_document_id = " + reportID + ";")
		WebUI.click(findTestObject('Object Repository/9. Database/Execute SQL button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/9. Database/Success message'), 30)

		resultmysql = WebUI.getText(findTestObject('Object Repository/9. Database/Success message'))
		result = resultmysql.split('\\.')[0]
		assert WebUI.verifyEqual(result, "MySQL a retourné un résultat vide (aucune ligne)") == true
		WebUI.refresh()
		WebUI.waitForElementVisible(findTestObject('Object Repository/9. Database/SQL tab'), 30)
		WebUI.click(findTestObject('Object Repository/9. Database/SQL tab'))
		WebUI.waitForElementPresent(findTestObject('Object Repository/9. Database/SQL textarea'), 30)

		Thread.sleep(1500);
		WebUI.sendKeys(findTestObject('Object Repository/9. Database/SQL textarea'), "SELECT * FROM tradelab_dev.folio_subreport where folio_document_id = " + reportID + ";")
		WebUI.click(findTestObject('Object Repository/9. Database/Execute SQL button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/9. Database/Success message'), 30)
		resultmysql = WebUI.getText(findTestObject('Object Repository/9. Database/Success message'))
		result = resultmysql.split('\\.')[0]
		assert WebUI.verifyEqual(result, "MySQL a retourné un résultat vide (aucune ligne)") == true
		WebUI.refresh()
		WebUI.waitForElementVisible(findTestObject('Object Repository/9. Database/SQL tab'), 30)
		WebUI.click(findTestObject('Object Repository/9. Database/SQL tab'))
		WebUI.waitForElementPresent(findTestObject('Object Repository/9. Database/SQL textarea'), 30)

		Thread.sleep(1500);
		WebUI.sendKeys(findTestObject('Object Repository/9. Database/SQL textarea'), "SELECT * FROM tradelab_dev.folio_document_schedule where folio_document_id = " + reportID + ";")
		WebUI.click(findTestObject('Object Repository/9. Database/Execute SQL button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/9. Database/Success message'), 30)
		resultmysql = WebUI.getText(findTestObject('Object Repository/9. Database/Success message'))
		result = resultmysql.split('\\.')[0]
		assert WebUI.verifyEqual(result, "MySQL a retourné un résultat vide (aucune ligne)") == true

		WebUI.refresh()
		WebUI.waitForElementVisible(findTestObject('Object Repository/9. Database/SQL tab'), 30)
		WebUI.click(findTestObject('Object Repository/9. Database/SQL tab'))
		WebUI.waitForElementPresent(findTestObject('Object Repository/9. Database/SQL textarea'), 30)

		Thread.sleep(1500);
		WebUI.sendKeys(findTestObject('Object Repository/9. Database/SQL textarea'), "SELECT * FROM tradelab_dev.folio_document_user where folio_document_id = " + reportID + ";")
		WebUI.click(findTestObject('Object Repository/9. Database/Execute SQL button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/9. Database/Success message'), 30)

		resultmysql = WebUI.getText(findTestObject('Object Repository/9. Database/Success message'))
		result = resultmysql.split('\\.')[0]
		assert WebUI.verifyEqual(result, "MySQL a retourné un résultat vide (aucune ligne)") == true
	}
}
