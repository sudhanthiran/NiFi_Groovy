import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

def ff = session.get()
if(ff == null) {
    return;
}


try {

    ff = session.write(ff, {rawIn, rawOut->
        text =IOUtils.toString(rawIn, StandardCharsets.UTF_8)

        mappingFile =mappingFile_att.evaluateAttributeExpressions(ff).value //"D:\\EAI\\Auto Extraction\\Map_DocType.txt;D:\\EAI\\Auto Extraction\\Map_DocType.txt;D:\\EAI\\Auto Extraction\\Map_FiserveEntity.txt;D:\\EAI\\Auto Extraction\\Map_FiserveEntity.txt"
        mappingFields = mappingFields_att.evaluateAttributeExpressions(ff).value //Counterpart_name

        validateAgainstFile =validateAgainstFile_att.evaluateAttributeExpressions(ff).value//"Document_Type_Extracted;Fiserv_Entity_Extracted"
        validateAgainstField=validateAgainstField_att.evaluateAttributeExpressions(ff).value//"Counterparty_Entity_Extracted"

        jsonSlurper = new JsonSlurper()
        data = jsonSlurper.parseText(text)

        validateAgainstFile_List = validateAgainstFile.split(";")
        validateAgainstField_List = validateAgainstField.split(";")

        mappingFile_List = mappingFile.split(";")
        mappingFields_List = mappingFields.split(";")

        validateAgainstFile_List.eachWithIndex{ String entry, int i ->
            data_value = ff.getAttribute(entry)

            if(data_value!="" && data_value!="null" && data_value!=null) {
                prop = new File(mappingFile_List[i].toString()).text
                property = prop.split('~')

                data_value = data_value.replace(" ", "")
                //println(DocumentType)
                int success_flag = 0
                println(data_value)
                for (String s : property) {
                    //list_value = property[j]
                    //println(list_value)
                    //println(list_value.getClass())
                    s = s.replace(" ", "")
                    if (data_value.equalsIgnoreCase(s)) {
                        //println("Success")
                        success_flag = 1
                        if(entry == "Document_Type_Extracted"){
                            data[0].putAt("Document_Type_Mismatch", "null")
                        }
                        if(entry == "Fiserv_Entity_Extracted"){
                            data[0].putAt("Fiserv_Entity_Mismatch", "null")
                        }
                        break
                    }
                }
                //println(DocumentType)
                if (success_flag == 0) {
                    if(entry == "Document_Type_Extracted"){
                        data[0].putAt("Document_Type_Mismatch", 17855)
                    }
                    if(entry == "Fiserv_Entity_Extracted"){
                        data[0].putAt("Fiserv_Entity_Mismatch", 17857)
                    }
                }
            }
            else {
                if(entry == "Document_Type_Extracted"){
                    data[0].putAt("Document_Type_Mismatch", 17855)
                }
                if(entry == "Fiserv_Entity_Extracted"){
                    data[0].putAt("Fiserv_Entity_Mismatch", 17857)
                }
            }
        }
        validateAgainstField_List.eachWithIndex{ String entry, int i ->
            data_value = ff.getAttribute(entry)
            if(data_value!="" && data_value!="null" && data_value!=null) {
                data_value = data_value.replace(" ", "")
                int success_flag = 0
                s = data[0].getAt(mappingFields_List[i])
                s = s.replace(" ", "")
                if (!data_value.equalsIgnoreCase(s)) {
                    data[0].putAt("Counterpart_Entity_Mismatch", 17859)
                }
                else {
                    data[0].putAt("Counterpart_Entity_Mismatch", "null")
                }

            }
            else {
                data[0].putAt("Counterpart_Entity_Mismatch", "null")
            }
        }
        rawOut.withWriter("UTF-8"){ it.write( JsonOutput.toJson(data) )}
    } as StreamCallback)
    session.transfer(ff, REL_SUCCESS)
}
catch (Exception e){
    ff = session.putAttribute(ff, 'Error', e.message)
    session.transfer(ff, REL_FAILURE)
}
