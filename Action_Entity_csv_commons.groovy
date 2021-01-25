import java.util.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.apache.commons.csv.*;
import java.io.Reader


String text = new File("D:\\EAI\\CS\\Coupa\\Custom API\\Action_Entity_Data.csv").text
//Reader in1 = new FileReader("D:\\EAI\\CS\\Coupa\\Custom API\\Action_Entity_Data.csv")
Reader in1 = new StringReader(text)
propertiesFileLoc = "D:\\EAI\\CS\\Coupa\\Custom API\\Action_Entity_Data.properties"

Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in1)
Writer fileWriter = new FileWriter(propertiesFileLoc, false); //overwrites file

Properties properties = new Properties()
File propertiesFile = new File(propertiesFileLoc)
if (propertiesFile.exists()) {
    propertiesFile.withInputStream {
        properties.load(it)
    }

Map<String,Integer> map = new HashMap<>()

for (CSVRecord record : records) {
    String Action_DB_ID = record.getAt(0)
    String Action_Entity_ID = record.getAt(1)
    String Status = record.getAt(2)
    String Action_Request_Type = record.getAt(3)
    String Action_Plan_Item_ID = record.getAt(4)
    String Risk_ID = record.getAt(5)

    Action_Request_Type_modified = Action_Request_Type.replaceAll(' ','').toLowerCase()
    Action_Entity_ID = "AC"+Action_Entity_ID


    if(Action_Request_Type_modified.equalsIgnoreCase('ActionPlan-SupplierRisk'))
    {
        Action_Request_Type_modified = "SUPPLIER_RISK"
    }
    else if(Action_Request_Type_modified.equalsIgnoreCase('ActionPlan-RelationshipRisk'))
    {
        Action_Request_Type_modified ="RELATION_RISK"
    }
    else {
        Action_Request_Type_modified = Action_Request_Type
    }
    UniqueID =Action_Request_Type_modified+'~'+Action_Plan_Item_ID+'~'+Risk_ID

    if(map.get(UniqueID)==null){
        map.put(UniqueID,1)
    }
    else{
        int cnt = map.get(UniqueID)
        cnt++
        map.put(UniqueID,cnt)
    }

    properties.putAt(UniqueID,Action_DB_ID+";"+map.get(UniqueID)+";"+Status+";"+Action_Entity_ID)
    properties.store(propertiesFile.newWriter(), null)

}
}