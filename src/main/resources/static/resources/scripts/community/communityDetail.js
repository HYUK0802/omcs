const commentForm = document.getElementById('commentForm');
const commentContainer = document.getElementById('commentContainer');
const likeButton = document.getElementById('like');


function postComment(content, commentIndex, toFocus, callCommentAfter) {
    callCommentAfter ??= true;

    const articleIndex = commentForm['articleIndex'].value;
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('articleIndex', articleIndex);
    formData.append('content', content);
    if (commentIndex) {
        formData.append('commentIndex', commentIndex);
    }
    xhr.open('POST', '/community/communityDetail/comment');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                if (xhr.responseText === 'true') {
                    if (toFocus) {
                        toFocus.value = '';
                        toFocus.focus();
                    }
                    if (callCommentAfter === true) {
                        callComment();
                    }
                } else {
                    alert('댓글을 작성하지 못하였습니다. 다시 시도해 주세요.');
                }
            } else {
                alert('로그인 후 이용해주세요.');
            }
        }
    };
    xhr.send(formData);
}

function callComment() {
    commentContainer.innerHTML = '';
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/community/communityDetail/comment?articleIndex=${commentForm['articleIndex'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const createComment = function (comment, indent) {
                    const tr = document.createElement('tr');
                    const td = document.createElement('td');
                    const headDiv = document.createElement('div');
                    const neckDiv = document.createElement('div');
                    const bodyDiv = document.createElement('div');
                    const footDiv = document.createElement('div');
                    const dtDate = comment['createdAt'].split('T')[0];
                    const dtTime = comment['createdAt'].split('T')[1].split('+')[0];
                    footDiv.innerText = `${dtDate} ${dtTime}`;
                    console.log(comment['createdAt']);
                    if (comment['deleted'] === false) {
                        if (comment['mine'] === true) {
                            console.log('???')
                            const deleteAnchor = document.createElement('a')
                            deleteAnchor.setAttribute('href', '#');
                            // deleteAnchor.setAttribute('th:if', '')
                            deleteAnchor.innerText = '삭제';
                            deleteAnchor.onclick = function (e) {
                                e.preventDefault();
                                if (!confirm('정말로 댓글을 삭제할까요?')) {
                                    return;
                                }
                                const xhr = new XMLHttpRequest();
                                const formData = new FormData();
                                formData.append('index', comment['index']);
                                xhr.open('DELETE', '/community/communityDetail/comment');
                                xhr.onreadystatechange = () => {
                                    if (xhr.readyState === XMLHttpRequest.DONE) {
                                        if (xhr.status >= 200 && xhr.status < 300) {
                                            if (xhr.responseText === 'true') {
                                                callComment();
                                            } else {
                                                alert('알 수 없는 이유로 댓글을 삭제하지 못했습니다. 잠시후 다시 시도해 주세요.');
                                            }
                                        } else {
                                            alert('서버와 통신하지 못했습니다. 잠시후 다시 시도해주세요.2');
                                        }
                                    }
                                }
                                xhr.send(formData);
                            };

                            neckDiv.append(deleteAnchor);
                            neckDiv.style.display = 'flex';
                            neckDiv.style.flexDirection = 'row';
                            neckDiv.style.justifyContent = 'flex-end';
                            neckDiv.style.fontSize = '0.8rem';
                        }

                        tr.style.width = '100%';
                        tr.style.position = 'relative';
                        tr.style.alignItems = 'flex-start';
                        tr.style.display = 'flex';
                        tr.style.flexDirection = 'row';
                        tr.style.justifyContent = 'flex-start';
                        tr.style.marginTop = '0.5rem';


                        td.style.marginTop = '0.5rem';

                        headDiv.style.display = 'flex';
                        headDiv.style.flexDirection = 'row';
                        headDiv.style.justifyContent = 'space-between';
                        headDiv.innerText = comment['nickname'];
                        headDiv.style.fontSize = '0.9rem';
                        bodyDiv.innerText = comment['content'];
                        bodyDiv.style.fontSize = '1.05rem';
                        footDiv.style.marginTop = '0.5rem';
                        footDiv.style.fontSize = '0.8rem';
                    } else {
                        tr.style.width = '40rem';
                        tr.style.position = 'relative';
                        tr.style.display = 'flex';
                        tr.style.flexDirection = 'row';
                        tr.style.justifyContent = 'stretch';
                        tr.style.marginLeft = '8rem';
                        td.style.marginTop = '0.5rem';
                        bodyDiv.innerText = '삭제된 댓글입니다.';
                        bodyDiv.style.color = '#a0a0a0';
                        bodyDiv.style.fontStyle = 'italic';
                        footDiv.style.display = 'none';
                    }
                    td.append(headDiv, neckDiv, bodyDiv, footDiv);

                    if (comment['deleted'] === false) {
                        const replyForm = document.createElement('form');
                        const replyCommentIndexInput = document.createElement('input');
                        const replyCommentContentInput = document.createElement('input');
                        const replySubmitInput = document.createElement('input');
                        replyCommentIndexInput.setAttribute('type', 'hidden');
                        replyCommentIndexInput.setAttribute('name', 'commentIndex');
                        replyCommentIndexInput.value = comment['index'];
                        replyCommentContentInput.setAttribute('type', 'text');
                        replyCommentContentInput.setAttribute('maxlength', '100');
                        replyCommentContentInput.setAttribute('placeholder', '답글을 입력해 주세요.');
                        replyCommentContentInput.setAttribute('name', 'content');
                        replySubmitInput.setAttribute('type', 'submit');
                        replyCommentContentInput.style.padding = '5px';
                        replyCommentContentInput.style.marginBottom = '0.5rem';
                        replySubmitInput.value = '작성';
                        replySubmitInput.style.backgroundColor = '#d6BBFF'
                        replySubmitInput.style.border = 'none';
                        replySubmitInput.style.borderRadius = '5px';
                        replySubmitInput.style.color = '#ffffff';
                        replySubmitInput.style.padding = '5px';
                        replySubmitInput.style.marginLeft = '5px';
                        replySubmitInput.style.cursor = 'pointer';
                        replyForm.style.display = 'none';
                        replyForm.style.paddingLeft = '1rem';
                        replyForm.onsubmit = function (e) {
                            e.preventDefault();
                            if (replyForm['content'].value === '') {
                                alert('내용을 입력해주세요.');
                                replyForm['content'].focus();
                                return;
                            }
                            postComment(replyForm['content'].value, replyForm['commentIndex'].value, replyForm['content'])
                        }
                        replyForm.append(replyCommentIndexInput, replyCommentContentInput, replySubmitInput);

                        const replyHeadDiv = document.createElement('div');
                        const replyToggle = document.createElement('a');
                        replyToggle.setAttribute('href', '#');
                        replyToggle.innerText = '답글';
                        replyToggle.dataset.toggled = 'false';
                        replyToggle.onclick = function (e) {
                            e.preventDefault();
                            if (replyToggle.dataset.toggled === 'false') {
                                replyToggle.innerText = '취소';
                                replyToggle.dataset.toggled = 'true';
                                replyForm.style.display = 'block';
                                replyForm['commentContent'].value = '';
                                replyForm['commentContent'].focus();
                            } else {
                                replyToggle.innerText = '답글';
                                replyToggle.dataset.toggled = 'false';
                                replyForm.style.display = 'none';
                            }
                        };
                        replyHeadDiv.append(replyToggle);
                        td.append(replyHeadDiv, replyForm);
                    }
                    const hr = document.createElement('hr');
                    td.append(hr);
                    tr.style.position = 'relative';
                    tr.style.left = `${indent}px`;
                    tr.append(td);
                    return tr;
                }
                const handleSubComments = function (commentCollection, subComments, level = 0) {
                    const indentFactor = 50;
                    const indent = level * indentFactor;
                    for (const subComment of subComments) {
                        const tr = createComment(subComment, indent);
                        commentContainer.append(tr);

                        const subSubComments = commentCollection.filter(x => x['commentIndex'] === subComment['index']);
                        handleSubComments(commentCollection, subSubComments, level + 1);
                    }
                };
                const comments = JSON.parse(xhr.responseText);
                const parentComments = comments.filter(x => !x['commentIndex']);
                handleSubComments(comments, parentComments);
            } else {
                alert('서버와 통신하지 못했습니다. 잠시 후 다시 시도해 주세요.3')
            }
        }
    };
    xhr.send();
}

commentForm.onsubmit = function (e) {
    e.preventDefault();

    if (commentForm['content'].value === '') {
        alert('댓글을 입력해 주세요.');
        commentForm['content'].focus();
        return;
    }
    postComment(commentForm['content'].value, undefined, commentForm['content']);
};

document.getElementById('callComment').onclick = callComment;

likeButton.onclick = function (e) {
    e.preventDefault();
    const articleIndex = commentForm['articleIndex'].value;
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('articleIndex', articleIndex)
    xhr.open('POST', '/community/communityDetail')
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'not_login':
                        alert('로그인 후 이용해주세요.');
                        location.href = '/user/login';
                        break;
                    case 'login':
                        e.preventDefault();
                        alert('이 게시물을 좋아합니다.');
                        location.href += '';
                        break;
                    default:
                        loginForm.loginWarning.show('서버가 알수 없는 응답을 반환했습니다. 관리자에게 문의해 주새요.')
                }
            } else {
                alert('이미 좋아요를 누른 게시물입니다.');
            }
        }
    };
    xhr.send(formData);
}