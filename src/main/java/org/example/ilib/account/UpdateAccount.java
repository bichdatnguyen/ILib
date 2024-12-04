package org.example.ilib.account;

public class UpdateAccount {
    private String newEmail;
    private String newPassword;
    private String acceptedPassword;
    private String newName;
    private String newPhoneNumber;
    private static UpdateAccount instance;

    private UpdateAccount() {
    }

    public static UpdateAccount getInstance() {
        if (instance == null) {
            instance = new UpdateAccount();
        }
        return instance;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }

    public void setNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }

    public String getAcceptedPassword() {
        return acceptedPassword;
    }

    public void setAcceptedPassword(String acceptedPassword) {
        this.acceptedPassword = acceptedPassword;
    }
}
