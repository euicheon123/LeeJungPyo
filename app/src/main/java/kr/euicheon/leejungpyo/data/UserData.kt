package kr.euicheon.leejungpyo.data

data class UserData(
    var userId: String? = null,
    var name: String? = null,
    var username: String? = null,
) {
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "username" to username
    )                                       // map 특정 value -> 해당 keyword 에 저장
}
