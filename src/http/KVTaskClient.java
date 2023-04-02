package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class KVTaskClient {
    private final HttpClient client;

    public KVTaskClient() {
        client = HttpClient.newHttpClient();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        if (json != null) {
            String token = getToken();
            URI url = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN="+ token + "/");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        }
    }

    public Optional<String> load(String key) throws IOException, InterruptedException {
        String token = getToken();
        URI url = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN="+ token + "/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return Optional.ofNullable(response.body());
    }

    private String getToken() throws IOException, InterruptedException {
        URI getToken = URI.create("http://localhost:8078/register/");
        HttpRequest requestRegister = HttpRequest.newBuilder()
                .uri(getToken)
                .GET()
                .build();
        HttpResponse<String> responseToken = client.send(requestRegister, HttpResponse.BodyHandlers.ofString());
        return responseToken.body();
    }
}
