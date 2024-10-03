import React, { useState, useEffect } from 'react';

function NetworkCreation() {
  const [vms, setVMs] = useState([]);
  const [newNetworkName, setNewNetworkName] = useState('');
  const [selectedOrgVM, setSelectedOrgVM] = useState(null);
  const [selectedCaVM, setSelectedCaVM] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchVMs = async () => {
      const userId = parseInt(localStorage.getItem('userId'));
      try {
        const awsResponse = await fetch(`http://192.168.20.38:8080/api/vm/aws/con/${userId}`, { method: 'GET' });
        const azureResponse = await fetch(`http://192.168.20.38:8080/api/vm/azure/con/${userId}`, { method: 'GET' });
        if (!awsResponse.ok || !azureResponse.ok) throw new Error('Failed to fetch VMs');
        const awsData = await awsResponse.json();
        const azureData = await azureResponse.json();
        const awsVMs = awsData.map(vm => ({ ...vm, csp: 'AWS' }));
        const azureVMs = azureData.map(vm => ({ ...vm, csp: 'Azure' }));
        setVMs([...awsVMs, ...azureVMs]);
      } catch (error) {
        console.error('Error fetching VMs:', error);
      }
    };
    fetchVMs();
  }, []);

  const handleCreateNetwork = async () => {
    if (!selectedOrgVM || !selectedCaVM) {
      alert('Please select both Org and CA VMs');
      return;
    }
    setLoading(true);
    try {
      const networkPayload = {
        networkName: newNetworkName,
        userId: parseInt(localStorage.getItem('userId')),
        caCSP: selectedCaVM.csp,
        caIP: selectedCaVM.ip,
        caSecretKey: selectedCaVM.privatekey,
        orgCSP: selectedOrgVM.csp,
        orgIP: selectedOrgVM.ip,
        orgSecretKey: selectedOrgVM.privatekey,
      };

      const response = await fetch('http://192.168.20.2:5000/api/network', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(networkPayload),
      });

      if (!response.ok) throw new Error('Failed to create network');
      alert('Network created successfully');
      setNewNetworkName('');
      setSelectedOrgVM(null);
      setSelectedCaVM(null);
    } catch (error) {
      console.error('Error creating network:', error);
      alert(`Network creation failed: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h4>Create New Network</h4>
      <input
        type="text"
        value={newNetworkName}
        onChange={(e) => setNewNetworkName(e.target.value)}
        placeholder="Network Name"
      />
      <select
        value={selectedOrgVM?.vmId || ''}
        onChange={(e) => setSelectedOrgVM(vms.find((vm) => vm.vmId === parseInt(e.target.value)))}
      >
        <option value="">Select Org VM</option>
        {vms.map((vm) => (
          <option key={vm.vmId} value={vm.vmId}>
            {vm.vmName} ({vm.csp}) - {vm.ip}
          </option>
        ))}
      </select>
      <select
        value={selectedCaVM?.vmId || ''}
        onChange={(e) => setSelectedCaVM(vms.find((vm) => vm.vmId === parseInt(e.target.value)))}
      >
        <option value="">Select CA VM</option>
        {vms.map((vm) => (
          <option key={vm.vmId} value={vm.vmId}>
            {vm.vmName} ({vm.csp}) - {vm.ip}
          </option>
        ))}
      </select>
      <button onClick={handleCreateNetwork} disabled={loading}>
        {loading ? 'Creating...' : 'Create Network'}
      </button>
    </div>
  );
}

export default NetworkCreation;
