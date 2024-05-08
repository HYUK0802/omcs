const recommendBtn = document.getElementById('recommendBtn')

const detailWindow = document.getElementById('detailWindow');

const detailFrame = detailWindow.querySelector(':scope  > .iframe');

// recommendBtn.onclick = function (){
//     alert('ㅎㅇ?')
//
//     coverElement.classList.add('visible');
// }
// recommendBtn.addEventListener('click', function () {
//     const placeBox = this.closest('.place-box');
//     const name = placeBox.querySelector('.name').textContent;
//     alert('여행지 제목: ' + name);
// });

const coverElement = document.getElementById('cover');

document.querySelectorAll('.recommend-button').forEach(button => {
    button.onclick = function() {
        const placeBox = event.target.closest('.place-box');
        const name = placeBox.querySelector('.name').textContent;
        // alert('여행지 제목: ' + name);

        // detailFrame.onload = function () {
        //     if (detailFrame.contentWindow.location.href.indexOf('/user/login') > -1) {
        //         location.href = '/user/login';
        //     }
        // }
        //
        // detailFrame.setAttribute('src', `detail?contentId=${button.dataset.index}`);
        // detailWindow.classList.add('visible');
        // detailWindow.querySelector(':scope > .close').onclick = function () {
        //     detailWindow.classList.remove('visible');
        //     coverElement.classList.remove('visible');
        // }
        //
        //
        // coverElement.classList.add('visible');
        location.href = `detail?contentId=${button.dataset.index}`
    }
});
//
// document.querySelector('.search-result-container').addEventListener('click', function (event) {
//     if (event.target.classList.contains('recommend-button')) {
//         const placeBox = event.target.closest('.place-box');
//         const name = placeBox.querySelector('.name').textContent;
//         alert('여행지 제목: ' + name);
//
//     const xhr = new XMLHttpRequest();
//     xhr.open('GET',)
//
//         detailFrame.onload = function () {
//             if (detailFrame.contentWindow.location.href.indexOf('/user/login') > -1) {
//                 location.href = '/user/login';
//             }
//         }
//
//
//         detailFrame.setAttribute('src', 'detail?contentId=125266');
//         detailWindow.classList.add('visible');
//         detailWindow.querySelector(':scope > .close').onclick = function () {
//             detailWindow.classList.remove('visible');
//             coverElement.classList.remove('visible');
//         }
//
//
//         coverElement.classList.add('visible');
//     detailFrame.onload = function () {
//         if (detailFrame.contentWindow.location.href.indexOf('/user/login') > -1) {
//             location.href = '/user/login';
//         }
//     }
//     detailFrame.setAttribute('src', `/detail?contentId=${areaPlace['contentId']}`);
//     detailWindow.classList.add('visible');
//     detailWindow.querySelector(':scope > .close').onclick = function () {
//         detailWindow.classList.remove('visible');
//         coverElement.classList.remove('visible');
//     }
//     coverElement.classList.add('visible');
//
//     }
// });

// recommendBtn.onclick = function () {
//     alert('ㅎㅇ?');
//     coverElement.classList.add('visible');
// };

// }
