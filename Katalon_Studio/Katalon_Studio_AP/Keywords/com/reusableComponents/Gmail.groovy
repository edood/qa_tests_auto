package com.reusableComponents

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.Instant

import org.openqa.selenium.Keys

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import com.kms.katalon.core.webui.driver.DriverFactory
import com.reusableComponents.Grafana

public class Gmail extends Common
{

	@Keyword
	def goToGmail()
	{
		/**
		 * Navigate to Gmail
		 */
		KeywordUtil.logInfo("Navigating to Gmail")
		WebUI.navigateToUrl('https://accounts.google.com/signin/v2/identifier?service=mail')
		WebUI.waitForPageLoad(30)
		WebUI.delay(2)
	}

	@Keyword
	def logInToGmail(boolean newTab)
	{
		/**
		 * Log in to Gmail
		 * 
		 * @param newTab	Open Gmail in a new tab if set to "true", else open in current tab
		 */

		selenium = getSelenium()
		if(newTab == true)
			openNewTab()
		WebUI.navigateToUrl('https://accounts.google.com/signin/v2/identifier?service=mail')
		WebUI.waitForPageLoad(30)
		Thread.sleep(1000)
		WebUI.waitForPageLoad(30)
		Thread.sleep(1000)
		if(selenium.isElementPresent(getSelector(findTestObject('0. Gmail/Gmail logo'))) == false)
		{
			WebUI.setText(findTestObject('0. Login Google/Login type email'), GlobalVariable.googleID)
			WebUI.sendKeys(findTestObject('0. Login Google/Login type email'), Keys.chord(Keys.ENTER))
			Thread.sleep(500)
			assert WebUI.waitForElementNotPresent(findTestObject('0. Login Google/Loading progress'), 15) : "Loading bar is still present after 15 seconds"
			assert WebUI.waitForElementVisible(findTestObject('0. Login Google/Password type password'), 15) : "Password input is not visible after 15 seconds"
			WebUI.setEncryptedText(findTestObject('0. Login Google/Password type password'), GlobalVariable.googlePassword)
			WebUI.sendKeys(findTestObject('0. Login Google/Password type password'), Keys.chord(Keys.ENTER))
			Thread.sleep(500)
			assert WebUI.waitForElementNotPresent(findTestObject('0. Login Google/Loading progress'), 15) : "Loading bar is still present after 15 seconds"
			WebUI.waitForPageLoad(30)

			if(selenium.isElementPresent(getSelector(findTestObject('0. Login Google/2FA header'))))
				Googleauthentificator()
		}
		else
		{
			KeywordUtil.logInfo("Already logged in")
		}
	}

	@Keyword
	def waitForNewEmail(String ruleName, boolean warningEmail, String epochSecond, int attempts, boolean expectNoMail = false)
	{
		/**
		 * Wait for a new email in Gmail
		 * 
		 * @param ruleName					Rule name
		 * @param warningEmail				true if warning email option is checked for the awaited rule, false otherwise
		 * @param epochSecond				Time from which we start the mail check, in epoch format
		 * @param attempts					Maximum attempts to check for the email (multiply by 5 seconds)
		 * @param expectNoMail				true to verify that no mail is coming during the attempts, false to wait for the email title
		 */

		String expectedEmailTitle = ""
		String emailurl = ""
		if (warningEmail == false)
		{
			expectedEmailTitle = 'Delivery paused for rule ' + ruleName
			emailurl = "https://mail.google.com/mail/u/0/#search/from%3A(autopause%40jellyfish.net)+subject%3A("+ expectedEmailTitle.replaceAll(" ", "+") +")+after%3A"
		} else if (warningEmail == true)
		{
			expectedEmailTitle = 'Spend warning - Autopause ' + ruleName
			emailurl = "https://mail.google.com/mail/u/0/#search/from%3A(autopause%40jellyfish.net)+subject%3A("+ expectedEmailTitle.replaceAll(" ", "+") +")+after%3A"
		}
		KeywordUtil.logInfo('rule url = '+ emailurl)

		WebUI.navigateToUrl(emailurl + epochSecond)
		while(WebUI.getAttribute(findTestObject('Object Repository/0. Gmail/Search bar input'), 'value') == "")
			WebUI.delay(1)
		assert WebUI.waitForElementVisible(findTestObject('0. Gmail/Refresh button'), 45) : "The refresh button is not visible after 45 seconds"
		String firstMailTitlePresentSelector = getSelector(findTestObject('0. Gmail/Mail 1 title'))
		boolean isfirstMailTitlePresent = selenium.isElementPresent(firstMailTitlePresentSelector)
		String firstMailTitle = ""

		while(isfirstMailTitlePresent == false && attempts > 0 && expectNoMail == false)
		{
			WebUI.focus(findTestObject('0. Gmail/Refresh button'))
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('0. Gmail/Refresh button')).replaceAll('"', '\\"')+"').click()", null)

			Thread.sleep(5000)
			isfirstMailTitlePresent = selenium.isElementPresent(firstMailTitlePresentSelector)
			attempts--
		}

		if(expectNoMail == true)
		{
			Thread.sleep(attempts * 5000)
			assert selenium.isElementPresent(firstMailTitlePresentSelector) == false : "A mail was received when it should not have been."
		}
		else
		{
			assert firstMailTitle != null : "No mail was received"
			firstMailTitle = WebUI.getText(findTestObject('0. Gmail/Mail 1 Title'))
			assert expectedEmailTitle == firstMailTitle : "Expected mail title not found. Expected : " + expectedEmailTitle + ", First mail title : " + firstMailTitle
			WebUI.focus(findTestObject('0. Gmail/Mail 1 Title'))
			Thread.sleep(1000)
		}
	}

	@Keyword
	def checkMailContains(String expectedEmailContainedText)
	{
		/**
		 * Check that the content of the displayed mail contains a specific String
		 *
		 * @param expectedEmailContainedText		String to check in the mail
		 */
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/0. Gmail/Mail content'), 30) : "Mail is not present after 30 seconds"
		String mailText = WebUI.getText(findTestObject('Object Repository/0. Gmail/Mail content'))
		assert mailText.contains(expectedEmailContainedText) : "Mail does not contain expected text. Expected : " + expectedEmailContainedText + " \n\n Displayed : " + mailText
		Thread.sleep(1250)
	}

	@Keyword
	def checkMailContent(String expectedEmailContentRegex)
	{
		/**
		 * Check the content of the displayed mail from a regex String
		 * 
		 * @param expectedEmailContentRegex		Regex of the email content to check
		 * 
		 * @return spendValue
		 */
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/0. Gmail/Mail content'), 30) : "Mail is not present after 30 seconds"
		String mailText = WebUI.getText(findTestObject('Object Repository/0. Gmail/Mail content'))
		assert mailText.matches(expectedEmailContentRegex) : "Mail content does not match expected content regex. Expected : " + expectedEmailContentRegex + " \n\n Displayed : " + mailText
		Thread.sleep(250)
		def match = (mailText =~ /${expectedEmailContentRegex}/).findAll()
		String spendValue = match[0][1]
		return spendValue
	}

	@Keyword
	ArrayList<String> checkOIlist(ArrayList<String> list)
	{
		/**
		 * this function verify that the OI list in the mail belong to the selected advertiser
		 * 
		 * @param list
		 * The list of selected advertiser
		 * 
		 * @return emailOIlist
		 * The list of OI ID given by the received email
		 */
		boolean found = false
		ArrayList<String> emailOIlist = []
		ArrayList<String> split = WebUI.getText(findTestObject('0. Gmail/Autopause Email OI list')).split('\\)')
		for (String ios in split)
		{
			if (split.size() > 1 && ios == split[split.size() - 1])
				break
			found = false
			ArrayList<String> splitted = ios.split(" ")
			String advid = splitted[splitted.size() - 1]
			for (String adv in list)
			{
				if (adv.contains(advid) == true)
				{
					found = true
					for (String id in splitted)
					{
						if (id.matches("\\d+,") == true)
						{
							id = id.replace(',', '')
							emailOIlist.add(id)
							break
						}
					}
					break
				}
			}
			assert found == true : "OI belonging to the advertiser id " + advid + " are not expected in the email"
			WebUI.delay(1)
		}
		return emailOIlist
	}

	@Keyword
	ArrayList<String> VerifyEmail(String ruleName, String mailformat, boolean warningemail=false, ArrayList<String> list, int minute, int attempts, String epochSecond)
	{
		/**
		 * this function verify the email
		 * @param ruleName
		 * the name of the rule to be verify
		 * 
		 * @param mailformat
		 * the expected regex for the mail format
		 * 
		 * @param warningemail
		 * the boolean to determine if the rule is a warning or not
		 * 
		 * @param list
		 * the list of advertiser the rule is applied on
		 * 
		 * @param minute
		 * The minute for the email timer wait
		 * 
		 * @param attempts
		 * the number of attempts to achieve a pause
		 * 
		 * @param epochsecond
		 * the date of the email timer wait in epoch second format
		 * 
		 * @return emailOIlist
		 * the list of OI ID in the mail
		 */

		String ruleID = WebUI.getText(findTestObject('Object Repository/2. Schedule List/1. Table/IDs'))

		logInToGmail(true)

		// Max wait is 11 mins to wait for the grafana status to appear + 4mins of waiting the status of the rule (total 15mins)
		if(GlobalVariable.grafana == true)
		{
			Grafana grafa = new Grafana()
			attempts = getPauseAttempts(minute, 11, 5)
			grafa.loginToGrafana(true, true)
			grafa.checkAutoPauseRuleStatus(ruleID, attempts)
			WebUI.switchToWindowIndex(1)
		}

		waitForNewEmail(ruleName, warningemail, epochSecond, attempts)

		WebUI.click(findTestObject('Object Repository/0. Gmail/Mail 1 Title'))
		WebUI.delay(1)
		checkMailContent(mailformat)

		ArrayList<String> emailOIlist = []
		if (GlobalVariable.ruleCreate["Apply on"] == "Insertion Order")
		{
			for (String ios in (GlobalVariable.ruleCreate['Insertion Orders']).split(','))
			{
				checkMailContains(ios)
				emailOIlist.add(ios)
				WebUI.delay(1)
			}
		}
		else if (GlobalVariable.ruleCreate["Apply on"] == "Advertiser")
			emailOIlist = checkOIlist(list)

		return emailOIlist
	}
}
