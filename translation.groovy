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
//    dataList["CR_Value"]= "0 - 5% (5% included)"
//    dataList["COA"] = "Sometimes (50)"



    JsonSlurper slurper = new JsonSlurper()
    Map jsonObject


    propertiesFileloc = "D:\\EAI\\Saint Gobain\\value_Translation - Copy.txt"

    Properties properties = new Properties()
    File propertiesFile = new File(propertiesFileloc)
    if (propertiesFile.exists()) {
        propertiesFile.withInputStream {
           // properties.load(it)
            jsonObject = slurper.parse(it)
        }
    }
    for (i in attributeList)
    {
        def fieldName
        fieldList = jsonObject.get(i)
        for (j in fieldList)
        {
            for (k in j)
            {
                fieldName = k.getValue()
                if(dataList[i]==fieldName)
                {
                    println(k)

                }
            }
        }
//
    }
//println(jsonObject.get("CR_Value"))
//}
//catch (Exception e){
//    print(e)
//}