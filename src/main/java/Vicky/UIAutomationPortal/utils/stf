package utils.stf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.appium.manager.ConfigFileManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This class provides the capability to connect or disconnect device.
 */
public class DeviceApi{
    private OkHttpClient client;
    private JsonParser jsonParser;
    private STFService stfService;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
    private static ConfigFileManager configFileManager;
    
    private static DeviceApi deviceApi;
    
    private DeviceApi(STFService stfService) {
        this.client = new OkHttpClient();
        this.jsonParser = new JsonParser();
        this.stfService = stfService;
//        try {
//            configFileManager = ConfigFileManager.getInstance();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    
    public static DeviceApi getInstance() {
//        String STF_SERVICE_URL = System.getProperty("STF_URL");
//        String ACCESS_TOKEN = System.getProperty("STF_ACCESS_TOKEN");
        try {
          configFileManager = ConfigFileManager.getInstance();
      } catch (IOException e) {
          e.printStackTrace();
      }
        String STF_SERVICE_URL = configFileManager.getProperty("STF_URL");
        String ACCESS_TOKEN = configFileManager.getProperty("STF_ACCESS_TOKEN");
//        System.out.println(STF_SERVICE_URL+ACCESS_TOKEN);
        STFService stfService;
        
        try {
           
                stfService = new STFService(STF_SERVICE_URL,
                        ACCESS_TOKEN);
           
            deviceApi = new DeviceApi(stfService);
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return deviceApi;
    }

    /**
     * return connected devices' UDID
     */
    public ArrayList<String> getDeviceSerial() throws Exception {
        ArrayList<String> udidList = new ArrayList<String>();
//        String udid =  "UYT0218103002374"; 
//        lists.add(udid);
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + stfService.getAuthToken())
                .url(stfService.getStfUrl() + "devices/")
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            JsonObject jsonObject = jsonParser.parse(response.body().string()).getAsJsonObject();

            boolean isSuccess = jsonObject.get("success").getAsBoolean();
            if(!isSuccess) {
                throw new Exception("STF Server is not responded as success!!!");
            }
            
            JsonArray devicesObject = jsonObject.getAsJsonArray("devices");
            System.out.println("listed devices: " + devicesObject.size());
            //get each deive info
            for(int i = 0; i< devicesObject.size(); i++) {
                JsonObject deviceObject = devicesObject.get(i).getAsJsonObject();
                boolean present = deviceObject.get("present").getAsBoolean();
                if (present) {
                    String udid = deviceObject.get("serial").getAsString();
                    System.out.println("udid is :" + udid);
                    udidList.add(udid);
                }                
            }                         
        } catch (IOException e) {
            throw new IllegalArgumentException("STF service is unreachable", e);
        }     
        return udidList;    
    }
    
    /**
     * need to rewrite with getDevices
     * @param deviceSerial
     * @return
     */
    public boolean connectDevice(String deviceSerial) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + stfService.getAuthToken())
                .url(stfService.getStfUrl() + "devices/" + deviceSerial)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            JsonObject jsonObject = jsonParser.parse(response.body().string()).getAsJsonObject();

            if (!isDeviceFound(jsonObject)) {
                return false;
            }

            JsonObject deviceObject = jsonObject.getAsJsonObject("device");
            boolean present = deviceObject.get("present").getAsBoolean();
            boolean ready = deviceObject.get("ready").getAsBoolean();
            boolean using = deviceObject.get("using").getAsBoolean();
            JsonElement ownerElement = deviceObject.get("owner");
            boolean owner = !(ownerElement instanceof JsonNull);

            if (!present || !ready || using || owner) {
                LOGGER.severe("Device is in use");
                return false;
            }

            return addDeviceToUser(deviceSerial);
        } catch (IOException e) {
            throw new IllegalArgumentException("STF service is unreachable", e);
        }
    }

    private boolean isDeviceFound(JsonObject jsonObject) {
        if (!jsonObject.get("success").getAsBoolean()) {
            LOGGER.severe("Device not found");
            return false;
        }
        return true;
    }

    private boolean addDeviceToUser(String deviceSerial) {
        RequestBody requestBody = RequestBody.create(JSON, "{\"serial\": \"" + deviceSerial + "\"}");
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + stfService.getAuthToken())
                .url(stfService.getStfUrl() + "user/devices")
                .post(requestBody)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            JsonObject jsonObject = jsonParser.parse(response.body().string()).getAsJsonObject();

            if (!isDeviceFound(jsonObject)) {
                return false;
            }

            LOGGER.info("The device <" + deviceSerial + "> is locked successfully");
            return true;
        } catch (IOException e) {
            throw new IllegalArgumentException("STF service is unreachable", e);
        }
    }

    /**
     * to stop using the specific devices
     * @param deviceSerial
     * @return
     */
    public boolean releaseDevice(String deviceSerial) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + stfService.getAuthToken())
                .url(stfService.getStfUrl() + "user/devices/" + deviceSerial)
                .delete()
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            JsonObject jsonObject = jsonParser.parse(response.body().string()).getAsJsonObject();

            if (!isDeviceFound(jsonObject)) {
                return false;
            }

            LOGGER.info("The device <" + deviceSerial + "> is released successfully");
            return true;
        } catch (IOException e) {
            throw new IllegalArgumentException("STF service is unreachable", e);
        }
    }

    public void releaseDevice() {
        List<String> usingDevices = getUsingDevices();
        for (String device : usingDevices) {
            releaseDevice(device);
        }
        
    }

    private List<String> getUsingDevices() {
        ArrayList<String> udidList = new ArrayList<String>();
      Request request = new Request.Builder()
              .addHeader("Authorization", "Bearer " + stfService.getAuthToken())
              .url(stfService.getStfUrl() + "devices/")
              .build();
      Response response;
      try {
          response = client.newCall(request).execute();
          JsonObject jsonObject = jsonParser.parse(response.body().string()).getAsJsonObject();
          
          JsonArray devicesObject = jsonObject.getAsJsonArray("devices");
          
          for(int i = 0; i< devicesObject.size(); i++) {
              JsonObject deviceObject = devicesObject.get(i).getAsJsonObject();
              //check whether the device is in using
              boolean isUsing = deviceObject.get("using").getAsBoolean();
              if (isUsing) {
                  String udid = deviceObject.get("serial").getAsString();
                  System.out.println("udid is :" + udid);
                  udidList.add(udid);
              }                
          }                         
      } catch (IOException e) {
          throw new IllegalArgumentException("STF service is unreachable", e);
      }     
      return udidList;    
    }
}
