// CloudInfoNav.js
import React from 'react';

function CloudInfoNav({ setMenu }) {
  return (
    <nav className="nav-menu">
      <button onClick={() => setMenu('view')}>View Cloud Info</button>
      <button onClick={() => setMenu('create')}>Create Cloud Connection</button>
      <button onClick={() => setMenu('delete')}>Delete Cloud Connection</button>
      <button onClick={() => setMenu('update')}>Modify Cloud Info</button>
    </nav>
  );
}

export default CloudInfoNav;