/**
 * Copyright 2017 Syncleus, Inc.
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

import com.google.api.client.util.Lists;
import com.omertron.omdbapi.OMDBException;
import com.omertron.omdbapi.OmdbApi;
import com.omertron.omdbapi.model.OmdbVideoBasic;
import com.omertron.omdbapi.model.OmdbVideoFull;
import com.omertron.omdbapi.model.SearchResults;
import com.omertron.omdbapi.tools.OmdbBuilder;

import java.util.List;

public class OmdbManager {

    private final OmdbApi omdb = new OmdbApi();

    public List<String> getMovieInfo(String movieSearchString) throws OMDBException {
        SearchResults results = omdb.search(new OmdbBuilder().setSearchTerm(movieSearchString).build());
        if (!results.isResponse()) {
            return Lists.newArrayList();
        }
        List<String> resp = Lists.newArrayList();
        OmdbVideoBasic omdbVideoBasic = results.getResults().get(0);
        String title = omdbVideoBasic.getTitle();
        String year = omdbVideoBasic.getYear();
        String type = omdbVideoBasic.getType();

        OmdbVideoFull omdbVideoFull = omdb.getInfo(new OmdbBuilder()
                .setImdbId(omdbVideoBasic.getImdbID())
                .setPlotLong()
                .setTomatoesOn()
                .build());

        String imdbRating = omdbVideoFull.getImdbRating();
        String tomatoRating = omdbVideoFull.getTomatoRating();
        resp.add(title + " | " + year + " | " + type + " | imdb rating: " + imdbRating + " | rotten tomatoes: " + tomatoRating);
        resp.add(omdbVideoFull.getPlot());
        return resp;
    }
}
