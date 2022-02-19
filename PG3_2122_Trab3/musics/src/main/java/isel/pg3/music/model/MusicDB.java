package isel.pg3.music.model;

import java.lang.reflect.Method;
import java.util.*;


/**
 * TODO ...
 */
public class MusicDB {

    public MusicDB() {

    }

    /**
     * Methods to add songs, songs, albums and playlists
     */


    HashMap<String,Song> songMap = new HashMap();
    HashMap<String,Album> albumMap = new HashMap();
    HashMap<String,Playlist> playListMap = new HashMap();
    HashMap<String, Set<Song>> genresMap = new HashMap();// <Genero, Coleção de songs do género>
    HashMap<String, Set<Song>> artistMap = new HashMap();// <Artista, Coleção de songs do artista>



    public Song addSong(Song song) {
        songMap.putIfAbsent(song.getTitle(), song); // Introduzir Song hashmap songMap
        String genre = song.getGenre();
        String artist = song.getArtist();
        addToSet(genresMap, song, genre);
        addToSet(artistMap, song, artist);
        return song;
    }
    private void addToSet(HashMap<String, Set<Song>> HashMap, Song song, String string){ // Acrescenta nos mapas indicado as musicas

        if ((HashMap.get(string)) ==  null) {
            Set<Song> setSong = new HashSet<>();
            setSong.add(song);              // Criação de novo género
            HashMap.put(string, setSong);
        }
        else {
            HashMap.get(string).add(song);  // Adição ao set pré existente
        }

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

    //  ----------------------Criação de PlayList---------------------------------------------------
    private Song genRandomSong(ArrayList<Song> aListSong, long playDur, long maxDuration) {

        //Song s = null;
        for(Song s: aListSong) {
            //s = (Song) o;
            if (((playDur + (s.getDuration())) < maxDuration)) {// comparar Playlist.duration para ver se é possivel adicionar
                return s;
            }
            else {  // outra tentativa de inserção
                aListSong.remove(s);
                break;
            }
        }
        return null;//Song não cabe na playlist
    }

    public Playlist addRandomPlayList(String name, String[] genres, int totalSongs, long maxDuration) {
        Playlist playlist = new Playlist(name);
        long playDur =0; // Duração da Playlist
        ArrayList<Song> aListSong= new ArrayList<>();
        if(totalSongs < 1 || maxDuration < 1){
            return playlist;
        }


        for(int i =0 ; i<genres.length ; i++){// Tirar todos as songs dos géneros para um array
              Set<Song> a = genresMap.get(genres[i]);
              aListSong.addAll(a);
        }
        // Array aleatório

        for(int i=0; i<totalSongs ; i++) {
            Collections.shuffle(aListSong);
            playlist.addSong(genRandomSong(aListSong, playDur, maxDuration)); // adicionar à playlist uma song que cabe na playlist em função do max dur
        }
        playListMap.putIfAbsent(playlist.getTitle(),playlist);// Adicionar ao hashmap de playlist
        return playlist;
    }

    //---------------------------------------------------

    /**
     * Methods to get the song, album or playlist with given title
     */
    public Song getSong(String t)        { return songMap.get(t);    }
    public Album getAlbum(String t)      { return albumMap.get(t); }
    public Playlist getPlaylist(String t){ return playListMap.get(t); }

    /**
     * Methods to get all songs, albums, playlists, artists and genres.
     */
    public Iterable<Song> getSongs()         { return songMap.values();  }
    public Iterable<Album> getAlbums()       { return albumMap.values();  }
    public Iterable<Playlist> getPlaylists() { return playListMap.values();  }
    public Iterable<String> getArtists()     { return artistMap.keySet();  }
    public Iterable<String> getGenres()      { return genresMap.keySet();  }

    /**
     * Method to get the songs of the specified artist.
     */
    public Iterable<Song> getSongs(String artist) {
        return artistMap.get(artist);
    }

    /**
     * Method for getting songs that have at least one of the specified genres.
     */
    public Iterable<Song> getSongs(String ... genres) {
        ArrayList<Song> allSongGenre = new ArrayList<>();
        for(String s: genres){
            allSongGenre.addAll(genresMap.get(s));
        }
        return allSongGenre;
    }


}
