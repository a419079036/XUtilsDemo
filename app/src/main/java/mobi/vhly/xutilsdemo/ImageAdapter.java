package mobi.vhly.xutilsdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by vhly[FR].
 * <p>
 * Author: vhly[FR]
 * Email: vhly@163.com
 * Date: 2016/10/21
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private List<String> mImageUrls;

    public ImageAdapter(Context context, List<String> imageUrls) {
        mContext = context;
        mImageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (mImageUrls != null) {
            ret = mImageUrls.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return mImageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        if (convertView != null) {
            ret = convertView;
        }else{
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ret = inflater.inflate(R.layout.item_image, parent, false);
        }

        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder(ret);
            ret.setTag(holder);
        }

        holder.mImageView.clearAnimation();

        ImageOptions.Builder builder = new ImageOptions.Builder();
        builder.setFadeIn(true);

        // 图片加载中的动画
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_translate);
        animation.setDuration(20 * position);
        builder.setAnimation(animation);

        ImageOptions options = builder.build();

        x.image().bind(
                holder.mImageView,
                mImageUrls.get(position),
                options
        );

        return ret;
    }

    private static class ViewHolder {
        @ViewInject(R.id.item_icon)
        private ImageView mImageView;

        public ViewHolder(View itemView){
            x.view().inject(this, itemView);
        }
    }

}
