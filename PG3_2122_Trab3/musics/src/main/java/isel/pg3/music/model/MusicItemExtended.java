package isel.pg3.music.model;

import isel.pg3.music.utils.Mp3Player;

import java.util.*;

public abstract class MusicItemExtended extends MusicItem {
    protected Collection<Song> songs;
    protected MusicItemExtended(String title) {
        super(title);
        songs = new ArrayList<Song>();
    }

    protected MusicItemExtended(String title, String artist, String genre) {
        super(title,artist,genre);
        songs = new ArrayList<Song>();
    }

    protected MusicItemExtended(String title, Set<String> artists, Set<String> genres) {
        super(title,artists,genres);
        songs = new ArrayList<Song>();
    }


    public void playOn(Mp3Player player ){
        ArrayList<String> list = new ArrayList<>();
        for (Song s : songs ) {
            list.add(s.getPath());
        }
        player.play(list);
    }

    @Override
    public Iterator<Song> iterator() {
        Iterator<Song> it = songs.iterator();
        return it;
    }

}
