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
package com.syncleus.aethermud.core;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RecentChangesManager {

    private static LoadingCache<String, String> recentChanges = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, String>() {
                        public String load(String key) { // no checked exception
                            return _getRecentChanges();
                        }
                    });

    private static String _getRecentChanges() {
        StringBuilder sb = new StringBuilder();
        try {
            GitHub github = GitHub.connectAnonymously();
            GHRepository repo = github.getRepository("chriskearney/creeper");
            PagedIterable<GHCommit> list = repo.queryCommits().list();
            int i = 1;
            for (GHCommit ghCommit : list.asList()) {
                if (i < 10) {
                    sb.append("Change #" + i + " | " + ghCommit.getCommitShortInfo().getMessage() + "\r\n");
                } else {
                    continue;
                }
                i++;
            }
        } catch (IOException e) {
            SentryManager.logSentry(RecentChangesManager.class, e, "Recent changes problem!");
        }
        return sb.toString();
    }

    public static String getRecentChanges() throws ExecutionException {
        return recentChanges.get("recent_changes");
    }

}
