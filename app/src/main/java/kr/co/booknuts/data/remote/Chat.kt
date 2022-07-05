package kr.co.booknuts.data.remote

class Chat {
    var username: String? = null
    var message: String? = null
    var state: Boolean = false

    constructor() {}

    constructor(userName: String?, message: String?, state: Boolean) {
        this.username = userName
        this.message = message
        this.state = state
    }
}