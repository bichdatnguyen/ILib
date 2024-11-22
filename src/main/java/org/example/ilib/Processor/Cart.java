package org.example.ilib.Processor;

public class Cart {
    private String name;
    private int volume;
    private int money;
    private String status;
    private String voucher;

    public Cart(String name, int volume, int money, String status, String voucher) {
        this.name = name;
        this.volume = volume;
        this.money = money;
        this.status = status;
        this.voucher = voucher;
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


}
