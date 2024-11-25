package org.example.ilib.Processor;

public class updateAccount {
    private String newEmail;
    private String newPassword;
    private String acceptedPassword;
    private String newName;
    private String newPhoneNumber;
    private static updateAccount instance;
    private updateAccount() {}
    public static updateAccount getInstance() {
        if(instance == null) {
            instance = new updateAccount();
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
