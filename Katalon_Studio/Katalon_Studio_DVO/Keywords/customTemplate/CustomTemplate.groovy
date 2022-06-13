package customTemplate

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.OptimizerDV360

import internal.GlobalVariable


class CustomTemplate extends OptimizerDV360 {
	public TestObject saved = new TestObject("saved");
	String tmpsave = null;

	public CustomTemplate() {}

	public createConfig() {
		clickObjectbyCss('div.ag-side-bar > div.ag-side-buttons > div:nth-child(4) > button')
		waitforCssSelectorVisible('report-configs-panel > div > div.report-config-creator > button:nth-child(3)', 30)
		clickObjectbyCss('report-configs-panel > div > div.report-config-creator > div > mat-select > div')
		waitforCssSelectorVisible('div[class="cdk-overlay-pane"] > div > mat-option:last-child', 30)
		clickObjectbyCss('div[class="cdk-overlay-pane"] > div > mat-option:last-child')
		waitforCssSelectorVisible('report-configs-panel > div > div.report-config-creator > button:nth-child(3)', 30)
		clickObjectbyCss('report-configs-panel > div > div.report-config-creator > button:nth-child(3)')
		WebUI.delay(1)
		KeywordUtil.markPassed("Create Config successfull")
	}

	public editConfigName(String name) {
		clickObjectbyCss('report-configs-panel > div > div.report-configs-list > ul > li > contenteditable > div > i')
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'report-configs-panel > div > div.report-configs-list > ul > li > contenteditable > div > input', true)
		WebUI.sendKeys(tmp, name + '\n')
		WebUI.delay(1)
		KeywordUtil.markPassed("Edit Config Name successfull")
	}

	public updateConfig() {
		clickObjectbyCss('div.ag-side-bar > div.ag-side-buttons > div:nth-child(4) > button')
		WebUI.delay(1)
		clickObjectbyCss('report-configs-panel > div > div.report-config-creator > button.mat-raised-button.mat-accent')
	}

	public rememberConfig() {
		saved.addProperty('css', ConditionType.EQUALS, 'ag-grid-angular > div > div.ag-root-wrapper-body.ag-layout-normal > div.ag-root.ag-unselectable.ag-layout-normal')
		tmpsave = WebUI.getText(saved)
		println("TMPSAVE = " + tmpsave)
	}

	public checkSavedConfig(String name) {
		WebUI.refresh()
		WebUI.delay(10)
		selectDateRange(GlobalVariable.dateRange)

		WebUI.delay(1)
		clickObjectbyCss('div.ag-side-bar > div.ag-side-buttons > div:nth-child(4) > button')
		WebUI.delay(1)
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div.report-configs-list > ul > li:nth-child(1)', true)
		WebUI.verifyElementVisible(tmp)
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div.report-configs-list > ul > li:nth-child(1) > contenteditable > div > span', true)
		WebUI.verifyEqual(WebUI.getText(tmp), name)
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'report-configs-panel > div > div.report-config-creator > div', true)
		WebUI.verifyEqual(WebUI.getText(tmp), name)
		clickObjectbyCss('div.ag-side-bar > div.ag-side-buttons > div:nth-child(4) > button')
	}

	public deleteConfig() {
		clickObjectbyCss('div.ag-side-bar > div.ag-side-buttons > div:nth-child(4) > button')
		waitforCssSelectorVisible('div.report-configs-list > ul > li > mat-icon', 30)
		clickObjectbyCss('div.report-configs-list > ul > li > mat-icon')
		waitforCssSelectorVisible('confirm-dialog > div > button.mat-raised-button.mat-accent', 30)
		clickObjectbyCss('confirm-dialog > div > button.mat-raised-button.mat-accent')
		WebUI.delay(1)
		clickObjectbyCss('div.ag-side-bar > div.ag-side-buttons > div:nth-child(4) > button')
		KeywordUtil.markPassed("Delete Config successful")
	}
}