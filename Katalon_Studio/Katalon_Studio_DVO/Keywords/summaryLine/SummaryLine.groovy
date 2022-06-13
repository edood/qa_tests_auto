package summaryLine
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium
import com.reusableComponents.OptimizerDV360

class SummaryLine extends OptimizerDV360 {
	public WebDriver driver;
	public String baseUrl;
	public WebDriverBackedSelenium selenium;
	public TestObject tmp;

	public SummaryLine() {
		tmp = new TestObject("tmp")
		tmp.addProperty('css', ConditionType.EQUALS, '')
	}

	@Keyword
	def Checkgrid(WebDriverBackedSelenium selenium, String dateRange) {
		String attempts = 0
		String maxAttempts = 20
		Thread.sleep(1500);
		boolean isGridLoading = selenium.isElementPresent("css=app-progressbar > mat-progress-bar")
		while (isGridLoading  == 1 &&  attempts < maxAttempts) {
			Thread.sleep(250);
			isGridLoading = selenium.isElementPresent("css=app-progressbar > mat-progress-bar")
		}
		String noRowsToShow = 1
		while (noRowsToShow == 1) {
			boolean isErrorPresent = selenium.isElementPresent("css=snack-bar-container.mat-snack-bar-error > simple-snack-bar")
			while (isErrorPresent == 1) {
				Thread.sleep(250);
				isErrorPresent = selenium.isElementPresent("css=snack-bar-container.mat-snack-bar-error > simple-snack-bar")
			}
			if (dateRange != '3') {
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
				Thread.sleep(1500);
				isErrorPresent = selenium.isElementPresent("css=snack-bar-container.mat-snack-bar-error > simple-snack-bar")
				while (isErrorPresent == 1) {
					Thread.sleep(250);
					isErrorPresent = selenium.isElementPresent("css=snack-bar-container.mat-snack-bar-error > simple-snack-bar")
				}
				isGridLoading = selenium.isElementPresent("css=app-progressbar > mat-progress-bar")
				while (isGridLoading == 1 &&  attempts < maxAttempts) {
					Thread.sleep(250);
					isGridLoading = selenium.isElementPresent("css=app-progressbar > mat-progress-bar")
					attempts++
				}
			}
			noRowsToShow = selenium.isElementPresent("css=span.ag-overlay-no-rows-center")
			if (noRowsToShow == 1) {
				selenium.refresh()
			}
		}
	}

	@Keyword
	def CheckReportColumnsPixelsandMetaPixels(WebDriverBackedSelenium sel, String dateRange, boolean highlighting, TestObject tmp) {
		int[] headerIDs = [6, 1, 2, 3, 4, 5]

		int columnID = 1

		String firstColumnID = ''

		String secondColumnID = ''

		while (columnID < 7) {
			int currentHeaderID = headerIDs[(columnID - 1)]

			String currentColumn = sel.getText(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-header > div.ag-header-viewport > div > div:nth-child(2) > div:nth-child(' +
					currentHeaderID) + ')')

			if (currentColumn == 'CPA') {
				firstColumnID = '1'
				secondColumnID = '2'
			}
			else if (currentColumn == 'CPA PC') {
				firstColumnID = '1'
				secondColumnID = '4'
			}
			else if (currentColumn == 'CTR (%)') {
				firstColumnID = '22'
				secondColumnID = '21'
			}
			else if (currentColumn == 'CPM') {
				firstColumnID = '20'
				secondColumnID = '21'
			}
			else if (currentColumn == 'CPC') {
				firstColumnID = '20'
				secondColumnID = '22'
			} else if (currentColumn == 'Viewability Rate (%)') {
				firstColumnID = '30'

				secondColumnID = '31'
			} else if (currentColumn == 'VTR (%)') {
				firstColumnID = '32'

				secondColumnID = '21'
			}

			if (highlighting == true) {
				sel.highlight(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(' +
						columnID) + ')')
			}
			float summary = Float.parseFloat(sel.getText(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(' +
					columnID) + ')'))

			float calculatedSummary = 0

			float firstSummary = 0

			float secondSummary = 0

			float summaryDiff = 0

			int increment = 0

			boolean canScroll = true

			while ((increment > -1) && (canScroll == true)) {
				canScroll = sel.isElementPresent(('css=div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' +
						increment) + '\']')

				println(canScroll)

				if (canScroll == true) {
					println(currentColumn)

					println(increment)

					println(columnID)

					if (((increment % 25) == 0) && (canScroll == true)) {
						int currentLine = 0

						sel.runScript('document.querySelector("div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + increment + '\']").scrollIntoView()')
					}

					if (((((((currentColumn == 'CPA') || (currentColumn == 'CPA PC')) || (currentColumn == 'CTR (%)')) || (currentColumn ==
					'CPM')) || (currentColumn == 'CPC')) || (currentColumn == 'Viewability Rate (%)')) || (currentColumn == 'VTR (%)')) {
						if (highlighting == true) {
							sel.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + increment + '"] > div:nth-child(' + firstColumnID + ')')
							sel.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + increment + '"] > div:nth-child(' + secondColumnID + ')')
						}
						WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-child(' + firstColumnID + ')', true)
						firstSummary = firstSummary + (Float.parseFloat(WebUI.getText(tmp))).round(2)

						println("firstSummary = " + firstSummary)
						WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-child(' + secondColumnID + ')', true)
						secondSummary = secondSummary + (Float.parseFloat(WebUI.getText(tmp))).round(2)

						println("secondSummary = " + secondSummary)
					} else {
						if (highlighting == true) {
							sel.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + increment + '"] > div:nth-child(' + columnID + ')')
						}
						WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-of-type(' + columnID + ')', true)
						println("valeur recuperer : " + WebUI.getText(tmp))
						println("increment = " + increment)
						calculatedSummary = calculatedSummary + Float.parseFloat(WebUI.getText(tmp))
						println("calculatedSummary before round = " + calculatedSummary)
						calculatedSummary = calculatedSummary.round(2)
						println("calculatedSummary after round = " + calculatedSummary)
					}
					increment++
				}
			}
			println(firstSummary)

			println(secondSummary)

			if ((currentColumn == 'CPA') && (secondSummary != 0)) {
				calculatedSummary = (firstSummary / secondSummary).round(2)
			}
			else if ((currentColumn == 'CPA PC') && (secondSummary != 0)) {
				calculatedSummary = (firstSummary / secondSummary).round(2)
			}
			else if ((currentColumn == 'CTR (%)') && (secondSummary != 0)) {
				calculatedSummary = ((firstSummary / secondSummary) * 100).round(2)
			}
			else if ((currentColumn == 'CPM') && (secondSummary != 0)) {
				calculatedSummary = ((firstSummary / secondSummary) * 1000).round(2)
			}
			else if ((currentColumn == 'CPC') && (secondSummary != 0)) {
				calculatedSummary = (firstSummary / secondSummary).round(2)
			}
			else if ((currentColumn == 'Viewability Rate (%)') && (secondSummary != 0)) {
				calculatedSummary = ((firstSummary / secondSummary) * 100).round(2)
			}
			else if ((currentColumn == 'VTR (%)') && (secondSummary != 0)) {
				calculatedSummary = ((firstSummary / secondSummary) * 100).round(2)
			}
			else if (currentColumn == 'CPA' || currentColumn == 'CPA PC' || currentColumn == 'CTR (%)' || currentColumn == 'CPM' || currentColumn == 'CPC' || currentColumn == 'Viewability Rate (%)' || currentColumn == 'VTR (%)' &&
			secondSummary == 0) {
				calculatedSummary = 0
			}
			else {
				calculatedSummary = calculatedSummary.round(2)
			}
			summaryDiff = (summary - calculatedSummary).round(2)
			if ((summaryDiff < -0.05) || (summaryDiff > 0.05)) {
				println(currentColumn)
				println(summary)
				println(calculatedSummary)
				println(summaryDiff)
				KeywordUtil.markFailedAndStop('Summary difference range is too wide')
			}
			if (highlighting == true) {
				sel.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(' + columnID + ')')
			}
			columnID++
			WebUI.executeJavaScript('document.querySelector(\'.ag-body-viewport\').scrollTo(0, 0);', null)
		}
	}

	@Keyword
	def CheckReportColumns(WebDriverBackedSelenium sel, boolean highlighting, TestObject temporaire) {
		int columnID = 1
		int firstColumnID
		int secondColumnID
		int test = 4

		while (columnID < 17) {
			String currentColumn = sel.getText(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-header > div.ag-header-viewport > div > div:nth-child(2) > div:nth-of-type(' +
					columnID) + ')')

			if (currentColumn == 'CPA') {
				firstColumnID = 1
				secondColumnID = 4
			} else if (currentColumn == 'CPA PC') {
				firstColumnID = 1
				secondColumnID = 6
			} else if (currentColumn == 'CTR (%)') {
				firstColumnID = 3
				secondColumnID = 2
			} else if (currentColumn == 'CPM') {
				firstColumnID = 1
				secondColumnID = 2
			} else if (currentColumn == 'CPC') {
				firstColumnID = 1
				secondColumnID = 3
			} else if (currentColumn == 'Viewability Rate (%)') {
				firstColumnID = 11
				secondColumnID = 12
			} else if (currentColumn == 'VTR (%)') {
				firstColumnID = 13
				secondColumnID = 2
			}

			if (columnID % 10 == 0) {
				sel.runScript('const myElement = document.querySelector(\'[col-id=\"' + currentColumn + '\"]\'); document.querySelector(\'.ag-center-cols-viewport\').scrollLeft = myElement.offsetLeft;')
			}

			int increment = 0
			boolean canScroll = true
			float calculatedSummary = 0
			float firstSummary = 0
			float secondSummary = 0
			float summaryDiff = 0
			float summary = 0

			while ((increment > -1) && (canScroll == true)) {
				canScroll = sel.isElementPresent(('css=div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' +
						increment) + '\']')

				if (canScroll == true) {
					if (((increment % 25) == 0) && (canScroll == true)) {
						int currentLine = 0

						sel.runScript("document.querySelector(\'div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\"" + increment + "\"]\').scrollIntoView()")
						WebUI.executeJavaScript('document.querySelector("div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' + increment + '\']").scrollIntoView()', null)
					}

					if (highlighting == true) {
						sel.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-of-type(' + columnID + ')')
					}
					println('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-of-type(' +
							columnID + ')')
					summary = Float.parseFloat(sel.getText('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-of-type(' + columnID + ')'))



					if (((((((currentColumn == 'CPA') || (currentColumn == 'CPA PC')) || (currentColumn == 'CTR (%)')) || (currentColumn ==
					'CPM')) || (currentColumn == 'CPC')) || (currentColumn == 'Viewability Rate (%)')) || (currentColumn == 'VTR (%)')) {
						if (highlighting == true) {
							sel.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + increment + '"] > div:nth-child(' + firstColumnID + ')')
							sel.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + increment + '"] > div:nth-child(' + secondColumnID + ')')
						}
						WebUI.modifyObjectProperty(temporaire, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-child(' + firstColumnID + ')', true)
						firstSummary = firstSummary + (Float.parseFloat(WebUI.getText(temporaire))).round(2)

						println("firstSummary = " + firstSummary)
						WebUI.modifyObjectProperty(temporaire, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-child(' + secondColumnID + ')', true)
						secondSummary = secondSummary + (Float.parseFloat(WebUI.getText(temporaire))).round(2)

						println("secondSummary = " + secondSummary)
					} else {
						if (highlighting == true) {
							sel.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + increment + '"] > div:nth-of-type(' + columnID + ')')
						}
						WebUI.modifyObjectProperty(temporaire, 'css', ConditionType.EQUALS.toString(), 'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-of-type(' + columnID + ')', true)
						println("valeur recuperer : " + WebUI.getText(temporaire))
						println("increment = " + increment)
						calculatedSummary = calculatedSummary + Float.parseFloat(WebUI.getText(temporaire))
						println("calculatedSummary before round = " + calculatedSummary)
						calculatedSummary = calculatedSummary.round(2)
						println("calculatedSummary after round = " + calculatedSummary)
					}
					increment++
				}
			}

			if ((currentColumn == 'CPA') && (secondSummary != 0)) {
				calculatedSummary = (firstSummary / secondSummary)

				calculatedSummary = calculatedSummary.round(2)
			} else if ((currentColumn == 'CPA PC') && (secondSummary != 0)) {
				calculatedSummary = (firstSummary / secondSummary)

				calculatedSummary = calculatedSummary.round(2)
			} else if ((currentColumn == 'CTR (%)') && (secondSummary != 0)) {
				calculatedSummary = ((firstSummary / secondSummary) * 100)

				calculatedSummary = calculatedSummary.round(2)
			} else if ((currentColumn == 'CPM') && (secondSummary != 0)) {
				calculatedSummary = ((firstSummary / secondSummary) * 1000)

				calculatedSummary = calculatedSummary.round(2)
			} else if ((currentColumn == 'CPC') && (secondSummary != 0)) {
				calculatedSummary = (firstSummary / secondSummary)

				calculatedSummary = calculatedSummary.round(2)
			} else if ((currentColumn == 'Viewability Rate (%)') && (secondSummary != 0)) {
				calculatedSummary = ((firstSummary / secondSummary) * 100)

				calculatedSummary = calculatedSummary.round(2)
			} else if ((currentColumn == 'VTR (%)') && (secondSummary != 0)) {
				calculatedSummary = ((firstSummary / secondSummary) * 100)

				calculatedSummary = calculatedSummary.round(2)
			} else if ((((((((currentColumn == 'CPA') || (currentColumn == 'CPA PC')) || (currentColumn == 'CTR (%)')) || (currentColumn ==
			'CPM')) || (currentColumn == 'CPC')) || (currentColumn == 'Viewability Rate (%)')) || (currentColumn == 'VTR (%)')) &&
			(secondSummary == 0)) {
				calculatedSummary = 0
			} else {
				calculatedSummary.round(2)
			}

			println('summary = ' + summary)
			println('calculatedsummary = ' + calculatedSummary)
			summaryDiff = (summary - calculatedSummary)

			summaryDiff = summaryDiff.round(2)
			println('summaryDiff = ' + summaryDiff)

			if ((summaryDiff < -0.05) || (summaryDiff > 0.05)) {

				KeywordUtil.markFailedAndStop('Summary difference range is too wide')
			}
			if (highlighting == true) {
				sel.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(' + columnID + ')')
			}
			columnID++
		}
		sel.runScript('document.querySelector(\'.ag-center-cols-viewport\').scrollTo(0, 0);')
	}

	@Keyword
	public void checkReportColumnsFilterData(WebDriverBackedSelenium selenium, String dateRange, int numberOfColumnsToCheck, boolean highlighting) {
		//int[] headerIDs = [6, 1, 2, 3, 4, 5]

		int columnID = 1
		String firstColumnID = ''
		String secondColumnID = ''

		while (columnID < numberOfColumnsToCheck + 1) {
			//int currentHeaderID = headerIDs[(columnID - 1)]

			String currentColumn = selenium.getText(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-header > div.ag-header-viewport > div > div:nth-child(2) > div:nth-child(' +
					columnID) + ')')

			if (currentColumn == 'CPA') {
				firstColumnID = '1'
				secondColumnID = '4'
			} else if (currentColumn == 'CPA PC') {
				firstColumnID = '1'
				secondColumnID = '6'
			} else if (currentColumn == 'CTR (%)') {
				firstColumnID = '3'
				secondColumnID = '2'
			} else if (currentColumn == 'CPM') {
				firstColumnID = '1'
				secondColumnID = '2'
			} else if (currentColumn == 'CPC') {
				firstColumnID = '1'
				secondColumnID = '3'
			} else if (currentColumn == 'Viewability Rate (%)') {
				firstColumnID = '11'
				secondColumnID = '12'
			} else if (currentColumn == 'VTR (%)') {
				firstColumnID = '13'
				secondColumnID = '2'
			}

			if (highlighting == true) {
				selenium.highlight(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(' +
						columnID) + ')')
			}

			double summary = Double.parseDouble(selenium.getText(('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(' +
					columnID) + ')'))

			double calculatedSummary = 0
			double firstSummary = 0
			double secondSummary = 0
			double summaryDiff = 0
			int increment = 0
			boolean canScroll = true

			while ((increment > -1) && (canScroll == true)) {
				canScroll = selenium.isElementPresent(('css=div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' +
						increment) + '\']')
				println(canScroll)

				if (canScroll == true) {
					println(currentColumn)
					println(increment)
					println(columnID)

					if (((increment % 25) == 0) && (canScroll == true)) {
						int currentLine = 0
						selenium.runScript(('document.querySelector("div.ag-body-viewport > div:nth-of-type(2) > div > div > div[row-index=\'' +
								increment) + '\']").scrollIntoView()')
					}

					if (highlighting == true) {
						selenium.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-of-type(' + columnID + ')')
					}
					/*
					 if (currentColumn == "CPA" || currentColumn == "CPA PC" || currentColumn == "CTR (%)" || currentColumn == "CPM" || currentColumn == "CPC" || currentColumn == "Viewability Rate (%)" || currentColumn == "VTR (%)")
					 {
					 selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"" + increment + '\"] > div:nth-child(' + firstColumnID + ')')
					 selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"" + increment + "\"] > div:nth-child(" + secondColumnID + ")")
					 firstSummary = firstSummary + WebUI.executeJavaScript('document.querySelector(\'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-child("' + firstColumnID + '")).prop(\'innerText\')).toFixed(2))"', null)
					 secondSummary = secondSummary + WebUI.executeJavaScript('document.querySelector(\'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-child("' + secondColumnID + ")).prop(\'innerText\')).toFixed(2))", null)
					 }
					 else
					 {
					 selenium.highlight("css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"" + increment + "\"] > div:nth-child(" + columnID + ")")
					 calculatedSummary = calculatedSummary + WebUI.executeJavaScript('document.querySelector(\'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-child(' + columnID + ")).prop('innerText'))).toFixed(2))", null)
					 }
					 */
					if (((((((currentColumn == 'CPA') || (currentColumn == 'CPA PC')) || (currentColumn == 'CTR (%)')) || (currentColumn ==
					'CPM')) || (currentColumn == 'CPC')) || (currentColumn == 'Viewability Rate (%)')) || (currentColumn == 'VTR (%)')) {
						if (highlighting == true) {
							selenium.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + increment + '"] > div:nth-child(' + firstColumnID + ')')
							selenium.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + increment + '"] > div:nth-child(' + secondColumnID + ')')
						}
						firstSummary = firstSummary + Double.parseDouble(WebUI.executeJavaScript('return document.querySelector(\'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-child(' + firstColumnID + ')\').innerText', null))
						secondSummary = secondSummary + Double.parseDouble(WebUI.executeJavaScript('return document.querySelector(\'ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index=\"' + increment + '\"] > div:nth-child(' + secondColumnID + ')\').innerText', null))
					} else {
						if (highlighting == true) {
							selenium.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + increment + '"] > div:nth-of-type(' + columnID + ')')
						}
						WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), ((('ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-body-viewport > div.ag-center-cols-clipper > div > div > div[row-index="' + increment) + '"] > div:nth-of-type(') + columnID) + ')', true)

						println('valeur recuperer : ' + WebUI.getText(tmp))
						println('increment = ' + increment)
						calculatedSummary = (calculatedSummary + Double.parseDouble(WebUI.getText(tmp)))
						println('calculatedSummary before round = ' + calculatedSummary)
						calculatedSummary = round(calculatedSummary, 2)
						println('calculatedSummary after round = ' + calculatedSummary)
					}
					increment++
				}
			}

			firstSummary = round(firstSummary, 2)
			secondSummary = round(secondSummary, 2)

			if ((currentColumn == 'CPA') && (secondSummary != 0)) {
				calculatedSummary = round(firstSummary / secondSummary, 2)
			} else if ((currentColumn == 'CPA PC') && (secondSummary != 0)) {
				calculatedSummary = round(firstSummary / secondSummary, 2)
			} else if ((currentColumn == 'CTR (%)') && (secondSummary != 0)) {
				calculatedSummary = round((firstSummary / secondSummary) * 100, 2)
			} else if ((currentColumn == 'CPM') && (secondSummary != 0)) {
				calculatedSummary = round((firstSummary / secondSummary) * 1000, 2)
			} else if ((currentColumn == 'CPC') && (secondSummary != 0)) {
				calculatedSummary = round(firstSummary / secondSummary, 2)
			} else if ((currentColumn == 'Viewability Rate (%)') && (secondSummary != 0)) {
				calculatedSummary = round((firstSummary / secondSummary) * 100, 2)
			} else if ((currentColumn == 'VTR (%)') && (secondSummary != 0)) {
				calculatedSummary = round((firstSummary / secondSummary) * 100, 2)
			} else if (((((((currentColumn == 'CPA') || (currentColumn == 'CPA PC')) || (currentColumn == 'CTR (%)')) || (currentColumn ==
			'CPM')) || (currentColumn == 'CPC')) || (currentColumn == 'Viewability Rate (%)')) || ((currentColumn == 'VTR (%)') &&
			(secondSummary == 0))) {
				calculatedSummary = 0.0d
			} else {
				calculatedSummary = round(calculatedSummary, 2)
			}

			summaryDiff = round(summary - calculatedSummary, 2)

			if ((summaryDiff < -0.05) || (summaryDiff > 0.05)) {
				println("currentColumn : " + currentColumn)
				println("summary : " + summary)
				println("firstSummary : " + firstSummary)
				println("secondSummary : " + secondSummary)
				println("calculatedSummary : " + calculatedSummary)
				println("summaryDiff : " + summaryDiff)
				KeywordUtil.markFailedAndStop('Summary difference range is too wide')
			}

			if (highlighting == true) {
				selenium.highlight('css=ag-grid-angular > div > div.ag-root-wrapper-body > div.ag-root > div.ag-floating-top > div.ag-floating-top-viewport > div > div > div:nth-child(' + columnID + ')')
			}
			columnID++
			WebUI.executeJavaScript('document.querySelector(\'.ag-body-viewport\').scrollTo(0, 0);', null)
		}
	}

	public double round(double value, int precision) {
		final double mult = Math.pow(10, precision);
		return Math.round(value * mult) / mult;
	}
}
