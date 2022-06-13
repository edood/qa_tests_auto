package tools

import org.openqa.selenium.WebDriver
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.OptimizerDV360
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import internal.GlobalVariable

public class CheckAdvertisersHaveData extends OptimizerDV360 {

	public static List<String> advertiserNames = [
		"Auchan template",
		"Boursorama",
		"CRF Banque - Tradelab",
		"CRF Template - Tradelab",
		"FNAC",
		"Hello Bank 2017",
		"Le Monde - BR",
		"Nintendo SP",
		"Paris Filmes - Seat BR",
		"Sosh",
		"SPEEDY",
		"SQUARE ENIX",
		"Test SDFv4",
		"test_H",
		"Test_Produit_QA",
		"Test_QA_Arya",
		"Test_QA_Brienne",
		"Test_QA_Cersei",
		"Test_QA_JonSnow",
		"Test_QA_Khaldrogo",
		"Test_QA_RamsaySnow",
		"Test_QA_Sansa",
		"Test_QA_Tyrion",
		"Tradelab recrutement",
		"Ubisoft",
		"Yves Rocher"
	]
	public static List<String> advertiserIds = ["5002", "1574", "796", "4999", "758", "2161", "2558", "3018", "2683", "2268", "32", "1023", "3160", "4771", "4069", "4245", "4266", "4248", "4251", "4260", "4263", "4254", "4257", "2588", "647", "224"]


	@Keyword
	public void checkAdvertisersHaveData() {
		WebUI.delay(3)
		String baseUrl = WebUI.getUrl()
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver, baseUrl)

		String endMessage = ""

		int currentWindowId = 1
		for (advertiserId in advertiserIds) {
			selenium.openWindow(baseUrl.replaceFirst("\\/\\d+", "\\/" + advertiserId), "")
			WebUI.switchToWindowIndex(currentWindowId)
			WebUI.waitForElementPresent(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'),  30)
			currentWindowId++
		}
		currentWindowId = 1

		for (advertiserId in advertiserIds) {
			WebUI.switchToWindowIndex(currentWindowId)

			if(GlobalVariable.dateRange.equals("3") == false) {
				selectDateRange(GlobalVariable.dateRange)
				WebUI.waitForElementPresent(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'), 30)
				WebUI.waitForElementNotPresent(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'), Integer.valueOf(GlobalVariable.dateRange) * 60)
			} else if (selenium.isElementPresent(getSelector(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'))) == true ) {
				WebUI.waitForElementNotPresent(findTestObject('Object Repository/DV360 Optimizer/3. Grid/Loading Progress Bar'), 240)
			}

			if(selenium.isElementPresent("css=div.ag-overlay > div > div.ag-overlay-no-rows-wrapper > p") == false) {
				endMessage += advertiserNames[26 - currentWindowId] + " (" + advertiserIds[26 - currentWindowId] + ") has Data \n"
			}
			currentWindowId++
		}

		println endMessage
	}
}
