import React, { useState } from 'react';

function VMManagement() {
  const [vms, setVMs] = useState([
    { id: 1, name: 'VM-1', status: 'Running' },
    { id: 2, name: 'VM-2', status: 'Stopped' },
  ]);
  const [showForm, setShowForm] = useState(false);
  const [newVM, setNewVM] = useState({
    connectionName: '',
    vmName: '',
    vpcName: '',
    vpcIPv4_CIDR: '',
    keypairName: '',
    imageName: '',
    vmSpec: ''
  });

  const toggleVMStatus = (id) => {
    setVMs(vms.map(vm =>
       vm.id === id ? {...vm, status: vm.status === 'Running' ? 'Stopped' : 'Running'} : vm
    ));
  };

  const handleCreateNewVM = async () => {
    try {
      const response = await fetch('http://your-api-ip-address/create-vm', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newVM),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      console.log('VM created:', result);

      // 서버로부터 받은 응답을 사용하여 새 VM을 목록에 추가
      const newVMEntry = { id: vms.length + 1, ...result };
      setVMs([...vms, newVMEntry]);
      
      setShowForm(false);
      setNewVM({
        connectionName: '',
        vmName: '',
        vpcName: '',
        vpcIPv4_CIDR: '',
        keypairName: '',
        imageName: '',
        vmSpec: ''
      });
    } catch (error) {
      console.error('Error creating VM:', error);
      // 여기에 에러 처리 로직을 추가할 수 있습니다 (예: 사용자에게 에러 메시지 표시)
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewVM(prev => ({ ...prev, [name]: value }));
  };

  return (
    <div className="management-content">
      <h3>Virtual Machine Management</h3>
      {vms.map(vm => (
  <div key={vm.id} className="vm-item">
    <span>{vm.name} - Status: {vm.status}</span>
    <p>Connection: {vm.connectionName}</p>
    <p>VPC: {vm.vpcName} ({vm.vpcIPv4_CIDR})</p>
    <p>Keypair: {vm.keypairName}</p>
    <p>Image: {vm.imageName}</p>
    <p>Spec: {vm.vmSpec}</p>
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
              <label htmlFor="connectionName">Connection Name:</label>
              <input
                id="connectionName"
                name="connectionName"
                type="text"
                value={newVM.connectionName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="vmName">VM Name:</label>
              <input
                id="vmName"
                name="vmName"
                type="text"
                value={newVM.vmName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="vpcName">VPC Name:</label>
              <input
                id="vpcName"
                name="vpcName"
                type="text"
                value={newVM.vpcName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="vpcIPv4_CIDR">VPC IPv4 CIDR:</label>
              <input
                id="vpcIPv4_CIDR"
                name="vpcIPv4_CIDR"
                type="text"
                value={newVM.vpcIPv4_CIDR}
                onChange={handleInputChange}
              />
            </div>
            <div>
              <label htmlFor="keypairName">Keypair Name:</label>
              <input
                id="keypairName"
                name="keypairName"
                type="text"
                value={newVM.keypairName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="imageName">Image Name:</label>
              <input
                id="imageName"
                name="imageName"
                type="text"
                value={newVM.imageName}
                onChange={handleInputChange}
              />
            </div>
            <div>
              <label htmlFor="vmSpec">VM Spec:</label>
              <input
                id="vmSpec"
                name="vmSpec"
                type="text"
                value={newVM.vmSpec}
                onChange={handleInputChange}
              />
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