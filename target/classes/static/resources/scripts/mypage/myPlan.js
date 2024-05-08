const container = document.getElementById('map');


const areaCodes = {
    1: {center: new kakao.maps.LatLng(37.5665, 126.9780), level: 9}, // 서울
    2: {center: new kakao.maps.LatLng(37.5432, 126.4596), level: 11}, // 인천
    3: {center: new kakao.maps.LatLng(36.3223, 127.4029), level: 9}, // 대전
    6: {center: new kakao.maps.LatLng(35.1798, 129.0750), level: 10}, // 부산
    4: {center: new kakao.maps.LatLng(35.7852, 128.5697), level: 10}, // 대구
    7: {center: new kakao.maps.LatLng(35.4742, 129.2417), level: 10}, // 울산
    5: {center: new kakao.maps.LatLng(35.181832, 126.879582), level: 10}, // 광주
    39: {center: new kakao.maps.LatLng(33.3617, 126.5292), level: 10}, // 제주
    31: {center: new kakao.maps.LatLng(37.5582, 127.3514), level: 10}, // 경기
    32: {center: new kakao.maps.LatLng(37.4651, 128.322), level: 11}, // 강원
    33: {center: new kakao.maps.LatLng(36.6708, 127.9622), level: 12}, // 충북
    34: {center: new kakao.maps.LatLng(36.3521, 126.8086), level: 12}, // 충남
    35: {center: new kakao.maps.LatLng(36.2042, 128.9045), level: 12}, // 경북
    36: {center: new kakao.maps.LatLng(35.2598, 128.6642), level: 12}, // 경남
    37: {center: new kakao.maps.LatLng(35.6704, 127.2733), level: 12}, // 전북
    38: {center: new kakao.maps.LatLng(34.8679, 126.9910), level: 12}, // 전남
};

let areaCode = document.getElementById('areaCode').textContent;

let options = areaCodes[parseInt(areaCode)];
if (options) {
    map = new kakao.maps.Map(container, options);
} else {
    console.log('안되지롱');
}



document.addEventListener('DOMContentLoaded', function () {
    const mapCheckBtns = document.querySelectorAll('.map-check-btn'); // 여러 개의 버튼을 선택

    let currentMarkersAndLabels = []; // 현재 생성된 마커와 레이블 쌍을 저장할 배열
    let currentPolyline = null; // 현재 생성된 선 객체

    mapCheckBtns.forEach(function (mapCheckBtn) {
        mapCheckBtn.addEventListener('click', function () {
            // 기존 마커, 레이블, 선 제거
            currentMarkersAndLabels.forEach(item => {
                item.marker.setMap(null);
                item.label.setMap(null);
            });
            if (currentPolyline !== null) {
                currentPolyline.setMap(null);
            }

            let scheduleItem = mapCheckBtn.closest('.group'); // 클릭된 버튼의 부모

            if (scheduleItem) {
                let mapXElements = scheduleItem.querySelectorAll('.detailMapX');
                let mapYElements = scheduleItem.querySelectorAll('.detailMapY');
                let markersAndLabels = []; // 새로운 마커와 레이블 쌍을 저장할 배열

                mapXElements.forEach(function (mapXElement, index) {
                    let mapX = parseFloat(mapXElement.textContent); // 위도 값을 가져옴
                    let mapY = parseFloat(mapYElements[index].textContent); // 경도 값을 가져옴

                    console.log('schedule-item', index, '의 위도:', mapX);
                    console.log('schedule-item', index, '의 경도:', mapY);

                    // 위도, 경도 값을 이용하여 마커 생성
                    let markerPosition = new kakao.maps.LatLng(mapX, mapY);
                    let marker = new kakao.maps.Marker({
                        position: markerPosition,
                        map: map // 생성한 마커를 지도에 표시
                    });

                    // 레이블(숫자) 생성
                    let label = new kakao.maps.CustomOverlay({
                        content: String(index + 1),
                        position: markerPosition,
                        xAnchor: 0.5,
                        yAnchor: 1.5
                    });

                    markersAndLabels.push({ marker, label }); // 새로운 마커와 레이블 쌍을 배열에 추가
                });

                // 마커들을 선으로 연결하는 선 객체 생성
                let polyline = new kakao.maps.Polyline({
                    path: markersAndLabels.map(item => item.marker.getPosition()),
                    strokeWeight: 2, // 선의 두께
                    strokeColor: '#FF0000', // 선의 색상
                    strokeOpacity: 0.7, // 선의 투명도
                    strokeStyle: 'solid' // 선의 스타일
                });

                currentMarkersAndLabels = markersAndLabels; // 현재 생성된 마커와 레이블 쌍을 갱신
                currentPolyline = polyline; // 현재 생성된 선 객체 갱신

                polyline.setMap(map); // 선을 지도에 표시
                markersAndLabels.forEach(item => item.label.setMap(map)); // 레이블(숫자)들을 지도에 표시
            }
        });
    });
});






// textarea의 blur 이벤트 핸들러 등록
// 모든 textarea에 대해 이벤트 리스너 등록
const textareaValues = document.querySelectorAll('.textareaValue');
textareaValues.forEach(textarea => {
    textarea.addEventListener('input', function () {
        sendPatchRequest(textarea);
    });
});


// 모든 버튼에 대해 이벤트 리스너 등록
const textareaBtns = document.querySelectorAll('.textareaBtn');
textareaBtns.forEach(btn => {
    btn.addEventListener('click', function () {
        const textareaBox = btn.parentElement;
        const textarea = textareaBox.querySelector('.textareaValue');
        sendPatchRequest(textarea);
    });
});

function sendPatchRequest(textarea) {
    const detailIndex = textarea.parentElement.querySelector('.detailIndex').textContent;
    const patchMemo = textarea.value;

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('index', detailIndex);
    formData.append('patchMemo', patchMemo);

    xhr.open('PATCH', `/memo`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                // alert('업데이트 성공');
            } else {
                alert('업데이트 실패');
            }
        }
    };
    xhr.send(formData);

    console.log('수정할 인덱스: ' + detailIndex);
    console.log('메모 내용: ' + patchMemo);
}
