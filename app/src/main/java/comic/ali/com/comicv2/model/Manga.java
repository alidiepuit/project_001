package comic.ali.com.comicv2.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import comic.ali.com.comicv2.viewmodel.EpisodeCollection;
import comic.ali.com.comicv2.viewmodel.EpisodeViewModel;

/**
 * Created by AliPro on 2/23/2015.
 */
public class Manga extends Item {
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

    public Manga(int id, String title, String poster, String art, String des) {
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
        if(image_chapter == null || image_chapter.isEmpty()) {
            return this.initImage(image);
        }
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
