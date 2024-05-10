package com.hobbyhop.global.exception.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // JWT
    INVALID_JWT_SIGNATURE_EXCEPTION(401, "잘못된 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN_EXCEPTION(401, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN_EXCEPTION(401, "지원되지 않는 JWT 토큰입니다."),
    INVALID_JWT_EXCEPTION(401, "JWT 토큰이 잘못되었습니다"),
    INVALID_REFRESH_TOKEN_EXCEPTION(401, "RefreshToken이 유효하지 않습니다."),
    NOT_MISMATCHED_REFRESH_TOKEN_EXCEPTION(401, "DB의 리프레쉬 토큰 값과 다릅니다."),
    NO_JWT_EXCEPTION(401, "이 요청은 JWT가 필요합니다."),
    NOT_SUPPORTED_GRANT_TYPE_EXCEPTION(401, "지원하지 않는 권한 부여 유형입니다."),


    // 회원
    DUPLICATE_ENTRY_EXCEPTION(401, "중복된 사용자입니다. 다른 이름 또는 이메일을 입력해주세요."),
    SIGNUP_BLANK_EXCEPTION(401, "회원 가입란은 모두 필수입니다."),
    NOT_FOUND_USER_EXCEPTION(401, "회원 정보를 찾을 수 없습니다."),
    FAILED_AUTHENTICATION_EXCEPTION(401, "인증에 실패하였습니다."),
    ALREADY_EXIST_USERNAME_EXCEPTION(409, "이미 존재하는 이름입니다."),
    ALREADY_EXIST_EMAIL_EXCEPTION(409, "이미 존재하는 이메일입니다."),
    ALREADY_EXIST_USER_EXCEPTION(409, "이미 존재하는 유저입니다."),
    NOT_AVAILABLE_USERNAME_EXCEPTION(409, "사용할 수 없는 이름입니다."),
    NOT_AVAILABLE_EMAIL_EXCEPTION(409, "사용할 수 없는 이메일입니다."),
    UNAUTHORIZED_MODIFY_EXCEPTION(403, "수정할 권한이 없습니다."),
    NO_AUTHORIZATION_EXCEPTION(403, "접근 권한이 없습니다"),
    MISMATCHED_PASSWORD_EXCEPTION(401, "비밀번호가 일치하지 않습니다."),
    FAILED_LOGIN_EXCEPTION(401, "닉네임 또는 패스워드를 확인해주세요."),
    MISMATCHED_NEWPASSWORD_EXCEPTION(401, "새 비밀번호가 일치하지 않습니다."),
    MATCHED_PASSWORD_EXCEPTION(401, "새 비밀번호와 기존 비밀번호가 일치합니다."),
    JSON_PROCESSING_EXCEPTION(408, "요청 응답 시간 초과"),

    // 이메일 인증
    MISMATCHED_AUTH_CODE_EXCEPTION(401, "인증번호가 일치하지 않습니다."),
    NOT_FOUND_AUTH_CODE_EXCEPTION(401, "없는 인증 번호입니다."),

    // Club
    NOT_FOUND_CLUB_EXCEPTION(404, "해당 클럽을 찾을 수 없습니다."),
    ALREADY_USER_OF_CLUB_EXCEPTION(404, "이미 클럽에 유저가 있습니다."),
    ALREADY_CLUB_TITLE_EXIST_EXCEPTION(404, "이미 존재하는 클럽 명입니다."),

    // ClubMember
    NOT_FOUND_CLUB_MEMBER_EXCEPTION(404, "해당 멤버를 찾을 수 없습니다."),
    NO_PERMISSION_EXCEPTION(403, "수행할 권한이 없습니다."),
    CLUB_MEMBER_ALREADY_JOINED_EXCEPTION(409, "이미 가입된 유저입니다."),

    // JoinReqeust
    NO_SUCH_REQUEST_EXCEPTION(404, "존재하지 않는 요청입니다."),
    PENDING_REQUEST(409, "이전 가입신청에 대한 처리가 완료되지 않았습니다."),
    JOINING_CLUB_COUNT_EXCEEd(400, "가입 가능한 클럽의 갯수가 초과되었습니다."),

    // Comment
    NOT_FOUND_COMMENT_EXCEPTION(404, "해당 댓글을 찾을 수 없습니다."),

    // Post
    NOT_FOUND_POST_EXCEPTION(404, "해당 게시글을 찾을 수 없습니다."),
    NOT_CORRESPOND_USER_EXCEPTION(404, "해당 유저와 일치하는 게시글이 없습니다."),

    // 3S
    IMAGE_SAVE_EXCEPTION(401, "이미지 저장에 실패했습니다."),

    // Category
    NOT_FOUND_CATEGORY_EXCEPTION(404, "해당 카테고리를 찾을 수 없습니다."),
    ALREADY_EXIST_CATEGORY_NAME_EXCEPTION(400, "이미 존재하는 카테고리입니다.");

    private final int status;

    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
