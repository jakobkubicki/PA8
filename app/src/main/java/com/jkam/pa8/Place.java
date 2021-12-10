package com.jkam.pa8;

public class Place {
    private String name;
    private String address;
    private String phoneNum;
    private boolean open;
    private String review;

    public Place(String name, String address, String phoneNum, boolean open, String review) {
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.open = open;
        this.review = review;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() { return name; }

    public void setName(String newName) { name = newName; }

    public String getAddress() { return address; }

    public void setAddress(String newAddress) { name = newAddress; }

    public String getPhone() { return phoneNum; }

    public void setPhone(String newPhone) { name = newPhone; }

    public String getReview() { return review; }

    public void setReview(String newReview) { name = newReview; }

    public boolean getOpen() { return open; }

    public void setOpen(boolean open) { this.open = open; }

}
