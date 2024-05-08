const recoverEmail = document.getElementById('recoverEmail');
recoverEmail.warning = recoverEmail.querySelector('[rel="warning"]');
recoverEmail.warning.show = (text) => {
    recoverEmail.innerText = text;
    recoverEmail.warning.classList.add('visible');
}
recoverEmail.warning.hide = () => recoverEmail.warning.classList.remove('visible');
recoverEmail['eContactSend'].onclick = () => {
    recoverEmail.warning.hide();
    if (recoverEmail['eContact'].value === '') {
        recoverEmail.warning.show('연락처를 입력해 주세요.');
        recoverEmail['eContact'].focus();
        return;
    }
    if (!new RegExp('^(010)(\\d{8})$').test(recoverEmail['eContact'].value)) {
        recoverEmail.warning.show('올바른 연락처를 입력해 주세요.');
        recoverEmail['eContact'].focus();
        recoverEmail['eContact'].select();
        return;
    }
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/user/contactCodeRec?contact=${recoverEmail['eContact'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr. status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'success':
                        // recoverEmail.warning.show('입력하신 연락처로 인증번호를 전송하였습니다. 5분 이내로 입력해주세요.');
                        alert('입력하신 연락처로 인증번호를 전송했습니다. 5분이내로 입력해주세요,');
                        recoverEmail['eContact'].setAttribute('disabled','disabled');
                        recoverEmail['eContactCode'].removeAttribute('disabled');
                        recoverEmail['eContactSalt'].value = responseObject.salt;
                        break;
                    default:
                        recoverEmail.warning.show('서버가 알수없는 응답을 반환하였습니다. 다시 시도해 주세요.');
                }
            } else {
                recoverEmail.warning.show('서버와 통신하지 못하였습니다. 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
};

recoverEmail['eContactVerify'].onclick = () => {
    recoverEmail.warning.hide();
    if (recoverEmail['eContactCode'].value === '') {
        recoverEmail.warning.show('인증번호를 입력해주세요.');
        recoverEmail['eContactCode'].focus();
        return;
    }

    if (!new RegExp('^(\\d{6})$').test(recoverEmail['eContactCode'].value)) {
        recoverEmail.warning.show('올바른 인증번호를 입력해주세요.');
        recoverEmail['eContactCode'].focus();
        recoverEmail['eContactCode'].select();
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('contact', recoverEmail['eContact'].value);
    formData.append('code', recoverEmail['eContactCode'].value);
    formData.append('salt', recoverEmail['eContactSalt'].value);
    xhr.open('PATCH', '/user/contactCodeRec');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr. status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                console.log(responseObject.result);
                switch (responseObject.result) {
                    case 'failure_expired':
                        alert('해당 인증번호는 시간이 만료되었습니다. 인증번호를 다시 전송받아주세요');
                        recoverEmail['eContact'].removeAttribute('disabled');
                        recoverEmail['eContactCode'].setAttribute('disabled', 'disabled');
                        break;
                    case 'success':
                        alert(`이메일 주소는 '${responseObject.email}'입니다.`);
                        location.href += '';
                        break;
                    default:
                        alert('인증번호가 올바르지 않습니다. 다시 확인해 주세요.');
                        recoverEmail['eContactCode'].select();
                }
            } else {
                recoverEmail.warning.show('서버와 통신하지 못하였습니다. 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}