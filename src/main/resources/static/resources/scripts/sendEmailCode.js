const recoverForm = document.getElementById('recoverForm');

recoverForm.warning = recoverForm.querySelector('[rel="warning"]');
recoverForm.warning.show = (text) => {
    recoverForm.warning.innerText = text;
    recoverForm.warning.classList.add('visible');
};
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
let timerInterval





recoverForm.onsubmit = e => {
    e.preventDefault();
    Toast.fire({
        icon: 'success',
        title: '이메일 존재 여부 확인중입니다 잠시만 기다려주세요.'
    })
    // if (recoverForm['option'].value === 'password') {
    //     recoverForm.warning.hide();
        if (recoverForm['pEmail'].value === '') {
            recoverForm.warning.show('이메일 주소를 입력해 주세요.');
            recoverForm['pEmail'].focus();
            return;
        }
        // if (!new RegExp('^(?=.{10,50}$)([\\da-zA-Z\\-_\\.]{5,25})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$').test(recoverForm['pEmail'].value)) {
        //     recoverForm.warning.show('올바른 이메일 주소를 입력해 주세요.');
        //     return;
        // }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('email', recoverForm['pEmail'].value);
        xhr.open('POST', '/user/recoverPassword');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject.result) {
                        case 'failure':
                            recoverForm.warning.show('존재하지 않는 이메일입니다. 다시 한번 확인해 주세요.');
                            recoverForm['pEmail'].focus()
                            break;
                        case 'success':
                            // alert('비밀번호를 재설정할 수 있는 링크를 포함한 이메일을 전송하였습니다. 해당 링크는 한 시간만 유효함으로 유의해주시기 바랍니다.');
                            Swal.fire(
                                '전송이 완료되었습니다.',
                                '비밀번호를 재설정할 수 있는 링크를 포함한 이메일을 전송하였습니다. 해당 링크는 한 시간만 유효함으로 유의해주시기 바랍니다.',
                                'success'

                            )
                            recoverForm.hide();
                            coverElement.hide();
                            break;
                        default:
                            recoverForm.warning.show('서버가 알 수 없는 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.');
                    }
                } else {
                    recoverForm.warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
};

recoverForm.querySelectorAll('[name="cancel"]').forEach(x => x.addEventListener('click', () => {
    recoverForm.hide();
}));