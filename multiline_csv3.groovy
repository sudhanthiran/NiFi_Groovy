import java.util.*
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import org.apache.commons.csv.*


flowFile = session.get()
if (!flowFile) return



flowFile = session.write(flowFile, { inputStream, outputStream ->

    String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    CSVParser parser = CSVParser.parse(text, StandardCharsets.UTF_8 ,CSVFormat.DEFAULT);


    for (CSVRecord csvRecord : parser) {
        println(csvRecord.getHeaderNames())
        flowFile=session.putAttribute(flowFile,"priority"+csvRecord.getHeaderNames())
    }
} as StreamCallback)
session.transfer ( flowFile , REL_SUCCESS )

