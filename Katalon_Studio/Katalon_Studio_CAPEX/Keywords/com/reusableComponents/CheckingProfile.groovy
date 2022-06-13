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
	private static List<String> mandatoryVariables = ['googleID', 'googlePassword', 'environment', 'GoogleAuthentificatorKey', 'jobName']

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
					if (GlobalVariable."$var".contains(" "))
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
						a.each { key, value ->
							assert key != "" && key != null : "User identifier was not set in the profile"
							assert value != "" && key != null : "User legal hour was not set in the profile"
							if (value.toString().contains(" ") || key.toString().contains(" "))
							{
								KeywordUtil.markErrorAndStop('A space is present in '+value+' or '+key+' included in the global variable '+var+'. It should be deleted before running the test')
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
							KeywordUtil.markErrorAndStop('A space is present in '+GlobalVariable."$var"+' included in the global variable '+var+'. It should be deleted before running the test')
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

		switch(testCase)
		{
			case "QAP-360":
				List a = ["eraseData" , "worklog"]
				assert GlobalVariable.worklog != [:] && GlobalVariable.worklog != ["":""] : "Global variable 'worklog' is empty, please fill it with user identifiers and legal hours"
				checkNoSpace(a)
				break
		}
	}
}