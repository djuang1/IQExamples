package com.inquira.examples;

import java.net.URL;
import java.util.Vector;

import org.apache.soap.Fault;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Response;


public class WSCall {

	protected String serviceId;

	protected URL serviceURL;

	protected Integer timeout;

	public void setConnectionProperties(String urn, String url,
			String timeoutString) {

		try {

			serviceId = url;

			String tmp = url;

			serviceURL = new URL(tmp);

			tmp = timeoutString;

			if (tmp != null && (tmp = tmp.trim()).length() > 0)

				timeout = new Integer(tmp);

		} catch (Exception ex) {

			ex.printStackTrace();

			RuntimeException rex = new RuntimeException(

			"bad soap configuration");

			throw rex;

		}

	}

	public void process(String inXML) throws Exception {

		try {

			Call call = new Call();
	        call.setTargetObjectURI("urn:inquira");
	        call.setMethodName("process");
	        call.setEncodingStyleURI("http://schemas.xmlsoap.org/soap/encoding/");
	        Vector params = new Vector();
	        Parameter p = new Parameter("xmlRequest", String.class, inXML, null);
	        params.addElement(p);
	        call.setParams(params);
	        Response response = call.invoke(new URL("http://199.204.218.230:8223/inquiragw/servlet/rpcrouter"), "");
	        if(response.generatedFault())
	        {
	            Fault fault = response.getFault();
	            System.err.println("The call failed: ");
	            System.err.println((new StringBuilder()).append("Fault Code   = ").append(fault.getFaultCode()).toString());
	            System.err.println((new StringBuilder()).append("Fault String = ").append(fault.getFaultString()).toString());
	        } else
	        {
	            Parameter result = response.getReturnValue();
	            System.out.println("Returned response : ");
	            System.out.println(result.getValue());
	        }

		} catch (ThreadDeath td) {

			throw td;

		} catch (Throwable t) {

			throw new Exception("SOAP call failed.", t);

		}

	}

	public static void main(String args[]) {

		String xml = "<message type=\"request\"><params><parameter name=\"question\">redhat</parameter></params></message>";

		String urn = "urn:xml-soap-service-management-service";

		String url = "http://199.204.218.230:8223/inquiragw/servlet/rpcrouter";

		String timeout = "10";

		try {

			WSCall client1 = new WSCall();

			client1.setConnectionProperties(urn, url, timeout);

			client1.process(xml);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
