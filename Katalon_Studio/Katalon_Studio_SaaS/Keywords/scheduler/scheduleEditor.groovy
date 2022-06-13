package scheduler

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

public class scheduleEditor {

	private final static Map metricsMap = ["CPA":"First", "CPA PC":"Second", "Spend":"Third", "CPA PV":"Fourth"]
	private final static Map operatorsMap =
	["greater than":"First", "less than":"Second", "greater than or equal":"Third", "less than or equal":"Fourth", "equal":"Fifth"]

	private static WebDriver driver = DriverFactory.getWebDriver()
	private static String baseUrl = 'https://shared6-saas.tradelab.fr/scheduler'
	private static WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver, baseUrl)

	@Keyword
	public static void createSchedule(List<String> metrics, String operator, String value, String scheduleName, int delay) {
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/Add Schedule button'), 30)
		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/Add Schedule button'))

		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Action Type select'), 30)
		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Action Type select'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/Third option'), 30)
		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/Third option'))

		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Action select'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First option'), 30)
		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First option'))

		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/IO select'), 30)
		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/IO select'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First option'), 30)
		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First option'))

		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/LI select'))
		WebUI.waitForElementVisible(findTestObject('3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First unchecked option'), 30)
		WebUI.click(findTestObject('3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First unchecked option'))

		WebUI.clickOffset(findTestObject('3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First unchecked option'), 0, -100)

		addRules(metrics, operator, value)

		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Create action button'))

		WebUI.setText(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule name'), scheduleName)

		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date);
		int futureMinutes = calendar.get(Calendar.MINUTE)+21
		String scheduleTime = ""
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY)
		int nextHour = calendar.get(Calendar.HOUR_OF_DAY)+1
		int first = ((calendar.get(Calendar.MINUTE)+11)%60)
		int second = ((calendar.get(Calendar.MINUTE)+11)%60)

		//		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Enable reverse button'))
		//
		//		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule reverse date button'))
		//
		//		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/Today date'))

		//		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule reverse hour button'))
		//		Date date = new Date();
		//		Calendar calendar = GregorianCalendar.getInstance()
		//		calendar.setTime(date);
		//		int futureMinutes = calendar.get(Calendar.MINUTE)+21
		//		String scheduleTime = ""
		//		int currentHour = calendar.get(Calendar.HOUR_OF_DAY)
		//		int nextHour = calendar.get(Calendar.HOUR_OF_DAY)+1
		//		if(futureMinutes >= 60) {
		//			scheduleTime = nextHour < 10 ? "0" + nextHour.toString() : nextHour.toString()
		//		} else {
		//			scheduleTime = currentHour < 10 ? "0" + currentHour.toString() : currentHour.toString()
		//		}
		//		scheduleTime += ":"
		//		int first = ((calendar.get(Calendar.MINUTE)+21)%60)
		//		int second = ((calendar.get(Calendar.MINUTE)+21)%60)
		//		scheduleTime += (calendar.get(Calendar.MINUTE)+21)%60 < 10 ? "0" + first.toString() : second.toString()
		//
		//		WebUI.setText(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule reverse hour button'), scheduleTime)

		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule hour button'))

		Thread.sleep(2500)

		date = new Date();
		calendar = GregorianCalendar.getInstance()
		calendar.setTime(date);
		futureMinutes = calendar.get(Calendar.MINUTE) + delay
		scheduleTime = ""
		currentHour = calendar.get(Calendar.HOUR_OF_DAY)
		nextHour = calendar.get(Calendar.HOUR_OF_DAY)+ (delay / 60)
		if(futureMinutes >= 60) {
			scheduleTime = nextHour < 10 ? "0" + nextHour.toString() : nextHour.toString()
		} else {
			scheduleTime = currentHour < 10 ? "0" + currentHour.toString() : currentHour.toString()
		}
		scheduleTime += ":"
		first = ((calendar.get(Calendar.MINUTE)+ delay)%60)
		second = ((calendar.get(Calendar.MINUTE)+ delay)%60)
		scheduleTime += (calendar.get(Calendar.MINUTE)+delay)%60 < 10 ? "0" + first.toString() : second.toString()
		WebUI.setText(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule hour button'), scheduleTime)
		selenium.fireEvent("id=mat-input-1", "input")

		Thread.sleep(2500)

		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule date button'))
		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/Today date'))

		Thread.sleep(2500)

		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Create schedule button'))

		boolean buttonStillDisabled = selenium.isElementPresent("css=schedule-form > div.schedule-form.ng-star-inserted > mat-card:nth-child(6) > mat-card-content > div > button[disabled]")
		if(buttonStillDisabled == true) {
			//			WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule reverse hour button'))
			//			date = new Date();
			//			calendar = GregorianCalendar.getInstance()
			//			calendar.setTime(date);
			//			futureMinutes = calendar.get(Calendar.MINUTE)+21
			//			scheduleTime = ""
			//			currentHour = calendar.get(Calendar.HOUR_OF_DAY)
			//			nextHour = calendar.get(Calendar.HOUR_OF_DAY)+1
			//			if(futureMinutes >= 60) {
			//				scheduleTime = nextHour < 10 ? "0" + nextHour.toString() : nextHour.toString()
			//			} else {
			//				scheduleTime = currentHour < 10 ? "0" + currentHour.toString() : currentHour.toString()
			//			}
			//			scheduleTime += ":"
			//			first = ((calendar.get(Calendar.MINUTE)+21)%60)
			//			second = ((calendar.get(Calendar.MINUTE)+21)%60)
			//			scheduleTime += (calendar.get(Calendar.MINUTE)+21)%60 < 10 ? "0" + first.toString() : second.toString()
			//
			//			WebUI.setText(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule reverse hour button'), scheduleTime)

			WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule hour button'))

			date = new Date();
			calendar = GregorianCalendar.getInstance()
			calendar.setTime(date);

			futureMinutes = calendar.get(Calendar.MINUTE)+ delay
			scheduleTime = ""
			currentHour = calendar.get(Calendar.HOUR_OF_DAY)
			nextHour = calendar.get(Calendar.HOUR_OF_DAY)+ (delay / 60)

			if(futureMinutes >= 60) {
				scheduleTime = nextHour < 10 ? "0" + nextHour.toString() : nextHour.toString()
			} else {
				scheduleTime = currentHour < 10 ? "0" + currentHour.toString() : currentHour.toString()
			}

			scheduleTime += ":"
			first = ((calendar.get(Calendar.MINUTE)+delay)%60)
			second = ((calendar.get(Calendar.MINUTE)+delay)%60)

			scheduleTime += (calendar.get(Calendar.MINUTE)+delay)%60 < 10 ? "0" + first.toString() : second.toString()

			WebUI.setText(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Schedule hour button'), scheduleTime)
			Thread.sleep(2500)
			selenium.fireEvent("id=mat-input-1", "input")

			WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Create schedule button'))
		}

		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Toast message'), 30)
		WebUI.verifyElementText(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Toast message'), "Schedule created")

		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/Conditioned button'))

		boolean scheduleNameDisplayed = false
		int numberOfSchedules = selenium.getCssCount("ul.schedules > li")
		int i = 1
		String regex = "nth-of-type\\([^)]*\\)"
		TestObject currentScheduleName = findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/First Schedule name')
		while (scheduleNameDisplayed == false && i <= numberOfSchedules) {
			currentScheduleName.setSelectorValue(SelectorMethod.CSS, currentScheduleName.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, 'nth-of-type(' + i + ')'))
			String displayedScheduleName = WebUI.getText(currentScheduleName)
			scheduleNameDisplayed = displayedScheduleName.contains(scheduleName)
			WebUI.click(currentScheduleName)
			i++
		}
		WebUI.verifyEqual(scheduleNameDisplayed, true, FailureHandling.STOP_ON_FAILURE)
		WebUI.click(currentScheduleName)
		WebUI.delay(1)
	}


	@Keyword
	public static void addRules(List<String> metrics, String operator, String value) {
		int i = 1
		String regex = "nth-of-type\\([^)]*\\)"
		for (metric in metrics) {
			WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Add rule button'))
			// Metric
			TestObject metricSelect = findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Metric select')
			metricSelect.setSelectorValue(SelectorMethod.CSS, metricSelect.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, 'nth-of-type(' + i + ')'))
			WebUI.waitForElementVisible(metricSelect, 30)
			WebUI.click(metricSelect)
			WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First option'), 30)
			WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/'+ metricsMap.get(metric) + ' option'))
			// Pixel
			TestObject pixelSelect = findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Pixels select')
			pixelSelect.setSelectorValue(SelectorMethod.CSS, pixelSelect.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, 'nth-of-type(' + i + ')'))
			WebUI.click(pixelSelect)
			WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First option'), 30)
			WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First unchecked option'))

			WebUI.clickOffset(findTestObject('3. Scheduler - RBL/2. Schedule Editor/Overlay backdrop (use clickOffset)'), 0, 0)
			// Operator
			TestObject operatorSelect = findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Operator select')
			operatorSelect.setSelectorValue(SelectorMethod.CSS, operatorSelect.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, 'nth-of-type(' + i + ')'))
			WebUI.click(operatorSelect)
			WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/First option'), 30)
			WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/1.Dropdowns/'+ operatorsMap.get(operator) + ' option'))
			// Value
			TestObject valueInput = findTestObject('Object Repository/3. Scheduler - RBL/2. Schedule Editor/Value input')
			valueInput.setSelectorValue(SelectorMethod.CSS, valueInput.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, 'nth-of-type(' + i + ')'))
			WebUI.setText(valueInput, value)
			i++
		}
	}

	@Keyword
	public static void deleteSchedules(boolean deleteSchedules) {
		WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/Conditioned button'))

		if(deleteSchedules == true) {
			int numberOfSchedules = selenium.getCssCount("ul.schedules > li")
			int num = numberOfSchedules
			int i = 1
			String regex = "nth-of-type\\([^)]*\\)"
			while (i <= numberOfSchedules) {
				WebUI.waitForElementVisible(findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/First Schedule menu'))
				WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/First Schedule menu'))
				WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/Delete button'))
				WebUI.click(findTestObject('Object Repository/3. Scheduler - RBL/1. Schedule List/Yes button'))
				while (num != numberOfSchedules - i) {
					WebUI.delay(1)
					num = selenium.getCssCount("ul.schedules > li")
				}
				i++
			}
		}
	}
}
