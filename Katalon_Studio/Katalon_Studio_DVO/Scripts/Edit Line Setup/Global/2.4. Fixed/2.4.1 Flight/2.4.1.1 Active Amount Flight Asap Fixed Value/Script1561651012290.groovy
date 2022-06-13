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

CustomKeywords.'testCases.editLineSetup.EditLineSetup.editLineSetup'(true, 'Active', true, 'Amount', true, '1', true, 'Flight', 
    false, '', true, 'ASAP', true, 'Fixed', true, '1', false, '', false, '')

