const article = document.getElementById('article');
const communityForm = document.getElementById('communityForm');
const communityClose = document.getElementById('communityClose');
const myPostsBtn = document.getElementById('myPostsBtn');
const myWriteForm = document.getElementById('myWriteForm');
const writeClose = document.getElementById('writeClose');


if (communityForm !== null) {
    communityForm.hide = () => {
        communityForm.classList.remove('visible');
    }
    communityForm.show = () => {
        communityForm.classList.add('visible');
    }
    communityClose.onclick = function () {
        coverElement.hide();
        communityForm.hide();
    }
}
document.getElementById("writeButton").addEventListener("click", function () {
    const loggedInUser = session.getAttribute("user");

    if (!loggedInUser) {
        alert("로그인 후 작성해주세요.");

        event.preventDefault();
    }
    myPostsBtn.addEventListener('click', () => {
        myWriteForm.style.display = 'block';
        coverElement.style.display = 'block';
    });
    document.getElementById("myPostsBtn").addEventListener("click", function () {
        const loggedInUser = session.getAttribute("user");

        if (!loggedInUser) {
            alert("로그인 후 작성해주세요.");

            event.preventDefault();
        }
    });


})