package screenbox

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.CommonKeywords

protected class Tools extends CommonKeywords{

	/**
	 *
	 * This class contain tools used into Screenbox class
	 *
	 *
	 */

	protected void openDevices() {


		/**
		 * Open the device list
		 * Desktop is checked by default so
		 * if the device chosen is not Desktop, uncheck it
		 *
		 * @param device
		 *          represent a device (Desktop, Mobile or Tablet).
		 */
		if (WebUI.waitForElementVisible(findTestObject('Object Repository/3. Config Manager/Config creation/Devices/Devices selector opened'),2) == false) {
			WebUI.click(findTestObject('Object Repository/3. Config Manager/Config creation/Devices/Devices selector'))
			KeywordUtil.logInfo('Device is opened')
			WebUI.delay(1)
		} else {
			KeywordUtil.logInfo('Device is already opened')
		}
	}

	protected void closeDevices() {

		/**
		 * Close the device list
		 *
		 */

		if (WebUI.waitForElementVisible(findTestObject('Object Repository/3. Config Manager/Config creation/Devices/Devices selector opened'),2)) {
			WebUI.delay(1)
			WebUI.clickOffset(findTestObject('Object Repository/3. Config Manager/Config creation/Devices/Devices selector opened'), 500, 0)
			if (WebUI.waitForElementNotPresent(findTestObject('Object Repository/3. Config Manager/Config creation/Devices/Devices selector opened'),5)) {
				KeywordUtil.logInfo('Device is closed')
				WebUI.delay(1)
			} else {
				KeywordUtil.markErrorAndStop("Device box isn't closed")
			}
		} else {
			KeywordUtil.logInfo('Device is already closed')
		}
	}

	protected void checkDevice(String device) {

		/**
		 * Check a device (Desktop, Mobile or Tablet)
		 *
		 * @param device
		 *          represent a device (Desktop, Mobile or Tablet).
		 */

		while (WebUI.waitForElementVisible(findTestObject('Object Repository/3. Config Manager/Config creation/Devices/Devices selector opened'),2) == false) {
			openDevices()
		}
		if (WebUI.waitForElementVisible(findTestObject('Object Repository/3. Config Manager/Config creation/Devices/'+ device +' device checked'), 2)) {
			KeywordUtil.logInfo(''+ device +' is already selected')
		} else {
			WebUI.check(findTestObject('3. Config Manager/Config creation/Devices/'+ device +' device unchecked'))
			KeywordUtil.logInfo(''+ device +' is checked')
		}
	}

	protected void uncheckDevice(String device) {

		/**
		 * Uncheck a device (Desktop, Mobile or Tablet)
		 *
		 * @param device
		 *          represent a device (Desktop, Mobile or Tablet).
		 */

		while (WebUI.waitForElementVisible(findTestObject('Object Repository/3. Config Manager/Config creation/Devices/Devices selector opened'),2) == false) {
			openDevices()
		}

		if (WebUI.waitForElementVisible(findTestObject('Object Repository/3. Config Manager/Config creation/Devices/'+ device +' device unchecked'), 2)) {
			KeywordUtil.logInfo(''+ device +' is already unselected')
		} else {
			WebUI.click(findTestObject('3. Config Manager/Config creation/Devices/'+ device +' device checked'))
			KeywordUtil.logInfo(''+ device +' is unchecked')
		}
	}

	protected void openAnExistingConfig(boolean updateSite, int numberOfConfig, int configIndex) {


		/**
		 * 
		 * Open a chosen config
		 * 
		 * 
		 * @param updateSite
		 * 		True if we want to open an existing config, false otherwise
		 * @parm numberOfConfig
		 * 		The number of total created config
		 * @param configIndex
		 * 		Position of a config
		 * 
		 */

		//If we want to update an existed config
		if (updateSite) {
			//Position of a config
			int configPos = configIndex + 1

			//Check if the wanted config is opened
			if (WebUI.waitForElementNotPresent(changeSelector(findTestObject('Object Repository/3. Config Manager/Config creation/Config body opened'), 'nth-of-type\\(\\d\\)', 'nth-of-type(' + configPos + ')'), 2)) {
				WebUI.click(changeSelector(findTestObject('Object Repository/3. Config Manager/Config creation/Config number'), 'nth-of-type\\(\\d\\)', 'nth-of-type(' + configPos + ')'))
				KeywordUtil.logInfo("Config is opened")

			} else {
				KeywordUtil.logInfo("Config is already opened")
			}
		}
	}
}
