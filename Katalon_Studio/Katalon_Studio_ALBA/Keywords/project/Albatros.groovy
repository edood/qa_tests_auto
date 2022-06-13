package project

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.text.SimpleDateFormat

import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.Color

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.Common
import com.reusableComponents.HighlightElement

import internal.GlobalVariable

public class Albatros extends Common
{

	HighlightElement on = new HighlightElement()
	String AlbatrosEventColor = "#1a73f0"
	String AlbatrosVariableColor = "#00ccb0"
	public Albatros()
	{
	}

	@Keyword
	public String Start()
	{
		/**
		 * Log in to Albatros for specified environment
		 *
		 */
		String baseUrl = 'https://' + GlobalVariable.env
		WebUI.openBrowser(baseUrl)
		WebUI.maximizeWindow()
		login(GlobalVariable.googleID, GlobalVariable.googlePassword, GlobalVariable.checkHref)
		return baseUrl
	}

	@Keyword
	def login(String id, String pwd, boolean tip, boolean noPopUp = false)
	{
		/**
		 * Log in to Albatros
		 * @param id
		 * The id of the user
		 * @param pwd
		 * The encrypted password of the user
		 * @param gsi
		 * Define if login method should be Google Sign In or not
		 */

		if (tip == true)
		{
			KeywordUtil.logInfo('Checking login tip...')
			assert WebUI.waitForElementVisible(findTestObject('1. Login Google/tip'), 5) : "Tip message is not displayed after 5 second"
			String tiptext = WebUI.getText(findTestObject('1. Login Google/tip'))
			assert tiptext.trim() == "If you can’t access the page, please contact the authentifier team on the Slack Channel #prod_authentifier_support or send an email by clicking here." : "tip message has changed"
			assert WebUI.verifyElementAttributeValue(findTestObject('1. Login Google/Slack channel link'), 'href', 'slack://channel?team=T4A2YGWSF&id=CNTQWDJQP', 5) : "Channel Slack link is not correct"
			assert WebUI.verifyElementAttributeValue(findTestObject('1. Login Google/here link'), 'href', 'mailto:authentifier@jellyfish.com?subject=Add%20my%20email%20to%20Authentifier&body=Hello,%0D%0A%0D%0Aplease%20add%20me%20to%20the%20Authentifier%20in%20order%20to%20access%20%22Albatross%22.%0D%0A%0D%0AKind%20Regards,', 5) : "Email link is not correct"
		}
		driver = DriverFactory.getWebDriver()
		String parentWindow = driver.getWindowHandle()
		WebUI.click(findTestObject('1. Login Google/button'))
		Set<String> handles = driver.getWindowHandles()
		for(String windowHandle  : handles)
		{
			if(!windowHandle.equals(parentWindow) && !noPopUp)
			{
				driver.switchTo().window(windowHandle);
				googleSignIn(id, pwd)
				driver.switchTo().window(parentWindow);
				handles = driver.getWindowHandles()
				if (handles.size() == 1)
					return
				driver.switchTo().window(windowHandle);
				Googleauthentificator()
				driver.switchTo().window(parentWindow);
			}
		}
	}

	@Keyword
	def logout()
	{
		/**
		 * Log out of the Albatros
		 */
		WebUI.click(findTestObject('Object Repository/1. Login Google/User menu'))
		WebUI.waitForElementVisible(findTestObject('1. Login Google/Logout button'), 5)
		WebUI.click(findTestObject('1. Login Google/Logout button'))
	}

	@Keyword
	def CreateSelectClientTp(boolean Selectclient, String createclientname, String selectclientname, boolean SelectTP, String tpnameselect, Map tpSetup, Map tpTemplate)
	{
		/**
		 * This method creates or selects a client or tracking plan according to the parameters
		 * @param Selectclient
		 * The parameter to decide if the client is a creation or selection
		 * 
		 * @param createclientname
		 * The name of the client to be created
		 * 
		 * @param selectclientname
		 * The name of the client to be selected
		 * 
		 * @param SelectTP
		 * The parameter to decide if the tracking plan is a creation or selection
		 * 
		 * @tpnameselect
		 * The name of the tracking plan to be selected
		 * 
		 * @param tpSetup
		 * The map containing the information for the setup part of the creation
		 * 
		 * @param tpTemplate
		 * The map containing the information for the template part of the creation
		 */

		String[] name = new String[2]
		if (Selectclient == true)
		{
			selectClient(selectclientname)
			name[0] = selectclientname
		}
		else
		{
			goToClientManagement()
			name[0] = createClient(createclientname)
		}

		if (SelectTP == true)
		{
			selectTrackingPlan(tpnameselect, false)
			name[1] = tpnameselect
		}
		else
		{
			createTrackingPlan(name[0], tpSetup, tpTemplate)
			name[1] = tpSetup["Name"]
		}
		return (name)
	}

	@Keyword
	def selectClient(String clientToSelect)
	{
		/**
		 * Search for a Client, or check the no result message
		 *
		 * @param clientToSelect			Text to input in the Client search bar
		 */
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Homepage/Client search input'), 10) : "Client search input is not visible after 10 seconds"
		Thread.sleep(1000)
		WebUI.click(findTestObject('Object Repository/5. Homepage/Client search input'))
		Thread.sleep(250)
		WebUI.sendKeys(findTestObject('Object Repository/5. Homepage/Client search input'), clientToSelect)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Homepage/Overlay first option'), 30)
		WebUI.click(findTestObject('Object Repository/5. Homepage/Overlay first option'))
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/5. Homepage/Client search input'), "value", clientToSelect, 5) : "Client was not selected"
	}
	@Keyword
	def goToClientManagement()
	{
		/**
		 * Click on the Client management button
		 */
		assert WebUI.waitForElementVisible(findTestObject('5. Homepage/Client Management button'), 10) : 'Client Management button is not visible after 10 seconds'
		on.on(findTestObject('5. Homepage/Client Management button'))
		WebUI.click(findTestObject('5. Homepage/Client Management button'))
		assert WebUI.waitForElementVisible(findTestObject('6. Client Management/Client search bar'), 10) : 'Client search bar is not visible after 10 seconds'
	}

	@Keyword
	def deleteIndicatorPlan()
	{
		/**
		 * Delete all indicator of the plan
		 */
		Map variable = [Primary:'Variable', Module:'All']
		Map event = [Primary:'Event', Module:'All']
		deleteIndicator(event)
		deleteIndicator(variable)
	}

	@Keyword
	def deleteIndicator(Map indicatorsFilter)
	{
		/**
		 * Delete all indicator from the plan according to a specific filter
		 * @param indicatorsFilter
		 * The map containing the filter information to delete the indicator
		 */
		selenium = getSelenium()

		filterIndicators(indicatorsFilter['Primary'], indicatorsFilter['Module'], false)

		if (WebUI.getAttribute(findTestObject('8. Indicators List/primary indicator list'), 'childElementCount').toInteger() > 0)
		{
			WebUI.click(findTestObject('8. Indicators List/Button All'))
			WebUI.click(findTestObject('8. Indicators List/Button delete'))
			assert WebUI.waitForElementVisible(findTestObject('8. Indicators List/Button confirm delete'), 5) == true : "delete confirmation flow not displayed after 5 second"
			WebUI.click(findTestObject('8. Indicators List/Button confirm delete'))
			WebUI.delay(2)
			assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/8. Indicators List/Indicators'), 1) == true : "Indicator not deleted"
		}
	}

	@Keyword
	def getTrackingPlanIndicatorCount()
	{
		/**
		 * this method return the number of indicator (event & variable) in the tracking plan
		 * @return nbindicator
		 * the number of indicator in the tracking plan
		 */
		int nbindicator = WebUI.getAttribute(findTestObject('8. Indicators List/primary indicator list'), 'childElementCount').toInteger()
		WebUI.getText(findTestObject('8. Indicators List/Primary Filter')).contains('Event') ? filterIndicators('Variable', 'All', false) : filterIndicators('Event', 'All', false)
		nbindicator += WebUI.getAttribute(findTestObject('8. Indicators List/primary indicator list'), 'childElementCount').toInteger()
		return nbindicator
	}

	@Keyword
	def addIndicatorFromModule(Map indicator)
	{
		/**
		 * Add indicator from module from primary panel
		 * @param indicator
		 * The map containing the module and indicator to add
		 */
		Map IndicatorToCheckLater = [:]

		if (indicator.get('allModules') == true)
			WebUI.click(findTestObject('11. Add Indicator Panel/Button all'))
		else
		{
			ArrayList<String> categories = new ArrayList<String>()


			indicator.each
			{
				if (it.getKey() != "allModules" && it.getKey() != "indicatorFilters")
				{
					if (it.getValue() == "")
					{
						WebUI.setText(findTestObject('11. Add Indicator Panel/Search input'), " ")
						selectOnTheList(it.getKey(), it.getValue(), IndicatorToCheckLater, true)
					}
					else if (it.getValue().contains(','))
					{
						categories = it.getValue().split(',')

						for (int j = 0; j < categories.size();j++)
						{
							WebUI.setText(findTestObject('11. Add Indicator Panel/Search input'), categories[j])
							selectOnTheList(it.getKey(), categories[j], IndicatorToCheckLater, true)
						}
					}
					else
					{
						WebUI.setText(findTestObject('11. Add Indicator Panel/Search input'), it.getValue())
						IndicatorToCheckLater = selectOnTheList(it.getKey(), it.getValue(), IndicatorToCheckLater, true)
					}
				}
			}
		}
		WebUI.click(findTestObject('11. Add Indicator Panel/Button Add'))
		return IndicatorToCheckLater
	}

	def selectOnTheList(String getKey, String getValue, Map IndicatorToCheckLater, boolean bool)
	{
		/**
		 * Select indicator on the list
		 * 
		 * @param getKey
		 * 		Name of module
		 * @param getValue
		 * 		Name of indicator or category
		 * @param IndicatorToCheckLater
		 * 		map contains key and value or key and type(Event or variable) depending on bool value
		 * @param bool
		 * 		if true, we check module and key, if false we check key and type (event or variable)
		 * 
		 * @return IndicatorToCheckLater
		 * 
		 * 
		 */
		WebUI.delay(3)
		ArrayList<WebElement> allrow = new ArrayList<WebElement>()
		boolean found = false
		String typeOfIndicator = ''

		allrow = WebUI.findWebElements(findTestObject('Object Repository/11. Add Indicator Panel/row'), 1)
		found = false
		for (int i = 0; i < allrow.size() && found == false; i++)
		{
			if (allrow[i].getText().contains(getKey) == true)
			{
				if (getValue == "")
				{
					WebUI.click(changeSelector(findTestObject('11. Add Indicator Panel/select row'), "nth-child\\(1\\)", "nth-child(" + (i + 1) + ")"))
					found = true
					KeywordUtil.logInfo(getKey.toString() + ' : ' + getKey.toString() + ' has been selected')
					break
				}
				else
				{
					for (; i < allrow.size() && found == false; i++)
					{
						String[] formatted = allrow[i].getText().split('\n')
						if (formatted[0] == getValue)
						{
							WebUI.click(changeSelector(findTestObject('11. Add Indicator Panel/select row'), "nth-child\\(1\\)", "nth-child(" + (i + 1) + ")"))
							found = true
							KeywordUtil.logInfo(getKey.toString() + ' : ' + getValue.toString() + ' has been selected')
							if (WebUI.getAttribute(changeSelector(findTestObject('11. Add Indicator Panel/indicator level'), "nth-child\\(1\\)", "nth-child(" + (i + 1) + ")"), 'className').contains('third-level') == true)
								if(bool)
									IndicatorToCheckLater.put(getKey, getValue)
								else if(bool == false)
							{
								if (WebUI.waitForElementVisible(findTestObject('Object Repository/11. Add Indicator Panel/Type of indicator'), 5))
								{
									typeOfIndicator = WebUI.getText(findTestObject('Object Repository/11. Add Indicator Panel/Type of indicator'))
									IndicatorToCheckLater.put(getValue, typeOfIndicator)
								}
							}

							break
						}
						TestObject expand = changeSelector(findTestObject('11. Add Indicator Panel/row arrow state'), "nth-child\\(1\\)", "nth-child(" + (i + 1) + ")")
						TestObject iconexpand = changeSelector(findTestObject('11. Add Indicator Panel/icon expend'), "nth-child\\(1\\)", "nth-child(" + (i + 1) + ")")
						WebUI.delay(1)
						if (WebUI.waitForElementVisible(iconexpand, 2) == true && WebUI.getAttribute(expand, 'className').contains('pannel-close') == true)
						{
							WebUI.click(iconexpand)
							WebUI.delay(1)
							allrow = WebUI.findWebElements(findTestObject('Object Repository/11. Add Indicator Panel/row'), 1)
						}
					}
				}
			}
		}

		assert found == true : "Indicator " + getKey + " : " + getValue + " does not exist in the list of indicator"
		return IndicatorToCheckLater
	}

	@Keyword
	def addAssociatedIndicatorFromModule(Map indicator)
	{
		/**
		 * Add Associated indicator from module from primary panel
		 * @param indicator
		 * The map containing the module and indicator to add
		 */
		Map typeOfIndicator = [:]


		if (indicator.get('allModules') == true)
			WebUI.click(findTestObject('11. Add Indicator Panel/Button all'))
		else
		{
			ArrayList<String> categories = new ArrayList<String>()


			indicator.each
			{
				if (it.getKey() != "allModules" && it.getKey() != "indicatorFilters")
				{
					if (it.getValue() == "")
					{
						WebUI.setText(findTestObject('11. Add Indicator Panel/Search input'), " ")
						selectOnTheList(it.getKey(), it.getValue(), typeOfIndicator, false)
					}
					else if (it.getValue().contains(','))
					{
						categories = it.getValue().split(',')

						for (int j = 0; j < categories.size();j++)
						{
							WebUI.setText(findTestObject('11. Add Indicator Panel/Search input'), categories[j])
							selectOnTheList(it.getKey(), categories[j], typeOfIndicator, false)
						}
					}
					else
					{
						WebUI.setText(findTestObject('11. Add Indicator Panel/Search input'), it.getValue())
						typeOfIndicator = selectOnTheList(it.getKey(), it.getValue(), typeOfIndicator, false)
					}
				}
			}
		}
		WebUI.click(findTestObject('11. Add Indicator Panel/Button Add'))
		return typeOfIndicator
	}

	@Keyword
	def addIndicator(Map indicator)
	{
		/**
		 * This method add indicator from indicator or association panel depending on map in parameter
		 * @return map
		 * A map containing added indicator
		 */

		if (indicator == GlobalVariable.createfromModule)
			return addIndicatorFromModule(indicator)
		else if (indicator == GlobalVariable.createfromAssociated)
		{
			return addAssociatedIndicatorFromModule(indicator)
		}
		else if (indicator == GlobalVariable.createfromCustom)
		{
			filterIndicators(indicator['IndicatorFilters'], 'All')
			return addIndicatorFromCustomFromPrimaryPanel(indicator)
		}
	}

	@Keyword
	def addIndicatorFromCustomFromPrimaryPanel(Map ind)
	{
		/**
		 * this method add a custom indicator from the primary panel
		 * @param ind
		 * The map containing the indicicator information
		 */
		Map createdindicator = [:]
		String indicatortype = WebUI.getText(findTestObject('8. Indicators List/Primary Filter'))
		String tptype = getTrackingPlanType()
		WebUI.click(findTestObject('8. Indicators List/Button add primary'))
		assert WebUI.waitForElementVisible(findTestObject('11. Add Indicator Panel/Button custom'), 5) == true : "Panel Add indicators not displayed after 5sec"
		WebUI.click(findTestObject('11. Add Indicator Panel/Button custom'))
		assert WebUI.waitForElementVisible(findTestObject('12. Custom Panel/header panel'), 5) == true : "Panel create custom indicator is not displayed after 5sec"

		// Indicator details
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Key'), 5) : "\n\nIndicator Key input is not present after 5 seconds.\n\n"
		if(ind['Key'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Key'), ind['Key'])
		if(ind['Name'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Name'), ind['Name'])
		if(ind['Use Case'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Use Case'), ind['Use Case'])

		WebUI.click(findTestObject('12. Custom Panel/Add association'))
		Thread.sleep(250)
		String[] association = ind['Association'].toString().split(';')
		String finalasso = ""
		for (def str in association)
		{
			if (str.isInteger() == true)
			{

				ArrayList<WebElement> asso = WebUI.findWebElements(findTestObject('Object Repository/12. Custom Panel/Association name'), 1)
				int nbtoadd = str.toInteger()
				if (nbtoadd > asso.size() == true)
					KeywordUtil.markErrorAndStop('The number of available association is less than the number of association set in the profile')
				for (; nbtoadd > 0; nbtoadd--)
				{
					int nbfreeasso = asso.size()
					int randomIndex = 0
					randomIndex = Math.abs(new Random().nextInt() % nbfreeasso)
					asso[randomIndex].click()
					KeywordUtil.logInfo('association selected: ' + asso[randomIndex].getText().trim())
					finalasso += asso[randomIndex].getText().trim() + ";"
					Thread.sleep(250)
					asso = WebUI.findWebElements(findTestObject('Object Repository/12. Custom Panel/Association name'), 1)
				}
			}
			else
			{
				WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", " " + str + " "))
				finalasso += str + ";"
			}
		}
		WebUI.delay(1)
		KeywordUtil.logInfo('finalasso = ' + finalasso)
		createdindicator.put(ind['Key'], finalasso)
		WebUI.doubleClick(findTestObject('Object Repository/12. Custom Panel/header panel'))

		if(ind['Module'] != "")
		{
			WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Module'))
			String moduleSelector = " " + ind['Module'] + " "
			WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", moduleSelector))
			assertText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Module'), ind['Module'])
		}
		boolean isRequiredOptionChecked = selenium.isElementPresent(getSelector(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option checked')))
		if((ind['Required'] && !isRequiredOptionChecked) || (!ind['Required'] && isRequiredOptionChecked))
			WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option'))
		if(ind['Required'])
			assert WebUI.verifyElementPresent(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option checked'), 1) : "\n\nRequired option was not checked.\n\n"
		else
			assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option checked'), 1) : "\n\nRequired option is checked when it should not.\n\n"

		if(ind['Origin'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Origin'), ind['Origin'])


		// Second panel
		String selectedAction = ""
		String selectedLabel = ""
		if(indicatortype.contains("Variable") == true)
		{
			if(ind['Explanation'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Explanation'), ind['Explanation'])
			if(ind['Example'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Example'), ind['Example'])
			if(ind['Expected Format'] != "")
			{
				WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Expected Format'))
				String expectedFormatSelector = " " + ind['Expected Format'].toUpperCase() + " "
				WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", expectedFormatSelector))
				assertText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Expected Format'), ind['Expected Format'].toUpperCase())
			}
			assert WebUI.getAttribute(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Format Regex'), "value") != "" : "\n\nRegex field is empty, it should have been filled automatically\n\n"
		}
		else if (indicatortype.contains("Event") == true && tptype == 'Google Analytics 4 - Dual Tracking')
		{
			if (ind['Action'] != "")
			{
				WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Event Action'))
				Thread.sleep(250)
				WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", " " + ind['Action'] + " "))
			}
			if (ind['Label'] != "")
			{
				WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Event Label'))
				Thread.sleep(250)
				WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", " " + ind['Label'] + " "))
			}
			assert WebUI.getAttribute(findTestObject('12. Custom Panel/Category input'), 'value') == ind['Name'] : "Event cateogry is different than indicator name"
		}

		WebUI.click(findTestObject('12. Custom Panel/Add button'))
		return createdindicator
	}

	@Keyword
	def checkIndicatorList(Map ind)
	{
		/**
		 * this method verify in the indicator list if the indicator are present
		 * @param ind
		 * The map containing indicator to verify
		 */
		boolean found
		ArrayList<String> indicatorList
		ind.each
		{
			found = false
			KeywordUtil.logInfo('checking display: ' + it.getValue())
			filterIndicators('Event', it.getKey(), false)
			

			if (WebUI.getAttribute(findTestObject('8. Indicators List/primary indicator list'), 'childElementCount').toInteger() > 0)
			{
				searchAtIndicatorPanel(it.getValue())
				indicatorList = WebUI.findWebElements(findTestObject('Object Repository/8. Indicators List/Indicators'), 1).collect { it.getText() }
				for(int j = 0 ; j < indicatorList.size() ; j++)
				{
					if(it.getValue() == indicatorList.get(j))
					{
						found = true
						break
					}
				}
			}
			if (found == false)
			{
				filterIndicators('Variable', it.getKey(), false)
				

				if (WebUI.getAttribute(findTestObject('8. Indicators List/primary indicator list'), 'childElementCount').toInteger() > 0)
				{
					searchAtIndicatorPanel(it.getValue())
					indicatorList = WebUI.findWebElements(findTestObject('Object Repository/8. Indicators List/Indicators'), 1).collect { it.getText() }
					for(int j = 0 ; j < indicatorList.size() ; j++)
					{
						KeywordUtil.logInfo('comparaison variable: ind(i) = ' + it.getValue() + ' list(j) = ' + indicatorList.get(j))
						if(it.getValue() == indicatorList.get(j))
						{
							found = true
							break
						}
					}
				}
			}
			assert found == true : it.getValue() + " was not found in the tracking plan"
		}
	}
	@Keyword
	def checkIndicatorListforAssociation(String getKey)
	{
		/**
		 * this method verify in the indicator list if the indicator is present
		 * @param ind
		 * 		The map containing indicator to verify
		 */

		ArrayList<String> indicatorList
		boolean isFound = false
		searchAtIndicatorPanel(getKey)
		if (WebUI.getAttribute(findTestObject('8. Indicators List/primary indicator list'), 'childElementCount').toInteger() > 0)
		{
			indicatorList = WebUI.findWebElements(findTestObject('Object Repository/8. Indicators List/Indicators'), 1).collect { it.getText() }
			for(int j = 0 ; j < indicatorList.size() ; j++)
			{
				if(getKey == indicatorList.get(j))
				{
					return isFound = true
					
				}
			}
		}
		return isFound
	}
	@Keyword
	def checkAssociationListForNewAssociation(Map crossing)
	{
		/**
		 * We check if the indicator selected in the association panel (Event or Variable) is present in the first or second panel,
		 * depending on indicatorFilters value
		 *
		 * @param crossing
		 *
		 */
		WebUI.delay(5)
		Map indicatorFilters = GlobalVariable.createfromAssociated
		crossing.each
		{
			KeywordUtil.logInfo('checking indicator: ' + it.getKey() +" with type "+it.getValue())
			if(indicatorFilters['indicatorFilters'] == "Variable" && it.getValue() == "event")
			{
				searchAtAssociationPanel(it.getKey())
				checkAssociatedVariableKey(it.getKey())
			}
			else if (indicatorFilters['indicatorFilters'] == "Event" && it.getValue() == "variable")
			{
				searchAtIndicatorPanel(it.getKey())
				checkAssociatedVariableKey(it.getKey())

			}
			else if (indicatorFilters['indicatorFilters'] == "Variable" && it.getValue() == "variable")
			{
				assert checkIndicatorListforAssociation(it.getKey()): "Indicator "+it.getKey()+" is not found"

			}
			else if (indicatorFilters['indicatorFilters'] == "Event" && it.getValue() == "event")
			{
				assert checkIndicatorListforAssociation(it.getKey()): "Indicator "+it.getKey()+" is not found"
			}
			else
				KeywordUtil.markErrorAndStop('Type of indicator should be event or variable. Displayed: '+it.getValue())

		}
	}


	@Keyword
	def checkClientListSort(List columnsToCheck, List sortType, boolean isDefaultSort = false)
	{
		/**
		 * Check the Client list sort feature
		 * 
		 * @param columnsToCheck		List of the columns to check. Possible choices : "ID" / "Name"
		 * @param sortType				List of the sort to check. Possible choices : "Ascending" / "Descending"
		 * @param isDefaultSort			Set to true to check the default sort instead of using the sort type, or set to false to sort the columns
		 */

		ArrayList<WebElement> currentClientColumnElements = new ArrayList<WebElement>()
		ArrayList<String> currentClientColumn = new ArrayList<String>()
		ArrayList<String> displayedColumnList = new ArrayList<String>()

		ArrayList<String> clientSorted = new ArrayList<String>()

		for(String currentColumn in columnsToCheck)
		{
			for(String currentSortType in sortType)
			{
				currentClientColumn = new ArrayList<String>()
				displayedColumnList = new ArrayList<String>()
				currentClientColumnElements = WebUI.findWebElements(findTestObject('6. Client Management/1. Table/'+currentColumn+'s'), 1)
				currentClientColumnElements.each
				{
					currentClientColumn.add('"'+it.getText()+'"')
					displayedColumnList.add(it.getText())
				}
				String sortedCurrentClientColumn = String.join(", ", currentClientColumn)
				String displayedColumn1 = String.join(", ", displayedColumnList)
				displayedColumn1 = "["+displayedColumn1+"]"


				if(isDefaultSort == false)
				{
					if(currentSortType == "Descending")
					{
						sortedCurrentClientColumn = sortedCurrentClientColumn.replaceAll('\\[', '')
						sortedCurrentClientColumn = sortedCurrentClientColumn.replaceAll('\\]', '')
						sortedCurrentClientColumn = " "+sortedCurrentClientColumn
						List<String> myList = new ArrayList<String>(Arrays.asList(sortedCurrentClientColumn.split(","))).reverse()
						sortedCurrentClientColumn = String.join(",", myList)
						sortedCurrentClientColumn = sortedCurrentClientColumn.trim()
						sortedCurrentClientColumn = "["+sortedCurrentClientColumn+"]"
						sortedCurrentClientColumn = WebUI.executeJavaScript("return ["+sortedCurrentClientColumn+"].sort((entityA, entityB) => entityA.toLowerCase().localeCompare(entityB.toLowerCase()))", null)
						sortedCurrentClientColumn = sortedCurrentClientColumn.replaceAll('"', '')
						sortedCurrentClientColumn = sortedCurrentClientColumn.replaceAll('\\[', '')
						sortedCurrentClientColumn = sortedCurrentClientColumn.replaceAll('\\]', '')
						sortedCurrentClientColumn = "["+sortedCurrentClientColumn+"]"
					}
					WebUI.click(findTestObject('Object Repository/6. Client Management/Sort Client '+currentColumn))
					Thread.sleep(250)
					currentClientColumn = new ArrayList<String>()
					currentClientColumnElements = WebUI.findWebElements(findTestObject('6. Client Management/1. Table/'+currentColumn+'s'), 1)
					currentClientColumnElements.each
					{
						currentClientColumn.add(it.getText())
					}
					String displayedColumn = String.join(", ", currentClientColumn)
					if(currentSortType == "Ascending")
					{
						sortedCurrentClientColumn = WebUI.executeJavaScript("return ["+sortedCurrentClientColumn+"].sort((entityA, entityB) => entityA.toLowerCase().localeCompare(entityB.toLowerCase()))", null)
						sortedCurrentClientColumn = sortedCurrentClientColumn.replaceAll('"', '')
						
						assert "["+displayedColumn.toLowerCase()+"]" == sortedCurrentClientColumn.toLowerCase() : "\n\n" +currentSortType.low+" sort did not work.\nExpected "+currentColumn+"s : " + sortedCurrentClientColumn.toLowerCase() + "\nDisplayed Ids : " + "["+displayedColumn.toLowerCase()+"]" + "\n\n"
					}
				} else
				{
					sortedCurrentClientColumn = WebUI.executeJavaScript("return ["+sortedCurrentClientColumn+"].sort((entityA, entityB) => entityA.toLowerCase().localeCompare(entityB.toLowerCase()))", null)
					sortedCurrentClientColumn = sortedCurrentClientColumn.replaceAll('"', '')

					assert displayedColumn1.toLowerCase() == sortedCurrentClientColumn.toLowerCase() : "\n\nDefault sort is not correct.\nExpected "+currentColumn+"s : " + sortedCurrentClientColumn.toLowerCase() + "\nDisplayed Ids : " + displayedColumn1.toLowerCase() + "\n\n"
					break
				}
			}
		}
	}
	def clearClientSearch()
	{
		/**
		 * Clear the client management search bar
		 */
		WebUI.clearText(findTestObject('Object Repository/6. Client Management/Client search bar'))
		on.on(findTestObject('Object Repository/6. Client Management/Client search bar'))
		WebUI.sendKeys(findTestObject('Object Repository/6. Client Management/Client search bar'), Keys.chord(Keys.ENTER))
		Thread.sleep(250)
	}
	@Keyword
	def createClient(String newClientName, boolean cancel = false)
	{
		/**
		 * Creates a new Client
		 * 
		 * @param newClientName		Name of the new client to create (will be made unique)
		 * @param cancel			Set to true to cancel the creation, false otherwise
		 * 
		 * @return newUniqueClientName
		 */

		assert newClientName != '' : "newClientName should not be empty, please fill it."
		String newUniqueClientName = newClientName + "_" + new Date().getTime()
		assert WebUI.waitForElementVisible(findTestObject('6. Client Management/Create Client button'), 10) : 'Create Client button is not visible after 10 seconds'
		on.on(findTestObject('6. Client Management/Create Client button'))
		WebUI.click(findTestObject('6. Client Management/Create Client button'))

		assert WebUI.waitForElementVisible(findTestObject('6. Client Management/Client creation Create client button disabled'), 10) : 'Create client button may be enabled or is not present after 10 seconds'
		assert WebUI.waitForElementVisible(findTestObject('6. Client Management/Client creation Client name'), 1) : 'Client name input is not visible after 10 seconds'
		on.on(findTestObject('6. Client Management/Client creation Client name'))
		WebUI.setText(findTestObject('6. Client Management/Client creation Client name'), newUniqueClientName)

		if(cancel)
		{
			assert WebUI.waitForElementVisible(findTestObject('6. Client Management/Client creation Create client button'), 1) : 'Create client button may be disabled or is not present after 10 seconds'
			WebUI.click(findTestObject('6. Client Management/Client creation Cancel button'))
			assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/6. Client Management/Changes saved message'), 1) : "Client was created despite cancelling the process"
		} else
		{
			assert WebUI.waitForElementVisible(findTestObject('6. Client Management/Client creation Create client button'), 1) : 'Create client button may be disabled or is not present after 10 seconds'
			WebUI.click(findTestObject('6. Client Management/Client creation Create client button'))
			assert WebUI.verifyElementPresent(findTestObject('Object Repository/6. Client Management/Changes saved message'), 5) : "New client was not created"
		}

		return newUniqueClientName
	}
	@Keyword
	def editClientName(String clientName, String editedClientName)
	{
		/**
		 * Edit a client's name
		 * 
		 * @param clientName			Name of the client to edit
		 * @param editedClientName		New name of the client (will be made unique)
		 * 
		 * @return uniqueEditedClientName
		 */

		String uniqueEditedClientName = editedClientName + "_" + new Date().getTime()

		clearClientSearch()
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/6. Client Management/Client search bar'), 10)
		WebUI.sendKeys(findTestObject('Object Repository/6. Client Management/Client search bar'), clientName)
		Thread.sleep(250)
		WebUI.click(findTestObject('Object Repository/6. Client Management/1. Table/Names'))
		assert WebUI.waitForElementVisible(findTestObject('6. Client Management/Client creation Client name'), 10) : 'Create Client button is not visible after 10 seconds'
		WebUI.setText(findTestObject('6. Client Management/Client creation Client name'), uniqueEditedClientName)

		WebUI.click(findTestObject('6. Client Management/Client creation Create client button'))
		assert WebUI.verifyElementPresent(findTestObject('Object Repository/6. Client Management/Changes saved message'), 1) : "New client was not updated"

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/6. Client Management/Client search bar'), 10)
		WebUI.sendKeys(findTestObject('Object Repository/6. Client Management/Client search bar'), uniqueEditedClientName)
		Thread.sleep(250)
		assert WebUI.getText(findTestObject('Object Repository/6. Client Management/1. Table/Names')) == uniqueEditedClientName : "New client name is not present in the list"
		clearClientSearch()

		return uniqueEditedClientName
	}
	@Keyword
	def searchClient(String searchText, boolean isNoResultCheck = false)
	{
		/**
		 * Search for a client, or check the no result message
		 * 
		 * @param searchText			Text to input in the client search bar
		 * @param isNoResultCheck		Set to true to check for the no result message, false to search for a client (already default)
		 */

		if (WebUI.waitForElementPresent(findTestObject('6. Client Management/Create Client button'), 5) == false)
			goToClientManagement()

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/6. Client Management/Client search bar'), 10)
		WebUI.sendKeys(findTestObject('Object Repository/6. Client Management/Client search bar'), searchText)
		Thread.sleep(250)
		if(isNoResultCheck == true)
		{
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/6. Client Management/No result found message'), 10) : "A result was found when it should not"
			assert WebUI.getText(findTestObject('Object Repository/6. Client Management/No result found message')) == "No result found" : "The message displayed is not 'No result found'"
		} else
		{
			assert WebUI.getText(findTestObject('Object Repository/6. Client Management/1. Table/Names')) == searchText : "Searched client name is not present in the list"
		}
		clearClientSearch()
	}

	@Keyword
	def selectTrackingPlan(String trackingPlanToSelect, boolean isEmpty = false)
	{
		/**
		 * Search for a Tracking Plan, or check the no result message
		 *
		 * @param trackingPlanToSelect			Text to input in the Tracking Plan search bar
		 * @param isEmpty						Set to true to check if the Tracking Plan is empty, false otherwise (default)
		 * 
		 * @return true	or false				Is tracking plan empty or not
		 */
		selenium = getSelenium()
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Homepage/Tracking Plan search input'), 10) : "Tracking Plan search input is not visible after 10 seconds, or is disabled (no client selected)"
		Thread.sleep(1000)
		WebUI.sendKeys(findTestObject('Object Repository/5. Homepage/Tracking Plan search input'), trackingPlanToSelect)
		Thread.sleep(250)
		WebUI.click(findTestObject('Object Repository/5. Homepage/Overlay first option'))
		Thread.sleep(1000)

		if(isEmpty == true)
		{
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/8. Indicators List/Export button'), 10) : "Export button is not visible after 10 seconds"
			assert selenium.isElementPresent(getSelector(findTestObject('8. Indicators List/No Rows To Show text'))) : "A result was found when it should not"
			return true
		}
		return false
	}
	@Keyword
	def createTrackingPlan(String newClientName, Map setup, Map tem, boolean cancelButton = false)
	{
		/**
		 * Create a tracking plan
		 * 
		 * @param newClientName
		 * 		Client name
		 * @param setup
		 * 		setup map contains name and main url
		 * @param tem
		 * 		template map contains some template or not
		 * @param cancelButton
		 * 		If true, we click on cancel when creating a tracking plan. If false, we create a tracking plan
		 * 
		 */

		if (WebUI.waitForElementPresent(findTestObject('Object Repository/7. Tracking plan/New tracking plan disable'), 5))
		{
			WebUI.setText(findTestObject('6. Client Management/Client select bar'), newClientName)
			WebUI.click(findTestObject('6. Client Management/Client select bar'))
			on.on(findTestObject('Object Repository/6. Client Management/New client autocomplete'))
			WebUI.click(findTestObject('6. Client Management/New client autocomplete'))

			on.on(findTestObject('Object Repository/7. Tracking plan/New tracking plan'))
			WebUI.click(findTestObject('Object Repository/7. Tracking plan/New tracking plan'))
		} else if (WebUI.waitForElementPresent(findTestObject('Object Repository/7. Tracking plan/New tracking plan'), 5))
		{
			on.on(findTestObject('Object Repository/7. Tracking plan/New tracking plan'))
			WebUI.click(findTestObject('Object Repository/7. Tracking plan/New tracking plan'))
		} else
		{
			KeywordUtil.markErrorAndStop("Tracking plan button is not visible")
		}

		on.on(findTestObject('6. Client Management/Client select bar'))
		String checkClientName = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('6. Client Management/Client select bar'))+"').value", null)

		assert checkClientName == newClientName : "Client name is not correct: "+checkClientName

		setUp(setup, tem, cancelButton)
	}

	@Keyword
	def setUp(Map setup, Map tem, boolean cancelButton = false)
	{
		/**
		 * Fill set up form
		 * 
		 * @param setupName
		 * 		Name of the setup
		 * @param mainUrl
		 * 		Main url of the setup
		 * @param cancelButton
		 * 		If true, we click on cancel when creating a tracking plan. If false, we create a tracking plan
		 */
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/7. Tracking plan/Set up/Set up tab'), 10): "Name field is not displayed"

		if (!cancelButton)
		{
			WebUI.delay(3)
			on.on(findTestObject('Object Repository/7. Tracking plan/Set up/Name'))
			WebUI.setText(findTestObject('Object Repository/7. Tracking plan/Set up/Name'), setup["Name"])
			WebUI.delay(1)
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/7. Tracking plan/Set up/Error message fields'), 5): "Tracking plan name is already taken"
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/7. Tracking plan/Set up/Main url'), 5): "Main URL is not displayed"
			on.on(findTestObject('Object Repository/7. Tracking plan/Set up/Main url'))
			WebUI.setText(findTestObject('Object Repository/7. Tracking plan/Set up/Main url'), setup["Main URL"])
			WebUI.click(findTestObject('Object Repository/7. Tracking plan/Set up/Analytics destination'))

			TestObject analyticsDestination = changeSelectorXpath(findTestObject('Object Repository/7. Tracking plan/Set up/Analytics destination xpath'), 'AD', " " + setup["Analytics destination"] + " ")
			assert WebUI.waitForElementVisible(analyticsDestination, 10)
			WebUI.click(analyticsDestination)

			WebUI.setText(findTestObject('Object Repository/7. Tracking plan/Set up/Analytics destination identifier input'), setup["Analytics destination identifier"])
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/7. Tracking plan/Set up/Error message fields'), 5): "URL is not valid"

			on.on(findTestObject('Object Repository/7. Tracking plan/Set up/Next button'))
			WebUI.click(findTestObject('Object Repository/7. Tracking plan/Set up/Next button'))
			WebUI.delay(2)
			template(tem)
		}
		else
		{
			on.on(findTestObject('Object Repository/7. Tracking plan/Set up/Cancel button'))
			WebUI.click(findTestObject('Object Repository/7. Tracking plan/Set up/Cancel button'))
			KeywordUtil.logInfo("Cancel button is clicked")

			String homepageMessage = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/5. Homepage/Welcome header'))+"').textContent", null)
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/5. Homepage/Welcome header'), 10): "Welcome text is not displayed"

			on.on(findTestObject('Object Repository/5. Homepage/Welcome header'))

			KeywordUtil.logInfo(homepageMessage)
		}
	}

	def template(Map template)
	{

		/**
		 * User can select some templates, all template or no template
		 * 
		 * @param template
		 * 		Map contains templates which user want to select
		 * 
		 * @return true	or false				
		 * 		Is tracking plan empty or not
		 */
		switch(template["selectType"])
		{
			case "Selection":
				selectTemplate(template)
				break
			case "":
				WebUI.delay(2)
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/2. Without source/tab'))+"').item(2).click()", null)
				KeywordUtil.logInfo("No template is selected")
				break
			case "All":
				KeywordUtil.logInfo("All template and industries are selected")
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('7. Tracking plan/Template/template checkbox'))+"').forEach(button=>button.click())", null)
				WebUI.delay(2)
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('7. Tracking plan/Template/Industries checkbox'))+"').forEach(button=>button.click())", null)
				break
			case "Plan":
				KeywordUtil.logInfo("From existing plan tab is selected")
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/2. Without source/tab'))+"').item(1).click()", null)

				String isSourcePlanDisabled

				for (int i=0; i<2; i++)
				{

					assert WebUI.waitForElementPresent(findTestObject("7. Tracking plan/Template/1. From existing plan/Source dropdown value"), 5)

					isSourcePlanDisabled = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/1. From existing plan/Source checkbox content'))+"').item(1).ariaDisabled", null)
					String createButtonValue = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/1. From existing plan/Create button disabled'))+"')", null)

					assert isSourcePlanDisabled == "true": "Select source plan dropdown is not disabled"
					if (createButtonValue == null)
					{
						KeywordUtil.markErrorAndStop("Create button should be disabled")
					}
					WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('7. Tracking plan/Template/1. From existing plan/Source dropdown value'))+"').item(0).click()", null)
					WebUI.click(changeSelectorXpath(findTestObject('7. Tracking plan/Template/1. From existing plan/Select source xpath'),"client", template["sourceClient"]))
					WebUI.delay(1)
					isSourcePlanDisabled = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/1. From existing plan/Source checkbox content'))+"').item(1).ariaDisabled", null)
					assert isSourcePlanDisabled == "false": "Select source plan dropdown is disabled"
					WebUI.delay(1)

					createButtonValue = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/1. From existing plan/Create button disabled'))+"')", null)

					if (createButtonValue != null)
					{
						KeywordUtil.markErrorAndStop("Create button should be enabled")
					}

					WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('7. Tracking plan/Template/1. From existing plan/Source dropdown value'))+"').item(1).click()", null)
					WebUI.click(changeSelectorXpath(findTestObject('7. Tracking plan/Template/1. From existing plan/Select source xpath'),"client", template["sourcePlan"]))

					if (i==0)
					{
						WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/2. Without source/tab'))+"').item(2).click()", null)
						WebUI.delay(1)
						WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/2. Without source/tab'))+"').item(1).click()", null)
					}
				}

				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/1. From existing plan/Previous button'))+"').click()", null)
				WebUI.delay(1)
				assert WebUI.waitForElementPresent(findTestObject('Object Repository/7. Tracking plan/Set up/Main url'), 5): "Set up tab is not displayed"
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Set up/Next button'))+"').click()", null)
				WebUI.delay(1)
				isSourcePlanDisabled = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/1. From existing plan/Source checkbox content'))+"').item(1).ariaDisabled", null)
				assert isSourcePlanDisabled == "false": "Select source plan dropdown is disabled"
				WebUI.delay(1)

				break
			default:
				KeywordUtil.markErrorAndStop("selectType is not correct")
		}

		assert WebUI.waitForElementPresent(findTestObject('Object Repository/7. Tracking plan/Template/Create button'), 1) : "Tracking plan create button is disabled"
		on.on(findTestObject('Object Repository/7. Tracking plan/Template/Create button'))
		WebUI.click(findTestObject('Object Repository/7. Tracking plan/Template/Create button'))

		assert WebUI.waitForElementPresent(findTestObject('5. Homepage/Progress bar'), 240)
		assert WebUI.waitForElementNotPresent(findTestObject('5. Homepage/Progress bar'), 240): "Progress bar is still displayed"

		assert WebUI.waitForElementPresent(findTestObject('5. Homepage/Snackbar message'), 30) : "The tracking plan creation confirmation message is not displayed"
		
		WebUI.delay(3)
		if(template["selectType"]=="")
		{
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/8. Indicators List/Indicator cells'), 10) : "Indicators should not displayed for empty tracking plan"
			KeywordUtil.logInfo("Indicators are not displayed")

			return true
		}
		else if (template["selectType"]=="All" || template["selectType"]=="Selection")
		{
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/5. Homepage/Cells displayed'), 10) : "Indicator list should not be empty"
			KeywordUtil.logInfo("Indicators are displayed")

			return false
		}
	}

	def selectTemplate(Map template)
	{
		/**
		 * Select some template depending on the profile
		 * 
		 * @param template
		 * 		Map contains templates which user want to select
		 * 
		 */


		Iterator it = template.entrySet().iterator()
		ArrayList<String> temp = new ArrayList<String>()
		while (it.hasNext())
		{
			Map.Entry pair = (Map.Entry)it.next();
			if (pair.getKey() != "selectType" && pair.getKey() != "sourceClient" && pair.getKey() != "sourcePlan")
			{
				temp.add(pair.getKey())
			}
		}

		for (int i = 0; i < temp.size(); i++)
		{
			try
			{
				switch(temp[i])
				{

					case "Content Tracking":
						WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('7. Tracking plan/Template/template checkbox'))+"').item(0).click()", null)
						WebUI.delay(1)
						KeywordUtil.logInfo(temp[i]+" is selected")
						WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/Template lists'))+"').item(0).scroll(0,1000)", null)
						for(String industrie in template[temp[i]].split(','))
						{
							if (industrie == "")
							{
								KeywordUtil.logInfo("Default industrie is selected for "+temp[i])
							}
							else
							{
								WebUI.click(changeSelectorXpath(findTestObject('Object Repository/7. Tracking plan/Template/Goal content tracking'),"goals", industrie))
							}
						}
						break
					case "Ecommerce Tracking":
						WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('7. Tracking plan/Template/template checkbox'))+"').item(1).click()", null)
						WebUI.delay(1)
						KeywordUtil.logInfo(temp[i]+" is selected")
						WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/7. Tracking plan/Template/Template lists'))+"').item(1).scroll(0,1000)", null)
						for(String industrie in template[temp[i]].split(','))
						{
							if (industrie == "")
							{
								KeywordUtil.logInfo("Default industrie is selected for "+temp[i])
							}
							else
							{
								WebUI.click(changeSelectorXpath(findTestObject('Object Repository/7. Tracking plan/Template/Goal ecommerce tracking'),"goals", industrie))
							}
						}
						break
					case "Form Tracking":
						WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('7. Tracking plan/Template/template checkbox'))+"').item(2).click()", null)
						WebUI.delay(1)
						KeywordUtil.logInfo(temp[i]+" is selected")
						for(String industrie in template[temp[i]].split(','))
						{
							if (industrie == "")
							{
								KeywordUtil.logInfo("Default industrie is selected for "+temp[i])
							}
							else
							{
								WebUI.click(changeSelectorXpath(findTestObject('Object Repository/7. Tracking plan/Template/Goal form tracking'),"goals", industrie))
							}
						}
						break
					case "Platform Setup":
						WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('7. Tracking plan/Template/template checkbox'))+"').item(3).click()", null)
						WebUI.delay(1)
						KeywordUtil.logInfo(temp[i]+" is selected")
						for(String industrie in template[temp[i]].split(','))
						{
							if (industrie == "")
							{
								KeywordUtil.logInfo("Default industrie is selected for "+temp[i])
							}
							else
							{
								WebUI.click(changeSelectorXpath(findTestObject('Object Repository/7. Tracking plan/Template/Goal platform setup'),"goals", industrie))
							}
						}
						break
					case "User Data Tracking":
						WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('7. Tracking plan/Template/template checkbox'))+"').item(4).click()", null)
						WebUI.delay(1)
						KeywordUtil.logInfo(temp[i]+" is selected")
						for(String industrie in template[temp[i]].split(','))
						{
							if (industrie == "")
							{
								KeywordUtil.logInfo("Default industrie is selected for "+temp[i])
							}
							else
							{
								WebUI.click(changeSelectorXpath(findTestObject('Object Repository/7. Tracking plan/Template/Goal user data tracking'),"goals", industrie))
							}
						}
						break
					case "Video Tracking":
						WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('7. Tracking plan/Template/template checkbox'))+"').item(5).click()", null)
						WebUI.delay(1)
						KeywordUtil.logInfo(temp[i]+" is selected")
						for(String industrie in template[temp[i]].split(','))
						{
							if (industrie == "")
							{
								KeywordUtil.logInfo("Default industrie is selected for "+temp[i])
							}
							else
							{
								WebUI.click(changeSelectorXpath(findTestObject('Object Repository/7. Tracking plan/Template/Goal video tracking'),"goals", industrie))
							}
						}
						break
					default:
						KeywordUtil.markErrorAndStop("Template name is not correct or not present: "+temp[i])
				}
			}
			catch (StepFailedException e)
			{
				KeywordUtil.logInfo("" + e)
				KeywordUtil.markErrorAndStop("Cannot select one of these industries: "+template[temp[i]]+" for this template: "+temp[i]+". It doesn't exit for the template")
			}
		}
	}

	@Keyword
	def download(String clientName, String trackingPlanName)
	{
		/**
		 *  This method download the json file of the export gtm
		 *  @param clientName
		 *  The client to select
		 *  
		 *  @param trackingPlanName
		 *  The tracking plan to select
		 *  
		 *  @return file
		 *  The json file downloaded
		 */
		WebUI.click(findTestObject('Object Repository/8. Indicators List/Export button'))
		assert WebUI.waitForElementVisible(findTestObject('8. Indicators List/GTM option'), 30)
		WebUI.click(findTestObject('8. Indicators List/GTM option'))

		if (WebUI.waitForElementPresent(findTestObject('5. Homepage/Snackbar message'), 3) == true)
		{
			if (WebUI.getText(findTestObject('5. Homepage/Snackbar message')) == 'Please provide analytics destination identifier.')
			{
				assert WebUI.waitForElementNotPresent(findTestObject('5. Homepage/Snackbar message'), 10) == true : "Message is still displayed after 10 seconds"
				WebUI.click(findTestObject('5. Homepage/Detailbar/Identifier'))
				WebUI.setText(findTestObject('5. Homepage/Detailbar/Identifier'), "G-" + new SimpleDateFormat("MMddHHmmss").format(new Date()))
				WebUI.click(findTestObject('8. Indicators List/Primary indicator container header'))
				WebUI.delay(1)
				assert WebUI.waitForElementVisible(findTestObject('5. Homepage/Snackbar message'), 3) == true : "Change was not automatically saved"
				assert WebUI.waitForElementNotPresent(findTestObject('5. Homepage/Snackbar message'), 10) == true : "Message is still displayed after 10 seconds"
				WebUI.click(findTestObject('Object Repository/8. Indicators List/Export button'))
				assert WebUI.waitForElementVisible(findTestObject('8. Indicators List/GTM option'), 30)
				WebUI.click(findTestObject('8. Indicators List/GTM option'))
			}
		}
		String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date())
		assert WebUI.waitForElementNotPresent(findTestObject('5. Homepage/Progress bar'), 60) : "The export progress bar is still present after 60 seconds"

		String Jsonfilename = clientName + "-" + trackingPlanName + "-" + time + "-export.json"
		KeywordUtil.logInfo('json file name = ' + Jsonfilename)
		String sdfPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator
		File file = new File(sdfPath + Jsonfilename)
		Thread.sleep(500)
		int attempts = 10
		while(file.exists() == false && attempts > 0)
		{
			WebUI.delay(5)
			attempts--
		}
		assert file.exists() == true : "Json file not downloaded"

		return (file)
	}

	@Keyword
	String getTrackingPlanType()
	{
		if (WebUI.getAttribute(findTestObject('8. Indicators List/Kpi container'), 'childElementCount').toInteger() == 6)
		{
			return ('Google Analytics 4 - Dual Tracking')
		}
		return ('Google Analytics 4')
	}

	@Keyword
	String getTrackingPlanOptions(String client, String trackingplan)
	{
		/**
		 * this method get the option from a tracking plan
		 * @param client
		 * The client to select
		 * 
		 * @param trackingplan
		 * The tracking plan to select
		 * 
		 * @return
		 * A string containing the option of the selected tracking plan
		 */

		selectClient(client)
		selectTrackingPlan(trackingplan)
		return (WebUI.getText(findTestObject('8. Indicators List/Tracking plan option')))
	}

	@Keyword
	def checkDetailBar(Map TpSetup, String ImportedPlanOptions = "Click to select option")
	{
		/**
		 * this method check the detail bar
		 * @param TpSetup
		 * The map containing the creation information
		 * 
		 * @param ImportedPlanOptions
		 * The option from the source plan when the creation is from an existing plan
		 */

		assert WebUI.getAttribute(findTestObject('8. Indicators List/Tracking plan name'), 'value') == TpSetup["Name"] : "Name displayed " + WebUI.getAttribute(findTestObject('8. Indicators List/Tracking plan name'), 'value') + " is different than name expected " + TpSetup["Name"]
		assert WebUI.getAttribute(findTestObject('8. Indicators List/Tracking plan url'), 'value') == TpSetup["Main URL"] : "Url displayed " + WebUI.getAttribute(findTestObject('8. Indicators List/Tracking plan url'), 'value') + " is different than url expected " + TpSetup["Main URL"]
		assert WebUI.getAttribute(findTestObject('8. Indicators List/Tracking plan identifier'), 'value') == TpSetup["Analytics destination identifier"] : "Identifier displayed " + WebUI.getAttribute(findTestObject('8. Indicators List/Tracking plan identifier'), 'value') + " is different than identifier expected " + TpSetup["Analytics destination identifier"]
		assert WebUI.getText(findTestObject('8. Indicators List/Tracking plan synchronisation date')) == new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : "Synchronisation date displayed " + WebUI.getText(findTestObject('8. Indicators List/Tracking plan synchronisation date')) + " is different than synchronisation date expected " + new SimpleDateFormat("dd/MM/yyyy").format(new Date())


		if (ImportedPlanOptions.contains("Click to select option") == false)
			assert WebUI.getText(findTestObject('8. Indicators List/Tracking plan option')) == ImportedPlanOptions : "Source tracking plan option " + ImportedPlanOptions + " are different from current tracking plan option " + WebUI.getText(findTestObject('8. Indicators List/Tracking plan option'))
		else
			assert WebUI.getText(findTestObject('8. Indicators List/Tracking plan option')).contains("Click to select option") == true : "Source tracking plan option " + ImportedPlanOptions + " are different from current tracking plan option " + WebUI.getText(findTestObject('8. Indicators List/Tracking plan option'))

		assert WebUI.verifyElementClickable(findTestObject('8. Indicators List/Tracking plan spreadsheet')) == true : "Spreadsheet is not clickable"
	}

	@Keyword
	def checkGTM(File json)
	{
		/**
		 * Verify the import of the json file on gtm
		 * @param json
		 * The albatros json file downloaded
		 */

		WebUI.navigateToUrl('https://tagmanager.google.com/')
		assert WebUI.waitForElementVisible(findTestObject('10. GTM/First container'), 30) == true : "GTM container not visible after 30 sec"
		WebUI.click(findTestObject('10. GTM/First container'))

		if (WebUI.waitForElementVisible(findTestObject('10. GTM/workspace selection'), 5) == true)
		{
			WebUI.click(findTestObject('10. GTM/Workspace info'))
			assert WebUI.waitForElementVisible(findTestObject('10. GTM/more option button'), 10) == true : "more option button not visible after 10 sec"
			WebUI.click(findTestObject('10. GTM/more option button'))
			assert WebUI.waitForElementVisible(findTestObject('10. GTM/option delete'), 10) == true : "option delete button not visible after 10 sec"
			WebUI.click(findTestObject('10. GTM/option delete'))
			if (WebUI.waitForElementVisible(findTestObject('10. GTM/confirm delete'), 10) == true)
				WebUI.click(findTestObject('10. GTM/confirm delete'))
		}
		WebUI.delay(2)
		assert WebUI.waitForElementVisible(findTestObject('10. GTM/Admin tab'), 30) == true: "admin tab is not visible after 30 sec"
		WebUI.click(findTestObject('10. GTM/Admin tab'))
		assert WebUI.waitForElementVisible(findTestObject('10. GTM/Import container'), 30) == true : "import container button not visible after 30 sec"
		WebUI.click(findTestObject('10. GTM/Import container'))
		assert WebUI.waitForElementVisible(findTestObject('10. GTM/new workspace'), 30) == true : "new workspace button not visible after 30 sec"
		WebUI.click(findTestObject('10. GTM/new workspace'))
		assert WebUI.waitForElementVisible(findTestObject('10. GTM/new workspace title'), 30) == true : "workspace title not visible after 30 sec"
		WebUI.setText(findTestObject('10. GTM/new workspace title'), "Autoworkspace-" + new SimpleDateFormat("hhmmss-dd-MM").format(new Date()))
		WebUI.click(findTestObject('10. GTM/Save button'))
		WebUI.delay(1)
		WebUI.uploadFile(findTestObject('10. GTM/upload file button'), json.getAbsolutePath())
		assert WebUI.waitForElementVisible(findTestObject('10. GTM/Confirm import button'), 10) == true : WebUI.getText(findTestObject('10. GTM/Error gtm import'))
		int numberOfTags = Integer.valueOf(WebUI.getText(findTestObject('Object Repository/10. GTM/Import Summary New Tag number')))
		WebUI.click(findTestObject('10. GTM/Confirm import button'))

		if(GlobalVariable.consentMode)
		{
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. GTM/Search input'), 30)
			WebUI.setText(findTestObject('Object Repository/10. GTM/Search input'), "analytics_storage")
			WebUI.delay(2)
			WebUI.sendKeys(findTestObject('Object Repository/10. GTM/Search input'), Keys.chord(Keys.ENTER))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/10. GTM/Search Popup title'), 30)
			WebUI.delay(5)
			assert WebUI.getText(findTestObject('Object Repository/10. GTM/Search Popup title')).contains(numberOfTags + " results") : "\n\nConsent Mode value 'analytics_storage' was not found in all " + numberOfTags + " tags, only the following results were found : "+ WebUI.getText(findTestObject('Object Repository/10. GTM/Search Popup title')) +"\n\n"
		}
	}

	@Keyword
	def exportGTM(String clientName, String trackingPlanName, boolean checkgtm = true)
	{
		/**
		 * Export indicator in the json file
		 * @param clientName
		 * The name of the client
		 * 
		 * @paran trackingPlanName
		 * The name of the tracking plan
		 * 
		 * @return jsonfile
		 * The file download from the albatros
		 */

		File jsonfile = download(clientName, trackingPlanName)
		if (checkgtm == true)
			checkGTM(jsonfile)
		return (jsonfile)
	}

	@Keyword
	def selectTrackingPlanOption(String expectedLabel)
	{
		/**
		 *  Select/Unselect the Consent Mode option on the tracking plan header
		 *  
		 *  @param expectedLabel	Expected label after the selection/unselection
		 */

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/8. Indicators List/Options button'), 30)
		if(WebUI.getText(findTestObject('Object Repository/8. Indicators List/Options button')).contains(expectedLabel))
			return // Option is already selected
		WebUI.click(findTestObject('Object Repository/8. Indicators List/Options button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/8. Indicators List/Options menu First option'), 30)
		WebUI.click(findTestObject('Object Repository/8. Indicators List/Options menu First option'))
		WebUI.click(findTestObject('Object Repository/8. Indicators List/Overlay backdrop'))
		assert WebUI.getText(findTestObject('Object Repository/8. Indicators List/Options button')).contains(expectedLabel)
	}

	@Keyword
	def filterIndicators(String primary, String module, boolean withassert=true)
	{
		/**
		 * Select the option for the Primary field
		 * 
		 * @param primary		Primary field option : "Event" or "Variable"
		 * @param module		Module field option
		 */

		waitVisibleAndClick(findTestObject('Object Repository/8. Indicators List/Primary select button'))
		waitVisibleAndClick(changeSelector(findTestObject('Object Repository/8. Indicators List/Primary select option'), 'primary', primary.toLowerCase()))
		KeywordUtil.logInfo("Primary is: "+primary+". Module is "+module)
		if(module != "")
		{
			if (withassert == true)
				assert WebUI.waitForElementVisible(findTestObject('Object Repository/8. Indicators List/Indicators'), 10) : "\n\nThere are no indicators to filter in this tracking plan. Please select an other one.\n\n"
			WebUI.delay(1)
			waitVisibleAndClick(findTestObject('Object Repository/8. Indicators List/Module select button'))
			String moduleSelector = " " + module + " "
			if(module == "All") moduleSelector = module
			waitVisibleAndClick(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), 'select', moduleSelector))
		}
		WebUI.delay(2)
	}

	@Keyword
	def checkRandomIndicatorPopupAndEdit()
	{
		/**
		 * Select a random indicator and check the navigation features of its popup, then edits its values
		 * 
		 */

		boolean isDualTracking
		Map editIndicatorMap = GlobalVariable.editIndicator


		waitPresent(findTestObject('Object Repository/8. Indicators List/Indicators'), 2000, true)
		ArrayList<String> indicatorsElements = WebUI.findWebElements(findTestObject('Object Repository/8. Indicators List/Indicators'), 1)
		int indicatorsNumber = indicatorsElements.size()
		if(indicatorsNumber == 0) KeywordUtil.markFailedAndStop("\n\nTracking plan selected is empty, cannot run the next steps of this test case.\n\n")
		int randomIndex = 0
		randomIndex = Math.abs(new Random().nextInt() % indicatorsNumber)

		indicatorsElements.get(randomIndex).click()
		if(randomIndex > 0)
		{
			String previousIndicatorName = indicatorsElements.get(randomIndex-1).getText()
			waitVisibleAndClick(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Previous Indicator button'), 3)
			assert WebUI.getAttribute(findTestObject('8. Indicators List/Indicator details popup/Indicator Key'), "value") == previousIndicatorName : "\n\nPrevious indicator name is incorrect. Expected : " + previousIndicatorName + " ; Displayed : " + WebUI.getAttribute(findTestObject('8. Indicators List/Indicator details popup/Indicator Key'), "value") + "\n\n"
			WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Next Indicator button'))
		}
		if(randomIndex < indicatorsNumber - 1)
		{
			String nextIndicatorName = indicatorsElements.get(randomIndex+1).getText()
			WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Next Indicator button'))
			assert WebUI.getAttribute(findTestObject('8. Indicators List/Indicator details popup/Indicator Key'), "value") == nextIndicatorName  : "\n\nNext indicator name is incorrect. Expected : " + nextIndicatorName + " ; Displayed : " + WebUI.getAttribute(findTestObject('8. Indicators List/Indicator details popup/Indicator Key'), "value") + "\n\n"
		}
		WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Close button'))
		assert WebUI.waitForElementNotVisible(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Close button'), 5) : "\n\nIndicator popup close button is still visible after 5 seconds.\n\n"
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Previous Indicator button'), 1) : "\n\nIndicator popup close button is still present after 3 seconds.\n\n"
		Thread.sleep(2000)

		randomIndex = Math.abs(new Random().nextInt() % indicatorsNumber)
		WebUI.executeJavaScript("document.querySelector('" + getOnlySelector(findTestObject('Object Repository/8. Indicators List/Indicator Table Scroll Bar')) + "').scrollTo(0,0)", null)
		indicatorsElements.get(randomIndex).click()
		WebUI.waitForElementVisible(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Previous Indicator button'), 3)

		isDualTracking= checkIsDualtracking("IndicatorPanel")
		editIndicator(editIndicatorMap, isDualTracking)

		WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Save Changes button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/6. Client Management/Changes saved message'), 30) : "\n\nIndicator edition message is not displayed after 30 seconds.\n\n"


	}

	@Keyword
	def checkIsDualtracking(String panel, String indicatorsFilter = GlobalVariable.indicatorsFilter['Primary'])
	{
		/**
		 * 
		 * Check if TP is dual tracking
		 * 
		 * @param panel
		 * 		Check into indicator panel or association panel
		 * @param indicatorsFilter
		 * 		Event or Variable
		 * 
		 * @return isDualTracking
		 * 		if yes, TP is dual tracking, false otherwise
		 * 
		 */

		boolean isDualTracking = selenium.isElementPresent(getSelector(findTestObject('Object Repository/8. Indicators List/6th KPI')))

		if((indicatorsFilter == "Variable" && panel == "IndicatorPanel") || (indicatorsFilter == "Event" && panel == "AssociationPanel"))
		{
			assert WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/First section title')) == "Indicator details" : "\n\nThe first section of the indicator popup is incorrect. Expected : 'Indicator details' ; Displayed : '"+ WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/First section title'))+"'.\n\n"
			assert WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Second section title')) == "Variable implementation instructions" : "\n\nThe second section of the indicator popup is incorrect. Expected : 'Variable implementation instructions' ; Displayed : '"+ WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Second section title'))+"'.\n\n"
			return false
		}
		else if((indicatorsFilter == "Event" && panel == "IndicatorPanel") || (indicatorsFilter == "Variable" && panel == "AssociationPanel"))
		{
			assert WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/First section title')) == "Indicator details" : "\n\nThe first section of the indicator popup is incorrect. Expected : 'Indicator details' ; Displayed : '"+ WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/First section title'))+"'.\n\n"

			if((!GlobalVariable.isSelectTP && GlobalVariable.tpSetup['Analytics destination'] == "Google Analytics 4 - Dual Tracking") || (GlobalVariable.isSelectTP && isDualTracking))
			{
				assert WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Second section title')) == "Universal Analytics mapping" : "\n\nThe second section of the indicator popup is incorrect. Expected : 'Universal Analytics mapping' ; Displayed : '"+ WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Second section title'))+"'.\n\n"
				KeywordUtil.logInfo("TP is dual tracking. Universal Analytics mapping section is displayed for edit event")
				return true
			} else {
				assert !selenium.isElementPresent(getSelector(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Second section title'))) : "\n\nThe second section of the indicator popup is incorrect. Expected no section ; Displayed section : '"+ WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Second section title'))+"'.\n\n"
				KeywordUtil.logInfo("TP is not dual tracking. No section displayed for edit event")
				return false
			}
		}
	}


	@Keyword
	def editIndicator(Map editIndicator, boolean isDualTracking)
	{
		/**
		 * Edit the indicator details popup
		 * 
		 * @param editIndicator			Map containing info on what to edit in the indicator
		 * @param isDualTracking		true if the tracking plan analytics destination is Dual tracking, else otherwise 
		 * 
		 * @return	[selectedAction, selectedLabel]
		 */

		// Indicator details
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Key'), 5) : "\n\nIndicator Key input is not present after 5 seconds.\n\n"
		if(editIndicator['Key'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Key'), editIndicator['Key'])
		if(editIndicator['Name'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Name'), editIndicator['Name'])
		if(editIndicator['Use Case'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Use Case'), editIndicator['Use Case'])

		if(editIndicator['Module'] != "")
		{
			WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Module'))
			String moduleSelector = " " + editIndicator['Module'] + " "
			WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", moduleSelector))
			assertText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Module'), editIndicator['Module'])
		}
		boolean isRequiredOptionChecked = selenium.isElementPresent(getSelector(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option checked')))
		if((editIndicator['Required'] && !isRequiredOptionChecked) || (!editIndicator['Required'] && isRequiredOptionChecked))
			WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option'))
		if(editIndicator['Required'])
			assert WebUI.verifyElementPresent(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option checked'), 1) : "\n\nRequired option was not checked.\n\n"
		else
			assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option checked'), 1) : "\n\nRequired option is checked when it should not.\n\n"

		if(editIndicator['Origin'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Origin'), editIndicator['Origin'])

		// Second panel
		Map indicatorsFilter = GlobalVariable.indicatorsFilter
		String selectedAction = ""
		String selectedLabel = ""
		if(indicatorsFilter['Primary'] == "Variable")
		{
			if(editIndicator['Explanation'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Explanation'), editIndicator['Explanation'])
			if(editIndicator['Example'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Example'), editIndicator['Example'])
			if(editIndicator['Expected Format'] != "")
			{
				WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Expected Format'))
				String expectedFormatSelector = " " + editIndicator['Expected Format'].toUpperCase() + " "
				WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", expectedFormatSelector))
				assertText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Expected Format'), editIndicator['Expected Format'].toUpperCase())
			}
			assert WebUI.getAttribute(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Format Regex'), "value") != "" : "\n\nRegex field is empty, it should have been filled automatically\n\n"
		} else if(indicatorsFilter['Primary'] == "Event" && isDualTracking)
		{
			ArrayList<String> addAssociations = WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Add Associations text')).split(", ")
			int numberOfAssociation = addAssociations.size()
			if(numberOfAssociation > 0)
			{
				WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Event Action'))
				int randomIndex = Math.abs(new Random().nextInt() % numberOfAssociation)
				String actionSelect = " " + addAssociations.get(randomIndex) + " "
				selectedAction = addAssociations.get(randomIndex)
				WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", actionSelect))

				WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Event Label'))
				randomIndex = Math.abs(new Random().nextInt() % numberOfAssociation)
				String labelSelect = " " + addAssociations.get(randomIndex) + " "
				selectedLabel = addAssociations.get(randomIndex)
				WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", labelSelect))
			}
		}

		return [
			selectedAction,
			selectedLabel
		]
	}

	@Keyword
	def editAssociated(Map editIndicator, boolean isDualTracking)
	{
		/**
		 * Edit the indicator details popup
		 *
		 * @param editIndicator			Map containing info on what to edit in the indicator
		 * @param isDualTracking		true if the tracking plan analytics destination is Dual tracking, else otherwise
		 *
		 * @return	[selectedAction, selectedLabel]
		 */

		// Indicator details
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Key'), 5) : "\n\nIndicator Key input is not present after 5 seconds.\n\n"
		if(editIndicator['Key'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Key'), editIndicator['Key'])
		if(editIndicator['Name'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Name'), editIndicator['Name'])
		if(editIndicator['Use Case'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Use Case'), editIndicator['Use Case'])

		if(editIndicator['Module'] != "")
		{
			WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Module'))
			String moduleSelector = " " + editIndicator['Module'] + " "
			WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", moduleSelector))
			assertText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Module'), editIndicator['Module'])
		}
		boolean isRequiredOptionChecked = selenium.isElementPresent(getSelector(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option checked')))
		if((editIndicator['Required'] && !isRequiredOptionChecked) || (!editIndicator['Required'] && isRequiredOptionChecked))
			WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option'))
		if(editIndicator['Required'])
			assert WebUI.verifyElementPresent(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option checked'), 1) : "\n\nRequired option was not checked.\n\n"
		else
			assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Required option checked'), 1) : "\n\nRequired option is checked when it should not.\n\n"

		if(editIndicator['Origin'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Origin'), editIndicator['Origin'])

		// Second panel
		Map indicatorsFilter = GlobalVariable.indicatorsFilter
		String selectedAction = ""
		String selectedLabel = ""
		if(indicatorsFilter['Primary'] == "Event")
		{
			if(editIndicator['Explanation'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Explanation'), editIndicator['Explanation'])
			if(editIndicator['Example'] != "") setText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Example'), editIndicator['Example'])
			if(editIndicator['Expected Format'] != "")
			{
				WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Expected Format'))
				String expectedFormatSelector = " " + editIndicator['Expected Format'].toUpperCase() + " "
				WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", expectedFormatSelector))
				assertText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Expected Format'), editIndicator['Expected Format'].toUpperCase())
			}
			assert WebUI.getAttribute(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Format Regex'), "value") != "" : "\n\nRegex field is empty, it should have been filled automatically\n\n"
		} else if(indicatorsFilter['Primary'] == "Variable" && isDualTracking)
		{
			ArrayList<String> addAssociations = WebUI.getText(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Add Associations text')).split(", ")
			int numberOfAssociation = addAssociations.size()
			if(numberOfAssociation > 0)
			{
				WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Event Action'))
				int randomIndex = Math.abs(new Random().nextInt() % numberOfAssociation)
				String actionSelect = " " + addAssociations.get(randomIndex) + " "
				selectedAction = addAssociations.get(randomIndex)
				WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", actionSelect))

				WebUI.click(findTestObject('Object Repository/8. Indicators List/Indicator details popup/Indicator Event Label'))
				randomIndex = Math.abs(new Random().nextInt() % numberOfAssociation)
				String labelSelect = " " + addAssociations.get(randomIndex) + " "
				selectedLabel = addAssociations.get(randomIndex)
				WebUI.click(changeSelectorXpath(findTestObject('8. Indicators List/Overlay select option'), "select", labelSelect))
			}
		}

		return [
			selectedAction,
			selectedLabel
		]
	}

	@Keyword
	def checkIndicatorSearchResult(String column, String expectedValue)
	{
		/**
		 * Check that first indicator listed has the expected value in the selected column 
		 *
		 * @param column			Selected column name : "Key", "Name", "Association", "Use Case", "Explanation"
		 * @param expectedValue		Expected text value in the selected column
		 */

		int columnIndex = 0
		switch(column) {
			case "Key":
				columnIndex = 2
				break
			case "Name":
				columnIndex = 4
				break
			case "Association":
				columnIndex = 5
				break
			case "Use Case":
				columnIndex = 6
				break
			case "Explanation":
				columnIndex = 7 // Displayed as "Example" in UI for now
				break
		}
		String columnValue = WebUI.getText(changeSelector(findTestObject('Object Repository/8. Indicators List/Indicator Table first column element'), "nth-child\\(2\\)", "nth-child(" + columnIndex+ ")"))
		assert columnValue == expectedValue : "Searched value is not correct. Expected : " + expectedValue + "; Displayed : " + columnValue
	}

	@Keyword
	def exportIndicators (String clientName, String trackingPlanName)
	{
		/**
		 * Export indicators and check the sidenav elements
		 * 
		 * @param clientName				Client name to check the Spreadsheet title
		 * @param trackingPlanName			Tracking Plan name to check the Spreadsheet title
		 * 
		 * @return spreadsheet title
		 */
		selenium = getSelenium()
		WebUI.click(findTestObject('Object Repository/8. Indicators List/Export button'))
		assert WebUI.waitForElementVisible(findTestObject('8. Indicators List/First menu button'), 30)
		WebUI.click(findTestObject('8. Indicators List/First menu button'))
		assert WebUI.waitForElementPresent(findTestObject('5. Homepage/Progress bar'), 10) : "The export progress bar is not present after 10 seconds"
		assert WebUI.waitForElementNotPresent(findTestObject('5. Homepage/Progress bar'), 60) : "The export progress bar is still present after 60 seconds"
		assert WebUI.waitForElementPresent(findTestObject('5. Homepage/Snackbar message'), 30) : "The export confirmation message is not displayed"

		assertText(findTestObject('8. Indicators List/Sidenav/Sidenav title'), "Summary export")
		assertText(findTestObject('8. Indicators List/Sidenav/Close button'), "Close")
		assertText(findTestObject('8. Indicators List/Sidenav/Sheet title label'), "Sheet title")
		assertText(findTestObject('8. Indicators List/Sidenav/Sheet title'), clientName + "-" + trackingPlanName)
		assertText(findTestObject('8. Indicators List/Sidenav/Sheet url label'), "Sheet url")
		assert selenium.isElementPresent(getSelector(findTestObject('Object Repository/8. Indicators List/Sidenav/Spreadsheet link'))) : "Spreadsheet link is not present"
		assert WebUI.verifyElementClickable(findTestObject('Object Repository/8. Indicators List/Sidenav/Spreadsheet link')) : "Spreadsheet link is not clickable"
		assert selenium.isElementPresent(getSelector(findTestObject('Object Repository/8. Indicators List/Sidenav/Close button'))) : "Close button is not present"
		assert selenium.isElementPresent(getSelector(findTestObject('Object Repository/8. Indicators List/Sidenav/Copy url icon'))) : "Copy url icon is not present"

		return clientName + "-" + trackingPlanName
	}

	@Keyword
	def openSpreadsheet(String sheetTitle, boolean isEmptySheet = false)
	{
		/**
		 * Open the link to the exported spreadsheet from the sidenav and verifies the spreadsheet title
		 * Verifies the contact information if the spreadsheet was created
		 * 
		 * @param sheetTitle		Spreadsheet title to verify
		 * @param isEmptySheet		Set to true if spreadsheet should be empty, false otherwise (default)
		 */
		WebUI.click(findTestObject('Object Repository/8. Indicators List/Sidenav/Spreadsheet link'))
		WebUI.delay(1)
		WebUI.switchToWindowIndex(1)
		WebUI.waitForPageLoad(60)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/9. Spreadsheet/Spreadsheet title'), 30) : "Spreadsheet title is not visible after 30 seconds"
		assert WebUI.getText(findTestObject('Object Repository/9. Spreadsheet/Spreadsheet title')) == sheetTitle : "Sheet title is not correct. Displayed : '" + WebUI.getText(findTestObject('Object Repository/9. Spreadsheet/Spreadsheet title')) + "'; Expected : '"+sheetTitle+"'"

		if (GlobalVariable.isSelectTP == false)
		{
			WebUI.setText(findTestObject('Object Repository/9. Spreadsheet/Name box input'), "D35:J36")
			WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/Name box input'), Keys.chord(Keys.ENTER))
			assert WebUI.getText(findTestObject('Object Repository/9. Spreadsheet/input cell')) == "=HYPERLINK(\"mailto:" + GlobalVariable.googleID + "\";\"" + GlobalVariable.googleID + "\")" || WebUI.getText(findTestObject('Object Repository/9. Spreadsheet/input cell')) == "=LIEN_HYPERTEXTE(\"mailto:" + GlobalVariable.googleID + "\";\"" + GlobalVariable.googleID + "\")" : "Contact email is not correct: " + WebUI.getText(findTestObject('Object Repository/9. Spreadsheet/Contact email'))

			WebUI.setText(findTestObject('Object Repository/9. Spreadsheet/Name box input'), "D31:J32")
			WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/Name box input'), Keys.chord(Keys.ENTER))
			assert WebUI.getText(findTestObject('Object Repository/9. Spreadsheet/input cell')) == GlobalVariable.tpSetup["ContactName"] + ' - Analytics consulant' : "Contact name " + WebUI.getText(findTestObject('Object Repository/9. Spreadsheet/input cell')) + " is not correct: expected " + GlobalVariable.tpSetup["ContactName"] + ' - Analytics consulant'
		}
		//Go to indicators tab
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/9. Spreadsheet/Third tab button'), 30) : "Spreadsheet third tab is not visible after 30 seconds"
		WebUI.click(findTestObject('Object Repository/9. Spreadsheet/Third tab button'))
		if(isEmptySheet) {
			WebUI.setText(findTestObject('Object Repository/9. Spreadsheet/Name box input'), "A11")
			WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/Name box input'), Keys.chord(Keys.ENTER))
			assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/9. Spreadsheet/Alert dialog'), 1)
			WebUI.setText(findTestObject('Object Repository/9. Spreadsheet/Name box input'), "A12")
			WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/Name box input'), Keys.chord(Keys.ENTER))
			assert WebUI.verifyElementPresent(findTestObject('Object Repository/9. Spreadsheet/Alert dialog'), 1)
		}
	}

	@Keyword
	def setTextGsheet(String col, String text)
	{
		/**
		 * Method to write in a spreadsheet cell
		 * 
		 * @param col
		 * the cell id: ex: A12, D60 ...
		 * 
		 * @param text
		 * the text to insert in the cell
		 */
		WebUI.setText(findTestObject('Object Repository/9. Spreadsheet/Name box input'), col)
		WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/Name box input'), Keys.chord(Keys.ENTER))
		WebUI.delay(1)
		WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/input cell'), Keys.chord(Keys.DELETE))
		WebUI.setText(findTestObject('Object Repository/9. Spreadsheet/input cell'), text)
		WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/input cell'), Keys.chord(Keys.ENTER))
	}

	@Keyword
	def createindicatorfromgsheet(Map indicator)
	{
		/**
		 * Method to create indicator in the spreadsheet
		 * Indicator are created at the end of the file
		 * 
		 * @param indicator
		 * The map containing the information for the indicator creation
		 */

		if (indicator.size() > 0)
		{
			int row = 11

			WebUI.setText(findTestObject('Object Repository/9. Spreadsheet/Name box input'), "A" + row)
			WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/Name box input'), Keys.chord(Keys.ENTER))
			WebUI.delay(1)
			System.getProperty('os.name').contains('Windows') == true? WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/menubar'), Keys.chord(Keys.CONTROL, Keys.END)) : WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/menubar'), Keys.chord(Keys.COMMAND, Keys.END))
			WebUI.click(findTestObject('Object Repository/9. Spreadsheet/Name box input'))
			String lastcell = WebUI.getAttribute(findTestObject('Object Repository/9. Spreadsheet/Name box input'), 'value')
			row = lastcell.replaceAll(/\D+/, "").toInteger()
			WebUI.setText(findTestObject('Object Repository/9. Spreadsheet/Name box input'), "A" + row)
			WebUI.sendKeys(findTestObject('Object Repository/9. Spreadsheet/Name box input'), Keys.chord(Keys.ENTER))
			WebUI.delay(1)
			WebUI.scrollToElement(findTestObject('9. Spreadsheet/input add row'), 30)
			WebUI.sendKeys(findTestObject('9. Spreadsheet/input add row'), indicator.size())
			WebUI.click(findTestObject('9. Spreadsheet/button add row'))
			WebUI.delay(3)

			indicator.each()
			{
				setTextGsheet("A" + row, "creation")
				setTextGsheet("I" + row, it.key)
				String[] str = it.value.toString().split(';')
				for (def tmp in str)
				{
					String[] splittedstr = tmp.split('=')
					if (splittedstr.size() == 2)
						setTextGsheet(splittedstr[0] + row, splittedstr[1])
					else
						KeywordUtil.logInfo(splittedstr[0] + row +' ignored format not correct : ' + tmp)
				}
				row++
			}
		}
	}

	@Keyword
	def importIndicators (String sheetTitle, boolean isEmptyTP = false, boolean indicCreated = false, Map indicator = GlobalVariable.GsheetIndicators)
	{
		/**
		 * Import indicators and check the popup and sidenav elements
		 * 
		 * @param sheetTitle		Spreadsheet title to verify
		 * @param isEmptyTP			Set to true if tracking plan is empty, false otherwise (default)
		 */

		selenium = getSelenium()
		if(WebUI.getWindowTitle() != "Albatross")
			WebUI.switchToWindowIndex(0)
		if(selenium.isElementPresent(getSelector(findTestObject('Object Repository/8. Indicators List/Sidenav/Close button'))))
		{
			WebUI.click(findTestObject('Object Repository/8. Indicators List/Sidenav/Close button'))
			WebUI.delay(1)
		}
		WebUI.click(findTestObject('Object Repository/8. Indicators List/Import button'))
		assert WebUI.waitForElementVisible(findTestObject('8. Indicators List/First menu button'), 30)
		WebUI.click(findTestObject('8. Indicators List/First menu button'))
		assert WebUI.waitForElementVisible(findTestObject('8. Indicators List/Import popup/Confirm button'), 30)

		assertText(findTestObject('8. Indicators List/Import popup/Confirm button'), "Confirm")
		assertText(findTestObject('8. Indicators List/Import popup/Cancel button'), "Cancel")
		assertText(findTestObject('8. Indicators List/Import popup/Import popup title'), "Import indicators")
		assertText(findTestObject('8. Indicators List/Import popup/Import popup text'), "Are you sure you want to import the content of the spreadsheet in Albatross ?")

		WebUI.click(findTestObject('8. Indicators List/Import popup/Confirm button'))
		assert WebUI.waitForElementPresent(findTestObject('5. Homepage/Progress bar'), 10)
		assert WebUI.waitForElementNotPresent(findTestObject('5. Homepage/Progress bar'), 60)
		assert WebUI.waitForElementVisible(findTestObject('8. Indicators List/Sidenav/Indicators creation failed'), 10)

		assertText(findTestObject('8. Indicators List/Sidenav/Sidenav title'), "Summary import")
		assertText(findTestObject('8. Indicators List/Sidenav/Close button'), "Close")
		assertText(findTestObject('8. Indicators List/Sidenav/Sheet title label'), "Sheet title")
		assertText(findTestObject('8. Indicators List/Sidenav/Sheet title'), sheetTitle)
		assertText(findTestObject('8. Indicators List/Sidenav/Sheet url label'), "Sheet url")
		assert WebUI.verifyElementClickable(findTestObject('Object Repository/8. Indicators List/Sidenav/Spreadsheet link')) : "Spreadsheet link is not clickable"
		assertText(findTestObject('8. Indicators List/Sidenav/Spreadsheet link'), "here")

		assertText(findTestObject('8. Indicators List/Sidenav/Indicators deleted'), "Indicators deleted")
		assertText(findTestObject('8. Indicators List/Sidenav/Indicators failed'), "Indicators failed")
		assertText(findTestObject('8. Indicators List/Sidenav/Indicators updated'), "Indicators updated")
		assertText(findTestObject('8. Indicators List/Sidenav/Indicators created'), "Indicators created")
		assertText(findTestObject('8. Indicators List/Sidenav/Indicators creation failed'), "Indicators creation failed")

		if (indicCreated == true)
		{
			assertText(findTestObject('8. Indicators List/Sidenav/Indicators deleted number'), "0")
			assertText(findTestObject('8. Indicators List/Sidenav/Indicators failed number'), "0")
			assertText(findTestObject('8. Indicators List/Sidenav/Indicators updated number'), "0")
			assertText(findTestObject('8. Indicators List/Sidenav/Indicators created number'), indicator.size().toString())
			assertText(findTestObject('8. Indicators List/Sidenav/Indicators creation failed number'), "0")
			if (indicator.size() > 0)
				checkCreatedIndicatorKeyTypeAndColor(indicator)
		}
		else if(isEmptyTP)
		{
			assertText(findTestObject('8. Indicators List/Sidenav/Indicators deleted number'), "0")
			assertText(findTestObject('8. Indicators List/Sidenav/Indicators failed number'), "0")
			assertText(findTestObject('8. Indicators List/Sidenav/Indicators updated number'), "0")
			assertText(findTestObject('8. Indicators List/Sidenav/Indicators created number'), "0")
			assertText(findTestObject('8. Indicators List/Sidenav/Indicators creation failed number'), "0")
		}
	}
	@Keyword
	def checkCreatedIndicatorKeyTypeAndColor(Map indicator)
	{
		/**
		 * this method verify if the created indicator from gsheet have the correct key, prototype and color set in the profile
		 * @param indicator
		 * The map containing the information for the indicator creation from spreadsheet
		 */

		WebUI.click(findTestObject('8. Indicators List/Sidenav/Indicator created section'))
		for (int i = 0; i < indicator.size(); i++)
		{
			TestObject fullname = changeSelector(findTestObject('8. Indicators List/Sidenav/created indicator name'), "p:nth-child\\(\\d+\\)", "p:nth-child("+ (i + 1) +")")
			TestObject type = changeSelector(findTestObject('8. Indicators List/Sidenav/indicator created type'), "p:nth-child\\(\\d+\\)", "p:nth-child("+ (i + 1) +")")

			String[] splittedname = WebUI.getText(fullname).split(" \\(")
			String name = splittedname[0]
			boolean found = false
			indicator.each()
			{
				if (it.key == name)
				{
					found = true
					String[] values = it.value.toString().split(';')
					String[] splittedvalue = values[5].split('=')
					if (splittedvalue.size() > 1)
					{
						String value = splittedvalue[1]
						String typeformatted = 'type-' + value
						//Check the prototype
						assert WebUI.getAttribute(type, 'className').contains(typeformatted) == true: "Created indicator type does not match indicator type set in the profile"
					}
				}
			}
			if (found == false) //Check the key
				KeywordUtil.markFailedAndStop('Indicator key created ('+ name +') is different than indicator key set in the profile')

			// Check the color
			String rgbacolor = WebUI.getCSSValue(type, 'color')
			Color color = Color.fromString(rgbacolor)
			if (WebUI.getAttribute(type, 'className').contains('type-event') == true)
				assert color.asHex() == AlbatrosEventColor : "Event color is not correct: displayed: " + color.asHex() + " / expected: " + AlbatrosEventColor
			else if (WebUI.getAttribute(type, 'className').contains('type-variable') == true)
				assert color.asHex() == AlbatrosVariableColor : "Variable color is not correct: displayed: " + color.asHex() + " / expected: " + AlbatrosVariableColor
		}
	}

	@Keyword
	def selectIndicators(boolean isSelectedIndicator = false, int numberOfIndicator = 3)
	{
		/**
		 * selectIndicator
		 * 
		 * @param isSelectedIndicator
		 * 		if true, we select indicators from profile. If false, we select the first 3 indicators from list
		 * 
		 */

		// indicator number in the list
		ArrayList<Integer> indicators = new ArrayList<Integer>()

		if (isSelectedIndicator == false)
		{
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/8. Indicators List/Indicator cells'), 5): "No indicators are visible on the list"
			int randomNumber
			String indicatorLength = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/Indicators checkbox'))+"').length", null)

			if(indicatorLength.toInteger() == 0) KeywordUtil.markError("No indicator is present on the list")


			for (int i = 0; i<indicatorLength.toInteger();i++)
			{

				randomNumber = getRandomArbitrary(0, indicatorLength.toInteger())
				if (!indicators.contains(randomNumber))
				{
					indicators.add(randomNumber)
				}

				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/Indicators checkbox'))+"').item("+indicators[i]+").click()", null)
				WebUI.delay(1)

				if (indicators.size() == numberOfIndicator)
					break

			}
			return indicators
		}
		else if (isSelectedIndicator)
		{
			//TODO
			//If we choose an associated indicator by name
		}


	}

	@Keyword
	def selectAssociatedIndicators(boolean isSelectedAssociatedIndicator = false, int numberOfAssociatedIndicator = 3)
	{
		/**
		 * Select associated indicator
		 * 
		 * @param isSelectedAssociatedIndicator
		 * 		if true, we select associated indicator from profile. If false if select 3 random associated indicators
		 * @param numberOfIndicator
		 * 		Number of indicator we want to select randomly
		 * 
		 */

		assert WebUI.waitForElementPresent(findTestObject("Object Repository/8. Indicators List/Associated checkbox"), 5) != null :"No associated indicators are visible"
		ArrayList<Integer> randomAssociateIndicator = new ArrayList<Integer>()
		int randomNumber
		if (isSelectedAssociatedIndicator == false)
		{
			String associatedLength = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/Associated checkbox'))+"').length", null)


			for (int i = 0; i<associatedLength.toInteger();i++)
			{
				randomNumber = getRandomArbitrary(0, associatedLength.toInteger())
				if (!randomAssociateIndicator.contains(randomNumber))
				{
					randomAssociateIndicator.add(randomNumber)
				}

				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/Associated checkbox'))+"').item("+randomAssociateIndicator[i]+").click()", null)
				WebUI.delay(1)
				if (randomAssociateIndicator.size() == numberOfAssociatedIndicator)
					break
			}
		}
		else if (isSelectedAssociatedIndicator)
		{
			//TODO
			//If we choose an associated indicator by name
		}
	}
	@Keyword
	def removeAssociation()
	{
		/**
		 * Remove association indicator
		 * 
		 */

		WebUI.click(findTestObject("Object Repository/8. Indicators List/Remove association button"))
		WebUI.delay(1)
		assert WebUI.waitForElementPresent(findTestObject("Object Repository/8. Indicators List/Remove assoction confirm button"), 5)
		WebUI.click(findTestObject("Object Repository/8. Indicators List/Remove assoction confirm button"))
		KeywordUtil.logInfo("Associations are removed")

	}

	@Keyword
	def createAssociationButton()
	{
		/**
		 * Create Association button
		 * 
		 */

		assert WebUI.waitForElementClickable(findTestObject("Object Repository/8. Indicators List/Create new association button"), 5): "Create association button is not clickable"
		WebUI.click(findTestObject("Object Repository/8. Indicators List/Create new association button"))

	}
	@Keyword
	def createAssociation()
	{
		/**
		 * Create association for an indicator
		 * 
		 */

		createAssociationButton()
		ArrayList<String> keyTab = new ArrayList<String>()
		String keys = GlobalVariable.createAssociation
		keyTab = keys.split(',')

		for (int i =0; i < keyTab.size();i++)
		{
			assert WebUI.waitForElementPresent(findTestObject("Object Repository/8. Indicators List/1. Create association/Search bar"), 5): "Search bar is no visible"
			WebUI.setText(findTestObject("Object Repository/8. Indicators List/1. Create association/Search bar"), keyTab[i])

			selectKey(keyTab[i])
		}
		WebUI.delay(2)
		boolean addAssociationClickable = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/1. Create association/Add association button'))+"').disabled", null)
		assert addAssociationClickable == false: "Add association button is no clickable. Key searched is not visible"

		WebUI.click(findTestObject('Object Repository/8. Indicators List/1. Create association/Add association button'))
		assert WebUI.waitForElementPresent(findTestObject("Object Repository/8. Indicators List/1. Create association/Snackbar association create"), 10): "Assocociation is not added correctly"
	}
	def selectKey(String keyTab)
	{
		/**
		 * Select a key on the edit list
		 * 
		 * @param keyTab
		 * 		
		 * 
		 */
		int chevronLength = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/1. Create association/All chevron'))+"').length", null)
		for (int j =0; j< chevronLength;j++)
		{
			WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/1. Create association/All chevron'))+"').item("+j+").click()", null)
		}
		WebUI.delay(2)
		int keyLength = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/1. Create association/Key name'))+"').length", null)

		for (int i =0; i < keyLength; i++)
		{

			String keyName = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/1. Create association/Key name'))+"').item("+i+").textContent.trim()", null)
			if (keyName != keyTab)
				continue
			else
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/1. Create association/Key checkbox'))+"').item("+i+").click()", null)

		}
	}

	@Keyword
	def checkAssociationDisplayed(ArrayList<Integer> indicators, String keys)
	{
		/**
		 * Check that association is done in the list
		 * 
		 * @param indicators
		 * 		Number of key associated
		 * 		
		 * @param keys
		 * 		Name of key
		 * 
		 */
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/8. Indicators List/1. Create association/Search bar'), 5)
		int keyLength = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/Key name association'))+"').length", null)
		int increment = 0
		String keyName
		for (int i =0; i < keyLength; i++)
		{

			keyName = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/Key name association'))+"').item("+i+").textContent.trim()", null)

			if (keyName != keys)
				continue
			else
				increment ++
		}
		assert increment == indicators.size(): "Element: "+keys+" is not associated correctly"
	}

	@Keyword
	def checkAssociatedVariable()
	{
		/**
		 * Check edited filed on the association field
		 * 
		 * @param elementAssociation
		 * 		List of edited field
		 * 
		 */
		Map indicatorsFilter = GlobalVariable.indicatorsFilter

		String keyList = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/Key name association'))+"').textContent.trim()", null)
		String nameList = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/Name association'))+"').textContent.trim()", null)
		Map editIndicator = GlobalVariable.editIndicator

		assert editIndicator['Key'] == keyList: "Key is not correct. It should be: "+editIndicator['Key']+" instead of: "+keyList
		assert editIndicator['Name'] == nameList: "Name is not correct. It should be: "+editIndicator['Name']+" instead of: "+nameList
		if (indicatorsFilter['Primary'] == "Event")
		{
			String exampleList = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/Example association'))+"').textContent.trim()", null)
			assert editIndicator['Explanation'] == exampleList: "Example is not correct. It should be: "+editIndicator['Explanation']+" instead of: "+exampleList
		}
		KeywordUtil.logInfo("All edited fields are correct at association panel for the key "+editIndicator['Key'])
	}

	@Keyword
	def checkAssociatedVariableKey(String getKey)
	{
		/**
		 * Check edited filed on the association field
		 *
		 * @param getKey
		 * 		Key to check
		 *
		 */
		String keyList = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/8. Indicators List/Key name association'))+"').textContent.trim()", null)
		assert getKey == keyList: "Key is not correct."
	}
	@Keyword
	def searchAtAssociationPanel(String keys)
	{
		/**
		 *Search a key at association panel
		 *
		 */

		assert WebUI.waitForElementPresent(findTestObject("Object Repository/8. Indicators List/Search bar associated"), 5): "Search bar is not visible"
		WebUI.setText(findTestObject('Object Repository/8. Indicators List/Search bar associated'), keys)
		assert WebUI.waitForElementPresent(findTestObject('8. Indicators List/Key name association'), 10)
	}

	@Keyword
	def searchAtIndicatorPanel(String keys)
	{
		/**
		 * Search a key at indicator panel
		 * 
		 */
		WebUI.sendKeys(findTestObject('Object Repository/8. Indicators List/Indicator Search Bar'), Keys.chord(Keys.END, Keys.SHIFT, Keys.HOME, Keys.DELETE, Keys.ENTER))
		assert WebUI.waitForElementPresent(findTestObject("Object Repository/8. Indicators List/Indicator Search Bar"), 5): "Search bar is not visible"
		WebUI.setText(findTestObject('Object Repository/8. Indicators List/Indicator Search Bar'), keys)
		assert WebUI.waitForElementPresent(findTestObject('8. Indicators List/Key name indicator'), 10): keys+" is not found"
		KeywordUtil.logInfo("Indicator "+keys+" is found")
		WebUI.delay(3)

	}
}
