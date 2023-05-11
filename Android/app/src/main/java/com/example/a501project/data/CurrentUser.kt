package com.example.a501project.data

object CurrentUser {
    lateinit var nickname: String
    lateinit var username: String
    lateinit var password: String
    lateinit var favoriteList: List<Game>
    lateinit var description: String

    // TODO login需要使用的方法， 获取favoriteList....

    fun getFavList(): List<Game> {
        return favoriteList
    }

    fun syncFavoriteList(){
        // TODO http request
        // favoriteList =
    }

    fun addFavorite(game: Game) {

        // TODO http request
        syncFavoriteList()
    }

    fun setCredentials(username: String, password: String, description: String){
        this.username = username
        this.password = password
        this.description = description
    }

}