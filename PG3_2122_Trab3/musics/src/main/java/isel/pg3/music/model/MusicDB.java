package isel.pg3.music.model;

import java.util.*;


/**
 * TODO ...
 */
public class MusicDB {
    // Date

    // TODO - not is completed

    public MusicDB() {

    }

    /**
     * Methods to add songs, songs, albums and playlists
     */


    HashMap<String,Song> songMap = new HashMap();
    HashMap<String,Album> albumMap = new HashMap();
    HashMap<String, HashMap<Integer,Song>> genresMap = new HashMap();
    //HashMap<String, Set<Song>> genresMap = new HashMap(); ??


    // TODO criar função generica para fazer os adds
    // usar o computeifabsent em vez do putifabsent

    public Song addSong(Song song) {
        songMap.put(song.getTitle(), song);
        //if (genresMap.get(song.getGenre()) == null){
        //Set<Song> s = new HashSet<>();
        HashMap<Integer, Song> s = new HashSet<>();
       // s.add(song);
        genresMap.putIfAbsent(song.getGenre(),s); //V putIfAbsent(K key, V value)
        //genresMap.put(song.getGenre(),s);
        //s= genresMap.get(song.getGenre() );
        s.add(song);
        genresMap.put(song.getGenre(),s);

        return song;
    }

    public Album addAlbum(Album album) {
        albumMap.putIfAbsent(album.getTitle(), album);
        for (Song s : album.songs ) {
            addSong(s);
        }
        return album;
    }

    public List<MusicItem> addMusics( List<MusicItem> items ) {
        for(MusicItem m : items){
            if ( m instanceof Song ){
               addSong((Song) m);
            }
            else {
                addAlbum((Album) m);
            }
        }
        return items;
    }

    private Song genRandomSong(HashMap<Integer,Song> genrehash , long playDur, long maxDuration){
        Random rand = new Random(); //instance of random class
        int upperbound = genrehash.size();//generate random values from 0-HashMap size
        int int_random = rand.nextInt(upperbound);
        Song s = genrehash.get(int_random);
        if(((playDur + (s.getDuration())) < maxDuration)) {// comparar Playlist.duration para ver se é possivel adicionar
            return s;
        }
        else {// outra tentativa de inserção
            s = genrehash.get(int_random);
            if (((playDur + (s.getDuration())) < maxDuration)) {// comparar Playlistduration para ver se é possivel adicionar
                return s;
            }
            else return null;
        }
    }

    private Integer genRandomInt(String[] genres) {
        int min = 0;
        int max = genres.length;
        //Generate random int value from 0 to genres.length
        return (int) Math.floor(Math.random() * (max - min + 1) + min);

    }

    public Playlist addRandomPlayList(String name, String[] genres, int totalSongs, long maxDuration) {
        Playlist playlist = new Playlist(name);
        long playDur =0;
        int resto =0;

        if((resto= (genres.length-totalSongs ))< 0){ // Se o Nº Songs < Nº Generos requisitada
            for(int i =0 ; i<totalSongs ; i++){
                HashMap<Integer,Song> genrehash =genresMap.get(genRandomInt(genres)); // Buscar o genero aleatorio
                playlist.addSong(genRandomSong(genrehash,playDur,maxDuration)); // adicionar à playlist
            }
            return playlist;
        }

        if(resto== 0){ // Nº Songs == Nº Generos
          for(String g : genres){
              HashMap<Integer,Song> genrehash =genresMap.get(g);
              playlist.addSong(genRandomSong(genrehash,playDur,maxDuration));
          }
          return playlist;
        }
        else{  // Nº Songs == Nº Generos
            for(int i =0 ; i<resto ; i++){
                HashMap<Integer,Song> genrehash =genresMap.get(genRandomInt(genres));
                playlist.addSong(genRandomSong(genrehash,playDur,maxDuration));
            }
            return playlist;

       }
    }

    /**
     * Methods to get the song, album or playlist with given title
     */
    public Song getSong(String t)        { return null; }
    public Album getAlbum(String t)      { return null; }
    public Playlist getPlaylist(String t){ return null; }

    /**
     * Methods to get all songs, albums, playlists, artists and genres.
     */
    public Iterable<Song> getSongs()         { return null;  }
    public Iterable<Album> getAlbums()       { return null;  }
    public Iterable<Playlist> getPlaylists() { return null;  }
    public Iterable<String> getArtists()     { return null;  }
    public Iterable<String> getGenres()      { return null;  }

    /**
     * Method to get the songs of the specified artist.
     */
    public Iterable<Song> getSongs(String artist) {
        return null;
    }

    /**
     * Method for getting songs that have at least one of the specified genres.
     */
    public Iterable<Song> getSongs(String ... genres) { return null;  }

}
