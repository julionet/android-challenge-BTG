package com.msn.julio_net.desafiobtg.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ConvertHelper {

    public static Bitmap ByteArrayToBitmap(byte[] byteImage) {
        if (byteImage == null)
            return null;
        else
            return BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
    }
}
