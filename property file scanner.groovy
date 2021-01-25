import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


try {
    Vendor = ""
    propertiesFileloc = "C:\\Users\\sudhanthiran.b\\Desktop\\Groovy\\fileEntry.properties"

    Properties properties = new Properties()
    File propertiesFile = new File(propertiesFileloc)
    if (propertiesFile.exists()) {
        propertiesFile.withInputStream {
            properties.load(it)
        }
    }
    def dbid=  properties.get(Vendor)
    if(dbid !=null){
        Supplier_Entity_ID = dbid.toString().split(";")
        println(Supplier_Entity_ID[1])
        println(Supplier_Entity_ID[0])
    }
    //println(dbid)
    else if(dbid == null){
        println("Entity ID is empty")
        println("DB ID is empty")
    }
}
catch (Exception e){
print(e.message)
}