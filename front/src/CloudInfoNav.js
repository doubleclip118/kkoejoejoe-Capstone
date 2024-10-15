import React from 'react';

function CloudInfoNav({ setMenu }) {
  return (
    <nav className="nav-menu">
      <button onClick={() => setMenu('create_and_connect')}>Create and Connect</button>
      <button onClick={() => setMenu('view_connection')}>View Connection</button>
      <button onClick={() => setMenu('delete_connection')}>Delete Connection</button>
      <button onClick={() => setMenu('view_information')}>View Cloud Info</button>
      <button onClick={() => setMenu('delete')}>Delete Cloud Connection</button>
      <button onClick={() => setMenu('update')}>Modify Cloud Info</button>
    </nav>
  );
}

export default CloudInfoNav;