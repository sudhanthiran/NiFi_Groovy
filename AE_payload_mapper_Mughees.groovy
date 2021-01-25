import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


text =new File("D:\\EAI\\Auto Extraction\\Mughees\\Mughees_input.json").text
prop = new File("D:\\EAI\\Auto Extraction\\Mughees\\Mapping.json").text

entityID = "20116"
jsonSlurper = new JsonSlurper()
data = jsonSlurper.parseText(text)
property = jsonSlurper.parseText(prop)

entity_list =[]

print(property)

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
def test_list = []

data.eachWithIndex { entry,index ->
    fieldID = property.getAt(entry.key).toString().toInteger()
    fieldValue = data.getAt(entry.key).toString()
    if(fieldValue != "null" && fieldValue !="" && entry.key != "Filename")
    {
        temp_map =[:]
        temp_map.put("fieldId",fieldID)
        temp_map.put("fieldValue",fieldValue)
        //dynaminMetaDataList.add(temp_map)
        test_list.add(temp_map)
    }
    if(test_list.size()> 0)
    {
        outgoing_data.putAt("dynamicDataList",test_list)
    }

}

print(outgoing_data)