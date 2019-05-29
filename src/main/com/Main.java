package main.com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.com.controllers.MainController;
import main.com.controllers.SettingsController;
import org.opencv.core.Core;

import static main.com.SSHConnection.sendingCommands;

public class Main extends Application {

    public static Stage stageSettings = new Stage();

    public static String forwardPlatform;
    public static String backPlatform;
    public static String rightPlatform;
    public static String leftPlatform;
    public static String lightPlatform;
    public static String demoModePlatform;

    public static String forwardPlatformCommand;
    public static String backPlatformCommand;
    public static String rightPlatformCommand;
    public static String leftPlatformCommand;
    public static String lightPlatformCommand;
    public static String demoModePlatformCommand;
    public static String stopPlatformCommand;

    public static boolean lightActive = false;

    public static String upCamera;
    public static String downCamera;
    public static String rightCamera;
    public static String leftCamera;

    public static String pathCommandSend;

    private Scene scene;

    //private int x = 0, y = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Управление роботизированной платформой");
        scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("images/webcam.png"));
        primaryStage.show();

        MainController controller = loader.getController();
        primaryStage.setOnCloseRequest((event -> controller.setClosed()));

        stageSettings.setOnCloseRequest(event -> controller.updateGUI());
        stageSettings.setOnHiding(event -> controller.updateGUI());

        if (SettingsController.fileSettings.isFile()) {
            SettingsController.readSettings();
        }
        control();
    }

    public static void startSettings() throws Exception {

        Parent root = FXMLLoader.load(Main.class.getResource("/views/settings.fxml"));
        stageSettings.setTitle("Настройки");
        stageSettings.setScene(new Scene(root, 605, 550));
        stageSettings.getIcons().add(new Image("/images/settings.png"));
        stageSettings.setResizable(false);
        stageSettings.show();
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    private void control() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode().getName().equals(forwardPlatform)) {
                sendingCommands("echo '" + forwardPlatformCommand + "'" + pathCommandSend);
            }
            if (event.getCode().getName().equals(backPlatform)) {
                sendingCommands("echo '" + backPlatformCommand + "'" + pathCommandSend);
            }
            if (event.getCode().getName().equals(rightPlatform)) {
                sendingCommands("echo '" + rightPlatformCommand + "'" + pathCommandSend);
            }
            if (event.getCode().getName().equals(leftPlatform)) {
                sendingCommands("echo '" + leftPlatformCommand + "'" + pathCommandSend);
            }
            if (event.getCode().getName().equals(lightPlatform)) {
                if (!lightActive) {
                    sendingCommands("echo '" + lightPlatformCommand + "'" + pathCommandSend);
                    lightActive = true;
                } else {
                    sendingCommands("echo '" + lightPlatformCommand + "'" + pathCommandSend);
                    lightActive = false;
                }
            }
            if (event.getCode().getName().equals(demoModePlatform)) {
                sendingCommands("echo '" + demoModePlatformCommand + "'" + pathCommandSend);
            }

            /*if (event.getCode().getName().equals(upCamera)) {
                if (y < 90) {
                    ++y;
                    sendingCommands("echo '" + x + "," + y + "'" + pathCommandSend);
                }
            }
            if (event.getCode().getName().equals(downCamera)) {
                if (y > -90) {
                    --y;
                    sendingCommands("echo '" + x + "," + y + "'" + pathCommandSend);
                }
            }
            if (event.getCode().getName().equals(rightCamera)) {
                if (x < 90) {
                    ++x;
                    sendingCommands("echo '" + x + "," + y + "'" + pathCommandSend);
                }
            }
            if (event.getCode().getName().equals(leftCamera)) {
                if (x > -90) {
                    --x;
                    sendingCommands("echo '" + x + "," + y + "'" + pathCommandSend);
                }
            }*/
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode().getName().equals(forwardPlatform) ||
                    event.getCode().getName().equals(rightPlatform) ||
                    event.getCode().getName().equals(leftPlatform) ||
                    event.getCode().getName().equals(backPlatform)) {
                sendingCommands("echo '" + stopPlatformCommand + "'" + pathCommandSend);
            }
        });
    }
}