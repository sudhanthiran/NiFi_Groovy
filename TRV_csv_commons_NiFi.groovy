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
import org.apache.commons.io.IOUtils
import java.util.*
import java.io.Reader

flowFile = session.get()
if (!flowFile) return

flowFile = session.write(flowFile, { inputStream, outputStream ->

    String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)

    Reader in1 = new StringReader(text)
    CSVParser csvRecords1 = CSVFormat.EXCEL.withFirstRecordAsHeader().withDelimiter((Character)',').withQuote((Character)'"').parse(in1)
    List<CSVRecord> csvRecords = csvRecords1.getRecords()

//Writer writer = Files.newBufferedWriter(Paths.get("D:\\EAI\\Travellers\\Migration\\Sample Data\\ModifiedData.csv"))
    StringWriter writer1 = new StringWriter()

//CSVPrinter csvPrinter = CSVFormat.EXCEL.withHeader(csvRecords1.getHeaderNames().toString()).withDelimiter((Character)'|').withQuote((Character)'"').withQuoteMode(QuoteMode.ALL).print(writer)
//csvPrinter.printRecords(csvRecords)
//csvPrinter.flush()
//writer.close()

    CSVPrinter csvPrinter1 = CSVFormat.EXCEL.withDelimiter((Character)'|').withQuote((Character)'"').withQuoteMode(QuoteMode.ALL).print(writer1)
    csvPrinter1.printRecords(csvRecords)
    csvPrinter1.flush()
    writer1.close()


//println(writer1)

    outputStream.write(writer1.toString().getBytes(StandardCharsets.UTF_8))
} as StreamCallback)
session.transfer(flowFile, REL_SUCCESS)