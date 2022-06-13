package project
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.DV360
import com.reusableComponents.TargetObject
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import internal.GlobalVariable

class Targeting extends Builder
{
	String[] IncludeKwords = ["france", "test", "programmatic"]
	String[] ExcludeKwords = ["dance", "game", "cultural",]

	public Targeting()
	{}

	boolean ShouldBeTested(String target, ArrayList<String> testToAvoid)
	{
		if (testToAvoid != [''])
		{
			for (def test in testToAvoid)
			{
				if (target.equals(test) == true)
					return false
			}
		}
		return true
	}

	def cleanTargeting(String itemType, String target)
	{
		/**
		 * This method remove the included object for the specified targeting
		 *
		 * @param itemType : Type of item : "Insertion Order" or "Line Item"
		 * @param target : Name of the targeting to clean included object
		 *
		 */
		KeywordUtil.logInfo("Début clean targeting " + target)
		if (target == 'Keyword')
		{
			KeywordUtil.logInfo("Targeting Keyword ignoré")
			return
		}
		if (target == 'Custom Affinity' || target == 'Conversion Pixel')
		{
			CleanTargetingConvPixelCustAffinity(itemType, target)
			return
		}
		if (ShouldBeTested(target, GlobalVariable.doNotTest) == false)
			return

		switch (target)
		{
			case "Affinity & In Market" :
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/3. Affinity/Affinity category'))
				break;
			case "Audience" :
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Audience category'))
				break;
			case "Category" :
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/4. Category/Category targeting'))
				break;
			case "Geography" :
				if(itemType == 'Insertion Order')
					WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/Insertion order/Geography category'))
				else if(itemType == 'Line Item')
					WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/Line item/Geography category'))
				break;
			case "Inventory Source" :
				if(itemType == 'Insertion Order')
					WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Insertion order/Inventory category'))
				else if(itemType == 'Line Item')
					WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Line item/Inventory category'))
				break;
			case "" :
			// On charge tous les targetings et tous les onglets pour gagner du temps
			// On clique sur les onglets en alternant pour lancer le chargement de chaque liste en arrière-plan
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/3. Affinity/Affinity category'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Audience category'))
				if(itemType == 'Line Item')
					WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/Conversion Pixel tab'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/'+itemType+'/Geography category'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/'+itemType+'/Inventory category'))

				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/3. Affinity/Affinity category'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Second tab unselected'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Audience category'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Second tab unselected'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/'+itemType+'/Geography category'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Second tab unselected'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/'+itemType+'/Inventory category'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Second tab unselected'))

				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/'+itemType+'/Geography category'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Third tab unselected'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/'+itemType+'/Inventory category'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Third tab unselected'))

				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/'+itemType+'/Geography category'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Fourth tab unselected'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/4. Category/Category targeting'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/'+itemType+'/Geography category'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Fifth tab unselected'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/5. Custom list/Line item/Custom list category'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/'+itemType+'/Geography category'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Sixth tab unselected'))

			// Clean de tous les targetings
				cleanTargeting(itemType, "Affinity & In Market")
				cleanTargeting(itemType, "Category")
				if(itemType == 'Line Item')
					cleanTargeting(itemType, "Conversion Pixel")
				cleanTargeting(itemType, "Custom Affinity")
				cleanTargeting(itemType, "Geography")
				cleanTargeting(itemType, "Inventory Source")
				cleanTargeting(itemType, "Audience")
			// Audience 3P prend du temps donc on les charge à la fin et le targeting pour Audience dans checkTargeting() est retardé
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Audience category'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Third tab unselected'))
				break;
			default :
				KeywordUtil.markFailedAndStop(target + ' is not recognized')
				break;
		}
		if(target != "Keyword" && target != "")
		{
			if(target != "Custom Affinity" && target != "Conversion Pixel" && target != "Category")
			{
				if(selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/First tab unselected'))))
					WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/First tab unselected'))
			}
			// Attente du chargement des listes
			assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Spinner'), 240) == true

			// Ajout d'éléments au cas où il n'y en aurait pas (si possible)
			if (selenium.getCssCount(findTestObject('5. Editor/2. Targeting/1. Common/Listed Items').getSelectorCollection().get(SelectorMethod.CSS)) > 1) {
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/First unselected item'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Include button'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/First unselected item'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Exclude button'))
			}
			// Remove all targeting
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Select all Included items'))
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Remove Included button'))
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Select all Excluded items'))
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Remove Excluded button'))
			// Vérification que les listes sont vides
			assert WebUI.verifyElementNotPresent(findTestObject('5. Editor/2. Targeting/1. Common/First Included item'), 2) == true : "List is not empty"
			assert WebUI.verifyElementNotPresent(findTestObject('5. Editor/2. Targeting/1. Common/First Excluded item'), 2) == true : "List is not empty"
		}

		KeywordUtil.logInfo("Fin clean targeting " + target)
		Thread.sleep(250)
	}

	def CleanTargetingConvPixelCustAffinity(String itemType, String target) {
		/**
		 * This method remove the included object for the custom affinity and conversion pixel panel
		 *
		 * @param itemType : Type of item : "Insertion Order" or "Line Item"
		 * @param target : Name of the targeting to clean included object
		 *
		 */
		KeywordUtil.logInfo("Début clean targeting ConvPix_CustAff: " + target)

		if (ShouldBeTested(target, GlobalVariable.doNotTest) == false)
			return

		if (itemType == 'Insertion Order')
			WebUI.click(findTestObject('5. Editor/2. Targeting/3. Custom Affinity/Custom Affinity tab for OI'))
		else if (itemType == 'Line Item' && target == 'Custom Affinity')
			WebUI.click(findTestObject('5. Editor/2. Targeting/3. Custom Affinity/Custom Affinity tab for Line'))
		else if (itemType == 'Line Item' && target == 'Conversion Pixel') {

			KeywordUtil.logInfo("Inside click")
			WebUI.click(findTestObject('5. Editor/2. Targeting/2. Conversion Pixel/Conversion Pixel tab'))
		}
		Thread.sleep(2500)

		KeywordUtil.logInfo("is present ? = " + WebUI.verifyElementPresent(findTestObject('5. Editor/2. Targeting/3. Custom Affinity/List Element left panel'), 1))
		if (WebUI.verifyElementPresent(findTestObject('5. Editor/2. Targeting/3. Custom Affinity/List Element left panel'), 1) == false &&
		driver.findElements(By.cssSelector(findTestObject('5. Editor/2. Targeting/3. Custom Affinity/List Element left panel').getSelectorCollection().get(SelectorMethod.CSS))).size() > 1)
		{
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/First unselected item'))
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Include button'))
		}
		KeywordUtil.logInfo('before remove included')
		WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Select all Included items'))
		WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Remove Selected button'))
		KeywordUtil.logInfo('after remove included')

		assert WebUI.verifyElementVisible(findTestObject('5. Editor/2. Targeting/3. Custom Affinity/Empty span right panel')) == true : "List is not empty"
		KeywordUtil.logInfo("Fin clean targeting ConvPix_CustAff")
		Thread.sleep(250)
	}

	LinkedList<TargetObject> EditTargeting(String target, String itemType, String type, int[] object, LinkedList<TargetObject> results) {
		/**
		 * This method go to the correct panel on the builder for the specified target and edit it
		 *
		 * @param target : Name of the targeting to edit
		 * @param type : Type of edition : "Exclude" or "Include"
		 * @param object : List of items to include/exclude (by index in the list starting from 1)
		 * @param results : List of TargetObject, corresponding to items that will be edited and stored in this list
		 *
		 * @return List of TargetObject
		 */	
		KeywordUtil.logInfo("Début EditTargeting: " + target)
		if(itemType != 'Insertion Order' && itemType != 'Line Item') KeywordUtil.markFailed("This method needs a correct itemType : Insertion Order or Line Item")
		if(type != 'Include' && type != 'Exclude') KeywordUtil.markFailed("This method needs a correct edition type : Include or Exclude")
		Thread.sleep(500)
		if (WebUI.waitForElementNotPresent(findTestObject('Object Repository/6. Objects/Line-OI opened'), 3))
		{
			WebUI.doubleClick(findTestObject('Object Repository/6. Objects/First Loaded ' + itemType))
		}
		Thread.sleep(250)

		if (ShouldBeTested(target, GlobalVariable.doNotTest) == false)
			return results

		switch (target) {
			case "Affinity & In Market" :
				if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/3. Affinity/Affinity category'), "class") != "active")
					WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/3. Affinity/Affinity category'))
				results = EditIncludeExclude(target, type, object, results, 2)
				break;
			case "Audience" :
				if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Audience category'), "class") != "active")
					WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Audience category'))
				results = EditIncludeExclude(target, type, object, results, 3)
				break;
			case "Category" :
				if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/4. Category/Category targeting'), "class") != "active")
					WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/4. Category/Category targeting'))
				results = EditIncludeExclude(target, type, object, results, 1)
				break;
			case "Conversion Pixel" :
				if(itemType == 'Insertion Order') KeywordUtil.markFailed("Cannot target Conversion Pixel for an Insertion Order")
				if (type == 'Exclude')
					KeywordUtil.markFailedAndStop('Exclude Conversion Pixel is not allowed')
				if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/Conversion Pixel tab'), "class") != "active")
					WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/Conversion Pixel tab'))
				results = EditInclude_RemoveInclude(target, type, object, results)
				break;
			case "Custom Affinity" :
				if (type == 'Exclude')
					KeywordUtil.markFailedAndStop('Exclude custom affinity is not allowed')

				if(itemType == 'Insertion Order') {
					if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/5. Custom list/Insertion order/Custom list category'), "class") != "active")
						WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/5. Custom list/Insertion order/Custom list category'))
				} else if(itemType == 'Line Item') {
					if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/5. Custom list/Line item/Custom list category'), "class") != "active")
						WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/5. Custom list/Line item/Custom list category'))
				}
				results = EditInclude_RemoveInclude(target, type, object, results)
				break;
			case "Geography" :
				if(itemType == 'Insertion Order') {
					if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/Insertion order/Geography category'), "class") != "active")
						WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/Insertion order/Geography category'))
				} else if(itemType == 'Line Item') {
					if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/Line item/Geography category'), "class") != "active")
						WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/8. Geography/Line item/Geography category'))
				}
				results = EditIncludeExclude(target, type, object, results, 6)
				break;
			case "Inventory Source" :
				if(itemType == 'Insertion Order') {
					if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Insertion order/Inventory category'), "class") != "active")
						WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Insertion order/Inventory category'))
				} else if(itemType == 'Line Item') {
					if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Line item/Inventory category'), "class") != "active")
						WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Line item/Inventory category'))
				}
				results = EditIncludeExclude(target, type, object, results, 3)
				break;
			case "Keyword" :
				if(itemType == 'Insertion Order') {
					if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/9. Keyword/Insertion order/Keyword category'), "class") != "active")
						WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/9. Keyword/Insertion order/Keyword category'))
				} else if(itemType == 'Line Item') {
					if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/2. Targeting/9. Keyword/Line item/Keyword category'), "class") != "active")
						WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/9. Keyword/Line item/Keyword category'))
				}
				results = EditKeyWords(target, type, IncludeKwords, ExcludeKwords, results)
				break;
			default :
				KeywordUtil.markFailedAndStop(target + ' is not recognized')
				break;
		}
		KeywordUtil.logInfo("Fin EditTargeting")

		return (results)
	}

	LinkedList<TargetObject> EditKeyWords(String target, String type, String[] IncludeKwords, String[] ExcludeKwords, LinkedList<TargetObject> results)
	{
		KeywordUtil.logInfo("Début EditKeyWords")

		String[] keywordsToInclude = []
		String[] keywordsToExclude = []
		int currentKeywordInc = 0
		Map includeTargetingIds = GlobalVariable.includeTargetingIds
		Map excludeTargetingIds = GlobalVariable.excludeTargetingIds
		TestObject keywordIncludeTextarea = findTestObject('Object Repository/5. Editor/2. Targeting/8. Keywords/Keyword Include textarea')
		TestObject keywordExcludeTextarea = findTestObject('Object Repository/5. Editor/2. Targeting/8. Keywords/Keyword Exclude textarea')

		if(includeTargetingIds["Keywords"] == "" || includeTargetingIds["Keywords"] == null) keywordsToInclude = IncludeKwords
		else keywordsToInclude = includeTargetingIds["Keywords"].split(",")

		if(excludeTargetingIds["Keywords"] == "" || excludeTargetingIds["Keywords"] == null) keywordsToExclude = ExcludeKwords
		else keywordsToExclude = excludeTargetingIds["Keywords"].split(",")

		Thread.sleep(2500)

		if (type == 'Include')
		{
			WebUI.setText(keywordIncludeTextarea, "")
			for (int i = 0; i < keywordsToInclude.size(); i++)
			{
				String txt = WebUI.getAttribute(keywordIncludeTextarea, 'value')
				if(keywordsToInclude[i].equals(IncludeKwords) && (includeTargetingIds["Keywords"] == "" || includeTargetingIds["Keywords"] == null))
				{
					txt = txt + "\n" + keywordsToInclude[i]
					WebUI.setText(keywordIncludeTextarea, txt)
				} else
				{
					if(keywordsToInclude[i] != "")
						WebUI.sendKeys(keywordIncludeTextarea, keywordsToInclude[i])
					if(i != keywordsToInclude.size() - 1)
					{
						if(keywordsToInclude[i+1] == "")
							WebUI.sendKeys(keywordIncludeTextarea, ",")
						else if (keywordsToInclude[i] != "")
							WebUI.sendKeys(keywordIncludeTextarea, Keys.chord(Keys.ENTER))
					} else if (keywordsToInclude[i] != "")
					{
						WebUI.sendKeys(keywordIncludeTextarea, Keys.chord(Keys.ENTER))
					}
				}
				if(keywordsToInclude[i] != "")
				{
					def save = new TargetObject()
					save.type = type
					save.target = target
					save.ID = keywordsToInclude[i]
					save.name = keywordsToInclude[i]
					results.add(save)
				}
				Thread.sleep(250)
			}
		} else if (type == 'Exclude')
		{
			WebUI.setText(keywordExcludeTextarea, "")
			for (int i = 0; i < keywordsToExclude.size(); i++)
			{
				String txt = WebUI.getAttribute(keywordExcludeTextarea, 'value')
				if(keywordsToExclude[i].equals(ExcludeKwords) && (excludeTargetingIds["Keywords"] == "" || excludeTargetingIds["Keywords"] == null))
				{
					txt = txt + "\n" + keywordsToExclude[i]
					WebUI.setText(keywordExcludeTextarea, txt)
				} else
				{
					if(keywordsToExclude[i] != "")
						WebUI.sendKeys(keywordExcludeTextarea, keywordsToExclude[i])
					if(i != keywordsToExclude.size() - 1)
					{
						if(keywordsToExclude[i+1] == "")
							WebUI.sendKeys(keywordExcludeTextarea, ",")
						else if (keywordsToExclude[i] != "")
							WebUI.sendKeys(keywordExcludeTextarea, Keys.chord(Keys.ENTER))
					} else if (keywordsToExclude[i] != "")
					{
						WebUI.sendKeys(keywordExcludeTextarea, Keys.chord(Keys.ENTER))
					}
				}
				if(keywordsToExclude[i] != "")
				{
					def save = new TargetObject()
					save.type = type
					save.target = target
					save.ID = keywordsToExclude[i]
					save.name = keywordsToExclude[i]
					results.add(save)
				}
				Thread.sleep(250)
			}
		}

		KeywordUtil.logInfo("Fin EditKeyWords")
		return (results)
	}

	TargetObject RecordInclusion(String target, String type, Map includeTargetingIds, String span)
	{
		/**
		 * This method create the object to be recorded in the list to verify on dv360
		 *
		 * @param target : Name of the targeting to edit
		 * @param type : Type of edition : "Exclude" or "Include"
		 * @param includeTargetingIds : A map of IDs to be exclude or included
		 * @param span : A span containing name and ID of the item just selected
		 *
		 * @return TargetObject: the object to be added in the list to verify
		 */
		String[] split = []
		TargetObject save = new TargetObject()
		save.line = GlobalVariable.items[2]
		save.type = type
		save.target = target

		KeywordUtil.logInfo("Début Record inclusion" + span)

		if (target == "Custom Affinity" && includeTargetingIds["Custom Affinity"] == "" || target == "Conversion Pixel" && includeTargetingIds["Conversion Pixel"] == "")
			split = WebUI.getText(tmp).split('-', 2)
		else
			split = span.split('-', 2)

		save.ID = split[0].trim()
		save.name = split[1].trim()

		return save
	}

	LinkedList<TargetObject> EditInclude_RemoveInclude(String target, String type, int[] object, LinkedList<TargetObject> results)
	{
		/**
		 * This method edits the specified targeting (custom affinity or conversion pixel) section for an (Exclude or Include)
		 *
		 * @param target : Name of the targeting to edit
		 * @param type : Type of edition : "Exclude" or "Include"
		 * @param object : List of items to include/exclude (by index in the list starting from 1)
		 * @param results : List of TargetObject, corresponding to items that will be edited and stored in this list
		 *
		 * @return List of TargetObject
		 */
		KeywordUtil.logInfo("Début EditInclude_RemoveInclude: " + target)
		Thread.sleep(250)
		WebDriver driver = DriverFactory.getWebDriver()
		LinkedList<TargetObject> recordincluded = []
		LinkedList<TargetObject> recordexcluded = []
		ArrayList<WebElement> elementsincluded = []
		Map includeTargetingIds = GlobalVariable.includeTargetingIds
		String[] affinities = []
		String[] conversion = []
		String span = ""
		String[] split = []

		if (target == 'Custom Affinity')
			affinities = includeTargetingIds["Custom Affinity"].split(',')
		else if (target == 'Conversion Pixel')
			conversion = includeTargetingIds["Conversion Pixel"].split(',')



		assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Spinner'), 240) == true

		if (target == 'Conversion Pixel')
			elementsincluded = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/Pixels included'))))
		else if (target == 'Custom Affinity')
			elementsincluded = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/Pixels included 2'))))
		ArrayList<WebElement> elements = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/Pixels not included'))))

		int found = 0
		def save = new TargetObject()

		if (type == 'Include' && includeTargetingIds["Custom Affinity"] != "" && target == 'Custom Affinity')
		{
			for (int it = 0; it < affinities.size(); it++)
			{
				WebUI.setText(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), affinities[it])
				WebUI.sendKeys(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), Keys.chord(Keys.ENTER))
				Thread.sleep(250)
				assert WebUI.verifyElementPresent(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'), 5) : "ID "+ affinities[it] + " is not present is the list (invalid or already selected in opposite target type)"
				span = WebUI.getText(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'))
				KeywordUtil.logInfo('span = ' + span)

				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'))
				WebUI.sendKeys(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), Keys.chord(Keys.SHIFT, Keys.ARROW_UP, Keys.DELETE, Keys.ENTER))
				recordincluded.add(RecordInclusion(target, type, includeTargetingIds, span))
			}
		}
		else if (type == 'Include' && includeTargetingIds["Conversion Pixel"] != "" && target == 'Conversion Pixel')
		{
			for (int it = 0; it < conversion.size(); it++)
			{
				WebUI.setText(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), conversion[it])
				WebUI.sendKeys(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), Keys.chord(Keys.ENTER))
				Thread.sleep(250)
				assert WebUI.verifyElementPresent(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'), 5) : "ID "+ conversion[it] + " is not present is the list (invalid or already selected in opposite target type)"
				span = WebUI.getText(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'))
				KeywordUtil.logInfo('span = ' + span)

				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'))
				WebUI.sendKeys(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), Keys.chord(Keys.SHIFT, Keys.ARROW_UP, Keys.DELETE, Keys.ENTER))
				recordincluded.add(RecordInclusion(target, type, includeTargetingIds, span))
			}
		}
		else if (type == 'Include')
		{
			for (int nbr in object)
			{
				if (nbr > 0 && nbr <= elements.size())
				{
					WebUI.click(changeSelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Not included item checkbox'),"nth-child\\(1\\)","nth-child("+nbr+")"))
					WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), getOnlySelector(changeSelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Item name'),"nth-child\\(1\\)","nth-child("+nbr+")")), true)

					recordincluded.add(RecordInclusion(target, type, includeTargetingIds, span))
				}
				else
					continue
			}
		}

		WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Include button'))

		Thread.sleep(500)
		ArrayList<WebElement> elementsincluded2 = []
		if (target == 'Conversion Pixel')
			elementsincluded2 = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/Pixels included'))))
		else if (target == 'Custom Affinity')
			elementsincluded2 = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/2. Conversion Pixel/Pixels included 2'))))
		int elem2 = 0

		for (WebElement elem1 in elementsincluded)
		{
			save = new TargetObject()
			save.line = GlobalVariable.items[2]
			save.type = type
			save.target = target
			split = elem1.getText().split('-', 2)
			save.ID = split[0].trim()
			save.name = split[1].trim()
			if (elem1.getText() != elementsincluded2[elem2].getText())
				KeywordUtil.markFailedAndStop("Included " + target + " are different1")

			results.add(save)
			elem2++
		}

		for (def elem1 in recordincluded)
		{
			save = new TargetObject()

			save.line = GlobalVariable.items[2]
			save.type = type
			save.target = target
			split = elementsincluded2[elem2].getText().split('-', 2)
			save.ID = split[0].replaceAll("check_box_outline_blank(\\d+) .*", "\$1").trim()
			save.name = split[1].trim()
			KeywordUtil.logInfo('saveid = ' + save.ID)
			KeywordUtil.logInfo('elemid = ' + elem1.ID)
			if (save.ID != elem1.ID)
				KeywordUtil.markFailedAndStop("Included " + target + " Activity are different2")
			results.add(save)
			elem2++;
		}

		KeywordUtil.logInfo("Fin EditInclude_RemoveInclude")
		for (def stylo in results)
		{
			KeywordUtil.logInfo(stylo.line + " ; " + stylo.ID + " ; " + stylo.name + " ; " + stylo.type + " ; " + stylo.target)
		}


		return (results)
	}

	def setupInventorySection (String target, String type) {
		/**
		 * Remove invalid inventories and put inventories in the left panel to allow them to be targeted
		 * 
		 * @param target : Name of the targeting to edit
		 * @param type : Type of edition : "Exclude" or "Include"
		 */

		ArrayList<WebElement> inventories = null
		if (target == 'Inventory Source') {
			inventories = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventories2'))))
			if(!inventories.isEmpty())
			{
				int elemInc = 1
				for (def element in inventories) {
					if (element.text.replace("check_box_outline_blank", "") == "39 - Display & Video 360 Tag" || element.text.replace("check_box_outline_blank", "") == "99 - Supership") {
						WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(changeSelector(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/First inventory item'), "nth-child\\(\\d\\)","nth-child("+elemInc+")"))+"').remove();", null)
					} else {
						elemInc++
					}
				}
			}

			if(type == "Include" && selenium.isElementPresent(getSelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First Included item'))) == true)
			{
				// On retire tous les objets inclus car ils ne sont pas disponibles sinon
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Select all Included items'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Remove Included button'))
			} else if(type == "Exclude" && selenium.isElementPresent(getSelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First Excluded item'))) == true)
			{
				// On retire toutes tous les objets exclus car ils ne sont pas disponibles sinon
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Select all Excluded items'))
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Remove Excluded button'))
			}
		}
	}
	def selectTargetingElements(String target, int[] object, String type, int tabs)
	{
		/**
		 * Select targeting elements depending on global variable
		 * 
		 * @param target 	Name of the targeting to edit
		 * @param object 	List of items to include/exclude (by index in the list starting from 1)
		 * @param type 		Type of edition : "Exclude" or "Include"
		 * @param tabs 		Number of tabs in the targeting section
		 * 
		 * @return records	List of included/excluded elements information
		 */
		LinkedList<TargetObject> recordincluded = []
		LinkedList<TargetObject> recordexcluded = []
		String currentItemText = ""
		int currentAudienceType = 1
		int offset = 0
		int currentId = 0
		boolean firstTabDone = false
		boolean secondTabDone = false
		boolean thirdTabDone = false
		boolean fourthTabDone = false
		boolean fifthTabDone = false
		boolean sixthTabDone = false
		Map includeTargetingIds = GlobalVariable.includeTargetingIds
		Map excludeTargetingIds = GlobalVariable.excludeTargetingIds
		Map currentTargetingIdsMap = null
		String[] currentTabKeys = ["", "", "", "", "", ""]
		int[] previousObjects = object
		boolean multiTabs = false
		boolean areTabsLoaded = false

		if(type == "Include") {
			currentTargetingIdsMap = includeTargetingIds
		} else if (type == "Exclude") {
			currentTargetingIdsMap = excludeTargetingIds
		}

		if(type == "Exclude" || GlobalVariable.targetting == "") areTabsLoaded = true

		switch(target) {
			case "Audience":
				currentTabKeys = ["Audience Advertiser", "1P", "3P", "", "", ""]
				object = getAudienceObjectsToTarget(type, object)
				if(!previousObjects.equals(object) || currentTargetingIdsMap["Audience Advertiser"] != "" || currentTargetingIdsMap["1P"] != "" || currentTargetingIdsMap["3P"] != "") multiTabs = true
				break;
			case "Affinity & In Market":
				currentTabKeys = ["In-Market", "Affinity", "", "", "", ""]
				object = getObjectsToTarget(type, object, currentTabKeys)
				if(!previousObjects.equals(object) || currentTargetingIdsMap["In-Market"] != "" || currentTargetingIdsMap["Affinity"] != "") multiTabs = true
				break;
			case "Geography":
				currentTabKeys = ["France", "Spain", "Italy", "UK", "US", "Others"]
				object = getObjectsToTarget(type, object, currentTabKeys)
				if(!previousObjects.equals(object) || currentTargetingIdsMap["France"] != "" || currentTargetingIdsMap["Spain"] != "" || currentTargetingIdsMap["Italy"] != "" || currentTargetingIdsMap["UK"] != "" || currentTargetingIdsMap["US"] != "" || currentTargetingIdsMap["Others"] != "" ) multiTabs = true
				break
			case "Inventory Source":
				currentTabKeys = ["Exchanges", "Inventory Advertiser", "Network", "", "", ""]
				object = getObjectsToTarget(type, object, currentTabKeys)
				if(!previousObjects.equals(object) || currentTargetingIdsMap["Exchanges"] != "" || currentTargetingIdsMap["Inventory Advertiser"] != "" || currentTargetingIdsMap["Network"] != "") multiTabs = true
				break;
			case "Category":
				currentTabKeys = ["Category", "", "", "", "", ""]
				object = getObjectsToTarget(type, object, currentTabKeys)
				break;
		}

		for (int nbr in object) {
			if(multiTabs == true) {
				if(areTabsLoaded == false) loadAllTabs(currentTabKeys[1], currentTabKeys[2], currentTabKeys[3], currentTabKeys[4], currentTabKeys[5])
				areTabsLoaded = true
				if(currentId < currentTargetingIdsMap[currentTabKeys[0]].split(',').size() && firstTabDone == false && currentTargetingIdsMap[currentTabKeys[0]] != "" && currentTargetingIdsMap[currentTabKeys[0]] != "skip") {
					currentItemText = selectTargetElement(currentTargetingIdsMap, currentTabKeys[0], currentId)
				} else if (currentId < currentTargetingIdsMap[currentTabKeys[0]].split(',').size() && currentTargetingIdsMap[currentTabKeys[0]] == "" && firstTabDone == false) {
					currentItemText = selectDefaultTargetElement(nbr, offset)
				} else if ((currentId >= currentTargetingIdsMap[currentTabKeys[0]].split(',').size() || currentTargetingIdsMap[currentTabKeys[0]] == "skip") && firstTabDone == false) {
					firstTabDone = true
					currentId = 0
					if(currentTargetingIdsMap[currentTabKeys[0]] == "skip") continue
				}

				if(tabs > 1) {
					if(currentId < currentTargetingIdsMap[currentTabKeys[1]].split(',').size() && firstTabDone == true && secondTabDone == false && currentTargetingIdsMap[currentTabKeys[1]] != "" && currentTargetingIdsMap[currentTabKeys[1]] != "skip")  {
						selectTab("Second", currentTabKeys[1])
						currentItemText = selectTargetElement(currentTargetingIdsMap, currentTabKeys[1], currentId)
					} else if (currentId < currentTargetingIdsMap[currentTabKeys[1]].split(',').size() && firstTabDone == true && secondTabDone == false && currentTargetingIdsMap[currentTabKeys[1]] == "") {
						selectTab("Second", currentTabKeys[1])
						currentItemText = selectDefaultTargetElement(nbr, offset)
					} else if ((currentId >= currentTargetingIdsMap[currentTabKeys[1]].split(',').size() || currentTargetingIdsMap[currentTabKeys[1]] == "skip") && firstTabDone == true && secondTabDone == false) {
						secondTabDone = true
						currentId = 0
						if(currentTargetingIdsMap[currentTabKeys[1]] == "skip") continue
					}
				}

				if(tabs > 2) {
					if(currentId < currentTargetingIdsMap[currentTabKeys[2]].split(',').size() && firstTabDone == true  && secondTabDone == true && thirdTabDone == false && currentTargetingIdsMap[currentTabKeys[2]] != "" && currentTargetingIdsMap[currentTabKeys[2]] != "skip")  {
						selectTab("Third", currentTabKeys[2])
						currentItemText = selectTargetElement(currentTargetingIdsMap, currentTabKeys[2], currentId)
					} else if (currentId < currentTargetingIdsMap[currentTabKeys[2]].split(',').size() && firstTabDone == true  && secondTabDone == true && thirdTabDone == false && currentTargetingIdsMap[currentTabKeys[2]] == "") {
						selectTab("Third", currentTabKeys[2])
						currentItemText = selectDefaultTargetElement(nbr, offset)
					} else if ((currentId >= currentTargetingIdsMap[currentTabKeys[2]].split(',').size() || currentTargetingIdsMap[currentTabKeys[2]] == "skip") && firstTabDone == true  && secondTabDone == true && thirdTabDone == false) {
						thirdTabDone = true
						currentId = 0
						if(currentTargetingIdsMap[currentTabKeys[2]] == "skip") continue
					}
				}

				if(tabs > 3) {
					if(currentId < currentTargetingIdsMap[currentTabKeys[3]].split(',').size() && firstTabDone == true  && secondTabDone == true && thirdTabDone == true && fourthTabDone == false && currentTargetingIdsMap[currentTabKeys[3]] != "" && currentTargetingIdsMap[currentTabKeys[3]] != "skip")  {
						selectTab("Fourth", currentTabKeys[3])
						currentItemText = selectTargetElement(currentTargetingIdsMap, currentTabKeys[3], currentId)
					} else if (currentId < currentTargetingIdsMap[currentTabKeys[3]].split(',').size() && firstTabDone == true  && secondTabDone == true && thirdTabDone == true && fourthTabDone == false && currentTargetingIdsMap[currentTabKeys[3]] == "") {
						selectTab("Fourth", currentTabKeys[3])
						currentItemText = selectDefaultTargetElement(nbr, offset)
					} else if ((currentId >= currentTargetingIdsMap[currentTabKeys[3]].split(',').size() || currentTargetingIdsMap[currentTabKeys[3]] == "skip")  && firstTabDone == true  && secondTabDone == true && thirdTabDone == true && fourthTabDone == false) {
						fourthTabDone = true
						currentId = 0
						if(currentTargetingIdsMap[currentTabKeys[3]] == "skip") continue
					}
				}
				if(tabs > 4) {
					if(currentId < currentTargetingIdsMap[currentTabKeys[4]].split(',').size() && firstTabDone == true  && secondTabDone == true && thirdTabDone == true && fourthTabDone == true && fifthTabDone == false && currentTargetingIdsMap[currentTabKeys[4]] != "" && currentTargetingIdsMap[currentTabKeys[4]] != "skip")  {
						selectTab("Fifth", currentTabKeys[4])
						currentItemText = selectTargetElement(currentTargetingIdsMap, currentTabKeys[4], currentId)
					} else if (currentId < currentTargetingIdsMap[currentTabKeys[4]].split(',').size() && firstTabDone == true  && secondTabDone == true && thirdTabDone == true && fourthTabDone == true && fifthTabDone == false && currentTargetingIdsMap[currentTabKeys[4]] == "") {
						selectTab("Fifth", currentTabKeys[4])
						currentItemText = selectDefaultTargetElement(nbr, offset)
					} else if ((currentId >= currentTargetingIdsMap[currentTabKeys[4]].split(',').size() || currentTargetingIdsMap[currentTabKeys[4]] == "skip")  && firstTabDone == true  && secondTabDone == true && thirdTabDone == true && fourthTabDone == true && fifthTabDone == false) {
						fifthTabDone = true
						currentId = 0
						if(currentTargetingIdsMap[currentTabKeys[4]] == "skip") continue
					}
				}
				if(tabs > 5) {
					if(currentId < currentTargetingIdsMap[currentTabKeys[5]].split(',').size() && firstTabDone == true  && secondTabDone == true && thirdTabDone == true && fourthTabDone == true && fifthTabDone == true && sixthTabDone == false && currentTargetingIdsMap[currentTabKeys[5]] != "" && currentTargetingIdsMap[currentTabKeys[5]] != "skip")  {
						selectTab("Sixth", currentTabKeys[5])
						currentItemText = selectTargetElement(currentTargetingIdsMap, currentTabKeys[5], currentId)
					} else if (currentId < currentTargetingIdsMap[currentTabKeys[5]].split(',').size() && firstTabDone == true  && secondTabDone == true && thirdTabDone == true && fourthTabDone == true && fifthTabDone == true && sixthTabDone == false &&  currentTargetingIdsMap[currentTabKeys[5]] == "") {
						selectTab("Sixth", currentTabKeys[5])
						currentItemText = selectDefaultTargetElement(nbr, offset)
					} else if ((currentId >= currentTargetingIdsMap[currentTabKeys[5]].split(',').size() || currentTargetingIdsMap[currentTabKeys[5]] == "skip")  && firstTabDone == true  && secondTabDone == true && thirdTabDone == true && fourthTabDone == true && fifthTabDone == true && sixthTabDone == false) {
						sixthTabDone = true
						currentId = 0
						if(currentTargetingIdsMap[currentTabKeys[5]] == "skip") continue
					}
				}
				currentId++
			} else {
				if(target == "Category" && currentTargetingIdsMap["Category"] != null && currentTargetingIdsMap["Category"] != "") {
					WebUI.clearText(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'))
					WebUI.setText(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), currentTargetingIdsMap["Category"].split(',')[currentId])
					WebUI.sendKeys(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), Keys.chord(Keys.ENTER))
					Thread.sleep(250)
					assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventory Load spinner'), 120) : "Advertiser audiences still loading after 2 minutes"
					assert WebUI.verifyElementPresent(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'), 5) : "ID "+ currentTargetingIdsMap["Category"].split(',')[currentId] + " is not present is the list (invalid or already selected in opposite target type)"
					currentItemText = WebUI.getText(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'))
					WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'))
					WebUI.sendKeys(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), Keys.chord(Keys.SHIFT, Keys.ARROW_UP, Keys.DELETE, Keys.ENTER))
					Thread.sleep(250)

					currentId++
				} else if(selenium.isElementPresent(getSelector(changeSelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First item'),"nth-child\\(1\\)","nth-child(" + (nbr + offset) + ")"))) == false) {
					KeywordUtil.markFailedAndStop("Element with set position is not present : "  + nbr + " + offset : " + offset)
				} else {
					WebUI.click(changeSelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First item'),"nth-child\\(1\\)","nth-child(" + (nbr + offset) + ")"))
					currentItemText = WebUI.getText(changeSelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First item'),"nth-child\\(1\\)","nth-child(" + (nbr + offset) + ")"))
				}
			}

			def save = new TargetObject()

			save.line = GlobalVariable.items[1]
			save.type = type
			save.target = target
			if(target == "Inventory Source") {
				if(firstTabDone == false) save.subTarget = "Exchange"
				else save.subTarget = "Private Deals"
			}
			String[] split = currentItemText.split('-', 2)
			save.ID = split[0].trim()
			save.name = split[1].trim()
			if (type == "Include")
				recordincluded.add(save)
			else if (type == "Exclude")
				recordexcluded.add(save)

			if(target == "Audience") offset = GlobalVariable.targetingOffset // We only set the offset after the first audience (in order to still add an Advertiser audience)
		}

		return [recordincluded, recordexcluded]
	}
	def loadAllTabs(String[] currentTabKeys)
	{
		/**
		 * Load all targeting tabs and ends with the first one
		 *
		 * @param tabNumber			Tab number in letters : "First", "Second", "Third" etc.
		 * @param currentTabKey		Current tab key (label)
		 */

		if(selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/Second tab unselected'))) == true) {
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Second tab unselected'))
			Thread.sleep(500)
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventory Load spinner'), 120) : +currentTabKeys[1]+" still loading after 2 minutes"
		}
		if(selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/Third tab unselected'))) == true) {
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Third tab unselected'))
			Thread.sleep(500)
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventory Load spinner'), 180) : +currentTabKeys[2]+" still loading after 3 minutes"
		}
		if(selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/Fourth tab unselected'))) == true) {
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Fourth tab unselected'))
			Thread.sleep(500)
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventory Load spinner'), 180) : +currentTabKeys[3]+" still loading after 3 minutes"
		}
		if(selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/Fifth tab unselected'))) == true) {
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Fifth tab unselected'))
			Thread.sleep(500)
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventory Load spinner'), 180) : +currentTabKeys[4]+" still loading after 3 minutes"
		}
		if(selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/Sixth tab unselected'))) == true) {
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Sixth tab unselected'))
			Thread.sleep(500)
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventory Load spinner'), 180) : +currentTabKeys[5]+" still loading after 3 minutes"
		}
		if(selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/First tab unselected'))) == true) {
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/First tab unselected'))
			Thread.sleep(500)
		}
		Thread.sleep(1500)
	}
	def selectTab(String tabNumber, String currentTabKey)
	{
		/**
		 * Select targeting tab
		 * 
		 * @param tabNumber			Tab number in letters : "First", "Second", "Third" etc.
		 * @param currentTabKey		Current tab key (label)
		 */
		if(selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/'+tabNumber+' tab unselected'))) == true) {
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/'+tabNumber+' tab unselected'))
			Thread.sleep(500)
			assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventory Load spinner'), 120) : +currentTabKey+" still loading after 2 minutes"
			Thread.sleep(500)
		}
	}
	def selectTargetElement(Map currentTargetingIdsMap, String currentTabKey, int currentId) {
		/**
		 * Select one targeting element and returns its name
		 * 
		 * @param currentTargetingIdsMap	Map containing items to target
		 * @param currentTabKey				Key of the current tab
		 * @param currentId					Index of items containing in the current tab
		 * 
		 * @return currentItemText			Targeted item name
		 */
		WebUI.waitForElementPresent(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), 30)
		WebUI.clearText(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'))
		WebUI.setText(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), currentTargetingIdsMap[currentTabKey].split(',')[currentId])
		WebUI.sendKeys(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), Keys.chord(Keys.ENTER))
		Thread.sleep(250)
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventory Load spinner'), 120) : +currentTabKey+" audiences still loading after 2 minutes"
		assert WebUI.verifyElementPresent(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'), 5) : "ID "+ currentTargetingIdsMap[currentTabKey].split(',')[currentId] + " is not present is the list (invalid or already selected in opposite target type)"
		String currentItemText = WebUI.getText(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'))
		WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First unselected item'))

		return currentItemText
	}
	def selectDefaultTargetElement(int nbr, int offset)
	{
		/**
		 * Select one targeting element and returns its name
		 * 
		 * @param nbr				Index of item to target
		 * @param offset			Index offset
		 * 
		 * @return currentItemText	Targeted item name
		 */
		WebUI.click(changeSelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First item'),"nth-child\\(1\\)","nth-child(" + (nbr + offset) + ")"))
		String currentItemText = WebUI.getText(changeSelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/First item'),"nth-child\\(1\\)","nth-child(" + (nbr + offset) + ")"))
		return currentItemText
	}
	def checkElementsAlreadyIncludedExcluded(LinkedList<TargetObject> recordincluded, LinkedList<TargetObject> recordexcluded, ArrayList<WebElement> elementsincluded1, ArrayList<WebElement> elementsexcluded1)
	{
		/**
		 * Check if elements are already included/excluded, fails if that's the case
		 * 
		 * @param recordincluded	Elements to be included
		 * @param recordincluded	Elements to be excluded
		 * @param elementsincluded1	Included elements before edition
		 * @param elementsexcluded1	Excluded elements before edition
		 */
		//check element already included
		def findvalue = null
		for (def recorded in recordincluded)
		{
			findvalue = elementsincluded1.find{element -> return element.getText() == recorded.ID + ' - ' + recorded.name}
			if (findvalue != null) KeywordUtil.markFailedAndStop(recorded.ID + ' - ' + recorded.name + ' is already included')
		}

		findvalue = null
		//check element already excluded
		for (def recorded in recordexcluded)
		{
			findvalue = elementsexcluded1.find{element -> element.getText() == recorded.ID + ' - ' + recorded.name}
			if (findvalue != null) KeywordUtil.markFailedAndStop(recorded.ID + ' - ' + recorded.name + ' is already excluded')
		}
	}
	LinkedList<TargetObject> EditIncludeExclude(String target, String type, int[] object, LinkedList<TargetObject> results, int tabs) {
		/**
		 * This method edits the specified targeting section for an (Exclude or Include)
		 *
		 * @param target 	Name of the targeting to edit
		 * @param type 		Type of edition : "Exclude" or "Include"
		 * @param object 	List of items to include/exclude (by index in the list starting from 1)
		 * @param results 	List of TargetObject, corresponding to items that will be edited and stored in this list
		 * @param tabs 		Number of tabs in the targeting section
		 *
		 * @return List of TargetObject
		 */

		KeywordUtil.logInfo("Début EditIncludeExclude: " + target)
		Thread.sleep(250)
		ArrayList<WebElement> elementsincluded1
		ArrayList<WebElement> elementsexcluded1
		WebDriver driver = DriverFactory.getWebDriver()
		String baseUrl = 'https://displayvideo.google.com/#ng_nav/overview'
		WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver, baseUrl)

		assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Spinner'), 240) == true

		// Reset section state (tab, search field)
		if(selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/First tab unselected'))) == true)
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/First tab unselected'))
		WebUI.sendKeys(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Left Filter input'), Keys.chord(Keys.SHIFT, Keys.ARROW_UP, Keys.DELETE, Keys.ENTER))
		Thread.sleep(250)

		// Specific setup for Inventory
		setupInventorySection(target, type)

		elementsincluded1 = driver.findElements(By.cssSelector('#include-exclude > div.card.inclusion > div.card-body p > span:nth-last-child(1)'))
		elementsexcluded1 = driver.findElements(By.cssSelector('#include-exclude > div.card.exclusion > div.card-body p > span:nth-last-child(1)'))

		// Click on load more button
		if(type == GlobalVariable.type[0] || (type == "Include" && GlobalVariable.type[0] == "")) {
			if (selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/Audience Load more item'))) == true){
				WebUI.click(findTestObject('5. Editor/2. Targeting/Audience Load more item'))
				Thread.sleep(250)
				WebUI.waitForElementNotPresent(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventory Load spinner'), 120)
			}
		}

		// Select targeting elements depending on global variable
		ArrayList<LinkedList<TargetObject>> records = selectTargetingElements(target, object, type, tabs)

		// Check if elements are already included/excluded
		checkElementsAlreadyIncludedExcluded(records[0], records[1], elementsincluded1, elementsexcluded1)

		// Click on Include / Exclude button
		if (type == "Include") {
			WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Include button'))
		} else if (type == "Exclude") {
			WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/Exclude button'))
		}

		if(target == 'Audience' && type == "Include") {
			WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/New AND Group button'))

			records[0] = records[0].sort{a, b -> Double.valueOf(a.ID) <=> Double.valueOf(b.ID) }

			int currentGroupIndexToSet = 0
			boolean skippedFirstGroup = false
			int objectToGroupIndex = 1
			int numberOfAudiencesToIncludeInFirstGroup = GlobalVariable.audienceIncludeGroups[0]
			for (int numberOfAudiencesToInclude in GlobalVariable.audienceIncludeGroups) {
				int currentAudienceNumber = 1 + numberOfAudiencesToIncludeInFirstGroup
				while(currentAudienceNumber <= numberOfAudiencesToInclude + numberOfAudiencesToIncludeInFirstGroup) {
					// Skip first group because it is already okay
					if(skippedFirstGroup == true) {
						WebUI.selectOptionByIndex(changeSelector(findTestObject('Object Repository/5. Editor/2. Targeting/6. Audience/Included Audience Group select'),"nth-of-type\\(1\\)","nth-of-type\\(" +objectToGroupIndex+"\\)"), currentGroupIndexToSet)
						Thread.sleep(500)
					}
					currentAudienceNumber++
				}
				if(skippedFirstGroup == false) objectToGroupIndex = currentAudienceNumber - numberOfAudiencesToInclude
				currentGroupIndexToSet++
				skippedFirstGroup = true
				WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/New AND Group button'))
			}
		} else if (target == 'Audience' && type == "Exclude") {
			records[1] = records[1].sort{a, b -> Double.valueOf(a.ID) <=> Double.valueOf(b.ID) }
		}


		ArrayList<WebElement> elementsincluded2
		ArrayList<WebElement> elementsexcluded2
		if (target == 'Audience') {
			elementsincluded2 = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/All included items'))))
			elementsexcluded2 = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/All excluded items'))))
		} else {
			elementsincluded2 = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/All included items'))))
			elementsexcluded2 = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/1. Common/All excluded items'))))
		}

		int elem2 = 0
		if (type == 'Include') {
			for (def elem1 in elementsincluded1) {
				def save = new TargetObject()
				save.type = type
				save.target = target
				String[] split = elem1.getText().split('-', 2)
				save.ID = split[0].replaceAll("check_box_outline_blank(\\d+) .*", "\$1").trim()
				save.name = split[1].trim()
				if (elem1.getText() != elementsincluded2[elem2].getText())
					KeywordUtil.markFailedAndStop("Included Object are different")
				results.add(save)
				elem2++;
			}
			for (def elem1 in records[0]) {
				def save = new TargetObject()
				save.type = type
				save.target = target
				String[] split = elementsincluded2[elem2].getText().split('-', 2)
				save.ID = split[0].replaceAll("check_box_outline_blank(\\d+) .*", "\$1").trim()
				save.name = split[1].trim()
				if (save.ID != elem1.ID)
					KeywordUtil.markFailedAndStop("Included Object are different : " + save.ID + " =/= " + elem1.ID)
				results.add(save)
				elem2++
			}
		} else if (type == 'Exclude') {
			elem2 = 0
			for (def elem1 in elementsexcluded1) {
				def save = new TargetObject()
				save.type = type
				save.target = target
				String[] split = elem1.getText().split('-', 2)
				save.ID = split[0].replaceAll("check_box_outline_blank(\\d+) .*", "\$1").trim()
				save.name = split[1].trim()
				if (elem1.getText() != elementsexcluded2[elem2].getText())
					KeywordUtil.markFailedAndStop("Excluded Object are different")
				results.add(save)
				elem2++
			}
			for (def elem1 in records[1]) {
				def save = new TargetObject()
				save.type = type
				save.target = target
				String[] split = elementsexcluded2[elem2].getText().split('-', 2)
				save.ID = split[0].replaceAll("check_box_outline_blank(\\d+) .*", "\$1").trim()
				save.name = split[1].trim()
				if (save.ID != elem1.ID)
					KeywordUtil.markFailedAndStop("Excluded Object are different")

				results.add(save)
				elem2++
			}
		}

		if (elementsincluded1.size() + records[0].size() != elementsincluded2.size())
			KeywordUtil.markFailedAndStop("There is more included object than predicted")
		if (elementsexcluded1.size() + records[1].size() != elementsexcluded2.size())
			KeywordUtil.markFailedAndStop("There is more excluded object than predicted")

		KeywordUtil.logInfo("Fin EditIncludeExclude")

		return (results)
	}

	def editIoInventory()
	{
		driver = getWebDriver()
		WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/Insertion order Inventory button'))
		Thread.sleep(250)
		assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Spinner'), 240) == true
		ArrayList inventories = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventories2'))))
		if (!(inventories.isEmpty())) {
			int elemInc = 1
			for (def element : inventories) {
				if ((element.text.replace('check_box_outline_blank', '') == '39 - Display & Video 360 Tag') || (element.text.replace('check_box_outline_blank', '') == '99 - Supership')) {
					WebUI.executeJavaScript(('document.querySelector(\'' + getOnlySelector(changeSelector(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/First inventory item'), 'nth-child\\(\\d\\)',('nth-child(' + elemInc) + ')'))) + '\').remove();', null)
				} else {
					elemInc++
				}    }    }

		inventories = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventories2'))))
		if (inventories.isEmpty()) {
			assert WebUI.verifyElementPresent(findTestObject('5. Editor/2. Targeting/1. Common/First Excluded item'), 1) == true : 'At least one Exchange must be excluded to be included'
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/First Excluded item'))
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Remove Excluded button'))
		}
		WebUI.waitForElementVisible(findTestObject('5. Editor/2. Targeting/1. Common/First item'), 30)
		WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/First item'))
		WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Include button'))
		inventories = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventories2'))))
		if (inventories.isEmpty()) {
			assert WebUI.verifyElementPresent(findTestObject('5. Editor/2. Targeting/1. Common/First Included item'), 1) == true : 'At least one Exchange must be included to be excluded'
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/First Included item'))
			WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Remove Included button'))
		}
		WebUI.waitForElementVisible(findTestObject('5. Editor/2. Targeting/1. Common/First item'), 30)
		WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/First item'))
		WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Exclude button'))
		driver = DriverFactory.getWebDriver()
		ArrayList includedIoInventories = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('5. Editor/2. Targeting/1. Common/Loader inclusion items'))))
		int j = 0
		int[] includedIoInventoriesNumber = []
		for (def includedIoInventory : includedIoInventories) {
			(includedIoInventories[j]) = includedIoInventory.getText().replaceAll('(\\d+) .*', '$1')
			j++
		}
		ArrayList excludedIoInventories = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('5. Editor/2. Targeting/1. Common/Loader exclusion items'))))
		j = 0
		int[] excludedIoInventoriesNumber = []
		for (def excludedIoInventory : excludedIoInventories) {
			(excludedIoInventories[j]) = excludedIoInventory.getText().replaceAll('(\\d+) .*', '$1')
			j++
		}
		String includedIoInventoriesToCheck = includedIoInventories.toString().replaceAll(',|$', ';').replaceAll('\\[|\\]| ', '')
		String excludedIoInventoriesToCheck = excludedIoInventories.toString().replaceAll(',|$', ';').replaceAll('\\[|\\]| ', '')

		return [includedIoInventoriesToCheck, excludedIoInventoriesToCheck]
	}
	def editLineInventory(String liId)
	{
		DV360 dv360 = new DV360()
		WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Line Item'))
		WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/Line item Inventory button'))
		Thread.sleep(250)
		assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Spinner'), 240) == true
		ArrayList exchangeNames = new ArrayList()
		int exchangeIndex = 0
		int privateDealIndex = 0
		int currentExchangeId = 0
		String currentExchangeName = ''
		ArrayList privateDealNames = new ArrayList()
		int currentPrivateDealId = 0
		String currentPrivateDealName = ''
		int inc = 0
		// On exclut / inclut un Exchange et un Private Deal (Network)
		if (!(GlobalVariable.targetting.equals('Advertiser Inventory'))) {
			// Exchanges
			// Veut-on inclure ou exclure un exchange ?
			if ((GlobalVariable.type[0]).equals('Include')) {
				// Y a-t-il des inventories exclus ?
				boolean isExcludedInventoryPresent = selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Excluded item')))
				boolean isExcludedExchanges = false // Y a-t-il des exchanges déjà exclus ?
				// Si oui, stocker les exchanges à vérifier à partir de la liste des exchanges déjà exclus
				if (isExcludedInventoryPresent == true) {
					inc = 1
					int listLength = selenium.getCssCount(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Excluded Inventories').getSelectorCollection().get(SelectorMethod.CSS))
					while (inc < (listLength + 1)) {
						currentExchangeId = Integer.valueOf(WebUI.getText(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Excluded item'),'nth-of-type\\(\\d\\)', 'nth-of-type(' + inc + ')')).replaceAll('(\\d+) - .*', '$1'))
						currentExchangeName = WebUI.getText(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Excluded item'),'nth-of-type\\(\\d\\)', 'nth-of-type(' + inc + ')')).replaceAll('\\d+ - (.*)', '$1')
						if (currentExchangeId < 100) {
							exchangeNames.add(currentExchangeName)
							isExcludedExchanges = true // Au moins un exchange est déjà exclus
							// Necessaire pour garder l'ordre des indexs
						} else { exchangeNames.add('Skip') }
						inc++
					}
				}
				// Si aucun inventory ou aucun exchange n'est déjà exclus, le test ne peut pas fonctionner, il faut exclure au moins un exchange pour qu'il puisse être inclus
				if (isExcludedExchanges == false) {
					KeywordUtil.markFailedAndStop('At least one exchange must be excluded in order to be included') // Y a-t-il des inventories inclus ?
				} // Y a-t-il des exchanges déjà inclus ?
				// Si oui, stocker les exchanges à vérifier à partir de la liste des exchanges déjà inclus
				// Au moins un exchange est déjà inclus
				// Necessaire pour garder l'ordre des indexs
				// Si aucun inventory ou aucun exchange n'est déjà inclus, le test ne peut pas fonctionner, il faut inclure au moins un exchange pour qu'il puisse être exclus
			} else if ((GlobalVariable.type[0]).equals('Exclude')) {
				boolean isIncludedInventoryPresent = selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Included item')))
				boolean isIncludedExchanges = false
				if (isIncludedInventoryPresent == true) {
					inc = 1
					int listLength = selenium.getCssCount(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Included Inventories').getSelectorCollection().get(SelectorMethod.CSS))
					while (inc < (listLength + 1)) {
						currentExchangeId = Integer.valueOf(WebUI.getText(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Included item'),'nth-of-type\\(\\d\\)', ('nth-of-type(' + inc) + ')')).replaceAll('(\\d+) - .*', '$1'))
						currentExchangeName = WebUI.getText(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Included item'),'nth-of-type\\(\\d\\)', ('nth-of-type(' + inc) + ')')).replaceAll('\\d+ - (.*)', '$1')
						if (currentExchangeId < 100) {
							exchangeNames.add(currentExchangeName)
							isIncludedExchanges = true
						} else { exchangeNames.add('Skip') }
						inc++
					}
				}
				if (isIncludedExchanges == false) { KeywordUtil.markFailedAndStop('At least one exchange must be included in order to be excluded') }
			} else { KeywordUtil.markFailedAndStop('Check the global variable \'type\' is either \'Include\' or \'Exclude\'') }

			// Vérifie sur DV360 la liste des exchanges et renvoie d'index du premier pouvant être inclus ou eclus
			exchangeIndex = (dv360.getReadyExchangeIndex(liId, exchangeNames) + 1)
			if (exchangeIndex == 0) { exchangeIndex = 1 }
			// Inclusion / Exclusion de l'exchange
			if ((GlobalVariable.type[0]).equals('Include')) {
				WebUI.click(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Excluded item'), 'nth-of-type\\(\\d\\)',('nth-of-type(' + exchangeIndex) + ')'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Remove Excluded button'))
				WebUI.setText(findTestObject('5. Editor/2. Targeting/1. Common/Left Filter input'), exchangeNames.get(exchangeIndex -1))
				WebUI.click(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First item'), 'nth-child\\(\\d\\)',('nth-child(' + 1) + ')'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Include button'))
			} else if ((GlobalVariable.type[0]).equals('Exclude')) {
				WebUI.click(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Included item'), 'nth-of-type\\(\\d\\)',('nth-of-type(' + exchangeIndex) + ')'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Remove Included button'))
				WebUI.setText(findTestObject('5. Editor/2. Targeting/1. Common/Left Filter input'), exchangeNames.get(exchangeIndex -1))
				WebUI.click(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First item'), 'nth-child\\(\\d\\)',('nth-child(' + 1) + ')'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Exclude button'))
			}
			// Private Deals (Network)
			WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Advertiser tab'))
			Thread.sleep(250)
			assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Spinner'), 240) == true
			WebUI.click(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Network tab'))
			Thread.sleep(250)
			assert WebUI.waitForElementNotPresent(findTestObject('3. Loader/Loader Spinner'), 240) == true
			// Veut-on inclure ou exclure un private deal ?
			if ((GlobalVariable.type[1]).equals('Include')) {
				// On inclut depuis la liste des private deals ni inclus, ni exclus
				inc = 1
				int listLength = selenium.getCssCount(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Inventories').getSelectorCollection().get(SelectorMethod.CSS))
				while (inc < (listLength + 1)) {
					currentPrivateDealId = Integer.valueOf(WebUI.getText(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First item'),'nth-child\\(\\d\\)', ('nth-child(' + inc) + ')')).replaceAll('(\\d+) - .*', '$1'))
					currentPrivateDealName = WebUI.getText(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First item'),'nth-child\\(\\d\\)', ('nth-child(' + inc) + ')')).replaceAll('\\d+ - (.*)', '$1')
					if (currentPrivateDealId > 100) {
						privateDealNames.add(currentPrivateDealName) // Necessaire pour garder l'ordre des indexs
					} else { privateDealNames.add('Skip') }
					inc++ // On exclut depuis la liste des private deals déjà inclus
				} // Y a-t-il des inventories inclus ?
				// Y a-t-il des private deals déjà inclus ?
				// Si oui, stocker les private deals à vérifier depuis la liste des private deals déjà inclus
				// Au moins un exchange est déjà inclus
				// Necessaire pour garder l'ordre des indexs
				// Si aucun inventory ou aucun private deal n'est déjà inclus, le test ne peut pas fonctionner, il faut inclure au moins un private deal pour qu'il puisse être exclus
			} else if ((GlobalVariable.type[1]).equals('Exclude')) {
				boolean isIncludedInventoryPresent = selenium.isElementPresent(getSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Excluded item')))
				boolean isIncludedPrivateDeals = false
				if (isIncludedInventoryPresent == true) {
					inc = 1
					int listLength = selenium.getCssCount(findTestObject('Object Repository/5. Editor/2. Targeting/7. Inventory/Included Inventories').getSelectorCollection().get(SelectorMethod.CSS))
					while (inc < (listLength + 1)) {
						currentPrivateDealId = Integer.valueOf(WebUI.getText(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Included item'),'nth-of-type\\(\\d\\)', ('nth-of-type(' + inc) + ')')).replaceAll('(\\d+) - .*', '$1'))
						currentPrivateDealName = WebUI.getText(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Included item'),'nth-of-type\\(\\d\\)', ('nth-of-type(' + inc) + ')')).replaceAll('\\d+ - (.*)', '$1')
						if (currentPrivateDealId > 100) {
							privateDealNames.add(currentPrivateDealName)
							isIncludedPrivateDeals = true
						} else { privateDealNames.add('Skip') }
						inc++
					}
				}
				if (isIncludedPrivateDeals == false) { KeywordUtil.markFailedAndStop('At least one private deal must be included in order to be excluded') }
			}

			// Vérifie sur DV360 la liste des private deals et renvoie d'index du premier pouvant être inclus ou eclus
			privateDealIndex = (dv360.getReadyPrivateDealIndex(liId, privateDealNames) + 1)
			// Inclusion / Exclusion du Private Deal
			if ((GlobalVariable.type[1]).equals('Include')) {
				WebUI.click(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First item'), 'nth-child\\(\\d\\)',('nth-child(' + privateDealIndex) + ')'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Include button')) // On exclut / inclut un Private Deal (Advertiser)
			} else if ((GlobalVariable.type[1]).equals('Exclude')) {
				WebUI.click(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First Included item'), 'nth-of-type\\(\\d\\)',('nth-of-type(' + privateDealIndex) + ')'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Remove Included button'))
				WebUI.setText(findTestObject('5. Editor/2. Targeting/1. Common/Left Filter input'), privateDealNames.get(privateDealIndex -1))
				WebUI.click(changeSelector(findTestObject('5. Editor/2. Targeting/1. Common/First item'), 'nth-child\\(\\d\\)',('nth-child(' + 1) + ')'))
				WebUI.click(findTestObject('5. Editor/2. Targeting/1. Common/Exclude button'))
			}
		} else {}
		String liName = WebUI.getText(findTestObject('Object Repository/6. Objects/First Loaded Line Item')).replaceFirst('(.*)\\(\\d+\\)','$1')
		LinkedList<TargetObject> liIncludeResult = []
		LinkedList<TargetObject> liExcludeResult = []
		driver = DriverFactory.getWebDriver()
		ArrayList includedLiInventories = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('5. Editor/2. Targeting/1. Common/Loader inclusion items'))))
		ArrayList excludedLiInventories = driver.findElements(By.cssSelector(getOnlySelector(findTestObject('5. Editor/2. Targeting/1. Common/Loader exclusion items'))))
		int k = 0
		int[] includedLiInventoriesNumber = []
		for (def includedLiInventory : includedLiInventories) {
			(includedLiInventories[k]) = includedLiInventory.getText().replaceAll('(\\d+) .*', '$1')
			k++
		}
		k = 0
		int[] excludedLiInventoriesNumber = []
		for (def excludedLiInventory : excludedLiInventories) {
			(excludedLiInventories[k]) = excludedLiInventory.getText().replaceAll('(\\d+) .*', '$1')
			k++
		}
		String includedLiInventoriesToCheck = includedLiInventories.toString().replaceAll(',|$', ';').replaceAll('\\[|\\]| ', '')
		String excludedLiInventoriesToCheck = excludedLiInventories.toString().replaceAll(',|$', ';').replaceAll('\\[|\\]| ', '')

		return [includedLiInventoriesToCheck, excludedLiInventoriesToCheck]
	}

	def CheckTargetinCSVfile(String fileToUpload, LinkedList<TargetObject> change) {
		/**
		 * This method checks the edited targeting in the CSV file
		 *
		 * @param fileToUpload : Full path to the CSV to check
		 * @param change : List of TargetObject, corresponding to items that were be edited and stored in this list
		 */
		String resultToCheckColumn = ""

		for (def result in change)
		{

			if (result.target == 'Custom List')
				resultToCheckColumn = result.target + " Targeting"
			else if (result.target == 'Custom Affinity')
				resultToCheckColumn = 'Custom List Targeting'
			else if (result.target == 'Conversion Pixel')
				resultToCheckColumn = "Conversion Floodlight Activity Ids"
			else
				resultToCheckColumn = result.target + " Targeting - " + result.type
					
				KeywordUtil.logInfo(resultToCheckColumn)
				checkCsvByName(fileToUpload, resultToCheckColumn, result.ID)

		}
	}

	int[] getObjectsToTarget (String type, int[] objectToTarget, String[] currentTabKeys) {
		/**
		 * Returns the correct array of integer to target for audiences, depending on profile
		 * 
		 * @param type							Type of targeting ("Include" or "Exclude")
		 * @param objectToTarget				Array of audiences to target, to return in case the IDs are not specified in the profile
		 * @param currentTabKeys				Keys in the targetingIds GlobalVariable for the all tabs
		 * 
		 * @return objectsToTarget		Array of integers to target (either for IDs or positions)
		 */

		int totalNumberOfObjectToTarget = 0
		int[] objectsToTarget = null
		Map includeTargetingIds = GlobalVariable.includeTargetingIds
		Map excludeTargetingIds = GlobalVariable.excludeTargetingIds
		Map currentTargetingIds = null
		int numberOfObjectsFirstTab = 0
		int numberOfObjectsSecondTab = 0
		int numberOfObjectsThirdTab = 0
		int numberOfObjectsFourthTab = 0
		int numberOfObjectsFifthTab = 0
		int numberOfObjectsSixthTab = 0

		if(type == "Include") currentTargetingIds = includeTargetingIds
		else if(type == "Exclude") currentTargetingIds = excludeTargetingIds

		if(currentTargetingIds[currentTabKeys[0]] == ""
		&& (currentTargetingIds[currentTabKeys[1]] == null || currentTargetingIds[currentTabKeys[1]] == "")
		&& (currentTargetingIds[currentTabKeys[2]] == null || currentTargetingIds[currentTabKeys[2]] == "")
		&& (currentTargetingIds[currentTabKeys[3]] == null || currentTargetingIds[currentTabKeys[3]] == "")
		&& (currentTargetingIds[currentTabKeys[4]] == null || currentTargetingIds[currentTabKeys[4]] == "")
		&& (currentTargetingIds[currentTabKeys[5]] == null || currentTargetingIds[currentTabKeys[5]] == "") ) {
			objectsToTarget = objectToTarget
		} else {
			numberOfObjectsFirstTab = currentTargetingIds[currentTabKeys[0]].split(",").size()
			if(currentTargetingIds[currentTabKeys[1]] != null && currentTargetingIds[currentTabKeys[1]] != "")
				numberOfObjectsSecondTab = currentTargetingIds[currentTabKeys[1]].split(",").size()
			if(currentTargetingIds[currentTabKeys[2]] != null && currentTargetingIds[currentTabKeys[2]] != "")
				numberOfObjectsThirdTab = currentTargetingIds[currentTabKeys[2]].split(",").size()
			if(currentTargetingIds[currentTabKeys[3]] != null && currentTargetingIds[currentTabKeys[3]] != "")
				numberOfObjectsFourthTab = currentTargetingIds[currentTabKeys[3]].split(",").size()
			if(currentTargetingIds[currentTabKeys[4]] != null && currentTargetingIds[currentTabKeys[4]] != "")
				numberOfObjectsFifthTab = currentTargetingIds[currentTabKeys[4]].split(",").size()
			if(currentTargetingIds[currentTabKeys[5]] != null && currentTargetingIds[currentTabKeys[5]] != "")
				numberOfObjectsSixthTab = currentTargetingIds[currentTabKeys[5]].split(",").size()

			totalNumberOfObjectToTarget = numberOfObjectsFirstTab + numberOfObjectsSecondTab + numberOfObjectsThirdTab + numberOfObjectsFourthTab + numberOfObjectsFifthTab + numberOfObjectsSixthTab

			objectsToTarget = new int[totalNumberOfObjectToTarget]
			for(int i = 0; i < totalNumberOfObjectToTarget;i++) {
				objectsToTarget[i] = i+1
			}
		}

		return objectsToTarget
	}

	int[] getAudienceObjectsToTarget (String type, int[] objectToTarget) {
		/**
		 * Returns the correct array of integer to target for audiences, depending on profile
		 * 
		 * @param type							Type of targeting ("Include" or "Exclude")
		 * @param objectToTarget				Array of audiences to target, to return in case the IDs are not specified in the profile
		 * 
		 * @return audienceObjectsToTarget		Array of integers to include audiences (either for IDs or positions)
		 */

		// On modifie les objets à editer en fonction des variables globales si elles sont remplies

		if(type == "Exclude") return getObjectsToTarget(type, objectToTarget, "Audience Advertiser", "1P", "3P", "", "", "")

		int totalNumberOfAudiencesToTarget = 0
		for (numberOfAudience in GlobalVariable.audienceIncludeGroups) {
			totalNumberOfAudiencesToTarget += numberOfAudience
		}

		Map includeTargetingIds = GlobalVariable.includeTargetingIds
		int numberOfObjectsFirstTab = 0
		int numberOfObjectsSecondTab = 0
		int numberOfObjectsThirdTab = 0

		// On inclut une passe sur les onglets à skip, même si on ne fera rien
		if(includeTargetingIds["Audience Advertiser"] == "skip")
			totalNumberOfAudiencesToTarget++
		if(includeTargetingIds["1P"] == "skip")
			totalNumberOfAudiencesToTarget++
		if(includeTargetingIds["3P"] == "skip")
			totalNumberOfAudiencesToTarget++

		int[] audienceObjectsToTarget = new int[totalNumberOfAudiencesToTarget]
		for(int i = 0; i < totalNumberOfAudiencesToTarget;i++) {
			audienceObjectsToTarget[i] = i+1
		}


		assert includeTargetingIds["Audience Advertiser"] != null && includeTargetingIds["1P"] != null && includeTargetingIds["3P"] != null : 'The variable audienceIncludeGroups does not contain all of the following keys : "Audience Advertiser", "1P" & "3P"'

		if(includeTargetingIds["Audience Advertiser"] == "" && includeTargetingIds["1P"] == "" && includeTargetingIds["3P"] == "") {
			audienceObjectsToTarget = objectToTarget
			assert totalNumberOfAudiencesToTarget == objectToTarget.size() : "Elements in global variable audienceIncludeGroups do not match number of positions in variable objectToInclude"
		} else {
			numberOfObjectsFirstTab = includeTargetingIds["Audience Advertiser"].split(",").size()
			numberOfObjectsSecondTab = includeTargetingIds["1P"].split(",").size()
			numberOfObjectsThirdTab = includeTargetingIds["3P"].split(",").size()

			assert totalNumberOfAudiencesToTarget == numberOfObjectsFirstTab + numberOfObjectsSecondTab + numberOfObjectsThirdTab : "Elements in global variable audienceIncludeGroups do not match number of positions in variable audienceObjectsToInclude"
		}

		return audienceObjectsToTarget
	}

	@Keyword
	def checkTargeting(String item){
		/**
		 * Edit the given targeting
		 * 
		 * @param item
		 * 		Specific item (Insertion order or Line item)
		 * 
		 * @return results
		 * 		List of TargetObject, corresponding to items that will be edited and stored in this list
		 */

		// Si l'on veut utiliser ce paramètre pour audience (tous les onglets de la variable includeTargetingIds vides),
		// il faut faire correspondre le nombre d'élément à la variable audienceIncludeGroups
		int[] ObjectToInclude = [2]
		int[] ObjectToExclude = [1]
		LinkedList<TargetObject> results = []

		KeywordUtil.logInfo("Check targeting")

		if (GlobalVariable.targetting == '') {
			cleanTargeting(item, "")
			results = EditTargeting('Affinity & In Market', item, 'Include', ObjectToInclude, results)
			results = EditTargeting('Affinity & In Market', item, 'Exclude', ObjectToExclude, results)

			results = EditTargeting('Category', item, 'Include', ObjectToInclude, results)
			results = EditTargeting('Category', item, 'Exclude', ObjectToExclude, results)

			if(item=="Line Item"){
				results = EditTargeting('Conversion Pixel', item, 'Include', ObjectToInclude, results)
			}

			results = EditTargeting('Custom Affinity', item, 'Include', ObjectToInclude, results)

			results = EditTargeting('Geography', item, 'Include', ObjectToInclude, results)
			results = EditTargeting('Geography', item, 'Exclude', ObjectToExclude, results)

			results = EditTargeting('Inventory Source', item, 'Include', ObjectToInclude, results)
			results = EditTargeting('Inventory Source', item, 'Exclude', ObjectToExclude, results)

			results = EditTargeting('Keyword', item, 'Include', ObjectToInclude, results)
			results = EditTargeting('Keyword', item, 'Exclude', ObjectToExclude, results)

			results = EditTargeting('Audience', item, 'Include', ObjectToInclude, results)
			results = EditTargeting('Audience', item, 'Exclude', ObjectToExclude, results)
		} else if ((GlobalVariable.type[0]).equals('') == true) {
			cleanTargeting(item, GlobalVariable.targetting)
			EditTargeting(GlobalVariable.targetting, item, 'Include', ObjectToInclude, results)
			if(GlobalVariable.targetting != 'Custom Affinity' && GlobalVariable.targetting != 'Conversion Pixel'){
				EditTargeting(GlobalVariable.targetting, item, 'Exclude', ObjectToExclude, results)
			}
		} else if ((GlobalVariable.type[0]).equals('Include') == true) {
			cleanTargeting(item, GlobalVariable.targetting)
			EditTargeting(GlobalVariable.targetting, item, 'Include', ObjectToInclude, results)
		} else if ((GlobalVariable.type[0]).equals('Exclude') == true) {
			cleanTargeting(item, GlobalVariable.targetting)
			EditTargeting(GlobalVariable.targetting, item, 'Exclude', ObjectToExclude, results)
		}

		return results
	}

}
