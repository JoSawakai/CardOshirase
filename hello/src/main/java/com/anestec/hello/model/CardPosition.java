package com.anestec.hello.model;

/**
 * カード位置データモデル
 */
public class CardPosition {

    private String cardId;    // カードID
    private double positionX; // X座標
    private double positionY; // Y座標

    /**
     * デフォルトのコンストラクタ
     */
    public CardPosition() {
    }

    /**
     * パラメータ付きのコンストラクタ
     *
     * @param cardId カードID
     * @param positionX X座標
     * @param positionY Y座標
     */
    public CardPosition(String cardId, double positionX, double positionY) {
        this.cardId = cardId;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    // Getter と Setter メソッド
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    @Override
    public String toString() {
        return "CardPosition{"
                + "cardId='" + cardId + '\''
                + ", positionX=" + positionX
                + ", positionY=" + positionY
                + '}';
    }
}
