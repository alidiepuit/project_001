/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package comic.ali.com.truyentranh.viewmodel;

import com.google.gson.annotations.SerializedName;

import comic.ali.com.truyentranh.model.Item;

/**
 * TvShowViewModel implementation created to contain all the tv show information and to keep all
 * the representation state of a tv show.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class TvShowViewModel extends Item{
    @SerializedName("id_manga")
    public int id;

    @SerializedName("manga_name")
    public String name;

    @SerializedName("image_chapter")
    public String image_chapter;

    @SerializedName("image")
    public String image;

    @SerializedName("description")
    public String description;

    private final EpisodeCollection episodes;

    public TvShowViewModel(int id, String title, String poster, String art, String des) {
        this.id = id;
        this.name = title;
        this.image_chapter = poster;
        this.image = art;
        this.description = des;
        this.episodes = new EpisodeCollection();
    }

    /**
     * Add an episode to the tvShowViewModel.
     */
    public void addEpisode(EpisodeViewModel episodeViewModel) {
        episodes.add(episodeViewModel);
    }

    /**
     * @return the tv show title.
     */
    public String getTitle() {
        return name;
    }

    /**
     * @return the tv show poster.
     */
    public String getPoster() {
        return this.initImage(image_chapter);
    }

    /**
     * @return the tv show art.
     */
    public String getArt() {
        return this.initImage(image);
    }

    /**
     * @return the tv show description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the tv show EpisodeCollection.
     */
    public EpisodeCollection getEpisodes() {
        return episodes;
    }

    /**
     * @return the tv show id.
     */
    public int getId() {
        return id;
    }

    private String initImage(String path) {
        if(!path.startsWith("http")) {
            path = "http://comicvn.net/static/" + path;
        }
        return path;
    }
}
