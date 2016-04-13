package team6.photoball;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class MyPicMapsPageAdapter extends RecyclerView.Adapter<ItemHolder> {
    private Context context;
    static ArrayList<ImageModel> data;
    public MyPicMapsPageAdapter(Context context, ArrayList<ImageModel> data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ItemHolder itemHolder = new ItemHolder(layoutView);
        return itemHolder;
    }
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Glide.with(context).load(data.get(position).getUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((holder).imageView);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}

    /*

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
*/

