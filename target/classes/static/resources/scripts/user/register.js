const registerForm = document.getElementById('registerForm');
const step1 = document.getElementById('step1')
const step2 = document.getElementById('step2')
const finishBtn = document.getElementById('register-finish-btn')

const Toast = Swal.mixin({
    toast: true,
    position: 'center-center',
    showConfirmButton: false,
    timer: 3500,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
})



registerForm.onsubmit = e => {
    e.preventDefault();

    Toast.fire({
        icon: 'success',
        title: '잠시만 기다려주세요.'
    })


    if (registerForm['email'].value === '') {
        alert('이메일을 입력해주세요.')
        registerForm['email'].focus();
        return;
    }
    if (!new RegExp('^(?=.{10,50}$)([\\da-zA-Z\\-_]{5,25})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$').test(registerForm['email'].value)) {
        alert('올바른 이메일을 입력해 주세요.');
        registerForm['email'].focus();
        registerForm['email'].select();
        return;
    }
    if (registerForm['password'].value === '') {
        alert('비밀번호를 입력해 주세요.');
        registerForm['password'].focus();
        return;
    }
    if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]};:\'",<.>/?]{8,50})$').test(registerForm['password'].value)) {
        alert('올바른 비밀번호를 입력해 주세요.');
        registerForm['password'].focus();
        registerForm['password'].select();
        return;
    }
    if (registerForm['passwordCheck'].value === '') {
        alert('비밀번호를 다시 한번 더 입력해 주세요.');
        registerForm['passwordCheck'].focus();
        return;
    }
    if (registerForm['password'].value !== registerForm['passwordCheck'].value) {
        alert('비밀번호가 서로 일치하지 않습니다. 다시 한번 더 확인해 주세요.');
        registerForm['passwordCheck'].focus();
        registerForm['passwordCheck'].select();
        return;
    }
    if (registerForm['nickname'].value === '') {
        alert('별명을 입력해 주세요.');
        registerForm['nickname'].focus();
        return;
    }
    if (!new RegExp('^([가-힣]{2,10})$').test(registerForm['nickname'].value)) {
        alert('올바른 별명을 입력해 주세요.');
        registerForm['nickname'].focus();
        registerForm['nickname'].select();
        return;
    }
    if (registerForm['contactSalt'].value === '') {
        alert('연락처 인증을 완료해 주세요.');
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', registerForm['email'].value);
    formData.append('password', registerForm['password'].value);
    formData.append('nickname', registerForm['nickname'].value)
    formData.append('contact', registerForm['contact'].value)
    formData.append('code', registerForm['contactCode'].value);
    formData.append('salt', registerForm['contactSalt'].value);

    xhr.open('POST', '/user/register');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'failure':
                        alert('알 수 없는 이유로 가입하지 못함')
                        break;
                    case 'failure_duplicate_email':
                        alert('해당 이메일은 이미 사용중입니다.')
                        break;
                    case 'failure_duplicate_nickname':
                        alert('해당 닉네임은 이미 사용중입니다.')
                        break;
                    case 'failure_duplicate_contact':
                        alert('해당 연락처는 이미 사용중입니다.')
                        break;
                    case 'success':
                        // alert('가입성공')

                        Swal.fire(
                            '전송이 완료되었습니다.',
                            '입력하신 이메일로 계정 인증을 위한 링크가 담긴 메일을 전송하였습니다. 이메일 인증을 완료한 후 로그인 및 서비스 이용이 가능합니다.' +
                            '해당 링크는 한 시간만 유효함으로 유의해주시기 바랍니다.',
                            'success'

                        )

                        // location.href = "/user/login";
                        step2.classList.remove('visible');
                        step1.classList.add('visible')
                        break;
                    default:
                        alert('서버가 알 수 없는 응답을 반환함 ')
                }
            } else {
                alert('서버와 통신하지 못함')
            }
        }
    };
    xhr.send(formData);
}

registerForm['contactSend'].addEventListener('click', () => {
    if (!new RegExp('^(010)(\\d{8})$').test(registerForm['contact'].value)) {
        alert('올바른 연락처를 입력해 주세요.');
        registerForm['contact'].focus();
        registerForm['contact'].select();
        return;
    }
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/user/contactCode?contact=${registerForm['contact'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObjet = JSON.parse(xhr.responseText);
                switch (responseObjet.result) {
                    case 'failure_duplicate':
                        alert('해당연락처는 이미 사용중')
                        registerForm['contact'].focus();
                        registerForm['contact'].select();
                        break;
                    case 'success':
                        alert('입력하신 연락처로 인증번호를 전송하였습니다. 5분 이내로 입력해 주세요.');
                        console.log(responseObjet['salt']);
                        registerForm['contactSalt'].value = responseObjet['salt']; // 솔트주입!!!!!
                        break;
                    default:
                        alert('서버가 알 수 없는 응답을 반환했습니다. 잠시후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
})
registerForm['contactVerify'].addEventListener('click', () => {
    if (registerForm['contactCode'].value === '') {
        alert('인증번호를 입력해 주세요.');
        registerForm['contactCode'].focus();
        return;
    }
    if (!new RegExp('^(\\d{6})$').test(registerForm['contactCode'].value)) {
        alert('올바른 인증번호를 입력해 주세요.');
        registerForm['contactCode'].focus();
        registerForm['contactCode'].select();
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('contact', registerForm['contact'].value);
    formData.append('salt', registerForm['contactSalt'].value);
    formData.append('code', registerForm['contactCode'].value);
    xhr.open('PATCH', '/user/contactCode');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'failure_expired':
                        alert('해당 인증번호는 만료되었습니다. 처음부터 다시 진행해 주세요.');
                        break;
                    case 'success':
                        registerForm['contactCode'].setAttribute('disabled', 'disabled');
                        registerForm['contactVerify'].setAttribute('disabled', 'disabled');
                        alert('인증이 완료되었습니다.');
                        break;
                    default:
                        registerForm['contactCode'].focus();
                        registerForm['contactCode'].select();
                        alert('인증번호가 올바르지않습니다. 다시 확인해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
});

['passwordCheck'].forEach(name => {
    registerForm[name].addEventListener('focusout', () => {
        if (registerForm['password'].value !== registerForm['passwordCheck'].value) {
            alert('비밀번호가 서로 일치하지 않습니다.');
            return;
        }
    });
});
//이메일, 비밀번호 중복체크 포커스아웃하면 나오게하기

finishBtn.addEventListener('click', () => {
    location.href = "/user/login";
})
