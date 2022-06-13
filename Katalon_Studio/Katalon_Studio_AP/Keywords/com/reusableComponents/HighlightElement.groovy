package com.reusableComponents

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords

public class HighlightElement
{

	private static String oldBackground = ''

	@Keyword
	public static void on(TestObject testObject)
	{
		influence(testObject)
	}
	@Keyword
	public static void on1(TestObject testObject)
	{
		influenceHighlightBlue(testObject)
	}

	private static void influence(TestObject testObject)
	{
		try
		{
			WebDriver driver = DriverFactory.getWebDriver()
			WebElement element = WebUiCommonHelper.findWebElement(testObject, 20);
			JavascriptExecutor js = (JavascriptExecutor) driver;

			oldBackground = js.executeScript("return arguments[0].background;", element);

			js.executeScript("arguments[0].setAttribute('style', 'background: yellow;');", element);
			Thread.sleep(250)
			js.executeScript("arguments[0].setAttribute('style', 'background: "+ oldBackground +";');", element);
		} catch (Exception e)
		{
			// TODO use Katalon Logging
			e.printStackTrace()
		}
	}

	private static void influenceHighlightBlue(TestObject testObject)
	{
		try
		{
			WebDriver driver = DriverFactory.getWebDriver()
			WebElement element = WebUiCommonHelper.findWebElement(testObject, 20);
			JavascriptExecutor js = (JavascriptExecutor) driver;

			oldBackground = js.executeScript("return arguments[0].border;", element);

			js.executeScript("arguments[0].setAttribute('style', 'border: 2px outset red;');", element);
			Thread.sleep(1000)
			js.executeScript("arguments[0].setAttribute('style', 'border: "+ oldBackground +";');", element);
		} catch (Exception e)
		{
			// TODO use Katalon Logging
			e.printStackTrace()
		}
	}

	private static List<String> influencedKeywords = ['click', 'selectOptionByIndex', 'selectOptionByLabel', 'selectOptionByValue', 'setEncryptedText', 'getText', 'setText', 'verifyElementText']

	/**
	 * change some of methods of WebUiBuiltInKeywords so that they call HighlightElement.on(testObject)
	 * before invoking their original method body.
	 * 
	 * http://docs.groovy-lang.org/latest/html/documentation/core-metaprogramming.html#metaprogramming
	 */
	@Keyword
	public static void pandemic()
	{
		WebUiBuiltInKeywords.metaClass.'static'.invokeMethod =
		{ String name, args ->
			if (name in influencedKeywords)
			{
				TestObject to = (TestObject)args[0]
				HighlightElement.on(to)
			}
			def result
			try
			{
				result = delegate.metaClass.getMetaMethod(name, args).invoke(delegate, args)
			} catch(Exception e)
			{
				System.out.println("Handling exception for method $name")
			}
			return result
		}
	}
}