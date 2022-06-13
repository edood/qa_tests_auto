import com.reusableComponents.TargetObject as TargetObject
import internal.GlobalVariable as GlobalVariable

LinkedList<TargetObject> results = []
String line_test = GlobalVariable.items[2]

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'("QAP-165")
CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'project.Builder.Start'(false)
CustomKeywords.'project.Builder.selectadvertiser'(GlobalVariable.advertiserName)
CustomKeywords.'project.Builder.refreshAdvertiser'()
CustomKeywords.'project.Builder.load'('LI', '', '', '')
CustomKeywords.'project.Builder.selectItem'("Line Item", 1)
results = CustomKeywords.'project.Targeting.checkTargeting'('Line Item')
File sdfFile = CustomKeywords.'project.Builder.download'()
ArrayList<String> FileToUpload = CustomKeywords.'project.Builder.unzip'(sdfFile)
CustomKeywords.'project.Targeting.CheckTargetinCSVfile'(FileToUpload[1], results)
CustomKeywords.'com.reusableComponents.DV360.DV360Upload'(FileToUpload)
CustomKeywords.'com.reusableComponents.DV360.checkTargetingOnDv'(line_test, results)
CustomKeywords.'project.Builder.deleteFile'(FileToUpload[0])
CustomKeywords.'project.Builder.deleteFile'(FileToUpload[1])
