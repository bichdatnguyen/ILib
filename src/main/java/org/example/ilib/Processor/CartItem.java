package org.example.ilib.Processor;

public class CartItem {
    private String id;
    private String name;
    private int volume;
    private int money;
    private String status;
    private String voucher;

    public CartItem(){

    }
    public CartItem(String id ,String name, int volume, int money, String status, String voucher) {
        this.id = id;
        this.name = name;
        this.volume = volume;
        this.money = money;
        this.status = status;
        this.voucher = voucher;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public int getVolume() {
        return volume;
    }

    public int getMoney() {
        return money;
    }

    public String getStatus() {
        return status;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

}
