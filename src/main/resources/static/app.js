// LMS SPA Frontend
const API_BASE = 'http://localhost:8080';

// Simple store
const Store = {
  get user(){ const x = localStorage.getItem('lms_user'); return x? JSON.parse(x): null },
  set user(v){ v? localStorage.setItem('lms_user', JSON.stringify(v)): localStorage.removeItem('lms_user') }
};

// DOM helpers
const $ = s => document.querySelector(s);
const $$ = s => Array.from(document.querySelectorAll(s));
const toast = (msg, type='info') => {
  const el = $('#toast');
  el.textContent = msg; el.classList.remove('hidden');
  el.style.background = type==='error' ? '#dc2626' : type==='success' ? '#16a34a' : '#111827';
  clearTimeout(toast._t); toast._t = setTimeout(()=> el.classList.add('hidden'), 2600);
};

const showAuth = () => {
  if (Store.user){
    $('#auth-guest').classList.add('hidden');
    $('#auth-user').classList.remove('hidden');
    $('#user-info').textContent = `${Store.user.display_name || Store.user.username} (${Store.user.role})`;
  } else {
    $('#auth-guest').classList.remove('hidden');
    $('#auth-user').classList.add('hidden');
  }
};

// API wrapper
async function api(path, {method='GET', body, headers}={}){
  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers: { 'Content-Type': 'application/json', ...(headers||{}) },
    body: body ? JSON.stringify(body) : undefined
  });
  if (!res.ok){
    let text; try{ text = await res.text(); } catch{}
    throw new Error(`${res.status} ${res.statusText} ${text||''}`.trim());
  }
  const ct = res.headers.get('content-type')||'';
  return ct.includes('application/json') ? res.json() : res.text();
}

// Views
const Views = {
  home: async () => {
    const [books, cats, issued] = await Promise.all([
      api('/books'), api('/categories'), api('/issued-books')
    ]);
    return `
      <div class="grid">
        <div class="card stat"><div class="muted">Books</div><div style="font-size:28px;font-weight:700">${books.length}</div></div>
        <div class="card stat"><div class="muted">Categories</div><div style="font-size:28px;font-weight:700">${cats.length}</div></div>
        <div class="card stat"><div class="muted">Issued</div><div style="font-size:28px;font-weight:700">${issued.length}</div></div>
      </div>
    `;
  },

  books: async () => {
    const cats = await api('/categories');
    const books = await api('/books');
    const catOptions = cats.map(c=>`<option value="${c.id}">${c.name}</option>`).join('');
    const list = books.map(b=>`
      <div class="card">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px">
          <div style="font-weight:600">${b.title}</div>
          <div class="actions">
            <button class="btn" data-edit-book="${b.id}">Edit</button>
            <button class="btn btn-danger" data-del-book="${b.id}">Delete</button>
          </div>
        </div>
        <div class="muted">Tag: ${b.tag}</div>
        <div class="muted">Authors: ${b.authors}</div>
        <div class="muted">Category: ${b.category? b.category.name : '—'}</div>
        <div class="muted">Status: ${b.status===1? 'Available':'Not Available'}</div>
      </div>
    `).join('');
    return `
      <div class="section-header">
        <h2>Books</h2>
        <button class="btn btn-primary" id="btn-open-add-book">Add Book</button>
      </div>
      <div class="grid">${list||'<div class="muted">No books yet</div>'}</div>
      <div id="panel-add-book" class="modal hidden">
        <div class="modal-card">
          <div class="modal-header"><h3>Add Book</h3><button class="modal-close" data-close>×</button></div>
          <form id="form-add-book" class="form">
            <div class="row">
              <label style="flex:1"><span>Title</span><input name="title" required></label>
              <label style="flex:1"><span>Tag</span><input name="tag" required></label>
            </div>
            <label><span>Authors</span><input name="authors" required></label>
            <div class="row">
              <label style="flex:1"><span>Publisher</span><input name="publisher"></label>
              <label style="flex:1"><span>ISBN</span><input name="isbn"></label>
            </div>
            <div class="row">
              <label style="flex:1"><span>Status</span>
                <select name="status">
                  <option value="1">Available</option>
                  <option value="0">Not Available</option>
                </select>
              </label>
              <label style="flex:1"><span>Category</span>
                <select name="category" required>${catOptions}</select>
              </label>
            </div>
            <button class="btn btn-primary" type="submit">Create</button>
          </form>
        </div>
      </div>
    `;
  },

  categories: async () => {
    const cats = await api('/categories');
    const list = cats.map(c=>`
      <div class="card">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px">
          <div style="font-weight:600">${c.name}</div>
          <div class="actions">
            <button class="btn" data-edit-cat="${c.id}">Edit</button>
            <button class="btn btn-danger" data-del-cat="${c.id}">Delete</button>
          </div>
        </div>
        <div class="muted">Short: ${c.shortName}</div>
        <div class="muted">Notes: ${c.notes||'—'}</div>
      </div>
    `).join('');
    return `
      <div class="section-header">
        <h2>Categories</h2>
        <button class="btn btn-primary" id="btn-open-add-cat">Add Category</button>
      </div>
      <div class="grid">${list||'<div class="muted">No categories yet</div>'}</div>
      <div id="panel-add-cat" class="modal hidden">
        <div class="modal-card">
          <div class="modal-header"><h3>Add Category</h3><button class="modal-close" data-close>×</button></div>
          <form id="form-add-cat" class="form">
            <label><span>Name</span><input name="name" required></label>
            <div class="row">
              <label style="flex:1"><span>Short Name (max 4)</span><input name="shortName" maxlength="4" required></label>
              <label style="flex:1"><span>Notes</span><input name="notes"></label>
            </div>
            <button class="btn btn-primary" type="submit">Create</button>
          </form>
        </div>
      </div>
    `;
  },

  issued: async () => {
    const issued = await api('/issued-books');
    const books = await api('/books');
    const options = books.map(b=>`<option value="${b.id}">${b.title} — ${b.authors}</option>`).join('');
    const list = issued.map(i=>`
      <div class="card">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px">
          <div style="font-weight:600">${i.book? i.book.title : 'Unknown'}</div>
          <div class="actions">
            <button class="btn" data-edit-issued="${i.id}">Edit</button>
            <button class="btn btn-danger" data-del-issued="${i.id}">Delete</button>
          </div>
        </div>
        <div class="muted">Author: ${i.book? i.book.authors : '—'}</div>
        <div class="muted">Status: ${i.returned===1? 'Returned':'Issued'}</div>
      </div>
    `).join('');
    return `
      <div class="section-header">
        <h2>Issued Books</h2>
        <button class="btn btn-primary" id="btn-open-issue">Issue Book</button>
      </div>
      <div class="grid">${list||'<div class="muted">No records yet</div>'}</div>
      <div id="panel-issue" class="modal hidden">
        <div class="modal-card">
          <div class="modal-header"><h3>Issue Book</h3><button class="modal-close" data-close>×</button></div>
          <form id="form-issue" class="form">
            <label><span>Book</span><select name="book" required>${options}</select></label>
            <label><span>Status</span>
              <select name="returned">
                <option value="0">Issued</option>
                <option value="1">Returned</option>
              </select>
            </label>
            <button class="btn btn-primary" type="submit">Save</button>
          </form>
        </div>
      </div>
    `;
  }
};

// Router
async function render(){
  const hash = (location.hash||'#/home').replace('#','');
  const view = hash.split('?')[0];
  const root = $('#app');
  try{
    root.innerHTML = '<div class="card">Loading...</div>';
    let html;
    if (view==='/home') html = await Views.home();
    else if (view==='/books') html = await Views.books();
    else if (view==='/categories') html = await Views.categories();
    else if (view==='/issued') html = await Views.issued();
    else html = '<div class="card">Not Found</div>';
    root.innerHTML = html;
    postRender(view);
  } catch(err){
    root.innerHTML = `<div class="card">Error: ${err.message}</div>`;
  }
}

// Wire dynamic events after each render
function postRender(view){
  // open modals
  const overlay = $('#modal-overlay');
  const open = id => { overlay.classList.remove('hidden'); $(id).classList.remove('hidden'); };
  const closeAll = () => { overlay.classList.add('hidden'); $$('#app .modal').forEach(m=>m.classList.add('hidden')); $$('.modal').forEach(m=>m.id.startsWith('modal-') && m.classList.add('hidden')); };

  overlay.onclick = closeAll;
  $$('.modal [data-close]').forEach(b=> b.onclick = closeAll);

  if (view==='/books'){
    const btn = $('#btn-open-add-book'); if (btn) btn.onclick = ()=> open('#panel-add-book');
    const form = $('#form-add-book'); if (form) form.onsubmit = async e=>{
      e.preventDefault();
      const fd = new FormData(form);
      const payload = {
        title: fd.get('title'), tag: fd.get('tag'), authors: fd.get('authors'),
        publisher: fd.get('publisher')||null, isbn: fd.get('isbn')||null,
        status: Number(fd.get('status')), category: { id: Number(fd.get('category')) }
      };
      try{ await api('/books', { method:'POST', body: payload }); toast('Book added','success'); closeAll(); render(); }
      catch(err){ toast(err.message,'error'); }
    };
    // delete handlers
    $$("[data-del-book]").forEach(b=> b.onclick = async ()=>{
      if (!confirm('Delete this book?')) return;
      const id = b.getAttribute('data-del-book');
      try{ await api(`/books/${id}`, { method:'DELETE' }); toast('Deleted','success'); render(); }
      catch(err){ toast(err.message,'error'); }
    });
  }

  if (view==='/categories'){
    const btn = $('#btn-open-add-cat'); if (btn) btn.onclick = ()=> open('#panel-add-cat');
    const form = $('#form-add-cat'); if (form) form.onsubmit = async e=>{
      e.preventDefault();
      const fd = new FormData(form);
      const payload = { name: fd.get('name'), shortName: fd.get('shortName'), notes: fd.get('notes')||null };
      try{ await api('/categories', { method:'POST', body: payload }); toast('Category added','success'); closeAll(); render(); }
      catch(err){ toast(err.message,'error'); }
    };
    $$("[data-del-cat]").forEach(b=> b.onclick = async ()=>{
      if (!confirm('Delete this category?')) return;
      const id = b.getAttribute('data-del-cat');
      try{ await api(`/categories/${id}`, { method:'DELETE' }); toast('Deleted','success'); render(); }
      catch(err){ toast(err.message,'error'); }
    });
  }

  if (view==='/issued'){
    const btn = $('#btn-open-issue'); if (btn) btn.onclick = ()=> open('#panel-issue');
    const form = $('#form-issue'); if (form) form.onsubmit = async e=>{
      e.preventDefault();
      const fd = new FormData(form);
      const payload = { book: { id: Number(fd.get('book')) }, returned: Number(fd.get('returned')) };
      try{ await api('/issued-books', { method:'POST', body: payload }); toast('Saved','success'); closeAll(); render(); }
      catch(err){ toast(err.message,'error'); }
    };
    $$("[data-del-issued]").forEach(b=> b.onclick = async ()=>{
      if (!confirm('Delete this record?')) return;
      const id = b.getAttribute('data-del-issued');
      try{ await api(`/issued-books/${id}`, { method:'DELETE' }); toast('Deleted','success'); render(); }
      catch(err){ toast(err.message,'error'); }
    });
  }
}

// Auth modals
function openModal(id){ $('#modal-overlay').classList.remove('hidden'); $(id).classList.remove('hidden'); }
function closeModals(){ $('#modal-overlay').classList.add('hidden'); $$('.modal').forEach(m=> m.classList.add('hidden')); }

$('#btn-open-login').onclick = ()=> openModal('#modal-login');
$('#btn-open-signup').onclick = ()=> openModal('#modal-signup');
$$('[data-close]').forEach(b=> b.onclick = closeModals);

$('#form-login').onsubmit = async e=>{
  e.preventDefault();
  const fd = new FormData(e.target);
  try{
    const resp = await api('/auth/login', { method:'POST', body: { username: fd.get('username'), password: fd.get('password') } });
    if (resp && resp.message === 'LOGIN_SUCCESS'){
      Store.user = { username: resp.username, role: resp.role, display_name: resp.username };
      showAuth(); closeModals(); toast('Logged in','success'); render();
    } else { throw new Error('Invalid response'); }
  } catch(err){ toast('Login failed','error'); }
};

$('#form-signup').onsubmit = async e=>{
  e.preventDefault();
  const fd = new FormData(e.target);
  const body = { username: fd.get('username'), password: fd.get('password'), role: fd.get('role'), display_name: fd.get('display_name'), active: true };
  try{
    const user = await api('/auth/signup', { method:'POST', body });
    Store.user = { username: user.username, role: user.role, display_name: user.display_name || user.displayName || user.username };
    showAuth(); closeModals(); toast('Account created','success'); render();
  } catch(err){ toast('Signup failed','error'); }
};

$('#btn-logout').onclick = ()=>{ Store.user = null; showAuth(); toast('Logged out','success'); render(); };

// Init
showAuth();
window.addEventListener('hashchange', render);
render();


