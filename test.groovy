import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


flowFile = session.get()
if (!flowFile) return


try {
    def json
    Map old_map
    Map new_map
    flowFile = session.write(flowFile, { inputStream, outputStream ->


        dyn = dyn_Supplier_Site.evaluateAttributeExpressions(flowFile).value

        jsonSlurper = new JsonSlurper()

        jsonFlowFile = jsonSlurper.parse(inputStream)
        json1 = jsonFlowFile.getAt("body").getAt("data").getAt("dynamicMetadata").getAt(dyn)
        value = json1.getAt("values")

        if (!value.toString().startsWith('[') && !value.toString().endsWith(']'))
        {
            value = '[' + value + ']'
        }

        oldList = jsonSlurper.parseText(value)

        json = Supplier_Site_Merged.evaluateAttributeExpressions(flowFile).value
        filterList = jsonSlurper.parseText(json)


        count = 0
        Map map = [:]
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

            StringBuilder map1 = new StringBuilder("""{"id": "$id","name": "$name","date": "$date"}""")//,"externalsupplierid":"$externalsupplierid"}""")
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


        outputStream.withWriter("UTF-8") { it.write(JsonOutput.toJson(jsonFlowFile)) }
    } as StreamCallback)
    flowFile = session.putAttribute(flowFile, "old Map", old_map.toString())
    flowFile = session.putAttribute(flowFile, "New Map", new_map.toString())
    flowFile = session.putAttribute(flowFile, "list", list.toString())

    session.transfer(flowFile, REL_SUCCESS)

}

catch (Exception e) {
    e.printStackTrace()

    flowFile = session.putAttribute(flowFile, "Error", e.getMessage())
    session.transfer(flowFile, REL_FAILURE)
}
