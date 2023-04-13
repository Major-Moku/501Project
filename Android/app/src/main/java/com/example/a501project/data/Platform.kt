package com.example.a501project.data

class Platform {
    lateinit var name: String
    lateinit var description: String
    val games =  ArrayList<Game>()
    // TODO 一个平台需要有哪些东西： 图标，描述，有哪些游戏，访问网络状态的方法.....
}