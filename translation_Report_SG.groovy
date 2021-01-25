import java.awt.List
import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


//try {

Report_Count = "18"
NB_PO_Score = "600"
Total_PO_Amount = "200000"
dyn_TCC = "dyn107574"


Report_Count = Report_Count.toDouble()
NB_PO_Score = NB_PO_Score.toDouble()
Total_PO_Amount = Total_PO_Amount.toDouble()

COA_Flag = new ArrayList<Integer>()



JsonSlurper slurper = new JsonSlurper()
Map jsonObject


propertiesFileloc = "D:\\EAI\\Saint Gobain\\Kurary report.txt"
String fileContents = new File(propertiesFileloc).text
jsonObject = slurper.parseText(fileContents)

//println(jsonObject)

data = jsonObject.get("data")
Double TCC_Sum = 0

data.each { field ->
    String TCC = field.getAt(dyn_TCC)

    if (TCC != null)
    {
        TCC_Sum += TCC.toDouble()
    }

    String resolution_date =  field.getAt("resolution_date")
    String  planned_completion_date = field.getAt("planned_completion_date")

    if (resolution_date != null && planned_completion_date != null)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy")
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))

        Long R_date = simpleDateFormat.parse(resolution_date).getTime()
        Long P_date = simpleDateFormat.parse(planned_completion_date).getTime()

        println(R_date)
        println(P_date)

        val = P_date - R_date

        println(val)

        if (val > 0) {
            COA_Flag.push(1)
        } else if (val < 0) {
            COA_Flag.push(0)
        } else if (val == 0) {
            COA_Flag.push(1)
        }

    }
}

if (Total_PO_Amount != 0 && Total_PO_Amount > 0)
{
    CR_Value = ((TCC_Sum/Total_PO_Amount)*100)
    if (CR_Value > 15)
    {
        CR_Value = "> 15%"
    }
    else if(CR_Value > 5 && CR_Value <= 15)
    {
        CR_Value = "5% - 15% (15% included)"
    }
    else if(CR_Value > 0 && CR_Value <= 5)
    {
        CR_Value ="0% - 5% (5% included)"
    }
    else if(CR_Value == 0)
    {
        CR_Value = "0 (100)"
    }
    else
    {
        CR_Value = "Not applicable (n/a)"
    }

}
if (NB_PO_Score != 0 && NB_PO_Score > 0) {
    CR_Volume = ((Report_Count / NB_PO_Score) * 100)
    if(CR_Volume > 30)
    {
        CR_Volume = "greater than 30%"
    }
    else if(CR_Volume > 10 && CR_Volume <= 30)
    {
        CR_Volume ="10% - 30% (30% included)"
    }
    else if(CR_Volume > 0 && CR_Volume <= 10)
    {
        CR_Volume ="0% - 10% (10% included)"
    }
    else if(CR_Volume == 0)
    {
        CR_Volume ="0 (100)"
    }
    else {
        CR_Volume = "Not applicable (n/a)"
    }
}


if(!COA_Flag.isEmpty())
{
    COA = "Reliable (100)"


for (i in COA_Flag)
{
    if(i!=1)
    {
        COA = "No respect to commitments (0)"
    }

}
}
else
{
    COA ="Not applicable (n/a)"
}
println(COA)
//}
//catch (Exception e){
//    print(e)
//}
