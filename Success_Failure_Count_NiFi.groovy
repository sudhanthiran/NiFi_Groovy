import java.util.*
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets


try {
flowFile = session.get()
if (!flowFile) return

int i=0

flowFile = session.write(flowFile, { inputStream, outputStream ->

    String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)

    Total_Count = Total_Count.evaluateAttributeExpressions(flowFile).value
    Word = Word.evaluateAttributeExpressions(flowFile).value

    Total_Count = Total_Count.toInteger()

    Pattern p = Pattern.compile(Word);
    Matcher m = p.matcher( text );
    while (m.find()) {
        i++;
    }

    outputStream.write(text.trim().getBytes(StandardCharsets.UTF_8))
} as StreamCallback)
flowFile=session.putAttribute(flowFile,"Failure_Count",i.toString())
flowFile=session.putAttribute(flowFile,"Success_Count",(Total_Count - i).toString() )

session.transfer ( flowFile , REL_SUCCESS )
}
catch (Exception e) {
    flowFile = session.putAttribute(flowFile, 'Error', e.message)
    session.transfer(flowFile, REL_FAILURE)
}