package com.reusableComponents

import javax.wsdl.WSDLElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil

import internal.GlobalVariable as GlobalVariable

public class CheckingProfile
{
	/**
	 * 
	 * Checking profile class
	 * 
	 * @author esaad
	 * 
	 * 
	 */

	private static List<String> mandatoryVariables = ['env', 'advertiserName', 'googleID', 'googlePassword', 'refreshAdvertiser', 'GoogleAuthentificatorKey', 'checkHref']


	private static List<String> includeExcludeTargetingIds = ['Audience Advertiser', '1P', '3P', 'In-Market', 'Affinity', 'Category', 'Conversion Pixel', 'Custom Affinity', 'France', 'Spain', 'Italy', 'UK', 'US', 'Others', 'Exchanges', 'Inventory Advertiser', 'Network', 'Keywords', '']


	private static List<String> targeting = ['Audience', 'Affinity & In Market', 'Category', 'Conversion Pixel', 'Custom Affinity', 'Geography', 'Inventory Source', 'Keyword', '']

	private static List<String> type = ['Include', 'Exclude', '']

	private void checkNoSpaceForMandotaryVariable(List mandatory)
	{
		/**
		 * Check if no space is present in the mandatory string global variables
		 *
		 * @param mandatory
		 * 		List of global variables
		 *
		 */

		for (var in mandatory)
		{
			try
			{
				//Si la variable globale est un String
				if (GlobalVariable."$var".getClass().getSimpleName() == "String")
				{
					//Si la valeur de l'indice i contient un espace
					if (GlobalVariable."$var".contains(" ") && var != "advertiserName")
					{
						KeywordUtil.markErrorAndStop('A space is present in '+GlobalVariable."$var"+' includes in global variable '+var+'. It should be deleted before running the test')
					}
				}
			} catch(MissingPropertyException e)
			{
				KeywordUtil.markErrorAndStop("A Global Variable is missing: "+ e)
			}
		}
	}

	private void checkNoSpace(List optional)
	{
		/**
		 * Check if no space is present for a String number includes in a Map or a list.
		 * An exception is raised if a global variable is missing
		 * We check if values in type, targetting, includeTargetingIds and excludeTargetingIds are correct
		 * 
		 * @param optional
		 * 		List of global variables
		 * 
		 */

		for (var in optional)
		{
			try
			{
				//Si la variable globale est une liste
				if (GlobalVariable."$var".getClass().getSimpleName() == "ArrayList")
				{
					for (int i=0; i < GlobalVariable."$var".size(); i++)
					{
						if (var == "type")
						{
							assert type.contains(GlobalVariable."$var"[i]) : "Type "+GlobalVariable."$var"[i]+" is not correct"
						}
						//Si la valeur de l'indice i contient un espace
						if (GlobalVariable."$var"[i].contains(" "))
						{
							//Si la valeur de l'indice i contient un espace et est un nombre
							if (GlobalVariable."$var"[i].trim().matches("\\p{Digit}+"))
							{
								KeywordUtil.markErrorAndStop('A space is present in '+GlobalVariable."$var"[i]+' includes in global variable '+var+'. It should be deleted before running the test')
							}
						}
					}
					//Si la variable globale est une Map
				} else if(GlobalVariable."$var".getClass().getSimpleName() == "LinkedHashMap")
				{
					Map a = GlobalVariable."$var"
					for (Map.Entry<String, String> entry : a.entrySet())
					{
						if (var == "includeTargetingIds" || var == "excludeTargetingIds")
						{
							assert includeExcludeTargetingIds.contains(entry.getKey()) : "Targeting "+entry.getKey()+" into "+var+" is not correct"
						}
						//Si la valeur contient un espace
						if (entry.getValue().contains(" "))
						{
							//Si la valeur contient un espace et est un nombre
							if (entry.getValue().trim().matches("\\p{Digit}+"))
							{
								KeywordUtil.markErrorAndStop('A space is present in '+entry.getValue()+' includes in global variable '+var+'. It should be deleted before running the test')

							}
						}
					}
					//Si la variable globale est un String
				} else if (GlobalVariable."$var".getClass().getSimpleName() == "String")
				{
					if (var == "targetting")
					{
						assert targeting.contains(GlobalVariable."$var") : "Targeting "+GlobalVariable."$var"+" into "+var+" is not correct"
					}
					//Si la valeur contient un espace
					if (GlobalVariable."$var".contains(" "))
					{
						//Si la valeur contient un espace et est un nombre
						if (GlobalVariable."$var".trim().matches("\\p{Digit}+"))
						{
							KeywordUtil.markErrorAndStop('A space is present in '+GlobalVariable."$var"+' includes in global variable '+var+'. It should be deleted before running the test')
						}
					}
				}
			}
			catch(MissingPropertyException e)
			{
				KeywordUtil.markErrorAndStop("A Global Variable is missing or incorrect: "+ e)
			}
		}
	}

	@Keyword
	public void checkEmptyList(def profilevar)
	{
		ArrayList<String> template = profilevar
		assert template.size() > 0 : "There is no line template name in the profile"
		for (int i = 0; i < template.size(); i++)
			assert template[i] != '' : "A line template name is not defined"
	}

	@Keyword
	public void checkProfile(String testCase)
	{
		/**
		 * Check in a chosen test if right variables are present and without spaces
		 * 
		 * @param testCase
		 * 		Testcase reference
		 * 
		 */

		KeywordUtil.logInfo("Checking variables in profile...")
		checkNoSpaceForMandotaryVariable(mandatoryVariables)

		switch(testCase)
		{
			case "QAP-165":
				List a = ["items", "includeTargetingIds", "excludeTargetingIds", "targetting", "type"]
				checkNoSpace(a)
				break
			case "QAP-166":
				List a = ["items"]
				checkNoSpace(a)
				break
			case "QAP-167":
				List a = ["items", "type"]
				checkNoSpace(a)
				break
			case "QAP-195":
				List a = ["items", "includeTargetingIds", "excludeTargetingIds", "targetting", "type"]
				checkNoSpace(a)
				break
			case "QAP-201":
				List a = ["items", "itemNames"]
				checkNoSpace(a)
				break
			case "QAP-203":
				List a = ["items", "itemNames", "type"]
				checkNoSpace(a)
				break
			case "QAP-204":
				List a = ["items"]
				checkNoSpace(a)
				break
			case "QAP-219":
				List a = ["items"]
				checkNoSpace(a)
				break
			case "QAP-220":
				List a = ["items", "lineTemplateNames", "ioTemplateNames", "itemNames"]
				checkNoSpace(a)
				break
			case "QAP-221":
				List a = ["items"]
				checkNoSpace(a)
				break
			case "QAP-222":
				List a = ["items", "includeTargetingIds", "excludeTargetingIds", "targetting", "type"]
				checkNoSpace(a)
				break
			case "QAP-223":
				List a = ["items"]
				checkNoSpace(a)
				break
			case "QAP-224":
				List a = ["items", "Performance"]
				checkNoSpace(a)
				break
			case "QAP-225":
				List a = ["items", "Performance"]
				checkNoSpace(a)
				break
			case "QAP-242":
				List a = ["items", "templateFilters"]
				checkNoSpace(a)
				break
			case "QAP-243":
				List a = ["items", "itemNames"]
				checkNoSpace(a)
				break
			case "QAP-245":
				List a = ["type"]
				checkNoSpace(a)
				break
			case "QAP-270":
				List a = ["items"]
				checkNoSpace(a)
				break
			case "QAP-334":
				List a = ["items"]
				checkNoSpace(a)
				break
			case "QAP-271":
				List a = ["items", "lineTemplateNames"]
				checkNoSpace(a)
				checkEmptyList(GlobalVariable.lineTemplateNames)
				break
		}
	}
}