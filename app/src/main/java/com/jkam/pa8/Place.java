/**
 * This program creates Search for nerby restraunts
 * CPSC 312-01, Fall 2019
 * Programming Assignment #8
 * No sources to cite.
 *
 * @author Jakob Kubicci and Ahmad Moltafet
 * @version v1.0 12/10/21
 */

package com.jkam.pa8;

public class Place {
    private String name;
    private String address;
    private String phoneNum;
    private boolean open;
    private String review;
    private String url;


    /**
     Constructor for the Place object
     *
     * @param name, address, phone, open, review
     */
    public Place(String name, String address, String phoneNum, boolean open, String review) {
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.open = open;
        this.review = review;
    }

    /**
     Convert to place to string
     *
     * @return name of the place
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     Getter for name
     *
     * @return name
     */
    public String getName() { return name; }

    /**
     Setter for url
     *
     * @param newUrl
     * @return area of a circle
     */
    public void setURL(String newUrl) { url = newUrl; }

    /**
     Getter for url
     *
     * @return area of a circle
     */
    public String getURL() { return url; }

    /**
     Setter for the name
     *
     * @param newName
     */
    public void setName(String newName) { name = newName; }

    /**
     Getter for the address
     *
     * @return address
     */
    public String getAddress() { return address; }

    /**
     Setter for address
     *
     * @param newAddress
     */
    public void setAddress(String newAddress) { name = newAddress; }

    /**
     Getter for the phone
     *
     * @return phone number
     */
    public String getPhone() { return phoneNum; }

    /**
     Setter for the phone
     *
     * @param newPhone number
     */
    public void setPhone(String newPhone) { phoneNum = newPhone; }

    /**
     Getter for the review
     *
     * @return review
     */
    public String getReview() { return review; }

    /**
     Setter for the review
     *
     * @param newReview
     */
    public void setReview(String newReview) { name = newReview; }

    /**
     Getter for open now
     *
     * @return open
     */
    public boolean getOpen() { return open; }

    /**
     Setter for the open
     *
     * @param open
     */
    public void setOpen(boolean open) { this.open = open; }

}
