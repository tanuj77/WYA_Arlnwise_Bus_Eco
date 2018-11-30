package com.ccs.ccs81.wya_arlnwise_bus_eco;

/**
 * Created by ccs81 on 04/05/2017.
 */

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Banke Bihari on 5/12/2017.
 */

public class DropdownWebservice {

    String namespace = "http://tempuri.org/";
    private String url = null;
    String SOAP_ACTION;
    SoapObject request = null, objMessages = null;
    SoapSerializationEnvelope envelope;
    AndroidHttpTransport androidHttpTransport;

    private List<String> dropDownList = new ArrayList<>();

    DropdownWebservice() {
    }

    /**
     * Set Envelope
     */
    protected void SetEnvelope(String appUrl) {
        url = appUrl + "GetDataDropdown.asmx";
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
    public String getConvertedWeight(String webUrl, String MethodName, String compID, String method) {

        try {
            SOAP_ACTION = namespace + MethodName;

            //Adding values to request object
            request = new SoapObject(namespace, MethodName);
            //Adding String value to request object
            request.addProperty("Compid", compID);

            SetEnvelope(webUrl);

            try {

                //SOAP calling webservice
                androidHttpTransport.call(SOAP_ACTION, envelope);

                //Got Webservice response
                String result = envelope.getResponse().toString();
                if (result.equals("No Branch Found") || result.equalsIgnoreCase("No Branch Found") || result == "No Branch Found") {

                    return result;
                } else {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.optJSONArray("Table");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                        if (method.equalsIgnoreCase("Branch") || method.equals("Branch") || method == "Branch") {
                            dropDownList.add(jsonObject1.getString("Branchid"));
                        }

                    }

                    return String.valueOf(dropDownList);
                }
            } catch (Exception e) {
                // TODO: handle exception
                return e.toString();
            }
        } catch (Exception e) {
            // TODO: handle exception
            return e.toString();
        }

    }

    /************************************/
}