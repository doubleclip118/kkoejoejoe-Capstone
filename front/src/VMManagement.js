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
    subnetName: '',
    subnetIPv4_CIDR: '',
    securityGroupName: '',
    securityGroupRules: [{ fromPort: '', toPort: '', ipProtocol: '', direction: '' }],
    keypairName: '',
    imageName: '',
    vmSpec: '',
    regionName: '',
    zoneName: ''
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

      const newVMEntry = { id: vms.length + 1, ...result };
      setVMs([...vms, newVMEntry]);
      
      setShowForm(false);
      setNewVM({
        connectionName: '',
        vmName: '',
        vpcName: '',
        vpcIPv4_CIDR: '',
        subnetName: '',
        subnetIPv4_CIDR: '',
        securityGroupName: '',
        securityGroupRules: [{ fromPort: '', toPort: '', ipProtocol: '', direction: '' }],
        keypairName: '',
        imageName: '',
        vmSpec: '',
        regionName: '',
        zoneName: ''
      });
    } catch (error) {
      console.error('Error creating VM:', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewVM(prev => ({ ...prev, [name]: value }));
  };

  const handleSecurityRuleChange = (index, field, value) => {
    const newSecurityGroupRules = [...newVM.securityGroupRules];
    newSecurityGroupRules[index][field] = value;
    setNewVM(prev => ({ ...prev, securityGroupRules: newSecurityGroupRules }));
  };

  const addSecurityRule = () => {
    setNewVM(prev => ({ ...prev, securityGroupRules: [...prev.securityGroupRules, { fromPort: '', toPort: '', ipProtocol: '', direction: '' }] }));
  };

  return (
    <div className="management-content">
      <h3>Virtual Machine Management</h3>
      {vms.map(vm => (
        <div key={vm.id} className="vm-item">
          <span>{vm.name} - Status: {vm.status}</span>
          <p>Connection: {vm.connectionName}</p>
          <p>VPC: {vm.vpcName} ({vm.vpcIPv4_CIDR})</p>
          <p>Subnet: {vm.subnetName} ({vm.subnetIPv4_CIDR})</p>
          <p>Security Group: {vm.securityGroupName}</p>
          <p>Keypair: {vm.keypairName}</p>
          <p>Image: {vm.imageName}</p>
          <p>Spec: {vm.vmSpec}</p>
          <p>Region: {vm.regionName}</p>
          <p>Zone: {vm.zoneName}</p>
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
                required
              />
            </div>
            <div>
              <label htmlFor="subnetName">Subnet Name:</label>
              <input
                id="subnetName"
                name="subnetName"
                type="text"
                value={newVM.subnetName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="subnetIPv4_CIDR">Subnet IPv4 CIDR:</label>
              <input
                id="subnetIPv4_CIDR"
                name="subnetIPv4_CIDR"
                type="text"
                value={newVM.subnetIPv4_CIDR}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="securityGroupName">Security Group Name:</label>
              <input
                id="securityGroupName"
                name="securityGroupName"
                type="text"
                value={newVM.securityGroupName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label>Security Group Rules:</label>
              {newVM.securityGroupRules.map((rule, index) => (
                <div key={index}>
                  <label htmlFor={`fromPort-${index}`}>From Port:</label>
                  <input
                    id={`fromPort-${index}`}
                    name="fromPort"
                    type="text"
                    value={rule.fromPort}
                    onChange={(e) => handleSecurityRuleChange(index, 'fromPort', e.target.value)}
                    required
                  />
                  <label htmlFor={`toPort-${index}`}>To Port:</label>
                  <input
                    id={`toPort-${index}`}
                    name="toPort"
                    type="text"
                    value={rule.toPort}
                    onChange={(e) => handleSecurityRuleChange(index, 'toPort', e.target.value)}
                    required
                  />
                  <label htmlFor={`ipProtocol-${index}`}>IP Protocol:</label>
                  <input
                    id={`ipProtocol-${index}`}
                    name="ipProtocol"
                    type="text"
                    value={rule.ipProtocol}
                    onChange={(e) => handleSecurityRuleChange(index, 'ipProtocol', e.target.value)}
                    required
                  />
                  <label htmlFor={`direction-${index}`}>Direction:</label>
                  <input
                    id={`direction-${index}`}
                    name="direction"
                    type="text"
                    value={rule.direction}
                    onChange={(e) => handleSecurityRuleChange(index, 'direction', e.target.value)}
                    required
                  />
                </div>
              ))}
              <button type="button" onClick={addSecurityRule}>Add Security Rule</button>
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
                required
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
                required
              />
            </div>
            <div>
              <label htmlFor="regionName">Region Name:</label>
              <input
                id="regionName"
                name="regionName"
                type="text"
                value={newVM.regionName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="zoneName">Zone Name:</label>
              <input
                id="zoneName"
                name="zoneName"
                type="text"
                value={newVM.zoneName}
                onChange={handleInputChange}
                required
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