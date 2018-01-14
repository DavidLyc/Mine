package com.whut.mine.network;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.whut.mine.entity.CheckTableFirstIndex;
import com.whut.mine.entity.CheckTableSecondIndex;
import com.whut.mine.entity.Institution;
import com.whut.mine.entity.SafetyCheckTable;
import com.whut.mine.entity.User;
import com.whut.mine.util.ApplicationUtil;
import com.whut.mine.util.DBUtils;
import com.whut.mine.util.ImageUtils;
import com.whut.mine.util.ShellUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class NetFactory {

    private static class SingletonHolder {
        private static NetFactory instance = new NetFactory();
    }

    private NetFactory() {
    }

    public static NetFactory getInstance() {
        return SingletonHolder.instance;
    }

    public Observable<Boolean> isAvailableByPing() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(isIpAvailable());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Boolean isIpAvailable() {
        String ip = DBUtils.getIp();
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(String.format("ping -c 1 -w 5 %s", ip)
                , false);
        return commandResult.result == 0;
    }

    public Observable<String> postDetailJson(final JsonArray jsonResult) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if (isIpAvailable()) {
                    DBUtils dbUtils = new DBUtils(8030);
                    String result = dbUtils.uploadJson(jsonResult);
                    e.onNext(result);
                } else {
                    e.onNext("-1");
                }
                e.onComplete();
            }
        });
    }

    public Observable<String> postHidDangerJson(final JsonArray jsonArray) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (isIpAvailable()) {
                    DBUtils dbUtils = new DBUtils(8060);
                    String sendResult = dbUtils.uploadJson(jsonArray);
                    e.onNext(sendResult);
                } else {
                    e.onNext("-1");
                }
                e.onComplete();
            }
        });
    }

    public Observable<String> postRectifyJson(final JsonArray jsonArray) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (isIpAvailable()) {
                    DBUtils dbUtils = new DBUtils(8050);
                    String result = dbUtils.uploadJson(jsonArray);
                    e.onNext(result);
                } else {
                    e.onNext("-1");
                }
                e.onComplete();
            }
        });
    }

    public Observable<UserInstBean> getUsersAndInst() {
        return Observable.create(new ObservableOnSubscribe<JsonArray>() {
            @Override
            public void subscribe(ObservableEmitter<JsonArray> e) throws Exception {
                DBUtils dbUtils = new DBUtils(8012);
                String json = "1";
                String result = dbUtils.connStr(json);
                e.onNext(new JsonParser().parse(result).getAsJsonArray());
                e.onComplete();
            }
        }).map(new Function<JsonArray, UserInstBean>() {
            @Override
            public UserInstBean apply(JsonArray jsonArray) throws Exception {
                List<User> users = new ArrayList<>();
                List<Institution> institutions = new ArrayList<>();
                JsonArray usersJson = jsonArray.get(0).getAsJsonArray();
                JsonArray institutionsJson = jsonArray.get(1).getAsJsonArray();
                for (JsonElement element : usersJson) {
                    JsonArray json = element.getAsJsonArray();
                    User user = new User();
                    user.setEmployeeNum(json.get(0).getAsString());
                    user.setEmployeeName(json.get(1).getAsString());
                    user.setInstitutionNum(json.get(2).getAsString());
                    user.setPassword(json.get(3).getAsString());
                    user.setUserRight(json.get(4).getAsString());
                    users.add(user);
                }
                for (JsonElement element : institutionsJson) {
                    JsonArray json = element.getAsJsonArray();
                    Institution inst = new Institution();
                    inst.setInstitutionNum(json.get(0).getAsString());
                    inst.setInstitutionName(json.get(1).getAsString());
                    inst.setICategoryNum(json.get(2).getAsInt());
                    inst.setPeopleInCharge(json.get(3).getAsString());
                    inst.setCategory(json.get(4).getAsString());
                    inst.setInstitutionPrefix(json.get(5).getAsString());
                    institutions.add(inst);
                }
                return new UserInstBean(users, institutions);
            }
        }).observeOn(Schedulers.io())
                .doOnNext(new Consumer<UserInstBean>() {
                    @Override
                    public void accept(UserInstBean bean) throws Exception {
                        User.saveUserInDB(bean.getUsers());
                        Institution.saveInstitutionInDB(bean.getInstitutions());
                    }
                });
    }

    public Observable<SafetyCheckBean> getSafetyCheckTables() {
        return Observable.create(new ObservableOnSubscribe<JsonArray>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<JsonArray> e) throws Exception {
                DBUtils dbUtils = new DBUtils(8011);
                SharedPreferences pref = ApplicationUtil.getInstance().getApplicationContext()
                        .getSharedPreferences("mine", MODE_PRIVATE);
                String userInst = pref.getString("user_inst", "user_inst");
                String json = "[\"" + userInst + "\"]";
                String accept = dbUtils.connStr(json);
                accept = accept.substring(accept.indexOf('['));
                JsonArray jsonArray = new JsonParser().parse(accept).getAsJsonArray();
                e.onNext(jsonArray);
                e.onComplete();
            }
        }).map(new Function<JsonArray, SafetyCheckBean>() {
            @Override
            public SafetyCheckBean apply(@NonNull JsonArray jsonArray) throws Exception {
                if (String.valueOf(jsonArray).length() < 5) {
                    return new SafetyCheckBean(null, null, null);
                }
                List<SafetyCheckTable> checkTables = new ArrayList<>();
                List<CheckTableFirstIndex> firstIndices = new ArrayList<>();
                List<CheckTableSecondIndex> secondIndices = new ArrayList<>();
                JsonArray checkTablesJson = jsonArray.get(0).getAsJsonArray();
                JsonArray firstIndicesJson = jsonArray.get(1).getAsJsonArray();
                JsonArray secondIndicesJson = jsonArray.get(2).getAsJsonArray();
                for (JsonElement element : checkTablesJson) {
                    JsonArray json = element.getAsJsonArray();
                    SafetyCheckTable checkTable = new SafetyCheckTable();
                    checkTable.setCheckTableID(json.get(0).getAsLong());
                    checkTable.setCheckTableNum(json.get(1).getAsString());
                    checkTable.setCheckTableName(json.get(2).getAsString());
                    checkTable.setCategory(json.get(3).getAsString());
                    checkTable.setInstitutionNum(json.get(4).getAsString());
                    checkTable.setVisibleToWho(json.get(5).getAsString());
                    checkTables.add(checkTable);
                }
                for (JsonElement element : firstIndicesJson) {
                    JsonArray json = element.getAsJsonArray();
                    CheckTableFirstIndex index = new CheckTableFirstIndex();
                    index.setFirstIndexID(json.get(0).getAsLong());
                    index.setCheckTableID(json.get(1).getAsInt());
                    index.setSerialNum(json.get(2).getAsInt());
                    index.setFirstIndexName(json.get(3).getAsString());
                    firstIndices.add(index);
                }
                for (JsonElement element : secondIndicesJson) {
                    JsonArray json = element.getAsJsonArray();
                    CheckTableSecondIndex index = new CheckTableSecondIndex();
                    index.setSecondIndexID(json.get(0).getAsLong());
                    index.setFirstIndexID(json.get(1).getAsInt());
                    index.setSerialNum(json.get(2).getAsString());
                    index.setSecondIndexName(json.get(3).getAsString());
                    index.setSecondIndexDemo(json.get(4).getAsString());
                    index.setDeleted(json.get(5).getAsString());
                    secondIndices.add(index);
                }
                return new SafetyCheckBean(checkTables, firstIndices, secondIndices);
            }
        }).observeOn(Schedulers.io())
                .doOnNext(new Consumer<SafetyCheckBean>() {
                    @Override
                    public void accept(SafetyCheckBean bean) throws Exception {
                        if (bean.getCheckTables() != null) {
                            //存入数据库
                            SafetyCheckTable.saveSafetyCheckTableInDB(bean.getCheckTables());
                            CheckTableFirstIndex.saveCheckTableFirstIndexInDB(bean.getFirstIndices());
                            CheckTableSecondIndex.saveCheckTableSecondIndexInDB(bean.getSecondIndices());
                        }
                    }
                });
    }

    private Observable<JsonArray> getDangers(final int confirmState, final int port) {
        return Observable.create(new ObservableOnSubscribe<JsonArray>() {
            @Override
            public void subscribe(ObservableEmitter<JsonArray> e) throws Exception {
                SharedPreferences pref = ApplicationUtil.getInstance().getApplicationContext()
                        .getSharedPreferences("mine", MODE_PRIVATE);
                String userNum = pref.getString("user_num", "user_num");
                String instNum = pref.getString("user_inst", "user_inst");
                JsonArray json = new JsonArray();
                json.add(userNum);
                json.add(instNum);
                json.add(String.valueOf(confirmState));
                DBUtils dbUtils = new DBUtils(port);
                String accept = dbUtils.connStr(String.valueOf(json));
                accept = accept.substring(accept.indexOf('['));
                JsonArray result = new JsonParser().parse(accept).getAsJsonArray();
                e.onNext(result);
                e.onComplete();
            }
        });
    }

    public Observable<JsonArray> getTodoDangers() {
        return getDangers(0, 8015);
    }

    public Observable<JsonArray> getDoingDangers() {
        return getDangers(1, 8014);
    }

    public Observable<JsonArray> getOverdueDangers() {
        return getDangers(2, 8016);
    }

    public Observable<JsonArray> getRectifyInfo(final String confirmState) {
        return Observable.create(new ObservableOnSubscribe<JsonArray>() {
            @Override
            public void subscribe(ObservableEmitter<JsonArray> e) throws Exception {
                JsonArray json = new JsonArray();
                SharedPreferences pref = ApplicationUtil.getInstance().getApplicationContext()
                        .getSharedPreferences("mine", MODE_PRIVATE);
                String user_num = pref.getString("user_num", "user_num");
                json.add(user_num);
                json.add(confirmState);
                DBUtils dbUtils = new DBUtils(8013);
                JsonArray result = new JsonParser().parse(dbUtils.connStr(String.valueOf(json))).getAsJsonArray();
                e.onNext(result);
                e.onComplete();
            }
        });
    }

    public Observable<Boolean> uploadImages(final List<String> imageList, final int checkSign) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                if (isIpAvailable()) {
                    for (String picPath : imageList) {
                        ImageUtils.uploadImage(picPath, checkSign);
                    }
                    e.onNext(true);
                } else {
                    e.onNext(false);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
