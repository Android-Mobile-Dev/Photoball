package team6.photoball;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private ProgressDialog progressDialog; //to show a little modal with a progress Bar
    private Context context;  //needed to create the progress bar
    int requestCode, resultCode;
    Intent data;
    int callerType;
    Fragment fragment;
    Bitmap mBitmap;

    public ProcessTask(Context context, Fragment fragment, int requestCode, int resultCode, Intent data, int callerType){
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        this.context = context;
        this.callerType = callerType;
        this.fragment = fragment;
    }

    //this is called BEFORE you start doing anything
    @Override
    protected void onPreExecute(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait, processing image");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    //every time you call publishProgress this method is executed, in this case receives an Integer
    @Override
    protected void onProgressUpdate(Integer ... option){
        progressDialog.setMessage("I have found :" + option[0]);
    }

    @Override
    protected void onPostExecute(Void unused){
        progressDialog.dismiss(); //hides the progress bar
        //do whatever you want now
        ImageView img = (ImageView) fragment.getView().findViewById(callerType);
        img.setImageBitmap(mBitmap);
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

                Bitmap bitmap = modifyImage(fragment, imageFile);

                if (callerType == R.id.imageViewCamera) {
                    try {
                        bitmap = rotateImageIfRequired(context, bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mBitmap = bitmap;

                String appDirectoryName = "Photoball";

                File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES) + "/" + appDirectoryName);

                if (!imageRoot.exists()) {
                    imageRoot.mkdirs();
                }

                ContentValues values = new ContentValues();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String firstPartFileName = sdf.format(new Date());

                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

                File file = new File(new File(imageRoot.toString()), firstPartFileName + "_img.jpg");

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                values.put(MediaStore.MediaColumns.DATA, file.toString());

                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
        });

        return null;
    }

    private Bitmap rotateImageIfRequired(Context context, Bitmap img) throws IOException {

        int orientation = context.getResources().getConfiguration().orientation;

        switch (orientation) {
            case 0:
                if (img.getWidth() < img.getHeight())
                    return rotateImage(img, 90);
            case 1:
                if (img.getWidth() > img.getHeight())
                    return rotateImage(img, 90);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private Bitmap modifyImage(Fragment fragment, File imageFile) {

        //Image modification here
        GPUImage gpuImage = new GPUImage(fragment.getActivity());
        Bitmap bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        gpuImage.setImage(bm);
        GPUImageFilterGroup groupFilter = new GPUImageFilterGroup();
        groupFilter.addFilter(new GPUImageSobelEdgeDetection());
        groupFilter.addFilter(new GPUImageColorInvertFilter());
        gpuImage.setFilter(groupFilter);
        Bitmap bmWithFilter = gpuImage.getBitmapWithFilterApplied();

        if (callerType == R.id.imageViewGallery) {
            try {
                ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                }
                else if (orientation == 3) {
                    matrix.postRotate(180);
                }
                else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                bmWithFilter = Bitmap.createBitmap(bmWithFilter, 0, 0, bmWithFilter.getWidth(), bmWithFilter.getHeight(), matrix, true); // rotating bitmap
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bmWithFilter;
    }
}
