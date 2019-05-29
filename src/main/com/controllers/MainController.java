package main.com.controllers;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.com.Main;
import main.com.SSHConnection;
import main.com.utils.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static main.com.SSHConnection.sendingCommands;
import static main.com.SSHConnection.session;

public class MainController implements Initializable {

    @FXML
    private Button recordCamera;
    @FXML
    private Button lightButton;
    @FXML
    private JFXButton buttonCameraFirst;
    @FXML
    private JFXButton buttonCameraSecond;
    @FXML
    private JFXButton buttonTurnConnection;
    @FXML
    private AnchorPane buttonsControl;
    @FXML
    private ImageView imageViewFirst;
    @FXML
    private ImageView imageViewSecond;
    @FXML
    private VBox vBox;

    static boolean isUSBCameraFirst;
    static String videoStreamAddressCameraFirst;
    static boolean isUSBCameraSecond;
    static String videoStreamAddressCameraSecond;

    static boolean turnGray;
    static boolean displayButtonsControl;
    static boolean displayButtonCameraSecond;

    private ScheduledExecutorService serviceFrameGrabberFirst;
    private ScheduledExecutorService serviceFrameGrabberSecond;
    private ScheduledExecutorService serviceWriterFrame;

    private VideoCapture captureFirst = new VideoCapture();
    private VideoCapture captureSecond = new VideoCapture();

    private VideoWriter writer = new VideoWriter();

    private boolean cameraActiveFirst = false;
    private boolean cameraActiveSecond = false;

    private boolean connectionActive = false;
    private boolean recordActive = false;

    private Mat frame;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            recordCamera.setVisible(false); //При запуске программы делаем кнопку записи видео невидимой
            recordCamera.setGraphic(new ImageView("/images/record.png")); //устанавливаем изображения для кнопки записи видео
            lightButton.setVisible(displayButtonsControl); //устанавливаем видимость кнопки включения света в зависимости от настроек
            lightButton.setGraphic(new ImageView("/images/light.png")); //устанавливаем изображения для кнопки включения света
            buttonsControl.setVisible(displayButtonsControl); //устанавливаем видимость кнопок управления платформой в зависимости от настроек
            buttonCameraSecond.setVisible(displayButtonCameraSecond); //устанавливаем видимость кнопки захвата видео второй камеры в зависимости от настроек
        });
    }

    public void updateGUI() {
        Platform.runLater(() -> {
            lightButton.setVisible(displayButtonsControl);
            buttonsControl.setVisible(displayButtonsControl);
            buttonCameraSecond.setVisible(displayButtonCameraSecond);
        });
    }

    @FXML
    protected void startCameraFirst() {
        if (!cameraActiveFirst) { //если захват видеопотока не активен
            if (isUSBCameraFirst) { //если выбрана опция USB камара
                //то мы преобразуем полученное значение в тип Integer, и передаем это значение в качестве индификатора оборудования
                captureFirst.open(Integer.parseInt(videoStreamAddressCameraFirst));
            }
            if (!isUSBCameraFirst) { //если не выбрана опция USB камера
                captureFirst.open(videoStreamAddressCameraFirst); //то мы передаем путь к источнику видео
            }
            if (captureFirst.isOpened()) { //если привязка класса к источнику видео была успешной
                //изменяем значение переменной на true, тем самым обозначая что захват видео поток активен
                cameraActiveFirst = true;

                Runnable frameGrabber = () -> {
                    frame = grabFrame(captureFirst); //получение кадра
                    Image imageToShow = Utils.mat2Image(frame); //преобразование кадра
                    //изменяем размер элемента ImageView под размер окна
                    imageViewFirst.fitHeightProperty().bind(vBox.heightProperty().subtract(25));
                    imageViewFirst.fitWidthProperty().bind(vBox.widthProperty());
                    updateImageView(imageViewFirst, imageToShow); //помещаем изображение на элемент ImageView
                };
                serviceFrameGrabberFirst = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
                serviceFrameGrabberFirst.scheduleAtFixedRate(frameGrabber, 0,
                        (long) (1000 / captureFirst.get(Videoio.CAP_PROP_FPS)), TimeUnit.MILLISECONDS); //ждем 1000/FPS мс
                recordCamera.setVisible(true); //делаем кнопку записи видео видимой
                buttonCameraFirst.setStyle("-fx-border-color: #00c853"); //делаем рамку вокруг кнопки "Камера 1" зеленой

            } else { //если видео поток не доступен
                notConnectCamera(); //то вызывает функцию для вывода сообщения на экран о недоступности видеопотока
            }
        } else {
            buttonCameraFirst.setStyle("-fx-border-color: #d50000"); //делаем рамку вокруг кнопки "Камера 1" красной
            recordCamera.setVisible(false); //делаем кнопку записи видео невидимой
            cameraActiveFirst = false; //изменяем значение переменной на false, тем самым обозначая что камера в данный момент не активна
            recordActive = false; //изменяем значение переменной на false, тем самым обозначая что запись видео в данный момент не активна
            stopService(serviceWriterFrame); //вызываем функцию завершения потока и передаем ей процес записи видео
            stopRecord(); //вызываем функцию завершения записи видео
            stopService(serviceFrameGrabberFirst); //вызываем функцию завершения потока и передаем ей процес захвата видео
            stopCapture(captureFirst); //вызываем функцию завершения захвата видео
        }
    }


    @FXML
    protected void startCameraSecond() {
        if (!cameraActiveSecond) {
            if (isUSBCameraSecond) {
                captureSecond.open(Integer.parseInt(videoStreamAddressCameraSecond));
            }
            if (!isUSBCameraSecond) {
                captureSecond.open(videoStreamAddressCameraSecond);
            }
            if (captureSecond.isOpened()) {
                cameraActiveSecond = true;

                Runnable frameGrabber = () -> {
                    Mat frame = grabFrame(captureSecond);
                    Image imageToShow = Utils.mat2Image(frame);
                    updateImageView(imageViewSecond, imageToShow);
                };
                serviceFrameGrabberSecond = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
                serviceFrameGrabberSecond.scheduleAtFixedRate(frameGrabber, 0,
                        (long) (1000 / captureSecond.get(Videoio.CAP_PROP_FPS)), TimeUnit.MILLISECONDS);
                buttonCameraSecond.setStyle("-fx-border-color: #00c853");
            } else {
                notConnectCamera();
            }
        } else {
            buttonCameraSecond.setStyle("-fx-border-color: #d50000");
            cameraActiveSecond = false;
            stopService(serviceFrameGrabberSecond);
            stopCapture(captureSecond);
        }
    }

    private void notConnectCamera() {
        JFXAlert alert = new JFXAlert((Stage) buttonCameraFirst.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("ОШИБКА"));
        layout.setBody(new Label("Не удалось установить соединение с камерой..."));
        JFXButton closeButton = new JFXButton("ПРИНЯТЬ");
        closeButton.getStyleClass().add("dialog-accept");
        closeButton.setOnAction(event -> alert.hideWithAnimation());
        layout.setActions(closeButton);
        alert.setContent(layout);
        alert.show();
    }

    @FXML
    protected void recordCamera() {
        if (captureFirst.isOpened()) {
            if (!recordActive) { //если запись видео не активна
                recordActive = true; //изменяем значение переменной на true, тем самым обозначая что запись видео активна
                /*FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(//
                        new FileChooser.ExtensionFilter("AVI", "*.avi"),
                        new FileChooser.ExtensionFilter("WMV", "*.wmv"),
                        new FileChooser.ExtensionFilter("MOV", "*.mov"),
                        new FileChooser.ExtensionFilter("MKV", "*.mkv"));
                File videoFile = fileChooser.showSaveDialog(recordCamera.getScene().getWindow());*/

                //устанавливаем формат даты и времени, которые будут оборажатся названии сохраненного видео
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH-mm-ss");
                //задаем параметры с которыми будет происходить запись видео, а именно название, кодек, частота кадров, ширина и высота, а так же цветное или нет
                writer = new VideoWriter(dateFormat.format(new Date()) + ".avi",
                        VideoWriter.fourcc('X', 'V', 'I', 'D'), captureSecond.get(Videoio.CAP_PROP_FPS),
                        new Size(frame.width(), frame.height()), !turnGray);

                Runnable writerFrame = (() -> writer.write(frame)); //запускаем поток для записи видео и начинаем запись

                serviceWriterFrame = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
                serviceWriterFrame.scheduleAtFixedRate(writerFrame, 0,
                        (long) (1000 / captureSecond.get(Videoio.CAP_PROP_FPS)), TimeUnit.MILLISECONDS);

                recordCamera.setGraphic(new ImageView("/images/stop.png")); //изменяем изображение кнопки
            } else {
                recordCamera.setGraphic(new ImageView("/images/record.png")); //изменяем изображение кнопки
                recordActive = false; //изменяем значение переменной на true, тем самым обозначая что запись видео не активна
                stopService(serviceWriterFrame); //вызываем функцию завршения потока
                stopRecord(); //вызываем функцию завршения записи
            }
        }
    }


    private Mat grabFrame(VideoCapture capture) {

        Mat frame = new Mat();

        if (capture.isOpened()) { //если привязка класса к источнику видео была успешной
            try {
                capture.read(frame); //то мы считываем текущий кадр

                if (turnGray && frame.empty()) { //если в настройках включен черно-белый видеопоток, и кадр не пустой
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY); //то мы преобразуем кадр в черно-белый формат
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return frame; //возвращаем кадр
    }

    private void stopCapture(VideoCapture capture) {
        if (capture.isOpened()) {
            capture.release(); //особождаем
        }
    }

    private void stopRecord() {
        if (writer.isOpened()) {
            writer.release();
        }
    }

    private void stopService(ScheduledExecutorService service) {
        if (service != null && !service.isShutdown()) { //если поток запущен
            try {
                service.shutdown(); //инициируем упорядоченное завершение ранее поступивших задач, и отключаем отправку новых задач
                //ждем 1 секунду пока все задачи не завершат выполнение
                if (!service.awaitTermination(1, TimeUnit.SECONDS)) { //если по прошествию времени задачи не завершены
                    service.shutdownNow(); //то отменяем выполнение всех текущщих задач
                    if (!service.awaitTermination(1, TimeUnit.SECONDS)) { //ждем еще 1 секунду пока задачи не отреагируют на отмену
                        System.err.println("Не удалось завершить задачи"); //если задачи по прежнему не завершены, то выводим сообщение об этом в консоль
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    public void setClosed() {
        stopService(serviceWriterFrame);
        stopRecord();
        stopService(serviceFrameGrabberFirst);
        stopService(serviceFrameGrabberSecond);
        stopCapture(captureFirst);
        stopCapture(captureSecond);
        if (connectionActive) {
            session.disconnect();
        }
    }

    @FXML
    private void settings() throws Exception {
        Main.startSettings();
    }

    @FXML
    private void forwardPlatform() {
        sendingCommands("echo '" + Main.forwardPlatformCommand + "'" + Main.pathCommandSend);
    }

    @FXML
    private void backPlatform() {
        sendingCommands("echo '" + Main.backPlatformCommand + "'" + Main.pathCommandSend);
    }

    @FXML
    private void rightPlatform() {
        sendingCommands("echo '" + Main.rightPlatformCommand + "'" + Main.pathCommandSend);
    }

    @FXML
    private void leftPlatform() {
        sendingCommands("echo '" + Main.leftPlatformCommand + "'" + Main.pathCommandSend);
    }

    @FXML
    private void stopPlatform() {
        sendingCommands("echo '" + Main.stopPlatformCommand + "'" + Main.pathCommandSend);
    }

    @FXML
    private void light() {
        if (!Main.lightActive) {
            sendingCommands("echo '" + Main.lightPlatformCommand + "'" + Main.pathCommandSend);
            lightButton.setGraphic(new ImageView("/images/lightTurn.png"));
            Main.lightActive = true;
        } else {
            sendingCommands("echo '" + Main.lightPlatformCommand + "'" + Main.pathCommandSend);
            lightButton.setGraphic(new ImageView("/images/light.png"));
            Main.lightActive = false;
        }
    }

    @FXML
    private void demoMode() {
        sendingCommands("echo '" + Main.demoModePlatformCommand + "'" + Main.pathCommandSend);
    }

    @FXML
    private void turnConnection() {
        if (!connectionActive) {
            SSHConnection.connection();
            if (SSHConnection.session.isConnected()) {
                buttonTurnConnection.setStyle("-fx-border-color: #00c853");
                connectionActive = true;
            }
        } else {
            SSHConnection.session.disconnect();
            if (!SSHConnection.session.isConnected()) {
                buttonTurnConnection.setStyle("-fx-border-color: #d50000");
                connectionActive = false;
            }
        }
    }

/*    @FXML
    private void mouseCameraControl(MouseEvent mouseEvent) {
        double centerX = mouseEvent.getSceneX() - (imageViewFirst.getFitWidth() / 2);
        double centerY = (mouseEvent.getSceneY() - 25) - (imageViewFirst.getFitHeight() / 2);
        double degreeX = centerX / (imageViewFirst.getFitWidth() / 180);
        double degreeY = centerY / (imageViewFirst.getFitHeight() / 180);
        sendingCommands("echo '" + degreeX + "," + degreeY + "'" + Main.pathCommandSend);
    }*/
}
