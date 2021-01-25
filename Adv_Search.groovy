String reader = "74274,73089,854135\n" +
"74257,73072,854138\n" +
"74321,73136,854139\n" +
"74316,73131,854130\n" +
"74267,73082,854068\n" +
"74283,73098,854064\n" +
"74345,73160,854060\n" +
"74246,73061,854059\n" +
"74247,73062,854062\n" +
"74266,73081,854055\n"

String Filepath1= "D:\\EAI\\RBI\\Adv search API\\Vendor.properties"
//File propertiesFile = new File(Filepath)
Properties properties = new Properties()
//propertiesFile.withReader { reader ->
    reader.eachLine { line ->
        data = line.split(',')
        DBID = data[0]
        EntityID = data[1]
        ECCID= data[2]
        concat = DBID+','+EntityID
        properties.put(ECCID,concat)
        println(DBID)
    }
//}
File propertiesFile1 = new File(Filepath1)
properties.store(propertiesFile1.newWriter(), null)
