package com.whut.mine.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.whut.mine.R;
import com.whut.mine.adapter.AlbumAdapter;
import com.whut.mine.base.BaseActivity;
import com.whut.mine.data.AlbumItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumActivity extends AppCompatActivity implements BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.watchphoto_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    private List<String> mPhotoUrls;
    private List<AlbumItem> mPhotoViewItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        mPhotoUrls = getIntent().getStringArrayListExtra("photo_url");
        initToolbar();
        initRecyclerview();
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(mToolbar);
        setTitle("");
        mToolbarTitle.setText("查看图片");
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePhotos();
                finish();
            }
        });
    }

    public void initRecyclerview() {
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        mPhotoViewItems = new ArrayList<>();
        for (String photoUrl : mPhotoUrls) {
            mPhotoViewItems.add(new AlbumItem(photoUrl));
        }
        AlbumAdapter adapter = new AlbumAdapter(R.layout.image_item_view, mPhotoViewItems);
        mRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        handlePhotos();
        super.onBackPressed();
    }

    private void handlePhotos() {
        mPhotoUrls.clear();
        for (AlbumItem item : mPhotoViewItems) {
            mPhotoUrls.add(item.getPhotoPath());
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra("photo_url_return", (ArrayList<String>) mPhotoUrls);
        setResult(RESULT_OK, intent);
    }

}
