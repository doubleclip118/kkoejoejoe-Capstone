import React, { useState } from 'react';
import './App.css';
import pusanLogo from './pusan.png';
import Main from './Main';

function App() {
  const [isLogin, setIsLogin] = useState(true);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleSwitch = () => {
    setIsLogin(!isLogin);
    setUsername('');
    setPassword('');
    setConfirmPassword('');
    setMessage('');
  };

  const handleLogin = async () => {
    try {
      const response = await fetch('http://192.168.20.38:8080/api/login', {
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
      setMessage('Login successful!');
      localStorage.setItem('userId', data.id);
      setIsLoggedIn(true);
    } catch (error) {
      setMessage('Login failed: ' + error.message);
    }
  };

  const handleSignup = async () => {
    try {
      const response = await fetch('http://192.168.20.38:8080/api/register', {
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
      setMessage('Signup successful!');
      localStorage.setItem('userId', data.id);
      setIsLoggedIn(true);
    } catch (error) {
      setMessage('Signup failed: ' + error.message);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');

    if (!username || !password || (!isLogin && !confirmPassword)) {
      setMessage('All fields are required');
      return;
    }
    if (!isLogin && password !== confirmPassword) {
      setMessage('Passwords do not match');
      return;
    }

    if (isLogin) {
      await handleLogin();
    } else {
      await handleSignup();
    }
  };

  const handleTestLogin = () => {
    setIsLoggedIn(true);
  };

  if (isLoggedIn) {
    return <Main />;
  }

  return (
    <div className="ec2-app">
      <div className="ec2-card">
        <div className='ec2-logo-container'>
          <img src={pusanLogo} alt="Pusan Logo" className="ec2-logo" />
        </div>
        <h2>{isLogin ? 'Login' : 'Signup'}</h2>
        <form onSubmit={handleSubmit} className="ec2-form">
          <input
            type="text"
            placeholder="Enter your username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            className="ec2-input"
          />
          <input
            type="password"
            placeholder={isLogin ? "Enter your password" : "Create a password"}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="ec2-input"
          />
          {!isLogin && (
            <input
              type="password"
              placeholder="Confirm your password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
              className="ec2-input"
            />
          )}
          <button type="submit" className="ec2-button">
            {isLogin ? 'Login' : 'Signup'}
          </button>
        </form>
        {message && <p className="ec2-message">{message}</p>}
        <p className="ec2-switch-text">
          {isLogin ? "Don't have an account? " : "Already have an account? "}
          <button onClick={handleSwitch} className="ec2-link-button">
            {isLogin ? 'Signup' : 'Login'}
          </button>
        </p>
      </div>
    </div>
  );
}

export default App;