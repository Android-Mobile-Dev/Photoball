package team6.photoball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

public class MyPicMapsPageAdapter extends BaseAdapter {
    private Context mContext;

    private String mAppDirectoryName = "Photoball";

    private File mImageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES) + "/" + mAppDirectoryName);

    private File[] mDirFiles = mImageRoot.listFiles();
    private Bitmap[] mThumbIds = new Bitmap[mDirFiles.length];

    public MyPicMapsPageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    private void ThumbIds() {
        System.out.println(mImageRoot.toString());
        for (int i = 0; i < mDirFiles.length; ++i) {
            mThumbIds[i] = (BitmapFactory.decodeFile(mDirFiles[i].toString()));
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }

        ThumbIds();
        imageView.setImageBitmap(mThumbIds[position]);
        return imageView;
    }
}