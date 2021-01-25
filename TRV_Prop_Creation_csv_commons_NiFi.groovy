import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

flowFile = session.get()
if(!flowFile) return
try {

    propertiesFileloc = propertyFile.evaluateAttributeExpressions(flowFile).value;

    Search_Key = SearchKey.evaluateAttributeExpressions(flowFile).value;
    Contract_DB_ID =Contract_DB_ID.evaluateAttributeExpressions(flowFile).value;
    Contract_Entity_ID=Contract_Entity_ID.evaluateAttributeExpressions(flowFile).value;
    DOC_TYPE=DOC_TYPE.evaluateAttributeExpressions(flowFile).value;
    WO_Folder_DB_ID=WO_Folder_DB_ID.evaluateAttributeExpressions(flowFile).value;
    AMD_Folder_DB_ID=AMD_Folder_DB_ID.evaluateAttributeExpressions(flowFile).value;
    WA_Folder_DB_ID=WA_Folder_DB_ID.evaluateAttributeExpressions(flowFile).value;
    Supplier_Name=Supplier_Name.evaluateAttributeExpressions(flowFile).value;


    Properties properties = new Properties()
    File propertiesFile = new File(propertiesFileLoc)
    if (propertiesFile.exists()) {
        propertiesFile.withInputStream {
            properties.load(it)
        }
    }
    properties.putAt(Search_Key,Contract_DB_ID+";"+Contract_Entity_ID+";"+DOC_TYPE+";"+WO_Folder_DB_ID+";"+AMD_Folder_DB_ID+";"+WA_Folder_DB_ID+";"+Supplier_Name)
    properties.store(propertiesFile.newWriter(), null)

    session.transfer(flowFile, REL_SUCCESS)
}
catch (Exception e){
    flowFile = session.putAttribute(flowFile, "Error", e.getMessage())
    session.transfer(flowFile, REL_FAILURE)
}