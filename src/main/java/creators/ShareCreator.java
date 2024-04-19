package creators;

import entities.Share;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Random;

public class ShareCreator {
    private final static Random random = new Random();

    protected static int amount = 0;

    private static String[] nameList;

    private static final String[] shortlList = new String[]{"QTI", "BGI", "AFD", "NNS", "OVV", "SFI", "RHT", "SGS", "ASI", "VBD", "EWT", "NCE", "QHH", "BSI", "AGD", "NNN", "TWV", "HCI", "CGS", "ASD", "QQT", "BNI", "AVV", "NGD", "SST", "OCI", "RPD", "ASS", "QNV", "BHI"};

    private static final Double[] ppsList = new Double[]{45.67, 32.89, 78.12, 54.23, 65.43, 89.76, 41.09, 72.54, 60.98, 55.76, 67.34, 49.21, 80.45, 43.67, 75.32, 58.90, 69.12, 62.45};

    private static final Double[] dividendList = new Double[]{45.67, 3.21, 78.12, 2.43, 65.43, 1.98, 54.23, 2.76, 89.76, 1.54, 41.09, 2.32, 72.54, 1.89, 60.98, 2.11, 55.76, 1.76, 67.34, 2.09, 49.21, 2.45, 80.45, 1.32, 43.67, 2.67, 75.32, 1.90, 58.90, 2.12, 69.12, 1.54, 62.45, 2.32};

    private static final Integer[] existingShares = new Integer[]{1500, 321, 800, 243, 1200, 198, 1000, 276, 500, 154, 1500, 232, 700, 189, 900, 211, 1100, 176, 1000, 209, 1300, 245, 600, 132, 1400, 267, 800, 190, 1000, 212, 1200, 154, 900, 232};


    public static Share[] createNewShares(int givenAmount) throws Exception {
        /*amount = Math.min(amount, nameList.length);
        List<Integer> indices = new ArrayList<>(IntStream.range(0, amount).boxed().toList());
        Collections.shuffle(indices);*/
        amount = givenAmount;
        Thread thread = new Request();
        thread.start();
        while (thread.isAlive()) {
        }
        Share[] shares = new Share[amount];
        for (int i = 0; i < nameList.length; i++) {
            Share share = new Share();
            setShareInformation(share, i);
            if (i > nameList.length - 1) {
                break;
            }
            shares[i] = share;
        }
        return shares;
    }

    private static void setShareInformation(Share share, int i) {
        share.setName(nameList[i]);
        share.setShortl(shortlList[random.nextInt(shortlList.length - 1)]);
        share.setPricePerShare(randomEntry(ppsList));
        share.setStockReturn(randomEntry(dividendList));
        share.setExistingSharesAmount(randomEntry(existingShares));
        share.setDate(LocalDateTime.now());
    }


    private static <T> T randomEntry(T[] list) {
        return list[randEntry(list)];
    }

    private static <T> int randEntry(T[] list) {
        return random.nextInt(list.length - 1);
    }

    public static class Request extends Thread {
        @Override
        public void start() {
            String url = "https://api.openai.com/v1/chat/completions";
            String apiKey = getAPIKey();
            String model = "gpt-3.5-turbo";

            try {
                URL obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);

                // The request body
                String prompt = "Give me a List of " + amount + " fictitious company names. Do not number them!!. seperate them by a comma";
                String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();

                // Response from ChatGPT
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                StringBuilder response = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                // calls the method to extract the message.
                String list = extractMessageFromJSONResponse(response.toString());
                list = list.replaceAll("\\n", "");
                nameList = list.split(", ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
