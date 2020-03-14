package com.example.doujineventapp

import java.io.Serializable

data class Circle  (
    var id: Int, // データベースのID
    var penName: String, // サークル主のペンネーム
    var circleName: String, // サークル名
    var space: String, // スペース
    var price: Int, // 購入予定金額
    var giftExists: Boolean, // 差し入れの有無
    var note: String, // 備考
    var isChecked: Boolean // リストのチェックボックスの状態(購入済みか否か)
) : Serializable