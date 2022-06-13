package workaround
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.netty.handler.codec.http.multipart.FileUpload


class UploadGrid {
	TestObject tmp = new TestObject()

	@Keyword
	def UploadFakeGrid (String filepath) {
		tmp.addProperty('css', ConditionType.EQUALS, 'mat-sidenav-content > div > templates > div > div > line-grid > div:nth-child(2)')
		tmp.addProperty('style', ConditionType.EQUALS, 'display: block')
		WebUI.modifyObjectProperty(tmp, 'css', ConditionType.EQUALS.toString(), 'mat-sidenav-container > mat-sidenav-content > div > templates > div > div > line-grid > div:nth-child(2) > input', true)
		WebUI.uploadFile(tmp, filepath)
	}
}