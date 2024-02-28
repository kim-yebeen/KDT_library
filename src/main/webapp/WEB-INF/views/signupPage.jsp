<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sign Up</title>
</head>
<body>
<h2>회원가입</h2>
<form action="/signup" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />


    <div>
        <input type="text" name="id" placeholder="*아이디"/>
    </div>
    <div>
        <input type="password" name="pw" placeholder="*비밀번호"/>
    </div>
    <div>
        <input type="text" name="email" placeholder="*이메일"/>
    </div>
    <div>
        <input type="text" name="name" placeholder="*이름"/>
    </div>

    <button type="submit">회원가입</button>
</form>
</body>
</html>