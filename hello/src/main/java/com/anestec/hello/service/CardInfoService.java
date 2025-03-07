package com.anestec.hello.service;

import java.util.Map;

/**
 * カード情報サービスインターフェース カードのテキスト内容を取得するためのもの
 */
public interface CardInfoService {

    /**
     * すべてのカードの内容
     *
     * @return カードの内容
     */
    Map<String, String> getAllCardContents();

    /**
     * カードIDに基づいてカードの内容を取得
     *
     * @param cardId カードID
     * @return カードの内容テキスト
     */
    String getCardContent(String cardId);

    /**
     * すべてのカードの詳細内容
     *
     * @return カードIDと詳細内容のマップ
     */
    Map<String, String> getAllCardDetails();
}
