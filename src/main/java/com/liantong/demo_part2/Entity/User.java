package com.liantong.demo_part2.Entity;



public class User {

    private Integer user_id;

    private String login_name;

    private String password;

    private String is_admin;

    private String level;

    public User(Integer user_id, String login_name, String password, String is_admin, String level) {
        this.user_id = user_id;
        this.login_name = login_name;
        this.password = password;
        this.is_admin = is_admin;
        this.level = level;
    }

    public User(Integer user_id, String login_name, String password) {
        this.user_id = user_id;
        this.login_name = login_name;
        this.password = password;

    }
    public User( String login_name, String password) {
        this.user_id = user_id;
        this.login_name = login_name;
        this.password = password;

    }


    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", login_name='" + login_name + '\'' +
                ", password='" + password + '\'' +
                ", is_admin='" + is_admin + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
