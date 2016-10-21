package cn.superid.webapp.service.impl;

import cn.superid.webapp.service.IPictureService;
import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.ReadRender;
import com.alibaba.simpleimage.render.ScaleParameter;
import com.alibaba.simpleimage.render.ScaleRender;
import com.alibaba.simpleimage.render.WriteRender;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jzy on 16/10/19.
 */
@Service
public class PictureService implements IPictureService {

    @Override
    public void cutPicture(InputStream inputStream, OutputStream outputStream, int x, int y, int width, int height) throws IOException {
        BufferedImage sourceImage = ImageIO.read(inputStream);
        if(x>=sourceImage.getWidth()) {
            x = 0;
        }
        if(y>=sourceImage.getHeight()) {
            y = 0;
        }
        if(width>sourceImage.getWidth()) {
            width = sourceImage.getWidth();
        }
        if(height > sourceImage.getHeight()) {
            height = sourceImage.getHeight();
        }
        if(x+width>sourceImage.getWidth()) {
            x = sourceImage.getWidth() - width;
        }
        if(y + height > sourceImage.getHeight()) {
            y = sourceImage.getHeight() - height;
        }
        if(x<0) {
            x = 0;
        }
        if(y < 0) {
            y = 0;
        }
        BufferedImage outImage = sourceImage.getSubimage(x, y, width, height);
        ImageIO.write(outImage, "jpg", outputStream);
    }

    @Override
    public void resizePicture(InputStream inputStream, OutputStream outputStream, int resizeWidth, int resizeHeight) throws IOException {
        ImageRender rr = new ReadRender(inputStream);
        ScaleParameter scaleParam = new ScaleParameter(resizeWidth,resizeHeight);
        ImageRender sr = new ScaleRender(rr, scaleParam);
        WriteRender wr =new WriteRender(sr, outputStream);

        try {
            wr.render();
        } catch (SimpleImageException e) {
            e.printStackTrace();
        }
    }

}
