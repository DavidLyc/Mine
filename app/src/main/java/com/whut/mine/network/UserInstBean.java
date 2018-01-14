package com.whut.mine.network;

import com.whut.mine.entity.Institution;
import com.whut.mine.entity.User;

import java.util.List;

public class UserInstBean {

    private List<User> users;
    private List<Institution> institutions;

    public UserInstBean(List<User> users, List<Institution> institutions) {
        this.users = users;
        this.institutions = institutions;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Institution> getInstitutions() {
        return institutions;
    }

}
