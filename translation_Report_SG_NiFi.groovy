import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import java.text.SimpleDateFormat
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

flowFile = session.get()
if (!flowFile) return

try {
    COA_att = ""
    CR_Volume_att = ""
    CR_Value_att = ""
    CR_Value=(Double)0.0
    CR_Volume=(Double)0.0

    flowFile = session.write(flowFile, { inputStream, outputStream ->


        Report_Count = Report_Count.evaluateAttributeExpressions(flowFile).value
        NB_PO_Score = NB_PO_Score.evaluateAttributeExpressions(flowFile).value
        Total_PO_Amount = Total_PO_Amount.evaluateAttributeExpressions(flowFile).value
        dyn_TCC = dyn_TCC.evaluateAttributeExpressions(flowFile).value


        Report_Count = Report_Count.toDouble()
        NB_PO_Score = NB_PO_Score.toDouble()
        Total_PO_Amount = Total_PO_Amount.toDouble()

        COA_Flag = new ArrayList<Integer>()



        JsonSlurper slurper = new JsonSlurper()
        String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
        Map jsonObject = slurper.parseText(text)


        data = jsonObject.get("data")
        Double TCC_Sum = 0

        data.each { field ->
            String TCC = field.getAt(dyn_TCC)

            if (TCC != null) {
                TCC_Sum += TCC.toDouble()
            }

            String resolution_date = field.getAt("resolution_date")
            String planned_completion_date = field.getAt("planned_completion_date")

            if (resolution_date != null && planned_completion_date != null) {
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

        if (Total_PO_Amount != 0 && Total_PO_Amount > 0) {
            CR_Value = ((TCC_Sum / Total_PO_Amount) * 100)

            if (CR_Value > 15) {
                CR_Value_att = "> 15%"

            } else if (CR_Value > 5 && CR_Value <= 15) {
                CR_Value_att = "5% - 15% (15% included)"

            } else if (CR_Value > 0 && CR_Value <= 5) {
                CR_Value_att = "0% - 5% (5% included)"

            } else if (CR_Value == 0) {
                CR_Value_att = "0 (100)"

            } else {
                CR_Value_att = "Not applicable (n/a)"

            }


        }

        if (NB_PO_Score != 0 && NB_PO_Score > 0) {
            CR_Volume = ((Report_Count / NB_PO_Score) * 100)

            if (CR_Volume > 30) {
                CR_Volume_att = "greater than 30%"

            } else if (CR_Volume > 10 && CR_Volume <= 30) {
                CR_Volume_att = "10% - 30% (30% included)"

            } else if (CR_Volume > 0 && CR_Volume <= 10) {
                CR_Volume_att = "0% - 10% (10% included)"

            } else if (CR_Volume == 0) {
                CR_Volume_att = "0 (100)"

            } else {
                CR_Volume_att = "Not applicable (n/a)"

            }

        }


        if (!COA_Flag.isEmpty()) {
            COA_att = "Always"


            for (i in ROC_Flag) {
                if (i != 1) {
                    COA_att = "Never"
                }

            }
        } else {
            COA_att = "Not applicable (n/a)"
        }




        outputStream.withWriter("UTF-8") { it.write(JsonOutput.toJson(jsonObject)) }
    } as StreamCallback)
    flowFile = session.putAttribute(flowFile, "COA", COA_att)
    flowFile = session.putAttribute(flowFile, "CR_Value", CR_Value_att)
    flowFile = session.putAttribute(flowFile, "CR_Volume", CR_Volume_att)
    flowFile = session.putAttribute(flowFile, "CR_Value_Calculated", CR_Value)
    flowFile = session.putAttribute(flowFile, "CR_Volume_Calculated", CR_Volume)
    flowFile = session.putAttribute(flowFile, "COA_Flag", COA_Flag)
    session.transfer(flowFile, REL_SUCCESS)
}
catch (Exception e) {
    flowFile = session.putAttribute(flowFile, 'Error', e.message)
    session.transfer(flowFile, REL_FAILURE)
}
