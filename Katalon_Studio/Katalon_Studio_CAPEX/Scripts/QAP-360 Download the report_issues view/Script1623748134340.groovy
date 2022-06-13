import com.kms.katalon.core.testdata.CSVData
import com.kms.katalon.core.testdata.reader.CSVSeparator
import com.kms.katalon.core.util.KeywordUtil

import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'("QAP-360")
CustomKeywords.'project.Capex.Start'()
CustomKeywords.'project.Capex.login'(GlobalVariable.googleID, GlobalVariable.googlePassword)
CustomKeywords.'project.Capex.searchJob'(GlobalVariable.jobName)
CustomKeywords.'project.Capex.configureJobTarget'(GlobalVariable.eraseData)
CustomKeywords.'project.Capex.searchJob'(GlobalVariable.jobName)
CustomKeywords.'project.Capex.runCommand'()
String[] dateTimeTab = CustomKeywords.'project.Capex.checkStatus'()
String csvName = CustomKeywords.'project.Capex.parseCsvName'(dateTimeTab)
String csvFullPath = CustomKeywords.'project.Capex.pathOfCsv'(csvName)
CustomKeywords.'project.Capex.checkCsvHeader'(csvFullPath)
CustomKeywords.'project.Capex.checkCsvContent'(csvFullPath)