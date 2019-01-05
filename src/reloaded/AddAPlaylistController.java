package reloaded;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class AddAPlaylistController {

    @FXML
    TextField newPlaylistList;
    AddToPlaylistController addToPlaylistController= new AddToPlaylistController();
    @FXML
    public void createPlaylist(ActionEvent event) throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("addToPlaylist.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        AddToPlaylistController controller =loader.getController();
        controller.playlistList.getItems().add(newPlaylistList.getText());

        Stage Primarystage=Reloaded.stageReference;

        Primarystage.setScene(scene);
        Stage  stage= (Stage)((Node)event.getSource()).getScene().getWindow();

        Primarystage.show();
        stage.close();

    }



}
