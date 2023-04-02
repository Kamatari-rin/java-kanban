package services.taskmanagers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        try {
            long durationInSeconds = duration.getSeconds();
            jsonWriter.value(Long.toString(durationInSeconds));
        } catch (IOException | IllegalStateException | NullPointerException exception) {
            jsonWriter.value((String) null);
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        try {
            Duration duration = Duration.ofSeconds(Integer.parseInt(jsonReader.nextString()));
            return duration;
        } catch (IOException | IllegalStateException | NullPointerException exception) {
            return null;
        }
    }
}
