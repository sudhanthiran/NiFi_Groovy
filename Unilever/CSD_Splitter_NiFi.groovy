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

        csdIds =csdIds.evaluateAttributeExpressions(ff).value

        //csd_list = csdIds.tokenize(',[]')
        csd_list = new JsonSlurper().parseText(csdIds)

        def test_list = []
        def outgoing_data =[:]

        csd_list.eachWithIndex { entry,index ->
            temp_map =[:]
            temp_map.put("CSD_DB_ID",entry)
            //dynaminMetaDataList.add(temp_map)
            test_list.add(temp_map)

        }
        if(test_list.size()> 0)
        {
            outgoing_data.putAt("dynamicDataList",test_list)
        }



        rawOut.withWriter("UTF-8"){ it.write( JsonOutput.toJson(outgoing_data) )}
    } as StreamCallback)
    session.transfer(ff, REL_SUCCESS)
}
catch (Exception e){
    ff = session.putAttribute(ff, 'Error', e.message)
    session.transfer(ff, REL_FAILURE)
}
