package main.com.utils;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public final class Utils {

    public static Image mat2Image(Mat frame) {
        try {
            return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void onFXThread(final ObjectProperty<T> property, final T value) {
        Platform.runLater(() -> property.set(value));
    }

    private static BufferedImage matToBufferedImage(Mat original) {
        BufferedImage image;
        //объявляем переменные со значениями ширины, высоты и количеством цветовых каналов
        int width = original.width(), height = original.height(), channels = original.channels();
        //объявляем байтовый массив, этот массив представляет исходное количество пикселей,
        // размер его определяется с помощью произведения ширины, высоты и количества каналов
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1) { //цветовых каналов больше одного, то мы делаем цветное изображение
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else { //в противном случае черно-белое
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }
}
