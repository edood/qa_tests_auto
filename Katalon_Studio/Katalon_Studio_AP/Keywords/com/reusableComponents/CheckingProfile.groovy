package com.reusableComponents

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
	private static List<String> mandatoryVariables = ['env', 'partnerName', 'GoogleAuthentificatorKey', 'googleID', 'googlePassword', 'checkHref']

	private static List<String> ruleCreate = ['Currency', 'Budget Value', 'Budget Type', 'Apply on', 'Warning Email', 'Advertisers', 'Campaigns', 'Insertion Orders', 'Threshold', 'Start Date', 'End Date', 'Rule Name', 'Recipients', 'Status', 'Reactivate IO', 'Warning Email', 'Condition', 'PauseOthersCurrencies', 'CheckEmail', 'CheckRuleList']

	private static List<String> ruleEdit = ['Currency', 'Budget Value', 'Budget Type', 'Advertisers', 'Campaigns', 'Insertion Orders', 'Threshold', 'Start Date', 'End Date', 'Rule Name', 'Recipients', 'Warning Email', 'Condition', 'Status', 'Keep Old Advertisers', 'Keep Old Campaigns', 'Keep Old Insertion Orders', 'KeepOldCondition', 'Keep Old Recipients', 'PauseOthersCurrencies', 'Reactivate IO']

	private static List<String> ruleDuplicate = ['Currency', 'Budget Value', 'Budget Type', 'Advertisers', 'Campaigns', 'Insertion Orders', 'Threshold', 'Start Date', 'End Date', 'Rule Name', 'Recipients', 'Warning Email', 'Status', 'Keep Old Advertisers', 'Keep Old Campaigns', 'Keep Old Insertion Orders', 'Keep Old Recipients', 'Reactivate IO']

	private static List<String> CPMapDV = ['Advertiser', 'cpName', 'cpStatus', 'cpOverallCampaignGoal', 'cpKpi', 'cpCreaType', 'cpPlannedStartDate', 'cpPlannedEndDate', 'cpPlannedSpend', 'archiveCP', 'createOnlyCP']
	private static List<String> OIMapDV = ['Campaign', 'ioName', 'ioStatus', 'ioBudgetType', 'ioBudgetValue', 'ioBudgetStartDate', 'ioBudgetEndDate', 'ioPacing', 'ioGoal', 'archiveOI']

	private void checkPartner()
	{
		/**
		 * Check if Partner is TP - JellyTrain - JEL - DBM - UK and not TradeLab1 when rule is applied on Partner
		 * 
		 */
		Map create = GlobalVariable.ruleCreate
		assert GlobalVariable.partnerName == "TP - JellyTrain - JEL - DBM - UK" || GlobalVariable.partnerName == "1800736" : "Rule is applied on Partner. Please change your partner to TP - JellyTrain - JEL - DBM - UK on the profile "
		assert create.containsKey("Condition"): "Condition variable should be present for a Partner"
	}

	private void checkPartnerSameCondition(Map currentMap)
	{
		/**
		 * Check if 2 conditions with same budget type and same currency is set in the profile
		 * 
		 * @param currentMap		Current map to check conditions from
		 */

		ArrayList<String> conditions = new ArrayList<String>()
		ArrayList<String> oneCondition = new ArrayList<String>()
		ArrayList<String> budgetTypeList = new ArrayList<String>()
		ArrayList<String> currencyTypeList = new ArrayList<String>()

		conditions = currentMap["Condition"].split(";")

		assert currentMap["Condition"] != "" : "One of the maps Condition key has empty value"

		for (int i =0; i<conditions.size; i++)
		{
			oneCondition = conditions[i].split(",")
			String budgetValue = oneCondition[0]
			String budgetType = oneCondition[1]
			budgetTypeList.add(budgetType)

			String currency = oneCondition[2]
			currencyTypeList.add(currency)
		}

		for (int i=0; i<budgetTypeList.size; i++)
		{
			for (int j=i; j<currencyTypeList.size;j++)
			{
				if ((budgetTypeList[i] == budgetTypeList[j+1]) && j != currencyTypeList.size)
				{
					if (currencyTypeList[i] == currencyTypeList[j+1])
					{
						KeywordUtil.markErrorAndStop("Several conditions can not have the same Budget type and currency: "+budgetTypeList[i]+" and "+currencyTypeList[i])
					}
				}
			}
		}
	}

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
					KeywordUtil.logInfo("Checking string " + var)
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
		KeywordUtil.logInfo("")
		KeywordUtil.logInfo("Checking for missing map keys or empty spaces in Number variables/keys :")
		for (var in optionalVariables)
		{
			try
			{
				//Si la variable globale est une liste
				if (GlobalVariable."$var".getClass().getSimpleName() == "ArrayList")
				{
					KeywordUtil.logInfo("Checking list " + var)
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
					KeywordUtil.logInfo("Checking map " + var)
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
					KeywordUtil.logInfo("Checking string " + var)
					//Si la valeur contient un espace
					if (GlobalVariable."$var".contains(" "))
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
			}
			catch(MissingPropertyException e)
			{
				KeywordUtil.markErrorAndStop("A Global Variable is missing or incorrect: "+ e)
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
				if (GlobalVariable.ruleCreate['Apply on'] != 'Partner')
				{
					if(currentMap == GlobalVariable.ruleCreate) assert currentMap['Currency'].matches("[A-Z]{3}") : "\n\nCurrency is not correct : " + currentMap['Currency'] + "\n\n"

					if(currentMap == GlobalVariable.ruleCreate) assert currentMap['Budget Type'] == "Currency" || currentMap['Budget Type'] == "Impressions" : "\n\nBudget Type is not 'Impressions' or 'Currency' : " + currentMap['Budget Type'] + "\n\n"

					if(GlobalVariable.ruleCreate['Budget Type'] == "Currency") assert Float.valueOf(currentMap['Budget Value']) > 0.0f : "\n\nBudget Value cannot be negative : " + currentMap['Budget Value'] + "\n\n"
					else if(GlobalVariable.ruleCreate['Budget Type'] == "Impressions") {
						assert currentMap['Budget Value'].isInteger() : "\n\nBudget Value cannot be a float number for 'Impressions' budget type : " + currentMap['Budget Value'] + "\n\n"
						assert Integer.valueOf(currentMap['Budget Value']) >= 1 : "\n\nBudget Value cannot be inferior to 1 for 'Impressions' budget type : " + currentMap['Budget Value'] + "\n\n"
					}
				}
				assert Integer.valueOf(currentMap['Threshold']) > 0 && Integer.valueOf(currentMap['Threshold']) <= 100 : "\n\nThreshold value is not contained within the accepted range [0-100] : " + currentMap['Threshold'] + "\n\n"

				// Check date contains no leading zero (optional for date parsing but mandatory for date selector)
				assert currentMap['Start Date'].matches("[1-9][0-9]?\\/[1-9][0-9]?\\/\\d\\d\\d\\d") : KeywordUtil.markFailedAndStop("\n\nStart Date format is not 'M/d/yyyy' : " + currentMap['Start Date']) + "\n\n"
				assert currentMap['End Date'].matches("[1-9][0-9]?\\/[1-9][0-9]?\\/\\d\\d\\d\\d") : KeywordUtil.markFailedAndStop("\n\nEnd Date format is not 'M/d/yyyy' : " + currentMap['End Date']) + "\n\n"
				// Check date can be parsed correctly (does not care for leading zero despite format M and d...)
				LocalDate today = LocalDate.now()
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy")
				LocalDate currentStartDate
				LocalDate currentEndDate
				try { currentStartDate = LocalDate.parse(currentMap['Start Date'], dtf) } catch(def error) { KeywordUtil.markFailedAndStop("\n\nStart Date format is not 'M/d/yyyy' : " + currentMap['Start Date'] + "\n\n") }
				try { currentEndDate = LocalDate.parse(currentMap['End Date'], dtf) } catch(def error) { KeywordUtil.markFailedAndStop("\n\nEnd Date format is not 'M/d/yyyy' : " + currentMap['End Date'] + "\n\n") }
				// Check date range is inferior to 600 days
				assert ChronoUnit.DAYS.between(currentStartDate, currentEndDate) < 600 : "\n\nDate range between start and end dates is superior to 600 days\n\n"
				// Check start date comes before end date
				assert currentStartDate < currentEndDate : "\n\nEnd date + '" + currentMap['End Date'] + "' cannot be set before Start date '" + currentMap['Start Date'] + "'\n\n"

				assert currentMap['Recipients'] == "" || currentMap['Recipients'].matches("[^@]+@[^.]+\\.[^.]+") : "\n\nRecipient is not empty or does not contain a valid email format : '" + currentMap['Recipients'] + "'\n\n"

				if(currentMap == GlobalVariable.ruleCreate)	assert currentMap['Apply on'] == "Partner" || currentMap['Apply on'] == "Advertiser" || currentMap['Apply on'] == "Insertion Order" : "\n\nruleCreate 'Apply on' key value is not correct : " + currentMap['Apply on'] + ".\nOnly the following values are accepted : 'Partner', 'Advertiser', 'Insertion Order'\n\n"

				assert currentMap['Status'] == "Active" || currentMap['Status'] == "Paused" : "\n\nStatus is not correct : " + currentMap['Status'] + ".\nOnly the following values are accepted : 'Active' or 'Paused'\n\n"
				if(GlobalVariable.ruleCreate['Apply on'] == "Partner" && currentMap != GlobalVariable.ruleDuplicate) assert currentMap['Condition'].matches("^(\\d+(.\\d+)?,(Currency|Impressions),[A-Z]{3};?)*\$") : "\n\nCondition format is not correct : " + currentMap['Condition'] + "\n\n"

				if(GlobalVariable.ruleCreate['Apply on'] == "Partner" && currentMap == GlobalVariable.ruleEdit) assert currentMap['KeepOldCondition'].matches("(^(,?C\\d+)*\$|^\$)") : "\n\nKeepOldCondition format is not correct : " + currentMap['KeepOldCondition'] + "\n\n"
			} else if(GlobalVariable."$var".getClass().getSimpleName() == "String")
			{
				KeywordUtil.logInfo("Checking string " + var + " value")
				String currentString = GlobalVariable."$var"
				if(currentString == GlobalVariable.ruleID ) try {Integer.valueOf(currentString) } catch (def error) { KeywordUtil.markFailedAndStop("\n\nruleID is not correct : " + currentString + "\n") }
				if(currentString == GlobalVariable.tabChosen) assert currentString == "Passed" || currentString == "Scheduled" || currentString == "Future" : "\n\ntabChosen is not correct : " + currentString + ".\nOnly the following values are accepted : 'Passed' or 'Scheduled' or 'Future\n\n'"
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

		KeywordUtil.logInfo("Checking for spaces in mandatory strings in profile :")
		checkNoSpaceForMandotaryVariable(mandatoryVariables)
		if (GlobalVariable.ruleCreate["Apply on"] == "Partner") {
			checkPartner()
			checkPartnerSameCondition(GlobalVariable.ruleCreate)
			checkPartnerSameCondition(GlobalVariable.ruleEdit)
		}

		List optionalVariables = []

		switch(testCase)
		{
			case "QAP-264":
				optionalVariables = ["ruleCreate", "CheckEmail", "CheckRuleList"]
				break
			case "QAP-295":
				optionalVariables = ["ruleCreate", "grafana"]
				break
			case "QAP-299":
				optionalVariables = ["ruleCreate", "ruleEdit", "grafana"]
				break
			case "QAP-328":
				optionalVariables = ["ruleCreate", "grafana"]
				break
			case "QAP-333":
				optionalVariables = ["CPMapDV", "OIMapDV"]
				break
			case "QAP-340":
				optionalVariables = ["ruleCreate", "ruleEdit", "ruleDuplicate"]
				assert GlobalVariable.ruleDuplicate['Status'] == "Active" : "Global variable 'ruleDuplicate' must have the 'Status' key set to 'Active'"
				break
			case "QAP-385":
				optionalVariables = ["tabChosen", "ruleID"]
				break
			case "QAP-329":
				optionalVariables = ["ruleCreate", "ruleEdit", "grafana"]
				assert GlobalVariable.ruleCreate['Apply on'] == "Advertiser" : "Global variable 'ruleCreate' must have the 'Apply on' key set to 'Advertiser'"
				break
			case "QAP-384":
				optionalVariables = ["ruleCreate", "ruleEdit"]
				break
			case "QAP-390":
				optionalVariables = ["ruleCreate", "ruleDuplicate", "CheckEmail"]
				break
		}

		checkNoSpace(optionalVariables)
		if(testCase != "QAP-333") checkValues(optionalVariables)
	}
}
