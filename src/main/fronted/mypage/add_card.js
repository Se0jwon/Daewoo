// // 모달을 열고 닫는 함수
// function openCardModal() {
//     const modal = document.getElementById('card-add-modal');
//     modal.style.display = 'block'; // 모달을 보이게 함
// }
//
// function closeCardModal() {
//     const modal = document.getElementById('card-add-modal');
//     modal.style.display = 'none'; // 모달을 숨김
// }
//
// // 카드를 추가하는 함수
// function addNewCard() {
//     const existingCardContainer = document.querySelector('.existing-card');
//
//     const cardInfo = {
//         last4Digits:'2232',
//         expiry: '02/27',
//         cvc:'626',
//         name_on_card:'LEE JongSU',
//         country_or_region: 'united states'
//     };
//
//     const newCardElement = createCardElement(cardInfo);
//
//     existingCardContainer.appendChild(newCardElement);
//     closeCardModal(); // 카드 추가 후 모달을 닫음
// }
//
// // 카드 HTML 요소를 생성하는 함수 (수정 사항 없음)
// function createCardElement(cardInfo){
//     const cardDiv = document.createElement('div');
//     cardDiv.classList.add('registered-card');
//
//     cardDiv.innerHTML = `
//     <div class="card-number"> **** **** **** ${cardInfo.last4Digits}</div>
//     <div class="card-details">
//         <span>Valid thru</span>
//         <span class="expiry-date">${cardInfo.expiry}</span>
//         <span class="cvc-numer">${cardInfo.cvc}</span>
//     </div>
//     `;
//
//     return cardDiv;
// }