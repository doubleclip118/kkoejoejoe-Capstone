import React, { useState } from 'react';

function DeleteVM() {
  const [vmId, setVmId] = useState('');

  const handleDeleteVM = async () => {
    try {
      const response = await fetch(`http://your-api-ip-address/delete-vm/${vmId}`, {
        method: 'DELETE',
      });

      if (!response.ok) throw new Error('Failed to delete VM');

      const result = await response.json();
      alert('VM이 성공적으로 삭제되었습니다.');
    } catch (error) {
      console.error('Error deleting VM:', error);
    }
  };

  return (
    <div>
      <h4>VM 삭제</h4>
      <input type="text" placeholder="VM ID" value={vmId} onChange={(e) => setVmId(e.target.value)} />
      <button className="action-button" onClick={handleDeleteVM}>삭제</button>
    </div>
  );
}

export default DeleteVM;