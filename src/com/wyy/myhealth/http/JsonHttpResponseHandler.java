package com.wyy.myhealth.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonHttpResponseHandler extends AsyncHttpResponseHandler {

	//
	// Callbacks to be overridden, typically anonymously
	//

	/**
	 * Fired when a request returns successfully and contains a json object at
	 * the base of the response string. Override to handle in your own code.
	 * 
	 * @param response
	 *            the parsed json object found in the server response (if any)
	 */
	public void onSuccess(JSONObject response) {
	}

	/**
	 * Fired when a request returns successfully and contains a json array at
	 * the base of the response string. Override to handle in your own code.
	 * 
	 * @param response
	 *            the parsed json array found in the server response (if any)
	 */
	public void onSuccess(JSONArray response) {
	}

	// Utility methods
	@Override
	protected void handleSuccessMessage(String responseBody) {
		super.handleSuccessMessage(responseBody);

		try {
			Object jsonResponse = parseResponse(responseBody);
			if (jsonResponse instanceof JSONObject) {
				onSuccess((JSONObject) jsonResponse);
			} else if (jsonResponse instanceof JSONArray) {
				onSuccess((JSONArray) jsonResponse);
			} else {
				throw new JSONException("Unexpected type "
						+ jsonResponse.getClass().getName());
			}
		} catch (JSONException e) {
			onFailure(e, responseBody);
		}
	}

	protected Object parseResponse(String responseBody) throws JSONException {
		if (responseBody == null) {
			return "";
		}
		return new JSONTokener(responseBody).nextValue();
	}

	/**
	 * Handle cases where a failure is returned as JSON
	 */
	public void onFailure(Throwable e, JSONObject errorResponse) {
	}

	public void onFailure(Throwable e, JSONArray errorResponse) {
	}

	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		super.handleFailureMessage(e, responseBody);
		if (responseBody != null)
			try {
				Object jsonResponse = parseResponse(responseBody);
				if (jsonResponse instanceof JSONObject) {
					onFailure(e, (JSONObject) jsonResponse);
				} else if (jsonResponse instanceof JSONArray) {
					onFailure(e, (JSONArray) jsonResponse);
				}
			} catch (JSONException ex) {
				onFailure(e, responseBody);
			}
		else {
			onFailure(e, "");
		}
	}

}
