package com.legacyminecraft.poseidon.util;

import com.legacyminecraft.poseidon.PoseidonConfig;
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

    public static class UUIDAndUsernameResult {
        private UUIDResult uuidResult;
        private String returnedUsername;

        public UUIDAndUsernameResult(UUIDResult uuidResult, String returnedUsername) {
            this.uuidResult = uuidResult;
            this.returnedUsername = returnedUsername;
        }

        public UUIDResult getUuidResult() {
            return uuidResult;
        }

        public String getReturnedUsername() {
            return returnedUsername;
        }
    }


    public static UUIDAndUsernameResult getUUID(String username) {
        try {
            String url = PoseidonConfig.getInstance().getString("settings.uuid-fetcher.get.value", "https://api.minecraftservices.com/minecraft/profile/lookup/name/{username}");
            url = url.replace("{username}", encode(username));

            JSONObject jsonObject = readJsonFromUrl(url);
            UUIDResult uuidResult;
            String returnedUsername = null;

            if (!jsonObject.containsKey("id")) {
                uuidResult = new UUIDResult(generateOfflineUUID(username), UUIDResult.ReturnType.OFFLINE);
            } else {
                UUID uuid = toUUIDWithDashes(String.valueOf(jsonObject.get("id")));
                if (uuid == null) {
                    uuidResult = new UUIDResult(null, UUIDResult.ReturnType.API_OFFLINE);
                } else {
                    returnedUsername = String.valueOf(jsonObject.get("name"));
                    uuidResult = new UUIDResult(uuid, UUIDResult.ReturnType.ONLINE);
                }
            }

            return new UUIDAndUsernameResult(uuidResult, returnedUsername);

        } catch (Exception exception) {
            UUIDResult uuidResult = null;
            if (exception == null || exception instanceof FileNotFoundException || exception.getMessage() == null) {
                // This is a hacky solution to tell if the API is offline, or the user is cracked.
                uuidResult = new UUIDResult(generateOfflineUUID(username), UUIDResult.ReturnType.OFFLINE);
            } else {
                uuidResult = new UUIDResult(null, UUIDResult.ReturnType.API_OFFLINE);
                uuidResult.setException(exception);
            }
            return new UUIDAndUsernameResult(uuidResult, null);
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
