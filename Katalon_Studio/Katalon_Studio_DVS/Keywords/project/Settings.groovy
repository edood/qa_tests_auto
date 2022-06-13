package project
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.text.DateFormatSymbols
import java.util.List

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.DV360

import internal.GlobalVariable


public class CPSettings extends Builder
{
	String ID
	String Name
	String Status
	String Start
	String End
	String BudgetTotal
	String Goal
	String GoalKPI
	String GoalKPIValue
	String Currency

	CPSettings()
	{
		ID = ''
		Name = ''
		Status = 'inactive'
		BudgetTotal = '1'
		Goal = 'Raise awareness of my brand or product'
		GoalKPI = 'CPM'
		GoalKPIValue = '50'
		Currency = 'EUR'
	}

	CPSettings(String Name, String Status, String Start, String End, String BudgetTotal, String Goal, String GoalKPI, String GoalKPIValue)
	{
		this.ID = ''
		this.Name = Name
		this.Status = Status
		this.Start = Start
		this.End = End
		this.BudgetTotal = BudgetTotal
		this.Goal = Goal
		this.GoalKPI = GoalKPI
		this.GoalKPIValue = GoalKPIValue
	}

	CPSettings(CPSettings other)
	{
		this.ID = other.ID
		this.Name = other.Name
		this.Status = other.Status
		this.Start = other.Start
		this.End = other.End
		this.BudgetTotal = other.BudgetTotal
		this.Goal = other.Goal
		this.GoalKPI = other.GoalKPI
		this.GoalKPIValue = other.GoalKPIValue
		this.Currency = other.Currency
	}

	@Keyword
	def DV360VerifyUpload(CPSettings CSetup, String property)
	{
		/**
		 *  This function verify a property of a campaign on dv360
		 *  it searches the advertiser
		 *  it open the campaign settings
		 *  it verifies the selected property
		 *  property handled are "Name")
		 *  property CP Settings verifies all the properties
		 *
		 *  @param CSetup
		 *  the campaign object to verify
		 *
		 *  @param property
		 *  the name of the property to verify
		 */

		DV360 dv360 = new DV360()
		dv360.dv360SearchItem(GlobalVariable.advertiserName)
		WebUI.delay(5)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Item List/Filter input'), 10) == true
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Item List/Remove Filter button'))))
		{
			WebUI.click(findTestObject('10. DV360/Item List/Remove Filter button'))
			WebUI.delay(1)
			assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 10) == true
		}
		WebUI.setText(findTestObject('10. DV360/Item List/Filter input'), CSetup.Name)
		WebUI.sendKeys(findTestObject('10. DV360/Item List/Filter input'), Keys.chord(Keys.ENTER))
		WebUI.delay(3)
		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Campaign/Campaign list'), 30) == true : "No Campaign available"

		WebDriver driver = DriverFactory.getWebDriver()
		ArrayList<WebElement> campaigns = driver.findElements(By.cssSelector(findTestObject('Object Repository/10. DV360/Campaign/Campaign list').getSelectorCollection().get(SelectorMethod.CSS)))

		KeywordUtil.logInfo('checking id : ' + CSetup.ID)
		KeywordUtil.logInfo('checking name : ' + CSetup.Name)
		for (def elem in campaigns)
		{
			KeywordUtil.logInfo('checking elem : ' + elem.getText())
			if (elem.getText() == CSetup.Name)
			{
				WebUI.navigateToUrl(elem.getAttribute('href'))
				break;
			}
		}
		WebUI.delay(5)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Campaign/Campaign Settings button'), 30) == true : "Settings button not visible"
		WebUI.click(findTestObject('Object Repository/10. DV360/Campaign/Campaign Settings button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Campaign/Name'), 10) == true : "Name is not visible before timeout"
		switch(property)
		{
			case "CP Settings" :
				VerifyBasicSettings()
				VerifyAdvancedSettings()
				break
			case "Name" :
				dv360.VerifyName("Campaign", CSetup.Name)
				break
			case "Status" :
				dv360.VerifyStatus(CSetup.Status)
				break
			case "Budget" :
				dv360.VerifyTotalBudget(CSetup.BudgetTotal)
				break
			case "StartDate" :
				dv360.VerifyStartDate(CSetup.Start)
				break
			case "EndDate" :
				dv360.VerifyEndDate(CSetup.End)
			case "Currency" :
				dv360.VerifyCurrency(CSetup.Currency)
				break
		}
	}

	void VerifyBasicSettings()
	{
		/**
		 *  This function verify all the basic settings (name, status, budget, startdate, enddate) property of a campaign on dv360
		 */
		DV360 dv360 = new DV360()
		dv360.VerifyName("Campaign", this.Name)
		dv360.VerifyStatus(this.Status)
		dv360.VerifyTotalBudget(this.BudgetTotal)
		dv360.VerifyStartDate(this.Start)
		dv360.VerifyEndDate(this.End)
	}

	void VerifyAdvancedSettings()
	{
		/**
		 *  This function verify all the advanced settings (goal, goalkpi, goalkpivalue) property of a campaign on dv360
		 */
		DV360 dv360 = new DV360()
		dv360.VerifyGoal(this.goal)
		dv360.VerifyGoalKPI(this.goalKPI)
		dv360.VerifyGoalKPIValue(this.goalKPIValue)
	}

	@Keyword
	CPSettings createCampaignForCurrencyCheck(String itemName, String itemStatus, String itemBudget, String budgetdvcurrency, int startDateOffset, int endDateOffset)
	{
		/**
		 * Create new campaign for a specified currency
		 *
		 * @param itemName
		 * 		New campaign name
		 * @param itemStatus
		 * 		Status of the campaign
		 * @param itemBudget
		 * 		Campaign budget value
		 * @param budgetdvcurrency
		 * 		the currency accronym from dv360
		 * @param startDateOffset
		 * 		Campaign offset start date
		 * @param endDateOffset
		 * 		Campaign offset end date
		 * @return CPSettings
		 * The object containing the campaign information
		 */
		CPSettings CSetup = new CPSettings()

		assert WebUI.waitForElementVisible(findTestObject('3. Loader/Creator button'), 30)
		WebUI.click(findTestObject('3. Loader/Creator button'))
		selectCreatorDimension("Campaign")

		xsetOption(findTestObject('4. Creator/Campaign/Campaign status select'), itemStatus)
		xsetText(findTestObject('4. Creator/Campaign/Name'), itemName)

		String currency = WebUI.getText(findTestObject('4. Creator/Campaign/Campaign budget currency'))
		VerifyBuilderCurrency(null, budgetdvcurrency)

		xsetText(findTestObject('4. Creator/Campaign/Campaign budget'), itemBudget)
		Date date = new Date()
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date)
		if (startDateOffset > 0) calendar.add(Calendar.DATE, startDateOffset)
		String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()
		String startDateValue = calendar.get(Calendar.DAY_OF_MONTH) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR)

		modifyDate(startDateValue, "Campaign", "Start")

		Calendar calendarEnd = GregorianCalendar.getInstance()
		calendarEnd.setTime(date)
		calendarEnd.add(Calendar.DATE, endDateOffset)
		String endDateValue = ""
		int endDate = calendarEnd.get(Calendar.DAY_OF_MONTH)

		if (endDate > 0 && endDate <= 31)
		{
			endDateValue = endDate + " " + months[calendarEnd.get(Calendar.MONTH)] + " " + calendarEnd.get(Calendar.YEAR)
			modifyDate(endDateValue, "Campaign", "End")
		}

		WebDriver driver = DriverFactory.getWebDriver()
		int oldNumberOfLoadedItems = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/Loaded Items').getSelectorCollection().get(SelectorMethod.CSS))).size()
		WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))
		int newNumberOfLoadedItems = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/Loaded Items').getSelectorCollection().get(SelectorMethod.CSS))).size()
		assert newNumberOfLoadedItems == oldNumberOfLoadedItems + 1
		KeywordUtil.markPassed("Campaign " + itemName + " created")
		WebUI.click(changeSelector(findTestObject('6. Objects/First Loaded Object name'),"nth-child\\(\\d\\)","nth-child("+newNumberOfLoadedItems+")"))

		CSetup.Name = itemName
		CSetup.Status = itemStatus
		CSetup.BudgetTotal = itemBudget
		CSetup.Start = calendar.getTime().format('MM/dd/yyyy') + " 00:00"
		CSetup.End = calendarEnd.getTime().format('MM/dd/yyyy') + " 23:59"
		CSetup.Currency = currency

		return CSetup
	}

	@Keyword
	String getID(TestObject object)
	{
		/**
		 *  This function return the ID of an object
		 *  @param object
		 *  The object to get the ID
		 *  @return String
		 *  The id of the object
		 */
		WebUI.click(object)
		WebUI.delay(1)
		String newCampaignId = WebUI.getText(changeSelector(object, "nth-child\\(1\\)", "nth-child(" + 2 + ")")).replaceFirst(".*\\((\\d+)\\)", "\$1")
		KeywordUtil.logInfo('campaign ID = ' + newCampaignId)
		return newCampaignId
	}

	@Keyword
	void VerifyBuilderCurrency(TestObject mode, String correctcurrency)
	{
		/**
		 *  This function verify the currency on the builder of a campaign
		 *  @param mode
		 *  The testObject to select before testing the currency (creator button or CP loaded or null)
		 *  @param correctcurrency
		 *  The currency acronyme from dv360
		 */

		if (mode != null)
		{
			WebUI.click(mode)
			WebUI.delay(1)
		}

		String budgetbuildercurrency = WebUI.getText(findTestObject('4. Creator/Campaign/Campaign budget currency'))
		DV360 dv360 = new DV360()
		budgetbuildercurrency = dv360.convertcurrencydv360(budgetbuildercurrency)

		assert budgetbuildercurrency == correctcurrency : budgetbuildercurrency + " currency is different than dv currency " + correctcurrency
	}
}

public class LineSettings extends Builder
{
	String Name
	String Status
	Date Start
	Date End
	String BudgetType
	String LifetimeBudget
	String Pacing
	String PacingRate
	String PacingAmount
	String BidStrategyType
	String BidStrategyValue
	String BidStrategyUnit
	String BidStrategyLimit
	boolean Frequency
	String Exposure
	String Per
	String Period

	LineSettings(String Linename)
	{
		this.Name = Linename
	}

	@Keyword
	def ShareSettings(String propertyname, TestObject sharedfrom, String sharedto)
	{
		/**
		 * This function share a setting from an object to another
		 *
		 * @param propertyname
		 * the name of the setting to be shared
		 *
		 * @param sharedfrom
		 * the object the property will be shared from
		 *
		 * @param sharedto
		 * the name or ID of the object to receive the property
		 **/
		WebUI.click(sharedfrom)
		WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/Share Settings'))
		WebUI.setText(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/Search Property'), propertyname)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/First property'), 5) == true : "Property Name was not found"
		WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/First property'))

		WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/Search OI'))
		WebUI.setText(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/Search OI'), sharedto)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/First Insertion Orders'), 5) == true : "OI was not found"
		WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/First Insertion Orders'))
		WebUI.click(findTestObject('Object Repository/5. Editor/Apply button'))
	}
}

public class OISettings extends Builder
{
	String Name
	String Status
	ArrayList<String> budget = []
	String PerformanceGoalType
	String PerformanceGoalValue
	String Pacing

	String PacingRate
	String PacingAmount
	boolean Frequency
	String FrequencyExposure
	String Per
	String Period

	ArrayList<String> display = []
	ArrayList<String> OIToArchive = []

	OISettings(String OIname)
	{
		Name = OIname
		Status = "Active"
		PerformanceGoalType = "CPM"
		PerformanceGoalValue = "2"
		Pacing = "Daily"
		PacingRate = "Even"
		PacingAmount = "2"
		Frequency = true
		FrequencyExposure = ""
		Per = ""
		Period = ""
	}

	def SetPerformance(ArrayList<String> GoalKpi, List<String> Performance, String record_the_name)
	{
		/**
		 * this function set the performance
		 * if performance parameters are set it will create a specific performance
		 * if performance parameters are not set it will create all possible performance kpi
		 * 
		 * @param GoalKpi
		 * An array of the goal kpi possible in the builder
		 * 
		 * @param Performance
		 * A list of parameter for the performance to be set [goal, goalkpi, value]
		 * 
		 * @param record_the_name
		 * The name of the OI to be archived
		 */
		int record_int
		double record_value
		Random kpivalue = new Random()
		String record_the_value
		String record_the_type

		KeywordUtil.logInfo('setPerformance begin: name = ' + record_the_name)
		KeywordUtil.logInfo('goal = ' + Performance[0])
		KeywordUtil.logInfo('goalkpi = ' + Performance[1])
		KeywordUtil.logInfo('goalkpivalue = ' + Performance[2])
		if (GlobalVariable.Performance[1] != '')
		{
			WebUI.click(findTestObject('Object Repository/6. Objects/Second OI'))
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))
			modifyIoFieldValue('performance goal type', Performance[1])
			WebUI.clearText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'))

			if ((Performance[2]) != '')
			{
				modifyIoFieldValue('performance goal value', Performance[2])
				record_the_value = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), 'value')
			}
			else if (Performance[1] != 'Other')
			{
				record_int = kpivalue.nextInt(100)
				modifyIoFieldValue('performance goal value', record_int.toString())
				record_the_value = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), 'value')
			}
			else if (Performance[1] == 'Other')
			{
				modifyIoFieldValue('performance goal value', Performance[2])
				record_the_value = Performance[2]
			}

			record_the_type = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), 'value')
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))

			assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), 'value') == (Performance[1]) : 'Selected goalKPI is different'

			if ((Performance[1]) == (GoalKpi[7]))
			{
				assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), 'value') == (Performance[2]) : 'Kpi value has changed'
			}
			else if ((Performance[2]) != '')
			{
				assert Double.parseDouble(Performance[2]).round(2) == Double.parseDouble(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), 'value')) : 'Kpi value has changed'
			}
			else
			{
				assert record_int == Double.parseDouble(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), 'value')) : 'Kpi value has changed'
			}
			OIToArchive.push(record_the_name)
			display.push(record_the_type)
			display.push(record_the_value)
		}
		else
		{
			for (int j = 0; j < GoalKpi.size(); j++)
			{
				changeSelector(findTestObject('Object Repository/6. Objects/Second OI'), 'nth-child\\(2\\)', ('nth-child(' + (j + 3) + ')'))
				QuickDuplicate('OI', findTestObject('Object Repository/6. Objects/Second OI'))
				WebUI.click(findTestObject('Object Repository/6. Objects/Second OI'))
				modifyIoFieldValue('name', 'QAP-225 OI auto_' + new Date().getTime().toString())
				OIToArchive.push(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Insertion Order Name'), 'value'))
				WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))

				modifyIoFieldValue('performance goal type', GoalKpi[j])
				record_value = kpivalue.nextDouble()
				record_int = kpivalue.nextInt(100)
				WebUI.clearText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'))
				if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'),	'value') != 'Other' && Performance[2] != '')
				{
					modifyIoFieldValue('performance goal value', Performance[2])
				}
				else if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), 'value') != 'Other' && Performance[2] == '')
				{
					modifyIoFieldValue('performance goal value', record_value.toString())
				}
				else if (Performance[2] != '')
				{
					modifyIoFieldValue('performance goal value', Performance[2])
				}
				WebUI.click(findTestObject('Object Repository/6. Objects/Second OI'))
				assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), 'value') == (GoalKpi[j]) : 'Selected goalKPI is not recognised'

				if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), 'value') != 'Other' && Performance[2] == '')
				{
					assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), 'value') == String.format(Locale.US, '%.02f', record_value.round(2)) : 'Kpi value has changed'
				}
				else if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), 'value') != 'Other' && Performance[2] != '')
				{
					assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), 'value') == String.format(Locale.US, '%.02f', Double.parseDouble(Performance[2]).round(2)) : 'Kpi value has changed'
				}
				else if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), 'value') == 'Other')
				{
					assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), 'value') == Performance[2] : 'Kpi Other is different'
				}

				display.push(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), 'value'))
				if (GoalKpi[j] == 'Other')
				{
					display.push(Performance[2])
				}
				else if (GoalKpi[j] != 'Other')
				{
					display.push(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), 'value'))
				}
			}
		}
	}

	def EditSetting(TestObject selector)
	{
		/**
		 * This function edit the settings of an OI
		 * Change the end date of the budget interval to in the present
		 * Change performance to CPM 2
		 * Change pacing to daily even 2
		 * Change the frequency
		 * Change the name to QAWaveYoutube
		 * 
		 * @param selector
		 * the testobject to be edited
		 */
		int randomIndex = 0

		WebUI.click(selector)

		this.renameObj(selector, 'QAWaveYoutube', 'OI')

		//Modify Budget Interval StarDate
		Date date = new Date()
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date)
		String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()
		String startDateValue = calendar.get(Calendar.DAY_OF_MONTH) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR)
		modifyDate(startDateValue, 'Insertion Order', 'Start')

		//Modify Budget Interval EndDate
		Calendar calendarEnd = GregorianCalendar.getInstance()
		calendarEnd.setTime(date)
		calendarEnd.add(Calendar.DATE, 1)
		String endDateValue = ""
		int endDate = calendarEnd.get(Calendar.DAY_OF_MONTH)
		if (endDate > 0 && endDate <= 31) {
			endDateValue = endDate + " " + months[calendarEnd.get(Calendar.MONTH)] + " " + calendarEnd.get(Calendar.YEAR)
			this.modifyDate(endDateValue, 'Insertion Order', 'End')
		}

		WebDriver driver = DriverFactory.getWebDriver()
		ArrayList<WebElement> budgets = driver.findElements(By.cssSelector(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/All Budget objects').getSelectorCollection().get(SelectorMethod.CSS)))
		int j = 0
		for (def elemline in budgets)
		{
			int i = 0
			for (; i < 3; i++)
			{
				if (i == 0)
				{
					int tmp1 = i + 1
					int tmp2 = j + 2
					this.tmp = this.changeSelector(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget by interval'), "div:nth-child\\(0\\) > div:nth-child\\(0\\)", "div:nth-child("+ tmp2 +") > div:nth-child("+ tmp1 +")")
				}
				else
				{
					int tmp1 = i + 1
					int tmp2 = j + 2
					this.tmp = this.changeSelector(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget Date by interval'), "div:nth-child\\(0\\) > div:nth-child\\(0\\)", "div:nth-child("+ tmp2 +") > div:nth-child("+ tmp1 +")")
				}
				this.budget[j * 3 + i] = WebUI.getAttribute(this.tmp, 'value')
			}
			j++
		}

		WebUI.doubleClick(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))

		//Performance goal
		modifyIoFieldValue('performance goal type', PerformanceGoalType)
		modifyIoFieldValue('performance goal value', PerformanceGoalValue)

		//Pacing
		modifyIoFieldValue('pacing', Pacing)
		modifyIoFieldValue('pacing rate', PacingRate)
		modifyIoFieldValue('daily budget', '2.3')
		this.PacingAmount = 2.3

		// Switch Insertion Order Frequency Capping
		boolean frequencyCappingToCheck =
				WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping switch'), "class")
				.contains("ivu-switch-checked")
		if (frequencyCappingToCheck == true) {
			String frequencyExposuresToCheck = "0"
			String frequencyPeriodToCheck = "Minutes"
			String frequencyAmountToCheck = "0"
			String currentPeriod = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping period select'), "value")
			if(currentPeriod.equals("Lifetime")) {
				randomIndex = new Random().nextInt((4 - 0) + 1) + 1
			} else {
				randomIndex = 5
			}
			xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping period select'), randomIndex, 'index')
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping exposure input'))
			frequencyPeriodToCheck = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping period select'), "value")
			int exposure = Integer.parseInt(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping exposure input'), "value")) + 1
			frequencyExposuresToCheck = exposure.toString()
			modifyIoFieldValue('exposure', frequencyExposuresToCheck)
			assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping exposure input'), "value") == frequencyExposuresToCheck
			if(randomIndex != 5) {
				frequencyAmountToCheck = (Integer.parseInt(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping per input'), "value")) + 1).toString()
				modifyIoFieldValue('per', frequencyAmountToCheck)
				assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping per input'), "value") == frequencyAmountToCheck
				this.Per = frequencyAmountToCheck
			}
			this.FrequencyExposure = frequencyExposuresToCheck
			this.Period = frequencyPeriodToCheck
		} else {
			this.Frequency = false
		}
	}

	@Keyword
	def ShareSettings(String propertyname, TestObject sharedfrom, String sharedto)
	{
		/**
		 * This function share a setting from an object to another
		 * 
		 * @param propertyname
		 * the name of the setting to be shared
		 * 
		 * @param sharedfrom
		 * the object the property will be shared from
		 * 
		 * @param sharedto
		 * the name or ID of the object to receive the property
		 **/
		WebUI.click(sharedfrom)
		WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/Share Settings'))
		xsetText(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/Search Property'), propertyname)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/First property'), 5) == true : "Property Name was not found"
		WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/First property'))

		xsetText(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/Search OI'), sharedto)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/First Insertion Orders'), 5) == true : "OI was not found"
		WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/1. Share Settings/First Insertion Orders'))
		WebUI.click(findTestObject('Object Repository/5. Editor/Apply button'))
	}

	@Keyword
	def DV360VerifyUpload(String ID, String property)
	{
		/**
		 *  This function verify a property of an OI on dv360
		 *  it searches the insertion order
		 *  it open the insertion order tab
		 *  it verifies the selected property
		 *  property handled are "Name, Pacing, PacingRate, PacingAmount, PerformanceGoalType, Frequency, FrequencyExposure, Period)
		 *  property OI Settings verifies all the properties
		 *
		 *  @param ID
		 *  the ID of the OI to verify
		 *
		 *  @param property
		 *  the name of the property to verify
		 */

		DV360 dv360 = new DV360()
		dv360.dv360SearchItem(GlobalVariable.advertiserName)
		WebUI.click(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/OI list'), 30) == true : "No OI available"

		assert WebUI.waitForElementVisible(findTestObject('10. DV360/Item List/Filter input'), 10) == true
		if(selenium.isElementPresent(getSelector(findTestObject('10. DV360/Item List/Remove Filter button')))) {
			WebUI.click(findTestObject('10. DV360/Item List/Remove Filter button'))
			WebUI.delay(1)
			assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Loading bar'), 10) == true
		}

		WebUI.sendKeys(findTestObject('Object Repository/10. DV360/Item List/Filter input'), ID)
		WebUI.sendKeys(findTestObject('Object Repository/10. DV360/Item List/Filter input'), Keys.chord(Keys.ENTER))
		Thread.sleep(500)
		assert WebUI.verifyElementNotPresent(findTestObject('10. DV360/Filter loader'), 30)
		WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/10. DV360/Pagination/OI name'))+"').click()", null)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/Line Items tab'), 60) == true : "Timeout loading OI page"
		WebUI.executeJavaScript("document.querySelector('" + getOnlySelector(findTestObject('Object Repository/10. DV360/Insertion Order/Insertion Order tab')) + "').click()", null)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. DV360/Insertion Order/name'), 60) == true : "OI tab not accessible before timeout"
		switch(property)
		{
			case "OI Settings" :
				dv360.VerifyName("Insertion Order", this.Name)
				dv360.VerifyBudget(this.budget)
				dv360.VerifyPacing(this.Pacing)
				dv360.VerifyPacingRate(this.PacingRate)
				dv360.VerifyPacingAmount(this.PacingAmount)
				dv360.VerifyPerformanceGoalType(this.PerformanceGoalType)
				dv360.VerifyPerformanceValue(this.PerformanceGoalValue, this.PerformanceGoalType)
				dv360.VerifyFrequency(this.Frequency)
				if(this.Frequency == true) {
					dv360.VerifyFrequencyExposure(this.FrequencyExposure)
					dv360.VerifyPeriod(this.Period)
				}
				break
			case "Name" :
				dv360.VerifyName("Insertion Order", this.Name)
				break
			case "Budget" :
				dv360.VerifyBudget(this.budget)
				break
			case "Pacing" :
				dv360.VerifyPacing(this.Pacing)
				break
			case "Pacing Rate" :
				dv360.VerifyPacingRate(this.PacingRate)
				break
			case "Pacing Amount" :
				dv360.VerifyPacingAmount(this.PacingAmount)
				break
			case "PerformanceGoalType" :
				dv360.VerifyPerformanceGoalType(this.PerformanceGoalType)
				break
			case "PerformanceGoalValue" :
				dv360.VerifyPerformanceValue(this.PerformanceGoalValue, this.PerformanceGoalType)
				break
			case "Frequency" :
				dv360.VerifyFrequency(this.Frequency)
				break
			case "FrequencyExposure" :
				dv360.VerifyFrequencyExposure(this.FrequencyExposure)
				break
			case "Period" :
				dv360.VerifyPeriod(this.Period)
				break
		}
	}
}


