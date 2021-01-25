import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


map = "123"
dbid="67890"
entityID = "1"

String concat = dbid+";"+entityID

propertiesFileloc = "C:\\Users\\sudhanthiran.b\\Desktop\\Groovy\\fileEntry.properties"
Properties properties = new Properties()
File propertiesFile = new File(propertiesFileloc)
if (propertiesFile.exists()) {
    propertiesFile.withInputStream {
        properties.load(it)
    }
}

properties.put(map,concat)




properties.store(propertiesFile.newWriter(), null)

