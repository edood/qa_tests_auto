import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import project.CPSettings as CPSettings
import internal.GlobalVariable as GlobalVariable


CPSettings testcase = new CPSettings()

Long uniquename = new Date().getTime()

ArrayList<String> display = []

ArrayList<String> CampaignToArchive = []

Random kpivalue = new Random()

double record_value

int record_int

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-224')

testcase.Start(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

testcase.refreshAdvertiser()

String record_the_name = 'QAP-224 CP auto_' + uniquename

testcase.createNewCampaign(record_the_name, 'Paused', 0, 1)

CampaignToArchive.push(record_the_name)

testcase.VerifyCreatedTag()

ArrayList<String> Goal = ['Raise awareness of my brand or product', 'Drive online action or visits', 'Drive offline or in-store sales'
    , 'Drive app installs or engagements']

ArrayList<String> GoalKpi = ['CPM', 'Other', 'Viewable %', 'CPIAVC', 'CPA', 'CPC', 'CTR', 'CPV']

//cas du campaigne goal specifique
if (((GlobalVariable.Performance[0]) != '') && ((GlobalVariable.Performance[1]) != '')) {
    WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Campaign'))

    WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))

    WebUI.selectOptionByValue(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal'), 
        GlobalVariable.Performance[0], false)

    WebUI.selectOptionByValue(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI name'), 
        GlobalVariable.Performance[1], false)

    WebUI.clearText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'))

    if ((GlobalVariable.Performance[2]) != '') {
        WebUI.setText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), 
            GlobalVariable.Performance[2])
    } else if (GlobalVariable.Performance[1] != 'Other'){
        record_int = kpivalue.nextInt(100)

        WebUI.setText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), 
            record_int.toString())
    }
    
    WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))

    assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal'), 
        'value') == (GlobalVariable.Performance[0]) : 'Selected goal is different'

    assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI name'), 
        'value') == (GlobalVariable.Performance[1]) : 'Selected goalKPI is different'

    if ((GlobalVariable.Performance[1]) == (GoalKpi[1]) && GlobalVariable.Performance[2] != '') {
        assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), 
            'value') == (GlobalVariable.Performance[2]) : 'Kpi value has changed'
    }
    else if ((GlobalVariable.Performance[1]) == (GoalKpi[1]) && GlobalVariable.Performance[2] == '') {
        assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), 
            'value') == '' : 'Kpi value is not empty'
    } // cas all campaign goal
    else if ((GlobalVariable.Performance[2]) != '') {
        assert Double.parseDouble(GlobalVariable.Performance[2]).round(2) == Double.parseDouble(WebUI.getAttribute(findTestObject(
                    'Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), 'value')) : 'Kpi value has changed'
    } else {
        assert record_int == Double.parseDouble(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), 
                'value')) : 'Kpi value has changed'
    }
} else {
    for (int j = 0; j < 4; j++) {
        int i = 0

        for (; i < 8; i++) {
    	    testcase.QuickDuplicate('CP', findTestObject('Object Repository/6. Objects/First Loaded Campaign'))
			
			WebUI.click(findTestObject('Object Repository/6. Objects/First Loaded Campaign'))
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/General button'))
			String namebeforeduplicate =  WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Name'), 'value')
			WebUI.click(findTestObject('Object Repository/6. Objects/Last CP object'))
			
			assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Name'), 'value') == namebeforeduplicate + ' - Copy'

	       testcase.renameObj(findTestObject('Object Repository/6. Objects/Last CP object'), 'QAP-224 CP auto_' + new Date().getTime().toString(), 'CP')
				
            WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))

            WebUI.selectOptionByIndex(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal'), 
                j)

            WebUI.selectOptionByIndex(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI name'), 
                i)

            record_value = (1 + kpivalue.nextDouble())

            record_int = kpivalue.nextInt(100)

            WebUI.clearText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'))

            if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI name'), 
                'value') != 'Other') {
                WebUI.setText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), 
                    record_value.toString())
            } else {
                WebUI.setText(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), 
                    record_int.toString())
            }
            
            WebUI.click(findTestObject('Object Repository/6. Objects/Last CP object'))
			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/General button'))
			CampaignToArchive.push(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/1. General/Campaign/Name'), 'value'))

			WebUI.click(findTestObject('Object Repository/5. Editor/1. Settings/Advanced button'))	
            assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal'), 
                'value') == (Goal[j]) : 'Selected goal is not recognised'
            assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI name'), 
                'value') == (GoalKpi[i]) : 'Selected goalKPI is not recognised'

            if (WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI name'), 
                'value') != 'Other') {
                assert WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), 
                    'value') == String.format(Locale.US, '%.02f', record_value) : 'Kpi value has changed'
            }
            		
     	 display.push(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal'), 
                    'value'))

            display.push(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI name'), 
                    'value'))

            display.push(WebUI.getAttribute(findTestObject('Object Repository/5. Editor/1. Settings/2. Advanced/Campaign/Campaign Goal KPI Value'), 
                    'value'))
        }
    }
    
    for (def elem : display) {
        KeywordUtil.logInfo('goal settings : ' + elem)
    }
}

File sdfFile = testcase.download()

ArrayList<String> sdfpaths= CustomKeywords.'project.Builder.unzip'(sdfFile)

if (CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(sdfpaths) == false) {
    KeywordUtil.markFailedAndStop('Upload failed')

    return null
}

for (def elem : CampaignToArchive)
	KeywordUtil.logInfo('CampaignToArchive : ' + elem)
	
testcase.deleteFile(sdfpaths[0])
testcase.deleteFile(sdfpaths[1])
	
CustomKeywords.'com.reusableComponents.DV360.archiveObject'(CampaignToArchive, "CP")


