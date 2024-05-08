// const container = document.getElementById('map');
//
// let options = {
//     center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
//     level: 10 //지도의 레벨(확대, 축소 정도)
// }
//
// let map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
//
// 마커찍기
// 지도를 클릭한 위치에 표출할 마커입니다
// var marker = new kakao.maps.Marker({
//     // 지도 중심좌표에 마커를 생성합니다
//     position: map.getCenter()
// });
// 지도에 마커를 표시합니다
// marker.setMap(map);

// 지도에 클릭 이벤트를 등록합니다
// 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
//
//
// kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
//
//     // 클릭한 위도, 경도 정보를 가져옵니다
//     let latlng = mouseEvent.latLng;
//
//     let x = latlng.getLat();
//     let y = latlng.getLng();
//     // 마커 위치를 클릭한 위치로 옮깁니다
//     marker.setPosition(latlng);
//
//     let message = '클릭한 위치의 위도는 ' + x + ' 이고, ';
//     message += '경도는 ' + y + ' 입니다';
//
//     let resultDiv = document.getElementById('clickLatlng');
//     resultDiv.innerHTML = message;
//
//     console.log('사용자 선택 장소 x : '+ x);
//     console.log('사용자 선택 장소 y : '+ y);
//
//     const xhr = new XMLHttpRequest();
//     const formData = new FormData();
//     formData.append('x', x);
//     formData.append('y', y);
//     xhr.open('POST', `/`);
//     xhr.onreadystatechange = () => {
//         if (xhr.readyState === XMLHttpRequest.DONE) {
//             if (xhr.status >= 200 && xhr.status < 300) {
//                 alert('성공');
//             } else {
//                 alert('통신 실패');
//             }
//         }
//     }
//     xhr.send(formData);
// })
//
//
//
//
//
//
//     xhr.onreadystatechange = () => {
//         if(xhr.readyState === XMLHttpRequest.DONE){
//             if(xhr.status >= 200 && xhr.status < 300){
//                 alert('성공')
//             }else{
//                     alert('통신실패')
//             }
//
//         }
//     };
//     xhr.send(formData);
// });
