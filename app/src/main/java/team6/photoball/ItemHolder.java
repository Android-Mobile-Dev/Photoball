package team6.photoball;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nelma on 4/13/2016.
 */
public class ItemHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    //TextView textView;
    public ItemHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.img);
        //textView = (TextView) itemView.findViewById(R.id.img_name);
    }
}
