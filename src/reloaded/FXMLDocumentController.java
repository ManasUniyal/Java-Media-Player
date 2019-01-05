/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reloaded;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.helpers.Loader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 *
 * @author DELL
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private Button openFile;
    @FXML
    private Button slow;
    @FXML
    private Button previous;
    @FXML
    private Button pause;
    @FXML
    private Button play;
    @FXML
    private Button next;
    @FXML
    private Button fast;
    @FXML
    private Button repeat;
    @FXML
    private Button shuffle;
    @FXML
    private Button search;
    @FXML
    private Button refresh;

    public static double k=1;

    public String filePath;

    public static String songName;

    @FXML
    ImageView star1;

    @FXML
    ImageView star2;

    @FXML
    ImageView star3;

    @FXML
    ImageView star4;

    @FXML
    ImageView star5;

    @FXML
    StackPane stackPane;

    @FXML
    private  Button mostPlayed;

    static Media media;
    public static MediaPlayer mediaPlayer;


    @FXML
    public MediaView mediaView;

    @FXML
    Slider slider;

    @FXML
    Slider seekSlider;

    @FXML
    ImageView mainImage;

    @FXML
    TextField mytext;

    @FXML
    ListView<String> musicList;

    @FXML
    TextField searchBar;

    @FXML
    Label songLabel;

    public static String ratingPath;

    private void handleButtonAction(ActionEvent event) {

        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
   
    public void initialize(URL url, ResourceBundle rb) {


    }

    @FXML
    public void playButton(ActionEvent event){
        mediaPlayer.play();
        play.setDisable(true);
        pause.setDisable(false);
        play.setVisible(false);
        pause.setVisible(true);
    }

    @FXML
    public void pausesong(ActionEvent event){
        mediaPlayer.pause();
        play.setDisable(false);
        pause.setDisable(true);
        play.setVisible(true);
        pause.setVisible(false);

    }
    @FXML
    public void slowvideo(ActionEvent event){
        k-=.5;
        mediaPlayer.setRate(k);
    }

    @FXML
    public void fastvideo(ActionEvent event){
        k+=.5;
        mediaPlayer.setRate(k);

    }
    @FXML
    public void openfilefunc(ActionEvent event) throws ClassNotFoundException, SQLException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a file (*.mp4)", " *.mp4","*.mp3");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        songName= file.getName();
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connect = DriverManager.getConnection(url, "root", "utkarshsql");
        String query2 = "SELECT Song_Name,Duration from MainTable;";
        seekSlider.setMax(250);
        PreparedStatement pre = connect.prepareStatement(query2);
        ResultSet result = pre.executeQuery();
        System.out.println(songName);
        while (result.next()) {
            String song=result.getString("Song_Name");
            //System.out.println(song);
            String duration = result.getString("Duration");

           // System.out.println(Double.parseDouble(duration));
            if(songName.compareTo(song)==0)
                seekSlider.setMax(Double.parseDouble(duration));
               // System.out.println(duration);


        }
        filePath = file.toURI().toString();
        songName= file.getName();
        setSongLabel(songName);
        if( filePath != null ){

            media = new Media(filePath);
            mediaPlayer = new MediaPlayer (media);
           mediaView.setMediaPlayer(mediaPlayer);
            DoubleProperty Width = mediaView.fitWidthProperty();
            DoubleProperty Hight = mediaView.fitHeightProperty();

           Width.bind(Bindings.selectDouble(mediaView.sceneProperty(),"width"));
            Hight.bind(Bindings.selectDouble(mediaView.sceneProperty(),"hight"));
           setVolume();
            //System.out.println(media.getDuration());

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    seekSlider.setValue(newValue.toSeconds());
                }
            });

            seekSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    mediaPlayer.seek(Duration. seconds( seekSlider.getValue()));
                }
            });
            mainImage.setVisible(false);
            play.setVisible(false);
            play.setDisable(true);
            pause.setDisable(false);
            pause.setVisible(true);
            mediaPlayer.play();
            showRating(songName);
        }
    }

    public void setVolume() {
        if (mediaPlayer != null) {
            slider.setValue(mediaPlayer.getVolume() * 100);
            slider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(slider.getValue() / 100);
                }
            });
        boolean musicState=getStatus();
        play.setDisable(musicState);
        pause.setDisable(!musicState);
        play.setVisible(!musicState);
        pause.setVisible(musicState);
        }
    }

    private void setSongLabel(String songName) {
        songLabel.setText(songName);
    }

    public boolean getStatus(){
        if(FXMLDocumentController.mediaPlayer!=null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PAUSED)
                return false;
            else
                return true;
        }
        else
            return false;

    }

    @FXML
    public void reloadData(ActionEvent event) throws ClassNotFoundException, SAXException, TikaException, SQLException, IOException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String url = "jdbc:mysql://localhost:3306/media";
                    Connection connect = DriverManager.getConnection(url, "root", "utkarshsql");
                    String query = "Delete from MainTable;";
                    Statement statement = connect.createStatement();
                    statement.executeUpdate(query);
                    statement.close();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    pathfind("E:\\Songs");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TikaException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
        }




    public void pathfind(String dir) throws IOException, TikaException, SAXException, SQLException, ClassNotFoundException,NullPointerException {
        File direc = new File(dir);
        for(File f:direc.listFiles()){
            if(f.isFile()){
                String extension=FilenameUtils.getExtension(f.getAbsolutePath());
                if(extension.equals("mp3")) {
                    StringBuffer s = new StringBuffer(f.getAbsolutePath().toString());
                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) == '\\')
                            s.setCharAt(i, '/');
                    }

                    try {
                        File file = new File(s.toString());
                        InputStream input = new FileInputStream(file);
                        ContentHandler handler = new DefaultHandler();
                        Metadata metadata = new Metadata();
                        Parser parser = new Mp3Parser();
                        ParseContext parseCtx = new ParseContext();
                        parser.parse(input, handler, metadata, parseCtx);
                        input.close();
                        String name=f.getName();
                        String artist=metadata.get("xmpDM:artist");
                        String genre=metadata.get("xmpDM:genre");
                        String duration=metadata.get("xmpDM:duration");
                        Float d=Float.parseFloat(duration);
                        d=d/1000;
                        duration=d.toString();
                        String album=metadata.get("xmpDM:album");

                        sqldata(name,f.toURI().toString(),artist,album,genre,0,0,extension,0,null,duration);



                      /*  System.out.println("Title: " + metadata.get("title"));
                        System.out.println("Artists : " + metadata.get("xmpDM:artist"));
                        System.out.println("Composer : " + metadata.get("xmpDM:composer"));
                        System.out.println("Duration : " +duration);
                        System.out.println("Album : " + metadata.get("xmpDM:album"));*/


                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }

                else if(extension.equals("mp4"))
                {

                    StringBuffer s = new StringBuffer(f.getAbsolutePath().toString());
                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) == '\\')
                            s.setCharAt(i, '/');
                    }
                    BodyContentHandler handler = new BodyContentHandler();
                    Metadata metadata = new Metadata();
                    FileInputStream inputstream = new FileInputStream(new File(s.toString()));
                    ParseContext pcontext = new ParseContext();

                    MP4Parser MP4Parser = new MP4Parser();
                    MP4Parser.parse(inputstream, handler, metadata,pcontext);
                    //  System.out.println("Document Content  :" + handler.toString());
                    // System.out.println("Document Metadata :");
                    String name=f.getName();
                    String duration=metadata.get("xmpDM:duration");
                    sqldata(name,f.toURI().toString(),null,null,null,0,0,extension,0,null,duration);

                }

            }
            else if (f.isDirectory()) {

                // System.out.println(f.getName());
                pathfind(f.getAbsolutePath());
            }

        }

    }

    public void sqldata(String name, String songurl, String artist,String album, String genre, int count, int recent, String extension, int rating, String playlist, String duration) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connection = DriverManager.getConnection(url, "root", "utkarshsql");




        String query1 = "INSERT INTO MainTable VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preStat = connection.prepareStatement(query1);
        preStat.setString(1, name);
        preStat.setString(2, songurl);
        preStat.setString(3, artist);
        preStat.setString(4, album);
        preStat.setString(5, genre);
        preStat.setInt(6, count);
        preStat.setString(8, extension);
        preStat.setInt(7, recent);
        preStat.setInt(9, rating);
        preStat.setString(10, playlist);
        preStat.setString(11, duration);
        preStat.executeUpdate();


    }




    @FXML
    public void searchButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        String search=searchBar.getText();
        if(search!=null)
            transferToSearch(search,event);

    }

    public void transferToSearch(String search,ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("search.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);
        SearchController controller = loader.getController();
        controller.showSearchList(search);
        controller.showRating(FXMLDocumentController.songName);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }


    @FXML
    public void homeButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("homePage.fxml"));
        Parent parent = loader.load();
        FXMLDocumentController Loadcontroller = loader.getController();
        Scene scene = new Scene(parent);
        Loadcontroller.setVolume();
        Loadcontroller.showRating(songName);
        Loadcontroller.songLabel.setText(songName);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void myMusicButton(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {


        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("myMusic.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        musicController controller =loader.getController();
        controller.setMusiclList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void myMusicImageButton(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("myMusic.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        musicController controller =loader.getController();
        controller.setMusiclList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void artistButton(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {


        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("artist.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        ArtistController controller =loader.getController();
        controller.setArtistList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void artistImageButton(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {


        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("artist.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        ArtistController controller =loader.getController();
        controller.setArtistList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void albumButton(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {


        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("album.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        AlbumController controller =loader.getController();
        controller.setAlbumList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void albumImageButton(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("album.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        AlbumController controller =loader.getController();
        controller.setAlbumList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void genreButton(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("genre.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        GenreController controller =loader.getController();
        controller.setGenreList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void genreImageButton(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {


        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("genre.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        GenreController controller =loader.getController();
        controller.setGenreList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void myVideosButton(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("myVideos.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        VideosController controller =loader.getController();
        controller.setVideosList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void myVideosImageButton(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("myVideos.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        VideosController controller =loader.getController();
        controller.setVideosList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void playlistsButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("playlist.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        PlaylistController controller =loader.getController();
        controller.setPlaylists();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void playlistsImageButton(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("playlist.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        PlaylistController controller =loader.getController();
        controller.setPlaylists();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void addToPlaylistButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("addToPlaylist.fxml"));

       Parent parent = loader.load();

        Scene scene = new Scene(parent);

        AddToPlaylistController controller =loader.getController();
        controller.setPlaylists();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }
    @FXML
    public void addToPlaylistImageButton(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("playlist.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        PlaylistController controller =loader.getController();
        controller.setPlaylists();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }
    @FXML
    public void mostPlayedButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("mostPlayed.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        MostPlayedController controller =loader.getController();
        controller.setMostPlayedList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void mostPlayedImageButton(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("mostPlayed.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        MostPlayedController controller =loader.getController();
        controller.setMostPlayedList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public void incrementMostPlayed() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connection = DriverManager.getConnection(url, "root", "utkarshsql");
        String query = "UPDATE MainTable SET No_Of_Times = No_Of_Times+1 WHERE Song_Name='"+songName+"';";
        Statement statement=connection.createStatement();
        statement.executeUpdate(query);
    }

    public void updateRecentValues() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connection = DriverManager.getConnection(url, "root", "utkarshsql");
        String query = "SELECT MAX(Recent) FROM MainTable;";
        PreparedStatement pre = connection.prepareStatement(query);
        ResultSet result = pre.executeQuery();
        while(result.next()){
            int recentCounter = result.getInt("MAX(Recent)");
            System.out.println(recentCounter);
            if(recentCounter==15) {
                String query1 = "UPDATE MainTable SET Recent = Recent - 1 WHERE Recent>0;";
                Statement statement=connection.createStatement();
                statement.executeUpdate(query1);
                recentCounter--;
            }
            String query2 = "UPDATE MainTable SET Recent = "+recentCounter+"+1 WHERE Song_Name='" + songName + "';";
            Statement statement=connection.createStatement();
            statement.executeUpdate(query2);
        }
    }

    @FXML
    public void recentPlaysButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("recentPlays.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        RecentPlaysController controller =loader.getController();
        controller.setRecentList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void recentPlaysImageButton(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("recentPlays.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        RecentPlaysController controller =loader.getController();
        controller.setRecentList();
        controller.showRating(FXMLDocumentController.songName);
        controller.setVolume();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }

    void showRating(String songName) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connect = DriverManager.getConnection(url, "root", "utkarshsql");
        String query= "SELECT Rating from MainTable  WHERE Song_Name='" + songName +"'";
        PreparedStatement pre = connect.prepareStatement(query);
        ResultSet result = pre.executeQuery();
        Image ratedImage=new Image("\\images\\icons8-star-64.png");
        while (result.next()) {
            int rating = result.getInt("Rating");
            switch (rating){
                case 5:
                    star5.setImage(ratedImage);
                case 4:
                    star4.setImage(ratedImage);
                case 3:
                    star3.setImage(ratedImage);
                case 2:
                    star2.setImage(ratedImage);
                case 1:
                    star1.setImage(ratedImage);
            }

        }


    }

    @FXML
    public void setRating(MouseEvent event) throws FileNotFoundException, ClassNotFoundException, SQLException {
        Image unratedImage=new Image("\\images\\icons8-add-to-favorites-64.png");
        star1.setImage(unratedImage);
        star2.setImage(unratedImage);
        star3.setImage(unratedImage);
        star4.setImage(unratedImage);
        star5.setImage(unratedImage);
        ImageView star = (ImageView) event.getSource();
        String starID= star.getId();
        Image ratedImage=new Image("\\images\\icons8-star-64.png");

        switch (starID){
            case "star5":
                star5.setImage(ratedImage);
            case "star4":
                star4.setImage(ratedImage);
            case "star3":
                star3.setImage(ratedImage);
            case "star2":
                star2.setImage(ratedImage);
            case "star1":
                star1.setImage(ratedImage);
        }
        int starRating=starID.charAt(starID.length()-1)-'0';
        System.out.println(starRating);
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connect = DriverManager.getConnection(url, "root", "utkarshsql");

        String query = "UPDATE MainTable set Rating  ='"+starRating +"' WHERE Song_Name='" + songName +"'";
        Statement statement=connect.createStatement();
        statement.executeUpdate(query);
        }

        public void addNewPlaylist() throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AddAPlaylist.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add a playlist");
            stage.setScene(new Scene(root, 600, 300));
            stage.show();
        }


    public void playMyVideos(String videoUrl) {
        if(videoUrl!=null) {
            media = new Media(videoUrl);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaView.setFitWidth(400);
            mediaView.setFitHeight(300);
            mediaPlayer.play();
            play.setDisable(true);
            pause.setDisable(false);
            play.setVisible(false);
            pause.setVisible(true);
        }
    }
}




