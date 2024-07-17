import React, { useState } from 'react';
import './App.css';
import pusanLogo from './pusan.png';
import Main from './Main'; // Main 컴포넌트를 import 합니다.

function App() {
  const [isLogin, setIsLogin] = useState(true);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  // Local storage key for storing userId
  const userIdKey = 'userId';

  // Function to store userId in local storage
  const storeUserIdInLocalStorage = (userId) => {
    localStorage.setItem(userIdKey, userId);
  };

  // Function to remove userId from local storage
  const removeUserIdFromLocalStorage = () => {
    localStorage.removeItem(userIdKey);
  };

  const handleSwitch = () => {
    setIsLogin(!isLogin);
    setError('');
    setSuccessMessage('');
    setUsername('');
    setPassword('');
    setConfirmPassword('');
  };

  const handleLogin = async () => {
    try {
      const response = await fetch('http://3.34.135.215:8080/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error('Login failed');
      }

      const data = await response.json();
      console.log('Login successful:', data);
      setSuccessMessage('Login successful!');
      // Store userId in local storage
      storeUserIdInLocalStorage(data.id);
      setIsLoggedIn(true);
    } catch (error) {
      setError('Login failed: ' + error.message);
    }
  };

  const handleSignup = async () => {
    try {
      const response = await fetch('http://3.34.135.215:8080/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error('Signup failed');
      }

      const data = await response.json();
      console.log('Signup successful:', data);
      setSuccessMessage('Signup successful!');
      // Store userId in local storage
      storeUserIdInLocalStorage(data.id);
      setIsLoggedIn(true);
    } catch (error) {
      setError('Signup failed: ' + error.message);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMessage('');

    if (!username || !password || (!isLogin && !confirmPassword)) {
      setError('All fields are required');
      return;
    }
    if (!isLogin && password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    if (isLogin) {
      await handleLogin();
    } else {
      await handleSignup();
    }
  };

  const handleLogout = () => {
    // Remove userId from local storage on logout
    removeUserIdFromLocalStorage();
    setIsLoggedIn(false);
  };

  const handleTestLogin = () => {
    setIsLoggedIn(true);
  };

  // Check if user is already logged in using local storage
  // This can be called in useEffect on component mount to persist login state
  // if the app supports auto-login on refresh or revisit
  const checkIfLoggedIn = () => {
    const storedUserId = localStorage.getItem(userIdKey);
    if (storedUserId) {
      setIsLoggedIn(true);
    }
  };

  if (isLoggedIn) {
    return <Main onLogout={handleLogout} />;
  }

  return (
    <div className="App">
      <div className="card">
        <img src={pusanLogo} alt="Pusan Logo" className="logo" />
        <h2>{isLogin ? 'Login' : 'Signup'}</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Enter your username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder={isLogin ? "Enter your password" : "Create a password"}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          {!isLogin && (
            <input
              type="password"
              placeholder="Confirm your password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          )}
          {error && <p className="error">{error}</p>}
          {successMessage && <p className="success">{successMessage}</p>}
          <button type="submit">{isLogin ? 'Login' : 'Signup'}</button>
        </form>
        <button onClick={handleTestLogin}>Test Login</button>
        <p className="switch-text">
          {isLogin ? "Don't have an account? " : "Already have an account? "}
          <a href="#" onClick={handleSwitch}>{isLogin ? 'Signup' : 'Login'}</a>
        </p>
      </div>
    </div>
  );
}

export default App;
