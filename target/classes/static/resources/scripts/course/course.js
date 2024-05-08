const plannerForm = document.getElementById('plannerForm');
const plannerClose = document.getElementById('plannerClose');

let markers = [];
let selectedMarkers = [];
let polylines = [];
let distanceOverlays = [];
let distanceOverlay = null;
let addedListItems = [];

const container = document.getElementById('map');

let options = {
    center: new kakao.maps.LatLng(36.45828, 127.85527),
    level: 13
}


let map = new kakao.maps.Map(container, options);

plannerForm.show = () => {
    plannerForm.classList.add('visible');
}

plannerForm.hide = () => {
    plannerForm.classList.remove('visible');
}

const recommendList = document.querySelector('#recommend .map-DB-list');
const areaCodes = {
    1: {center: new kakao.maps.LatLng(37.5665, 126.9780), level: 9}, // 서울
    2: {center: new kakao.maps.LatLng(37.5432, 126.4596), level: 9}, // 인천
    3: {center: new kakao.maps.LatLng(36.3223, 127.4029), level: 7}, // 대전
    6: {center: new kakao.maps.LatLng(35.1798, 129.0750), level: 9}, // 부산
    4: {center: new kakao.maps.LatLng(35.7852, 128.5697), level: 8}, // 대구
    7: {center: new kakao.maps.LatLng(35.4742, 129.2417), level: 9}, // 울산
    5: {center: new kakao.maps.LatLng(35.181832, 126.879582), level: 8}, // 광주
    39: {center: new kakao.maps.LatLng(33.3617, 126.5292), level: 9}, // 제주
    31: {center: new kakao.maps.LatLng(37.5582, 127.3514), level: 10}, // 경기
    32: {center: new kakao.maps.LatLng(37.4651, 128.322), level: 11}, // 강원
    33: {center: new kakao.maps.LatLng(36.6708, 127.9622), level: 10}, // 충북
    34: {center: new kakao.maps.LatLng(36.3521, 126.8086), level: 10}, // 충남
    35: {center: new kakao.maps.LatLng(36.2042, 128.9045), level: 11}, // 경북
    36: {center: new kakao.maps.LatLng(35.2598, 128.6642), level: 11}, // 경남
    37: {center: new kakao.maps.LatLng(35.6704, 127.2733), level: 11}, // 전북
    38: {center: new kakao.maps.LatLng(34.8679, 126.9910), level: 11}, // 전남
};

function getDistance(lat1, lng1, lat2, lng2) {
    return kakao.maps.geometry.getDistance(new kakao.maps.LatLng(lat1, lng1), new kakao.maps.LatLng(lat2, lng2))
}

plannerForm['city'].addEventListener('change', (event) => {

    // alert('또잉' + plannerForm['city'].value)
    const selectedValue = event.target.value;


    function createListItem(place) {
        const listItem = `
            <li class="item" data-map-Y="${place['mapY']}" data-map-X="${place['mapX']}">            
              <div class="image-container">
                <img alt="" class="image" src="${place['firstImage']}">
              </div>
              <div class="spec-container">
                <div class="name-container">
                  <span class="name">${place['title']}</span>
                  <span id="content-id" style="display: none">${place['contentId']}</span>
                  <span class="place-img" style="display: none">${place['firstImage']}</span>
                </div>
                <div class="area-button-container">
                  <button class="recommend-button"><a href="/detail?contentId=${place['contentId']}">상세보기</a></button>
                  <button class="recommend-button">일정에 추가하기</button>
                  <button class="recommend-button">삭제하기</button>
                </div>
              </div>
            </li>`;

        const dom = new DOMParser().parseFromString(listItem, 'text/html');

        const addedListItem = dom.querySelector('li');
        const addToPlanButton = addedListItem.querySelector('.recommend-button:nth-child(2)');
        const deleteToPlanButton = addedListItem.querySelector('.recommend-button:nth-child(3)');
        deleteToPlanButton.classList.add('delete-hide');

        addedListItem.addEventListener('mouseover', function () {
            const lat = parseFloat(addedListItem.dataset.mapY);
            const lng = parseFloat(addedListItem.dataset.mapX);
            const position = new kakao.maps.LatLng(lat, lng);
            const marker = new kakao.maps.Marker({
                position: position
            })
            marker.setMap(map);

            const prevMarker = marker;
            const prevCenter = map.getCenter();

            map.panTo(new kakao.maps.LatLng(lat, lng));
            addedListItem.addEventListener('mouseout', function () {
                if (prevMarker) {
                    prevMarker.setMap(null);
                }
                if (prevCenter) {
                    map.panTo(prevCenter);
                }
            })

        });
        addToPlanButton.addEventListener('click', function () {
            addedListItems.push(addedListItem);
            const index = addedListItems.indexOf(addedListItem);
            if (index !== -1) {
                createPolylineForItems(addedListItems.slice(0, index + 1))
            }
            const lat = parseFloat(addedListItem.dataset.mapY);
            const lng = parseFloat(addedListItem.dataset.mapX);
            addedListItem.marker = createMarker(lat, lng);
            markers.push(addedListItem.marker);
            addedListItem.marker.setMap(map);
            // addedListItem.marker = markerChange(addedListItem);
            moveToPlanForm(addedListItem);
        });

        return addedListItem;
    }

    function createMarker(lat, lng) {
        const position = new kakao.maps.LatLng(lat, lng);
        return new kakao.maps.Marker({
            position: position
        })
    }

    function createPolylineForItems(items) {
        clearMapElements();
        distanceOverlays = [];
        // polyline을 그릴 좌표 배열을 생성합니다.
        const coordinates = items.map((item) => {
            return new kakao.maps.LatLng(
                parseFloat(item.dataset.mapY),
                parseFloat(item.dataset.mapX)
            );
        });

        // polyline을 생성하고 지도에 표시합니다.
        const polyline = new kakao.maps.Polyline({
            path: coordinates,
            strokeWeight: 5,
            strokeColor: 'red',
            strokeOpacity: 0.7,
            strokeStyle: 'solid',
        });
        polyline.setMap(map);
        polylines.push(polyline);
        const distance = Math.round(polyline.getLength());
        console.log(polylines);
        getTime(distance, coordinates[coordinates.length - 1]);
    }


    // function markerChange(listItem) {
    //     const lat = parseFloat(listItem.dataset.mapY);
    //     const lng = parseFloat(listItem.dataset.mapX);
    //     const markerPosition = new kakao.maps.LatLng(lat, lng);
    //     const marker = new kakao.maps.Marker({
    //         position: markerPosition,
    //     });
    //     marker.setMap(map);
    //     marker.lat = lat;
    //     marker.lng = lng;
    //     markers.push(marker);
    //
    //     kakao.maps.event.addListener(marker, 'click', function () {
    //         if (selectedMarkers.length >= 2) {
    //             for (let i = 0; i < polylines.length; i++) {
    //                 polylines[i].setMap(null);
    //             }
    //             selectedMarkers = [];
    //         }
    //
    //         selectedMarkers.push(marker);
    //
    //         if (selectedMarkers.length === 2) {
    //             updateDistances(selectedMarkers[0]);
    //         }
    //         // const infoOverlay = new kakao.maps.CustomOverlay({
    //         //     map: map,
    //         //     content: createInfoOverlay(place),
    //         //     position: markerPosition
    //         // });
    //         // infoOverlay.setMap(map);
    //     });
    //
    //
    //     kakao.maps.event.addListener(map, "idle", updateMarkersPosition);
    //     return marker;
    // }
    //
    // function createInfoOverlay(listItem) {
    //     const infoContent = '<div class="wrap">' +
    //         '<div class="info">' +
    //         '<div class="title">' + listItem['title'] + '</div>' +
    //         '</div>' +
    //         '</div>';
    //     return infoContent;
    // }
    //
    // function updateMarkersPosition() {
    //     for (let i = 0; i < markers.length; i++) {
    //         const marker = markers[i];
    //         const lat = parseFloat(marker.lat);
    //         const lng = parseFloat(marker.lng);
    //
    //         const markerPosition = new kakao.maps.LatLng(lat, lng);
    //         marker.setPosition(markerPosition);
    //
    //         updateDistances(marker);
    //     }
    // }
    //
    // function updateDistances(updatedMarker) {
    //     for (let i = 0; i < polylines.length; i++) {
    //         polylines[i].setMap(null);
    //     }
    //     polylines = [];
    //     for (let i = 0; i < distanceOverlays.length; i++) {
    //         distanceOverlays[i].setMap(null);
    //     }
    //     distanceOverlays = [];
    //
    //     for (let i = 0; i < markers.length; i++) {
    //         const targetMarker = markers[i]
    //         if (targetMarker === updatedMarker) {
    //             continue;
    //         }
    //         const linePath = [
    //             new kakao.maps.LatLng(updatedMarker.lat, updatedMarker.lng),
    //             new kakao.maps.LatLng(targetMarker.lat, targetMarker.lng)
    //         ];
    //         const polyline = new kakao.maps.Polyline({
    //             path: linePath,
    //             strokeWeight: 5,
    //             strokeColor: '#FFAE00',
    //             strokeOpacity: 0.7,
    //             strokeStyle: 'solid'
    //         });
    //         polyline.setMap(map);
    //         polylines.push(polyline);
    //
    //         const distance = Math.round(polyline.getLength());
    //         const content = getTime(distance)
    //
    //         const overlay = new kakao.maps.CustomOverlay({
    //             map: map,
    //             content: content,
    //             position: targetMarker.getPosition(),
    //             xAnchor: 0.5, // 오버레이의 가로 위치를 중앙으로 설정합니다.
    //             yAnchor: 0, // 오버레이의 세로 위치를 하단으로 설정합니다.
    //             zIndex: 3,
    //         });
    //         distanceOverlays.push(overlay);
    //     }
    // }

    function getTime(distance, position) {
        const walkTime = distance / 67 | 0;
        let walkHour = '';
        let walkMin = '';
        if (walkTime > 60) {
            walkHour = '<span class="number">' + Math.floor(walkTime / 60) + '</span>시간'
        }
        walkMin = '<span class="number">' + walkTime % 60 + '</span>분'
        let content = '<ul class="dotOverlay distanceInfo">';
        content += '    <li>';
        content += '        <span class="label">총거리</span><span class="number">' + Math.floor(distance / 1000) + '</span>km';
        content += '    </li>';
        content += '    <li>';
        content += '        <span class="label">도보</span>' + walkHour + walkMin;
        content += '    </li>';
        content += '</ul>'


        const overlay = new kakao.maps.CustomOverlay({
            content: content,
            map: map,
            position: position,
            xAnchor: 0.5,
            yAnchor: 0,
            zIndex: 3,
        });
        distanceOverlays.push(overlay);
    }

    function moveToPlanForm(listItem) {
        const planFormList = document.querySelectorAll(".plan-list form");
        if (planFormList.length === 0) {
            alert("일차 폼을 생성해주세요.");
            return;
        }
        if (selectedForm) {
            const clonedListItem = listItem.cloneNode(true);
            const clonedDeleteToPlanButton = clonedListItem.querySelector('.recommend-button:nth-child(3)');
            clonedDeleteToPlanButton.classList.add('delete-show');
            // 복제된 목록 항목의 "일정에 추가하기" 버튼을 제거하고, "삭제하기" 버튼을 활성화합니다.
            const clonedAddToPlanButton = clonedListItem.querySelector('.recommend-button:nth-child(2)');
            clonedAddToPlanButton.remove();
            clonedDeleteToPlanButton.disabled = false;

            selectedForm.insertBefore(clonedListItem, selectedForm.lastElementChild);

            clonedDeleteToPlanButton.addEventListener('click', function (e) {
                e.preventDefault();
                listItem.marker.setMap(null);
                const listItemIndex = addedListItems.indexOf(listItem);
                addedListItems.splice(listItemIndex, 1);
                createPolylineForItems(addedListItems.slice(0, listItemIndex+1));
                clonedListItem.remove(selectedMarkers);
                addToPlanButton.disabled = false;
            });
        } else {
            console.log('No selected form found');
        }
        document.querySelectorAll('.plan-list form').forEach((form) => {
            form.addEventListener('submit', (event) => {
                event.preventDefault();
            });
        });

        // 클릭된 목록 항목의 "일정에 추가하기" 버튼을 비활성화합니다.
        const addToPlanButton = listItem.querySelector('.recommend-button:nth-child(2)');
        addToPlanButton.disabled = true;


    }

    let areaCode = plannerForm['city'].value;


    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/course/place?areaCode=${areaCode}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                recommendList.querySelectorAll(`:scope > .item`).forEach(
                    list => list.remove()
                );

                const places = JSON.parse(xhr.responseText);
                console.log(`지역 여행지 배열 : ${places.length} 개`);

                const responseData = JSON.parse(xhr.responseText);
                const areaPlaces = [];

                //지도 옮기기


                let options = areaCodes[parseInt(areaCode)];
                if (options) {
                    map = new kakao.maps.Map(container, options);
                } else {
                    console.log('안되지롱');
                }


                responseData.forEach(place => {
                    const listItem = createListItem(place);
                    areaPlaces.push(listItem);

                    const position = new kakao.maps.LatLng(place['mapY'], place['mapX']);
                    // console.log("마커 생성:" + position);
                    // const marker = new kakao.maps.Marker({
                    //     position: position
                    // });
                    // marker.setMap(map);
                    // placeMakers.push(marker);
                    // console.log("마커 개수:", placeMakers.length);
                    // console.log('지역코드 : '+areaCode)


                });

                recommendList.append(...areaPlaces);
            }
        }

    };

    xhr.send();

});


//  검색기능
const searchInput = document.getElementById('searchInput');
searchInput.addEventListener('input', function () {
    const searchTerm = this.value.trim().toLowerCase();

    const allListItems = document.querySelectorAll('#recommend .map-DB-list .item');
    allListItems.forEach(listItem => {
        const titleElement = listItem.querySelector('.name');
        const title = titleElement.textContent.trim().toLowerCase();

        if (title.includes(searchTerm)) {
            listItem.style.display = 'flex';
        } else {
            listItem.style.display = 'none';
        }
    });
});

const plannerList = document.getElementById('planner-info-container')

let plannerIndex;

plannerForm.onsubmit = e => {
    e.preventDefault();

    if (plannerForm['city'].value === '0') {
        alert('도시를 선택해주세요')
        return;
    } else if (plannerForm['startDate'].value === "") {
        alert('여행 출발일자를 선택해주세요')
        return;
    } else if (plannerForm['endDate'].value === "") {
        alert('여행 마지막일자를 선택해주세요')
        return;
    } else if (plannerForm['planner-title'].value === "") {
        alert('플래너 제목을 입력해주세요')
        return;
    }

    function createPlannerIndex(planner) {
        const listPlanner = `
                <li class="item">
                    <span style="display: none">${planner['index']}</span>
                    <span style="display: none"> '${planner['plannerTitle']}' <br>상세 일정을 완료해주세요.</span>
                </li>`;
        const dom = new DOMParser().parseFromString(listPlanner, 'text/html');
        const addedListPlanner = dom.querySelector('li');

        plannerIndex = planner['index'];
        return addedListPlanner;
    }


    // 일정 생성 부분에 선택한 여행지 정보 동적으로 추가
    const addressContainer = document.querySelector('#plan .address-container');
    addressContainer.innerHTML = `
        <span class="text gu" rel="addressGu">${selectedCity}</span>
    `;
    addressContainer.nextElementSibling.innerHTML = `
        <span class="text gu" rel="addressGu">${selectedStartDate} ~ ${selectedEndDate}</span>
    `;
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('title', plannerForm['planner-title'].value);


    formData.append('start_Date', plannerForm['startDate'].value);
    formData.append('end_Date', plannerForm['endDate'].value);
    formData.append('areaCode', plannerForm['city'].value);
    xhr.open('POST', '/planner');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                //get요청 추가 title로 select 하기
                let title = plannerForm['planner-title'].value;

                const xhr = new XMLHttpRequest();
                xhr.open('GET', `/planner?title=${title}`);
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status >= 200 && xhr.status < 300) {
                            // alert('planner 타이틀 : ' + title)
                            plannerList.querySelectorAll(`:scope >.item`).forEach(
                                list => list.remove()
                            )
                            const places = JSON.parse(xhr.responseText);
                            console.log(places['title'])

                            const responseData = JSON.parse(xhr.responseText);
                            let getPlanner = [];

                            // responseData가 객체인 경우, 플래너를 생성하여 추가
                            const listItem = createPlannerIndex(responseData);
                            getPlanner.push(listItem);

                            plannerList.append(...getPlanner);

                        } else {

                        }
                    }
                };
                xhr.send();

                alert(`나의 일정에 '${plannerForm['planner-title'].value}'이(가) 생성되었습니다.
상세일정도 완료해주시길 바랍니다.`)
            } else {
                alert('로그인 후 이용해주세요');
            }
        }
    };
    xhr.send(formData);

    coverElement.hide();
    plannerForm.hide();
}

// plannerClose.onclick = function () {
//     coverElement.hide();
//     plannerForm.hide();
// }


// 선택한 여행지 정보를 저장할 변수
let selectedCity = "";
let selectedStartDate = "";
let selectedEndDate = "";

// 지역 선택 시 실행되는 함수
document.getElementById('city').addEventListener('change', function () {
    selectedCity = this.options[this.selectedIndex].text;
});

// 시작 날짜 선택 시 실행되는 함수
document.getElementById('startDate').addEventListener('change', function () {
    selectedStartDate = this.value;
});

// 종료 날짜 선택 시 실행되는 함수
document.getElementById('endDate').addEventListener('change', function () {
    selectedEndDate = this.value;
});

// 일정 생성 버튼 클릭 이벤트 추가
// document.querySelector('#plannerForm .button').addEventListener('click', function () {
//     document.getElementById('plannerForm').dispatchEvent(new Event('submit'));
// });
// 시작 날짜와 종료 날짜의 입력 요소
const startDateInput = document.querySelector('.start-date');
const endDateInput = document.querySelector('.end-date');

// 날짜 계산 함수
function calculateDuration() {
    const startDate = new Date(startDateInput.value);
    const endDate = new Date(endDateInput.value);

    // 날짜 차이 계산
    const timeDiff = Math.abs(endDate.getTime() - startDate.getTime());
    const durationDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); // 일 수 계산

    // 박과 일로 변환
    const nights = Math.floor(durationDays); // 박
    const days = durationDays + 1; // 일

    // 결과 표시
    const durationContainer = document.querySelector('#plan .address-container:nth-child(3)');
    durationContainer.nextElementSibling.innerHTML = `
        <div class="text gu" rel="addressGu">${nights}박 ${days}일</div>
    `;
    return days;
}


// 날짜 입력 요소의 변경 이벤트에 계산 함수 연결
startDateInput.addEventListener('change', calculateDuration);
endDateInput.addEventListener('change', calculateDuration);


// x 일차 버튼 만들기
// 날짜 입력 요소의 변경 이벤트에 계산 함수 연결
startDateInput.addEventListener('change', handleDateChange);
endDateInput.addEventListener('change', handleDateChange);

function isDaySaved(day) {
    const dayForm = document.getElementById(`form-day${day}`);
    if (!dayForm) {
        return false;
    }
    const saveButton = dayForm.querySelector('.submit-dayForm-button');
    return saveButton.classList.contains('saved');
}

function handleDayButtonClick(day) {
    // 현재 클릭한 day 이전의 일정이 저장되어 있는지 확인
    let hasUnsavedDay = false;
    for (let i = 1; i < day; i++) {
        if (!isDaySaved(i)) {
            hasUnsavedDay = true;
            showPlanForm(i);
            break;
        }
    }
    if (hasUnsavedDay) {
        alert(`먼저 day${day-1}의 일정을 저장하세요.`);
    } else {
        showPlanForm(day);
    }
}

function handleDateChange() {
    const calculatedDays = calculateDuration();
    createDayButtons(calculatedDays);
}

// x 일차 버튼 만들기
function createDayButtons(days) {
    const dayBtnContainer = document.querySelector('.plan-day-btn');
    dayBtnContainer.innerHTML = '';

    for (let i = 1; i <= days; i++) {
        const button = document.createElement('button');
        button.textContent = `day${i}`;
        button.setAttribute('data-day', i);

        dayBtnContainer.appendChild(button);

        button.addEventListener('click', function () {
            showPlanForm(i);
        });

        // 선택된 폼과 버튼의 일치 여부를 확인하여 특정 클래스를 추가합니다.
        if (selectedForm && selectedForm.id === `form-day${i}`) {
            button.classList.add('selected-day-button');
        }
    }
}

let selectedForm = null;


function clearMapElements() {
    // polyline 제거
    for (let i = 0; i < polylines.length; i++) {
        polylines[i].setMap(null);
    }
    polylines = [];

    // 오버레이 제거
    for (let i = 0; i < distanceOverlays.length; i++) {
        distanceOverlays[i].setMap(null);
    }
    distanceOverlays = [];

}

function showPlanForm(day) {
    const planForms = document.querySelectorAll('.plan-list form');


    // 숨겨진 활성 폼이 있는지 확인
    planForms.forEach((form) => {
        form.style.display = "none";
        const buttons = form.querySelectorAll('.recommend-button:nth-child(2)');
        buttons.forEach((button) => {
            button.classList.remove('selected-day-button');
        });
    });

    let form = document.getElementById(`form-day${day}`);

    if (!form) {
        form = createPlanForm(day);
        document.querySelector('.plan-list').appendChild(form);
    }

    form.style.display = "block";
    selectedForm = form;

    const buttons = form.querySelectorAll('.plan-day-btn');
    buttons.forEach((button) => {
        button.classList.add('selected-day-button');
    });

    document.querySelectorAll('.plan-day-btn button').forEach((button) => {
        // button.addEventListener('click', function () {
        //     const day = button.getAttribute('data-day');
        //     handleDayButtonClick(day);
        // });
        button.onclick = function () {
            const day = button.getAttribute('data-day');
            handleDayButtonClick(day);
        }
    });

    form.onsubmit = async (e) => {
        e.preventDefault();
        // alert('각 서브밋 구분완료' + day);
        const h3Elements = form.getElementsByTagName('h3');
        for (let i = 0; i < h3Elements.length; i++) {
            if (h3Elements[i].classList.contains('h3-day')) {
                const h3Day = h3Elements[i].textContent;
                // alert('날짜 : ' + h3Day );
                let title;
                let Img;
                // name-container 클래스의 타이틀 확인

                const nameContainers = form.querySelectorAll('.name-container');
                // const placeImg = form.querySelectorAll('.place-img');
                // const placeImgList = form.querySelectorAll('.place-img');

                // <span class="place-mapX" ">${pclassName'mapX']}</span>
                //   <span class=" place-mapY" >${place['mapY']}</span>
                const placeMapX = form.querySelectorAll('.place-mapX')
                const placeMapY = form.querySelectorAll('.place-mapY')

                for (let j = 0; j < nameContainers.length; j++)
                    if (nameContainers.length > 0) {
                        const placeTitle = nameContainers[j].innerText;
                        // const placeImg = placeImgList[j].innerText; // 이미지 URL
                        // const ChoiceMapX = placeMapX[j].innerText;
                        // const ChoiceMapY = placeMapY[j].innerText;

                        console.log(`'${j} 번째 타이틀:'` + placeTitle);
                        // console.log(`'${j} 번째 이미지 URL:'` + placeImg);
                        // console.log(`${j} 번째 mapX : ` + ChoiceMapX);
                        // console.log(`${j} 번째 mapY : ` + ChoiceMapY);

                        title = placeTitle

                        const xhr = new XMLHttpRequest();
                        const formData = new FormData();
                        // const dateString = new Date(h3Day).toISOString(); //string 형식을 Date형식으로 바꿈
                        const dateString = new Date(h3Day) //string 형식을 Date형식으로 바꿈
                        // const contentId = document.getElementById('content-id').textContent
                        formData.append('plannerIndex', plannerIndex)
                        // formData.append('thisDate', dateString);
                        dateString.setDate(dateString.getDate() + 1);
                        formData.append('thisDate', dateString.toISOString().slice(0, 10)); // YYYY-MM-DD 형식으로 변환
                        formData.append('title', title);
                        formData.append('day', day)
                        // formData.append('contentId', contentId)
                        // formData.append('place-img', placeImg)
                        // formData.append('mapX', ChoiceMapX)
                        // formData.append('mapY', ChoiceMapY)
                        xhr.open('POST', '/detailPlan');
                        xhr.onreadystatechange = () => {
                            if (xhr.readyState === XMLHttpRequest.DONE) {
                                if (xhr.status >= 200 && xhr.status < 300) {
                                    // alert(placeImg)
                                } else {
                                    alert('연결실패')
                                }
                            }
                        };
                        await new Promise((resolve) => {
                            xhr.send(formData);
                            xhr.onload = resolve;
                        });
                    } else {
                        alert('name-container를 찾을 수 없습니다.');
                    }
            }
        }
        alert(day + "일차 상세일정 저장완료")
        clearMapElements();
        // 마커 제거
        for (let i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }
        markers = [];
        addedListItems = [];

        const dayBtnContainer = document.querySelector(`.plan-day-btn button:nth-child(${day})`);
        dayBtnContainer.classList.add('grey-btn')

        const saveBtn = document.querySelector(`#form-day${day} input`);
        saveBtn.classList.add(`cursor-none`);
        saveBtn.classList.add(`grey-btn`);
        saveBtn.classList.add('saved');
        saveBtn.disabled = true;
        saveBtn.value = '저장 완료';


        const deleteBtns = document.querySelectorAll(`#form-day${day} .delete-show`)
        deleteBtns.forEach((deleteBtn) => {
            deleteBtn.disabled = true;
            deleteBtn.classList.add(`cursor-none`);
        });
    };
}

// 페이지 로딩 후 첫 번째 일차 폼을 보여줍니다.
window.addEventListener('DOMContentLoaded', function () {
    showPlanForm(1);
});


const calculatedDays = calculateDuration();
createDayButtons(calculatedDays);

// 페이지 로딩 후 첫 번째 일차 폼을 보여줍니다.
const firstPlanForm = document.querySelector('.plan-list form');
if (firstPlanForm) {
    firstPlanForm.classList.remove('hidden');
}

function createPlanForm(day, savedListItems = []) {
    const planListContainer = document.querySelector('.plan-list');

    // 기존 폼들을 숨김 처리합니다.
    const existingForms = planListContainer.querySelectorAll('form');
    existingForms.forEach((form) => {
        form.style.display = "none";
    });

    const startDate = new Date(startDateInput.value);
    if (startDate.toString() === 'Invalid Date') {
        return;
    }
    const currentDate = new Date(startDate);
    currentDate.setDate(startDate.getDate() + (day - 1));
    const dateString = currentDate.toLocaleDateString();

    // 선택한 일차에 대한 새로운 폼을 생성합니다.
    const form = document.createElement('form');
    form.id = `form-day${day}`;
    const h3 = document.createElement('h3');
    // h3.textContent = `${day}일차 (${dateString})`;
    h3.textContent = `${dateString}`;
    // h3.id = 'h3-day'
    h3.classList.add('h3-day')
    h3.setAttribute('name', 'detailDate');
    form.appendChild(h3);


    // Submit button을 생성하고 추가합니다.
//     const submitButton = document.createElement('button');
//     submitButton.textContent = '일정 저장';
//     submitButton.classList.add('submit-button');
//     submitButton.classList.add('submit-dayForm-button');
//
//
//
//     form.appendChild(submitButton);
//
//
//     // 저장된 리스트 항목을 새로운 폼에 추가합니다.
//     savedListItems.forEach((listItem) => {
//         form.appendChild(listItem);
//     });
//
//     form.style.display = "block"; // 새로운 폼을 보여줍니다.
//     planListContainer.appendChild(form);
//
//
//     return form;
//
// }

    //버튼에서 인풋으로 바꿈
    const submitButton = document.createElement('input');
    submitButton.type = 'submit'
    submitButton.value = '일정 저장';
    submitButton.classList.add('submit-button');
    submitButton.classList.add('submit-dayForm-button');


    form.appendChild(submitButton);

// 저장된 리스트 항목을 새로운 폼에 추가합니다.
    savedListItems.forEach((listItem) => {
        form.appendChild(listItem);
    });

    form.style.display = "block"; // 새로운 폼을 보여줍니다.
    planListContainer.appendChild(form);

    return form;
}
