package reloaded;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

import static reloaded.FXMLDocumentController.mediaPlayer;
import static reloaded.FXMLDocumentController.songName;

public class MostPlayedController {

    FXMLDocumentController controller = new FXMLDocumentController();

    @FXML
    Slider slider;

    @FXML
    ListView mostPlayedList;

    @FXML
    private Button play;

    @FXML
    private Button pause;

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

    @FXML
    Label songLabel;

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

    public void setMostPlayedList() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        String url2 = "jdbc:mysql://localhost:3306/media";
        Connection connect = DriverManager.getConnection(url2, "root", "utkarshsql");

        int times,l;
        StringBuffer space = new StringBuffer("");
        for(int i=0;i<80;i++)
            space.append(" ");
        StringBuffer str= new StringBuffer("");

        String query2 = "SELECT Song_Name,No_Of_Times FROM MainTable GROUP BY Song_Name ORDER BY No_Of_Times desc limit 10;";
        PreparedStatement pre = connect.prepareStatement(query2);
        ResultSet result = pre.executeQuery();

        while (result.next()) {
            StringBuffer s = new StringBuffer();
            String song = result.getString("Song_Name");
            times = result.getInt("No_Of_Times");

            if(song.length()<60) {
                for (int i = 0; i < 74 - song.length(); i++)
                    s.append(" ");
                mostPlayedList.getItems().add(song + s + times);
            }
            else {

                for (int i = 0; i < 74 - 60; i++)
                    s.append(" ");
                mostPlayedList.getItems().add(song.substring(0, 60) + s + times);
            }
        }
        boolean musicState=controller.getStatus();
        play.setDisable(musicState);
        pause.setDisable(!musicState);
        play.setVisible(!musicState);
        pause.setVisible(musicState);
        if(songLabel!=null)
            songLabel.setText(songName);
    }

    @FXML
    public void playMostPlayedList(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {
        String searchSong=mostPlayedList.getSelectionModel().getSelectedItem().toString().substring(0,35).trim();
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connect = DriverManager.getConnection(url, "root", "utkarshsql");
        String query= "SELECT Song_Name,SongUrl from MainTable;";
        PreparedStatement pre = connect.prepareStatement(query);
        ResultSet result = pre.executeQuery();
        while (result.next()) {
            String song = result.getString("Song_Name");
            String songUrl = result.getString("SongUrl");
            if (song.contains(searchSong)) {
                FXMLDocumentController.songName = song;
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
        showRating(FXMLDocumentController.songName);
        controller.updateRecentValues();
        controller.mostPlayedImageButton(event);
    }

    @FXML
    public void searchButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.transferToSearch(searchBar.getText(),event);
    }


    @FXML
    public void addToPlaylistButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.addToPlaylistButton(event);
    }

    @FXML
    public void addToPlaylistImageButton(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.addToPlaylistImageButton(event);
    }

    @FXML
    public void homeButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        controller.homeButton(event);
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
    public void recentPlaysButton(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.recentPlaysButton(event);
    }
    @FXML
    public void recentPlaysImageButton(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
        controller.recentPlaysImageButton(event);
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

        String query = "UPDATE MainTable set Rating  ='"+starRating +"' WHERE Song_Name='" + FXMLDocumentController.songName +"'";
        Statement statement=connect.createStatement();
        statement.executeUpdate(query);
    }

    public void setVolume() {
        if (mediaPlayer != null)
        slider.setValue(FXMLDocumentController.mediaPlayer.getVolume() * 100);
        slider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                FXMLDocumentController.mediaPlayer.setVolume(slider.getValue()/100);
            }
        });
    }
}
