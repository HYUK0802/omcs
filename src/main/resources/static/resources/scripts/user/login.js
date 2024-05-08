const loginForm = document.getElementById('loginForm');
// const btn = document.getElementById('button');
loginForm.emailWarning = loginForm.querySelector('[rel="emailWarning"]');
loginForm.emailWarning.show = (text) => {
    loginForm.emailWarning.innerText = text;
    loginForm.emailWarning.classList.add('visible');
};

loginForm.emailWarning.hide = () => loginForm.emailWarning.classList.remove('visible');
loginForm.passwordWarning = loginForm.querySelector('[rel="passwordWarning"]');
loginForm.passwordWarning.show = (text) => {
    loginForm.passwordWarning.innerText = text;
    loginForm.passwordWarning.classList.add('visible');
};
loginForm.passwordWarning.hide = () => loginForm.passwordWarning.classList.remove('visible');
loginForm.loginWarning = loginForm.querySelector('[rel="loginWarning"]');
loginForm.loginWarning.show = (text) => {
    loginForm.loginWarning.innerText = text;
    loginForm.loginWarning.classList.add('visible');
};
loginForm.loginWarning.hide = () => loginForm.loginWarning.classList.remove('visible');
loginForm.show = () => {
    loginForm.emailWarning.hide();
    loginForm.passwordWarning.hide();
    loginForm.loginWarning.hide();
}
loginForm.hide = () => {
    loginForm.classList.remove('visible');
};

// btn.addEventListener('click', function () {
//     loginForm.emailWarning.show('이메일을 입력하세요.');
// });

loginForm.onsubmit = e => {
    e.preventDefault();
    loginForm.emailWarning.hide();
    loginForm.passwordWarning.hide();
    loginForm.loginWarning.hide();
    if (loginForm['email'].value === '') {
        loginForm['email'].focus();
        loginForm.emailWarning.show('이메일을 입력해 주세요.');
        return false;
    }
    if (loginForm['password'].value === '') {
        loginForm['password'].focus();
        loginForm.passwordWarning.show('비밀번호를 입력해 주세요.');
        return false;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', loginForm['email'].value);
    formData.append('password', loginForm['password'].value);
    xhr.open('POST', '/user/login');
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status >= 200 && xhr.status < 300) {
            const responseObject = JSON.parse(xhr.responseText);
            switch (responseObject.result) {
                case 'failure':
                    loginForm.loginWarning.show('이메일 혹은 비밀번호가 올바르지 않습니다.');
                    loginForm['email'].focus();
                    loginForm['email'].select();
                    break;
                case 'success':

                    location.href = "/";
                    break;
                default:
                    loginForm.loginWarning.show('서버가 알수 없는 응답을 반환했습니다. 관리자에게 문의해 주새요.')
            }
        } else {
            loginForm.loginWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해주세요.');
        }
    };
    xhr.send(formData);
};

