package com.whut.mine.check.detail;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whut.mine.base.BasePresenter;
import com.whut.mine.base.BaseView;
import com.whut.mine.data.CheckTableSecondItem;
import com.whut.mine.entity.SafetyCheckTableInfo;
import com.yanzhenjie.album.AlbumFile;

import java.util.List;

interface CheckDetailContract {

    interface View extends BaseView<Presenter> {

        void takePhoto(CheckTableSecondItem tempSecondItem);

        void takePhotoFromAlbum(CheckTableSecondItem tempSecondItem);

        void hidKeyboard();

        void cancelProgressDialog();

        void setRecyclerviewEntity(List<MultiItemEntity> entities, Boolean isVisible);

        SafetyCheckTableInfo getCheckTableInfo();

        void addPhotoUrlToSecondItem(List<String> labelPhotoUrls);

        void setPhotoLabel();

        void setCheckInfo(SafetyCheckTableInfo info);

        void finishActivity();

    }

    interface Presenter extends BasePresenter {

        void saveCheckTableInDao(Boolean isShowToast);

        void postCheckTable();

        void saveShotPhoto(String result, int startCount);

        void saveAlbumPhoto(List<AlbumFile> albumFiles, int startCount);

        void deleteCheckData();

    }

}
