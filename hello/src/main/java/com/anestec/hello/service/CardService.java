package com.anestec.hello.service;

import com.anestec.hello.model.CardPosition;

/**
 * カードサービスインターフェース
 */
public interface CardService {

    /**
     * カード位置を保存
     *
     * @param cardId カードID
     * @param positionX X座標
     * @param positionY Y座標
     * @return 保存後のカード位置オブジェクト
     */
    CardPosition savePosition(String cardId, double positionX, double positionY);

    /**
     * カード位置を取得
     *
     * @param cardId カードID
     * @return カード位置オブジェクト
     */
    CardPosition getCardPosition(String cardId);

    /**
     * カード位置を削除
     *
     * @param cardId カードID
     * @return 削除成功状況
     */
    boolean deleteCardPosition(String cardId);

    /**
     * すべてのカード位置をリセット
     */
    void resetAllCardPositions();
}
