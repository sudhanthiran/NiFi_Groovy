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
        Action_Entity_ID = dbid.toString().split(";") //custype ==WO
        flowFile = session.putAttribute(flowFile, "Action_DB_ID", Action_Entity_ID[0])
        flowFile = session.putAttribute(flowFile, "ActionID_Duplicate_Count", Action_Entity_ID[1])
        flowFile = session.putAttribute(flowFile, "ActionID_Current_Status", Action_Entity_ID[2])
        flowFile = session.putAttribute(flowFile, "Action_Entity_ID", Action_Entity_ID[3])
    }
    //println(dbid)
    else if(dbid == null){
        flowFile = session.putAttribute(flowFile, "Action_DB_ID", "null")
        flowFile = session.putAttribute(flowFile, "ActionID_Duplicate_Count", "null")
        flowFile = session.putAttribute(flowFile, "ActionID_Current_Status", "null")
        flowFile = session.putAttribute(flowFile, "Action_Entity_ID", "null")
    }
    session.transfer(flowFile, REL_SUCCESS)
}
catch (Exception e){
    flowFile = session.putAttribute(flowFile, "Error", e.getMessage())
    session.transfer(flowFile, REL_FAILURE)
}
