import java.util.*


propertiesFileLoc = "D:\\EAI\\Travellers\\Migration\\Prop\\TRV_CW_Prop.properties"
Search_Key ="CW123"

Contract_DB_ID ="1233333333333"
Contract_Entity_ID="CO123"
DOC_TYPE="MSA"
WO_Folder_DB_ID="1234"
AMD_Folder_DB_ID="12345"
WA_Folder_DB_ID="123456"
Supplier_Name="Test_Supplier"


Properties properties = new Properties()
File propertiesFile = new File(propertiesFileLoc)
if (propertiesFile.exists()) {
    propertiesFile.withInputStream {
        properties.load(it)
    }
}
properties.putAt(Search_Key,Contract_DB_ID+";"+Contract_Entity_ID+";"+DOC_TYPE+";"+WO_Folder_DB_ID+";"+AMD_Folder_DB_ID+";"+WA_Folder_DB_ID+";"+Supplier_Name)
properties.store(propertiesFile.newWriter(), null)
