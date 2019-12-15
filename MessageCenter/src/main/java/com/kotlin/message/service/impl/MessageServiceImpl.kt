package com.kotlin.message.service.impl


import com.kotlin.base.ext.convert
import com.kotlin.message.data.protocol.Message
import com.kotlin.message.data.repository.MessageRepository
import com.kotlin.message.service.MessageService
import rx.Observable
import javax.inject.Inject

/*
   消息业务层
 */
class MessageServiceImpl @Inject constructor(): MessageService {

    @Inject
    lateinit var repository: MessageRepository

    /*
        获取消息列表
     */
    override fun getMessageList(): Observable<MutableList<Message>?> {
        return repository.getMessageList().convert()
    }

}
