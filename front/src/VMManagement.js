import React, { useState } from 'react';

function VMManagement() {
  const [vms, setVMs] = useState([
    { id: 1, name: 'VM-1', status: 'Running' },
    { id: 2, name: 'VM-2', status: 'Stopped' },
  ]);

  const toggleVMStatus = (id) => {
    setVMs(vms.map(vm => 
      vm.id === id ? {...vm, status: vm.status === 'Running' ? 'Stopped' : 'Running'} : vm
    ));
  };

  return (
    <div className="management-content">
      <h3>Virtual Machine Management</h3>
      {vms.map(vm => (
        <div key={vm.id} className="vm-item">
          <span>{vm.name} - Status: {vm.status}</span>
          <button className="action-button" onClick={() => toggleVMStatus(vm.id)}>
            {vm.status === 'Running' ? 'Stop' : 'Start'}
          </button>
        </div>
      ))}
      <button className="action-button">Create New VM</button>
    </div>
  );
}

export default VMManagement;