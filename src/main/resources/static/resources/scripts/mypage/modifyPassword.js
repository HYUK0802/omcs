const pwRecoverForm = document.getElementById('pwRecoverForm');

// user 데이터에 있는 비밀번호 와 비교?

pwRecoverForm.mdRcPasswordWarning = pwRecoverForm.querySelector('[rel="mdRcPasswordWarning"]');
pwRecoverForm.passwordWarning = pwRecoverForm.querySelector('[rel="passwordWarning"]');
pwRecoverForm.passwordWarning.show = (text) => {
    pwRecoverForm.passwordWarning.innerText = text;
    pwRecoverForm.passwordWarning.classList.add('visible');
};
pwRecoverForm.mdRcPasswordWarning.show = (text) => {
    pwRecoverForm.mdRcPasswordWarning.innerText = text;
    pwRecoverForm.mdRcPasswordWarning.classList.add('visible');
};
pwRecoverForm.passwordWarning.hide = () => { // hide 함수 추가
    pwRecoverForm.passwordWarning.classList.remove('visible');
};
pwRecoverForm.mdRcPasswordWarning.hide = () => { // hide 함수 추가
    pwRecoverForm.mdRcPasswordWarning.classList.remove('visible');
};
pwRecoverForm.show = () => {
    pwRecoverForm.passwordWarning.hide();
    pwRecoverForm.mdRcPasswordWarning.hide();
}
pwRecoverForm.onsubmit = e => {
    e.preventDefault();

    const password = pwRecoverForm['password'].value;
    const passwordCheck = pwRecoverForm['passwordCheck'].value;
    // const nwPassword = pwRecoverForm['nwPassword'].value;


    // pwRecoverForm.nwPasswordWarning.hide();
    pwRecoverForm.mdRcPasswordWarning.hide();
    if (pwRecoverForm['password'].value === '') {
        pwRecoverForm['password'].focus();
        pwRecoverForm.passwordWarning.show('재설정할 비밀번호를 입력해 주세요.');
        return false;
    }
    if (pwRecoverForm['passwordCheck'].value === '') {
        pwRecoverForm['passwordCheck'].focus();
        pwRecoverForm.mdRcPasswordWarning.show('비밀번호를 입력해 주세요.');
        return false;
    }

    if (password !== passwordCheck) {
        pwRecoverForm['password'].focus();

        pwRecoverForm.mdRcPasswordWarning.show('비밀번호가 일치하지 않습니다.');
        return;
    }

    const formData = new FormData();
    formData.append('password', password);

    const xhr = new XMLHttpRequest();
    xhr.open('PATCH', '/myPage/editPersonal/modifyPassword');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'failure':
                        alert('다시 로그인 후 시도해주세요.');
                        break;
                    case 'success':
                        alert('비밀번호를 재설정 하였습니다.');
                        location.href = "/mypage";
                        break;
                    case 'failure_password_mismatch':
                        pwRecoverForm.passwordWarning.show('현재 사용중인 비밀번호와 일치합니다.');
                        pwRecoverForm['password'].focus();
                        pwRecoverForm['password'].select();
                        break;
                    default:
                        alert('태초로 돌아가세요');
                }
            } else {
                alert('서버와 통신하지 못했습니다.');
            }
        }
    };
    xhr.send(formData);
};
