<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>JOBON 로그인</title>
</head>
<body>

<h1>JOBON 로그인</h1>

<form method="post" action="/login">

    <div>
        <label for="username">아이디</label>
        <input type="text" id="username" name="username">
    </div>

    <div>
        <label for="password">비밀번호</label>
        <input type="password" id="password" name="password">
    </div>

    <button type="submit">로그인</button>

</form>

<a href="/main">메인으로</a>

</body>
</html>