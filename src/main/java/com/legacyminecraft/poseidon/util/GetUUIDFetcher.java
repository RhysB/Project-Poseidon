package com.legacyminecraft.poseidon.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.projectposeidon.johnymuffin.UUIDManager.generateOfflineUUID;

public class GetUUIDFetcher {


    public static UUIDResult getUUID(String username) {
        try {
            JSONObject jsonObject = readJsonFromUrl("https://api.mojang.com/users/profiles/minecraft/" + encode(username));
            if (!jsonObject.containsKey("id")) {
                return new UUIDResult(generateOfflineUUID(username), UUIDResult.ReturnType.OFFLINE);
            }
            UUID uuid = toUUIDWithDashes(String.valueOf(jsonObject.get("id")));
            if (uuid == null) {
                return new UUIDResult(null, UUIDResult.ReturnType.API_OFFLINE);
            }

            return new UUIDResult(uuid, UUIDResult.ReturnType.ONLINE);


        } catch (Exception exception) {
            UUIDResult uuidResult;
            if (exception == null || exception.getMessage() == null) {
                //This is a hacky solution to tell if the API is offline, or the user is cracked.
                uuidResult = new UUIDResult(generateOfflineUUID(username), UUIDResult.ReturnType.OFFLINE);
            } else {
                uuidResult = new UUIDResult(null, UUIDResult.ReturnType.API_OFFLINE);
            }
            uuidResult.setException(exception);
            return uuidResult;
        }
    }


    public static String encode(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }

    public static UUID toUUIDWithDashes(String uuid) {
        return UUID.fromString(uuid.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
    }

    public static boolean verifyJSONArguments(JSONObject jsonObject, String... arguments) {
        for (String s : arguments) {
            if (!jsonObject.containsKey(s)) return false;
        }
        return true;
    }

    public static boolean isURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validUUID(String uuid) {
        return Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$").matcher(uuid).matches();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, ParseException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONParser jsp = new JSONParser();
            JSONObject json = (JSONObject) jsp.parse(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
