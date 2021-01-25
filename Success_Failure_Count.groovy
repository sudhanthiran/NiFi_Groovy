import java.util.*
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets






propertiesFileloc = "C:\\Users\\sudhanthiran.b\\Downloads\\RBUA_PO_SUPPLIER_20191108_27261182_Status (1).csv"
String text = new File(propertiesFileloc).text
   // String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
int i=0
Total_Count = "8688"

Total_Count = Total_Count.toInteger()
Word = "Failure"

//    ArrayList str = text.split("\n").toList()
//    println(str.size())
//
//    for (String element : str) {
//        ArrayList str1 = element.split(",")
//        String Status = str1.getAt(3)
//
//
//        println("tet data\n" + element)
//    }
//    println(text1)

Pattern p = Pattern.compile(Word);
Pattern p1 = Pattern.compile("Success");
Matcher m = p.matcher( text );
while (m.find()) {
    i++;
}

println(i)

println(Total_Count - i)

