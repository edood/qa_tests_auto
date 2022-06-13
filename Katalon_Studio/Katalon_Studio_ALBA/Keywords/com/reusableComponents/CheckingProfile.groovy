package com.reusableComponents

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
	private static List<String> mandatoryVariables = ['googleID', 'googlePassword', 'env', 'checkHref', 'GoogleAuthentificatorKey']
	private static List<String> createTP = [
		'selectType',
		'Content Tracking',
		'Ecommerce Tracking',
		'Form Tracking',
		'Platform Setup',
		'User Data Tracking',
		'Video Tracking',
		'sourceClient',
		'sourcePlan'
	]
	private static List<String> tpSetup = ['Name', 'Main URL', 'Analytics destination', 'Analytics destination identifier', 'ContactName']
	private static List<String> indicatorsFilter = ['Primary', 'Module']
	private static List<String> editIndicator = ['Key', 'Name', 'Use Case', 'Module', 'Required', 'Origin', 'Explanation', 'Example', 'Expected Format']
	private static List<String> createfromAssociated = ['allModules', 'indicatorFilters', 'Content Tracking', 'Ecommerce Tracking', 'Form Tracking', 'Platform Setup', 'User Data Tracking', 'Video Tracking']
	private static List<String> createfromModule = ['allModules', 'indicatorFilters', 'Content Tracking', 'Ecommerce Tracking', 'Form Tracking', 'Platform Setup', 'User Data Tracking', 'Video Tracking']

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
					if (GlobalVariable."$var".contains(" ") && var != "partnerName")
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

	private void checkNoSpace(List optionalVariables)
	{
		/**
		 * Check if no space is present for a String number includes in a Map or a list.
		 * An exception is raised if a global variable is missing
		 * 
		 * @param optionalVariables
		 * 		List of optional global variables
		 * 
		 */

		for (var in optionalVariables)
		{
			try
			{
				//Si la variable globale est une liste
				if (GlobalVariable."$var".getClass().getSimpleName() == "ArrayList")
				{
					for (int i=0; i < GlobalVariable."$var".size(); i++)
					{
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
				}
				//Si la variable globale est une Map
				else if(GlobalVariable."$var".getClass().getSimpleName() == "LinkedHashMap")
				{
					Map a = GlobalVariable."$var"
					a.each { key, value ->
						if (key.toString() != key.toString().trim())
							KeywordUtil.markErrorAndStop("Key "+key+" into "+var+" has a space should be deleted")
							
						assert this."$var".contains(key) : "Key "+key+" into "+var+" is not correct"
						if (value.toString().contains(" "))
						{
							//Si la valeur contient un espace et est un nombre
							if (value.toString().trim().matches("\\p{Digit}+"))
							{
								KeywordUtil.markErrorAndStop('A space is present in '+value+' includes in global variable '+var+'. It should be deleted before running the test')
							}
						}
					}
					//Si la variable globale est un String
				} else if (GlobalVariable."$var".getClass().getSimpleName() == "String")
				{
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
				KeywordUtil.logInfo("" + e)
				KeywordUtil.markErrorAndStop("A Global Variable is missing or incorrect: "+ var)
			}
		}
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
		if (GlobalVariable.isSelectClient == false && GlobalVariable.createTP["selectType"] == "Plan")
		KeywordUtil.markErrorAndStop("Cannot select source from existing plan for a client newly created. Please modify isSelectClient")

		List optionalVariables =  ['']
		switch(testCase)
		{
			case "QAP-318":
				optionalVariables = ['newClientName']
				break
			case "QAP-356":
				optionalVariables = ['newClientName', 'tpSetup', 'createTP']
				break
			case "QAP-357":
				optionalVariables = ['clientNameToSelect', 'trackingPlanNameToSelect', 'isSelectTP', 'tpSetup', 'createTP']
				break
			case "QAP-387":
				optionalVariables = ['isSelectClient', 'clientNameToSelect', 'trackingPlanNameToSelect', 'isSelectTP', 'tpSetup', 'createTP', 'newClientName']
				break
			case "QAP-399":
				optionalVariables = ['editIndicator', 'indicatorsFilter', 'clientNameToSelect', 'trackingPlanNameToSelect', 'isSelectTP', 'tpSetup', 'createTP']
				assert GlobalVariable.isSelectTP || GlobalVariable.createTP['selectType'] != "" : "\n\nQAP-399 cannot be run with empty tracking plan\n\n"
				assert (GlobalVariable.indicatorsFilter["Primary"] == "Variable" && GlobalVariable.searchColumn != "Use Case") || (GlobalVariable.indicatorsFilter["Primary"] == "Event" && GlobalVariable.searchColumn != "Explanation") : "\n\nWrong searchColumn value : " + GlobalVariable.searchColumn +".\nPossible Values :\n\tIf Primary = 'Event' : 'Key', 'Name' or 'Use Case'\n\tIf Primary = 'Variable' : 'Key', 'Name' or 'Explanation'\n\n"
				assert GlobalVariable.editIndicator[GlobalVariable.searchColumn] != "" && GlobalVariable.editIndicator[GlobalVariable.searchColumn] != null : "\n\nsearchColumn value must target edited field.\nsearchColumn = '" + GlobalVariable.searchColumn + "'\neditIndicator['"+GlobalVariable.searchColumn+"'] = '" + GlobalVariable.editIndicator[GlobalVariable.searchColumn] + "'\n\n"
				break
			case "QAP-402":
				optionalVariables = ['isSelectClient', 'clientNameToSelect', 'trackingPlanNameToSelect', 'isSelectTP', 'tpSetup', 'createTP', 'newClientName', 'createfromAssociated', 'createfromModule']
				assert ((Map)GlobalVariable.createfromModule).size() > 1 || (((Map)GlobalVariable.createfromModule).size() == 1 && GlobalVariable.createfromModule["allModules"] == true) : "Setting is not correct in the profile map createfromModule"
				if (GlobalVariable.isSelectTP == false && GlobalVariable.createTP['selectType'] == '')
				assert GlobalVariable.indicatorsFilter['Module'] == 'All' || GlobalVariable.indicatorsFilter['Module'] == '' : "The indicator filter module " + GlobalVariable.indicatorsFilter['Module'] + " is not possible for a tracking plan empty"
				if (GlobalVariable.createfromCustom['IndicatorFilters'] == 'Event' && GlobalVariable.createfromCustom['Action'] != '')
				assert GlobalVariable.createfromCustom['Association'].toString().contains(GlobalVariable.createfromCustom['Action'].toString()) : "The action is not included in the profil association"
				if (GlobalVariable.createfromCustom['IndicatorFilters'] == 'Event' && GlobalVariable.createfromCustom['Label'] != '')
				assert GlobalVariable.createfromCustom['Association'].toString().contains(GlobalVariable.createfromCustom['Label'].toString()) : "The label is not included in the profil association"
				break
			case "QAP-396":
				optionalVariables = ['clientNameToSelect', 'trackingPlanNameToSelect', 'isSelectTP', 'tpSetup', 'createTP', 'isSelectClient', 'newClientName']
				break
			case "QAP-401":
				optionalVariables = ['clientNameToSelect', 'trackingPlanNameToSelect', 'isSelectTP', 'tpSetup', 'createTP', 'isSelectClient', 'newClientName', 'createAssociation', 'indicatorsFilter', 'editIndicator']
				break
		}

		if(optionalVariables != ['']) checkNoSpace(optionalVariables)
	}
}
