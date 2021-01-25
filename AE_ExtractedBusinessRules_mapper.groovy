import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

jsonSlurper = new JsonSlurper()
text =new File("D:\\EAI\\Auto Extraction\\input.json").text
data = jsonSlurper.parseText(text)

data_attributes=[:]
data_attributes["Document_Type_Extracted"]="ACCELNetworkMembershipAgreement"
data_attributes["Fiserv_Entity_Extracted"]="Accurate Software Inc."
data_attributes["Counterparty_Entity_Extracted"]="1st United Bank"

validateAgainstFile ="Document_Type_Extracted;Fiserv_Entity_Extracted"

validateAgainstField="Counterparty_Entity_Extracted"

mappingFile ="D:\\EAI\\Auto Extraction\\Map_DocType.txt;D:\\EAI\\Auto Extraction\\Map_FiserveEntity.txt"
mappingFields ="Counterpart_name"

validateAgainstFile_List = validateAgainstFile.split(";")
validateAgainstField_List = validateAgainstField.split(";")
mappingFile_List = mappingFile.split(";")
mappingFields_List = mappingFields.split(";")

validateAgainstFile_List.eachWithIndex{ String entry, int i ->

    if(data_attributes[entry]!="" && data_attributes[entry]!="null") {
        prop = new File(mappingFile_List[i].toString()).text
        property = prop.split('~')


        data_value = data_attributes[entry].toString()
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
    if(data_attributes[entry]!="" && data_attributes[entry]!="null") {
        data_value = data_attributes[entry].toString()
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

println(data)
