import internal.GlobalVariable as GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'("QAP-333")

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()
CustomKeywords.'com.reusableComponents.DV360.Start'()
CustomKeywords.'com.reusableComponents.DV360.logInToDv360'(false)

String cpName = ''
String cp = GlobalVariable.OIMapDV["Campaign"]
boolean archiveOI = GlobalVariable.OIMapDV["archiveOI"]
boolean archiveCP = GlobalVariable.CPMapDV["archiveCP"]
boolean createOnlyCP = GlobalVariable.CPMapDV["createOnlyCP"]

ArrayList<String> Name = new ArrayList<String>()
if (!createOnlyCP)
{
	if (cp.isEmpty())
	{
		cpName = CustomKeywords.'com.reusableComponents.DV360.createCampaign'()
		String oiName = CustomKeywords.'com.reusableComponents.DV360.createInsertionOrder'(cp)
		if (archiveCP && archiveOI || archiveCP && !archiveOI)
			{
				Name.add(cpName)
				CustomKeywords.'com.reusableComponents.DV360.archiveObject'(Name, 'CP')
				
			}
		else if(!archiveCP && archiveOI)
		{
			Name.add(oiName)
			CustomKeywords.'com.reusableComponents.DV360.archiveObject'(Name, 'OI')
		}
	
	} else
	{
		String oiName = CustomKeywords.'com.reusableComponents.DV360.createInsertionOrder'(cp)
		Name.add(oiName)
		if (archiveOI)
			{
				CustomKeywords.'com.reusableComponents.DV360.archiveObject'(Name, 'OI')
			}
	}

}

else
{
	cpName = CustomKeywords.'com.reusableComponents.DV360.createCampaign'()
}