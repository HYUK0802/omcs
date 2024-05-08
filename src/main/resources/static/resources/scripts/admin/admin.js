const userDeleteBtn = document.querySelectorAll('.userDeleteBtn')
const userPatchBtn = document.querySelectorAll('.userPatchBtn')

userDeleteBtn.forEach(userDeleteBtn => {
    userDeleteBtn.addEventListener('click', e => {
        e.preventDefault();
        const email = userDeleteBtn.dataset.email;

        if (confirm('정말로 삭제하시겠습니까?')) {
            const xhr = new XMLHttpRequest();
            xhr.open('DELETE', `/admin/delete/?email=${email}`);
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {

                        const responseText = xhr.responseText;
                        if (responseText === 'true') {

                            location.href = `/admin`;
                            alert('해당 계정이 삭제되었습니다.')
                        }
                    } else {

                    }
                }
            };
            xhr.send();
        }
    })
})


userPatchBtn.forEach(userPatchBtn => {
    userPatchBtn.addEventListener('click', e => {
        e.preventDefault();

        const contact = userPatchBtn.dataset.contact;
        const changeContact = prompt('수정할 전화번호를 입력해 주세요', '수정할 번호')

        if (changeContact !== null) { // 취소를 누른 경우에는 POST 요청을 보내지 않음

            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            formData.append('contact', contact)
            formData.append('changeContact', changeContact);
            xhr.open('PATCH', `/admin/patch`);
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseText = xhr.responseText;
                        if (responseText == 'true') {
                            location.href = `/admin`;
                            alert('해당 연락처가 수정되었습니다.')
                        }
                    } else {
                        alert('수정실패')
                    }
                }
            };
            xhr.send(formData);
        }
    })
})
