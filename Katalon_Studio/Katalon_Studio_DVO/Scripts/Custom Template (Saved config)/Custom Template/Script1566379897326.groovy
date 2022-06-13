import customTemplate.CustomTemplate
import internal.GlobalVariable

CustomTemplate testcase = new CustomTemplate()

testcase.start()
testcase.refreshAdvertiser()
testcase.gotoTemplate(testcase.templateToInt(GlobalVariable.template))
testcase.selectDateRange(GlobalVariable.dateRange)
testcase.createConfig()
testcase.editConfigName("Config renamed")
testcase.addGroupbyTemplateDimension(GlobalVariable.template)

testcase.hideColumns(12)
testcase.hideColumns(7)
testcase.hideColumns(9)
testcase.hideColumns(10)

testcase.resizeAll()

testcase.changeOrderColumns(4, -100, 0)
testcase.changeOrderColumns(5, -300, 0)
testcase.changeOrderColumns(6, -600, 0)

testcase.sortColumn(null)

testcase.updateConfig()
testcase.rememberConfig()
testcase.logout()
testcase.login(GlobalVariable.username, GlobalVariable.password)
testcase.selectAdvertiser(GlobalVariable.advertiser)
testcase.selectTemplate(testcase.templateToInt(GlobalVariable.template))
testcase.checkSavedConfig("Config renamed")
testcase.deleteConfig()