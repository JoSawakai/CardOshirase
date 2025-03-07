/**
 * カードのドラッグ機能
 * カードのドラッグ、ホバー拡大などのインタラクティブな効果を実装
 */

// DOMのロードを待つ
document.addEventListener('DOMContentLoaded', () => {
    // すべてのカード要素を取得
    const cards = document.querySelectorAll('.card');
    
    // カードコンテナ要素を取得
    const cardsContainer = document.querySelector('.cards-container');
    
    // リセットボタンを取得
    const resetButton = document.getElementById('reset-all-cards');
    
    // ドラッグ層を作成，これによりドラッグされたカードが最上層に表示される
    const dragLayer = document.createElement('div');
    dragLayer.style.cssText = 'position:fixed; top:0; left:0; width:100%; height:100%; pointer-events:none; z-index:10000; display:none;';
    document.body.appendChild(dragLayer);
    
    // 拖拽状態を保存するための変数
    let isDragging = false; // 現在のドラッグ状態
    let currentCard = null; // 現在のドラッグされているカード
    let dragClone = null; // ドラッグ時のクローン要素
    let originalPosition = null; // カードの元の位置情報
    let mouseDownTime = 0; // マウスクリック時間
    
    // カードの階層順序を管理するための変数
    let topZIndex = 1000; // 開始z-index値
    
    // 初期化 - サーバーからカード位置をロード
    loadCardPositions();
    
    // すべてのカードに初期z-indexを設定
    setupInitialZIndex();
    
    // 各カードにイベントリスナーを追加
    cards.forEach(card => {
        // 直接的なクリックイベント - カードを最上層に移動
        card.addEventListener('mousedown', function(e) {
            // マウスクリック時間を記録
            mouseDownTime = Date.now();
            // 左クリックのみ処理
            if (e.button === 0) {
                bringToFront(this);
            }
        });
        
        // ドラッグ開始処理
        card.addEventListener('mousedown', startDrag);
        
        // カードのデフォルトドラッグ動作を阻止
        card.addEventListener('dragstart', function(e) {
            e.preventDefault();
        });
    });
    
    // リセットボタンのクリックイベントを追加
    if (resetButton) {
        resetButton.addEventListener('click', resetAllCards);
    }
    
    // グローバルマウス移動とマウス解放イベント
    document.addEventListener('mousemove', drag);
    document.addEventListener('mouseup', stopDrag);
    
    /**
     * カードの初期z-index設定
     */
    function setupInitialZIndex() {
        cards.forEach((card, index) => {
            // DOMに基づいてz-indexを設定
            card.style.zIndex = 100 + index;
        });
    }
    
    /**
     * カードを最上層に移動
     * @param {HTMLElement} card - 最上層に置くカード要素
     */
    function bringToFront(card) {
        // グローバル上位z-index値を増加
        topZIndex += 10;
        
        // 強制的に上位に設定
        card.style.setProperty('z-index', topZIndex);
        
        // 定位クラスを追加（まだない場合）
        if (!card.classList.contains('positioned')) {
            card.classList.add('positioned');
        }
        
        // DOM末尾に移動して表示優先度を高める
        cardsContainer.appendChild(card);
        
        console.log(`カードが最上層に置かれました，z-index: ${topZIndex}`);
    }
    
    /**
     * サーバーからすべてのカードの位置情報をロード
     */
    function loadCardPositions() {
        cards.forEach(card => {
            const cardId = card.getAttribute('data-card-id');
            
            // サーバーから位置情報を取得
            fetch(`/api/cards/position/${cardId}`)
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'success' && data.data) {
                        // 保存された位置を適用
                        const position = data.data;
                        
                        // 定位クラスを追加
                        card.classList.add('positioned');
                        
                        // 位置を設定
                        card.style.left = `${position.positionX}px`;
                        card.style.top = `${position.positionY}px`;
                        card.style.margin = '0';
                        
                        console.log(`カード ${cardId} の位置を読み込みました`);
                    }
                })
                .catch(error => {
                    console.log(`カード ${cardId} の保存位置が見つかりませんでした，デフォルト位置を使用します`);
                });
        });
    }
    
    /**
     * ドラッグ開始処理
     * @param {MouseEvent} e - マウスイベントオブジェクト
     */
    function startDrag(e) {
        // 左クリックのみ処理
        if (e.button !== 0) return;
        
        // 現在のドラッグ中のカードを記録
        currentCard = this;
        isDragging = true;
        
        // カードの元位置と状態を記録
        originalPosition = {
            position: currentCard.style.position || 'relative',
            left: currentCard.style.left || 'auto',
            top: currentCard.style.top || 'auto',
            zIndex: currentCard.style.zIndex || '100',
            transform: currentCard.style.transform || 'none',
            margin: currentCard.style.margin || '0',
            wasPositioned: currentCard.classList.contains('positioned')
        };
        
        // カードのコピーを作成し、ドラッグ層に追加
        dragClone = currentCard.cloneNode(true);
        
        // コピーの詳細内容を非表示にし、シンプルな外観を維持
        const detailElement = dragClone.querySelector('.card-detail');
        if (detailElement) {
            detailElement.style.display = 'none';
        }
        
        // カードのサイズとスタイルを取得
        const cardRect = currentCard.getBoundingClientRect();
        const computedStyle = window.getComputedStyle(currentCard);
        
        // 原カードと同じスタイルを適用
        dragClone.style.cssText = currentCard.style.cssText;
        dragClone.style.position = 'absolute';
        dragClone.style.width = `${cardRect.width}px`;
        dragClone.style.height = `${cardRect.height}px`;
        dragClone.style.left = `${e.clientX - (cardRect.width / 2)}px`;
        dragClone.style.top = `${e.clientY - (cardRect.height / 2)}px`;
        dragClone.style.zIndex = '9999';
        dragClone.style.pointerEvents = 'none';
        dragClone.style.opacity = '0.9';
        dragClone.style.boxShadow = '0 12px 24px rgba(0, 0, 0, 0.3)';
        dragClone.style.transform = 'none';
        dragClone.style.transition = 'none'; // 遷移効果を無効化
        
        // ドラッグ層を表示し、コピーを追加
        dragLayer.style.display = 'block';
        dragLayer.appendChild(dragClone);
        
        // 原カードを非表示にする
        currentCard.style.visibility = 'hidden';
        
        // イベントのバブルアップとデフォルト動作を阻止
        e.preventDefault();
        e.stopPropagation();
    }
    
    /**
     * ドラッグ中の処理
     * @param {MouseEvent} e - マウスイベントオブジェクト
     */
    function drag(e) {
        if (!isDragging || !currentCard || !dragClone) return;
        
        // カードのサイズを取得
        const width = dragClone.offsetWidth;
        const height = dragClone.offsetHeight;
        
        // コピーの位置を更新
        dragClone.style.left = `${e.clientX - (width / 2)}px`;
        dragClone.style.top = `${e.clientY - (height / 2)}px`;
        
        e.preventDefault();
    }
    
    /**
     * ドラッグ停止処理
     * @param {MouseEvent} e - マウスイベントオブジェクト
     */
    function stopDrag(e) {
        if (!isDragging || !currentCard || !dragClone) return;
        
        // ドラッグ中のコピーとコンテナの位置を取得
        const cloneRect = dragClone.getBoundingClientRect();
        const containerRect = cardsContainer.getBoundingClientRect();
        
        // カードのコンテナに対する相対位置を計算
        const relativeLeft = cloneRect.left - containerRect.left;
        const relativeTop = cloneRect.top - containerRect.top;
        
        // 原カードの可視性を復元
        currentCard.style.visibility = '';
        
        // 原カードの新位置を設定
        currentCard.classList.add('positioned'); // 定位クラスを追加
        currentCard.style.left = `${relativeLeft}px`;
        currentCard.style.top = `${relativeTop}px`;
        currentCard.style.margin = '0';
        
        // カードIDを取得
        const cardId = currentCard.getAttribute('data-card-id');
        console.log(`カード ${cardId} を位置: X=${relativeLeft}, Y=${relativeTop} に配置しました`);
        
        // カード位置をサーバーに保存
        saveCardPosition(cardId, relativeLeft, relativeTop);
        
        // ドラッグ中のコピーを削除し、ドラッグ層を非表示にする
        if (dragClone.parentNode) {
            dragClone.parentNode.removeChild(dragClone);
        }
        dragLayer.style.display = 'none';
        
        // カードを最上層に配置
        bringToFront(currentCard);
        
        // ドラッグ状態をリセット
        isDragging = false;
        currentCard = null;
        dragClone = null;
        originalPosition = null;
    }
    
    /**
     * すべてのカード位置をリセット
     */
    function resetAllCards() {
        // 確認ダイアログを表示
        if (confirm('すべてのカード位置をリセットしますか？')) {
            // すべてのカードをリセット
            cards.forEach(card => {
                card.classList.remove('positioned'); // 定位クラスを削除
                card.style.position = '';
                card.style.left = '';
                card.style.top = '';
                card.style.zIndex = '';
                card.style.transform = '';
                card.style.margin = '';
                card.style.visibility = '';
                
                const cardId = card.getAttribute('data-card-id');
                console.log(`カード ${cardId} を元の位置にリセットしました`);
            });
            
            // 上位z-indexをリセット
            topZIndex = 1000;
            
            // 基本z-indexを再設定
            setupInitialZIndex();
            
            // サーバーにすべてのカード位置をリセットするリクエストを送信
            fetch('/api/cards/reset-all', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    console.log('すべてのカード位置をリセットしました');
                } else {
                    console.error('すべてのカード位置をリセットできませんでした:', data.message);
                }
            })
            .catch(error => console.error('リセットリクエストに失敗しました:', error));
        }
    }
    
    /**
     * カード位置をサーバーに保存
     * @param {string} cardId - カードID
     * @param {number} x - X座標
     * @param {number} y - Y座標
     */
    function saveCardPosition(cardId, x, y) {
        // Spring Boot REST APIを呼び出す
        fetch('/api/cards/position', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                cardId: cardId,
                positionX: x,
                positionY: y
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                console.log('カード位置をサーバーに保存しました', data);
            } else {
                console.error('保存位置に失敗しました:', data.message);
            }
        })
        .catch(error => console.error('保存位置リクエストに失敗しました:', error));
    }
    
    /**
     * カード位置情報を削除
     * @param {string} cardId - カードID
     */
    function deleteCardPosition(cardId) {
        // Spring Boot REST APIを呼び出す
        fetch(`/api/cards/position/${cardId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('位置情報を削除できませんでした');
            }
            return response.json();
        })
        .then(data => {
            if (data.status === 'success') {
                console.log(`カード ${cardId} の位置情報をサーバーから削除しました`);
            } else {
                console.error(`カード ${cardId} の位置情報を削除できませんでした:`, data.message);
            }
        })
        .catch(error => console.error('删除位置请求失败:', error));
    }
}); 