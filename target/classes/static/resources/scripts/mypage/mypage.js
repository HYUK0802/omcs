// const container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
// let options = { //지도를 생성할 때 필요한 기본 옵션
//     center: new kakao.maps.LatLng(36.1398393,128.1135947), //지도의 중심좌표.
//     level: 13 //지도의 레벨(확대, 축소 정도)
// };
//
// let map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
//
// // 마커를 표시할 위치와 내용을 가지고 있는 객체 배열입니다
// let positions = [
//     {
//         content: '<div>카카오</div>',
//         latlng: new kakao.maps.LatLng(36.1398393, 128.1135947)
//     },
//     {
//         content: '<div>생태연못</div>',
//         latlng: new kakao.maps.LatLng( 37.566535, 126.9779692)
//     },
//     {
//         content: '<div>텃밭</div>',
//         latlng: new kakao.maps.LatLng(36.3504119, 127.3845475)
//     },
//     {
//         content: '<div>근린공원</div>',
//         latlng: new kakao.maps.LatLng(34.7603737,  127.6622221)
//     }
// ];

// for (let i = 0; i < positions.length; i ++) {
//     // 마커를 생성합니다
//     let marker = new kakao.maps.Marker({
//         map: map, // 마커를 표시할 지도
//         position: positions[i].latlng // 마커의 위치
//     });
//
//     // 마커에 표시할 인포윈도우를 생성합니다
//     let infowindow = new kakao.maps.InfoWindow({
//         content: positions[i].content // 인포윈도우에 표시할 내용
//     });
//
//     // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
//     // 이벤트 리스너로는 클로저를 만들어 등록합니다
//     // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
//     kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
//     kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
// }

// 인포윈도우를 표시하는 클로저를 만드는 함수입니다
// function makeOverListener(map, marker, infowindow) {
//     return function() {
//         infowindow.open(map, marker);
//     };
// }
//
// // 인포윈도우를 닫는 클로저를 만드는 함수입니다
// function makeOutListener(infowindow) {
//     return function() {
//         infowindow.close();
//     };
// }

/* 아래와 같이도 할 수 있습니다 */
/*
for (var i = 0; i < positions.length; i ++) {
    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        course: course, // 마커를 표시할 지도
        position: positions[i].latlng // 마커의 위치
    });

    // 마커에 표시할 인포윈도우를 생성합니다
    var infowindow = new kakao.maps.InfoWindow({
        content: positions[i].content // 인포윈도우에 표시할 내용
    });

    // 마커에 이벤트를 등록하는 함수 만들고 즉시 호출하여 클로저를 만듭니다
    // 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
    (function(marker, infowindow) {
        // 마커에 mouseover 이벤트를 등록하고 마우스 오버 시 인포윈도우를 표시합니다
        kakao.maps.event.addListener(marker, 'mouseover', function() {
            infowindow.open(course, marker);
        });

        // 마커에 mouseout 이벤트를 등록하고 마우스 아웃 시 인포윈도우를 닫습니다
        kakao.maps.event.addListener(marker, 'mouseout', function() {
            infowindow.close();
        });
    })(marker, infowindow);
}
*/

const planForm = document.getElementById('planCardForm')
const modalForm = document.getElementById('modalForm')
const deleteButton = document.querySelectorAll('[rel="delete"]')
const deletePickButton = document.querySelectorAll('[rel="deletePick"]')

deleteButton.forEach(deleteButton => {
    deleteButton.addEventListener('click', e => {
        e.preventDefault();

        const index = deleteButton.parentElement.parentElement.querySelector('input[name="index"]').value;

        // 확인 대화 상자 표시
        Swal.fire({
            title: '일정을 삭제하시겠습니까?',
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
                xhr.open('DELETE', `/mypage?index=${index}`);
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status >= 200 && xhr.status < 300) {
                            const responseText = xhr.responseText; // 'true' | 'false'
                            if (responseText === 'true') {
                                Swal.fire(
                                    '삭제되었습니다!'
                                ).then(() => {
                                    location.href = '/mypage'; // Redirect after successful deletion
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
deletePickButton.forEach(deletePickButton => {
    deletePickButton.addEventListener('click', e => {
        e.preventDefault();

        const pickIndex = deletePickButton.parentElement.parentElement.querySelector('input[name="pickIndex"]').value;
        alert(pickIndex);

        // 확인 대화 상자 표시
        Swal.fire({
            title: '삭제하시겠습니까?',
            text: "",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d6bbff',
            cancelButtonColor: '#d33',
            confirmButtonText: '네, 삭제할게요!',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                const xhr = new XMLHttpRequest();
                xhr.open('DELETE', `/deleteMyPick?index=${pickIndex}`);
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status >= 200 && xhr.status < 300) {
                            const responseText = xhr.responseText; // 'true' | 'false'
                            if (responseText === 'true') {
                                Swal.fire(
                                    '삭제되었습니다!'
                                ).then(() => {
                                    location.href = '/mypage'; // Redirect after successful deletion
                                });
                            } else {
                                Swal.fire(
                                    'Error',
                                    '알 수 없는 이유로 삭제하지 못하였습니다.이미 삭제된 글일 수도 있습니다.',
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
document.addEventListener('DOMContentLoaded', function () {
    const myWishlistLink = document.getElementById('myWishlistLink');
    const modal = document.getElementById('myModal');
    const closeBtn = document.getElementsByClassName('close')[0];

    myWishlistLink.addEventListener('click', function (event) {
        event.preventDefault();
        modal.style.display = 'block';
    });

    closeBtn.addEventListener('click', function () {
        modal.style.display = 'none';
    });

    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});

