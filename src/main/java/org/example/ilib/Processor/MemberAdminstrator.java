package org.example.ilib.Processor;

public class MemberAdminstrator {
    private MemberAdminstrator() {}
    private static MemberAdminstrator instance;
    public static MemberAdminstrator getInstance() {
        if (instance == null) {
            instance = new MemberAdminstrator();
        }
        return instance;
    }

    private String memberEmailAddress;
    private String memberPassword;
    private String memberFullname;
    private String memberPhoneNumber;

    public String getMemberEmailAddress() {
        return memberEmailAddress;
    }

    public void setMemberEmailAddress(String memberEmailAddress) {
        this.memberEmailAddress = memberEmailAddress;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public String getMemberFullname() {
        return memberFullname;
    }

    public void setMemberFullname(String memberFullname) {
        this.memberFullname = memberFullname;
    }

    public String getMemberPhoneNumber() {
        return memberPhoneNumber;
    }

    public void setMemberPhoneNumber(String memberPhoneNumber) {
        this.memberPhoneNumber = memberPhoneNumber;
    }
}
