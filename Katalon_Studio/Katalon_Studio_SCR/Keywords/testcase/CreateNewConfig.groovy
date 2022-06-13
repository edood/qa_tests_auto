package testcase

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import internal.GlobalVariable
import screenbox.Screenbox

public class CreateNewConfig extends Screenbox {

	/**
	 * 
	 *Mainly class for creating, updating and deleting configs
	 * 
	 *@author Edouard SAAD
	 *@maintainer Edouard SAAD
	 */

	@Keyword
	public void createCompleteConfig() {

		/**
		 * Create one config
		 * 
		 * 
		 * Create one config with predefines information following these steps:
		 * - Open SCR
		 * - Login
		 * - Clear the default advertiser
		 * - Set a test advertiser
		 * - Open Config manager tab
		 * - Create a new config
		 * - Set a title, an url and save the config
		 * - Check if the toast appear and disappear
		 * 
		 * TestCase: C266645
		 *
		 */

		setAdvertiser(GlobalVariable.advertiserName)

		def addSite = false

		try{
			createNewConfig(GlobalVariable.siteTitle, GlobalVariable.siteURL, GlobalVariable.devices, addSite)
			deleteConfigOrSite(0)
		}
		catch(WebElementNotFoundException ex) {
			KeywordUtil.markErrorAndStop("New config has not been created")
		}
	}

	@Keyword
	public void createMultipleConfig() {

		/**
		 * Create many configs
		 * 
		 * 
		 * This test create 3 configs:
		 * First one with 2 sites: 
		 * 		- first one with with site title, site url, default device desktop
		 * 		- Second one with site title, site url, device desktop, mobile, tablet
		 * Second one with site title, site url, device tablet
		 * Third one with site title, site url, device mobile
		 * 
		 * TestCase: C266668
		 * 
		 */

		setAdvertiser(GlobalVariable.advertiserName)
		try{

			createNewConfig(GlobalVariable.siteTitle, GlobalVariable.siteURL, GlobalVariable.devices, GlobalVariable.addSite)
		}
		catch(WebElementNotFoundException ex) {
			KeywordUtil.markErrorAndStop("New config has not been created")
		}
	}

	@Keyword
	public void updateConfig() {

		/**
		 * Update an existing configs
		 * 
		 * 
		 * - Create 2 basics configs
		 * - Update the name of first config according to updateConfigName global variable if she exists.
		 * 	Otherwise, we catch a MissingPropertyException excpetion and we update the config with a default name
		 * - A site is added to the first config with default device
		 * - The first site of the second config is edited with addSiteTitle1, addSiteURL1 and addSiteDevices1 of the first config
		 * - We add 2 more sites on the second config. One with device mobile, the other with all devices 
		 * 
		 */

		setAdvertiser(GlobalVariable.advertiserName)
		List updateConfigName = ['configNameUpdated']
		try{

			createNewConfig(GlobalVariable.siteTitle, GlobalVariable.siteURL, GlobalVariable.devices, false, GlobalVariable.updateConfigName)
		}
		catch (WebElementNotFoundException ex) {
			KeywordUtil.markErrorAndStop("New config has not been created")
		}
		catch(MissingPropertyException ex) {
			KeywordUtil.logInfo("Default update name")
			createNewConfig(GlobalVariable.siteTitle, GlobalVariable.siteURL, GlobalVariable.devices, false, updateConfigName)
		}

		List titleNumber = GlobalVariable.siteTitle
		int numberOfConfig = titleNumber.size()

		//site is added on the first config (k=0)
		addSite(GlobalVariable.addSite, 0, numberOfConfig, true)
		saveConfig(1)

		//I edit the second config site title, site url, devices with first config information
		editConfig(GlobalVariable.addSiteTitle1, GlobalVariable.addSiteURL1, GlobalVariable.addSiteDevice1, 1, numberOfConfig)

		//site is added on the second config (k=1)
		addSite(GlobalVariable.addSite, 1, numberOfConfig, true)
		saveConfig(0)

	}

	@Keyword
	public void deleteConfigs() {

		/**
		 * Delete config Test
		 *
		 *- Create 2 basics configs
		 *- Delete 1 site of the first config and save
		 *- Delete 1 site of the second config and check that save config button is not clickable
		 *- Delete the second config and check with toast message that it is well deleted
		 *
		 */

		List titleNumber = GlobalVariable.siteTitle
		int numberOfConfig = titleNumber.size()

		setAdvertiser(GlobalVariable.advertiserName)
		try{

			createNewConfig(GlobalVariable.siteTitle, GlobalVariable.siteURL, GlobalVariable.devices, GlobalVariable.addSite)
		}
		catch(WebElementNotFoundException ex) {
			KeywordUtil.markErrorAndStop("New config has not been created")
		}

		deleteConfigOrSite(numberOfConfig, 0, 0)
		saveConfig(1)
		deleteConfigOrSite(numberOfConfig, 1, 1)
		saveConfig(0)
		deleteConfigOrSite(0)
	}
}
