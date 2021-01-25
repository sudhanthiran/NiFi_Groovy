import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

flowFile = session.get()
if (!flowFile) return
try {
    attribute = attribute.evaluateAttributeExpressions(flowFile).value;
    attributeList = attribute.split(";")

//    Map dataList=[:]
//
//    dataList["CR_Volume"]= "0 (100)"
//    dataList["CR_Value"]= "0 - 5% (5% included)"
//    dataList["COA"] = "Sometimes (50)"



    JsonSlurper slurper = new JsonSlurper()
    Map jsonObject


    propertiesFileloc = propertyFile.evaluateAttributeExpressions(flowFile).value
    String fileContents = new File(propertiesFileloc).text
    jsonObject = slurper.parseText(fileContents)


    for (i in attributeList)
    {
        dataList = flowFile.getAttribute(i)
        options = jsonObject.get(i)
        values = dataList
        int numSelections = 0
        selectedValues = ''
        options.each{ option ->
                if(option.feedValue.equalsIgnoreCase(values)){
                    selectedValues =option.sirionValue
                    numSelections++
                }
        }
//        println(selectedValues)
        flowFile = session.putAttribute(flowFile, i+"_translated", selectedValues.toString())
    }
    session.transfer(flowFile, REL_SUCCESS)
}
catch (Exception e){
//    print(e)
    flowFile = session.putAttribute(flowFile, "Error", e.getMessage())
    session.transfer(flowFile, REL_FAILURE)
}