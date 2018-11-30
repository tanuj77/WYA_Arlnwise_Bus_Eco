package com.ccs.ccs81.wya_arlnwise_bus_eco;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Banke Bihari on 10/1/2016.
 */
public class SecondWebservice {

    /**
     * Variable Decleration................
     */
    String namespace = "http://tempuri.org/";

    private String url = null;
    String SOAP_ACTION;
    SoapObject request = null;
    SoapSerializationEnvelope envelope;
    AndroidHttpTransport androidHttpTransport;


    private List<String> listAirline = new ArrayList<>();
    private List<String> listAirlineName = new ArrayList<>();
    private List<String> listBranch = new ArrayList<>();
    private List<String> listBusiness = new ArrayList<>();
    private List<String> listBusinessAmount = new ArrayList<>();
    private List<String> listEco = new ArrayList<>();
    private List<String> listEcoAmount = new ArrayList<>();
    private List<String> listFirst = new ArrayList<>();
    private List<String> listFirstAmount = new ArrayList<>();
    private List<String> listPremium = new ArrayList<>();
    private List<String> listPremiumAmount = new ArrayList<>();


    private CustomAdapter mAdapter;

    SecondWebservice() {
    }


    /**
     * Set Envelope
     */
    protected void SetEnvelope(String appUrl) {
        url = appUrl + "WSMP_ArlnwiseBusinessEconomy.asmx";
        try {

            // Creating SOAP envelope
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //You can comment that line if your web service is not .NET one.
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new AndroidHttpTransport(url);
            androidHttpTransport.debug = true;

        } catch (Exception e) {
            System.out.println("Soap Exception---->>>" + e.toString());
        }
    }

    // MethodName variable is define for which webservice function  will call
    public String getConvertedWeight(String webUrl, String MethodName, String companyID, String branchName, String startDate, String endDate, String checkBF ,String checkYQ,String checkYR,String total,String rbFlagBE,String rgFlag,String mobileNo) {

        try {
            SOAP_ACTION = namespace + MethodName;

            //Adding values to request object
            request = new SoapObject(namespace, MethodName);
            request.addProperty("CompId", "" + companyID);
            request.addProperty("BranchName", "" + branchName.trim());
            request.addProperty("BUSINESS_ECONOMY", "" + rbFlagBE.trim());
            request.addProperty("ECO_BUS_PRM_FRST_ALL",""+rgFlag.trim());
            request.addProperty("Both","");
            request.addProperty("Basic_Fare", ""+checkBF.trim());
            request.addProperty("TK_YQ", "" + checkYQ.trim());
            request.addProperty("TK_YR", "" + checkYR.trim());
            request.addProperty("Total",""+total);
            request.addProperty("StrStrtDt", "" + startDate.toString().trim());
            request.addProperty("StrEndDt", "" + endDate.trim());
            request.addProperty("Mob", "" + mobileNo);
            SetEnvelope(webUrl);

            try {
                //SOAP calling webservice
                androidHttpTransport.call(SOAP_ACTION, envelope);
                //Got Webservice response
                String result = envelope.getResponse().toString();
                if (result.trim().equals("No Data Found") || result.trim().equalsIgnoreCase("No Data Found") || result.trim() == "No Data Found") {

                    return result;
                } else if (result.equalsIgnoreCase("Party doesn't belongs to this branch") || result.equals("Party doesn't belongs to this branch") || result == "Party doesn't belongs to this branch") {
                    return result;
                } else {
                    JSONObject jsonRootObject = new JSONObject(result);

                    // table= {{"AIRLINE":"098","BASFARE":"90"},{"AIRLINE":"098","BASFARE":"90"}};
                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray = jsonRootObject.optJSONArray("Table");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        listAirline.add(jsonObject.optString("AIRLINE").toString());
                        listAirlineName.add(jsonObject.optString("AIRLINENAME").toString());
                        listBranch.add(jsonObject.getString("BRANCHID").toString());
                        listBusiness.add(jsonObject.getString("BUSINESS").toString());
                        listBusinessAmount.add(jsonObject.getString("BUSINESS_AMOUNT"));
                        listEco.add(jsonObject.optString("ECO").toString());
                        listEcoAmount.add(jsonObject.getString("ECO_AMOUNT").toString());
                        listFirst.add(jsonObject.getString("FIRST").toString());
                        listFirstAmount.add(jsonObject.getString("FIRST_AMOUNT").toString());
                        listPremium.add(jsonObject.getString("PREMIUM").toString());
                        listPremiumAmount.add(jsonObject.getString("PREMIUM_AMOUNT").toString());

                    }
                    return listAirline + "%" + listBranch + "%" + listBusiness + "%" + listBusinessAmount + "%" + listEco + "%" + listEcoAmount + "%" + listFirst + "%" + listFirstAmount + "%" + listPremium + "%" + listPremiumAmount+ "%" + listAirlineName;

                }

            } catch (Exception e) {
                // TODO: handle exception

                String error = "Something went wrong at server side or wrong input";
                //return e.toString();
                return error;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String error = "Something went wrong at server side or wrong input";
            //return e.toString();
            return error;
        }

    }
    /************************************/
}
