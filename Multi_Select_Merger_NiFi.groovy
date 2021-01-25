import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


flowFile = session.get()
if (!flowFile) return


try {


    Map ErrorHandling=[:]
    flowFile = session.write(flowFile, { inputStream, outputStream ->
        // item = fieldName.evaluateAttributeExpressions(flowFile).value

        String text = flowFile.getAttribute("Merge")
        attributeList = text.split(";")

        JsonSlurper slurper = new JsonSlurper()
        Map jsonObject = slurper.parse(inputStream)

        //for (var = 0; var < attributeList.size(); var++)

        for (String item : attributeList)

        {
            Map map = [:]
            //  String item = attributeList.getAt(var)

            Map json1 = jsonObject.get("body").get("data").get(item)


            def arrayList = json1.get("values")
            loopCount = 0
            arrayList.each {
                loopCount++
            }
            println("Arraylist size\n" + loopCount)


            for (i = 0; i < loopCount; i++) {
                map.put(jsonObject.get("body").get("data").get(item).get("values").get(i).get("name"),
                        jsonObject.get("body").get("data").get(item).get("values").get(i).get("id"))
            }

            println(map)


            Map json2 = jsonObject.get("body").get("data").get(item)
                    .get("options")
            def arrayList1 = json2.get("data")
            loopCount1 = 0
            arrayList1.each {
                loopCount1++
            }
            println("Arraylist1 size\n" + loopCount1)


            for (j = 0; j < loopCount1; j++) {
                map.putIfAbsent(jsonObject.get("body").get("data").get(item).get("options").get("data").get(j).get("name"),
                        jsonObject.get("body").get("data").get(item).get("options").get("data").get(j).get("id"))
                ErrorHandling.put(jsonObject.get("body").get("data").get(item).get("options").get("data").get(j).get("name"),
                        jsonObject.get("body").get("data").get(item).get("options").get("data").get(j).get("id"))
            }
            println("Map after adding data values\n" + map)
            println(map.size())
            println(map.values())

            loopCount2 = map.size() - loopCount


            Collection id = map.values()
            Collection name = map.keySet()

            /*   println("id\n" + id)

        println("name\n" + name)

        def json = new groovy.json.JsonBuilder()
        json jsonObject*/

            for (z = loopCount; z < map.size(); z++) {
                Map mapNew = [:]
                mapNew.put("name", name.toArray()[z])
                mapNew.put("id", id.toArray()[z])

                arrayList.add(mapNew)

            }

            /*   println "json output: "
        println groovy.json.JsonOutput.prettyPrint(json.toString())*/



        }
        outputStream.withWriter("UTF-8") { it.write(JsonOutput.toJson(jsonObject)) }
    } as StreamCallback)

    /*flowFile=session.putAttribute(flowFile,"key of service values",map.values().toString())*/
    flowFile=session.putAttribute(flowFile,"key of service data",ErrorHandling.values().toString())
    session.transfer(flowFile, REL_SUCCESS)


}

catch (Exception e) {
    e.printStackTrace()

    flowFile = session.putAttribute(flowFile, "Error", e.getMessage())
    session.transfer(flowFile, REL_FAILURE)
}
