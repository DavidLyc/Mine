package com.whut.mine.danger.rectify;

import com.google.gson.JsonArray;
import com.whut.mine.base.BasePresenter;
import com.whut.mine.base.BaseView;
import com.whut.mine.data.RectifyListItem;
import com.yanzhenjie.album.AlbumFile;

import java.util.List;

public interface RectifyOverdueContract {

    interface View extends BaseView<Presenter> {
        void showOverdueAlarms(List<RectifyListItem> items);

        void showNoData();

        void selectAllItems();

        void clearAllSelectedItems();

        void takePhoto(RectifyListItem item);

        void takePhotoFromAlbum(RectifyListItem item);

        void hideKeyboard();

        void showPhotoInfo(RectifyListItem item);

        void cancelProgressDialog();

        void addPhotoToRectifyListItem(List<String> labelPhotoUrls);

        void showRectifyTip();

        void clearAllItems();

        void showErrorDialog();

    }

    interface Presenter extends BasePresenter {
        void loadData();

        Boolean loadMoreCacheData();

        void loadOverdueAlarmInfo();

        void saveRectifyInfo(List<RectifyListItem> items);

        void postRectifyInfo(JsonArray json, List<String> urls);

        void saveShotPhoto(String result,RectifyListItem item);

        void saveAlbumPhoto(List<AlbumFile> albumFiles, RectifyListItem item);
    }

}
