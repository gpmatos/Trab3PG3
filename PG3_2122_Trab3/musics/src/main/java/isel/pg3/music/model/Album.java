package isel.pg3.music.model;

import isel.pg3.music.utils.Mp3Player;

import java.util.*;


/**
 * TODO ...
 */
public class Album  extends MusicItemExtended {
    private String path;

   /** Build a Album with:
     * @param title   - album title
     * @param path    - absolute pathname
     * @param songs   - collection of songs
     * @param artists - set of artists
     * @param genres  - set of artists
     */
    public Album(String title, String path, Collection<Song> songs,
                 Set<String> artists, Set<String> genres)  {
        super(title);
        this.path = path;
        this.songs=songs;

        // TODO
    }

    @Override
    public long getDuration() {

        int duration = 0;
        for (Song s : songs ) {
            duration += s.getDuration();
        }
        return duration;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void playOn(Mp3Player player) {
        ArrayList<String> list = new ArrayList<>();
        for (Song s : songs ) {
            list.add(s.getPath());
        }
        player.play(list);
    }

}