package com.hip.ujr.ujrhip.Etc

class StringData {
    companion object {
        //게시물 타입
        const val POST_TYPE = "POST"
        const val NAUTH_POST_TYPE = "NON_AUTH"
        const val NEWS_TYPE = "NEWS"
        //초기화시 리스트 가져올 개수
        const val initialListSize = 20
        //하단 닿을시 리스트 추가 개수
        const val addListSize = 10
        //사진 없을시 저장 데이터
        const val EMPTY = "EMPTY"

        //게시물 생성 액티비티 request 코드
        const val CREATE_ACTIVITY = 123
        //게시물 등록 완료 후 result 코드
        const val UPLOAD_COMPLETED = 1111

        //키보드 View
        const val VISIBLE = 1213
        const val INVISIBLE = 1212

        //댓글창 intent name
        const val POSITION = "POSITION"
        const val POST_DATA = "POST_DATA"

        //어뎁터 푸터,아이템,헤더 구분
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
        const val TYPE_FOOTER = 2

        //인덱스 테이블 저장 구분 파티션
        const val POST = "partition"
        const val COMMENT = "Comment"

        //confirm 유저이름 인텐트 키
        const val USER_NAME = "USER_NAME"
    }
}