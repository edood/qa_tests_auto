package tools

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable
import reportfolio.Reportfolio

public class DeleteAllUserModels extends Reportfolio {
	
	@Keyword
	public void deleteAllUserModels() {
		selenium = getSelenium()
		
		int i = 3
		boolean isItemPresent = false
		String currentItemType = ""
		int numberOfFolders = selenium.getCssCount("folders-list > folder-element").intValue()
		int numberOfItems = 0

		TestObject currentFolderButton = findTestObject('2. Folders list/Open first folder button')
		TestObject currentFolder = findTestObject('2. Folders list/First folder name')
		TestObject currentItem = findTestObject('2. Folders list/First report name')
		
		
		while(numberOfFolders > 0) {
			currentFolder = changeSelector(currentFolder, "child\\(\\d+\\)", "child(" + i + ")")
			currentFolderButton = changeSelector(currentFolderButton, "child\\(\\d+\\)", "child(" + i + ")")
			WebUI.scrollToElement(currentFolderButton, 10)
			WebUI.click(currentFolderButton)
			Thread.sleep(500);
			numberOfItems = selenium.getCssCount("div.folder-content.open > div.file").intValue()
			
			
			while (numberOfItems > 0) {
				isItemPresent = selenium.isElementPresent("css=div.folder-content.open > div:nth-child(" + numberOfItems + ") > span > a")
				currentItemType = selenium.getText("css=div.folder-content.open > div:nth-child(" + numberOfItems + ") > span > a > i")
				if (isItemPresent == true && currentItemType.equals("insert_drive_file")) {
					currentItem = changeSelector(currentItem, "child\\(\\d+\\)", "child(" + numberOfItems + ")")
					WebUI.click(currentItem)
					WebUI.delay(2)
					WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 5)
					WebUI.waitForElementVisible(findTestObject('3. Report/9. Delete Report/Delete Report button'), 30)
					WebUI.click(findTestObject('3. Report/9. Delete Report/Delete Report button'))
					WebUI.click(findTestObject('Object Repository/3. Report/9. Delete Report/Delete Report Confirm button'))
					WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 5)
					assert WebUI.verifyEqual("Model successfully deleted", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
				}
				numberOfItems--
			}
			WebUI.scrollToElement(currentFolderButton, 10)
			WebUI.click(currentFolderButton)
			numberOfFolders--
			i++
		}
	}
}
