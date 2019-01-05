/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reloaded;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author DELL
 */
public class Reloaded extends Application {

    String databaseName;
    String tableName;
    String userName;
    String password;
    String url;
    static Stage stageReference;
    @Override
    public void start(Stage stage) throws Exception {

        try {
            stageReference=stage;
            databaseName = "media";
            userName = "root";
            password = "utkarshsql";
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=CONVERT_TO_NULL";
            Connection connection = DriverManager.getConnection(url,userName, password);

            String sql = "CREATE DATABASE if not exists " + databaseName;

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            System.out.println("Success");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {


           // Class.forName("com.mysql.cj.jdbc.Driver");
            url = "jdbc:mysql://localhost:3306/media";
            Connection connection = DriverManager.getConnection(url,userName, password);

            String sql = "CREATE Table if not exists MainTable(Song_Name varchar(200) ,SongUrl varchar(200), Artist varchar(100) ,Album  varchar(100) ,Genre varchar(50), No_Of_Times int(11) ,Recent int(11) ,Extension varchar(5) ,Rating int(11), Playlist varchar(50), Duration varchar(10) );";

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            System.out.println("Success");

        } catch (Exception e) {
            e.printStackTrace();
        }


        Parent root = FXMLLoader.load(getClass().getResource("homePage.fxml"));
        Scene scene = new Scene(root);
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent doubleclick) {
                if(doubleclick.getClickCount()==2)
                    stage.setFullScreen(true);
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
