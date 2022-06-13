package com.reusableComponents

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.Keys

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import project.AutoPause

import internal.GlobalVariable

public class DV360 extends Common
{

	@Keyword
	public String Start()
	{
		/**
		 * Open browser with DV360
		 */
		KeywordUtil.logInfo("Navigating to DV360")
		String baseUrl = 'https://displayvideo.google.com/#ng_nav/overview'
		WebUI.openBrowser(baseUrl)
		WebUI.maximizeWindow()
		WebUI.waitForPageLoad(30)
		WebUI.delay(2)
	}

	@Keyword
	def goToDv360(boolean newTab = false)
	{
		/**
		 * Navigate to DV360 homepage
		 */
		KeywordUtil.logInfo("Navigating to DV360")

		if(newTab == true)
			openNewTab()

		WebUI.navigateToUrl('https://displayvideo.google.com/#ng_nav/overview')
		WebUI.waitForPageLoad(30)
		WebUI.delay(2)
	}

	@Keyword
	def logInToDv360(boolean newTab)
	{
		/**
		 * Log in to DV360
		 * 
		 * @param newTab	Open DV360 in a new tab if set to "true", else open in current tab
		 */

		selenium = getSelenium()
		if(newTab == true)
			openNewTab()
		WebUI.navigateToUrl('https://displayvideo.google.com/#ng_nav/overview')
		WebUI.waitForPageLoad(30)
		WebUI.delay(2)
		WebUI.setText(findTestObject('1. Login/Email input'), GlobalVariable.googleID)
		WebUI.sendKeys(findTestObject('1. Login/Email input'), Keys.chord(Keys.ENTER))
		Thread.sleep(500)
		assert WebUI.waitForElementNotPresent(findTestObject('0. Login Google/Loading progress'), 15) : "Loading bar is still present after 15 seconds"
		WebUI.setEncryptedText(findTestObject('1. Login/Password input'), GlobalVariable.googlePassword)
		WebUI.sendKeys(findTestObject('1. Login/Password input'), Keys.chord(Keys.ENTER))
		Thread.sleep(500)
		assert WebUI.waitForElementNotPresent(findTestObject('0. Login Google/Loading progress'), 15) : "Loading bar is still present after 15 seconds"

		if(selenium.isElementPresent(getSelector(findTestObject('0. Login Google/2FA header'))))
			Googleauthentificator()
	}
	@Keyword
	def dv360SearchItem(String itemToSearch)
	{
		/**
		 * Search an item on DV360
		 * 
		 * @param itemToSearch	Name or ID of the item to search
		 */

		selenium = getSelenium()
		assert WebUI.waitForElementVisible(findTestObject('0. DV360/DV360 search button'), 60) : "Search button is not visible after 60 seconds"
		int attempts = 3
		while(attempts > 0 && !selenium.isElementPresent(getSelector(findTestObject('0. DV360/Search suggestion'))))
		{
			WebUI.click(findTestObject('0. DV360/DV360 search button'))
			WebUI.setText(findTestObject('0. DV360/input_Search dv360'), itemToSearch)
			WebUI.waitForElementVisible(findTestObject('0. DV360/Search suggestion'), 30, FailureHandling.OPTIONAL)
			attempts--
		}
		assert selenium.isElementPresent(getSelector(findTestObject('0. DV360/Search suggestion'))) : "Searched element not found : " + itemToSearch
		WebUI.click(findTestObject('0. DV360/Search suggestion'), FailureHandling.STOP_ON_FAILURE)
		WebUI.delay(5)
		WebUI.waitForPageLoad(30)
		assert WebUI.verifyElementNotPresent(findTestObject('0. DV360/Item List/List Loading bar'), 60) == true
	}

	def goToCPSettings()
	{
		/**
		 * Go to 'Campaign Settings' using current URL
		 */

		String cpUrl = WebUI.getUrl().replaceFirst("explorer", "details")
		WebUI.navigateToUrl(cpUrl)
		WebUI.waitForPageLoad(60)
		assert WebUI.verifyElementNotPresent(findTestObject('0. DV360/Item List/List Loading bar'), 60)
	}

	@Keyword
	def gotoIoOverview()
	{
		/**
		 * Go to the 'Insertion Order view'
		 */
		WebUI.click(findTestObject('0. DV360/Navigation insertion order tab'))
		Thread.sleep(250)
		assert WebUI.waitForElementNotPresent(findTestObject('0. DV360/Campaign/Campaign creation/Progress bar'), 10) == true : "Timeout : loading still displayed after 10 sec"
	}

	@Keyword
	def setOptimizationView()
	{
		/**
		 * Set the view optimization in the dv grid
		 */
		WebUI.click(findTestObject('0. DV360/Insertion Order/Grid view menu'))
		assert WebUI.waitForElementVisible(findTestObject('0. DV360/Insertion Order/Grid view menu dropdown'), 5) == true : "Timeout : menu not displayed after 5sec"
		WebUI.click(findTestObject('0. DV360/Insertion Order/Grid view menu option optimization'))
		WebUI.delay(2)
		assert WebUI.waitForElementNotPresent(findTestObject('0. DV360/Insertion Order/Progress bar'), 30) == true : "Timeout : loading still displayed after 30 sec"
		WebUI.click(findTestObject('0. DV360/Item List/Remove Filter button'))
		WebUI.delay(2)
	}

	@Keyword
	ArrayList<String> getIosStatusmatchingProfile(String Appliedon, boolean reactivateIO, Map currentMap = GlobalVariable.ruleCreate,
			String previousIos = GlobalVariable.ruleCreate['Insertion Orders'], previousAdvertisers = GlobalVariable.ruleCreate['Advertisers'])
	{
		/**
		 * Get the OI status, id, name, from the OI set in the profile
		 * 
		 * @param Appliedon
		 * Determine if we get specific OI or all OIs matching the budgetType
		 * 
		 * @return IosIntel
		 * A list of data (status;name;id) per OI found
		 */
		int currentOI = 0
		ArrayList<String> result = new ArrayList<String>()
		boolean lastfound = false
		ArrayList<String> IosIntel = []
		String[] advListArray = currentMap["Advertisers"].toString().split(',')
		String advList = currentMap["Advertisers"].toString()
		String ioList = currentMap["Insertion Orders"].toString()
		
		if(currentMap != GlobalVariable.ruleCreate)
		{
			if(currentMap['Keep Old Advertisers']) advList += "," + previousAdvertisers
			if(currentMap['Keep Old Insertion Orders']) ioList += "," + previousIos
			advListArray = advList.split(',')
		}

		for (int i = 0; i < advListArray.size(); i++)
		{
			dv360SearchItem(advListArray[i])
			gotoIoOverview()
			setOptimizationView()
			result.addAll(getIoStatusfromBudgetType(GlobalVariable.ruleCreate["Budget Type"], Appliedon, ioList, currentOI, reactivateIO))
			for (String res in result)
			{
				if (res.split(';').size() == 1)
				{
					currentOI = res.toInteger()
					assert i + 1 < advListArray.size() : "The searched OI was not found"
					continue
				}
				else
					IosIntel += res
			}
		}
		return IosIntel
	}

	@Keyword
	ArrayList<String> getIoStatusfromBudgetType(String budgetType, String Appliedon, String Ios, int currentOI, boolean reactivateIO)
	{
		/**
		 * Retrieve the OI status, id, name, from the OI set in the profile
		 * 
		 * @param budgetType
		 * Determine the type of OI to retrieve data
		 * 
		 * @param Appliedon
		 * Determine the recuperation of data for specific OI or all OIs matching budgetType
		 * 
		 * @param Ios
		 * A string of Ios coma separated if Appliedon is set to Insertion Order
		 * 
		 * @param currentOI
		 * The position of the current OI to retrieve data
		 * 
		 * @return IosIntel
		 * A list of data (status;name;id) per OI found
		 * or if no result, the value of currentOI param
		 */
		String result
		ArrayList<String> IosIntel = []
		if (Appliedon == 'Advertiser')
			IosIntel = getAllIoStatus(budgetType)
		else if (Appliedon == 'Insertion Order')
		{
			String[] Iostab = Ios.split(',')
			for (; currentOI < Iostab.size() ; currentOI++)
			{
				result = getSpecificIoStatus(budgetType, Iostab[currentOI], reactivateIO)
				if (result == null)
				{
					IosIntel.add(currentOI.toString())
					return IosIntel
				}
				else
					IosIntel.add(result)
			}
		}
		return IosIntel
	}

	@Keyword
	def getSpecificIoStatus(String budgetType, String OI, boolean reactivateIO)
	{
		/**
		 * The function get the status, name and id of a specific OI
		 * if the OI does not match the budgetType an error is thrown
		 * 
		 * @param budgetType
		 * The budgetType that the OI should be
		 * 
		 * @param OI
		 * The identifier of the OI
		 * 
		 * @return intel
		 * A string of the data found for the OI. format(status;name;id)
		 * 
		 * 
		 */
		WebUI.sendKeys(findTestObject('0. DV360/Item List/Filter input'), OI + Keys.chord(Keys.ENTER))
		WebUI.delay(3)
		selenium = getSelenium()
		KeywordUtil.logInfo('selector = ' + getSelector(findTestObject('0. DV360/Insertion Order/Grid empty')))
		if (selenium.isElementPresent(getSelector(findTestObject('0. DV360/Insertion Order/Grid empty'))) == true)
		{
			KeywordUtil.logInfo('empty result going to next advertiser for OI : ' + OI)
			return null
		}
		String intel
		TestObject rowbudget = findTestObject('0. DV360/Insertion Order/Grid row column budget')
		TestObject rowstatus = findTestObject('0. DV360/Insertion Order/Grid row column status')
		TestObject rowname = findTestObject('0. DV360/Insertion Order/Grid row column name')
		TestObject rowid = findTestObject('0. DV360/Insertion Order/Grid row column id')

		if (GlobalVariable.ruleCreate['Apply on'] == 'Insertion Order' && GlobalVariable.ruleCreate['Reactivate IO'] == true)
		{
			AutoPause ap = new AutoPause()
			ap.setRuleIOsActive(GlobalVariable.ruleCreate['Insertion Orders'], false)
		}

		intel = WebUI.getAttribute(rowstatus, 'className').find(/\w+$/) + ';' + WebUI.getAttribute(rowname, 'innerText') + ';' + WebUI.getText(rowid)

		if (budgetType == 'Impressions')
			assert WebUI.getText(rowbudget).contains('impr.') == true : "OI: " + OI + "does not have the correct budgetType : " + budgetType
		else if (budgetType != 'Impressions')
			assert WebUI.getText(rowbudget).contains('impr.') == false : "OI: " + OI + " does not have the correct budgetType : " + budgetType

		WebUI.click(findTestObject('0. DV360/Item List/Remove Filter button'))
		WebUI.delay(1)

		return intel
	}

	@Keyword
	def getAllIoStatus(String budgetType)
	{
		/**
		 * Get the status, name and ID of IO from the dv optimization view according to the specified budgetType
		 * @param budgetType
		 * The budget type to retrieve IO's information
		 * 
		 * @return IosIntel
		 * A list of IO containing the data, each IO is represented by a string containing (status;name;id)
		 */
		String intel
		ArrayList<String> IosIntel = []
		int Ionumbermax = WebUI.getText(findTestObject('0. DV360/Pagination/pagination range')).find(/\d+$/).toInteger()
		int paginationsize =  WebUI.getText(findTestObject('0. DV360/Pagination/pagination size')).toInteger()
		TestObject rowbudget = findTestObject('0. DV360/Insertion Order/Grid row column budget')
		TestObject rowstatus = findTestObject('0. DV360/Insertion Order/Grid row column status')
		TestObject rowname = findTestObject('0. DV360/Insertion Order/Grid row column name')
		TestObject rowid = findTestObject('0. DV360/Insertion Order/Grid row column id')
		for (int i = 3;i < Ionumbermax + 3; i++)
		{
			rowbudget = changeSelector(findTestObject('0. DV360/Insertion Order/Grid row column budget'),"row:nth-child\\(\\d+\\)", "row:nth-child("+ (i % paginationsize) +")")
			WebUI.scrollToElement(rowbudget , 10)
			if ((budgetType == "Impressions" && WebUI.getText(rowbudget).contains('impr.')) ||
			(budgetType != "Impressions" && WebUI.getText(rowbudget).contains('impr.') == false))
			{
				rowstatus = changeSelector(findTestObject('0. DV360/Insertion Order/Grid row column status'),"row:nth-child\\(\\d+\\)", "row:nth-child("+ (i % paginationsize) +")")
				rowname = changeSelector(findTestObject('0. DV360/Insertion Order/Grid row column name'),"row:nth-child\\(\\d+\\)", "row:nth-child("+ (i % paginationsize) +")")
				rowid = changeSelector(findTestObject('0. DV360/Insertion Order/Grid row column id'),"row:nth-child\\(\\d+\\)", "row:nth-child("+ (i % paginationsize) +")")
				intel = WebUI.getAttribute(rowstatus, 'className').find(/\w+$/) + ';' + WebUI.getAttribute(rowname, 'innerText') + ';' + WebUI.getText(rowid)
				IosIntel += intel
			}
		}
		return IosIntel
	}

	@Keyword
	def checkDataStatus(ArrayList<String> data)
	{
		/**
		 * This method verify if there is at least one OI active in the data
		 */
		boolean Isactive = false
		for (String str in data)
		{
			String[] splitted = str.split(';')
			if (splitted[0] != 'paused')
				Isactive = true
		}
		assert Isactive == true : "The rule does not have any active OI"
	}

	def goToIoTab()
	{
		/**
		 * Go to the 'Insertion Order details' tab using current URL
		 */

		String ioUrl = WebUI.getUrl().replaceFirst("lis", "details")
		WebUI.navigateToUrl(ioUrl)
		WebUI.waitForPageLoad(60)
		assert WebUI.verifyElementNotPresent(findTestObject('0. DV360/Item List/List Loading bar'), 60)
	}

	def openIoTab()
	{
		/**
		 * Open the 'Insertion Order' or 'Insertion Order details' tab
		 */

		WebUI.click(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion Order tab'))
		Thread.sleep(500)
		assert WebUI.verifyElementNotPresent(findTestObject('0. DV360/Item List/List Loading bar'), 60)
	}

	@Keyword
	def dvSetLineStatusPaused()
	{
		selenium = getSelenium()
		assert WebUI.waitForElementPresent(findTestObject('0. DV360/Line Item fields/Status button'), 15) : "Status button is not present after 15 seconds"
		if(WebUI.getText(findTestObject('0. DV360/Line Item fields/Status button')) != "Paused")
		{
			WebUI.focus(findTestObject('0. DV360/Line Item fields/Status button'))
			WebUI.click(findTestObject('0. DV360/Line Item fields/Status button'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/0. DV360/Line Item fields/Status Paused'), 30) : "Paused Status button is not present after 30 seconds"
			WebUI.click(findTestObject('Object Repository/0. DV360/Line Item fields/Status Paused'))
			WebUI.delay(1)
			WebUI.click(findTestObject('0. DV360/Line Item fields/Save button'))
			assert WebUI.waitForElementVisible(findTestObject('0. DV360/Line Item fields/Changes saved message'), 30) == true : "Line was not saved"
			assert WebUI.verifyElementText(findTestObject('0. DV360/Line Item fields/Changes saved message'), "Changes saved") == true : "Save message is incorrect"
			int attempts = 30
			while(attempts > 0 && selenium.isElementPresent(getSelector(findTestObject('0. DV360/Line Item fields/Dismiss button'))) == true)
			{
				WebUI.click(findTestObject('0. DV360/Line Item fields/Dismiss button'))
				attempts--
			}
		}
		WebUI.delay(1)
		KeywordUtil.logInfo("Item has been paused")
	}
	@Keyword
	def dvSetLineStatusActive()
	{
		selenium = getSelenium()
		assert WebUI.waitForElementPresent(findTestObject('0. DV360/Line Item fields/Status button'), 15) : "Status button is not present after 15 seconds"
		if(WebUI.getText(findTestObject('0. DV360/Line Item fields/Status button')) != "Active")
		{
			WebUI.focus(findTestObject('0. DV360/Line Item fields/Status button'))
			WebUI.click(findTestObject('0. DV360/Line Item fields/Status button'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/0. DV360/Line Item fields/Status Active'), 30) : "Active Status button is not present after 30 seconds"
			WebUI.click(findTestObject('Object Repository/0. DV360/Line Item fields/Status Active'))
			WebUI.delay(1)
			WebUI.click(findTestObject('0. DV360/Line Item fields/Save button'))
			assert WebUI.waitForElementPresent(findTestObject('0. DV360/Line Item fields/Error snackbar'), 30) == true : "Progress saving popup not found"
			if (WebUI.getText(findTestObject('0. DV360/Line Item fields/Error snackbar')).contains('Saving...') == true)
				assert WebUI.verifyElementText(findTestObject('0. DV360/Line Item fields/Error snackbar'), 'Saving...refreshing may lose your changes') == true : "Saving message popup is not correct"

			for (int i = 0; WebUI.waitForElementPresent(findTestObject('0. DV360/Line Item fields/Changes saved message'), 3) == false; i++)
			{
				if (i > 10)
					KeywordUtil.markFailedAndStop('Timeout popup changes saved not displayed after 30 sec')
				if (selenium.isElementPresent(getSelector(findTestObject('0. DV360/Line Item fields/Error snackbar'))) == true &&
				WebUI.getText(findTestObject('0. DV360/Line Item fields/Error snackbar')).contains('An insertion order cannot be active under a paused campaign.') == true)
					KeywordUtil.markFailedAndStop(WebUI.getText(findTestObject('0. DV360/Line Item fields/Error snackbar')))
			}

			assert WebUI.verifyElementText(findTestObject('0. DV360/Line Item fields/Changes saved message'), "Changes saved") == true : "Save message is incorrect"
			int attempts = 30
			while(attempts > 0 && selenium.isElementPresent(getSelector(findTestObject('0. DV360/Line Item fields/Dismiss button'))) == true)
			{
				WebUI.click(findTestObject('0. DV360/Line Item fields/Dismiss button'))
				attempts--
			}

			WebUI.delay(1)
			KeywordUtil.logInfo("Item has been activated")
		} else
		{
			KeywordUtil.logInfo("Item already Active")
		}
	}

	@Keyword
	def verifyIosPaused(def ios)
	{
		/**
		 *  Search IOs and verify the status is Paused
		 */
		for (String io in ios)
		{
			if(io == "") continue
				dv360SearchItem(io)
			goToIoTab()
			verifyStatus("Paused")
		}
		WebUI.delay(1)
	}
	@Keyword
	def verifyIosActive(def ios)
	{
		/**
		 *  Search IOs and verify the status is Active
		 */
		for (String io in ios)
		{
			if(io == "") continue
				dv360SearchItem(io)
			goToIoTab()
			verifyStatus("Active")
		}
	}


	@Keyword
	def setIosActive(String[] ios, boolean go_to = true)
	{
		/**
		 *  Search IOs and set the status to Active
		 */
		for (String io in ios)
		{
			KeywordUtil.logInfo('Activation OI : ' + io)
			if (go_to == true && io != "")
			{
				dv360SearchItem(io)
				goToIoTab()
				dvSetLineStatusActive()
			}
			else if (go_to == false && io != "")
			{
				assert WebUI.waitForElementVisible(findTestObject('0. DV360/Insertion Order/Grid row status icon'), 30) == true : "Timeout status icon not visible after 30sec"
				if (WebUI.getAttribute(findTestObject('0. DV360/Insertion Order/Grid row status icon'), 'className').contains('paused') == true)
				{
					WebUI.mouseOver(findTestObject('0. DV360/Insertion Order/Grid row status icon'))
					WebUI.click(findTestObject('0. DV360/Insertion Order/Grid row status edit'))
					assert WebUI.waitForElementVisible(findTestObject('0. DV360/Insertion Order/Grid row edit status dropdown'), 10) == true : "Timeout : dropdown not visible after 10 sec"
					WebUI.click(findTestObject('0. DV360/Insertion Order/Grid row edit status to active option'))
					assert WebUI.waitForElementVisible(findTestObject('0. DV360/Insertion Order/Grid row edit status loading spinner'), 10) == true : "Timeout: Loading not displayed after 10sec"
					assert WebUI.waitForElementNotPresent(findTestObject('0. DV360/Insertion Order/Grid row edit status loading spinner'), 30) == true : "Timeout: Loading still displayed after 30 sec"
					WebUI.delay(1)
					assert WebUI.verifyElementNotPresent(findTestObject('0. DV360/Line Item fields/Error snackbar'), 3) == true : WebUI.getText(findTestObject('0. DV360/Line Item fields/Error snackbar'))
				}
				assert WebUI.getAttribute(findTestObject('0. DV360/Insertion Order/Grid row status icon'), 'className').contains('paused') == false : "OI : " + io + " is not active for unknow reason"
			}
		}
	}

	@Keyword
	def verifyStatus(String expectedStatus)
	{
		/**
		 * this function verify the status
		 *
		 * @param correctstatus
		 * The status uploaded
		 */
		KeywordUtil.logInfo("Expected status " + expectedStatus)
		String displayedStatus = WebUI.getText(findTestObject('Object Repository/0. DV360/Line Item fields/Status label'))
		assert displayedStatus == expectedStatus : "Expected status is not present. Expected : " + expectedStatus + " ; Displayed : " + displayedStatus
	}

	@Keyword
	def verifyIosStatusNotChanged(ArrayList<String> IOdata)
	{
		for (String IO in IOdata)
		{
			String[] data = IO.split(';')
			dv360SearchItem(data[2])
			goToIoTab()
			data[0] == 'paused' ? verifyStatus("Paused") : verifyStatus("Active")
		}
	}

	@Keyword
	def verifyautopauseondv(ArrayList<String> IOmail, ArrayList<String> IOdata, boolean warningemail=false)
	{
		/**
		 * this function verify the OI status on dv
		 * @param ios
		 * The list of OIs ID to be verified
		 * 
		 * @param warningemail
		 * Determine the status to be checked
		 */
		if (warningemail == true)
		{
			openNewTab()
			goToDv360()
			verifyIosStatusNotChanged(IOdata)
		}
		else if (warningemail == false)
		{
			openNewTab()
			goToDv360()
			verifyIosPaused(IOmail)
		}
	}

	def createCampaign(String Adv =  GlobalVariable.CPMapDV['Advertiser'])
	{
		/**
		 * Create a new campaign in DV360. We can choose an Advertiser where we want to create an IO. 
		 * 
		 * @param Adv
		 * 		By default, Adv = GlobalVariable.CPMapDV["Advertiser"]
		 * @return name
		 *		name + timestamps
		 * 
		 */
		ArrayList<String> kpi = new ArrayList<String>()

		dv360SearchItem(Adv)
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/New campaign'), 15): "New campaign button is not visible"
		WebUI.click(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/New campaign'))
		WebUI.delay(2)
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Progress bar'), 15): "New campaign panel is not loaded"

		//Name - Mandatory
		String name = setNewName(GlobalVariable.CPMapDV['cpName'])

		//Status - Mandatory
		setNewStatus(GlobalVariable.CPMapDV['cpStatus'])

		//Overall campaign goal - Mandatory
		WebUI.click(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Overall campaign goal'))
		switch(GlobalVariable.CPMapDV['cpOverallCampaignGoal'])
		{
			case '1':
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('0. DV360/Campaign/Campaign creation/List value'))+"').item(0).click()", null)
				KeywordUtil.logInfo("Overall campaign goal: Raise awareness of my brand or product")
				break
			case '2':
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('0. DV360/Campaign/Campaign creation/List value'))+"').item(1).click()", null)
				KeywordUtil.logInfo("Overall campaign goal: Drive online action or visits")
				break
			case '3':
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('0. DV360/Campaign/Campaign creation/List value'))+"').item(2).click()", null)
				KeywordUtil.logInfo("Overall campaign goal: Drive offline or in-store sales")
				break
			case '4':
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('0. DV360/Campaign/Campaign creation/List value'))+"').item(3).click()", null)
				KeywordUtil.logInfo("Overall campaign goal: Drive app installs or engagements")
				break
			default:
				KeywordUtil.markErrorAndStop("You need to set a correct Overall CampaignGoal in the profile to create a campaign")
		}
		//KPI - Mandatory
		WebUI.click(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI'))
		for (String value in (GlobalVariable.CPMapDV['cpKpi']).split(','))
		{
			kpi.add(value)
		}
		switch(kpi[0])
		{
			case "CPV":
			//WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('0. DV360/Campaign/Campaign creation/List value'))+"').item(0).click()", null)
				TestObject kpiType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI type'), "kpi", kpi[0])
				assert WebUI.waitForElementVisible(kpiType, 10)
				WebUI.click(kpiType)
				KeywordUtil.logInfo("KPI: CPV")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI goal'), kpi[1])
				break

			case "CPM":
				TestObject kpiType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI type'), "kpi", kpi[0])
				assert WebUI.waitForElementVisible(kpiType, 10)
				WebUI.click(kpiType)
				KeywordUtil.logInfo("KPI: CPM")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI goal'), kpi[1])
				break

			case "Viewable%":
				TestObject kpiType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI type'), "kpi", "Viewable %")
				assert WebUI.waitForElementVisible(kpiType, 10)
				WebUI.click(kpiType)
				KeywordUtil.logInfo("KPI: Viewable%")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI goal'), kpi[1])
				break

			case "CPIAVC":
				TestObject kpiType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI type'), "kpi", kpi[0])
				assert WebUI.waitForElementVisible(kpiType, 10)
				WebUI.click(kpiType)
				KeywordUtil.logInfo("KPI: CPIAVC")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI goal'), kpi[1])
				break

			case "CPA":
				TestObject kpiType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI type'), "kpi", kpi[0])
				assert WebUI.waitForElementVisible(kpiType, 10)
				WebUI.click(kpiType)
				KeywordUtil.logInfo("KPI: CPA")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI goal'), kpi[1])
				break

			case "CPC":
				TestObject kpiType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI type'), "kpi", kpi[0])
				assert WebUI.waitForElementVisible(kpiType, 10)
				WebUI.click(kpiType)
				KeywordUtil.logInfo("KPI: CPC")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI goal'), kpi[1])
				break

			case "CTR":
				TestObject kpiType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI type'), "kpi", kpi[0])
				assert WebUI.waitForElementVisible(kpiType, 10)
				WebUI.click(kpiType)
				KeywordUtil.logInfo("KPI: CTR")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI goal'), kpi[1])
				break

			case "Other...":
				TestObject kpiType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/KPI type'), "kpi", kpi[0])
				assert WebUI.waitForElementVisible(kpiType, 10)
				WebUI.click(kpiType)
				KeywordUtil.logInfo("KPI: Other")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Kpi goal other'), kpi[1])
				break
			default:
				KeywordUtil.markErrorAndStop("You need to set a correct KPI in the profile to create a campaign. KPI: "+kpi[0]+" and value "+kpi[1]+" are not correct")
		}

		//Creative type you expected to use - Mandatory
		switch(GlobalVariable.CPMapDV['cpCreaType'])
		{
			case "Display":
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Creative type Display'))+"').click()", null)
				KeywordUtil.logInfo("Creative type: Display")
				break

			case "Video":
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Creative type Video'))+"').click()", null)
				KeywordUtil.logInfo("Creative type: Video")
				break

			case "Audio":
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Creative type Audio'))+"').click()", null)
				KeywordUtil.logInfo("Creative type: Audio")
				break
			default:
				KeywordUtil.markErrorAndStop("You need to set a correct creative type in the profile to create a campaign")
		}

		//Planned spend
		if (GlobalVariable.CPMapDV['cpPlannedSpend'] != "")
		{
			WebUI.setText(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Planned spend'), GlobalVariable.CPMapDV['cpPlannedSpend'])
		}


		//Planned start date - Mandatory
		if (GlobalVariable.CPMapDV['cpPlannedStartDate'] != "")
		{
			WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Planned start and end date'))+"').item(0).click()", null)
			Thread.sleep(500)
			WebUI.setText(findTestObject('0. DV360/Campaign/Campaign creation/input start end date'), GlobalVariable.CPMapDV['cpPlannedStartDate'])
			WebUI.sendKeys(findTestObject('0. DV360/Campaign/Campaign creation/input start end date'), Keys.chord(Keys.ENTER))
		} else
		{
			KeywordUtil.markErrorAndStop("You need to set a correct start date in the profile to create a campaign")
		}

		//Planned end date - Mandatory
		if (GlobalVariable.CPMapDV['cpPlannedEndDate'] != "")
		{
			WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Planned start and end date'))+"').item(1).click()", null)
			Thread.sleep(500)
			WebUI.setText(findTestObject('0. DV360/Campaign/Campaign creation/input start end date'), GlobalVariable.CPMapDV['cpPlannedEndDate'])
			WebUI.sendKeys(findTestObject('0. DV360/Campaign/Campaign creation/input start end date'), Keys.chord(Keys.ENTER))

		} else
		{
			KeywordUtil.logInfo("No end date")
		}

		WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Create button'))+"').click()", null)

		assert WebUI.waitForElementPresent(findTestObject("Object Repository/0. DV360/Campaign/Campaign creation/Create button spinner"), 10)
		assert WebUI.waitForElementNotPresent(findTestObject("Object Repository/0. DV360/Campaign/Campaign creation/Create button spinner"), 10)

		WebUI.delay(3)
		if (WebUI.waitForElementPresent(findTestObject("Object Repository/0. DV360/Campaign/Campaign creation/Error creation"), 5))
		{
			String errorMessage = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Error creation'))+"').textContent", null)
			if (errorMessage == "Error when saving the campaign.: Campaign name already exists for advertiser.")
			{
				KeywordUtil.markErrorAndStop("Error when saving the campaign.: Campaign name already exists for advertiser.")
			}
		}
		assert WebUI.waitForElementPresent(findTestObject("Object Repository/0. DV360/Campaign/Campaign creation/Check creation is ok"), 30) : "Error: Campaign is not created"

		return name
	}


	@Keyword
	def createInsertionOrder(String cp)
	{
		/**
		 * Create a new insertion order in DV360. We can choose a campaign where we want to create an IO. 
		 * If createCampaign() is called before, an IO can be created automatically after the campaign creation so no need to set a parameter in this function
		 * 
		 * @param cp
		 * 		campaign id or name
		 * @return name
		 *		name + timestamps
		 * 
		 */
		ArrayList<String> pacing = new ArrayList<String>()
		ArrayList<String> goal = new ArrayList<String>()
		String cpKpi = GlobalVariable.CPMapDV['cpKpi']
		if (cp.isEmpty())
		{
			WebUI.waitForElementPresent(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/New insertion order'), 10)
			WebUI.click(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/New insertion order'))
		} else
		{
			dv360SearchItem(cp)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion Order tab'), 30) : "Error: Insertion order tab is not visible"
			WebUI.click(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion Order tab'))
			assert WebUI.waitForElementPresent(findTestObject("Object Repository/0. DV360/Insertion Order/Insertion order creation/New insertion order"), 30) : "Error: Insertion order button is not present"
			WebUI.click(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/New insertion order'))
		}
		WebUI.click(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Real time bidding button'))
		WebUI.delay(2)
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Progress bar'), 30): "New campaign panel is not loaded"

		//Name - Mandatory
		String name = setNewName(GlobalVariable.OIMapDV['ioName'])

		//Status - Mandatory
		setNewStatus(GlobalVariable.OIMapDV['ioStatus'])

		//Budget type
		WebUI.click(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Budget type'))

		switch(GlobalVariable.OIMapDV['ioBudgetType'])
		{
			case 'EUR':
			case 'USD':
			case 'CHF':
			case 'GBP':
			case 'BRL':
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('0. DV360/Campaign/Campaign creation/List value'))+"').item(0).click()", null)
				KeywordUtil.logInfo("Budget type: EUR")
				break
			case 'Impressions':
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('0. DV360/Campaign/Campaign creation/List value'))+"').item(1).click()", null)
				KeywordUtil.logInfo("Budget type: Impressions")
				break
			default:
				KeywordUtil.markErrorAndStop("ioBudgetType set in the profil is not correct")

		}

		//Budget value
		WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Budget value'), GlobalVariable.OIMapDV['ioBudgetValue'])


		//Flight start date - Mandatory
		if (GlobalVariable.OIMapDV['ioBudgetStartDate'] != "")
		{
			WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/start and end date button'))+"').item(0).click()", null)
			Thread.sleep(500)
			WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/input start and end date'), GlobalVariable.OIMapDV['ioBudgetStartDate'])
			WebUI.sendKeys(findTestObject('0. DV360/Campaign/Campaign creation/input start end date'), Keys.chord(Keys.ENTER))

		} else
		{
			KeywordUtil.markErrorAndStop("You need to set a correct start date in the profile to create a campaign or an insertion order")
		}

		//Flight end date - Mandatory
		if (GlobalVariable.OIMapDV['ioBudgetEndtDate'] != "")
		{
			WebUI.delay(2)
			WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/start and end date button'))+"').item(1).click()", null)
			Thread.sleep(500)
			WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/input start and end date'), GlobalVariable.OIMapDV['ioBudgetEndDate'])
			WebUI.sendKeys(findTestObject('0. DV360/Campaign/Campaign creation/input start end date'), Keys.chord(Keys.ENTER))

		} else
		{
			KeywordUtil.markErrorAndStop("You need to set a correct end date in the profile to create a campaign or an insertion order")
		}

		//Pacing
		for (String value in (GlobalVariable.OIMapDV['ioPacing']).split(','))
		{
			pacing.add(value)
		}
		WebUI.click(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Pacing type'))
		switch(pacing[0])
		{
			case "Flight":
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/List value'))+"').item(0).click()", null)
				KeywordUtil.logInfo("Pacing Flight")
				WebUI.click(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Pacing value'))
				if (pacing[1] == "ASAP")
				{
					WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/List value'))+"').item(0).click()", null)
					KeywordUtil.logInfo("Value ASAP")
				} else if (pacing[1] == "Even")
				{
					WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/List value'))+"').item(1).click()", null)
					KeywordUtil.logInfo("Value Even")

				} else if (pacing[1] == "Ahead")
				{
					WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/List value'))+"').item(2).click()", null)
					KeywordUtil.logInfo("Value Ahead")
				}
				break

			case "Daily":
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/List value'))+"').item(1).click()", null)
				KeywordUtil.logInfo("Pacing Daily")
				WebUI.click(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Pacing value daily'))
				if (pacing[1] == "ASAP")
				{
					WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/List value'))+"').item(0).click()", null)
					KeywordUtil.logInfo("Value ASAP")

				} else if (pacing[1] == "Even")
				{
					WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/List value'))+"').item(1).click()", null)
					KeywordUtil.logInfo("Value Even")
				}
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Pacing amount'), pacing[2])
				break
		}

		//Goal - Mandatory if CP vidéo and if cpKPI is Other...
		if (GlobalVariable.OIMapDV['ioGoal'] != '')
		{
			WebUI.click(findTestObject('0. DV360/Insertion Order/Insertion order creation/Goal'))

			for (String value in (GlobalVariable.OIMapDV['ioGoal']).split(','))
			{
				goal.add(value)
			}
		}
		switch(goal[0])
		{

			case "CPM":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", "(CPM)")
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: CPM")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break

			case "Viewability%":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", "Viewability %")
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: Viewability%")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break

			case "CPIAVC":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: CPIAVC")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break

			case "CPA":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: CPA")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break

			case "CPC":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", "(CPC)")
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: CPC")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break

			case "CTR":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: CTR")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			case "CPE":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: CPE")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			case "CVRclick":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", "Click conversion")
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: CVRclick")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			case "CVRimpr":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", "Impression conversion")
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: CVRimpr")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			case "VCPM":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: VCPM")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			case "Audio":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: Audio")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			case "Video":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: Video")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			case "CPCL":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: CPCL")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			case "CPCV":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: CPCV")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			case "VTR":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: VTR")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			case "TOS10":
				TestObject goalType = changeSelectorXpath(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal type'), "goal", goal[0])
				assert WebUI.waitForElementVisible(goalType, 10)
				WebUI.click(goalType)
				KeywordUtil.logInfo("Goal: TOS10")
				WebUI.setText(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Goal value'), goal[1])
				break
			default:
				if (GlobalVariable.CPMapDV['cpCreaType'] == "Video" || cpKpi.contains("Other..."))
				{
					KeywordUtil.markErrorAndStop("You need to set a correct goal performance in the profile to create an OI")
				}
				else
				{
					KeywordUtil.logInfo("Goal performance not mandatory")
				}

		}

		assert WebUI.waitForElementPresent(findTestObject("Object Repository/0. DV360/Insertion Order/Insertion order creation/Create button"), 5)
		WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/0. DV360/Insertion Order/Insertion order creation/Create button'))+"').click()", null)

		assert WebUI.waitForElementPresent(findTestObject("Object Repository/0. DV360/Campaign/Campaign creation/Check creation is ok"), 30) : "Error: Insertion order is not created"

		return name
	}

	def setNewName(String Name)
	{
		/**
		 * Set a new name for a new CP or IO
		 * 
		 * @param Name
		 * 		Name of the object
		 * @return name
		 *		name + timestamps
		 */

		String name = Name + "_" + new Date().getTime()
		WebUI.setText(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Name'), name)
		return name
	}

	def setNewStatus(String Status)
	{
		/**
		 * 
		 * Set a new status for a new CP or IO
		 * @param Status
		 * 		Status of the object (Active, Pause or draft)
		 */
		WebUI.click(findTestObject('Object Repository/0. DV360/Campaign/Campaign creation/Status'))
		WebUI.delay(1)
		switch (Status)
		{
			case "Paused":
			case "Draft":
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('0. DV360/Campaign/Campaign creation/List value'))+"').item(1).click()", null)
				KeywordUtil.logInfo("Statut Pause")
				break
			case "Active":
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('0. DV360/Campaign/Campaign creation/List value'))+"').item(0).click()", null)
				KeywordUtil.logInfo("Statut Active")
				break
			default:
				KeywordUtil.markErrorAndStop("You need to set a correct status in the profile to create a campaign or an insertion order. Actual result: "+Status)
		}
	}
	@Keyword
	def archiveObject(ArrayList<String> ObjectID, String type) {
		/**
		 * This function search a list of object name on dv360 to archive it.
		 * Search the object name in the search bar
		 * take the first suggestion
		 * Navigate to the parent page
		 * Remove the active filter
		 * Search object by ID
		 * archive selected object
		 * Verify object is not visible anymore
		 *
		 * It handles Campaign, Insertion Order or Line
		 *
		 * @param ObjectID : the list of ID of object to archive
		 * @param type: the type of the object (CP, OI, LI)
		 */

		selenium = getSelenium()

		for (def elem in ObjectID) {
			if (type == 'LI' && selenium.isElementPresent(getSelector(findTestObject('Object Repository/0. DV360/Item List/New Line Item button'))) == false) {
				dv360SearchItem(elem)
				WebUI.delay(1)
				assert WebUI.waitForElementVisible(findTestObject('0. DV360/OI link'), 30)== true
				WebUI.click(findTestObject('0. DV360/OI link'))
			} else if (type == 'OI' && selenium.isElementPresent(getSelector(findTestObject('Object Repository/0. DV360/Item List/New Insertion Order button'))) == false) {
				if (GlobalVariable.OIMapDV["archiveOI"])
				{
					dv360SearchItem(elem)
					WebUI.delay(1)
					assert WebUI.waitForElementVisible(findTestObject('0. DV360/Overview link'), 30) == true
					WebUI.click(findTestObject('0. DV360/Overview link'))
					WebUI.verifyElementPresent(findTestObject('0. DV360/Insertion Order/Insertion Order tab'), 3)
					assert WebUI.waitForElementVisible(findTestObject('0. DV360/Insertion Order/Insertion Order tab'), 30) == true
					WebUI.click(findTestObject('0. DV360/Insertion Order/Insertion Order tab'))
				}
				else
				{
					KeywordUtil.logInfo("We don't archive the OI")
				}
			} else if (type == 'CP' && selenium.isElementPresent(getSelector(findTestObject('Object Repository/0. DV360/Item List/New Campaign button'))) == false) {
				dv360SearchItem(elem)
				assert WebUI.waitForElementVisible(findTestObject('0. DV360/Campaign link'), 30)== true
				WebUI.click(findTestObject('0. DV360/Advertiser link'))
			}
			WebUI.delay(1)
			assert WebUI.waitForElementVisible(findTestObject('0. DV360/Item List/Filter input'), 10) == true
			if(selenium.isElementPresent(getSelector(findTestObject('0. DV360/Item List/Remove Filter button')))) {
				WebUI.click(findTestObject('0. DV360/Item List/Remove Filter button'))
				WebUI.delay(1)
				assert WebUI.verifyElementNotPresent(findTestObject('0. DV360/Loading bar'), 10) == true
			}

			WebUI.setText(findTestObject('0. DV360/Item List/Filter input'), elem)
			WebUI.sendKeys(findTestObject('0. DV360/Item List/Filter input'), Keys.chord(Keys.ENTER))
			if (type == 'CP') {
				Thread.sleep(500)
				assert WebUI.verifyElementNotPresent(findTestObject('0. DV360/Filter loader'), 10)
			} else {
				Thread.sleep(500)
				assert WebUI.verifyElementNotPresent(findTestObject('0. DV360/Loading bar'), 10)
			}
			assert WebUI.waitForElementVisible(findTestObject('0. DV360/Item List/First Checkbox'), 10) == true : elem + " object was not found in the list"
			WebUI.click(findTestObject('0. DV360/Item List/First Checkbox'))

			assert WebUI.waitForElementVisible(findTestObject('Object Repository/0. DV360/Item List/Action Menu button'), 5) == true
			WebUI.click(findTestObject('Object Repository/0. DV360/Item List/Action Menu button'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/0. DV360/Item List/Action menu/Archive button'), 5) == true
			WebUI.click(findTestObject('Object Repository/0. DV360/Item List/Action Menu/Archive button'))
			if (type != 'CP') {
				WebUI.waitForElementVisible(findTestObject('Object Repository/0. DV360/Item List/Action Menu/OK, archive button'), 10)
				WebUI.click(findTestObject('Object Repository/0. DV360/Item List/Action Menu/OK, archive button'))
			}
			Thread.sleep(500)
			assert WebUI.verifyElementNotPresent(findTestObject('0. DV360/Filter loader'), 15)
			assert WebUI.verifyElementNotPresent(findTestObject('0. DV360/Item List/List First Element checkbox'), 10) == true
			KeywordUtil.logInfo(type + " " + elem +" has been archived")
		}
		WebUI.refresh()
	}
}