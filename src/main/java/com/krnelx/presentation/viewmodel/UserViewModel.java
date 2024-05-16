package com.krnelx.presentation.viewmodel;

import com.krnelx.persistence.entity.Users.UsersRole;
import javafx.beans.property.*;

import java.util.UUID;
import java.util.StringJoiner;

public class UserViewModel {

    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty login = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final ObjectProperty<UsersRole> role = new SimpleObjectProperty<>();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    public UserViewModel(UUID id, String login, String password, UsersRole role, String name) {
        this.id.set(id);
        this.login.set(login);
        this.password.set(password);
        this.role.set(role);
        this.name.set(name);
    }

    public UserViewModel(UUID id, String name, String phone, String address) {
        this.id.set(id);
        this.name.set(name);
        this.phone.set(phone);
        this.address.set(address);
    }

    public String getLogin() {
        return login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public UUID getId() {
        return id.get();
    }

    public ObjectProperty<UUID> idProperty() {
        return id;
    }

    public void setId(UUID id) {
        this.id.set(id);
    }

    public String getlogin() {
        return login.get();
    }

    public StringProperty loginProperty() {
        return login;
    }

    public void setlogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public UsersRole getRole() {
        return role.get();
    }

    public ObjectProperty<UsersRole> roleProperty() {
        return role;
    }

    public void setRole(UsersRole role) {
        this.role.set(role);
    }
    public String getname() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setname(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserViewModel.class.getSimpleName() + "[", "]")
            .add("id=" + id.get())
            .add("login=" + login.get())
            .add("password=" + password.get())
            .add("Role=" + role.get())
            .add("name=" + name.get())
            .toString();
    }
}
