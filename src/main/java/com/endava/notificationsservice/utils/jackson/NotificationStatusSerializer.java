package com.endava.notificationsservice.utils.jackson;

import com.endava.notificationsservice.enums.Status;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;


public class NotificationStatusSerializer extends StdSerializer<Status> {

    public NotificationStatusSerializer(){
        this(null);
    }

    public NotificationStatusSerializer(Class<Status> t) {
        super(t);
    }

    @Override
    public void serialize(Status status, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeBoolean(status.equals(Status.READ));
    }
}
