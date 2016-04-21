package team6.photoball;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by nelma on 4/13/2016.
 */
public class ProcessTask extends AsyncTask<Void, Integer, Void> {

    private ProgressDialog progressDialog;
    private static Context context;
    private int requestCode, resultCode;
    private Intent data;
    private static int callerType;
    private static Fragment fragment;
    public static Bitmap mBitmap;
    private static ImageView mImageView;
    public static File mImageFile;

    public ProcessTask(Context tcontext, Fragment tfragment, int requestCode, int resultCode, Intent data, int tcallerType){
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        context = tcontext;
        callerType = tcallerType;
        fragment = tfragment;
    }

    //this is called BEFORE you start doing anything
    @Override
    protected void onPreExecute(){
        mImageView = (ImageView) fragment.getView().findViewById(callerType);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait, processing image");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    //every time you call publishProgress this method is executed, in this case receives an Integer
    @Override
    protected void onProgressUpdate(Integer ... option){

    }

    @Override
    protected void onPostExecute(Void unused){
        try {
            if (mBitmap != null) initRotateImageIfRequired();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (callerType == R.id.imageViewGallery) ((Gallery)fragment).b = true;
        if (callerType == R.id.imageViewCamera) ((Camera)fragment).b = true;
        progressDialog.dismiss();
    }

    //in here is where you execute your php script or whatever "heavy" stuff you need
    @Override
    protected Void doInBackground(Void... params) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, fragment.getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource imagePath, int type) {

                //Handle the image

                modifyImage(imageFile);

                String appDirectoryName = "Photoball";

                File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES) + "/" + appDirectoryName);

                if (!imageRoot.exists()) {
                    imageRoot.mkdirs();
                }

                ContentValues values = new ContentValues();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
                String firstPartFileName = sdf.format(new Date());

                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

                try {
                    FileOutputStream out = new FileOutputStream(imageFile);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File file = new File(new File(imageRoot.toString()), firstPartFileName + "_img.jpg");

                values.put(MediaStore.MediaColumns.DATA, file.toString());

                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                if (callerType == R.id.imageViewGallery) ((Gallery) fragment).mImageFile = file;
                if (callerType == R.id.imageViewCamera) ((Camera) fragment).mImageFile = file;
                mImageFile = file;

                int t = MyPicMaps.items.size() + 1;
                MyPicMaps.items.add(new ImageModel("Item " + t, file.getAbsolutePath()));

            }

            private void modifyImage(File imageFile) {
                int reqWidth = mImageView.getWidth();
                int reqHeight = mImageView.getHeight();

                decodeSampledBitmapFromResource(imageFile, reqWidth, reqHeight);

                //Image modification here
                GPUImage gpuImage = new GPUImage(fragment.getActivity());
                gpuImage.setImage(mBitmap);
                GPUImageFilterGroup groupFilter = new GPUImageFilterGroup();
                groupFilter.addFilter(new GPUImageSobelEdgeDetection());
                groupFilter.addFilter(new GPUImageColorInvertFilter());
                gpuImage.setFilter(groupFilter);
                mBitmap = gpuImage.getBitmapWithFilterApplied();
            }

            public void decodeSampledBitmapFromResource(File imageFile, int reqWidth, int reqHeight) {

                // First decode with inJustDecodeBounds=true to check dimensions
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                if (mBitmap != null) mBitmap.recycle();
                mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            }

            public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
                // Raw height and width of image
                final int height = options.outHeight;
                final int width = options.outWidth;
                int inSampleSize = 1;

                if (height > reqHeight || width > reqWidth) {

                    final int halfHeight = height / 2;
                    final int halfWidth = width / 2;

                    // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                    // height and width larger than the requested height and width.
                    while ((halfHeight / inSampleSize) > reqHeight
                            && (halfWidth / inSampleSize) > reqWidth) {
                        inSampleSize *= 2;
                    }
                }

                return inSampleSize;
            }
        });

        return null;
    }

    public static void initRotateImageIfRequired() throws IOException {
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (mBitmap.getWidth() < mBitmap.getHeight())
                rotateImage(90);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            if (mBitmap.getWidth() > mBitmap.getHeight())
                rotateImage(90);
        }
        ((ImageView) fragment.getView().findViewById(callerType)).setImageBitmap(mBitmap);
    }

    public static  void setRotateImageIfRequired(Configuration newConfig) throws IOException {
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (mBitmap.getWidth() < mBitmap.getHeight())
                rotateImage(270);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            if (mBitmap.getWidth() > mBitmap.getHeight())
                rotateImage(90);
        }
        ((ImageView) fragment.getView().findViewById(callerType)).setImageBitmap(mBitmap);
    }

    private static void rotateImage(int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
    }
}
