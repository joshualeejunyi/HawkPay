package cxa.overclockedtoaster.hawkpay;

import android.renderscript.ScriptGroup;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class actuallyml {

    public static String imageresult(byte[] file, String imagefilepath) {
        DataOutputStream dataOut = null;
        BufferedReader in = null;
        ByteArrayInputStream fileInputStream = null;
        String newresult = "";

        try {

            String url = "https://sandbox.api.sap.com/ml/imageclassification/classification";

            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            // setting request method
            connection.setRequestMethod("POST");

            // adding headers
            connection.setRequestProperty("Content-Type", "multipart/form-data");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("APIKey", "3aafz0LIZ85wqhQKfIgFT5Ls9vlYsLrF");

            connection.setDoInput(true);

            // sending POST request
            connection.setDoOutput(true);

            // read the input file name from user input
            Scanner scanner = new Scanner(System.in);

            // prepare the constant for the form data
            String LINE_FEED = "\r\n";
            String SEPARATOR = "--";
            String BOUNDARY = "------Boundary" + new BigInteger(128, new SecureRandom()).toString(32);

            // set the form content as multipart
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            // open the input file
            fileInputStream = new ByteArrayInputStream(file);

            // write the form data content
            dataOut = new DataOutputStream(connection.getOutputStream());
            dataOut.writeBytes(SEPARATOR + BOUNDARY + LINE_FEED);
            System.out.println(imagefilepath);
            dataOut.writeBytes("Content-Disposition: form-data; name=\"files\"; filename=\"" + imagefilepath + "\"" + LINE_FEED);
            dataOut.writeBytes(LINE_FEED);

            // read the file as byte array
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = fileInputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dataOut.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }

            // finish the form content
            dataOut.writeBytes(LINE_FEED);
            dataOut.writeBytes(SEPARATOR + BOUNDARY + SEPARATOR + LINE_FEED);
            dataOut.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // convert json string to jsonobj
            String ans = response.toString();
            JSONTokener tokener = new JSONTokener(ans);
            JSONObject root = new JSONObject(tokener);
            JSONArray predict = root.getJSONArray("predictions");
            for (int i = 0; i < predict.length(); i++)
            {
                JSONObject jobj = predict.getJSONObject(i);
                JSONArray label = jobj.getJSONArray("results");

                JSONObject result = label.getJSONObject(0);
                newresult = label.toString();
                System.out.println(newresult);

            }


        } catch (Exception e) {
            // do something with exception
            e.printStackTrace();
        } finally {
            try {
                if (dataOut != null) {
                    dataOut.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                // do something with exception
                e.printStackTrace();
            }
        }

        return newresult;
    }
}
