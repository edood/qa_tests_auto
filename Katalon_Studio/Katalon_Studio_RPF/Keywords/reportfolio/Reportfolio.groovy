package reportfolio

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.reusableComponents.CommonKeywords
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium

import internal.GlobalVariable as GlobalVariable

public class Reportfolio extends CommonKeywords {
	protected static String sep = File.separator
	protected static String csvAndTemplateFolderPath = getAbsolutePath(System.getProperty("user.dir") + sep + ".." + sep + ".." + sep +"Katalon_Recorder" + sep + "Reportfolio" + sep + "CSV & Template" + sep)
	protected static String downloadFolderPath = System.getProperty("user.home") + sep + "Downloads" + sep
	protected static String apnCsvFullPath = csvAndTemplateFolderPath + "APN.csv"
	protected static String apnCsvTwoFullPath = csvAndTemplateFolderPath + "APN 2.csv"
	protected static String apnCsvThreeFullPath = csvAndTemplateFolderPath + "APN 3.csv"
	protected static String dv360CsvFullPath = csvAndTemplateFolderPath + "DV360.csv"
	protected static String cmCsvFullPath = csvAndTemplateFolderPath + "CM.csv"
	protected static String gaCsvFullPath = csvAndTemplateFolderPath + "GA.csv"
	protected static String eulCsvFullPath = csvAndTemplateFolderPath + "EUL.csv"
	protected static String biggerCsvFullPath = csvAndTemplateFolderPath + "Bigger CSV.csv"
	protected static String biggerCsvTwoFullPath = csvAndTemplateFolderPath + "Bigger CSV 2.csv"
	protected static String biggerCsvWithEmptyLinesFullPath = csvAndTemplateFolderPath + "Bigger CSV with empty lines.csv"
	protected static String smallerCsvFullPath = csvAndTemplateFolderPath + "Smaller CSV.csv"
	protected static String smallerCsvTwoFullPath = csvAndTemplateFolderPath + "Smaller CSV 2.csv"
	protected static String smallerCsvThreeFullPath = csvAndTemplateFolderPath + "Smaller CSV 3.csv"

	protected static String apnCsvExpectedContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L \n1	43774.0	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	9725057.0	PROSPECTION_ALI_CPA_MOBILE	450250.0	64.0	204.877435	 	 \n2	43774.0	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	6694498.0	PROSPECTION_PUBLISHER_MOBILE	230550.0	40.0	116.174718	 	 \n3	43774.0	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	8016072.0	PROSPECTION_MOBILE_MMO	172410.0	42.0	79.121204	 	 \n4	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	2791000.0	PROSPECTION_AUTOMATIONVISITE_DESKTOP	133539.0	17.0	126.471034	 	 \n5	43774.0	845763.0	Boursorama	3113824.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Novembre19	10044300.0	PROSPECTION_BIDDER_DESKTOP	83853.0	26.0	80.558281	 	 \n6	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	3936328.0	PROSPECTION_APB_DESKTOP	82437.0	42.0	87.334454	 	 \n7	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	7662574.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP	61555.0	3.0	60.85293	 	 \n8	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	8023288.0	PROSPECTION_AUTOMATION-LEADv2_DESKTOP	57821.0	10.0	61.283288	 	 \n9	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	9650618.0	PROSPECTION_APB_DESKTOP_NEW	29638.0	12.0	49.251197	 	 \n10	43774.0	845763.0	Boursorama	3108427.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	9106179.0	PROFILING_MOBILE	26957.0	7.0	43.264773	 	 \n11	43774.0	845763.0	Boursorama	3108508.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	8724447.0	PROFILING_ULTIM_DESKTOP	8436.0	2.0	17.516538	 	 \n12	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	6694498.0	PROSPECTION_PUBLISHER_MOBILE	0.0	0.0	0.0	 	 \n13	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	8016072.0	PROSPECTION_MOBILE_MMO	0.0	0.0	0.0	 	 \n14	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9106179.0	PROFILING_MOBILE	0.0	0.0	0.0	 	 \n15	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9725057.0	PROSPECTION_ALI_CPA_MOBILE	0.0	0.0	0.0	 	 \n16	43774.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9760094.0	PROSPECTION_NATIVE	0.0	0.0	0.0	 	 \n17	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	2791000.0	PROSPECTION_AUTOMATIONVISITE_DESKTOP	0.0	0.0	0.0	 	 \n18	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	3936328.0	PROSPECTION_APB_DESKTOP	0.0	0.0	0.0	 	 \n19	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	7662574.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP	0.0	0.0	0.0	 	 \n20	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	8023288.0	PROSPECTION_AUTOMATION-LEADv2_DESKTOP	0.0	0.0	0.0	 	 \n21	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	8724447.0	PROFILING_ULTIM_DESKTOP	0.0	0.0	0.0	 	 \n22	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9650618.0	PROSPECTION_APB_DESKTOP_NEW	0.0	0.0	0.0	 	 \n23	43774.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9760155.0	PROSPECTION_NATIVE_DESKTOP	0.0	0.0	0.0	 	 \n24	43774.0	845763.0	Boursorama	2842170.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Octobre19	4612927.0	PROSPECTION_BIDDER_DESKTOP	0.0	0.0	0.0	 	 "
	protected static String dv360CsvExpectedContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L \n1	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No	29316317.0	EUR	982.0	1.0	0.602487	 	 \n2	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No	31514957.0	EUR	2678.0	0.0	2.183799	 	 \n3	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No_CPA	29316319.0	EUR	13858.0	2.0	10.666706	 	 \n4	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_Retargeting-6h_Google_Final Fantasy Fans_No	29316321.0	EUR	35.0	0.0	0.436678	 	 \n5	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_Retargeting-7j_Google_FFXIV Interest_No	29316349.0	EUR	284.0	0.0	1.143764	 	 \n6	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_x_Google_Final Fantasy Fans_No	29316353.0	EUR	6672.0	0.0	3.758819	 	 \n7	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No	29316361.0	EUR	124.0	0.0	0.952738	 	 \n8	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No	31514962.0	EUR	144.0	0.0	1.052764	 	 \n9	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1j_Webedia_PC Gamers / MMORPG Games Fans_No	29316362.0	EUR	388.0	0.0	2.459044	 	 \n10	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No	29316368.0	EUR	2653.0	0.0	1.710639	 	 \n11	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No_Surpression Placement Test	30731876.0	EUR	12.0	0.0	0.009859	 	 \n12	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_J-RPG and RPG Fans_No	29316372.0	EUR	6228.0	0.0	10.588579	 	 \n13	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_J-RPG and RPG Fans_No - Meet or beat	30087396.0	EUR	4435.0	0.0	0.922908	 	 \n14	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_Retargeting-30j_Webedia_Pack Gaming - IAB_Yes	29320180.0	EUR	6591.0	4.0	7.095674	 	 \n15	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No	31514950.0	EUR	7589.0	1.0	10.288911	 	 \n16	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No_CPA	29316367.0	EUR	37764.0	3.0	7.323307	 	 \n17	43775.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_Pack Gaming - IAB_Yes	29320181.0	EUR	727.0	0.0	0.60003	 	 \n18	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	Boutique_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No	29543363.0	EUR	11936.0	2.0	5.494966	 	 \n19	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	Boutique_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No	29543364.0	EUR	1543.0	0.0	0.917072	 	 \n20	43775.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique - Aot-Novembre	 	 	 	 	 	 	 	 "
	protected static String cmCsvExpectedContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L 	M 	N 	O 	P 	Q 	R 	S 	T 	U 	V 	W 	X 	Y 	Z 	AA 	AB 	AC 	AD 	AE 	AF 	AG 	AH 	AI 	AJ 	AK 	AL 	AM 	AN \n1	BS FR ALWAYS ON 2019	22450063.0	BS-FR-AND-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X	43587.0	16044513.0	243731813.0	2.0	0.0	0.0	2.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n2	BS FR ALWAYS ON 2019	22450063.0	BS-FR-AND-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X	43594.0	16044513.0	243731813.0	1.0	0.0	0.0	1.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n3	BS FR ALWAYS ON 2019	22450063.0	BS-FR-AND-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X	43598.0	16044513.0	243731813.0	4.0	1.0	25.0	4.0	4.0	4.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n4	BS FR ALWAYS ON 2019	22450063.0	BS-FR-iOS-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X	43587.0	16044455.0	244058808.0	1.0	0.0	0.0	1.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n5	BS FR ALWAYS ON 2019	22450063.0	BS-FR-iOS-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X	43594.0	16044455.0	244058808.0	1.0	0.0	0.0	1.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n6	BS FR ALWAYS ON 2019	22450063.0	BS-FR-iOS-AlwaysOn-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-UT100-FDB-LP-300x250-P-X	43598.0	16044455.0	244058808.0	4.0	3.0	75.0	4.0	4.0	4.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n7	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43575.0	16043499.0	243869100.0	31235.0	48.0	0.15	16566.0	30490.0	30558.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n8	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43576.0	16043499.0	243869100.0	33139.0	89.0	0.27	17481.0	32182.0	32273.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n9	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43577.0	16043499.0	243869100.0	34701.0	66.0	0.19	18402.0	33926.0	34008.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n10	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43578.0	16043499.0	243869100.0	29444.0	56.0	0.19	14293.0	28617.0	28697.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n11	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43579.0	16043499.0	243869100.0	19791.0	45.0	0.23	9961.0	19338.0	19387.0	4.0	4.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	4.0	4.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n12	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43580.0	16043499.0	243869100.0	11123.0	30.0	0.27	5802.0	10874.0	10905.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n13	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43582.0	16043499.0	243869100.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n14	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43585.0	16043499.0	243869100.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n15	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43586.0	16043499.0	243869100.0	1.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n16	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43587.0	16043499.0	243869100.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n17	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43588.0	16043499.0	243869100.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n18	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43589.0	16043499.0	243869100.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n19	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-P-X	43595.0	16043499.0	243869100.0	1.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n20	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43575.0	16044091.0	243869097.0	6050.0	21.0	0.35	2131.0	6022.0	6033.0	7.0	7.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	7.0	7.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n21	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43576.0	16044091.0	243869097.0	5626.0	22.0	0.39	1967.0	5595.0	5606.0	3.0	3.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	3.0	3.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n22	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43577.0	16044091.0	243869097.0	6375.0	25.0	0.39	2339.0	6330.0	6347.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n23	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43578.0	16044091.0	243869097.0	5220.0	9.0	0.17	1684.0	5173.0	5179.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n24	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43579.0	16044091.0	243869097.0	4452.0	16.0	0.36	1500.0	4423.0	4426.0	7.0	7.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	7.0	7.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n25	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43580.0	16044091.0	243869097.0	1632.0	6.0	0.37	579.0	1613.0	1619.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n26	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43581.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n27	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43582.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	4.0	4.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	4.0	4.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n28	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43583.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	4.0	4.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	4.0	4.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n29	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43584.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n30	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43585.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n31	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43586.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n32	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43587.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n33	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43588.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	3.0	3.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n34	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43590.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n35	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43592.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	0.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n36	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43593.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	0.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n37	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43601.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	0.0	1.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n38	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-300x250-R-X	43602.0	16044091.0	243869097.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	0.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n39	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-P-X	43575.0	16043523.0	243869094.0	12792.0	42.0	0.33	7903.0	12124.0	12184.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n40	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-P-X	43576.0	16043523.0	243869094.0	15250.0	69.0	0.45	9965.0	14531.0	14619.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n41	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-P-X	43577.0	16043523.0	243869094.0	10978.0	40.0	0.36	6094.0	9888.0	9953.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n42	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-P-X	43578.0	16043523.0	243869094.0	6305.0	20.0	0.32	3552.0	5967.0	6001.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n43	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-P-X	43579.0	16043523.0	243869094.0	12388.0	43.0	0.35	7359.0	11892.0	11975.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n44	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-P-X	43580.0	16043523.0	243869094.0	6875.0	20.0	0.29	4199.0	6690.0	6721.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	2.0	2.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n45	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-P-X	43581.0	16043523.0	243869094.0	1.0	0.0	0.0	0.0	1.0	1.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n46	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-P-X	43583.0	16043523.0	243869094.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n47	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X	43575.0	16044125.0	243869091.0	361.0	0.0	0.0	195.0	360.0	361.0	1.0	1.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n48	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X	43576.0	16044125.0	243869091.0	550.0	8.0	1.45	321.0	546.0	549.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n49	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X	43577.0	16044125.0	243869091.0	473.0	6.0	1.27	254.0	471.0	471.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n50	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X	43578.0	16044125.0	243869091.0	327.0	3.0	0.92	146.0	325.0	326.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n51	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X	43579.0	16044125.0	243869091.0	413.0	8.0	1.94	222.0	412.0	413.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n52	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X	43580.0	16044125.0	243869091.0	171.0	1.0	0.58	79.0	170.0	170.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n53	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X	43581.0	16044125.0	243869091.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n54	PS BR FRIENDS 2019	22504307.0	PS-BR-AND-FRIENDS-G-StrangeThoughts-Tradelab-OpenMarket-ROS-ALL-ALL-LARGEST-FDB-LP-320x480-R-X	43585.0	16044125.0	243869091.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	 \n55	Grand Total:	---	---	---	---	---	94384605.0	87185.0	0.09	47399586.0	87196125.0	89461295.0	18578.0	17777.0	801.0	608.0	9682.0	9074.0	4.0	265.0	261.0	3.0	140.0	137.0	6.0	231.0	225.0	4.0	210.0	206.0	87.0	3362.0	3275.0	14.0	644.0	630.0	75.0	4044.0	3969.0	 "
	protected static String gaCsvExpectedContent = " 	A 	B 	C 	D 	E 	F 	G \n1	code:RF_ERR1,email:debug.tradelab@gmail.com	5.0	43.0	8.0	0.0	0.0	 \n2	code:RF_ERR1,email:debug.tradelab@gmail.com	20.0	26.0	3.0	0.0	0.0	 \n3	code:RF_ERR1,email:debug.tradelab@gmail.com	50.0	25.0	1.0	0.0	0.0	 \n4	code:RF_ERR1,email:debug.tradelab@gmail.com	15.0	21.0	1.0	0.0	0.0	 \n5	code:RF_ERR1,email:debug.tradelab@gmail.com	10.0	19.0	0.0	0.0	0.0	 \n6	code:RF_ERR1,email:debug.tradelab@gmail.com	0.0	7.0	1.0	0.0	0.0	 \n7	code:RF_ERR1,email:debug.tradelab+59b210f119ab12817aaf44df4197aad4@gmail.com	5.0	7.0	5.0	0.0	0.0	 \n8	code:RF_ERR1,email:debug.tradelab@gmail.com	30.0	6.0	0.0	0.0	0.0	 \n9	code:RF_ERR1,email:debug.tradelab@gmail.com	55.0	5.0	1.0	0.0	0.0	 \n10	code:RF_ERR1,email:debug.tradelab+59b210f119ab12817aaf44df4197aad4@gmail.com	10.0	5.0	2.0	0.0	0.0	 \n11	code:RF_ERR1,email:debug.tradelab+c7237bbe635a59a51c88773a8a90f7a5@gmail.com	5.0	5.0	5.0	0.0	0.0	 \n12	code:RF_ERR1,email:debug.tradelab@gmail.com	25.0	4.0	0.0	0.0	0.0	 \n13	code:RF_ERR1,email:debug.tradelab+1a6e19348ece5c9fffff4ccf8dc0c654@gmail.com	55.0	4.0	4.0	0.0	0.0	 \n14	code:RF_ERR1,email:debug.tradelab+2d978ce430aa2e3ac8814ce5764cca3d@gmail.com	5.0	4.0	4.0	0.0	0.0	 \n15	code:RF_ERR1,email:debug.tradelab+d5750b4a400699aad7d0bb7d94e8f87b@gmail.com	10.0	4.0	4.0	0.0	0.0	 \n16	code:RF_ERR1,email:debug.tradelab+1a6e19348ece5c9fffff4ccf8dc0c654@gmail.com	50.0	3.0	3.0	0.0	0.0	 \n17	code:RF_ERR1,email:debug.tradelab+2d978ce430aa2e3ac8814ce5764cca3d@gmail.com	10.0	3.0	3.0	0.0	0.0	 \n18	code:RF_ERR1,email:debug.tradelab+5c3c7ee14bc02a0cb2f52a2be56a8c8d@gmail.com	25.0	3.0	0.0	0.0	0.0	 \n19	code:RF_ERR1,email:debug.tradelab+5c3c7ee14bc02a0cb2f52a2be56a8c8d@gmail.com	30.0	3.0	2.0	0.0	0.0	 \n20	code:RF_ERR1,email:debug.tradelab+7d3eeeaca3807977522eb7ff73db5361@gmail.com	5.0	3.0	3.0	0.0	0.0	 \n21	code:RF_ERR1,email:debug.tradelab+d5750b4a400699aad7d0bb7d94e8f87b@gmail.com	5.0	3.0	3.0	0.0	0.0	 \n22	code:RF_ERR1,email:debug.tradelab+5c3c7ee14bc02a0cb2f52a2be56a8c8d@gmail.com	5.0	2.0	2.0	0.0	0.0	 \n23	code:RF_ERR1,email:debug.tradelab+5c3c7ee14bc02a0cb2f52a2be56a8c8d@gmail.com	10.0	2.0	0.0	0.0	0.0	 \n24	code:RF_ERR1,email:debug.tradelab+5c3c7ee14bc02a0cb2f52a2be56a8c8d@gmail.com	15.0	2.0	2.0	0.0	0.0	 \n25	code:RF_ERR1,email:debug.tradelab+7d3eeeaca3807977522eb7ff73db5361@gmail.com	10.0	2.0	2.0	0.0	0.0	 \n26	code:RF_ERR1,email:debug.tradelab+7d3eeeaca3807977522eb7ff73db5361@gmail.com	15.0	2.0	2.0	0.0	0.0	 \n27	code:RF_ERR1,email:debug.tradelab+c7237bbe635a59a51c88773a8a90f7a5@gmail.com	10.0	2.0	2.0	0.0	0.0	 \n28	code:RF_ERR1,email:debug.tradelab@gmail.com	49.0	1.0	1.0	0.0	0.0	 \n29	code:RF_ERR1,email:debug.tradelab@gmail.com	1.0	1.0	0.0	0.0	0.0	 \n30	code:RF_ERR1,email:debug.tradelab@gmail.com	44.0	1.0	1.0	0.0	0.0	 \n31	code:RF_ERR1,email:debug.tradelab@gmail.com	45.0	1.0	1.0	0.0	0.0	 \n32	code:RF_ERR1,email:debug.tradelab+59b210f119ab12817aaf44df4197aad4@gmail.com	15.0	1.0	0.0	0.0	0.0	 \n33	code:RF_ERR1,email:debug.tradelab+59b210f119ab12817aaf44df4197aad4@gmail.com	20.0	1.0	0.0	0.0	0.0	 \n34	code:RF_ERR1,email:debug.tradelab+5c3c7ee14bc02a0cb2f52a2be56a8c8d@gmail.com	20.0	1.0	1.0	0.0	0.0	 \n35	code:RF_ERR1,email:debug.tradelab+5c3c7ee14bc02a0cb2f52a2be56a8c8d@gmail.com	50.0	1.0	0.0	0.0	0.0	 \n36	code:RF_ERR1,email:debug.tradelab+eac2b4652e5b7c1a3f39e958ce264197@gmail.com	44.0	1.0	1.0	0.0	0.0	 \n37	code:RF_ERR1,email:debug.tradelab+eac2b4652e5b7c1a3f39e958ce264197@gmail.com	49.0	1.0	1.0	0.0	0.0	 \n38	code:RF_ERR1,email:debug.tradelab+eac2b4652e5b7c1a3f39e958ce264197@gmail.com	55.0	1.0	1.0	0.0	0.0	 "
	protected static String eulCsvExpectedContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L 	M 	N 	O 	P 	Q 	R 	S 	T 	U 	V 	W 	X \n1	x1global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n2	x2Tradelab-Tradelab-Strat	281832.0	12676.0	1635.0	0.2559	0.4763	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	58.0	26653.0	6m.4s.	1355.0	0.0066	0.0048	0.0017	 \n3	x3Tradelab-Tradelab-Teads-RG-Mobile	0.0	1.0	0.0	0%	0%	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	28s.	0.0	0%	0%	0%	 \n4	x4Tradelab-Tradelab-Strat-Device-Mobile	5.0	33.0	2.0	0.1333	0.2909	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	55.0	6m.39s.	0.0	0.6	0%	0.6	 \n5	x5global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n6	x6Tradelab-Tradelab-Strat	281832.0	12676.0	1635.0	0.2559	0.4763	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	58.0	26653.0	6m.4s.	1355.0	0.0066	0.0048	0.0017	 \n7	x7global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n8	x8Tradelab-Tradelab-Teads-RG-Mobile	0.0	1.0	0.0	0%	0%	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	28s.	0.0	0%	0%	0%	 \n9	x9Tradelab-Tradelab-Strat-Device-Mobile	5.0	33.0	2.0	0.1333	0.2909	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	55.0	6m.39s.	0.0	0.6	0%	0.6	 \n10	x10global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n11	Tradelab-Tradelab-Strat	281832.0	12676.0	1635.0	0.2559	0.4763	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	58.0	26653.0	6m.4s.	1355.0	0.0066	0.0048	0.0017	 \n12	global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n13	Tradelab-Tradelab-Teads-RG-Mobile	0.0	1.0	0.0	0%	0%	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	28s.	0.0	0%	0%	0%	 \n14	Tradelab-Tradelab-Strat-Device-Mobile	5.0	33.0	2.0	0.1333	0.2909	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	55.0	6m.39s.	0.0	0.6	0%	0.6	 \n15	global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n16	Tradelab-Tradelab-Strat	281832.0	12676.0	1635.0	0.2559	0.4763	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	58.0	26653.0	6m.4s.	1355.0	0.0066	0.0048	0.0017	 \n17	global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n18	Tradelab-Tradelab-Teads-RG-Mobile	0.0	1.0	0.0	0%	0%	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	28s.	0.0	0%	0%	0%	 \n19	Tradelab-Tradelab-Strat-Device-Mobile	5.0	33.0	2.0	0.1333	0.2909	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	55.0	6m.39s.	0.0	0.6	0%	0.6	 \n20	global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n21	Tradelab-Tradelab-Strat	281832.0	12676.0	1635.0	0.2559	0.4763	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	58.0	26653.0	6m.4s.	1355.0	0.0066	0.0048	0.0017	 \n22	global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n23	Tradelab-Tradelab-Strat	281832.0	12676.0	1635.0	0.2559	0.4763	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	58.0	26653.0	6m.4s.	1355.0	0.0066	0.0048	0.0017	 \n24	z10global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n25	z9global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n26	z8Tradelab-Tradelab-Strat	281832.0	12676.0	1635.0	0.2559	0.4763	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	58.0	26653.0	6m.4s.	1355.0	0.0066	0.0048	0.0017	 \n27	z7Tradelab-Tradelab-Teads-RG-Mobile	0.0	1.0	0.0	0%	0%	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	28s.	0.0	0%	0%	0%	 \n28	z6Tradelab-Tradelab-Strat-Device-Mobile	5.0	33.0	2.0	0.1333	0.2909	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	55.0	6m.39s.	0.0	0.6	0%	0.6	 \n29	z5global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 \n30	z4Tradelab-Tradelab-Strat	281832.0	12676.0	1635.0	0.2559	0.4763	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	58.0	26653.0	6m.4s.	1355.0	0.0066	0.0048	0.0017	 \n31	z3Tradelab-Tradelab-Teads-RG-Mobile	0.0	1.0	0.0	0%	0%	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	28s.	0.0	0%	0%	0%	 \n32	z2Tradelab-Tradelab-Strat-Device-Mobile	5.0	33.0	2.0	0.1333	0.2909	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	1.0	55.0	6m.39s.	0.0	0.6	0%	0.6	 \n33	z1global	281837.0	12710.0	1637.0	0.2556	0.4759	224.0	224.0	19.0	203.0	2.0	87.0	87.0	7.0	2.0	78.0	59.0	26709.0	6m.4s.	1355.0	0.0066	0.0048	0.0018	 "

	protected static String apnMailCsvExpectedContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L 	M 	N 	O 	P \n1	43770.0	845763.0	Boursorama	3108427.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	9725057.0	PROSPECTION_ALI_CPA_MOBILE	1061421.0	356.0	532.947821	 	 	 	 	 	 \n2	43770.0	845763.0	Boursorama	3108427.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	8016072.0	PROSPECTION_MOBILE_MMO	454385.0	107.0	222.918579	 	 	 	 	 	 \n3	43770.0	845763.0	Boursorama	3108427.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	6694498.0	PROSPECTION_PUBLISHER_MOBILE	405666.0	79.0	191.863635	 	 	 	 	 	 \n4	43770.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	2791000.0	PROSPECTION_AUTOMATIONVISITE_DESKTOP	320463.0	59.0	281.129518	 	 	 	 	 	 \n5	43770.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	7662574.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP_OLD	171551.0	17.0	162.185814	 	 	 	 	 	 \n6	43770.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	8023288.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP	157262.0	40.0	166.053386	 	 	 	 	 	 \n7	43770.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	3936328.0	PROSPECTION_APB_DESKTOP	80945.0	54.0	84.020066	 	 	 	 	 	 \n8	43770.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	9650618.0	PROSPECTION_APB_DESKTOP_NEW	69399.0	26.0	110.499216	 	 	 	 	 	 \n9	43770.0	845763.0	Boursorama	3108427.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	9106179.0	PROFILING_MOBILE	34824.0	11.0	55.813976	 	 	 	 	 	 \n10	43770.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	8724447.0	PROFILING_DESKTOP	17574.0	10.0	36.424572	 	 	 	 	 	 \n11	43770.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	6694498.0	PROSPECTION_PUBLISHER_MOBILE	0.0	0.0	0.0	 	 	 	 	 	 \n12	43770.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	8016072.0	PROSPECTION_MOBILE_MMO	0.0	0.0	0.0	 	 	 	 	 	 \n13	43770.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9106179.0	PROFILING_MOBILE	0.0	0.0	0.0	 	 	 	 	 	 \n14	43770.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9725057.0	PROSPECTION_ALI_CPA_MOBILE	0.0	0.0	0.0	 	 	 	 	 	 \n15	43770.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9760094.0	PROSPECTION_NATIVE	0.0	0.0	0.0	 	 	 	 	 	 \n16	43770.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	2791000.0	PROSPECTION_AUTOMATIONVISITE_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n17	43770.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	3936328.0	PROSPECTION_APB_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n18	43770.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	7662574.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP_OLD	0.0	0.0	0.0	 	 	 	 	 	 \n19	43770.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	8023288.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n20	43770.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	8724447.0	PROFILING_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n21	43770.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9650618.0	PROSPECTION_APB_DESKTOP_NEW	0.0	0.0	0.0	 	 	 	 	 	 \n22	43770.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9814449.0	PROSPECTION_DEALORANGE_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n23	43770.0	845763.0	Boursorama	2842170.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Octobre19	4612927.0	PROSPECTION_BIDDER_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n24	43771.0	845763.0	Boursorama	3108427.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	9725057.0	PROSPECTION_ALI_CPA_MOBILE	979347.0	334.0	507.568818	 	 	 	 	 	 \n25	43771.0	845763.0	Boursorama	3108427.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	6694498.0	PROSPECTION_PUBLISHER_MOBILE	485109.0	111.0	259.824698	 	 	 	 	 	 \n26	43771.0	845763.0	Boursorama	3108427.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	8016072.0	PROSPECTION_MOBILE_MMO	356950.0	77.0	178.030041	 	 	 	 	 	 \n27	43771.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	2791000.0	PROSPECTION_AUTOMATIONVISITE_DESKTOP	316089.0	55.0	277.873022	 	 	 	 	 	 \n28	43771.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	7662574.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP_OLD	164030.0	22.0	164.178437	 	 	 	 	 	 \n29	43771.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	8023288.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP	155464.0	29.0	165.864847	 	 	 	 	 	 \n30	43771.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	9650618.0	PROSPECTION_APB_DESKTOP_NEW	69921.0	19.0	111.22032	 	 	 	 	 	 \n31	43771.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	3936328.0	PROSPECTION_APB_DESKTOP	67358.0	48.0	68.408963	 	 	 	 	 	 \n32	43771.0	845763.0	Boursorama	3108427.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Mobile_Novembre19	9106179.0	PROFILING_MOBILE	36535.0	13.0	59.202109	 	 	 	 	 	 \n33	43771.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	8724447.0	PROFILING_DESKTOP	16998.0	13.0	34.597634	 	 	 	 	 	 \n34	43771.0	845763.0	Boursorama	3108508.0	OI_201911_Boursorama_AEGIS_Performance_BtoC_Desktop_Novembre19	9814449.0	PROSPECTION_DEALORANGE_DESKTOP	498.0	0.0	0.833513	 	 	 	 	 	 \n35	43771.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	6694498.0	PROSPECTION_PUBLISHER_MOBILE	0.0	0.0	0.0	 	 	 	 	 	 \n36	43771.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	8016072.0	PROSPECTION_MOBILE_MMO	0.0	0.0	0.0	 	 	 	 	 	 \n37	43771.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9106179.0	PROFILING_MOBILE	0.0	0.0	0.0	 	 	 	 	 	 \n38	43771.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9725057.0	PROSPECTION_ALI_CPA_MOBILE	0.0	0.0	0.0	 	 	 	 	 	 \n39	43771.0	845763.0	Boursorama	2842154.0	OI_201909_Boursorama_AEGIS_Performance_BtoC_Mobile_Octobre19	9760094.0	PROSPECTION_NATIVE	0.0	0.0	0.0	 	 	 	 	 	 \n40	43771.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	2791000.0	PROSPECTION_AUTOMATIONVISITE_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n41	43771.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	3936328.0	PROSPECTION_APB_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n42	43771.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	7662574.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP_OLD	0.0	0.0	0.0	 	 	 	 	 	 \n43	43771.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	8023288.0	PROSPECTION_AUTOMATION-LEAD_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n44	43771.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	8724447.0	PROFILING_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n45	43771.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9650618.0	PROSPECTION_APB_DESKTOP_NEW	0.0	0.0	0.0	 	 	 	 	 	 \n46	43771.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9760155.0	PROSPECTION_NATIVE_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n47	43771.0	845763.0	Boursorama	2842164.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Octobre19	9814449.0	PROSPECTION_DEALORANGE_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 \n48	43771.0	845763.0	Boursorama	2842170.0	OI_201910_Boursorama_AEGIS_Performance_BtoC_Desktop_Bidder_Octobre19	4612927.0	PROSPECTION_BIDDER_DESKTOP	0.0	0.0	0.0	 	 	 	 	 	 "
	protected static String dv360MailCsvExpectedContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L \n1	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No	29316317.0	EUR	8451.0	5.0	2.032759	 	 \n2	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No	31514957.0	EUR	13206.0	0.0	10.021726	 	 \n3	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Category_x_Google_ Games/Online Games/Massive Multiplayer Games Fans_No_CPA	29316319.0	EUR	83936.0	9.0	36.320054	 	 \n4	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_Retargeting-6h_Google_Final Fantasy Fans_No	29316321.0	EUR	240.0	0.0	3.014481	 	 \n5	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_Retargeting-7j_Google_FFXIV Interest_No	29316349.0	EUR	991.0	0.0	4.238659	 	 \n6	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Keywords_x_Google_Final Fantasy Fans_No_MinimizeCPA	29316353.0	EUR	26293.0	1.0	10.104678	 	 \n7	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest-optimise-CPA_No	31514962.0	EUR	578.0	0.0	4.174334	 	 \n8	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest_No	29316361.0	EUR	593.0	0.0	4.216604	 	 \n9	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1h_Webedia_FFXIV Interest_No_CPA	31514948.0	EUR	12.0	0.0	0.148069	 	 \n10	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_Retargeting-1j_Webedia_PC Gamers / MMORPG Games Fans_No	29316362.0	EUR	2211.0	0.0	11.460839	 	 \n11	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise-CPA_No_Surpression Placement Test	30731876.0	EUR	1007.0	0.0	0.753551	 	 \n12	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_FFXIV Interest-optimise_No_CPA	29316368.0	EUR	12614.0	0.0	4.888776	 	 \n13	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_J-RPG and RPG Fans_No - Meet or beat	30087396.0	EUR	40092.0	2.0	8.977179	 	 \n14	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Profiling_x_Webedia_J-RPG and RPG Fans_No_MinimizeCPA	29316372.0	EUR	27043.0	3.0	22.319398	 	 \n15	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_Retargeting-30j_Webedia_Pack Gaming - IAB_Yes	29320180.0	EUR	23562.0	5.0	20.110971	 	 \n16	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No	31514950.0	EUR	7614.0	1.0	10.121849	 	 \n17	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_FFXIV Interest-Best Placement_No_MinimizeCPA	29316367.0	EUR	185489.0	26.0	30.344232	 	 \n18	43770.0	SQUARE ENIX	10307151.0	Square Enix_Display_IAB_Multi_Free-Trial	Free-Trial_IAB_Multi_Prospecting_x_Webedia_Pack Gaming - IAB_No	29320181.0	EUR	3941.0	1.0	2.006611	 	 \n19	43770.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique#	Boutique_IAB_Multi_Affinity_x_Google_Gamers / Hardcore Games + Roleplaying Game Fans_No_MinimizeCPA	29543363.0	EUR	46469.0	21.0	20.106038	 	 \n20	43770.0	SQUARE ENIX	10411476.0	Square Enix_Display_IAB_Multi_Boutique#	 	 	 	 	 	 	 	 "
	protected static String cmMailCsvExpectedContent = " 	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L 	M \n1	SquareEnix_FFXIV_102019	23420490.0	SquareEnix_160x600_FFXIV_102019	43770.0	258945981.0	67.0	21.0	31.34	54.0	67.0	67.0	0.0	 \n2	SquareEnix_FFXIV_102019	23420490.0	SquareEnix_160x600_FFXIV_102019	43771.0	258945981.0	19.0	6.0	31.58	16.0	19.0	19.0	0.0	 \n3	SquareEnix_FFXIV_102019	23420490.0	SquareEnix_300x250_FFXIV_102019	43770.0	258945243.0	113.0	36.0	31.86	86.0	113.0	113.0	0.0	 \n4	SquareEnix_FFXIV_102019	23420490.0	SquareEnix_300x250_FFXIV_102019	43771.0	258945243.0	4.0	0.0	0.0	4.0	4.0	4.0	0.0	 \n5	SquareEnix_FFXIV_102019	23420490.0	SquareEnix_300x600_FFXIV_102019	43770.0	258945246.0	63.0	15.0	23.81	50.0	63.0	63.0	0.0	 \n6	SquareEnix_FFXIV_102019	23420490.0	SquareEnix_300x600_FFXIV_102019	43771.0	258945246.0	4.0	0.0	0.0	4.0	4.0	4.0	0.0	 \n7	SquareEnix_FFXIV_102019	23420490.0	SquareEnix_728x90_FFXIV_102019	43770.0	258945258.0	63.0	15.0	23.81	47.0	63.0	63.0	0.0	 \n8	SquareEnix_FFXIV_102019	23420490.0	SquareEnix_728x90_FFXIV_102019	43771.0	258945258.0	4.0	0.0	0.0	4.0	4.0	4.0	0.0	 \n9	Grand Total:	---	---	---	---	337.0	93.0	27.6	265.0	337.0	337.0	0.0	 "

	protected void createFolder(String folderName) {
		/**
		 * Creates a folder
		 * 
		 * @author fbalhier
		 * 
		 * @param folderName			Name of the folder
		 */
		
		// Wait for folders to load because modal is broken before that
		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Open first folder button'), 5)

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/New Folder button'), 30)
		WebUI.click(findTestObject('Object Repository/2. Folders list/New Folder button'))
		WebUI.waitForElementVisible(findTestObject('2. Folders list/Modal/Modal label'), 5)
		assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal label'), "Create New Folder") == true
		WebUI.setText(findTestObject('2. Folders list/Modal/Modal 1st position input'), folderName)
		assert WebUI.verifyElementPresent(findTestObject('2. Folders list/Modal/Cancel button'), 2) == true
		WebUI.click(findTestObject('2. Folders list/Modal/Basic Report Save button'))
	}

	protected String createModel(String modelName, String scheduleFrequency, String templateName, String templateFormat, String sheetNumber, String subreportName, int datasource, String startFromRow, String excludeLast, String separator, boolean cumulative) {
		/**
		 * Creates a model with full setup
		 * 
		 * @author fbalhier
		 * 
		 * @param modelName				Name of the model to create
		 * @param scheduleFrequency		Schedule frequency : "daily", "weekly", "every two weeks", "monthly"
		 * @param templateName			Name of the template to upload. The file must exist.
		 * @param templateFormat		Extension of the template to upload : ".xlsx" / ".xlsm"
		 * @param sheetNumber			Sheet number to set in the XLS Settings
		 * @param subreportName			Name of the subreport to create
		 * @param datasource			Chosen datasource for the creation of the subreport (position in the list of datasources)
		 * @param startFromRow			"Start from row" value to set in the CSV Settings
		 * @param excludeLast			"Exclude last" value to set in the CSV Settings
		 * @param separator				Separator value to set in the CSV Settings : "," / ";" / "tab"
		 * @param cumulative			"true" to enable and "false" to disable the cumulative option in the CSV Settings.
		 */
		
		WebUI.click(findTestObject('Object Repository/2. Folders list/New Model button'))
		WebUI.waitForElementVisible(findTestObject('2. Folders list/Modal/Modal label'), 5)
		assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal label'), "Create New Model") == true
		assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal 1st position select'), "Folder") == true
		WebUI.click(findTestObject('2. Folders list/Modal/Modal 1st position select'))
		Thread.sleep(500);
		WebUI.click(findTestObject('2. Folders list/Modal/Overlay First option'))
		WebUI.waitForElementVisible(findTestObject('2. Folders list/Modal/Modal 2nd position input'), 2)
		assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal 2nd position label'), "Model Name") == true
		WebUI.setText(findTestObject('2. Folders list/Modal/Modal 2nd position input'), modelName)
		assert WebUI.verifyElementPresent(findTestObject('2. Folders list/Modal/Cancel button'), 2) == true
		WebUI.click(findTestObject('2. Folders list/Modal/Basic Report Save button'))
		WebUI.delay(2)
		assert WebUI.waitForElementNotPresent(findTestObject('Object Repository/3. Report/Progress bar'), 10) == true
		createSubreport(subreportName, datasource)
		setupScheduleToday(scheduleFrequency)
		String toast = uploadModelTemplate(templateFormat, templateName)
		setupModelXlsSettings(sheetNumber)
		setupCsvSettings(startFromRow, excludeLast, separator, cumulative)

		return toast
	}

	protected void createReport(String reportName, String advertiserName) {
		/**
		 * Creates a report with no setup
		 *
		 * @author fbalhier
		 *
		 * @param reportName			Name of the report to create
		 * @param advertiserName		Name of the advertiser that will automatically be used as a prefix in the name of the report
		 */

		WebUI.click(findTestObject('Object Repository/2. Folders list/New Report button'))
		WebUI.click(findTestObject('Object Repository/2. Folders list/Basic report button'))
		WebUI.waitForElementVisible(findTestObject('2. Folders list/Modal/Modal label'), 5)
		assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal label'), "Create New Report") == true
		assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal 1st position select'), "Folder") == true
		WebUI.click(findTestObject('2. Folders list/Modal/Modal 1st position select'))
		Thread.sleep(500);
		WebUI.click(findTestObject('2. Folders list/Modal/Overlay First option'))
		WebUI.waitForElementVisible(findTestObject('2. Folders list/Modal/Modal 2nd position input'), 2)
		assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal 2nd position label'), "Report Name") == true
		WebUI.setText(findTestObject('2. Folders list/Modal/Modal 2nd position input'), reportName)
		WebUI.setText(findTestObject('2. Folders list/Modal/Modal 3rd position input'), advertiserName)
		assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal 3rd position label'), "Advertiser") == true
		assert WebUI.verifyElementPresent(findTestObject('2. Folders list/Modal/Cancel button'), 2) == true
		WebUI.click(findTestObject('2. Folders list/Modal/Basic Report Save button'))
		WebUI.delay(2)
		assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/3. Report/Progress bar'), 10) == true
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/4. Subreports/No datasource yet label'), 5)
	}

	protected void createReportFromModel(String modelName, String reportName, String advertiserName) {
		/**
		 * Creates a report from a model
		 *
		 * @author fbalhier
		 *
		 * @param modelName				Name of the model to create the report from
		 * @param reportName			Name of the report to create
		 * @param advertiserName		Name of the advertiser that will automatically be used as a prefix in the name of the report
		 */
		
		WebUI.click(findTestObject('Object Repository/2. Folders list/New Report button'))
		String currentModelName = WebUI.getText(findTestObject('Object Repository/2. Folders list/First model button'))
		int i = 1
		WebUI.delay(2)
		String numberOfModelsSelector = findTestObject('Object Repository/2. Folders list/First model button').getSelectorCollection().get(SelectorMethod.CSS).replaceFirst(":nth-child\\(\\d\\)", "")
		WebUI.delay(2)
		int numberOfModels = selenium.getCssCount(numberOfModelsSelector)
		WebUI.delay(2)
		while(i < numberOfModels && !currentModelName.replaceAll("\\n\\?", "").equals(modelName.replaceAll("\u00A0", " "))) {
			i++
			TestObject currentModel = changeSelector(findTestObject('Object Repository/2. Folders list/First model button'), "nth-child\\(\\d\\)", "nth-child(" + i + ")")
			WebUI.focus(currentModel)
			currentModelName = WebUI.getText(currentModel)
		}
		if( i != numberOfModels) {
			WebUI.click(changeSelector(findTestObject('Object Repository/2. Folders list/First model button'), "nth-child\\(\\d\\)", "nth-child(" + i + ") > span > button"))

			WebUI.waitForElementVisible(findTestObject('2. Folders list/Modal/Modal label'), 5)
			assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal label'), "Create New Report\n" + modelName.replaceAll("\u00A0", " ")) == true
			assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal 1st position select'), "Folder") == true
			WebUI.click(findTestObject('2. Folders list/Modal/Modal 1st position select'))
			Thread.sleep(500);
			WebUI.click(findTestObject('2. Folders list/Modal/Overlay First option'))
			WebUI.waitForElementVisible(findTestObject('2. Folders list/Modal/Modal 2nd position input'), 30)
			assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal 2nd position label'), "Report Name") == true
			WebUI.setText(findTestObject('2. Folders list/Modal/Modal 2nd position input'), reportName)
			WebUI.setText(findTestObject('2. Folders list/Modal/Modal 3rd position input'), advertiserName)
			assert WebUI.verifyElementText(findTestObject('2. Folders list/Modal/Modal 3rd position label'), "Advertiser") == true
			assert WebUI.verifyElementPresent(findTestObject('2. Folders list/Modal/Cancel button'), 2) == true
			String buttonLabel = WebUI.getText(findTestObject('2. Folders list/Modal/Basic Report Save button'))
			WebUI.click(findTestObject('2. Folders list/Modal/Basic Report Save button')) // Same selector as the Next button
			if(buttonLabel.equals("Save"))
			{
				WebUI.delay(2)
				assert WebUI.verifyElementNotPresent(findTestObject('Object Repository/3. Report/Progress bar'), 10) == true
			}
		} else {
			WebUI.executeJavaScript("alert(\"Model not found\")", null)
		}
	}

	protected String createReportFromModelWithCustomParams(String modelName, String reportName, String advertiserName, String[] customParamsLabels, String customParamsValue) {
		/**
		 * Creates a report from a model with custom params
		 *
		 * @author fbalhier
		 *
		 * @param modelName				Name of the model to create the report from
		 * @param reportName			Name of the report to create
		 * @param advertiserName		Name of the advertiser that will automatically be used as a prefix in the name of the report
		 * @param customParamsLabels	List of the custom param labels to check
		 * @param customParamsValue		Value of every custom param. Value will be incremented with " X" for every label
		 */
		
		createReportFromModel(modelName, reportName, advertiserName)

		WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Modal/Custom param caution message'), 30)

		assert WebUI.getText(findTestObject('Object Repository/2. Folders list/Modal/Custom param caution message')).equals("CAUTION : Once the report is created you can not change these values !\nWill be possible soon in an other version") == true

		int i = 2
		int j = 1
		for(customParamLabel in customParamsLabels) {
			assert WebUI.verifyElementText(changeSelector(findTestObject('Object Repository/2. Folders list/Modal/First custom param label'), 'nth-child\\(\\d\\)', 'nth-child(' + i + ')'), customParamLabel) == true
			if(customParamsValue.equals("") == false) {
				WebUI.setText(changeSelector(findTestObject('Object Repository/2. Folders list/Modal/First custom param input'), 'nth-child\\(\\d\\)', 'nth-child(' + i + ')'), customParamsValue + " " + j)
			}
			i++
			j++
		}

		WebUI.click(findTestObject('Object Repository/2. Folders list/Modal/Report from a model Save button'))
		WebUI.delay(2)
		WebUI.waitForElementNotPresent(findTestObject('Object Repository/3. Report/Progress bar'), 5)
	}

	protected void deleteModel(String folderName, String modelName) {
		/**
		 * Deletes a model
		 *
		 * @author fbalhier
		 *
		 * @param folderName			Name of the folder containing the file to delete
		 * @param modelName				Name of the item to delete
		 */
		
		deleteItem(folderName, modelName, "insert_drive_file")
	}

	protected void deleteReport(String folderName, String reportName) {
		/**
		 * Deletes a report
		 *
		 * @author fbalhier
		 *
		 * @param folderName			Name of the folder containing the file to delete
		 * @param modelName				Name of the item to delete
		 */
		
		deleteItem(folderName, reportName, "description")
	}

	private void deleteItem(String folderName, String itemName, String reportType) {
		/**
		 * Method used to delete a report or model
		 *
		 * @author fbalhier
		 *
		 * @param folderName			Name of the folder containing the file to delete
		 * @param itemName				Name of the item to delete
		 * @param reportType			Type of the item to delete : "description" for a report, "insert_drive_file" for a model
		 */
		
		int i = 3
		boolean isItemPresent = false
		String currentItemType = ""
		int numberOfFolders = selenium.getCssCount("folders-list > folder-element").intValue()
		int numberOfItems = 0

		TestObject currentFolderButton = findTestObject('2. Folders list/Open first folder button')
		TestObject currentFolder = findTestObject('2. Folders list/First folder name')
		TestObject currentItem = findTestObject('2. Folders list/First report name')
		String currentItemName = ""
		String currentFolderName = ""

		while(numberOfFolders > 0 && (currentItemType != reportType || isItemPresent == false || !currentItemName.equals(itemName))) {
			currentFolder = changeSelector(currentFolder, "child\\(\\d+\\)", "child(" + i + ")")
			currentFolderName = WebUI.getText(currentFolder)
			if(currentFolderName.equals(folderName)) {
				currentFolderButton = changeSelector(currentFolderButton, "child\\(\\d+\\)", "child(" + i + ")")
				WebUI.scrollToElement(currentFolderButton, 10)
				WebUI.click(currentFolderButton)
				Thread.sleep(500);
				numberOfItems = selenium.getCssCount("div.folder-content.open > div.file").intValue()

				while (numberOfItems > 0 && !currentItemName.equals(itemName)) {
					isItemPresent = selenium.isElementPresent("css=div.folder-content.open > div:nth-child(" + numberOfItems + ") > span > a")
					if (isItemPresent == true) {
						currentItemType = selenium.getText("css=div.folder-content.open > div:nth-child(" + numberOfItems + ") > span > a > i")
						currentItem = changeSelector(currentItem, "child\\(\\d+\\)", "child(" + numberOfItems + ")")
						currentItemName = WebUI.getText(currentItem).replaceFirst(reportType + " ", "")
						if (currentItemName.equals(itemName)) {
							WebUI.scrollToElement(currentItem, 10)
							WebUI.click(currentItem)
							WebUI.delay(2)
							WebUI.waitForElementVisible(findTestObject('3. Report/9. Delete Report/Delete Report button'), 30)
							WebUI.click(findTestObject('3. Report/9. Delete Report/Delete Report button'))
							WebUI.click(findTestObject('Object Repository/3. Report/9. Delete Report/Delete Report Confirm button'))
							WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
							assert WebUI.verifyEqual("Model successfully deleted", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
							WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
						}
					}
					numberOfItems--
				}
				WebUI.scrollToElement(currentFolderButton, 10)
				WebUI.click(currentFolderButton)

			}
			numberOfFolders--
			i++
		}
	}

	protected void generateXlsReport(String subreportName, int datasource, String frequency, String templateFormat, String templateName, String xlsSheetNumber, String csvFullPath) {
		/**
		 * Generates an XLS report with the default CSV settings (must be inside a report)
		 *
		 * @author fbalhier
		 *
		 * @param subreportName			Name of the subreport to create
		 * @param datasource			Chosen datasource for the creation of the subreport (position in the list of datasources)
		 * @param frequency				Schedule frequency : "daily", "weekly", "every two weeks", "monthly"
		 * @param templateFormat		Extension of the template to upload : ".xlsx" / ".xlsm"
		 * @param templateName			Name of the template to upload. The file must exist.
		 * @param xlsSheetNumber		Sheet number to set in the XLS Settings
		 * @param csvFullPath			Full path of the csv to upload manually
		 */
		
		createSubreport(subreportName, datasource)
		setupScheduleToday(frequency)
		uploadReportTemplate(templateFormat, templateName)
		setupReportXlsSettings(xlsSheetNumber)
		manualUpload(csvFullPath)
	}

	protected void createSubreport(String subreportName, int datasource) {
		/**
		 * Creates a subreport
		 *
		 * @author fbalhier
		 * 
		 * @param subreportName			Name of the subreport to create
		 * @param datasource			Chosen datasource for the creation of the subreport (position in the list of datasources)
		 */
		
		String regex = "nth-child\\(\\d+\\)"
		WebUI.waitForElementPresent(selectorToTestObject("subreport > div > header"), 30)
		int i = selenium.getCssCount("subreport > div > header > tab-sheet")
		i++
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/4. Subreports/New Subreport button'), 30)
		WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/New Subreport button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/4. Subreports/Datasources/Datasource ' + datasource), 30)
		WebUI.click(findTestObject('3. Report/4. Subreports/Datasources/Datasource ' + datasource))
		WebUI.waitForElementPresent(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport button'), regex, 'nth-child('+ i +')'), 5)
		WebUI.delay(1)
		int j = 5
		while(j > 0 && WebUI.getText(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Subreport name'), regex, 'nth-child('+ i +')')).equals(subreportName) == false) {
			WebUI.click(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport button'), regex, 'nth-child('+ i +')'))
			WebUI.delay(1)
			WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport input'), regex, 'nth-child('+ i +')'), subreportName)
			WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport input'), regex, 'nth-child('+ i +')'), Keys.chord(Keys.ENTER))
			j--
		}
		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("Modification successfully saved", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyElementText(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Subreport name'), regex, 'nth-child('+ i +')'), subreportName) == true
	}

	protected void createAllSubreports(String[] subreportNames) {
		/**
		 * Creates one subreport per existing datasource
		 *
		 * @author fbalhier
		 *
		 * @param subreportNames		List of the names of the subreports to create
		 */
		
		String regex = "nth-child\\(\\d+\\)"
		int i = 1
		int j = selenium.getCssCount("subreport > div > header > tab-sheet") + 1
		for(subreportName in subreportNames) {
			WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/4. Subreports/New Subreport button'), 30)
			WebUI.click(findTestObject('Object Repository/3. Report/4. Subreports/New Subreport button'))
			TestObject currentDatasource = findTestObject('3. Report/4. Subreports/Datasources/Datasource ' + i)
			WebUI.waitForElementVisible(currentDatasource, 30)
			WebUI.click(currentDatasource)
			TestObject renameSubreportButton = changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport button'), regex, 'nth-child(' + j + ')')
			WebUI.waitForElementPresent(renameSubreportButton, 30)
			int k = 5
			while(k > 0 && WebUI.getText(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Subreport name'), regex, 'nth-child('+ i +')')).equals(subreportName) == false) {
				WebUI.click(renameSubreportButton)
				WebUI.delay(1)
				WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport input'), regex, 'nth-child(' + j + ')'), subreportName)
				WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Rename Subreport input'), regex, 'nth-child(' + j + ')'), Keys.chord(Keys.ENTER))
				k--
			}
			WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
			assert WebUI.verifyEqual("Modification successfully saved", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
			TestObject currentSubreportName = changeSelector(findTestObject('Object Repository/3. Report/4. Subreports/Subreport name'), regex, 'nth-child(' + j + ')')
			WebUI.verifyElementText(currentSubreportName, subreportName)
			WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
			i++
			j++
		}
	}

	protected void setupScheduleToday(String frequency) {
		/**
		 * Creates a schedule on current date only
		 *
		 * @author fbalhier
		 *
		 * @param frequency				Schedule frequency : "daily", "weekly", "every two weeks", "monthly"
		 */
		
		WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Schedule button'))
		WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Frequency select'))
		switch (frequency.toLowerCase()) {
			case 'daily':
				WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Frequency Daily'))
				break
			case 'weekly':
				WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Frequency Weekly'))
				break
			case 'monthly':
				WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Frequency Monthly'))
				break
			case 'every two weeks':
				WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Frequency Every 2 Weeks'))
				break
			case 'every 2 weeks':
				WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Frequency Every 2 Weeks'))
				break
			default:
				println "Frequency en paramètre invalide"
				break
		}

		boolean isDateSelectorPresent = selenium.isElementPresent("css=" + findTestObject('Object Repository/3. Report/1. Schedule/Date Selector button').getSelectorCollection().get(SelectorMethod.CSS))
		if (isDateSelectorPresent == true) {
			WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Date Selector button'))
			WebUI.delay(1)
			WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Today cell'))
			WebUI.delay(1)
			WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Today cell'))
			WebUI.delay(1)
		}

		boolean isDaySelectorPresent = selenium.isElementPresent("css=" + findTestObject('3. Report/1. Schedule/Day select').getSelectorCollection().get(SelectorMethod.CSS))
		if (isDaySelectorPresent == true) {
			WebUI.click(findTestObject('Object Repository/3. Report/1. Schedule/Day select'))
			WebUI.waitForElementVisible(findTestObject('Object Repository/2. Folders list/Modal/Overlay First option'), 30)
			WebUI.click(findTestObject('Object Repository/2. Folders list/Modal/Overlay First option'))
		}

		WebUI.click(findTestObject('2. Folders list/Modal/Basic Report Save button'))
		if (isDateSelectorPresent == true) {
			WebUI.waitForElementNotPresent(findTestObject('Object Repository/3. Report/8. Iterations/Not report yet label'), 30)
		}
		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("Scheduling settings updated", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
	}

	protected void changeScheduleDate(String nthDateFromToday) {
		/**
		 * Change the existing schedule date
		 *
		 * @author fbalhier
		 *
		 * @param nthDateFromToday		Offset from current day, to set the new schedule date
		 */
		
		WebUI.waitForElementVisible(selectorToTestObject("div.option.schedule > label > i"), 30)
		selenium.click("css=div.option.schedule > label > i")
		WebUI.waitForElementNotPresent(selectorToTestObject("div.option.schedule > button:nth-of-type(2)"), 30)
		selenium.click("css=document-setup > mat-card > mat-card-content > div.option.schedule > button")
		WebUI.waitForElementPresent(selectorToTestObject("sat-datepicker-toggle > button"), 30)
		selenium.click("css=sat-datepicker-toggle > button")
		Date date = new Date()
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date)
		String tomorrow = String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH) + Integer.valueOf(nthDateFromToday))
		String tomorrowNumeric = calendar.get(Calendar.YEAR).toString() + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH) + Integer.valueOf(nthDateFromToday))
		WebUI.waitForElementVisible(selectorToTestObject("[aria-label*=\"" + tomorrow + "\"]"), 30)
		Thread.sleep(1000)
		selenium.click("css=[aria-label*=\"" + tomorrow + "\"]")
		WebUI.waitForElementVisible(selectorToTestObject("[aria-label*=\"" + tomorrow + "\"]"), 30)
		selenium.click("css=[aria-label*=\"" + tomorrow + "\"]")
		WebUI.waitForElementVisible(selectorToTestObject("document-scheduler > div.mat-dialog-actions > button.mat-raised-button.mat-accent"), 30)
		selenium.click("css=document-scheduler > div.mat-dialog-actions > button.mat-raised-button.mat-accent")
		WebUI.waitForElementVisible(xpathSelectorToTestObject("//*[text()='Report " + tomorrowNumeric + "']"), 30)
	}

	protected String addRecipients(List<String> recipientsToAdd) {
		/**
		 * Add recipients to the report
		 *
		 * @author fbalhier
		 *
		 * @param recipientsToAdd		List of the recipients to add (email addresses)
		 */
		
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/2. Recipients/Recipients button'), 30)
		String previousRecipientsLabel = WebUI.getText(findTestObject('Object Repository/3. Report/2. Recipients/Recipients button'))
		String endOfLabel = " registered recipients"
		int previousNumberOfRecipients = Integer.valueOf(previousRecipientsLabel.replaceFirst(endOfLabel, ""))

		WebUI.click(findTestObject('Object Repository/3. Report/2. Recipients/Recipients button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/2. Recipients/Recipients input'), 30)

		assert WebUI.getText(findTestObject('Object Repository/3. Report/2. Recipients/Recipients title')).equals("Manage your recipients") == true
		assert WebUI.getText(findTestObject('Object Repository/3. Report/2. Recipients/Recipients sub-title')).equals("Fill with email adresses separate by a comma") == true
		assert WebUI.getText(findTestObject('Object Repository/3. Report/2. Recipients/Default recipients label')).equals("Recipient registered by default : " + GlobalVariable.username) == true

		for(recipientToAdd in recipientsToAdd) {
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/2. Recipients/Recipients input'), recipientToAdd)
			WebUI.sendKeys(findTestObject('Object Repository/3. Report/2. Recipients/Recipients input'), Keys.chord(Keys.ENTER))
		}

		WebUI.delay(2)
		String newRecipientsLabel = WebUI.getText(findTestObject('Object Repository/3. Report/2. Recipients/Recipients button'))
		int newNumberOfRecipients = recipientsToAdd.size() + previousNumberOfRecipients
		assert newRecipientsLabel.equals(newNumberOfRecipients.toString() + endOfLabel) == true
		WebUI.delay(1)
		WebUI.focus(findTestObject('Object Repository/3. Report/2. Recipients/Recipients button'))
		WebUI.delay(1)
	}

	protected String uploadModelTemplate(String templateFormat, String templateName) {
		/**
		 * Uploads a template for models only
		 *
		 * @author fbalhier
		 *
		 * @param templateFormat		Extension of the template to upload : ".xlsx" / ".xlsm"
		 * @param templateName			Name of the template to upload. The file must exist.
		 */
		
		String templateFullPath = csvAndTemplateFolderPath + templateName + "." + templateFormat.toLowerCase()
		String toast = ""
		WebUI.waitForElementPresent(changeSelector(findTestObject('Object Repository/3. Report/3. XLS Settings/Upload Template input'), "nth-child\\(\\d+\\)", "nth-child(2)"), 30)
		WebUI.uploadFile(changeSelector(findTestObject('Object Repository/3. Report/3. XLS Settings/Upload Template input'), "nth-child\\(\\d+\\)", "nth-child(2)"), templateFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/Template Toast message'), 5)
		toast = WebUI.getText(findTestObject('3. Report/Toast message'))
		// assert WebUI.verifyEqual(templateName + "." + templateFormat.toLowerCase() +" has been successfully uploaded.", toast) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Template Toast message'), 5)

		return toast
	}

	protected String uploadReportTemplate(String templateFormat, String templateName) {
		/**
		 * Uploads a template for reports only
		 *
		 * @author fbalhier
		 *
		 * @param templateFormat		Extension of the template to upload : ".xlsx" / ".xlsm"
		 * @param templateName			Name of the template to upload. The file must exist.
		 */
		
		WebUI.refresh()
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementPresent(findTestObject('Object Repository/3. Report/3. XLS Settings/Upload Template input'), 30)
		String templateFullPath = csvAndTemplateFolderPath + templateName + "." + templateFormat.toLowerCase()
		String toast = ""
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/3. XLS Settings/Upload Template input'), templateFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/Template Toast message'), 5)
		toast = WebUI.getText(findTestObject('3. Report/Toast message'))
		//assert WebUI.verifyEqual("Template." + templateFormat.toLowerCase() +" has been successfully uploaded.", toast) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Template Toast message'), 5)

		return toast
	}

	protected void setupModelXlsSettings(String xlsSheetNumber) {
		/**
		 * Setup XLS Settings for models only
		 *
		 * @author fbalhier
		 *
		 * @param xlsSheetNumber		Sheet number to set
		 */
		
		WebUI.click(changeSelector(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'), "child\\(\\d+\\)", "child(2)"))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), 5)
		WebUI.setText(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), xlsSheetNumber)
		WebUI.sendKeys(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), Keys.chord(Keys.ENTER))
		WebUI.click(findTestObject('2. Folders list/Modal/Basic Report Save button'))
		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("Settings updated sucessfully", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
	}

	protected void setupReportXlsSettings(String xlsSheetNumber) {
		/**
		 * Setup XLS Settings for reports only
		 *
		 * @author fbalhier
		 *
		 * @param xlsSheetNumber		Sheet number to set
		 */
		
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/XLS Settings button'))
		int numberOfSubreports = selenium.getCssCount("section.display-sheets tbody > tr > td.mat-column-xls_sheet_index > input")
		int i = 1
		int currentSheetNumber = Integer.valueOf(xlsSheetNumber)
		while(i <= numberOfSubreports) {
			WebUI.clearText(changeSelector(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "nth-of-type\\(1\\)", 'nth-of-type('+ i +')'))
			WebUI.delay(1)
			WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "nth-of-type\\(1\\)", 'nth-of-type('+ i +')'), currentSheetNumber.toString())
			WebUI.sendKeys(changeSelector(findTestObject('Object Repository/3. Report/3. XLS Settings/First XLS sheet number'), "nth-of-type\\(1\\)", 'nth-of-type('+ i +')'), Keys.chord(Keys.ENTER))
			i++
			currentSheetNumber++
		}
		WebUI.click(findTestObject('Object Repository/3. Report/3. XLS Settings/Save button'))
		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("Settings updated sucessfully", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
	}

	protected void setupCsvSettings(String startFromRow, String excludeLast, String separator, boolean cumulative) {
		/**
		 * Setup CSV Settings
		 *
		 * @author fbalhier
		 *
		 * @param startFromRow			"Start from row" value to set
		 * @param excludeLast			"Exclude last" value to set
		 * @param separator				Separator value to set : "," / ";" / "tab"
		 * @param cumulative			"true" to enable and "false" to disable the cumulative option.
		 */
		
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/CSV Settings button'))
		WebUI.setText(findTestObject('Object Repository/3. Report/5. CSV Settings/Start from row input'), startFromRow)
		WebUI.setText(findTestObject('Object Repository/3. Report/5. CSV Settings/Exclude last'), excludeLast)
		WebUI.selectOptionByLabel(findTestObject('Object Repository/3. Report/5. CSV Settings/Separator select'), separator, false)
		if(cumulative == true) {
			WebUI.check(findTestObject('Object Repository/3. Report/5. CSV Settings/Cumulative checkbox'))
		}
		WebUI.click(findTestObject('Object Repository/3. Report/5. CSV Settings/Apply CSV Settings button'))
		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("CSV settings successfully saved", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
	}

	protected void renameReport(String reportName, String advertiserName) {
		/**
		 * Rename a report
		 *
		 * @author fbalhier
		 *
		 * @param reportName			New report name to set
		 * @param advertiserName		New advertiser name to set
		 */
		
		WebUI.click(findTestObject('Object Repository/3. Report/Edit Report Name button'))
		Thread.sleep(500)
		WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), "( " + advertiserName + " ) " + reportName)
		WebUI.sendKeys(findTestObject('Object Repository/3. Report/Edit Report Name input'), Keys.chord(Keys.ENTER))
		assert WebUI.verifyElementText(findTestObject('Object Repository/3. Report/Report Name'), "( " + advertiserName + " ) " + reportName) == true
		WebUI.waitForElementVisible(findTestObject('3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("Modification successfully saved", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
	}

	protected void manualUpload(String csvFullPath) {
		/**
		 * Uploads a CSV to the report using manual upload
		 *
		 * @author fbalhier
		 *
		 * @param csvFullPath			Full path of the csv to upload manually
		 */
		
		WebUI.waitForElementPresent(findTestObject('Object Repository/3. Report/6. CSV Preview/Upload CSV input'), 30)
		Thread.sleep(500)
		WebUI.uploadFile(findTestObject('Object Repository/3. Report/6. CSV Preview/Upload CSV input'), csvFullPath)
		WebUI.waitForElementVisible(findTestObject('3. Report/6. CSV Preview/CSV Preview info'), 30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/Toast message'), 30)
		assert WebUI.verifyEqual("File well uploaded.", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)
	}

	protected String downloadReport(String reportName, String advertiserName, String templateFormat, String currentDate) {
		/**
		 * Download report when it is ready
		 *
		 * @author fbalhier
		 *
		 * @param reportName			Name of the report (included in the downloaded file name)
		 * @param advertiserName		Name of the report's advertiser (included in the downloaded file name)
		 * @param templateFormat		Extension of the report's template (included in the downloaded file name)
		 * @param currentDate			Current date (included in the downloaded file name)
		 */
		
		deleteFileIfExists(downloadFolderPath + "( " + advertiserName + " ) " + reportName + " (" + currentDate + ")"  +  "." + templateFormat)
		int waitForReport = 5000
		int remainingAttempts = 120
		boolean canDownloadReport = selenium.isElementPresent("css="+ findTestObject('Object Repository/3. Report/7. Download report/Active Download button').getSelectorCollection().get(SelectorMethod.CSS))
		while(canDownloadReport == false && remainingAttempts > 0) {
			Thread.sleep(waitForReport)
			selenium.refresh()
			WebUI.waitForPageLoad(30)
			WebUI.click(findTestObject('Object Repository/3. Report/7. Download report/Download button'))
			WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/7. Download report/Overlay button'), 30)
			findTestObject('Object Repository/3. Report/7. Download report/Active Download button')
			canDownloadReport = selenium.isElementPresent("css="+ findTestObject('Object Repository/3. Report/7. Download report/Active Download button').getSelectorCollection().get(SelectorMethod.CSS))
			remainingAttempts = remainingAttempts - 1
		}

		WebUI.click(findTestObject('Object Repository/3. Report/7. Download report/Active Download button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/3. Report/Toast message'), 30)
		String reportFullName
		if(advertiserName != "") {
			reportFullName = "( " + advertiserName + " ) " + reportName + " (" + currentDate + ")"
		} else {
			reportFullName = reportName + " (" + currentDate + ")"
		}
		assert WebUI.verifyEqual(reportFullName + " has been successfully download.", WebUI.getText(findTestObject('3. Report/Toast message'))) == true
		WebUI.waitForElementNotPresent(findTestObject('3. Report/Toast message'), 30)

		return downloadFolderPath + reportFullName +  "." + templateFormat
	}

	protected void checkReportDownloaded (String downloadedReportPath, String sheetNumber, String sheetContent) {
		/**
		 * Check the content of the download report
		 *
		 * @author fbalhier
		 *
		 * @param downloadedReportPath	Name of the report (included in the downloaded file name)
		 * @param sheetNumber			Number of the sheet to check
		 * @param sheetContent			Expected sheet content
		 */
		
		WebUI.navigateToUrl("https://xmlgrid.net/excelViewer.html")
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/4. Tool for checking xls report content/Open file button'), 30)
		WebUI.click(findTestObject('Object Repository/4. Tool for checking xls report content/Open file button'))
		WebUI.waitForElementVisible(findTestObject('Object Repository/4. Tool for checking xls report content/Upload file input'), 30)
		WebUI.uploadFile(findTestObject('Object Repository/4. Tool for checking xls report content/Upload file input'), downloadedReportPath)
		WebUI.waitForElementPresent(findTestObject('Object Repository/4. Tool for checking xls report content/Submit button ready'), 30)
		WebUI.click(findTestObject('Object Repository/4. Tool for checking xls report content/Submit button ready'))
		WebUI.waitForElementPresent(findTestObject('Object Repository/4. Tool for checking xls report content/Sheets/Sheet ' + sheetNumber), 5)
		WebUI.delay(2)
		WebUI.click(findTestObject('Object Repository/4. Tool for checking xls report content/Sheets/Sheet ' + sheetNumber))
		WebUI.waitForElementPresent(findTestObject('Object Repository/4. Tool for checking xls report content/Sheet table'), 5)
		WebUI.delay(1)
		assert WebUI.verifyEqual(WebUI.getAttribute(findTestObject('Object Repository/4. Tool for checking xls report content/Sheet table'), "innerText"), sheetContent) == true
	}

	protected void checkMailReceivedSince (String mailAddress, String mailPassword, String mailTitle, String instantDotNowDotGetEpochSecond) {
		/**
		 * Check that a mail was received since specified time, and check the title is correct
		 *
		 * @author fbalhier
		 *
		 * @param mailAddress						Gmail Address
		 * @param mailPassword						Gmail password
		 * @param mailTitle							Expected mail title
		 * @param instantDotNowDotGetEpochSecond	Epoch date chosen to filter mails
		 */
		
		int remainingAttempts = 60
		WebUI.navigateToUrl("https://mail.google.com/mail/u/0/#inbox")
		WebUI.waitForPageLoad(30)
		WebUI.delay(2)
		boolean loggedInToGoogle = selenium.isElementPresent("css=" + findTestObject('Object Repository/7. Gmail/Check all mails').getSelectorCollection().get(SelectorMethod.CSS))

		if(loggedInToGoogle == false) {
			loginToGoogle(mailAddress, mailPassword)
			WebUI.navigateToUrl("https://mail.google.com/mail/u/0/#inbox")
			WebUI.waitForPageLoad(30)
		}

		WebUI.navigateToUrl("https://mail.google.com/mail/u/0/#search/from%3A(reportfolio%40tradelab.fr)+subject%3A(New+report)+after%3A"+ instantDotNowDotGetEpochSecond)
		WebUI.waitForPageLoad(30)
		WebUI.waitForElementVisible(findTestObject('Object Repository/7. Gmail/Check all mails'), 45)
		String firstMailTitlePresentSelector = findTestObject('7. Gmail/Mail 1 title').getSelectorCollection().get(SelectorMethod.CSS)
		boolean isfirstMailTitlePresent = selenium.isElementPresent("css=" + firstMailTitlePresentSelector)
		String firstMailTitle = ""

		while(isfirstMailTitlePresent == false && remainingAttempts > 0) {
			WebUI.click(findTestObject('Object Repository/7. Gmail/Refresh mails button'))
			Thread.sleep(1500)
			isfirstMailTitlePresent = selenium.isElementPresent("css=" + firstMailTitlePresentSelector)
			remainingAttempts = remainingAttempts - 1
		}

		firstMailTitle = WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))

		assert WebUI.verifyEqual(mailTitle, WebUI.getText(findTestObject('7. Gmail/Mail 1 Title'))) == true
		WebUI.focus(findTestObject('7. Gmail/Mail 1 Title'))
		Thread.sleep(1000)
	}
		
	@Keyword
	public void changeSetupDates(String newSetupDates) {
		/**
		 * Change the date of the current environment via the Commander
		 *
		 * @author fbalhier
		 *
		 * @param newSetupDates			New setup date
		 */
		
		selenium = getSelenium()
		if(GlobalVariable.env.contains("shared") == true) {
			WebUI.navigateToUrl("https://" + GlobalVariable.apiUsername + ":" + GlobalVariable.apiPassword + "@" + GlobalVariable.env + "-api2.tradelab.fr/internals/commander/index.php")
		} else {
			WebUI.navigateToUrl("https://" + GlobalVariable.apiUsername + ":" + GlobalVariable.apiPassword + "@" + GlobalVariable.env + "-reportfolio-api.tradelab.fr/internals/commander/index.php")
		}

		WebUI.waitForPageLoad(30)
		WebUI.waitForElementPresent(selectorToTestObject("body > p > a > button"), 30)
		selenium.click("css=body > p > a > button")
		selenium.waitForPageToLoad("30000")
		String currentTime = selenium.getValue("css=#delta_interval")
		if(currentTime != newSetupDates) {
			WebUI.clearText(selectorToTestObject("#delta_interval"))
			Thread.sleep(1000)
			WebUI.sendKeys(selectorToTestObject("#delta_interval"), newSetupDates)
			WebUI.waitForElementPresent(selectorToTestObject("form > p > input[type=\"submit\"]"), 30)
			selenium.click("css=form > p > input[type=\"submit\"]")
			selenium.waitForPageToLoad("30000")
		}

		WebUI.navigateToUrl(baseUrl)
		WebUI.waitForPageLoad(30)
	}	

	protected String generateCurrentDate() {
		/**
		 * Change current date with format : YYYY-MM-DD
		 *
		 * @author fbalhier
		 */
		
		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance()
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR).toString() + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
	}

	public void renameFile(String filePath, String fileName, String newFileName) {
		/**
		 * Rename a file on your computer
		 *
		 * @author fbalhier
		 * 
		 * @param filePath				Path to the folder containing the file to rename
		 * @param fileName				Name of the file to rename
		 * @param newFileName			New file name
		 */
		
		File file = new File(filePath + fileName)
		File newFile = new File(filePath + newFileName)
		WebUI.delay(1)
		int attempts = 10
		while(file.renameTo(newFile) == false && attempts > 0) {
			WebUI.delay(5)
			attempts--
		}
		assert newFile.exists() == true
		KeywordUtil.markPassed("SDF renamed")
	}

	public void deleteFile(String fileFullPath) {
		/**
		 * Deletes a file on your computer (must exist)
		 *
		 * @author fbalhier
		 * 
		 * @param fileFullPath			Full path to the file to delete
		 */
		
		File file = new File(fileFullPath)
		assert file.delete() == true
		KeywordUtil.markPassed("File deleted : " + file.name)
	}

	public void deleteFileIfExists(String fileFullPath) {
		/**
		 * Deletes a file on your computer only if it exists
		 * If it does not exist, the test will continue
		 *
		 * @author fbalhier
		 * 
		 * @param fileFullPath			Full path to the file to delete
		 */
		
		File file = new File(fileFullPath)
		if(file.exists() == true) {
			assert file.delete() == true
			KeywordUtil.markPassed("File already exist, so we deleted it : " + file.name)
		}
	}

	protected static String getAbsolutePath(String relativePath) {
		/**
		 * Get the absolute path from a relative file path
		 *
		 * @author fbalhier
		 *
		 * @param relativePath		Relative file path
		 */
		
		File file = new File(relativePath)
		return file.getCanonicalPath() + sep
	}
}
