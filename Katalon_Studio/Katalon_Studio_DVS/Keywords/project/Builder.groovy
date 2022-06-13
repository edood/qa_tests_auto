package project
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.Month as Month
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.jboss.aerogear.security.otp.Totp


import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testdata.CSVData
import com.kms.katalon.core.testdata.DBData
import com.kms.katalon.core.testdata.reader.CSVSeparator
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.Common
import com.reusableComponents.DV360
import com.reusableComponents.HighlightElement
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import groovy.time.TimeCategory
import internal.GlobalVariable as GlobalVariable

class Builder extends Common
{
	Keys control

	public Builder()
	{
		tmp.addProperty('css', ConditionType.EQUALS, '')
		String osName = System.properties['os.name'].toLowerCase()
		if (osName.contains('windows'))
		{
			control = Keys.CONTROL
		} else if (osName.contains("mac os x") || osName.contains("darwin") || osName.contains("osx"))
		{
			control = Keys.COMMAND
		} else
		{
			control = Keys.CONTROL
		}
	}

	@Keyword
	def xclick(TestObject obj, String type)
	{
		/**
		 * this function clicks and verifies the output for click fields
		 *
		 * @param obj
		 * the objet to click
		 *
		 * @param type
		 * Determine if the type of click (checkbox / calendar)
		 *
		 */
		switch(type)
		{
			case 'checkbox':
				WebUI.click(obj)
				String before = WebUI.getAttribute(obj, 'checked')
				KeywordUtil.logInfo('input checkbox = ' + before)
				WebUI.delay(1)
				WebUI.click(findTestObject('6. Objects/Object Header'))
				String after = WebUI.getAttribute(obj, 'checked')
				KeywordUtil.logInfo('ouput = ' + after)
				assert after == before : "Saio has changed on its own"
				break
			case 'calendar':
				WebUI.click(obj)
				KeywordUtil.logInfo('obj = ' + obj.getSelectorCollection().get(SelectorMethod.CSS))
				String before = WebUI.getAttribute(obj, 'value')
				KeywordUtil.logInfo('input calendar = ' + before)
				WebUI.delay(1)
				WebUI.click(findTestObject('6. Objects/Object Header'))
				String after = WebUI.getAttribute(obj, 'value')
				KeywordUtil.logInfo('ouput = ' + after)
				assert before == after : "date has changed on its own"
				break
		}
	}

	@Keyword
	def xsetText(TestObject obj, String text, String modificator = '', boolean inputmethod = true)
	{
		/**
		 * this function sets and verifies the output for input fields
		 *
		 * @param obj
		 * the input to set
		 *
		 * @param text
		 * the value to set
		 *
		 * @param modificator
		 * Determine the method to use to set and verify the input ('' = raw / 'A2' = rounded 2 decimal)
		 *
		 * @param inputmethod
		 * determine the method to use to set (true = setText / false = sendkeys)
		 */
		switch (modificator)
		{
			case '':
				inputmethod == true ? WebUI.setText(obj, text) : WebUI.sendKeys(obj, text)
				WebUI.click(findTestObject('6. Objects/Object Header'))
				WebUI.delay(1)
				String temporaire = WebUI.getAttribute(obj, 'value')
				KeywordUtil.logInfo('input set : ' + text)
				KeywordUtil.logInfo('output : ' + temporaire)
				if ( text == Keys.chord(Keys.HOME, Keys.SHIFT, Keys.END, Keys.DELETE) || text == Keys.chord(Keys.SHIFT, Keys.ARROW_UP, Keys.DELETE, Keys.ENTER))
					assert temporaire == '' : "Input has changed on his own: " + temporaire + " is different than " + ''
				else
				{
					if (text.contains(Keys.chord(Keys.SHIFT, Keys.ARROW_UP)) == true)
					{
						text = text.replace(Keys.chord(Keys.SHIFT, Keys.ARROW_UP), '')
						KeywordUtil.logInfo('text replaced = ' + text)
					}
					else if (text == Keys.chord(Keys.ENTER))
						text = temporaire
					else
						KeywordUtil.logInfo('No need to replace: ' + text)
					assert temporaire == text : "Input has changed on his own: " + temporaire + " is different than " + text
				}
				break
			case 'A2':
				inputmethod == true ? WebUI.setText(obj, text) : WebUI.sendKeys(obj, text)
				WebUI.click(findTestObject('6. Objects/Object Header'))
				WebUI.delay(1)
				Double temporaire2 = -1
				String tmp = WebUI.getAttribute(obj, 'value')
				KeywordUtil.logInfo('input set : ' + text)
				KeywordUtil.logInfo('output : ' + tmp)
				if (text != '')
					temporaire2 = Double.parseDouble(tmp)
				if ( text == Keys.chord(Keys.HOME, Keys.SHIFT, Keys.END, Keys.DELETE) || text == Keys.chord(Keys.SHIFT, Keys.ARROW_UP, Keys.DELETE, Keys.ENTER))
					assert '' == temporaire2 : "Input has changed on his own: " + '' + " is different than " + temporaire2
				else
				{
					if (text == '')
					{
						assert '' == tmp : "Input has changed on his own: " + '' + " is different than " + tmp
						return
					}
					else if (text.contains(Keys.chord(Keys.SHIFT, Keys.ARROW_UP)) == true)
					{
						text = text.replace(Keys.chord(Keys.SHIFT, Keys.ARROW_UP), '')
						KeywordUtil.logInfo('text round replace = ' + text)
					}
					else
						KeywordUtil.logInfo('No need to replace: ' + text)

					Double temporaire1 = Double.parseDouble(text).round(2)
					assert temporaire1 == temporaire2 : "Input has changed on his own: " + temporaire1 + " is different than " + temporaire2
				}
				break
		}
	}

	@Keyword
	def xsetOption(TestObject obj, def option, String modificator = 'value', boolean regex = false)
	{
		/**
		 * this function click and verifies the output for dropdownlist
		 * 
		 * @param obj
		 * the dropdownlist to select
		 * 
		 * @param option
		 * the value, index or label to be selected
		 * 
		 * @param modificator
		 * Determine the method of select to use
		 * 
		 * @param regex
		 * determine if the option is a regex
		 */
		switch (modificator)
		{
			case 'value':
				WebUI.selectOptionByValue(obj, option, regex)
				WebUI.click(findTestObject('6. Objects/Object Header'))
				String tmp = WebUI.getAttribute(obj, 'value')
				KeywordUtil.logInfo('input is : ' + option)
				KeywordUtil.logInfo('output : ' + tmp)
				assert tmp == option : "Dropdown has changed on its own: " + tmp + " is different than " + option
				break
			case 'label':
				WebUI.selectOptionByLabel(obj, option, regex)
				WebUI.click(findTestObject('6. Objects/Object Header'))
				String tmp2 = WebUI.getAttribute(obj, 'value')
				KeywordUtil.logInfo('input is : ' + option)
				KeywordUtil.logInfo('output : ' + tmp2)
				assert option == tmp2 : "Dropdown has changed on its own: " + option + " is different than " + tmp2
				break
			case 'index':
				WebUI.selectOptionByIndex(obj, option)
				String tmp1 = WebUI.getAttribute(obj, 'value')
				WebUI.click(findTestObject('6. Objects/Object Header'))
				String tmp2 = WebUI.getAttribute(obj, 'value')
				KeywordUtil.logInfo('input is : ' + option)
				KeywordUtil.logInfo('index set : ' + tmp1)
				KeywordUtil.logInfo('output : ' + tmp2)
				assert tmp1 == tmp2 : "Dropdown has changed on its own: " + tmp1 + " is different than " + tmp2
				break
		}
	}


	@Keyword
	def hotspot(boolean display)
	{

		/**
		 * Check if welcome message is correct and click on Yes or No accroding to display variable
		 * 
		 * @param display
		 * 		True or False
		 * 
		 */

		if (WebUI.verifyElementVisible(findTestObject('2. Homepage/Hotspot popup')) == true)
		{
			String hotspotText = "Welcome to the Builder. With it you will be able to create your setup in few clicks."
			String hotspotText1 = "Would you like to see a tour of what you can do?"
			KeywordUtil.logInfo('Hotspot popup displayed')
			String hotspotTextDisplayed = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspot text message'))+"').textContent", null)
			assert hotspotTextDisplayed.contains(hotspotText): "Hotspot text does not contain" + hotspotText + " ; Displayed text : " + hotspotTextDisplayed
			assert hotspotTextDisplayed.contains(hotspotText1): "Hotspot text does not contain" + hotspotText1 + " ; Displayed text : " + hotspotTextDisplayed

			if (display == true)
				WebUI.click(findTestObject('2. Homepage/Hotspot popup yes button'))
			else if (display == false)
				WebUI.click(findTestObject('2. Homepage/Hotspot popup no button'))
		}
		else
			KeywordUtil.logInfo('Hotspot popup not displayed')
	}

	@Keyword
	public String Start(boolean hotspotBool)
	{
		// Open DVS for specified environment
		String baseUrl = 'https://' + GlobalVariable.env

		WebUI.openBrowser(baseUrl)
		WebUI.maximizeWindow()

		selenium = getSelenium()
		// Login to DVS
		login(GlobalVariable.googleID, GlobalVariable.googlePassword, GlobalVariable.checkHref, true)

		assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Grid'), 60) == true : "Loader still present after 60 second"

		// hide or display hotspot
		hotspot(hotspotBool)

		assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Grid'), 60) == true : "Loader still present after 60 second"
		return baseUrl
	}

	@Keyword
	def gotoBuilder()
	{
		/**
		 * this function redirect to builder and select the advertiser
		 */
		WebUI.navigateToUrl('https://' + GlobalVariable.env)
		assert WebUI.waitForElementPresent(findTestObject('2. Homepage/Advertiser Input'), 30)
		assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Grid'), 60) == true

		// Select advertiser
		selectadvertiser(GlobalVariable.advertiserName)
		Thread.sleep(500)
		assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Grid'), 60) == true
	}

	@Keyword
	def refreshAdvertiser()
	{
		if(GlobalVariable.refreshAdvertiser == true)
		{
			Thread.sleep(500)
			assert WebUI.waitForElementNotPresent(findTestObject('2. Homepage/Advertiser loader'), 30) == true
			WebUI.click(findTestObject('Object Repository/2. Homepage/Refresh advertiser button'))
			WebUI.waitForElementVisible(findTestObject('Object Repository/2. Homepage/Refresh Advertiser Confirmation button'), 30)
			WebUI.click(findTestObject('Object Repository/2. Homepage/Refresh Advertiser Confirmation button'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/2. Homepage/Refresh Advertiser Loading title'), 120) == true : "Failed to start refreshing the advertiser after 120 seconds"
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/2. Homepage/Refresh Advertiser Loading title'), 240) == true : "Advertiser has not finished refreshing after 240 seconds"
		} else if (GlobalVariable.refreshAdvertiser == false)
		{
			KeywordUtil.logInfo("Skipping advertiser refresh.")
		} else
		{
			KeywordUtil.markErrorAndStop("Global variable 'refreshAdvertiser' is not set.")
		}
	}

	@Keyword
	def Googleauthentificator()
	{
		/**
		 * This function log in google with the 2FA google authentificator
		 * the global variable GoogleAuthentificatorKey is requiered
		 */

		WebUI.maximizeWindow()
		assert WebUI.waitForElementVisible(findTestObject('1. Login/GoogleSignIn/2FA header'), 30) : "2FA popup not visible"
		KeywordUtil.logInfo('2FA header = ' + WebUI.getText(findTestObject('1. Login/GoogleSignIn/2FA header')))
		assert WebUI.getText(findTestObject('1. Login/GoogleSignIn/2FA header')) == 'Validation en deux étapes' : "Connection failed"
		WebUI.delay(3)
		selenium = getSelenium()
		if (selenium.isElementPresent(getSelector(findTestObject('1. Login/GoogleSignIn/Try another way span'))) == true)
		{
			String result = WebUI.getText(findTestObject('1. Login/GoogleSignIn/Try another way span'))
			if (result == 'Essayer une autre méthode')
			{
				KeywordUtil.logInfo('authentificator not visible')
				assert WebUI.verifyElementVisible(findTestObject('1. Login/GoogleSignIn/Try another way'))
				WebUI.click(findTestObject('1. Login/GoogleSignIn/Try another way'))
				assert WebUI.waitForElementVisible(findTestObject('1. Login/GoogleSignIn/Google Authentificator'), 30) : "The connection google authentificator is not available for this account"
			}
			KeywordUtil.logInfo('result = ' + result)
		}
		ArrayList<WebElement> ConnectionMethod = driver.findElements(By.cssSelector(findTestObject('1. Login/GoogleSignIn/other authentification method').getSelectorCollection().get(SelectorMethod.CSS)))
		for (def elem in ConnectionMethod)
		{
			if (elem.getText().contains('Google Authenticator') == true)
				elem.click()
		}
		assert WebUI.waitForElementVisible(findTestObject('1. Login/GoogleSignIn/input tel'), 30) : "Input to submit code is not visible"
		try
		{
			Totp auth = new Totp(GlobalVariable.GoogleAuthentificatorKey)
			String code = auth.now()
			KeywordUtil.logInfo('code to submit = ' + code)
			WebUI.sendKeys(findTestObject('1. Login/GoogleSignIn/input tel'), code)
			WebUI.click(findTestObject('1. Login/GoogleSignIn/Totp next'))
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

		assert WebUI.waitForElementVisible(findTestObject('1. Login/GoogleSignIn/popup email'), 30) : "Email popup is not visible"
		WebUI.setText(findTestObject('1. Login/Login type email'), id)
		WebUI.click(findTestObject('1. Login/Login Next button'))
		WebUI.delay(3)
		WebUI.setEncryptedText(findTestObject('1. Login/Password type password'), pwd)
		WebUI.click(findTestObject('1. Login/GoogleSignIn/Password next button'))
		WebUI.delay(5)
	}

	@Keyword
	def login(String id, String pwd, boolean tip, boolean firstconnection = true)
	{
		/**
		 * this function log the user in the builder
		 * @param id
		 * The id of the user
		 * @param pwd
		 * The encrypted password of the user
		 * @param tip
		 * Define if the tip on login page should be verified
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
			assert WebUI.verifyElementAttributeValue(findTestObject('1. Login/here link'), 'href', 'mailto:authentifier@jellyfish.com?subject=Add%20my%20email%20to%20Authentifier&body=Hello,%0D%0A%0D%0Aplease%20add%20me%20to%20the%20Authentifier%20in%20order%20to%20access%20%22Builder%22.%0D%0A%0D%0AKind%20Regards,', 5) : "Email link is not correct"
		}
		if (firstconnection == true)
		{
			driver = DriverFactory.getWebDriver()
			String parentWindow = driver.getWindowHandle()
			WebUI.click(findTestObject('1. Login/GoogleSignIn/button'))
			Set<String> handles = driver.getWindowHandles()
			for(String windowHandle  : handles)
			{
				if(!windowHandle.equals(parentWindow))
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
		else if (firstconnection == false)
			WebUI.click(findTestObject('1. Login/GoogleSignIn/button'))

		assert WebUI.waitForElementVisible(findTestObject('2. Homepage/Advertiser Input'), 30) : "Login failed"
	}

	@Keyword
	def selectadvertiser(String advertiser)
	{
		/**
		 * this function select an advertiser
		 * @param advertiser
		 * the name of the advertiser to be selected
		 */

		assert WebUI.waitForElementVisible(findTestObject('2. Homepage/Advertiser Input'), 60) == true : "Advertiser input not visible after 60 sec"
		WebUI.sendKeys(findTestObject('2. Homepage/Advertiser Input'), advertiser)
		assert WebUI.waitForElementVisible(findTestObject('2. Homepage/Advertiser Input Autocomplete'), 60) == true : "Advertiser input not displayed after 60 second"
		WebUI.click(findTestObject('2. Homepage/Advertiser Input Autocomplete'))
		Thread.sleep(500)
		assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Grid'), 60) == true : "Loading not finished after 60 second"
	}
	@Keyword
	def logout()
	{
		/**
		 * this function log the user out of the builder
		 */
		WebUI.click(findTestObject('Object Repository/1. Login/user menu'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/1. Login/Logout'), 5)
		WebUI.click(findTestObject('Object Repository/1. Login/Logout'))
	}

	@Keyword
	def load(String objectType, String CP, String OI, String LI)
	{
		/**
		 * this function load an object accord to the items[] in the profile
		 * 
		 * @param objectType
		 * The type of the object to load (CP, OI, LI)
		 * 
		 * @param CP
		 * The name/id of the campaign to load
		 * 
		 * @param OI
		 * The name/id of the OI to load
		 * 
		 * @param LI
		 * The name/id of the LI to load
		 */


		if (CP == '') CP = GlobalVariable.items[0]
		if (OI == '') OI = GlobalVariable.items[1]
		if (LI == '') LI = GlobalVariable.items[2]

		selenium = getSelenium()
		boolean isEditorOpened = selenium.isElementPresent(getSelector(findTestObject('Object Repository/5. Editor/Builder Close Editor button')))
		if(isEditorOpened) WebUI.click(findTestObject('Object Repository/5. Editor/Builder Close Editor button'))
		Thread.sleep(500)
		assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Spinner'), 30)
		assert WebUI.waitForElementVisible(findTestObject('3. Loader/Search bar campaign'), 30) == true
		WebUI.clearText(findTestObject('3. Loader/Search bar campaign'))
		xsetText(findTestObject('3. Loader/Search bar campaign'), CP)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Loader/Loader First CP'), 60) == true
		WebUI.click(findTestObject('Object Repository/3. Loader/Loader First CP'))
		if (objectType == "CP")
		{
			assert WebUI.waitForElementClickable(findTestObject('Object Repository/3. Loader/Load Your Object(s) Button'), 30) == true
			WebUI.click(findTestObject('3. Loader/Load Your Object(s) Button'))
			KeywordUtil.markPassed("Campaign " + CP + " loaded")
			return
		}
		assert WebUI.waitForElementVisible(findTestObject('3. Loader/Search bar insertion order'), 30) == true
		WebUI.clearText(findTestObject('3. Loader/Search bar insertion order'))
		xsetText(findTestObject('3. Loader/Search bar insertion order'), OI)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Loader/Loader First OI'), 60) == true
		WebUI.click(findTestObject('Object Repository/3. Loader/Loader First OI'))
		if (objectType == "OI")
		{
			assert WebUI.waitForElementClickable(findTestObject('Object Repository/3. Loader/Load Your Object(s) Button'), 30) == true
			WebUI.click(findTestObject('Object Repository/3. Loader/Load Your Object(s) Button'))
			KeywordUtil.markPassed("Insertion Order " + OI + " loaded")
			return
		}
		assert WebUI.waitForElementVisible(findTestObject('3. Loader/Search bar line item'), 30) == true
		WebUI.clearText(findTestObject('3. Loader/Search bar line item'))
		xsetText(findTestObject('3. Loader/Search bar line item'), LI.trim())
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/3. Loader/Loader First LI'), 60)
		WebUI.click(findTestObject('Object Repository/3. Loader/Loader First LI'))
		assert WebUI.waitForElementClickable(findTestObject('Object Repository/3. Loader/Load Your Object(s) Button'), 30) == true
		WebUI.click(findTestObject('3. Loader/Load Your Object(s) Button'))
		KeywordUtil.markPassed("Line Item " + LI + " loaded")
	}

	def selectItem(String itemType, int itemIndex)
	{
		/**
		 * 	Select an item in the Objects list
		 * 
		 * @param itemType 			Type of item to select : "Campaign", "Insertion Order" or "Line Item"
		 * @param itemIndex			Item number in the list, starting from the first of its type
		 */
		if(itemType != "Campaign") itemIndex++

		WebUI.click(changeSelector(findTestObject('Object Repository/6. Objects/First Loaded ' + itemType), "nth-child\\(\\d\\)", "nth-child("+ itemIndex +")"))
		Thread.sleep(250)
	}

	@Keyword
	def goToCreator()
	{
		assert WebUI.waitForElementVisible(findTestObject('3. Loader/Creator button'), 30)
		WebUI.click(findTestObject('3. Loader/Creator button'))
	}
	@Keyword
	def selectCreatorDimension(String dimension)
	{
		/**
		 * Select Creator dimension
		 * 
		 * @param dimension		Dimension name to select : "Campaign", "Line" or "Insertion Order"
		 */
		assert WebUI.waitForElementPresent(findTestObject('4. Creator/Item type selector'), 5) : "Cannot open creator dimension dropdown"
		WebUI.click(findTestObject('4. Creator/Item type selector'))
		assert WebUI.waitForElementVisible(findTestObject('4. Creator/' + dimension + '/Creator dimension ' + dimension), 5)
		WebUI.click(findTestObject('4. Creator/' + dimension + '/Creator dimension ' + dimension))
		assert WebUI.waitForElementPresent(findTestObject('4. Creator/' + dimension + '/' + dimension + ' title'), 2) : dimension + ' panel does not appear'
	}

	@Keyword
	def openTemplateFilters()
	{
		/**
		 * Open the template filters panel
		 * 
		 */

		assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Template Filters button'), 5) : "Template filters button is not present"
		WebUI.click(findTestObject('Object Repository/4. Creator/Template Filters button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Template Filters/Title'), 5) : "Template filters were not opened"
		KeywordUtil.logInfo("Filter panel is opened")
	}
	@Keyword
	def closeTemplateFilters(String closingType)
	{
		/**
		 * Close template filter panel with chevron or clicking on overlay
		 * 
		 * @param closingType	Closing type : "Chevron" or "Overlay"
		 * 
		 */

		if(closingType == "Chevron")
			WebUI.click(findTestObject('Object Repository/4. Creator/Template Filters/Close Filters button'))
		else if(closingType == "Overlay")
			WebUI.executeJavaScript("document.querySelector('" + getSelector(findTestObject('Object Repository/4. Creator/Template Filters/Template Backdrop Overlay')).replaceFirst("css=","") + "').click()", null)
		else
			KeywordUtil.markFailedAndStop("Closing type parameter is not correct")

		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Template Filters/Title'), 5)
		KeywordUtil.logInfo("Filter panel has been closed")
	}
	@Keyword
	def selectTemplateFilterType()
	{
		/**
		 * Select the type of object to filter templates on.
		 * 
		 */

		def filterItemTypeIndex = 0
		if(GlobalVariable.templateFiltersType == "Insertion Orders" || GlobalVariable.templateFiltersType == "Insertion Order")
			filterItemTypeIndex = 2
		else if(GlobalVariable.templateFiltersType == "Line Items" || GlobalVariable.templateFiltersType == "Line Item")
			filterItemTypeIndex = 3
		else
			filterItemTypeIndex = 1

		WebUI.click(changeSelector(findTestObject('4. Creator/Template Filters/First Item Type Filter checkbox'), "nth-of-type\\(1\\)", "nth-of-type(" + filterItemTypeIndex + ")"))
		KeywordUtil.logInfo("Filter templates on " + WebUI.getText(changeSelector(findTestObject('4. Creator/Template Filters/First Item Type Filter label'), "nth-of-type\\(1\\)", "nth-of-type(" + filterItemTypeIndex + ")")))
		Thread.sleep(500)
		if(GlobalVariable.templateFiltersType == "Insertion Orders" || GlobalVariable.templateFiltersType == "Insertion Order")
		{
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order load spinner'), 180)
		} else if(GlobalVariable.templateFiltersType == "Line Items" || GlobalVariable.templateFiltersType == "Line Item")
		{
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Line/Line load spinner'), 180)
		} else
		{
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order load spinner'), 180)
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Line/Line load spinner'), 180)
		}

		KeywordUtil.logInfo("Template list has been filtered")
	}
	@Keyword
	def resetTemplateFilters()
	{
		/**
		 * 
		 * Reset template filters and verify the filters are empty
		 * 
		 */
		WebUI.click(findTestObject('Object Repository/4. Creator/Template Filters/Reset Filters button'))
		KeywordUtil.logInfo("Resetting filters")
		checkTemplateFilters([:])
	}
	@Keyword
	def selectTemplateFilters(def selectedTemplateFilters)
	{
		/**
		 * 
		 * Select template filters passed in parameters
		 * 
		 * @param selectedTemplateFilters	Map of filters (any other object type will be stored in a map)
		 * 
		 */

		Map selectedTemplateFiltersMap = [:]
		boolean isFilterSelectable = false
		String unselectableFilters = ""
		selectedTemplateFiltersMap.put(selectedTemplateFilters.key, selectedTemplateFilters.value)

		for(def filterColumn in selectedTemplateFiltersMap)
		{
			def filters = filterColumn.value.split(",")
			def columnName = filterColumn.key
			KeywordUtil.logInfo("Current column : " + columnName)

			if(columnName != "Geolocation" && columnName != "Daily budget")
			{
				String mapFiltersValue = ""
				for(def filter in filters)
				{
					TestObject filterTestObject = changeSelectorXpath(findTestObject('Object Repository/4. Creator/Template Filters/Filter label by xpath'), "Strategy", columnName)
					filterTestObject = changeSelectorXpath(filterTestObject, "Prospection", filter)
					assert WebUI.verifyElementPresent(filterTestObject, 2) : "The filter name does not exist : " + filter
					if(!WebUI.getAttribute(filterTestObject, "class").contains("ivu-checkbox-wrapper-checked") && !WebUI.getAttribute(filterTestObject, "class").contains("ivu-checkbox-wrapper-disabled"))
					{
						WebUI.click(filterTestObject)
						KeywordUtil.logInfo("Selecting filter " + filter)
						mapFiltersValue += filter + ","
						isFilterSelectable = true
					} else
					{
						if(unselectableFilters != "") unselectableFilters += ", "
						unselectableFilters += "'" + filter + "'"
					}
				}

				assert isFilterSelectable == true : "Filters for column '" + columnName + "' in global variable cannot be selected (no result) : " + unselectableFilters
				isFilterSelectable = false
				mapFiltersValue = mapFiltersValue.replaceFirst(",\$", "")
				selectedTemplateFiltersMap[columnName] = mapFiltersValue
			} else if(columnName == "Geolocation")
			{
				for(def filter in filters)
				{
					if(filter == "default")
						WebUI.click(findTestObject('Object Repository/4. Creator/Template Filters/Geolocation Search input'))
					else
						WebUI.setText(findTestObject('Object Repository/4. Creator/Template Filters/Geolocation Search input'), filter)
					assert WebUI.waitForElementVisible(findTestObject('4. Creator/Template Filters/Search suggestion'), 5)

					if(WebUI.getText(findTestObject('4. Creator/Template Filters/Search suggestion')) != "No matching data")
					{
						if(filter == "default")
							selectedTemplateFiltersMap['Geolocation'] = WebUI.getText(findTestObject('4. Creator/Template Filters/Search suggestion'))
						WebUI.click(findTestObject('4. Creator/Template Filters/Search suggestion'))
						WebUI.click(findTestObject('Object Repository/4. Creator/Template Filters/Title'))
						KeywordUtil.logInfo("Selecting Geolocation " + filter)
					} else
					{
						KeywordUtil.markFailedAndStop("The geolocation " + filter + " in global variable was not found")
					}
				}
			} else if(columnName == "Daily budget")
			{
				def filterLow
				def filterHigh

				// Budget Low
				KeywordUtil.logInfo("Selecting Daily budget Low " + filters[0])
				if(filters[0] == "default") {
					WebUI.click(findTestObject('Object Repository/4. Creator/Template Filters/Daily Budget Search Low input'))
				} else {
					WebUI.click(findTestObject('Object Repository/4. Creator/Template Filters/Daily Budget Search Low input'))
					Thread.sleep(500)
					WebUI.setText(findTestObject('Object Repository/4. Creator/Template Filters/Daily Budget Search Low input'), filters[0])
				}

				assert WebUI.waitForElementVisible(findTestObject('4. Creator/Template Filters/Search suggestion'), 10)
				if(WebUI.getText(findTestObject('4. Creator/Template Filters/Search suggestion')) != "No matching data") {
					if(filters[0] == "default")
						filterLow = WebUI.getText(findTestObject('4. Creator/Template Filters/Search suggestion'))
					WebUI.click(findTestObject('4. Creator/Template Filters/Search suggestion'))
				} else {
					KeywordUtil.markFailedAndStop("The Low budget " + filters[0] + " in global variable was not found")
				}

				// Budget High
				KeywordUtil.logInfo("Selecting Daily budget High " + filters[1])
				WebUI.click(findTestObject('Object Repository/4. Creator/Template Filters/Daily Budget Search High input'))
				int numberOfDropdownLabels = selenium.getCssCount(findTestObject('Object Repository/4. Creator/Template Filters/Daily Budget Dropdown Labels').getSelectorCollection().get(SelectorMethod.CSS))
				for(int i = 1; i <= numberOfDropdownLabels; i++) {
					if(WebUI.getAttribute(changeSelector(findTestObject('Object Repository/4. Creator/Template Filters/Daily Budget Dropdown First Label'), "nth-of-type\\(1\\)", "nth-of-type("+i+")"), "class").contains("active"))
						assert filters[1] < Integer.valueOf(WebUI.getText(changeSelector(findTestObject('Object Repository/4. Creator/Template Filters/Daily Budget Dropdown First Label'))))
				}
				if(filters[1] != "default") {
					Thread.sleep(500)
					WebUI.setText(findTestObject('Object Repository/4. Creator/Template Filters/Daily Budget Search High input'), filters[1])
				}
				Thread.sleep(500)
				assert WebUI.waitForElementVisible(findTestObject('4. Creator/Template Filters/Search suggestion'), 10)
				if(WebUI.getText(findTestObject('4. Creator/Template Filters/Search suggestion')) != "No matching data") {
					if(filters[0] == "default") {
						filterHigh = WebUI.getText(findTestObject('4. Creator/Template Filters/Search suggestion'))
						selectedTemplateFiltersMap['Daily budget'] = filterLow + "," + filterHigh
					}
					WebUI.click(findTestObject('4. Creator/Template Filters/Search suggestion'))
				} else {
					KeywordUtil.markFailedAndStop("The high budget " + filters[1] + " in global variable was not found")
				}
			}
		}

		KeywordUtil.logInfo("All filters have been selected")

		if(GlobalVariable.templateFiltersType == "Insertion Orders" || GlobalVariable.templateFiltersType == "Insertion Order") {
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order load spinner'), 180)
		} else if(GlobalVariable.templateFiltersType == "Line Items" || GlobalVariable.templateFiltersType == "Line Item") {
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Line/Line load spinner'), 180)
		} else {
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order load spinner'), 180)
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Line/Line load spinner'), 180)
		}

		KeywordUtil.logInfo("Template list has been filtered")

		return selectedTemplateFiltersMap
	}
	@Keyword
	def checkTemplateFilters(Map selectedTemplateFiltersMap) {
		/**
		 * 
		 * Check template filters matches the passed parameter
		 * 
		 * @param selectedTemplateFilters	Map of expected filters
		 * 
		 */

		assert WebUI.verifyElementPresent(findTestObject('4. Creator/Template Filters/First Item Type Filter checkbox checked'), 2)

		for(def filterColumn in selectedTemplateFiltersMap) {
			def filters = filterColumn.value.split(",")
			def columnName = filterColumn.key
			if(filters != [""]) {
				if(columnName != "Geolocation" && columnName != "Daily budget") {
					for(def filter in filters) {
						TestObject filterTestObject = changeSelectorXpath(findTestObject('Object Repository/4. Creator/Template Filters/Filter label by xpath'), "Strategy", columnName)
						filterTestObject = changeSelectorXpath(filterTestObject, "Prospection", filter)
						if(!WebUI.getAttribute(filterTestObject, "class").contains("ivu-checkbox-wrapper-disabled")) {
							assert WebUI.getAttribute(filterTestObject, "class").contains("ivu-checkbox-wrapper-checked")
							WebUI.focus(filterTestObject)
						}
					}
				} else if(columnName == "Geolocation") {
					TestObject filterTestObject = findTestObject('Object Repository/4. Creator/Template Filters/Geolocation tag')
					assert WebUI.getText(filterTestObject) == filters[0]
				} else if(columnName == "Daily budget") {
					TestObject lowFilterTestObject = findTestObject('4. Creator/Template Filters/Selected Daily Budget Low input')
					assert WebUI.getAttribute(lowFilterTestObject, "value") == filters[0]
					TestObject highFilterTestObject = findTestObject('4. Creator/Template Filters/Selected Daily Budget High input')
					assert WebUI.getAttribute(highFilterTestObject, "value") == filters[1]
				}
			} else if(selectedTemplateFiltersMap.equals([:])) {
				assert WebUI.verifyElementNotPresent(findTestObject('4. Creator/Template Filters/Any filter checked'), 2)
				assert WebUI.getText(findTestObject('4. Creator/Template Filters/Selected Geolocations input')) == ""
				assert WebUI.getAttribute(findTestObject('4. Creator/Template Filters/Selected Daily Budget High input'), "value") == ""
				assert WebUI.getAttribute(findTestObject('4. Creator/Template Filters/Selected Daily Budget Low input'), "value") == ""
			}
		}
		KeywordUtil.logInfo("Filters are displayed as expected")
	}
	@Keyword
	def checkFilteredTemplatesViaGCP(Map selectedTemplateFiltersMap) {
		/**
		 * Check filtered templates on Builder based on GCP database results
		 * 
		 * @param selectedTemplateFiltersMap	Array of filters to add in the request
		 *
		 */
		selenium = getSelenium()
		DBData dbTestData = findTestData('Get templates')

		dbTestData.query = "SELECT name FROM tradelab_dv360_dev.template where partner_id = '"+ GlobalVariable.partnerID +"' and is_active = 1"
		for(def filterColumn in selectedTemplateFiltersMap) {
			def filters = filterColumn.value.split(",")
			def columnName = filterColumn.key.toLowerCase().replaceAll(' ', "_")
			if(filters != [""]) {
				if(columnName != "geolocation" && columnName != "daily_budget") {
					if(columnName == "goal") columnName = "objective"
					dbTestData.query += " AND " + columnName + " in ('" + filters[0].toLowerCase().replaceAll("\\/", "_").replaceAll(" ", "_").replaceAll('_\\+_', "_") + "'"
					Boolean skippedFirst = false
					for(def filter in filters) {
						if (skippedFirst == true)
							dbTestData.query += ", '" + filter.toLowerCase().replaceAll("\\/", "_").replaceAll(" ", "_").replaceAll('_\\+_', "_") + "'"
						else skippedFirst = true
					}
					dbTestData.query += ")"
				} else if (columnName == "daily_budget") {
					dbTestData.query += " AND daily_budget_interval_min >= " + filters[0]
					dbTestData.query += " AND daily_budget_interval_max <= " + filters[1]
				}  else if (columnName == "geolocation") {
					if(filters[0] == "default") filters[0] = "France"
					dbTestData.query += " AND country_iso_3166_2 in ('" + filters[0].substring(0, 2).toUpperCase() + "')"
				}
			}
		}

		if (GlobalVariable.advertiserVersion == "5.0")
		{
			dbTestData.query += "AND name NOT LIKE '%5.2' order by name DESC;"
		} else
		{
			dbTestData.query += "AND name LIKE '%"+GlobalVariable.advertiserVersion+"' order by name DESC;"
		}

		KeywordUtil.logInfo("Requête : " + dbTestData.query)

		def queryResults = dbTestData.fetchData()
		int numberOfNames = queryResults.size()
		assert numberOfNames > 0 : "The query did not retun any result. Something is wrong with the query (eg. partner ID)"
		def templateNames = []
		String lastTemplateName = "lastTemplatePlaceholder"
		String currentTemplateName = "currentTemplatePlaceholder"

		for(def templateColumns in queryResults) {
			currentTemplateName = templateColumns[0]
			if(currentTemplateName != lastTemplateName) {
				templateNames.add(currentTemplateName)
			}

			lastTemplateName = currentTemplateName
		}

		if(GlobalVariable.templateFiltersType == "Insertion Orders" || GlobalVariable.templateFiltersType == "Insertion Order" || GlobalVariable.templateFiltersType == "") {
			TestObject templateNameTestObject = null
			boolean templateFound = false
			for(String templateName in templateNames) {
				KeywordUtil.logInfo("Checking template " + templateName)
				for(int i = 1 ; i <= templateNames.size(); i++) {
					templateNameTestObject = changeSelector(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template name'), "nth-of-type\\(1\\)", "nth-of-type("+ i +")")
					//KeywordUtil.logInfo("Displayed template " + WebUI.getText(templateNameTestObject))
					if(WebUI.getText(templateNameTestObject) == templateName) {
						templateFound = true
						//KeywordUtil.logInfo("Template found")
						break;
					}
				}
				if(templateFound == false)
					println("Line for breakpoint")
				assert templateFound == true : "Template matching filter was not found"
				templateFound = false
			}
			KeywordUtil.logInfo("All insertion order templates found\n")
		}

		if(GlobalVariable.templateFiltersType == "Line Items" || GlobalVariable.templateFiltersType == "Line Item" || GlobalVariable.templateFiltersType == "") {
			TestObject templateNameTestObject = null
			boolean templateFound = false
			for(String templateName in templateNames) {
				KeywordUtil.logInfo("Checking template " + templateName)
				for(int i = 2 ; i <= templateNames.size() + 1; i++) {
					templateNameTestObject = changeSelector(findTestObject('Object Repository/4. Creator/Line/Line First Template name'), "nth-of-type\\(2\\)", "nth-of-type("+ i +")")
					//KeywordUtil.logInfo("Displayed template " + WebUI.getText(templateNameTestObject))
					if(WebUI.getText(templateNameTestObject) == templateName) {
						templateFound = true
						//KeywordUtil.logInfo("Template found")
						break;
					}
				}
				if(templateFound == false)
					println("Line for breakpoint")
				assert templateFound == true : "Template matching filter was not found"
				templateFound = false
			}
			KeywordUtil.logInfo("All Line Item templates found")
		}

		KeywordUtil.logInfo("Templates are properly displayed according to filters\n")
	}
	@Keyword
	def checkFilteredTemplatesViaPostman(Map selectedTemplateFiltersMap) {
		/**
		 * Check filtered templates on Builder based on API results
		 *
		 * @param selectedTemplateFiltersMap	Array of filters to add in the request
		 *
		 */
		DV360 dv360 = new DV360()
		selenium = getSelenium()
		String env = ""
		if(GlobalVariable.env.contains("dev")) env = ".dev"
		else if (GlobalVariable.env.contains("shared")) env = ".dev"
		else if (GlobalVariable.env.contains("staging")) env = ".staging"

		if(driver.getWindowHandles().size() < 2) {
			dv360.logInToDv360(true)
			WebUI.navigateToUrl('https://web.postman.co/build/workspace/')
			WebUI.waitForElementVisible(findTestObject('Object Repository/13. Postman/Google Sign-in button'), 30)
			WebUI.click(findTestObject('Object Repository/13. Postman/Google Sign-in button'))
			WebUI.waitForElementVisible(findTestObject('Object Repository/13. Postman/Requests Search bar'), 60)
			WebUI.setText(findTestObject('Object Repository/13. Postman/Requests Search bar'), "Builder get templates")
			WebUI.click(findTestObject('Object Repository/13. Postman/Collections tab'))
			WebUI.waitForElementVisible(findTestObject('13. Postman/First Collection Unwrap button'), 30)
			if(!selenium.isElementPresent(getSelector(findTestObject('13. Postman/First Collection opened'))))
				WebUI.click(findTestObject('13. Postman/First Collection Unwrap button'))
			WebUI.click(findTestObject('Object Repository/13. Postman/Search result request'))
			WebUI.click(findTestObject('Object Repository/13. Postman/Agent Selection button'))
			WebUI.waitForElementVisible(findTestObject('Object Repository/13. Postman/Agent Selection Select button'), 30)
			WebUI.click(findTestObject('Object Repository/13. Postman/Agent Selection Select button'))
			WebUI.waitForElementVisible(findTestObject('Object Repository/13. Postman/Agent Desktop'), 30)
			WebUI.click(findTestObject('Object Repository/13. Postman/Agent Desktop'))
			WebUI.delay(2)
			WebUI.click(findTestObject('Object Repository/13. Postman/Agent Selection close button'))
		} else {
			WebUI.switchToWindowIndex(1)
		}

		String request = 'https://dv360builder-api'+ env +'.tradelab-apps.com/dv360-templates?search={"active" : 1,"partner_id" : ['+GlobalVariable.partnerID+']'
		for(def filterColumn in selectedTemplateFiltersMap) {
			def filters = filterColumn.value.split(",")
			def columnName = filterColumn.key.toLowerCase().replaceAll(' ', "_")
			if(filters != [""]) {
				if(columnName != "geolocation" && columnName != "daily_budget") {
					if(columnName == "goal") columnName = "objective"
					request += ', "' + columnName + '" : ["' + filters[0].toLowerCase().replaceAll("\\/", "_").replaceAll(" ", "_").replaceAll('_\\+_', "_") + '"'
					Boolean skippedFirst = false
					for(def filter in filters) {
						if (skippedFirst == true)
							request += ', "' + filter.toLowerCase().replaceAll("\\/", "_").replaceAll(" ", "_").replaceAll('_\\+_', "_") + '"'
						else skippedFirst = true
					}
					request += ']'
				} else if (columnName == "daily_budget") {
					request += ', "daily_budget_interval_min" : ' + filters[0]
					request += ', "daily_budget_interval_max" : ' + filters[1]
				} else if (columnName == "geolocation") {
					if(filters[0] == "default") filters[0] = "France"
					request += ', "country_iso_3166_2" : ["' + filters[0].substring(0, 2).toUpperCase() + '"]'
				}
				request += '}'
			}
		}

		KeywordUtil.logInfo("Request : " + request)

		WebUI.click(findTestObject('Object Repository/13. Postman/Request field'))
		WebUI.delay(1)
		WebUI.sendKeys(findTestObject('Object Repository/13. Postman/Request input'), Keys.chord(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.SHIFT, Keys.ARROW_UP, Keys.ARROW_UP, Keys.ARROW_UP, Keys.DELETE))
		WebUI.sendKeys(findTestObject('Object Repository/13. Postman/Request field'), request)
		WebUI.sendKeys(findTestObject('Object Repository/13. Postman/Request field'), Keys.chord(Keys.ENTER))
		WebUI.click(findTestObject('Object Repository/13. Postman/Save button'))
		WebUI.delay(1)
		WebUI.refresh()
		WebUI.waitForElementVisible(findTestObject('Object Repository/13. Postman/Send button'), 60)
		WebUI.click(findTestObject('Object Repository/13. Postman/Send button'))
		WebUI.delay(1)

		assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/13. Postman/Request loading'), 30)
		assert WebUI.getText(findTestObject('Object Repository/13. Postman/Response status code')) == "200"

		// On récupère la réponse de la requête via le bouton copy to clipboard, car la réponse affichée n'est pas entière (faux scroll)
		WebUI.click(findTestObject('Object Repository/13. Postman/Copy Response to Clipboard button'))
		WebUI.delay(1)

		String text = storeTextFromClipboard()
		def regex = /name":"(.*?)"/

		def templateNames = (text =~ regex).collect() { it[1] }
		int numberOfNames = templateNames.size()

		WebUI.switchToWindowIndex(0)

		if(GlobalVariable.templateFiltersType == "Insertion Orders" || GlobalVariable.templateFiltersType == "Insertion Order" || GlobalVariable.templateFiltersType == "") {
			TestObject templateNameTestObject = null
			boolean templateFound = false
			for(String templateName in templateNames) {
				KeywordUtil.logInfo("Checking template " + templateName)
				for(int i = 1 ; i <= templateNames.size(); i++) {
					templateNameTestObject = changeSelector(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template name'), "nth-of-type\\(1\\)", "nth-of-type("+ i +")")
					//KeywordUtil.logInfo("Displayed template " + WebUI.getText(templateNameTestObject))
					if(WebUI.getText(templateNameTestObject) == templateName) {
						templateFound = true
						//KeywordUtil.logInfo("Template found")
						break;
					}
				}
				assert templateFound == true : "Template matching filter was not found : \n" + templateName
				templateFound = false
			}
			KeywordUtil.logInfo("All insertion order templates found\n")
		}

		if(GlobalVariable.templateFiltersType == "Line Items" || GlobalVariable.templateFiltersType == "Line Item" || GlobalVariable.templateFiltersType == "") {
			TestObject templateNameTestObject = null
			boolean templateFound = false
			for(String templateName in templateNames) {
				KeywordUtil.logInfo("Checking template " + templateName)
				for(int i = 2 ; i <= templateNames.size() + 1; i++) {
					templateNameTestObject = changeSelector(findTestObject('Object Repository/4. Creator/Line/Line First Template name'), "nth-of-type\\(2\\)", "nth-of-type("+ i +")")
					//KeywordUtil.logInfo("Displayed template " + WebUI.getText(templateNameTestObject))
					if(WebUI.getText(templateNameTestObject) == templateName) {
						templateFound = true
						//KeywordUtil.logInfo("Template found")
						break;
					}
				}
				assert templateFound == true : "Template matching filter was not found : \n" + templateName
				templateFound = false
			}
			KeywordUtil.logInfo("All Line Item templates found")
		}

		KeywordUtil.logInfo("Templates are properly displayed according to filters\n")
	}
	@Keyword
	def checkFilteredTemplatesInDatabase(Map selectedTemplateFiltersMap) {
		/**
		 * Check filtered templates on Builder based on MySQL database results
		 * 
		 * @param selectedTemplateFiltersMap	Array of filters to add in the request
		 *
		 */
		selenium = getSelenium()
		String env = ""
		if(GlobalVariable.env.contains("dev")) env = "dev"
		else if (GlobalVariable.env.contains("shared")) env = GlobalVariable.env
		else if (GlobalVariable.env.contains("staging")) env = "staging"
		else env = "dev"
		String dbUrl = 'https://' + env + '-saas.tradelab.fr' + "/tvflNDOqWgcZ/index.php?server=2&token=e01e8392215c0a5f4a3e5bbfcb54d0c5&old_usr=tradelab_dev&db=tradelab_dv360_dev"

		if(driver.getWindowHandles().size() < 2) {
			selenium.openWindow("","")
			WebUI.switchToWindowIndex(1)
			WebUI.navigateToUrl(dbUrl)
			WebUI.waitForPageLoad(60)
			WebUI.setText(findTestObject('Object Repository/12. Database/DB Username input'), GlobalVariable.dbLogin)
			Thread.sleep(500);
			WebUI.setEncryptedText(findTestObject('Object Repository/12. Database/DB Password input'), GlobalVariable.dbPassword)
			boolean isStaging = selenium.isElementPresent("id=select_server")
			if(isStaging == true) {
				WebUI.click(findTestObject('Object Repository/12. Database/Server select'))
				WebUI.click(findTestObject('Object Repository/12. Database/Second server'))
			}

			WebUI.click(findTestObject('Object Repository/12. Database/DB Login button'))
			WebUI.click(findTestObject('Object Repository/12. Database/SQL tab'))
		} else {
			WebUI.switchToWindowIndex(1)
		}
		WebUI.waitForElementPresent(findTestObject('Object Repository/12. Database/SQL textarea'), 30)

		Thread.sleep(2000);

		String request = "SELECT name FROM tradelab_dv360_dev.template where partner_id = '"+ GlobalVariable.partnerID +"' and is_active = 1"
		for(def filterColumn in selectedTemplateFiltersMap) {
			def filters = filterColumn.value.split(",")
			def columnName = filterColumn.key.toLowerCase().replaceAll(' ', "_")
			if(filters != [""]) {
				if(columnName != "geolocation" && columnName != "daily_budget") {
					if(columnName == "goal") columnName = "objective"
					request += " AND " + columnName + " in ('" + filters[0].toLowerCase().replaceAll("\\/", "_").replaceAll(" ", "_").replaceAll('_\\+_', "_") + "'"
					Boolean skippedFirst = false
					for(def filter in filters) {
						if (skippedFirst == true)
							request += ", '" + filter.toLowerCase().replaceAll("\\/", "_").replaceAll(" ", "_").replaceAll('_\\+_', "_") + "'"
						else skippedFirst = true
					}
					request += ")"
				} else if (columnName == "daily_budget") {
					request += " AND daily_budget_interval_min >= " + filters[0]
					request += " AND daily_budget_interval_max <= " + filters[1]
				}  else if (columnName == "geolocation") {
					if(filters[0] == "default") filters[0] = "France"
					request += " AND country_iso_3166_2 in ('" + filters[0].substring(0, 2).toUpperCase() + "')"
				}
			}
		}

		request += " order by name DESC;"

		KeywordUtil.logInfo("Requête : " + request)
		selenium.click(getSelector(findTestObject('Object Repository/12. Database/SQL Query Container')))
		WebUI.sendKeys(findTestObject('Object Repository/12. Database/SQL textarea'), Keys.chord(Keys.SHIFT, Keys.ARROW_UP, Keys.ARROW_UP, Keys.ARROW_UP, Keys.ARROW_UP, Keys.ARROW_UP, Keys.ARROW_UP, Keys.DELETE))
		WebUI.sendKeys(findTestObject('Object Repository/12. Database/SQL textarea'), request)
		WebUI.click(findTestObject('Object Repository/12. Database/Execute SQL button'))
		WebUI.delay(1)
		assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/12. Database/Loading message'), 30)
		assert WebUI.verifyElementPresent(findTestObject('Object Repository/12. Database/Success message'), 30)

		int numberOfNames = selenium.getCssCount(findTestObject("12. Database/Result Rows").getSelectorCollection().get(SelectorMethod.CSS))
		def templateNames = []
		String lastTemplateName = "lastTemplatePlaceholder"
		String currentTemplateName = "currentTemplatePlaceholder"

		for(int i = 1; i <= numberOfNames; i++) {
			currentTemplateName = WebUI.getText(changeSelector(findTestObject('Object Repository/12. Database/Result First Row text'), "nth-child\\(1\\)", "nth-child("+ i +")"))
			if(currentTemplateName != lastTemplateName) {
				templateNames.add(currentTemplateName)
			}

			lastTemplateName = currentTemplateName
		}

		WebUI.switchToWindowIndex(0)

		if(GlobalVariable.templateFiltersType == "Insertion Orders" || GlobalVariable.templateFiltersType == "Insertion Order" || GlobalVariable.templateFiltersType == "") {
			TestObject templateNameTestObject = null
			boolean templateFound = false
			for(String templateName in templateNames) {
				KeywordUtil.logInfo("Checking template " + templateName)
				for(int i = 1 ; i <= templateNames.size(); i++) {
					templateNameTestObject = changeSelector(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template name'), "nth-of-type\\(1\\)", "nth-of-type("+ i +")")
					//KeywordUtil.logInfo("Displayed template " + WebUI.getText(templateNameTestObject))
					if(WebUI.getText(templateNameTestObject) == templateName) {
						templateFound = true
						//KeywordUtil.logInfo("Template found")
						break;
					}
				}
				if(templateFound == false)
					println("Line for breakpoint")
				assert templateFound == true : "Template matching filter was not found"
				templateFound = false
			}
			KeywordUtil.logInfo("All insertion order templates found\n")
		}

		if(GlobalVariable.templateFiltersType == "Line Items" || GlobalVariable.templateFiltersType == "Line Item" || GlobalVariable.templateFiltersType == "") {
			TestObject templateNameTestObject = null
			boolean templateFound = false
			for(String templateName in templateNames) {
				KeywordUtil.logInfo("Checking template " + templateName)
				for(int i = 2 ; i <= templateNames.size() + 1; i++) {
					templateNameTestObject = changeSelector(findTestObject('Object Repository/4. Creator/Line/Line First Template name'), "nth-of-type\\(2\\)", "nth-of-type("+ i +")")
					//KeywordUtil.logInfo("Displayed template " + WebUI.getText(templateNameTestObject))
					if(WebUI.getText(templateNameTestObject) == templateName) {
						templateFound = true
						//KeywordUtil.logInfo("Template found")
						break;
					}
				}
				if(templateFound == false)
					println("Line for breakpoint")
				assert templateFound == true : "Template matching filter was not found"
				templateFound = false
			}
			KeywordUtil.logInfo("All Line Item templates found")
		}

		KeywordUtil.logInfo("Templates are properly displayed according to filters\n")
	}

	@Keyword
	List<String> createNewCampaign(String itemName, String itemStatus, int startDateOffset, int endDateOffset) {
		goToCreator()
		selectCreatorDimension("Campaign")
		xsetOption(findTestObject('4. Creator/Campaign/Campaign status select'), itemStatus)
		xsetText(findTestObject('4. Creator/Campaign/Name'), itemName)

		DecimalFormat format = new DecimalFormat("0.00")

		String budget = GlobalVariable.budgetField[0]
		float budgetValue = 0.0f
		if(budget != "") {
			budget = budget.replaceFirst("[,\\.](\\d+\$)", ".\$1")
			budgetValue = Float.valueOf(budget)
		}
		if(budgetValue > 0 || budget == "") {
			xsetText(findTestObject('4. Creator/Campaign/Campaign budget'), budget, 'A2')
			WebUI.click(findTestObject('4. Creator/Campaign/Name'))
			HighlightElement on = new HighlightElement()
			on.on1(findTestObject('4. Creator/Campaign/Campaign budget'))
			if(budgetValue % 1 != 0)
				budget = budget.replaceFirst("(.*)", "\$100").replaceFirst("(\\d{2}).*\$", "\$1")
			//	assert WebUI.getAttribute(findTestObject('4. Creator/Campaign/Campaign budget'), "value") == budget : "Displayed budget value and global variable does not match : " + WebUI.getAttribute(findTestObject('4. Creator/Campaign/Campaign budget'), "value") + " (displayed) =/= " + budget + " (expected)"
			KeywordUtil.logInfo("Campaign budget set : " + budget)
		} else if (budget == "0") { // on ne touche pas au champ
			budget = ""
			assert WebUI.getAttribute(findTestObject('4. Creator/Campaign/Campaign budget'), "value") == ""
		} else {
			KeywordUtil.markFailedAndStop("Global variable 'budgetField' cannot be negative")
		}
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

		if (endDate > 0 && endDate <= 31) {
			endDateValue = endDate + " " + months[calendarEnd.get(Calendar.MONTH)] + " " + calendarEnd.get(Calendar.YEAR)
			modifyDate(endDateValue, "Campaign", "End")
		}

		WebDriver driver = DriverFactory.getWebDriver()
		int oldNumberOfLoadedItems = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/Loaded Items').getSelectorCollection().get(SelectorMethod.CSS))).size()
		WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))
		assert WebUI.waitForElementVisible(changeSelector(findTestObject('6. Objects/First Loaded Object name'),"nth-child\\(\\d\\)","nth-child("+(oldNumberOfLoadedItems+1)+")"), 10) : "Campaign was not created"
		KeywordUtil.markPassed("Campaign " + itemName + " created")
		WebUI.click(changeSelector(findTestObject('6. Objects/First Loaded Object name'),"nth-child\\(\\d\\)","nth-child("+(oldNumberOfLoadedItems+1)+")"))

		ArrayList<String> newCampaignSetup = new ArrayList<String>(5)
		newCampaignSetup.add(itemName)
		newCampaignSetup.add(itemStatus)
		newCampaignSetup.add(budget)
		newCampaignSetup.add(calendar.getTime().format('MM/dd/yyyy') + " 00:00")
		newCampaignSetup.add(calendarEnd.getTime().format('MM/dd/yyyy') + " 23:59")

		return newCampaignSetup
	}
	def createNewCampaign1(String itemName, String itemStatus, String itemBudget, int startDateOffset, int endDateOffset) {
		/**
		 * Create new campaign
		 *
		 * @param itemName
		 * 		New campaign name
		 * @param itemStatus
		 * 		Status of the campaign
		 * @param itemBudget
		 * 		Campaign budget value
		 * @param startDateOffset
		 * 		Campaign offset start date
		 * @param endDateOffset
		 * 		Campaign offset end date
		 */

		KeywordUtil.logInfo("Create new Campaign")

		if (WebUI.waitForElementNotPresent(findTestObject('4. Creator/Item type selector'), 2) && WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Header'), 2)) {
			KeywordUtil.logInfo("Open CP panel")
			goToCreator()
			selectCreatorDimension("Campaign")
		}

		selectCreatorDimension("Campaign")
		xsetOption(findTestObject('4. Creator/Campaign/Campaign status select'), itemStatus)
		xsetText(findTestObject('4. Creator/Campaign/Name'), itemName)
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

		if (endDate > 0 && endDate <= 31) {
			endDateValue = endDate + " " + months[calendarEnd.get(Calendar.MONTH)] + " " + calendarEnd.get(Calendar.YEAR)
			modifyDate(endDateValue, "Campaign", "End")
		}

		ArrayList<String> newCampaignSetup = new ArrayList<String>(5)
		newCampaignSetup.add(itemName)
		newCampaignSetup.add(itemStatus)
		newCampaignSetup.add(itemBudget)
		newCampaignSetup.add(calendar.getTime().format('MM/dd/yyyy') + " 00:00")
		newCampaignSetup.add(calendarEnd.getTime().format('MM/dd/yyyy') + " 23:59")

	}
	def createNewIo(String OIname, int OIPosition)
	{
		/**
		 * Create a new Insertion order
		 *
		 * @param OIname
		 * 		Name of the insertion order
		 * @param OIPosition
		 * 		position of the insertion order chosen
		 *
		 */

		Date date = new Date()
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date)
		String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()

		KeywordUtil.logInfo("Create new Insertion order")

		if (WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Header'), 1) && WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Line/Line Item title'), 1)) {
			goToCreator()
			selectCreatorDimension("Insertion Order")
		}

		createNewCampaign1("Test_Auto_Campaign_Create_And_Edit_" + date.getTime(), "Paused", "1", 0, 1)

		WebUI.click(changeSelector(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template'), 'nth-child\\(\\d\\)', 'nth-child(' + OIPosition + ')'))

		xsetText(findTestObject('Object Repository/4. Creator/Insertion Order/Name'), OIname)
		WebUI.delay(1)
	}
	@Keyword
	def CreateOIfromCampaign(String Campaign, String OIname)
	{
		/**
		 * this function create an OI from a specific campaign
		 * @param Campaign
		 * The name of the campaign to select
		 *
		 * @param OIname
		 * The name of the OI to be created
		 */

		goToCreator()
		selectCreatorDimension("Insertion Order")
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'))

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Campaign filter input'), 5) == true
		xsetText(findTestObject('Object Repository/4. Creator/Campaign/Campaign filter input'), Campaign)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'), 10) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'))

		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template'))
		xsetText(findTestObject('Object Repository/4. Creator/Insertion Order/Name'), OIname)
		WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))
		Thread.sleep(500)
	}
	@Keyword
	def CreateOIfromCampaign(String Campaign, String OIname, String Templatename, String Status, int startDay, int endDay)
	{
		/**
		 * this function create an OI from a specific campaign
		 * @param Campaign
		 * The name of the campaign to select
		 *
		 * @param OIname
		 * The name of the OI to be created
		 *
		 * @param Template
		 * The template to be selected for the creation
		 *
		 * @param Status
		 * The status of the new OI
		 *
		 * @param startDateoffset
		 * The number of day from today to start the OI
		 *
		 * @param endDateoffset
		 * The number of day from today to end the OI
		 */

		goToCreator()
		selectCreatorDimension("Insertion Order")
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'))

		// mettre le nom campaign dans le filtre et selectioné first template
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Campaign filter input'), 5) == true
		xsetText(findTestObject('Object Repository/4. Creator/Campaign/Campaign filter input'), Campaign)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'), 10) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'))

		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template'))
		xsetText(findTestObject('Object Repository/4. Creator/Insertion Order/Name'), OIname)
		xsetOption(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Status select'), Status)

		Date date = new Date()
		Calendar calendarEnd = GregorianCalendar.getInstance()
		String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()
		calendarEnd.setTime(date)
		if (endDay > 0)
			calendarEnd.add(Calendar.DATE, endDay)
		String endDateValue = ""
		int endDate = calendarEnd.get(Calendar.DAY_OF_MONTH)

		if (endDate > 0 && endDate <= 31) {
			endDateValue = String.format("%02d", endDate) + " " + months[calendarEnd.get(Calendar.MONTH)] + " " + calendarEnd.get(Calendar.YEAR)
			modifyDate(endDateValue, findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order End Date button'))
		}

		// Setting the start date
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date)
		if (startDay > 0)
			calendar.add(Calendar.DATE, startDay)
		String startDateValue = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR)

		modifyDate(startDateValue, findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Start Date button'))

		WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))
		Thread.sleep(500)
	}
	@Keyword
	def CreateOIfromLoadedCampaign()
	{
		/**
		 * this function create an OI with a loaded campaign template
		 *
		 **/
		goToCreator()
		selectCreatorDimension("Insertion Order")

		WebUI.click(findTestObject('4. Creator/Item type selector'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Insertion Order/Creator dimension Insertion Order'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/Creator dimension Insertion Order'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'), 30) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'))

		Thread.sleep(500)
		if (WebUI.verifyElementVisible(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template')) == false)
			KeywordUtil.markFailedAndStop('No template available on the advertiser')
		// Select IO template
		if(!GlobalVariable.ioTemplateNames.equals(['']) && GlobalVariable.ioTemplateNames != null && !GlobalVariable.ioTemplateNames.equals([]) && GlobalVariable.ioTemplateNames[0] != null) {
			xsetText(findTestObject('Object Repository/4. Creator/Insertion Order/Search filter'), GlobalVariable.ioTemplateNames[0])
			Thread.sleep(500)
		}

		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/First Template Displayed'))
		xsetText(findTestObject('Object Repository/4. Creator/Insertion Order/Name'), GlobalVariable.itemNames[1] + "_" + Instant.now().getEpochSecond())
		WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))
		Thread.sleep(500)
	}
	@Keyword
	def CreateLinefromLoadedCampaignLoadedOI(int startDay, int endDay, String linename1, String linename2)
	{
		/** @param startDay
		 * The number of day from today before the line start diffusing
		 *
		 * @param endDay
		 * The number of day from today before the line stops diffusing
		 *
		 *@param linename1
		 * The name of the first line created
		 *
		 * @param linename2
		 * The name of the second line created
		 *
		 * This function create two line from the first campaign template loaded and the first template OI loaded
		 *
		 */

		//Select the line creation
		goToCreator()
		selectCreatorDimension("Line")

		//Select the first campaign template in loaded mode
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'))

		//Select the first OI template in loaded mode
		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/Creator OI mode button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Insertion Order/OI mode Loaded'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/OI mode Loaded'))
		Thread.sleep(500)
		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/First Template Displayed'))

		WebUI.click(findTestObject('4. Creator/Line/Line title'))

		// Select Line templates
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Line/First Template div'), 60) == true : "Line template not available for the selected filter\n"

		if(!GlobalVariable.lineTemplateNames.equals(['']) && GlobalVariable.lineTemplateNames != null && !GlobalVariable.lineTemplateNames.equals([]) && GlobalVariable.lineTemplateNames[0] != null && GlobalVariable.lineTemplateNames[1] != null)
		{
			xsetText(findTestObject('Object Repository/4. Creator/Line/Search filter'), GlobalVariable.lineTemplateNames[0])
			Thread.sleep(500)
			WebUI.click(findTestObject('4. Creator/Line/First template checked dispayed'))
			xsetText(findTestObject('Object Repository/4. Creator/Line/Search filter'), GlobalVariable.lineTemplateNames[1])
			Thread.sleep(500)
			WebUI.click(findTestObject('4. Creator/Line/First template checked dispayed'))
		}
		else {
			WebUI.click(findTestObject('Object Repository/4. Creator/Line/First Template'))
		}

		assert WebUI.verifyElementClickable(findTestObject('Object Repository/4. Creator/Line/Next Step button')) == true : "Next Step button is not clickable. Some mandatory data are missing"
		WebUI.click(findTestObject('Object Repository/4. Creator/Line/Next Step button'))

		if(GlobalVariable.sameAsInsertionOrder == false) {
			xclick(findTestObject('Object Repository/4. Creator/Line/1. Next step/Same as Insertion Order button'), 'checkbox')

			//Setting the end date
			Date date = new Date()
			Calendar calendarEnd = GregorianCalendar.getInstance()
			String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()
			calendarEnd.setTime(date)
			calendarEnd.add(Calendar.DATE, endDay)
			String endDateValue = ""
			int endDate = calendarEnd.get(Calendar.DAY_OF_MONTH)

			if (endDate > 0 && endDate <= 31) {
				endDateValue = endDate + " " + months[calendarEnd.get(Calendar.MONTH)] + " " + calendarEnd.get(Calendar.YEAR)
				modifyDate(endDateValue, "Line Item", "End")
			}

			//Setting the start date
			Calendar calendar = GregorianCalendar.getInstance()
			calendar.setTime(date)
			if (startDay > 0)
				calendar.add(Calendar.DATE, startDay)
			String startDateValue = calendar.get(Calendar.DAY_OF_MONTH) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR)

			modifyDate(startDateValue, "Line Item", "Start")
		}

		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/4. Creator/Settings/all specific settings'), 'childElementCount', '2', 5) == true : "The number of line does not match the predicted result"

		//setting lines names
		WebUI.clearText(findTestObject('Object Repository/4. Creator/Settings/Specific setting name 1'))
		xsetText(findTestObject('Object Repository/4. Creator/Settings/Specific setting name 1'), linename1)
		WebUI.clearText(findTestObject('Object Repository/4. Creator/Settings/Specific setting name 2'))
		xsetText(findTestObject('Object Repository/4. Creator/Settings/Specific setting name 2'), linename2)

		WebUI.click(findTestObject('Object Repository/4. Creator/Line/Create your Object(s) button'))
	}

	@Keyword
	def CreateLinefromLoadedCampaignLoadedOI(int startDay, int endDay, Object templates, ArrayList<String> linename)
	{
		/**
		 * This function create line depending on the number of line template from the first campaign template loaded and the first template OI loaded 
		 *  @param startDay
		 * The number of day from today before the line start diffusing
		 *
		 * @param endDay
		 * The number of day from today before the line stops diffusing
		 *
		 *@param template
		 * The list of template to select
		 *
		 * @param linename
		 * The list of name for the line to create
		 *
		 *
		 */

		//Select the line creation
		goToCreator()
		selectCreatorDimension("Line")
		int numbertemplate = 1
		//Select the first campaign template in loaded mode
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'))

		//Select the first OI template in loaded mode
		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/Creator OI mode button'))
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Insertion Order/OI mode Loaded'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/OI mode Loaded'))
		Thread.sleep(500)
		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/First Template Displayed'))

		WebUI.click(findTestObject('4. Creator/Line/Line title'))

		// Select Line templates
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Line/First Template div'), 60) == true : "Line template not available for the selected filter\n"

		if(!templates.equals(['']) && templates != null && !templates.equals([]) && templates[0] != null)
		{
			int i = 0
			for (def elem in templates)
			{
				xsetText(findTestObject('Object Repository/4. Creator/Line/Search filter'), GlobalVariable.lineTemplateNames[i])
				Thread.sleep(500)
				WebUI.click(findTestObject('4. Creator/Line/First template checked dispayed'))
				i++
				WebUI.clearText(findTestObject('Object Repository/4. Creator/Line/Search filter'))
			}
			numbertemplate = i
		}
		else {
			WebUI.click(findTestObject('Object Repository/4. Creator/Line/First Template'))
		}

		assert WebUI.verifyElementClickable(findTestObject('Object Repository/4. Creator/Line/Next Step button')) == true : "Next Step button is not clickable. Some mandatory data are missing"
		WebUI.click(findTestObject('Object Repository/4. Creator/Line/Next Step button'))

		if(GlobalVariable.sameAsInsertionOrder == false) {
			xclick(findTestObject('Object Repository/4. Creator/Line/1. Next step/Same as Insertion Order button'), 'checkbox')

			//Setting the end date
			Date date = new Date()
			Calendar calendarEnd = GregorianCalendar.getInstance()
			String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()
			calendarEnd.setTime(date)
			calendarEnd.add(Calendar.DATE, endDay)
			String endDateValue = ""
			int endDate = calendarEnd.get(Calendar.DAY_OF_MONTH)

			if (endDate > 0 && endDate <= 31) {
				endDateValue = endDate + " " + months[calendarEnd.get(Calendar.MONTH)] + " " + calendarEnd.get(Calendar.YEAR)
				modifyDate(endDateValue, "Line Item", "End")
			}

			//Setting the start date
			Calendar calendar = GregorianCalendar.getInstance()
			calendar.setTime(date)
			if (startDay > 0)
				calendar.add(Calendar.DATE, startDay)
			String startDateValue = calendar.get(Calendar.DAY_OF_MONTH) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR)

			modifyDate(startDateValue, "Line Item", "Start")
		}

		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/4. Creator/Settings/all specific settings'), 'childElementCount', numbertemplate.toString(), 5) == true : "The number of line does not match the predicted result"

		//setting lines names
		int i = 1
		TestObject settingname = findTestObject('Object Repository/4. Creator/Settings/Specific setting name 1')
		for (def name in linename)
		{
			settingname = changeSelector(settingname, "nth-child\\(\\d+\\)", "nth-child\\(" + i + "\\)")
			WebUI.clearText(settingname)
			xsetText(settingname, name)
			i++
		}
		WebUI.click(findTestObject('Object Repository/4. Creator/Line/Create your Object(s) button'))
	}


	String[] CreateCpIoLineWithFiltersCheckComputing(String mode, int startDay, int endDay)
	{
		/**
		 * This fonction create a campaign, an insertion order and lines using template names from the global variable lineTemplateName
		 * Loaded / New mode can be set in parameter
		 *
		 * @param mode			"loaded" or "new" mode for the campaign and insertion order
		 * @param startDay		* The number of day from today before the line starts diffusing
		 * @param endDay		* The number of day from today before the line stops diffusing
		 *
		 * @return newItems		Array of names of the items created (can be used or archiving)
		 *
		 */
		selenium = getSelenium()
		String[] newItems = new String[99]
		HighlightElement on = new HighlightElement()

		// Select the line creation
		goToCreator()
		selectCreatorDimension("Line")

		String insertionOrderBudgetText = ""
		String insertionOrderBudget = ""
		float insertionOrderBudgetValue = 0.0f
		String insertionOrderBudgetStart = ""
		String insertionOrderBudgetEnd = ""

		if (mode == "loaded") {
			// Select the first campaign template in loaded mode
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'))

			// Select the first OI template in loaded mode
			WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/Creator OI mode button'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Insertion Order/OI mode Loaded'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/OI mode Loaded'))
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template'))

			insertionOrderBudgetText = WebUI.getText(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Loaded Budget text'))
			insertionOrderBudget = Float.valueOf(insertionOrderBudgetText.replaceAll("\\d++ [a-z-A-Z]+ \\d+ - \\d++ [a-z-A-Z]+ \\d+ - (\\d+)", "\$1"))
			insertionOrderBudgetStart = insertionOrderBudgetText.replaceAll("(\\d++ [a-z-A-Z]+ \\d+) - \\d++ [a-z-A-Z]+ \\d+ - \\d+", "\$1")
			insertionOrderBudgetEnd = insertionOrderBudgetText.replaceAll("\\d++ [a-z-A-Z]+ \\d+ - (\\d++ [a-z-A-Z]+ \\d+) - \\d+", "\$1")
		} else if (mode == "new") {
			// Setup Campaign
			WebUI.waitForElementVisible(findTestObject('4. Creator/Item type selector'), 30)
			xsetOption(findTestObject('4. Creator/Campaign/Campaign status select'), "Paused")
			String newCpName = GlobalVariable.itemNames[0] + Instant.now().getEpochSecond()
			xsetText(findTestObject('4. Creator/Campaign/Name'), newCpName)
			newItems[0] = newCpName

			// Fill budget field
			DecimalFormat format = new DecimalFormat("0.00")

			String budget = GlobalVariable.budgetField[0]
			float budgetValue = 0.0f
			if(budget != "") {
				budget = budget.replaceFirst("[,\\.](\\d+\$)", ".\$1")
				budgetValue = Float.valueOf(budget)
			}
			if(budgetValue > 0 || budget == "") {
				xsetText(findTestObject('4. Creator/Campaign/Campaign budget'), budget, 'A2')
				WebUI.click(findTestObject('4. Creator/Campaign/Name'))
				on.on1(findTestObject('4. Creator/Campaign/Campaign budget'))
				if(budgetValue % 1 != 0)
					budget = budget.replaceFirst("(.*)", "\$100").replaceFirst("(\\d{2}).*\$", "\$1")
				//			assert WebUI.getAttribute(findTestObject('4. Creator/Campaign/Campaign budget'), "value") == budget : "Displayed budget value and global variable do not match : " + WebUI.getAttribute(findTestObject('4. Creator/Campaign/Campaign budget'), "value") + " (displayed) =/= " + budget + " (expected)"
				KeywordUtil.logInfo("Campaign budget set : " + budget)
			} else if (budget == "0") { // on ne touche pas au champ
				budget = ""
				assert WebUI.getAttribute(findTestObject('4. Creator/Campaign/Campaign budget'), "value") == ""
			} else {
				KeywordUtil.markFailedAndStop("Global variable 'budgetField' cannot be negative")
			}

			// Disable auto-select & Select IO template
			if(!GlobalVariable.lineTemplateNames.equals(['']) && GlobalVariable.lineTemplateNames != null && !GlobalVariable.lineTemplateNames.equals([]))
				WebUI.click(findTestObject('4. Creator/Line/Auto Select toggle'))
			xsetText(findTestObject('Object Repository/4. Creator/Insertion Order/Search filter'), GlobalVariable.ioTemplateNames[0])
			WebUI.delay(1)
			WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/First Template Displayed'))


			// Setup IO
			xsetOption(findTestObject('4. Creator/Insertion Order/Insertion Order Status select'), "Paused")
			String newIoName = GlobalVariable.itemNames[1] + Instant.now().getEpochSecond()
			xsetText(findTestObject('4. Creator/Insertion Order/Name'), newIoName)
			newItems[1] = newIoName
			insertionOrderBudget = GlobalVariable.budgetField[1]
			if(insertionOrderBudget != "") {
				insertionOrderBudget = insertionOrderBudget.replaceFirst("[,\\.](\\d+\$)", ".\$1")
				insertionOrderBudgetValue = Float.valueOf(insertionOrderBudget)
			}
			if(insertionOrderBudgetValue >= 0 || insertionOrderBudget == "") {
				if(insertionOrderBudget == "") {
					insertionOrderBudget = "20" // Valeur par défaut
					insertionOrderBudgetValue = 20.0f
				}
				WebUI.click(findTestObject('4. Creator/Insertion Order/Insertion Order budget'))
				xsetText(findTestObject('4. Creator/Insertion Order/Insertion Order budget'), Keys.chord(Keys.SHIFT, Keys.ARROW_UP) + insertionOrderBudget.toString(), 'A2', false)
				//				xsetText(findTestObject('4. Creator/Insertion Order/Insertion Order budget'), insertionOrderBudget.toString())

				WebUI.click(findTestObject('4. Creator/Insertion Order/Name'))
				on.on1(findTestObject('4. Creator/Insertion Order/Insertion Order budget'))
				if(insertionOrderBudgetValue % 1 != 0)
					insertionOrderBudget = insertionOrderBudget.replaceFirst("(.*)", "\$100").replaceFirst("(\\d{2}).*\$", "\$1")
				//			assert WebUI.getAttribute(findTestObject('4. Creator/Insertion Order/Insertion Order budget'), "value") == insertionOrderBudget : "Displayed budget value and global variable do not match : " + WebUI.getAttribute(findTestObject('4. Creator/Insertion Order/Insertion Order budget'), "value") + " (displayed) =/= " + insertionOrderBudget + " (expected)"
				KeywordUtil.logInfo("Insertion Order budget set : " + insertionOrderBudget)
			} else {
				KeywordUtil.markFailedAndStop("Global variable 'budgetField' cannot be negative")
			}

			// Setting the end date
			Date date = new Date()
			Calendar calendarEnd = GregorianCalendar.getInstance()
			String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()
			calendarEnd.setTime(date)
			if (endDay > 0)
				calendarEnd.add(Calendar.DATE, endDay)
			String endDateValue = ""
			int endDate = calendarEnd.get(Calendar.DAY_OF_MONTH)

			if (endDate > 0 && endDate <= 31) {
				endDateValue = String.format("%02d", endDate) + " " + months[calendarEnd.get(Calendar.MONTH)] + " " + calendarEnd.get(Calendar.YEAR)
				modifyDate(endDateValue, "Insertion Order", "End")
			}

			// Setting the start date
			Calendar calendar = GregorianCalendar.getInstance()
			calendar.setTime(date)
			if (startDay > 0)
				calendar.add(Calendar.DATE, startDay)
			String startDateValue = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR)

			modifyDate(startDateValue, "Insertion Order", "Start")

			insertionOrderBudgetStart = startDateValue
			insertionOrderBudgetEnd = endDateValue
		} else {
			KeywordUtil.markFailedAndStop('You must specify a creation mode in this method')
		}

		Date insertionOrderBudgetStartDate = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH).parse(insertionOrderBudgetStart)
		Date insertionOrderBudgetEndDate = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH).parse(insertionOrderBudgetEnd)
		int daysDiff = -1

		daysDiff = TimeCategory.minus(insertionOrderBudgetEndDate, insertionOrderBudgetStartDate).days

		int currentLineNumber = 0

		if(!GlobalVariable.lineTemplateNames.equals(['']) && GlobalVariable.lineTemplateNames != null && !GlobalVariable.lineTemplateNames.equals([])) {
			currentLineNumber = GlobalVariable.lineTemplateNames.size()

			for(int i = 0; i < currentLineNumber;i++) {
				xsetText(findTestObject('Object Repository/4. Creator/Line/Search filter'), GlobalVariable.lineTemplateNames[i])
				WebUI.delay(1)
				WebUI.click(findTestObject('4. Creator/Line/First template checked dispayed'))
				WebUI.clearText(findTestObject('Object Repository/4. Creator/Line/Search filter'))
				Thread.sleep(500)
			}
		} else {
			currentLineNumber = selenium.getCssCount(findTestObject('Object Repository/4. Creator/Line/Selected Line Templates').getSelectorCollection().get(SelectorMethod.CSS))
		}

		int numberOfLines = selenium.getCssCount(findTestObject('Object Repository/4. Creator/Line/Selected Line Templates').getSelectorCollection().get(SelectorMethod.CSS))

		assert WebUI.verifyElementClickable(findTestObject('Object Repository/4. Creator/Line/Next Step button')) == true : "Next Step button is not clickable. Some mandatory data are missing"
		WebUI.click(findTestObject('Object Repository/4. Creator/Line/Next Step button'))

		xclick(findTestObject('Object Repository/4. Creator/Line/1. Next step/Same as Insertion Order button'), 'checkbox')

		double computedLifetimeBudget = Math.round((insertionOrderBudgetValue / numberOfLines))
		double computedDailyBudget = Math.round(((insertionOrderBudgetValue / numberOfLines) / (daysDiff + 1)))
		if(computedLifetimeBudget < 1) computedLifetimeBudget = 1
		if(computedDailyBudget < 1) computedDailyBudget = 1

		if(mode == "loaded") {
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Line/1. Next step/Warning message'), 5) == true
			assert WebUI.verifyElementText(findTestObject('Object Repository/4. Creator/Line/1. Next step/Warning message'), "Warning! The pre-computed budgets proposed here are only informative. It's a base for you to configure the budgets accordingly with the rest of the Insertion Order's setup.") == true
		} else {
			assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/4. Creator/Line/1. Next step/Warning message'), 1) == true
		}

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Line/1. Next step/Start date'), 30)
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/4. Creator/Line/1. Next step/Start date'), "value", insertionOrderBudgetStart, 1)
		assert WebUI.verifyElementAttributeValue(findTestObject('Object Repository/4. Creator/Line/1. Next step/End date'), "value", insertionOrderBudgetEnd, 1)

		DecimalFormat format = new DecimalFormat("0.00")

		String lineBudget = GlobalVariable.budgetField[2]
		float budgetValue = 0.0f
		if(lineBudget != "") {
			lineBudget = lineBudget.replaceFirst("[,\\.](\\d+\$)", ".\$1")
			budgetValue = Float.valueOf(lineBudget)
		}

		if(budgetValue < 0 || lineBudget == "")
			budgetValue = 1.00 // Valeur par défaut

		for(int i = 1; i <= currentLineNumber;i++) {
			TestObject currentName = changeSelector(findTestObject('Object Repository/4. Creator/Line/1. Next step/First Name'),"nth-child\\(1\\)","nth-child("+i+")")
			TestObject currentLifetimeBudget = changeSelector(findTestObject('Object Repository/4. Creator/Line/1. Next step/First Lifetime Budget'),"nth-child\\(1\\)","nth-child("+i+")")
			TestObject currentDailyBudget = changeSelector(findTestObject('Object Repository/4. Creator/Line/1. Next step/First Daily Budget'),"nth-child\\(1\\)","nth-child("+i+")")
			if (selenium.isElementPresent(getSelector(currentLifetimeBudget)) == true) {
				assert WebUI.verifyElementAttributeValue(currentLifetimeBudget, "value", computedLifetimeBudget.toString().replaceAll("\\.0+\$", ""), 1) : "The computed value is not correct for the lifetime budget of line n°" + i
				WebUI.clearText(currentLifetimeBudget)
				xsetText(currentLifetimeBudget, budgetValue.toString(), 'A2')
				on.on1(currentLifetimeBudget)
				KeywordUtil.logInfo("Line item n°" + i + " budget set : " + budgetValue.toString())
			}
			if (selenium.isElementPresent(getSelector(currentDailyBudget)) == true) {
				assert WebUI.verifyElementAttributeValue(currentDailyBudget, "value", computedDailyBudget.toString().replaceAll("\\.0+\$", ""), 1) : "The computed value is not correct for the lifetime budget of line n°" + i
				WebUI.clearText(currentDailyBudget)
				xsetText(currentDailyBudget, "1", 'A2')
			}

			String newName = GlobalVariable.itemNames[2] + "_" + i + "_" + Instant.now().getEpochSecond()
			WebUI.clearText(currentName)
			xsetText(currentName, newName)
			newItems[(i+1)] = newName
		}
		// Setting the end date
		Date date = new Date()
		Calendar calendarEnd = GregorianCalendar.getInstance()
		String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()
		calendarEnd.setTime(date)
		calendarEnd.add(Calendar.DATE, endDay)
		String endDateValue = ""
		int endDate = calendarEnd.get(Calendar.DAY_OF_MONTH)

		if (endDate > 0 && endDate <= 31) {
			endDateValue = String.format("%02d", endDate) + " " + months[calendarEnd.get(Calendar.MONTH)] + " " + calendarEnd.get(Calendar.YEAR)
			modifyDate(endDateValue, "Line Item", "End")
		}

		// Setting the start date
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date)
		if (startDay > 0)
			calendar.add(Calendar.DATE, startDay)
		String startDateValue = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR)

		modifyDate(startDateValue, "Line Item", "Start")

		WebUI.click(findTestObject('Object Repository/4. Creator/Line/Create your Object(s) button'))

		return newItems
	}

	def modifyCampaignFieldValue(String fieldName, String fieldNewValue) {
		/**
		 * Modify cp settings in General tab and Advanced tab
		 * 
		 * @param fieldName
		 * 		Name of the field we want to update
		 * @param fieldNewValue
		 * 		Value of the field should be updated
		 */
		def campaignGeneralFields = ['name', 'status', 'budget', 'start date', 'end date']
		def campaignAdvancedFields = ['goal', 'goal kpi', 'goal kpi value']

		if(campaignGeneralFields.contains(fieldName.toLowerCase())) {
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/General button'))
			switch(fieldName.toLowerCase()) {
				case 'name':
					xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Name'), fieldNewValue)
					break;
				case 'status':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Campaign Status'), fieldNewValue)
					break;
				case 'budget':
					xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Campaign Budget'), fieldNewValue)
					break;
				case 'start date':
					modifyDate(fieldNewValue, "Campaign", "Start")
					break;
				case 'end date':
					modifyDate(fieldNewValue, "Campaign", "End")
					break;
			}
		} else if (campaignAdvancedFields.contains(fieldName.toLowerCase())) {
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))
			switch(fieldName.toLowerCase()) {
				case 'goal':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal'), fieldNewValue)
					break;
				case 'goal kpi':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Name'), fieldNewValue)
					break;
				case 'goal kpi value':
					xsetText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), fieldNewValue)
					break;
			}
		} else {
			KeywordUtil.markFailedAndStop("Second parameter is not recognized")
		}
		KeywordUtil.markPassed("New '" + fieldName + "' : '" + fieldNewValue + "'")
	}
	def addBudgetIntervalNoOverlap(int endDaysAdded)
	{
		SimpleDateFormat simpleDate = new SimpleDateFormat('dd MMM yyyy', Locale.ENGLISH)
		Calendar cal = Calendar.getInstance()
		WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Insertion Order'))
		String ioNewStartDateValue = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Last Budget Interval End Date button'),'_value')
		Date ioEndDate = simpleDate.parse(ioNewStartDateValue)
		Date today = new Date()
		if (ioEndDate.compareTo(today) < 0) { // If last budget interval date is in the past, we take present date
			ioNewStartDateValue = simpleDate.format(today)
			ioEndDate = simpleDate.parse(ioNewStartDateValue)
		} else if (ioEndDate.compareTo(today) >= 0) { // If last budget interval date is in the future, we only add one day
			Date ioNewStartDate = simpleDate.parse(ioNewStartDateValue)
			cal.setTime(ioNewStartDate)
			cal.add(Calendar.DAY_OF_MONTH, 1)
			ioNewStartDate = cal.getTime()
			ioNewStartDateValue = simpleDate.format(ioNewStartDate)
			ioEndDate = simpleDate.parse(ioNewStartDateValue)
		}
		WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Add budget interval button'))
		String ioNewLastBudget = (Integer.parseInt(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Last Budget field'),'value')) + 1).toString()
		WebUI.clearText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Last Budget field'))
		Thread.sleep(500)
		WebUI.setText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Last Budget field'), ioNewLastBudget)

		// Modify Insertion Order End date (Start date + endDaysAdded)
		cal.setTime(ioEndDate)
		if(endDaysAdded <= 0) endDaysAdded = 1
		cal.add(Calendar.DAY_OF_MONTH, endDaysAdded)
		ioEndDate = cal.getTime()
		String ioNewLastEndDateValue = simpleDate.format(ioEndDate)
		modifyDate(ioNewLastEndDateValue, 'Insertion Order', 'End')

		// Modify Insertion Order Start date (No overlap with other budget intervals)
		modifyDate(ioNewStartDateValue, 'Insertion Order', 'Start')


		// Return all budget intervals infos
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver, baseUrl)
		int budgetIntervalsCount = selenium.getCssCount(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget Intervals').getSelectorCollection().get(SelectorMethod.CSS).replaceFirst('css=', ''))
		ArrayList budgetIntervals = new ArrayList(budgetIntervalsCount)
		int inc = 1
		String ioCurrentStartDateValue
		String ioCurrentEndDateValue
		String ioNewStartDateToCheck
		String ioNewEndDateToCheck
		while (inc <= budgetIntervalsCount) {
			budgetIntervals.add(new ArrayList())
			budgetIntervals.get(inc - 1).add('(')
			budgetIntervals.get(inc - 1).add(WebUI.getAttribute(changeSelector(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget field'),'nth-of-type\\(\\d+\\)', ('nth-of-type(' + inc) + ')'), '_value'))
			budgetIntervals.get(inc - 1).add(';')
			WebUI.doubleClick(changeSelector(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget Interval Start Date'),'nth-of-type\\(\\d+\\)', ('nth-of-type(' + inc) + ')'))
			String ioCurrentBudgetIntervalStartMonth = WebUI.getText(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Header Month'))
			ioCurrentStartDateValue = WebUI.getAttribute(changeSelector(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget Interval Start Date'),'nth-of-type\\(\\d+\\)', ('nth-of-type(' + inc) + ')'), '_value')
			ioNewStartDateToCheck = ioCurrentStartDateValue.replaceAll('(\\d+) (.+) (\\d+)', '$2\\/$1\\/$3')
			budgetIntervals.get(inc - 1).add(ioNewStartDateToCheck.replaceAll('([a-zA-Z]+)', String.format('%02d', Month.valueOf(ioCurrentBudgetIntervalStartMonth.toUpperCase()).getValue())))
			budgetIntervals.get(inc - 1).add(';')
			WebUI.doubleClick(changeSelector(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget Interval End Date'),'nth-of-type\\(\\d+\\)', ('nth-of-type(' + inc) + ')'))
			String ioCurrentBudgetIntervalEndMonth = WebUI.getText(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Header Month'))
			ioCurrentEndDateValue = WebUI.getAttribute(changeSelector(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget Interval End Date'),'nth-of-type\\(\\d+\\)', ('nth-of-type(' + inc) + ')'), '_value')
			ioNewEndDateToCheck = ioCurrentEndDateValue.replaceAll('(\\d+) (.+) (\\d+)', '$2\\/$1\\/$3')
			budgetIntervals.get(inc - 1).add(ioNewEndDateToCheck.replaceAll('([a-zA-Z]+)', String.format('%02d', Month.valueOf(ioCurrentBudgetIntervalEndMonth.toUpperCase()).getValue())))
			budgetIntervals.get(inc - 1).add(';);')
			inc = (inc + 1)
		}

		String budgetIntervalsToCheck = ''

		for (ArrayList budgetInterval : budgetIntervals) {
			for (String element : budgetInterval) {
				budgetIntervalsToCheck += element
			}
		}

		return budgetIntervalsToCheck
	}
	def setRandomFrequencyCapping()
	{
		WebUI.doubleClick(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))

		//WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping switch'))
		String frequencyCapping = toUpperLowerCase(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping switch'),'class').contains('ivu-switch-checked').toString())
		String frequencyExposure = '0'
		String frequencyPeriod = 'Minutes'
		String frequencyAmount = '0'
		int randomIndex = 0
		if (frequencyCapping.equals('True')) {
			String currentPeriod = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping period select'),'value')
			if (currentPeriod.equals('Lifetime')) {
				randomIndex = (new Random().nextInt((4 - 0) + 1) + 1)
			} else {
				randomIndex = 5
			}
			WebUI.selectOptionByIndex(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping period select'),randomIndex)
			frequencyPeriod = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping period select'),'value')
			frequencyExposure = (Integer.parseInt(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping exposure input'),'value')) + 1).toString()
			WebUI.sendKeys(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping exposure input'),Keys.chord(Keys.CONTROL, 'a'))
			WebUI.sendKeys(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping exposure input'),Keys.chord(Keys.BACK_SPACE))
			WebUI.setText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping exposure input'),frequencyExposure)
			if (randomIndex != 5) {
				frequencyAmount = (Integer.parseInt(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping per input'),'value')) + 1).toString()
				WebUI.sendKeys(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping per input'),Keys.chord(Keys.CONTROL, 'a'))
				WebUI.sendKeys(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping per input'),Keys.chord(Keys.BACK_SPACE))
				WebUI.setText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping per input'),frequencyAmount)
			}
		}

		return [frequencyCapping, frequencyExposure, frequencyPeriod, frequencyAmount]
	}

	def campaignShareSettings(String fieldToShare, int cpToShareWithListIndex, int itemToShareWithObjectsIndex) {
		def fieldsToShare = ["name", "status", "goal", "goal kpi", "goal kpi value", "budget", "start", "end", "geography_targeting_include", "geography_targeting_exclude", "language_targeting_include", "language_targeting_exclude", "viewability_targeting_ad_position_include", "viewability_targeting_ad_position_exclude", "inventory_source_targeting_include", "inventory_source_targeting_exclude"]

		int fieldIndex = fieldsToShare.indexOf(fieldToShare.toLowerCase()) + 1
		WebUI.click(findTestObject('5. Editor/3. Operations/1. Share Settings/Campaign Share Settings button'))
		WebUI.click(findTestObject('5. Editor/3. Operations/1. Share Settings/Share Settings first property'))
		WebUI.click(changeSelector(findTestObject('5. Editor/3. Operations/1. Share Settings/Share Settings first item'), "nth-child\\(1\\)", "nth-child("+ cpToShareWithListIndex +")"))
		WebUI.click(findTestObject('5. Editor/3. Operations/1. Share Settings/Share Settings Apply button'))

		verifyCPSharedSettings(fieldToShare, fieldIndex, cpToShareWithListIndex, itemToShareWithObjectsIndex)
	}
	def verifyCPSharedSettings(String fieldToShare, int fieldIndex, int cpToShareWithListIndex, int cpToShareWithObjectsIndex) {
		WebUI.click(findTestObject('5. Editor/3. Operations/1. Share Settings/Campaign Share Settings button'))

		switch(fieldToShare.toLowerCase()) {
			case 'name':
				String campaignNameToShare = WebUI.getText(changeSelector(findTestObject('5. Editor/3. Operations/1. Share Settings/Share Settings first property'), "nth-child\\(1\\)", "nth-child("+ fieldIndex +")")).replaceFirst("Name : ", "")
				String sharedCampaignId = WebUI.getText(changeSelector(findTestObject('5. Editor/3. Operations/1. Share Settings/Share Settings first item'), "nth-child\\(1\\)", "nth-child("+ cpToShareWithListIndex +")")).replaceFirst("(\\d+).*", "\$1")
				WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Campaign'))
				WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/General button'))
				String sharedCampaignName = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Name'), "value")
				String campaignToShareWithName = WebUI.getText(changeSelector(findTestObject('Object Repository/6. Objects/First Loaded Object name'), "nth-child\\(1\\)", "nth-child("+ cpToShareWithObjectsIndex +")"))

				assert sharedCampaignName.equals(campaignNameToShare) == true
				assert campaignToShareWithName.equals(campaignNameToShare + " (" + sharedCampaignId + ")") == true
				break;
			case 'status':
				break;
			case 'goal':
				break;
			case 'goal kpi':
				break;
			case 'goal kpi value':
				break;
			case 'budget':
				break;
			case 'start':
				break;
			case 'end':
				break;
			case 'geography_targeting_include':
				break;
			case 'geography_targeting_exclude':
				break;
			case 'language_targeting_include':
				break;
			case 'language_targeting_exclude':
				break;
			case 'viewability_targeting_ad_position_include':
				break;
			case 'viewability_targeting_ad_position_exclude':
				break;
			case 'inventory_source_targeting_include':
				break;
			case 'inventory_source_targeting_exclude':
				break;
		}
		WebUI.click(changeSelector(findTestObject('Object Repository/6. Objects/First Loaded Object name'), "nth-child\\(1\\)", "nth-child("+ cpToShareWithObjectsIndex +")"))
		KeywordUtil.markPassed("Settings '"+ fieldToShare +"' was shared properly with item number " + cpToShareWithObjectsIndex + " in the list of Objects")
	}
	@Keyword
	def modifyIoFieldValue(String fieldName, String fieldNewValue) {
		/**
		 * Modify io settings in General tab and Advanced tab
		 * 	 	
		 * @param fieldName
		 * 		Name of the field we want to update
		 * @param fieldNewValue
		 * 		Value of the field should be updated
		 */
		def ioGeneralFields = ['name', 'status', 'budget interval budget', 'start date', 'end date']
		def ioAdvancedFields = ['performance goal type', 'performance goal value', 'pacing', 'pacing rate', 'daily budget', 'frequency', 'exposure', 'per', 'period']

		if(ioGeneralFields.contains(fieldName.toLowerCase())) {
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/General button'))
			switch(fieldName.toLowerCase()) {
				case 'name':
					xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Insertion Order Name'), fieldNewValue)
					break;
				case 'status':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Insertion Order Status'), fieldNewValue)
					break;
				case 'budget interval budget':
					xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Last Budget field'), fieldNewValue, 'A2')
					break;
				case 'start date':
					modifyDate(fieldNewValue, "Insertion Order", "Start")
					break;
				case 'end date':
					modifyDate(fieldNewValue, "Insertion Order", "End")
					break;
			}
		} else if (ioAdvancedFields.contains(fieldName.toLowerCase())) {
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))
			switch(fieldName.toLowerCase()) {
				case 'performance goal type':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), fieldNewValue)
					break;
				case 'performance goal value':
					if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Type'), 'value') == 'Other')
						xsetText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), fieldNewValue)
					else
						xsetText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Performance Goal/Value'), fieldNewValue, 'A2')
					break;
				case 'pacing':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Pacing/Pacing selector'), fieldNewValue)
					break
				case 'pacing rate':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Pacing/Pacing Rate Selector'), fieldNewValue)
					break
				case 'daily budget':
					WebUI.clearText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Pacing/Daily budget'))
					Thread.sleep(500)
					xsetText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Pacing/Daily budget'), fieldNewValue, 'A2')
					break
				case 'frequency':
				//TODO
					break
				case 'exposure':
					xsetText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping exposure input'), Keys.chord(Keys.SHIFT, Keys.ARROW_UP) + fieldNewValue, '', false)
					break
				case 'per':
					xsetText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping per input'), Keys.chord(Keys.SHIFT, Keys.ARROW_UP) + fieldNewValue, '', false)
					break
				case 'period':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Insertion Order/Frequency Capping/Frequency Capping period select'), fieldNewValue)
					break
			}
		} else {
			KeywordUtil.markFailedAndStop("Second parameter is not recognized")
		}
		KeywordUtil.markPassed("New '" + fieldName + "' : '" + fieldNewValue + "'")
	}
	@Keyword
	def modifyLiFieldValue(String fieldName, String fieldNewValue) {
		/**
		 * Modify li settings in General tab and Advanced tab
		 *
		 * @param fieldName
		 * 		Name of the field we want to update
		 * @param fieldNewValue
		 * 		Value of the field should be updated
		 */
		def liGeneralFields = ['name', 'status', 'start date', 'end date', 'budget type', 'lifetime budget', 'pacing', 'pacing rate', 'daily budget']
		def liAdvancedFields = ['bid strategy type', 'bid strategy value', 'bid strategy unit', 'bid strategy do not exceed', 'frequency', 'exposure', 'per', 'period']

		if(liGeneralFields.contains(fieldName.toLowerCase())) {
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/General button'))
			switch(fieldName.toLowerCase()) {
				case 'name':
					xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Name'), fieldNewValue)
					break;
				case 'status':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Status'), fieldNewValue)
					break;
				case 'start date':
					modifyDate(fieldNewValue, "Line Item", "Start")
					break;
				case 'end date':
					modifyDate(fieldNewValue, "Line Item", "End")
					break;
				case 'budget type':
					WebUI.selectOptionByValue(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Budget Type'), fieldNewValue, false)
					break;
				case 'lifetime budget':
					WebUI.clearText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Lifetime budget'))
					WebUI.delay(1)
					fieldNewValue.contains('.') == true ? xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Lifetime budget'), fieldNewValue, 'A2') : xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Lifetime budget'), fieldNewValue)
					break;
				case 'pacing':
					WebUI.selectOptionByValue(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Pacing'), fieldNewValue, false)
					break;
				case 'pacing rate':
					WebUI.selectOptionByValue(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Pacing rate'), fieldNewValue, false)
					break;
				case 'daily budget':
					WebUI.clearText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Daily Budget'))
					WebUI.delay(1)
					fieldNewValue.contains('.') == true ? xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Daily Budget'), fieldNewValue, 'A2') : xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Daily Budget'), fieldNewValue)
					break;
			}
		} else if (liAdvancedFields.contains(fieldName.toLowerCase())) {
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))
			switch(fieldName.toLowerCase()) {
				case 'bid strategy type':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Line item/Bid Strategy Type'), fieldNewValue)
					break;
				case 'bid strategy value':
					fieldNewValue.contains('.') == true ? xsetText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Line item/Bid Strategy Value'), fieldNewValue, 'A2') : xsetText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Line item/Bid Strategy Value'), fieldNewValue)
					break;
				case 'bid strategy unit':
					xsetOption(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Line item/Bid Strategy Unit'), fieldNewValue)
					break;
				case 'bid strategy do not exceed':
				// TODO
					break;
				case 'frequency':
				// TODO
					break;
				case 'exposure':
				// TODO
					break;
				case 'per':
				// TODO
					break;
				case 'period':
				// TODO
					break;
			}
		} else {
			KeywordUtil.markFailedAndStop("Second parameter is not recognized")
		}
		KeywordUtil.markPassed("New '" + fieldName + "' : '" + fieldNewValue + "'")
	}

	def campaignQuickDuplicate() {
		WebDriver driver = DriverFactory.getWebDriver()
		int oldNumberOfLoadedItems = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/Loaded Items').getSelectorCollection().get(SelectorMethod.CSS))).size()
		WebUI.click(findTestObject('5. Editor/3. Operations/2. Quick Duplicate/CP Quick Duplicate'))
		WebUI.waitForElementClickable(findTestObject('5. Editor/3. Operations/2. Quick Duplicate/Yes'), 5)
		WebUI.click(findTestObject('5. Editor/3. Operations/2. Quick Duplicate/Yes'))
		int newNumberOfLoadedItems = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/Loaded Items').getSelectorCollection().get(SelectorMethod.CSS))).size()
		assert newNumberOfLoadedItems == oldNumberOfLoadedItems + 1 : "Campaign not duplicated"
		String campaignToDuplicateName = WebUI.getText(changeSelector(findTestObject('Object Repository/6. Objects/First Loaded Object name'), "nth-child\\(1\\)", "nth-child("+ oldNumberOfLoadedItems +")")).replaceFirst(" \\((\\d+)\\)", "")
		String duplicatedCampaignName = WebUI.getText(changeSelector(findTestObject('Object Repository/6. Objects/First Loaded Object name'), "nth-child\\(1\\)", "nth-child("+ newNumberOfLoadedItems +")")).replaceFirst(" \\((\\d+)\\)", "")

		assert campaignToDuplicateName + ' - Copy' == duplicatedCampaignName : "Campaign duplicated has a different name"

		WebUI.click(changeSelector(findTestObject('Object Repository/6. Objects/First Loaded Object name'), "nth-child\\(1\\)", "nth-child("+ newNumberOfLoadedItems +")"))
		KeywordUtil.markPassed("Campaign was duplicated")
	}
	@Keyword
	File download()
	{
		/**
		 * Download zip file from builder
		 *
		 * @return
		 * 		File object contains zip file path
		 *
		 */
		WebUI.click(findTestObject('Object Repository/6. Objects/Upload Button'))
		String customName = "file" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip"
		WebUI.delay(1)
		WebUI.executeJavaScript("document.querySelector('#download-btn').download = '" + customName + "'", null)
		Thread.sleep(500)
		WebUI.click(findTestObject('Object Repository/7. Uploader/Download zip button'))
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/7. Uploader/Second Loading icon'), 180) == true : "Download did not start"

		String firstMessage = WebUI.getText(findTestObject('Object Repository/7. Uploader/First message'))
		String secondMessage = WebUI.getText(findTestObject('Object Repository/7. Uploader/Second message'))
		assert !firstMessage.contains("Cannot create SDF file. Please contact engineering team with technical information") : "Download failed: Cannot create SDF file. Please contact engineering team with technical information: cannot create SDF file for task XXXXXX"
		assert !secondMessage.contains("Cannot create SDF file. Please contact engineering team with technical information") : "Download failed: Cannot create SDF file. Please contact engineering team with technical information: cannot create SDF file for task XXXXXX"

		WebUI.waitForElementNotPresent(findTestObject('Object Repository/7. Uploader/Second Loading icon'), 180)

		// Waiting for file download
		String sdfPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator
		File file = new File(sdfPath + customName)
		Thread.sleep(500)
		int attempts = 10
		while(file.exists() == false && attempts > 0) {
			WebUI.delay(5)
			attempts--
		}
		assert file.exists() == true : "SDF not downloaded"

		KeywordUtil.markPassed("SDF downloaded")

		return (file)
	}
	@Keyword
	def checkCsvByName(String csvFullPath, int row = 1, String columnName, String valueToCompare) {
		/**
		 * Check values into CSV
		 *
		 * @param csvFullPath
		 * 		Path of zip file
		 * @param row
		 * 		row of the CSV we want to check
		 * @param columnName
		 * 		Name of the column we want to check
		 * @param valueToCompare
		 * 		Value we want to compare with that of the CSV
		 *
		 */
		KeywordUtil.logInfo("Checking CSV file ...")
		CSVData csvData = new CSVData(csvFullPath, true, CSVSeparator.COMMA)
		assert csvData.getValue(columnName, row).contains(valueToCompare) : "The value "+valueToCompare+" does not match with "+csvData.getValue(columnName, row)+", column "+columnName+", row "+row+" into the csv is not correct"
		KeywordUtil.logInfo("Values in CSV: "+csvData.getValue(columnName, row) +" contains: "+valueToCompare+"")
	}
	@Keyword
	ArrayList<String> unzip(File fileZip){
		/**
		 * Unzip file function
		 *
		 * @param
		 * 		Zip file path
		 *
		 * @return
		 * 		ArrayList includes zip and CSVs files paths
		 *
		 */
		KeywordUtil.logInfo("Unziping file ...")

		File destDir = new File(fileZip.getParent())
		byte[] buffer = new byte[1024]
		ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip.getPath()))
		ZipEntry zipEntry = zis.getNextEntry()
		ArrayList <String> sdfpaths = new ArrayList <>()
		sdfpaths.add(fileZip.getPath())

		while (zipEntry != null) {
			File newFile = newFile(destDir, zipEntry)
			sdfpaths.add(newFile.getPath())

			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					throw new IOException("Failed to create directory " + newFile)
				}
			} else {
				// fix for Windows-created archives
				File parent = newFile.getParentFile()
				if (!parent.isDirectory() && !parent.mkdirs()) {
					throw new IOException("Failed to create directory " + parent)
				}

				// write file content
				FileOutputStream fos = new FileOutputStream(newFile)
				int len
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len)
				}
				fos.close()
			}
			zipEntry = zis.getNextEntry()
		}
		zis.closeEntry()
		zis.close()

		return sdfpaths
	}

	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}

	def clearCPDate(String endOrStart)
	{
		/**
		 * Clears Campaign date
		 * 
		 * @param endOrStart	Date to clear : "End" or "Start" date
		 */
		WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/General button'))
		WebUI.mouseOver(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Campaign '+ toUpperLowerCase(endOrStart) +' Date clear button'))
		WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Campaign '+ toUpperLowerCase(endOrStart) +' Date clear button'))
	}

	@Keyword
	def clickDate(String itemType, String startOrEnd, boolean option = true)
	{
		/**
		 * this function click on the right object for the date for CP, OI, Line
		 * if option is true it verifies the input after setting it
		 * if option is false it is just a click
		 * By default the option is set at true. 
		 *
		 *@param itemType
		 *The type of item for the date (CP, OI, Li)
		 *
		 *@param startOrEnd
		 *The string used to determine if it's the start or end date button
		 *
		 *@param option
		 *The option to determine if the input is verified after setting the input.
		 */
		String view = WebUI.getText(findTestObject('6. Objects/Active view type'))
		KeywordUtil.logInfo('mode = ' + view)
		KeywordUtil.logInfo('itemType = ' + itemType)

		if (view == 'Editor')
		{
			if (itemType == 'Campaign')
				option == true ? xclick(findTestObject('5. Editor/1. Settings/1. General/' + itemType + '/Campaign ' + startOrEnd + ' Date input'), 'calendar') : WebUI.click(findTestObject('5. Editor/1. Settings/1. General/' + itemType + '/Campaign ' + startOrEnd + ' Date input'))
			else if (itemType == 'Insertion Order')
				option == true ? xclick(findTestObject('5. Editor/1. Settings/1. General/' + itemType + '/Last Budget Interval ' + startOrEnd + ' Date button'), 'calendar') : WebUI.click(findTestObject('5. Editor/1. Settings/1. General/' + itemType + '/Last Budget Interval ' + startOrEnd + ' Date button'))
			else if (itemType == 'Line Item')
				option == true ? xclick(findTestObject('5. Editor/1. Settings/1. General/' + itemType + '/' + startOrEnd + ' Date button'), 'calendar') : WebUI.click(findTestObject('5. Editor/1. Settings/1. General/' + itemType + '/' + startOrEnd + ' Date button'))
		}
		else if (view == 'Creator')
		{
			if (itemType == 'Campaign')
				option == true ? xclick(findTestObject('4. Creator/Campaign/Campaign ' + startOrEnd + ' Date'), 'calendar') : WebUI.click(findTestObject('4. Creator/Campaign/Campaign ' + startOrEnd + ' Date'))
			else if (itemType == 'Insertion Order')
				option == true ? xclick(findTestObject('4. Creator/Insertion Order/Insertion Order ' + startOrEnd + ' Date button'), 'calendar') : WebUI.click(findTestObject('4. Creator/Insertion Order/Insertion Order ' + startOrEnd + ' Date button'))
			else if (itemType == 'Line Item')
				option == true ? xclick(findTestObject('4. Creator/Line/1. Next step/' + startOrEnd + ' date'), 'calendar') : WebUI.click(findTestObject('4. Creator/Line/1. Next step/' + startOrEnd + ' date'))
		}

	}

	@Keyword
	def modifyDate(String startDateValue, String itemType, String startOrEnd)
	{
		/*
		 * 	Modifies start or end date depending on second parameter
		 * 
		 * @param startDateValue 	Start date to set. Format : dd MM YYYY (ie. "06 Jul 2020")
		 * @param itemType 			Type of item to modify : "Line Item", "Campaign" or "Insertion Order"
		 * @param startOrEnd		Type of date to modify : "Start" or "End" or "Last Budget Interval Start" or "Last Budget Interval End"
		 */

		//Select Year
		String year = startDateValue.replaceFirst("\\d++ [a-z-A-Z]+ (\\d+)", "\$1")
		int yearIndex = Integer.valueOf(year.replaceFirst("\\d\\d\\d(\\d)", "\$1")) + 1
		int decade = Integer.valueOf(year.replaceFirst("\\d\\d(\\d)\\d", "\$1"))

		clickDate(itemType, startOrEnd, false)
		//WebUI.doubleClick(findTestObject('5. Editor/1. Settings/1. General/' + itemType + '/' + startOrEnd + ' Date button'))
		WebUI.click(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Header Year'))
		int currentDecade = Integer.valueOf(WebUI.getText(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Years')).replaceFirst("\\d\\d(\\d)\\d", "\$1"))
		int inc = decade - currentDecade
		while (inc > 0) {
			WebUI.click(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Next Decade'))
			inc = inc - 1
		}
		WebUI.click(changeSelector(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Years'), "nth-child\\(\\d+\\)", "nth-child\\(" + yearIndex + "\\)"))

		//Select Month
		String month = startDateValue.replaceFirst("\\d+ ([a-z-A-Z]+) \\d+", "\$1")
		List<String> months = Arrays.asList(new DateFormatSymbols(new Locale("en", "GB")).getShortMonths())
		int monthIndex = months.indexOf(month) + 1
		WebUI.click(changeSelector(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Months'), "nth-child\\(\\d+\\)", "nth-child\\(" + monthIndex + "\\)"))

		//Select Day
		int dayIndex = Integer.valueOf(startDateValue.replaceFirst("(\\d+) [a-z-A-Z]+ \\d+", "\$1"))
		int dayOffset = 1
		String firstValue = WebUI.getText(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Days'))
		while(!firstValue.equals("1")) {
			firstValue = WebUI.getText(changeSelector(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Days'), "nth-of-type\\(\\d+\\)", "nth-of-type\\(" + dayOffset + "\\)"))
			if(!firstValue.equals("1")) dayOffset++
		}
		dayIndex += dayOffset - 1

		WebUI.click(changeSelector(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Days'), "nth-of-type\\(\\d+\\)", "nth-of-type\\(" + dayIndex + "\\)"))
		clickDate(itemType, startOrEnd, true)

	}
	@Keyword
	def modifyDate(String dateValue, TestObject selector)
	{
		//Select Year
		String year = dateValue.replaceFirst("\\d++ [a-z-A-Z]+ (\\d+)", "\$1")
		int yearIndex = Integer.valueOf(year.replaceFirst("\\d\\d\\d(\\d)", "\$1")) + 1
		int decade = Integer.valueOf(year.replaceFirst("\\d\\d(\\d)\\d", "\$1"))
		WebUI.doubleClick(selector)
		WebUI.click(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Header Year'))
		int currentDecade = Integer.valueOf(WebUI.getText(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Years')).replaceFirst("\\d\\d(\\d)\\d", "\$1"))
		int inc = decade - currentDecade
		while (inc > 0) {
			WebUI.click(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Next Decade'))
			inc = inc - 1
		}
		WebUI.click(changeSelector(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Years'), "nth-child\\(\\d+\\)", "nth-child\\(" + yearIndex + "\\)"))

		//Select Month
		String month = dateValue.replaceFirst("\\d+ ([a-z-A-Z]+) \\d+", "\$1")
		List<String> months = Arrays.asList(new DateFormatSymbols(new Locale("en", "GB")).getShortMonths())
		int monthIndex = months.indexOf(month) + 1
		WebUI.click(changeSelector(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Months'), "nth-child\\(\\d+\\)", "nth-child\\(" + monthIndex + "\\)"))

		//Select Day
		int dayIndex = Integer.valueOf(dateValue.replaceFirst("(\\d+) [a-z-A-Z]+ \\d+", "\$1"))
		int dayOffset = 1
		String firstValue = WebUI.getText(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Days'))
		while(!firstValue.equals("1")) {
			firstValue = WebUI.getText(changeSelector(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Days'), "nth-of-type\\(\\d+\\)", "nth-of-type\\(" + dayOffset + "\\)"))
			if(!firstValue.equals("1")) dayOffset++
		}
		dayIndex += dayOffset - 1

		WebUI.click(changeSelector(findTestObject('5. Editor/1. Settings/Date Picker/Date Picker Days'), "nth-of-type\\(\\d+\\)", "nth-of-type\\(" + dayIndex + "\\)"))
	}
	def addDate(String itemType, String startOrEnd, int daysAdded) {
		String currentDateString = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/'+itemType+'/'+itemType+' '+startOrEnd+' Date input'), '_value')
		SimpleDateFormat simpleDate = new SimpleDateFormat('dd MMM yyyy', Locale.ENGLISH)
		Calendar cal = Calendar.getInstance()
		Date currentDate
		if (currentDateString != '') {
			currentDate = simpleDate.parse(currentDateString)
		} else {
			currentDate = simpleDate.parse(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/'+itemType+'/'+itemType+' Start Date input'),'_value'))
		}
		cal.setTime(currentDate)
		if(daysAdded <= 0) daysAdded = 1
		cal.add(Calendar.DAY_OF_MONTH, daysAdded)
		currentDate = cal.getTime()
		String dateValue = simpleDate.format(currentDate)
		modifyDate(dateValue, itemType, startOrEnd)

		return dateValue
	}

	public String toUpperLowerCase(String string) {
		// Retourne un string commençant par une majuscule et suivi de minuscules
		return string.charAt(0).toUpperCase().toString() + string.substring(1).toLowerCase()
	}
	@Keyword
	def AssignCreatives(String type, String name, TestObject Line)
	{
		/**
		 * This function include or remove a creative from a line
		 * 
		 * @param type
		 * the type of inclusion (Include or Exclude)
		 * 
		 * @param name
		 * The name of the creative to be assigned/removed
		 * 
		 * @param Line
		 * The line to assign/remove a creative
		 */
		WebUI.click(Line)
		WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/Assign Creatives button'))
		if (type.equals('Include') == true)
		{
			if(name != "") xsetText(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/Search Include'), name)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/First Crea To Include'), 30) == true : "Creative does not exist"
			WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/First Crea To Include'))
			WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/Include button'))
		}
		else if (type.equals('Exclude') == true)
		{
			if(name != "") xsetText(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/Search Remove Selected'), name)
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/First Crea To Remove'), 30) == true : "Creative does not exist"
			WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/First Crea To Remove'))
			WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/Remove Selected button'))
		}

	}
	@Keyword
	def CheckSettingsApplied(String property, String sharedfrom, TestObject sharedto)
	{
		/**
		 * This function check that shared settings has been correctly applied
		 * Only handle name and creative at the moment
		 * 
		 * @param property
		 * The setting to be verified
		 * 
		 * @param shared
		 * The value of the property that was shared
		 */
		switch (property)
		{
			case "Name" :
				WebUI.click(sharedto)
				assert WebUI.getAttribute(findTestObject('5. Editor/1. Settings/1. General/Insertion Order/Insertion Order Name'), "value").equals(sharedfrom) == true : "The name was not shared"
				break

			case "creative_assignments" :
				WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/Assign Creatives button'))
				WebDriver driver = DriverFactory.getWebDriver()
				ArrayList<WebElement> creativesorigin = driver.findElements(By.cssSelector(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/Creative Inclusions').getSelectorCollection().get(SelectorMethod.CSS)))
				WebUI.click(sharedto)
				ArrayList<WebElement> creativesDest = driver.findElements(By.cssSelector(findTestObject('Object Repository/5. Editor/3. Operations/0. Assign Creatives/Creative Inclusions').getSelectorCollection().get(SelectorMethod.CSS)))
				for (int i = 0; i < creativesDest.size(); i++)
					assert creativesorigin[i].getText() == creativesDest[i].getText() : "Creatives Dupplicated are differents"
				assert creativesorigin.size() == creativesDest.size() : "Number of creative are different after duplicate"
				break
		}
	}
	@Keyword
	def QuickDuplicate(String TypeObject, TestObject ObjectToDuplicate)
	{
		/**
		 * this function quickduplicate an object
		 * 
		 * @param TypeObject
		 * the type of the object (CP, OI, LI)
		 * 
		 * @param ObjectToDuplicate
		 * the object to duplicate
		 **/

		WebDriver driver = DriverFactory.getWebDriver()
		int beforeduplicate = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/'+TypeObject+'s').getSelectorCollection().get(SelectorMethod.CSS))).size()
		WebUI.click(ObjectToDuplicate)
		WebUI.click(findTestObject('5. Editor/3. Operations/2. Quick Duplicate/'+TypeObject+' Quick Duplicate'))
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/5. Editor/3. Operations/2. Quick Duplicate/Yes'), 5) == true : "Impossible to confirm duplicate"
		WebUI.click(findTestObject('Object Repository/5. Editor/3. Operations/2. Quick Duplicate/Yes'))
		assert WebUI.waitForElementPresent(changeSelector(ObjectToDuplicate, "nth-child\\(\\d+\\)", "nth-child(" + (beforeduplicate+1) + ")"), 5) : "Duplicate did not work"
		KeywordUtil.logInfo(TypeObject + " was correctly duplicated")
	}

	@Keyword
	def renameObj(TestObject tobj, String renamed, String type)
	{
		/**
		 * this function rename an OI
		 * 
		 * @param tobj
		 * the object to be renamed
		 * 
		 * @param renamed
		 * the new name for the OI
		 **/
		WebUI.click(tobj)
		WebUI.delay(1)
		WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/General button'))
		WebUI.delay(1)
		if (type == 'OI')
			xsetText(findTestObject('5. Editor/1. Settings/1. General/Insertion Order/Insertion Order Name'), renamed)
		else if (type == 'LI')
			xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Line Item/Name'), renamed)
		else if (type == 'CP')
			xsetText(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Name'), renamed)
	}
	@Keyword
	def removeAllExcludedElements(){
		/**
		 * 
		 * Remove all excluded element in Audience section
		 * 
		 */

		if (WebUI.waitForElementPresent(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First Excluded item'), 2)) {
			WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Select all excluded items'))
			WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Remove Excluded button'))
		} else {
			KeywordUtil.logInfo("No excluded elements")
		}
	}
	@Keyword
	def checkObjectStatus(List exptectedTag){

		/**
		 * Check CP,  IO and LI tag. It should be LOADED, EDITED or CREATED
		 * User should put a list of expected tags
		 *
		 * @param exptectedTag
		 * 		List of the expected object Tags should be displayed in DVS
		 *
		 */

		KeywordUtil.logInfo("Check object tags")

		String[] tab1 = new String[exptectedTag.size]
		String[] tab = new String[exptectedTag.size]
		int j=0

		for (int l=0;l<exptectedTag.size;l++){
			String item = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/6. Objects/All tags'))+"').item("+l+").textContent", null)
			tab1[l] = item
		}

		for (int i=0; i<tab1.length; i++) {
			if (tab1[i].trim() == "LOADED"){
				KeywordUtil.logInfo("LOADED")
				tab[j]= tab1[i].trim()
				j++
			} else if (tab1[i].trim() == "EDITED") {
				KeywordUtil.logInfo("EDITED")
				tab[j]= tab1[i].trim()
				j++
			} else if (tab1[i].trim() == "CREATED") {
				KeywordUtil.logInfo("CREATED")
				tab[j]= tab1[i].trim()
				j++
			}
		}


		for (int k=0; k < exptectedTag.size; k++) {

			assert tab[k].equals(exptectedTag[k]) : "Tag "+tab[k]+" not equal with the expected: "+exptectedTag[k]+""
		}
	}
	@Keyword
	def emptyBuilder(){

		/**
		 * Delete all created or loaded objects with empty builder button
		 *
		 */

		KeywordUtil.logInfo("Empty builder")

		assert WebUI.waitForElementPresent(findTestObject('Object Repository/6. Objects/Empty builder button'), 5) : "Empty builder button is not visible"
		WebUI.click(findTestObject('Object Repository/6. Objects/Empty builder button'))
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/6. Objects/Empty builder yes button'), 5) : "Cannot click on yes button"
		WebUI.click(findTestObject('Object Repository/6. Objects/Empty builder yes button'))
		WebUI.delay(2)
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/6. Objects/cp container selector'), 3) : "Empty builder failed"
		KeywordUtil.markPassed("Object are deleted with success")
	}
	@Keyword
	def checkLoadedStatus()
	{
		/**
		 * this function check that all object loaded have the loaded status
		 *
		 */
		//WebUI.click(findTestObject('Object Repository/6. Objects/Tag remove'))
		//WebUI.delay(1)

		WebDriver driver = DriverFactory.getWebDriver()
		ArrayList<WebElement> Alltags = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/All tags').getSelectorCollection().get(SelectorMethod.CSS)))
		ArrayList<WebElement> Brighttags = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/Tag bright').getSelectorCollection().get(SelectorMethod.CSS)))

		assert Alltags.size() != 0 : "No object loaded"
		assert Alltags.size() == Brighttags.size() : "All objects are not in loaded state"
	}
	@Keyword
	int getnumberofTag(String status)
	{
		/**
		 * this function count the number of object for a status
		 * @param status
		 * The status used to get the number of element
		 */
		WebDriver driver = DriverFactory.getWebDriver()
		ArrayList<WebElement> Alltags = driver.findElements(By.cssSelector(findTestObject('Object Repository/6. Objects/All tags').getSelectorCollection().get(SelectorMethod.CSS)))

		int counttag = 0
		for (def elem in Alltags)
		{
			if (elem.getText().trim().equals(status) == true)
				counttag++
		}
		return (counttag)
	}
	@Keyword
	def VerifyStatus(int expectedcreated, int expectededited, int expectedloaded)
	{
		/**
		 * this function check that the status are correct
		 * @param expectedcreated
		 * the number of status created expected
		 * 
		 * @param expectededited
		 * the number of status edited expected
		 * 
		 * @param expectedloaded
		 * the number of status loaded expected
		 */

		assert getnumberofTag('LOADED') == expectedloaded: "The number of loaded status is not correct"
		assert getnumberofTag('EDITED') == expectededited: "The number of edited status is not correct"
		assert getnumberofTag('CREATED') == expectedcreated: "The number of created status is not correct"
	}
	@Keyword
	def VerifyLoadedTag()
	{
		/**
		 * this function verify if the loaded filter only display loaded object
		 * @param specifictest
		 * The ticket ID of a test to verify a special behavior  
		 */
		WebUI.click(findTestObject('Object Repository/6. Objects/Tag remove'))
		WebUI.delay(1)
		int loaded = getnumberofTag('LOADED')
		WebUI.click(findTestObject('Object Repository/6. Objects/Tag Loaded'))
		assert loaded == getnumberofTag('LOADED'): "The number of loaded object are different"

	}
	@Keyword
	def VerifyCreatedTag()
	{
		/**
		 * this function verify if the created filter only display created object
		 * @param specifictest
		 * The ticket ID of a test to verify a special behavior 
		 */
		WebUI.click(findTestObject('Object Repository/6. Objects/Tag remove'))
		WebUI.delay(1)
		int created = getnumberofTag('CREATED')
		WebUI.click(findTestObject('Object Repository/6. Objects/Tag Created'))
		assert created == getnumberofTag('CREATED'): "The number of created object are different"
	}
	@Keyword
	def VerifyEditedTag()
	{
		/**
		 * this function verify if the edited filter only display edited object
		 * @param specifictest
		 * The ticket ID of a test to verify a special behavior 
		 */
		WebUI.click(findTestObject('Object Repository/6. Objects/Tag remove'))
		WebUI.delay(1)
		int edited = getnumberofTag('EDITED')
		WebUI.click(findTestObject('Object Repository/6. Objects/Tag Edited'))
		assert edited == getnumberofTag('EDITED'): "The number of edited object are different"
	}

	@Keyword
	def SelectAllLinePanel()
	{
		/**
		 * this function select all line in the object panel
		 */
		if (!findTestObject('6. Objects/Object Panel Tree header list'))
			KeywordUtil.markFailedAndStop("No line loaded")

		WebUI.click(findTestObject('6. Objects/Object Panel Select all line'))
	}

	@Keyword
	ArrayList<TestObject> GetSelectedLinePanel()
	{
		/**
		 * this function return the line objects that are selected in the panel
		 */
		ArrayList<TestObject> lineselected = []
		String result
		driver = DriverFactory.getWebDriver()
		ArrayList<WebElement> allLinesContainer = driver.findElements(By.cssSelector(findTestObject('6. Objects/Object Panel All Li Container').getSelectorCollection().get(SelectorMethod.CSS)))
		TestObject it_name = findTestObject('6. Objects/Object Panel line name iterator')
		TestObject it_checkbox = findTestObject('6. Objects/Object Panel line checkbox iterator')
		for (int i = 2; i < allLinesContainer.size() + 2; i++)
		{
			it_name = changeSelector(it_name, 'nth-child\\(\\d+\\)', 'nth-child(' + i + ')')
			it_checkbox = changeSelector(it_checkbox, 'nth-child\\(\\d+\\)', 'nth-child(' + i + ')')
			result = WebUI.getAttribute(it_checkbox, 'className')
			if (result.contains('checked') == true)
			{
				TestObject tmp = new TestObject(WebUI.getText(it_name))
				tmp = WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), it_name.getSelectorCollection().get(SelectorMethod.CSS), true)
				lineselected.add(tmp)
			}

		}
		return (lineselected)
	}

	@Keyword
	ArrayList<TestObject> SelectLinePanel(String selectiontype, String selectionvalue)
	{
		/**
		 * this function select the object that match the parameters
		 * 
		 * @param selection type
		 * The type of search for the line
		 * possible value:
		 * 'tag' => search line by tag
		 * 'name' => search line by name
		 *
		 *@param selectionvalue
		 * the value to be selected
		 * possible values:
		 * 'CREATED'
		 * 'LOADED'
		 * 'EDITED'
		 * 	'the name of a line'
		 */
		driver = DriverFactory.getWebDriver()
		String result
		ArrayList<TestObject> lineselected = []
		ArrayList<WebElement> allLinesContainer = driver.findElements(By.cssSelector(findTestObject('6. Objects/Object Panel All Li Container').getSelectorCollection().get(SelectorMethod.CSS)))
		TestObject it_name = findTestObject('6. Objects/Object Panel line name iterator')
		TestObject it_tag = findTestObject('6. Objects/Object Panel line tag iterator')
		TestObject it_checkbox = findTestObject('6. Objects/Object Panel line checkbox iterator')

		KeywordUtil.logInfo('number of line = ' + allLinesContainer.size())
		for (int i = 2; i < allLinesContainer.size() + 2; i++)
		{
			it_name = changeSelector(it_name, 'nth-child\\(\\d+\\)', 'nth-child(' + i + ')')
			it_tag = changeSelector(it_tag, 'nth-child\\(\\d+\\)', 'nth-child(' + i + ')')
			it_checkbox = changeSelector(it_checkbox, 'nth-child\\(\\d+\\)', 'nth-child(' + i + ')')
			if (selectiontype == "name") result = WebUI.getText(it_name)
			if (selectiontype == "tag")	result = WebUI.getText(it_tag)

			KeywordUtil.logInfo('result = ' + result + " / selectionvalue = " + selectionvalue)
			if (result.contains(selectionvalue) == true)
			{
				KeywordUtil.logInfo('Line selected by '+ selectiontype +': ' + result)
				WebUI.click(it_checkbox)
				TestObject tmp = new TestObject()
				tmp = WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), it_name.getSelectorCollection().get(SelectorMethod.CSS), true)
				lineselected.add(tmp)
			}
		}

		return (lineselected)
	}

	@Keyword
	def VerifyPanelLine(String verificationtype = '', ArrayList<String> expected, String line)
	{
		/**
		 * This function verify if specific line in the panel is selected/unselected and has the correct tag
		 * 
		 * @param verificationtype
		 * Determine the behavior of the methode
		 * possible values:
		 * '' => the method verify both the status and selection
		 * 'tag' => the method verify just the status
		 * 'checkbox' => the method verify just the selection
		 * 
		 * @param expected
		 * A list of value to be expected
		 * possible values:
		 * ['EDITED']
		 * ['CREATED']
		 * ['LOADED']
		 * ['true']
		 * ['false']
		 * if verifytype is '' a combinaison of 'true' or 'false' and 'EDITED' or 'CREATED' or 'LOADED' ex : ['false', 'CREATED']
		 * 
		 */
		driver = DriverFactory.getWebDriver()
		String result

		ArrayList<WebElement> allLinesContainer = driver.findElements(By.cssSelector(findTestObject('6. Objects/Object Panel All Li Container').getSelectorCollection().get(SelectorMethod.CSS)))
		TestObject it_name = findTestObject('6. Objects/Object Panel line name iterator')
		TestObject it_tag = findTestObject('6. Objects/Object Panel line tag iterator')
		TestObject it_checkbox = findTestObject('6. Objects/Object Panel line checkbox iterator')
		boolean found = false
		KeywordUtil.logInfo('number of line on the panel = ' + allLinesContainer.size())
		for (int i = 2; i < allLinesContainer.size() + 2; i++)
		{
			it_name = changeSelector(it_name, 'nth-child\\(\\d+\\)', 'nth-child(' + i + ')')
			it_tag = changeSelector(it_tag, 'nth-child\\(\\d+\\)', 'nth-child(' + i + ')')
			it_checkbox = changeSelector(it_checkbox, 'nth-child\\(\\d+\\)', 'nth-child(' + i + ')')
			KeywordUtil.logInfo('names = ' + WebUI.getText(it_name))
			KeywordUtil.logInfo('Searching for line = ' + line)
			if (WebUI.getText(it_name).contains(line) == true)
			{
				if (verificationtype == '')
				{
					KeywordUtil.logInfo('line iterator = ' + i)
					KeywordUtil.logInfo('checked = ' + WebUI.getAttribute(it_checkbox, 'className'))
					KeywordUtil.logInfo('tag = ' + WebUI.getText(it_tag))
					result = WebUI.getAttribute(it_checkbox, 'className')
					if (expected[0] == 'true')
						assert result.contains('checked') == true : "The checkbox is " + result + " and was supposed to be " + expected[0] + " and supposed to contain checked"
					else if (expected[0] == 'false')
						assert result.contains('checked') == false : "The checkbox is " + result + " and was supposed to be " + expected[0] + " and supposed to not contain checked"
					result = WebUI.getText(it_tag)
					assert result == expected[1] : "The status is " + result + " and was supposed to be " + expected[1]
				}
				else if (verificationtype == "tag")
				{
					result = WebUI.getText(it_tag)
					assert result == expected[0] : "The status is " + result + " and was supposed to be " + expected[0]
				}
				else if (verificationtype == "checkbox")
				{
					result = WebUI.getAttribute(it_checkbox, 'className')
					if (expected[0] == 'true')
						assert result.contains('checked') == true : "The checkbox is " + result + " and was supposed to be " + expected[0] + " and supposed to contain checked"
					else if (expected[0] == 'false')
						assert result.contains('checked') == false : "The checkbox is " + result + " and was supposed to be " + expected[0] + " and supposed to not contain checked"
				}
				found = true
				break
			}
		}
		assert found == true : line + " was not found in the panel"
	}

	@Keyword
	def VerifySelectAllLinePanel(String check, String status)
	{
		/**
		 * this function verify if all lines are selected/unselected and have the correct status in the panel
		 * @param check
		 * determine if the line is selected/unselected or is not tested
		 * possible value: 
		 * null => selection is not verified
		 * true => verify if the line is selected
		 * false => verify if the line is unselected
		 * 
		 * @param status
		 * the status to be verified
		 * possible value:
		 * null => status is not verified
		 * "LOADED" => verify if the line is loaded
		 * "CREATED" => verify if the line is created
		 * "EDITED" => verify if the line is edited
		 */

		driver = DriverFactory.getWebDriver()

		if (check != null)
		{
			KeywordUtil.logInfo('Verifying checkbox')
			ArrayList<WebElement> allLinesCheckbox = driver.findElements(By.cssSelector(findTestObject('6. Objects/Object Panel all line checkbox').getSelectorCollection().get(SelectorMethod.CSS)))
			String result
			for (def elem in allLinesCheckbox)
			{
				result = elem.getAttribute('className')
				KeywordUtil.logInfo('result = ' + result)
				if (check == 'true')
					assert result.contains('checked') == true : "A Line not selected after a select all"
				else if (check == 'false')
					assert result.contains('checked') == false : " A Line is selected after an unselect all"
			}
		}

		if (status != null)
		{
			KeywordUtil.logInfo('Verifying status')
			ArrayList<WebElement> allLinesTags = driver.findElements(By.cssSelector(findTestObject('6. Objects/Object Panel all line tag').getSelectorCollection().get(SelectorMethod.CSS)))
			for (def elem in allLinesTags)
			{
				KeywordUtil.logInfo('text = ' + elem.getText())
				assert elem.getText() == status : "Line status is not " + status + " it is : " + elem.getText()
			}
		}
	}

	@Keyword
	def LinePanelActivate()
	{
		/**
		 * this function activate the selected line
		 */
		if (!findTestObject('6. Objects/Object Panel Tree header list'))
			KeywordUtil.markFailedAndStop("No line loaded")

		WebUI.click(findTestObject('6. Objects/Object Panel Activate button'))
	}

	@Keyword
	def LinePanelPause()
	{
		/**
		 * this function pause the line selected in the panel
		 */
		if (!findTestObject('6. Objects/Object Panel Tree header list'))
			KeywordUtil.markFailedAndStop("No line loaded")

		WebUI.click(findTestObject('6. Objects/Object Panel Pause button'))
	}

	@Keyword
	def CheckLinePanelStatus(String status, ArrayList<TestObject> lines)
	{
		/**
		 * this function verifie the status in the editor
		 * @param status
		 * the status to be verified
		 * 
		 * @param lines
		 * The object line to verify the status
		 */
		for (def elem in lines)
		{
			WebUI.click(elem)
			assert WebUI.getAttribute(findTestObject('5. Editor/1. Settings/1. General/Line Item/Status'), 'value') == status : "Line Status is not " + status
		}
		WebUI.scrollToElement(findTestObject('6. Objects/Object Panel Tree header list'), 30)
	}

	@Keyword
	def LinePanelDuplicate()
	{
		/**
		 * this function duplicate the selected line
		 */
		if (!findTestObject('6. Objects/Object Panel Tree header list'))
			KeywordUtil.markFailedAndStop("No line loaded")

		WebUI.click(findTestObject('6. Objects/Object Panel Duplicate button'))
	}

	@Keyword
	def CheckLinePanelDuplicated(ArrayList<TestObject> lines)
	{
		/**
		 * this function verify the duplication of the selected line
		 * @params lines
		 * The selected object line before duplication
		 */

		int loadedline = 0
		for (def elem in lines)
		{
			if (WebUI.getText(elem).contains(GlobalVariable.items[2]) == true)
				loadedline = 1
		}
		KeywordUtil.logInfo('number of line = ' +  getnumberofTag('CREATED') + ' line size *2 = ' + lines.size() * 2)
		assert getnumberofTag('CREATED') + loadedline == lines.size() * 2 : "The number of line does not match, there should be " + (lines.size() * 2) + ' lines'

		for (def elem in lines)
		{
			KeywordUtil.logInfo('line original = ' + WebUI.getText(elem))
			String duplicata = WebUI.getText(elem).replaceFirst("\\(\\d+\\)", "- Copy ")
			KeywordUtil.logInfo('line duplicated = ' + duplicata)
			VerifyPanelLine('', ['false', 'CREATED'], duplicata)
		}
	}

	@Keyword
	def createNewCampaignGeneric(String itemName, String itemStatus, String itemBudget, int startDateOffset, int endDateOffset) {
		/**
		 * Create new campaign
		 * 
		 * @param itemName
		 * 		New campaign name
		 * @param itemStatus
		 * 		Status of the campaign
		 * @param startDateOffset
		 * 		Campaign offset start date
		 * @param endDateOffset
		 * 		Campaign offset end date
		 */
		HighlightElement on = new HighlightElement()
		KeywordUtil.logInfo("Create new Campaign")

		if (WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Item type selector'), 2) && WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Header'), 2)) {

			KeywordUtil.logInfo("Open CP panel")

			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Creator button'), 5)
			WebUI.click(findTestObject('Object Repository/4. Creator/Creator button'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Item type selector'), 2) : "Cannot open creator mode"
		}

		xsetOption(findTestObject('4. Creator/Campaign/Campaign status select'), itemStatus)
		xsetText(findTestObject('4. Creator/Campaign/Name'), itemName)

		DecimalFormat format = new DecimalFormat("0.00")

		String budget = GlobalVariable.budgetField[0]
		float budgetValue = 0.0f
		if(budget != "") {
			budget = budget.replaceFirst("[,\\.](\\d+\$)", ".\$1")
			budgetValue = Float.valueOf(budget)
		}
		if(budgetValue > 0 || budget == "") {
			xsetText(findTestObject('4. Creator/Campaign/Campaign budget'), budget, 'A2')
			WebUI.click(findTestObject('4. Creator/Campaign/Name'))
			on.on1(findTestObject('4. Creator/Campaign/Campaign budget'))
			WebUI.delay(1)
			if(budgetValue % 1 != 0)
				budget = budget.replaceFirst("(.*)", "\$100").replaceFirst("(\\d{2}).*\$", "\$1")
			assert WebUI.getAttribute(findTestObject('4. Creator/Campaign/Campaign budget'), "value") == budget : "Displayed budget value and global variable does not match : " + WebUI.getAttribute(findTestObject('4. Creator/Campaign/Campaign budget'), "value") + " (displayed) =/= " + budget + " (expected)"
			KeywordUtil.logInfo("Campaign budget set : " + budget)
		} else if (budget == "0") { // on ne touche pas au champ
			budget = ""
			WebUI.delay(1)
			assert WebUI.getAttribute(findTestObject('4. Creator/Campaign/Campaign budget'), "value") == ""
		} else {
			KeywordUtil.markFailedAndStop("Global variable 'budgetField' cannot be negative")
		}

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

		if (endDate > 0 && endDate <= 31) {
			endDateValue = endDate + " " + months[calendarEnd.get(Calendar.MONTH)] + " " + calendarEnd.get(Calendar.YEAR)
			modifyDate(endDateValue, "Campaign", "End")
		}

		ArrayList<String> newCampaignSetup = new ArrayList<String>(5)
		newCampaignSetup.add(itemName)
		newCampaignSetup.add(itemStatus)
		newCampaignSetup.add(budget)
		newCampaignSetup.add(calendar.getTime().format('MM/dd/yyyy') + " 00:00")
		newCampaignSetup.add(calendarEnd.getTime().format('MM/dd/yyyy') + " 23:59")
	}
	@Keyword
	def createLoadedCampaignGeneric(){

		/**
		 * Create a loaded campaign
		 * 
		 */

		KeywordUtil.logInfo("Load a Campaign")

		if (WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Item type selector'), 2) && WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Header'), 2)) {

			KeywordUtil.logInfo("Open CP panel")

			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Creator button'), 5)
			WebUI.click(findTestObject('Object Repository/4. Creator/Creator button'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Item type selector'), 2) : "Cannot open creator mode"
		}

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode button'))
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Creator Campaign mode Loaded'))

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'), 10) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Campaign/Campaign First Template'))

	}

	@Keyword
	def createNewIoGeneric(String stateCp, String OIname, int OIPosition) {
		/**
		 * Create a new Insertion order
		 *
		 * @param stateCp
		 * 		Loaded or New campaign
		 * @param OIname
		 * 		Name of the insertion order
		 * @param OIPosition
		 * 		position of the insertion order chosen
		 *
		 */
		HighlightElement on = new HighlightElement()
		Date date = new Date()
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date)
		String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()

		KeywordUtil.logInfo("Create new Insertion order")

		if (WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Header'), 1) && WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Line/Line title'), 1)) {
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Creator button'), 5)
			WebUI.click(findTestObject('Object Repository/4. Creator/Creator button'))

			KeywordUtil.logInfo("Open OI panel")

			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Item type selector'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Item type selector'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Creator dimension Insertion Order'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/Creator dimension Insertion Order'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Header'), 2) : "Cannot open Insertion order panel"
		}

		if (stateCp == "Loaded"){
			createLoadedCampaignGeneric()
		} else if (stateCp == "New"){
			createNewCampaignGeneric("Test_Auto_Campaign_Create_And_Edit_" + date.getTime(), "Paused", "1", 0, 1)
		}
		if (WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Line/Auto Select toggle true'), 1))
			WebUI.click(findTestObject('Object Repository/4. Creator/Line/Auto select toggle'))

		WebUI.click(changeSelector(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template'), 'nth-child\\(\\d\\)', 'nth-child(' + OIPosition + ')'))
		xsetText(findTestObject('Object Repository/4. Creator/Insertion Order/Name'), OIname)
		WebUI.delay(1)
		String insertionOrderBudget = GlobalVariable.budgetField[1]
		float insertionOrderBudgetValue = 0.0f
		if(insertionOrderBudget != "") {
			insertionOrderBudget = insertionOrderBudget.replaceFirst("[,\\.](\\d+\$)", ".\$1")
			insertionOrderBudgetValue = Float.valueOf(insertionOrderBudget)
		}
		if(insertionOrderBudgetValue >= 0 || insertionOrderBudget == "") {
			if(insertionOrderBudget == "") insertionOrderBudget = "3" // Valeur par défaut
			WebUI.click(findTestObject('4. Creator/Insertion Order/Insertion Order budget'))
			xsetText(findTestObject('4. Creator/Insertion Order/Insertion Order budget'), Keys.chord(Keys.SHIFT, Keys.ARROW_UP) + insertionOrderBudget, 'A2', false)
			WebUI.click(findTestObject('4. Creator/Insertion Order/Name'))
			on.on1(findTestObject('4. Creator/Insertion Order/Insertion Order budget'))
			WebUI.delay(1)
			if(insertionOrderBudgetValue % 1 != 0)
				insertionOrderBudget = insertionOrderBudget.replaceFirst("(.*)", "\$100").replaceFirst("(\\d{2}).*\$", "\$1")
			assert WebUI.getAttribute(findTestObject('4. Creator/Insertion Order/Insertion Order budget'), "value") == insertionOrderBudget : "Displayed budget value and global variable do not match : " + WebUI.getAttribute(findTestObject('4. Creator/Insertion Order/Insertion Order budget'), "value") + " (displayed) =/= " + insertionOrderBudget + " (expected)"
			KeywordUtil.logInfo("Insertion Order budget set : " + insertionOrderBudget)
		} else {
			KeywordUtil.markFailedAndStop("Global variable 'budgetField' cannot be negative")
		}
	}

	@Keyword
	def createLoadedIoGeneric(String stateCp, int OIPosition, String IO){
		/**
		 * Create a loaded insertion order and select the first IO
		 * 
		 * @param stateCp
		 * 		Loaded or New campaign
		 * @param OIPosition
		 * 		Position of loaded oi we want to select
		 * 
		 * 
		 */

		Date date = new Date()
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date)
		String[] months = new DateFormatSymbols(new Locale("en", "GB")).getShortMonths()
		String[] budgetText = []
		String currencyBuilder
		HighlightElement on = new HighlightElement()

		KeywordUtil.logInfo("Load an Insertion order")

		if (WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Header'), 1) && WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Line/Line title'), 1)) {
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Creator button'), 5)
			WebUI.click(findTestObject('Object Repository/4. Creator/Creator button'))

			KeywordUtil.logInfo("Open OI panel")

			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Item type selector'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Item type selector'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Creator dimension Insertion Order'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/Creator dimension Insertion Order'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order Header'), 2) : "Cannot open Insertion order panel"
		}

		if (stateCp == "Loaded"){
			createLoadedCampaignGeneric()
		} else if (stateCp == "New"){
			createNewCampaignGeneric(stateCp, "Test_Auto_Campaign_Create_And_Edit_" + date.getTime(), "Paused", "1", 0, 1)
		}

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Insertion Order/Creator OI mode button'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/Creator OI mode button'))

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Insertion Order/OI mode Loaded'), 5) == true
		WebUI.click(findTestObject('Object Repository/4. Creator/Insertion Order/OI mode Loaded'))

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template'), 10) == true
		WebUI.click(changeSelector(findTestObject('Object Repository/4. Creator/Insertion Order/Insertion Order First Template'), 'nth-child\\(\\d\\)', 'nth-child(' + OIPosition + ')'))

		assert WebUI.waitForElementVisible(findTestObject('4. Creator/Insertion Order/Loaded io budget field'), 5) : "No budget segment avalaible for this insertion order"
		budgetText = WebUI.getText(findTestObject('4. Creator/Insertion Order/Loaded io budget field')).split("-")

		on.on1(findTestObject('4. Creator/Insertion Order/Loaded io budget field'))
		currencyBuilder = budgetText[2].replaceAll("[\\d.]+", "").trim()
		checkCurrency(currencyBuilder)
		compareCurrencyWithDB(currencyBuilder, IO)
	}

	@Keyword
	int createNewLineGeneric(String stateCp, String stateIo, String OIname, int OIPosition, String TemNumbers, String IO) {
		/**
		 * Create a new Line item in DVS. We create a new cp and a new Oi before that
		 *
		 *@param stateCp
		 *		"Loaded" or "New" campaign
		 *@param stateIo
		 *		"Loaded" or "New" IO
		 *@param OIname
		 *		Name of the new IO
		 *@param OIPosition
		 *		Position of the Oi we want to check
		 *@param TemNumbers
		 *		Number of templates we want to select
		 *
		 *@return
		 *		We return the total number of lines selected (Lines selected with Oi and lines selected manually)
		 *
		 */

		WebDriver driver = DriverFactory.getWebDriver()
		KeywordUtil.logInfo("Create new Line item")

		if (WebUI.waitForElementNotPresent(findTestObject('Object Repository/4. Creator/Line/Line title'), 2)) {
			assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Creator button'), 30)
			WebUI.click(findTestObject('Object Repository/4. Creator/Creator button'))

			KeywordUtil.logInfo("Open Li panel")

			assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Item type selector'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Item type selector'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Line/Creator dimension Line'), 5) == true
			WebUI.click(findTestObject('Object Repository/4. Creator/Line/Creator dimension Line'))

			assert WebUI.waitForElementPresent(findTestObject('Object Repository/4. Creator/Line/Line title'), 2) : "Cannot open Line item panel"
		}

		if (stateIo == "Loaded"){
			createLoadedIoGeneric(stateCp, OIPosition, IO)
		} else if (stateIo == "New"){
			createNewIoGeneric(stateCp, OIname, OIPosition)
		}

		ArrayList<WebElement> allCheckedElements = driver.findElements(By.cssSelector(findTestObject('Object Repository/4. Creator/Line/template checkbox').getSelectorCollection().get(SelectorMethod.CSS)))

		//if TemNumbers = All, on selectionne tout les templates
		if (TemNumbers == "All"){
			KeywordUtil.logInfo("All templates will be selected")
			TemNumbers = allCheckedElements.size
		} else {
			KeywordUtil.logInfo("Some templates will be selected")
		}

		int numberOfChecked = 0
		int numberOfUnchecked = 0
		int totalLines = 0

		for (int i=0; i < Integer.parseInt(TemNumbers); i++ ) {

			if (WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/4. Creator/Line/template checkbox'))+"').item("+i+").checked", null)) {
				KeywordUtil.logInfo("Template n°"+(i+1)+" already checked")
				numberOfChecked ++
			} else {
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/4. Creator/Line/template checkbox'))+"').item("+i+").focus()", null)
				WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/4. Creator/Line/template checkbox'))+"').item("+i+").click()", null)
				numberOfUnchecked ++
			}
		}

		//Check if other lines are selected if TemNumbers != All
		for (int j=Integer.parseInt(TemNumbers); j<allCheckedElements.size; j++) {
			WebUI.executeJavaScript("document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/4. Creator/Line/template checkbox'))+"').item("+j+").focus()", null)

			if (WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/4. Creator/Line/template checkbox'))+"').item("+j+").checked", null)) {
				numberOfChecked ++
			}
		}

		totalLines = numberOfChecked + numberOfUnchecked

		WebUI.delay(2)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Creator/Create your Object(s) button'), 2) : "Next step button is not clickable"
		WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))

		ArrayList<WebElement> allCheckedLineItems = driver.findElements(By.cssSelector(findTestObject('Object Repository/4. Creator/Settings/All line items').getSelectorCollection().get(SelectorMethod.CSS)))
		assert totalLines.equals(allCheckedLineItems.size) : "Number of line item selected is different than number of lines in settings menu"

		return totalLines
	}

	@Keyword
	int csvCountLine(String fullCsvPath){
		/**
		 * Count number of line in the CSV
		 * 
		 * @param fullCsvPath
		 * 		Full path of the CSV
		 * 
		 * @return count
		 * 		Number of lines
		 * 
		 */

		KeywordUtil.logInfo("Number of lines counting in the CSV in progress...")

		int count = 0;
		String str = "";

		FileInputStream fis = new FileInputStream(fullCsvPath);
		LineNumberReader l = new LineNumberReader(
				new BufferedReader(new InputStreamReader(fis)));
		while ((str=l.readLine())!=null)
		{
			count = l.getLineNumber() - 1;
		}
		fis.close()
		return count
	}
	@Keyword
	def checkObjectStatus(String expectedIoTag, String expectedLiTag){

		/**
		 * Check IO and LI tag. It should be LOADED or EDITED
		 *
		 * @param expectedIoTag
		 * 			Io tag should be diaplayed in builder front
		 * @param expectedLiTag
		 * 			Li tag should be diaplayed in builder front
		 *
		 */

		KeywordUtil.logInfo("Check object tags")

		String returnIo = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/6. Objects/IO status'))+"').textContent", null)
		String returnLi = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/6. Objects/LI status'))+"').textContent", null)

		String ioTag = returnIo.trim()
		String liTag = returnLi.trim()

		KeywordUtil.markPassed("IoTag = "+ ioTag)
		KeywordUtil.markPassed("liTag = "+ liTag)

		assert ioTag.equals(expectedIoTag) : "IO tag "+ ioTag + " has not the expected. It should be "+ expectedIoTag +""
		assert liTag.equals(expectedLiTag) : "LI tag "+ liTag + " has not the expected. It should be "+ expectedLiTag +""
	}
	@Keyword
	int[] checkNumberOfIncludedExcludedAudiences(){

		/**
		 * Check the number of excluded and inluded audiences are same before and after the test
		 *
		 * @return tab
		 * 		return the number of Included/Excluded audiences for IO and LI
		 *
		 */

		WebDriver driver = DriverFactory.getWebDriver()
		int[] tab = [0, 0]

		if (WebUI.waitForElementPresent(findTestObject('5. Editor/2. Targeting/6. Audience/All Li excluded audiences'), 2)) {

			ArrayList<WebElement> allExcluded = driver.findElements(By.cssSelector(findTestObject('5. Editor/2. Targeting/6. Audience/All Li excluded audiences').getSelectorCollection().get(SelectorMethod.CSS)))
			KeywordUtil.logInfo(allExcluded.size + " excluded audiences")
			tab[0] = allExcluded.size

		} else if (WebUI.waitForElementPresent(findTestObject('5. Editor/2. Targeting/6. Audience/All Io excluded audiences'), 2)) {

			ArrayList<WebElement> allExcluded = driver.findElements(By.cssSelector(findTestObject('5. Editor/2. Targeting/6. Audience/All Io excluded audiences').getSelectorCollection().get(SelectorMethod.CSS)))
			KeywordUtil.logInfo(allExcluded.size + " excluded audiences")

			tab[0] = allExcluded.size

		} else {

			KeywordUtil.logInfo("No excluded audiences")

		}


		if (WebUI.waitForElementPresent(findTestObject('5. Editor/2. Targeting/6. Audience/All Li included audiences'), 2)) {

			ArrayList<WebElement> allIncluded = driver.findElements(By.cssSelector(findTestObject('5. Editor/2. Targeting/6. Audience/All Li included audiences').getSelectorCollection().get(SelectorMethod.CSS)))
			KeywordUtil.logInfo(allIncluded.size + " included audiences")
			tab[1] = allIncluded.size

		} else if (WebUI.waitForElementPresent(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/All Io included audiences'), 2)) {

			ArrayList<WebElement> allIncluded = driver.findElements(By.cssSelector(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/All Io included audiences').getSelectorCollection().get(SelectorMethod.CSS)))
			KeywordUtil.logInfo(allIncluded.size + " included audiences")
			tab[1] = allIncluded.size

		} else {

			KeywordUtil.logInfo("No included audiences")

		}

		return tab
	}
	@Keyword
	int checkNumberOfIncludedConversionPixels() {

		/**
		 * Check the number included conversion pixels are same before and after the test
		 *
		 * @return numberOfIncludedPixel
		 * 		return the number of Included audiences for IO and LI. It return 0 if no included pixels
		 *
		 */

		int numberOfIncludedPixel = 0
		WebDriver driver = DriverFactory.getWebDriver()

		WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/Conversion Pixel tab'))
		if (WebUI.waitForElementPresent(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/All conversion pixel included items'), 10)) {
			ArrayList<WebElement> allIncluded = driver.findElements(By.cssSelector(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/All conversion pixel included items').getSelectorCollection().get(SelectorMethod.CSS)))
			return numberOfIncludedPixel = allIncluded.size

		} else {

			KeywordUtil.logInfo("No pixels are included")
			return numberOfIncludedPixel
		}
	}
	def checkLineSameAsIO()
	{
		selectItem("Insertion Order", 1)
		String ioStartDate = WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Insertion Order/Budget Interval Start Date'), 'value')
		String ioLastEndDate = WebUI.getAttribute(findTestObject('5. Editor/1. Settings/1. General/Insertion Order/Last Budget Interval End Date button'), 'value')
		selectItem("Line Item", 1)
		assert WebUI.getAttribute(findTestObject('5. Editor/1. Settings/1. General/Line Item/Start Date button'), 'value') == ioStartDate
		assert WebUI.getAttribute(findTestObject('5. Editor/1. Settings/1. General/Line Item/End Date button'), 'value') == ioLastEndDate
	}

	@Keyword
	def checkOnDatabase(String sql){
		/**
		 * Get data from the builder database through sql command
		 *
		 * @param sql
		 * 		SQL command
		 * @return dbTestData.getData()
		 * 		List of data maches with sql command from the table
		 *
		 */
		KeywordUtil.logInfo("Check on DVS database")
		DBData dbTestData = findTestData('currency database')
		dbTestData.query = sql

		KeywordUtil.logInfo("Query used= "+dbTestData.query)
		dbTestData.fetchedData = dbTestData.fetchData()
		KeywordUtil.logInfo("Database= "+dbTestData.fetchedData[0][0])

		return (dbTestData.fetchedData[0])[0]

	}

	@Keyword
	def checkCurrency(String currency){
		/**
		 *
		 * Check builder currency or database currency and budget type
		 * @param currency
		 * 		third value parsed from IO budget filed or database value
		 *
		 */

		switch(currency){
			case '€':
			case 'EUR':
				KeywordUtil.logInfo("Currency "+currency+" is euro")
				break
			case '$':
			case 'USD':
				KeywordUtil.logInfo("Currency "+currency+" is dollar")
				break
			case 'Dhs':
			case 'AED':
				KeywordUtil.logInfo("Currency "+currency+" is arabic emirates dirham")
				break
			case '£':
			case 'GBP':
				KeywordUtil.logInfo("Currency "+currency+" is pound sterling")
				break
			case 'R$':
			case 'BRL':
				KeywordUtil.logInfo("Currency "+currency+" is Brasil real")
				break
			case 'imprs':
			case 'Impressions':
				KeywordUtil.logInfo("Budget type is impression")
				break
			case 'Amount':
				KeywordUtil.logInfo("Budget type is amount")
				break
			default:
				KeywordUtil.markErrorAndStop("Currency value is not correct: "+currency+"")
		}
	}

	@Keyword
	def compareCurrencyWithDB(String currencyBuilder, String IO){
		/**
		 *Compare builder currency with database currency for the same advertiser and check budget type if impression
		 *
		 *@param currencyBuilder
		 *		currency get from builder
		 *@param IO
		 *		ID of the insertion order
		 */
		KeywordUtil.logInfo("Compare builder currency with database currency and budget type")

		String currencyDB = ""
		if(GlobalVariable.advertiserName.matches("\\d+"))
			currencyDB = checkOnDatabase('SELECT currency_code FROM dv360_advertiser WHERE id="'+GlobalVariable.advertiserName+'"')
		else
			currencyDB = checkOnDatabase('SELECT currency_code FROM dv360_advertiser WHERE name="'+GlobalVariable.advertiserName+'"')

		checkCurrency(currencyDB)

		String budgetTypeDB = checkOnDatabase('SELECT budget_type FROM dv360_insertion_order_'+ GlobalVariable.advertiserVersion.replaceFirst('\\.', '_') +' WHERE io_id="'+IO+'"')
		checkCurrency(budgetTypeDB)

		if(currencyBuilder== '€' || currencyBuilder=='EUR' && currencyDB== 'EUR'){
			KeywordUtil.logInfo("Currency euro is correct")
		}
		else if(currencyBuilder== '$' || currencyBuilder=='USD' && currencyDB== 'USD'){
			KeywordUtil.logInfo("Currency dollars is correct")
		}
		else if(currencyBuilder== 'Dhs' || currencyBuilder=='AED' && currencyDB== 'AED'){
			KeywordUtil.logInfo("Currency dirham is correct")
		}
		else if(currencyBuilder== '£' || currencyBuilder=='GBP' && currencyDB== 'GBP'){
			KeywordUtil.logInfo("Currency pound is correct")
		}
		else if(currencyBuilder== 'R$' || currencyBuilder=='BRL' && currencyDB== 'BRL'){
			KeywordUtil.logInfo("Currency real is correct")
		}
		else if(currencyBuilder== 'imprs' && budgetTypeDB== 'Impressions'){
			KeywordUtil.logInfo("Budget impression is correct")
		}
		else{
			KeywordUtil.markErrorAndStop("Builder setting: "+currencyBuilder+" is not match with setting from database: "+currencyDB+" or budget type: "+budgetTypeDB+"")

		}
	}
	String storeTextFromClipboard() {
		String text = ""
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
		Transferable contents = clipboard.getContents(null)
		boolean hasStringText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasStringText) {
			try {
				text = (String)contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException | IOException ex) {
				System.out.println(ex); ex.printStackTrace();
			}
		}
		return text
	}

	@Keyword
	def checkHotspots(String io_test, boolean hotspot)
	{
		/**
		 * 
		 * Check all hotspots popup
		 * 
		 * @param io_test
		 * 		object id
		 * @param hotspot
		 * 		state of hotspot: true or false
		 * 
		 */

		String advHotspotTitle = "Select the advertiser of your choice"
		String advHotspotText = "You can select your advertiser by the name or the ID."
		String creHotspotTitle = "Creator"
		String creHotspotText = "You can also create Campaign, Insertion Order, Lines Items by using predefined setup templates."
		String loaderHotspotTitle = "Loader"
		String loaderHotspotText = "When you have selected an advertiser you can load already existing Campaigns, Insertion Orders, Lines Items in DV in order to edit them."
		String dropHotspotTitle = "Creator"
		String dropHotspotText = "Select the type of item you want to create (Campaign, Insertion Orders, Lines)."
		String tempHotspotTitle = "Templates filter"
		String tempHotspotText = "You can use filters to display only templates matching with your needs."
		String uploadHotspotTitle = "Uploader"
		String uploadHotspotText = "When your setup is finished you can upload your changes on DV."

		String[] hotspotTitleText = new String[2]

		if (hotspot)
		{
			//Advertiser hotspot
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Advertiser hotspot'), 30): "Advertiser hotspot is not present"
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Advertiser hotspot'))+"').click()", null)
			hotspotTitleText = hotspotPopup(hotspotTitleText)
			assert hotspotTitleText[0].contains(advHotspotTitle): "Hotspot text does not contain" + advHotspotTitle + " ; Displayed text : " + hotspotTitleText[0]
			assert hotspotTitleText[1].contains(advHotspotText): "Hotspot text does not contain" + advHotspotTitle + " ; Displayed text : " + hotspotTitleText[1]
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Hotspot close'))+"').click()", null)

			//Creator hotspot
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Creator hotspot'), 30): "Creator hotspot is not present"
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Creator hotspot'))+"').click()", null)
			hotspotTitleText = hotspotPopup(hotspotTitleText)
			assert hotspotTitleText[0].contains(creHotspotTitle): "Hotspot text does not contain" + creHotspotTitle + " ; Displayed text : " + hotspotTitleText[0]
			assert hotspotTitleText[1].contains(creHotspotText): "Hotspot text does not contain" + creHotspotText + " ; Displayed text : " + hotspotTitleText[1]
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Hotspot close'))+"').click()", null)

			//Loader hotspot
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Loader hotspot'), 30): "Loader hotspot is not present"
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Loader hotspot'))+"').click()", null)
			hotspotTitleText = hotspotPopup(hotspotTitleText)
			assert hotspotTitleText[0].contains(loaderHotspotTitle): "Hotspot text does not contain" + loaderHotspotTitle + " ; Displayed text : " + hotspotTitleText[0]
			assert hotspotTitleText[1].contains(loaderHotspotText): "Hotspot text does not contain" + loaderHotspotText + " ; Displayed text : " + hotspotTitleText[1]
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Hotspot close'))+"').click()", null)

			goToCreator()

			//Dropdown list hotspot
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Dropdown list hotspot'), 30): "Dropdown hotspot is not present"
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Dropdown list hotspot'))+"').click()", null)
			hotspotTitleText = hotspotPopup(hotspotTitleText)
			assert hotspotTitleText[0].contains(dropHotspotTitle): "Hotspot text does not contain" + dropHotspotTitle + " ; Displayed text : " + hotspotTitleText[0]
			assert hotspotTitleText[1].contains(dropHotspotText): "Hotspot text does not contain" + dropHotspotText + " ; Displayed text : " + hotspotTitleText[1]
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Hotspot close'))+"').click()", null)


			//Filter hotspot
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Filter hotspot'), 30): "Filter hotspot is not present"
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Filter hotspot'))+"').click()", null)
			hotspotTitleText = hotspotPopup(hotspotTitleText)
			assert hotspotTitleText[0].contains(tempHotspotTitle): "Hotspot text does not contain" + tempHotspotTitle + " ; Displayed text : " + hotspotTitleText[0]
			assert hotspotTitleText[1].contains(tempHotspotText): "Hotspot text does not contain" + tempHotspotText + " ; Displayed text : " + hotspotTitleText[1]
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Hotspot close'))+"').click()", null)

			createNewLineGeneric("New", "New", 'OI', 1, '1', io_test)
			WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))

			//Upload hotspot
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Upload button hotspot'), 30): "Upload hotspot is not present"
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Upload button hotspot'))+"').click()", null)
			hotspotTitleText = hotspotPopup(hotspotTitleText)
			assert hotspotTitleText[0].contains(uploadHotspotTitle): "Hotspot text does not contain" + uploadHotspotTitle + " ; Displayed text : " + hotspotTitleText[0]
			assert hotspotTitleText[1].contains(uploadHotspotText): "Hotspot text does not contain" + uploadHotspotText + " ; Displayed text : " + hotspotTitleText[1]
			WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Homepage/Hotspots/Hotspot close'))+"').click()", null)
			KeywordUtil.logInfo("All hotspots are displayed")

		}
		else if (hotspot==false)
		{
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Advertiser hotspot hidden'), 30): "Advertiser hotspot is not hidden"
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Creator hotspot hidden'), 30): "Creator hotspot is not hidden"
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Loader hotspot hidden'), 30): "Loader hotspot is not hidden"
			goToCreator()
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Dropdown list hotspot hidden'), 30): "Dropdown hotspot is not hidden"
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Filter hotspot hidden'), 30): "Filter hotspot is not hidden"
			createNewLineGeneric("New", "New", 'OI', 1, '1', io_test)
			WebUI.click(findTestObject('Object Repository/4. Creator/Create your Object(s) button'))
			assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Homepage/Hotspots/Upload button hotspot hidden'), 30): "Upload hotspot is not hidden"
			KeywordUtil.logInfo("All hotspots are hidden")

		}
	}

	def hotspotPopup(String[] hotspotTitleText)
	{
		/**
		 * Check text and title for the 5 hotspot popup
		 * 
		 * @param hotspotTitleText
		 * 		Contains String title and String text for each hotspot popup
		 * 
		 */

		String hotspotTitleDisplayed = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('2. Homepage/Hotspots/Hotspot title'))+"').textContent", null)
		String hotspotTextDisplayed = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('2. Homepage/Hotspots/Hotspot text'))+"').textContent", null)
		hotspotTitleText = [hotspotTitleDisplayed, hotspotTextDisplayed]
		KeywordUtil.logInfo(hotspotTitleText[0])
		KeywordUtil.logInfo(hotspotTitleText[1])

		return hotspotTitleText
	}
}
