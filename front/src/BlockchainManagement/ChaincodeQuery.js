import React, { useState } from 'react';

function ChaincodeQuery() {
  const [query, setQuery] = useState('');
  const [response, setResponse] = useState(null);

  const handleQueryChaincode = async () => {
    try {
      const result = await fetch(`http://192.168.20.2:5000/api/chaincode/query?query=${query}`, { method: 'GET' });
      if (!result.ok) throw new Error('Failed to query chaincode');
      const data = await result.json();
      setResponse(data);
    } catch (error) {
      console.error('Error querying chaincode:', error);
      alert(`Chaincode query failed: ${error.message}`);
    }
  };

  return (
    <div>
      <h4>Query Chaincode</h4>
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Query"
      />
      <button onClick={handleQueryChaincode}>Query Chaincode</button>

      {response && (
        <div>
          <h5>Query Result:</h5>
          <pre>{JSON.stringify(response, null, 2)}</pre>
        </div>
      )}
    </div>
  );
}

export default ChaincodeQuery;
