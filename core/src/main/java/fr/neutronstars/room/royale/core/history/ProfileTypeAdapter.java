package fr.neutronstars.room.royale.core.history;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import fr.neutronstars.room.royale.core.game.entity.Profile;

import java.io.IOException;
import java.util.UUID;

public class ProfileTypeAdapter extends TypeAdapter<Profile> {

    @Override
    public void write(JsonWriter out, Profile profile) throws IOException {
        out.beginObject();
        out.name("id");
        if (profile.id() == null) {
            out.nullValue();
        } else {
            out.value(profile.id().toString());
        }
        out.name("name");
        out.value(profile.name());
        out.endObject();
    }

    @Override
    public Profile read(JsonReader in) throws IOException {
        Object id = null;
        String name = null;

        in.beginObject();
        while (in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("id")) {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                } else {
                    String idValue = in.nextString();
                    id = parseId(idValue);
                }
            } else if (fieldName.equals("name")) {
                name = in.nextString();
            } else {
                in.skipValue();
            }
        }
        in.endObject();

        return new Profile(id, name);
    }

    private Object parseId(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            try {
                return UUID.fromString(value);
            } catch (IllegalArgumentException ex) {
                return value;
            }
        }
    }
}
