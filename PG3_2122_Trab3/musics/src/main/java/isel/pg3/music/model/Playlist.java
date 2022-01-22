package isel.pg3.music.model;

import isel.pg3.music.utils.Mp3Player;

import java.util.*;

/**
 * TODO ...
 * The playlist is very similar to album, so it seems
 * reasonable to have an intermediary abstract class //criar +1 ficheiro de classe que extende musicItem para esconder algumas funções
 * to represent albums and play lists.
 */
public class Playlist extends MusicItemExtended {
    public Playlist(String name ) {
        super(name);
    }

    public void addSong( Song song ) {
        songs.add(song);
    }

    public long getDuration() {

        int duration = 0;
        for (Song s : this ) {
            duration += s.getDuration();
        }
        return duration;
    }

    public String getPath() {
        return null;
    }

}
