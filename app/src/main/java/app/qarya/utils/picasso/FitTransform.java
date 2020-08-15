package app.qarya.utils.picasso;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class FitTransform implements Transformation {

    int targetWidth;

    public FitTransform(int imageWidth) {
        this.targetWidth = imageWidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
        int targetHeight = (int) (targetWidth * aspectRatio);
        if (targetWidth>0 && targetHeight>0){
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }else {
            return source;
        }
    }

    @Override
    public String key() {
        return "transformation" + " desiredWidth";
    }
}