package com.whut.mine.danger.manage;

import com.whut.mine.base.BasePresenter;
import com.whut.mine.base.BaseView;
import com.whut.mine.data.ManageTodoItem;

import java.util.List;

public interface ManageTodoContract {

    interface View extends BaseView<Presenter> {

        void showDangerItems(List<ManageTodoItem> items);

        void selectAllItems();

        void clearAllSelectedItems();

        void clearAllItems();

        void showRectifyInstructionDialog();

        void showPeopleTip(CharSequence[] peopleList, final Boolean isSending);

        void showErrorDialog();

    }

    interface Presenter extends BasePresenter {

        void loadTodoDanger();

        void loadData();

        void loadMoreCacheData();

        CharSequence[] searchPeopleTip(String inputPeople, String instNum);

        void sendRectInstruction(List<ManageTodoItem> selectedItems, RectifyMsg msg);

    }

}
