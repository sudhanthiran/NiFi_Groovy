import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.Date
import java.text.SimpleDateFormat
import java.text.ParseException
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

def ff = session.get()
if(ff == null) {
    return;
}
boolean isValidDate(String inDate) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
    dateFormat.setLenient(false);
    try {
        dateFormat.parse(inDate.trim());
    } catch (ParseException pe) {
        return false;
    }
    return true;
}

try {

    ff = session.write(ff, {rawIn, rawOut->
        text =IOUtils.toString(rawIn, StandardCharsets.UTF_8)
        date_to_change ="Start_Date;End_Date;FISV_Effective_Date"
        jsonSlurper = new JsonSlurper()
        data = jsonSlurper.parseText(text)
        date_list = date_to_change.split(";")

        for (String item : date_list)

        {
            date_string =""
            temp = data[0].getAt(item)
            if (temp.toString().isNumber()) {
                LocalDate EXCEL_EPOCH_REFERENCE = LocalDate.of(1899, Month.DECEMBER, 30)
                BigDecimal countFromEpoch = new BigDecimal(temp)
                long days = countFromEpoch.longValue()
                LocalDate localDate = EXCEL_EPOCH_REFERENCE.plusDays(days)
                date_string = localDate.format("MM/dd/YYYY").toString()
            }
            else
            {
                if(isValidDate(temp)) {
                    date_try = Date.parse(temp)
                    LocalDate EXCEL_EPOCH_REFERENCE = LocalDate.of(1970, Month.JANUARY, 1)
                    //LocalDate localDate = EXCEL_EPOCH_REFERENCE.plusDays(date_try)
                    Date date = new Date(date_try)
                    date_string = date.format("MM/dd/YYYY")
                }
            }
            if(date_string != "" && item.equalsIgnoreCase("start_date"))
            {
                year = date_string.split("/")
                println(year[2])

                if (year[2].toInteger() < 1984)
                {
                    data[0].putAt("Pre_1984",17853)
                }
                else
                {
                    data[0].putAt("Pre_1984",null)
                }
            }
            data[0].putAt(item,date_string)

        }
        //print(data[0])
        rawOut.withWriter("UTF-8"){ it.write( JsonOutput.toJson(data) )}
    } as StreamCallback)
    session.transfer(ff, REL_SUCCESS)
}
catch (Exception e){
    ff = session.putAttribute(ff, 'Error', e.message)
    session.transfer(ff, REL_FAILURE)
}