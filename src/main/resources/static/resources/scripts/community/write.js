const writeForm = document.getElementById('writeForm');

ClassicEditor.create(writeForm['content'],{
    simpleUpload : {
        uploadUrl : '/community/uploadImage' //이 주소로 업로드를 함
    }
});

