package main.com.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import main.com.Main;
import main.com.SSHConnection;
import org.ini4j.Ini;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    public static final File fileSettings = new File("settings.ini");

    @FXML
    private JFXTextField videoStreamAddressCameraFirstTextField;
    @FXML
    private JFXTextField videoStreamAddressCameraSecondTextField;
    @FXML
    private JFXCheckBox turnGray;

    @FXML
    private JFXTextField fpsTextField;

    @FXML
    private JFXTextField hostTextField;
    @FXML
    private JFXTextField usernameTextField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXTextField textFieldPathCommandSend;

    @FXML
    private JFXTextField forwardPlatformTextField;
    @FXML
    private JFXTextField backPlatformTextField;
    @FXML
    private JFXTextField rightPlatformTextField;
    @FXML
    private JFXTextField leftPlatformTextField;
    @FXML
    private JFXTextField lightPlatformTextField;
    @FXML
    private JFXTextField demoModePlatformTextField;

    @FXML
    private JFXTextField forwardPlatformCommandTextField;
    @FXML
    private JFXTextField backPlatformCommandTextField;
    @FXML
    private JFXTextField rightPlatformCommandTextField;
    @FXML
    private JFXTextField leftPlatformCommandTextField;
    @FXML
    private JFXTextField lightPlatformCommandTextField;
    @FXML
    private JFXTextField demoModePlatformCommandTextField;
    @FXML
    private JFXTextField stopPlatformCommandTextField;

    @FXML
    private JFXTextField upCameraTextField;
    @FXML
    private JFXTextField downCameraTextField;
    @FXML
    private JFXTextField rightCameraTextField;
    @FXML
    private JFXTextField leftCameraTextField;

    @FXML
    private JFXCheckBox displayButtonsControlCheckBox;
    @FXML
    private JFXCheckBox displayButtonCameraSecondCheckBox;

    @FXML
    private JFXCheckBox usbCameraFirstCheckBox;
    @FXML
    private JFXCheckBox usbCameraSecondCheckBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usbCameraFirstCheckBox.setSelected(MainController.isUSBCameraFirst);
        videoStreamAddressCameraFirstTextField.setText(MainController.videoStreamAddressCameraFirst);
        usbCameraSecondCheckBox.setSelected(MainController.isUSBCameraSecond);
        videoStreamAddressCameraSecondTextField.setText(MainController.videoStreamAddressCameraSecond);
        turnGray.setSelected(MainController.turnGray);

        hostTextField.setText(SSHConnection.host);
        usernameTextField.setText(SSHConnection.username);
        passwordField.setText(SSHConnection.password);
        textFieldPathCommandSend.setText(Main.pathCommandSend);
        displayButtonsControlCheckBox.setSelected(MainController.displayButtonsControl);
        displayButtonCameraSecondCheckBox.setSelected(MainController.displayButtonCameraSecond);

        forwardPlatformTextField.setText(Main.forwardPlatform);
        backPlatformTextField.setText(Main.backPlatform);
        rightPlatformTextField.setText(Main.rightPlatform);
        leftPlatformTextField.setText(Main.leftPlatform);
        lightPlatformTextField.setText(Main.lightPlatform);
        demoModePlatformTextField.setText(Main.demoModePlatform);

        forwardPlatformCommandTextField.setText(Main.forwardPlatformCommand);
        backPlatformCommandTextField.setText(Main.backPlatformCommand);
        rightPlatformCommandTextField.setText(Main.rightPlatformCommand);
        leftPlatformCommandTextField.setText(Main.leftPlatformCommand);
        lightPlatformCommandTextField.setText(Main.lightPlatformCommand);
        demoModePlatformCommandTextField.setText(Main.demoModePlatformCommand);
        stopPlatformCommandTextField.setText(Main.stopPlatformCommand);

        upCameraTextField.setText(Main.upCamera);
        downCameraTextField.setText(Main.downCamera);
        rightCameraTextField.setText(Main.rightCamera);
        leftCameraTextField.setText(Main.leftCamera);
    }

    @FXML
    private void saveSettings() {

        MainController.isUSBCameraFirst = usbCameraFirstCheckBox.isSelected();
        MainController.videoStreamAddressCameraFirst = videoStreamAddressCameraFirstTextField.getText();
        MainController.isUSBCameraSecond = usbCameraSecondCheckBox.isSelected();
        MainController.videoStreamAddressCameraSecond = videoStreamAddressCameraSecondTextField.getText();
        MainController.turnGray = turnGray.isSelected();

        SSHConnection.host = hostTextField.getText();
        SSHConnection.username = usernameTextField.getText();
        SSHConnection.password = passwordField.getText();
        Main.pathCommandSend = textFieldPathCommandSend.getText();

        Main.forwardPlatform = forwardPlatformTextField.getText();
        Main.backPlatform = backPlatformTextField.getText();
        Main.rightPlatform = rightPlatformTextField.getText();
        Main.leftPlatform = leftPlatformTextField.getText();
        Main.lightPlatform = lightPlatformTextField.getText();
        Main.demoModePlatform = demoModePlatformTextField.getText();

        Main.forwardPlatformCommand = forwardPlatformCommandTextField.getText();
        Main.backPlatformCommand = backPlatformCommandTextField.getText();
        Main.rightPlatformCommand = rightPlatformCommandTextField.getText();
        Main.leftPlatformCommand = leftPlatformCommandTextField.getText();
        Main.lightPlatformCommand = lightPlatformCommandTextField.getText();
        Main.demoModePlatformCommand = demoModePlatformCommandTextField.getText();
        Main.stopPlatformCommand = stopPlatformCommandTextField.getText();

        Main.upCamera = upCameraTextField.getText();
        Main.downCamera = downCameraTextField.getText();
        Main.rightCamera = rightCameraTextField.getText();
        Main.leftCamera = leftCameraTextField.getText();

        MainController.displayButtonsControl = displayButtonsControlCheckBox.isSelected();
        MainController.displayButtonCameraSecond = displayButtonCameraSecondCheckBox.isSelected();

        Ini iniWrite = new Ini();
        iniWrite.put("camera", "isUSBCameraFirst", usbCameraFirstCheckBox.isSelected());
        iniWrite.put("camera", "videoStreamAddressCameraFirst", videoStreamAddressCameraFirstTextField.getText());
        iniWrite.put("camera", "isUSBCameraSecond", usbCameraSecondCheckBox.isSelected());
        iniWrite.put("camera", "videoStreamAddressCameraSecond", videoStreamAddressCameraSecondTextField.getText());
        iniWrite.put("camera", "turnGray", turnGray.isSelected());

        iniWrite.put("connection", "host", hostTextField.getText());
        iniWrite.put("connection", "username", usernameTextField.getText());
        iniWrite.put("connection", "password", passwordField.getText());
        iniWrite.put("connection", "pathCommandSend", textFieldPathCommandSend.getText());

        iniWrite.put("control", "forwardPlatform", forwardPlatformTextField.getText());
        iniWrite.put("control", "backPlatform", backPlatformTextField.getText());
        iniWrite.put("control", "rightPlatform", rightPlatformTextField.getText());
        iniWrite.put("control", "leftPlatform", leftPlatformTextField.getText());
        iniWrite.put("control", "lightPlatform", lightPlatformTextField.getText());
        iniWrite.put("control", "demoModePlatform", demoModePlatformTextField.getText());

        iniWrite.put("control", "forwardPlatformCommand", forwardPlatformCommandTextField.getText());
        iniWrite.put("control", "backPlatformCommand", backPlatformCommandTextField.getText());
        iniWrite.put("control", "rightPlatformCommand", rightPlatformCommandTextField.getText());
        iniWrite.put("control", "leftPlatformCommand", leftPlatformCommandTextField.getText());
        iniWrite.put("control", "lightPlatformCommand", lightPlatformCommandTextField.getText());
        iniWrite.put("control", "demoModePlatformCommand", demoModePlatformCommandTextField.getText());
        iniWrite.put("control", "stopPlatformCommand", stopPlatformCommandTextField.getText());

        iniWrite.put("control", "upCamera", upCameraTextField.getText());
        iniWrite.put("control", "downCamera", downCameraTextField.getText());
        iniWrite.put("control", "rightCamera", rightCameraTextField.getText());
        iniWrite.put("control", "leftCamera", leftCameraTextField.getText());

        iniWrite.put("appearance", "displayButtonsControl", displayButtonsControlCheckBox.isSelected());
        iniWrite.put("appearance", "displayButtonCameraSecond", displayButtonCameraSecondCheckBox.isSelected());

        try {
            iniWrite.store(new FileOutputStream(fileSettings));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Main.stageSettings.close();
    }

    @FXML
    private void cancelSettings() {
        Main.stageSettings.close();
    }

    public static void readSettings() {
        Ini iniRead = new Ini();
        try {
            iniRead.load(new FileInputStream(fileSettings));
            MainController.isUSBCameraFirst = iniRead.get("camera", "isUSBCameraFirst", Boolean.class);
            MainController.videoStreamAddressCameraFirst = iniRead.get("camera", "videoStreamAddressCameraFirst", String.class);
            MainController.isUSBCameraSecond = iniRead.get("camera", "isUSBCameraSecond", Boolean.class);
            MainController.videoStreamAddressCameraSecond = iniRead.get("camera", "videoStreamAddressCameraSecond", String.class);
            MainController.turnGray = iniRead.get("camera", "turnGray", boolean.class);

            SSHConnection.host = iniRead.get("connection", "host", String.class);
            SSHConnection.username = iniRead.get("connection", "username", String.class);
            SSHConnection.password = iniRead.get("connection", "password", String.class);
            Main.pathCommandSend = iniRead.get("connection", "pathCommandSend", String.class);

            Main.forwardPlatform = iniRead.get("control", "forwardPlatform", String.class);
            Main.backPlatform = iniRead.get("control", "backPlatform", String.class);
            Main.rightPlatform = iniRead.get("control", "rightPlatform", String.class);
            Main.leftPlatform = iniRead.get("control", "leftPlatform", String.class);
            Main.lightPlatform = iniRead.get("control", "lightPlatform", String.class);
            Main.demoModePlatform = iniRead.get("control", "demoModePlatform", String.class);

            Main.forwardPlatformCommand = iniRead.get("control", "forwardPlatformCommand", String.class);
            Main.backPlatformCommand = iniRead.get("control", "backPlatformCommand", String.class);
            Main.rightPlatformCommand = iniRead.get("control", "rightPlatformCommand", String.class);
            Main.leftPlatformCommand = iniRead.get("control", "leftPlatformCommand", String.class);
            Main.lightPlatformCommand = iniRead.get("control", "lightPlatformCommand", String.class);
            Main.demoModePlatformCommand = iniRead.get("control", "demoModePlatformCommand", String.class);
            Main.stopPlatformCommand = iniRead.get("control", "stopPlatformCommand", String.class);

            Main.upCamera = iniRead.get("control", "upCamera", String.class);
            Main.downCamera = iniRead.get("control", "downCamera", String.class);
            Main.rightCamera = iniRead.get("control", "rightCamera", String.class);
            Main.leftCamera = iniRead.get("control", "leftCamera", String.class);

            MainController.displayButtonsControl = iniRead.get("appearance", "displayButtonsControl", boolean.class);
            MainController.displayButtonCameraSecond = iniRead.get("appearance", "displayButtonCameraSecond", boolean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

