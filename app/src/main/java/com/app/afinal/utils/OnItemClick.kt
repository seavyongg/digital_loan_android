package com.app.afinal.utils

interface OnItemClick<T : Any> {
    fun onItemClickListener(model: T, position: Int)
}
interface OnUpdateDeleteClick<T : Any> {
    fun onUpdateListener(model: T, position: Int)
    fun onDeleteListener(model: T, position: Int)
}
interface OnDeleteClick<T : Any> {
    fun onDeleteListenerWithPos(model: T, position: Int)
}
interface OnRequestResponse {
    fun onFailed(message: String)
}

interface OnBackResponse<T : Any>{
    fun success(message: T)
    fun fail(message: String)
}
