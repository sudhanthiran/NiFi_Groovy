import groovy.json.*
def flowFile = session.get()
if(!flowFile) return
flowFile = session.write(flowFile, {inputStream, outputStream ->
    jsonSlurper = new JsonSlurper()
    def json = jsonSlurper.parse(inputStream)
    json.remove("header")
    json.remove("session")
    json.remove("actions")
    json.remove("createLinks")
    json.body.remove("layoutInfo")
    json.body.remove("globalData")
    json.body.remove("errors")

    key = key.evaluateAttributeExpressions(flowFile).value

    Map map = new HashMap()
    map.put("key",key)
    map.put("performanceData",false)
    map.put("searchable",false)
    map.put("legal",false)
    map.put("financial",false)
    map.put("businessCase",false)
    def temp_json = jsonSlurper.parseText('[]')
    temp_json.putAt(0,map)

    json.body.data.comment.commentDocuments.values = temp_json


//    json.body.data.comment.commentDocuments.values[0].key = key
//    json.body.data.comment.commentDocuments.values[0].performanceData = false
//    json.body.data.comment.commentDocuments.values[0].searchable = false
//    json.body.data.comment.commentDocuments.values[0].legal = false
//    json.body.data.comment.commentDocuments.values[0].financial = false
//    json.body.data.comment.commentDocuments.values[0].businessCase = false



    outputStream.withWriter("UTF-8"){ it.write( JsonOutput.toJson(json) )} } as StreamCallback)
session.transfer(flowFile, REL_SUCCESS)
