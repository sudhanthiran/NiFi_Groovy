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
        prop = new File(PropFile.evaluateAttributeExpressions(ff).value).text
        entityID = EntityID.evaluateAttributeExpressions(ff).value

        jsonSlurper = new JsonSlurper()

        data = jsonSlurper.parseText(text)
        property = jsonSlurper.parseText(prop)


        entity_list =[]

        outgoing_data =[:]
        outgoing_data.putAt("entityTypeId",316)
        //outgoing_data.putAt("entityIds",entityID.split(";"))

        for (i in entityID.split(";"))
        {
            a = i.toString()
            entity_list.add(a.toInteger())
        }

        outgoing_data.putAt("entityIds",entity_list)
        dynaminMetaDataList =[]


        data[0].eachWithIndex { entry,index ->
            fieldID = property.getAt(entry.key).toString().toInteger()
            fieldValue = data[0].getAt(entry.key).toString()
            if(fieldValue != "null" && fieldValue !="" && entry.key != "Filename")
            {
                temp_map =[:]
                temp_map.put("fieldId",fieldID)
                temp_map.put("fieldValue",fieldValue)
                dynaminMetaDataList.add(temp_map)
            }
            if(dynaminMetaDataList.size()> 0)
            {
                outgoing_data.putAt("dynamicDataList",dynaminMetaDataList)
            }

        }

        //print(data[0])
        rawOut.withWriter("UTF-8"){ it.write( JsonOutput.toJson(outgoing_data) )}
    } as StreamCallback)
    session.transfer(ff, REL_SUCCESS)
}
catch (Exception e){
    ff = session.putAttribute(ff, 'Error', e.message)
    session.transfer(ff, REL_FAILURE)
}