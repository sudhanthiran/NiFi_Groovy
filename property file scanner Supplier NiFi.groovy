import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

flowFile = session.get()
if(!flowFile) return
try {
    key = SearchKey.evaluateAttributeExpressions(flowFile).value;
    propertiesFileloc = propertyFile.evaluateAttributeExpressions(flowFile).value;

    Properties properties = new Properties()
    File propertiesFile = new File(propertiesFileloc)
    if (propertiesFile.exists()) {
        propertiesFile.withInputStream {
            properties.load(it)
        }
    }
    def dbid=  properties.get(key)
    if(dbid !=null){
        Supplier_Entity_ID = dbid.toString().split(";")
        flowFile = session.putAttribute(flowFile, "Supplier_DB_ID", Supplier_Entity_ID[0])
        flowFile = session.putAttribute(flowFile, "Supplier_Entity_ID", Supplier_Entity_ID[1])
    }
    //println(dbid)
    else if(dbid == null){
        flowFile = session.putAttribute(flowFile, "Supplier_DB_ID", "null")
        flowFile = session.putAttribute(flowFile, "Supplier_Entity_ID", "null")
    }
    session.transfer(flowFile, REL_SUCCESS)
}
catch (Exception e){
    flowFile = session.putAttribute(flowFile, "Error", e.getMessage())
    session.transfer(flowFile, REL_FAILURE)
}