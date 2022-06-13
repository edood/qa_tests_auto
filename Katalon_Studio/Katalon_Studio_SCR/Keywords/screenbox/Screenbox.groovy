package screenbox

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.Instant

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.CommonKeywords
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import internal.GlobalVariable

public class Screenbox extends CommonKeywords{

	/**
	 * 
	 * This class is a toolkit which contains mainly methods used for Screenbox 
	 * for creating a config and adding a setup
	 * 
	 * @author esaad
	 * @maintainer esaad
	 * 
	 */

	Tools tools = new Tools()
	private static String DESKTOP = 'Desktop'
	private static String MOBILE = 'Mobile'
	private static String TABLET = 'Tablet'
	protected static WebDriver driver = DriverFactory.getWebDriver()
	protected static String baseUrl = 'https://' + GlobalVariable.env + '-' + GlobalVariable.env2 + '/folders'
	protected static WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver, baseUrl)

	protected void createNewConfig(List title, List url, List devices, boolean addBool) {

		/**
		 * Create a config
     *
		 * Create a new config with siteTitle, siteURL in chosen devices, and save it
		 * Add one more site in a config according to user choices
		 * 
		 * @param title
		 * 		represent a siteTite
		 * @param url
		 * 		represent a siteURL
		 * @param devices
		 * 		represent device lists (Desktop, Mobile or Tablet)
		 * @param addBool
		 * 		We decide to add site in a config if this booelean is true
		 */

		int titleNumber = title.size()
		int urlNumber = url.size()
		int devicesNumber = devices.size()

		//Check if user set the same number of title and url
		assert titleNumber.equals(urlNumber) : "You need to set the same number of site titles and site URLs"
		assert titleNumber.equals(devicesNumber) : "You need to set the same number of site titles and devices"

		if (WebUI.verifyElementVisible(findTestObject('3. Config Manager/Config manager tab not opened'))) {

			WebUI.click(findTestObject('3. Config Manager/Config manager tab not opened'))
			KeywordUtil.logInfo("Config manager tab is opened")
		} else {
			KeywordUtil.markError("Config manager tab cannot open")
		}

		WebUI.delay(1)
		KeywordUtil.logInfo("Loading...")
		assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/3. Config Manager/Loader'), 10)

		// We create the config's number chosen by user in his profile
		for (int i=0; i<titleNumber; i++) {

			if (WebUI.waitForElementClickable(findTestObject('Object Repository/3. Config Manager/Create new config enabled button'), 10)){
				KeywordUtil.logInfo("Create new config button can be clicked")
			} else {

				KeywordUtil.markError("Create new config button is not clickable after 10s")
				throw new WebElementNotFoundException('Object Repository/3. Config Manager/Create new config enabled button')
			}

			WebUI.click(findTestObject('3. Config Manager/Create new config enabled button'))
			KeywordUtil.logInfo("Create new config...")
			WebUI.delay(1)


			if (WebUI.waitForElementVisible(findTestObject('3. Config Manager/Config creation/Config Created'), 10)) {

				if (WebUI.verifyElementText(findTestObject('3. Config Manager/Config creation/Site title input'), "")) {
					setSiteTitle(title[i])
					setSiteURL(url[i])
					setDevices(devices[i])
					addSlot()
					addSite(addBool, i, titleNumber, false)
					saveConfig(0)
					KeywordUtil.markPassed("Config n°${i+1} is created")
					WebUI.delay(1)
				}
			} else {

				KeywordUtil.markError("Config is not created")
				throw new WebElementNotFoundException('3. Config Manager/Config creation/Config Created')
			}
		}
	}

	protected void createNewConfig(List title, List url, List devices, boolean addBool, List updateConfig) {

		/**
		 * Create a config - method overloaded
		 * 
		 * createNewConfig method overloaded
		 * 1- We create a basic config
		 * 2 - Config name is updated and saved
		 * 
		 * @param title
		 * 		represent a siteTite
		 * @param url
		 * 		represent a siteURL
		 * @param devices
		 * 		represent device lists (Desktop, Mobile or Tablet)
		 * @param addBool
		 * 		We decide to add site in a config if this booelean is true
		 * @param updateConfig
		 * 		represent list of update name configs set by user (Not mandatory)
		 */

		createNewConfig(title, url, devices, addBool)
		updateConfigName(updateConfig)
	}

	protected void editConfig(List title, List url, List devices, int configIndex, int numberOfConfig) {

		/**
		 * Edit config
		 * 
		 * This Edit config method can edit the first site of a chosen config
		 *
		 * @param title
		 * 		represent a siteTite
		 * @param url
		 * 		represent a siteURL
		 * @param devices
		 * 		represent device lists (Desktop, Mobile or Tablet)
		 * @param configIndex
		 * 		Position of a config
		 * @parm numberOfConfig
		 * 		The number of total created config
		 *
		 */
		KeywordUtil.logInfo("Config Edit")
		tools.openAnExistingConfig(true, numberOfConfig, configIndex)
		setSiteTitle(title[0])
		setSiteURL(url[0])
		setDevices(devices[0])

	}

	protected void setDevices(String devices) {

		/**
		 * Set devices on a config
		 * 
		 * Check the chosen devices for each config according to user choices
		 *
		 * @param devices
		 *     represent devices we want to set for one config
		 */
		int f = 0
		//if we set more than 1 device for a config
		if (devices.split(';').size() > 1) {
			def a = devices.split(';')
			for (int j=0; j<(a.size()); j++) {
				if (a[j]=='d') {
					tools.checkDevice(DESKTOP)
					f = 1
				} else if (a[j]=='m') {
					tools.checkDevice(MOBILE)
				} else if (a[j]=='t') {
					tools.checkDevice(TABLET)
				} else {
					KeywordUtil.markWarning("Device name is not set correctly. Please use lettres d, m or t")
				}
			}
			if (f==0) {
				tools.uncheckDevice(DESKTOP)
			}
			tools.closeDevices()
			//if we set only one device for a config
		} else {
			if (devices=='d' || devices=='' || devices==null) {
				KeywordUtil.logInfo("Desktop is selected by default")
			} else if (devices=='m') {
				tools.uncheckDevice(DESKTOP)
				tools.checkDevice(MOBILE)
			} else if (devices=='t') {
				tools.uncheckDevice(DESKTOP)
				tools.checkDevice(TABLET)
			} else {
				KeywordUtil.markWarning("Device name is not set correctly. Please use lettres d, m or t")
			}
			tools.closeDevices()
		}
	}

	protected void setSiteTitle(String siteTitle) {

		/**
		 * Set site title on a config
		 * 
		 * Set a site title in the appropriate field
		 *
		 * @param siteTitle
		 *          represent a site title.
		 */

		WebUI.setText(findTestObject('Object Repository/3. Config Manager/Config creation/Site title input'), siteTitle)
		KeywordUtil.logInfo("Site title is set")
	}

	protected void setSiteURL(String siteURL) {

		/**
		 * Set site url in a config
		 * 
		 * Set a site URL in the appropriate field
		 *
		 * @param siteURL
		 *          represent a site URL.
		 */

		WebUI.setText(findTestObject('Object Repository/3. Config Manager/Config creation/Site url input'), siteURL)
		KeywordUtil.logInfo("Site URL is set")
		WebUI.sendKeys(findTestObject('Object Repository/3. Config Manager/Config creation/Site url input'), Keys.chord(Keys.TAB))
		WebUI.delay(1)
	}

	protected void addSite(boolean addBool, int configIndex, int numberOfConfig, boolean updateSite) {

		/**
		 * Add one more site in  a config
		 * 
		 * We define in the profile if a site is added and for which config
		 *
		 * @param addBool
		 * 		We decide to add site in a config if this booelean is true
		 * @param configIndex
		 * 		Position of a config
		 * @parm numberOfConfig
		 * 		The number of total created config
		 * @param updateSite
		 * 		We decide if we want update a site after all configs were created
		 * 
		 */	

		int k = configIndex

		try {
			//if addSite variable is true and if the variable exists
			if (addBool && GlobalVariable."addSiteTitle${k+1}") {

				//Number of sites we want to add
				int size = GlobalVariable."addSiteTitle${k+1}".size()

				tools.openAnExistingConfig(updateSite, numberOfConfig, k)

				//Each list index correspond to a site
				for (int siteIndex=0; siteIndex < size; siteIndex++) {
					WebUI.click(findTestObject('Object Repository/3. Config Manager/Config creation/Add site button'))
					WebUI.delay(1)
					setSiteTitle(GlobalVariable."addSiteTitle${k+1}"[siteIndex])
					setSiteURL(GlobalVariable."addSiteURL${k+1}"[siteIndex])
					setDevices(GlobalVariable."addSiteDevice${k+1}"[siteIndex])
					KeywordUtil.logInfo("Site is Added")
				}

			}
			else if(addBool == false) {
				KeywordUtil.logInfo("addSite is false")
			}
		}
		//if user does not want to add a site in a config, we raise an excepetion and the program continue
		catch(MissingPropertyException ex) {
			KeywordUtil.logInfo("We don't add site for config n°${k+1}, variables are not present in profile")
		}
	}
	
	protected void addSlot(){
		/**
		 * Add slot field 
		 * 
		 * This field can be auto or manual. User can choose
		 * to set a selector slot manually
		 * 
		 * 
		 */
		
		if (WebUI.verifyElementPresent(findTestObject('Object Repository/3. Config Manager/Config creation/Slot on page empty or not'), 1)) {
			KeywordUtil.logInfo("Add slot field is in auto mode")
		} else {
			KeywordUtil.logInfo("Add slot field is in manual mode")
		}
	}

	protected void saveConfig(int configIndex) {

		/**
		 * Save a config
		 * 
		 * Firstly If devices box is opened, we close it
		 * Save the config and check 
		 * if toast message "New config saved" 
		 * appears and disappear after a few seconds
		 * 
		 * @param configIndex
		 * 		Position of a config
		 *
		 */
		//Position of a config

		int configPos = configIndex+3

		if (WebUI.waitForElementClickable(changeSelector(findTestObject('Object Repository/3. Config Manager/Config creation/Save config button'), 'nth-child\\(\\d\\)', 'nth-child(' + configPos + ')'), 10)) {
			WebUI.click(changeSelector(findTestObject('Object Repository/3. Config Manager/Config creation/Save config button'), 'nth-child\\(\\d\\)', 'nth-child(' + configPos + ')'))
			KeywordUtil.logInfo("Config is saved")
			toastMessage("New config saved")
		} else {

			KeywordUtil.markWarning("Save config button is not clickable")
		}
	}

	protected void deleteConfigOrSite(int configIndex) {

		/**
		 * Delete a config
		 * 
		 * Delete an existing config and check 
		 * if toast message "New config deleted with success!" 
		 * appears and disappear after a few seconds
		 * 
		 */
		int configPos = configIndex+3
		
		WebUI.click(changeSelector(findTestObject('Object Repository/3. Config Manager/Config creation/Delete config button'),'nth-child\\(\\d\\)', 'nth-child(' + configPos + ')'))
		WebUI.delay(2)
		WebUI.click(findTestObject('Object Repository/3. Config Manager/Config creation/Delete confirm button'))
		KeywordUtil.logInfo("Config is deleted")

		if (WebUI.verifyElementPresent(findTestObject('Object Repository/3. Config Manager/Config creation/New config deleted with succes Toast'), 5)) {
			KeywordUtil.logInfo("Toast is visible: New config deleted with success!")
		} else {
			KeywordUtil.markError("Toast does not appear")
			throw new WebElementNotFoundException('Object Repository/3. Config Manager/Config creation/New config deleted with succes Toast')
		}

		if (WebUI.verifyElementNotPresent(findTestObject('Object Repository/3. Config Manager/Config creation/New config deleted with succes Toast'), 10)) {
			KeywordUtil.logInfo("Toast is no longer visible")
		} else {
			KeywordUtil.markError("Toast is still visible")
			throw new WebElementNotFoundException('Object Repository/3. Config Manager/Config creation/New config deleted with succes Toast')
		}
	}
	
	protected void deleteConfigOrSite(int numberOfConfig, int configIndex, int siteIndex) {
		/**
		 * Delete a site inside a config 
		 * 
		 * @parm numberOfConfig
		 * 		The number of total created config
		 * @param configIndex
		 * 		Position of a config
		 * @param siteIndex
		 * 		Position of a site
		 *
		 */
		
		int sitePos = siteIndex+3
		
		tools.openAnExistingConfig(true, numberOfConfig, configIndex)
		WebUI.click(changeSelector(findTestObject('Object Repository/3. Config Manager/Config creation/Delete site button'),'nth-child\\(\\d\\)', 'nth-child(' + sitePos + ')'))
		KeywordUtil.logInfo("Site n°${siteIndex+1} is deleted")
		
	}

	protected void updateConfigName(List updateConfig) {

		/**
		 *Update config name
		 * 
		 *@param updateConfig
		 *		represent list of update name configs set by user (Not mandatory)
		 */

		int updateConfigNumber = updateConfig.size()

		for (int updateIndex = 0; updateIndex<updateConfigNumber; updateIndex++) {

			if (updateConfig[updateIndex]=='' || updateConfig[updateIndex]==null) {

				KeywordUtil.logInfo("No update name for this config ")
			}
			else {
				int updatePos=updateIndex+3
				//Config name should be updated depends on position in the list set by user
				WebUI.click(changeSelector(findTestObject('Object Repository/3. Config Manager/Config creation/Update config name'), 'nth-child\\(\\d\\)', 'nth-child(' + updatePos + ')'))
				WebUI.delay(2)
				if (WebUI.verifyElementPresent(findTestObject('Object Repository/3. Config Manager/Config creation/Edit config name field'), 10)) {
					WebUI.sendKeys(findTestObject('Object Repository/3. Config Manager/Config creation/Edit config name field'), updateConfig[updateIndex])
					KeywordUtil.logInfo("Config name is set")
					WebUI.sendKeys(findTestObject('Object Repository/3. Config Manager/Config creation/Edit config name field'), Keys.chord(Keys.ENTER))
					saveConfig(updateIndex)
				} else {
					KeywordUtil.markErrorAndStop("Cannot edit config")
				}
			}
		}
	}

	protected void setAdvertiser(String advertiserName) {

		/**
		 * Set an advertiser in the appropriate the field
		 *
		 * @param advertiserName
		 *          represent an advertiser.
		 */
		WebUI.delay(1)
		KeywordUtil.logInfo("Loading...")
		assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/3. Config Manager/Loader'), 10)

		if (WebUI.clearText(findTestObject('Object Repository/4. Advertiser/Advertiser field'))) {
			KeywordUtil.logInfo("Advertiser is cleared")
		} else {

			KeywordUtil.markError("Advertiser is not cleared")
		}

		WebUI.setText(findTestObject('4. Advertiser/Advertiser field'), advertiserName)
		assert WebUI.waitForElementVisible(findTestObject('4. Advertiser/Advertisers lists'),10)
		WebUI.click(findTestObject('4. Advertiser/Advertisers lists'))
		WebUI.delay(1)
		assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/3. Config Manager/Loader'), 5)
		KeywordUtil.logInfo("Advertiser is set")
	}

	protected void toastMessage(String message) {
		/**
		 * Toast message method
		 * 
		 * Check if the toast is displayed
		 * 
		 * @param message
		 * 		Toast message
		 */

		if (WebUI.verifyElementPresent(findTestObject('3. Config Manager/Config creation/New config saved Toast'), 5)) {
			KeywordUtil.logInfo("Toast is visible: "+ message + "")
		} else {
			KeywordUtil.markError("Toast does not appear")
			throw new WebElementNotFoundException('Object Repository/3. Config Manager/Create new config enabled button')
		}
		if (WebUI.verifyElementNotPresent(findTestObject('3. Config Manager/Config creation/New config saved Toast'), 10)) {
			KeywordUtil.logInfo("Toast is no longer visible")
		} else {
			KeywordUtil.markError("Toast is still visible")
			throw new WebElementNotFoundException('Object Repository/3. Config Manager/Create new config enabled button')
		}

	}

	protected void setupConfig() {
		/**
		 * Set a config in Setup Configs field
		 * 
		 */

		String advertiser = GlobalVariable.advertiserName
		String currentTime = Instant.now().getEpochSecond()

		if (WebUI.verifyElementVisible(findTestObject('2. Setup/Setup tab not opened'))) {
			WebUI.click(findTestObject('2. Setup/Setup tab not opened'))
			assert WebUI.verifyElementVisible(findTestObject('2. Setup/Setup tab opened')) : "Cannot opened setup tab"
			KeywordUtil.logInfo("Setup tab is opened")
		} else if (WebUI.verifyElementVisible(findTestObject('2. Setup/Setup tab opened'))) {
			KeywordUtil.logInfo("Setup tab is already open")
		}
		//This line will be deleted when SCR-63 will be merged
		WebUI.click(findTestObject('Object Repository/2. Setup/delete config button'))

		selectConfig()
		selectApnCrea()
		selectDvCrea()
		checkEmailPptAndSend()
		checkMailReceivedSince()

	}

	protected void selectApnCrea() {
		/**
		 * 
		 * Select an Appnexus creative
		 * 
		 */
		WebUI.click(findTestObject('Object Repository/2. Setup/Apn creative url button'))
		if (WebUI.waitForElementVisible(findTestObject('Object Repository/2. Setup/checkbox config'), 5)) {
			WebUI.check(findTestObject('Object Repository/2. Setup/checkbox config'))
			KeywordUtil.logInfo("Apn creative is set")
			WebUI.clickOffset(findTestObject('Object Repository/2. Setup/Config list'), 500, 0)

		} else {
			KeywordUtil.logInfo("No Appnexus creative exists")
		}
	}

	protected void selectDvCrea() {
		/**
		 * 
		 * Select a DV creative
		 * 
		 */
		WebUI.click(findTestObject('Object Repository/2. Setup/DV creative url button'))
		if (WebUI.waitForElementVisible(findTestObject('Object Repository/2. Setup/checkbox config'), 5)) {
			WebUI.check(findTestObject('Object Repository/2. Setup/checkbox config'))
			KeywordUtil.logInfo("DV creative is set")
			WebUI.clickOffset(findTestObject('Object Repository/2. Setup/Config list'), 500, 0)
		} else {
			KeywordUtil.logInfo("No DV creative exists")
		}
	}

	protected void selectConfig() {
		/**
		 * Select a config
		 * 
		 */
		WebUI.click(findTestObject('Object Repository/2. Setup/setup configs button'))
		if (WebUI.waitForElementVisible(findTestObject('Object Repository/2. Setup/checkbox config'), 5)) {
			WebUI.check(findTestObject('Object Repository/2. Setup/checkbox config'))
			KeywordUtil.logInfo("Config is set")
			WebUI.clickOffset(findTestObject('Object Repository/2. Setup/Config list'), 500, 0)
		} else {
			KeywordUtil.logInfo("No config is created")
		}
	}

	protected void checkEmailPptAndSend() {
		/**
		 * Check Email present and ppt option is selected by default
		 * 
		 * 
		 */

		if (WebUI.waitForElementVisible(findTestObject('Object Repository/2. Setup/email field'), 2)) {
			KeywordUtil.markPassed("Email is present by default")
		} else {
			KeywordUtil.markFailed("Email is not present by default")
			throw new WebElementNotFoundException('Object Repository/2. Setup/email field', By.cssSelector('div > mat-chip > span '))
		}
		if (WebUI.waitForElementVisible(findTestObject('Object Repository/2. Setup/ppt checkbox'), 2)) {
			KeywordUtil.markPassed("PPT is selected by default")
		} else {
			KeywordUtil.markFailed("PPT should be selected by default")
			throw new WebElementNotFoundException('Object Repository/2. Setup/ppt checkbox', By.cssSelector('input.mat-checkbox-input[aria-checked=true]'))
		}
		
		String estimatedTime = WebUI.executeJavaScript("return document.querySelector('screenbox-timer').textContent", null)
		KeywordUtil.markPassed(estimatedTime)
		
		if (WebUI.waitForElementClickable(findTestObject('Object Repository/2. Setup/send email button'), 2)){
			WebUI.click(findTestObject('Object Repository/2. Setup/send email button'))
			KeywordUtil.markPassed("We send Email with screenshots")
			toastMessage("Sending email")

		} else {
			KeywordUtil.markFailed("We cannot click on Send email button")
			throw new WebElementNotFoundException('Object Repository/2. Setup/send email button', By.cssSelector('body > app-root > app-layout > div > report > div > div.options > div > button'))
		}
	}
	
	protected boolean checkMailReceivedSince() {
		/**
		 * Check that a correct mail is received
		 *
		 *
		 */

		String mailTitle = ""
		String env = GlobalVariable.env
		String advertiser = GlobalVariable.advertiserName


		KeywordUtil.logInfo("Check email received...")

		if (env.startsWith("shared")) {
			String envUpper = env.toUpperCase()
			mailTitle = "[" + advertiser + "] - Live Campaign Report"

		} else if (env.startsWith("dev")){
			mailTitle = "[DEV] [" + advertiser + "] - Live Campaign Report"
		}

		String currentTime = Instant.now().getEpochSecond()
		int remainingAttempts = 3000

		WebUI.navigateToUrl("https://mail.google.com/mail/u/0/#inbox")
		WebUI.waitForPageLoad(30)
		WebUI.delay(2)
		boolean loggedInToGoogle = selenium.isElementPresent("css=" + findTestObject('Object Repository/7. Gmail/Check all mails').getSelectorCollection().get(SelectorMethod.CSS))
		if(loggedInToGoogle == false) {
			loginToGoogle()
			WebUI.navigateToUrl("https://mail.google.com/mail/u/0/#inbox")
			WebUI.waitForPageLoad(30)
		}
		WebUI.navigateToUrl("https://mail.google.com/mail/u/0/#search/from%3A(screenbox%40tradelab.fr)+subject%3A("+mailTitle+")+after%3A"+ currentTime)
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/7. Gmail/Check all mails'), 45)
		String firstMailTitlePresentSelector = findTestObject('7. Gmail/Mail 1 title').getSelectorCollection().get(SelectorMethod.CSS)
		boolean isfirstMailTitlePresent = selenium.isElementPresent("css=" + firstMailTitlePresentSelector)
		String firstMailTitle = ""

		while(isfirstMailTitlePresent == false && remainingAttempts > 0) {
			WebUI.click(findTestObject('Object Repository/7. Gmail/Refresh mails button'))
			Thread.sleep(1500)
			isfirstMailTitlePresent = selenium.isElementPresent("css=" + firstMailTitlePresentSelector)
			remainingAttempts = remainingAttempts - 1
		}

		firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))

		assert WebUI.getText(findTestObject('7. Gmail/Mail 1 Title')).contains(mailTitle) :

		WebUI.focus(findTestObject('7. Gmail/Mail 1 Title'))
		Thread.sleep(1000)
		WebUI.click(findTestObject('7. Gmail/Mail 1 Title'))
	}
}
