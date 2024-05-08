const writeForm = document.getElementById('writeForm');
const wrtieBtn = document.getElementById('writeBtn')
const noticeDeleteBtn = document.querySelectorAll('.noticeDeleteBtn')
const noticePatchBtn = document.querySelectorAll('.noticePatchBtn')
document.addEventListener("DOMContentLoaded", function () {
    const writeForm = document.getElementById('writeForm');

    //공지사항 추가
    writeForm.addEventListener("submit", function (e) {
        e.preventDefault();
        // alert('서브밋 클릭');
        if (isAdmin) {
            const xhr = new XMLHttpRequest();
            const formData = new FormData();

            formData.append('title', writeForm['noticeTitle'].value);
            formData.append('content', writeForm['noticeContent'].value);
            xhr.open('POST', '/noticeWrite');
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        location.href = `/notice`;
                        alert('공지사항이 등록되었습니다.');
                    } else {
                        alert('연결실패');
                    }
                }
            };
            xhr.send(formData);
        } else {
            alert('관리자만 작성할 수 있습니다.');
        }
    });
})


//공지사항 삭제
noticeDeleteBtn.forEach(noticeDeleteBtn => {
    noticeDeleteBtn.addEventListener('click', e => {
        e.preventDefault();

        const index = noticeDeleteBtn.dataset.index;

        if (confirm('정말로 삭제하시겠습니까?')) {
            const xhr = new XMLHttpRequest();
            xhr.open('DELETE', `/deleteNotice/?index=${index}`);
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const respopnseText = xhr.responseText;
                        if (respopnseText === 'true') {
                            location.href = `/notice`;
                            alert('공지사항이 삭제되었습니다.')
                        }
                    } else {

                    }
                }
            };
            xhr.send();
        }
    })
})

//공지사항 수정
noticePatchBtn.forEach(noticePatchBtn => {
    noticePatchBtn.addEventListener('click', e => {
        e.preventDefault();

        const index = noticePatchBtn.dataset.index;
        const changeContent = prompt('수정할 내용을 입력해주세요.', '수정할 내용'); // 수정된 부분

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('index', index);
        formData.append('text', changeContent);
        xhr.open('PATCH', `/notice/patch`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    alert('연결성공');
                    const responseText = xhr.responseText;
                    if (responseText == 'true') {
                        location.href = `/notice`;
                        alert('공지사항이 수정되었습니다. ');
                    } else {
                        alert('수정실패 ');
                    }
                }
            }
        };
        xhr.send(formData);
    });
});
