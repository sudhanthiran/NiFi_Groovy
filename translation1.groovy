import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


//try {
    attribute = "CR_Volume;CR_Value;COA"
    attributeList = attribute.split(";")

    Map dataList=[:]

    dataList["CR_Volume"]= "0 (100)"
    dataList["CR_Value"]= "0 - 5% (5% included)"
    dataList["COA"] = "Sometimes (50)"



    JsonSlurper slurper = new JsonSlurper()
    Map jsonObject


    propertiesFileloc = "D:\\EAI\\Saint Gobain\\value_Translation - Copy.txt"
String fileContents = new File(propertiesFileloc).text
jsonObject = slurper.parseText(fileContents)

//    Properties properties = new Properties()
//    File propertiesFile = new File(propertiesFileloc)
//    if (propertiesFile.exists()) {
//        propertiesFile.withInputStream {
//           // properties.load(it)
//            jsonObject = slurper.parse(it)
//        }
//    }
    for (i in attributeList)
    {
        options = jsonObject.get(i)
        values = dataList[i]
        int numSelections = 0
        selectedValues = []
        options.each{ option ->
                if(option.feedValue.equalsIgnoreCase(values)){
                    selectedValues.push(option.sirionValue)
                    numSelections++
                }
        }
        println(selectedValues)
    }
//println(jsonObject.get("CR_Value"))
//}
//catch (Exception e){
//    print(e)
//}