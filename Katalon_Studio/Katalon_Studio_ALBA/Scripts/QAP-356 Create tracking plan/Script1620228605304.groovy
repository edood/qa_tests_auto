import internal.GlobalVariable as GlobalVariable

CustomKeywords.'com.reusableComponents.HighlightElement.pandemic'()

CustomKeywords.'com.reusableComponents.CheckingProfile.checkProfile'('QAP-356')

CustomKeywords.'project.Albatros.Start'()

CustomKeywords.'project.Albatros.goToClientManagement'()

String clientName = CustomKeywords.'project.Albatros.createClient'(GlobalVariable.newClientName)

CustomKeywords.'project.Albatros.setUp'(GlobalVariable.tpSetup, GlobalVariable.createTP, true)

CustomKeywords.'project.Albatros.createTrackingPlan'(clientName, GlobalVariable.tpSetup, GlobalVariable.createTP)

