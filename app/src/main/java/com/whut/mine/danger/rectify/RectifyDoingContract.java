package com.whut.mine.danger.rectify;

import com.google.gson.JsonArray;
import com.whut.mine.base.BasePresenter;
import com.whut.mine.base.BaseView;
import com.whut.mine.data.RectifyListItem;
import com.yanzhenjie.album.AlbumFile;

import java.util.List;

public interface RectifyDoingContract {

    interface View extends BaseView<Presenter> {
        void showDoingAlarms(List<RectifyListItem> items);

        void showNoData();

        void selectAllItems();

        void clearAllSelectedItems();

        void clearAllItems();

        void takePhoto(RectifyListItem item);

        void takePhotoFromAlbum(RectifyListItem item);

        void hideKeyboard();

        void showPhotoInfo(RectifyListItem item);

        void cancelProgressDialog();

        void addPhotoToRectifyListItem(List<String> labelPhotoUrls);

        void showRectifyTip();

        void showErrorDialog();
    }

    interface Presenter extends BasePresenter {
        void loadData();

        Boolean loadMoreCacheData();

        void loadDoingAlarmInfo();

        void saveRectifyInfo(List<RectifyListItem> items);

        void postRectifyInfo(JsonArray json,List<String> urls);

        void saveShotPhoto(String result,RectifyListItem rectifyListItem);

        void saveAlbumPhoto(List<AlbumFile> albumFiles, RectifyListItem item);
    }

}
