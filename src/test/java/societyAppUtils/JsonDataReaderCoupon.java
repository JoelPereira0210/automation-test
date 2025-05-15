package societyAppUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import societyAppModels.CouponData;

import java.io.File;
import java.util.List;

public class JsonDataReaderCoupon {
    public static List<CouponData> getCoupons() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File("src/test/resources/coupons.json"),
                new TypeReference<List<CouponData>>() {});
    }
}
