@Grapes(@Grab(group='org.apache.commons', module='commons-csv', version='1.8'))
import org.apache.tools.ant.types.resources.selectors.None

import java.util.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.apache.commons.csv.*;
import java.io.Reader


String text = new File("D:\\EAI\\CS\\Coupa\\Status File\\SIRION_CREDITSUISSE_ACTION_PLAN_08212020.csv").text
propertiesFileLoc = "D:\\EAI\\CS\\Coupa\\Custom API\\Action_Entity_Data.properties"

Reader in1 = new StringReader(text)

Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().withQuote('"').withEscape('\\').parse(in1)
Writer fileWriter = new FileWriter(propertiesFileLoc, false); //overwrites file

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

if (Failure_Count > 1 || Partial_Success_Count >1)
{
    Success_Flag ="False"
}

println(Success_Flag)