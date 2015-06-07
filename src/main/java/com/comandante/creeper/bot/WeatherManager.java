package com.comandante.creeper.bot;

import com.comandante.creeper.CreeperConfiguration;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.Lists;
import org.apache.commons.lang.WordUtils;

import java.io.IOException;
import java.util.List;

public class WeatherManager {

    private final CreeperConfiguration creeperConfiguration;
    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final JsonFactory JSON_FACTORY = new JacksonFactory();

    public WeatherManager(CreeperConfiguration creeperConfiguration) {
        this.creeperConfiguration = creeperConfiguration;
    }

    public List<String> getWeather(String cityName, String state) throws IOException {
        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });

        GenericUrl url = new GenericUrl("http://api.wunderground.com/api/62651e7a5762cda8/conditions/q/" + state.toUpperCase() + "/" + convertToUrlFriendly(cityName) + ".json");
        HttpRequest httpRequest = requestFactory.buildGetRequest(url);
        GenericJson content = httpRequest.execute().parseAs(GenericJson.class);
        ArrayMap current_observation = (ArrayMap) content.get("current_observation");
        Object weather = current_observation.get("weather");
        Object temperature_string = current_observation.get("temperature_string");
        Object wind_string = current_observation.get("wind_string");
        Object relative_humidity = current_observation.get("relative_humidity");
        Object feelslike_string = current_observation.get("feelslike_string");

        List<String> response = Lists.newArrayList();
        response.add("Current Conditions: " + weather);
        response.add("Temperature: " + temperature_string);
        response.add("Wind: " + wind_string);
        response.add("Humidity: " + relative_humidity);
        response.add("Feels Like: " + feelslike_string);
        return response;
    }

    private String convertToUrlFriendly(String cityName) {
        String newName = cityName.replaceAll(" ", "_").toLowerCase();
        String capitalize = WordUtils.capitalize(newName);
        return capitalize;
    }


}
