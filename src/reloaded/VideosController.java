package reloaded;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

import static reloaded.FXMLDocumentController.mediaPlayer;
import static reloaded.FXMLDocumentController.songName;

public class VideosController {

    FXMLDocumentController controller = new FXMLDocumentController();

    @FXML
    ListView videosList;

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
    Slider slider;


    public void setVideosList() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        String url2 = "jdbc:mysql://localhost:3306/media";
        Connection connect = DriverManager.getConnection(url2, "root", "utkarshsql");


        String query2 = "SELECT * FROM MainTable where Extension ='mp4' GROUP BY Song_Name ORDER BY Song_Name ;";
        PreparedStatement pre = connect.prepareStatement(query2);
        ResultSet result = pre.executeQuery();


        StringBuffer space = new StringBuffer("");
        for (int i = 0; i <= 60; i++)
            space.append(" ");
        while (result.next()) {
            StringBuffer str = new StringBuffer("");
            String video = result.getString("Song_Name");
            String duration = result.getString("Duration");
            int rating = result.getInt("Rating");
            str.append(video.substring(0, video.length() - 4));
            int l = str.length();
            if (l <= 60) {
                str.append(space.substring(0, 60- l + 1));
            } else {
                str.replace(60, l + 1, "");
            }
            str.append(space.substring(0, 6));
            double double_duration = Double.parseDouble(duration);
            int int_duration = (int) double_duration;
            int hour = int_duration / 3600;
            int minute = (int_duration - hour * 3600) / 60;
            int second = (int_duration) % 60;
            str.append(String.format("%02d:%02d:%02d", hour, minute, second));
            if (rating == 0) {
                str.append(space.substring(0, 5));
                str.append("Not rated");
            } else {
                str.append(space.substring(0, 10));
                str.append(Integer.toString(rating));
            }
            videosList.getItems().add(str);

        }

        controller.incrementMostPlayed();

        boolean musicState=controller.getStatus();
        play.setDisable(musicState);
        pause.setDisable(!musicState);
        play.setVisible(!musicState);
        pause.setVisible(musicState);
        if(songLabel!=null)
            songLabel.setText(songName);
    }

    @FXML
    public void playVideo(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("homePage.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        FXMLDocumentController loaderController = loader.getController();

        String searchSong = videosList.getSelectionModel().getSelectedItem().toString();
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/media";
        Connection connect = DriverManager.getConnection(url, "root", "utkarshsql");
        String query2 = "SELECT Song_Name,SongUrl from MainTable;";
        PreparedStatement pre = connect.prepareStatement(query2);
        ResultSet result = pre.executeQuery();
        while (result.next()) {
            String video = result.getString("Song_Name");
            String videoUrl = result.getString("SongUrl");
            if (video.contains(searchSong.substring(0, 60).trim())) {
                FXMLDocumentController.songName = video;
                if (FXMLDocumentController.mediaPlayer != null)
                    FXMLDocumentController.mediaPlayer.stop();
                if (videoUrl != null)
                    loaderController.playMyVideos(videoUrl);

               /* FXMLDocumentController.mediaPlayer = new MediaPlayer(new Media(videoUrl));
                loaderController.mediaView.setMediaPlayer(FXMLDocumentController.mediaPlayer);
                FXMLDocumentController.mediaPlayer.play();*/
            }
        }
        loaderController.showRating(FXMLDocumentController.songName);
        loaderController.updateRecentValues();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (stage != null) {
            stage.setScene(scene);
            stage.show();
        }
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
        if (mediaPlayer != null) {
            slider.setValue(FXMLDocumentController.mediaPlayer.getVolume() * 100);
            slider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    FXMLDocumentController.mediaPlayer.setVolume(slider.getValue() / 100);
                }
            });
        }
    }
}
