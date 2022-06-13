package testCases.editLineSetup

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.text.DateFormatSymbols

import org.openqa.selenium.Keys

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.OptimizerDV360

import internal.GlobalVariable

public class EditLineSetup extends OptimizerDV360 {

	private String regex = 'row-index=\\"(\\d+)\\"'
	private int defaultRowIndex = 2
	private int currentRowIndex = defaultRowIndex

	private TestObject currentStatus = findTestObject('DV360 Optimizer/2. Setup/Setup First Line Status')
	private TestObject currentStatusPaused = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Status Paused')
	private TestObject currentStatusActive = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Status Active')
	private TestObject currentBudget = findTestObject('DV360 Optimizer/2. Setup/Setup First Line Budget')
	private TestObject currentBudgetType = findTestObject('DV360 Optimizer/2. Setup/Setup First Line Budget type')
	private TestObject currentBudgetTypeAmount = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Budget type Amount')
	private TestObject currentBudgetTypeUnlimited = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Budget type Unlimited')
	private TestObject currentBudgetInput = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Budget Input')
	private TestObject currentPacing = findTestObject('DV360 Optimizer/2. Setup/Setup First Line Pacing')
	private TestObject currentPacingDaily = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Pacing Daily')
	private TestObject currentPacingFlight = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Pacing Flight')
	private TestObject currentPacingAmount = findTestObject('DV360 Optimizer/2. Setup/Setup First Line Pacing amount')
	private TestObject currentPacingAmountInput = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Pacing amount Input')
	private TestObject currentPacingRate = findTestObject('DV360 Optimizer/2. Setup/Setup First Line Pacing rate')
	private TestObject currentPacingRateASAP = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Pacing rate ASAP')
	private TestObject currentPacingRateEven = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Pacing rate Even')
	private TestObject currentPacingRateAhead = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Pacing rate Ahead')
	private TestObject currentBidStrategyValue = findTestObject('DV360 Optimizer/2. Setup/Setup First Line Bid Strategy value')
	private TestObject currentBidStrategyUnit = findTestObject('DV360 Optimizer/2. Setup/Setup First Line Bid Strategy unit')
	private TestObject currentBidStrategyType = findTestObject('DV360 Optimizer/2. Setup/Setup First Line Bid Strategy type')
	private TestObject currentBidStrategyLimit = findTestObject('DV360 Optimizer/2. Setup/Setup First Line Bid Strategy limit')
	private TestObject currentBidStrategyValueInput = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy value Input')
	private TestObject currentBidStrategyUnitCustom = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy unit Custom')
	private TestObject currentBidStrategyUnitCPC = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy unit CPC')
	private TestObject currentBidStrategyUnitCPA = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy unit CPA')
	private TestObject currentBidStrategyTypeOptimize = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy type Optimize vCPM')
	private TestObject currentBidStrategyTypeMinimize = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy type Minimize')
	private TestObject currentBidStrategyTypeFixed = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy type Fixed')
	private TestObject currentBidStrategyTypeBeat = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy type Beat')
	private TestObject currentBidStrategyLimitInput = findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy limit Input')

	private List<TestObject> itemsToEdit = Arrays.asList(currentStatus, currentStatusActive, currentStatusPaused, currentBudgetType, currentBudgetTypeAmount,
	currentBudgetTypeUnlimited, currentBudget, currentBudgetInput, currentPacing, currentPacingDaily, currentPacingFlight,
	currentPacingAmount, currentPacingAmountInput, currentPacingRate, currentPacingRateASAP, currentPacingRateEven, currentPacingRateAhead,
	currentBidStrategyValue, currentBidStrategyValueInput, currentBidStrategyUnit, currentBidStrategyUnitCPA, currentBidStrategyUnitCPC,
	currentBidStrategyUnitCustom, currentBidStrategyType, currentBidStrategyTypeMinimize, currentBidStrategyTypeBeat, currentBidStrategyTypeOptimize,
	currentBidStrategyTypeFixed, currentBidStrategyLimit, currentBidStrategyLimitInput)

	private List<TestObject> itemsToCheck = Arrays.asList(currentStatus, currentBudgetType, currentBudget, currentPacing, currentPacingAmount, currentPacingRate,
	currentBidStrategyType, currentBidStrategyValue, currentBidStrategyUnit, currentBidStrategyLimit)

	@Keyword
	public void editLineSetup(
			boolean status, String statusValue,
			boolean budgetType, String budgetTypeValue,
			boolean budget, String budgetValue,
			boolean pacing, String pacingValue,
			boolean pacingAmount, String pacingAmountValue,
			boolean pacingRate, String pacingRateValue,
			boolean bidStrategyType, String bidStrategyTypeValue,
			boolean bidStrategyValue, String bidStrategyValueValue,
			boolean bidStrategyUnit, String bidStrategyUnitValue,
			boolean bidStrategyLimit, String bidStrategyLimitValue) {

		driver = DriverFactory.getWebDriver()
		selenium = getSelenium()

		WebUI.waitForElementPresent(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'), 60)
		WebUI.waitForElementNotPresent(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'), 240)

		if(!GlobalVariable.dateRange.equals("3")) {

			WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Daterange selector'))

			if (GlobalVariable.dateRange == "5") {
				clickObjectbyCss('mat-option[value="custom"]')
				WebUI.delay(2)
				Date date = new Date();
				Calendar calendar = GregorianCalendar.getInstance()
				calendar.setTime(date);
				String[] months = new DateFormatSymbols(new Locale("en", "GB")).getMonths();
				clickObjectbyCss('td[aria-label="' + toUpperLowerCase(months[calendar.get(Calendar.MONTH)]) + ' ' + GlobalVariable.startDay + ', ' + calendar.get(Calendar.YEAR).toString() + '"]')
				clickObjectbyCss('td[aria-label="' + toUpperLowerCase(months[calendar.get(Calendar.MONTH)]) + ' ' + GlobalVariable.endDay + ', ' + calendar.get(Calendar.YEAR).toString() + '"]')
				clickObjectbyCss('div.cdk-overlay-backdrop.cdk-overlay-transparent-backdrop.cdk-overlay-backdrop-showing')
			} else {
				String option = ('body > div.cdk-overlay-container > div.cdk-overlay-connected-position-bounding-box > div > div > mat-option:nth-child(' +
						GlobalVariable.dateRange) + ')'
				WebUI.delay(1)
				WebUI.click(selectorToTestObject(option))
			}

			WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Daterange-Interval-Validator'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'), 30) == true
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'), 240) == true
		}

		assert WebUI.waitForElementVisible(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'), 90) == true : "No data displayed after 90 seconds"

		String lineId = WebUI.getText(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow')).replaceAll(".*\\((\\d+)\\)", "\$1")

		if (selenium.isElementPresent(getSelector(findTestObject('DV360 Optimizer/Toast message'))) == true) {
			assert WebUI.waitForElementNotPresent(findTestObject('DV360 Optimizer/Toast message'), 30) == true
		}

		String advertiserName = WebUI.getAttribute(findTestObject('Object Repository/DV360 Optimizer/1. Top Bar/Advertiser input'), "value")

		ArrayList<ArrayList<String>> linesAndValuesToCheck = new ArrayList<>(1)

		if(!GlobalVariable.item.equals("")) {
			WebUI.setText(findTestObject('Object Repository/DV360 Optimizer/3. Grid/1. Filter column/First dimension quick filter input'), GlobalVariable.item)
			WebUI.sendKeys(findTestObject('Object Repository/DV360 Optimizer/3. Grid/1. Filter column/First dimension quick filter input'), Keys.chord(Keys.ENTER))
			WebUI.delay(1)
		}
		// Modifie les champs du Setup pour la première line de la grille et retourne les champs modifiés sous forme d'ArrayList à deux dimensions
		linesAndValuesToCheck = editLineSetupFields(status, statusValue, budgetType, budgetTypeValue, budget, budgetValue, pacing, pacingValue, pacingAmount, pacingAmountValue, pacingRate, pacingRateValue, bidStrategyType, bidStrategyTypeValue, bidStrategyValue, bidStrategyValueValue, bidStrategyUnit, bidStrategyUnitValue, bidStrategyLimit, bidStrategyLimitValue)

		assert WebUI.waitForElementNotHasAttribute(findTestObject('DV360 Optimizer/1. Top Bar/Apply optimization button'), 'disabled', 30) == true
		boolean isToastPresent = selenium.isElementPresent(getSelector(findTestObject('DV360 Optimizer/Toast message')))
		while (isToastPresent == true) {
			WebUI.delay(1)
			isToastPresent = selenium.isElementPresent(getSelector(findTestObject('DV360 Optimizer/Toast message')))
		}
		WebUI.click(findTestObject('DV360 Optimizer/1. Top Bar/Apply optimization button'))

		// Pas d'autopush
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'), 30) == true
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'), 30) == true

		// Waiting for file download
		String sdfPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator
		File file = new File(sdfPath + advertiserName + "-sdf.zip")
		WebUI.delay(1)
		int attempts = 10
		while(file.exists() == false && attempts > 0) {
			WebUI.delay(5)
			attempts--
		}

		assert file.exists() == true
		KeywordUtil.markPassed("SDF downloaded")
		String randomNumber = String.valueOf((int)(10000 + Math.random() * 9000000))
		renameFile(sdfPath, advertiserName + "-sdf.zip", advertiserName + "-sdf-"+ randomNumber + ".zip")
		WebUI.navigateToUrl('http://www.ezyzip.com/decompresser-des-fichiers-en-ligne.html')
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/11. Misc/ezyzip.com/Extract File upload input'), 30) == true
		WebUI.delay(1)
		WebUI.uploadFile(findTestObject('Object Repository/11. Misc/ezyzip.com/Extract File upload input'), sdfPath + advertiserName + "-sdf-"+ randomNumber + ".zip")
		WebUI.delay(1)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/11. Misc/ezyzip.com/Extract button'), 30) == true
		WebUI.click(findTestObject('Object Repository/11. Misc/ezyzip.com/Extract button'))
		assert WebUI.waitForElementVisible(findTestObject('11. Misc/ezyzip.com/First save file button'), 30) == true

		String extractedLiFileName = WebUI.getText(findTestObject('11. Misc/ezyzip.com/Extracted line item file name')).replaceFirst("\\.csv(\n?.*)+", ".csv")
		String downloadedLiCsvPath = sdfPath + extractedLiFileName
		// Download unzipped files
		WebUI.click(findTestObject('Object Repository/11. Misc/ezyzip.com/First save file button'))
		WebUI.delay(3)
		KeywordUtil.markPassed("SDF unzipped")

		this.ConnectToDV360(GlobalVariable.googleID, GlobalVariable.googlePassword)

		if (uploadSdf(downloadedLiCsvPath, lineId) == true)
		{
			CheckOnDV360 check = new CheckOnDV360()
			check.checkSetupValuesOnDV360(linesAndValuesToCheck)

			deleteFile(sdfPath + advertiserName + "-sdf-"+ randomNumber + ".zip")
			deleteFile(downloadedLiCsvPath)
		}
	}

	private boolean uploadSdf(String sdfFilePath, String itemId) {
		WebUI.click(findTestObject('10. DV360/DV360 search button'))
		WebUI.setText(findTestObject('10. DV360/input_Search dv360'), itemId)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Search suggestion'), 30) == true : "No match found for this line ID"
		WebUI.click(findTestObject('10. DV360/Search suggestion'))
		WebUI.delay(2)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/OI link'), 30) == true
		WebUI.click(findTestObject('10. DV360/OI link'))
		WebUI.delay(5)
		WebUI.click(findTestObject('10. DV360/OI option'))
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/OI button Upload'), 30) == true
		WebUI.click(findTestObject('10. DV360/OI button Upload'))
		WebUI.delay(2)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Upload dialog'), 10) == true
		WebUI.delay(2)
		WebUI.uploadFile(findTestObject('10. DV360/Upload Input'), sdfFilePath)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Uploaded file'), 30) == true
		assert WebUI.verifyElementVisible(findTestObject('10. DV360/Uploaded file')) == true
		WebUI.click(findTestObject('10. DV360/Upload Button'))

		KeywordUtil.markPassed("Uploading SDF")
		int maxWait = 60

		while (maxWait > 0 && selenium.isElementPresent(getSelector(findTestObject('10. DV360/Success Upload'))) == false) {
			WebUI.delay(1)
			maxWait--
		}

		if (selenium.isElementPresent(getSelector(findTestObject('10. DV360/Success Upload'))) == false)
		{
			WebUI.click(findTestObject('10. DV360/Upload expand error'))
			String str = WebUI.getText(findTestObject('10. DV360/Upload error')) + WebUI.getText(findTestObject('10. DV360/Upload error main'))
			KeywordUtil.markErrorAndStop(str)
		}

		assert WebUI.verifyElementVisible(findTestObject('10. DV360/Success Upload')) == true
		WebUI.refresh()
		WebUI.delay(5)

		KeywordUtil.markPassed("SDF uploaded")
		return (true)
	}

	private void renameFile(String filePath, String fileName, String newFileName) {
		File file = new File(filePath + fileName)
		File newFile = new File(filePath + newFileName)
		WebUI.delay(1)
		int attempts = 10
		while(file.renameTo(newFile) == false && attempts > 0) {
			WebUI.delay(5)
			attempts--
		}
		assert newFile.exists() == true
		KeywordUtil.markPassed("SDF renamed")
	}

	// Modifie les champs du Setup pour la première line de la grille
	private ArrayList<ArrayList<String>> editLineSetupFields(
			boolean status, String statusValue,
			boolean budgetType, String budgetTypeValue,
			boolean budget, String budgetValue,
			boolean pacing, String pacingValue,
			boolean pacingAmount, String pacingAmountValue,
			boolean pacingRate, String pacingRateValue,
			boolean bidStrategyType, String bidStrategyTypeValue,
			boolean bidStrategyValue, String bidStrategyValueValue,
			boolean bidStrategyUnit, String bidStrategyUnitValue,
			boolean bidStrategyLimit, String bidStrategyLimitValue)
	{
		ArrayList<ArrayList<String>> linesAndValuesToCheck = new ArrayList<>(1)
		linesAndValuesToCheck.add(new ArrayList<String>())

		linesAndValuesToCheck.get(0).add(WebUI.getText(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow')))

		WebUI.click(findTestObject('DV360 Optimizer/Expand Setup tab'))

		WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridSetupFirstRowFirstCol'))

		WebUI.delay(2)

		if(status == true) {
			WebUI.click(findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Status ' + toUpperLowerCase(statusValue)))
			WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))

			linesAndValuesToCheck.get(0).add(toUpperLowerCase(statusValue))
		}

		if(budgetType == true) {
			WebUI.doubleClick(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Budget type'))
			WebUI.delay(1)
			WebUI.click(findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Budget type ' + toUpperLowerCase(budgetTypeValue)))
			WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
		}

		if(budget == true) {
			WebUI.doubleClick(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Budget'))
			WebUI.delay(1)
			WebUI.setText(findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Budget Input'), budgetValue)
			WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
			linesAndValuesToCheck.get(0).add("Amount " + budgetValue)
		} else {
			linesAndValuesToCheck.get(0).add(toUpperLowerCase(budgetTypeValue))
		}

		if(pacing == true) {
			WebUI.doubleClick(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Pacing'))
			WebUI.delay(1)
			WebUI.click(findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Pacing ' + toUpperLowerCase(pacingValue)))
			WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
			if (pacingValue.toLowerCase().equals("flight") == true && budgetTypeValue.toLowerCase().equals("unlimited") == true) {
				assert WebUI.verifyElementText(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Pacing rate'), 'ASAP') == true
				linesAndValuesToCheck.get(0).add('ASAP')
			}
		}

		if(pacingAmount == true) {
			WebUI.doubleClick(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Pacing amount'))
			WebUI.delay(1)
			WebUI.setText(findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Pacing amount Input'), pacingAmountValue)
			WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
			linesAndValuesToCheck.get(0).add("Daily " + pacingAmountValue)
		} else {
			linesAndValuesToCheck.get(0).add(toUpperLowerCase(pacingValue))
		}

		if(pacingRate == true) {
			WebUI.doubleClick(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Pacing Rate'))
			WebUI.delay(1)
			WebUI.click(findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Pacing rate ' + pacingRateValue))
			WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
			linesAndValuesToCheck.get(0).add(pacingRateValue)
		}

		if(bidStrategyType == true) {
			WebUI.doubleClick(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Bid Strategy type'))
			WebUI.delay(1)
			WebUI.click(findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy type ' + bidStrategyTypeValue))
			WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
		}

		if(bidStrategyValue == true) {
			WebUI.doubleClick(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Bid Strategy value'))
			WebUI.delay(1)
			WebUI.setText(findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy value Input'), bidStrategyValueValue)
			WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
		}

		if(bidStrategyUnit == true) {
			WebUI.doubleClick(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Bid Strategy unit'))
			WebUI.delay(1)
			WebUI.click(findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy unit '+ bidStrategyUnitValue))
			WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
		}

		if(bidStrategyType == true && bidStrategyValue == true && bidStrategyUnit == true) {
			linesAndValuesToCheck.get(0).add(bidStrategyTypeValue + " " + bidStrategyUnitValue + " " + String.format(Locale.US, "%.2f", Float.valueOf(bidStrategyValueValue)))
		} else if(bidStrategyType == true && bidStrategyValue == true) {
			linesAndValuesToCheck.get(0).add(bidStrategyTypeValue + " " + String.format(Locale.US, "%.2f", Float.valueOf(bidStrategyValueValue)))
		} else if (bidStrategyType == true && bidStrategyUnit == true) {
			linesAndValuesToCheck.get(0).add(bidStrategyTypeValue + " " + bidStrategyUnitValue)
		}

		if(bidStrategyLimit == true) {
			WebUI.doubleClick(findTestObject('DV360 Optimizer/2. Setup/Setup First Line Bid Strategy limit'))
			WebUI.delay(1)
			WebUI.setText(findTestObject('DV360 Optimizer/2. Setup/Options/Setup First Line Bid Strategy limit Input'), bidStrategyLimitValue)
			WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
			linesAndValuesToCheck.get(0).add("Limit " + String.format(Locale.US, "%.2f", Float.valueOf(bidStrategyLimitValue)))
		}

		KeywordUtil.markPassed("Setup edited : " + linesAndValuesToCheck)

		return linesAndValuesToCheck
	}


	private void selectLineSetupOption(int fieldIndex, int optionIndex) {
		WebUI.doubleClick(itemsToEdit[fieldIndex])
		WebUI.click(itemsToEdit[optionIndex])
		WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
	}


	private void verifyLineSetupOption(int fieldIndex, int optionIndex) {
		//		println itemsToEdit[fieldIndex].getSelectorCollection().get(SelectorMethod.CSS)
		//		println itemsToEdit[fieldIndex]
		//		println WebUI.getText(itemsToEdit[fieldIndex])
		String currentValue = WebUI.getText(itemsToEdit[fieldIndex])
		//		println currentValue
		if(currentValue == "Optimize VCPM") currentValue = "Optimize vCPM"
		WebUI.doubleClick(itemsToEdit[fieldIndex])
		assert currentValue == WebUI.getText(itemsToEdit[optionIndex])
		WebUI.click(itemsToEdit[optionIndex])
		WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
		selenium.highlight('css=' + itemsToEdit[fieldIndex].getSelectorCollection().get(SelectorMethod.CSS))
	}


	private void setLineSetupValue(int fieldIndex, int valueIndex, String value) {
		WebUI.doubleClick(itemsToEdit[fieldIndex])
		WebUI.setText(itemsToEdit[valueIndex], value)
		WebUI.doubleClick(findTestObject('DV360 Optimizer/3. Grid/GridReportLineNameFirstRow'))
	}

	private void verifyLineSetupValue(int fieldIndex, String value) {
		//		println itemsToEdit[fieldIndex].getSelectorCollection().get(SelectorMethod.CSS)
		assert WebUI.verifyElementText(itemsToEdit[fieldIndex], value) == true
		selenium.highlight('css=' + itemsToEdit[fieldIndex].getSelectorCollection().get(SelectorMethod.CSS))
	}

	private void switchToVerifyMode() {
		for (def itemToEdit : itemsToEdit) {
			itemToEdit.setSelectorValue(SelectorMethod.CSS, itemToEdit.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst('\\[col\\-id\\^="budget"\\]:nth-child\\(3\\)', '[col-id^="budget"]:nth-child(2)'))
		}
	}

	private void switchToEditMode() {
		for (def itemToEdit : itemsToEdit) {
		}
	}


	private void goToNextRow() {
		currentRowIndex++
		for (def itemToEdit : itemsToEdit) {
			itemToEdit.setSelectorValue(SelectorMethod.CSS, itemToEdit.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, 'row-index="' + currentRowIndex + '"'))
		}
	}


	private void goToFirstRow() {
		currentRowIndex = defaultRowIndex
		for (def itemToEdit : itemsToEdit) {
			itemToEdit.setSelectorValue(SelectorMethod.CSS, itemToEdit.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, 'row-index="' + currentRowIndex + '"'))
		}
	}


	private void goToNextRowCheck() {
		currentRowIndex++
		for (def itemToCheck : itemsToCheck) {
			itemToCheck.setSelectorValue(SelectorMethod.CSS, itemToCheck.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, 'row-index="' + currentRowIndex + '"'))
		}
	}


	private void goToFirstRowCheck() {
		currentRowIndex = defaultRowIndex
		for (def itemToCheck : itemsToCheck) {
			itemToCheck.setSelectorValue(SelectorMethod.CSS, itemToCheck.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, 'row-index="' + currentRowIndex + '"'))
		}
	}


	private void verifyDefaultOption(int optionIndex, String option) {
		assert WebUI.verifyElementText(itemsToEdit[optionIndex], option) == true
	}


	private void verifyGridLocked(boolean[] linesAreEdited) {
		goToFirstRowCheck()
		int i = 0;

		for(lineIsEdited in linesAreEdited) {
			//			println i
			//			println lineIsEdited
			//			println itemsToCheck[i].getSelectorCollection().get(SelectorMethod.CSS)
			if (lineIsEdited == true && i == 0) {
				//				println "First row"
				assert WebUI.verifyElementAttributeValue(itemsToCheck[i], 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height ag-cell-first-right-pinned edited-cell is-locked ag-cell-value', 30) == true
			} else if (lineIsEdited == true && i > 0) {
				//				println "Edited row"
				assert WebUI.verifyElementAttributeValue(itemsToCheck[i], 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height edited-cell is-locked ag-cell-value', 30) == true
			} else if (lineIsEdited == false) {
				//				println "Not edited row"
				assert WebUI.verifyElementAttributeValue(itemsToCheck[i], 'class', 'ag-cell ag-cell-not-inline-editing ag-cell-with-height is-locked ag-cell-value', 30) == true
			} else { println "what" }

			i++;
			if (i == 10) {
				i = 0;
				goToNextRowCheck();
			}
			//			println "\n"
		}
	}

	private void editLineSetupFirstTwo() {
		defaultRowIndex = 0;
		goToFirstRow()
		selectLineSetupOption(0, 2)
		goToNextRow()
		selectLineSetupOption(0, 1)
	}

	private void editLineSetupFull() {
		goToFirstRow()
		selectLineSetupOption(0, 2)
		selectLineSetupOption(3, 5)
		selectLineSetupOption(8, 10)
		verifyDefaultOption(13, 'ASAP')
		selectLineSetupOption(23, 24)
		selectLineSetupOption(19, 21)
		setLineSetupValue(28, 29, '1')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 10)
		selectLineSetupOption(13, 16)
		selectLineSetupOption(23, 24)
		selectLineSetupOption(19, 20)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(28, 29, '0')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 10)
		selectLineSetupOption(13, 15)
		selectLineSetupOption(23, 24)
		selectLineSetupOption(19, 20)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(28, 29, '0')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 9)
		selectLineSetupOption(13, 14)
		selectLineSetupOption(23, 24)
		selectLineSetupOption(19, 20)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(11, 12, '1')
		setLineSetupValue(28, 29, '1')
		goToNextRow()
		selectLineSetupOption(0, 2)
		selectLineSetupOption(3, 5)
		selectLineSetupOption(8, 9)
		selectLineSetupOption(13, 15)
		selectLineSetupOption(23, 24)
		selectLineSetupOption(19, 21)
		setLineSetupValue(11, 12, '1')
		setLineSetupValue(28, 29, '0')
		goToNextRow()
		selectLineSetupOption(0, 2)
		selectLineSetupOption(3, 5)
		selectLineSetupOption(8, 10)
		verifyDefaultOption(13, 'ASAP')
		selectLineSetupOption(23, 25)
		selectLineSetupOption(19, 21)
		setLineSetupValue(17, 18, '1')
		setLineSetupValue(28, 29, '0')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 10)
		selectLineSetupOption(13, 16)
		selectLineSetupOption(23, 25)
		selectLineSetupOption(19, 20)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(17, 18, '1')
		setLineSetupValue(28, 29, '1')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 10)
		selectLineSetupOption(13, 15)
		selectLineSetupOption(23, 25)
		selectLineSetupOption(19, 20)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(17, 18, '1')
		setLineSetupValue(28, 29, '0')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 9)
		selectLineSetupOption(13, 14)
		selectLineSetupOption(23, 25)
		selectLineSetupOption(19, 20)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(11, 12, '1')
		setLineSetupValue(17, 18, '1')
		setLineSetupValue(28, 29, '0')
		goToNextRow()
		selectLineSetupOption(0, 2)
		selectLineSetupOption(3, 5)
		selectLineSetupOption(8, 9)
		selectLineSetupOption(13, 15)
		selectLineSetupOption(23, 25)
		selectLineSetupOption(19, 21)
		setLineSetupValue(11, 12, '1')
		setLineSetupValue(17, 18, '1')
		setLineSetupValue(28, 29, '1')
		goToNextRow()
		selectLineSetupOption(0, 2)
		selectLineSetupOption(3, 5)
		selectLineSetupOption(8, 10)
		verifyDefaultOption(13, 'ASAP')
		selectLineSetupOption(23, 26)
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 10)
		selectLineSetupOption(13, 16)
		selectLineSetupOption(23, 26)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 10)
		selectLineSetupOption(13, 15)
		selectLineSetupOption(23, 26)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 2)
		selectLineSetupOption(3, 5)
		selectLineSetupOption(8, 9)
		selectLineSetupOption(13, 14)
		selectLineSetupOption(23, 26)
		setLineSetupValue(11, 12, '1')
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 9)
		selectLineSetupOption(13, 15)
		selectLineSetupOption(23, 26)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(11, 12, '1')
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 10)
		selectLineSetupOption(13, 14)
		selectLineSetupOption(23, 27)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 2)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 10)
		selectLineSetupOption(13, 15)
		selectLineSetupOption(23, 27)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 5)
		selectLineSetupOption(8, 10)
		verifyDefaultOption(13, 'ASAP')
		selectLineSetupOption(23, 27)
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 2)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 10)
		selectLineSetupOption(13, 16)
		selectLineSetupOption(23, 27)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 9)
		selectLineSetupOption(13, 14)
		selectLineSetupOption(23, 27)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(11, 12, '1')
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 2)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 9)
		selectLineSetupOption(13, 14)
		selectLineSetupOption(23, 27)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(11, 12, '1')
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 1)
		selectLineSetupOption(3, 5)
		selectLineSetupOption(8, 9)
		selectLineSetupOption(13, 15)
		selectLineSetupOption(23, 27)
		setLineSetupValue(11, 12, '1')
		setLineSetupValue(17, 18, '1')
		goToNextRow()
		selectLineSetupOption(0, 2)
		selectLineSetupOption(3, 4)
		selectLineSetupOption(8, 9)
		selectLineSetupOption(13, 15)
		selectLineSetupOption(23, 27)
		setLineSetupValue(6, 7, '1')
		setLineSetupValue(11, 12, '1')
		setLineSetupValue(17, 18, '1')
	}









	private void verifyLineSetupFull() {
		goToFirstRow()
		verifyLineSetupValue(28,'1€')
		verifyDefaultOption(13, 'ASAP')
		verifyLineSetupOption(23, 24)
		verifyLineSetupOption(19, 21)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 5)
		verifyLineSetupOption(0, 2)
		goToNextRow()
		switchToVerifyMode()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(28,'0€')
		verifyLineSetupOption(23, 24)
		verifyLineSetupOption(19, 20)
		verifyLineSetupOption(13, 16)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(28,'0€')
		verifyLineSetupOption(23, 24)
		verifyLineSetupOption(19, 20)
		verifyLineSetupOption(13, 15)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(11,'1€')
		verifyLineSetupValue(28,'1€')
		verifyLineSetupOption(23, 24)
		verifyLineSetupOption(19, 20)
		verifyLineSetupOption(13, 14)
		verifyLineSetupOption(8, 9)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(11,'1€')
		verifyLineSetupValue(28,'0€')
		verifyLineSetupOption(23, 24)
		verifyLineSetupOption(19, 21)
		verifyLineSetupOption(13, 15)
		verifyLineSetupOption(8, 9)
		verifyLineSetupOption(3, 5)
		verifyLineSetupOption(0, 2)
		goToNextRow()
		verifyLineSetupValue(17,'1€')
		verifyLineSetupValue(28,'0€')
		verifyDefaultOption(13, 'ASAP')
		verifyLineSetupOption(23, 25)
		verifyLineSetupOption(19, 21)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(0, 2)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupValue(28,'1€')
		verifyLineSetupOption(23, 25)
		verifyLineSetupOption(19, 20)
		verifyLineSetupOption(13, 16)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupValue(28,'0€')
		verifyLineSetupOption(23, 25)
		verifyLineSetupOption(19, 20)
		verifyLineSetupOption(13, 15)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(11,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupValue(28,'0€')
		verifyLineSetupOption(23, 25)
		verifyLineSetupOption(19, 20)
		verifyLineSetupOption(13, 14)
		verifyLineSetupOption(8, 9)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(11,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupValue(28,'1€')
		verifyLineSetupOption(23, 25)
		verifyLineSetupOption(19, 21)
		verifyLineSetupOption(13, 15)
		verifyLineSetupOption(8, 9)
		verifyLineSetupOption(3, 5)
		verifyLineSetupOption(0, 2)
		goToNextRow()
		verifyDefaultOption(13, 'ASAP')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 26)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 5)
		verifyLineSetupOption(0, 2)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 26)
		verifyLineSetupOption(13, 16)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 26)
		verifyLineSetupOption(13, 15)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(11,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 26)
		verifyLineSetupOption(13, 14)
		verifyLineSetupOption(8, 9)
		verifyLineSetupOption(3, 5)
		verifyLineSetupOption(0, 2)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(11,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 26)
		verifyLineSetupOption(13, 15)
		verifyLineSetupOption(8, 9)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 27)
		verifyLineSetupOption(13, 14)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 27)
		verifyLineSetupOption(13, 15)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 2)
		goToNextRow()
		verifyDefaultOption(13, 'ASAP')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 27)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 5)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 27)
		verifyLineSetupOption(13, 16)
		verifyLineSetupOption(8, 10)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 2)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(11,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 27)
		verifyLineSetupOption(13, 14)
		verifyLineSetupOption(8, 9)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(11,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 27)
		verifyLineSetupOption(13, 14)
		verifyLineSetupOption(8, 9)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 2)
		goToNextRow()
		verifyLineSetupValue(11,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 27)
		verifyLineSetupOption(13, 15)
		verifyLineSetupOption(8, 9)
		verifyLineSetupOption(3, 5)
		verifyLineSetupOption(0, 1)
		goToNextRow()
		verifyLineSetupValue(6,'1€')
		verifyLineSetupValue(11,'1€')
		verifyLineSetupValue(17,'1€')
		verifyLineSetupOption(23, 27)
		verifyLineSetupOption(13, 15)
		verifyLineSetupOption(8, 9)
		verifyLineSetupOption(3, 4)
		verifyLineSetupOption(0, 2)
	}
}
