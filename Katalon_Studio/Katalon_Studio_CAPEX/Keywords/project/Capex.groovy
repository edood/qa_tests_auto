package project

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.text.SimpleDateFormat
import java.time.LocalDate

import org.openqa.selenium.Keys

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testdata.CSVData
import com.kms.katalon.core.testdata.reader.CSVSeparator
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.Common

import internal.GlobalVariable

public class Capex extends Common
{
	private String bucket = "https://console.cloud.google.com/storage/browser/bkt-jira2ff-"+GlobalVariable.environment+"-eu-reports/issues;tab=objects?project=tr-pdct-jira2ff-dev&pageState=(%22StorageObjectListTable%22:(%22f%22:%22%255B%255D%22,%22s%22:%5B(%22i%22:%22objectListDisplayFields%2FtimeCreated%22,%22s%22:%220%22),(%22i%22:%22displayName%22,%22s%22:%220%22)%5D))&prefix=&forceOnObjectsSortingFiltering=true"
	private static List<String> csvHeader = ['account_id', 'issue_id', 'date', 'issue_key', 'issue_type', 'project', 'project_category', 'epic_link', 'user_email', 'timespent_seconds', 'custom_field_id_11626', 'finance_Label', 'custom_field_id_11636', 'custom_field_id_11637', 'working_hours', 'country']

	Map financelbl = ['Feature release':"Dev Work - Build",
		'Pair programming / Peer review':"Dev Work - Build",
		'Agile ceremonies for development':"Dev Work - Build",
		Development:"Dev Work - Build",
		'Other meetings':"Project Planning",
		'Agile ceremonies for project management':"Project Management",
		'Product post launch actions':"Post-Launch Support",
		'Run (improve)':"Dev Work - Build",
		'Run (fix)':"Post-Launch Support",
		'Product research':"Market Research",
		'Product mgt.':"Dev Work - Build",
		Research:"Product / Technical Research",
		'Client project Build':"Dev Work - Build"]

	class Worklog
	{
		String name
		String time
		String label
		String[] date = ['', '', '', '', '', 'WE', 'WE']
	}

	@Keyword
	public String Start()
	{
		/**
		 * Log in to Capex for specified environment
		 *
		 */
		assert GlobalVariable.environment == "dev" || GlobalVariable.environment == "uat" || GlobalVariable.environment == "prod" : "Environment set in profile is incorrect: "+GlobalVariable.environment
		String baseUrl = 'https://console.cloud.google.com/cloudscheduler?project=tr-pdct-jira2ff-'+GlobalVariable.environment+'&folder=&organizationId='
		WebUI.openBrowser(baseUrl)
		WebUI.maximizeWindow()
		return baseUrl
	}

	@Keyword
	def login(String id, String pwd)
	{
		/**
		 * Log in to Capex
		 * @param id
		 * The id of the user
		 * @param pwd
		 * The encrypted password of the user
		 */
		driver = DriverFactory.getWebDriver()
		String parentWindow = driver.getWindowHandle()
		Set<String> handles = driver.getWindowHandles()
		googleSignIn(id, pwd)
		if (WebUI.waitForElementPresent(findTestObject('Object Repository/1. Login Google/2FA header'), 2))
			Googleauthentificator()
		else
			return

		WebUI.waitForPageLoad(30)
		assert WebUI.waitForElementPresent(findTestObject('2. Run command/Filter input'), 60)
	}

	@Keyword
	def searchJob(name)
	{
		/**
		 * Search job by name
		 * 
		 * @param name
		 * 		Job name
		 */

		assert WebUI.waitForElementPresent(findTestObject('2. Run command/Filter input'), 10)
		WebUI.setText(findTestObject('2. Run command/Filter input'), name)
		WebUI.sendKeys(findTestObject('2. Run command/Filter input'), Keys.chord(Keys.ENTER))
		WebUI.delay(2)
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/2. Run command/job list'), 2): "This job does not exit: "+name
	}

	@Keyword
	def runCommand()
	{
		/**
		 * Run a job
		 * 
		 * @param name
		 * 		Name of the job
		 * 
		 */

		WebUI.executeJavaScript("document.querySelector('"+getOnlySelector(findTestObject('2. Run command/Run now button'))+"').click()", null)
		WebUI.delay(2)
	}

	def streamLog(String severity)
	{
		/**
		 * 
		 * @param severity
		 * 		severity chosen: severity=(ERROR) or severity=(DEBUG)
		 */

		String href = WebUI.executeJavaScript("return document.querySelector('"+getOnlySelector(findTestObject('Object Repository/2. Run command/View button'))+"').getAttribute('href')", null)
		WebUI.navigateToUrl("https://console.cloud.google.com"+href)
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementNotPresent(findTestObject("Object Repository/3. Log explorer/Query progress bar"), 20)
		assert WebUI.waitForElementPresent(findTestObject("Object Repository/3. Log explorer/Edit query button"), 15)
		WebUI.click(findTestObject("Object Repository/3. Log explorer/Edit query button"))
		assert WebUI.waitForElementPresent(findTestObject("Object Repository/3. Log explorer/Severity button"), 15)
		assert WebUI.clearText(findTestObject("Object Repository/3. Log explorer/Editor query")): "query not cleared"

		WebUI.setText(findTestObject("Object Repository/3. Log explorer/Editor query"), severity)
		WebUI.sendKeys(findTestObject('Object Repository/3. Log explorer/Editor query'), Keys.chord(Keys.ENTER))

		WebUI.click(findTestObject('Object Repository/3. Log explorer/Run query button'))
		WebUI.click(findTestObject('Object Repository/3. Log explorer/Stream log button'))
		WebUI.delay(2)
	}
	@Keyword
	def configureJobTarget(boolean earseData)
	{
		/**
		 * Configure erase data at true or false
		 * 
		 * @param earseData
		 * 		true if we want to erase old data, false otherwise
		 */
		String urlConfiguration = "https://gcr-jira2ff-dev-europe-west1-laravel-fkqpvfmp3a-ew.a.run.app/generate-report/worklogs?start_date=last_week&end_date=last_week&--erase_data="+earseData

		WebUI.click(findTestObject('Object Repository/2. Run command/Job select'))
		WebUI.click(findTestObject('Object Repository/5. Edition/Edition button'))
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/5. Edition/Job target tab'),30)
		WebUI.click(findTestObject('Object Repository/5. Edition/Job target tab'))
		assert WebUI.waitForElementPresent(findTestObject('Object Repository/5. Edition/Url input'),10)
		WebUI.clearText(findTestObject('Object Repository/5. Edition/Url input'))
		WebUI.setText(findTestObject('Object Repository/5. Edition/Url input'), urlConfiguration)
		WebUI.delay(1)
		WebUI.click(findTestObject('Object Repository/5. Edition/Update button'))
		assert WebUI.waitForElementPresent(findTestObject('2. Run command/Filter input'), 15)
	}

	def checklogError()
	{
		/**
		 * Check log error messages if job status is failed
		 * 
		 * @return errorMessage
		 * 		Error message
		 * 
		 */
		String errorMessage = ""

		streamLog("severity=(ERROR)")
		assert WebUI.waitForElementNotPresent(findTestObject("Object Repository/3. Log explorer/Query progress bar"), 20)
		errorMessage = 	WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/3. Log explorer/log window'))+"').item(0).textContent", null)

		return errorMessage
	}

	@Keyword
	def checkStatus()
	{
		/**
		 * Check if job status is success or failed
		 * 
		 * @return dateTime
		 * 		tab contains date and time of the job
		 */
		Date currentDate = new Date()
		SimpleDateFormat hour = new SimpleDateFormat("HH:mm")
		int i = 0
		String[] dateTimeTab = new String[2]

		Date d = hour.parse(hour.format(currentDate))
		Calendar cal = Calendar.getInstance()
		cal.setTime(d)
		cal.add(Calendar.MINUTE, -1)
		String startDate = hour.format(cal.getTime())
		cal.add(Calendar.MINUTE, 2)
		String endDate =  hour.format(cal.getTime())
		String dateTime = WebUI.getText(findTestObject("Object Repository/2. Run command/Date of last run"))
		if (dateTime.trim().contains("à"))
			dateTimeTab = dateTime.trim().split("à")
		else if (dateTime.trim().contains(","))
			dateTimeTab = dateTime.trim().split(",")
		KeywordUtil.logInfo("Target time should be between "+startDate+" and "+endDate+"")

		while(!isHourInInterval(dateTimeTab[1].substring(0, dateTimeTab[1].length()-3).trim(), startDate, endDate))
		{
			if (i==100)
				KeywordUtil.markErrorAndStop("Timeout is reached. The run was not started correctly. Please check the last run time")

			if (dateTime.trim().contains("à"))
				dateTimeTab = WebUI.getText(findTestObject("Object Repository/2. Run command/Date of last run")).trim().split("à")
			else if (dateTime.trim().contains(","))
				dateTimeTab = WebUI.getText(findTestObject("Object Repository/2. Run command/Date of last run")).trim().split(",")

			WebUI.click(findTestObject('Object Repository/2. Run command/Refresh button'))
			i++
			WebUI.delay(2)
		}
		KeywordUtil.logInfo("Target time: "+dateTimeTab[1].substring(0, dateTimeTab[1].length()-3).trim())

		String result = WebUI.getText(findTestObject("Object Repository/2. Run command/Result of last run")).trim()
		if (result == "Success" || result == "Opération réussie")
		{
			KeywordUtil.logInfo("The run passed with success")
		}
		else
		{
			String errorMessage = checklogError()
			KeywordUtil.markErrorAndStop("The command run is failed: "+errorMessage)
		}
		return dateTimeTab
	}

	@Keyword
	def parseCsvName(String[] dateTimeTab)
	{
		/**
		 * Parse date time in bucket and download the correct csv 
		 * 
		 * @param dateTimeTab
		 * 		tab contains date and time of the job has been ran
		 * @return csvName
		 * 		name of the correct csv downloaded
		 */
		openNewTab()
		WebUI.navigateToUrl(bucket)
		Date currentDate = new Date()

		String localDate = LocalDate.now()
		String[] createdTimeTab = new String[2]
		Calendar cal = Calendar.getInstance()
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm")

		Date date = sdf.parse(dateTimeTab[1])
		cal.setTime(date)
		cal.add(Calendar.MINUTE, 3)
		String endDate =  sdf.format(cal.getTime())

		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Bucket/Csv list line'), 10)
		assert WebUI.waitForElementVisible(findTestObject('Object Repository/4. Bucket/Filter input'), 10)
		WebUI.setText(findTestObject('Object Repository/4. Bucket/Filter input'), localDate)
		WebUI.sendKeys(findTestObject('Object Repository/4. Bucket/Filter input'), Keys.chord(Keys.ENTER))
		WebUI.delay(2)
		int listLength = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/4. Bucket/Csv list line'))+"').length", null)
		WebUI.sendKeys(findTestObject('Object Repository/4. Bucket/Filter input'), Keys.chord(Keys.ESCAPE))

		for(int i=0; i<listLength; i++)
		{
			String createdTime = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/4. Bucket/Csv list line'))+"').item("+i+").textContent", null)
			if (createdTime.trim().contains("à"))
				createdTimeTab = createdTime.trim().split("à")
			else if (createdTime.trim().contains(","))
				createdTimeTab = createdTime.trim().split(",")

			if (isHourInInterval(createdTimeTab[1], dateTimeTab[1], endDate))
			{
				WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/4. Bucket/Download csv button'))+"').item("+i+").click()", null)
				String csvName = WebUI.executeJavaScript("return document.querySelectorAll('"+getOnlySelector(findTestObject('Object Repository/4. Bucket/Csv name'))+"').item("+i+").textContent.trim()", null)
				KeywordUtil.logInfo("CSV is downloaded: "+csvName)
				return csvName
			}
		}
	}

	@Keyword
	def pathOfCsv(String csvName)
	{
		/**
		 * Verify if the CSV from the bucket was downloaded correctly
		 * 
		 * @param csvName
		 * 		csv name downloaded from bucket
		 * 
		 * @return csvFilePath
		 * 		csv file full path
		 */
		String fullCsvName = "issues_"+csvName

		String sdfPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator
		File file = new File(sdfPath + fullCsvName)

		Thread.sleep(500)
		int attempts = 10
		while(file.exists() == false && attempts > 0)
		{
			WebUI.delay(5)
			attempts--
		}
		assert file.exists() == true : "CSV not downloaded"

		String csvFullPath = file.path
		KeywordUtil.markPassed("CSV downloaded. Path: "+csvFullPath)

		return csvFullPath
	}

	@Keyword
	def checkCsvHeader(String csvFullPath, List columnToCompare = csvHeader)
	{
		/**
		 * Check csv Header is correct
		 *
		 * @param csvFullPath
		 * 		Path of csv file
		 * 
		 * @param columnToCompare
		 * 		List of column to compare with the csv
		 */
		KeywordUtil.logInfo("Checking CSV file ...")
		ArrayList<String> columntab = new ArrayList<String>()
		CSVData csvData = new CSVData(csvFullPath, true, CSVSeparator.COMMA)

		columntab = csvData.columnNames
		assert columntab.equals(columnToCompare): "Header is not correct into the csv. Expected: "+columnToCompare+", Actual: "+columntab
		KeywordUtil.logInfo("Csv header is correct: "+columntab)
	}

	ArrayList<Worklog>	fillDate(String date, Worklog wrow, ArrayList<Worklog> user)
	{
		int indexweek = 5
		boolean pushnewlog = false

		if (Date.parse("yyyy-MM-dd", date)[Calendar.DAY_OF_WEEK] == Calendar.MONDAY)
			indexweek = 0
		else if (Date.parse("yyyy-MM-dd", date)[Calendar.DAY_OF_WEEK] == Calendar.TUESDAY)
			indexweek = 1
		else if (Date.parse("yyyy-MM-dd", date)[Calendar.DAY_OF_WEEK] == Calendar.WEDNESDAY)
			indexweek = 2
		else if (Date.parse("yyyy-MM-dd", date)[Calendar.DAY_OF_WEEK] == Calendar.THURSDAY)
			indexweek = 3
		else if (Date.parse("yyyy-MM-dd", date)[Calendar.DAY_OF_WEEK] == Calendar.FRIDAY)
			indexweek = 4
		else if (Date.parse("yyyy-MM-dd", date)[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY)
			indexweek = 5
		else if (Date.parse("yyyy-MM-dd", date)[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY)
			indexweek = 6
		
		boolean dateadded = false
		for (def tmp in user)
		{
			if (tmp.name == wrow.name && tmp.label == wrow.label && tmp.time == wrow.time)
			{
				if (tmp.date[indexweek] == '')
				{
					tmp.date[indexweek] = date
					dateadded = true
					break
				}
			}
		}
		if (dateadded == false)
		{
			wrow.date[indexweek] = date
			user.add(wrow)
		}
		return user
	}

	def checkCsvContent(String csvFullPath)
	{
		/**
		 * Check the Csv legal hours by user email and user country
		 * Check the custom field id 11626 and the matching label by user
		 * 
		 * @param csvFullPath	Full path of the CSV file to check
		 */
		CSVData csvData = new CSVData(csvFullPath, true, CSVSeparator.COMMA)
		ArrayList<String> columntab = csvData.columnNames
		int workingHoursIndex = columntab.indexOf("working_hours")
		int timespentSecondsIndex = columntab.indexOf("timespent_seconds")
		int userEmailIndex = columntab.indexOf("user_email")

		ArrayList<ArrayList<String>> csvDataStringArray = new ArrayList<ArrayList<String>>()
		csvDataStringArray = csvData.getData()

		int j = 0
		boolean userFound = false
		int rowNumbers = csvData.getRowNumbers()
		int numberOfUsers = GlobalVariable.worklog.size()
		ArrayList<String> userEmails = GlobalVariable.worklog.keySet()
		ArrayList<String> userInfos = GlobalVariable.worklog.values()
		ArrayList<String> currentLine = new ArrayList<String>()
		String currentUser = ""
		ArrayList<String> missingUsers = new ArrayList<String>()
		String failedUsersMessage = ""
		float currentUserWorkingHours = 0
		float legalHours = 0
		ArrayList<Worklog> user = new ArrayList<Worklog>()
		int countrow = 0
		for(int i = 0; i < rowNumbers; i++)
		{
			if(j == numberOfUsers)
				break

			currentLine = csvDataStringArray[i]
			currentUser = currentLine[userEmailIndex]

			if(currentUser == userEmails[j])
			{
				countrow++
				Worklog wrow = new Worklog()
				wrow.name = currentUser
				String currentCustomField = currentLine[columntab.indexOf("custom_field_id_11626")]
				wrow.label = currentCustomField
				wrow.time = currentLine[timespentSecondsIndex]
				user = fillDate(currentLine[columntab.indexOf("date")], wrow, user)
				currentUserWorkingHours += Float.valueOf(currentLine[timespentSecondsIndex])
				userFound = true
				if (currentCustomField == '')
					failedUsersMessage += "Row " + (i + 2) + ": The custom_field_id_11626 is empty for the user " + currentUser + "\n"
				else
				{
					String currentFinanceLabel = currentLine[columntab.indexOf("finance_Label")]
					boolean keyfound = false
					financelbl.each
					{
						if (it.key == currentCustomField)
						{
							keyfound = true
							if (currentFinanceLabel != it.value)
								failedUsersMessage += "Row " + (i + 2) + ": User " + currentUser + " financelabel is not correct. displayed: " + currentCustomField + ":" + currentFinanceLabel + " but expected was: " + it.key + ":" + it.value + "\n"
						}
					}
					if (keyfound == false)
						failedUsersMessage += "Row " + (i + 2) + ": User " + currentUser + " custom_field_d_11626 is not correct. displayed: " + currentCustomField + "\n"
				}
			}
			else if (userFound == true)
			{
				legalHours = (Float.valueOf(userInfos[j])) * 60 * 60
				if((legalHours-30.0f) < currentUserWorkingHours && currentUserWorkingHours < (legalHours+30.0f))
				{
					KeywordUtil.logInfo("Current user ("+userEmails[j]+") total working hours match its legal hours")
				} else
				{
					failedUsersMessage += "Total of timespent_seconds column is not correct for user " + userEmails[j] + ". CSV : " + currentUserWorkingHours + " seconds ; Expected between " + (legalHours-30.0f) + " and " + (legalHours+30.0f) + " seconds\n"
				}
				j++
				i = -1
				currentUserWorkingHours = 0
				userFound = false
			} else if (i == rowNumbers - 1 && j != userEmails.size())
			{
				missingUsers.add(userEmails[j])
				j++
				i = -1
				currentUserWorkingHours = 0
				userFound = false
			}
		}

		if (countrow % 5 != 0 && user.size() * 5 != countrow)
			failedUsersMessage += 'Split hours is not correct\n'

		int[] weekend = [5, 6]
		for (def tmp in user)
		{
			for (int day = 1; day < 8; day++)
			{
				if (weekend.contains(day - 1) == false && tmp.date[day - 1] == '')
					failedUsersMessage += 'Split hours is not correct for ' + tmp.name + ', ' + tmp.time + ', ' + tmp.label + ', ' + tmp.date + '\n'
			}
		}

		String missingUsersMessage = ""
		if(!missingUsers.isEmpty()) missingUsersMessage = "\n\nSome users are missing from the CSV : " + missingUsers + "\n\n"
		if(!missingUsers.isEmpty() || failedUsersMessage != "") KeywordUtil.markFailedAndStop(missingUsersMessage + failedUsersMessage)

		}
}
