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
        Business_rules =BR_Attributes.evaluateAttributeExpressions(ff).value //"Document_Type;FISV_Agreement_Type;Fiserv_Entity_name;FISV_Contracting_Party"
        mappingFile =BR_Prop_Attributes.evaluateAttributeExpressions(ff).value //"D:\\EAI\\Auto Extraction\\Map_DocType.txt;D:\\EAI\\Auto Extraction\\Map_DocType.txt;D:\\EAI\\Auto Extraction\\Map_FiserveEntity.txt;D:\\EAI\\Auto Extraction\\Map_FiserveEntity.txt"

        jsonSlurper = new JsonSlurper()
        data = jsonSlurper.parseText(text)

        Business_rules_List = Business_rules.split(";")
        mappingFile_List = mappingFile.split(";")

        Business_rules_List.eachWithIndex{ String entry, int i ->
            prop = new File(mappingFile_List[i].toString()).text
            property = prop.split('~')


            DocumentType = data[0].getAt(entry).toString()
            DocumentType = DocumentType.replace(" ","")
            int success_flag = 0
            for(String s : property)
            {
                s = s.replace(" ","")
                if (DocumentType.equalsIgnoreCase(s))
                {
                    success_flag = 1
                    break
                }
            }
            if(success_flag == 0)
            {
                data[0].putAt(entry,"")
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
