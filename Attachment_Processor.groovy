import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


//try {
    attachment_Count =3

    Map dataList=[:]


//    dataList["CR_Value"]= "0 - 5% (5% included)"
//    dataList["COA"] = "Sometimes (50)"



    JsonSlurper slurper = new JsonSlurper()
    Map jsonObject


    propertiesFileloc = "D:\\EAI\\Ariba\\Document Upload\\Attachment.txt"

    Properties properties = new Properties()
    File propertiesFile = new File(propertiesFileloc)
    if (propertiesFile.exists()) {
        propertiesFile.withInputStream {
           // properties.load(it)
            jsonObject = slurper.parse(it)
        }
    }
    while (attachment_Count > 0)
    {

    }
//println(jsonObject.get("CR_Value"))
//}
//catch (Exception e){
//    print(e)
//}