import java.util.*
@Grab('com.xlson.groovycsv:groovycsv')
import static com.xlson.groovycsv.CsvParser.parseCsv
//import org.apache.commons.io.IOUtils
//import java.nio.charset.StandardCharsets
//import com.opencsv.CSVReader
//@Grab('com.xlson.groovycsv:groovycsv:1.3')
/*

@Grapes(@Grab(group='com.xlson.groovycsv', module='groovycsv', version='1.3'))
import static com.xlson.groovycsv.CsvParser.parseCsv
*/


  String text="CTR000051,CTR000050,0000012345,0000012345,0000012345,Activated,Standalone Contract,Order Schedule,Order Schedule,FALSE,Auto-renewal same terms,FALSE,,,Credit Suisse Securities (USA) LLC,Advent Software INC,,,03-15-2019,12-15-2019,Infrastructure Software - Perpetual licenses,Infrastructure Software - Development Advice;Infrastructure Software - Maintenance & Support;Infrastructure Software - Perpetual licenses;Infrastructure Software - Term Licenses,EMEA,,USD,241.0000000000,36,True,yes,,,Running,,Credit Suisse Securities (USA) LLC,,Non Outsourcing\n" +
            "CTR048548,CTR000051,0000103330,,0000103330,Activated,Order Documents,,3-year maintenance and support renewal for Dell Toad for Oracle licenses.,FALSE,Contract Ends (not extendable),FALSE,English,,Credit Suisse AG,Dell Software International Limited,,,09-15-2016,09-15-2019,Application Software - Maintenance & Support,Application Software - Maintenance & Support,Global,,EUR,309000.0000000000,,False,,06-SEP-19,5786738,To be reviewed,,,,Non Outsourcing\n" +
            "CTR027412,CTR020550,0000079216,0000107176,0000107176,Activated,Order Documents,,,FALSE,,FALSE,English,,,DELL CORPORATION LIMITED,,,09-25-2017,09-25-2017,End User Software - Development Advice,End User Software - Development Advice,Global,,USD,5648654.0000000000,,False,,15-JAN-19,4722728,Closed,,,,Non Outsourcing\n" +
            "CTR049498,CTR051576,0000053433,0000089786,0000089786,Activated,Changes and Notices,STaaS Steady State SOW Amendment #1,\"Governs the provision of STaaS The STaaS includes operational services , service levels and storage technology infrastructure provided on utility based consumption pricing\",FALSE,Contract Ends (not extendable),FALSE,English,,Credit Suisse AG,EMC CORPORATION,,,06-21-2017,06-20-2022,Storage - Managed Service,Storage - Managed Service,Global,,USD,134572843.0000000000,,False,yes,02-JAN-19,3910679,Running,,,,Regulatory Outsourcing\n" +
            "CTR052113,CTR048551,0000053433,0000089786,0000089786,Activated,Order Documents,Third amendment to Application Service Provider agreement,,True,Auto-renewal same terms,True,,,Credit Suisse Securities (USA) LLC,DELL MARKETING LP,,,03-27-2012,,Infrastructure Software - Perpetual licenses,Infrastructure Software - Development Advice;Infrastructure Software - Maintenance & Support;Infrastructure Software - Perpetual licenses;Infrastructure Software - Term Licenses,Americas,,USD,0.0000000000,36,True,yes,,,Running,,Credit Suisse Securities (USA) LLC,,Non Outsourcing\n" +
            "CTR029514,CTR037412,0000060887,0000089786,0000089786,Activated,Notices,PCR17 Extension of Resources CloudServe Enterprise Hybrid Cloud,Extension of resources in the US and UK,ABC,not extendable,XYZ,Hindi,,Credit Suisse Securities (USA) LLC,EMC CORPORATION,ABC,XYZ,01-01-2018,09-30-2018,Servers - Managed Service,Servers - Managed Service,,\"UNITED KINGDOM,United States\",USD,1100000.0000000000,,False,yes,31-JAN-18,5457009,Closed,,Credit Suisse Securities (Europe) Limited,,Non\n" +
            "CTR054319,CTR048548,0000060887,0000089786,0000089786,Activated,Changes and Notices,EHC (CloudServe) Upgrade,Upgrade EHC (CloudServe) environment from 4.1.1 to 4.1.x,FALSE,Contract Ends (not extendable),FALSE,English,,Credit Suisse AG,EMC CORPORATION,,,07-12-2018,01-11-2019,Servers - Managed Service,Servers - Managed Service,Global,,USD,0.0000000000,,False,yes,24-DEC-18,4358392,Closed,,,,Non Outsourcing\n" +
            "CTR055633,CTR048548,0000060887,0000089786,0000089786,Activated,Order Documents,CloudServe Application Adoption Services - \$2M Incentive,CloudServe Application Adoption Services - \$2M Incentive negotiated as part of Project Thunderball (STaaS - Storage as a Service),FALSE,Contract Ends (not extendable),FALSE,English,,Credit Suisse Securities (USA) LLC,EMC CORPORATION,High,High,01-01-2018,12-31-2018,Servers - Managed Service,Servers - Managed Service;S4 Infrastructure Services,Americas,,USD,0.0000000000,,False,yes,03-OCT-18,3910679,Closed,,,,Non Outsourcing\n" +
            "CTR048551,CTR048548,0000060887,0000089786,0000089786,Activated,Order Documents,STaaS Steady State SOW,\"Governs the provision of STaaS The STaaS includes operational services , service levels and storage technology infrastructure provided on utility based consumption pricing\",FALSE,Contract Ends (not extendable),FALSE,Polish,,Credit Suisse AG,EMC CORPORATION,,,06-21-2017,06-20-2022,Storage - Managed Service,Storage - Managed Service,Global,,USD,134572843.0000000000,50,False,,13-JUL-18,3910679,Running,,,,Non Outsourcing\n" +
            "CTR058052,CTR048552,0000060887,0000089786,0000089786,Activated,Order Documents,Amendment STaaS - Third Party Maintenance Agreement #1,\"Put in place to have Dell EMC be able to provide support on their competitors storage technology Primarily applies to providing third party support on Hitachi technology\",FALSE,Contract Ends (not extendable),FALSE,English,,Credit Suisse AG,EMC CORPORATION,,,06-16-2017,12-31-2021,Storage - Managed Service,Storage - Managed Service,Global,,USD,0.0000000000,,False,yes,19-JUL-18,6120229,Running,,,,Non Outsourcing\n" +
            "CTR048550,CTR048548,0000060887,0000089786,0000089786,Activated,Order Documents,Transformation SOW,\"Sets forth how Dell will transform and lifecycle the current state services to the desired target state service Three key components of the SOW: Discovery and Analysis (D&A), Data Migrations and Operational Service Transformation\",FALSE,Contract Ends (not extendable),FALSE,English,,Credit Suisse AG,EMC CORPORATION,,,06-21-2017,06-21-2021,Storage - Managed Service,Storage - Managed Service,Global,,USD,18103552.0000000000,,False,yes,10-DEC-18,3910679,Running,,,,Regulatory Outsourcing\n" +
            "CTR048555,CTR032547,0000060887,0000089786,0000089786,Activated,Exhibits,CloudServe Amendment 1 - Storage Unit Rate,Reduction of Storage unit rate and incorporation of data reduction guarantee,FALSE,Contract Ends (manually extendable,FALSE,,,Credit Suisse AG,EMC CORPORATION,,,06-19-2017,10-17-2020,Servers - Managed Service,Servers - Managed Service;S4 Infrastructure Services,Global,,USD,0.0000000000,,False,yes,24-DEC-18,4358392,Running,,,,\n" +
            "CTR048552,CTR048548,0000060887,0000089786,0000089786,Activated,Order Documents,STaaS - Third Party Maintenance Agreement,\"Put in place to have Dell EMC be able to provide support on their competitors storage technology Primarily applies to providing third party support on Hitachi technology\",FALSE,Contract Ends (not extendable),FALSE,English,,Credit Suisse AG,EMC CORPORATION,,,06-16-2017,12-31-2021,Storage - Managed Service,Storage - Managed Service,Global,,USD,8688000.0000000000,,False,yes,13-JUL-18,3910679,Running,,,,Non Outsourcing\n" +
            "CTR038907,CTR032547,0000052915,0000089786,0000089786,Activated,Changes and Notices,Amendment 2 to the EMC MAMS,Amendment 2 to the EMC MAMS,FALSE,Contract Ends (not extendable),FALSE,English,,Dummy Value,EMC COMPUTER SYSTEMS,,,05-19-2016,12-31-2019,Storage - Managed Service,Storage - Managed Service,Global,,USD,0.0000000000,,False,,24-DEC-18,4358392,To be reviewed,,,,Regulatory Outsourcing\n" +
            "CTR025268,CTR042737,0000060887,0000089786,0000089786,Activated,Changes and Notices,Amendment and Addendum to Master Customer Agreement Number 2,,FALSE,,FALSE,English,,Credit Suisse Securities (USA) LLC,EMC CORPORATION,,,06-15-2019,12-15-2019,EUP site operations and service desk,EUP hardware maintenance;EUP site operations and service desk;Managed Print Service Multi-Functional Printers;Managed Print Service Personal Printers,Americas,,USD,19452600.0000000000,,True,,16-JAN-18,6120229,Running,,Credit Suisse Securities (USA) LLC,,Non Outsourcing\n" +
            "CTR051313,CTR051576,00000ABCDE,0000089786,0000089786,Activated,Changes and Notices,Dell Amendment #1 to Global MSA for IT Services,Amendment covering updated data protection clauses,FALSE,,FALSE,English,,Credit Suisse AG,DELL MARKETING LP,,,09-22-2017,,End User Software - Development Advice,End User Software - Development Advice,Global,,USD,0.0000000000,,True,yes,31-OCT-17,4985818,Running,,,,Non Outsourcing\n" +
            "CTR000000,CTR052113,0000060887,0000089786,0000089786,Activated,Order Documents,STaaS Steady State SOW,\"Governs the provision of STaaS The STaaS includes operational services , service levels and storage technology infrastructure provided on utility based consumption pricing\",FALSE,Contract Ends (not extendable),FALSE,Polish,,Credit Suisse AG,EMC CORPORATION,,,06-21-2017,06-20-2022,Storage - Managed Service,Storage - Managed Service,Global,,USD,134572843.0000000000,50,False,,13-JUL-18,3910679,Running,,,,Non Outsourcing\n" +
            "CTR038908,CTR032547,0000060887,0000089786,0000089786,Activated,Order Documents,Amendment STaaS - Third Party Maintenance Agreement #1,\"Put in place to have Dell EMC be able to provide support on their competitors storage technology Primarily applies to providing third party support on Hitachi technology\",FALSE,Contract Ends (not extendable),FALSE,English,,Credit Suisse AG,EMC CORPORATION,,,06-16-2017,12-31-2021,Storage - Managed Service,Storage - Managed Service,Global,,USD,0.0000000000,,False,yes,19-JUL-18,6120229,Running,,,,Non Outsourcing\n" +
            "CTR038900,CTR038908,0000060887,0000089786,0000089786,Activated,Order Documents,Transformation SOW,\"Sets forth how Dell will transform and lifecycle the current state services to the desired target state service Three key components of the SOW: Discovery and Analysis (D&A), Data Migrations and Operational Service Transformation\",FALSE,Contract Ends (not extendable),FALSE,English,,Credit Suisse AG,EMC CORPORATION,,,06-21-2017,06-21-2021,Storage - Managed Service,Storage - Managed Service,Global,,USD,18103552.0000000000,,False,yes,10-DEC-18,3910679,Running,,,,Regulatory Outsourcing\n" +
            "CTR038901,CTR038908,0000060887,0000089786,0000089786,Activated,Exhibits,CloudServe Amendment 1 - Storage Unit Rate,Reduction of Storage unit rate and incorporation of data reduction guarantee,FALSE,Contract Ends (manually extendable,FALSE,,,Credit Suisse AG,EMC CORPORATION,,,06-19-2017,10-17-2020,Servers - Managed Service,Servers - Managed Service;S4 Infrastructure Services,Global,,USD,0.0000000000,,False,yes,24-DEC-18,4358392,Running,,,,\n" +
            "7thlevel,CTR058052,0000060887,0000089786,0000089786,Activated,Order Documents,STaaS - Third Party Maintenance Agreement,\"Put in place to have Dell EMC be able to provide support on their competitors storage technology Primarily applies to providing third party support on Hitachi technology\",FALSE,Contract Ends (not extendable),FALSE,English,,Credit Suisse AG,EMC CORPORATION,,,06-16-2017,12-31-2021,Storage - Managed Service,Storage - Managed Service,Global,,USD,8688000.0000000000,,False,yes,13-JUL-18,3910679,Running,,,,Non Outsourcing\n" +
            "9thlevel,8thlevel,0000052915,0000089786,0000089786,Activated,Changes and Notices,Amendment 2 to the EMC MAMS,Amendment 2 to the EMC MAMS,FALSE,Contract Ends (not extendable),FALSE,English,,Dummy Value,EMC COMPUTER SYSTEMS,,,05-19-2016,12-31-2019,Storage - Managed Service,Storage - Managed Service,Global,,USD,0.0000000000,,False,,24-DEC-18,4358392,To be reviewed,,,,Regulatory Outsourcing\n" +
            "8thlevel,7thlevel,0000060887,0000089786,0000089786,Activated,Changes and Notices,Amendment and Addendum to Master Customer Agreement Number 2,,FALSE,,FALSE,English,,Credit Suisse Securities (USA) LLC,EMC CORPORATION,,,06-15-2019,12-15-2019,EUP site operations and service desk,EUP hardware maintenance;EUP site operations and service desk;Managed Print Service Multi-Functional Printers;Managed Print Service Personal Printers,Americas,,USD,19452600.0000000000,,True,,16-JAN-18,6120229,Running,,Credit Suisse Securities (USA) LLC,,Non Outsourcing"


//fh = new File('C:\\Users\\sudhanthiran.b\\Desktop\\Multi Line CSV\\SIRION_CREDITSUISSE_CON_10242019.csv')
//String text = fh.getText('utf-8')
    //ArrayList str = text.split("\n").toList()

//    for (String entry : str) {
//        println(entry)
//
//    }

    //println(str.size())

//    for (String element : str) {
//        ArrayList str1 = element.split(",")
//        String Sui = str1.getAt(0)
//        String Pui = str1.getAt(1)
//        MyTreeNode.pair.put(Sui, Pui)
//        MyTreeNode.dataDict.put(Sui, element)
//
//        println("tet data\n" + element)
//    }

def csv_content = "CONTRACT_ID,PARENT_CONTRACT_ID,CS_VENDOR_ID,PARENT_VENDOR_ID,SUPERPARENT_VENDORID,STATUS,CONTRACT_TYPE,CONTRACT_NAME,CONTRACT_DESCRIPTION,RRP_APPLICABLE,RENEWAL_TYPE,CONFIDENTIAL,CONTRACT_LANGUAGE,TPRM_ID,CONTRACTING_LEGAL_ENTITY,VENDOR_CONTRACTING_ENTITY,IRR,RRR,CONTRACT_START_DATE,CONTRACT_END_DATE,SOURCING_CATEGORY,SUB_CATEGORY,REGIONS,COUNTRIES,CURRENCY,TCV_OF_CURRENT_CONTRACT,RENEWAL_TERMS,PERPETUAL,DORMANT_CONTRACT,MODIFIED_DATE,MODIFIED_BY,VALIDITY_STATUS,AUTOMATIC_RENEWAL,ADDITIONAL_CS_LEGAL_ENTITIES,GL_OR_PU_BU,OUTSOURCING_TYPE,SOURCING_CATEGORYCODE,SUB_CATEGORYCODE\n" +text
//def csv_content = text
def data_iterator = parseCsv(csv_content, separator: ',', readFirstLine: false)


for (line in data_iterator)
{
    str1 = line.toMap()
    a= (line.values).join(",")
    String Sui = str1.get("CONTRACT_ID")
    String Pui = str1.get("PARENT_CONTRACT_ID")
    MyTreeNode.pair.put(Sui, Pui)
    MyTreeNode.dataDict.put(Sui, a)

    //println("tet data\n" + a)

}


/*    println("map Key\n" + pair.keySet())
    println("Map values\n" + pair.values())*/
    MyTreeNode<String> root = new MyTreeNode<>("Root")
    ArrayList<MyTreeNode> sortedList = new ArrayList<>()
    Set<String> rootNode = new LinkedHashSet<>()
    for (String entry : MyTreeNode.pair.values()) {

        if (!MyTreeNode.pair.containsKey(entry)) {
            rootNode.add(entry)

        }
    }

    for (String entry : rootNode) {
        root.addChild(entry)
    }
    println("Rootnode\n" + rootNode)

    ArrayList<MyTreeNode> l1 = new ArrayList<>()
    ArrayList<MyTreeNode> l2 = new ArrayList<>()
    ArrayList<MyTreeNode> l3 = new ArrayList<>()

    //making super parent child second level

    for (MyTreeNode entry : root.getChildren()) {

        l1 = entry.findChildren(entry, entry.getData().toString())
        l2.addAll(l1)
        sortedList.addAll(l1)
    }

    l1.removeAll(l1)
    //Now I have l1,l3 list and l2 contains child
    //3rd level
    while (!MyTreeNode.pair.isEmpty()) {

        for (MyTreeNode entry : l2) {
            l1 = entry.findChildren(entry, entry.getData().toString())
            l3.addAll(l1)
            sortedList.addAll(l1)
            l1.removeAll(l1)
        }
        l2.removeAll(l2)
        l2.addAll(l3)
        l3.removeAll(l3)


    }

//  println(MyTreeNode.pair)

    println("SortedList size " + sortedList.size())
    int i = 1
    for (MyTreeNode entry : sortedList) {
        // print(entry.getData() + ",")
        String data = entry.getData()
        MyTreeNode.map.put(entry, i + "," + MyTreeNode.dataDict.get(data))
        i++
    }
    String text1 = ""
    for (String entry : MyTreeNode.map.values()) {
        //  println(entry)
        text1 += entry + "\n"

    }

    println(text1)


count=1
for(String entry:MyTreeNode.map.values()){
   // flowFile=session.putAttribute(flowFile,"priority"+count,entry)
    count++
}
println(count)

class MyTreeNode<T> {
    private T data = null
    private List<MyTreeNode> children = new ArrayList<>()
    private MyTreeNode parent = null
    static Map<String, String> pair = new LinkedHashMap<>()
    static Map<String, String> dataDict = new LinkedHashMap<>()
    static Map<String, String> map = new LinkedHashMap<>()


    public MyTreeNode(T data) {
        this.data = data
    }

    public void addChild(MyTreeNode child) {
        child.setParent(this)
        this.children.add(child)
    }

    public void addChild(T data) {
        MyTreeNode<T> newChild = new MyTreeNode<>(data)
        this.addChild(newChild)
    }

    public void addChildren(List<MyTreeNode> children) {
        for (MyTreeNode t : children) {
            t.setParent(this)
        }
        this.children.addAll(children)
    }

    public List<MyTreeNode> getChildren() {
        return children
    }

    public T getData() {
        return data
    }

    public void setData(T data) {
        this.data = data
    }

    private void setParent(MyTreeNode parent) {
        this.parent = parent
    }

    public MyTreeNode getParent() {
        return parent
    }


    public ArrayList<MyTreeNode> findChildren(MyTreeNode obj, String parent) {


        ArrayList<String> child = new ArrayList<String>(pair.values())
        ArrayList<String> child1 = new ArrayList<String>(pair.keySet())
        ArrayList<MyTreeNode> list = new ArrayList()

        loopBreak:
        while (child.contains(parent)) {


            int index = child.indexOf(parent)
            if (index == -1) {
                break loopBreak
            }

            String val = child1.get(index)
            MyTreeNode parentChild = new MyTreeNode(val)

            parentChild.setParent(obj)
            list.add(parentChild)

            child.remove(index)
            child1.remove(index)
            pair.remove(val)


        }
        return list
    }



}
