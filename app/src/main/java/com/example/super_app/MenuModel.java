package com.example.super_app;

public class MenuModel {

    private int cardTitle;
    private int cardText;
    private int cardImage;

    public MenuModel(int cardTitle, int cardText, int cardImage) {
        this.cardTitle = cardTitle;
        this.cardText = cardText;
        this.cardImage = cardImage;
    }

    public void setCardTitle(int cardTitle) {
        this.cardTitle = cardTitle;
    }

    public void setCardText(int cardText) {
        this.cardText = cardText;
    }

    public void setCardImage(int cardImage) {
        this.cardImage = cardImage;
    }

    public int getCardTitle() {
        return cardTitle;
    }

    public int getCardText() {
        return cardText;
    }

    public int getCardImage() {
        return cardImage;
    }
}
