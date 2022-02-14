package isel.pg3.music.views;


import isel.pg3.music.model.*;
import isel.pg3.music.utils.Mp3Player;
import isel.pg3.music.utils.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import static isel.pg3.music.model.MusicItemFinder.*;
import static isel.pg3.music.utils.Utils.*;
import static isel.pg3.music.views.ItemsViewer.*;

public class Mp3PlayerFrame extends JFrame {
    private static final String DEFAULT_FOLDER_MUSICS = ".";

    // states for the music player
    private enum State { Playing, Paused, Stopped }

    // current state
    private State state = State.Stopped;

    // Components for the south panel
    private Icon playIcon =  Utils.getIconFromResource("play.png");
    private Icon pauseIcon = Utils.getIconFromResource("pause.png");
    private Icon stopIcon = Utils.getIconFromResource("stop.png");

    private JButton playPauseBut = getButton(playIcon, this::onPlayPause);
    private JTextField albumName = new JTextField(32);
    private JTextField songName  = new JTextField(32);

    // Content panel for viewer
    private ItemsViewer musicItemsViewer = new ItemsViewer();

    // Components for the playList dialog creation - is incompleted
    private JTextField playlistName = new JTextField(20);
    private JTextField playlistDuration = new JTextField("3600", 6);
    private JTextField playlistNsongs = new JTextField(6);
    private final JButton b = new JButton("Genres");







    // Model

    // This list is used just for this sample
    // In your aplication you must replace this with a MusicDB as shown in the next commented line
   // private List<Song> songs = new ArrayList<>();

    private MusicDB musicDB = new MusicDB();

    private Mp3Player player = new Mp3Player();

    private MenuItemCheckList menuGeneros= new MenuItemCheckList("Genres");
    private MenuItemCheckList menuGeneros4PlayList= new MenuItemCheckList("Pick Your Genre");


    private void showOn(String title, Iterable<?> values,
                        SelectedItemListener selected, ZoomItemListener zoom)
    {
        musicItemsViewer.setItems(title, values, selected, zoom);
    }

    private  JComponent[] inputsPlayList = {
        new JLabel("Name"), playlistName,
        new JLabel("Duration"), playlistDuration,
        new JLabel("N Songs"),playlistNsongs,
        new JLabel(""), b

    };


    private void initInputsPlayList() {
        playlistName.setText( "" );
        playlistDuration.setText( "3600");
    }

    private int getInputsPlaylist(String title ) {
        buildGenresTabforPlayList(menuGeneros4PlayList);
        initInputsPlayList();
        return JOptionPane.showConfirmDialog(this, inputsPlayList,
                title, JOptionPane.PLAIN_MESSAGE);
    };



    JMenuBar miG = new JMenuBar(); // Criação da janela que abre com o Botão Genre
    private  JMenuBar[] inputsGenre = {
            new JMenuBar(), miG
    };

    public void buildGenreSelector(){
        miG.add(menuGeneros4PlayList);
        // Graphics g =;
       // g.setColor(Color.BLACK);
        //miG.paint(g);
    }
    private int getInputsGenre(String title ) {
        buildGenreSelector();

        return JOptionPane.showConfirmDialog(this,inputsGenre ,
                title, JOptionPane.PLAIN_MESSAGE);
    }; // -------------------------------------------------------------------------------


    //-----------------------------------------------------------------------------------------------------------
    //------------------------------------ listeners for viewer -------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------

    private void onSelectedSongItem(Object obj) {
        Song s = (Song) obj;
        songName.setText( s.getTitle() );
        albumName.setText(s.getAlbum());
    }

    private void onSelectedGenreItem( Object obj) {
        String s = (String) obj;
        songName.setText("");
        albumName.setText("");
    }
    private void onZoomGenresItem( Object obj) {
        String sObj = (String) obj;
        String[] genre = new String[1];
        genre[0] = sObj;
        songName.setText("");
        albumName.setText("");
        showOn("Genre->"+genre[0], musicDB.getSongs(genre) , this::onSelectedSongItem, null);
    }

    private void onSelectedAlbumItem( Object obj) {
        Album album = (Album) obj;
        songName.setText("");
        albumName.setText(album.getTitle());
    }
    private void onZoomAlbumItem( Object obj) {
        Album album = (Album) obj;
        songName.setText("");
        albumName.setText(album.getTitle());
        showOn("Album->"+album.getTitle(), musicDB.getAlbum(album.getTitle()) , this::onSelectedAlbumItem, null);
    }

    private void onSelectedPlayListItem( Object obj) {
        Playlist s = (Playlist) obj;
        songName.setText("");
        albumName.setText("");
    }
    private void onZoomPlayListItem( Object obj) {//??
        Playlist playlist = (Playlist) obj;
        songName.setText("");
        albumName.setText("");
        showOn("Playlist->"+playlist.getTitle(), musicDB.getPlaylist(playlist.getTitle()) , this::onSelectedPlayListItem, null);
    }

    private void onZoomArtistsItem( Object obj) {
        String artist = (String) obj;
        songName.setText("");
        albumName.setText("");
        showOn("Artist->"+artist, musicDB.getSongs(artist) , this::onSelectedSongItem, null);
    }


    //-----------------------------------------------------------------------------------------------------------
    // ------------------------------------listeners for Player--------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------

    private void onCompletedList( ) {
        state = State.Stopped;
        player.stop();
        //TODO - Update text fields
    }

    private void onMusicStartPlay( String  title ) {
        songName.setText(title);
        //TODO -Update text field albumName with album name
    }

    private void onError( Exception ex ) {
        // TODO
        playPauseBut.setIcon(pauseIcon);
        state = State.Stopped;
        player.stop();
        songName.setText("ERROR ERROR");
        albumName.setText("ERROR ERROR");
    }

    /**
     * listeners for buttons
     * Incomplete methods to exemplify the use of MP3Player
     */
    private void onStop(ActionEvent evt) {
         if (state != State.Stopped) {
            state = State.Stopped;
            player.stop();
            //TODO
        }
    }

    private void onPlayPause(ActionEvent evt) {

        switch(state) {
            case Playing:
                playPauseBut.setIcon(playIcon);
                state = State.Paused;
                player.pause();
                break;
            case Stopped:
                MusicItem selectedItem = musicItemsViewer.getSelectedItem();
                if (selectedItem != null ) {
                    state = State.Playing;
                    playPauseBut.setIcon(pauseIcon);
                    selectedItem.playOn(player);
                }
                break;
            case Paused:
                playPauseBut.setIcon(pauseIcon);
                state = State.Playing;
                player.resume();
                break;
        }
    }

    /**
     * Incomplete methods to exemplify addition and visualization
     * (use song as example)
     */
    //-----------------------------------------------------------------------------------------------------------
    // -----------------------------------Add--------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------
    private void addSongItem(ActionEvent e) {
        try {
            //Note that you must define DEFAULT_FOLDER_MUSICS according to your file system
            File  f = chooseFile(DEFAULT_FOLDER_MUSICS);
            if (f != null) {
                Song s = getSong(f);
                //songs.add(s);
                musicDB.addSong(s);

                showOn("Songs", musicDB.getSongs() , this::onSelectedSongItem, null);
                if (menuGeneros.isItem(s.getGenre())== false)
                    buildGenresTab(menuGeneros);
            }
        }
        catch (Exception ex ) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    private void addAlbumItem(ActionEvent e) {
        try {
            //Note that you must define DEFAULT_FOLDER_MUSICS according to your file system
            File  f = chooseFile(DEFAULT_FOLDER_MUSICS);
            if (f != null) {
                Album a = getAlbum(f);
                musicDB.addAlbum(a);
               // songs.add(s);
               showOn("Albums", musicDB.getAlbums(), this::onSelectedAlbumItem, this::onZoomAlbumItem);
                if (menuGeneros.isItem(a.getGenre())== false)
                    buildGenresTab(menuGeneros);

            }
        }
        catch (Exception ex ) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    private void addFolder(ActionEvent e) {
        try {
            //Note that you must define DEFAULT_FOLDER_MUSICS according to your file system
            File  f = chooseFile(DEFAULT_FOLDER_MUSICS);
            if (f != null) {
                List<MusicItem> a = getMusics(f);
                musicDB.addMusics(a);
               // songs.add(s);
               showOn("Songs", musicDB.getSongs(), this::onSelectedSongItem, null);
               if (menuGeneros.isItem(a)== false)// Falta aqui!!!!
                   buildGenresTab(menuGeneros);
            }
        }
        catch (Exception ex ) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    //-----------------------------------------------------------------------------------------------------------
    // ---------------------------- Show ------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------
    private void showSongItem(ActionEvent e) {
        albumName.setText( "" );
        songName.setText( "" );
        showOn("Songs", musicDB.getSongs(), this::onSelectedSongItem, null);
    }
    private void showAlbumsItems(ActionEvent e) {
        albumName.setText( "" );
        songName.setText( "" );
        showOn("Albums", musicDB.getAlbums(),this::onSelectedAlbumItem , this::onZoomAlbumItem);
    }
    private void showPlayListsItems(ActionEvent e) {
        albumName.setText( "" );
        songName.setText( "" );
        showOn("PlayList", musicDB.getPlaylists(), this::onSelectedPlayListItem, this::onZoomPlayListItem);
    }
    private void showGenresItems(ActionEvent e) {
        albumName.setText( "" );
        songName.setText( "" );
        showOn("Genres", musicDB.getGenres(),this::onSelectedGenreItem , this::onZoomGenresItem);
    }
    private void showArtistsItem(ActionEvent e) {
        albumName.setText( "" );
        songName.setText( "" );
        showOn("Artists", musicDB.getArtists(),null , this::onZoomArtistsItem);
    }
    //-----------------------------------------------------------------------------------------------------------
    // ---------------------------- Menu ------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------

    private void buildGenresTab(MenuItemCheckList mi){
        try {
            //musicDB.getGenres();
            Iterator<String> genreIt = musicDB.getGenres().iterator();

            while (genreIt.hasNext()) {
                menuGeneros.addItem(genreIt.next(), a ->showOn("Generos", musicDB.getGenres(), this::onSelectedGenreItem, this::onZoomGenresItem));

            }
        } catch (Exception ex ) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    String[] array = null; // trocar nome
    private void arrayChoosegeneros() {

        array = menuGeneros4PlayList.getSelected();


    }
    private void buildGenresTabforPlayList(MenuItemCheckList mi){
        try {
            //musicDB.getGenres();
            Iterator<String> genreIt = musicDB.getGenres().iterator();

            while (genreIt.hasNext()) {

               menuGeneros4PlayList.addItem(genreIt.next(),a ->arrayChoosegeneros() );


            }
        } catch (Exception ex ) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    private void newWindow(ActionEvent e){ // Janela de Adição de PlayList
        getInputsPlaylist("Playlist");
        musicDB.addRandomPlayList(playlistName.getText(),menuGeneros4PlayList.getSelected(),Integer.parseInt(playlistNsongs.getText()) ,(long) Integer.parseInt(playlistDuration.getText()));
        menuGeneros4PlayList.removeAll();
        //b.removeActionListener();

    }
    private void newWindowGenres(ActionEvent e){// Janela de Adição de Genero

        getInputsGenre("Choose Genre");

    }
    private void buildMenus() {
        JMenuBar menuBar = new JMenuBar();
        //------------------Nova Tab Genres-----------------------


        b.addActionListener(this::newWindowGenres); // Adição de ação ao botão

    //------------------Nova Tab Add-----------------------
        JMenu menuAdd = new JMenu("Add");

        JMenuItem mi = new JMenuItem("Song");
        mi.addActionListener(this::addSongItem);
        menuAdd.add( mi );
        mi = new JMenuItem( "Album");
        mi.addActionListener(this::addAlbumItem);
        menuAdd.add( mi );
        mi = new JMenuItem( "Folder ");
        mi.addActionListener(this::addFolder);
        menuAdd.add( mi );
        mi = new JMenuItem( "PlayList");
        mi.addActionListener(this::newWindow);
        menuAdd.add( mi );
        //------------------Nova Tab Show-----------------------
        JMenu menuShow = new JMenu("Show");
        mi = new JMenuItem( "Show Song ");
        mi.addActionListener(this::showSongItem);
        menuShow.add( mi );
        mi = new JMenuItem( "Show Album ");
        mi.addActionListener(this::showAlbumsItems);
        menuShow.add( mi );
        mi = new JMenuItem( "Show PlayList ");
        mi.addActionListener(this::showPlayListsItems);
        menuShow.add( mi );
        mi = new JMenuItem( "Genres");
        mi.addActionListener(this::showGenresItems);
        menuShow.add( mi );
        mi = new JMenuItem( "Artists");
        mi.addActionListener(this::showArtistsItem);
        menuShow.add( mi );
        //---------------Construcao final------------------
        menuBar.add(menuGeneros);
        menuBar.add(menuAdd);
        menuBar.add(menuShow);
        setJMenuBar( menuBar );
    }



    private void initComponents() {
        JPanel musicPanel = new JPanel(new GridLayout(3,1));
        musicPanel.add(getBox("Album", albumName, playPauseBut));
        musicPanel.add(getBox("Song", songName, getButton(stopIcon, this::onStop)));

        Container pane = getContentPane();
        pane.add(musicItemsViewer);
        pane.add(musicPanel, BorderLayout.SOUTH);
    }

    private void initModel() {
        player.setCompletedListListener (this::onCompletedList);
        player.setStartMusicListener(this::onMusicStartPlay);
        player.setErrorListener(this::onError);
    }

    public Mp3PlayerFrame() {
        initModel();
        initComponents();
        buildMenus();
        setSize(750, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
