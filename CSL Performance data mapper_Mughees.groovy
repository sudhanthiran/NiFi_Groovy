import java.util.Collection
import java.util.LinkedHashMap
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


//try {
    propertiesFileloc = "D:\\EAI\\Vodafone\\Reports\\1YearData_CSl.txt"
    JsonSlurper slurper = new JsonSlurper()
    Map jsonObject
    String fileContents = new File(propertiesFileloc).text
    //jsonObject = slurper.parseText(fileContents)
    jsonObject = slurper.parseText(fileContents)
    //println(fileContents)
    data = jsonObject.getAt("items")


    data.each { field ->
        String ID = field.'ID'
        String service_date = field.'SERVICE DATE'
        String reporting_date = field.'REPORTING DATE'

        ID1 = (List) ID.split(':')
        if (ID1[0].startsWith(';')) {
            ID = ID1[1]
        } else {
            ID = ID1[0]
        }
        field.'ID' = ID

        if (service_date != null) {
            service_date = service_date.substring(6, service_date.length()) + '-' + service_date.substring(3, 5) + '-' + service_date.substring(0, 2)
            field.'SERVICE DATE' = service_date
        }




        if (reporting_date != null) {

            reporting_date = reporting_date.substring(6, reporting_date.length()) + '-' + reporting_date.substring(3, 5) + '-' + reporting_date.substring(0, 2)
            field.'REPORTING DATE' = reporting_date
        }
        // Change in PArent Supplier

        String Supplier = field.'PARENT SUPPLIER'

        if (Supplier != null) {
            pSupplier = Supplier.split(':')
            field.'PARENT SUPPLIER' = pSupplier[0]
        }

// Change in External contracting party




        //  if(party.size() !=0){
        // field.'EXTERNAL CONTRACTING PARTY'.removeAll()
        party = field.'EXTERNAL CONTRACTING PARTY'
        if (party != null) {
            party1 = party.pop()
            field.'EXTERNAL CONTRACTING PARTY' = party1
            print(party1)
        }
        //}
        //MAster Sl id change

        String slId = field.'MASTER SL ID'
        if (slId != null) {
            arr = slId.split(':')

            if (arr[0].length() == 5) {
                field.'MASTER SL ID' = "SL" + arr[0]
            } else {
                field.'MASTER SL ID' = "SL0" + arr[0]

            }

        }
        jsonObject.putAt("items", data)

    }
//}
//catch (Exception e){
//    print(e)
//}
