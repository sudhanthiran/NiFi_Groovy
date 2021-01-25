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
    def invoice_id=  properties.get(key)
    if(invoice_id !=null){
        Invoice_Entity_ID = invoice_id.toString().split(";")
        if(Invoice_Entity_ID[2] != "Success")
        {
            map = key
            dbid = "null;null;Processing"
            properties.put(map,dbid)
            properties.store(propertiesFile.newWriter(), null)
            flowFile = session.putAttribute(flowFile, "Invoice_Entity_ID", "null")
            flowFile = session.putAttribute(flowFile, "Invoice_DB_ID", "null")
            flowFile = session.putAttribute(flowFile, "Invoice_Status", Invoice_Entity_ID[2])
        }
        if(Invoice_Entity_ID[2] == "Success"){
        flowFile = session.putAttribute(flowFile, "Invoice_Entity_ID", Invoice_Entity_ID[0])
        flowFile = session.putAttribute(flowFile, "Invoice_DB_ID", Invoice_Entity_ID[1])
        flowFile = session.putAttribute(flowFile, "Invoice_Status", Invoice_Entity_ID[2])
        }
    }
    //println(dbid)
    else if(invoice_id == null){
        map = key
        dbid = "null;null;Processing"
        properties.put(map,dbid)
        properties.store(propertiesFile.newWriter(), null)
        flowFile = session.putAttribute(flowFile, "Invoice_Entity_ID", "null")
        flowFile = session.putAttribute(flowFile, "Invoice_DB_ID", "null")
        flowFile = session.putAttribute(flowFile, "Invoice_Status", "ToBeCreated")
    }
    session.transfer(flowFile, REL_SUCCESS)
}
catch (Exception e){
    flowFile = session.putAttribute(flowFile, "Error", e.getMessage())
    session.transfer(flowFile, REL_FAILURE)
}