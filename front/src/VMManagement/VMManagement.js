import React, { useState } from 'react';
import VMConnectionForm from './VMConnectionForm';
import VMConnect from './VMConnect';
import ConnectionView from './ConnectionView';
import DeleteVM from './DeleteVM';

function VMManagement() {
  const [menu, setMenu] = useState('');

  return (
    <div className="management-content">
      <h3>Virtual Machine Management</h3>
      <button className="action-button" onClick={() => setMenu('save')}>VM Connection 생성</button>
      <button className="action-button" onClick={() => setMenu('connect')}>VM Connect</button>
      <button className="action-button" onClick={() => setMenu('view')}>Connection View</button>
      <button className="action-button" onClick={() => setMenu('delete')}>Delete VM</button>

      {menu === 'save' && <VMConnectionForm />}
      {menu === 'connect' && <VMConnect />}
      {menu === 'view' && <ConnectionView />}
      {menu === 'delete' && <DeleteVM />}
    </div>
  );
}

export default VMManagement;