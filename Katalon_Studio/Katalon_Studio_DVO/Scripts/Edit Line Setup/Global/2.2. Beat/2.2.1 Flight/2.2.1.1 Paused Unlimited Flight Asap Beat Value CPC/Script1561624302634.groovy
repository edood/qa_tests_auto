import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'com.reusableComponents.OptimizerDV360.start'()

CustomKeywords.'testCases.editLineSetup.EditLineSetup.editLineSetup'(true, 'Paused', true, 'Unlimited', false, '', true, 
    'Flight', false, '', false, '', true, 'Beat', true, '1', true, 'CPC', true, '0')

