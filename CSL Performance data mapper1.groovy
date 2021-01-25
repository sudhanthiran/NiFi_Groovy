import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import java.util.ArrayList


try {
    def flowFile = session.get()
    if (!flowFile) return


    flowFile = session.write(flowFile, { rawIn, rawOut ->

        jsonSlurper = new JsonSlurper()

        String text = IOUtils.toString(rawIn, StandardCharsets.UTF_8)

        def json = jsonSlurper.parseText(text)

        data = json.getAt("items")
        data.each { field ->

            String ID = field.'ID'
            String service_date = field.'SERVICE DATE'
            String reporting_date = field.'REPORTING DATE'

            if(ID !=null) {

                ID1 = (List) ID.split(':')

                if (ID1[0].startsWith(';')) {
                    ID = ID1[1].toString()
                } else {
                    ID = ID1[0].toString()
                }
                field.'ID' = ID
            }



            if(service_date !=null) {
                service_date = service_date.substring(6, service_date.length()) + '-' + service_date.substring(3, 5) + '-' + service_date.substring(0, 2)
                field.'SERVICE DATE' = service_date
            }




            if(reporting_date !=null) {

                reporting_date = reporting_date.substring(6, reporting_date.length()) + '-' + reporting_date.substring(3, 5) + '-' + reporting_date.substring(0, 2)
                field.'REPORTING DATE' = reporting_date
            }
            // Change in PArent Supplier

            String Supplier=field.'PARENT SUPPLIER'

            if(Supplier !=null) {
                pSupplier = Supplier.split(':')
                field.'PARENT SUPPLIER' = pSupplier[0]
            }

// Change in External contracting party

            party = field.'EXTERNAL CONTRACTING PARTY'[0]

            //  if(party.size() !=0){
            // field.'EXTERNAL CONTRACTING PARTY'.removeAll()

            field.'EXTERNAL CONTRACTING PARTY'= party

            //}
            //MAster Sl id change

            String slId = field.'MASTER SL ID'
            if(slId !=null) {
                arr = slId.split(':')

                if(arr[0].length()==5) {
                    field.'MASTER SL ID' = "SL" + arr[0]
                }

                else {
                    field.'MASTER SL ID' = "SL0" + arr[0]

                }

            }
        }
        json.putAt("items", data)

        rawOut.withWriter("UTF-8") { it.write(JsonOutput.toJson(json)) }
    } as StreamCallback)

    session.transfer(flowFile, REL_SUCCESS)


}

catch (Exception e) {
    flowFile = session.putAttribute(flowFile, 'Error', e.message)
    session.transfer(flowFile, REL_FAILURE)
}