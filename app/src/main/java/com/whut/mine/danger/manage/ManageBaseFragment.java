package com.whut.mine.danger.manage;

import com.afollestad.materialdialogs.MaterialDialog;
import com.whut.mine.adapter.ManageDoingOverAdapter;
import com.whut.mine.base.BaseFragment;
import com.whut.mine.data.ManageDoingOverItem;

import java.util.List;

import butterknife.Unbinder;

public abstract class ManageBaseFragment extends BaseFragment {

    Unbinder unbinder;
    MaterialDialog mProgressDialog;
    List<ManageDoingOverItem> mListItems;
    ManageDoingOverAdapter mAdapter;

    void clearAllItems() {
        if (mListItems != null) {
            mListItems.clear();
        }
    }

}
