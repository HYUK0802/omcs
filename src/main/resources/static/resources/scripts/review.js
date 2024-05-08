const reviewDeleteButtons = detailForm.querySelectorAll('[rel="reviewDelete"]');
const reviewModifyButtons = detailForm.querySelectorAll('[rel="reviewModify"]');
const reviewModifyCover = document.getElementById('modifyCover');
const reviewModifyForm = document.getElementById('modifyForm');

reviewDeleteButtons.forEach(reviewDeleteBtn => {
    reviewDeleteBtn.addEventListener('click', e => {
        e.preventDefault();

        const index = reviewDeleteBtn.dataset.index;

        // 삭제 여부 확인 다이얼로그 표시
        if (confirm('정말로 삭제하시겠습니까?')) {
            const xhr = new XMLHttpRequest();
            xhr.open('DELETE', `./?index=${index}`);
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseText = xhr.responseText;
                        if (responseText === 'true') {
                            location.href = `/detail?contentId=${detailForm['placeIndex'].value}`;
                            alert('리뷰가 삭제되었습니다.');
                        } else {
                            alert('알 수 없는 이유로 삭제하지 못하였습니다.\n\n관리자에게 문의해 주세요.');
                        }
                    } else {
                        alert('서버와 통신하지 못하였습니다.\n\n관리자에게 문의해 주세요.');
                    }
                }
            };
            xhr.send();
        }
    });
});

reviewModifyCover.addEventListener('click', () => {
    reviewModifyCover.classList.remove('visible');
    reviewModifyForm.classList.remove('visible');
});
reviewModifyForm.querySelector('[rel="close"]').addEventListener('click', () => {
    reviewModifyCover.classList.remove('visible');
    reviewModifyForm.classList.remove('visible');
});

reviewModifyForm.onsubmit = e => {
    e.preventDefault();
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('index', reviewModifyForm['index'].value);
    formData.append('content', reviewModifyForm.querySelector('input[name="content"]').value);
    xhr.open('PATCH', './');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) { //서버오류가 나지않으면
                const alertText = xhr.responseText; //서버에 전달받은 데이터를 문자열로 바꾼다
                if (alertText == 'true') { //문자열이 참이면 출력
                    location.href += '';
                    alert('리뷰가 수정되었습니다.');
                } else { //문자열이 거짓이면 출력
                    alert('리뷰 수정에 실패했습니다.\n\n관리자에게 문의해 주세요.');
                }
            }
        }
    }
    xhr.send(formData);
};
reviewModifyButtons.forEach(modifyButton => {
    modifyButton.addEventListener('click', e => {
        e.preventDefault(); // 아무일도 안나게하는거

        reviewModifyCover.classList.add('visible');
        reviewModifyForm.querySelector('input[name="index"]').value = modifyButton.dataset.index;
        reviewModifyForm.querySelector('input[name="content"]').value = modifyButton.dataset.text;
        reviewModifyForm.querySelector('input[name="content"]').focus();
        reviewModifyForm.querySelector('input[name="content"]').select();
        reviewModifyForm.classList.add('visible');
    });
});

pickButton.onclick = function (e) {
    e.preventDefault()
    const placePic = pickForm['place-picture'].value;
    const placeTitle = pickForm['place-Title'].value;
    const contentId = pickForm['contentId'].value;
    const areaCode = pickForm['areaCode'].value;
    const address = pickForm['addr1'].value;
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('placePic', placePic);
    formData.append('contentId', contentId);
    formData.append('placeTitle', placeTitle);
    formData.append('areaCode', areaCode);
    formData.append('address', address);
    xhr.open('POST', '/myPick')
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'not_login':
                        alert('로그인 후 이용해주세요.');
                        location.href += '';
                        break;
                    case 'login':
                        e.preventDefault();
                        alert('이 여행지를 내 찜목록에 추가했습니다.');
                        break;
                    default:
                        loginForm.loginWarning.show('서버가 알수 없는 응답을 반환했습니다. 관리자에게 문의해 주세요.');
                }
            } else {
                alert('이미 찜한 여행지 입니다.');
            }
        }
    };
    xhr.send(formData);
}
