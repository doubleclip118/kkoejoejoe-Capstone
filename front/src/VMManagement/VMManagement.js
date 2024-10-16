import React, { useState, useEffect } from 'react';
import './VMManagement.css';

function VMManagement() {
  const [vms, setVms] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newVM, setNewVM] = useState({
    userId: parseInt(localStorage.getItem('userId'), 10),
    csp: 'aws',
    connectionName: '',
    vmName: '',
    vpcName: '',
    vpcIPv4Cidr: '',
    subnetName: '',
    subnetIPv4Cidr: '',
    securityGroupName: '',
    securityGroupRules: [{ fromPort: '', toPort: '', ipProtocol: '', direction: '' }],
    keypairName: '',
    imageName: '',
    vmSpec: '',
    imageType: '',
    vmSpecName: '',
    rootDiskType: '',
    dataDiskNames: [],
    vmUserId: '',
    vmUserPasswd: '',
  });

  useEffect(() => {
    fetchAllVMs();
  }, []);

  const fetchAllVMs = async () => {
    setLoading(true);
    try {
      const userId = parseInt(localStorage.getItem('userId'), 10);
      const providers = ['aws', 'azure', 'openstack'];
      let allVMs = [];

      for (const provider of providers) {
        try {
          const response = await fetch(`http://192.168.20.38:8080/api/vm/${provider}/con/${userId}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
          });

          if (!response.ok) throw new Error(`Failed to fetch ${provider} VMs`);
          
          const data = await response.json();
          allVMs = [...allVMs, ...data.map(vm => ({ ...vm, csp: provider }))];
        } catch (error) {
          console.error(`Error fetching ${provider} VMs:`, error);
        }
      }

      setVms(allVMs);
    } catch (error) {
      console.error('Error fetching VMs:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewVM(prev => ({ ...prev, [name]: value }));
  };

  const handleSecurityGroupRuleChange = (index, e) => {
    const { name, value } = e.target;
    const updatedRules = [...newVM.securityGroupRules];
    updatedRules[index] = { ...updatedRules[index], [name]: value };
    setNewVM(prev => ({ ...prev, securityGroupRules: updatedRules }));
  };

  const addSecurityGroupRule = () => {
    setNewVM(prev => ({
      ...prev,
      securityGroupRules: [...prev.securityGroupRules, { fromPort: '', toPort: '', ipProtocol: '', direction: '' }]
    }));
  };

  const handleCreateVM = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await fetch(`http://192.168.20.38:8080/api/vm/${newVM.csp}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newVM),
      });

      if (!response.ok) throw new Error('Failed to save VM information');

      const result = await response.json();
      const vmId = result.vmId;

      const vmCreateResponse = await fetch(`http://192.168.20.38:8080/api/vm/${newVM.csp}/con/${vmId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });

      if (!vmCreateResponse.ok) throw new Error('VM creation failed');

      setShowCreateModal(false);
      fetchAllVMs();
    } catch (error) {
      console.error('Error creating VM:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteVM = async (vmId, csp) => {
    setLoading(true);

    try {
      const response = await fetch(`http://192.168.20.38:8080/api/vm/${csp}/con/${vmId}`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
      });

      if (!response.ok) throw new Error('Failed to delete VM');

      const result = await response.text();
      if (result === '성공') {
        fetchAllVMs();
      } else {
        throw new Error('Unexpected response: ' + result);
      }
    } catch (error) {
      console.error('Error deleting VM:', error);
    } finally {
      setLoading(false);
    }
  };

  const renderCSPSpecificFields = () => {
    switch (newVM.csp) {
      case 'aws':
        return (
          <>
            <div className="vm-management-form-group">
              <label>VM Spec:</label>
              <input type="text" name="vmSpec" value={newVM.vmSpec} onChange={handleInputChange} required />
            </div>
          </>
        );
      case 'azure':
        return (
          <>
            <div className="vm-management-form-group">
              <label>VM Spec:</label>
              <input type="text" name="vmSpec" value={newVM.vmSpec} onChange={handleInputChange} required />
            </div>
          </>
        );
      case 'openstack':
        return (
          <>
            <div className="vm-management-form-group">
              <label>Image Type:</label>
              <input type="text" name="imageType" value={newVM.imageType} onChange={handleInputChange} required />
            </div>
            <div className="vm-management-form-group">
              <label>VM Spec Name:</label>
              <input type="text" name="vmSpecName" value={newVM.vmSpecName} onChange={handleInputChange} required />
            </div>
            <div className="vm-management-form-group">
              <label>Root Disk Type:</label>
              <input type="text" name="rootDiskType" value={newVM.rootDiskType} onChange={handleInputChange} required />
            </div>
            <div className="vm-management-form-group">
              <label>VM User ID:</label>
              <input type="text" name="vmUserId" value={newVM.vmUserId} onChange={handleInputChange} required />
            </div>
            <div className="vm-management-form-group">
              <label>VM User Password:</label>
              <input type="password" name="vmUserPasswd" value={newVM.vmUserPasswd} onChange={handleInputChange} required />
            </div>
          </>
        );
      default:
        return null;
    }
  };

  return (
    <div className="vm-management-container">
      <div className="vm-management-header">
        <h2>Virtual Machine Management</h2>
        <button 
          className="vm-management-button vm-management-button-primary"
          onClick={() => setShowCreateModal(true)}
        >
          Create VM
        </button>
      </div>

      <div className="vm-management-content">
        {loading ? (
          <div className="vm-management-loading">Loading...</div>
        ) : (
          <table className="vm-management-table">
            <thead>
              <tr>
                <th>VM Name</th>
                <th>CSP</th>
                <th>IP Address</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {vms.map(vm => (
                <tr key={`${vm.csp}-${vm.vmId}`}>
                  <td>{vm.vmName}</td>
                  <td>{vm.csp.toUpperCase()}</td>
                  <td>{vm.ip}</td>
                  <td>
                    <button 
                      className="vm-management-button vm-management-button-danger"
                      onClick={() => handleDeleteVM(vm.vmId, vm.csp)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {showCreateModal && (
        <div className="vm-management-modal">
          <div className="vm-management-modal-content">
            <h3>Create New VM</h3>
            <form onSubmit={handleCreateVM}>
              <div className="vm-management-form-group">
                <label>CSP:</label>
                <select name="csp" value={newVM.csp} onChange={handleInputChange} required>
                  <option value="aws">AWS</option>
                  <option value="azure">Azure</option>
                  <option value="openstack">OpenStack</option>
                </select>
              </div>
              <div className="vm-management-form-group">
                <label>Connection Name:</label>
                <input type="text" name="connectionName" value={newVM.connectionName} onChange={handleInputChange} required />
              </div>
              <div className="vm-management-form-group">
                <label>VM Name:</label>
                <input type="text" name="vmName" value={newVM.vmName} onChange={handleInputChange} required />
              </div>
              <div className="vm-management-form-group">
                <label>VPC Name:</label>
                <input type="text" name="vpcName" value={newVM.vpcName} onChange={handleInputChange} required />
              </div>
              <div className="vm-management-form-group">
                <label>VPC IPv4 CIDR:</label>
                <input type="text" name="vpcIPv4Cidr" value={newVM.vpcIPv4Cidr} onChange={handleInputChange} required />
              </div>
              <div className="vm-management-form-group">
                <label>Subnet Name:</label>
                <input type="text" name="subnetName" value={newVM.subnetName} onChange={handleInputChange} required />
              </div>
              <div className="vm-management-form-group">
                <label>Subnet IPv4 CIDR:</label>
                <input type="text" name="subnetIPv4Cidr" value={newVM.subnetIPv4Cidr} onChange={handleInputChange} required />
              </div>
              <div className="vm-management-form-group">
                <label>Security Group Name:</label>
                <input type="text" name="securityGroupName" value={newVM.securityGroupName} onChange={handleInputChange} required />
              </div>
              <div className="vm-management-form-group">
                <label>Security Group Rules:</label>
                {newVM.securityGroupRules.map((rule, index) => (
                  <div key={index} className="vm-management-form-group">
                    <input type="text" name="fromPort" value={rule.fromPort} onChange={(e) => handleSecurityGroupRuleChange(index, e)} placeholder="From Port" required />
                    <input type="text" name="toPort" value={rule.toPort} onChange={(e) => handleSecurityGroupRuleChange(index, e)} placeholder="To Port" required />
                    <input type="text" name="ipProtocol" value={rule.ipProtocol} onChange={(e) => handleSecurityGroupRuleChange(index, e)} placeholder="IP Protocol" required />
                    <input type="text" name="direction" value={rule.direction} onChange={(e) => handleSecurityGroupRuleChange(index, e)} placeholder="Direction" required />
                  </div>
                ))}
                <button type="button" onClick={addSecurityGroupRule}>Add Rule</button>
              </div>
              <div className="vm-management-form-group">
                <label>Keypair Name:</label>
                <input type="text" name="keypairName" value={newVM.keypairName} onChange={handleInputChange} required />
              </div>
              <div className="vm-management-form-group">
                <label>Image Name:</label>
                <input type="text" name="imageName" value={newVM.imageName} onChange={handleInputChange} required />
              </div>
              {renderCSPSpecificFields()}
              <div className="vm-management-form-actions">
                <button type="submit" className="vm-management-button vm-management-button-primary" disabled={loading}>
                  {loading ? 'Creating...' : 'Create VM'}
                </button>
                <button type="button" className="vm-management-button" onClick={() => setShowCreateModal(false)}>
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default VMManagement;