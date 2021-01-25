import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

String setRiskAssessment(json, fieldProperties,fieldValue) {
    fieldId = fieldProperties.name
    boolean matchFound = false
    errorMessage =""
    if(fieldId !="")
    {
        options = json.body.data.dynamicMetadata[fieldId].options.data
        options.each{ option ->
            if(option.name.equalsIgnoreCase(fieldValue)){
                json.body.data.dynamicMetadata[fieldId].values = option
                matchFound = true
            }
        }
    }
    if (!matchFound) {
        errorMessage = fieldProperties.uiName + " : No match found in dropdown for "+ fieldValue + ". "
    }
    return errorMessage
}

def ff = session.get()


def errorOccured = false
def errorMessage = ""
if(!ff) return

try{
ff = session.write(ff, {rawIn, rawOut->

    text =IOUtils.toString(rawIn, StandardCharsets.UTF_8)
    jsonSlurper = new JsonSlurper()
    json = jsonSlurper.parseText(text)

    RISK_TYPE = RISK_TYPE.evaluateAttributeExpressions(ff).value//"RELATION_RISK" //SUPPLIER_RISK
    RISK_ASSESSMENT = RISK_ASSESSMENT.evaluateAttributeExpressions(ff).value//"Other: Incident"//"Legal CDAT" //

    dyn_mapping_text =dyn_mapping_text.evaluateAttributeExpressions(ff).value//'{"dyn_Supplier_Level_Risk_Assessment_text":{"name": "dyn113773","uiName": "Supplier Level Risk Assessment"},"dyn_Relationship_Level_Risk_Assessment_text": {"name": "dyn113774","uiName": "Relationship Level Risk Assessment"},"dyn_Other_Risk_Assessment_text": {"name": "dyn113775","uiName": "Risk Assessment If Other"}}'

    dyn_mapping = jsonSlurper.parseText(dyn_mapping_text)
    dyn_Supplier_Level_Risk_Assessment = dyn_mapping.dyn_Supplier_Level_Risk_Assessment_text
    dyn_Relationship_Level_Risk_Assessment = dyn_mapping.dyn_Relationship_Level_Risk_Assessment_text
    dyn_Other_Risk_Assessment = dyn_mapping.dyn_Other_Risk_Assessment_text

    fieldId =""
    fieldProperties =""
    fieldValue = RISK_ASSESSMENT

    errorMessage =""

    if (RISK_TYPE =="RELATION_RISK"){
        fieldId = dyn_Relationship_Level_Risk_Assessment.name
        fieldProperties = dyn_Relationship_Level_Risk_Assessment
    }
    else if(RISK_TYPE =="SUPPLIER_RISK"){
        fieldId = dyn_Supplier_Level_Risk_Assessment.name
        fieldProperties = dyn_Supplier_Level_Risk_Assessment
    }
    else {
        errorMessage += "Not a valid RISK TYPE. Data Received from feed file: "+RISK_TYPE
    }

    if(fieldId !="" && !fieldValue.toLowerCase().startsWith("other"))//
    {
        errorMessage +=setRiskAssessment(json,fieldProperties,fieldValue)
    }
    else if(fieldId !="" && fieldValue.toLowerCase().startsWith("other"))
    {
        if (RISK_TYPE =="RELATION_RISK"){
            errorMessage +=setRiskAssessment(json,fieldProperties,"Other (it has a free text along)")
        }
        else if(RISK_TYPE =="SUPPLIER_RISK"){
            errorMessage +=setRiskAssessment(json,fieldProperties,"Other")
        }
        json.body.data.dynamicMetadata[dyn_Other_Risk_Assessment.name].values = fieldValue.substring(fieldValue.indexOf(":")+1,fieldValue.length()).trim()
    }

    rawOut.withWriter("UTF-8"){ it.write( JsonOutput.toJson(json) )}
} as StreamCallback)
    if (errorMessage.length() > 0) {
        ff = session.putAttribute(ff, 'Error', errorMessage)
        ff = session.putAttribute(ff, 'status', 'failure')
        session.transfer(ff, REL_FAILURE)
    }
    else {
        session.transfer(ff, REL_SUCCESS)
    }
}
catch (Exception e){
    ff = session.putAttribute(ff, "Error", e.getMessage())
    session.transfer(ff, REL_FAILURE)
}