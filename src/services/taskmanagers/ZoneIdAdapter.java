package services.taskmanagers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

public class ZoneIdAdapter extends TypeAdapter<ZoneId> {

    @Override
    public void write(JsonWriter jsonWriter, ZoneId zoneId) throws IOException {
        try {
            jsonWriter.value(zoneId.toString());
        } catch (IOException | IllegalStateException | NullPointerException exception) {
            jsonWriter.value((String) null);
        }
    }

    @Override
    public ZoneId read(JsonReader jsonReader) throws IOException {
        try {
            ZoneId zoneId = ZoneId.of(jsonReader.nextString());
            return zoneId;
        } catch (IOException | IllegalStateException | NullPointerException exception) {
            return null;
        }
    }
}
