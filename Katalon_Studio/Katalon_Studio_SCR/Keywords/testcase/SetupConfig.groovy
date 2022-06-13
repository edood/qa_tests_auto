package testcase
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import screenbox.Screenbox


class SetupConfig extends Screenbox{

	/**
	 * 
	 * 
	 */

	CreateNewConfig createNewConfig = new CreateNewConfig()

	@Keyword
	public void setupAndCheckEmail() {
		/**
		 * Send an email with screenshot after creating a basic config.
		 * We check that a correct email is reveived
		 * 
		 */

		createNewConfig.createMultipleConfig()

		try {
			
			setupConfig()
		}
		catch(WebElementNotFoundException ex) {
			ex.printStackTrace()
			System.exit(1);
		}
	}
}