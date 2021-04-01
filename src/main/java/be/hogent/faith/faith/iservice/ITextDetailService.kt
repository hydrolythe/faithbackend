package be.hogent.faith.faith.iservice

import detail.TextDetail

interface ITextDetailService {
    fun insertText(text:String)
    fun loadExistingDetail(existingDetail: TextDetail):String
    fun onSaveClicked():TextDetail
}