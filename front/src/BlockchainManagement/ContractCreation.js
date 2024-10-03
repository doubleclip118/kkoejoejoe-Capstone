import React, { useState } from 'react';

function ContractCreation() {
  const [contractName, setContractName] = useState('');
  const [author, setAuthor] = useState('');
  const [description, setDescription] = useState('');
  const [platform, setPlatform] = useState('Hyperledger');
  const [signaturePolicy, setSignaturePolicy] = useState('');
  const [chaincodeTitle, setChaincodeTitle] = useState('');

  const handleCreateContract = async () => {
    // Prepare JSON body
    const body = {
      name: contractName,
      author: author,
      description: description,
      platform: platform,
      signature_policy: signaturePolicy,
      chaincodeTitle: chaincodeTitle,
      // Remove chaincodeFile since it doesn't fit in JSON
    };

    try {
      const response = await fetch('http://43.203.227.190:8080/fabric/dashboard/smart-contracts', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json', // Set content type to JSON
        },
        body: JSON.stringify(body), // Convert the body object to JSON
      });

      if (!response.ok) {
        throw new Error('Failed to create contract');
      }

      alert('Smart contract created successfully');
      setContractName('');
      setAuthor('');
      setDescription('');
      setSignaturePolicy('');
      setChaincodeTitle('');
    } catch (error) {
      console.error('Error creating contract:', error);
      alert(`Smart contract creation failed: ${error.message}`);
    }
  };

  return (
    <div>
      <h4>Create Smart Contract</h4>
      <input
        type="text"
        value={contractName}
        onChange={(e) => setContractName(e.target.value)}
        placeholder="Contract Name"
      />
      <input
        type="text"
        value={author}
        onChange={(e) => setAuthor(e.target.value)}
        placeholder="Author Name"
      />
      <textarea
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        placeholder="Contract Description"
      />
      <input
        type="text"
        value={signaturePolicy}
        onChange={(e) => setSignaturePolicy(e.target.value)}
        placeholder="Signature Policy"
      />
      <input
        type="text"
        value={chaincodeTitle}
        onChange={(e) => setChaincodeTitle(e.target.value)}
        placeholder="Chaincode Title"
      />
      <button onClick={handleCreateContract}>Create Contract</button>
    </div>
  );
}

export default ContractCreation;
