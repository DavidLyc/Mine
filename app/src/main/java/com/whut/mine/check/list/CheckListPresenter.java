package com.whut.mine.check.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.whut.mine.data.CheckListItem;
import com.whut.mine.entity.SafetyCheckTable;
import com.whut.mine.network.NetFactory;
import com.whut.mine.network.SafetyCheckBean;
import com.whut.mine.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CheckListPresenter implements CheckListContract.Presenter {

    private CheckListContract.View mView;
    private Context mContext;
    private CompositeDisposable mDisposables;
    private boolean mFirstLoad = true;

    CheckListPresenter(@NonNull CheckListContract.View view, @NonNull Context context) {
        mView = view;
        mContext = context;
        mView.setPresenter(this);
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        if (mFirstLoad) {
            mFirstLoad = false;
            loadCheckTableData();
        }
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }

    @Override
    public void destroy() {
        mView = null;
        mContext = null;
    }

    private void loadCheckTableData() {
        if (SafetyCheckTable.isSafetyCheckTableEmpty()) {
            mDisposables.add(NetFactory.getInstance()
                    .isAvailableByPing()
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean result) throws Exception {
                            if (result) {
                                loadCheckTablesOnServer();
                            } else {
                                Toast.makeText(mContext, "无法连接到服务器！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
            );
        } else {
            loadCheckTableInDB();
        }
    }

    @Override
    public void loadCheckTableInDB() {
        List<SafetyCheckTable> tables = SafetyCheckTable.getSafetyCheckTableInDB();
        List<CheckListItem> items = new ArrayList<>();
        for (SafetyCheckTable table : tables) {
            items.add(new CheckListItem(table.getCheckTableName(), table.getCheckTableID()));
        }
        mView.setRecyclerViewItems(items);
    }

    @Override
    public void loadCheckTablesOnServer() {
        mDisposables.add(NetFactory.getInstance()
                .getSafetyCheckTables()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<SafetyCheckBean>() {
                            @Override
                            public void accept(SafetyCheckBean bean) throws Exception {
                                if (bean.getCheckTables() != null) {
                                    List<CheckListItem> items = new ArrayList<>();
                                    List<SafetyCheckTable> tableList = bean.getCheckTables();
                                    for (SafetyCheckTable table : tableList) {
                                        items.add(new CheckListItem(table.getCheckTableName(), table.getCheckTableID()));
                                    }
                                    mView.setRecyclerViewItems(items);
                                } else {
                                    ToastUtil.getInstance().showToast("没有检查表！");
                                }
                            }
                        }
                )
        );
    }

}
