// 게시글 삭제 모달
function openPostModal(){
    const postDeleteModal = document.querySelector('#postDeleteModal'); // 삭제 모달
    postDeleteModal.style.display = 'flex';
}

function closePostModal(){
    const postDeleteModal = document.querySelector('#postDeleteModal');
    postDeleteModal.style.display = 'none';
}

const postDelete = document.querySelector('.postDelete');
postDelete.addEventListener('click', openPostModal);

// 댓글 수정 모달 창 열기
function openModal(){
    const commentUpdateModal = document.querySelector('#commentUpdateModal');
    commentUpdateModal.style.display = 'flex';
}
// 댓글 수정 모달 창 닫기
function closeModal() {
    const commentUpdateModal = document.querySelector('#commentUpdateModal');
    commentUpdateModal.style.display = 'none';
}

// 댓글 삭제 모달 창 열기
function openCommentModal() {
    const commentDeleteModal = document.querySelector('#commentDeleteModal');
    commentDeleteModal.style.display = 'flex';
}

// 댓글 삭제 모달 창 닫기
function closeCommentModal(){
    const commentDeleteModal = document.querySelector('#commentDeleteModal');
    commentDeleteModal.style.display = 'none';
}


document.addEventListener('DOMContentLoaded', (event) => {
    getAllComment();
});