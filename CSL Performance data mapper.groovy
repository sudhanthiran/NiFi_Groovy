import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


try {
    propertiesFileloc = "D:\\EAI\\Vodafone\\Reports\\CSL translation input.txt"
    JsonSlurper slurper = new JsonSlurper()
    Map jsonObject
    String fileContents = new File(propertiesFileloc).text
    //jsonObject = slurper.parseText(fileContents)
    jsonObject = slurper.parseText(fileContents)
    //println(fileContents)
    data = jsonObject.getAt("items")


   data.each { field ->
       String ID = field.'ID'
       ID1 = (List)ID.split(':')
       if (ID1[0].startsWith(';'))
       {
           ID = ID1[1]
       }
       else {
           ID = ID1[0]
       }
       field.'ID' = ID

       String service_date = field.'SERVICE DATE'
       service_date = service_date.substring(6,service_date.length())+'-'+service_date.substring(3,5)+'-'+service_date.substring(0,2)
       field.'SERVICE DATE' = service_date

       String reporting_date = field.'REPORTING DATE'
       reporting_date = reporting_date.substring(6,reporting_date.length())+'-'+reporting_date.substring(3,5)+'-'+reporting_date.substring(0,2)
       field.'REPORTING DATE' = reporting_date

   }
    jsonObject.putAt("items",data)

}
catch (Exception e){
    print(e.message)
}
