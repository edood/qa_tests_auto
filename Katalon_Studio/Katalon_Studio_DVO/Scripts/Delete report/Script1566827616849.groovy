import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

import com.thoughtworks.selenium.Selenium
import org.openqa.selenium.firefox.FirefoxDriver
import org.stringtemplate.v4.compiler.STParser.element_return
import org.openqa.selenium.WebDriver
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium
import static org.junit.Assert.*
import java.util.regex.Pattern
import static org.apache.commons.lang3.StringUtils.join

String ENV_TEST = "staging"
String ENV_URL = "https://staging-reportfolio.tradelab.fr/"
String DB_URL = "https://staging-saas.tradelab.fr/tvflNDOqWgcZ/index.php?server=2&token=e01e8392215c0a5f4a3e5bbfcb54d0c5&old_usr=tradelab_dev"
String DB_ID = "tradelab_dev"
String DB_PWD = "9ayW0eAZNtl82oad"
String USER_ID = "canceau"
String USER_PWD = "anjou73!"

WebUI.openBrowser('https://www.katalon.com/')
def driver = DriverFactory.getWebDriver()
String baseUrl = "https://www.katalon.com/"
selenium = new WebDriverBackedSelenium(driver, baseUrl)
selenium.open("${ENV_URL}")
selenium.type("css=app-login > div > section > form > div:nth-child(2) > input", "${USER_ID}")
selenium.type("css=div > section > form > div:nth-child(3) > input", "${USER_PWD}")
selenium.click("css=app-login > div > section > form > button")

/*
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { 
		if (selenium.isVisible("css=folders-list > folder-element:nth-child(3) > div > div.folder-header > button:nth-child(5) > span > i"))
		{
			break;
		}
	} 
	catch (Exception e) 
	{
		
	}
	Thread.sleep(1000);
}*/
TestObject tmp = NULL;
int testlog = 4;

tmp = findTestObject("css=folders-list > folder-element:nth-child(3) > div > div.folder-header > button:nth-child(5) > span > i")
if (!tmp)
{
	printf("%d\n", testlog)
}
 
selenium.click("css=folders-list > folder-element:nth-child(3) > div > div.folder-header > button:nth-child(5) > span > i")
selenium.click("css=folders-list > folder-element:nth-child(3) > div > div.folder-content.open > div:nth-child(1) > span > a")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=div.folder-content.open > div > span > a")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

selenium.click("css=div.folder-content.open > div > span > a")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=document-setup > mat-card")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

String url = selenium.getLocation()
String reportID = selenium.getEval("var tab=storedVars['url'].split('\\/'); tab[6];")
selenium.click("css=app-document > section > header > button")
selenium.click("css=confirm-dialog > div.mat-dialog-actions > button.mat-raised-button.mat-accent")
selenium.click("css=mat-sidenav > div > button > div.mat-button-ripple.mat-ripple")
selenium.open("${DB_URL}")
selenium.type("css=fieldset > div:nth-child(2) > input", "${DB_ID}")
selenium.type("css=fieldset > div:nth-child(3) > input", "${DB_PWD}")
selenium.click("id=select_server")
selenium.click("css=#select_server > option:nth-child(2)")
selenium.click("css=fieldset.tblFooters > input:nth-child(1)")
selenium.click("css=#topmenu > li:nth-child(2) > a")
selenium.type("css=#sqlquerycontainerfull > div > div:nth-child(1) > textarea", "SELECT * FROM tradelab_dev.folio_document where folio_document_id = " + reportID + ";")
selenium.click("css=fieldset.tblFooters > input")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=div.sqlquery_message > div:nth-child(1) > div")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

resultmysql = selenium.getText("css=div.success")
result = selenium.getEval("var tab=storedVars['resultmysql'].split('.'); tab[0];")
verifyEquals("MySQL a retourné un résultat vide (aucune ligne)", selenium.getEval("\"MySQL a retourné un résultat vide (aucune ligne)\""));
selenium.click("css=#topmenu > li:nth-child(2) > a")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=#sqlquerycontainerfull > div > div:nth-child(1) > textarea")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

selenium.type("css=#sqlquerycontainerfull > div > div:nth-child(1) > textarea", "SELECT * FROM tradelab_dev.folio_document_iteration where folio_document_id = " + reportID + ";")
selenium.click("css=fieldset.tblFooters > input")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=div.sqlquery_message > div:nth-child(1) > div")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

resultmysql = selenium.getText("css=div.success")
result = selenium.getEval("var tab=storedVars['resultmysql'].split('.'); tab[0];")
verifyEquals("MySQL a retourné un résultat vide (aucune ligne)", selenium.getEval("\"MySQL a retourné un résultat vide (aucune ligne)\""));
selenium.click("css=#topmenu > li:nth-child(2) > a")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=#sqlquerycontainerfull > div > div:nth-child(1) > textarea")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

selenium.type("css=#sqlquerycontainerfull > div > div:nth-child(1) > textarea", "SELECT * FROM tradelab_dev.folio_subreport where folio_document_id = " + reportID + ";")
selenium.click("css=fieldset.tblFooters > input")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=div.sqlquery_message > div:nth-child(1) > div")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

resultmysql = selenium.getText("css=div.success")
result = selenium.getEval("var tab=storedVars['resultmysql'].split('.'); tab[0];")
verifyEquals("MySQL a retourné un résultat vide (aucune ligne)", selenium.getEval("\"MySQL a retourné un résultat vide (aucune ligne)\""));
selenium.click("css=#topmenu > li:nth-child(2) > a")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=#sqlquerycontainerfull > div > div:nth-child(1) > textarea")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

selenium.type("css=#sqlquerycontainerfull > div > div:nth-child(1) > textarea", "SELECT * FROM tradelab_dev.folio_document_schedule where folio_document_id = " + reportID + ";")
selenium.click("css=fieldset.tblFooters > input")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=div.sqlquery_message > div:nth-child(1) > div")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

resultmysql = selenium.getText("css=div.success")
result = selenium.getEval("var tab=storedVars['resultmysql'].split('.'); tab[0];")
verifyEquals("MySQL a retourné un résultat vide (aucune ligne)", selenium.getEval("\"MySQL a retourné un résultat vide (aucune ligne)\""));
selenium.click("css=#topmenu > li:nth-child(2) > a")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=#sqlquerycontainerfull > div > div:nth-child(1) > textarea")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

selenium.type("css=#sqlquerycontainerfull > div > div:nth-child(1) > textarea", "SELECT * FROM tradelab_dev.folio_document_user where folio_document_id = " + reportID + ";")
selenium.click("css=fieldset.tblFooters > input")
for (int second = 0;; second++) {
	if (second >= 60) fail("timeout");
	try { if (selenium.isVisible("css=div.sqlquery_message > div:nth-child(1) > div")) break; } catch (Exception e) {}
	Thread.sleep(1000);
}

resultmysql = selenium.getText("css=div.success")
result = selenium.getEval("var tab=storedVars['resultmysql'].split('.'); tab[0];")
verifyEquals("MySQL a retourné un résultat vide (aucune ligne)", selenium.getEval("\"MySQL a retourné un résultat vide (aucune ligne)\""));
