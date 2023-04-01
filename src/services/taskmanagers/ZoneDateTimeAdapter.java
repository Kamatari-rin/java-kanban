package services.taskmanagers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZoneDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    public void write(final JsonWriter jsonWriter, final ZonedDateTime zonedDateTime) throws IOException {
        try {
            jsonWriter.value(zonedDateTime.format(formatterWriter));
        } catch (IOException | IllegalStateException | NullPointerException exception) {
            jsonWriter.value((String) null);
        }
    }

    @Override
    public ZonedDateTime read(JsonReader jsonReader) throws IOException {
        try {
            return ZonedDateTime.parse(jsonReader.nextString(), formatterWriter);
        } catch (IOException | IllegalStateException | NullPointerException exception) {
            return null;
        }

    }

}
