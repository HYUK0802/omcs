const editWriteForm = document.getElementById('editWriteForm');



ClassicEditor.create(editWriteForm['content'],{
    simpleUpload : {
        uploadUrl : '/community/uploadImage' //이 주소로 업로드를 함
    }
});

editWriteForm.onsubmit = e => {
    e.preventDefault();
    alert("일단 받음")

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    const index = editWriteForm['index'].value;

    formData.append('index', index);
    formData.append('title', editWriteForm['title'].value);
    formData.append('content', editWriteForm['content'].value);

    xhr.open('PATCH', '/community/editWrite');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'failure':
                        alert('알 수 없는 이유로 수정하지 못함')
                        break;
                    case 'success':
                        alert('수정이 완료 되었습니다.')
                        location.href = "/community";
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
