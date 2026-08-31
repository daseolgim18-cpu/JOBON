/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * [추가] CRUD 공통 삭제 확인과 프로젝트 기능/트러블슈팅 동적 행 추가 스크립트입니다.
 */
document.addEventListener('submit', e => { const msg=e.target.dataset.confirm; if(msg && !window.confirm(msg)) e.preventDefault(); });
document.addEventListener('click', e => { const remove=e.target.closest('.remove-row'); if(remove){ const row=remove.closest('.nested-row,.trouble-row'); const box=row?.parentElement; if(row && box && box.children.length>1) row.remove(); else if(row) row.querySelectorAll('input,textarea').forEach(x=>x.value=''); return; } const add=e.target.closest('[data-add-row]'); if(!add)return; const box=document.getElementById(add.dataset.addRow); const first=box?.firstElementChild; if(!first)return; const clone=first.cloneNode(true); clone.querySelectorAll('input,textarea').forEach(x=>x.value=''); box.appendChild(clone); });
