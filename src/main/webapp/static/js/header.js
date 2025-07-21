// header.js
// 모든 페이지에서 <header><nav id="main-header"></nav></header> 구조로 사용
// <script src="/static/js/header.js"></script>로 import

document.addEventListener('DOMContentLoaded', function () {
  const header = document.getElementById('main-header');
  if (!header) return; // 헤더가 없으면 종료

  fetch('/whoami', { credentials: 'include' })
    .then(res => res.json())
    .then(user => {
      let html = '';
      // 로그아웃 버튼을 <a>로 변경, id 부여
      if (user && user.id) {
        html += `<a href="#" id="logout-btn">로그아웃</a>`;
      }
      html += `<a href="/history.html">대화목록</a>
               <a href="/loginHistory.html">로그인기록</a>
               <a href="/chat.html">채팅하러 가기</a>
               <a href="/hashtag.html">해시태그 순위</a>`;
      if (user && user.role === 'ADMIN') {
        html += `<a href="/adminLoginHistory.html">사용자 로그인 기록</a>
                 <a href="/adminLoginStats.html">총 접속시간 통계</a>
                 <a href="/adminDashboard.html">통계 그래프</a>`;
      }
      header.innerHTML = html;

      // 로그아웃 a태그 클릭 이벤트 바인딩 (submit 방지)
      const logoutBtn = document.getElementById('logout-btn');
      if (logoutBtn) {
        logoutBtn.onclick = function(e) {
          e.preventDefault();
          fetch('/logout', { method: 'POST', credentials: 'include' })
            .finally(() => window.location.href = '/login.html');
        };
      }
    })
    .catch(() => {
      header.innerHTML = `<a href="/login.html">로그인</a>`;
    });
});
