/**
 * Copyright 2017 - 2018 Syncleus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syncleus.aethermud.bot.command;

import com.syncleus.aethermud.configuration.AetherMudConfiguration;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeatherManager {

    private final AetherMudConfiguration aetherMudConfiguration;
    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final JsonFactory JSON_FACTORY = new JacksonFactory();
    private final HttpRequestFactory requestFactory;

    private static final Logger log = Logger.getLogger(WeatherManager.class);


    public WeatherManager(AetherMudConfiguration aetherMudConfiguration) {
        this.aetherMudConfiguration = aetherMudConfiguration;
        this.requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });
    }

    public List<String> getWeatherForecast(String zipCode) throws IOException {
        List<String> resp = Lists.newArrayList();
        GenericUrl url = new GenericUrl("http://api.wunderground.com/api/" + aetherMudConfiguration.weatherUndergroundApiKey + "/forecast/q/" + zipCode + ".json");
        HttpRequest httpRequest = requestFactory.buildGetRequest(url);
        GenericJson content = httpRequest.execute().parseAs(GenericJson.class);
        List<String> forecastString = getForecastString(content);
        String s = forecastString.get(0);
        forecastString.set(0, zipCode + ": " + s);
        resp.addAll(forecastString);
        return resp;
    }

    public List<String> getWeather(String zipCode) throws IOException {
        List<String> resp = Lists.newArrayList();
        GenericUrl url = new GenericUrl("http://api.wunderground.com/api/" + aetherMudConfiguration.weatherUndergroundApiKey + "/conditions/q/" + zipCode + ".json");
        HttpRequest httpRequest = requestFactory.buildGetRequest(url);
        GenericJson content = httpRequest.execute().parseAs(GenericJson.class);
        resp.addAll(getCurrentConditionsString(content));
        return resp;
    }

    public List<String> getWeather(String cityName, String state) throws IOException {
        List<String> resp = Lists.newArrayList();
        GenericUrl url = new GenericUrl("http://api.wunderground.com/api/" + aetherMudConfiguration.weatherUndergroundApiKey + "/conditions/q/" + state.toUpperCase() + "/" + convertToUrlFriendly(cityName) + ".json");
        HttpRequest httpRequest = requestFactory.buildGetRequest(url);
        GenericJson content = httpRequest.execute().parseAs(GenericJson.class);
        resp.addAll(getCurrentConditionsString(content));
        return resp;
    }

    public List<String> getWeatherForecast(String cityName, String state) throws IOException {
        List<String> resp = Lists.newArrayList();
        GenericUrl url = new GenericUrl("http://api.wunderground.com/api/" + aetherMudConfiguration.weatherUndergroundApiKey + "/forecast/q/" + state.toUpperCase() + "/" + convertToUrlFriendly(cityName) + ".json");
        HttpRequest httpRequest = requestFactory.buildGetRequest(url);
        GenericJson content = httpRequest.execute().parseAs(GenericJson.class);
        List<String> forecastString = getForecastString(content);
        String s = forecastString.get(0);
        forecastString.set(0, convertToUrlFriendly(cityName) + "," + state.toUpperCase() + ": " + s);
        resp.addAll(forecastString);
        return resp;
    }

    private List<String> getForecastString(GenericJson content) {
        List<String> response = Lists.newArrayList();
        StringBuilder respLin = new StringBuilder();
        try {
            ArrayMap forecast = (ArrayMap) content.get("forecast");
            ArrayMap simpleforecast = (ArrayMap) forecast.get("simpleforecast");
            ArrayList forecastday = (ArrayList) simpleforecast.get("forecastday");
            Iterator iterator = forecastday.iterator();
            while (iterator.hasNext()) {
                ArrayMap day = (ArrayMap) iterator.next();
                ArrayMap date = (ArrayMap) day.get("date");
                ArrayMap high = (ArrayMap) day.get("high");
                ArrayMap low = (ArrayMap) day.get("low");
                String conditions = (String) day.get("conditions");
                respLin.append(date.get("weekday_short")).append(" ").append((String)high.get("fahrenheit")).append("F/").append((String)low.get("fahrenheit")).append("F ").append("(").append(conditions).append(")").append(" | ");

            }
        } catch (Exception e) {
            log.error("Error obtaining weather!", e);
        }
        String finalstring = respLin.toString().substring(0, respLin.toString().length() - 2);
        response.add(finalstring);
        return response;
    }

    private List<String> getCurrentConditionsString(GenericJson content) {
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
