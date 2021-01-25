import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

jsonSlurper = new JsonSlurper()
text =new File("D:\\EAI\\Auto Extraction\\input.json").text
data = jsonSlurper.parseText(text)

Business_rules ="Document_Type;FISV_Agreement_Type;Fiserv_Entity_name;FISV_Contracting_Party"
mappingFile ="D:\\EAI\\Auto Extraction\\Map_DocType.txt;D:\\EAI\\Auto Extraction\\Map_DocType.txt;D:\\EAI\\Auto Extraction\\Map_FiserveEntity.txt;D:\\EAI\\Auto Extraction\\Map_FiserveEntity.txt"

Business_rules_List = Business_rules.split(";")
mappingFile_List = mappingFile.split(";")

Business_rules_List.eachWithIndex{ String entry, int i ->
    prop = new File(mappingFile_List[i].toString()).text
    property = prop.split('~')


    DocumentType = data[0].getAt(entry).toString()
    DocumentType = DocumentType.replace(" ","")
    //println(DocumentType)
    int success_flag = 0
    for(String s : property)
    {
        //list_value = property[j]
        //println(list_value)
        //println(list_value.getClass())
        s = s.replace(" ","")
        if (DocumentType.equalsIgnoreCase(s))
        {
            //println("Success")
            success_flag = 1
            break
        }
    }
    //println(DocumentType)
    if(success_flag == 0)
    {
        data[0].putAt(entry,"")
    }

}

println(data)
