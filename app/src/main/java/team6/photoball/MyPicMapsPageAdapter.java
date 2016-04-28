package team6.photoball;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.io.File;
import java.util.List;

public class MyPicMapsPageAdapter extends RecyclerView.Adapter<MyPicMapsPageAdapter.ItemHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener onItemClickListener;

    public MyPicMapsPageAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        v.setOnClickListener(this);
        return new ItemHolder(v);
    }
    @Override
    public void onBindViewHolder(MyPicMapsPageAdapter.ItemHolder holder, int position) {
        ImageModel item = MyPicMaps.items.get(position);
        holder.text.setText(item.getText());
        File iFile = new File(item.getImage());

        holder.image.setImageBitmap(null);

        Glide.with(context).load(iFile)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);

        holder.itemView.setTag(item.getImage());
    }

    @Override
    public int getItemCount() {
        return MyPicMaps.items.size();
    }

    @Override public void onClick(final View v) {
        onItemClickListener.onItemClick((String)v.getTag());
    }

    public interface OnItemClickListener {
        void onItemClick(String viewModel);
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