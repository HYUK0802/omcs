
const containerForm = document.getElementById('containerForm');
// containerForm.nicknameWarning = containerForm.querySelector('[rel="nicknameWarning"]');
// containerForm.recoverWarning = containerForm.querySelector('[rel="recoverWarning"]');
// containerForm.nicknameWarning.show = (text) => {
//     containerForm.nicknameWarning.innerText = text;
//     containerForm.nicknameWarning.classList.add('visible');
// };
// containerForm.nicknameWarning.hide = () => containerForm.nicknameWarning.classList.remove('visible');




containerForm.onsubmit = e => {
    e.preventDefault();
    if (containerForm['nickname'].value === '') {
        alert('재설정할 닉네임을 입력해주세요.');
        containerForm['nickname'].focus();
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('nickname', containerForm['nickname'].value);
    xhr.open('PATCH', '/myPage/editPersonal/modifyNickname');
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status >= 200 && xhr.status < 300) {
            const responseObject = JSON.parse(xhr.responseText);
            switch (responseObject.result) {
                case 'failure':
                    alert('재설정할 닉네임을 입력해 주세요.');
                    containerForm['nickname'].focus();
                    containerForm['nickname'].select();
                    break;
                case 'success':
                    alert('닉네임이 변경되었습니다.')
                    location.href = "/mypage";
                    break;
                default:
                    alert(" 다시하세요")
            }
        } else {
            alert("서버와 통신하지 못했습니다. 다시 시도해주세요")
        }
    };
    xhr.send(formData);
};


