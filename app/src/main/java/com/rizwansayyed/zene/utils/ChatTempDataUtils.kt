package com.rizwansayyed.zene.utils

object ChatTempDataUtils {

    var currentOpenedChatProfile: String? = null
    var doOpenChatOnConnect = false
    private var messageGroup: HashMap<String, Array<String>> = HashMap()
    private var nameGroup: HashMap<String, String> = HashMap()

    fun clearAMessage(email: String) {
        messageGroup.remove(email)
    }

    fun addAMessage(email: String?, message: String?) {
        email ?: return
        message ?: return
        val array = ArrayList<String>(10)
        val list = messageGroup[email] ?: emptyArray()
        array.addAll(list)
        array.add(0, message)
        messageGroup[email] = array.toTypedArray()
    }

    fun getAGroupMessage(email: String?) = messageGroup[email] ?: emptyArray()

    fun addAName(email: String?, name: String?) {
        email ?: return
        name ?: return
        nameGroup[email] = name
    }

    fun getNameGroupName(email: String?) = nameGroup[email] ?: "User"
}