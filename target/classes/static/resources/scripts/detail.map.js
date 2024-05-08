// const container = document.getElementById('map');
//
// let options = {
//     center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
//     level: 10 //지도의 레벨(확대, 축소 정도)
// }
//
// let map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
//
// const mapX = document.getElementById('X-Value');
// const mapY = document.getElementById('Y-Value');
// const Y = document.getElementById('Y-Value');
// // console.log("mapx"+parseFloat(mapX.value()));
// // console.log(button.dataset.index)
//
// // console.log(Y.dataset.index)
// console.log(mapX.textContent)
// console.log(mapY.textContent)

// 마커찍기
// 지도를 클릭한 위치에 표출할 마커입니다
// var marker = new kakao.maps.Marker({
//     // 지도 중심좌표에 마커를 생성합니다
//     position: map.getCenter()
// });
// // 지도에 마커를 표시합니다
// marker.setMap(map);



document.addEventListener('DOMContentLoaded', function () {
    const container = document.getElementById('map');
    const mapX = document.getElementById('mapX').textContent;
    const mapY = document.getElementById('mapY').textContent;

    let options = {
        center: new kakao.maps.LatLng(mapY, mapX), // 지도의 중심 좌표
        level: 5 // 지도의 레벨(확대, 축소 정도)
    }

    let map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴

    // 마커 위치 지정
    const markerPosition = new kakao.maps.LatLng(mapY, mapX);

    // 마커 생성
    const marker = new kakao.maps.Marker({
        position: markerPosition
    });

    // 마커 지도에 추가
    marker.setMap(map);
});