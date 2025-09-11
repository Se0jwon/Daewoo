document.addEventListener('DOMContentLoaded', () => {
    // UI elements
    const tabs = document.querySelectorAll('.tab');
    const tabContents = document.querySelectorAll('.tab-content');
    const changeButtons = document.querySelectorAll('.change-btn');
    const filterOptionsBtn = document.getElementById('filter-options-btn');
    const filterDropdown = document.getElementById('filter-dropdown');
    const dropdownItems = document.querySelectorAll('.dropdown-item');
    const currentFilterText = document.getElementById('current-filter-text');
    const bookingListContainer = document.getElementById('booking-list-container');
    const profilePhoto = document.getElementById('profile-photo');
    const coverPhoto = document.getElementById('cover-photo');

    // 더미 예약 데이터 (나중에 db랑 연결해서 내용 가져올 수 있게끔 할거임)
    let allBookings = [
        {
            id: 1,
            roomName: "스탠다드 더블룸",
            roomImageSrc: "https://picsum.photos/seed/room1/80/80",
            checkInDate: "2023-12-08",
            checkInDisplay: "Thur, Dec 8",
            checkOutDate: "2023-12-09",
            checkOutDisplay: "Fri, Dec 9",
            checkInTime: "12:00pm",
            checkOutTime: "11:30am",
            roomNumber: "On arrival",
            status: "upcoming",
            hotelLink: "#"
        },
        {
            id: 2,
            roomName: "디럭스 스위트",
            roomImageSrc: "https://picsum.photos/seed/room1/80/80",
            checkInDate: "2024-01-15",
            checkInDisplay: "Mon, Jan 15",
            checkOutDate: "2024-01-18",
            checkOutDisplay: "Thur, Jan 18",
            checkInTime: "03:00pm",
            checkOutTime: "11:00am",
            roomNumber: "101",
            status: "upcoming",
            hotelLink: "#"
        },
        {
            id: 3,
            roomName: "패밀리 룸",
            roomImageSrc: "https://picsum.photos/seed/room1/80/80",
            checkInDate: "2023-11-20",
            checkInDisplay: "Mon, Nov 20",
            checkOutDate: "2023-11-22",
            checkOutDisplay: "Wed, Nov 22",
            checkInTime: "02:00pm",
            checkOutTime: "12:00pm",
            roomNumber: "203",
            status: "past",
            hotelLink: "#"
        },
        {
            id: 4,
            roomName: "프리미엄 룸",
            roomImageSrc: "https://picsum.photos/seed/room1/80/80",
            checkInDate: "2024-03-01",
            checkInDisplay: "Fri, Mar 1",
            checkOutDate: "2024-03-03",
            checkOutDisplay: "Sun, Mar 3",
            checkInTime: "04:00pm",
            checkOutTime: "10:00am",
            roomNumber: "305",
            status: "upcoming",
            hotelLink: "#"
        }
    ];

    const createBookingCard = (booking) => {
        const card = document.createElement('div');
        card.className = 'booking-item';
        card.innerHTML = `
            <div class="booking-card">
                <div class="card-left">
                    <div class="hotel-logo-container">
                        <img src="${booking.roomImageSrc}" alt="Hotel Logo" class="hotel-logo">
                    </div>
                    <div class="checkin-checkout-dates">
                        <div class="date-info-group">
                            <span class="date-label">Check-In</span>
                            <span class="date-value">${booking.checkInDisplay}</span>
                        </div>
                        <div class="dash-separator"></div>
                        <div class="date-info-group">
                            <span class="date-label">Check Out</span>
                            <span class="date-value">${booking.checkOutDisplay}</span>
                        </div>
                    </div>
                </div>
                
                <div class="vertical-line"></div>
                
                <div class="card-right">
                    <div class="time-room-info">
                        <div class="info-item">
                            <div class="info-line">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <circle cx="12" cy="12" r="10"></circle>
                                    <polyline points="12 6 12 12 16 14"></polyline>
                                </svg>
                                <span class="label">체크인</span>
                                <span class="value time-value">${booking.checkInTime}</span>
                            </div>
                            <div class="info-line">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <circle cx="12" cy="12" r="10"></circle>
                                    <polyline points="12 6 12 12 16 14"></polyline>
                                </svg>
                                <span class="label">체크아웃</span>
                                <span class="value time-value">${booking.checkOutTime}</span>
                            </div>
                        </div>
                        <div class="info-item room-info-item">
                            <div class="info-line">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M21 9V6a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v3"></path>
                                    <path d="M17 19H7a2 2 0 0 1-2-2v-6h14v6a2 2 0 0 1-2 2z"></path>
                                    <path d="M12 9v5h1.5a1.5 1.5 0 0 0 0-3H12"></path>
                                </svg>
                                <span class="label">방번호</span>
                                <span class="value">${booking.roomNumber}</span>
                            </div>
                        </div>
                    </div>
                    <div class="actions-container">
                        <button class="download-btn">Download Ticket</button>
                        <a href="#" class="arrow-btn">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <polyline points="9 18 15 12 9 6"></polyline>
                            </svg>
                        </a>
                    </div>
                </div>
            </div>
        `;
        return card;
    };

    // 예약 리스트를 렌더링하는 함수
    const renderBookings = (filter = 'upcoming') => {
        let filteredBookings = [];
        if (filter === 'upcoming') {
            const today = new Date();
            filteredBookings = allBookings.filter(booking => new Date(booking.checkInDate) >= today);
        } else if (filter === 'latest') {
            filteredBookings = [...allBookings].sort((a, b) => new Date(b.checkInDate) - new Date(a.checkInDate));
        } else if (filter === 'oldest') {
            filteredBookings = [...allBookings].sort((a, b) => new Date(a.checkInDate) - new Date(b.checkInDate));
        }

        bookingListContainer.innerHTML = '';
        if (filteredBookings.length === 0) {
            bookingListContainer.innerHTML = '<p style="text-align:center; color: #888;">예약 내역이 없습니다.</p>';
        } else {
            filteredBookings.forEach(booking => {
                bookingListContainer.appendChild(createBookingCard(booking));
            });
        }
    };

    //  탭 메뉴 클릭 이벤트
    tabs.forEach(tab => {
        tab.addEventListener('click', (e) => {
            e.preventDefault();
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            tabContents.forEach(content => {
                content.style.display = 'none';
            });
            const targetId = tab.getAttribute('data-tab');
            const targetContent = document.getElementById(targetId);
            if (targetContent) {
                targetContent.style.display = 'block';
                if (targetId === 'history-tab') {
                    renderBookings('upcoming');
                }
            }
        });
    });

    // 필터 드롭다운 클릭 이벤트
    filterOptionsBtn.addEventListener('click', () => {
        filterDropdown.classList.toggle('show');
        filterOptionsBtn.classList.toggle('expanded');
    });

    // 드롭다운 외부 클릭 시 드롭다운 닫기
    window.addEventListener('click', (e) => {
        if (!filterOptionsBtn.contains(e.target) && !filterDropdown.contains(e.target)) {
            filterDropdown.classList.remove('show');
            filterOptionsBtn.classList.remove('expanded');
        }
    });

    // 드롭다운 아이템 클릭 이벤트
    dropdownItems.forEach(item => {
        item.addEventListener('click', () => {
            const filterType = item.getAttribute('data-filter');
            const filterText = item.textContent;

            currentFilterText.textContent = filterText;
            renderBookings(filterType);
            filterDropdown.classList.remove('show');
            filterOptionsBtn.classList.remove('expanded');
        });
    });

    // 'Change' 버튼 클릭 이벤트 (계정 탭)
    changeButtons.forEach(button => {
        button.addEventListener('click', () => {
            const parent = button.closest('.info-group');
            const valueSpan = parent.querySelector('.info-value');
            const isEditMode = button.textContent.includes('Save');

            if (isEditMode) {
                const input = parent.querySelector('.edit-input');
                const newValue = input.value;
                valueSpan.textContent = newValue;
                restoreUI();
            } else {
                const originalValue = valueSpan.textContent;
                const input = document.createElement('input');
                input.type = (parent.dataset.field === 'password') ? 'password' : 'text';
                input.value = (parent.dataset.field === 'password') ? '' : originalValue;
                input.className = 'edit-input';
                valueSpan.style.display = 'none';
                button.innerHTML = 'Save';
                valueSpan.parentElement.insertBefore(input, button);
                input.focus();
            }

            function restoreUI() {
                valueSpan.style.display = 'inline';
                const input = parent.querySelector('.edit-input');
                if (input) input.remove();
                button.innerHTML = '<img src="Edit.svg" alt="Edit" class="edit-icon">Change';
            }
        });
    });

    // 페이지 로드 시 기본 탭 활성화
    const initialTab = document.querySelector('.tab[data-tab="account-tab"]');
    if (initialTab) {
        initialTab.click();
    }
});