import React, { useState } from 'react';

function VMManagement() {
  const [vms, setVMs] = useState([
    { id: 1, name: 'VM-1', status: 'Running' },
    { id: 2, name: 'VM-2', status: 'Stopped' },
  ]);
  const [showForm, setShowForm] = useState(false);
  const [newVMName, setNewVMName] = useState('');
  const [newCSP, setNewCSP] = useState('aws');

  const toggleVMStatus = (id) => {
    setVMs(vms.map(vm => 
      vm.id === id ? {...vm, status: vm.status === 'Running' ? 'Stopped' : 'Running'} : vm
    ));
  };

  const handleCreateNewVM = () => {
    // Logic to create a new VM would go here
    const newVM = { id: vms.length + 1, name: newVMName, status: 'Stopped', csp: newCSP };
    setVMs([...vms, newVM]);
    setShowForm(false);
    setNewVMName('');
    setNewCSP('aws');
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
      <button className="action-button" onClick={() => setShowForm(true)}>Create New VM</button>
      {showForm && (
        <div className="new-vm-form">
          <h4>Create New VM</h4>
          <form onSubmit={(e) => { e.preventDefault(); handleCreateNewVM(); }}>
            <div>
              <label htmlFor="vm-name">VM Name:</label>
              <input
                id="vm-name"
                type="text"
                value={newVMName}
                onChange={(e) => setNewVMName(e.target.value)}
                required
              />
            </div>
            <div>
              <label htmlFor="csp">CSP:</label>
              <select
                id="csp"
                value={newCSP}
                onChange={(e) => setNewCSP(e.target.value)}
              >
                <option value="aws">AWS</option>
                <option value="azure">Azure</option>
                <option value="openstack">OpenStack</option>
              </select>
            </div>
            <button type="submit" className="action-button">Create</button>
            <button type="button" className="action-button" onClick={() => setShowForm(false)}>Cancel</button>
          </form>
        </div>
      )}
    </div>
  );
}

export default VMManagement;
