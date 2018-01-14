package com.whut.mine.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.whut.mine.R;
import com.whut.mine.check.detail.CheckDetailFragment;
import com.whut.mine.data.CheckTableFirstItem;
import com.whut.mine.data.CheckTableSecondItem;
import com.whut.mine.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckTableAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_FIRSTINDEX = 1;
    public static final int TYPE_SECONDINDEX = 2;
    public static final int TYPE_THIRDINDEX = 3;
    private static int okColor;
    private static int notOkColor;
    private static final List<String> mTypeList = new ArrayList<>(Arrays.asList("设备设施类", "电气类"
            , "通风类", "顶板支护类", "文明生产类", "管理类", "不安全行为类", "警示标识类", "其他类"));
    private CheckDetailFragment mView;
    private View mTempFocusView;

    public CheckTableAdapter(Fragment fragment, List<MultiItemEntity> data) {
        super(data);
        //initial
        mView = (CheckDetailFragment) fragment;
        mContext = fragment.getContext();
        okColor = ContextCompat.getColor(mContext, R.color.OK_color);
        notOkColor = ContextCompat.getColor(mContext, R.color.NotOK_color);
        //add
        addItemType(TYPE_FIRSTINDEX, R.layout.checktable_first_index);
        addItemType(TYPE_SECONDINDEX, R.layout.checktable_second_index);
        expandAll();
        //set listener
        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mView.hidKeyboard();
                requestScreenFocus(view);
                int viewId = view.getId();
                switch (viewId) {
                    case R.id.second_index_check:
                        doWithCheckLabel(position);
                        break;
                    case R.id.second_index_check_icon:
                        doWithCheckLabel(position);
                        break;
                    case R.id.second_index_photo_icon:
                        doWithPhoto(position);
                        break;
                    case R.id.second_index_photo_text:
                        doWithPhoto(position);
                        break;
                    case R.id.second_index_watch_photo:
                        mView.showPhotoView((CheckTableSecondItem) getItem(position));
                        break;
                }
            }
        });
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {

        helper.addOnClickListener(R.id.second_index_check)
                .addOnClickListener(R.id.second_index_check_icon)
                .addOnClickListener(R.id.second_index_photo_icon)
                .addOnClickListener(R.id.second_index_photo_text)
                .addOnClickListener(R.id.second_index_watch_photo);

        switch (helper.getItemViewType()) {
            case TYPE_FIRSTINDEX:
                final CheckTableFirstItem firstItem = (CheckTableFirstItem) item;
                helper.setText(R.id.first_index_text, firstItem.getFirstIndexTitle())
                        .setImageResource(R.id.first_index_image,
                                firstItem.isExpanded() ? R.drawable.arrow_down : R.drawable.arrow_right)
                        .itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestScreenFocus(view);
                        mView.hidKeyboard();
                    }
                });
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = helper.getAdapterPosition();
                        notifyDataSetChanged();
                        if (firstItem.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;

            case TYPE_SECONDINDEX:
                final CheckTableSecondItem secondItem = (CheckTableSecondItem) item;
                final int status = secondItem.getCheckStatus();
                helper.setText(R.id.second_index_text, secondItem.getSecondIndexName())
                        .setText(R.id.second_index_check, status == 1 ? "合格" : "不合格")
                        .setTextColor(R.id.second_index_check, status == 1 ? okColor : notOkColor)
                        .setImageResource(R.id.second_index_check_icon, status == 1 ?
                                R.drawable.ok_icon : R.drawable.not_ok_icon)
                        .itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestScreenFocus(view);
                        mView.hidKeyboard();
                    }
                });
                View thirdItemView = helper.getView(R.id.third_index_view);
                if (secondItem.getCheckStatus() == 1) {
                    thirdItemView.setVisibility(View.GONE);
                    secondItem.setHidDangerType(mTypeList.get(0));
                } else {
                    thirdItemView.setVisibility(View.VISIBLE);
                    //spinner
                    final MaterialSpinner spinner = helper.getView(R.id.third_index_spinner);
                    spinner.setItems(mTypeList);
                    spinner.setGravity(Gravity.CENTER_HORIZONTAL);
                    spinner.setSelectedIndex(mTypeList.indexOf(secondItem.getHidDangerType()));
                    spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                            secondItem.setHidDangerType(item.toString());
                        }
                    });
                    //editText
                    final EditText editText = helper.getView(R.id.third_index_notOK_descrition);
                    editText.setText(secondItem.getHidDangerInfo());
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            secondItem.setHidDangerInfo(String.valueOf(charSequence).trim());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                editText.setCursorVisible(false);
                                mView.hidKeyboard();
                            }
                            return true;
                        }
                    });
                }
                break;
        }
    }

    private void doWithCheckLabel(int position) {
        final CheckTableSecondItem secondItem = (CheckTableSecondItem) getItem(position);
        assert secondItem != null;
        final int status = secondItem.getCheckStatus();
        secondItem.setCheckStatus(1 ^ status);
        notifyItemChanged(position);
    }

    private void doWithPhoto(int position) {
        final CheckTableSecondItem secondItem = (CheckTableSecondItem) getItem(position);
        assert secondItem != null;
        if (secondItem.getPhotoUrl().size() >= 9) {
            ToastUtil.getInstance().showToast("同一隐患最多上传9张照片！");
        } else {
            new MaterialDialog.Builder(mContext)
                    .items(R.array.photo_choice)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which == 0) {
                                mView.takePhoto(secondItem);
                            } else {
                                mView.takePhotoFromAlbum(secondItem);
                            }
                        }
                    }).show();
        }
    }

    private void requestScreenFocus(View view) {
        if (mTempFocusView != null) {
            mTempFocusView.clearFocus();
        }
        mTempFocusView = view;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

}
