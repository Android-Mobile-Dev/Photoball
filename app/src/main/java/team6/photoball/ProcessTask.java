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
    private Fragment fragment;
    private Bitmap mBitmap;
    private ImageView mImageView;
    static Configuration mConf;
    private Paint mBitmapPaint = new Paint();

    public ProcessTask(Context context, Fragment fragment, int requestCode, int resultCode, Intent data, int callerType){
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        this.context = context;
        this.callerType = callerType;
        this.fragment = fragment;
        this.mImageView = null;
    }

    //this is called BEFORE you start doing anything
    @Override
    protected void onPreExecute(){
        mImageView = (ImageView) fragment.getView().findViewById(callerType);
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
        if (callerType == R.id.imageViewGallery) {
            try {
                ((Gallery) fragment).mBitmap = mBitmap;
                ((Gallery) fragment).initRotateImageIfRequired();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (callerType == R.id.imageViewCamera) {
            try {
                ((Camera) fragment).mBitmap = mBitmap;
                ((Camera) fragment).initRotateImageIfRequired();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

            /*protected void onDraw(Canvas canvas) {
                canvas.drawColor(0xFFAAAAAA);
                canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            }*/

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

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String firstPartFileName = sdf.format(new Date());

                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

                File file = new File(new File(imageRoot.toString()), firstPartFileName + "_img.jpg");

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                values.put(MediaStore.MediaColumns.DATA, file.toString());

                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                int t = MyPicMaps.items.size() + 1;
                MyPicMaps.items.add(new ImageModel("Item " + t, file.getAbsolutePath()));

            }
        });

        return null;
    }

    private void modifyImage(File imageFile) {

        //Image modification here
        GPUImage gpuImage = new GPUImage(fragment.getActivity());
        mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        gpuImage.setImage(mBitmap);
        GPUImageFilterGroup groupFilter = new GPUImageFilterGroup();
        groupFilter.addFilter(new GPUImageSobelEdgeDetection());
        groupFilter.addFilter(new GPUImageColorInvertFilter());
        gpuImage.setFilter(groupFilter);
        mBitmap = gpuImage.getBitmapWithFilterApplied();
    }
}
