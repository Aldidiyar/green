import org.testng.annotations.*;
import static org.testng.Assert.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestGreen {

    private static final String BASE_URL = "https://7103.api.greenapi.com/waInstance7103179861";
    private static final String API_TOKEN = System.getenv("auth_token"); // Replace with your actual token
    private static final String CONTENT_TYPE = "application/json";


    @Test
    public void testSendMessageValid() throws IOException {
        String endpoint = "/sendMessage/" + API_TOKEN;
        String payload = "{\"chatId\":\"77711729924@c.us\",\"message\":\"hi\"}";

        int responseCode = sendPostRequest(BASE_URL + endpoint, payload);
        assertEquals(responseCode, 200, "Expected response code 200 for valid SendMessage request");
    }

    @Test
    public void testSendMessageInvalidChatId() throws IOException {
        String endpoint = "/sendMessage/" + API_TOKEN;
        String payload = "{\"chatId\":\"invalidChatId\",\"message\":\"hi\"}";

        int responseCode = sendPostRequest(BASE_URL + endpoint, payload);
        assertNotEquals(responseCode, 200, "Expected non-200 response code for invalid chatId");
    }

    @Test
    public void testGetChatHistoryValid() throws IOException {
        String endpoint = "/getChatHistory/" + API_TOKEN;
        String payload = "{\"chatId\":\"77711729924@c.us\"}";

        int responseCode = sendPostRequest(BASE_URL + endpoint, payload);
        assertEquals(responseCode, 200, "Expected response code 200 for valid getChatHistory request");
    }

    @Test(enabled=true)
    public void testGetChatHistoryMissingChatId() throws IOException {
        String endpoint = "/getChatHistory/" + API_TOKEN;
        String payload = "{}";

        int responseCode = sendPostRequest(BASE_URL + endpoint, payload);
        assertNotEquals(responseCode, 200, "Expected non-200 response code for missing chatId");
    }

    private int sendPostRequest(String urlString, String payload) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", CONTENT_TYPE);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading response: " + e.getMessage());
        }

        connection.disconnect();
        return responseCode;
    }
}
