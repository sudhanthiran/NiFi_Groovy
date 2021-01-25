import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

CCMDecisionMaker_Data ='[{"name":"Not Applicable","id":4499,"idType":1,"properties":{"Contact Number":"","Designation":"","Default Tier":"View All","Email":"fake_not.applicable@sirionlabs.com_fake","First Name":"Not","Legal Document":"No","Exclude From Filters":" - ","Business Case":"No","Financial Document":"No","Time Zone:":"Asia/Kolkata (GMT +05:30)","User Department":" - ","Last Name":"Applicable","User By":" - "}}]'
StakeHolderID_CCMDecisionMaker = 'rg_2262'
file = new File("D:\\EAI\\Vodafone\\data\\show page data.txt").text
jsonSlurper = new JsonSlurper()
def json = jsonSlurper.parseText(file)
//    StakeHolderID_CCMDecisionMaker = StakeHolderID_CCMDecisionMaker.evaluateAttributeExpressions(flowFile).value
//    CCMDecisionMaker_Data = CCMDecisionMaker_Data.evaluateAttributeExpressions(flowFile).value

//println(json.body.data.stakeHolders.values)
ccm_json = jsonSlurper.parseText(CCMDecisionMaker_Data)
def temp = [:]
temp.putAt("values",ccm_json)
json.body.data.stakeHolders.values.putAt(StakeHolderID_CCMDecisionMaker,temp)

println(json)