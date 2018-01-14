package com.whut.mine.check.list;

import com.whut.mine.base.BasePresenter;
import com.whut.mine.base.BaseView;
import com.whut.mine.data.CheckListItem;

import java.util.List;

public interface CheckListContract {

    interface View extends BaseView<Presenter> {

        void setRecyclerViewItems(List<CheckListItem> items);

    }

    interface Presenter extends BasePresenter {

        void loadCheckTablesOnServer();

        void loadCheckTableInDB();

    }

}
