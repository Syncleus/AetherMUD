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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class WeatherManager {

    private final CreeperConfiguration creeperConfiguration;
    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final JsonFactory JSON_FACTORY = new JacksonFactory();
    private final HttpRequestFactory requestFactory;

    private static final Logger log = Logger.getLogger(WeatherManager.class);


    public WeatherManager(CreeperConfiguration creeperConfiguration) {
        this.creeperConfiguration = creeperConfiguration;
        this.requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });
    }

    public List<String> getWeather(String zipCode) throws IOException {
        List<String> resp = Lists.newArrayList();
        GenericUrl url = new GenericUrl("http://api.wunderground.com/api/" + creeperConfiguration.weatherUndergroundApiKey + "/conditions/q/" + zipCode + ".json");
        HttpRequest httpRequest = requestFactory.buildGetRequest(url);
        GenericJson content = httpRequest.execute().parseAs(GenericJson.class);
        resp.addAll(getForecastString(content));
        return resp;
    }

    public List<String> getWeather(String cityName, String state) throws IOException {
        List<String> resp = Lists.newArrayList();
        GenericUrl url = new GenericUrl("http://api.wunderground.com/api/" + creeperConfiguration.weatherUndergroundApiKey + "/conditions/q/" + state.toUpperCase() + "/" + convertToUrlFriendly(cityName) + ".json");
        HttpRequest httpRequest = requestFactory.buildGetRequest(url);
        GenericJson content = httpRequest.execute().parseAs(GenericJson.class);
        resp.addAll(getForecastString(content));
        return resp;
    }

    private List<String> getForecastString(GenericJson content) {
        List<String> response = Lists.newArrayList();
        try {
            ArrayMap current_observation = (ArrayMap) content.get("current_observation");
            Object weather = current_observation.get("weather");
            Object temperature_string = current_observation.get("temperature_string");
            Object wind_string = current_observation.get("wind_string");
            Object relative_humidity = current_observation.get("relative_humidity");
            Object feelslike_string = current_observation.get("feelslike_string");
            ArrayMap display_location = (ArrayMap) current_observation.get("display_location");
            Object full = display_location.get("full");
            response.add(full.toString() + ": " + weather + " | Temperature: " + temperature_string + " | Humidity: " + relative_humidity + " | Feels Like: " + feelslike_string + " | Wind: " + wind_string);
        } catch (Exception e) {
            log.error("Error obtaining weather!", e);
        }
        return response;
    }

    private String convertToUrlFriendly(String cityName) {
        String newName = cityName.replaceAll(" ", "_").toLowerCase();
        String capitalize = WordUtils.capitalize(newName);
        return capitalize;
    }


}
