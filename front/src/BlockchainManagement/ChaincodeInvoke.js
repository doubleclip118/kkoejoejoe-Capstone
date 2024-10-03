import React, { useState } from 'react';

function ChaincodeInvoke() {
  const [functionName, setFunctionName] = useState('');
  const [args, setArgs] = useState('');

  const handleInvokeChaincode = async () => {
    try {
      const response = await fetch('http://192.168.20.2:5000/api/chaincode/invoke', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ functionName, args: args.split(',') }),
      });

      if (!response.ok) throw new Error('Failed to invoke chaincode');
      alert('Chaincode invoked successfully');
      setFunctionName('');
      setArgs('');
    } catch (error) {
      console.error('Error invoking chaincode:', error);
      alert(`Chaincode invoke failed: ${error.message}`);
    }
  };

  return (
    <div>
      <h4>Invoke Chaincode</h4>
      <input
        type="text"
        value={functionName}
        onChange={(e) => setFunctionName(e.target.value)}
        placeholder="Function Name"
      />
      <input
        type="text"
        value={args}
        onChange={(e) => setArgs(e.target.value)}
        placeholder="Arguments (comma-separated)"
      />
      <button onClick={handleInvokeChaincode}>Invoke Chaincode</button>
    </div>
  );
}

export default ChaincodeInvoke;
