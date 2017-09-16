package com.shapeomatic.Networking;

import android.util.Log;

import com.shapeomatic.Controller.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alex on 11/6/2015.
 */
public class RestClient {

    public class RestResponseException extends Exception {
        public int errorCode;

        public RestResponseException(int errorCode) {
            this.errorCode = errorCode;
        }
    }

    public enum RequestMethod
    {
        GET,
        POST,
        PUT,
        DELETE,
    }
    private String urlString;

    public RestClient(String url)
    {
        this.urlString = url;
    }

    public String execute(RequestMethod method, String requestBody)
            throws IOException, RestResponseException
    {
        Log.d(Settings.TAG, "Starting retrieving information from remote server.");
        String response = null;
        URL url = new URL(urlString);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setRequestProperty("Accept", "text/xml");
        httpCon.setRequestProperty("content-type", "text/xml");
//        httpCon.setRequestProperty(Settings.API_VERSION_HEADER, String.valueOf(Settings.clientVersion));
        httpCon.setConnectTimeout(Settings.CONNECT_TIMEOUT);
        httpCon.setReadTimeout(Settings.READ_TIMEOUT);
        try {
            OutputStreamWriter out;
            switch (method) {
                case GET:
                    httpCon.setRequestMethod("GET");
                    httpCon.setDoOutput(false);
                    break;
                case POST:
                    httpCon.setRequestMethod("POST");
                    httpCon.setDoOutput(true);
                    out = new OutputStreamWriter(
                            httpCon.getOutputStream());
                    out.write(requestBody);
                    out.close();
                    break;
                case PUT:
                    httpCon.setRequestMethod("PUT");
                    httpCon.setDoOutput(true);
                    out = new OutputStreamWriter(
                            httpCon.getOutputStream());
                    out.write(requestBody);
                    out.close();
                    break;
                case DELETE:
                    httpCon.setRequestMethod("DELETE");
                    httpCon.setDoOutput(false);
                    break;
                default:
                    return null;
            }
            //if(method == RequestMethod.GET || method == RequestMethod.DELETE)
                httpCon.connect();
            int responseCode = httpCon.getResponseCode();
            if(responseCode >= 400) {
                Log.d(Settings.TAG, "Server returned error. code: " + responseCode);
                throw new RestResponseException(responseCode);
            }
            response = convertStreamToString(httpCon.getInputStream());
            Log.d(Settings.TAG, "Successfully retrieved information from remote server.");
        }
        finally {
            httpCon.disconnect();
        }
        return response;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
