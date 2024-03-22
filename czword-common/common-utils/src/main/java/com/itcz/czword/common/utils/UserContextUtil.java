package com.itcz.czword.common.utils;


import com.itcz.czword.model.entity.user.User;

public class UserContextUtil {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();
    public static void setUser(User user){
        userThreadLocal.set(user);
    }
    public static User getUser(){
        return userThreadLocal.get();
    }
    public static void removeUser(){
        userThreadLocal.remove();
    }
}
