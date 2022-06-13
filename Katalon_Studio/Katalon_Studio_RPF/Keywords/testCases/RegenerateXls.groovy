package testCases

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject


import org.openqa.selenium.Keys

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.Instant

import internal.GlobalVariable
import reportfolio.Reportfolio

public class RegenerateXls extends Reportfolio {

	@Keyword
	public void regenerateXls() {
		selenium = getSelenium()
		String templateFullPath = csvAndTemplateFolderPath + "Template.xlsx"
		String templateXLSMFullPath = csvAndTemplateFolderPath + "Template.xlsm"
		String templateInsertTableFullPath = csvAndTemplateFolderPath + "Template Insert a Table.xlsx"
		String reportName = "Test auto - Report - Regenerate XLS - Report Creation"
		String advertiserName = "Test Advertiser"

		selenium.openWindow("","")
		WebUI.switchToWindowIndex(1)
		loginToGoogle()
		WebUI.delay(2)
		WebUI.navigateToUrl("https://mail.google.com/mail/u/0/#search/from%3A(reportfolio%40tradelab.fr)+subject%3A(New+report+(+Test+Advertiser+)+Test+Auto+-+Regenerate+XLS)+newer_than%3A1d")
		WebUI.waitForPageLoad(30)
		WebUI.navigateToUrl("https://mail.google.com/mail/u/0/#search/from%3A(reportfolio%40tradelab.fr)+subject%3A(New+report+(+Test+Advertiser+)+Test+Auto+-+Regenerate+XLS)+newer_than%3A1d")
		WebUI.delay(5)
		WebUI.waitForElementNotVisible(findTestObject('Object Repository/7. Gmail/Loading'), 30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/7. Gmail/Check all mails'), 45)
		assert WebUI.getAttribute(findTestObject('Object Repository/7. Gmail/Search bar input'), "value").equals("from:(reportfolio@tradelab.fr) subject:(New report ( Test Advertiser ) Test Auto - Regenerate XLS) newer_than:1d ") == true
		if(selenium.isElementPresent(getSelector(findTestObject('7. Gmail/Mail 1 Title'))) == true){
			WebUI.click(findTestObject('Object Repository/7. Gmail/Check all mails'))
			WebUI.waitForElementVisible(findTestObject('Object Repository/7. Gmail/Remove mail button'), 45)
			WebUI.click(findTestObject('Object Repository/7. Gmail/Remove mail button'))
		}

		WebUI.switchToWindowIndex(0)
		String currentTime = Instant.now().getEpochSecond()
		createFolder("Test auto - Regenerate XLS")
		createReport(reportName, advertiserName)
		createSubreport("APN Subreport", 1)
		setupScheduleToday("daily")
		uploadReportTemplate("xlsx", "Template")
		setupReportXlsSettings("8")
		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		manualUpload(apnCsvFullPath)
		WebUI.delay(15)
		reportName = "Test auto - Report - Regenerate XLS - First renaming"
		renameReport(reportName, advertiserName)


		WebUI.switchToWindowIndex(1)
		String reportCreationMailTitle = "New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Report Creation from " + GlobalVariable.username
		String reportRenamingMailTitle = "New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - First renaming from " + GlobalVariable.username

		WebUI.navigateToUrl("https://mail.google.com/mail/u/0/#search/from%3A(reportfolio%40tradelab.fr)+subject%3A(New+report)+after%3A"+ currentTime)
		WebUI.waitForPageLoad(30)
		WebUI.delay(5)
		WebUI.waitForElementVisible(findTestObject('Object Repository/7. Gmail/Check all mails'), 45)

		int remainingAttempts = 30
		while(selenium.isElementPresent(getSelector(findTestObject('7. Gmail/Mail 2 Title'))) == false  && remainingAttempts > 0) {
			WebUI.click(findTestObject('Object Repository/7. Gmail/Refresh mails button'))
			Thread.sleep(1000)
			remainingAttempts = remainingAttempts - 1
		}

		remainingAttempts = 30
		String firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))
		String secondMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))
		while((firstMailTitle != reportRenamingMailTitle || secondMailTitle != reportCreationMailTitle) && remainingAttempts > 0) {
			WebUI.click(findTestObject('Object Repository/7. Gmail/Refresh mails button'))
			Thread.sleep(1500)
			firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))
			secondMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))
			remainingAttempts = remainingAttempts - 1
		}
		assert WebUI.verifyEqual(reportCreationMailTitle, WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))) == true
		assert WebUI.verifyEqual(reportRenamingMailTitle, WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))) == true
		Thread.sleep(2500)
		WebUI.focus(findTestObject('7. Gmail/Mail 1 Title'))
		WebUI.focus(findTestObject('7. Gmail/Mail 2 Title'))
		Thread.sleep(2500)
		WebUI.switchToWindowIndex(0)
		WebUI.waitForElementPresent(findTestObject('Object Repository/3. Report/Edit Report Name button'), 30)

		String regex = "nth-child\\(\\d+\\)"
		int remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Template change XLSX") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Template change XLSX")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(10)
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Remove template button'))
		WebUI.waitForElementNotPresent(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'), 30)

		WebUI.uploadFile(findTestObject('Object Repository/3. Report/3. XLS Settings/Upload Template input'), templateFullPath)
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'), 30)

		checkLastMailReceived("New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Template change XLSX from " + GlobalVariable.username, 30)

		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Template change XLSM") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Template change XLSM")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(5)
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Remove template button'))
		WebUI.waitForElementNotPresent(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'), 30)

		WebUI.uploadFile(findTestObject('Object Repository/3. Report/3. XLS Settings/Upload Template input'), templateXLSMFullPath)
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'), 30)

		checkLastMailReceived("New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Template change XLSM from " + GlobalVariable.username, 30)

		WebUI.delay(10)
		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Fail change template") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Fail change template")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(7)
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Remove template button'))
		WebUI.waitForElementNotPresent(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'), 30)
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/3. XLS Settings/Upload Template input'), apnCsvFullPath)
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/Toast error message'), 30)
		WebUI.delay(10)

		String regenerationMailTitle = "New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Fail change template from " + GlobalVariable.username
		WebUI.switchToWindowIndex(1)
		remainingAttempts = 30
		firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))
		secondMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))
		String firstMailNumberPresentSelector = findTestObject('7. Gmail/Mail 1 number').getSelectorCollection().get(SelectorMethod.CSS)
		String firstMailNumber = ""
		boolean isfirstMailNumberPresent = selenium.isElementPresent("css=" + firstMailNumberPresentSelector)

		while((firstMailTitle != regenerationMailTitle || secondMailTitle == regenerationMailTitle || isfirstMailNumberPresent == true) && remainingAttempts > 0) {
			WebUI.click(findTestObject('Object Repository/7. Gmail/Refresh mails button'))
			Thread.sleep(1500)
			firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))
			secondMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))
			isfirstMailNumberPresent = selenium.isElementPresent("css=" + firstMailNumberPresentSelector)
			remainingAttempts = remainingAttempts - 1
		}
		assert WebUI.verifyNotEqual(regenerationMailTitle, WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))) == true
		assert WebUI.verifyEqual(regenerationMailTitle, WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))) == true
		assert WebUI.verifyElementNotPresent(findTestObject('7. Gmail/Mail 1 number'), 5) == true
		Thread.sleep(2500)
		WebUI.focus(findTestObject('7. Gmail/Mail 1 Title'))
		WebUI.focus(findTestObject('7. Gmail/Mail 2 Title'))
		Thread.sleep(2500)
		WebUI.switchToWindowIndex(0)
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/4. Subreports/New Subreport button'), 30)

		WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/New Subreport button'))
		WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/Datasources/Datasource 1'))
		WebUI.waitForElementNotPresent(findTestObject('Object Repository/3. Report/4. Subreports/Datasources/Datasource 1'), 5)
		WebUI.delay(1)
		WebUI.waitForElementPresent(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport button'), "nth-child\\(1\\)", 'nth-child(2)'), 30)
		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Subreport name'), "nth-child\\(1\\)", 'nth-child(2)')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Delete subreport") == false) {
			WebUI.click(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport button'), "nth-child\\(1\\)", 'nth-child(2)'))
			WebUI.delay(1)
			WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport input'), "nth-child\\(1\\)", 'nth-child(2)'), "APN Subreport 2")
			WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport input'), "nth-child\\(1\\)", 'nth-child(2)'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/3. XLS Settings/Upload Template input'), templateInsertTableFullPath)
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'), 30)
		WebUI.delay(2)

		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'))
		WebUI.clearText(changeSelector(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "nth-of-type\\(1\\)", 'nth-of-type(2)'))
		WebUI.delay(1)
		WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "nth-of-type\\(1\\)", 'nth-of-type(2)'), "9")
		WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "nth-of-type\\(1\\)", 'nth-of-type(2)'), Keys.chord(Keys.ENTER))
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Save button'))
		WebUI.delay(2)
		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/6. CSV Preview/Upload CSV input'), apnCsvFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/6. CSV Preview/CSV Preview info'), 30)

		WebUI.delay(10)
		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Delete subreport") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Delete subreport")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(10)
		WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/Delete subreport menu'))
		WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/Delete subreport button'))
		WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/Confirm subreport deletion button'))
		WebUI.delay(5)

		checkLastMailReceived("New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Delete subreport from " + GlobalVariable.username, 30)

		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Start from row") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Start from row")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(5)
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/CSV Settings button'))
		WebUI.setText(findTestObject('Object Repository/3. Report/5. CSV Settings/Start from row input'), "2")
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/Apply CSV Settings button'))

		checkLastMailReceived("New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Start from row from " + GlobalVariable.username, 30)

		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Exclude last") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Exclude last")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(5)
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/CSV Settings button'))
		WebUI.setText(findTestObject('Object Repository/3. Report/5. CSV Settings/Exclude last'), "1")
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/Apply CSV Settings button'))

		checkLastMailReceived("New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Exclude last from " + GlobalVariable.username, 30)

		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Separator") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Separator")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(5)
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/CSV Settings button'))
		WebUI.selectOptionByLabel(findTestObject('Object Repository/3. Report/5. CSV Settings/Separator select'), ";", false)
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/Apply CSV Settings button'))

		checkLastMailReceived("New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Separator from " + GlobalVariable.username, 30)

		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Sheet number") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Sheet number")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(5)
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'))
		WebUI.setText(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "9")
		WebUI.sendKeys(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), Keys.chord(Keys.ENTER))
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Save button'))

		checkLastMailReceived("New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Sheet number from " + GlobalVariable.username, 40)

		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Insert a table (check)") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Insert a table (check)")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(5)
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'))
		WebUI.clearText(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'))
		WebUI.sendKeys(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "8")
		WebUI.sendKeys(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), Keys.chord(Keys.ENTER))
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Save button'))
		WebUI.delay(10)

		regenerationMailTitle = "New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Insert a table (check) from " + GlobalVariable.username
		WebUI.switchToWindowIndex(1)
		remainingAttempts = 30
		firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))
		secondMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))
		firstMailNumberPresentSelector = findTestObject('7. Gmail/Mail 1 number').getSelectorCollection().get(SelectorMethod.CSS)
		firstMailNumber = ""
		isfirstMailNumberPresent = selenium.isElementPresent("css=" + firstMailNumberPresentSelector)

		while((firstMailTitle != regenerationMailTitle || secondMailTitle == regenerationMailTitle || firstMailNumber != "2") && remainingAttempts > 0) {
			WebUI.click(findTestObject('Object Repository/7. Gmail/Refresh mails button'))
			Thread.sleep(1500)
			firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))
			secondMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))
			isfirstMailNumberPresent = selenium.isElementPresent("css=" + firstMailNumberPresentSelector)
			if (firstMailTitle == regenerationMailTitle && secondMailTitle != regenerationMailTitle && isfirstMailNumberPresent == true) {
				firstMailNumber = WebUI.getText(findTestObject('7. Gmail/Mail 1 number'))
			}
			remainingAttempts = remainingAttempts - 1
		}
		assert WebUI.verifyEqual(regenerationMailTitle, WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))) == true
		assert WebUI.verifyNotEqual(regenerationMailTitle, WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))) == true
		assert Integer.valueOf(WebUI.getText(findTestObject('7. Gmail/Mail 1 number'))) >= 2
		Thread.sleep(2500)
		WebUI.focus(findTestObject('7. Gmail/Mail 1 Title'))
		WebUI.focus(findTestObject('7. Gmail/Mail 2 Title'))
		WebUI.focus(findTestObject('7. Gmail/Mail 1 number'))
		Thread.sleep(2500)
		WebUI.switchToWindowIndex(0)
		WebUI.waitForElementPresent(findTestObject('Object Repository/3. Report/Edit Report Name button'), 30)

		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Insert a table (uncheck)") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Insert a table (uncheck)")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(5)
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'))
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Insert a table checkbox'))
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Save button'))

		checkLastMailReceived("New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Insert a table (uncheck) from " + GlobalVariable.username, 30)

		remainingAttemptsRename = 5
		while(remainingAttemptsRename > 0 && WebUI.getText(findTestObject('Object Repository/3. Report/Report name')).equals("( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Cancel XLS changes") == false) {
			WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Cancel XLS changes")
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
			remainingAttemptsRename--
		}
		WebUI.delay(7)
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'))
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Insert a table checkbox'))
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Cancel button'))
		WebUI.delay(10)

		regenerationMailTitle = "New report ( " + advertiserName + " ) Test auto - Report - Regenerate XLS - Cancel XLS changes from " + GlobalVariable.username
		WebUI.switchToWindowIndex(1)
		remainingAttempts = 30
		firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))
		secondMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))

		while((firstMailTitle != regenerationMailTitle || secondMailTitle == regenerationMailTitle) && remainingAttempts > 0) {
			WebUI.click(findTestObject('Object Repository/7. Gmail/Refresh mails button'))
			Thread.sleep(1500)
			firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))
			secondMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))
			remainingAttempts = remainingAttempts - 1
		}
		assert WebUI.verifyEqual(regenerationMailTitle, WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))) == true
		assert WebUI.verifyNotEqual(regenerationMailTitle, WebUI.getText(findTestObject('7. Gmail/Mail 2 Title'))) == true
		assert WebUI.verifyElementNotPresent(findTestObject('7. Gmail/Mail 1 number'), 1) == true
		WebUI.focus(findTestObject('7. Gmail/Mail 1 Title'))
		WebUI.focus(findTestObject('7. Gmail/Mail 2 Title'))
	}

	private void checkLastMailReceived(String regenerationMailTitle, int remainingAttempts) {
		selenium = getSelenium()
		WebUI.switchToWindowIndex(1)
		Thread.sleep(1000)
		String firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))
		String firstMailNumberPresentSelector = findTestObject('7. Gmail/Mail 1 number').getSelectorCollection().get(SelectorMethod.CSS)
		String firstMailNumber = ""
		boolean isfirstMailNumberPresent = selenium.isElementPresent("css=" + firstMailNumberPresentSelector)

		while((firstMailTitle != regenerationMailTitle || firstMailNumber != "2") && remainingAttempts > 0) {
			WebUI.click(findTestObject('Object Repository/7. Gmail/Refresh mails button'))
			Thread.sleep(1500)
			firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))
			isfirstMailNumberPresent = selenium.isElementPresent("css=" + firstMailNumberPresentSelector)
			if (firstMailTitle == regenerationMailTitle && isfirstMailNumberPresent == true) {
				firstMailNumber = WebUI.getText(findTestObject('7. Gmail/Mail 1 number'))
			}
			remainingAttempts = remainingAttempts - 1
		}

		assert WebUI.verifyEqual(regenerationMailTitle, WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))) == true
		assert Integer.valueOf(WebUI.getText(findTestObject('7. Gmail/Mail 1 number'))) >= 2
		Thread.sleep(2500)
		WebUI.focus(findTestObject('7. Gmail/Mail 1 Title'))
		WebUI.focus(findTestObject('7. Gmail/Mail 1 number'))
		Thread.sleep(2500)
		WebUI.switchToWindowIndex(0)
		WebUI.waitForElementPresent(findTestObject('Object Repository/3. Report/Edit Report Name button'), 30)
	}
}
