package ua.ck.zabochen.android.paging3.common.extension

import retrofit2.Response
import ua.ck.zabochen.android.paging3.common.state.ResultState

//fun <T : Any> Response<T>.toResultState(): ResultState<T> {
//    return try {
//        val response = this
//        if (response.isSuccessful && response.body() != null) {
//            ResultState.Success(data = response.body()!!)
//        } else {
//            ResultState.Error("${response.code()}: ${response.message()} ")
//        }
//    } catch (e: Exception) {
//        ResultState.Error( "${e.message}")
//    }
//}
