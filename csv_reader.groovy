@Grab('com.xlson.groovycsv:groovycsv:1.3')
import static com.xlson.groovycsv.CsvParser.parseCsv
import groovy.json.*
//import static com.xlson.groovycsv.PropertyMapper

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


fh = new File('C:\\Users\\sudhanthiran.b\\Desktop\\Multi Line CSV\\SIRION_CREDITSUISSE_CON_10242019.csv')

def csv_content = fh.getText('utf-8')

def data_iterator = parseCsv(csv_content, separator: '|', readFirstLine: false)


for (line in data_iterator)
    {
        str1 = line.toMap()
        a= (line.values).join(",")
        String Sui = str1.get("CONTRACT_ID")
        String Pui = str1.get("PARENT_CONTRACT_ID")
        MyTreeNode.pair.put(Sui, Pui)
        MyTreeNode.dataDict.put(Sui, a)

        println("tet data\n" + str1)

}