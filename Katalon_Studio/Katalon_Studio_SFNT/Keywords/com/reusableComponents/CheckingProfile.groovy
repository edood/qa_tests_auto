package com.reusableComponents

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil

import internal.GlobalVariable as GlobalVariable

public class CheckingProfile
{	
	private static List<String> mandatoryVariables = ['env', 'googleID', 'googlePassword', 'GoogleAuthentificatorKey']
	
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
	
	private void checkValues(List optionalVariables)
	{
		/**
		 * Check if no space is present for a String number includes in a Map or a list.
		 * An exception is raised if a global variable is missing
		 *
		 * @param optionalVariables
		 * 		List of optional global variables
		 *
		 */
		KeywordUtil.logInfo("")
		KeywordUtil.logInfo("Checking values :")
		for (var in optionalVariables)
		{
			//Si la variable globale est une Map
			if(GlobalVariable."$var".getClass().getSimpleName() == "LinkedHashMap")
			{
				KeywordUtil.logInfo("Checking map " + var + " values")
				Map currentMap = GlobalVariable."$var"
				// To do 
			} else if(GlobalVariable."$var".getClass().getSimpleName() == "String")
			{
				KeywordUtil.logInfo("Checking string " + var + " value")
				String currentString = GlobalVariable."$var"
				// To do 				
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

		List optionalVariables =  ['']
		switch(testCase)
		{
			case "QAP-":
				break
		}

		if(optionalVariables != ['']) checkNoSpace(optionalVariables)
	}
}