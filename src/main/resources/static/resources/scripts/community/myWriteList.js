const containerForm = document.getElementById('containerForm');
const deleteButtons = containerForm.querySelectorAll('[rel="delete"]');

deleteButtons.forEach(deleteButton => {
    deleteButton.addEventListener('click', e => {
        e.preventDefault();

        const index = deleteButton.parentElement.parentElement.querySelector('input[name="index"]').value;

        // 확인 대화 상자 표시
        Swal.fire({
            title: '게시글을 삭제하시겠습니까?',
            text: "삭제하면 되돌릴수 없습니다.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d6bbff',
            cancelButtonColor: '#d33',
            confirmButtonText: '네, 삭제할게요!',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                const xhr = new XMLHttpRequest();
                xhr.open('DELETE', `/community/myWriteList?index=${index}`);
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status >= 200 && xhr.status < 300) {
                            const responseText = xhr.responseText; // 'true' | 'false'
                            if (responseText === 'true') {
                                Swal.fire(
                                    '삭제되었습니다!'
                                ).then(() => {
                                    location.href = '/community/myWriteList'; // Redirect after successful deletion
                                });
                            } else {
                                Swal.fire(
                                    'Error',
                                    '알 수 없는 이유로 삭제하지 못하였습니다.\\n\\n이미 삭제된 글일 수도 있습니다.',
                                    'error'
                                );
                            }
                        } else {
                            Swal.fire(
                                'Error',
                                '서버와 통신하지 못하였습니다.\\n\\n잠시 후 다시 시도해 주세요.',
                                'error'
                            );
                        }
                    }
                };
                xhr.send();
            } else {
                // 취소 버튼 클릭 시 아무 작업도 수행하지 않음
            }
        });
    });
});
