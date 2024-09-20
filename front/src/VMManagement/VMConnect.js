import React from 'react';

function VMConnect() {
  const handleCreateVMOnServer = async () => {
    try {
      const response = await fetch('http://your-api-ip-address/create-vm', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      });

      if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

      const result = await response.json();
      alert('VM이 성공적으로 생성되었습니다.');
    } catch (error) {
      console.error('Error creating VM:', error);
    }
  };

  return (
    <div>
      <h4>VM Connect</h4>
      <button className="action-button" onClick={handleCreateVMOnServer}>서버로 VM 생성 요청</button>
    </div>
  );
}

export default VMConnect;