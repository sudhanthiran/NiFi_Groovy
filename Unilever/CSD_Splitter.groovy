import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


csdIds ='[107197, 107199, 107200]'

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

println(JsonOutput.toJson(outgoing_data))