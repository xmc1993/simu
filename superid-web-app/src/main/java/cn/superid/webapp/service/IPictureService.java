package cn.superid.webapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jzy on 16/10/19.
 */
public interface IPictureService {

    void cutPicture(InputStream inputStream, OutputStream outputStream, int x, int y, int width, int height) throws IOException;

    void resizePicture(InputStream inputStream, OutputStream outputStream, int resizeWidth, int resizeHeight) throws IOException;
}
