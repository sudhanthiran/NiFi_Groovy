import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import org.apache.commons.csv.QuoteMode

import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Matcher
import java.util.regex.Pattern
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import java.util.*
import java.io.Reader


flowFile = session.get()
if (!flowFile) return

flowFile = session.write(flowFile, { inputStream, outputStream ->

    String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)

    Reader in1 = new StringReader(text)
    /* final String SAMPLE_CSV_FILE_PATH = "/home/tomcat7/integration/creditsuisse/ivalua/resources/csv/IVALUA_CSV_FILE.csv"
     Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH))*/
    //CSVParser csvRecords1 = new CSVParser(in1, CSVFormat.EXCEL.withDelimiter((Character)',').withQuote('"'))
    Iterable<CSVRecord> csvRecords1 = CSVFormat.EXCEL.withDelimiter((Character)',').withQuote((Character)'"').parse(in1)
    List<CSVRecord> csvRecords = csvRecords1.getRecords();
    //  println("Total Records==" + csvRecords.size())
    BigInteger firstTime = System.currentTimeMillis()
    for (CSVRecord csvRecord : csvRecords) {
        String Sui = csvRecord.get(26)
        String Pui = csvRecord.get(13)
        MyTreeNode.pair.put(Sui, Pui)
        StringWriter writer1 = new StringWriter()
        CSVPrinter csvPrinter1 = CSVFormat.EXCEL.withDelimiter((Character)'|').withQuote((Character)'"').withQuoteMode(QuoteMode.ALL).print(writer1)
        //csvPrinter1.printRecords(csvRecord)
        csvPrinter1.printRecord(csvRecord)
        csvPrinter1.flush()
        writer1.close()
        //println(writer1.toString())
        MyTreeNode.dataDict.put(Sui, writer1.toString())



    }

    /*ArrayList str = text.split("\n").toList()
    for (String element : str) {
        ArrayList str1 = element.split(",")
        String Sui = str1.getAt(0)
        String Pui = str1.getAt(1)
        MyTreeNode.pair.put(Sui, Pui)
        MyTreeNode.dataDict.put(Sui, element)


    }
*/
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
//println("Rootnode\n" + rootNode)

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
    Map<String, String> map = new LinkedHashMap<>()
    //  println("SortedList size " + sortedList.size())
    int i = 1
    for (MyTreeNode entry : sortedList) {
        // print(entry.getData() + ",")
        String data = entry.getData()
        map.put(entry, "\"" + i + "\"|" + MyTreeNode.dataDict.get(data))
        i++
    }
/*String text1 = ""

for (String entry : map.values()) {

    //  println(entry)

    text1 += entry.get + "\n"



}*/
    StringBuffer text1 = new StringBuffer()
//    int counter = 1;
//    for (String entry : map.values()) {
//        String[] breakByRegex = entry.split("values=");
//        String requiredString = breakByRegex[1];
//        requiredString = requiredString.substring(1, requiredString.length() - 2)
//        String[] addLikeCSVFormat = requiredString.split(",");
//        String temp = "";
//
//        String countString = "\"" + counter + "\"" + ","
//        for (String str1 : addLikeCSVFormat) {
//            temp = "";
//            temp += countString + "\"" + str1.trim() + "\""
//            text1.append(temp + ",")
//            countString = "";
//
//        }
//        text1 = new StringBuffer(text1.substring(0,text1.length()-1))
//        counter++
//
//        text1.append(System.getProperty("line.separator"))
//
//    }

    //  println(text1)
    for (String entry : map.values()) {
        text1.append(entry)
    }


    outputStream.write(text1.toString().getBytes(StandardCharsets.UTF_8))
} as StreamCallback)
session.transfer(flowFile, REL_SUCCESS)


class MyTreeNode<T> {
    private T data = null
    private List<MyTreeNode> children = new ArrayList<>()
    private MyTreeNode parent = null
    static Map<String, String> pair = new LinkedHashMap<>()
    static Map<String, String> dataDict = new LinkedHashMap<>()


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