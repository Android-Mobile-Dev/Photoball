package team6.photoball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyPicMapsPageAdapter extends RecyclerView.Adapter<MyPicMapsPageAdapter.ItemHolder> {
    private Context context;
    private List<ImageModel> items;
    public MyPicMapsPageAdapter(Context context, List<ImageModel> items) {
        this.context = context;
        this.items = items;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ItemHolder itemHolder = new ItemHolder(layoutView);
        return itemHolder;
    }
    @Override
    public void onBindViewHolder(MyPicMapsPageAdapter.ItemHolder holder, int position) {
        ImageModel item = items.get(position);
        holder.text.setText(item.getText());
        File iFile = new File(item.getImage());
        Bitmap bitmap = BitmapFactory.decodeFile(iFile.getAbsolutePath());
        holder.image.setImageBitmap(bitmap);
        //Picasso.with(holder.image.getContext()).load(item.getImage()).into(holder.image);
        holder.itemView.setTag(item);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    protected static class ItemHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;

        public ItemHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
        }
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

