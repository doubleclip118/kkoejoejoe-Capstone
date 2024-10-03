import React, { useState, useEffect } from 'react';
import './BlockchainManagement.css';

const BlockchainManagement = () => {
  const [networks, setNetworks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [vms, setVMs] = useState([]);
  const [newNetwork, setNewNetwork] = useState({ networkName: '', orgVMId: '', caVMId: '' });
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchNetworks();
    fetchVMs();
  }, []);

  const fetchNetworks = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://192.168.20.2:5000/api/networks');
      if (!response.ok) throw new Error('Failed to fetch networks');
      const data = await response.json();
      setNetworks(data);
    } catch (error) {
      console.error('Error fetching networks:', error);
      setMessage('Failed to load networks');
    } finally {
      setLoading(false);
    }
  };

  const fetchVMs = async () => {
    const userId = parseInt(localStorage.getItem('userId'));
    try {
      const awsResponse = await fetch(`http://192.168.20.38:8080/api/vm/aws/con/${userId}`);
      const azureResponse = await fetch(`http://192.168.20.38:8080/api/vm/azure/con/${userId}`);
      if (!awsResponse.ok || !azureResponse.ok) throw new Error('Failed to fetch VMs');
      const awsData = await awsResponse.json();
      const azureData = await azureResponse.json();
      setVMs([
        ...awsData.map(vm => ({ ...vm, csp: 'AWS' })),
        ...azureData.map(vm => ({ ...vm, csp: 'Azure' }))
      ]);
    } catch (error) {
      console.error('Error fetching VMs:', error);
      setMessage('Failed to load VMs');
    }
  };

  const handleCreateNetwork = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const orgVM = vms.find(vm => vm.vmId === parseInt(newNetwork.orgVMId));
      const caVM = vms.find(vm => vm.vmId === parseInt(newNetwork.caVMId));
      const networkPayload = {
        networkName: newNetwork.networkName,
        userId: parseInt(localStorage.getItem('userId')),
        caCSP: caVM.csp,
        caIP: caVM.ip,
        caSecretKey: caVM.privatekey,
        orgCSP: orgVM.csp,
        orgIP: orgVM.ip,
        orgSecretKey: orgVM.privatekey,
      };

      const response = await fetch('http://192.168.20.2:5000/api/network', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(networkPayload),
      });

      if (!response.ok) throw new Error('Failed to create network');
      setMessage('Network created successfully');
      setModalVisible(false);
      setNewNetwork({ networkName: '', orgVMId: '', caVMId: '' });
      fetchNetworks();
    } catch (error) {
      console.error('Error creating network:', error);
      setMessage(`Network creation failed: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteNetwork = async (networkId) => {
    try {
      const response = await fetch(`http://192.168.20.2:5000/api/network/${networkId}`, {
        method: 'DELETE',
      });
      if (!response.ok) throw new Error('Failed to delete network');
      setMessage('Network deleted successfully');
      fetchNetworks();
    } catch (error) {
      console.error('Error deleting network:', error);
      setMessage(`Failed to delete network: ${error.message}`);
    }
  };

  return (
    <div className="aws-container">
      <div className="aws-header">
        <h2>Blockchain Networks</h2>
        <div className="aws-actions">
          <button className="aws-button aws-button-primary" onClick={() => setModalVisible(true)}>Create Network</button>
          <button className="aws-button" onClick={fetchNetworks}>Refresh</button>
        </div>
      </div>
      {message && <div className="aws-message">{message}</div>}
      <div className="aws-table-container">
        <table className="aws-table">
          <thead>
            <tr>
              <th>Network Name</th>
              <th>Org VM</th>
              <th>CA VM</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {networks.map(network => (
              <tr key={network.id}>
                <td>{network.networkName}</td>
                <td>{network.orgIP}</td>
                <td>{network.caIP}</td>
                <td>{network.status}</td>
                <td>
                  <button className="aws-button aws-button-small aws-button-danger" onClick={() => handleDeleteNetwork(network.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {loading && <div className="aws-loading">Loading...</div>}
      {modalVisible && (
        <div className="aws-modal">
          <div className="aws-modal-content">
            <h3>Create New Network</h3>
            <form onSubmit={handleCreateNetwork}>
              <div className="aws-form-group">
                <label>Network Name</label>
                <input
                  type="text"
                  value={newNetwork.networkName}
                  onChange={(e) => setNewNetwork({...newNetwork, networkName: e.target.value})}
                  required
                />
              </div>
              <div className="aws-form-group">
                <label>Organization VM</label>
                <select
                  value={newNetwork.orgVMId}
                  onChange={(e) => setNewNetwork({...newNetwork, orgVMId: e.target.value})}
                  required
                >
                  <option value="">Select Organization VM</option>
                  {vms.map((vm) => (
                    <option key={vm.vmId} value={vm.vmId}>
                      {vm.vmName} ({vm.csp}) - {vm.ip}
                    </option>
                  ))}
                </select>
              </div>
              <div className="aws-form-group">
                <label>CA VM</label>
                <select
                  value={newNetwork.caVMId}
                  onChange={(e) => setNewNetwork({...newNetwork, caVMId: e.target.value})}
                  required
>
                  <option value="">Select CA VM</option>
                  {vms.map((vm) => (
                    <option key={vm.vmId} value={vm.vmId}>
                      {vm.vmName} ({vm.csp}) - {vm.ip}
                    </option>
                  ))}
                </select>
              </div>
              <div className="aws-form-actions">
                <button type="submit" className="aws-button aws-button-primary" disabled={loading}>
                  {loading ? 'Creating...' : 'Create Network'}
                </button>
                <button type="button" className="aws-button" onClick={() => setModalVisible(false)}>Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default BlockchainManagement;