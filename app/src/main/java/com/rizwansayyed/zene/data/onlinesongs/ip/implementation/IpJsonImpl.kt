package com.rizwansayyed.zene.data.onlinesongs.ip.implementation


import com.rizwansayyed.zene.data.onlinesongs.ip.AWSIpJsonService
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class IpJsonImpl @Inject constructor(private val ipJson: IpJsonService, private val awsIp: AWSIpJsonService) : IpJsonImplInterface {

    override suspend fun ip() = flow {
        emit(ipJson.ip())
    }.flowOn(Dispatchers.IO)

    override suspend fun awsIp() = flow {
        emit(awsIp.ip())
    }.flowOn(Dispatchers.IO)


}