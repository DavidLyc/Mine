package com.whut.mine.check.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.whut.mine.R;
import com.whut.mine.adapter.CheckTableAdapter;
import com.whut.mine.album.AlbumActivity;
import com.whut.mine.data.CheckTableSecondItem;
import com.whut.mine.entity.Institution;
import com.whut.mine.entity.SafetyCheckTableInfo;
import com.whut.mine.ui.DividerDecoration;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class CheckDetailFragment extends Fragment implements CheckDetailContract.View {

    @BindView(R.id.checktable_recyclerview)
    RecyclerView mRecyclerview;
    Unbinder unbinder;
    @BindView(R.id.take_label_photo)
    ImageView mLabelPhoto;
    @BindView(R.id.check_type_spinner)
    MaterialSpinner mCheckTypeSpinner;
    @BindView(R.id.check_info_PeopleForCheck)
    EditText mPeopleForCheck;
    @BindView(R.id.take_label_photo_text)
    TextView mLabelPhotoText;
    @BindView(R.id.check_inst_spinner)
    MaterialSpinner mCheckInstSpinner;
    private CheckDetailContract.Presenter mPresenter;
    private CheckTableAdapter mAdapter;
    private String mLabelPhotoUrl = null;  //保存标记照片
    private CheckTableSecondItem mTempSecondItem;  //用于保存拍照结果
    private CheckTableSecondItem mShowPhotoSecondItem;  //保存AlbumActivity返回的图片结果
    private MaterialDialog mProgressDialog;
    private final List<String> mTypeList = new ArrayList<>(
            Arrays.asList("公司级", "车间级", "班组级", "专业级"));

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_check_detail, container, false);
        unbinder = ButterKnife.bind(this, root);
        initSpinner();
        initRecyclerview();
        mRecyclerview.setVisibility(View.INVISIBLE);
        return root;
    }

    public static CheckDetailFragment newInstance() {
        return new CheckDetailFragment();
    }

    private void initSpinner() {
        mCheckTypeSpinner.setItems(mTypeList);
        mCheckTypeSpinner.setGravity(Gravity.END);
        List<String> instList = Institution.getAllInstitution();
        instList.remove("高管");
        mCheckInstSpinner.setItems(instList);
        int selectedIndex = instList.indexOf(Institution.getUserInstitution());
        mCheckInstSpinner.setSelectedIndex(selectedIndex == -1 ? 0 : selectedIndex);
        mCheckInstSpinner.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void initRecyclerview() {
        mAdapter = new CheckTableAdapter(this, null);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerview.addItemDecoration(new DividerDecoration(getContext()));
        mRecyclerview.setNestedScrollingEnabled(false);
        mRecyclerview.setFocusable(false);
    }

    @Override
    public void setPresenter(CheckDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    List<String> urls = data.getStringArrayListExtra("photo_url_return");
                    mShowPhotoSecondItem.setPhotoUrl((ArrayList<String>) urls);
                }
                break;
        }
    }

    public void showPhotoView(CheckTableSecondItem item) {
        if (item.getPhotoUrl().size() == 0) {
            Toast.makeText(getContext(), "还没有可查看的图片！", Toast.LENGTH_SHORT).show();
        } else {
            mShowPhotoSecondItem = item;
            Intent intent = new Intent(getActivity(), AlbumActivity.class);
            intent.putStringArrayListExtra("photo_url", item.getPhotoUrl());
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void setRecyclerviewEntity(List<MultiItemEntity> entities, Boolean isVisible) {
        mRecyclerview.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        mAdapter.addData(entities);
        mAdapter.notifyDataSetChanged();
        mAdapter.expandAll();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = new MaterialDialog.Builder(getContext())
                .content("处理中")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .show();
    }

    @Override
    public void cancelProgressDialog() {
        mProgressDialog.cancel();
    }

    @Override
    public void takePhoto(final CheckTableSecondItem tempSecondItem) {

        mTempSecondItem = tempSecondItem;

        Album.camera(this)
                .image()
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        mPresenter.saveShotPhoto(result, tempSecondItem == null ?
                                0 : tempSecondItem.getPhotoUrl().size());
                    }
                }).start();
    }

    @Override
    public void takePhotoFromAlbum(final CheckTableSecondItem tempSecondItem) {

        mTempSecondItem = tempSecondItem;

        Album.image(this) // 选择图片
                .multipleChoice()
                .camera(false)  //不允许拍照
                .columnCount(3)
                .selectCount(9 - tempSecondItem.getPhotoUrl().size())
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        mPresenter.saveAlbumPhoto(result, tempSecondItem.getPhotoUrl().size());
                    }
                }).start();
    }

    @Override
    public void hidKeyboard() {
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public SafetyCheckTableInfo getCheckTableInfo() {
        SafetyCheckTableInfo mSafetyCheckTableInfo = new SafetyCheckTableInfo();
        int checkCategoryNum = mTypeList.indexOf(String.valueOf(mCheckTypeSpinner.getText())) + 1;
        mSafetyCheckTableInfo.setCheckCatogoryNum(checkCategoryNum);
        mSafetyCheckTableInfo.setValidatePic(mLabelPhotoUrl);
        mSafetyCheckTableInfo.setPeopleForCheck(String.valueOf(mPeopleForCheck.getText()));
        mSafetyCheckTableInfo.setInstitutionChecked(String.valueOf(mCheckInstSpinner.getText()));
        return mSafetyCheckTableInfo;
    }

    @Override
    public void addPhotoUrlToSecondItem(List<String> labelPhotoUrls) {
        for (String url : labelPhotoUrls) {
            mTempSecondItem.addPhotoUrl(url);
        }
    }

    @Override
    public void setPhotoLabel() {
        mLabelPhotoText.setText("已标记");
        mLabelPhotoText.setTextColor(getContext().getResources().getColor(R.color.OK_color));
        mLabelPhoto.setEnabled(false);
        mRecyclerview.setVisibility(View.VISIBLE);
        cancelProgressDialog();
    }

    @Override
    public void setCheckInfo(SafetyCheckTableInfo info) {
        mCheckTypeSpinner.setSelectedIndex(info.getCheckCatogoryNum() - 1);
        mCheckInstSpinner.setSelectedIndex(Institution.getAllInstitution().indexOf(info.getInstitutionChecked()) - 1);
        mLabelPhotoUrl = info.getValidatePic();
        setPhotoLabel();
        mPeopleForCheck.setText(info.getPeopleForCheck());
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @OnClick(R.id.take_label_photo)
    public void onViewClicked() {
        takePhoto(null);
    }

}
