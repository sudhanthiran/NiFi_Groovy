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

flowFile = session.get()
if(!flowFile) return


url=nifi_token_url.evaluateAttributeExpressions(flowFile).value
username=nifi_token_username.evaluateAttributeExpressions(flowFile).value
password=nifi_token_password.evaluateAttributeExpressions(flowFile).value
pId=Process_Group_ID.evaluateAttributeExpressions(flowFile).value
Variable_Name=Variable_Name.evaluateAttributeExpressions(flowFile).value
Variable_Value=Variable_Value.evaluateAttributeExpressions(flowFile).value


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
    put.getOutputStream().write(payload.getBytes("UTF-8"))

    def StartResponse = put.getResponseCode()
    println(StartResponse)

    println("Variable has been updated\n" + put.getInputStream().getText())

    if (StartResponse != 202 && StartResponse != 200)
    {
        throw new Exception("Error Code"+ StartResponse +"Payload "+payload);
    }


    session.transfer(flowFile, REL_SUCCESS)
}

catch(Exception e){
    e.printStackTrace()
    flowFile = session.putAttribute(flowFile, "Error_Update_Variable_Script", e.getMessage())
    session.transfer(flowFile,REL_FAILURE)
}