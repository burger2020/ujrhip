package com.hip.ujr.ujrhip.Etc

class StringData {
    companion object {
        //초기화시 리스트 가져올 개수
        const val initialListSize = 20
        //하단 닿을시 리스트 추가 개수
        const val addListSize = 10
        //사진 없을시 저장 데이터
        const val EMPTY = "EMPTY"

        //aws s3 콜백
        const val COMPLETED = 853
        const val ERROR = 853
        //게시물 생성 액티비티 request 코드
        const val CREATE_ACTIVITY = 123
        //게시물 등록 완료 후 result 코드
        const val UPLOAD_COMPLETED = 1111

        //키보드 View
        const val VISIBLE = 1213
        const val INVISIBLE = 1212
    }
}