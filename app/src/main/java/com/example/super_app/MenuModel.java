package com.example.super_app;

public class MenuModel {

    private String cardTitle;
    private String cardText;
    private int cardImage;

    public MenuModel(String cardTitle, String cardText, int cardImage) {
        this.cardTitle = cardTitle;
        this.cardText = cardText;
        this.cardImage = cardImage;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public void setCardText(String cardText) {
        this.cardText = cardText;
    }

    public void setCardImage(int cardImage) {
        this.cardImage = cardImage;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getCardText() {
        return cardText;
    }

    public int getCardImage() {
        return cardImage;
    }
}
