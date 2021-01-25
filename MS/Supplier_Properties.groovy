import org.apache.commons.io.IOUtils
import java.io.*
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
String flag = "false"
flowFile = session.get()
if (!flowFile) return

flowFile = session.write(flowFile, { inputStream, outputStream ->

    String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    propertiesFileLoc = sourceFileDirectory.evaluateAttributeExpressions(flowFile).value
    Writer fileWriter = new FileWriter(propertiesFileLoc, false); //overwrites file

    Properties properties = new Properties()
    File propertiesFile = new File(propertiesFileLoc)
    if (propertiesFile.exists()) {
        propertiesFile.withInputStream {
            properties.load(it)
        }
        List<String> myVendorList = text.split("\n")
        Map<String,Integer> map = new HashMap<>()
        for(String lststr:myVendorList){
            String[] splitText = lststr.split(",")
            String _3 = splitText[3]
            if(map.get(_3)==null)
                map.put(_3,1)
            else{
                int cnt = map.get(_3)
                cnt++
                map.put(_3,cnt)
            }
        }
        boolean _skip = true
        for(String data : myVendorList){
            if(_skip){
                _skip = false
                continue
            }
            String[] splitText = data.split(",")
            if(!splitText[3].isEmpty() && !splitText[0].isEmpty()){
                properties[splitText[3]] = splitText[0]+";"+map.get(splitText[3])
                properties.store(propertiesFile.newWriter(), null)
            }
        }

        flag = "true"
    }
    else{
        flag = "false"
    }
    outputStream.write(text.toString().getBytes(StandardCharsets.UTF_8))
} as StreamCallback)
if(flag.equals("true"))
    session.transfer(flowFile, REL_SUCCESS)
else
    session.transfer(flowFile, REL_FAILURE)

