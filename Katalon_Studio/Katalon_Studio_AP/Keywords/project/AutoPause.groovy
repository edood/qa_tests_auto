package project

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.jboss.aerogear.security.otp.Totp
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.Common
import com.reusableComponents.DV360
import com.reusableComponents.Gmail
import com.reusableComponents.Grafana

import internal.GlobalVariable

public class AutoPause extends Common
{
	ArrayList<WebElement> listIds = new ArrayList<WebElement>()
	ArrayList<String> listIdsText = new ArrayList<String>()

	ArrayList<WebElement> listAnalysisPeriods = new ArrayList<WebElement>()
	ArrayList<String> listAnalysisPeriodsText = new ArrayList<String>()

	ArrayList<WebElement> listNames = new ArrayList<WebElement>()
	ArrayList<String> listNamesText = new ArrayList<String>()

	ArrayList<WebElement> listBudgets = new ArrayList<WebElement>()
	ArrayList<String> listBudgetsText = new ArrayList<String>()

	ArrayList<WebElement> listThresholds = new ArrayList<WebElement>()
	ArrayList<String> listThresholdsText = new ArrayList<String>()

	ArrayList<WebElement> listHiddenElements = new ArrayList<WebElement>()

	ArrayList<WebElement> listAdvertisers = new ArrayList<WebElement>()
	ArrayList<String> listAdvertisersText = new ArrayList<String>()

	ArrayList<WebElement> listRecipients = new ArrayList<WebElement>()
	ArrayList<String> listRecipientsText = new ArrayList<String>()

	@Keyword
	public String Start(boolean newbrowser=true)
	{
		/**
		 * Log in to Auto Pause for specified environment and select partner
		 *
		 * @return baseUrl
		 */
		String baseUrl = 'https://' + GlobalVariable.env

		if (newbrowser == true)
		{
			WebUI.openBrowser(baseUrl)
			WebUI.maximizeWindow()
			login(GlobalVariable.googleID, GlobalVariable.googlePassword, GlobalVariable.checkHref)
		}
		else
		{
			openNewTab()
			gotoautopause()
			assert WebUI.waitForElementVisible(findTestObject('1. Login/button'), 30) == true: "Timeout: Login button not visible after 30 sec"
			WebUI.click(findTestObject('1. Login/button'))
			WebUI.delay(2)
		}
		assert WebUI.waitForElementPresent(findTestObject('2. Schedule List/Partner input'), 10)
		assert WebUI.waitForElementNotPresent(findTestObject('2. Schedule List/Progress bar'), 60)

		selectPartner(GlobalVariable.partnerName)
		Thread.sleep(250)
		assert WebUI.waitForElementNotPresent(findTestObject('2. Schedule List/Progress bar'), 30)
		return baseUrl
	}

	@Keyword
	def gotoautopause()
	{
		WebUI.navigateToUrl('https://' + GlobalVariable.env)
	}

	@Keyword
	def Googleauthentificator()
	{
		/**
		 * This function log in google with the 2FA google authentificator
		 * the global variable GoogleAuthentificatorKey is required
		 */

		WebUI.maximizeWindow()
		assert WebUI.waitForElementVisible(findTestObject('0. Login Google/2FA header'), 30) : "2FA popup not visible"
		KeywordUtil.logInfo('2FA header = ' + WebUI.getText(findTestObject('0. Login Google/2FA header')))
		assert WebUI.getText(findTestObject('0. Login Google/2FA header')) == 'Validation en deux étapes' : "Connection failed"
		WebUI.delay(3)
		selenium = getSelenium()
		if (selenium.isElementPresent(getSelector(findTestObject('0. Login Google/Try another way span'))) == true)
		{
			String result = WebUI.getText(findTestObject('0. Login Google/Try another way span'))
			if (result == 'Essayer une autre méthode')
			{
				KeywordUtil.logInfo('authentificator not visible')
				assert WebUI.verifyElementVisible(findTestObject('0. Login Google/Try another way'))
				WebUI.click(findTestObject('0. Login Google/Try another way'))
				assert WebUI.waitForElementVisible(findTestObject('0. Login Google/Google Authentificator'), 30) : "The connection google authentificator is not available for this account"
			}
			KeywordUtil.logInfo('result = ' + result)
		}
		ArrayList<WebElement> ConnectionMethod = driver.findElements(By.cssSelector(findTestObject('0. Login Google/other authentification method').getSelectorCollection().get(SelectorMethod.CSS)))
		for (def elem in ConnectionMethod)
		{
			if (elem.getText().contains('Google Authenticator') == true)
				elem.click()
		}
		assert WebUI.waitForElementVisible(findTestObject('0. Login Google/input tel'), 30) : "Input to submit code is not visible"
		try
		{
			Totp auth = new Totp(GlobalVariable.GoogleAuthentificatorKey)
			String code = auth.now()
			KeywordUtil.logInfo('code to submit = ' + code)
			WebUI.sendKeys(findTestObject('0. Login Google/input tel'), code)
			WebUI.click(findTestObject('0. Login Google/Totp next'))
			WebUI.delay(3)
		}
		catch (Exception e)
		{
			KeywordUtil.logInfo(e.getMessage())
		}
	}

	@Keyword
	def googleSignIn(String id, String pwd)
	{
		/**
		 * This function log the user with google sign in
		 * @param id
		 * The google id to be set
		 * @param pwd
		 * The google password to be set
		 */

		assert WebUI.waitForElementVisible(findTestObject('0. Login Google/popup email'), 30) : "Email popup is not visible"
		WebUI.setText(findTestObject('0. Login Google/Email input'), id)
		WebUI.click(findTestObject('0. Login Google/Next'))
		WebUI.waitForElementPresent(findTestObject('0. Login Google/Password type password'), 15)
		WebUI.delay(1)
		WebUI.setEncryptedText(findTestObject('0. Login Google/Password type password'), pwd)
		WebUI.click(findTestObject('0. Login Google/Password next button'))
		WebUI.delay(5)
	}

	@Keyword
	def login(String id, String pwd, boolean tip)
	{
		/**
		 * this function log the user in the autopause
		 * @param id
		 * The id of the user
		 * @param pwd
		 * The encrypted password of the user
		 * @param tip
		 * Define if the tip link should be checked
		 */

		if (tip == true)
		{
			KeywordUtil.logInfo('Checking login tip...')
			assert WebUI.waitForElementVisible(findTestObject('1. Login/tip'), 5) : "Tip message is not displayed after 5 second"
			String tiptext = WebUI.getText(findTestObject('1. Login/tip'))
			assert tiptext.trim() == "If you can’t access the page, please contact the authentifier team on the Slack Channel #prod_authentifier_support or send an email by clicking here." : "tip message has changed"
			assert WebUI.verifyElementClickable(findTestObject('1. Login/Slack channel link')) : "The link prod authentifier support is not clickable"
			assert WebUI.verifyElementAttributeValue(findTestObject('1. Login/Slack channel link'), 'href', 'slack://channel?team=T4A2YGWSF&id=CNTQWDJQP', 5) : "Channel Slack link is not correct"
			assert WebUI.verifyElementClickable(findTestObject('1. Login/here link')) : "The email link is not clickable"
			assert WebUI.verifyElementAttributeValue(findTestObject('1. Login/here link'), 'href', 'mailto:authentifier@jellyfish.com?subject=Add%20my%20email%20to%20Authentifier&body=Hello,%0D%0A%0D%0Aplease%20add%20me%20to%20the%20Authentifier%20in%20order%20to%20access%20%22Autopause%22.%0D%0A%0D%0AKind%20Regards,', 5) : "Email link is not correct"
		}

		driver = DriverFactory.getWebDriver()
		String parentWindow = driver.getWindowHandle()
		WebUI.click(findTestObject('1. Login/button'))
		Set<String> handles = driver.getWindowHandles()
		for(String windowHandle  : handles)
		{
			if(!windowHandle.equals(parentWindow))
			{
				driver.switchTo().window(windowHandle)
				WebUI.delay(3)
				handles = driver.getWindowHandles()
				if (handles.size() == 1)
				{
					driver.switchTo().window(windowHandle)

					WebUI.delay(3)
					handles = driver.getWindowHandles()
					if (handles.size() == 1)
					{
						driver.switchTo().window(parentWindow)
						return
					}
					googleSignIn(id, pwd)
					driver.switchTo().window(parentWindow);
					handles = driver.getWindowHandles()
					if (handles.size() == 1)
						return
					driver.switchTo().window(windowHandle);
					Googleauthentificator()
					driver.switchTo().window(parentWindow);
				}
				googleSignIn(id, pwd)
				driver.switchTo().window(parentWindow)
				handles = driver.getWindowHandles()
				if (handles.size() == 1)
					return
				driver.switchTo().window(windowHandle)
				Googleauthentificator()
				driver.switchTo().window(parentWindow)
			}
		}

		assert WebUI.waitForElementVisible(findTestObject('1. Login/Autopause title'), 30) : "Login failed"
	}

	@Keyword
	def selectPartner(String partnerName)
	{
		/**
		 * Select a partner
		 * @param partnerName		The name of the partner to be selected
		 */
		Thread.sleep(1500)
		assert WebUI.waitForElementNotPresent(findTestObject('2. Schedule List/Progress bar'), 15)
		Thread.sleep(1500)
		assert WebUI.waitForElementNotPresent(findTestObject('2. Schedule List/Progress bar'), 15)
		Thread.sleep(1500)
		assert WebUI.waitForElementNotPresent(findTestObject('2. Schedule List/Progress bar'), 15)
		assert WebUI.waitForElementVisible(findTestObject('2. Schedule List/Partner input'), 30) == true
		WebUI.clearText(findTestObject('2. Schedule List/Partner input'))
		WebUI.sendKeys(findTestObject('2. Schedule List/Partner input'), partnerName)
		assert WebUI.waitForElementVisible(findTestObject('2. Schedule List/Partner Input autocomplete'), 30) : "Timeout: partner input is not visible after 30sec"
		WebUI.click(findTestObject('2. Schedule List/Partner Input autocomplete'))
		assert WebUI.getAttribute(findTestObject('2. Schedule List/Partner input'), 'value').contains(partnerName) == true : "The selected partner is not correct"
		WebUI.delay(2)
	}

	@Keyword
	def logout()
	{
		/**
		 * Log out of the Auto Pause
		 */
		WebUI.click(findTestObject('Object Repository/1. Login/User menu'))
		assert WebUI.waitForElementVisible(findTestObject('1. Login/Logout button'), 5) : "Logout button is not visible after 5 seconds"
		WebUI.click(findTestObject('1. Login/Logout button'))
	}

	@Keyword
	def returnToAutoPause()
	{
		/**
		 * Return to Autopause (first tab) and refresh the page
		 */

		WebUI.switchToWindowIndex(0)
		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		Thread.sleep(500)
		assert WebUI.waitForElementNotPresent(findTestObject('2. Schedule List/Progress bar'), 30)
	}

	def setRuleCPsActive(Map currentMap)
	{
		/**
		 * Set CPs from a map to Active if the "Apply On" key is set to "Insertion Order"
		 *
		 * @param currentMap
		 */

		if(currentMap["Apply on"] == "Insertion Order")
		{
			DV360 dv360 = new DV360()

			openNewTab()
			dv360.goToDv360()
			dv360.setCPsActive(currentMap['Campaigns'].split(","))
			closeNewTab()
		}
	}

	@Keyword
	def setRuleIOsActive(String ruleItems, boolean go_to = true)
	{
		/**
		 * Set IOs from a map to Active if the "Apply On" key is set to "Insertion Order"
		 * 
		 * @param ruleItems			Items to activate
		 * @param go_to				true to go to DV360 in a new tab, false if DV360 is already opened
		 */

		DV360 dv360 = new DV360()
		if (go_to == true)
		{
			openNewTab()
			dv360.goToDv360()
		}

		dv360.setIosActive(ruleItems.split(","), go_to)

		if (go_to == true)
			closeNewTab()
	}

	@Keyword
	String createNewRule(
			String startDate = GlobalVariable.ruleCreate['Start Date'],
			String endDate = GlobalVariable.ruleCreate['End Date'],
			boolean emailwarning = GlobalVariable.ruleCreate['Warning Email'],
			String status = GlobalVariable.ruleCreate['Status'],
			String applyOn = GlobalVariable.ruleCreate['Apply on'],
			String currency = GlobalVariable.ruleCreate['Currency'],
			String budgetValue = GlobalVariable.ruleCreate['Budget Value'],
			String budgetType = GlobalVariable.ruleCreate['Budget Type'],
			String threshold = GlobalVariable.ruleCreate['Threshold'],
			String ruleName = GlobalVariable.ruleCreate['Rule Name'],
			String dataConditions = GlobalVariable.ruleCreate['Condition'],
			String additionalRecipient = null,
			boolean pauseOthersCurrencies = GlobalVariable.ruleCreate['PauseOthersCurrencies']
			)
	{
		/**
		 * Creates a new rule based on the user profile or parameters
		 * Check the rule is created properly
		 * Check the display in the list
		 *
		 * @param startDate					Start date of the new rule
		 * @param endDate					End date of the new rule
		 * @param emailwarning				'true' to check the warning email option, else leave it unchecked
		 * @param status					Status of the new rule
		 * @param currency					Currency of the new rule
		 * @param budgetValue				Budget value of the new rule
		 * @param budgetType				Budget Type of the new rule
		 * @param threshold					Threshold value of the new rule
		 * @param ruleName					Name of the rule to create
		 * @param dataConditions			Text version of conditions to set for partner rules
		 * @param additionalRecipient		Custom recipient
		 * @param PauseOthersCurrencies		'true' to check the autopause delivery other currency option, else leave it unchecked
		 *
		 * @return ruleName
		 */

		selenium = getSelenium()
		WebUI.click(findTestObject('Object Repository/2. Schedule List/New rule button'))

		//Apply on
		if (applyOn == 'Advertiser' || applyOn == 'Insertion Order')
		{

			WebUI.click(findTestObject('3. Schedule Editor/Apply on '+applyOn+' button'))

			// Currency
			assert WebUI.waitForElementVisible(findTestObject('3. Schedule Editor/Advertiser Currency Select button'), 5)
			assert WebUI.waitForElementNotPresent(findTestObject('3. Schedule Editor/Progress bar'), 60)
			WebUI.click(findTestObject('3. Schedule Editor/Advertiser Currency Select button'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Schedule Editor/Advertiser currency EUR css'), 10)
			TestObject currencyOption = changeSelectorXpath(findTestObject('3. Schedule Editor/Advertiser Currency option'), "EUR", currency)
			assert WebUI.waitForElementVisible(currencyOption, 10)
			WebUI.click(currencyOption)

			// Budget
			WebUI.sendKeys(findTestObject('3. Schedule Editor/Budget input'), budgetValue)
			TestObject budgetTypeObject = changeSelectorXpath(findTestObject('3. Schedule Editor/Budget Type'), "Amount", budgetType)

			WebUI.click(budgetTypeObject)

			selectAdvertiserItems()

		} else if (applyOn == 'Partner')
		{
			WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Apply on Partner button'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Budget input partner'), 10)
			selectConditions(dataConditions)
			if (pauseOthersCurrencies == true)
				WebUI.click(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Pause Other Currencies input'))
		}

		// Analysis period
		String tabLandedStart = modifyDate(startDate, "Start", "Create")
		String tabLandedEnd = modifyDate(endDate, "End", "Create")

		// Threshold
		WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Threshold input'), threshold)

		// Rule name
		ruleName += "_" + new Date().getTime()
		WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Rule Name input'), ruleName)

		// Recipients
		for(String email in GlobalVariable.ruleCreate['Recipients'].split(",")) {
			WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), email)
			WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), Keys.chord(Keys.ENTER))
		}

		if(additionalRecipient != "" && additionalRecipient != null && additionalRecipient != GlobalVariable.googleID) {
			WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), additionalRecipient)
			WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), Keys.chord(Keys.ENTER))
		}

		// Warning Email option
		if(emailwarning == true)
			WebUI.click(findTestObject('Object Repository/3. Schedule Editor/warningemail checkbox'))

		//Status
		WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Status select button'))
		WebUI.waitForElementVisible(changeSelectorXpath(findTestObject('Object Repository/3. Schedule Editor/Status option'),"Active", status), 3)
		WebUI.click(changeSelectorXpath(findTestObject('Object Repository/3. Schedule Editor/Status option'),"Active", status))

		Map element = ["Currency": currency, "Budget Value": budgetValue, "Budget Type": budgetType, "Threshold": threshold, "Start Date": startDate, "End Date": endDate, "Rule Name": ruleName, "Recipients": GlobalVariable.ruleCreate['Recipients'], "Advertisers": GlobalVariable.ruleCreate['Advertisers'], "Campaigns": GlobalVariable.ruleCreate['Campaigns'], "Insertion Orders": GlobalVariable.ruleCreate['Insertion Orders']]

		checkInputs(element)

		// Save ruleCreate & check display
		WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Save Rule button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Passed Tab button'), 20) : "Rule list is not visible after 20 seconds"
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/2. Schedule List/Loader'), 10) : "The rule list is still loading after 10 seconds"
		if(tabLandedEnd == "Passed") {
			if(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Passed Tab Active'))))
				searchRuleInTabsAndStop(ruleName, "Passed")
		} else if (tabLandedStart == "Future") {
			if(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Future Tab Active'))))
				searchRuleInTabsAndStop(ruleName, "Future")
		} else {
			if(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Scheduled Tab Active'))))
				searchRuleInTabsAndStop(ruleName, "Scheduled")
		}

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Rule Cells'), 30)
		while(selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Next page button'))) && !selenium.isElementPresent(getSelector(changeSelectorXpath(findTestObject('2. Schedule List/Created Rule Name'), "ruleName", ruleName)))) {
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Next page button'))
			Thread.sleep(500)
		}
		assert WebUI.verifyElementPresent(changeSelectorXpath(findTestObject('2. Schedule List/Created Rule Name'), "ruleName", ruleName), 1) : "Created rule "+ruleName+" was not found"
		WebUI.focus(changeSelectorXpath(findTestObject('2. Schedule List/Created Rule Name'), "ruleName", ruleName))
		WebUI.sendKeys(findTestObject('Object Repository/2. Schedule List/Search Bar input'), ruleName)
		Thread.sleep(750)

		if (applyOn == 'Advertiser' || applyOn == 'Insertion Order')
		{
			// Check Spend is NA
			assert WebUI.getText(findTestObject('2. Schedule List/1. Table/Spends')) == "NA"
			verifyBudget(budgetType, currency, budgetValue)
		}
		else if (applyOn == 'Partner')
		{
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Spend popup'))
			// Check Spend is NA
			for (int i=0; i<dataConditions.split(";").size();i++)
			{
				String NAValue = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/Spend NA'))+"').item("+i+").textContent", null)
				assert NAValue == "NA": "Spend display should be NA"
			}
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Spend close button'))
			WebUI.delay(1)
		}
		// Check status icon
		if(tabLandedEnd != "Passed") {
			if(status == "Active") {
				assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/First Rule Status icon')) == "fiber_manual_record"
			} else {
				assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/First Rule Status icon')) == "pause"
			}
		}
		WebUI.sendKeys(findTestObject('Object Repository/2. Schedule List/Search Bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
		Thread.sleep(500)

		return ruleName

	}

	String modifyDate(String date, String startOrEnd, String currentMap)
	{
		/**
		 * 	Modifies start or end date depending on second parameter
		 * 
		 * @param date 				Date to set. Format : MM/dd/YYYY (ie. "1/12/2021")
		 * @param startOrEnd		Type of date to modify : "Start" or "End"
		 * 
		 * @return tabLanded
		 */

		def selenium = getSelenium()
		String tabLanded = ""
		WebUI.click(findTestObject('Object Repository/3. Schedule Editor/'+startOrEnd+' Date button'))
		if(startOrEnd == "Start" && GlobalVariable.ruleCreate['Start Date'] == "today") {
			WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Today Date button'))
		} else {
			TestObject targetDate = changeSelector(findTestObject('Object Repository/3. Schedule Editor/Target Date button'),"targetDate", date)
			LocalDate currentFocus
			LocalDate today = LocalDate.now()
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy")
			String inputValue = WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/'+startOrEnd+' Date input value'), "value").replaceFirst("(\\d\\d)\\/(\\d\\d)", "\$2/\$1")
			if(WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/'+startOrEnd+' Date input value'), "value") == "")
				currentFocus = today
			else
				currentFocus = LocalDate.parse(inputValue, dtf)

			LocalDate currentDate = LocalDate.parse(date, dtf)
			int attempts = 60
			if(startOrEnd == "Start" && today < currentDate) tabLanded = "Future"
			if(startOrEnd == "End" && today > currentDate) tabLanded = "Passed"
			while(attempts > 0 && !selenium.isElementPresent(getSelector(targetDate))) {
				if(currentFocus > currentDate) WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Previous Month button'))
				else if (currentFocus < currentDate) WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Next Month button'))
				Thread.sleep(250)
				attempts--
			}
			if(!selenium.isElementPresent(getSelector(targetDate))) KeywordUtil.markFailedAndStop("Target date "+date+" not found within the 36 months")
			assert !WebUI.getAttribute(targetDate, "class").contains("disabled") : "Target " + startOrEnd + " date is disabled. rule" + currentMap + " map " + startOrEnd + " may be in the past, or the start date is superior to the end date."
			WebUI.click(targetDate)
		}

		return tabLanded
	}

	@Keyword
	def parseCondition(String currentConditions = GlobalVariable.ruleCreate['Condition'], String previousConditions = "", String keepOldCondition = "")
	{
		/**
		 * Parse condition variable in map and save information in a tab
		 * 
		 * @param condition
		 * 		Condition for partner
		 * @param keepOldCondition
		 * 		Conditions to keep for edition / duplication
		 * 
		 * @return
		 * 		Two dimension tab contains budget values, types and currencies
		 * 
		 */
		ArrayList<String> conditionParsed = new ArrayList<String>()
		ArrayList<String> keepOldConditionParsed = new ArrayList<String>()
		ArrayList<String> oneCondition = new ArrayList<String>()
		ArrayList<String> budgetValue = new ArrayList<String>()
		ArrayList<String> budgetType = new ArrayList<String>()
		ArrayList<String> currency = new ArrayList<String>()

		if (previousConditions == "") conditionParsed = currentConditions.split(";")
		else conditionParsed = previousConditions.split(";") + currentConditions.split(";")

		if (keepOldCondition != "")
		{
			if(GlobalVariable.ruleEdit['KeepOldCondition'].contains(",")) {
				keepOldConditionParsed = GlobalVariable.ruleEdit['KeepOldCondition'].split(",C")
				keepOldConditionParsed[0] = keepOldConditionParsed[0].split("C")[1]
			} else {
				keepOldConditionParsed = GlobalVariable.ruleEdit['KeepOldCondition'].split("C")
			}
			keepOldConditionParsed.removeAll([""])

			int remainingConditions = 0
			boolean isRemoveLastCondition = false
			ArrayList<WebElement> conditionElements = driver.findElements(By.cssSelector(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Condition rows').getSelectorCollection().get(SelectorMethod.CSS)))
			remainingConditions = conditionElements.size()
		}
		for (int i =0; i<conditionParsed.size; i++)
		{
			oneCondition = conditionParsed[i].split(",")
			if(keepOldCondition == "" || (keepOldConditionParsed.contains((i+1).toString()) || i >= previousConditions.split(";").length)) {
				budgetValue.add(oneCondition[0])
				budgetType.add(oneCondition[1])
				currency.add(oneCondition[2])
			}
		}
		KeywordUtil.logInfo("budgetValue parsed: "+budgetValue)
		KeywordUtil.logInfo("budgetType parsed: "+budgetType)
		KeywordUtil.logInfo("currency parsed: "+currency)

		return [budgetValue, budgetType, currency]

	}

	@Keyword
	def selectConditions(String dataConditions, boolean isEdition = false)
	{
		/**
		 * Select one or more condition for partner with budget type, budget value and Advertiser currency
		 * 
		 * @param dataConditions
		 * 		One or more condition in a String
		 */

		selenium = getSelenium()
		ArrayList<String> conditions = new ArrayList<String>()
		ArrayList<ArrayList<String>> conditions1 = new ArrayList<ArrayList<String>>()
		conditions = dataConditions.split(";")
		if(!isEdition) conditions1 = parseCondition()

		if(isEdition) {
			conditions1 = parseCondition(GlobalVariable.ruleEdit['Condition'], GlobalVariable.ruleCreate['Condition'], GlobalVariable.ruleEdit['KeepOldCondition'])

			// Remove all conditions
			boolean isRemoveConditionPresent = true
			for (int i = 1; i <= conditions1[0].size; i++) {
				isRemoveConditionPresent = selenium.isElementPresent(getSelector(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Remove condition button')))
				if(isRemoveConditionPresent)
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Remove condition button'))
			}
		}
		KeywordUtil.logInfo(conditions1[0].size+" conditions should be set")

		for (int i = 0; i < conditions1[0].size; i++)
		{
			//Budget
			WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Budget input partner'), "nth-of-type\\(\\d+\\)", "nth-of-type("+(i+1)+")"), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE))
			WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Budget input partner'), "nth-of-type\\(\\d+\\)", "nth-of-type("+(i+1)+")"),conditions1[0][i])

			//Budget type
			TestObject amount = changeSelector(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Budget type selector'), "AMOUNT", conditions1[1][i].toUpperCase())
			WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(amount)+"').item(" + i + ").click()", null)

			//Advertiser currency
			assert WebUI.waitForElementVisible(findTestObject('3. Schedule Editor/Advertiser Currency Select button'), 5)
			WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('3. Schedule Editor/Advertiser Currency Select button'))+"').item(" + (i*3) + ").click()", null)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Schedule Editor/Advertiser currency EUR css'), 10)
			TestObject currencyOption = changeSelectorXpath(findTestObject('3. Schedule Editor/Advertiser Currency option'), "EUR", conditions1[2][i])
			assert WebUI.waitForElementVisible(currencyOption, 10)
			WebUI.click(currencyOption)

			if (conditions1[0].size != 1)
			{
				if (i != (conditions1[0].size) - 1)
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Add condition button'))
			}

			//Verify input
			assert conditions1[2][i] == WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('3. Schedule Editor/Advertiser Currency Select text'))+"').item(" + i + ").textContent", null): "Currency is false: Expected: "+conditions1[2][i]
			assert conditions1[0][i] == WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('3. Schedule Editor/Budget input'))+"').item(" + i + ").value", null) : "Budgetvalue is false: Expected: "+conditions1[0][i]
		}
	}

	def selectAdvertiserItems(boolean isEdition = false, Map currentMap = GlobalVariable.ruleCreate)
	{
		/**
		 * 	Select advertisers, campaigns and insertion orders specified in the user profile 
		 * 
		 * @param isEdition		true if editing or duplicating a rule, false for creation
		 * @param currentMap	Current map to use to select the advertiser items
		 */
		Map advertiserItems
		if (WebUI.getAttribute(findTestObject('3. Schedule Editor/Apply on advertiser button'), "class").contains("mat-radio-checked"))
			advertiserItems = ['Advertisers': currentMap['Advertisers']]
		else if (WebUI.getAttribute(findTestObject('3. Schedule Editor/Apply on insertion order button'), "class").contains("mat-radio-checked"))
			advertiserItems = ['Advertisers': currentMap['Advertisers'], 'Campaigns' : currentMap['Campaigns'], 'Insertion Orders' : currentMap['Insertion Orders']]

		advertiserItems.each() { key, val ->
			Thread.sleep(1000)
			if (val != "")
			{
				assert WebUI.waitForElementNotPresent(findTestObject('3. Schedule Editor/Progress bar'), 60)
				assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Schedule Editor/'+key+' Select button'), 5) : key + " button is not visible after 5 seconds"
				WebUI.click(findTestObject('Object Repository/3. Schedule Editor/'+key+' Select button'))
				Thread.sleep(500)
				assert WebUI.waitForElementPresent(findTestObject('Object Repository/3. Schedule Editor/Current Options Search input'), 10)

				// Clear existing items for edition / duplication
				if(isEdition && !currentMap["Keep Old " + key]) {
					boolean isCheckedAdvertiserItem = selenium.isElementPresent(getSelector(findTestObject('Object Repository/3. Schedule Editor/Current Options Checked options')))
					while(isCheckedAdvertiserItem) {
						WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Current Options Checked options'))
						Thread.sleep(250)
						isCheckedAdvertiserItem = selenium.isElementPresent(getSelector(findTestObject('Object Repository/3. Schedule Editor/Current Options Checked options')))
					}
					assert WebUI.waitForElementNotPresent(findTestObject('3. Schedule Editor/Progress bar'), 60)
				}

				// Select new items
				for(String advertiserItem in val.split(',')) {
					WebUI.delay(1)
					WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Current Options Search input'), advertiserItem)
					Thread.sleep(500)
					assert WebUI.waitForElementPresent(findTestObject('Object Repository/3. Schedule Editor/Current Options First checkbox'), 30) : key.replaceFirst("s\$", "") + " " + advertiserItem + " was not found in the list. Check the budget type or parent item"
					if(!WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/Current Options First checkbox'), "class").contains("mat-pseudo-checkbox-checked"))
						WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Current Options First checkbox'))
					Thread.sleep(500)
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Current Options Clear Search button'))

					if(key != "Insertion Orders") {
						Thread.sleep(1000)
						assert WebUI.waitForElementNotPresent(findTestObject('3. Schedule Editor/Progress bar'), 60)
					}
				}
				WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Current Options Search input'), Keys.chord(Keys.ESCAPE))
				assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/3. Schedule Editor/Overlay'), 15) : "Failed to close the list of " + key

				if(key != "Insertion Orders") {
					Thread.sleep(1500)
					assert WebUI.waitForElementNotPresent(findTestObject('3. Schedule Editor/Progress bar'), 180)
				}
			}
		}
	}
	def searchRuleInTabsAndStop(String ruleName, String expectedTab) {
		ArrayList<String> tabs = ["Passed", "Scheduled", "Future"]
		for(int i = 0; i < 3; i++) {
			WebUI.click(findTestObject('Object Repository/2. Schedule List/' + tabs[i] + ' Tab button'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/' + tabs[i] + ' Tab Active'), 5) : tabs[i] + " tab is not active despite clicking on the button"
			WebUI.sendKeys(findTestObject('2. Schedule List/Search bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/2. Schedule List/Loader'), 10) : "The rule list is still loading after 10 seconds"
			WebUI.sendKeys(findTestObject('2. Schedule List/Search bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
			Thread.sleep(500)
			WebUI.sendKeys(findTestObject('2. Schedule List/Search bar input'), ruleName)
			Thread.sleep(1000)
			if(selenium.isElementPresent(getSelector(findTestObject('2. Schedule List/1. Table/Names')))) {
				if(expectedTab != tabs[i] && WebUI.getText(findTestObject('2. Schedule List/1. Table/Names')) == ruleName)
					KeywordUtil.markErrorAndStop("The rule is not displayed at the expected tab. Expected : " + expectedTab + ". Actual : " + tabs[i])
				else if (expectedTab == tabs[i] && WebUI.getText(findTestObject('2. Schedule List/1. Table/Names')) == ruleName)
					KeywordUtil.markErrorAndStop("The user did not land in the expected tab after the rule creation. Expected : " + expectedTab)
			}
		}
		KeywordUtil.markErrorAndStop("The rule is not found")
	}

	@Keyword
	String editRule(
			String ruleToEdit,
			String startDate = GlobalVariable.ruleEdit['Start Date'],
			String endDate = GlobalVariable.ruleEdit['End Date'],
			boolean emailwarning = GlobalVariable.ruleEdit['Warning Email'],
			String status = GlobalVariable.ruleEdit['Status'],
			Map advertiserItemsMap = GlobalVariable.ruleEdit,
			String budgetValue = GlobalVariable.ruleEdit['Budget Value'],
			String threshold = GlobalVariable.ruleEdit['Threshold'],
			String ruleName = GlobalVariable.ruleEdit['Rule Name'],
			String dataConditions = GlobalVariable.ruleEdit['Condition'],
			boolean pauseOthersCurrencies = GlobalVariable.ruleEdit['PauseOthersCurrencies'],
			String additionalRecipient = null
			)
	{
		/**
		 * Edit a rule based on the user profile or parameters
		 * Check the rule is edited properly
		 * Check the display in the list
		 *
		 * @param ruleToEdit				Current name of the rule to edit
		 * @param startDate					New start date
		 * @param endDate					New end date
		 * @param emailwarning				'true' to check the warning email option, or 'false' to uncheck it
		 * @param status					New status
		 * @param advertiserItemsMap		Map to use to select the new advertiser items
		 * @param budgetValue				New budget value
		 * @param threshold					New threshold value
		 * @param ruleName					New name of the rule to edit
		 * @param dataConditions			Text version of conditions to set for partner rules
		 * @param PauseOthersCurrencies		'true' to check the autopause delivery other currency option, else leave it unchecked
		 * @param additionalRecipient		Custom recipient
		 *
		 * @return ruleName
		 */
		selenium = getSelenium()
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Schedule List/Search Bar input'), 10) : "Search Bar input is not visible after 10 seconds"
		Search(ruleToEdit)
		String numberOfRule = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/First rule Menu button'))+"').length", null)
		assert numberOfRule == "1" : numberOfRule+ " are present instead of 1"
		WebUI.click(findTestObject('Object Repository/2. Schedule List/First Rule Menu button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Menu edit button'), 3) : "Duplicate button is not visible after 3 seconds"
		WebUI.click(findTestObject('Object Repository/2. Schedule List/Menu Edit button'))
		Thread.sleep(5000)

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Schedule Editor/Apply on Partner button'), 30)
		assert WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/Apply on Partner button'), "class").contains("mat-radio-disabled")
		assert WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/Apply on Advertiser button'), "class").contains("mat-radio-disabled")
		assert WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/Apply on Insertion Order button'), "class").contains("mat-radio-disabled")

		if (GlobalVariable.ruleCreate['Apply on'] == 'Advertiser' || GlobalVariable.ruleCreate['Apply on'] == 'Insertion Order')
		{
			// Budget
			assert WebUI.waitForElementVisible(findTestObject('3. Schedule Editor/Budget input'), 15) : "Budget input is not visible after 5 seconds"
			Thread.sleep(500)
			WebUI.clearText(findTestObject('3. Schedule Editor/Budget input'))
			Thread.sleep(250)
			WebUI.sendKeys(findTestObject('3. Schedule Editor/Budget input'), budgetValue)

			// Advertiser items
			if(advertiserItemsMap != [:])
				selectAdvertiserItems(true, advertiserItemsMap)

			assert WebUI.verifyElementAttributeValue(findTestObject('3. Schedule Editor/Budget input'), "value", budgetValue, 1)
		} else if (GlobalVariable.ruleCreate['Apply on'] == 'Partner')
		{
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Partners List disabled'), 10)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Budget input partner'), 10)
			selectConditions(dataConditions, true)
			boolean isPausedOtherCurrencies = WebUI.getAttribute(changeSelector(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Pause Other Currencies input'),"div\$",""), "class").contains("mat-checkbox-checked")
			if ((pauseOthersCurrencies == true && !isPausedOtherCurrencies) || (pauseOthersCurrencies == false && isPausedOtherCurrencies))
				WebUI.click(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Pause Other Currencies input'))
		}

		// Analysis period
		String tabLandedStart = modifyDate(startDate, "Start", "Edit")
		String tabLandedEnd = modifyDate(endDate, "End", "Edit")

		// Threshold
		WebUI.clearText(findTestObject('Object Repository/3. Schedule Editor/Threshold input'))
		Thread.sleep(250)
		WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Threshold input'), threshold)

		// Rule name
		ruleName += "_" + new Date().getTime()
		WebUI.clearText(findTestObject('Object Repository/3. Schedule Editor/Rule Name input'))
		Thread.sleep(250)
		WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Rule Name input'), ruleName)

		// Recipients
		if(!GlobalVariable.ruleEdit["Keep Old Recipients"]) {
			boolean isSecondRecipientPresent = selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/3. Schedule Editor/Email Remove button'), "nth-of-type\\(1\\)", "nth-of-type(2)")))
			while(isSecondRecipientPresent) {
				isSecondRecipientPresent = selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/3. Schedule Editor/Email Remove button'), "nth-of-type\\(1\\)", "nth-of-type(2)")))
				if(WebUI.getText(findTestObject('Object Repository/3. Schedule Editor/First recipient text')) != GlobalVariable.googleID) {
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Email Remove button'))
				} else if(isSecondRecipientPresent) {
					WebUI.click(changeSelector(findTestObject('Object Repository/3. Schedule Editor/Email Remove button'), "nth-of-type\\(1\\)", "nth-of-type(2)"))
				}
				Thread.sleep(250)
			}
		}
		for(String email in GlobalVariable.ruleEdit['Recipients'].split(",")) {
			if(!selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/3. Schedule Editor/Email title'), "email", email)))) {
				WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), email)
				WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), Keys.chord(Keys.ENTER))
			}
		}
		if(additionalRecipient != "" && additionalRecipient != null && additionalRecipient != GlobalVariable.googleID) {
			if(!selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/3. Schedule Editor/Email title'), "email", additionalRecipient)))) {
				WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), additionalRecipient)
				WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), Keys.chord(Keys.ENTER))
			}
		}

		// Warning Email option
		if(emailwarning == true && !WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/warningemail checkbox'), "class").contains("mat-checkbox-checked"))
			WebUI.click(findTestObject('Object Repository/3. Schedule Editor/warningemail checkbox'))
		else if (emailwarning == false && WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/warningemail checkbox'), "class").contains("mat-checkbox-checked"))
			WebUI.click(findTestObject('Object Repository/3. Schedule Editor/warningemail checkbox'))

		//Status
		WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Status select button'))
		WebUI.waitForElementVisible(changeSelectorXpath(findTestObject('Object Repository/3. Schedule Editor/Status option'),"Active", status), 3)
		WebUI.click(changeSelectorXpath(findTestObject('Object Repository/3. Schedule Editor/Status option'),"Active", status))

		Map element = ["Currency": GlobalVariable.ruleCreate['Currency'], "Budget Value": budgetValue, "Budget Type": GlobalVariable.ruleCreate['Budget Type'], "Threshold": threshold, "Start Date": startDate, "End Date": endDate, "Rule Name": ruleName, "Recipients": GlobalVariable.ruleEdit['Recipients'], "Advertisers": GlobalVariable.ruleEdit['Advertisers'], "Campaigns": GlobalVariable.ruleEdit['Campaigns'], "Insertion Orders": GlobalVariable.ruleEdit['Insertion Orders']]

		checkInputs(element, true)

		// Save rule & check display
		WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Save Rule button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Passed Tab button'), 20)
		WebUI.waitForElementNotPresent(findTestObject('Object Repository/2. Schedule List/Loader'), 3)
		if(tabLandedEnd == "Passed") {
			if(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Passed Tab Active'))))
				searchRuleInTabsAndStop(ruleName, "Passed")
		} else if (tabLandedStart == "Future") {
			if(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Future Tab Active'))))
				searchRuleInTabsAndStop(ruleName, "Future")
		} else {
			if(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Scheduled Tab Active'))))
				searchRuleInTabsAndStop(ruleName, "Scheduled")
		}

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Rule Cells'), 30)
		while(selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Next page button'))) && !selenium.isElementPresent(getSelector(changeSelectorXpath(findTestObject('2. Schedule List/Created Rule Name'), "ruleName", ruleName)))) {
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Next page button'))
			Thread.sleep(500)
		}
		assert WebUI.verifyElementPresent(changeSelectorXpath(findTestObject('2. Schedule List/Created Rule Name'), "ruleName", ruleName), 1) : "Created rule "+ruleName+" was not found"
		WebUI.focus(changeSelectorXpath(findTestObject('2. Schedule List/Created Rule Name'), "ruleName", ruleName))

		WebUI.sendKeys(findTestObject('Object Repository/2. Schedule List/Search Bar input'), ruleName)
		Thread.sleep(750)

		// Check Spend is NA
		if (GlobalVariable.ruleCreate['Apply on'] == 'Advertiser' || GlobalVariable.ruleCreate['Apply on'] == 'Insertion Order')
		{
			assert WebUI.getText(findTestObject('2. Schedule List/1. Table/Spends')) == "NA"
			verifyBudget(GlobalVariable.ruleCreate['Budget Type'], GlobalVariable.ruleCreate['Currency'], budgetValue)
		}
		else if (GlobalVariable.ruleCreate['Apply on'] == 'Partner')
		{
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Spend popup'))
			int conditionNumber = GlobalVariable.ruleEdit['KeepOldCondition'].split(",").size()
			for (int i = 0; i < conditionNumber; i++)
			{
				String NAValue = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/Spend NA'))+"').item("+i+").textContent", null)
				assert NAValue == "NA": "Spend display should be NA"
			}
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Spend close button'))
			WebUI.delay(1)
		}

		// Check status icon
		if(tabLandedEnd != "Passed") {
			if(status == "Active") {
				assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/First Rule Status icon')) == "fiber_manual_record"
			} else {
				assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/First Rule Status icon')) == "pause"
			}
		}
		WebUI.sendKeys(findTestObject('Object Repository/2. Schedule List/Search Bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
		Thread.sleep(500)

		return ruleName
	}

	@Keyword
	String duplicateRule(
			String ruleToDuplicate,
			String startDate = GlobalVariable.ruleDuplicate['Start Date'],
			String endDate = GlobalVariable.ruleDuplicate['End Date'],
			boolean emailwarning = GlobalVariable.ruleDuplicate['Warning Email'],
			String status = GlobalVariable.ruleDuplicate['Status'],
			Map advertiserItemsMap = GlobalVariable.ruleDuplicate,
			String budgetValue = GlobalVariable.ruleDuplicate['Budget Value'],
			String threshold = GlobalVariable.ruleDuplicate['Threshold'],
			String ruleName = GlobalVariable.ruleDuplicate['Rule Name'],
			String additionalRecipient = null
			)
	{
		/**
		 * Duplicate a rule based on the user profile or parameters
		 * Check the rule is created properly
		 * Check the display in the list
		 *
		 * @param ruleToDuplicate			Name of the rule to duplicate
		 * @param startDate					Start date of the new duplicate rule
		 * @param endDate					End date of the new duplicate rule
		 * @param emailwarning				'true' to check the warning email option, or 'false' to uncheck it
		 * @param status					Status of the new duplicate rule
		 * @param advertiserItemsMap		Map to use to select the advertiser items
		 * @param budgetValue				Budget value of the new duplicate rule
		 * @param threshold					Threshold value of the new duplicate rule
		 * @param ruleName					Name of the new duplicate rule
		 * @param additionalRecipient		Custom recipient
		 *
		 * @return ruleName
		 */
		selenium = getSelenium()
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Schedule List/Search Bar input'), 10) : "Search Bar input is not visible after 10 seconds"
		Search(ruleToDuplicate)
		String numberOfRule = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/First rule Menu button'))+"').length", null)
		assert numberOfRule == "1" : numberOfRule+ " are present instead of 1"
		WebUI.click(findTestObject('Object Repository/2. Schedule List/First Rule Menu button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Menu Duplicate button'), 3) : "Duplicate button is not visible after 3 seconds"
		WebUI.click(findTestObject('Object Repository/2. Schedule List/Menu Duplicate button'))
		Thread.sleep(5000)

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Schedule Editor/Apply on Partner button'), 30)
		assert WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/Apply on Partner button'), "class").contains("mat-radio-disabled")
		assert WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/Apply on Advertiser button'), "class").contains("mat-radio-disabled")
		assert WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/Apply on Insertion Order button'), "class").contains("mat-radio-disabled")

		if (GlobalVariable.ruleCreate['Apply on'] == 'Advertiser' || GlobalVariable.ruleCreate['Apply on'] == 'Insertion Order')
		{
			// Budget
			assert WebUI.waitForElementVisible(findTestObject('3. Schedule Editor/Budget input'), 5) : "Budget input is not visible after 5 seconds"
			Thread.sleep(500)
			WebUI.clearText(findTestObject('3. Schedule Editor/Budget input'))
			Thread.sleep(250)
			WebUI.sendKeys(findTestObject('3. Schedule Editor/Budget input'), budgetValue)

			// Advertiser items
			if(advertiserItemsMap != [:])
				selectAdvertiserItems(true, advertiserItemsMap)

			assert WebUI.verifyElementAttributeValue(findTestObject('3. Schedule Editor/Budget input'), "value", budgetValue, 1)
		}

		// Analysis period
		String tabLandedStart = modifyDate(startDate, "Start", "Duplicate")
		String tabLandedEnd = modifyDate(endDate, "End", "Duplicate")

		// Threshold
		WebUI.clearText(findTestObject('Object Repository/3. Schedule Editor/Threshold input'))
		Thread.sleep(250)
		WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Threshold input'), threshold)

		// Rule name
		ruleName += "_" + new Date().getTime()
		WebUI.clearText(findTestObject('Object Repository/3. Schedule Editor/Rule Name input'))
		Thread.sleep(250)
		WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Rule Name input'), ruleName)

		// Recipients
		if(!GlobalVariable.ruleDuplicate["Keep Old Recipients"]) {
			boolean isSecondRecipientPresent = selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/3. Schedule Editor/Email Remove button'), "nth-of-type\\(1\\)", "nth-of-type(2)")))
			while(isSecondRecipientPresent) {
				isSecondRecipientPresent = selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/3. Schedule Editor/Email Remove button'), "nth-of-type\\(1\\)", "nth-of-type(2)")))
				if(WebUI.getText(findTestObject('Object Repository/3. Schedule Editor/First recipient text')) != GlobalVariable.googleID) {
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Email Remove button'))
				} else if (isSecondRecipientPresent) {
					WebUI.click(changeSelector(findTestObject('Object Repository/3. Schedule Editor/Email Remove button'), "nth-of-type\\(1\\)", "nth-of-type(2)"))
				}
				Thread.sleep(250)
			}
		}
		for(String email in GlobalVariable.ruleDuplicate['Recipients'].split(",")) {
			if(!selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/3. Schedule Editor/Email title'), "email", email)))) {
				WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), email)
				WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), Keys.chord(Keys.ENTER))
			}
		}
		if(additionalRecipient != "" && additionalRecipient != null && additionalRecipient != GlobalVariable.googleID) {
			if(!selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/3. Schedule Editor/Email title'), "email", additionalRecipient)))) {
				WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), additionalRecipient)
				WebUI.sendKeys(findTestObject('Object Repository/3. Schedule Editor/Email input'), Keys.chord(Keys.ENTER))
			}
		}

		// Warning Email option
		if(emailwarning == true && !WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/warningemail checkbox'), "class").contains("mat-checkbox-checked"))
			WebUI.click(findTestObject('Object Repository/3. Schedule Editor/warningemail checkbox'))
		else if (emailwarning == false && WebUI.getAttribute(findTestObject('Object Repository/3. Schedule Editor/warningemail checkbox'), "class").contains("mat-checkbox-checked"))
			WebUI.click(findTestObject('Object Repository/3. Schedule Editor/warningemail checkbox'))

		//Status
		WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Status select button'))
		WebUI.waitForElementVisible(changeSelectorXpath(findTestObject('Object Repository/3. Schedule Editor/Status option'),"Active", status), 3)
		WebUI.click(changeSelectorXpath(findTestObject('Object Repository/3. Schedule Editor/Status option'),"Active", status))

		Map element = ["Currency": GlobalVariable.ruleCreate['Currency'], "Budget Value": budgetValue, "Budget Type": GlobalVariable.ruleCreate['Budget Type'], "Threshold": threshold, "Start Date": startDate, "End Date": endDate, "Rule Name": ruleName, "Recipients": GlobalVariable.ruleDuplicate['Recipients'], "Advertisers": GlobalVariable.ruleDuplicate['Advertisers'], "Campaigns": GlobalVariable.ruleDuplicate['Campaigns'], "Insertion Orders": GlobalVariable.ruleDuplicate['Insertion Orders']]

		checkInputs(element)

		// Save rule & check display
		WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Save Rule button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Passed Tab button'), 20)
		WebUI.waitForElementNotPresent(findTestObject('Object Repository/2. Schedule List/Loader'), 3)
		if(tabLandedEnd == "Passed") {
			if(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Passed Tab Active'))))
				searchRuleInTabsAndStop(ruleName, "Passed")
		} else if (tabLandedStart == "Future") {
			if(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Future Tab Active'))))
				searchRuleInTabsAndStop(ruleName, "Future")
		} else {
			if(!selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Scheduled Tab Active'))))
				searchRuleInTabsAndStop(ruleName, "Scheduled")
		}

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Rule Cells'), 30)
		while(selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Next page button'))) && !selenium.isElementPresent(getSelector(changeSelectorXpath(findTestObject('2. Schedule List/Created Rule Name'), "ruleName", ruleName)))) {
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Next page button'))
			Thread.sleep(500)
		}
		assert WebUI.verifyElementPresent(changeSelectorXpath(findTestObject('2. Schedule List/Created Rule Name'), "ruleName", ruleName), 1) : "Created rule "+ruleName+" was not found"
		WebUI.focus(changeSelectorXpath(findTestObject('2. Schedule List/Created Rule Name'), "ruleName", ruleName))

		WebUI.sendKeys(findTestObject('Object Repository/2. Schedule List/Search Bar input'), ruleName)
		Thread.sleep(750)

		// Check Spend is NA
		if (GlobalVariable.ruleCreate['Apply on'] == 'Advertiser' || GlobalVariable.ruleCreate['Apply on'] == 'Insertion Order')
		{
			assert WebUI.getText(findTestObject('2. Schedule List/1. Table/Spends')) == "NA"
			verifyBudget(GlobalVariable.ruleCreate['Budget Type'], GlobalVariable.ruleCreate['Currency'], budgetValue)
		}

		// Check status icon
		if(tabLandedEnd != "Passed") {
			if(status == "Active") {
				assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/First Rule Status icon')) == "fiber_manual_record"
			} else {
				assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/First Rule Status icon')) == "pause"
			}
		}
		WebUI.sendKeys(findTestObject('Object Repository/2. Schedule List/Search Bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
		Thread.sleep(500)

		return ruleName
	}

	def verifyBudget(String budgetType, String currency, String budget) {
		/**
		 * 	Verify the budget value for the first rule listed
		 *
		 * @param budgetType	Budget type of the rule to verify the spend from
		 * @param currency		Currency of the rule to verify the spend from
		 * @param budget		Budget of the rule to verify the spend from
		 */
		if(GlobalVariable.ruleCreate['Apply on'] != "Partner") {
			if(budgetType == "Currency")
				assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/Budgets')).equals(currency + " " + budget)
			else if(budgetType == "Impressions")
				assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/Budgets')).equals("Imps " + budget)
		}
	}
	def verifySpend(String currency, String budget, String ruleSpend)
	{
		/**
		 * 	Verify the spend value and percentage of budget for the first rule listed, as well as the format of the text displayed using regex
		 * 
		 * @param currency		Currency of the rule to verify the spend from
		 * @param budget		Budget of the rule to verify the spend from
		 * @param ruleSpend		Approximate spend value of the rule (returned from the mail)
		 * 
		 */
		if(GlobalVariable.ruleCreate['Apply on'] != "Partner") {
			if(GlobalVariable.ruleCreate['Budget Type'] == "Impressions") currency = "Imps"
			String regex = currency + " (\\d+(.\\d+)?) \\(\\d+(.\\d+)?%\\)\\n\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d, \\d\\d:\\d\\d:\\d\\d"
			assert WebUI.getText(findTestObject('2. Schedule List/1. Table/Spends')) != "NA" : "The spend value was not updated, NA is displayed."
			String spendDisplayed = WebUI.getText(findTestObject('2. Schedule List/1. Table/Spends'))
			String rulePreciseSpend = (spendDisplayed =~ /${regex}/).findAll()[0][1]
			assert Math.abs((Float.valueOf(rulePreciseSpend) - Float.valueOf(ruleSpend))) < 0.01 : "Spend displayed is not correct : " + rulePreciseSpend + " =/= " + ruleSpend
			BigDecimal rulePreciseSpendValue = new BigDecimal(rulePreciseSpend)
			BigDecimal budgetValue = new BigDecimal(budget)
			String spendPercentText = rulePreciseSpendValue.multiply(new BigDecimal(100)).divide(budgetValue, 10, RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN).toString()
			regex = currency + " " + rulePreciseSpend + " \\(" + spendPercentText + "%\\)\\n\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d, \\d\\d:\\d\\d:\\d\\d"
			// Due to displayed spend percent value being obtained via javascript "toFixed" function,
			// which sometimes changes the rounding mode to half_up or half_down because of the "real" float value, we cannot always get the right precision for the last digit
			// (eg. 417.355.toFixed(2) = 417.35 instead of 417.36 with any half_even rounding in groovy)
			if(spendPercentText.contains(".")) {
				spendPercentText = spendPercentText.substring(0, spendPercentText.length() - 1)
				regex = currency + " " + rulePreciseSpend + " \\(" + spendPercentText + "\\d%\\)\\n\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d, \\d\\d:\\d\\d:\\d\\d"
			}
			assert spendDisplayed.matches(regex) : "Spend displayed does not match regex : " + regex
		} else {
			//			WebUI.click(findTestObject('Object Repository/2. Schedule List/Spend popup'))
			//			// Check wording for "Automatically pause the delivery" option
			//			// TODO
			//
			//			// Check Spend is NA
			//			String conditions = GlobalVariable.ruleCreate['Condition'].split(";")
			//			for (int i = 0; i < conditions.size(); i++)
			//			{
			//				verifySpendInPopup(currency, budget, ruleSpends[i])
			//			}
			//			WebUI.click(findTestObject('Object Repository/2. Schedule List/Spend close button'))
			//			WebUI.delay(1)
		}
	}
	def verifySpendInPopup(String currency, String budget, String ruleSpend) {
		/**
		 *  Verify spend in popup
		 *  
		 * @param currency		Currency of the rule to verify the spend from
		 * @param budget		Budget of the rule to verify the spend from
		 * @param ruleSpend		Approximate spend value of the rule (returned from the mail)
		 */

		if(GlobalVariable.ruleCreate['Budget Type'] == "Impressions") currency = "Imps"
		String regex = currency + " (\\d+(.\\d+)?) \\(\\d+(.\\d+)?%\\)\\n\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d, \\d\\d:\\d\\d:\\d\\d"
		assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/Spend NA')) != "NA" : "The spend value was not updated, NA is displayed."
		String spendDisplayed = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/Spend NA'))+"').item("+i+").textContent", null)

		String rulePreciseSpend = (spendDisplayed =~ /${regex}/).findAll()[0][1]
		assert Math.abs((Float.valueOf(rulePreciseSpend) - Float.valueOf(ruleSpend))) < 0.01 : "Spend displayed is not correct : " + rulePreciseSpend + " =/= " + ruleSpend
		BigDecimal rulePreciseSpendValue = new BigDecimal(rulePreciseSpend)
		BigDecimal budgetValue = new BigDecimal(budget)
		String spendPercentText = rulePreciseSpendValue.multiply(new BigDecimal(100)).divide(budgetValue, 10, RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN).toString()
		regex = currency + " " + rulePreciseSpend + " \\(" + spendPercentText + "%\\)\\n\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d, \\d\\d:\\d\\d:\\d\\d"
		// Due to displayed spend percent value being obtained via javascript "toFixed" function,
		// which sometimes changes the rounding mode to half_up or half_down because of the "real" float value, we cannot always get the right precision for the last digit
		// (eg. 417.355.toFixed(2) = 417.35 instead of 417.36 with any half_even rounding in groovy)
		if(spendPercentText.contains(".")) {
			spendPercentText = spendPercentText.substring(0, spendPercentText.length() - 1)
			regex = currency + " " + rulePreciseSpend + " \\(" + spendPercentText + "\\d%\\)\\n\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d, \\d\\d:\\d\\d:\\d\\d"
		}
		assert spendDisplayed.matches(regex) : "Spend displayed does not match regex : " + regex
	}

	def setRuleStatus(String newStatus, String ruleName)
	{
		/**
		 * Set the status of a rule to Active or Paused using the rule list menu
		 * Verifies the notification text and status icon
		 * 
		 * @param newStatus			New status of the rule : "Active" or "Paused" or "Activate" or "Pause" 
		 * @param ruleName			Name of the rule to change the status of.
		 */
		Search(ruleName)
		if(newStatus == "Paused") newStatus = "Pause"
		else if(newStatus == "Active") newStatus = "Activate"
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/First Rule Menu button'), 5) : "The first rule menu button is not visible after 5 seconds"
		WebUI.click(findTestObject('Object Repository/2. Schedule List/First Rule Menu button'))
		assert WebUI.waitForElementVisible(changeSelectorXpath(findTestObject('Object Repository/2. Schedule List/Menu Status button'), "status", newStatus), 5) : "The " + newStatus +" button is not visible after 5 seconds"
		WebUI.click(changeSelectorXpath(findTestObject('Object Repository/2. Schedule List/Menu Status button'), "status", newStatus))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Menu Status Confirm button'), 5) : "The 'Confirm' button is not visible after 5 seconds"
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/2. Schedule List/Notification text'), 6)
		WebUI.click(findTestObject('Object Repository/2. Schedule List/Menu Status Confirm button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Notification text'), 10) : "No notification was visible after 10 seconds"
		assert WebUI.verifyElementText(findTestObject('Object Repository/2. Schedule List/Notification text'), "Rule has been "+newStatus.toLowerCase()+"d.")

		if(newStatus == "Pause") assert !WebUI.getAttribute(findTestObject('Object Repository/2. Schedule List/1. Table/First Rule Status icon'), "class").contains("unpaused")
		else if(newStatus == "Activate") assert WebUI.getAttribute(findTestObject('Object Repository/2. Schedule List/1. Table/First Rule Status icon'), "class").contains("unpaused")
	}

	@Keyword
	def waitForRuleEmailAndCheckDvStatus(
			String ruleName, Map currentMap, String ruleBudget,
			int maxMinutes, int secondsToPause,
			boolean openDv360 = false, boolean openGmail,
			String previousInsertionOrders = GlobalVariable.ruleCreate['Insertion Orders'],
			String previousAdvertisers = GlobalVariable.ruleCreate['Advertisers'],
			boolean warningEmail,
			String conditions = GlobalVariable.ruleCreate['Condition'],
			boolean isCreation = false, ArrayList<String> iosIntel = null,
			int dv360WindowIndex = 2, int gmailWindowIndex = 1) {
		/**
		 * Wait for a rule email and check the content of the email
		 *  
		 * @param ruleName					Name of the rule to wait the email for
		 * @param currentMap				Current map to use to check the email
		 * @param ruleBudget				Rule budget value
		 * @param maxMinutes				Maximum number of minutes to wait for the email
		 * @param secondsToPause			Number of seconds to wait between each attempt
		 * @param openDv360					Set to true to open DV360 in a new tab, set to false to use the existing DV360 tab (should depend on previous rules warning email option)
		 * @param openGmail					Set to true to open Gmail in a new tab, set to false to use the existing Gmail tab
		 * @param previousInsertionOrders	Previous insertion order names, separated by ','. Mandatory for an edition or duplication
		 * @param previousAdvertisers		Previous advertiser names, separated by ','. Mandatory for an edition or duplication
		 * @param warningEmail				Current rule warning email option (true or false)
		 * @param conditions				Current or previous conditions, separated by ';'. Mandatory for an edition or duplication
		 * @param isCreation				Set to true if the email comes from a rule creation, false if it comes from and edition or duplication 
		 * @param iosIntel					A list of data (status;name;id)
		 * @param dv360WindowIndex			DV360 tab window index (default is third tab)
		 * @param gmailWindowIndex			Gmail tab window index (default is second tab)
		 * 
		 * @return [ruleSpend, ruleIOs, ruleAdvertisers]
		 */

		if(!isCreation) {
			if(GlobalVariable.ruleCreate['Apply on'] == "Insertion Order")	assert previousInsertionOrders != null : "The parameter previousInsertionOrders should be filled for an edition or duplication"
			if(GlobalVariable.ruleCreate['Apply on'] == "Advertiser")	assert previousAdvertisers != null : "The parameter previousAdvertisers should be filled for an edition or duplication"
			if(GlobalVariable.ruleCreate['Apply on'] == "Partner")	assert conditions != null : "The parameter conditions should be filled for an edition or duplication"
		}

		String[] newItems = ["", "", ""]

		if(GlobalVariable.ruleCreate['Apply on'] == "Partner")
			newItems = getNewItems(ruleName, currentMap, true, previousInsertionOrders, previousAdvertisers, warningEmail, conditions, isCreation, iosIntel)

		int minute = getCurrentMinute()
		int attempts = getPauseAttempts(minute, maxMinutes, secondsToPause)
		String epochSecond = ''
		if ((minute == 0) || (minute == 10)) epochSecond = Instant.parse(Instant.now().toString().replaceFirst('\\d\\.\\d\\d\\dZ$', '0.000Z')).getEpochSecond() // Rule created around 10mins, so we check the emails from the last 10mins just in case
		else epochSecond = getEpochSecond() // Rule not created around 10mins, so we can check the emails from now
		String expectedMailContentRegex = getExpectedMailContentRegex(ruleName, newItems[2], ruleBudget, warningEmail)
		Search(ruleName)
		String ruleID = WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/IDs'))

		WebUI.sendKeys(findTestObject('2. Schedule List/Search bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
		Thread.sleep(500)

		// Max wait is 11 mins to wait for the grafana status to appear + 4mins of waiting the status of the rule (total 15mins)
		if(GlobalVariable.grafana == true)
		{
			Grafana grafa = new Grafana()
			attempts = getPauseAttempts(minute, 11, 5)
			grafa.loginToGrafana(true, true)
			grafa.checkAutoPauseRuleStatus(ruleID, attempts)
			WebUI.switchToWindowIndex(1)
		}

		String ruleSpend = ""
		def gmail = new Gmail()
		if(openGmail) gmail.logInToGmail(true)
		else WebUI.switchToWindowIndex(gmailWindowIndex)
		gmail.waitForNewEmail(ruleName, warningEmail, epochSecond, attempts)
		WebUI.click(findTestObject('Object Repository/0. Gmail/Mail 1 Title'))
		WebUI.delay(1)
		ruleSpend = gmail.checkMailContent(expectedMailContentRegex)

		newItems = getNewItems(ruleName, currentMap, true, previousInsertionOrders, previousAdvertisers, warningEmail, conditions, isCreation, iosIntel)

		def dv360 = new DV360()
		if(warningEmail == false) {
			if(openDv360) {
				openNewTab()
				dv360.goToDv360()
			} else { WebUI.switchToWindowIndex(dv360WindowIndex) }
			dv360.verifyIosPaused(newItems[0].split(","))
		}
		return [ruleSpend, newItems[0], newItems[1], newItems[2]]
	}
	@Keyword
	ArrayList<String> getNewItems(String ruleName, Map currentMap, boolean isCheckMail = false,
			String previousInsertionOrders = GlobalVariable.ruleCreate['Insertion Orders'], String previousAdvertisers = GlobalVariable.ruleCreate['Advertisers'],
			boolean warningEmail = false,
			String conditions = GlobalVariable.ruleCreate['Condition'],
			boolean isCreation = false, ArrayList<String> iosIntel = null)
	{
		/**
		 * Get new rule items depending on the current map and previous items
		 * 
		 * @param ruleName					Name of the rule to check the advertiser popup
		 * @param currentMap				Current map to use to check the email
		 * @param isCheckMail				Set to true to check the advertiser items in the rule email, or false to only return the advertiser items 
		 * @param previousInsertionOrders	Previous insertion order names, separated by ','. Mandatory for an edition or duplication
		 * @param previousAdvertisers		Previous advertiser names, separated by ','. Mandatory for an edition or duplication
		 * @param warningEmail				Current rule warning email option (true or false)
		 * @param conditions				Current or previous conditions, separated by ';'. Mandatory for an edition or duplication
		 * @param isCreation				Set to true if the email comes from a rule creation, false if it comes from and edition or duplication 
		 * @param iosIntel					A list of data (status;name;id)
		 * 
		 * @returns [newInsertionOrders, newAdvertisers, newConditions]
		 */

		def gmail = new Gmail()
		String newInsertionOrders = ""
		String newAdvertisers = ""
		String newConditions = ""
		if(GlobalVariable.ruleCreate['Apply on'] == "Insertion Order") {
			if(!isCreation && previousInsertionOrders != "") newInsertionOrders = previousInsertionOrders + "," + currentMap['Insertion Orders']
			else newInsertionOrders = currentMap['Insertion Orders']
			if(!isCreation) if(!currentMap['Keep Old Insertion Orders'] || !currentMap['Keep Old Campaigns'] || !currentMap['Keep Old Advertisers']) newInsertionOrders = currentMap['Insertion Orders']

			if(isCheckMail) {
				for (String ios in newInsertionOrders.split(',')) {
					gmail.checkMailContains(ios)
				}
			}
		} else if(GlobalVariable.ruleCreate['Apply on'] == "Advertiser") {
			if(!isCreation && previousAdvertisers != "") newAdvertisers = previousAdvertisers + "," + currentMap['Advertisers']
			else newAdvertisers = currentMap['Advertisers']
			if(!isCreation) if(!currentMap['Keep Old Advertisers']) newAdvertisers = currentMap['Advertisers']

			if(isCheckMail) {
				ArrayList<String> advertiserList = new ArrayList<String>()
				for(adv in newAdvertisers.split(',')) {
					advertiserList.add(adv)
				}
				int gmailIndex = WebUI.getWindowIndex()
				WebUI.switchToWindowIndex(0)
				Search(ruleName)
				advertiserList = getAdvertiserIDs(advertiserList.unique())
				WebUI.switchToWindowIndex(gmailIndex)
				ArrayList<String> EmailOIList = gmail.checkOIlist(advertiserList)
				newInsertionOrders = EmailOIList.toString().replaceAll("[\\[\\]]", "").replaceAll(" ", "")

				DV360 dv360 = new DV360()

				dv360.verifyautopauseondv(EmailOIList, iosIntel, warningEmail)
			}
		} else if(GlobalVariable.ruleCreate['Apply on'] == "Partner") {
			if(!isCreation && conditions != "" && currentMap['KeepOldCondition'] != "" && currentMap['KeepOldCondition'].split(",").size() == conditions.split(';').size())
				newConditions = conditions + ";" + currentMap['Condition']
			else
				newConditions = currentMap['Condition']
		}
		KeywordUtil.logInfo("\nCurrent rule Insertion Orders : " + newInsertionOrders + "\nCurrent rule Advertisers : " + newAdvertisers + "\nCurrent rule conditions : " + newConditions)

		return [newInsertionOrders, newAdvertisers, newConditions]

	}
	ArrayList<String> getAdvertiserIDs(ArrayList<String> advertiserList)
	{
		/**
		 * Switch to Autopause (first tab), verifies the advertiser popup and return the adveriser IDs
		 *
		 * @param advertiserList	Expected advertiser list
		 * 
		 * @return advList			Advertiser IDs
		 */

		int currentWindowIndex = WebUI.getWindowIndex()
		WebUI.switchToWindowIndex(0)
		WebUI.click(findTestObject('Object Repository/2. Schedule List/1. Table/Advertiser iterator'))
		Thread.sleep(500)

		ArrayList<String> advList = []
		TestObject advertiserlistObject = findTestObject('2. Schedule List/3. Advertiser popup/iterator advertiser popup')
		String txt = ""
		for (int i = 0; i < advertiserList.size(); i++)
		{
			advertiserlistObject = changeSelector(advertiserlistObject,"nth-of-type\\(\\d+\\)", "nth-of-type("+ (i + 2) +")")

			boolean found = false
			for (String adv in advertiserList)
			{
				if (WebUI.getText(advertiserlistObject).contains(adv) == true)
				{
					found = true
					break
				}
			}
			assert found == true : "The advertisers listed do not match the advertiser list given in parameter. Expected : " + advertiserList

			txt = WebUI.getText(advertiserlistObject).replaceAll("[^\\)]*\\((\\d+)\\)", "\$1")
			advList.add(txt)
		}

		WebUI.click(findTestObject('Object Repository/2. Schedule List/2. Filter/Filter cross button'))
		Thread.sleep(500)

		WebUI.sendKeys(findTestObject('2. Schedule List/Search bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
		Thread.sleep(500)

		WebUI.switchToWindowIndex(currentWindowIndex)
		return (advList)
	}

	@Keyword
	String getExpectedMailContentRegex(String ruleName, String currentConditions, String ruleBudget, boolean warningemail= GlobalVariable.ruleCreate['Warning Email'])
	{
		/**
		 * Returns the expected mail content regex from current rule in the list
		 *
		 * @param ruleName				Rule name
		 * @param currentConditions		Current conditions for partner rules
		 * @param ruleBudget			Rule budget value for IO/Adv rules
		 * @param warningemail 			Determine if it is the warning email format or not
		 *
		 * @return String (expected mail content regex)
		 */

		selenium = getSelenium()
		if(selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Pagination First Page button enabled')))) {
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Pagination First Page button enabled'))
		}

		Search(ruleName)

		String partnerID = WebUI.getAttribute(findTestObject('Object Repository/2. Schedule List/Partner input'), "value").replaceFirst("(\\d+) - .*", "\$1")
		String partnerName = WebUI.getAttribute(findTestObject('Object Repository/2. Schedule List/Partner input'), "value").replaceFirst("\\d+ - (.*)", "\$1")
		String budgetCurrency = WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/Budgets')).replaceFirst("([A-Za-z]+).*", "\$1")


		ArrayList<String> budgetTexts = new ArrayList<String>()
		ArrayList<String> budgetCurrencies = new ArrayList<String>()
		if(GlobalVariable.ruleCreate['Apply on'] == "Partner") {
			WebUI.click(findTestObject('2. Schedule List/1. Table/Partner Budget button'))
			ArrayList<WebElement> partnerBudgetsElements = WebUI.findWebElements(findTestObject('Object Repository/2. Schedule List/1. Table/Partner Budgets'), 1)
			ArrayList<WebElement> partnerCurrenciesElements = WebUI.findWebElements(findTestObject('Object Repository/2. Schedule List/1. Table/Partner Currencies'), 1)
			assert partnerBudgetsElements.size() == partnerCurrenciesElements.size() : "\n\nList of budgets is not the same size as the list of currencies.\n\n"
			for (int i = 0; i < partnerCurrenciesElements.size(); i++)
			{
				budgetTexts.add(Float.parseFloat(partnerBudgetsElements[i].getText().replaceFirst("Budget amount: ", "")).toString())
				budgetCurrencies.add(partnerCurrenciesElements[i].getText().replaceFirst("Advertiser currency: ", ""))
			}
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Spend close button'))
		} else {
			float budget = Float.parseFloat(ruleBudget)
			String budgetText = budget.toString()
			if (budgetCurrency=="Imps")
			{
				budgetCurrency = GlobalVariable.ruleCreate["Currency"]
				budgetText = ruleBudget
			}
		}

		String threshold = WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/Thresholds'))
		String specialCharRegex = /[\W_&&[^\s]]/
		String dateRegex = "\\d+-\\d+-\\d+ \\d+:\\d+:\\d+\\+\\d+:\\d+"

		String format
		if(GlobalVariable.ruleCreate['Apply on'] != "Partner") {
			if (warningemail == false)
				format = "Hello\\,\\nThe spend limit has been reached for the rule " + ruleName.replaceAll(specialCharRegex, '\\\\$0') + " at "+dateRegex+" for following condition\\(s\\).\\nInsertion Orders delivery has been automatically paused.\\nPartner: " + partnerName + " \\(id: " + partnerID + "\\)\\nPeriod: " + dateRegex + " - " + dateRegex + "\\nThreshold: " + threshold + "\\nFollowing Insertion Orders have been paused:\\nInsertion Orders? Condition\\(?s?\\)?\\n(?:.*)\\n(?:Success|Failure|Unknown)\\nBudget : (?:Impressions|Currency) " + budgetCurrency + " (?:" + budgetText + "|< 0.01|0)\\nSpent : (?:Impressions|" + budgetCurrency + ") (\\d+(\\.\\d+)?)\\n(?:.*\\n(?:Success|Failure|Unknown)\\n)*The Jellyfish Team\\nCopyright JellyFish - " + LocalDate.now().getYear()
			else if (warningemail == true)
				format = "Hello\\,\\nThe spend limit has been reached for the rule " + ruleName.replaceAll(specialCharRegex, '\\\\$0') + " at "+dateRegex+" for following condition\\(s\\).\\nPartner: " + partnerName + " \\(id: " + partnerID + "\\)\\nPeriod: " + dateRegex + " - " + dateRegex + "\\nThreshold: " + threshold + "\\nInsertion Orders? Condition\\(?s?\\)?\\n(?:.*)\\nBudget : (?:Impressions|Currency) " + budgetCurrency + " (?:" + budgetText + "|< 0.01|0)\\nSpent : (?:Impressions|" + budgetCurrency + ") (\\d+(?:\\.\\d+)?)\\n(?:.*\\n)*The Jellyfish Team\\nCopyright JellyFish - " + LocalDate.now().getYear()
		} else {
			String currenciesRegex = ""
			String budgetsRegex = ""
			for(String bCurrency in budgetCurrencies)
				currenciesRegex += bCurrency + "|"
			for(String bText in budgetTexts)
				budgetsRegex += bText + "|"

			currenciesRegex = currenciesRegex.replaceFirst("\\|\$", "")
			budgetsRegex = budgetsRegex.replaceFirst("\\|\$", "")

			if (warningemail == false) {
				format = "Hello\\,\\nThe spend limit has been reached for the rule " + ruleName.replaceAll(specialCharRegex, '\\\\$0') +
						" at "+dateRegex+" for following condition\\(s\\).\\nInsertion Orders delivery has been automatically paused.\\nPartner: " +
						partnerName + " \\(id: " + partnerID + "\\)\\nPeriod: " + dateRegex + " - " + dateRegex +
						"\\nThreshold: " + threshold +
						"\\nFollowing Insertion Orders have been paused:\\nInsertion Orders? Condition\\(?s?\\)?"+
						"\\n(?:(?:.*\\n(?:Success|Failure|Unknown)\\n)(?:Budget : (?:Impressions|Currency) (?:" + currenciesRegex + ") (?:" +
						budgetsRegex + "< 0.01|0)\\nSpent : (?:" +
						currenciesRegex + "|Impressions) (\\d+(\\.\\d+)?)|Other Insertion Orders)\\n(?:.*\\n(?:Success|Failure|Unknown)\\n)*)*" +
						"The Jellyfish Team\\nCopyright JellyFish - " + LocalDate.now().getYear()
			} else if (warningemail == true) {
				format = "Hello\\,\\nThe spend limit has been reached for the rule " + ruleName.replaceAll(specialCharRegex, '\\\\$0') +
						" at "+dateRegex+" for following condition\\(s\\).\\nPartner: " +
						partnerName + " \\(id: " + partnerID + "\\)\\nPeriod: " + dateRegex + " - " + dateRegex +
						"\\nThreshold: " + threshold +
						"\\nInsertion Orders? Condition\\(?s?\\)?"+
						"\\n(?:(?:.*\\n)(?:Budget : (?:Impressions|Currency) (?:" + currenciesRegex + ") (?:" +
						budgetsRegex + "< 0.01|0)\\nSpent : (?:" +
						currenciesRegex + "|Impressions) (\\d+(?:\\.\\d+)?)|Other Insertion Orders)\\n(?:.*\\n)*)*" +
						"The Jellyfish Team\\nCopyright JellyFish - " + LocalDate.now().getYear()
			}
		}

		KeywordUtil.logInfo("\nMail regex : " + format)

		return format
	}

	@Keyword
	ArrayList<String> checkadvertiserpopup(String type, String identifiant, String appliedOn)
	{
		/**
		 * this function verify the popup when clicking on a rule advertiser in the schedule list
		 * @param type
		 * the type of identifiant to find the rule (Name or ID)
		 * 
		 * @param identifiant
		 * The name or id of the rule to be checked
		 * 
		 * @param appliedon
		 * the type of rule to be checked (Insertion Order or Advertiser)
		 * 
		 * @return list 
		 * list of advertiser of the rule
		 */
		ArrayList<String> list = []
		selenium = getSelenium()

		int pagination = 5
		int numberofrule = Integer.valueOf(WebUI.getText(findTestObject('Object Repository/2. Schedule List/Pagination Range')).find("\\d+\$"))
		if (numberofrule > pagination && selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Pagination First Page button enabled'))))
			WebUI.click(findTestObject('2. Schedule List/Pagination First Page button enabled'))
		TestObject rule = new TestObject()
		for (int i = 0; i < numberofrule; i++)
		{
			if (i % pagination == 0 && i > 0)
				WebUI.click(findTestObject('Object Repository/2. Schedule List/Next page button'))

			rule = changeSelector(findTestObject('Object Repository/2. Schedule List/1. Table/' + type +' iterator'),"nth-child\\(\\d+\\)", "nth-child("+ (i % pagination + 1) +")")
			if (identifiant == WebUI.getText(rule) == true)
			{
				WebUI.click(changeSelector(findTestObject('Object Repository/2. Schedule List/1. Table/Advertiser iterator'), "nth-child\\(\\d+\\)", "nth-child("+ (i % pagination + 1) +")"))
				assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/3. Advertiser popup/popup header'), 10) == true : "Popup advertiser not visible after 10 sec"

				ArrayList<WebElement> OIlist = driver.findElements(By.cssSelector(findTestObject('2. Schedule List/3. Advertiser popup/OI list').getSelectorCollection().get(SelectorMethod.CSS)))
				String[] profileadv = GlobalVariable.ruleCreate["Advertisers"].split(",")
				assert OIlist.size() == profileadv.size() : "The advertisers listed are not correct"
				TestObject Advertiserlist = findTestObject('2. Schedule List/3. Advertiser popup/iterator advertiser popup')
				String txt = ""
				for (int j = 0; j < OIlist.size(); j++)
				{
					Advertiserlist = changeSelector(Advertiserlist,"nth-of-type\\(\\d+\\)", "nth-of-type("+ (j + 2) +")")
					if (appliedOn == "Advertiser")
					{
						boolean found = false
						txt = OIlist[j].getText()
						KeywordUtil.logInfo('txt = ' + txt)
						assert txt == "All IOs" : "OI list: " + txt + " displayed is different than expected: All IOs"
						for (String padv in profileadv)
						{
							if (WebUI.getText(Advertiserlist).contains(padv) == true)
							{
								found = true
								break
							}
						}
						assert found == true : "The advertisers listed are not correct"
						list.add(WebUI.getText(Advertiserlist))
					}
					else if (appliedOn == "Insertion Order")
					{
						txt += OIlist[j].getText()
						list.add(WebUI.getText(Advertiserlist))
						if (j < OIlist.size() - 1)
							txt += ","
					}
				}
				if (appliedOn == "Insertion Order")
				{
					KeywordUtil.logInfo('txt = ' + txt)
					for (String OI in GlobalVariable.ruleCreate["Insertion Orders"].split(","))
						assert txt.contains(OI) == true : "OI in the popup are different than OI in the profile:" + txt
				}
			}
			Thread.sleep(500)
		}
		WebUI.click(findTestObject('Object Repository/2. Schedule List/2. Filter/Filter cross button'))
		Thread.sleep(500)
		return (list)
	}

	@Keyword
	def verifyAdvertiserList(String appliedOn, ArrayList<String> insertionOrders, ArrayList<String> advertisers, ArrayList<String> currencies = [], ArrayList<String> budgetTypes = []) {
		/**
		 * Check the advertiser list in the popup
		 * 
		 * @param appliedOn					Type of rule to be checked : "Insertion Order" or "Advertiser" or "Partner"
		 * @param insertionOrders			List of Insertion Orders, used for Insertion Order rules
		 * @param advertisers				List of Advertisers, used for Insertion Order / Advertiser rules
		 * @param currency					Currency, used for partner rules
		 * @param budgetType				Budget Type, used for partner rules
		 */

		WebUI.click(findTestObject('Object Repository/2. Schedule List/1. Table/Advertisers'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/3. Advertiser popup/popup header'), 10) == true : "Popup advertiser not visible after 10 sec"

		ArrayList<WebElement> ioList = WebUI.findWebElements(findTestObject('2. Schedule List/3. Advertiser popup/OI list'), 5)
		assert ioList.size() == advertisers.size() : "The advertisers listed are not correct"
		TestObject advertiserlist = findTestObject('2. Schedule List/3. Advertiser popup/iterator advertiser popup')
		String ioListText = ""
		String advertiserlistText = ""
		for (int i = 0; i < ioList.size(); i++)
		{
			advertiserlist = changeSelector(advertiserlist,"nth-of-type\\(\\d+\\)", "nth-of-type("+ (i + 2) +")")

			if (appliedOn == "Partner")
			{
				// Conditions can be swapped position when creating the rule,
				// so we check if both columns contain the expected budget type / currency
				advertiserlistText += WebUI.getText(advertiserlist)
				if (i < ioList.size() - 1)
					advertiserlistText += ","
				if(appliedOn == "Partner") assert WebUI.getText(advertiserlist).contains("All advertisers in ")
			} else {
				boolean found = false
				if (appliedOn == "Advertiser") {
					ioListText = ioList[i].getText()
					KeywordUtil.logInfo('IOs : ' + ioListText)
					assert ioListText == "All IOs" : "OI list: " + ioListText + " displayed is different than expected: All IOs"
				}
				for (String padv in advertisers)
				{
					if (WebUI.getText(advertiserlist).contains(padv) == true)
					{
						found = true
						break
					}
				}
				assert found == true : "The advertisers listed are not correct"
			}
			if (appliedOn == "Insertion Order" || appliedOn == "Partner")
			{
				ioListText += ioList[i].getText()
				if (i < ioList.size() - 1)
					ioListText += ","
				if(appliedOn == "Partner") assert ioList[i].getText().contains("All IOs in")
			}
		}
		if (appliedOn == "Insertion Order")
		{
			KeywordUtil.logInfo('IOs displayed = ' + ioListText)
			for (String OI in insertionOrders)
				assert ioListText.contains(OI) : "OI in the popup are different than OI in the profile:" + ioListText
		} else if (appliedOn == "Partner") {
			KeywordUtil.logInfo('IOs displayed = ' + ioListText)
			for (String budgetType in budgetTypes) {
				assert ioListText.contains(budgetType) : "OI in the popup are different than OI in the profile:" + ioListText
				ioListText -= budgetType // Remove budget type from text to verify exact count
			}
			for (String currency in currencies)
				assert advertiserlistText.contains(currency) : "OI in the popup are different than OI in the profile:" + advertiserlistText
		}
		WebUI.click(findTestObject('Object Repository/2. Schedule List/2. Filter/Filter cross button'))
		Thread.sleep(500)
	}

	@Keyword
	def filterCreator(String[] creators)
	{
		/**
		 * Filters the rule list with the specified creators
		 * 
		 * @param creators		List of creators to select in the filter
		 */

		KeywordUtil.logInfo('Creators in filter = ' + creators)
		assert WebUI.verifyElementVisible(findTestObject('2. Schedule List/2. Filter/Filter creator dropdown')) == true : "Filter creator dropdown is not displayed"
		// Check if filter already opened
		if(!selenium.isElementPresent(getSelector(findTestObject('2. Schedule List/2. Filter/Filter search'))))
		{
			WebUI.click(findTestObject('2. Schedule List/2. Filter/Filter creator dropdown'))
			Thread.sleep(500)
			assert WebUI.verifyElementVisible(findTestObject('2. Schedule List/2. Filter/Filter search')) == true : "Filter search is not displayed"
		}
		for (String creator in creators)
		{
			KeywordUtil.logInfo('Current creator = ' + creator)
			WebUI.setText(findTestObject('2. Schedule List/2. Filter/Filter search'), creator)
			assert WebUI.verifyElementVisible(findTestObject('2. Schedule List/2. Filter/Filter search first result')) == true : "Filter creator not displayed in the list"
			WebUI.click(findTestObject('2. Schedule List/2. Filter/Filter search first result'))
			Thread.sleep(500)
			WebUI.click(findTestObject('2. Schedule List/2. Filter/Filter remove search button'))
		}
		WebUI.sendKeys(findTestObject('2. Schedule List/2. Filter/Filter search'), Keys.chord(Keys.ESCAPE))
		WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Overlay'))
		WebUI.delay(2)
	}
	@Keyword
	def filter(String[] filteradv)
	{
		/**
		 * This function filter the rule with the specified advertiser
		 * @param filteradv
		 * The advertiser to select to filter the list of rule
		 */
		KeywordUtil.logInfo('filteradv in filter = ' + filteradv)
		assert WebUI.verifyElementVisible(findTestObject('2. Schedule List/2. Filter/Filter advertiser dropdown')) == true : "Filter advertiser dropdown is not displayed"
		WebUI.click(findTestObject('2. Schedule List/2. Filter/Filter advertiser dropdown'))
		Thread.sleep(500)
		assert WebUI.verifyElementVisible(findTestObject('2. Schedule List/2. Filter/Filter search')) == true : "Filter search is not displayed"

		for (String adv in filteradv)
		{
			KeywordUtil.logInfo('adv in filteradv = ' + adv)
			WebUI.setText(findTestObject('2. Schedule List/2. Filter/Filter search'), adv)
			assert WebUI.verifyElementVisible(findTestObject('2. Schedule List/2. Filter/Filter search first result')) == true : "Filter advertiser not displayed in the list"
			WebUI.click(findTestObject('2. Schedule List/2. Filter/Filter search first result'))
			Thread.sleep(500)
			WebUI.click(findTestObject('2. Schedule List/2. Filter/Filter remove search button'))
		}
		WebUI.sendKeys(findTestObject('2. Schedule List/2. Filter/Filter search'), Keys.chord(Keys.ESCAPE))
	}

	@Keyword
	def Verifyfilter(String[] filteradv)
	{
		/**
		 * This function verify that the rule advertiser displayed matches with the selected filters
		 * @param filteradv
		 * The list of filter selected
		 */
		selenium = getSelenium()
		int pagination = 5
		int numberofrule = Integer.valueOf(WebUI.getText(findTestObject('Object Repository/2. Schedule List/Pagination Range')).find("\\d+\$"))
		if (numberofrule > pagination && selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/Pagination First Page button enabled'))))
			WebUI.click(findTestObject('2. Schedule List/Pagination First Page button enabled'))
		KeywordUtil.logInfo('number of rule = ' + numberofrule)
		TestObject rowadvertiser = new TestObject()
		for (int i = 0; i < numberofrule; i++)
		{
			if (i % pagination == 0 && i > 0)
				WebUI.click(findTestObject('Object Repository/2. Schedule List/Next page button'))

			rowadvertiser = changeSelector(findTestObject('Object Repository/2. Schedule List/1. Table/Advertiser iterator'),"nth-child\\(\\d+\\)", "nth-child("+ (i % pagination + 1) +")")

			if (WebUI.getText(rowadvertiser).contains(" Advertisers") == true)
			{
				boolean found = false
				WebUI.click(rowadvertiser)
				WebUI.delay(1)
				ArrayList<WebElement> Listadv = driver.findElements(By.cssSelector(findTestObject('2. Schedule List/3. Advertiser popup/advertiser popup list').getSelectorCollection().get(SelectorMethod.CSS)))
				for (def adv in Listadv)
				{
					KeywordUtil.logInfo('filteradv = ' + filteradv)
					KeywordUtil.logInfo('adv = ' + adv.getText())
					if (filteradv.contains(adv.getText()) == true)
					{
						found = true
						break
					}
				}
				WebUI.click(findTestObject('Object Repository/2. Schedule List/2. Filter/Filter cross button'))
			}
			else
				assert filteradv.contains(WebUI.getText(rowadvertiser)) == true : "Unfiltered advertiser is displayed: " + WebUI.getText(rowadvertiser)

			Thread.sleep(500)
		}
	}

	@Keyword
	def Search(String searchtxt, String type = "name", boolean expectresult=true)
	{
		/**
		 * Search an item
		 * 
		 * @param searchtxt			The searched text
		 * @param type		 		The column to verify
		 * @param expectresult		Set to true to expect a result, false to expect an empty list
		 */
		WebUI.sendKeys(findTestObject('2. Schedule List/Search bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
		if (expectresult) assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/First Rule Menu button'), 10) : "The first rule menu button is not visible after 10 seconds"
		WebUI.sendKeys(findTestObject('2. Schedule List/Search bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
		Thread.sleep(500)
		WebUI.sendKeys(findTestObject('2. Schedule List/Search bar input'), searchtxt)
		Thread.sleep(500)
		if (expectresult == true)
		{
			if (type == "name")
				assert WebUI.getText(findTestObject('2. Schedule List/1. Table/Names')) == searchtxt : "No result found for search: " + searchtxt
			if (type == "ID")
				assert WebUI.getText(findTestObject('2. Schedule List/1. Table/IDs')) == searchtxt : "No result found for search: " + searchtxt
		}
		else if (expectresult == false)
			assert WebUI.verifyElementPresent(findTestObject('2. Schedule List/empty rule list'), 10) == true : "Search " + searchtxt + " is not found"
	}

	@Keyword
	def checkColumnValue(String columnName, String value)
	{
		/**
		 *  Check the first row of a specific column in the rule list
		 *  
		 *  @param columnName		Column name
		 *  @param value			Expected value of the column first row
		 */

		assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/'+columnName+'s')).replaceFirst("fiber_manual_record\\n", "").replaceFirst("pause\\n", "") == value
	}

	@Keyword
	def checkAdvertiserColumn()
	{
		/**
		 *  Check the advertiser column content
		 *
		 */

		if(GlobalVariable.ruleCreate['Apply on'] == "Partner")
			WebUI.click(findTestObject('Object Repository/2. Schedule List/1. Table/Advertisers'))
		else if(GlobalVariable.ruleCreate['Advertiser'].split(",").size() > 1)
			WebUI.click(findTestObject('Object Repository/2. Schedule List/1. Table/Advertisers'))
		else
			checkColumnValue("Advertiser", GlobalVariable.ruleCreate['Advertiser'])
	}

	def randomSearch()
	{
		/**
		 * Search for a random item in the Schedule list
		 * 
		 * @param fieldName 	Name of the field to search : "ID", "Name", "Budget", "Threshold", "Recipients"
		 */

		assert WebUI.waitForElementVisible(findTestObject('2. Schedule List/1. Table/Table'), 15)
		assert WebUI.waitForElementNotPresent(findTestObject('3. Schedule Editor/Progress bar'), 60)
		Thread.sleep(1000)
		assert WebUI.waitForElementNotPresent(findTestObject('3. Schedule Editor/Progress bar'), 60)
		assert WebUI.waitForElementPresent(findTestObject('2. Schedule List/1. Table/Analysis Periods'), 60) : KeywordUtil.markFailedAndStop("No rule to search in current tab")

		listAnalysisPeriods = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Analysis Periods'), 1)
		listAnalysisPeriods.each {
			listAnalysisPeriodsText.add(it.getText().replaceFirst("fiber_manual_record", "").replaceFirst("pause\\n", ""))
		}

		listIds = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/IDs'), 1)

		listIds.each {
			listIdsText.add(it.getText())
		}

		listNames = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Names'), 1)
		listNames.each { listNamesText.add(it.getText()) }

		listBudgets = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Budgets'), 1)
		listBudgets.each {
			listBudgetsText.add(it.getText().replaceFirst("[A-Za-z]+ ", ""))
		}

		listThresholds = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Thresholds'), 1)
		listThresholds.each { listThresholdsText.add(it.getText().replaceFirst("%", "")) }

		listAdvertisers = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Advertisers'), 1)
		listHiddenElements = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Elements hidden text'), 1)
		listAdvertisers.eachWithIndex { it, inc ->
			if(it.getText().contains("Advertisers")) {
				WebElement currentElement = it
				WebElement hiddenElement = listHiddenElements.find {
					it.getAttribute("id") == currentElement.getAttribute("aria-describedby")
				}
				listAdvertisersText.add(hiddenElement.getAttribute("textContent").split(",")[0])
			} else {
				listAdvertisersText.add(it.getText())
			}
		}

		listRecipients = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Recipients'), 1)
		listRecipients.eachWithIndex { it, inc ->
			if(it.getText().contains("emails")) {
				WebElement currentElement = it
				WebElement hiddenElement = listHiddenElements.find {
					it.getAttribute("id") == currentElement.findElement(By.xpath(".//a")).getAttribute("aria-describedby")
				}
				listRecipientsText.add(hiddenElement.getAttribute("textContent"))
			} else {
				listRecipientsText.add(it.getText())
			}
		}

		searchRandomValue("Analysis Period", listAnalysisPeriodsText)
		searchRandomValue("ID", listIdsText)
		searchRandomValue("Name", listNamesText)
		searchRandomValue("Budget", listBudgetsText)
		searchRandomValue("Threshold", listThresholdsText)
		searchRandomValue("Recipient", listRecipientsText)
	}
	def searchRandomValue(String fieldName, ArrayList<String> expectedTextList)
	{
		/**
		 * Search a random value from a list for a specific field 
		 *
		 * @param fieldName 			Name of the field to search : "Analysis Period", "ID", "Name", "Budget", "Threshold", "Advertiser", "Recipient"
		 * @param expectedTextList		Array of strings to randomly choose from
		 */

		KeywordUtil.logInfo("Searching random value for field : "+ fieldName)
		int randomIndex = 0
		randomIndex = new Random().nextInt(expectedTextList.size())
		Thread.sleep(500)
		Search(expectedTextList.get(randomIndex), "else")

		boolean searchWorks = false
		String expectedText = ""
		String displayedText = ""
		ArrayList<WebElement> listElements = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/'+fieldName+'s'), 1)
		int inc2 = 0
		for(def element in listElements) {
			expectedText = expectedTextList.get(randomIndex)
			if(fieldName == "Budget") {
				expectedText = expectedTextList.get(randomIndex)
				displayedText = element.getText().replaceFirst("[A-Za-z]+ ", "")
			} else if(fieldName == "Threshold") {
				expectedText = expectedTextList.get(randomIndex) + "%"
				displayedText = element.getText()
			} else if(fieldName == "Analysis Period") {
				displayedText = element.getText().replaceFirst("fiber_manual_record", "").replaceFirst("pause\\n", "")
			} else { displayedText = element.getText() }
			ArrayList<WebElement> listHiddenElements = new ArrayList<WebElement>()
			if(fieldName == "Recipient" || fieldName == "Advertiser") {
				listHiddenElements = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Elements hidden text'), 1)
				// For recipients, we compare the hidden ones in parameters with the new ones
				if((fieldName == "Recipient" && displayedText.contains("emails"))) {
					WebElement hiddenElement = listHiddenElements.find {
						it.getAttribute("id") == element.findElement(By.xpath(".//a")).getAttribute("aria-describedby")
					}
					displayedText = hiddenElement.getAttribute("textContent")
				} else if (fieldName == "Advertiser" && displayedText.contains("Advertisers")) {
					WebElement hiddenElement = listHiddenElements.find {
						it.getAttribute("id") == element.getAttribute("aria-describedby")
					}
					displayedText = hiddenElement.getAttribute("textContent")
					displayedText = displayedText.split(",")[0]
				}
			}
			if(displayedText == expectedText) {
				searchWorks = true
				KeywordUtil.logInfo(fieldName + " " + expectedText + " found.")
			}
		}

		assert listElements.size() > 0 : "Something went wrong with "+fieldName+" search, the list of rules is empty. Maybe the user added extra characters in the search"
		assert searchWorks == true : "Search did not work. Expected "+fieldName+" : " + expectedText + ", Displayed : " + displayedText
		WebUI.sendKeys(findTestObject('Object Repository/2. Schedule List/Search Bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
	}

	def checkSort()
	{
		/**
		 * Check the sorting feature of most fields
		 */

		assert WebUI.waitForElementVisible(findTestObject('2. Schedule List/1. Table/Table'), 15)
		assert WebUI.waitForElementNotPresent(findTestObject('3. Schedule Editor/Progress bar'), 60)
		Thread.sleep(1000)
		assert WebUI.waitForElementNotPresent(findTestObject('3. Schedule Editor/Progress bar'), 60)
		WebUI.sendKeys(findTestObject('Object Repository/2. Schedule List/Search Bar input'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
		assert WebUI.waitForElementPresent(findTestObject('2. Schedule List/1. Table/IDs'), 60) : KeywordUtil.markFailedAndStop("No rule to sort in current tab")

		int numberOfRules = Integer.valueOf(WebUI.getText(findTestObject('Object Repository/2. Schedule List/Pagination Range')).replaceFirst("1 . \\d+ of (\\d+)", "\$1"))
		int numberOfPages = 1 + Math.floor(numberOfRules / 5)
		if(numberOfRules % 5 == 0) numberOfPages--

		for(numberOfPages; numberOfPages > 0; numberOfPages--) {
			KeywordUtil.logInfo("Remaining pages to store field values from : "+ numberOfPages)
			listIds = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/IDs'), 1)
			listIds.each {
				listIdsText.add(it.getText())
			}

			listNames = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Names'), 1)
			listNames.each { listNamesText.add(it.getText()) }

			listBudgets = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Budgets'), 1)
			listBudgets.each {
				listBudgetsText.add(it.getText().replaceFirst("[A-Za-z]+ ", ""))
			}

			listThresholds = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Thresholds'), 1)
			listThresholds.each { listThresholdsText.add(it.getText().replaceFirst("%", "")) }

			listRecipients = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Recipients'), 1)
			listHiddenElements = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Elements hidden text'), 1)
			listRecipients.eachWithIndex { it, inc ->
				if(it.getText().contains("emails")) {
					WebElement currentElement = it
					WebElement hiddenElement = listHiddenElements.find {
						it.getAttribute("id") == currentElement.findElement(By.xpath(".//a")).getAttribute("aria-describedby")
					}
					listRecipientsText.add(hiddenElement.getAttribute("textContent"))
				} else {
					listRecipientsText.add(it.getText())
				}
			}

			if(numberOfPages > 1) WebUI.click(findTestObject('Object Repository/2. Schedule List/Next Page button'))
		}
		WebUI.click(findTestObject('Object Repository/2. Schedule List/First Page button'))

		checkFieldSort("ID", listIdsText)
		checkFieldSort("Name", listNamesText)
		// checkFieldSort("Budget", listBudgetsText)
		checkFieldSort("Threshold", listThresholdsText)
		checkFieldSort("Recipient", listRecipientsText)
	}
	def checkFieldSort(String fieldName, ArrayList<String> expectedTextList)
	{
		/**	
		 * Check the ascending and descending sort feature of specified field
		 * 
		 * @param fieldName 	Name of the field to search : "ID", "Name", "Budget", "Threshold", "Recipient"
		 * @param expectedTextList 	List of all field values to sort
		 */
		KeywordUtil.logInfo("Checking sort for field : "+ fieldName)
		// Ascending sort
		WebUI.click(findTestObject('Object Repository/2. Schedule List/1. Table/Sort '+fieldName+'s button'))
		if(fieldName == "Budget" || fieldName == "ID" || fieldName == "Threshold") expectedTextList = expectedTextList.sort { Float.valueOf(it) }
		else Collections.sort(expectedTextList, String.CASE_INSENSITIVE_ORDER)
		String displayedText = ""
		ArrayList<WebElement> listHiddenElements = new ArrayList<WebElement>()
		ArrayList<String> listDisplayedHiddenRecipients = new ArrayList<String>()
		if(fieldName == "Recipient") listHiddenElements = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Elements hidden text'), 1)
		ArrayList<WebElement> listElements = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/'+fieldName+'s'), 1)

		listElements.eachWithIndex { elem, inc ->
			String expectedText = expectedTextList.get(inc)
			if(fieldName == "Budget") {
				expectedText = expectedTextList.get(inc)
				displayedText = elem.getText().replaceFirst("[A-Za-z]+ ", "")
			} else if(fieldName == "Threshold") {
				expectedText = expectedTextList.get(inc) + "%"
				displayedText = elem.getText()
			} else { displayedText = elem.getText() }
			// For recipients, we compare the hidden ones in parameters with the new ones
			if(fieldName == "Recipient" && displayedText.contains("emails")) {
				WebElement hiddenElement = listHiddenElements.find {
					it.getAttribute("id") == elem.findElement(By.xpath(".//a")).getAttribute("aria-describedby")
				}
				displayedText = hiddenElement.getAttribute("textContent")
			}
			assert displayedText == expectedText : "Ascending sort did not work. Expected "+fieldName+" : " + expectedText + ", Displayed : " + displayedText
		}
		KeywordUtil.logInfo("Ascending sort complete")

		// Descending sort
		WebUI.click(findTestObject('Object Repository/2. Schedule List/1. Table/Sort '+fieldName+'s button'))
		if(fieldName == "Budget" || fieldName == "ID" || fieldName == "Threshold") expectedTextList = (ArrayList<String>) expectedTextList.reverse()
		else Collections.sort(expectedTextList, String.CASE_INSENSITIVE_ORDER.reversed())

		if(fieldName == "Recipient") listHiddenElements = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/Elements hidden text'), 1)
		listElements = WebUI.findWebElements(findTestObject('2. Schedule List/1. Table/'+fieldName+'s'), 1)
		listElements.eachWithIndex { elem, inc ->
			String expectedText = expectedTextList.get(inc)
			if(fieldName == "Budget") {
				expectedText = expectedTextList.get(inc)
				displayedText = elem.getText().replaceFirst("[A-Za-z]+ ", "")
			} else if(fieldName == "Threshold") {
				expectedText = expectedTextList.get(inc) + "%"
				displayedText = elem.getText()
			} else { displayedText = elem.getText() }
			// For recipients, we compare the hidden ones in parameters with the new ones
			if(fieldName == "Recipient" && displayedText.contains("emails")) {
				WebElement hiddenElement = listHiddenElements.find {
					it.getAttribute("id") == elem.findElement(By.xpath(".//a")).getAttribute("aria-describedby")
				}
				displayedText = hiddenElement.getAttribute("textContent")
			}
			assert displayedText == expectedText : "Descending sort did not work. Expected "+fieldName+" : " + expectedText + ", Displayed : " + displayedText
		}
		KeywordUtil.logInfo("Descending sort complete")
	}

	@Keyword
	def checkpausedOI(ArrayList<String> OIdv, ArrayList<String> OIemail)
	{
		boolean hasmatch
		for (String OI in OIemail)
		{
			hasmatch = false
			for (String IO in OIdv)
			{
				String[] IOdata = IO.split(';')
				if (IOdata[2] == OI)
				{
					hasmatch = true
					//	assert IOdata[0] != 'paused' : "The OI " + IOdata[2] + " is not active"
				}

			}
			assert hasmatch == true : "The OI : " + OI + " was not on dv, it should not be in the email"
		}
	}

	@Keyword
	def deleteRule(String ruleName, boolean cancel = false)
	{
		/**
		 *  Delete a rule
		 *  
		 *  @param ruleName		Name of the rule to delete
		 *  @param cancel		Set to true to click on the 'Cancel' button, false otherwise
		 */
		Search(ruleName)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/First Rule Menu button'), 30) : "Menu button is not visible after 30 seconds"
		WebUI.click(findTestObject('Object Repository/2. Schedule List/First Rule Menu button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Menu Delete button'), 30) : "Menu Delete button is not visible after 30 seconds"
		WebUI.click(findTestObject('Object Repository/2. Schedule List/Menu Delete button'))
		Thread.sleep(750)
		if(cancel == true) {
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Cancel Delete button'))
		} else {
			WebUI.click(findTestObject('Object Repository/2. Schedule List/Menu Delete Confirm button'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Notification text'), 60) == true : "Delete confirmation message not displayed after 60sec"
			assert WebUI.verifyElementText(findTestObject('Object Repository/2. Schedule List/Notification text'), "The rule has been successfully deleted") == true : "Delete confirmation message is not correct"
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/2. Schedule List/Notification text'), 15) == true : "Delete confirmation message is still displayed after 15sec"
		}
	}

	@Keyword
	def checkInputs(Map ruleMap, boolean isEdition = false)
	{
		/**
		 * Check inputs are correct in editor before saving the rule
		 * 
		 * @param ruleMap
		 * 		ruleCreate or ruleEdit or ruleDuplicate
		 *
		 */

		ArrayList<String> advProfile = new ArrayList<String>()
		ArrayList<String> cpProfile = new ArrayList<String>()
		ArrayList<String> oiProfile = new ArrayList<String>()

		//Verify applied on
		String appliedOnPartner = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Apply on Partner is checked'))+"')", null)
		String appliedOnAdvertiser = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Apply on Advertiser is checked'))+"')", null)
		String appliedOnOi = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Apply on insertion order is checked'))+"')", null)
		if (appliedOnPartner != null)
		{
			assert GlobalVariable.ruleCreate["Apply on"] == "Partner": "The rule is applied on the wrong object"
			KeywordUtil.markPassed("Rule is applied on Partner")
		}
		else if (appliedOnAdvertiser !=null)
		{
			assert GlobalVariable.ruleCreate["Apply on"] == "Advertiser": "The rule is applied on the wrong object"
			KeywordUtil.markPassed("Rule is applied on Advertiser")
		}
		else if (appliedOnOi !=null)
		{
			assert GlobalVariable.ruleCreate["Apply on"] == "Insertion Order": "The rule is applied on the wrong object"
			KeywordUtil.markPassed("Rule is applied on Insertion Order")
		}

		if (GlobalVariable.ruleCreate["Apply on"] != "Partner")
		{
			//Verify Budget value
			assert WebUI.verifyElementAttributeValue(findTestObject('3. Schedule Editor/Budget input'), "value", ruleMap["Budget Value"], 1): "Budget Value is not set corretly"
			KeywordUtil.markPassed("Budget value is ok")

			//Verify Budget type
			String budgetType = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Budget type selector'))+"')", null)
			if (budgetType == null)
			{
				assert ruleMap["Budget Type"] == "Impressions"
				KeywordUtil.markPassed("Budget type is impression")
			}
			else if (budgetType != null)
			{
				assert ruleMap["Budget Type"] == "Currency"
				KeywordUtil.markPassed("Budget type is currency")
			}

			//Advertiser Currency
			assert WebUI.verifyElementText(findTestObject('3. Schedule Editor/Advertiser Currency Select text'), ruleMap["Currency"])
			KeywordUtil.markPassed("Advertiser currency is ok")

			//Advertisers
			String Advertiser = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Advertisers select'))+"').textContent.split(',')", null)
			advProfile = ruleMap["Advertisers"].split(',')
			for (int i=0; i<advProfile.size();i++)
			{
				assert Advertiser.contains(advProfile[i]): "Following advertiser is not set correctly: "+advProfile[i]
			}
			KeywordUtil.markPassed("Advertiser is ok")

			//Campaign and insertion order
			if (GlobalVariable.ruleCreate["Apply on"] == "Insertion Order")
			{
				//Verify Campaign
				String campaign = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Campaigns Select button'))+"').textContent.split(',')", null)
				cpProfile = ruleMap["Campaigns"].split(',')
				for (int i=0; i<cpProfile.size();i++)
				{
					assert campaign.contains(cpProfile[i]): "Following advertiser is not set correctly: "+cpProfile[i]
				}
				KeywordUtil.markPassed("Campaign is ok")
				//Verify Insertion order
				String insertionOrder = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Insertion Orders Select button'))+"').textContent.split(',')", null)
				oiProfile = ruleMap["Insertion Orders"].split(',')
				for (int i=0; i<oiProfile.size();i++)
				{
					assert insertionOrder.contains(oiProfile[i]): "Following advertiser is not set correctly: "+oiProfile[i]
				}
				KeywordUtil.markPassed("Insertion order is ok")
			}
		}
		else if (GlobalVariable.ruleCreate["Apply on"] == "Partner")
		{
			ArrayList<String> condition = new ArrayList<String>()
			if(!isEdition)
				condition = parseCondition()
			else
				condition = parseCondition(GlobalVariable.ruleEdit['Condition'], GlobalVariable.ruleCreate['Condition'], GlobalVariable.ruleEdit['KeepOldCondition'])
			String budgetPartner=""
			String budgetTypePartner=""
			String advertiserCurrencyPartner=""
			String budgetValuesize = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('3. Schedule Editor/Budget input'))+"').length", null)

			for (int k=0; k<condition[0].size();k++)
			{
				budgetPartner = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('3. Schedule Editor/Budget input'))+"').item("+k+").value", null)
				budgetTypePartner = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/1. Partner/Budget type selector partner'))+"').item("+(k+1)+").textContent.trim()", null)
				advertiserCurrencyPartner = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('3. Schedule Editor/Advertiser Currency Select text'))+"').item("+k+").textContent", null)

				assert condition[0][k] == budgetPartner : "Budget set is not correct: "+budgetPartner
				assert condition[1][k] == budgetTypePartner : "Budget type set is not correct: "+budgetTypePartner
				assert condition[2][k] == advertiserCurrencyPartner : "Advertiser currency set is not correct: "+advertiserCurrencyPartner

				KeywordUtil.markPassed("Budget value for Partner is ok")
				KeywordUtil.markPassed("Budget type for Partner is ok")
				KeywordUtil.markPassed("Advertiser currency for Partner is ok")
			}

		}

		//Verify Threshold
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/3. Schedule Editor/Threshold input'), "value", ruleMap["Threshold"], 1) : "Threshold is not set correctly"
		KeywordUtil.markPassed("Threshold is ok")

		//Verify Rule name
		String ruleName = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Rule Name input'))+"').value", null)
		assert ruleName.contains(ruleMap["Rule Name"]): "Rule name is not set correctly"
		KeywordUtil.markPassed("Rule name is ok")

		//Verify start date
		String[] startDateProfile = new String[3]
		String startDate = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Start Date input value'))+"').value", null)
		startDateProfile = ruleMap["Start Date"].split("/")
		startDateProfile = [String.format("%02d", Integer.valueOf(startDateProfile[1])), String.format("%02d", Integer.valueOf(startDateProfile[0])), startDateProfile[2]]
		String startDateparsed = String.join("/", startDateProfile)
		assert startDate == startDateparsed : "Start date set is not set correctly. Displayed: "+startDate+", Expected: "+startDateparsed
		KeywordUtil.markPassed("Start date is ok")

		//Verify end date
		String[] endDateProfile = new String[3]
		String endDate = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/End Date input value'))+"').value", null)
		endDateProfile = ruleMap["End Date"].split("/")
		endDateProfile = [String.format("%02d", Integer.valueOf(endDateProfile[1])), String.format("%02d", Integer.valueOf(endDateProfile[0])), endDateProfile[2]]
		String endDateparsed = String.join("/", endDateProfile)
		assert endDate == endDateparsed : "End date set is not set correctly. Displayed: "+endDate+", Expected: "+endDateparsed
		KeywordUtil.markPassed("End date is ok")

		//Verify recipient
		ArrayList<String> recipients = new ArrayList<String>()
		ArrayList<String> recipientsProfile = new ArrayList<String>()
		recipients = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Email list'))+"').textContent.split('cancel')", null)

		assert recipients.contains(GlobalVariable.googleID): "Recipient is not correct"
		if (ruleMap["Recipients"] != "")
		{
			recipientsProfile = ruleMap["Recipients"].split(',')
			for (int j=0;j<(recipientsProfile.size());j++)
			{
				assert recipients.contains(recipientsProfile[j]): "Recipient is not correct"
			}
		}
		KeywordUtil.markPassed("Recipient is ok")
	}

	@Keyword
	def historicRule()
	{
		/**
		 * Check the historic for a rule
		 * 
		 */


		selectTab()
		String ruleID = GlobalVariable.ruleID
		String displayUserChanges = "true"
		String displaySystemChanges = "true"
		ArrayList<String> setupRule = new ArrayList<String>()
		ArrayList<String> listOfObject = new ArrayList<String>()

		int i=0

		Thread.sleep(500)
		WebUI.sendKeys(findTestObject('Object Repository/2. Schedule List/Search Bar input'), ruleID)

		WebUI.delay(2)
		assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/IDs')) == ruleID: "Rule is not correct"

		listOfObject = checkAppliedOnRule()
		setupRule = getSetupInARule(listOfObject[0])

		WebUI.click(findTestObject('Object Repository/2. Schedule List/4. Historic/Historic button'))

		//check rule name
		String ruleNameHistory = WebUI.getText(findTestObject("Object Repository/2. Schedule List/4. Historic/Rule name"))
		KeywordUtil.logInfo("Rule name expected: "+setupRule[0])
		assert ruleNameHistory.contains(setupRule[0]): "Rule name is not correct in historic page: "+ruleNameHistory

		//check rule level
		String ruleLevel = WebUI.getText(findTestObject("Object Repository/2. Schedule List/4. Historic/Rule level"))
		assert ruleLevel == ("Rule level: "+listOfObject[0]): "Rule Level is not correct in historic page: "+ruleLevel

		//Verify toggles are true
		assert displayUserChanges == WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display user changes toggle'))+"').ariaChecked", null)
		assert displaySystemChanges == WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display system changes toggle'))+"').ariaChecked", null)
		KeywordUtil.logInfo("Display user changes expected: "+displayUserChanges)
		KeywordUtil.logInfo("Display system changes expected: "+displaySystemChanges)
		assert WebUI.waitForElementPresent(findTestObject("Object Repository/2. Schedule List/4. Historic/History card"), 10): "Toggles are checked and no rules are displayed"

		//Check all field are correct
		checkFields(setupRule, listOfObject)


	}

	def checkFields(ArrayList<String> setupRule, ArrayList<String> listOfObject)
	{
		/**
		 * Check all fields are correct
		 * 
		 * @param setupRule
		 * 		tab contains the rule setup
		 * @param listOfObject
		 * 		Contains rule level and List of advertiser or insertion order to check in history
		 *
		 */

		String fieldName = ""
		String fieldValue = ""
		String length = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Fields'))+"').length", null)
		ArrayList<String> fieldNametab = new ArrayList<String>()

		for (int i=1; i< length.toInteger(); i++)
		{
			fieldName = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"nth-child\\(\\d+\\)", "nth-child("+i+")"))

			if (fieldNametab.contains(fieldName)) continue
			else fieldNametab.add(fieldName)

			switch(fieldName)
			{

				case "Rule name":
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					assert fieldValue.contains(setupRule[0]): "Wrong value. Displayed: "+fieldValue+". Expected: "+setupRule[0]
					break
				case "End date":
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					assert fieldValue.contains(setupRule[8]): "Wrong value. Displayed: "+fieldValue+". Expected: "+setupRule[8]
					break
				case "Start date":
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					assert fieldValue.contains(setupRule[7]): "Wrong value. Displayed: "+fieldValue+". Expected: "+setupRule[7]
					break
				case "Partner":
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					String Partner = GlobalVariable.partnerName
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					assert fieldValue.contains(Partner): "Wrong value. Displayed: "+fieldValue+". Expected: "+Partner
					break
				case "Other delivery pause":
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					assert fieldValue.contains(setupRule[2]): "Wrong value. Displayed: "+fieldValue+". Expected: "+setupRule[2]
					break
				case "Budget limits":
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					if (listOfObject[0] == "Partner")
					{
						for (int j =0; j<setupRule[3].size();j++)
						{
							assert fieldValue.contains(setupRule[3][j].toString()): "Wrong value. Displayed: "+fieldValue+". Expected: "+setupRule[3][j].toString()
						}
					}
					break
				case "Threshold":
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					fieldValue = fieldValue + "%"
					assert fieldValue.contains(setupRule[1]): "Wrong value. Displayed: "+fieldValue+". Expected: "+setupRule[1]
					break
				case "Warning only":
					if (listOfObject[0] == "Partner" && GlobalVariable.tabChosen == "Passed")
						break
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					assert fieldValue.contains(setupRule[5]): "Wrong value. Displayed: "+fieldValue+". Expected: "+setupRule[5]
					break
				case "Rule Status":
					if (listOfObject[0] == "Partner" && GlobalVariable.tabChosen == "Passed")
						break
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					assert fieldValue.contains(setupRule[4]): "Wrong value. Displayed: "+fieldValue+". Expected: "+setupRule[4]
					break
				case "Recipients":
					if (listOfObject[0] == "Partner" && GlobalVariable.tabChosen == "Passed")
						break
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					assert fieldValue.contains(setupRule[6]): "Wrong value. Displayed: "+fieldValue+". Expected: "+setupRule[6]
					break
				case "Advertisers":
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					if (listOfObject[0] == "Insertion Order(s)")
					{
						listOfObject[1] = "-"
						assert fieldValue.contains(listOfObject[1]): "Wrong value. Displayed: "+fieldValue+". Expected: "+listOfObject[1]
					}
					else
					{
						for (int j=0;j<listOfObject[1].size();j++)
						{
							assert fieldValue.contains(listOfObject[1][j]): "Wrong value. Displayed: "+fieldValue+". Expected: "+listOfObject[1][j]
						}
					}
					break
				case "Insertion Orders":
					WebUI.scrollToElement((changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)")),5)
					fieldValue = WebUI.getText(changeSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/History cells'),"tr\\:nth-child\\(\\d+\\) \\> td\\:nth\\-of\\-type\\(2\\)", "tr:nth-child("+i+") > td:nth-of-type(4)"))
					for (int j=0;j<listOfObject[2].size();j++)
					{
						assert fieldValue.contains(listOfObject[2][j].trim().replace("  "," ")): "Wrong value. Displayed: "+fieldValue+". Expected: "+listOfObject[2][j].replace("  "," ")
					}
					break
				default:
					continue

			}
		}

	}
	def selectTab()
	{
		/**
		 * Select the tab between Passed, Scheduled or Future
		 * 
		 */

		String tab = GlobalVariable.tabChosen
		WebUI.click(changeSelectorXpath(findTestObject('Object Repository/2. Schedule List/Tabs'),"tab", tab.toLowerCase()))
		WebUI.delay(1)
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/2. Schedule List/Circle waiting'), 10)

	}
	def checkAppliedOnRule()
	{
		/**
		 * 
		 * Check on which object the rule is applied on
		 * 
		 * @return objectRule
		 * 			Partner, advertiser or partner
		 * 
		 */

		String objectRule = ""
		ArrayList<String> Ois = new ArrayList<String>()
		ArrayList<String> Advs = new ArrayList<String>()
		String AdvsNb = ""

		String OiPopup = ""

		WebUI.click(findTestObject('Object Repository/2. Schedule List/1. Table/Advertiser iterator'))
		OiPopup = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/3. Advertiser popup/OI list'))+"').textContent", null)
		if (OiPopup == "All IOs in Currency" || OiPopup == "All IOs in Impressions")
		{
			objectRule = "Partner"
			Advs.add("-")
			Ois.add("-")
		}
		else if (OiPopup == "All IOs")
		{
			objectRule = "Advertiser(s)"
			Ois.add("-")
			AdvsNb = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/3. Advertiser popup/advertiser popup list'))+"').length", null)

			for (int i =0; i<AdvsNb.toInteger();i++)
			{
				Advs.add(WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/3. Advertiser popup/advertiser popup list'))+"').item("+i+").textContent.trim()", null))
			}
		}
		else
		{
			objectRule = "Insertion Order(s)"
			Ois = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/3. Advertiser popup/OI list'))+"').textContent.trim().split(',')", null)
			AdvsNb = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/3. Advertiser popup/advertiser popup list'))+"').length", null)

			for (int i =0; i<AdvsNb.toInteger();i++)
			{
				Advs.add(WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/3. Advertiser popup/advertiser popup list'))+"').item("+i+").textContent.trim()", null))
			}
		}
		KeywordUtil.logInfo("Rule level expected: "+objectRule)
		WebUI.click(findTestObject('Object Repository/2. Schedule List/Spend close button'))
		WebUI.delay(1)

		return [objectRule, Advs, Ois]
	}
	@Keyword
	def checkHistoricFields()
	{
		/**
		 * Check all fields are present
		 */

		List<String> historyFields = ['Threshold', 'End date', 'Partner', 'Rule Status', 'Rule name', 'Other delivery pause', 'Advertisers', 'Budget limits', 'Insertion Orders', 'Campaigns', 'Warning only', 'Recipients', 'Creator', 'Start date', 'Email sending', 'Insertion Orders status']
		String Fields = ""
		String length = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Fields'))+"').length", null)

		for(int i=1; i<length.toInteger();i++)
		{
			Fields = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Fields'))+"').item("+i+").textContent", null)
			if (Fields == "Field")
			{
				i++
				Fields = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Fields'))+"').item("+i+").textContent", null)

			}
			assert historyFields.contains(Fields): "Fields is not correct: "+Fields
		}

	}
	@Keyword
	def checkHistoricUser(boolean displayUserChanges, boolean displaySystemChanges)
	{
		/**
		 * Check User field is correct according to the filters.
		 * If only the first toggle is true, only user mails are displayed on User fields.
		 * If only the second toggle is true, "System" is displayed on User fields
		 * 
		 * @param displayUserChanges
		 * 		First toggle state
		 * @param displaySystemChanges
		 * 		Second toggle state
		 */

		String User = ""
		String length = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Users'))+"').length", null)

		if (displayUserChanges && !displaySystemChanges)
		{
			for(int i=1; i<length.toInteger();i++)
			{
				User = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Users'))+"').item("+i+").textContent", null)
				if (User == "User")
				{
					i++
					User = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Users'))+"').item("+i+").textContent", null)

				}
				assert User.contains("@jellyfish.com"): "User is not correct: "+User
				assert !User.contains("System"): "User is not correct: "+User
			}
		}
		else if (!displayUserChanges && displaySystemChanges)
		{
			for(int i=1; i<length.toInteger();i++)
			{
				User = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Users'))+"').item("+i+").textContent", null)
				if (User == "User")
				{
					i++
					User = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Users'))+"').item("+i+").textContent", null)

				}
				assert User.contains("System"): "User is not correct: "+User
				assert !User.contains("@jellyfish.com"): "User is not correct: "+User
			}
		}
	}
	@Keyword
	def manageFilter(boolean displayUserChanges, boolean displaySystemChanges)
	{
		/**
		 * Manager both filter in history page
		 * 
		 * @param displayUserChanges
		 * 		True if we want to activate this filter, false otherwise
		 * @param displaySystemChanges
		 * 		True if we want to activate this filter, false otherwise
		 */

		if (displayUserChanges && !displaySystemChanges)
		{
			if(WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display user changes toggle'))+"').ariaChecked", null) == "false")
			{
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display user changes toggle'))+"').click()", null)
			}
			if(WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display system changes toggle'))+"').ariaChecked", null) == "true")
			{
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display system changes toggle'))+"').click()", null)
			}
			if (selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/No data text'))))
			{
				KeywordUtil.markErrorAndStop("No data are present after checking Display user changes filter")
			}

		}
		else if (!displayUserChanges && displaySystemChanges)
		{
			if(WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display user changes toggle'))+"').ariaChecked", null) == "true")
			{
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display user changes toggle'))+"').click()", null)
			}
			if(WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display system changes toggle'))+"').ariaChecked", null) == "false")
			{
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display system changes toggle'))+"').click()", null)
			}

			if (selenium.isElementPresent(getSelector(findTestObject('Object Repository/2. Schedule List/4. Historic/No data text'))))
			{
				KeywordUtil.markWarning("No data are present after checking Display system changes filter")
			}
		}
		else if (!displayUserChanges && !displaySystemChanges)
		{
			if(WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display user changes toggle'))+"').ariaChecked", null))
			{
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display user changes toggle'))+"').click()", null)
			}
			if(WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display system changes toggle'))+"').ariaChecked", null))
			{
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/4. Historic/Display system changes toggle'))+"').click()", null)
			}

			assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/4. Historic/No data text')) == "No data. Select at least one option": "Text no data is not correct after unchecking both filter"
			KeywordUtil.logInfo("No data. Select at least one option")

		}

	}
	@Keyword
	def exitHistoryMode()
	{
		/**
		 * Exit history mode by clicking in arrow and check if we're redirected to the correct tab
		 */
		WebUI.delay(1)
		WebUI.click(findTestObject('Object Repository/2. Schedule List/4. Historic/Arrow'))

		String tabSelector="Object Repository/2. Schedule List/"+GlobalVariable.tabChosen+" Tab button"
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Schedule List/Rule Cells'), 10): "Nothing is displayed in the current tab"
		assert WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject(tabSelector))+"').ariaCurrent", null) == "page": "Wrong tab is opened"

		KeywordUtil.logInfo(GlobalVariable.tabChosen+" tab is open")
	}
	def getSetupInARule(String ruleObject)
	{
		/**
		 * get the setup to compare with history
		 * 
		 * @return setupRule
		 * 		Setup tab of a rule
		 */


		String ruleName = ""
		String budget = ""
		String budgetType = ""
		String advertiserCurrency = ""
		String budgetLimits = ""
		ArrayList<String> PatnerbudgetLimits = new ArrayList<String>()
		String threshold = ""
		String[] Date = ["", ""]
		String endDate = ""
		String status = ""
		String warning = ""
		String email = ""
		String otherDelivery = ""
		String otherDeliveryText = ""
		ArrayList<String> setupRule = new ArrayList<String>()
		ArrayList<String> partnerObject = new ArrayList<String>()


		int i=0

		//Get name
		ruleName = WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/Names'))
		setupRule.add(ruleName)

		//Get threshold
		threshold = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/1. Table/Thresholds'))+"').textContent.trim()", null)
		setupRule.add(threshold)

		if (ruleObject == "Partner")
		{
			//Get other delivery
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('2. Schedule List/1. Table/Partner Budget button'))+"').click()", null)
			otherDeliveryText = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/1. Table/Partner other delivery text'))+"').textContent.trim()", null)
			if (otherDeliveryText=="Delivering IOs not linked to a condition will not be paused")
			{
				otherDelivery = "-"
			}
			else if(otherDeliveryText == "Delivering IOs not linked to a condition will be paused")
			{
				otherDelivery = "true"
			}

			setupRule.add(otherDelivery)

			//Get Partner budget + budget type + advertiser currency
			String nbAdvertisers = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('2. Schedule List/1. Table/Partner Conditions list'))+"').length", null)
			for (int j = 1; j<nbAdvertisers.toInteger() ; j++)
			{
				PatnerbudgetLimits.add(WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('2. Schedule List/1. Table/Partner Conditions list'))+"').item("+j+").textContent.replace('Budget amount: ','').replace('Budget type: ',',').replace('Advertiser currency: ',',')", null))
			}

			for (int k=0; k<PatnerbudgetLimits.size;k++)
			{
				PatnerbudgetLimits[k] = PatnerbudgetLimits[k].split(",").reverse()
				Collections.swap(PatnerbudgetLimits[k], 0, 1)

				for(int m=0; m<PatnerbudgetLimits[k].size();m++)
				{
					if (PatnerbudgetLimits[k][0] == "Currency")
						PatnerbudgetLimits[k][2] = PatnerbudgetLimits[k][2].toFloat()
				}
				partnerObject[k] = PatnerbudgetLimits[k][0] + " " + PatnerbudgetLimits[k][1] + " " +PatnerbudgetLimits[k][2]
			}

			setupRule.add(partnerObject)

			WebUI.click(findTestObject('Object Repository/2. Schedule List/Spend close button'))
			WebUI.delay(1)

			if (GlobalVariable.tabChosen == "Passed")
			{
				//Get Date
				Date = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/1. Table/Analysis Periods'))+"').textContent.trim().split(' to ')", null)
				setupRule[7] = Date[0]
				setupRule[8] = Date[1]
			}
		} else
		{
			otherDelivery ="-"
			setupRule.add(otherDelivery)
		}
		if ((GlobalVariable.tabChosen == "Passed") && (ruleObject == "Partner"))
		{
			KeywordUtil.logInfo("setupRule: "+setupRule)
			return setupRule

		} else
		{
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/First Rule Menu button'))+"').click()", null)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Schedule List/Menu edit button'), 30) : "Edit is not present for the rule"
			WebUI.delay(3)
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/Menu edit button'))+"').click()", null)

			if (ruleObject != "Partner")
			{
				//Get Budget
				assert WebUI.waitForElementPresent(findTestObject('Object Repository/3. Schedule Editor/Budget input'), 5)
				budget = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Budget input'))+"').value", null)
			}
			if (ruleObject != "Partner")
			{
				//Get budget Type
				if (selenium.isElementPresent(getSelector(findTestObject('Object Repository/3. Schedule Editor/Currency checkbox'))))
				{
					budgetType = "Currency"
				} else
				{
					budgetType = "Impressions"
					if (!budget.matches("[+-]?\\d*(\\d+)?"))
						budget = "0"
				}

				//Get Advertiser currency
				advertiserCurrency = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Advertiser Currency Select text'))+"').textContent", null)

				budgetLimits = budgetType+" "+advertiserCurrency+" "+budget

				setupRule.add(budgetLimits)
			}
			//Get Status
			status = WebUI.getText(findTestObject('Object Repository/3. Schedule Editor/Status select button'))
			setupRule.add(status)


			//Get Warning status
			warning = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Warning input'))+"').ariaChecked", null)
			setupRule.add(warning)

			//Get Recipient
			email = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/3. Schedule Editor/Email name'))+"').textContent", null)
			setupRule.add(email)

			if (GlobalVariable.tabChosen != "Passed")
			{
				if (ruleObject == "Insertion Order(s)")
				{
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Cancel button'))
					assert WebUI.waitForElementPresent(findTestObject('Object Repository/3. Schedule Editor/Cancel popup'), 10)
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Cancel popup'))
				} else
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Cancel button'))

			} else
			{
				if (ruleObject == "Insertion Order(s)")
				{
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Cancel button'))
					assert WebUI.waitForElementPresent(findTestObject('Object Repository/3. Schedule Editor/Cancel popup'), 10)
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Cancel popup'))
				} else
					WebUI.click(findTestObject('Object Repository/3. Schedule Editor/Cancel duplicate'))
			}

			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Schedule List/Search Bar input'), 10)

			Thread.sleep(500)
			WebUI.sendKeys(findTestObject('Object Repository/2. Schedule List/Search Bar input'), GlobalVariable.ruleID)

			WebUI.delay(2)
			assert WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/IDs')) == GlobalVariable.ruleID: "Rule is not correct"

			//Get Start date
			if(status == "Active")
				Date = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/1. Table/Analysis Periods'))+"').textContent.trim().replace('fiber_manual_record ','').split(' to ')", null)
			else if (status == "Paused")
				Date = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Schedule List/1. Table/Analysis Periods'))+"').textContent.trim().replace('pause ','').split(' to ')", null)

			setupRule.add(Date[0])
			setupRule.add(Date[1])
		}

		KeywordUtil.logInfo("setupRule: "+setupRule)
		return setupRule
	}
}

