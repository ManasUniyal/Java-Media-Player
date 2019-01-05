package reloaded;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class SearchController {

    FXMLDocumentController controller = new FXMLDocumentController();

    @FXML
    ListView songsList;

    @FXML
    ListView artistList;

    @FXML
    ListView albumList;

    @FXML
    ListView genreList;

    @FXML
    ListView videosList;

    @FXML
    private Button play;

    @FXML
    private Button pause;

    @FXML
    Slider slider;

    @FXML
    TextField searchBar;

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

    ObservableSet<String> songsSet =FXCollections.observableSet();
    ObservableSet<String> artistSet =FXCollections.observableSet();
    ObservableSet<String> albumSet =FXCollections.observableSet();
    ObservableSet<String> genreSet =FXCollections.observableSet();
    ObservableSet<String> videosSet =FXCollections.observableSet();


    public void showSearchList(String search) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connect = DriverManager.getConnection(url, "root", "utkarshsql");

        String query1 = "SELECT Song_Name FROM MainTable WHERE Extension='mp4' ORDER BY Song_Name;";
        String query2 = "SELECT Song_Name,Artist,Genre,Album FROM MainTable WHERE Extension = 'mp3' ORDER BY Song_Name;";

        PreparedStatement pre = connect.prepareStatement(query1);
        ResultSet result1 = pre.executeQuery();
        while(result1.next()) {
            String video = result1.getString("Song_Name");
            if(video!=null && video.toLowerCase().contains(search.toLowerCase()))
              videosSet.add(video);
        }

        PreparedStatement post = connect.prepareStatement(query2);
        ResultSet result2 = post.executeQuery();
        while (result2.next()) {
            String song = result2.getString("Song_Name");
            String artist =result2.getString("Artist");
            String genre=result2.getString("Genre");
            String album=result2.getString("Album");

            if(song!=null && song.toLowerCase().contains(search.toLowerCase()))
                songsSet.add(song);
            if(artist!=null && artist.toLowerCase().contains(search.toLowerCase()))
                artistSet.add(artist);
            if(genre!=null && genre.toLowerCase().contains(search.toLowerCase()))
                genreSet.add(genre);
            if(album!=null && album.toLowerCase().contains(search.toLowerCase()))
                albumSet.add(album);
            }
            artistList.setItems(FXCollections.observableArrayList(artistSet));
            songsList.setItems(FXCollections.observableArrayList(songsSet));
            genreList.setItems(FXCollections.observableArrayList(genreSet));
            albumList.setItems(FXCollections.observableArrayList(albumSet));
            videosList.setItems(FXCollections.observableArrayList(videosList));



//            setVolume();
        }

    private void setVolume() {
        slider.setValue(FXMLDocumentController.mediaPlayer.getVolume() * 100);
       /* slider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                FXMLDocumentController.mediaPlayer.setVolume(slider.getValue()/100);
            }
        });*/

    }


    @FXML
    public void searchButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.transferToSearch(searchBar.getText(),event);
    }


    @FXML
    public void myMusicButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.myMusicButton(event);
    }
    @FXML
    public void myMusicImageButton(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.myMusicImageButton(event);
    }

    @FXML
    public void artistButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.artistButton(event);
    }
    @FXML
    public void artistImageButton(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.artistImageButton(event);
    }


    @FXML
    public void albumButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.albumButton(event);
    }
    @FXML
    public void albumImageButton(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.albumImageButton(event);
    }


    @FXML
    public void genreButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.genreButton(event);
    }
    @FXML
    public void genreImageButton(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.genreImageButton(event);
    }


    @FXML
    public void myVideosButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.myVideosButton(event);
    }
    @FXML
    public void myVideosImageButton(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.myVideosImageButton(event);
    }


    @FXML
    public void playlistsButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.playlistsButton(event);
    }
    @FXML
    public void playlistsImageButton(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.playlistsImageButton(event);
    }


    @FXML
    public void mostPlayedButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.mostPlayedButton(event);
    }
    @FXML
    public void mostPlayedImageButton(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.mostPlayedImageButton(event);
    }
    @FXML
    public void recentPlaysButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.recentPlaysButton(event);
    }
    @FXML
    public void recentPlaysImageButton(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.recentPlaysImageButton(event);
    }
    public void showRating(String songName) throws ClassNotFoundException, SQLException {
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

        String query = "UPDATE MainTable set Rating  ='"+starRating +"' WHERE Song_Name='" + FXMLDocumentController.songName +"'";
        Statement statement=connect.createStatement();
        statement.executeUpdate(query);
    }


    @FXML
    public void playButton(ActionEvent event){
        FXMLDocumentController.mediaPlayer.play();
        play.setDisable(true);
        pause.setDisable(false);
        play.setVisible(false);
        pause.setVisible(true);

    }
    @FXML
    public void pauseButton(ActionEvent event){
        FXMLDocumentController.mediaPlayer.pause();
        play.setDisable(false);
        pause.setDisable(true);
        play.setVisible(true);
        pause.setVisible(false);

    }

    @FXML
    public void playSongsList(MouseEvent event) throws ClassNotFoundException, SQLException {

        String songName= (String) songsList.getSelectionModel().getSelectedItem();
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connect = DriverManager.getConnection(url, "root", "utkarshsql");
        String query2 = "SELECT Song_Name,SongUrl from MainTable;";
        PreparedStatement pre = connect.prepareStatement(query2);
        ResultSet result = pre.executeQuery();
        while (result.next()) {
            String song = result.getString("Song_Name");
            String songUrl = result.getString("SongUrl");
            if (song.compareTo(songName)==0) {
                FXMLDocumentController.songName=song;
                if (FXMLDocumentController.mediaPlayer != null)
                    FXMLDocumentController.mediaPlayer.stop();
                FXMLDocumentController.mediaPlayer = new MediaPlayer(new Media(songUrl));
                FXMLDocumentController.mediaPlayer.play();
                play.setDisable(true);
                pause.setDisable(false);
                play.setVisible(false);
                pause.setVisible(true);
            }
        }
    }
    @FXML
    public void playArtistList(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("individualArtist.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        IndividualArtistController controller =loader.getController();

        controller.showArtistList(artistList.getSelectionModel().getSelectedItem().toString());

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();

    }
    @FXML
    public void playAlbumList(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("individualAlbum.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        IndividualAlbumController controller =loader.getController();

        controller.showAlbumList(albumList.getSelectionModel().getSelectedItem().toString());

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        stage.show();
    }
    @FXML
    public void playGenreList(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("individualGenre.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);
        IndividualGenreController controller =loader.getController();
        controller.showGenreList(genreList.getSelectionModel().getSelectedItem().toString());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void playVideosList(MouseEvent event) throws ClassNotFoundException, SQLException {
        String videoName= (String) videosList.getSelectionModel().getSelectedItem();
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connect = DriverManager.getConnection(url, "root", "utkarshsql");
        String query2 = "SELECT Song_Name,SongUrl from MainTable;";
        PreparedStatement pre = connect.prepareStatement(query2);
        ResultSet result = pre.executeQuery();
        while (result.next()) {
            String video = result.getString("Song_Name");
            String songUrl = result.getString("SongUrl");
            if (videoName.compareTo(video)==0) {
                FXMLDocumentController.songName=video;
                if (FXMLDocumentController.mediaPlayer != null)
                    FXMLDocumentController.mediaPlayer.stop();
                FXMLDocumentController.mediaPlayer = new MediaPlayer(new Media(songUrl));
                FXMLDocumentController.mediaPlayer.play();
                play.setDisable(true);
                pause.setDisable(false);
                play.setVisible(false);
                pause.setVisible(true);
            }
        }
    }


}
