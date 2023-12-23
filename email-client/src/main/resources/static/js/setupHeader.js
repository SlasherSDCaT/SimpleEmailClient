$.ajaxSetup({
    beforeSend: setAuthorizationHeader,
});

function setAuthorizationHeader(xhr) {
    var token = getAccessToken();
    if (token) {
        xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    }
}

// Функция для получения значения токена из localStorage или кук
function getAccessToken() {
    // Проверяем, есть ли токен в куках
    var token = getCookie("jwt");

    // Если токена в куках нет, пытаемся получить из localStorage
    if (!token) {
        token = localStorage.getItem("jwt");
    }

    return token;
}

// Функция для получения значения куки по имени
function getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
}