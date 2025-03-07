package com.anestec.hello.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.anestec.hello.model.CardPosition;

/**
 * カードサービス実装クラス
 */
@Service
public class CardServiceImpl implements CardService {

    // 使用内存存储カード位置 (実際のアプリケーションではデータベースを使用する必要があります)
    private final Map<String, CardPosition> cardPositions = new ConcurrentHashMap<>();

    @Override
    public CardPosition savePosition(String cardId, double positionX, double positionY) {
        // カード位置を作成または更新
        CardPosition position = new CardPosition(cardId, positionX, positionY);
        cardPositions.put(cardId, position);

        // ログを記録
        System.out.println("保存カード位置: " + position);

        return position;
    }

    @Override
    public CardPosition getCardPosition(String cardId) {
        // ストレージからカード位置を取得
        return cardPositions.get(cardId);
    }

    @Override
    public boolean deleteCardPosition(String cardId) {
        // カード位置が存在するかどうかを確認
        boolean exists = cardPositions.containsKey(cardId);

        if (exists) {
            // カード位置を削除
            cardPositions.remove(cardId);
            System.out.println("カード " + cardId + " の位置情報を削除しました");
            return true;
        }

        return false;
    }

    @Override
    public void resetAllCardPositions() {
        // すべてのカード位置をクリア
        cardPositions.clear();
        System.out.println("すべてのカード位置をリセットしました");
    }
}
