import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets


try {
def ff = session.get()
if(!ff) return
ff = session.write(ff, {rawIn, rawOut->

jsonSlurper = new JsonSlurper()
String text = IOUtils.toString(rawIn, StandardCharsets.UTF_8)
def json = jsonSlurper.parseText(text)
data = json.getAt("items")
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
    json.putAt("items",data)

    rawOut.withWriter("UTF-8"){ it.write( JsonOutput.toJson(json) )}
} as StreamCallback)
    session.transfer(ff, REL_SUCCESS)
}

catch (Exception e){
    ff = session.putAttribute(ff, 'Error', e.message)
    session.transfer(ff, REL_FAILURE)
}