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
    Flag=Flag.evaluateAttributeExpressions(flowFile).value;

    Properties properties = new Properties()
    File propertiesFile = new File(propertiesFileloc)
    if (propertiesFile.exists()) {
        propertiesFile.withInputStream {
            properties.load(it)
        }
    }
    def dbid=  properties.get(key)
    if(dbid !=null){
        Contract_Entity = dbid.toString().split(";")

        if(Flag=='WO'){Folder_DB_ID = Contract_Entity[3]}
        else if(Flag=='AMD'){Folder_DB_ID = Contract_Entity[4]}
        else if(Flag=='WA'){Folder_DB_ID = Contract_Entity[5]}
        else {Folder_DB_ID = "NotFound"}

        flowFile = session.putAttribute(flowFile, "Contract_DB_ID", Contract_Entity[0])
        flowFile = session.putAttribute(flowFile, "Folder_DB_ID", str(Folder_DB_ID))

    }
    //println(dbid)
    else if(dbid == null){
        flowFile = session.putAttribute(flowFile, "Contract_DB_ID", "NotFound")
        flowFile = session.putAttribute(flowFile, "Folder_DB_ID", "NotFound")
    }
    session.transfer(flowFile, REL_SUCCESS)
}
catch (Exception e){
    flowFile = session.putAttribute(flowFile, "Error", e.getMessage())
    session.transfer(flowFile, REL_FAILURE)
}
