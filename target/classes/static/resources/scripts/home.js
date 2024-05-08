const pickForm = document.getElementById('pickForm');
const pickButton = document.getElementById('pickButton');

$(document).ready(function () {
    contents()
    imgSlide();
    areaclick()
    areaPlace(1, 1)
    // page()
})

function contents() {
    $('.area-filter a').click(function () {
        $('.area2').addClass('active')
            .siblings()
            .removeClass('active');

        let index = $(this).index();  // 클릭한 a 태그의 인덱스
        $('.area-filter a').eq(index).addClass('visited')  // 해당 인덱스에 해당하는 area에만 add
            .siblings()
            .removeClass('visited');
        return false;
    })
}
// function page(){
//     $('.page').click(function (){
//         alert('클릭');
//         $(this).addClass('visited')  // 현재 클릭한 요소에 클래스 추가
//             .siblings()            // 형제 요소들 선택
//             .removeClass('visited'); // 형제 요소들의 visited 클래스 제거
//     });
// }


let areaList = document.getElementById('placeList')

function areaclick() {
    $('.seoul').click(function () {
        areaPlace(1, 1)
    });
    $('.busan').click(function () {
        areaPlace(6, 1)
    });
    $('.jeju').click(function () {
        areaPlace(39, 1)
    })
    $('.daegu').click(function () {
        areaPlace(4, 1)
    })
    $('.incheon').click(function () {
        areaPlace(2, 1)
    })
    $('.ulsan').click(function () {
        areaPlace(7, 1)
    })
    $('.daejeon').click(function () {
        areaPlace(3, 1)
    })
    $('.gwangju').click(function () {
        areaPlace(5, 1)
    })
    $('.gyeonggi').click(function () {
        areaPlace(31, 1)
    })
    $('.gangwon').click(function () {
        areaPlace(32, 1)
    })
    $('.gyeongbuk').click(function () {
        areaPlace(35, 1)
    })
    $('.gyeongnam').click(function () {
        areaPlace(36, 1)
    })
    $('.chungbuk').click(function () {
        areaPlace(33, 1)
    })
    $('.chungnam').click(function () {
        areaPlace(34, 1)
    })
    $('.jeonbuk').click(function () {
        areaPlace(37, 1)
    })
    $('.jeonnam').click(function () {
        areaPlace(38, 1)
    })
}


const pageContainer = document.getElementById('pageContainer');

function detailSubmitForm() {
    if (detailForm.dataset.signed === 'false') {
        alert('로그인 후 이용해주세요.');
        return false;
    }
    const reviewContent = document.getElementById('reviewContents').value;
    if (reviewContent.trim() === '') {
        alert('리뷰 내용을 입력해주세요.');
        return false;
    }
    const reviewStar = document.querySelector('input[name="reviewStar"]:checked');
    if (!reviewStar) {
        alert('별점을 선택해주세요.');
        return false;
    }
}

function areaPlace(areaCode, page) {
    function createListItem(areaPlace) {
        // console.log(areaPlace['contentId']);
        const htmlText = `
                <section class="area-list" data-action="showDetail">
                    <ul class="list seoul-placeList">
                        <li class="item">
                            <div class="area-image-container">
                                <img alt="" class="image"
                                     src="${areaPlace['firstImage']}" >
                            </div>
                            <div class="name-container">
                                <span class="trip-place-title">${areaPlace['title']}</span>
                            </div>
                        </li>
                    </ul>
                </section>`;
        return new DOMParser().parseFromString(htmlText, 'text/html').querySelector('section')
    }

    function createPageItem(areaCode, page) {
        let htmlText = `<li class="page">
                <a onclick="areaPlace(${areaCode}, ${page})" class="link">${page}</a>
            </li>`;
        return new DOMParser().parseFromString(htmlText, 'text/html').querySelector('li')
    }


    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/place?areaCode=${areaCode}&page=${page}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                // alert('백 연결성공' + areaCode)

                areaList.querySelectorAll(`:scope > .area-list`).forEach(
                    list => list.remove()
                )
                const places = JSON.parse(xhr.responseText)

                const responseData = JSON.parse(xhr.responseText);
                const areaPlaces = []; // 모든 요소를 저장할 배열
                const pageData = responseData['paging'];

                console.log(responseData)
                console.log(responseData['paging'])
                console.log(responseData['paging']['maxPage'])
                responseData['places'].forEach(areaPlace => {
                    const listItem = createListItem(areaPlace);
                    // const detailWindow = document.getElementById('detailWindow');
                    // const detailFrame = detailWindow.querySelector(':scope  > .iframe');
                    listItem.onclick = function () {
                        // detailFrame.onload = function () {
                        //     if (detailFrame.contentWindow.location.href.indexOf('/user/login') > -1) {
                        //         location.href = '/user/login';
                        //     }
                        // }
                        //
                        // detailFrame.setAttribute('src', `/detail?contentId=${areaPlace['contentId']}`);
                        // detailWindow.classList.add('visible');
                        // detailWindow.querySelector(':scope > .close').onclick = function () {
                        //     detailWindow.classList.remove('visible');
                        //     coverElement.classList.remove('visible');
                        // }
                        // coverElement.classList.add('visible');
                        location.href += `detail?contentId=${areaPlace['contentId']}`
                    }
                    areaPlaces.push(listItem);
                });
                areaList.append(...areaPlaces);
                pageContainer.innerHTML = '';

                for (let i = pageData['displayStartPage']; i <= pageData['displayEndPage']; i++) {
                    pageContainer.append(createPageItem(areaCode, i));
                }
            } else {

            }
        }
    };
    xhr.send();
}


const searchForm = document.getElementById('searchForm');
const searchInputValue = document.getElementsByClassName('input-value')[0]; // 첫 번째 요소를 가져옴

searchForm.onsubmit = () => {
    const encodedValue = encodeURIComponent(searchInputValue.value);
    location.href = `/search?c=title&q=${encodedValue}`;
    return false;
}



function imgSlide() {
    $('.slide-wrap a:gt(0)').hide();
    setInterval(function () {
        $('.slide-wrap a:first-child')
            .fadeOut(1000)
            .next('a')
            .fadeIn(1000)
            .end()
            .appendTo('.slide-wrap');
    }, 3000);
}









