import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

flowFile = session.get()
if(!flowFile) return



try {
    session.read(flowFile, {inputStream ->
    String reader = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    String Filepath1 = PropFile.evaluateAttributeExpressions(flowFile).value;
    //File propertiesFile = new File(Filepath)

    Properties properties = new Properties()
   // propertiesFile.withReader { reader ->
        reader.eachLine { line ->
            data = line.split(',')
            DBID = data[0]
            EntityID = data[1]
            ECCID = data[2]
            concat = DBID + ',' + EntityID
            properties.put(ECCID, concat)
            //println(DBID)
        }
    //}
    File propertiesFile1 = new File(Filepath1)
    properties.store(propertiesFile1.newWriter(false), null)
} as InputStreamCallback)
    session.transfer(flowFile, REL_SUCCESS)
}
catch (Exception e){
    flowFile = session.putAttribute(flowFile, "Error", e.getMessage())
    session.transfer(flowFile, REL_FAILURE)
}
