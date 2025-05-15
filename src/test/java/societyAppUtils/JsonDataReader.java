package societyAppUtils;

import societyAppModels.SubscriptionData;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDataReader {
    public static List<SubscriptionData> getSubscriptions() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
            new File("src/test/resources/subscriptions.json"),
            objectMapper.getTypeFactory().constructCollectionType(List.class, SubscriptionData.class)
        );
    }
}
