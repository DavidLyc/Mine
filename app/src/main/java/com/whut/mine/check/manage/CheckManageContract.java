package com.whut.mine.check.manage;

import com.whut.mine.base.BasePresenter;
import com.whut.mine.base.BaseView;
import com.whut.mine.data.CheckManageItem;

import java.util.List;

public interface CheckManageContract {

    interface View extends BaseView<Presenter> {

        void setRecyclerViewItems(List<CheckManageItem> items);

        void selectAllItems();

        void clearAllSelectedItems();

        void deleteSelectedItems();

    }

    interface Presenter extends BasePresenter {

        void loadSavedCheckTable();

        void sendJson(List<CheckManageItem> items);

        void deleteCheckRecord(List<CheckManageItem> items);

    }

}
