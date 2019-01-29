package com.example.eafor.socialnetwork.adapter;

public class UserData {
    public String id, login, password, nickname, avatar, description, status;

    public UserData(String id, String login, String password, String nickname, String avatar, String description, String status){
        this.id=id;this.login=login;this.password=password;this.nickname=nickname;this.avatar = avatar;this.description=description;this.status=status;
    }
    public String toStr(){
        StringBuilder result=new StringBuilder("");
        result.append(id+" "+login+" "+password+" "+nickname+" "+ avatar +" "+description+" "+status);
        return result.toString();
    }
}