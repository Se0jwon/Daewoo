//HTML에서 모달과 버튼 요소 가져오기
const addCardBtn = document.querySelector('.add-card');
const modalOverlay = document.querySelector('.modal-overlay');
const closeBtn = document.querySelector('.close-button');

addCardBtn.addEventListener('click', function (){
    modalOverlay.classList.add('show');
});

closeBtn.addEventListener('click', function (){
    modalOverlay.classList.remove('show');
});

modalOverlay.addEventListener('click', function(event){
    if(event.target === modalOverlay){
        modalOverlay.classList.remove('show');
    }
});
