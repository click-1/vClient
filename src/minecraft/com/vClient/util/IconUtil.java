package com.vClient.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* Skidded from LiquidBounce src. Credits to them
 */
public class IconUtil {
    public static ByteBuffer[] getLoadingIcon() {
        try {
            return new ByteBuffer[] {readImageToBuffer(IconUtil.class.getResourceAsStream("/assets/minecraft/pictures/V_16x16.png")), readImageToBuffer(IconUtil.class.getResourceAsStream("/assets/minecraft/pictures/V_32x32.png"))};
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static ByteBuffer readImageToBuffer(final InputStream imageStream) throws IOException {
        if (imageStream == null)
            return null;
        final BufferedImage bufferedImage = ImageIO.read(imageStream);
        final int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        final ByteBuffer byteBuffer = ByteBuffer.allocate(4 * rgb.length);
        for (int i : rgb)
            byteBuffer.putInt(i << 8 | i >> 24 & 255);
        byteBuffer.flip();
        return byteBuffer;
    }
}
