package testCases.customParams

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.driver.DriverFactory

import reportfolio.Reportfolio

public class ExtractPlaceholders extends Reportfolio  {

	// RPF-92
	@Keyword
	public void extractPlaceholders ()  {
		selenium = getSelenium()
		driver = DriverFactory.getWebDriver()
		// Case 1 - Verify the behavior when the model template doesn't have any custom param
		String randomNumber = String.valueOf((int)(10000 + Math.random() * 90000))
		createFolder("Extract placeholder")
		String modelName = "Model\u00A0without\u00A0custom\u00A0params\u00A0" + randomNumber
		String templateName = "Template"
		String templateFormat = "xlsx"

		String toast = createModel(modelName, "weekly", templateName, templateFormat, "8", "Subreport from model without custom params", 1, "2", "3", ";", true)
		assert toast != templateName + "." + templateFormat + " has been successfully uploaded.\n5 custom params found in the XLS template :\nDesktop, Mobile, Profiling, Prospection, Retargeting"

		// Case 2 - Verify the behavior when the model template has several custom params
		modelName = "Model\u00A0with\u00A0custom\u00A0params " + randomNumber
		templateName = "Template Insert a Table with custom params"
		toast = createModel(modelName, "weekly", templateName, templateFormat, "2", "Subreport from model with custom params", 1, "2", "3", ";", true)
		assert toast == templateName + "." + templateFormat + " has been successfully uploaded.\n5 custom params found in the XLS template :\nDesktop, Mobile, Profiling, Prospection, Retargeting"
	}
}
