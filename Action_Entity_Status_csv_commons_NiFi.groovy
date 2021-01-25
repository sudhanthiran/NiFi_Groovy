import java.util.*
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.apache.commons.csv.*;


flowFile = session.get()
if (!flowFile) return


try {
    flowFile = session.write(flowFile, { inputStream, outputStream ->

        String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
        Reader in1 = new StringReader(text)

        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in1)

        Success_Flag = "True"

        Create_Count = 0
        Update_Count = 0

        Success_Count = 0
        Failure_Count = 0
        Partial_Success_Count = 0
        Total_Count = 0

        for (CSVRecord record : records) {
            String Action_Type = record.get("RECORD_TYPE")
            String Action_Status = record.get("STATUS")

            Total_Count ++

            if(Action_Type.equalsIgnoreCase('Create'))
            {
                Create_Count ++
            }
            else if(Action_Type.equalsIgnoreCase('Update')) {
                Update_Count ++
            }

            if(Action_Status.equalsIgnoreCase('Success'))
            {
                Success_Count ++
            }
            else if(Action_Status.equalsIgnoreCase('Failure')) {
                Failure_Count ++
            }
            else if(Action_Status.equalsIgnoreCase('Partial Success')) {
                Partial_Success_Count ++
            }
        }
        if (Failure_Count >= 1 || Partial_Success_Count >= 1)
        {
            Success_Flag ="False"
        }

        outputStream.write(text.trim().getBytes(StandardCharsets.UTF_8))
    } as StreamCallback)

    flowFile = session.putAttribute(flowFile, 'Success_Flag', Success_Flag)
    flowFile = session.putAttribute(flowFile, 'Create_Count', Create_Count.toString())
    flowFile = session.putAttribute(flowFile, 'Update_Count', Update_Count.toString())
    flowFile = session.putAttribute(flowFile, 'Success_Count', Success_Count.toString())
    flowFile = session.putAttribute(flowFile, 'Failure_Count', Failure_Count.toString())
    flowFile = session.putAttribute(flowFile, 'Partial_Success_Count', Partial_Success_Count.toString())
    flowFile = session.putAttribute(flowFile, 'Total_Count', Total_Count.toString())

    session.transfer ( flowFile , REL_SUCCESS )
}
catch (Exception e){
    flowFile = session.putAttribute(flowFile, 'Error', e.message)
    session.transfer(flowFile, REL_FAILURE)
}