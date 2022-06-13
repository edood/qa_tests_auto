package editTargeting
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.Exchange
import com.reusableComponents.Line
import com.reusableComponents.OptimizerDV360

import creative.Creative

class targetObject {
	String line
	String target
	String type
	String name
	String ID
	String parent

	public targetObject() {}

	String getId() {
		return (ID)
	}
}

class Targeting extends OptimizerDV360 {
	String[] IncludeKwords
	String[] ExcludeKwords
	int it_obj
	int it_exchange
	int it_creative
	int it_line

	public Targeting() {
		it_obj = 0
		it_exchange = 0
		it_creative = 0
		it_line = 0
		IncludeKwords = ["beauty", "mode", "cosmetics"]
		ExcludeKwords = ["old", "conservative", "religious", "politician"]
	}

	LinkedList<targetObject> EditTargeting(String target, String type, int[] object, LinkedList<targetObject> results) {
		println("debut targeting")
		WebUI.doubleClick(findTestObject('Object Repository/6. Objects/First Loaded Line Item'))
		WebUI.delay(1)
		switch (target) {
			case "Affinity & In Market" :
				clickObjectbyCss('nav[id="editor-menu"] > div:nth-child(2) > div > p:nth-child(2)')
				results = EditIncludeExclude(target, type, object, results)
				break;
			case "Audience" :
				clickObjectbyCss('nav[id="editor-menu"] > div:nth-child(2) > div > p:nth-child(3)')
				clickObjectbyCss('div.btn-list-params > div > label:nth-child(2)')
				waitforCssSelectorVisible('#targeting-include-exclude > div.card.targeting.card-li > div.card-body.hasVirtualList > p:nth-child(2) > i', 30)
				results = EditIncludeExclude(target, type, object, results)
				break;
			case "Category" :
				clickObjectbyCss('nav[id="editor-menu"] > div:nth-child(2) > div > p:nth-child(4)')
				results = EditIncludeExclude(target, type, object, results)
				break;
			case "Conversion Pixel" :
				clickObjectbyCss('nav[id="editor-menu"] > div:nth-child(2) > div > p:nth-child(5)')
				results = EditInclude_RemoveInclude(target, type, object, results)
				break;
			case "Custom Affinity" :
				clickObjectbyCss('nav[id="editor-menu"] > div:nth-child(2) > div > p:nth-child(6)')
				results = EditInclude_RemoveInclude(target, type, object, results)
				break;
			case "Geography" :
				clickObjectbyCss('nav[id="editor-menu"] > div:nth-child(2) > div > p:nth-child(7)')
				results = EditIncludeExclude(target, type, object, results)
				break;
			case "Inventory Source" :
				clickObjectbyCss('nav[id="editor-menu"] > div:nth-child(2) > div > p:nth-child(8)')
				results = EditIncludeExclude(target, type, object, results)
				break;
			case "Keyword" :
				clickObjectbyCss('nav[id="editor-menu"] > div:nth-child(2) > div > p:nth-child(9)')
				results = EditKeyWords(target, type, IncludeKwords, ExcludeKwords, results)
				break;
		}
		println("fin editing")
		return (results)
	}

	LinkedList<targetObject> EditKeyWords(String target, String type, String[] IncludeKwords, String[] ExcludeKwords, LinkedList<targetObject> results) {
		println("Debut Editkeywords")
		if (type == 'Include') {
			for (def str in IncludeKwords) {
				WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), '#targeting-keywords > div:nth-child(1) > textarea', true)
				String txt = WebUI.getAttribute(tmp, 'value')
				txt = txt + "\n" + str
				WebUI.setText(tmp, txt)

				def save = new targetObject()
				save.line = "Test_Fabien_Line_Juil_Aout"
				save.type = type
				save.target = target
				save.ID = str
				save.name = str
				results.add(save)
			}
		}
		else if (type == 'Exclude') {
			for (def str in ExcludeKwords) {
				WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), '#targeting-keywords > div:nth-child(2) > textarea', true)
				String txt = WebUI.getAttribute(tmp, 'value')
				txt = txt + "\n" + str
				WebUI.setText(tmp, txt)

				def save = new targetObject()
				save.line = "Test_Fabien_Line_Juil_Aout"
				save.type = type
				save.target = target
				save.ID = str
				save.name = str
				results.add(save)
			}
		}

		println("Fin Editkeywords")
		return (results)
	}

	LinkedList<targetObject> EditInclude_RemoveInclude(String target, String type, int[] object, LinkedList<targetObject> results) {
		println("debut EditInclude_RemoveInclude")
		WebUI.delay(1)
		WebDriver driver = DriverFactory.getWebDriver()
		LinkedList<targetObject> recordincluded = []
		LinkedList<targetObject> recordexcluded = []
		ArrayList<WebElement> elementsincluded = []
		if (target == 'Conversion Pixel')
			elementsincluded = driver.findElements(By.cssSelector('#targeting-include > div.card.inclusion.card-li > div.card-body > p > span:nth-of-type(3)'))
		else if (target == 'Custom Affinity')
			elementsincluded = driver.findElements(By.cssSelector('#targeting-include > div.card.inclusion.card-li > div.card-body > p > span'))
		ArrayList<WebElement> elements = driver.findElements(By.cssSelector('#targeting-include > div.card.targeting.card-li > div.card-body.hasVirtualList > p > span'))
		for (int nbr in object) {
			def save = new targetObject()

			if (type == 'Include') {
				if (nbr > 0 && nbr <= elements.size()) {
					clickObjectbyCss('#targeting-include > div.card.targeting.card-li > div.card-body.hasVirtualList > p:nth-child(' + nbr + ') > i')
					WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), '#targeting-include > div.card.targeting.card-li > div.card-body.hasVirtualList > p:nth-child(' + nbr + ') > span', true)
				}
				else
					continue
			}
			else if (type == 'Exclude') {
				if (nbr > 0 && nbr <= elementsincluded.size()) {
					clickObjectbyCss('#targeting-include > div.card.inclusion.card-li > div.card-body > p:nth-child(' + nbr + ') > i')
					if (target == 'Conversion Pixel')
						WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), '#targeting-include > div.card.inclusion.card-li > div.card-body > p:nth-child(' + nbr + ') > span:nth-child(4)', true)
					else if (target == 'Custom Affinity')
						WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), '#targeting-include > div.card.inclusion.card-li > div.card-body > p:nth-child(' + nbr + ') > span', true)
				}
				else
					continue
			}
			save.line = "Test_Fabien_Line_Juil_Aout"
			save.type = type
			save.target = target
			String[] split = WebUI.getText(tmp).split('-', 2)
			save.ID = split[0].trim()
			save.name = split[1].trim()
			if (type == 'Include')
				recordincluded.add(save)
			else if (type == 'Exclude')
				recordexcluded.add(save)
		}

		if (type == 'Include')
			clickObjectbyCss('#targeting-include > div.targeting > div.card-header > div.card-header-main > div > button.include')
		else if (type == 'Exclude')
			clickObjectbyCss('#targeting-include > div.card.inclusion.card-li > div.card-header > div.card-header-main > div > button')

		ArrayList<WebElement> elementsincluded2 = []
		if (target == 'Conversion Pixel')
			elementsincluded2 = driver.findElements(By.cssSelector('#targeting-include > div.card.inclusion.card-li > div.card-body > p > span:nth-of-type(3)'))
		else if (target == 'Custom Affinity')
			elementsincluded2 = driver.findElements(By.cssSelector('#targeting-include > div.card.inclusion.card-li > div.card-body > p > span'))
		ArrayList<WebElement> elements2 = driver.findElements(By.cssSelector('#targeting-include > div.card.targeting.card-li > div.card-body.hasVirtualList > p > span'))
		int elem2 = 0

		if (type == 'Include') {
			for (WebElement elem1 in elementsincluded) {
				def save = new targetObject()
				save.line = "Test_Fabien_Line_Juil_Aout"
				save.type = type
				save.target = target
				String[] split = elem1.getText().split('-', 2)
				save.ID = split[0].trim()
				save.name = split[1].trim()
				if (elem1.getText() != elementsincluded2[elem2].getText())
					KeywordUtil.markFailedAndStop("Included Conversion Pixel are different")

				results.add(save)
				elem2++
			}
			for (def elem1 in recordincluded) {
				def save = new targetObject()

				save.line = "Test_Fabien_Line_Juil_Aout"
				save.type = type
				save.target = target
				String[] split = elementsincluded2[elem2].getText().split('-', 2)
				save.ID = split[0].replaceAll("check_box_outline_blank(\\d+) .*", "\$1").trim()
				save.name = split[1].trim()
				if (save.ID != elem1.ID)
					KeywordUtil.markFailedAndStop("Included Conversion Pixel are different")
				results.add(save)
				elem2++;
			}
		}
		else if (type == 'Exclude') {
			elem2 = 0;
			println("exclude part 1")
			for (def elemexc in recordexcluded) {
				for (def elem in elementsincluded2) {
					def save = new targetObject()
					String[] split = elem.getText().split('-', 2)
					save.ID = split[0].replaceAll("check_box_outline_blank(\\d+) .*", "\$1").trim()
					save.name = split[1].trim()
					if (save.ID == elemexc.ID)
						KeywordUtil.markFailedAndStop(save.ID + " - " + save.name + " was not excluded")
				}
			}
			println("exclude part 2")
			for (def elemtoexc in recordexcluded) {
				for (def elem in results) {
					if (elem.ID == elemtoexc.ID) {
						results.remove(elem)
						break
					}
				}
			}

			if (elementsincluded.size() - recordexcluded.size() != elementsincluded2.size()) {
				KeywordUtil.markFailedAndStop("Unexpected Object were excluded")
			}
		}
		println("Fin EditInclude_RemoveInclude")
		for (def stylo in results) {
			println(stylo.line + " ; " + stylo.ID + " ; " + stylo.name + " ; " + stylo.type + " ; " + stylo.target)
		}


		return (results)
	}

	LinkedList<targetObject> EditIncludeExclude(String target, String type, int[] object, LinkedList<targetObject> results) {
		println("debut EditIncludeExclude")
		WebUI.delay(1)
		LinkedList<targetObject> recordincluded = []
		LinkedList<targetObject> recordexcluded = []
		ArrayList<WebElement> elementsincluded1
		ArrayList<WebElement> elementsexcluded1
		WebDriver driver = DriverFactory.getWebDriver()
		if (target == 'Audience') {
			elementsincluded1 = driver.findElements(By.cssSelector('#include-exclude > div.card.inclusion.card-li > div.card-body > p > span:nth-of-type(2)'))
			elementsexcluded1 = driver.findElements(By.cssSelector('#include-exclude > div.card.exclusion.card-li > div.card-body > p > span:nth-of-type(2)'))
		}
		else {
			elementsincluded1 = driver.findElements(By.cssSelector('#include-exclude > div.card.inclusion.card-li > div.card-body > p > span'))
			elementsexcluded1 = driver.findElements(By.cssSelector('#include-exclude > div.card.exclusion.card-li > div.card-body > p > span'))
		}
		ArrayList<WebElement> elements1 = driver.findElements(By.cssSelector('#targeting-include-exclude > div.card.targeting.card-li > div.card-body.hasVirtualList > p'))

		for (int nbr in object) {
			def save = new targetObject()
			clickObjectbyCss('#targeting-include-exclude > div.card.targeting.card-li > div.card-body.hasVirtualList > p:nth-child(' + nbr + ') > i')
			WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), '#targeting-include-exclude > div.card.targeting.card-li > div.card-body.hasVirtualList > p:nth-child(' + nbr + ') > span', true)
			save.line = "Test_Fabien_Line_Juil_Aout"
			save.type = type
			save.target = target
			String[] split = WebUI.getText(tmp).split('-', 2)
			save.ID = split[0].trim()
			save.name = split[1].trim()
			if (type == "Include")
				recordincluded.add(save)
			else if (type == "Exclude")
				recordexcluded.add(save)
		}

		if (type == "Include")
			clickObjectbyCss('#targeting-include-exclude > div.targeting > div.card-header > div.card-header-main > div > button.include')
		else if (type == "Exclude")
			clickObjectbyCss('#targeting-include-exclude > div.targeting > div.card-header > div.card-header-main > div > button.exclude')


		ArrayList<WebElement> elementsincluded2
		ArrayList<WebElement> elementsexcluded2
		if (target == 'Audience') {
			elementsincluded2 = driver.findElements(By.cssSelector('#include-exclude > div.card.inclusion.card-li > div.card-body > p > span:nth-of-type(2)'))
			elementsexcluded2 = driver.findElements(By.cssSelector('#include-exclude > div.card.exclusion.card-li > div.card-body > p > span:nth-of-type(2)'))
		}
		else {
			elementsincluded2 = driver.findElements(By.cssSelector('#include-exclude > div.card.inclusion.card-li > div.card-body > p > span'))
			elementsexcluded2 = driver.findElements(By.cssSelector('#include-exclude > div.card.exclusion.card-li > div.card-body > p > span'))
		}
		ArrayList<WebElement> elements2 = driver.findElements(By.cssSelector('#targeting-include-exclude > div.card.targeting.card-li > div.card-body.hasVirtualList > p'))

		int elem2 = 0;
		if (type == 'Include') {
			for (def elem1 in elementsincluded1) {
				def save = new targetObject()
				save.line = "Test_Fabien_Line_Juil_Aout"
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
			for (def elem1 in recordincluded) {
				def save = new targetObject()
				save.line = "Test_Fabien_Line_Juil_Aout"
				save.type = type
				save.target = target
				String[] split = elementsincluded2[elem2].getText().split('-', 2)
				save.ID = split[0].replaceAll("check_box_outline_blank(\\d+) .*", "\$1").trim()
				save.name = split[1].trim()
				if (save.ID != elem1.ID)
					KeywordUtil.markFailedAndStop("Included Object are different")
				results.add(save)
				elem2++;
			}
			println("third for excluded")
		}
		else if (type == 'Exclude') {
			elem2 = 0;
			for (def elem1 in elementsexcluded1) {
				def save = new targetObject()
				save.line = "Test_Fabien_Line_Juil_Aout"
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
			for (def elem1 in recordexcluded) {
				def save = new targetObject()
				save.line = "Test_Fabien_Line_Juil_Aout"
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

		if (elementsincluded1.size() + recordincluded.size() != elementsincluded2.size())
			KeywordUtil.markFailedAndStop("There is more included object than predicted")
		if (elementsexcluded1.size() + recordexcluded.size() != elementsexcluded2.size())
			KeywordUtil.markFailedAndStop("There is more excluded object than predicted")


		println("fin EditIncludeExclude")

		return (results)
	}

	String[] RecordTargetingInventorySource(targetObject obj, boolean bef_or_aft) {
		String[] record = ['', '', '', '', '', '', '']

		Exchange infoparent = new Exchange()

		println("recordtargetingInventorySource")
		WebUI.delay(1)
		WebUI.click(findTestObject('10. DV360/DV360 search button'))
		WebUI.setText(findTestObject('10. DV360/input_Search dv360'), obj.line)
		WebUI.waitForElementVisible(findTestObject('10. DV360/Search suggestion'), 30)
		WebUI.click(findTestObject('10. DV360/Search suggestion'), FailureHandling.STOP_ON_FAILURE)
		WebUI.delay(15)
		println(obj.name)
		record[3] = obj.name.trim()
		if (obj.name.find(/deal/) == null) {
			record[0] = WebUI.getText(findTestObject('DV360 Line Item Details/Public Inventories'))
			println('record[0] = ' + record[0])
			WebUI.click(findTestObject('DV360 Line Item Details/EditPublicInventories'))
			WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(2) > ess-cell:nth-child(2)', true)
			int j = 2
			println("check condition = " + WebUI.getText(tmp).find(obj.parent))
			println("obj parent = " + obj.parent)
			String temporaire = obj.parent.replaceFirst('\\(\\d+\\)', '')
			println("temporaire = " + obj.parent)
			for (; WebUI.getText(tmp).find(temporaire) == null; j++) {
				WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(' + j + ') > ess-cell:nth-child(2)', true)
				println('search exchange = ' + WebUI.getText(tmp))
			}
			String[] subexchange = []
			println('tmp = ' + WebUI.getText(tmp))
			String tempo = WebUI.getText(tmp).find("\\d+ of \\d+")
			println('tempo = ' + tempo)
			subexchange = tempo.split(' of ')
			println('subexchange[0] = ' + subexchange[0])
			println('subexchange[1] = ' + subexchange[1])
			j = j - 1
			WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(' + j + ') > tools-cell > div > mat-checkbox', true)
			int save_parent = j
			if (subexchange == []) // cas de l'inventory sans exchange
			{
				if (WebUI.getAttribute(tmp, 'aria-checked') == "true")
					record[2] = "checked"
				else if (bef_or_aft == true)
						KeywordUtil.markFailedAndStop(obj.name.trim() + " is not included before exclusion")
				else
					record[2] = "unchecked"
			}
			else if (subexchange != []&& subexchange[0] != subexchange[1]) // cas ou certain inventory de l'exchange sont inclus
			{
				WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(' + j + ') > ess-cell:nth-child(2) > zippy-icon > material-icon', true)
				WebUI.click(tmp)
				WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(' + j + ') > ess-cell:nth-child(2)', true)
				println('obj name = ' + obj.name)
				for (; WebUI.getText(tmp) != obj.name.trim() && j < save_parent + Integer.parseInt(subexchange[1]); j++)
				{
					WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(' + j + ') > ess-cell:nth-child(2)', true)
					println(WebUI.getText(tmp))
				}
				if (WebUI.getText(tmp) == obj.name.trim())
				{
					j = j - 1
					WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(' + j + ') > tools-cell > div > mat-checkbox', true)

					if (WebUI.getAttribute(tmp, 'aria-checked') == "true")
						record[2] = "checked"
					else if (bef_or_aft == true)
						KeywordUtil.markFailedAndStop(obj.name.trim() + " is not included before exclusion")
					else
						record[2] = "unchecked"
					record[1] = "unchecked"
					record[5] = subexchange[0]
					record[6] = subexchange[1]
					if (bef_or_aft == true)
					{
						infoparent.checked = false
						infoparent.actual_included_subexchange = Integer.parseInt(subexchange[0])
						infoparent.total_included_subexchange = Integer.parseInt(subexchange[1])
					}
				}
			}
			else if (subexchange != []&& subexchange[0] == subexchange[1]) // cas ou TOUT les inventory de l'exchange sont inclus
			{
				println("cas de tous les inventories")
				WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(' + j + ') > ess-cell:nth-child(2) > zippy-icon > material-icon', true)
				WebUI.click(tmp)
				WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(' + j + ') > ess-cell:nth-child(2)', true)
				println('obj name = ' + obj.name)
				for (; WebUI.getText(tmp) != obj.name.trim() && j < save_parent + Integer.parseInt(subexchange[1]); j++)
				{
					WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(' + j + ') > ess-cell:nth-child(2)', true)
					println(WebUI.getText(tmp))
				}
				if (WebUI.getText(tmp) == obj.name.trim())
				{
					j = j - 1
					println('div:nth-child(' + j + ') > tools-cell > div > mat-checkbox')
					WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'div:nth-child(' + j + ') > tools-cell > div > mat-checkbox', true)
					println('getattribute check = ' + WebUI.getAttribute(tmp, 'aria-checked'))

					if (WebUI.getAttribute(tmp, 'aria-checked') == "true")
						record[2] = "checked"
					else if (bef_or_aft == true)
						KeywordUtil.markFailedAndStop(obj.name.trim() + " is not included before exclusion")
					else
						record[2] = "unchecked"
					record[1] = "checked"
					record[5] = subexchange[0]
					record[6] = subexchange[1]
					if (bef_or_aft == true)
					{
						infoparent.checked = true
						infoparent.actual_included_subexchange = Integer.parseInt(subexchange[0])
						infoparent.total_included_subexchange = Integer.parseInt(subexchange[1])
					}
				}

			}
			record[4] = obj.parent
			if (bef_or_aft == true)
			{
				this.it_exchange = 0
				while (this.it_exchange < this.line.size() && this.line[this.it_exchange].name != obj.line)
					this.it_exchange++
				println('this.it_exchange = ' + this.it_exchange)
				println('this.line.size = ' + this.line.size())
				if (this.it_exchange == this.line.size())
				{
					println('Ajout de la ligne : ' + obj.line)
					this.line[this.it_exchange] = new Line(obj.line)
				}
				this.line[this.it_exchange].count_exchange[0] = Integer.parseInt(record[0].find("^\\d+"))
				this.line[this.it_exchange].count_exchange[1] = Integer.parseInt(record[0].find(" \\d+ ").trim())
				println('this.it_exchange = ' + this.it_exchange)
				this.line[this.it_exchange].InventorySourceSummary = WebUI.getText(findTestObject('DV360 Line Item Details/Public Inventories'))
				infoparent.name = obj.parent
				infoparent.line  = obj.line
				println("this.it_obj = " + this.it_obj)
				if (this.it_obj == this.line[this.it_exchange].parent.size())
					this.line[this.it_exchange].parent += new Exchange(infoparent)
				else if (this.it_obj < this.line[this.it_exchange].parent.size())
					this.line[this.it_exchange].parent[this.it_obj] = infoparent
			}
			else if (bef_or_aft == false)
			{
				for (this.it_exchange = 0; this.it_exchange < this.line.size() && this.line[this.it_exchange].name != obj.line; this.it_exchange++)

					println('this.it_exchange = ' + this.it_exchange)
				this.line[this.it_exchange].InventorySourceSummary = WebUI.getText(findTestObject('DV360 Line Item Details/Public Inventories'))

			}
		}
		else // Partie sur les deals
		{
			record[0] = WebUI.getText(findTestObject('DV360 Line Item Details/Private Deals'))
			this.line[this.it_exchange].InventorySourceSummary = WebUI.getText(findTestObject('DV360 Line Item Details/Private Deals'))
			println(record[0])
		}
		WebUI.click(findTestObject('DV360 Line Item Details/Close button Editor'))
		WebUI.delay(2)
		println('end recordtargeting')
		return record
	}

	LinkedList<String> RecordTargetingCreative(targetObject obj, boolean bef_or_aft)
	{
		int found = 0
		LinkedList<String> record = []
		this.it_exchange = 0
		println("recordtargetingCreative")
		WebUI.delay(1)
		WebUI.click(findTestObject('10. DV360/DV360 search button'))
		WebUI.setText(findTestObject('10. DV360/input_Search dv360'), obj.line)
		WebUI.waitForElementVisible(findTestObject('10. DV360/Search suggestion'), 30)
		WebUI.click(findTestObject('10. DV360/Search suggestion'), FailureHandling.STOP_ON_FAILURE)
		WebUI.delay(15)
		println(obj.name)
		//	   WebUI.executeJavaScript('document.querySelector(\'#creatives-title > span\').scrollIntoView({block: \'center\'})', null)
		WebUI.switchToFrame(findTestObject('Frame/creative'), 60)

		ArrayList<WebElement> creative = this.driver.findElements(By.cssSelector('tr > td span.item-id > span:nth-child(1)'))

		println('creative size = ' + creative.size())
		if (creative.size == 0 && bef_or_aft == true)
			KeywordUtil.markFailedAndStop('Creative ' + obj.name + ' n est pas incluse avant exclusion')
		
		if (bef_or_aft == true)
		{
			for (def crea in creative)
			{
				println('crea = '+ crea.getText())
				println('obj.id = '+ obj.ID)
				if (crea.getText() == obj.ID)
					found = 1
			}
			if (found == 0)
				KeywordUtil.markFailedAndStop('Creative ' + obj.name + ' n est pas incluse avant exclusion')
		}
		if (creative.size > 0)
		{
			println('creatives text = ' + creative[0].getText())

			for (def crea in creative)
			{
				if (bef_or_aft == true)
					this.line[this.it_line].creatives += new Creative(crea.getText())
				record += crea.getText()
			}
		}
		else if (creative.size == 0 && bef_or_aft == false)
			record += ""

		WebUI.delay(2)
		println('end recordtargeting')
		WebUI.switchToDefaultContent()
		return (record)
	}

	LinkedList<String[]> RecordTargeting(LinkedList<targetObject> modifiedobject, boolean bef_or_aft)
	{
		LinkedList<String[]> Objbeforeupload = []
		for (def obj in modifiedobject)
		{
			this.it_obj = 0
			switch (obj.target)
			{
				case "Inventory Source":
					if (bef_or_aft == true)
					{

						for (this.it_exchange = 0; this.it_exchange < this.line.size() && this.line[this.it_exchange].name != obj.line; this.it_exchange++)
							println('this.it_exchange = ' + this.it_exchange)
						if (this.it_exchange == this.line.size())
						{
							println('Ajout dune nouvelle line : ' + obj.line + ' at index : ' + this.it_exchange)
							this.line += new Line(obj.line)
						}
						println('this.it_exchange = ' + this.it_exchange)
						//			if (this.line[this.it_exchange].parent.size() >= 0)
						//			{
						println('size parent = ' + this.line[this.it_exchange].parent.size())
						int i = 0
						for (; i < this.line[this.it_exchange].parent.size() && this.line[this.it_exchange].parent[i].name != obj.parent.trim(); i++)
							println('parent[i].name = ' + this.line[this.it_exchange].parent[i].name + ' et obj.parent.trim() = ' + obj.parent.trim())
						println('i = ' + i + ' et size = ' + this.line[this.it_exchange].parent.size() + ' et obj parent name = ' + obj.parent)
						if (i == this.line[this.it_exchange].parent.size())
						{
							println('je rajoute un exchange a la fin car cest un nouveau parent')
							this.it_obj = this.line[this.it_exchange].parent.size()
						}
						else
						{
							println('parent est lechange a lindex : ' + i)
							this.it_obj = i
						}
						//			}
					}
					Objbeforeupload.add(RecordTargetingInventorySource(obj, bef_or_aft))
					break

				case "Creative":
					for (this.it_line = 0; this.it_line < this.line.size() && this.line[this.it_line].name != obj.line; this.it_line++)

						println('this.itline = ' + this.it_line)
					if (this.it_line == this.line.size() && bef_or_aft == true)
					{
						println('Ajout dune nouvelle line : ' + obj.line + ' at index : ' + this.it_line)
						this.line += new Line(obj.line)
					}
					Objbeforeupload.add(RecordTargetingCreative(obj, bef_or_aft))
					break
			}
		}
		for (def str in Objbeforeupload)
			println('obj = ' + str)
		return (Objbeforeupload)
	}

	def CheckTargetingInventorySource(targetObject obj, LinkedList<String[]> beforeupload, LinkedList<String[]> afterupload)
	{
		this.it_exchange = 0
		while (this.it_exchange < this.line.size() && this.line[this.it_exchange].name != obj.line)
			this.it_exchange++

		println('Check targeting this.it_exchange = ' + this.it_exchange)

		int it_parent = 0;
		println('parentsize check = ' + this.line[this.it_exchange].parent.size())

		while (it_parent < this.line[this.it_exchange].parent.size() && obj.parent.trim() != this.line[this.it_exchange].parent[it_parent].name)
			it_parent++

		println('it_parent = ' + it_parent)
		println('it_exchange = ' + it_exchange)

		println('this.line[this.it_exchange].parent[it_parent].name = ' + this.line[this.it_exchange].parent[it_parent].name)
		if (this.line[this.it_exchange].parent[it_parent].name == obj.parent.trim())
		{
			if (this.line[this.it_exchange].parent[it_parent].actual_included_subexchange == this.line[this.it_exchange].parent[it_parent].total_included_subexchange)
			{
				println('par ici')
				this.line[this.it_exchange].count_exchange[0]--
				println('nombre subexchange avant = ' + this.line[this.it_exchange].count_exchange[1])
				this.line[this.it_exchange].count_exchange[1] += this.line[this.it_exchange].parent[it_parent].actual_included_subexchange - 1
				println('nombre subexchange après = ' + this.line[this.it_exchange].count_exchange[1])
				this.line[this.it_exchange].parent[it_parent].checked = false
			}
			else if (this.line[this.it_exchange].parent[it_parent].actual_included_subexchange != this.line[this.it_exchange].parent[it_parent].total_included_subexchange)
			{
				println('par la')
				this.line[this.it_exchange].count_exchange[1]--
			}
			this.line[this.it_exchange].parent[it_parent].actual_included_subexchange--
		}

	}

	def CheckTargetingCreative(Line line, targetObject obj, LinkedList<String[]> beforeupload, LinkedList<String[]> afterupload)
	{
		println('line name = ' + line.name)
		String[] check = []

		for (def str in afterupload)
			println('afterupload contient creative iD : ' + str)
		if (line.name == obj.line)
		{
			for (def creaID in afterupload)
			{
				println('comparaison ' + creaID + ' et ' + obj.ID)
				if (creaID.equals(obj.ID) == true)
					KeywordUtil.markFailedAndStop('Creative : ' + creaID + ' was not excluded')
			}

		}

	}

	def CheckTargeting(LinkedList<targetObject> result, LinkedList<String[]> beforeupload, LinkedList<String[]> afterupload, String test)
	{
		int total_crea_count_final = 0;
		int total_crea_count_initial = 0;
		this.it_exchange = 0
		for (def line in this.line)
		{
			println('line actuelle : ' + line.name)
			if (line.parent.size() > 0)
			{
				for (def tmp in line.parent)
					println('parent.name = ' + tmp.name + ' et parent.actual_sub = ' + tmp.actual_included_subexchange + ' et parent.total_sub = ' + tmp.total_included_subexchange + ' et check = ' + tmp.checked)
			}
			else if (line.creatives.size() > 0)
			{
				for (def tmp in line.creatives)
					println('creative.id = ' + tmp.ID)

			}

			for (def obj in result)
			{
				switch (obj.target)
				{
					case "Inventory Source":
						CheckTargetingInventorySource(obj, beforeupload, afterupload)
						break

					case "Creative":
						CheckTargetingCreative(line, obj, beforeupload, afterupload)
						break
				}
			}

			// Verification inventory source public summary
			if (test.equals("Inventory Source") == true)
			{
				String message = ''
				println('this.line.size() = ' + this.line.size())
				for (this.it_exchange = 0; this.it_exchange < this.line.size(); this.it_exchange++)
				{
					println('count exchange = ' + this.line[this.it_exchange].count_exchange[0] + ' et count subexchange = ' + this.line[this.it_exchange].count_exchange[1])
					message = this.line[this.it_exchange].count_exchange[0] + ' Exchanges and ' + this.line[this.it_exchange].count_exchange[1] + ' Subexchanges are selected'
					println('this.line[it_exchange].name = ' + this.line[this.it_exchange].name)
					println('inventorysourcesummary = ' + this.line[this.it_exchange].InventorySourceSummary)
					println('this.it_exchange = ' + this.it_exchange)
					if (message != this.line[this.it_exchange].InventorySourceSummary)
						KeywordUtil.markFailedAndStop(message + ' is different than ' + this.line[this.it_exchange].InventorySourceSummary)
				}
				KeywordUtil.markPassed(message + ' is correct')
			}
		}

		if (test.equals("Creative") == true)
		{
			String[] format = []
			for (String str in afterupload)
			{
				format = str.split(',')
				total_crea_count_final += format.size()
			}
			for (def line in this.line)
				total_crea_count_initial += line.creatives.size()

			println('total crea initial = ' + total_crea_count_initial)
			println('objet exclus = ' + result.size())
			println('total crea final = '+ total_crea_count_final)
			if (total_crea_count_initial - result.size() != total_crea_count_final)
				KeywordUtil.markFailedAndStop('Number of creative after upload does not match the number of creative before upload minus the excluded creative')

		}
	}
}