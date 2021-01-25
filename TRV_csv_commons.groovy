import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.apache.commons.csv.*

import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Matcher
import java.util.regex.Pattern
//import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import java.util.*
import java.io.Reader


    String text = new File("D:\\EAI\\Travellers\\Migration\\Sample Data\\data.csv").text

    Reader in1 = new StringReader(text)
    CSVParser csvRecords1 = CSVFormat.EXCEL.withFirstRecordAsHeader().withDelimiter((Character)',').withQuote((Character)'"').parse(in1)
    List<CSVRecord> csvRecords = csvRecords1.getRecords()

//for (CSVRecord record : csvRecords) {
//    String field_1 = record.get(2)
//    String field_2 = record.get(1)
//    println(field_1)
//    println(field_2)
//}


//Writer writer = Files.newBufferedWriter(Paths.get("D:\\EAI\\Travellers\\Migration\\Sample Data\\ModifiedData.csv"))
//CSVPrinter csvPrinter = CSVFormat.EXCEL.withHeader(csvRecords1.getHeaderNames().toString()).withDelimiter((Character)'|').withQuote((Character)'"').withQuoteMode(QuoteMode.ALL).print(writer)
//csvPrinter.printRecords(csvRecords)
//csvPrinter.flush()
//writer.close()

StringWriter writer1 = new StringWriter()
CSVPrinter csvPrinter1 = CSVFormat.EXCEL.withDelimiter((Character)'|').withQuote((Character)'"').withQuoteMode(QuoteMode.ALL).print(writer1)
csvPrinter1.printRecords(csvRecords)
csvPrinter1.flush()
writer1.close()


println(writer1.toString())