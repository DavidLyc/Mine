package com.whut.mine.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whut.mine.R;
import com.whut.mine.data.AlbumItem;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AlbumAdapter extends BaseQuickAdapter<AlbumItem, BaseViewHolder> {

    public AlbumAdapter(@LayoutRes int layoutResId, @Nullable List<AlbumItem> data) {
        super(layoutResId, data);

        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int viewId = view.getId();
                switch (viewId) {
                    case R.id.image_item_delete:
                        deletePhoto(position);
                        break;
                }
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumItem item) {

        helper.addOnClickListener(R.id.image_item_delete);

        Log.d(TAG, item.getPhotoPath());

        Glide.with(mContext)
                .load(item.getPhotoPath())
                .transition(withCrossFade())
                .into((ImageView) helper.getView(R.id.image_item));
    }

    private void deletePhoto(int position) {
        AlbumItem item = getItem(position);
        getData().remove(item);
        notifyItemRemoved(position);
    }

}
