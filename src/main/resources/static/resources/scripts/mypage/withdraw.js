const withDraw = document.getElementById('withDraw');

withDraw['sendContactCode'].onclick = () => {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/mypage/editPersonal/withDraw?contact=${withDraw['contact'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'failure':
                        alert('인증번호 전송에 실패했습니다.\n\n다시 시도해 주세요.');
                        break;
                    case 'success':
                        alert('등록된 전화번호로 인증번호를 전송하였습니다.\n\n인증번호를 확인해 주세요.');
                        withDraw['eContactSalt'].value = responseObject.salt;
                        break;
                    default:
                        alert('알 수 없는 이유로 오류가 발생했습니다.\n\n관리자에게 문의해 주세요.');
                }
            } else {
                alert('등록된 전화번호로 인증번호를 전송하였습니다.\n\n인증번호를 확인해 주세요.');
            }
        }
    };
    xhr.send();
};

withDraw['verifyContactCode'].addEventListener('click', function() {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('contact', withDraw['contact'].value); // 폼 데이터 필드와 값을 추가해야 합니다.
    formData.append('code', withDraw['verifyCode'].value);
    formData.append('salt', withDraw['eContactSalt'].value);
    xhr.open('PATCH', '/mypage/editPersonal/withDraw');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'failure':
                        alert('전화번호 인증에 실패했습니다.\n\n다시 시도해 주세요.');
                        break;
                    case 'success':
                        alert('전화번호 인증이 완료되었습니다.\n\n탈퇴하기를 눌러 주세요.');
                        break;
                    default:
                        alert('알 수 없는 이유로 오류가 발생했습니다.\n\n관리자에게 문의해 주세요.');
                }
            } else {
                alert('전화번호 인증이 완료되었습니다.\n\n탈퇴하기 버튼을 눌러 주세요.');
            }
        }
    };
    xhr.send(formData);
});

withDraw['deleteButton'].addEventListener('click', function() {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('contact', withDraw['contact'].value); // 폼 데이터 필드와 값을 추가해야 합니다.
    formData.append('code', withDraw['verifyCode'].value);
    formData.append('salt', withDraw['eContactSalt'].value);
    xhr.open('DELETE', '/mypage/editPersonal/withDraw');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'failure':
                        alert('회원 탈퇴에 실패했습니다.\n\n관리자에게 문의해 주세요.');
                        break;
                    case 'success':
                        alert('회원 탈퇴를 완료했습니다.');
                        window.location.href = '/'; // 홈 페이지로 리다이렉트
                        break;
                    default:
                        alert('알 수 없는 이유로 오류가 발생했습니다.\n\n관리자에게 문의해 주세요.');
                }
            } else {
                alert('회원 탈퇴에 성공했습니다.\n\n그 동안 이용해 주셔서 감사합니다.');
            }
        }
    };
    xhr.send(formData);
});

