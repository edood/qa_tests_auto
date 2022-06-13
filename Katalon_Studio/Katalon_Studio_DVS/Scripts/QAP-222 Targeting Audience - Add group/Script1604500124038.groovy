import com.reusableComponents.TargetObject as TargetObject
import project.Targeting as Targeting
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.reusableComponents.DV360 as DV360
import internal.GlobalVariable as GlobalVariable

if (GlobalVariable.doNotTest.contains('Audience')) {
    KeywordUtil.markFailedAndStop('Global variable "doNotTest" contains "Audience"')
}

Targeting testcase = new Targeting()
LinkedList<TargetObject> target = []

int[] ObjectToInclude = [1, 2]

String io_test = GlobalVariable.items[1]
String line_test = GlobalVariable.items[2]

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'("QAP-222")
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

testcase.Start(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)

testcase.refreshAdvertiser()

testcase.load('LI', '', '', '')

testcase.selectItem("Insertion Order", 1)

testcase.cleanTargeting('Insertion Order', "Audience")

LinkedList<TargetObject> targetOI = testcase.EditTargeting('Audience', 'Insertion Order', 'Include', ObjectToInclude, target)

target = []

testcase.selectItem("Line Item", 1)

testcase.cleanTargeting('Line Item', "Audience")

LinkedList<TargetObject> targetLine = testcase.EditTargeting('Audience', 'Line Item', 'Include', ObjectToInclude, target)

File sdfFile = testcase.download()

ArrayList<String> sdfPaths = CustomKeywords.'project.Builder.unzip'(sdfFile)

for(def item in targetOI) {
	CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[1], "Audience Targeting - Include", item.ID)
	}
for(def item in targetLine) {
	CustomKeywords.'project.Builder.checkCsvByName'(sdfPaths[2], "Audience Targeting - Include", item.ID)
	}


CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(sdfPaths)

CustomKeywords.'com.reusableComponents.DV360.checkIncludedAudiencesOnDV360'(io_test, 'Include', targetOI)

CustomKeywords.'com.reusableComponents.DV360.checkIncludedAudiencesOnDV360'(line_test, 'Include', targetLine)

testcase.deleteFile(sdfPaths[0])

testcase.deleteFile(sdfPaths[1])

testcase.deleteFile(sdfPaths[2])

KeywordUtil.markPassed('End')

