package backend.creators;

import backend.entities.Share;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class ShareCreator {
    private final static Random random = new Random();

    private static final String[] shortlList = new String[]{"QTI", "BGI", "AFD", "NNS", "OVV", "SFI", "RHT", "SGS", "ASI", "VBD", "EWT", "NCE", "QHH", "BSI", "AGD", "NNN", "TWV", "HCI", "CGS", "ASD", "QQT", "BNI", "AVV", "NGD", "SST", "OCI", "RPD", "ASS", "QNV", "BHI"};

    private static final Double[] ppsList = new Double[]{45.67, 32.89, 78.12, 54.23, 65.43, 89.76, 41.09, 72.54, 60.98, 55.76, 67.34, 49.21, 80.45, 43.67, 75.32, 58.90, 69.12, 62.45};

    private static final Double[] dividendList = new Double[]{45.67, 3.21, 78.12, 2.43, 65.43, 1.98, 54.23, 2.76, 89.76, 1.54, 41.09, 2.32, 72.54, 1.89, 60.98, 2.11, 55.76, 1.76, 67.34, 2.09, 49.21, 2.45, 80.45, 1.32, 43.67, 2.67, 75.32, 1.90, 58.90, 2.12, 69.12, 1.54, 62.45, 2.32};

    private static final Integer[] existingShares = new Integer[]{1500, 321, 800, 243, 1200, 198, 1000, 276, 500, 154, 1500, 232, 700, 189, 900, 211, 1100, 176, 1000, 209, 1300, 245, 600, 132, 1400, 267, 800, 190, 1000, 212, 1200, 154, 900, 232};


    public static Share[] createNewShares(int givenAmount) {
        String[] nameList = getNameList(givenAmount);
        Share[] shares = new Share[nameList.length];
        for (int i = 0; i < nameList.length; i++) {
            shares[i] = createShareWithName(nameList[i]);
        }
        return shares;
    }

    private static Share createShareWithName(String name) {
        Share share = new Share();
        share.setName(name);
        share.setShortl(shortlList[random.nextInt(shortlList.length - 1)]);
        share.setPricePerShare(randomEntry(ppsList));
        share.setStockReturn(randomEntry(dividendList));
        share.setExistingSharesAmount(randomEntry(existingShares));
        share.setDate(LocalDateTime.now());
        return share;
    }


    private static <T> T randomEntry(T[] list) {
        return list[randEntry(list)];
    }

    private static <T> int randEntry(T[] list) {
        return random.nextInt(list.length - 1);
    }

    public static String[] getNameList(int amount) {
        Set<String> nameSet = new HashSet<>();
        while (nameSet.size() < amount) {
            nameSet.addAll(retrieveNameList(amount));
        }
        String[] nameList = new String[nameSet.size()];
        nameSet.toArray(nameList);
        ArrayList<String> finalList = new ArrayList<>(List.of(nameList));
        while (finalList.size() > amount) {
            finalList.remove(finalList.size() - 1);
        }

        return finalList.toArray(new String[0]);
    }

    private static List<String> retrieveNameList(int amount) {
        String response = getResponse(amount);
        String list = extractMessageFromJSONResponse(response);
        list = list.replaceAll("\\n", "");
        return List.of(list.split(", "));
    }

    private static String getResponse(int amount) {
        try {
            String url = "https://api.openai.com/v1/chat/completions";
            String apiKey = getAPIKey();
            String model = "gpt-3.5-turbo";
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            String prompt = "Give me a List of " + amount + " fictitious company names. Do not number them!!. seperate them by a comma";
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuilder response = new StringBuilder();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            return response.toString();
        } catch (IOException ignored) {
        }
        return "";
    }

    private static String getAPIKey() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\fhaas\\OneDrive - Ergon Informatik AG\\Desktop\\files\\Erste Aufgaben\\Java\\shares_gradle\\.env"));
            String line;
            line = in.readLine();
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                return parts[1];
            }
            return "";
        } catch (IOException e) {
            return "";
        }
    }

    private static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }
}
