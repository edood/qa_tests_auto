package editedLine
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.OptimizerDV360
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

class EditedLine extends OptimizerDV360 {
	public WebDriver driver;
	public String baseUrl;
	public WebDriverBackedSelenium selenium;
	public TestObject tmp;

	public EditedLine() {
		tmp = new TestObject("tmp")
		tmp.addProperty('css', ConditionType.EQUALS, '')
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
		WebUI.waitForElementVisible(tmp, timeout)
		WebUI.delay(1)
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
	def Checkgrid(WebDriverBackedSelenium selenium, String dateRange) {
		String attempts = 0
		String maxAttempts = 20
		Thread.sleep(1500);
		boolean isGridLoading = selenium.isElementPresent("css=app-progressbar > mat-progress-bar")
		while (isGridLoading  == true &&  attempts < maxAttempts) {
			Thread.sleep(250);
			isGridLoading = selenium.isElementPresent("css=app-progressbar > mat-progress-bar")
		}
		String noRowsToShow = '1'
		while (noRowsToShow == '1') {
			boolean isErrorPresent = selenium.isElementPresent("css=snack-bar-container.mat-snack-bar-error > simple-snack-bar")
			while (isErrorPresent == true) {
				Thread.sleep(250);
				isErrorPresent = selenium.isElementPresent("css=snack-bar-container.mat-snack-bar-error > simple-snack-bar")
			}
			selectDateRange(dateRange)
			/*		if (dateRange != '3') {
			 selenium.click("css=mat-sidenav-content > div > templates > div > context > div > date-selector > div > div.date-range-selector > mat-select > div")
			 selenium.click("css=body > div.cdk-overlay-container > div.cdk-overlay-connected-position-bounding-box > div > div > mat-option:nth-child("+dateRange+")")
			 if (dateRange == '5') {
			 String currentMonth = selenium.getText("css=#sat-datepicker-0 > div > sat-month-view > table > tbody > tr:nth-child(1) > td[aria-hidden=\"true\"]")
			 currentMonth = selenium.getEval("storedVars['currentMonth'].replace(' ', '').replace(' ', '').charAt(0).toUpperCase() + storedVars['currentMonth'].replace(' ', '').replace(' ', '').slice(1).toLowerCase()")
			 String calendarStartDay = selenium.getEval("storedVars['currentMonth'] + \" \" + storedVars['startDay'] + \", \" + new Date().getFullYear()")
			 String calendarEndDay = selenium.getEval("storedVars['currentMonth'] + \" \" + storedVars['endDay'] + \", \" + new Date().getFullYear()")
			 selenium.click("css=#sat-datepicker-0 > div > sat-month-view > table > tbody > tr > td[aria-label=\"" + calendarStartDay + "\"] > div")
			 Thread.sleep(750);
			 selenium.click("css=#sat-datepicker-0 > div > sat-month-view > table > tbody > tr > td[aria-label=\"" + calendarEndDay + "\"] > div")
			 Thread.sleep(750);
			 }
			 selenium.click("css=templates > div > context > div > date-selector > div > button")
			 */
			Thread.sleep(1500);
			isErrorPresent = selenium.isElementPresent("css=snack-bar-container.mat-snack-bar-error > simple-snack-bar")
			while (isErrorPresent == true) {
				Thread.sleep(250);
				isErrorPresent = selenium.isElementPresent("css=snack-bar-container.mat-snack-bar-error > simple-snack-bar")
				//		}
				isGridLoading = selenium.isElementPresent("css=app-progressbar > mat-progress-bar")
				while (isGridLoading == true &&  attempts < maxAttempts) {
					Thread.sleep(250);
					isGridLoading = selenium.isElementPresent("css=app-progressbar > mat-progress-bar")
					attempts++
				}
			}
			noRowsToShow = selenium.isElementPresent("css=span.ag-overlay-no-rows-center")
			if (noRowsToShow == '1') {
				selenium.refresh()
			}
		}
	}
}