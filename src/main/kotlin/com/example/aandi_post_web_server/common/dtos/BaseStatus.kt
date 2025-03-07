package com.example.aandi_post_web_server.common.dtos

import com.example.aandi_post_web_server.common.enum.ResultStatus

data class BaseResponse<T>(
    var status : String = ResultStatus.SUCCESS.name,
    var data : T? = null,
    var resultMsg : String = ResultStatus.SUCCESS.msg
)