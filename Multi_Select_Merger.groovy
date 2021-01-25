import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


try {


    Map ErrorHandling=[:]


        fh = new File("D:\\EAI\\RBI\\Mapping Data\\Scripts\\Multiline.txt")
        String inputStream = fh.getText('utf-8')

        String text = "services;functions;globalCountries;globalRegions"
        attributeList = text.split(";")

        JsonSlurper slurper = new JsonSlurper()
        Map jsonObject = slurper.parseText(inputStream)

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
                map.put(jsonObject.get("body").get("data").get(item).get("values").get(i).get("id"),
                        jsonObject.get("body").get("data").get(item).get("values").get(i))
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
                map.putIfAbsent(jsonObject.get("body").get("data").get(item).get("options").get("data").get(j).get("id"),
                        jsonObject.get("body").get("data").get(item).get("options").get("data").get(j))
                ErrorHandling.put(jsonObject.get("body").get("data").get(item).get("options").get("data").get(j).get("id"),
                        jsonObject.get("body").get("data").get(item).get("options").get("data").get(j))
            }
            println("Map after adding data values\n" + map)
            println(map.size())
            println(map.values())

            loopCount2 = map.size() - loopCount


            Collection id = map.keySet()
            Collection name = map.values()

            /*   println("id\n" + id)

        println("name\n" + name)

        def json = new groovy.json.JsonBuilder()
        json jsonObject*/

//            for (z = loopCount; z < map.size(); z++) {
//                Map mapNew = [:]
//                mapNew.put("id", id.toArray()[z])
//                mapNew.put("name", JsonOutput.toJson(name.toString()))
//
//                arrayList.add(mapNew)
//
//            }
            println(item)

            json1.putAt("values",map.values())
            println(json1)

            /*   println "json output: "
        println groovy.json.JsonOutput.prettyPrint(json.toString())*/



        }


}

catch (Exception e) {
    e.printStackTrace()
}
