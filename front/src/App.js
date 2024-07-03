import React, { useState } from 'react';
import './App.css';
import pusanLogo from './pusan.png';

function App() {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');

  const handleSwitch = () => {
    setIsLogin(!isLogin);
    setError('');
    setUsername('');
    setPassword('');
    setConfirmPassword('');
  };

  const handleLogin = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/login', {
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
      // 여기서 로그인 성공 후 처리 (예: 토큰 저장, 리다이렉트 등)
    } catch (error) {
      setError('Login failed: ' + error.message);
    }
  };

  const handleSignup = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/signup', {
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
      // 여기서 회원가입 성공 후 처리
    } catch (error) {
      setError('Signup failed: ' + error.message);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!username || !password || (!isLogin && !confirmPassword)) {
      setError('All fields are required');
      return;
    }
    if (!isLogin && password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    setError('');

    if (isLogin) {
      await handleLogin();
    } else {
      await handleSignup();
    }
  };

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
          <button type="submit">{isLogin ? 'Login' : 'Signup'}</button>
        </form>
        <p className="switch-text">
          {isLogin ? "Don't have an account? " : "Already have an account? "}
          <a href="#" onClick={handleSwitch}>{isLogin ? 'Signup' : 'Login'}</a>
        </p>
      </div>
    </div>
  );
}

export default App;