import java.util.*
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
//@Grab('com.xlson.groovycsv:groovycsv:1.3')
@Grab(group='com.xlson.groovycsv', module='groovycsv', version='1.3')
import static com.xlson.groovycsv.CsvParser.parseCsv
//import com.opencsv.CSVReader
//@Grab('com.xlson.groovycsv:groovycsv:1.3')
/*

@Grapes(@Grab(group='com.xlson.groovycsv', module='groovycsv', version='1.3'))
import static com.xlson.groovycsv.CsvParser.parseCsv
*/


flowFile = session.get()
if (!flowFile) return



flowFile = session.write(flowFile, { inputStream, outputStream ->

    String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)

    def csv_content = "CONTRACT_ID,PARENT_CONTRACT_ID,CS_VENDOR_ID,PARENT_VENDOR_ID,SUPERPARENT_VENDORID,STATUS,CONTRACT_TYPE,CONTRACT_NAME,CONTRACT_DESCRIPTION,RRP_APPLICABLE,RENEWAL_TYPE,CONFIDENTIAL,CONTRACT_LANGUAGE,TPRM_ID,CONTRACTING_LEGAL_ENTITY,VENDOR_CONTRACTING_ENTITY,IRR,RRR,CONTRACT_START_DATE,CONTRACT_END_DATE,SOURCING_CATEGORY,SUB_CATEGORY,REGIONS,COUNTRIES,CURRENCY,TCV_OF_CURRENT_CONTRACT,RENEWAL_TERMS,PERPETUAL,DORMANT_CONTRACT,MODIFIED_DATE,MODIFIED_BY,VALIDITY_STATUS,AUTOMATIC_RENEWAL,ADDITIONAL_CS_LEGAL_ENTITIES,GL_OR_PU_BU,OUTSOURCING_TYPE,SOURCING_CATEGORYCODE,SUB_CATEGORYCODE\n" +text
//def csv_content = text
    def data_iterator = parseCsv(csv_content, separator: '|', readFirstLine: false)


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

//    ArrayList str = text.split("\n").toList()
//
//    for (String entry : str) {
//        println(entry)
//
//    }
//
//    println(str.size())
//
//    for (String element : str) {
//        ArrayList str1 = element.split(",")
//        String Sui = str1.getAt(0)
//        String Pui = str1.getAt(1)
//        MyTreeNode.pair.put(Sui, Pui)
//        MyTreeNode.dataDict.put(Sui, element)
//
//        println("tet data\n" + element)
//    }
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

outputStream.write(text1.trim().getBytes(StandardCharsets.UTF_8))
} as StreamCallback)
count=1
for(String entry:MyTreeNode.map.values()){
    flowFile=session.putAttribute(flowFile,"priority"+count,entry)
    count++
}
session.transfer ( flowFile , REL_SUCCESS )

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
