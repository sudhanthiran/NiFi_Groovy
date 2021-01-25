import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.net.ssl.HostnameVerifier
import groovy.json.JsonSlurper

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader


url="https://euintegration.sirioninc.net:8443/"
username="admin"
password="password"
pId="1f031628-17d0-15e5-4342-28acd7f57713"
Variable_Name = "RBI_Supplier_Migration_AuthToken"
Variable_Value="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXlsb2FkIjoiSUxFVXlVaVJaU0daUmYvS1E0N0MzUjVmVGFtSnZDTWg4OTFtTWpHbm93QVk0ZzJRNWtoN3RhcWxHc2xpVjNCR3ZGenFUSC9TUC82TlBsWUozUGgyNUFGNG5wTHNYOVlQYzRnVW11dkdWaElnTGxXUm5sdnBmbUUrTzI0WjhSV2YxSkZlT3FkaGdvaEpkS1RTb1ZHa2xPRGdiTFJuVjZXRVNFVkppWm9aT29FPSIsImlzcyI6InNpcmlvbiIsImV4cCI6MTU3OTcyMDA2M30.YRiX475Fz9xjyo6mh3NrDW-EcY7sSe2_f0FOlo7NOjc"


def nullTrustManager = [
        checkClientTrusted: { chain, authType -> },
        checkServerTrusted: { chain, authType -> },
        getAcceptedIssuers: { null }
]

def nullHostnameVerifier = [
        verify: { hostname, session -> true }
]


try {
    SSLContext sc = SSLContext.getInstance("SSL")
    sc.init(null, [nullTrustManager as X509TrustManager] as TrustManager[], null)
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
    HttpsURLConnection.setDefaultHostnameVerifier(nullHostnameVerifier as HostnameVerifier)


    def post = new URL(url + "nifi-api/access/token?username=" + username + "&password=" + password).openConnection()
    post.setRequestMethod("POST")
    post.setDoOutput(true)
    post.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")

    def getBT = post.getResponseCode()
    println(getBT);

    result = post.getInputStream().getText()
    println("Bearer Token\n" + result)

    def get = new URL(url + "nifi-api/process-groups/" + pId).openConnection()
    get.setRequestMethod("GET")
    get.setDoOutput(true)
    get.setRequestProperty("Content-Type", "application/json")
    get.setRequestProperty("Authorization", "Bearer " + result)
//get.getOutputStream().write(message.getBytes("UTF-8"));

    def getRC = get.getResponseCode()
    println(getRC);
    Response = get.getInputStream().getText()
    println("Response \n" + Response)

    JsonSlurper slurper = new JsonSlurper()
    Map parsedJson = slurper.parseText(Response)

    version = parsedJson.get("revision").get("version")
    clientid = parsedJson.get("revision").get("clientId")
    print("version\n" + version)
    print("clientid\n" + clientid)

    payload = "{\n" +
            "\t\"processGroupRevision\": {\n" +
            "\t\t\"clientId\": \"" + clientid + "\",\n" +
            "\t\t\"version\":" + version + "\n" +
            "\t},\n" +
            "\t\"disconnectedNodeAcknowledged\": false,\n" +
            "\t\"variableRegistry\": {\n" +
            "\t\t\"processGroupId\": \"" + pId + "\",\n" +
            "\t\t\"variables\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"variable\": {\n" +
            "\t\t\t\t\t\"name\": \""+Variable_Name+"\",\n" +
            "\t\t\t\t\t\"value\": \""+Variable_Value+"\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}\n" +
            "}"

    println("payload\n" + payload)

    def put = new URL(url + "nifi-api/process-groups/" + pId+"/variable-registry/update-requests").openConnection()
    put.setRequestMethod("POST")
    put.setDoOutput(true)
    put.setRequestProperty("Content-Type", "application/json")
    put.setRequestProperty("Authorization", "Bearer " + result)
   // put.getOutputStream().write(payload.getBytes("UTF-8"))

    def StartResponse = put.getResponseCode()
    println(StartResponse)

    if (StartResponse != 202 && StartResponse != 200)
    {
        throw new Exception("Error Code"+ StartResponse +"Payload "+payload);
    }

   // println("Variable has been updated\n" + put.getInputStream().getText())



}

catch(Exception e){
    println(e.getMessage())
}
