package com.reusableComponents

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.Instant

import org.openqa.selenium.Keys

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable
import com.kms.katalon.core.webui.driver.DriverFactory

public class Grafana extends Common
{
	@Keyword
	public def loginToGrafana(boolean newTab, boolean alreadyLoggedIn)
	{
		/**
		 * Log in to Grafana using googleID and googlePassword global variables
		 * 
		 * @param newTab		Open Grafana in a new tab if set to "true", else open in current tab
		 * @param alreadyLoggedIn		true if we are already logged in to google, else otherwise
		 */		

		if(GlobalVariable.grafana == true)
		{
			String environment = ""
			if(GlobalVariable.env.contains("uat") || GlobalVariable.env.contains("dev")) environment = "uat"

			selenium = getSelenium()
			if(newTab == true)
			{
				int currentWindowIndex = WebUI.getWindowIndex()
				selenium.openWindow("","")
				WebUI.switchToWindowIndex(currentWindowIndex + 1)
			}

			if(environment == "dev" || environment == "uat") WebUI.navigateToUrl("https://grafana.dev.jellyfish.net/login")
			else if(environment == "prod" || environment == "") WebUI.navigateToUrl("https://grafana.jellyfish.net/login")
			else KeywordUtil.markErrorAndStop('Environement '+ environment +' is not correct. Possible choices : "dev" / "uat" / "prod" / ""')
			WebUI.waitForPageLoad(30)
			assert WebUI.waitForElementVisible(findTestObject('0. Grafana/Google Sign-in button'), 60) : "Sign-in button is not visible after 60 seconds"
			Thread.sleep(1000)
			WebUI.click(findTestObject('0. Grafana/Google Sign-in button'))
			Thread.sleep(1000)

			if(selenium.isElementPresent(getSelector(findTestObject('0. Login Google/2FA header'))) && WebUI.getUrl().contains("signin"))
				Googleauthentificator()
			else if(selenium.isElementPresent(getSelector(findTestObject('0. Login Google/2FA header'))) && !WebUI.getUrl().contains("signin"))
				WebUI.click(findTestObject('0. Login Google/First mail'))
		} else
		{
			KeywordUtil.logInfo("Grafana skipped")
		}
	}

	@Keyword
	def checkAutoPauseRuleStatus(String ruleID, int attempts)
	{
		/**
		 * Check a rule status
		 * 
		 * @param ruleID		ID of the rule to check
		 * @param attempts		Maximum attempts to check for the rule status (multiply by 5 seconds)
		 */
		if(GlobalVariable.grafana == true)
		{
			String environment = ""
			if(GlobalVariable.env.contains("uat") || GlobalVariable.env.contains("dev")) environment = "uat"

			String grafanaRuleStatusUrl = "https://grafana.dev.jellyfish.net/d/wsBw1ILMz/2-autopause-rule-status?orgId=1&refresh=10m&from=now-15m&to=now&var-env="+environment
			if(environment == "prod" || environment == "") grafanaRuleStatusUrl = grafanaRuleStatusUrl.replaceFirst("\\.dev", "").replaceFirst("&var-env="+environment, "")
			WebUI.navigateToUrl(grafanaRuleStatusUrl)
			WebUI.waitForPageLoad(30)
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/0. Grafana/First panel'), 30) : "The first panel is not present after 30 seconds"

			boolean isRulesPresent = WebUI.getText(findTestObject('Object Repository/0. Grafana/First panel')) != "No data"
			while(!isRulesPresent && attempts > 0)
			{
				WebUI.click(findTestObject('0. Grafana/Refresh button'))
				isRulesPresent = WebUI.getText(findTestObject('Object Repository/0. Grafana/First panel')) != "No data"
				attempts--
				Thread.sleep(5000)
			}

			assert WebUI.getText(findTestObject('Object Repository/0. Grafana/First panel')) != "No data" : "No rule is displayed after the timeout. URL : " + grafanaRuleStatusUrl

			boolean isMyRulePresent = selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/0. Grafana/Filter ID title'), "1", ruleID)))
			while(!isMyRulePresent && attempts > 0)
			{
				WebUI.click(findTestObject('0. Grafana/Refresh button'))
				Thread.sleep(1000)
				WebUI.click(findTestObject('Object Repository/0. Grafana/First Column Filter button'))
				Thread.sleep(1000)
				WebUI.sendKeys(findTestObject('Object Repository/0. Grafana/Filter Search input'), ruleID)
				Thread.sleep(1000)
				isMyRulePresent = selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/0. Grafana/Filter ID title'), "1", ruleID)))
				attempts--
				Thread.sleep(2000)
			}

			if(!selenium.isElementPresent(getSelector(findTestObject('0. Grafana/First Rule ID'))))
			{
				return
			} else
			{
				WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(changeSelector(findTestObject('Object Repository/0. Grafana/Filter ID title'), "1", ruleID))+"').click()", null)
				Thread.sleep(1000)
				WebUI.click(findTestObject('Object Repository/0. Grafana/Filter OK button'))
				Thread.sleep(1000)
				assert WebUI.getText(findTestObject('0. Grafana/First Rule ID')) == ruleID : "Rule displayed is not correct, there may be an issue with the URL, or the rule is not present on Grafana. URL : " + grafanaRuleStatusUrl
			}
			String ruleStatus = WebUI.getText(findTestObject('0. Grafana/First Rule Status'))
			attempts = 48 // +4 mins waiting for status
			while(ruleStatus == "SPENDING" && attempts > 0)
			{
				WebUI.click(findTestObject('0. Grafana/Refresh button'))
				Thread.sleep(5000)
				ruleStatus = WebUI.getText(findTestObject('0. Grafana/First Rule Status'))
				attempts--
			}

			KeywordUtil.logInfo("Rule status on Grafana : " + ruleStatus)
		} else {
			KeywordUtil.logInfo("Grafana skipped")
		}
	}
}
