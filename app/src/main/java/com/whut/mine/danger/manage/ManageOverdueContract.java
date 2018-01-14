package com.whut.mine.danger.manage;

import com.whut.mine.base.BasePresenter;
import com.whut.mine.base.BaseView;
import com.whut.mine.data.ManageDoingOverItem;

import java.util.List;

public interface ManageOverdueContract {

    interface View extends BaseView<Presenter> {

        void showDangerItems(List<ManageDoingOverItem> items);

    }

    interface Presenter extends BasePresenter {

        void loadDoingDanger();

        void loadData();

        void loadMoreCacheData();

    }

}
