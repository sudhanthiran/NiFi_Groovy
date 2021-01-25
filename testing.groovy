import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

File inputStream = new File('C:\\Users\\sudhanthiran.b\\Desktop\\Groovy\\input.json')



def json
Map old_map
Map new_map
dyn = "dyn104347"

jsonSlurper = new JsonSlurper()

jsonFlowFile = jsonSlurper.parse(inputStream)
json1 = jsonFlowFile.getAt("body").getAt("data").getAt("dynamicMetadata").getAt(dyn)
value = json1.getAt("values")


if(value != null) {

    if (!value.toString().startsWith('[') && !value.toString().endsWith(']')) {
        value = '[' + value + ']'
    }
    oldList = jsonSlurper.parseText(value)
}


json = "[\n" +
        "  {\n" +
        "    \"id\": \"25\",\n" +
        "    \"name\": \"TEST\",\n" +
        "    \"date\": \"27.11.2019\",\n" +
        "    \"externalsupplierid\": \"123456\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"id\": \"25\",\n" +
        "    \"name\": \"TEST\",\n" +
        "    \"date\": \"27.11.20\",\n" +
        "    \"externalsupplierid\": \"123456\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"id\": \"25\",\n" +
        "    \"name\": \"TEST\",\n" +
        "    \"date\": \"27.11.201\",\n" +
        "    \"externalsupplierid\": \"123456\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"id\": \"149495\",\n" +
        "    \"name\": \"MOSCOW\"\n" +
        "  }\n" +
        "]"
filterList = jsonSlurper.parseText(json)

//println(oldList)
count = 0
Map map = [:]
if(value != null) {

    for (Object item : oldList) {
        Map mapNew = [:]
        mapNew.put("name", item.getAt("name"))
//            if (item.getAt("date").asBoolean()) {
//                mapNew.put("date", item.getAt("date"))
//            }
        mapNew.put("date", item.getAt("date"))
        mapNew.put("externalsupplierid", item.getAt("externalsupplierid"))
        map.put(item.getAt("id"), mapNew)
        count++
    }
    old_map = map
}
for (Object item : filterList) {
    Map mapNew = [:]
    mapNew.put("name", item.getAt("name"))
    mapNew.put("date", item.getAt("date"))
    mapNew.put("externalsupplierid", item.getAt("externalsupplierid"))
    map.put(item.getAt("id"), mapNew)
    count++
}
new_map = map



list = new ArrayList<>(count)

for (Map.Entry<String, String> entry : map.entrySet()) {


//            if (entry.getValue().getAt("date").asBoolean()) {
//                date = entry.getValue().getAt("date")
//            }
    date = entry.getValue().getAt("date")
    externalsupplierid = entry.getValue().getAt("externalsupplierid")

    id = entry.getKey()
    name = entry.getValue().getAt("name")

//            if (entry.getValue().getAt("date").asBoolean()) {

    StringBuilder map1 = new StringBuilder("""{"id": "$id","name": "$name","date": "$date","externalsupplierid":"$externalsupplierid"}""")
    list.add(map1)
//
//
//            } else {
//
//                StringBuilder map1 = new StringBuilder("""{"id": "$id","name": "$name"}""")
//                list.add(map1)
//
//
//            }
//
//
}



json1.putAt("values", list.toString())



println(json1)