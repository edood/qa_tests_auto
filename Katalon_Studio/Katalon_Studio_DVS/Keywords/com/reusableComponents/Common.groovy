package com.reusableComponents

import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import internal.GlobalVariable

public class Common
{
	public WebDriver driver;
	public String baseUrl;
	public WebDriverBackedSelenium selenium;

	TestObject tmp = new TestObject("Common")
	Keys control

	public TestObject getTmp()
	{
		return (tmp)
	}
	@Keyword
	def clickObjectbyCss(String cssSelector)
	{
		WebUI.delay(1)
		this.tmp = WebUI.modifyObjectProperty(this.tmp, 'css', ConditionType.EQUALS.toString(), cssSelector, true)
		WebUI.delay(1)
		WebUI.click(tmp)
	}
	@Keyword
	def waitforCssSelectorVisible(String CssSelector, int timeout)
	{
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), CssSelector, true)
		assert WebUI.waitForElementVisible(tmp, timeout) == true
		WebUI.delay(1)
	}
	public TestObject selectorToTestObject(String selector)
	{
		WebUI.removeObjectProperty(tmp, 'xpath')
		WebUI.removeObjectProperty(tmp, 'css')
		tmp.addProperty('css', ConditionType.EQUALS, selector, true)
		return tmp
	}
	public TestObject xpathSelectorToTestObject(String selector)
	{
		WebUI.removeObjectProperty(tmp, 'css')
		WebUI.removeObjectProperty(tmp, 'xpath')
		tmp.addProperty('xpath', ConditionType.EQUALS, selector, true)
		return tmp
	}
	public TestObject changeSelector(TestObject testObject, String regex, String replacement)
	{
		testObject.setSelectorValue(SelectorMethod.CSS, testObject.getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(regex, replacement))
		return testObject
	}
	public TestObject changeSelectorXpath(TestObject testObject, String regex, String replacement)
	{
		testObject.setSelectorValue(SelectorMethod.XPATH, testObject.getSelectorCollection().get(SelectorMethod.XPATH).replaceFirst(regex, replacement))
		return testObject
	}
	@Keyword
	public String getSelector(TestObject testObject)
	{
		return "css=" + testObject.getSelectorCollection().get(SelectorMethod.CSS)
	}
	public String getOnlySelector(TestObject testObject)
	{
		return testObject.getSelectorCollection().get(SelectorMethod.CSS)
	}

	WebDriverBackedSelenium getSelenium()
	{
		String tiret = '-'
		if (((GlobalVariable.env != 'dev') && (GlobalVariable.env != 'staging')) && (GlobalVariable.env != 'prod') && GlobalVariable.env != '')
		{
			GlobalVariable.env = 'saas.tradelab.fr'
		} else if (GlobalVariable.env == 'prod' || GlobalVariable.env == '')
		{
			tiret = ''
			GlobalVariable.env = ''
		}

		driver = DriverFactory.getWebDriver()
		baseUrl = 'https://' + GlobalVariable.env + tiret + GlobalVariable.env
		return selenium = new WebDriverBackedSelenium(driver, baseUrl)
	}
	WebDriver getWebDriver()
	{
		return driver = DriverFactory.getWebDriver()
	}
	def openNewTab()
	{
		selenium.openWindow("")
		WebUI.switchToWindowIndex(1)
	}
	def closeNewTab()
	{
		WebUI.closeWindowIndex(1)
		WebUI.switchToWindowIndex(0)
	}


	@Keyword
	public void deleteFile(String fileFullPath)
	{
		File file = new File(fileFullPath)
		assert file.delete() == true
		WebUI.delay(1)
		KeywordUtil.logInfo("File deleted : " + file.name)
	}
}
